package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.EMPTY_PRODUCT_SEARCH_IMAGE_URL
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_IS_SHOW_DEFAULT
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_IS_SHOW_ZERO_PRODUCT
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_SELECTED_ETALASE_ID
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_SHOP_ID
import com.tokopedia.shop.product.view.listener.OnShopProductListFragmentListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductEmptySearchListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListResultViewModel
import com.tokopedia.shop.search.view.activity.ShopSearchProductActivity.Companion.createIntent
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import javax.inject.Inject

class ShopPageProductListResultFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        WishListActionListener, BaseEmptyViewHolder.Callback, ShopProductClickedListener,
        ShopProductSortFilterViewHolder.ShopProductEtalaseChipListViewHolderListener,
        ShopProductImpressionListener, ShopProductEmptySearchListener {

    interface ShopPageProductListResultFragmentListener {
        fun onSortValueUpdated(sortValue: String)
        fun updateShopInfo(shopInfo: ShopInfo)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopPageProductListResultViewModel

    private var shopPageTracking: ShopPageTrackingBuyer? = null
    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }
    private var shopId: String? = null
    private var shopRef: String = ""
    private var keyword: String = ""
    private var sortValue: String = ""
    private var attribution: String? = null
    private var selectedEtalaseList: ArrayList<ShopEtalaseItemDataModel>? = null
    private var isNeedToReloadData: Boolean = false
    private var recyclerView: RecyclerView? = null
    private var shopInfo: ShopInfo? = null
    private var selectedEtalaseId: String = ""
    private var selectedEtalaseName: String = ""
    private var defaultEtalaseName = ""
    private var selectedEtalaseType: Int = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
    private var onShopProductListFragmentListener: OnShopProductListFragmentListener? = null
    private var shopPageProductListResultFragmentListener: ShopPageProductListResultFragmentListener? = null
    private var needReloadData: Boolean = false
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private var threeDotsClickShopProductUiModel: ShopProductViewModel? = null

    private var shopProductSortFilterUiModel: ShopProductSortFilterUiModel? = null
    private var keywordEmptyState = ""
    private var isEmptyState = false

    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private val isMyShop: Boolean
        get() = if (::viewModel.isInitialized) {
            shopId?.let { viewModel.isMyShop(it) } ?: false
        } else false
    private val isLogin: Boolean
        get() = if (::viewModel.isInitialized) {
            viewModel.isLogin
        } else false

    override fun getAdapterTypeFactory(): ShopProductAdapterTypeFactory {
        return ShopProductAdapterTypeFactory(
                null,
                this,
                this,
                null,
                this,
                this,
                null,
                null,
                null,
                this,
                true,
                0,
                ShopTrackProductTypeDef.PRODUCT
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {
        return ShopProductAdapter(adapterTypeFactory)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.urlRes = EMPTY_PRODUCT_SEARCH_IMAGE_URL
        if (TextUtils.isEmpty(keyword)) {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                emptyModel.title = getString(R.string.shop_product_empty_title_desc)
            } else {
                emptyModel.title = getString(R.string.shop_product_empty_title_etalase_desc)
            }
        } else {
            emptyModel.title = getString(R.string.shop_product_empty_product_title)
        }
        emptyModel.content = getString(R.string.shop_product_empty_product_content)
        return emptyModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let { attribution = it.getString(ShopParamConstant.EXTRA_ATTRIBUTION, "") }
        if (savedInstanceState == null) {
            selectedEtalaseList = ArrayList()
            arguments?.let {
                selectedEtalaseId = it.getString(ShopParamConstant.EXTRA_ETALASE_ID, "")
                selectedEtalaseName = ""
                keyword = it.getString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, "")
                sortValue = it.getString(ShopParamConstant.EXTRA_SORT_ID, Integer.MIN_VALUE.toString())
                shopId = it.getString(ShopParamConstant.EXTRA_SHOP_ID, "")
                shopRef = it.getString(ShopParamConstant.EXTRA_SHOP_REF, "")
                isNeedToReloadData = it.getBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA)
            }
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST)
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            selectedEtalaseType = savedInstanceState.getInt(SAVED_SELECTED_ETALASE_TYPE, SELECTED_ETALASE_TYPE_DEFAULT_VALUE)
            keyword = savedInstanceState.getString(SAVED_KEYWORD) ?: ""
            sortValue = savedInstanceState.getString(SAVED_SORT_VALUE, "")
            shopId = savedInstanceState.getString(SAVED_SHOP_ID)
            shopRef = savedInstanceState.getString(SAVED_SHOP_REF).orEmpty()
            needReloadData = savedInstanceState.getBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA)
        }
        shopPageProductListResultFragmentListener?.onSortValueUpdated(sortValue ?: "")
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        context?.let {
            shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it))
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageProductListResultViewModel::class.java)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.swipe_refresh_layout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_product_list_result_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            (it as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        observeLiveData()
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return staggeredGridLayoutManager
    }

    override fun onSwipeRefresh() {
        viewModel.clearCache()
        super.onSwipeRefresh()
    }

    public override fun loadInitialData() {
        isLoadingInitialData = true
        shopProductAdapter.clearProductList()
        shopProductAdapter.clearAllNonDataElement()
        showLoading()
        if (isNeedToReloadData) {
            viewModel.clearCache()
        }
        loadData(defaultInitialPage)
    }

    override fun loadData(page: Int) {
        shopInfo?.let {
            viewModel.getShopFilterData(
                    it,
                    isMyShop
            )
        } ?: viewModel.getShop(shopId)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerView?.layoutManager, shopProductAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopProductAdapter.showLoading()
                shopInfo?.let {
                    loadProductData(it, page)
                }
            }
        }
    }

    private fun loadProductData(shopInfo: ShopInfo, page: Int) {
        viewModel.getShopProduct(
                shopInfo.shopCore.shopID,
                page,
                ShopPageConstant.DEFAULT_PER_PAGE,
                sortValue.toIntOrZero(),
                selectedEtalaseId,
                keyword,
                isNeedToReloadData,
                selectedEtalaseType
        )
    }

    private fun loadProductDataEmptyState(shopInfo: ShopInfo, page: Int) {
        selectedEtalaseId = ""
        sortValue = SORT_NEWEST

        viewModel.getShopProductEmptyState(
                shopInfo.shopCore.shopID,
                page,
                ShopPageConstant.SHOP_PRODUCT_EMPTY_STATE_LIMIT,
                sortValue.toIntOrZero(),
                selectedEtalaseId,
                keywordEmptyState
        )
    }

    private fun initRecyclerView(view: View) {
        recyclerView = super.getRecyclerView(view)
        recyclerView?.let {
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
    }

    private fun observeLiveData() {
        viewModel.shopInfoResp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> onErrorGetShopInfo(it.throwable)
            }
        })

        viewModel.shopSortFilterData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetSortFilterData(it.data)
                is Fail -> showGetListError(it.throwable)
            }
        })
        viewModel.productData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val productList = it.data.second
                    renderProductList(productList, it.data.first)
                    isNeedToReloadData = false
                }
                is Fail -> showGetListError(it.throwable)
            }
        })
        viewModel.productDataEmpty.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> renderProductListEmptyState(it.data)
                is Fail -> showGetListError(it.throwable)
            }
        })
    }

    private fun renderProductList(productList: List<ShopProductViewModel>, hasNextPage: Boolean) {
        hideLoading()
        shopProductAdapter.clearAllNonDataElement()
        if (isLoadingInitialData) {
            shopProductAdapter.clearProductList()
            endlessRecyclerViewScrollListener.resetState()

            if (productList.isNotEmpty()) {
                shopProductSortFilterUiModel?.let { shopProductAdapter.setSortFilterData(it) }
            }
        }

        if (productList.isEmpty() && isLoadingInitialData) {
            showLoading()
            shopInfo?.let { loadProductDataEmptyState(it, defaultInitialPage) }
            isEmptyState = true
        } else {
            shopProductAdapter.setProductListDataModel(productList)
            updateScrollListenerState(hasNextPage)
            isLoadingInitialData = false
        }
    }

    private fun renderProductListEmptyState(productList: List<ShopProductViewModel>) {
        hideLoading()
        shopProductAdapter.clearAllElements()
        shopProductAdapter.addEmptyStateData(productList)
    }

    override fun onItemClicked(baseShopProductViewModel: BaseShopProductViewModel) {
        // no op
    }

    override fun onEmptyContentItemTextClicked() {
        // no-op
    }

    override fun showGetListError(throwable: Throwable) {
        hideLoading()
        updateStateScrollListener()
        if (shopProductAdapter.shopProductViewModelList.size > 0) {
            onGetListErrorWithExistingData(throwable)
        } else {
            onGetListErrorWithEmptyData(throwable)
        }
    }

    override fun onProductClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int,
                                  productPosition: Int) {
        if (!isEmptyState) {
            shopPageTracking?.clickProductSearchResult(
                    isMyShop,
                    isLogin,
                    getSelectedEtalaseChip(),
                    "",
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.isUpcoming,
                    keyword

            )
        } else {
            shopPageTracking?.clickProductListEmptyState(
                    isLogin,
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId
            )
        }
        startActivity(getProductIntent(shopProductViewModel.id ?: "", attribution,
                shopPageTracking?.getListNameOfProduct(OldShopPageTrackingConstant.SEARCH, getSelectedEtalaseChip())
                        ?: ""))
    }

    override fun onProductImpression(shopProductViewModel: ShopProductViewModel, shopTrackType: Int, productPosition: Int) {
        if (!isEmptyState) {
            shopPageTracking?.impressionProductListSearchResult(
                    isMyShop,
                    isLogin,
                    getSelectedEtalaseChip(),
                    "",
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId,
                    shopProductViewModel.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                    shopProductViewModel.isUpcoming,
                    keyword
            )
        } else {
            shopPageTracking?.impressionProductListEmptyState(
                    isLogin,
                    CustomDimensionShopPageAttribution.create(
                            shopInfo?.shopCore?.shopID,
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            shopProductViewModel.id,
                            attribution,
                            shopRef
                    ),
                    shopProductViewModel,
                    productPosition + 1,
                    shopId
            )
        }
    }

    private fun getProductIntent(productId: String, attribution: String?, listNameOfProduct: String): Intent? {
        return if (context != null) {
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    override fun onSuccessAddWishlist(productId: String) {
        showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist))
        shopProductAdapter.updateWishListStatus(productId, true)
    }

    override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        showToastSuccess(getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist))
        shopProductAdapter.updateWishListStatus(productId, false)
    }

    override fun onErrorAddWishList(errorMessage: String, productId: String) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
    }

    private fun onErrorAddToWishList(e: Throwable) {
        if (!viewModel.isLogin) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
        }
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onEmptyButtonClicked() {
        redirectToSearchResultPage()
    }

    private fun redirectToSearchResultPage() {
        RouteManager.route(
                context,
                "${ApplinkConst.DISCOVERY_SEARCH}?q=$keyword"
        )
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            Toaster.make(findViewById(android.R.id.content), message)
        }
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.shopId = shopInfo.shopCore.shopID
        this.isOfficialStore = shopInfo.goldOS.isOfficial == 1
        this.isGoldMerchant = shopInfo.goldOS.isGold == 1
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)
        onShopProductListFragmentListener?.updateUIByShopName(shopInfo.shopCore.name)
        shopPageProductListResultFragmentListener?.updateShopInfo(shopInfo)
        loadData(defaultInitialPage)
    }

    private fun onErrorGetShopInfo(e: Throwable) {
        showGetListError(e)
    }

    override fun onThreeDotsClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int) {
        threeDotsClickShopProductUiModel = shopProductViewModel
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = shopProductViewModel.isWishList,
                        productId = shopProductViewModel.id ?: ""
                )
        )
    }

    private fun onSuccessGetSortFilterData(shopStickySortFilter: ShopStickySortFilter) {
        val etalaseList = shopStickySortFilter.etalaseList
        defaultEtalaseName = etalaseList.firstOrNull()?.etalaseName.orEmpty()
        selectedEtalaseId = shopStickySortFilter.etalaseList.firstOrNull { isEtalaseMatch(it) }?.etalaseId
                ?: ""
        sortValue = shopStickySortFilter.sortList.firstOrNull { it.value == sortValue }?.value ?: ""
        selectedEtalaseName = etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.etalaseName
                ?: ""
        selectedEtalaseType = etalaseList.firstOrNull { it.etalaseId == selectedEtalaseId }?.type
                ?: -1
        val selectedSortName = shopStickySortFilter.sortList.firstOrNull { it.value == sortValue }?.name
                ?: ""
        shopProductSortFilterUiModel = ShopProductSortFilterUiModel(
                selectedEtalaseId = selectedEtalaseId.takeIf { it.isNotEmpty() } ?: "",
                selectedEtalaseName = selectedEtalaseName.takeIf { it.isNotEmpty() } ?: "",
                selectedSortId = sortValue,
                selectedSortName = selectedSortName,
                isShowSortFilter = selectedEtalaseType != ShopEtalaseTypeDef.ETALASE_CAMPAIGN
        )
        if (!isEmptyState) {
            viewModel.getShopProduct(
                    shopId ?: "",
                    defaultInitialPage,
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    sortValue.toIntOrZero(),
                    selectedEtalaseId,
                    keyword,
                    isNeedToReloadData,
                    selectedEtalaseType
            )
        } else {
            hideLoading()
            endlessRecyclerViewScrollListener.resetState()
        }
    }

    private fun isEtalaseMatch(model: ShopEtalaseItemDataModel): Boolean {
        return (model.etalaseId.toLowerCase() == selectedEtalaseId.toLowerCase() ||
                model.etalaseName.toLowerCase() == selectedEtalaseId.toLowerCase() ||
                model.alias.toLowerCase() == selectedEtalaseId.toLowerCase()) && selectedEtalaseId.isNotEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                data?.let {
                    selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_ID)
                    selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_NAME)
                    selectedEtalaseType = data.getIntExtra(ShopParamConstant.EXTRA_ETALASE_PICKER_ETALASE_TYPE, SELECTED_ETALASE_TYPE_DEFAULT_VALUE)
                    needReloadData = data.getBooleanExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
                    shopPageTracking?.clickMoreMenuChip(
                            isMyShop,
                            getSelectedEtalaseChip(),
                            customDimensionShopPage
                    )
                    shopInfo?.let {
                        shopPageTracking?.clickMenuFromMoreMenu(viewModel.isMyShop(it.shopCore.shopID),
                                selectedEtalaseName, CustomDimensionShopPage.create(it.shopCore.shopID, isOfficialStore, isGoldMerchant))
                    }
                    shopProductAdapter.changeSelectedEtalaseFilter(selectedEtalaseId, selectedEtalaseName)
                    shopProductAdapter.refreshSticky()
                    if (needReloadData) {
                        loadInitialData()
                        needReloadData = false
                    }
                }
            }

            REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                if (shopProductAdapter.isLoading) {
                    return
                }
                data?.let {
                    sortValue = it.getStringExtra(ShopProductSortActivity.SORT_VALUE) ?: ""
                    val sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    shopPageTracking?.sortProduct(sortName, isMyShop, customDimensionShopPage)
                    this.isLoadingInitialData = true
                    shopProductAdapter.changeSelectedSortFilter(sortValue ?: "", sortName)
                    shopProductAdapter.refreshSticky()
                    loadInitialData()
                }
            }
            else -> {
            }
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object : ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            onErrorAddToWishList(UserNotLoginException())
        } else {
            shopInfo?.let {
                //shopTrackType is always from Product
                shopPageTracking?.clickWishlistProductResultPage(
                        !productCardOptionsModel.isWishlisted,
                        isLogin,
                        getSelectedEtalaseChip(),
                        CustomDimensionShopPageProduct.create(
                                it.shopCore.shopID,
                                it.goldOS.isOfficial == 1,
                                it.goldOS.isGold == 1, productCardOptionsModel.productId, shopRef
                        ),
                        threeDotsClickShopProductUiModel?.etalaseType == ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
                        threeDotsClickShopProductUiModel?.isUpcoming ?: false
                )
            }

            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }
        threeDotsClickShopProductUiModel = null
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        viewModel.clearGetShopProductUseCase()

        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleWishlistActionAddToWishlist(productCardOptionsModel)
        } else {
            handleWishlistActionRemoveFromWishlist(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionAddToWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessAddWishlist(productCardOptionsModel.productId)
        } else {
            onErrorAddWishList(getString(com.tokopedia.wishlist.common.R.string.msg_error_add_wishlist), productCardOptionsModel.productId)
        }
    }

    private fun handleWishlistActionRemoveFromWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessRemoveWishlist(productCardOptionsModel.productId)
        } else {
            onErrorRemoveWishlist(getString(com.tokopedia.wishlist.common.R.string.msg_error_remove_wishlist), productCardOptionsModel.productId)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (needReloadData) {
//            loadInitialData()
//            needReloadData = false
//        }
//    }

    override fun onPause() {
        super.onPause()
        shopPageTracking?.sendAllTrackingQueue()
    }

    override fun initInjector() {
        DaggerShopProductComponent
                .builder()
                .shopProductModule(ShopProductModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onDestroy() {
        viewModel.shopSortFilterData.removeObservers(this)
        viewModel.shopInfoResp.removeObservers(this)
        viewModel.productData.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopProductListFragmentListener = context as OnShopProductListFragmentListener
        shopPageProductListResultFragmentListener = context as ShopPageProductListResultFragmentListener

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList)
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId)
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName)
        outState.putInt(SAVED_SELECTED_ETALASE_TYPE, selectedEtalaseType)
        outState.putString(SAVED_SORT_VALUE, sortValue)
        outState.putString(SAVED_KEYWORD, keyword)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putString(SAVED_SHOP_REF, shopRef)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
    }

    private fun redirectToEtalasePicker() {
        context?.let {
            val bundle = Bundle()

//            bundle.putString(BUNDLE_SELECTED_ETALASE_ID, selectedEtalaseId)
//            bundle.putBoolean(BUNDLE_IS_SHOW_DEFAULT, true)
//            bundle.putBoolean(BUNDLE_IS_SHOW_ZERO_PRODUCT, false)
//            bundle.putString(BUNDLE_SHOP_ID, shopInfo!!.shopCore.shopID)

            bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopInfo!!.shopCore.shopID)

            val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
            intent.putExtra(BUNDLE, bundle)
            startActivityForResult(intent, REQUEST_CODE_ETALASE)
        }
    }

    private fun redirectToShopSortPickerPage() {
        context?.run {
            val intent = ShopProductSortActivity.createIntent(activity, sortValue)
            startActivityForResult(intent, REQUEST_CODE_SORT)
        }
    }

    private fun getSelectedEtalaseChip(): String {
        return selectedEtalaseName.takeIf { it.isNotEmpty() } ?: defaultEtalaseName
    }

    companion object {

        private val REQUEST_CODE_USER_LOGIN = 100
        private val REQUEST_CODE_ETALASE = 200

        @JvmStatic
        val REQUEST_CODE_SORT = 300

        private val GRID_SPAN_COUNT = 2
        private const val SORT_NEWEST = "1"

        val SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list"
        val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        val SAVED_SELECTED_ETALASE_TYPE = "saved_etalase_type"
        val SAVED_SHOP_ID = "saved_shop_id"
        val SAVED_SHOP_REF = "saved_shop_ref"
        val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        val SAVED_KEYWORD = "saved_keyword"
        val SAVED_SORT_VALUE = "saved_sort_name"
        val BUNDLE = "bundle"

        private const val SELECTED_ETALASE_TYPE_DEFAULT_VALUE = -10

        @JvmStatic
        fun createInstance(shopId: String,
                           shopRef: String?,
                           keyword: String?,
                           etalaseId: String?,
                           sort: String?,
                           attribution: String?,
                           isNeedToReloadData: Boolean? = false
        ): ShopPageProductListResultFragment = ShopPageProductListResultFragment().also {
            it.arguments = Bundle().apply {
                putString(ShopParamConstant.EXTRA_SHOP_ID, shopId)
                putString(ShopParamConstant.EXTRA_SHOP_REF, shopRef.orEmpty())
                putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword ?: "")
                putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId ?: "")
                putString(ShopParamConstant.EXTRA_SORT_ID, sort ?: "")
                putString(ShopParamConstant.EXTRA_ATTRIBUTION, attribution ?: "")
                putBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData
                        ?: false)
            }
        }
    }

    override fun onEtalaseFilterClicked() {
        shopPageTracking?.clickEtalaseChip(
                isMyShop,
                getSelectedEtalaseChip(),
                CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
        )
        redirectToEtalasePicker()
    }

    override fun onSortFilterClicked() {
        shopPageTracking?.clickSort(isMyShop, customDimensionShopPage)
        redirectToShopSortPickerPage()
    }

    override fun onClearFilterClicked() {
        if (shopProductAdapter.isLoading) {
            return
        }
        shopPageTracking?.clickClearFilter(
                isMyShop,
                customDimensionShopPage
        )
        sortValue = ""
        val sortName = ""
        shopProductAdapter.changeSelectedSortFilter(sortValue ?: "", sortName)
        selectedEtalaseId = ""
        selectedEtalaseName = ""
        selectedEtalaseType = SELECTED_ETALASE_TYPE_DEFAULT_VALUE
        shopProductAdapter.changeSelectedEtalaseFilter(selectedEtalaseId, selectedEtalaseName)
        shopProductAdapter.refreshSticky()
        loadInitialData()
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onPrimaryButtonEmptyClicked() {
        shopPageTracking?.clickPrimaryBtnEmptyStateSearch(shopId, shopInfo?.shopHomeType)
        context?.let {
            startActivity(
                    createIntent(
                            it,
                            shopId.orEmpty(),
                            shopInfo?.shopCore?.name.orEmpty(),
                            shopInfo?.goldOS?.isOfficial == 1,
                            shopInfo?.goldOS?.isGold == 1,
                            keyword,
                            attribution,
                            shopRef
                    )
            )
        }
    }

    override fun onSecondaryButtonEmptyClicked() {
        shopPageTracking?.clickSecondaryBtnEmptyStateSearch(shopId, shopInfo?.shopHomeType)
        RouteManager.route(
                context,
                "${ApplinkConst.DISCOVERY_SEARCH}?q=$keyword"
        )
    }
}
