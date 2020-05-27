package com.tokopedia.shop.home.view.fragment

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
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_PLT_NETWORK_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_PLT_PREPARE_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_PLT_RENDER_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_TRACE
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHomeTabPerformanceMonitoringListener
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_page_home.*
import javax.inject.Inject

class ShopPageHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>(),
        ShopHomeDisplayWidgetListener,
        ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener,
        ShopPageHomeProductClickListener {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        const val KEY_SHOP_NAME = "SHOP_NAME"
        const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"
        const val KEY_SHOP_REF = "SHOP_REF"
        const val SPAN_COUNT = 2

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
        ShopHomeAdapterTypeFactory(this, this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPltMonitoring()
        getIntentData()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
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
        observeLiveData()
    }

    override fun onPause() {
        shopPageHomeTracking.sendAllTrackingQueue()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel?.productListData?.removeObservers(this)
        viewModel?.shopHomeLayoutData?.removeObservers(this)
        viewModel?.checkWishlistData?.removeObservers(this)
        viewModel?.flush()
        super.onDestroy()
    }

    override fun loadInitialData() {
        shopHomeAdapter.clearAllElements()
        recycler_view.visible()
        scrollView_globalError_shopPage.hide()
        globalError_shopPage.hide()
        showLoading()
        shopHomeAdapter.isOwner = isOwner
        startMonitoringPltNetworkRequest()
        viewModel?.getShopPageHomeData(shopId)
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

        viewModel?.productListData?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetProductListData(it.data.first, it.data.second)
                }
            }
            stopPerformanceMonitor()
        })

        viewModel?.checkWishlistData?.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessCheckWishlist(it.data)
                }
            }
        })
    }

    private fun onSuccessCheckWishlist(data: List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>>) {
        data.onEach {
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

    private fun onSuccessGetProductListData(hasNextPage: Boolean, productList: List<ShopHomeProductViewModel>) {
        shopHomeAdapter.hideLoading()
        if (shopHomeAdapter.endlessDataSize == 0) {
            shopHomeAdapter.setEtalaseTitleData()
        }
        shopHomeAdapter.setProductListData(productList)
        updateScrollListenerState(hasNextPage)
    }

    private fun onSuccessGetShopHomeLayoutData(data: ShopPageHomeLayoutUiModel) {
        shopHomeAdapter.hideLoading()
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
                    .shopComponent(ShopComponentInstance.getComponent(application))
                    .build()
                    .inject(this@ShopPageHomeFragment)
            }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(staggeredGridLayoutManager, shopHomeAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopHomeAdapter.showLoading()
                loadNextData(page)
            }
        }
    }

    fun loadNextData(page: Int) {
        if (shopId.isNotEmpty()) {
            viewModel?.getNextProductList(shopId, page)
        }
    }

    override fun loadData(page: Int) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })
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
                    customDimensionShopPage
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
                    customDimensionShopPage
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
                    customDimensionShopPage
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
                customDimensionShopPage
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
            showToastSuccess(getString(R.string.msg_success_remove_wishlist))
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
            showToastSuccess(getString(R.string.msg_success_add_wishlist))
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
}