package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.Util
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeVoucherViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_page_home.*
import javax.inject.Inject

class ShopPageHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>(),
        ShopHomeDisplayWidgetListener,
        ShopHomeVoucherViewHolder.OnMerchantVoucherListWidgetListener,
        ShopPageHomeProductClickListener {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        const val KEY_SHOP_NAME = "SHOP_NAME"
        const val KEY_SHOP_ATTRIBUTION = "SHOP_ATTRIBUTION"


        const val SPAN_COUNT = 2

        fun createInstance(
                shopId: String,
                isOfficialStore: Boolean,
                isGoldMerchant: Boolean,
                shopName: String,
                shopAttribution: String
        ): Fragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            bundle.putString(KEY_SHOP_NAME, shopName)
            bundle.putString(KEY_SHOP_ATTRIBUTION, shopAttribution)
            return ShopPageHomeFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var shopPageHomeTracking: ShopPageHomeTracking
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopHomeViewModel
    private var shopId: String = ""
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var shopName: String = ""
    private var shopAttribution: String = ""
    private val shopPageHomeLayoutUiModel: ShopPageHomeLayoutUiModel?
        get() = (viewModel.shopHomeLayoutData.value as? Success)?.data
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }

    private val shopHomeAdapter by lazy {
        adapter as ShopHomeAdapter
    }

    private val shopHomeAdapterTypeFactory by lazy {
        ShopHomeAdapterTypeFactory(this, this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_page_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view)?.apply {
            layoutManager = recyclerViewLayoutManager
        }
        getRecyclerView(view)?.let {
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
        observeLiveData()
    }

    override fun onPause() {
        super.onPause()
        shopPageHomeTracking.sendAllTrackingQueue()
    }

    override fun loadInitialData() {
        showLoading()
        viewModel.getShopPageHomeData(shopId)
    }

    private fun getIntentData() {
        arguments?.let {
            shopId = it.getString(KEY_SHOP_ID, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL_STORE, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
            shopName = it.getString(KEY_SHOP_NAME, "")
            shopAttribution = it.getString(KEY_SHOP_ATTRIBUTION, "")
        }
    }

    private fun observeLiveData() {
        viewModel.shopHomeLayoutData.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetShopHomeLayoutData(it.data)
                }
                is Fail -> {
                    onErrorGetShopHomeLayoutData(it.throwable)
                }
            }
        })

        viewModel.productListData.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetProductListData(it.data.first, it.data.second)
                }
            }
        })

        viewModel.checkWishlistData.observe(this, Observer {
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
        shopPageHomeTracking.addToCart(
                isOwner,
                dataModelAtc.cartId.toString(),
                shopAttribution,
                isLogin,
                shopPageHomeLayoutUiModel?.layoutId ?: "",
                shopHomeProductViewModel?.name ?: "",
                shopHomeProductViewModel?.id ?: "",
                shopHomeProductViewModel?.displayedPrice ?: "",
                dataModelAtc.quantity,
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
        recycler_view.visible()
        shopHomeAdapter.hideLoading()
        scrollView_globalError_shopPage.hide()
        globalError_shopPage.hide()
        val listProductWidget = data.listWidget.filterIsInstance<ShopHomeCarousellProductUiModel>()
        viewModel.getWishlistStatus(listProductWidget)
        shopHomeAdapter.setHomeLayoutData(data.listWidget)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory> {
        return ShopHomeAdapter(shopHomeAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory() = shopHomeAdapterTypeFactory

    private val gridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return gridLayoutManager
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerViewLayoutManager, shopHomeAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopHomeAdapter.showLoading()
                loadNextData(page)
            }
        }
    }

    fun loadNextData(page: Int) {
        if (shopId.isNotEmpty()) {
            viewModel.getNextProductList(shopId, page)
        }
    }

    override fun loadData(page: Int) {}

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
                shopPageHomeLayoutUiModel?.layoutId ?: "",
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
                shopPageHomeLayoutUiModel?.layoutId ?: "",
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

    override val isOwner: Boolean
        get() = Util.isMyShop(shopId, viewModel.userSessionShopId)

    val isLogin: Boolean
        get() = viewModel.isLogin

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {}

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        shopPageHomeTracking.clickDetailMerchantVoucher(isOwner, customDimensionShopPage)
        context?.let {
            val intentMerchantVoucherDetail = MerchantVoucherDetailActivity.createIntent(
                    it,
                    merchantVoucherViewModel.voucherId,
                    merchantVoucherViewModel, shopId
            )
            startActivity(intentMerchantVoucherDetail)
        }
    }

    override fun onSeeAllClicked() {
        shopPageHomeTracking.clickSeeAllMerchantVoucher(isOwner, customDimensionShopPage)
        context?.let {
            val intentMerchantVoucherList = MerchantVoucherListActivity.createIntent(
                    it,
                    shopId,
                    shopName
            )
            startActivity(intentMerchantVoucherList)
        }
    }

    override fun onVoucherListImpression(listVoucher: List<MerchantVoucherViewModel>) {
        shopPageHomeTracking.onImpressionVoucherList(isOwner, listVoucher, customDimensionShopPage)
    }

    override fun onAllProductItemClicked(itemPosition: Int, shopHomeProductViewModel: ShopHomeProductViewModel?) {
        val realItemPositonOnTheList = itemPosition - shopHomeAdapter.getAllProductWidgetPosition()
        shopHomeProductViewModel?.let {
            shopPageHomeTracking.clickProduct(
                    isOwner,
                    isLogin,
                    shopPageHomeLayoutUiModel?.layoutId ?: "",
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
                    shopPageHomeLayoutUiModel?.layoutId ?: "",
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

    override fun onAllProductItemWishlist(itemPosition: Int, shopHomeProductViewModel: ShopHomeProductViewModel) {
        if (isLogin) {
            viewModel.clearGetShopProductUseCase()
            if (shopHomeProductViewModel.isWishList) {
                viewModel.removeWishList(
                        shopHomeProductViewModel.id ?: "",
                        { onSuccessRemoveWishList(null, shopHomeProductViewModel) },
                        ::onErrorRemoveWishList
                )
            } else {
                viewModel.addWishList(
                        shopHomeProductViewModel.id ?: "",
                        { onSuccessAddWishlist(null, shopHomeProductViewModel) },
                        ::onErrorAddWishlist)
            }
        } else {
            redirectToLoginPage()
        }
    }

    override fun onCarouselProductItemClicked(parentPosition: Int, itemPosition: Int, shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?, shopHomeProductViewModel: ShopHomeProductViewModel?) {
        shopHomeProductViewModel?.let {
            shopPageHomeTracking.clickProduct(
                    isOwner,
                    isLogin,
                    shopPageHomeLayoutUiModel?.layoutId ?: "",
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
                shopPageHomeLayoutUiModel?.layoutId ?: "",
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

    override fun onCarouselProductItemWishlist(
            parentPosition: Int,
            itemPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel?,
            shopHomeProductViewModel: ShopHomeProductViewModel?
    ) {
        if (isLogin) {
            viewModel.clearGetShopProductUseCase()
            if (shopHomeProductViewModel?.isWishList == true) {
                viewModel.removeWishList(
                        shopHomeProductViewModel.id ?: "",
                        { onSuccessRemoveWishList(shopHomeCarousellProductUiModel, shopHomeProductViewModel) },
                        ::onErrorRemoveWishList
                )
            } else {
                viewModel.addWishList(
                        shopHomeProductViewModel?.id ?: "",
                        { onSuccessAddWishlist(shopHomeCarousellProductUiModel, shopHomeProductViewModel) },
                        ::onErrorAddWishlist
                )
            }
        } else {
            redirectToLoginPage()
        }
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
        shopPageHomeTracking.clickWishlist(
                isOwner,
                isWishlist,
                shopPageHomeLayoutUiModel?.layoutId ?: "",
                isLogin,
                shopHomeCarousellProductUiModel?.name ?: "",
                shopHomeCarousellProductUiModel?.widgetId ?: "",
                shopHomeProductViewModel.id ?: "",
                customDimensionShopPage
        )
    }

    override fun onCtaClicked(shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel?) {

        shopPageHomeTracking.clickCta(
                layoutId = shopPageHomeLayoutUiModel?.layoutId.toString(),
                widgetName = shopHomeCarouselProductUiModel?.name.toString(),
                widgetId = shopHomeCarouselProductUiModel?.widgetId.toString(),
                appLink = shopHomeCarouselProductUiModel?.header?.ctaLink.toString(),
                shopId = shopId,
                shopType = customDimensionShopPage.shopType,
                isOwner = isOwner
        )

        context?.let {
            RouteManager.route(it, shopHomeCarouselProductUiModel?.header?.ctaText)
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
            viewModel.addProductToCart(
                    shopHomeProductViewModel?.id ?: "",
                    shopId,
                    {
                        onSuccessAddToCart(it, shopHomeProductViewModel, parentPosition, shopHomeCarousellProductUiModel)
                    },
                    {
                        onErrorAddToCart(it)
                    }
            )
        } else {
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

    fun clearCache() {
        viewModel.clearCache()
    }
}