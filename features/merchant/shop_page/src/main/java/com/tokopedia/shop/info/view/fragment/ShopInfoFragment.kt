package com.tokopedia.shop.info.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingShopPageInfo
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.databinding.FragmentShopInfoBinding
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.activity.ShopInfoActivity.Companion.EXTRA_SHOP_INFO
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory
import com.tokopedia.shop.info.view.viewmodel.ShopInfoViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageHeaderActivity.Companion.SHOP_ID
import com.tokopedia.shop.report.activity.ReportShopWebViewActivity
import com.tokopedia.shop_widget.note.view.activity.ShopNoteDetailActivity
import com.tokopedia.shop_widget.note.view.adapter.ShopNoteAdapterTypeFactory
import com.tokopedia.shop_widget.note.view.adapter.viewholder.ShopNoteViewHolder
import com.tokopedia.shop_widget.note.view.model.ShopNoteUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopInfoFragment :
    BaseDaggerFragment(),
    BaseEmptyViewHolder.Callback,
    ShopNoteViewHolder.OnNoteClicked {

    companion object {
        private const val REQUEST_CODER_USER_LOGIN = 100
        private const val REQUEST_REPORT_USER = 110
        const val RESULT_REPORT_TOASTER = "result_report_toaster"
        const val RESULT_KEY_REPORT_USER = "result_key_report_shop"
        const val RESULT_KEY_PAYLOAD_REPORT_USER = "result_key_payload_report_shop"
        const val SOURCE_PAGE = "?source=shop_page"

        fun createInstance(
            shopId: String? = null,
            shopInfo: ShopInfoData? = null
        ): ShopInfoFragment {
            return ShopInfoFragment().apply {
                val bundle = Bundle()
                bundle.putString(SHOP_ID, shopId)
                bundle.putParcelable(EXTRA_SHOP_INFO, shopInfo)
                arguments = bundle
            }
        }

        private const val EMPTY_DESCRIPTION = "-"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var remoteConfig: RemoteConfig? = null
    private var shopViewModel: ShopInfoViewModel? = null
    private var shopPageTracking: ShopPageTrackingShopPageInfo? = null
    private var noteAdapter: BaseListAdapter<ShopNoteUiModel, ShopNoteAdapterTypeFactory>? = null
    private var shopInfo: ShopInfoData? = null
    private val isOfficial: Boolean
        get() = shopInfo?.isOfficial == 1
    private val isGold: Boolean
        get() = shopInfo?.isGold == 1
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(getShopId(), isOfficial, isGold)
    }
    private val recyclerViewNote: RecyclerView?
        get() = fragmentShopInfoBinding?.layoutPartialShopInfoNote?.recyclerViewNote
    private val shopInfoNoteLoading: View?
        get() = fragmentShopInfoBinding?.layoutPartialShopInfoNote?.loading

    private val viewReport: LinearLayoutCompat?
        get() = fragmentShopInfoBinding?.containerReport
    private val loadProgressGetMessageId: LoaderUnify?
        get() = fragmentShopInfoBinding?.loaderProgressGetMessageId
    private val labelRepost: Typography?
        get() = fragmentShopInfoBinding?.labelReport

    private var fragmentShopInfoBinding: FragmentShopInfoBinding? = null
    private val userId: String
        get() = shopViewModel?.userId().orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShopInfoBinding = FragmentShopInfoBinding.inflate(inflater, container, false)
        return fragmentShopInfoBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopInfo = arguments?.getParcelable(EXTRA_SHOP_INFO)
        shopPageTracking = ShopPageTrackingShopPageInfo(TrackingQueue(requireContext()))
        remoteConfig = FirebaseRemoteConfigImpl(context)
        initViewModel()
        initObservers()
        initView()
        
        shopViewModel?.getShopRating(getShopId().orEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentShopInfoBinding = null
    }

    override fun onDestroy() {
        shopViewModel?.let {
            removeObservers(it.shopInfo)
            removeObservers(it.shopNotesResp)
            removeObservers(it.messageIdOnChatExist)
            it.flush()
        }
        super.onDestroy()
    }

    override fun onEmptyButtonClicked() {
        shopInfo?.run {
            shopPageTracking?.clickAddNote(
                CustomDimensionShopPage
                    .create(shopId, isOfficial == 1, isGold == 1)
            )
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
        }
    }

    override fun onNoteClicked(position: Long, shopNoteUiModel: ShopNoteUiModel) {
        shopInfo?.run {
            val isMyShop = shopViewModel?.isMyShop(shopId) ?: false
            if (!isMyShop) {
                shopPageTracking?.clickReadNotes(shopId, userId)
            }
            startActivity(
                ShopNoteDetailActivity.createIntent(
                    activity,
                    shopId,
                    shopNoteUiModel.shopNoteId.toString()
                )
            )
        }
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun getScreenName() = null

    override fun initInjector() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
            .shopComponent(getComponent(ShopComponent::class.java))
            .build()
            .inject(this)
    }

    private fun initViewModel() {
        shopViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ShopInfoViewModel::class.java)
    }

    private fun initObservers() {
        observeShopNotes()
        observeShopInfo()
        observeShopBadgeReputation()
        observerMessageIdOnChatExist()
    }

    private fun observeShopBadgeReputation() {
        shopViewModel?.shopBadgeReputation?.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> showShopBadgeReputation(result.data)
                is Fail -> showToasterError(result.throwable)
            }
        }
    }

    private fun observerMessageIdOnChatExist() {
        shopViewModel?.messageIdOnChatExist?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> goToWebViewPage(it.data)
                is Fail -> handlingFailState(it.throwable)
                else -> hideProgressGetMessageId()
            }
        }
    }

    private fun showReportShop() {
        viewReport?.show()
    }

    private fun showProgressGetMessageId() {
        labelRepost?.gone()
        loadProgressGetMessageId?.show()
        viewReport?.isClickable = false
    }

    private fun handlingFailState(e: Throwable) {
        hideProgressGetMessageId()
        if (e is UserNotLoginException) {
            redirectToLoginPage()
            return
        }
    }

    private fun redirectToLoginPage(requestCode: Int = REQUEST_CODER_USER_LOGIN) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, requestCode)
    }

    private fun hideProgressGetMessageId() {
        labelRepost?.show()
        loadProgressGetMessageId?.gone()
        viewReport?.isClickable = true
    }

    private fun goToWebViewPage(messageId: String) {
        hideProgressGetMessageId()
        routeToWebViewPage(messageId)
    }

    private fun createUrl(messageId: String): String {
        return "${TkpdBaseURL.CHAT_REPORT_URL}$messageId$SOURCE_PAGE"
    }

    private fun routeToWebViewPage(messageId: String) {
        context?.let {
            val reportUrl = createUrl(messageId)
            val intent = ReportShopWebViewActivity.getStartIntent(it, reportUrl)
            startActivityForResult(intent, REQUEST_REPORT_USER)
        }
    }

    private fun showShopBadgeReputation(shopBadge: ShopBadge) {
        fragmentShopInfoBinding?.layoutPartialShopInfoDescription?.imageViewShopReputationBadge?.apply {
            show()
            loadImageFitCenter(shopBadge.badgeHD)
        }
    }

    private fun observeShopNotes() {
        shopViewModel?.shopNotesResp?.let { shopNoteResp ->
            observe(shopNoteResp) {
                when (it) {
                    is Success -> renderListNote(it.data)
                    is Fail -> {
                        hideNoteLoading()
                        showToasterError(it.throwable)
                    }
                }
            }
        }
    }

    private fun observeShopInfo() {
        shopViewModel?.shopInfo?.let { shopInfo ->
            observe(shopInfo) { result ->
                when(result) {
                    is Success -> renderShopInfo(result.data)
                    is Fail -> showError(result.throwable)
                }
            }
        }
    }

    private fun getShopBadgeReputation() {
        shopViewModel?.getShopReputationBadge(getShopId().orEmpty())
    }

    private fun setReportStoreView() {
        showReportShop()
        viewReport?.setOnClickListener {
            showProgressGetMessageId()
            getMessageIdOnChatExist()
        }
    }

    private fun getMessageIdOnChatExist() {
        shopViewModel?.getMessageIdOnChatExist(getShopId().orEmpty())
    }

    private fun initView() {
        getShopId()?.let { shopId ->
            setupShopNotesList()
            setStatisticsVisibility()

            if (shopInfo == null) {
                getShopInfo(shopId)
            } else {
                showShopInfo()
            }

            getShopNotes(shopId)
            setReportStoreView()

            registerGlobalErrorClickListener(shopId)
        }
    }

    private fun registerGlobalErrorClickListener(shopId: String) {
        fragmentShopInfoBinding?.globalError?.setActionClickListener {
            getShopInfo(shopId)
            getShopNotes(shopId)
            getShopBadgeReputation()
        }
    }

    private fun getShopInfo(shopId: String) {
        shopViewModel?.getShopInfo(shopId)
    }

    private fun showShopInfo() {
        shopInfo?.let {
            setToolbarTitle(it.name)
            displayShopDescription(it)
            displayShopLogistic(it)
        }
    }

    private fun setupShopNotesList() {
        noteAdapter = BaseListAdapter(ShopNoteAdapterTypeFactory(this))

        recyclerViewNote?.apply {
            adapter = noteAdapter
            isNestedScrollingEnabled = false
            isFocusable = false
        }
    }

    private fun getShopNotes(shopId: String) {
        showNoteLoading()
        shopViewModel?.getShopNotes(shopId)
    }

    private fun setStatisticsVisibility() {
        fragmentShopInfoBinding?.layoutPartialShopInfoStatistic?.root?.visibility = View.GONE
    }

    private fun showNoteLoading() {
        noteAdapter?.removeErrorNetwork()
        recyclerViewNote?.visibility = View.GONE
        shopInfoNoteLoading?.visibility = View.VISIBLE
    }

    private fun hideNoteLoading() {
        shopInfoNoteLoading?.visibility = View.GONE
        recyclerViewNote?.visibility = View.VISIBLE
    }

    private fun displayShopLogistic(shopInfo: ShopInfoData) {
        setupLogisticList(shopInfo)

        if (shopViewModel?.isMyShop(shopInfo.shopId) == true) {
            fragmentShopInfoBinding?.layoutPartialShopInfoDelivery?.labelViewLogisticTitle?.apply {
                content = getString(R.string.shop_info_label_manage_note)
                setOnClickListener { goToManageLogistic() }
            }
        }
    }

    private fun setupLogisticList(shopInfo: ShopInfoData) {
        val visitable = shopInfo.shipments.map { it.transformToVisitable() }
        val shopLogisticAdapter =
            ShopInfoLogisticAdapter(ShopInfoLogisticAdapterTypeFactory(), visitable)

        fragmentShopInfoBinding?.layoutPartialShopInfoDelivery?.recyclerViewLogistic?.apply {
            adapter = shopLogisticAdapter
            isNestedScrollingEnabled = false
            isFocusable = false
        }
    }

    private fun goToManageLogistic() {
        val shippingIntent = RouteManager.getIntent(
            activity,
            ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS
        ) ?: return
        startActivity(shippingIntent)
    }

    private fun displayShopDescription(shopInfo: ShopInfoData) {
        fragmentShopInfoBinding?.layoutPartialShopInfoDescription?.apply {
            // shop description info
            shopInfoDescription.apply {
                if (TextUtils.isEmpty(shopInfo.tagLine) && TextUtils.isEmpty(shopInfo.description)) {
                    hide()
                } else {
                    show()
                    text =
                        MethodChecker.fromHtmlPreserveLineBreak("${shopInfo.tagLine}<br/><br/>${shopInfo.description}")
                }
            }

            // go apotik info
            shopViewModel?.let {
                goApotikInfoContainer.shouldShowWithAction(
                    shouldShow = it.isShouldShowLicenseForDrugSeller(isGoApotik = shopInfo.isGoApotik, fsType = shopInfo.fsType)
                ) {
                    tvSiaDescription.text =
                        shopInfo.siaNumber.takeIf { it.isNotEmpty() } ?: EMPTY_DESCRIPTION
                    tvSipaDescription.text =
                        shopInfo.sipaNumber.takeIf { it.isNotEmpty() } ?: EMPTY_DESCRIPTION
                    tvApjDescription.text = shopInfo.apj.takeIf { it.isNotEmpty() } ?: EMPTY_DESCRIPTION
                }
            }

            // shop location info
            shopInfoLocation.text = shopInfo.location

            // shop establishment info
            shopInfoOpenSince?.text =
                getString(R.string.shop_info_label_open_since_v3, shopInfo.openSince)
        }
    }

    private fun renderListNote(notes: List<ShopNoteUiModel>) {
        getShopId()?.let {
            val isMyShop = shopViewModel?.isMyShop(it) ?: false

            hideNoteLoading()
            noteAdapter?.clearAllElements()

            if (notes.isEmpty()) {
                showEmptyShopNotes(isMyShop)
            } else {
                showShopNotes(notes)
            }

            if (isMyShop) {
                showManageNotesLabel()
            }
        }
    }

    private fun showManageNotesLabel() {
        fragmentShopInfoBinding?.layoutPartialShopInfoNote?.noteLabelView?.apply {
            content = getString(R.string.shop_info_label_manage_note)
            setOnClickListener { onEmptyButtonClicked() }
        }
    }

    private fun showShopNotes(notes: List<ShopNoteUiModel>) {
        noteAdapter?.addElement(notes)
    }

    private fun showEmptyShopNotes(isMyShop: Boolean) {
        noteAdapter?.addElement(
            EmptyModel().apply {
                if (isMyShop) {
                    title = getString(R.string.shop_note_empty_note_title_seller)
                    callback = this@ShopInfoFragment
                } else {
                    title = getString(R.string.shop_note_empty_note_title_buyer)
                }
            }
        )
    }

    private fun setToolbarTitle(title: String) {
        val toolbar = (activity as? AppCompatActivity)?.supportActionBar
        toolbar?.title = MethodChecker.fromHtml(title)
    }

    private fun getShopId(): String? {
        return arguments?.getString(SHOP_ID) ?: shopInfo?.shopId
    }

    fun onBackPressed() {
        shopPageTracking?.clickBackArrow(false, customDimensionShopPage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_REPORT_USER -> onReturnFromReportUser(data, resultCode)
        }
    }

    private fun onReturnFromReportUser(data: Intent?, resultCode: Int) {
        if (data == null || resultCode != Activity.RESULT_OK) return
        showToasterConfirmation(getString(R.string.label_report_success))
    }

    private fun showToasterConfirmation(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                .show()
        }
    }

    private fun renderShopInfo(shopInfo: ShopInfoData) {
        this@ShopInfoFragment.shopInfo = shopInfo
        customDimensionShopPage.updateCustomDimensionData(getShopId(), isOfficial, isGold)
        showShopInfo()
        setReportStoreView()
        if (!isOfficial) {
            getShopBadgeReputation()
        }

        fragmentShopInfoBinding?.run {
            nestedScrollView.visible()
            containerReport.visible()
            globalError.gone()
        }
    }
    private fun showError(error: Throwable) {
        fragmentShopInfoBinding?.run {
            nestedScrollView.gone()
            containerReport.gone()

            if (error is UnknownHostException || error is SocketTimeoutException) {
                globalError.setType(GlobalError.NO_CONNECTION)
            } else {
                globalError.setType(GlobalError.SERVER_ERROR)
            }

            globalError.visible()
        }
    }

    private fun showToasterError(error: Throwable) {
        if (error is UnknownHostException || error is SocketTimeoutException) return

        val errorMessage = ErrorHandler.getErrorMessage(activity ?: return, error)

        Toaster.build(
            view ?: return,
            errorMessage,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_ERROR
        ).apply {
            show()
        }
    }
}
