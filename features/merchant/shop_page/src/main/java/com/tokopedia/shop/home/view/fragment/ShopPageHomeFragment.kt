package com.tokopedia.shop.home.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.ERROR_WHEN_GET_YOUTUBE_DATA
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopPageHomePlayCarouselListener
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHomeTabPerformanceMonitoringListener
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopSortSharedViewModel
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
        ShopPageHomeProductClickListener,
        ShopPageHomePlayCarouselListener,
        ShopProductSortFilterViewHolder.ShopProductEtalaseChipListViewHolderListener {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        const val KEY_SHOP_NAME = "SHOP_NAME"
        const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        const val KEY_SHOP_REF = "SHOP_REF"
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        const val SPAN_COUNT = 2
        const val UPDATE_REMIND_ME_PLAY = "update_remind_me"
        const val SAVED_SHOP_SORT_ID = "saved_shop_sort_id"
        const val SAVED_SHOP_SORT_NAME = "saved_shop_sort_name"
        private const val REQUEST_CODE_ETALASE = 206
        private const val REQUEST_CODE_SORT = 301
        private const val REQUEST_CODE_PLAY_ROOM = 256
        const val BUNDLE_SELECTED_ETALASE_ID = "selectedEtalaseId"
        const val BUNDLE_IS_SHOW_DEFAULT = "isShowDefault"
        const val BUNDLE_IS_SHOW_ZERO_PRODUCT = "isShowZeroProduct"
        const val BUNDLE_SHOP_ID = "shopId"
        const val BUNDLE = "bundle"

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
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ShopHomeViewModel? = null
    private var shopId: String = ""
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var shopName: String = ""
    private var shopAttribution: String = ""
    private var shopRef: String = ""
    private var sortId = ""
    private var sortName = ""
    private var recyclerViewTopPadding = 0
    private var shopSortSharedViewModel: ShopSortSharedViewModel? = null

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
        ShopHomeAdapterTypeFactory(this, this, this, this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPltMonitoring()
        getIntentData()
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            sortId = it.getString(SAVED_SHOP_SORT_ID , "")
            sortName = it.getString(SAVED_SHOP_SORT_NAME, "")
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
        shopSortSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopSortSharedViewModel::class.java)
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initPltMonitoring() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.initShopPageHomeTabPerformanceMonitoring()
    }


    private fun startMonitoringPltNetworkRequest(){
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.let {shopPageActivity ->
            shopPageActivity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltNetworkRequest(it)
            }
        }
    }

    private fun startMonitoringPltRenderPage() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.let {shopPageActivity ->
            shopPageActivity.getShopPageHomeTabLoadTimePerformanceCallback()?.let {
                shopPageActivity.startMonitoringPltRenderPage(it)
            }
        }
    }

    private fun stopMonitoringPltRenderPage() {
        (activity as? ShopPageHomeTabPerformanceMonitoringListener)?.let {shopPageActivity ->
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
        observeShopSortSharedViewModel()
        observeLiveData()
        loadInitialData()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    private fun observeShopSortSharedViewModel() {
        shopSortSharedViewModel?.sharedSortData?.observe(this, Observer {
            if (!shopHomeAdapter.isLoading) {
                sortId = it.first
                sortName = it.second
                shopHomeAdapter.changeSelectedSortFilter(sortId, sortName)
                shopHomeAdapter.refreshSticky()
                refreshProductList()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shopHomeAdapter.resumePlayCarousel()
    }

    override fun onPause() {
        super.onPause()
        shopPageHomeTracking.sendAllTrackingQueue()
        shopHomeAdapter.pausePlayCarousel()
    }

    override fun onDestroy() {
        viewModel?.initialProductListData?.removeObservers(this)
        viewModel?.newProductListData?.removeObservers(this)
        viewModel?.shopHomeLayoutData?.removeObservers(this)
        viewModel?.checkWishlistData?.removeObservers(this)
        viewModel?.reminderPlayLiveData?.removeObservers(this)
        viewModel?.flush()
        shopHomeAdapter.onDestroy()
        shopSortSharedViewModel?.sharedSortData?.removeObservers(this)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SHOP_SORT_ID, sortId)
        outState.putString(SAVED_SHOP_SORT_NAME, sortName)
    }

    override fun loadInitialData() {
        shopHomeAdapter.clearAllElements()
        recycler_view.visible()
        recyclerViewTopPadding = recycler_view?.paddingTop ?: 0
        scrollView_globalError_shopPage.hide()
        globalError_shopPage.hide()
        showLoading()
        shopHomeAdapter.isOwner = isOwner
        startMonitoringPltNetworkRequest()
        viewModel?.getShopPageHomeData(shopId, sortId.toIntOrZero())
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
        viewModel?.shopHomeLayoutData?.observe(this, Observer {
            startMonitoringPltRenderPage()
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetShopHomeLayoutData(it.data)
                }
                is Fail -> {
                    onErrorGetShopHomeLayoutData(it.throwable)
                    stopPerformanceMonitor()
                }
            }
            stopMonitoringPltRenderPage()
        })

        viewModel?.initialProductListData?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    addProductListHeader()
                    updateProductListData(it.data.first, it.data.second, true)
                }
            }
            stopPerformanceMonitor()
        })

        viewModel?.newProductListData?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    updateProductListData(it.data.first, it.data.second, false)
                }
            }
        })

        viewModel?.checkWishlistData?.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessCheckWishlist(it.data)
                }
            }
        })

        viewModel?.reminderPlayLiveData?.observe(this, Observer {
            when(it.second){
                is Success -> {
                    showToastSuccess(
                            if((it.second as Success<Boolean>).data) getString(R.string.shop_page_play_card_success_add_reminder)
                            else getString(R.string.shop_page_play_card_success_remove_reminder)
                    )
                }
                is Fail -> {
                    adapter.notifyItemChanged(it.first, Bundle().apply { putBoolean(UPDATE_REMIND_ME_PLAY, true) })
                    showErrorToast(getString(R.string.shop_page_play_card_error_reminder))
                }
            }
        })

        viewModel?.updatePlayWidgetData?.observe(this, Observer {
            shopHomeAdapter.updatePlayWidget(it)
        })

        viewModel?.videoYoutube?.observe(this, Observer {
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
    }

    private fun addProductListHeader() {
        shopHomeAdapter.setEtalaseTitleData()
        val shopProductSortFilterUiModel = ShopProductSortFilterUiModel(
                selectedEtalaseId = "",
                selectedEtalaseName = "",
                selectedSortId = sortId,
                selectedSortName = sortName
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

        scrollView_globalError_shopPage.visible()
        globalError_shopPage.visible()
        recycler_view.hide()

        globalError_shopPage.setOnClickListener {
            loadInitialData()
        }
    }

    private fun updateProductListData(
            hasNextPage: Boolean,
            productList: List<ShopHomeProductViewModel>,
            isInitialData: Boolean
    ) {
        shopHomeAdapter.setProductListData(productList, isInitialData)
        updateScrollListenerState(hasNextPage)
    }

    private fun onSuccessGetShopHomeLayoutData(data: ShopPageHomeLayoutUiModel) {
        val listProductWidget = data.listWidget.filterIsInstance<ShopHomeCarousellProductUiModel>()
        viewModel?.getWishlistStatus(listProductWidget)
        shopHomeAdapter.setHomeLayoutData(data.listWidget)
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
            viewModel?.getNewProductList(shopId, sortId.toIntOrZero(), page)
        }
    }

    override fun loadData(page: Int) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (shopHomeAdapter.isLoading) {
                    return
                }
                val etalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_ID)
                val etalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_NAME)
                val isNeedToReloadData = data.getBooleanExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
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
                    sortId = data?.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    sortName = data?.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    shopPageHomeTracking.sortProduct(sortName, isOwner, customDimensionShopPage)
                    shopSortSharedViewModel?.changeSharedSortData(sortId, sortName)
                    shopHomeAdapter.changeSelectedSortFilter(sortId, sortName)
                    shopHomeAdapter.refreshSticky()
                    scrollToEtalaseTitlePosition()
                    refreshProductList()
                }
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
        if(requestCode == REQUEST_CODE_PLAY_ROOM) {
            if (data != null && data.hasExtra(EXTRA_TOTAL_VIEW) && data.hasExtra(EXTRA_CHANNEL_ID)) viewModel?.updatePlayWidgetData(data.getStringExtra(EXTRA_CHANNEL_ID), data.getStringExtra(EXTRA_TOTAL_VIEW))
        }
    }

    private fun scrollToEtalaseTitlePosition() {
        recycler_view?.smoothScrollBy(0, recyclerViewTopPadding * 2)
        staggeredGridLayoutManager?.scrollToPositionWithOffset(
                shopHomeAdapter.shopHomeEtalaseTitlePosition,
                stickySingleHeaderView.containerHeight
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

    override fun onPlayBannerCarouselRefresh(shopHomePlayCarouselUiModel: ShopHomePlayCarouselUiModel, position: Int) {
        viewModel?.onRefreshPlayBanner(shopId)
    }

    override fun onReminderClick(playBannerCarouselItemDataModel: PlayBannerCarouselItemDataModel, position: Int) {
        viewModel?.setToggleReminderPlayBanner(playBannerCarouselItemDataModel.channelId, playBannerCarouselItemDataModel.remindMe, position)
    }

    override fun onPlayBannerSeeMoreClick(appLink: String) {
        shopPageHomeTracking.clickSeeMorePlayCarouselBanner(shopId, viewModel?.userId ?: "")
        RouteManager.route(context, appLink)
    }

    override fun onPlayBannerImpressed(dataModel: PlayBannerCarouselItemDataModel, autoPlay: String, widgetId: String, widgetPosition: Int, position: Int) {
        shopPageHomeTracking.impressionPlayBanner(
                shopId = dataModel.partnerId,
                userId = viewModel?.userId ?: "",
                channelId = dataModel.channelId,
                bannerId = widgetId,
                creativeName = dataModel.coverUrl,
                autoPlay = autoPlay,
                positionWidget = widgetPosition + 1,
                positionChannel = position.toString()
        )
    }

    override fun onPlayBannerClicked(dataModel: PlayBannerCarouselItemDataModel, autoPlay: String, widgetId: String, widgetPosition: Int, position: Int) {
        shopPageHomeTracking.clickPlayBanner(
                shopId = dataModel.partnerId,
                userId = viewModel?.userId ?: "",
                channelId = dataModel.channelId,
                bannerId = widgetId,
                creativeName = dataModel.coverUrl,
                autoPlay = autoPlay,
                positionWidget = (widgetPosition + 1),
                positionChannel = position.toString()
        )
        RouteManager.route(context, dataModel.applink)
    }

    override fun onPlayLeftBannerImpressed(dataModel: PlayBannerCarouselOverlayImageDataModel, widgetId: String, position: Int) {
        shopPageHomeTracking.impressionLeftPlayBanner(
                shopId = shopId,
                userId = viewModel?.userId ?: "",
                bannerId = widgetId,
                creativeName = dataModel.imageUrl,
                positionChannel = "1",
                position = (position + 1).toString()
        )
    }

    override fun onPlayLeftBannerClicked(dataModel: PlayBannerCarouselOverlayImageDataModel, widgetId: String, position: Int) {
        shopPageHomeTracking.clickLeftPlayBanner(
                shopId = shopId,
                userId = viewModel?.userId ?: "",
                bannerId = widgetId,
                creativeName = dataModel.imageUrl,
                positionChannel = "1",
                position = (position + 1).toString()
        )
        RouteManager.route(context, dataModel.applink)
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

    private fun redirectToLoginPage() {
        RouteManager.route(context, ApplinkConst.LOGIN)
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
            bundle.putString(BUNDLE_SELECTED_ETALASE_ID, "")
            bundle.putBoolean(BUNDLE_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(BUNDLE_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(BUNDLE_SHOP_ID, shopId)
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
        sortId = ""
        sortName = ""
        shopSortSharedViewModel?.changeSharedSortData(sortId, sortName)
        shopHomeAdapter.changeSelectedSortFilter(sortId, sortName)
        shopHomeAdapter.refreshSticky()
        scrollToEtalaseTitlePosition()
        refreshProductList()
    }

    private fun refreshProductList() {
        shopHomeAdapter.removeProductList()
        shopHomeAdapter.showLoading()
        getProductList(1)
    }
}