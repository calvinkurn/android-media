package com.tokopedia.purchase_platform.features.promo.presentation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.data.response.ResultStatus.Companion.STATUS_PHONE_NOT_VERIFIED
import com.tokopedia.purchase_platform.features.promo.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import com.tokopedia.purchase_platform.features.promo.presentation.viewmodel.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.net.UnknownHostException
import javax.inject.Inject

class PromoCheckoutFragment : BaseListFragment<Visitable<*>, PromoCheckoutAdapterTypeFactory>(),
        PromoCheckoutActionListener, ToolbarPromoCheckoutListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var itemDecorator: PromoCheckoutDecoration

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PromoCheckoutViewModel::class.java]
    }

    // Use single recycler view to prevent memory leak & OOM caused by nested recyclerview
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoCheckoutAdapter

    // Main Section
    private var toolbar: ToolbarPromoCheckout? = null
    private val appBarLayout by lazy { view?.findViewById<AppBarLayout>(R.id.app_bar_layout) }
    private val swipeRefreshLayout by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout) }
    private val layoutMainContainer by lazy { view?.findViewById<FrameLayout>(R.id.layout_main_container) }
    private val containerActionBottom by lazy { view?.findViewById<ConstraintLayout>(R.id.container_action_bottom) }
    private val layoutGlobalError by lazy { view?.findViewById<GlobalError>(R.id.layout_global_error) }

    // Sticky header section
    private val headerPromoSection by lazy { view?.findViewById<LinearLayout>(R.id.header_promo_section) }
    private val sectionImagePromoListHeader by lazy { view?.findViewById<ImageView>(R.id.section_image_promo_list_header) }
    private val sectionLabelPromoListHeaderTitle by lazy { view?.findViewById<Typography>(R.id.section_label_promo_list_header_title) }
    private val sectionLabelPromoListHeaderSubTitle by lazy { view?.findViewById<Typography>(R.id.section_label_promo_list_header_sub_title) }
    private val sectionImageChevron by lazy { view?.findViewById<ImageView>(R.id.section_image_chevron) }

    // Footer section
    private val labelTotalPromoInfo by lazy { view?.findViewById<Typography>(R.id.label_total_promo_info) }
    private val labelTotalPromoAmount by lazy { view?.findViewById<Typography>(R.id.label_total_promo_amount) }
    private val buttonApplyPromo by lazy { view?.findViewById<UnifyButton>(R.id.button_apply_promo) }
    private val buttonApplyNoPromo by lazy { view?.findViewById<UnifyButton>(R.id.button_apply_no_promo) }

    companion object {
        const val REQUEST_CODE_PHONE_VERIFICATION = 9999
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        fun createInstance(pageSource: Int,
                           promoRequest: PromoRequest,
                           validateUsePromoRequest: ValidateUsePromoRequest,
                           bboPromoCodes: ArrayList<String>): PromoCheckoutFragment {
            return PromoCheckoutFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARGS_PAGE_SOURCE, pageSource)
                    putParcelable(ARGS_PROMO_REQUEST, promoRequest)
                    putParcelable(ARGS_VALIDATE_USE_REQUEST, validateUsePromoRequest)
                    putStringArrayList(ARGS_BBO_PROMO_CODES, bboPromoCodes)
                }
            }
        }
    }

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerPromoCheckoutMarketplaceComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_promo_checkout_marketplace, container, false)
        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(itemDecorator)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
        setToolbarShadowVisibility(false)
        viewModel.initFragmentUiModel(arguments?.getInt(ARGS_PAGE_SOURCE, 0) ?: 0)

        buttonApplyPromo?.let { buttonApplyPromo ->
            buttonApplyPromo.setOnClickListener {
                setButtonLoading(buttonApplyPromo, true)
                val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST) as ValidateUsePromoRequest
                val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, R.raw.mutation_validate_use_promo_revamp), validateUsePromoRequest, bboPromoCodes
                        ?: ArrayList())
            }
        }

        buttonApplyNoPromo?.let { buttonApplyNoPromo ->
            buttonApplyNoPromo.setOnClickListener {
                setButtonLoading(buttonApplyNoPromo, true)
                val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST) as ValidateUsePromoRequest
                val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                viewModel.clearPromo(GraphqlHelper.loadRawString(it.resources, R.raw.clear_promo), validateUsePromoRequest, bboPromoCodes
                        ?: ArrayList())
                viewModel.sendAnalyticsClickBeliTanpaPromo()
            }
        }

        activity?.let {
            swipeRefreshLayout?.setColorSchemeColors(ContextCompat.getColor(it, R.color.tkpd_main_green))
        }
        swipeRefreshLayout?.setOnRefreshListener {
            reloadData()
        }

        val lastHeaderUiModel: PromoListHeaderUiModel? = null
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
                handleStickyPromoHeader(recyclerView, lastHeaderUiModel)
            }
        })

        // Observe visitable data changes
        observeFragmentUiModel()
        observePromoRecommendationUiModel()
        observePromoInputUiModel()
        observePromoListUiModel()
        observeEmptyStateUiModel()
        observeVisitableChangeUiModel()
        observeVisitableListChangeUiModel()

        // Observe network call result
        observeGetCouponRecommendationResult()
        observeApplyPromoResult()
        observeClearPromoResult()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
            reloadData()
        }
    }

    private fun setButtonLoading(button: UnifyButton, isLoading: Boolean) {
        if (isLoading) {
            button.isLoading = true
            button.isClickable = false
        } else {
            button.isLoading = false
            button.isClickable = true
        }
    }

    private fun handleStickyPromoHeader(recyclerView: RecyclerView, lastHeaderUiModel: PromoListHeaderUiModel?) {
        if (adapter.data.isNotEmpty()) {
            var tmpLastHeaderUiModel = lastHeaderUiModel
            val topItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (topItemPosition == RecyclerView.NO_POSITION) return
            val lastData = adapter.data[topItemPosition]

            val isShow: Boolean
            if (lastData is PromoListHeaderUiModel && lastData.uiState.isEnabled && !lastData.uiState.isCollapsed) {
                tmpLastHeaderUiModel = lastData
                isShow = true
            } else if (tmpLastHeaderUiModel != null && lastData is PromoListItemUiModel &&
                    lastData.uiData.parentIdentifierId == tmpLastHeaderUiModel.uiData.identifierId &&
                    lastData.uiState.isParentEnabled) {
                isShow = true
            } else if (lastData is PromoListItemUiModel && lastData.uiState.isParentEnabled) {
                if (tmpLastHeaderUiModel != null && lastData.uiData.parentIdentifierId == tmpLastHeaderUiModel.uiData.identifierId) {
                    isShow = true
                } else {
                    var foundHeader = false
                    adapter.data.forEach {
                        if (it is PromoListHeaderUiModel && it.uiData.identifierId == lastData.uiData.parentIdentifierId) {
                            tmpLastHeaderUiModel = it
                            foundHeader = true
                            return@forEach
                        }
                    }
                    isShow = foundHeader
                }
            } else {
                isShow = false
            }

            // View logic here should be same as view logic on #PromoListHeaderEnabledViewHolder
            if (tmpLastHeaderUiModel != null) {
                sectionImagePromoListHeader?.let { sectionImagePromoListHeader ->
                    if (tmpLastHeaderUiModel?.uiData?.iconUrl?.isNotBlank() == true) {
                        ImageHandler.loadImageRounded2(context, sectionImagePromoListHeader, tmpLastHeaderUiModel?.uiData?.iconUrl)
                        sectionImagePromoListHeader.show()
                    } else {
                        sectionImagePromoListHeader.gone()
                    }
                    setImageFilterNormal(sectionImagePromoListHeader)
                }

                sectionLabelPromoListHeaderTitle?.text = tmpLastHeaderUiModel?.uiData?.title
                sectionLabelPromoListHeaderSubTitle?.text = tmpLastHeaderUiModel?.uiData?.subTitle

                if (tmpLastHeaderUiModel?.uiState?.isCollapsed == false) {
                    sectionImageChevron?.rotation = 180f
                } else {
                    sectionImageChevron?.rotation = 0f
                }

                sectionLabelPromoListHeaderSubTitle?.show()
                sectionImageChevron?.show()
                headerPromoSection?.setOnClickListener {
                    if (tmpLastHeaderUiModel != null) {
                        onClickPromoListHeader(tmpLastHeaderUiModel!!)
                    }
                }
            }

            if (isShow) {
                headerPromoSection?.show()
                setToolbarShadowVisibility(false)
            } else {
                headerPromoSection?.gone()
            }
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (show) {
                appBarLayout?.elevation = HAS_ELEVATION.toFloat()
            } else {
                appBarLayout?.elevation = NO_ELEVATION.toFloat()
            }
        }
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(this, Observer {
            renderFragmentState(it)
        })
    }

    private fun addOrModify(it: Visitable<*>) {
        if (adapter.data.contains(it)) {
            adapter.modifyData(adapter.data.indexOf(it))
        } else {
            adapter.addVisitable(it)
        }
    }

    private fun observeEmptyStateUiModel() {
        viewModel.promoEmptyStateUiModel.observe(this, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoRecommendationUiModel() {
        viewModel.promoRecommendationUiModel.observe(this, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoInputUiModel() {
        viewModel.promoInputUiModel.observe(this, Observer {
            addOrModify(it)
        })
    }

    private fun observePromoListUiModel() {
        viewModel.promoListUiModel.observe(this, Observer {
            adapter.addVisitableList(it)
        })
    }

    private fun observeVisitableChangeUiModel() {
        viewModel.tmpUiModel.observe(this, Observer {
            when (it) {
                is Update -> {
                    adapter.modifyData(adapter.data.indexOf(it.data))
                }
                is Delete -> {
                    adapter.removeData(it.data)
                }
            }
        })
    }

    private fun observeVisitableListChangeUiModel() {
        viewModel.tmpListUiModel.observe(this, Observer {
            when (it) {
                is Insert -> {
                    it.data.forEach {
                        adapter.addVisitableList((adapter.data.indexOf(it.key) + 1), it.value)
                    }
                }
            }
        })
    }

    private fun observeGetCouponRecommendationResult() {
        viewModel.getCouponRecommendationResponse.observe(this, Observer {
            when {
                it.state == GetCouponRecommendationAction.ACTION_CLEAR_DATA -> {
                    clearAllData()
                }
                it.state == GetCouponRecommendationAction.ACTION_SHOW_TOAST_ERROR -> {
                    it.exception?.let {
                        showToastMessage(it)
                    }
                }
            }
        })
    }

    private fun observeApplyPromoResult() {
        viewModel.applyPromoResponse.observe(this, Observer {
            buttonApplyPromo?.let {
                setButtonLoading(it, false)
            }
            when {
                it.state == ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CART -> {
                    val intent = Intent()
                    if (it.data != null) {
                        intent.putExtra(ARGS_VALIDATE_USE_DATA_RESULT, it.data)
                    }
                    if (it.lastValidateUseRequest != null) {
                        intent.putExtra(ARGS_LAST_VALIDATE_USE_REQUEST, it.lastValidateUseRequest)
                    }
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
                it.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO -> {
                    it.exception?.let {
                        showToastMessage(it)
                    }
                    reloadData()
                }
                it.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR -> {
                    it.exception?.let {
                        showToastMessage(it)
                        viewModel.sendAnalyticsClickPakaiPromoFailed(getErrorMessage(it))
                    }
                }
            }
        })
    }

    private fun observeClearPromoResult() {
        viewModel.clearPromoResponse.observe(this, Observer {
            buttonApplyNoPromo?.let {
                setButtonLoading(it, false)
            }
            when {
                it.state == ClearPromoResponseAction.ACTION_STATE_SUCCESS -> {
                    val intent = Intent()
                    if (it.data != null) {
                        intent.putExtra(ARGS_CLEAR_PROMO_RESULT, it.data)
                    }
                    if (it.lastValidateUseRequest != null) {
                        intent.putExtra(ARGS_LAST_VALIDATE_USE_REQUEST, it.lastValidateUseRequest)
                    }
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }
                it.state == ClearPromoResponseAction.ACTION_STATE_ERROR -> it.exception?.let {
                    showToastMessage(it)
                }
            }
        })
    }

    private fun renderFragmentState(fragmentUiModel: FragmentUiModel) {
        if (fragmentUiModel.uiState.isLoading) {
            showLoading()
        } else {
            hideLoading()
            swipeRefreshLayout?.isRefreshing = false
        }

        if (!fragmentUiModel.uiState.hasFailedToLoad) {
            if (fragmentUiModel.uiState.hasAnyPromoSelected) {
                toolbar?.enableResetButton()
                toolbar?.showResetButton()
                activity?.let {
                    labelTotalPromoInfo?.show()
                    labelTotalPromoAmount?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.uiData.totalBenefit, false)
                    labelTotalPromoAmount?.show()
                    buttonApplyPromo?.text = String.format(it.resources.getString(R.string.promo_checkout_label_button_apply_promo), fragmentUiModel.uiData.usedPromoCount)
                    buttonApplyPromo?.show()
                    buttonApplyNoPromo?.gone()
                    containerActionBottom?.show()
                }
            } else {
                toolbar?.disableResetButton()
                toolbar?.showResetButton()
                if (fragmentUiModel.uiState.hasPreAppliedPromo) {
                    labelTotalPromoInfo?.gone()
                    labelTotalPromoAmount?.gone()
                    buttonApplyPromo?.gone()
                    buttonApplyNoPromo?.show()
                    containerActionBottom?.show()
                } else {
                    containerActionBottom?.gone()
                }
            }
            layoutGlobalError?.gone()
            layoutMainContainer?.show()
        } else {
            toolbar?.disableResetButton()
            toolbar?.hideResetButton()
            fragmentUiModel.uiData.exception?.let {
                layoutGlobalError?.setType(getGlobalErrorType(it))
            }
            layoutGlobalError?.setActionClickListener { view ->
                viewModel.sendAnalyticsClickCobaLagi()
                layoutGlobalError?.gone()
                reloadData()
            }
            layoutGlobalError?.show()
            layoutMainContainer?.gone()
            containerActionBottom?.gone()
            viewModel.sendAnalyticsViewErrorPopup()
        }
    }

    private fun reloadData() {
        viewModel.resetPromoInput()
        toolbar?.disableResetButton()
        toolbar?.hideResetButton()
        containerActionBottom?.gone()
        adapter.clearAllElements()
        layoutMainContainer?.show()
        loadData(0)
    }

    private fun getGlobalErrorType(e: Throwable): Int {
        return if (e is UnknownHostException) {
            NO_CONNECTION
        } else {
            SERVER_ERROR
        }
    }

    private fun setupToolbar(view: View) {
        activity?.let {
            val appbar = view.findViewById<Toolbar>(R.id.toolbar)
            appbar.removeAllViews()
            toolbar = getToolbarPromoCheckout()
            toolbar?.let {
                appbar.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(appbar)
            }
        }
    }

    private fun getToolbarPromoCheckout(): ToolbarPromoCheckout? {
        activity?.let {
            return ToolbarPromoCheckout(it).apply {
                listener = this@PromoCheckoutFragment
            }
        }

        return null
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PromoCheckoutAdapterTypeFactory> {
        adapter = PromoCheckoutAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getAdapterTypeFactory(): PromoCheckoutAdapterTypeFactory {
        return PromoCheckoutAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun loadData(page: Int) {
        activity?.let {
            showLoading()
            val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) as PromoRequest
            val mutation = GraphqlHelper.loadRawString(it.resources, R.raw.get_coupon_list_recommendation)
            viewModel.loadData(mutation, promoRequest, "")
        }
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    fun showToastMessage(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    fun showToastMessage(throwable: Throwable) {
        showToastMessage(getErrorMessage(throwable))
    }

    private fun getErrorMessage(throwable: Throwable): String {
        var errorMessage = throwable.message
        if (throwable !is PromoErrorException) errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        if (errorMessage.isNullOrBlank()) {
            errorMessage = getString(R.string.label_error_global_promo_checkout)
        }
        return errorMessage
    }

    private fun showSavePromoDialog() {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_title_promo_dialog_backpressed))
                setDescription(getString(R.string.label_description_promo_dialog_backpressed))
                setPrimaryCTAText(getString(R.string.label_primary_cta_promo_dialog_backpressed))
                setSecondaryCTAText(getString(R.string.label_secondary_cta_promo_dialog_backpressed))
                setPrimaryCTAClickListener {
                    viewModel.sendAnalyticsClickSimpanPromoBaru()
                    if (viewModel.isHasAnySelectedPromoItem()) {
                        val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST) as ValidateUsePromoRequest
                        val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                        viewModel.applyPromo(GraphqlHelper.loadRawString(it.resources, R.raw.mutation_validate_use_promo_revamp), validateUsePromoRequest, bboPromoCodes
                                ?: ArrayList())
                    } else {
                        val validateUsePromoRequest = arguments?.getParcelable(ARGS_VALIDATE_USE_REQUEST) as ValidateUsePromoRequest
                        val bboPromoCodes = arguments?.getStringArrayList(ARGS_BBO_PROMO_CODES) as ArrayList<String>?
                        viewModel.clearPromo(GraphqlHelper.loadRawString(it.resources, R.raw.clear_promo), validateUsePromoRequest, bboPromoCodes
                                ?: ArrayList())
                    }
                }
                setSecondaryCTAClickListener {
                    viewModel.sendAnalyticsClickKeluarHalaman()
                    dismiss()
                    it.finish()
                }
            }.show()

            viewModel.sendAnalyticsViewPopupSavePromo()
        }
    }

    override fun onBackPressed() {
        if (viewModel.fragmentUiModel.value != null) {
            if (viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false && viewModel.hasDifferentPreAppliedState()) {
                showSavePromoDialog()
            } else {
                activity?.finish()
            }
        } else {
            activity?.finish()
        }
    }

    override fun onClickResetPromo() {
        viewModel.resetPromo()
    }

    override fun onClickApplyRecommendedPromo() {
        viewModel.applyRecommendedPromo()
    }

    override fun onClickApplyManualInputPromo(promoCode: String) {
        activity?.let {
            viewModel.updatePromoInputStateBeforeApplyPromo(promoCode)
            val promoRequest = arguments?.getParcelable(ARGS_PROMO_REQUEST) as PromoRequest
            val mutation = GraphqlHelper.loadRawString(it.resources, R.raw.get_coupon_list_recommendation)
            viewModel.loadData(mutation, promoRequest, promoCode)
        }
    }

    override fun onCLickClearManualInputPromo() {
        viewModel.sendAnalyticsClickRemovePromoCode()
    }

    override fun onClickPromoListHeader(element: PromoListHeaderUiModel) {
        viewModel.updatePromoListAfterClickPromoHeader(element)
    }

    override fun onClickPromoListItem(element: PromoListItemUiModel) {
        viewModel.updatePromoListAfterClickPromoItem(element)
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        viewModel.sendAnalyticsClickLihatDetailKupon(element.uiData.promoCode)
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            val promoCodeLink = element.uiData.couponAppLink + element.uiData.promoCode
            putExtra(EXTRA_KUPON_CODE, promoCodeLink)
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
        }
        startActivity(intent)
    }

    override fun onClickPromoEligibilityHeader(element: PromoEligibilityHeaderUiModel) {
        viewModel.updateIneligiblePromoList(element)
    }

    override fun onClickEmptyStateButton(element: PromoEmptyStateUiModel) {
        if (element.uiData.emptyStateStatus == STATUS_PHONE_NOT_VERIFIED) {
            viewModel.sendAnalyticsClickButtonVerifikasiNomorHp()
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
            startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION)
        } else {
            reloadData()
        }
    }

}