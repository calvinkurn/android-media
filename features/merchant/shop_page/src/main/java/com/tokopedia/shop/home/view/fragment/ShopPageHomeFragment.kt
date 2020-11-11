package com.tokopedia.shop.home.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.dialog.PlayWidgetDeleteDialogContainer
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalViewUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.ext.hasFailedTranscodedChannel
import com.tokopedia.play.widget.ui.model.ext.hasSuccessfulTranscodedChannel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.ShopPlayWidgetAnalyticListener
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.common.constant.*
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.ERROR_WHEN_GET_YOUTUBE_DATA
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.shop.common.util.ShopPageProductChangeGridRemoteConfig
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.getIndicatorCount
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.view.viewmodel.ShopChangeProductGridSharedViewModel
import com.tokopedia.shop.common.view.viewmodel.ShopProductFilterParameterSharedViewModel
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.util.CheckCampaignNplException
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.bottomsheet.PlayWidgetSellerActionBottomSheet
import com.tokopedia.shop.home.view.bottomsheet.ShopHomeNplCampaignTncBottomSheet
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHomeTabPerformanceMonitoringListener
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import kotlinx.android.synthetic.main.fragment_shop_page_home.*
import javax.inject.Inject

class ShopPageHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>(),
        ShopHomeDisplayWidgetListener,
        ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener,
        ShopHomeEndlessProductListener,
        ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener,
        ShopHomeCarouselProductListener,
        ShopHomeCampaignNplWidgetListener,
        ShopProductChangeGridSectionListener,
        SortFilterBottomSheet.Callback,
        PlayWidgetListener {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        const val KEY_SHOP_NAME = "SHOP_NAME"
        const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        const val KEY_SHOP_REF = "SHOP_REF"
        const val SPAN_COUNT = 2
        const val SAVED_SHOP_SORT_ID = "saved_shop_sort_id"
        const val SAVED_SHOP_SORT_NAME = "saved_shop_sort_name"
        const val SAVED_SHOP_PRODUCT_FILTER_PARAMETER = "SAVED_SHOP_PRODUCT_FILTER_PARAMETER"
        private const val REQUEST_CODE_ETALASE = 206
        private const val REQUEST_CODE_SORT = 301
        private const val REQUEST_CODE_PLAY_ROOM = 256
        private const val REQUEST_CODE_USER_LOGIN = 101
        const val BUNDLE = "bundle"
        const val REGISTER_VALUE = "REGISTER"
        const val UNREGISTER_VALUE = "UNREGISTER"
        const val NPL_REMIND_ME_CAMPAIGN_ID =  "NPL_REMIND_ME_CAMPAIGN_ID"
        const val NUM_VOUCHER_DISPLAY = 10

        fun createInstance(
                shopId: String,
                isOfficialStore: Boolean,
                isGoldMerchant: Boolean,
                shopName: String,
                shopAttribution: String,
                shopRef: String
        ): Fragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
            bundle.putString(KEY_SHOP_REF, shopRef)

            return ShopPageHomeFragment().apply {
                arguments = bundle
            }
        }
    }

    private var threeDotsClickShopProductViewModel: ShopHomeProductViewModel? = null
    private var threeDotsClickShopCarouselProductUiModel: ShopHomeCarousellProductUiModel? = null
    @Inject
    lateinit var shopPageHomeTracking: ShopPageHomeTracking
    @Inject
    lateinit var shopPlayWidgetAnalytic: ShopPlayWidgetAnalyticListener
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ShopHomeViewModel? = null
    private var shopId: String = ""
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var shopName: String = ""
    private var shopAttribution: String = ""
    private var shopRef: String = ""
    private var productListName: String = ""
    private var sortId
        get() = shopProductFilterParameter?.getSortId().orEmpty()
        set(value) {
            shopProductFilterParameter?.setSortId(value)
        }
    private val sortName
        get() = viewModel?.getSortNameById(sortId).orEmpty()
    private var recyclerViewTopPadding = 0
    private var shopProductFilterParameterSharedViewModel: ShopProductFilterParameterSharedViewModel? = null
    private var shopChangeProductGridSharedViewModel: ShopChangeProductGridSharedViewModel? = null
    private var remoteConfig: RemoteConfig? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null
    private var shopProductFilterParameter: ShopProductFilterParameter? = ShopProductFilterParameter()

    val isLogin: Boolean
        get() = viewModel?.isLogin ?: false
    val isOwner: Boolean
        get() = ShopUtil.isMyShop(shopId, viewModel?.userSessionShopId ?: "")
    private val shopPageHomeLayoutUiModel: ShopPageHomeLayoutUiModel?
        get() = (viewModel?.shopHomeLayoutData?.value as? Success)?.data
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private val shopHomeAdapter: ShopHomeAdapter
        get() = adapter as ShopHomeAdapter

    private val shopHomeAdapterTypeFactory by lazy {
        ShopHomeAdapterTypeFactory(this, this, this, this, this, this, this, playWidgetCoordinator)
    }

    private val widgetDeleteDialogContainer by lazy {
        PlayWidgetDeleteDialogContainer(object : PlayWidgetDeleteDialogContainer.Listener {
            override fun onDeleteButtonClicked(channelId: String) {
                deleteChannel(channelId)
            }
        })
    }

    private lateinit var playWidgetCoordinator: PlayWidgetCoordinator
    private lateinit var playWidgetActionBottomSheet: PlayWidgetSellerActionBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        initPltMonitoring()
        getIntentData()
        setupPlayWidget()
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            shopProductFilterParameter = it.getParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER)
        }
        remoteConfig = FirebaseRemoteConfigImpl(context)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
        shopProductFilterParameterSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopProductFilterParameterSharedViewModel::class.java)
        shopChangeProductGridSharedViewModel = ViewModelProvider(requireActivity()).get(ShopChangeProductGridSharedViewModel::class.java)
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        setupPlayWidgetAnalyticListener()
    }

    private fun initPltMonitoring() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.initShopPageHomeTabPerformanceMonitoring()
    }


    private fun startMonitoringPltNetworkRequest() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltPreparePage(it)
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
        }
    }

    private fun startMonitoringPltRenderPage() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.let { shopPageActivity ->
            shopPageActivity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.stopMonitoringPltRenderPage(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_page_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view)?.let {
            it.clearOnScrollListeners()
            it.layoutManager = staggeredGridLayoutManager
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
        observeShopProductFilterParameterSharedViewModel()
        observeShopChangeProductGridSharedViewModel()
        observeLiveData()
        loadInitialData()
    }

    private fun observeShopChangeProductGridSharedViewModel() {
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.observe(viewLifecycleOwner, Observer {
            if (!shopHomeAdapter.isLoading) {
                changeProductListGridView(it)
            }
        })
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    private fun observeShopProductFilterParameterSharedViewModel() {
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.observe(viewLifecycleOwner, Observer {
            if (!shopHomeAdapter.isLoading) {
                shopProductFilterParameter = it
                changeSortData(sortId)
            }
        })
    }

    override fun onResume() {
        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
        shopHomeAdapter.resumeSliderBannerAutoScroll()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        playWidgetOnVisibilityChanged(
                isUserVisibleHint = isVisibleToUser
        )
    }

    override fun onPause() {
        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
        shopPageHomeTracking.sendAllTrackingQueue()
        shopHomeAdapter.pauseSliderBannerAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::playWidgetCoordinator.isInitialized) {
            playWidgetCoordinator.onDestroy()
        }
    }

    override fun onDestroy() {
        viewModel?.initialProductListData?.removeObservers(this)
        viewModel?.newProductListData?.removeObservers(this)
        viewModel?.shopHomeLayoutData?.removeObservers(this)
        viewModel?.checkWishlistData?.removeObservers(this)
        viewModel?.bottomSheetFilterLiveData?.removeObservers(this)
        viewModel?.shopProductFilterCountLiveData?.removeObservers(this)
        viewModel?.flush()
        shopProductFilterParameterSharedViewModel?.sharedShopProductFilterParameter?.removeObservers(this)
        shopChangeProductGridSharedViewModel?.sharedProductGridType?.removeObservers(this)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_SHOP_PRODUCT_FILTER_PARAMETER, shopProductFilterParameter)
    }

    override fun loadInitialData() {
        shopHomeAdapter.clearAllElements()
        recycler_view.visible()
        recyclerViewTopPadding = recycler_view?.paddingTop ?: 0
        globalError_shopPage.hide()
        showLoading()
        shopHomeAdapter.isOwner = isOwner
        startMonitoringPltNetworkRequest()
        viewModel?.getShopPageHomeData(shopId, shopProductFilterParameter ?: ShopProductFilterParameter())
    }

    private fun getIntentData() {
        arguments?.let {
            shopId = it.getString(KEY_SHOP_ID, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL_STORE, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
            shopName = it.getString(KEY_SHOP_NAME, "")
            shopAttribution = it.getString(KEY_SHOP_ATTRIBUTION, "")
            shopRef = it.getString(KEY_SHOP_REF, "")
        }
    }

    private fun observeLiveData() {
        viewModel?.shopHomeLayoutData?.observe(viewLifecycleOwner, Observer {
            startMonitoringPltRenderPage()
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetShopHomeLayoutData(it.data)
                    stopMonitoringPltRenderPage()
                    viewModel?.getMerchantVoucherList(shopId, NUM_VOUCHER_DISPLAY)
                }
                is Fail -> {
                    onErrorGetShopHomeLayoutData(it.throwable)
                    stopPerformanceMonitor()
                    val throwable = it.throwable
                    ShopUtil.logTimberWarning(
                            "SHOP_PAGE_HOME_TAB_WIDGET_ERROR",
                            "shop_id='${shopId}';" +
                                    "error_message='${ErrorHandler.getErrorMessage(context, throwable)}'" +
                                    ";error_trace='${Log.getStackTraceString(throwable)}'"
                    )
                    stopMonitoringPltRenderPage()
                }
            }
        })

        viewModel?.initialProductListData?.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    addProductListHeader()
                    updateProductListData(it.data.hasNextPage, it.data.listShopProductUiModel, it.data.totalProductData, true)
                    productListName = it.data.listShopProductUiModel.joinToString(","){ product -> product.name.orEmpty() }
                }
            }
            stopPerformanceMonitor()
        })

        viewModel?.newProductListData?.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    updateProductListData(it.data.hasNextPage, it.data.listShopProductUiModel, it.data.totalProductData, false)
                    productListName = it.data.listShopProductUiModel.joinToString(","){ product -> product.name.orEmpty() }
                }
            }
        })

        viewModel?.checkWishlistData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessCheckWishlist(it.data)
                }
            }
        })

        viewModel?.videoYoutube?.observe(viewLifecycleOwner, Observer {
            val result = it.second
            when (result) {
                is Success -> {
                    onSuccessGetYouTubeData(it.first, result.data)
                }
                is Fail -> {
                    onFailedGetYouTubeData(it.first, result.throwable)
                }
            }
        })

        viewModel?.campaignNplRemindMeStatusData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetCampaignNplRemindMeStatusData(it.data)
                }
            }
        })

        viewModel?.checkCampaignNplRemindMeStatusData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.success) {
                        onSuccessCheckCampaignNplNotifyMe(it.data)
                    } else {
                        onFailCheckCampaignNplNotifyMe(it.data.campaignId, it.data.errorMessage)
                    }
                }
                is Fail -> {
                    (it.throwable as? CheckCampaignNplException)?.let{ checkCampaignException ->
                        val errorMessage = ErrorHandler.getErrorMessage(context, checkCampaignException)
                        onFailCheckCampaignNplNotifyMe(checkCampaignException.campaignId, errorMessage)
                    }
                }
            }
        })

        viewModel?.bottomSheetFilterLiveData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetBottomSheetFilterData(it.data)
                }
            }
        })

        viewModel?.shopProductFilterCountLiveData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopProductFilterCount(it.data)
                }
            }
        })

        viewModel?.shopHomeMerchantVoucherLayoutData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    shopHomeAdapter.setHomeMerchantVoucherData(it.data)
                }
                is Fail -> {
                    shopPageHomeLayoutUiModel?.listWidget?.indexOfFirst { uiModel -> uiModel is ShopHomeVoucherUiModel }?.let { index ->
                        val data = shopPageHomeLayoutUiModel?.listWidget?.get(index) as ShopHomeVoucherUiModel
                        shopHomeAdapter.setHomeMerchantVoucherData(data.copy(isError = true))
                    }
                }
            }
        })

        observePlayWidget()
        observePlayWidgetToggleReminder()
    }

    private fun onSuccessGetShopProductFilterCount(count: Int) {
        val countText = String.format(
                getString(com.tokopedia.filter.R.string.bottom_sheet_filter_finish_button_template_text),
                count.thousandFormatted()
        )
        sortFilterBottomSheet?.setResultCountText(countText)
    }

    private fun onSuccessGetBottomSheetFilterData(model: DynamicFilterModel) {
        model.defaultSortValue = DEFAULT_SORT_ID
        sortFilterBottomSheet?.setDynamicFilterModel(model)
    }

    private fun addChangeProductGridSection(totalProductData: Int) {
        shopHomeAdapter.updateShopPageProductChangeGridSection(totalProductData)
    }

    private fun onFailCheckCampaignNplNotifyMe(campaignId: String, errorMessage: String) {
        view?.let{
            Toaster.make(
                    it,
                    errorMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    getString(R.string.shop_string_ok)
            )
        }
        shopHomeAdapter.updateRemindMeStatusCampaignNplWidgetData(campaignId)
    }

    private fun onSuccessCheckCampaignNplNotifyMe(data: CheckCampaignNotifyMeUiModel) {
        val isRegisterCampaign = data.action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()
        shopHomeAdapter.updateRemindMeStatusCampaignNplWidgetData(
                data.campaignId,
                isRegisterCampaign,
                true
        )
        shopPageHomeTracking.clickNotifyMeButton(
                isOwner,
                data.action,
                customDimensionShopPage
        )
        view?.let {
            Toaster.make(
                    it,
                    data.message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.shop_string_ok),
                    View.OnClickListener {
                        shopPageHomeTracking.toasterActivationClickOk(isOwner, customDimensionShopPage)
                    }
            )
            shopPageHomeTracking.impressionToasterActivation(isOwner, customDimensionShopPage)
        }
    }

    private fun onSuccessGetCampaignNplRemindMeStatusData(data: GetCampaignNotifyMeUiModel) {
        shopHomeAdapter.updateRemindMeStatusCampaignNplWidgetData(data.campaignId, data.isAvailable)
        if (getNplRemindMeClickedCampaignId() == data.campaignId && !data.isAvailable) {
            val nplCampaignModel = shopHomeAdapter.getNplCampaignUiModel(data.campaignId)
            nplCampaignModel?.let{
                shopHomeAdapter.showNplRemindMeLoading(data.campaignId)
                handleClickRemindMe(it)
                setNplRemindMeClickedCampaignId("")
            }
        }
    }

    private fun addProductListHeader() {
        shopHomeAdapter.setEtalaseTitleData()
        val shopProductSortFilterUiModel = ShopProductSortFilterUiModel(
                selectedEtalaseId = "",
                selectedEtalaseName = "",
                selectedSortId = sortId,
                selectedSortName = sortName,
                filterIndicatorCounter = getIndicatorCount(
                        shopProductFilterParameter?.getMapData()
                )
        )
        shopHomeAdapter.setSortFilterData(shopProductSortFilterUiModel)
    }

    private fun onSuccessCheckWishlist(data: List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>) {
        data.onEach { pairCheckWishlistData ->
            pairCheckWishlistData?.let {
                it.second.onEach { checkWishlistResult ->
                    val productData = it.first.productList.find { shopHomeProductViewModel ->
                        shopHomeProductViewModel.id == checkWishlistResult.productId
                    }
                    productData?.let { shopHomeProductViewModel ->
                        shopHomeProductViewModel.isWishList = checkWishlistResult.isWishlist
                    }
                }
                shopHomeAdapter.updateProductWidgetData(it.first)
            }
        }
    }

    private fun onSuccessGetYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        shopHomeAdapter.setHomeYouTubeData(widgetId, data)
    }

    private fun onFailedGetYouTubeData(widgetId: String, throwable: Throwable) {
        logExceptionToCrashlytics(ERROR_WHEN_GET_YOUTUBE_DATA, throwable)
        shopHomeAdapter.setHomeYouTubeData(widgetId, YoutubeVideoDetailModel())
    }

    private fun onSuccessAddToCart(
            dataModelAtc: DataModel,
            shopHomeProductViewModel: ShopHomeProductViewModel?,
            parentPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?
    ) {
        view?.let { view ->
            NetworkErrorHelper.showGreenCloseSnackbar(view, dataModelAtc.message.first())
        }
        trackClickAddToCart(dataModelAtc, shopHomeProductViewModel, parentPosition, shopHomeCarousellProductUiModel)
    }

    private fun trackClickAddToCart(
            dataModelAtc: DataModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?,
            parentPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?
    ) {
        shopPageHomeTracking.addToCart(
                isOwner,
                dataModelAtc?.cartId ?: "",
                shopAttribution,
                isLogin,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                shopHomeProductViewModel?.name ?: "",
                shopHomeProductViewModel?.id ?: "",
                shopHomeProductViewModel?.displayedPrice ?: "",
                dataModelAtc?.quantity ?: 1,
                shopName,
                parentPosition + 1,
                shopHomeCarousellProductUiModel?.widgetId ?: "",
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                shopHomeCarousellProductUiModel?.header?.isATC ?: 0,
                customDimensionShopPage
        )
    }

    private fun onErrorAddToCart(
            exception: Throwable
    ) {
        view?.let { view ->
            val errorMessage = ErrorHandler.getErrorMessage(context, exception)
            NetworkErrorHelper.showRedCloseSnackbar(view, errorMessage)
        }
    }

    private fun onErrorGetShopHomeLayoutData(throwable: Throwable) {
        if (throwable is MessageErrorException) {
            globalError_shopPage.setType(GlobalError.SERVER_ERROR)
        } else {
            globalError_shopPage.setType(GlobalError.NO_CONNECTION)
        }
        globalError_shopPage.visible()
        recycler_view.hide()

        globalError_shopPage.setOnClickListener {
            loadInitialData()
        }
    }

    private fun updateProductListData(
            hasNextPage: Boolean,
            productList: List<ShopHomeProductViewModel>,
            totalProductData: Int,
            isInitialData: Boolean
    ) {
        if(ShopPageProductChangeGridRemoteConfig.isFeatureEnabled(remoteConfig)) {
            addChangeProductGridSection(totalProductData)
        }
        shopHomeAdapter.setProductListData(productList, isInitialData)
        updateScrollListenerState(hasNextPage)
    }

    private fun onSuccessGetShopHomeLayoutData(data: ShopPageHomeLayoutUiModel) {
        shopHomeAdapter.hideLoading()
        shopHomeAdapter.setHomeLayoutData(data.listWidget)
        checkProductWidgetWishListStatus(data)
        checkCampaignNplWidgetRemindMeStatus(data)
    }

    private fun checkCampaignNplWidgetRemindMeStatus(data: ShopPageHomeLayoutUiModel) {
        viewModel?.let {
            if (it.isLogin) {
                val listCampaignNplWidget = data.listWidget.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>()
                listCampaignNplWidget.forEach { nplCampaignUiModel ->
                    nplCampaignUiModel.data?.firstOrNull()?.let { nplCampaignItem ->
                        if (nplCampaignItem.statusCampaign.toLowerCase() == StatusCampaign.UPCOMING.statusCampaign.toLowerCase())
                            viewModel?.getCampaignNplRemindMeStatus(nplCampaignItem)
                    }
                }
            }
        }
    }

    private fun checkProductWidgetWishListStatus(data: ShopPageHomeLayoutUiModel) {
        viewModel?.let {
            if (it.isLogin) {
                val listProductWidget = data.listWidget.filterIsInstance<ShopHomeCarousellProductUiModel>()
                if (listProductWidget.isNotEmpty()) {
                    viewModel?.getWishlistStatus(listProductWidget)
                }
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory> {
        return ShopHomeAdapter(shopHomeAdapterTypeFactory).apply {
            shopHomeAdapterTypeFactory.adapter = this
        }
    }

    override fun getAdapterTypeFactory() = shopHomeAdapterTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                    .builder()
                    .shopPageHomeModule(ShopPageHomeModule())
                    .shopComponent(ShopComponentHelper().getComponent(application, this))
                    .build()
                    .inject(this@ShopPageHomeFragment)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopHomeAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopHomeAdapter.showLoading()
                getProductList(page)
            }
        }
    }

    fun getProductList(page: Int) {
        if (shopId.isNotEmpty()) {
            viewModel?.getNewProductList(shopId, page, shopProductFilterParameter ?: ShopProductFilterParameter())
        }
    }

    override fun loadData(page: Int) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (shopHomeAdapter.isLoading) {
                    return
                }

                val etalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID) ?: ""
                val etalaseName = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME) ?: ""
                val isNeedToReloadData = data.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
                shopPageHomeTracking.clickMoreMenuChip(
                        isOwner,
                        etalaseName,
                        customDimensionShopPage
                )
                val intent = ShopProductListResultActivity.createIntent(
                        activity,
                        shopId,
                        "",
                        etalaseId,
                        "",
                        "",
                        shopRef
                )

                intent.putExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData)
                startActivity(intent)
            }
            REQUEST_CODE_SORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (shopHomeAdapter.isLoading) {
                        return
                    }
                    val sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    shopPageHomeTracking.sortProduct(sortName, isOwner, customDimensionShopPage)
                    changeSortData(sortId)
                    scrollToEtalaseTitlePosition()
                }
            }
            REQUEST_CODE_USER_LOGIN -> {
                if (resultCode == Activity.RESULT_OK)
                    (parentFragment as? ShopPageFragment)?.refreshData()
            }
            else -> {
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })
        if (requestCode == PlayWidgetCardMediumChannelViewHolder.KEY_PLAY_WIDGET_REQUEST_CODE && data != null) {
            notifyPlayWidgetTotalView(data)
        }
    }

    private fun scrollToEtalaseTitlePosition() {
        recycler_view?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
                shopHomeAdapter.shopHomeEtalaseTitlePosition,
                0
        )
    }

    override fun onDisplayItemImpression(displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?, displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, parentPosition: Int, adapterPosition: Int) {
        val destinationLink: String
        val creativeUrl: String
        when (displayWidgetUiModel?.name ?: "") {
            VIDEO -> {
                destinationLink = displayWidgetItem.videoUrl
                creativeUrl = displayWidgetItem.videoUrl
            }
            else -> {
                destinationLink = displayWidgetItem.appLink
                creativeUrl = displayWidgetItem.imageUrl
            }
        }
        shopPageHomeTracking.impressionDisplayWidget(
                false,
                shopId,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                displayWidgetUiModel?.name ?: "",
                displayWidgetUiModel?.widgetId ?: "",
                parentPosition + 1,
                displayWidgetUiModel?.header?.ratio ?: "",
                destinationLink,
                creativeUrl,
                adapterPosition + 1,
                customDimensionShopPage
        )
    }

    override fun onDisplayItemClicked(displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?, displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, parentPosition: Int, adapterPosition: Int) {
        val destinationLink: String
        val creativeUrl: String
        when (displayWidgetUiModel?.name ?: "") {
            VIDEO -> {
                destinationLink = displayWidgetItem.videoUrl
                creativeUrl = displayWidgetItem.videoUrl
            }
            else -> {
                destinationLink = displayWidgetItem.appLink
                creativeUrl = displayWidgetItem.imageUrl
            }
        }
        shopPageHomeTracking.clickDisplayWidget(
                false,
                shopId,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                displayWidgetUiModel?.name ?: "",
                displayWidgetUiModel?.widgetId ?: "",
                parentPosition + 1,
                displayWidgetUiModel?.header?.ratio ?: "",
                destinationLink,
                creativeUrl,
                adapterPosition + 1,
                customDimensionShopPage
        )
        context?.let {
            if (displayWidgetItem.appLink.isNotEmpty())
                RouteManager.route(it, displayWidgetItem.appLink)
        }
    }

    override fun loadYouTubeData(videoUrl: String, widgetId: String) {
        viewModel?.getVideoYoutube(videoUrl, widgetId)
    }

    override fun onVoucherClicked(parentPosition: Int, position: Int, merchantVoucherViewModel: MerchantVoucherViewModel) {
        shopPageHomeTracking.clickDetailMerchantVoucher(
                isOwner,
                shopId,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                parentPosition + 1,
                position + 1,
                merchantVoucherViewModel,
                customDimensionShopPage
        )
        context?.let {
            val intentMerchantVoucherDetail = MerchantVoucherDetailActivity.createIntent(
                    it,
                    merchantVoucherViewModel.voucherId,
                    merchantVoucherViewModel, shopId
            )
            startActivity(intentMerchantVoucherDetail)
        }
    }

    override fun onVoucherReloaded() {
        viewModel?.getMerchantVoucherList(shopId, NUM_VOUCHER_DISPLAY)
    }

    override fun onVoucherSeeAllClicked() {
        shopPageHomeTracking.clickSeeAllMerchantVoucher(
                isOwner,
                shopId,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                customDimensionShopPage)
        context?.let {
            val intentMerchantVoucherList = MerchantVoucherListActivity.createIntent(
                    it,
                    shopId,
                    shopName
            )
            startActivity(intentMerchantVoucherList)
        }
    }

    override fun onVoucherItemImpressed(
            parentPosition: Int,
            itemPosition: Int,
            voucher: MerchantVoucherViewModel
    ) {
        shopPageHomeTracking.onImpressionVoucherItem(
                isOwner,
                shopId,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                parentPosition + 1,
                itemPosition + 1,
                voucher,
                customDimensionShopPage
        )
    }

    override fun onAllProductItemClicked(itemPosition: Int, shopHomeProductViewModel: ShopHomeProductViewModel?) {
        val realItemPositonOnTheList = itemPosition - shopHomeAdapter.getAllProductWidgetPosition()
        shopHomeProductViewModel?.let {
            shopPageHomeTracking.clickProduct(
                    isOwner,
                    isLogin,
                    shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                    shopHomeProductViewModel.name ?: "",
                    shopHomeProductViewModel.id ?: "",
                    shopHomeProductViewModel.displayedPrice ?: "",
                    shopName,
                    shopHomeAdapter.getAllProductWidgetPosition() + 1,
                    realItemPositonOnTheList + 1,
                    "",
                    "",
                    0,
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopHomeProductViewModel.id,
                            shopAttribution,
                            shopRef
                    )
            )
            goToPDP(it.id ?: "")
        }
    }

    override fun onAllProductItemImpression(itemPosition: Int, shopHomeProductViewModel: ShopHomeProductViewModel?) {
        val realItemPositonOnTheList = itemPosition - shopHomeAdapter.getAllProductWidgetPosition()
        shopHomeProductViewModel?.let {
            shopPageHomeTracking.impressionProduct(
                    isOwner,
                    isLogin,
                    shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                    shopHomeProductViewModel.name ?: "",
                    shopHomeProductViewModel.id ?: "",
                    shopHomeProductViewModel.displayedPrice ?: "",
                    shopName,
                    shopHomeAdapter.getAllProductWidgetPosition() + 1,
                    realItemPositonOnTheList + 1,
                    "",
                    "",
                    0,
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopHomeProductViewModel.id,
                            shopAttribution,
                            shopRef
                    )
            )
        }
    }

    override fun onThreeDotsAllProductClicked(shopHomeProductViewModel: ShopHomeProductViewModel) {
        threeDotsClickShopCarouselProductUiModel = null
        threeDotsClickShopProductViewModel = shopHomeProductViewModel
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = shopHomeProductViewModel.isWishList,
                        productId = shopHomeProductViewModel.id ?: ""
                )
        )
    }

    override fun onCarouselProductItemClicked(parentPosition: Int, itemPosition: Int, shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?, shopHomeProductViewModel: ShopHomeProductViewModel?) {
        shopHomeProductViewModel?.let {
            shopPageHomeTracking.clickProduct(
                    isOwner,
                    isLogin,
                    shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                    shopHomeProductViewModel.name ?: "",
                    shopHomeProductViewModel.id ?: "",
                    shopHomeProductViewModel.displayedPrice ?: "",
                    shopName,
                    parentPosition + 1,
                    itemPosition + 1,
                    shopHomeCarousellProductUiModel?.widgetId ?: "",
                    shopHomeCarousellProductUiModel?.header?.title ?: "",
                    shopHomeCarousellProductUiModel?.header?.isATC ?: 0,
                    CustomDimensionShopPageAttribution.create(
                            shopId,
                            isOfficialStore,
                            isGoldMerchant,
                            shopHomeProductViewModel.id,
                            shopAttribution,
                            shopRef
                    )
            )
            goToPDP(it.id ?: "")
        }
    }

    override fun onCarouselProductItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        shopPageHomeTracking.impressionProduct(
                isOwner,
                isLogin,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                shopHomeProductViewModel?.name ?: "",
                shopHomeProductViewModel?.id ?: "",
                shopHomeProductViewModel?.displayedPrice ?: "",
                shopName,
                parentPosition + 1,
                itemPosition + 1,
                shopHomeCarousellProductUiModel?.widgetId ?: "",
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                shopHomeCarousellProductUiModel?.header?.isATC ?: 0,
                CustomDimensionShopPageAttribution.create(
                        shopId,
                        isOfficialStore,
                        isGoldMerchant,
                        shopHomeProductViewModel?.id.orEmpty(),
                        shopAttribution,
                        shopRef
                )
        )
    }

    override fun onThreeDotsCarouselProductItemClicked(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        threeDotsClickShopCarouselProductUiModel = shopHomeCarousellProductUiModel
        threeDotsClickShopProductViewModel = shopHomeProductViewModel
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = shopHomeProductViewModel?.isWishList ?: false,
                        productId = shopHomeProductViewModel?.id ?: ""
                )
        )
    }

    private fun onSuccessRemoveWishList(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        shopHomeProductViewModel?.let {
            showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist))
            shopHomeAdapter.updateWishlistProduct(it.id ?: "", false)
            trackClickWishlist(shopHomeCarousellProductUiModel, shopHomeProductViewModel, false)
        }
    }

    private fun trackClickWishlist(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel,
            isWishlist: Boolean
    ) {
        val customDimensionShopPageProduct = CustomDimensionShopPageProduct.create(
                shopId,
                isOfficialStore,
                isGoldMerchant,
                shopHomeProductViewModel.id,
                shopRef
        )
        shopPageHomeTracking.clickWishlist(
                isOwner,
                isWishlist,
                shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                isLogin,
                shopHomeCarousellProductUiModel?.header?.title ?: "",
                shopHomeCarousellProductUiModel?.widgetId ?: "",
                shopHomeProductViewModel.id ?: "",
                customDimensionShopPageProduct
        )
    }

    override fun onCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?) {

        shopPageHomeTracking.clickCta(
                layoutId = shopPageHomeLayoutUiModel?.masterLayoutId.toString(),
                widgetName = shopHomeCarouselProductUiModel?.header?.title.toString(),
                widgetId = shopHomeCarouselProductUiModel?.widgetId.toString(),
                appLink = shopHomeCarouselProductUiModel?.header?.ctaLink.toString(),
                shopId = shopId,
                shopType = customDimensionShopPage.shopType,
                isOwner = isOwner
        )

        context?.let {
            RouteManager.route(it, shopHomeCarouselProductUiModel?.header?.ctaLink)
        }
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    private fun onSuccessAddWishlist(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        shopHomeProductViewModel?.let {
            showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist))
            shopHomeAdapter.updateWishlistProduct(it.id ?: "", true)
            trackClickWishlist(shopHomeCarousellProductUiModel, shopHomeProductViewModel, true)
        }
    }

    private fun onErrorAddWishlist(errorMessage: String?) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onCarouselProductItemClickAddToCart(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        if (isLogin) {
            shopHomeProductViewModel?.let { product ->
                viewModel?.addProductToCart(
                        product,
                        shopId,
                        {
                            onSuccessAddToCart(it, shopHomeProductViewModel, parentPosition, shopHomeCarousellProductUiModel)
                        },
                        {
                            onErrorAddToCart(it)
                        }
                )
            }
        } else {
            trackClickAddToCart(
                    null,
                    shopHomeProductViewModel,
                    parentPosition,
                    shopHomeCarousellProductUiModel
            )
            redirectToLoginPage()
        }
    }

    private fun goToPDP(productId: String) {
        context?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
            startActivity(intent)
        }
    }

    private fun redirectToLoginPage(requestCode: Int = REQUEST_CODE_USER_LOGIN) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            Toaster.make(findViewById(android.R.id.content), message)
        }
    }

    private fun showErrorToast(message: String) {
        activity?.run {
            Toaster.make(findViewById(android.R.id.content), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            threeDotsClickShopProductViewModel?.let {
                trackClickWishlist(threeDotsClickShopCarouselProductUiModel, it, true)
            }
            redirectToLoginPage()
        } else {
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        viewModel?.clearGetShopProductUseCase()
        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleWishlistActionAddToWishlist(productCardOptionsModel)
        } else {
            handleWishlistActionRemoveFromWishlist(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionAddToWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessAddWishlist(
                    threeDotsClickShopCarouselProductUiModel,
                    threeDotsClickShopProductViewModel
            )
        } else {
            onErrorAddWishlist(
                    getString(com.tokopedia.wishlist.common.R.string.msg_error_add_wishlist)
            )
        }
    }

    private fun handleWishlistActionRemoveFromWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessRemoveWishList(
                    threeDotsClickShopCarouselProductUiModel,
                    threeDotsClickShopProductViewModel
            )
        } else {
            onErrorRemoveWishList(
                    getString(com.tokopedia.wishlist.common.R.string.msg_error_remove_wishlist)
            )
        }
    }

    private fun stopPerformanceMonitor() {
        (activity as? ShopPageActivity)?.stopShopHomeTabPerformanceMonitoring()
    }


    fun clearCache() {
        viewModel?.clearCache()
    }


    private fun redirectToShopSortPickerPage() {
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, sortId)
            startActivityForResult(intent, REQUEST_CODE_SORT)
        }
    }

    private fun redirectToEtalasePicker() {
        context?.let {
            val bundle = Bundle()
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)

            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(BUNDLE, bundle)
            startActivityForResult(intent, REQUEST_CODE_ETALASE)
        }
    }

    override fun onEtalaseFilterClicked() {
        redirectToEtalasePicker()
    }

    override fun onSortFilterClicked() {
        shopPageHomeTracking.clickSort(isOwner, customDimensionShopPage)
        redirectToShopSortPickerPage()
    }

    override fun onClearFilterClicked() {
        if (shopHomeAdapter.isLoading) {
            return
        }
        shopPageHomeTracking.clickClearFilter(
                isOwner,
                customDimensionShopPage
        )
        changeSortData("")
        scrollToEtalaseTitlePosition()
    }

    override fun setSortFilterMeasureHeight(measureHeight: Int) {}

    override fun onFilterClicked() {
        shopPageHomeTracking.clickFilterChips(productListName, customDimensionShopPage)
        showBottomSheetFilter()
    }

    private fun showBottomSheetFilter() {
        val mapParameter = if (sortId.isNotEmpty())
            shopProductFilterParameter?.getMapData()
        else
            shopProductFilterParameter?.getMapDataWithDefaultSortId()
        sortFilterBottomSheet = SortFilterBottomSheet()
        sortFilterBottomSheet?.show(
                requireFragmentManager(),
                mapParameter,
                null,
                this
        )
        sortFilterBottomSheet?.setOnDismissListener {
            sortFilterBottomSheet = null
        }
        viewModel?.getBottomSheetFilterData()
    }


    private fun refreshProductList() {
        shopHomeAdapter.removeProductList()
        shopHomeAdapter.showLoading()
        getProductList(1)
    }

    override fun onCampaignCarouselProductItemClicked(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let{
            shopPageHomeTracking.clickCampaignNplProduct(
                    isOwner,
                    it.statusCampaign,
                    shopHomeProductViewModel?.name ?: "",
                    shopHomeProductViewModel?.id ?: "",
                    shopHomeProductViewModel?.displayedPrice ?: "",
                    shopName,
                    parentPosition + 1,
                    itemPosition + 1,
                    isLogin,
                    customDimensionShopPage
            )
        }
        shopHomeProductViewModel?.let {
            goToPDP(it.id ?: "")
        }
    }

    override fun onCampaignCarouselProductItemImpression(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let{
            shopPageHomeTracking.impressionCampaignNplProduct(
                    isOwner,
                    it.statusCampaign,
                    shopHomeProductViewModel?.name ?: "",
                    shopHomeProductViewModel?.id ?: "",
                    shopHomeProductViewModel?.displayedPrice ?: "",
                    shopName,
                    parentPosition + 1,
                    itemPosition + 1,
                    isLogin,
                    customDimensionShopPage
            )
        }
    }

    override fun onClickTncCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.clickTncButton(isOwner, it.statusCampaign, customDimensionShopPage)
            showNplCampaignTncBottomSheet(it.campaignId, it.statusCampaign)
        }
    }

    private fun showNplCampaignTncBottomSheet(campaignId: String, statusCampaign: String) {
        val bottomSheet = ShopHomeNplCampaignTncBottomSheet.createInstance(
                campaignId,
                statusCampaign,
                shopId,
                isOfficialStore,
                isGoldMerchant
        )
        bottomSheet.show(childFragmentManager, "")
    }

    override fun onClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        viewModel?.let {
            val campaignId = model.data?.firstOrNull()?.campaignId.orEmpty()
            if (it.isLogin) {
                shopHomeAdapter.showNplRemindMeLoading(campaignId)
                handleClickRemindMe(model)
            } else {
                setNplRemindMeClickedCampaignId(campaignId)
                redirectToLoginPage()
            }
        }
    }

    private fun handleClickRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val isRemindMe = model.data?.firstOrNull()?.isRemindMe ?: false
        val action = if (isRemindMe) {
            UNREGISTER_VALUE
        } else {
            REGISTER_VALUE
        }
        val campaignId = model.data?.firstOrNull()?.campaignId ?: ""
        viewModel?.clickRemindMe(campaignId, action)
    }

    override fun onClickCtaCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            shopPageHomeTracking.clickCtaCampaignNplWidget(
                    isOwner,
                    it.statusCampaign,
                    customDimensionShopPage
            )
            context?.let {context ->
                RouteManager.route(context, model.header.ctaLink)
            }
        }
    }

    override fun onImpressionCampaignNplWidget(
            position: Int,
            shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel
    ) {
        shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.let {
            val statusCampaign = it.statusCampaign
            val selectedBannerType = when (statusCampaign.toLowerCase()) {
                StatusCampaign.UPCOMING.statusCampaign.toLowerCase() -> BannerType.UPCOMING.bannerType
                StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> BannerType.LIVE.bannerType
                StatusCampaign.FINISHED.statusCampaign.toLowerCase() -> BannerType.FINISHED.bannerType
                else -> ""
            }
            val selectedBanner = it.bannerList.firstOrNull {
                it.bannerType.toLowerCase() == selectedBannerType.toLowerCase()
            }
            val isSeeCampaign = if (statusCampaign.toLowerCase() == StatusCampaign.UPCOMING.statusCampaign.toLowerCase()) {
                it.totalNotifyWording.isNotEmpty()
            } else {
                null
            }
            shopPageHomeTracking.impressionCampaignNplWidget(
                    it.statusCampaign,
                    shopId,
                    position + 1,
                    isSeeCampaign,
                    selectedBanner?.imageId.toString(),
                    selectedBanner?.imageUrl ?: "",
                    customDimensionShopPage,
                    isOwner
            )
        }
    }

    override fun onTimerFinished(model: ShopHomeNewProductLaunchCampaignUiModel) {
        shopHomeAdapter.removeShopHomeCampaignNplWidget(model)
        viewModel?.getShopPageHomeData(shopId, shopProductFilterParameter ?: ShopProductFilterParameter(),true)
    }

    private fun setNplRemindMeClickedCampaignId(campaignId: String) {
        PersistentCacheManager.instance.put(NPL_REMIND_ME_CAMPAIGN_ID, campaignId)
    }

    private fun getNplRemindMeClickedCampaignId(): String {
        return PersistentCacheManager.instance.get(NPL_REMIND_ME_CAMPAIGN_ID, String::class.java, "").orEmpty()
    }

    private fun changeProductListGridView(gridType: ShopProductViewGridType){
        shopHomeAdapter.updateShopPageProductChangeGridSection(gridType)
        shopHomeAdapter.changeProductCardGridType(gridType)
    }

    override fun onChangeProductGridClicked(gridType: ShopProductViewGridType) {
        shopPageHomeTracking.clickProductListToggle(productListName, isOwner, customDimensionShopPage)
        changeProductListGridView(gridType)
        scrollToEtalaseTitlePosition()
        shopChangeProductGridSharedViewModel?.changeSharedProductGridType(gridType)
    }


    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        val isResetButtonVisible = sortFilterBottomSheet?.bottomSheetAction?.isVisible
        sortFilterBottomSheet = null
        shopProductFilterParameter?.clearParameter()
        shopProductFilterParameter?.setMapData(applySortFilterModel.mapParameter)
        if(isResetButtonVisible == false){
            sortId = ""
        }
        changeSortData(shopProductFilterParameter?.getSortId().orEmpty())
        scrollToEtalaseTitlePosition()
        applySortFilterTracking(sortName, applySortFilterModel.selectedFilterMapParameter)
    }

    private fun changeSortData(sortId: String){
        this.sortId = sortId
        shopProductFilterParameterSharedViewModel?.changeSharedSortData(
                shopProductFilterParameter?:ShopProductFilterParameter()
        )
        shopHomeAdapter.changeSelectedSortFilter(this.sortId, sortName)
        shopHomeAdapter.changeSortFilterIndicatorCounter(getIndicatorCount(
                shopProductFilterParameter?.getMapData()
        ))
        shopHomeAdapter.refreshSticky()
        refreshProductList()
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        val tempShopProductFilterParameter = ShopProductFilterParameter()
        tempShopProductFilterParameter.setMapData(mapParameter)
        viewModel?.getFilterResultCount(
                shopId,
                tempShopProductFilterParameter
        )
    }

    private fun applySortFilterTracking(selectedSortName: String, selectedFilterMap: Map<String, String>) {
        if (selectedSortName.isNotBlank()) {
            shopPageHomeTracking.clickFilterSortBy(productListName, selectedSortName, customDimensionShopPage)
        }
        if (!selectedFilterMap[PMAX_PARAM_KEY].isNullOrBlank() || !selectedFilterMap[PMIN_PARAM_KEY].isNullOrBlank()) {
            shopPageHomeTracking.clickFilterPrice(productListName, selectedFilterMap[PMIN_PARAM_KEY] ?: "0", selectedFilterMap[PMAX_PARAM_KEY] ?: "0", customDimensionShopPage)
        }
        if (!selectedFilterMap[RATING_PARAM_KEY].isNullOrBlank()) {
            shopPageHomeTracking.clickFilterRating(productListName, selectedFilterMap[RATING_PARAM_KEY] ?: "0", customDimensionShopPage)
        }
    }
    /**
     * Play Widget
     */
    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        viewModel?.getPlayWidget(shopId)
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, PlayWidgetCardMediumChannelViewHolder.KEY_PLAY_WIDGET_REQUEST_CODE)
    }

    override fun onToggleReminderClicked(view: PlayWidgetMediumView, channelId: String, remind: Boolean, position: Int) {
        if (isLogin) {
            viewModel?.setToggleReminderPlayWidget(channelId, remind, position)
        } else {
            shopHomeAdapter.updatePlayWidgetReminder(PlayWidgetReminderUiModel(
                    remind = remind,
                    success = false,
                    position = position
            ))
            redirectToLoginPage()
        }
    }

    override fun onDeleteFailedTranscodingChannel(view: PlayWidgetMediumView, channelId: String) {
        viewModel?.deleteChannel(channelId)
    }

    override fun onMenuActionButtonClicked(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, position: Int) {
        showPlayWidgetBottomSheet(item)
    }

    private fun setupPlayWidget() {
        playWidgetCoordinator = PlayWidgetCoordinator().apply {
            setListener(this@ShopPageHomeFragment)
        }
    }

    private fun setupPlayWidgetAnalyticListener() {
        playWidgetCoordinator.apply {
            shopPlayWidgetAnalytic.shopId = shopId
            setAnalyticListener(shopPlayWidgetAnalytic)
        }
    }

    private fun notifyPlayWidgetTotalView(data: Intent) {
        val channelId = data.getStringExtra(PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_CHANNEL_ID).orEmpty()
        val totalView = data.getStringExtra(PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_TOTAL_VIEW).orEmpty()
        shopHomeAdapter.updatePlayWidgetTotalView(PlayWidgetTotalViewUiModel(channelId, totalView))
    }

    private fun observePlayWidget() {
        viewModel?.playWidgetObservable?.observe(viewLifecycleOwner, Observer { carouselPlayWidgetUiModel ->
            shopPlayWidgetAnalytic.widgetId = carouselPlayWidgetUiModel?.widgetId.orEmpty()
            shopHomeAdapter.updatePlayWidget(carouselPlayWidgetUiModel?.widgetUiModel)

            val widget = carouselPlayWidgetUiModel?.widgetUiModel

            if (widget is PlayWidgetUiModel.Medium) {
                if (widget.hasSuccessfulTranscodedChannel) {
                    //TODO("Transcode Successful")
                } else if (widget.hasFailedTranscodedChannel) {
                    //TODO("Transcode Failed")
                }
            }

            carouselPlayWidgetUiModel?.actionEvent?.getContentIfNotHandled()?.let {
                when (it) {
                    is CarouselPlayWidgetUiModel.Action.Delete -> showWidgetDeletedToaster()
                    is CarouselPlayWidgetUiModel.Action.DeleteFailed -> showWidgetDeleteFailedToaster(it.channelId, it.reason)
                    else -> {}
                }
            }
        })
    }

    private fun observePlayWidgetToggleReminder() {
        viewModel?.playWidgetToggleReminderObservable?.observe(viewLifecycleOwner, Observer {
            if (it.success) {
                showToastSuccess(
                        if(it.remind) {
                            getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                        } else {
                            getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder)
                        })
            } else {
                shopHomeAdapter.updatePlayWidgetReminder(it)
                showErrorToast(getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder))
            }
        })
    }

    private fun playWidgetOnVisibilityChanged(
            isViewResumed: Boolean = if (view == null) false else viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED),
            isUserVisibleHint: Boolean = userVisibleHint,
            isParentHidden: Boolean = parentFragment?.isHidden ?: true
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint && !isParentHidden

            if (isViewVisible) playWidgetCoordinator.onResume()
            else playWidgetCoordinator.onPause()
        }
    }

    private fun showPlayWidgetBottomSheet(channelUiModel: PlayWidgetMediumChannelUiModel) {
        getPlayWidgetActionBottomSheet(channelUiModel).show(childFragmentManager)
    }

    private fun getPlayWidgetActionBottomSheet(channelUiModel: PlayWidgetMediumChannelUiModel): PlayWidgetSellerActionBottomSheet {
        if (!::playWidgetActionBottomSheet.isInitialized) {
            playWidgetActionBottomSheet = PlayWidgetSellerActionBottomSheet()
        }

        val bottomSheetActionList = mutableListOf<PlayWidgetSellerActionBottomSheet.Action>()
        if (channelUiModel.share.isShow) {
            bottomSheetActionList.add(
                PlayWidgetSellerActionBottomSheet.Action(
                    com.tokopedia.resources.common.R.drawable.ic_system_action_share_grey_24,
                    MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N400),
                        getString(R.string.shop_page_play_widget_sgc_copy_link)
                ) { copyToClipboard(channelUiModel.share.fullShareContent) }
            )
        }
        bottomSheetActionList.add(
                PlayWidgetSellerActionBottomSheet.Action(
                com.tokopedia.resources.common.R.drawable.ic_system_action_delete_black_24,
                MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N400),
                context?.getString(R.string.shop_page_play_widget_sgc_delete_video).orEmpty()) {
                    showDeleteWidgetConfirmationDialog(channelUiModel.channelId)
                    playWidgetActionBottomSheet.dismiss()
                }
        )

        playWidgetActionBottomSheet.setActionList(bottomSheetActionList)
        return playWidgetActionBottomSheet
    }

    private fun deleteChannel(channelId: String) {
        viewModel?.deleteChannel(channelId)
    }

    private fun showWidgetDeletedToaster() {
        activity?.run {
            Toaster.make(
                    findViewById(android.R.id.content),
                    getString(R.string.shop_page_play_widget_sgc_video_deleted),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL
            )
        }
    }

    private fun showWidgetDeleteFailedToaster(channelId: String, reason: Throwable) {
        activity?.run {
            Toaster.make(
                    view = findViewById(android.R.id.content),
                    text = reason.localizedMessage,
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.shop_page_play_widget_sgc_try_again),
                    clickListener = View.OnClickListener {
                        deleteChannel(channelId)
                    }
            )
        }
    }

    private fun showDeleteWidgetConfirmationDialog(channelId: String) {
        widgetDeleteDialogContainer.confirmDelete(requireContext(), channelId)
    }

    private fun copyToClipboard(shareContents: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("play-widget", shareContents))
    }
}