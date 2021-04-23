package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingShopPageInfo
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.config.ShopPageConfig
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.activity.ShopInfoActivity.Companion.EXTRA_SHOP_INFO
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory
import com.tokopedia.shop.info.view.viewmodel.ShopInfoViewModel
import com.tokopedia.shop.note.view.activity.ShopNoteDetailActivity
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory
import com.tokopedia.shop.note.view.adapter.viewholder.ShopNoteViewHolder
import com.tokopedia.shop.note.view.model.ShopNoteUiModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.partial_new_shop_page_header.view.*
import kotlinx.android.synthetic.main.partial_shop_info_delivery.*
import kotlinx.android.synthetic.main.partial_shop_info_description.*
import kotlinx.android.synthetic.main.partial_shop_info_note.*
import kotlinx.android.synthetic.main.partial_shop_info_statistics.*
import javax.inject.Inject

class ShopInfoFragment : BaseDaggerFragment(), BaseEmptyViewHolder.Callback, ShopNoteViewHolder.OnNoteClicked {

    companion object {
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
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var remoteConfig: RemoteConfig? = null
    private var shopPageConfig: ShopPageConfig? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopInfo = arguments?.getParcelable(EXTRA_SHOP_INFO)
        shopPageTracking = ShopPageTrackingShopPageInfo(TrackingQueue(requireContext()))
        remoteConfig = FirebaseRemoteConfigImpl(context)
        shopPageConfig = ShopPageConfig(context)
        initViewModel()
        initObservers()
        initView()
    }

    override fun onPause() {
        super.onPause()
        shopPageTracking?.sendAllTrackingQueue()
    }

    override fun onDestroy() {
        shopViewModel?.let{
            removeObservers(it.shopInfo)
            removeObservers(it.shopNotesResp)
            removeObservers(it.shopStatisticsResp)
            it.flush()
        }
        super.onDestroy()
    }

    override fun onEmptyButtonClicked() {
        shopInfo?.run {
            shopPageTracking?.clickAddNote(CustomDimensionShopPage
                    .create(shopId, isOfficial == 1, isGold == 1))
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
        }
    }

    override fun onNoteClicked(position: Long, shopNoteUiModel: ShopNoteUiModel) {
        shopInfo?.run {
            shopPageTracking?.clickReadNotes(
                    shopViewModel?.isMyShop(shopId) == true, position.toInt(),
                    CustomDimensionShopPage.create(shopId, isOfficial == 1,
                            isGold == 1))
            startActivity(ShopNoteDetailActivity.createIntent(
                    activity,
                    shopId,
                    shopNoteUiModel.shopNoteId.toString()
            ))
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
    }

    private fun observeShopBadgeReputation() {
        shopViewModel?.shopBadgeReputation?.observe(viewLifecycleOwner,  Observer {
            if (it is Success) {
                showShopBadgeReputation(it.data)
            }
        })
    }

    private fun showShopBadgeReputation(shopBadge: ShopBadge) {
        image_view_shop_reputation_badge?.show()
        ImageHandler.LoadImage(image_view_shop_reputation_badge, shopBadge.badgeHD)
    }

    private fun observeShopNotes() {
        shopViewModel?.shopNotesResp?.let { shopNoteResp ->
            observe(shopNoteResp) {
                when (it) {
                    is Success -> renderListNote(it.data)
                    is Fail -> hideNoteLoading()
                }
            }
        }
    }

    private fun observeShopInfo() {
        shopViewModel?.shopInfo?.let { shopInfo ->
            observe(shopInfo) {
                this@ShopInfoFragment.shopInfo = it
                customDimensionShopPage.updateCustomDimensionData(getShopId(), isOfficial, isGold)
                showShopInfo()
                if (!isOfficial) {
                    getShopBadgeReputation()
                }
            }
        }
    }

    private fun getShopBadgeReputation() {
        shopViewModel?.getShopReputationBadge(getShopId().orEmpty())
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

        recyclerViewNote.apply {
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
        shopInfoStatistics.visibility = View.GONE
    }

    private fun showNoteLoading() {
        noteAdapter?.removeErrorNetwork()
        recyclerViewNote.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun hideNoteLoading() {
        loading.visibility = View.GONE
        recyclerViewNote.visibility = View.VISIBLE
    }

    private fun displayShopLogistic(shopInfo: ShopInfoData) {
        setupLogisticList(shopInfo)

        if (shopViewModel?.isMyShop(shopInfo.shopId) == true) {
            labelViewLogisticTitle.content = getString(R.string.shop_info_label_manage_note)
            labelViewLogisticTitle.setOnClickListener { goToManageLogistic() }
        }
    }

    private fun setupLogisticList(shopInfo: ShopInfoData) {
        val visitable = shopInfo.shipments.map { it.transformToVisitable() }
        val shopLogisticAdapter = ShopInfoLogisticAdapter(ShopInfoLogisticAdapterTypeFactory(), visitable)

        recyclerViewLogistic.apply {
            adapter = shopLogisticAdapter
            isNestedScrollingEnabled = false
            isFocusable = false
        }
    }

    private fun goToManageLogistic() {
        val shippingIntent = RouteManager.getIntent(activity,
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS) ?: return
        startActivity(shippingIntent)
    }

    private fun displayShopDescription(shopInfo: ShopInfoData) {
        if (TextUtils.isEmpty(shopInfo.tagLine) && TextUtils.isEmpty(shopInfo.description)) {
            shopInfoDescription.hide()
        } else {
            shopInfoDescription.show()
            shopInfoDescription.text = MethodChecker
                    .fromHtmlPreserveLineBreak("${shopInfo.tagLine}<br/><br/>${shopInfo.description}")
        }
        shopInfoLocation.text = shopInfo.location
        shopInfoOpenSince.text = getString(R.string.shop_info_label_open_since_v3, shopInfo.openSince)
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
        noteLabelView.content = getString(R.string.shop_info_label_manage_note)
        noteLabelView.setOnClickListener { onEmptyButtonClicked() }
    }

    private fun showShopNotes(notes: List<ShopNoteUiModel>) {
        noteAdapter?.addElement(notes)
    }

    private fun showEmptyShopNotes(isMyShop: Boolean) {
        noteAdapter?.addElement(EmptyModel().apply {
            if (isMyShop) {
                title = getString(R.string.shop_note_empty_note_title_seller)
                callback = this@ShopInfoFragment
            } else {
                title = getString(R.string.shop_note_empty_note_title_buyer)
            }
        })
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

}