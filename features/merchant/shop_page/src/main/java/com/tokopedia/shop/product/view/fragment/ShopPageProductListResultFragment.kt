package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.view.*
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant
import com.tokopedia.shop.analytic.model.*
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.EMPTY_PRODUCT_SEARCH_IMAGE_URL
import com.tokopedia.shop.common.constant.ShopPageConstant.ETALASE_TO_SHOW
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.ShopProductEtalaseAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductEtalaseAdapterTypeFactory
import com.tokopedia.shop.product.view.datamodel.BaseShopProductViewModel
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseChipItemViewModel
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseListViewModel
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.ShopProductEtalaseListViewHolder
import com.tokopedia.shop.product.view.viewmodel.ShopPageProductListResultViewModel
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_IS_SHOW_DEFAULT
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_IS_SHOW_ZERO_PRODUCT
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_SELECTED_ETALASE_ID
import com.tokopedia.shop.product.view.fragment.ShopPageProductListFragment.Companion.BUNDLE_SHOP_ID
import com.tokopedia.shop.product.view.listener.OnShopProductListFragmentListener
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import kotlinx.android.synthetic.main.fragment_shop_product_list_result_new.*
import javax.inject.Inject

class ShopPageProductListResultFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        WishListActionListener, BaseEmptyViewHolder.Callback, ShopProductClickedListener,
        ShopProductEtalaseListViewHolder.ShopProductEtalaseChipListViewHolderListener,
        ShopProductImpressionListener {

    interface ShopPageProductListResultFragmentListener {
        fun onSortValueUpdated(sortValue: String)
        fun updateShopInfo(shopInfo: ShopInfo)
    }

    override fun onEtalaseChipClicked(shopProductEtalaseChipItemViewModel: ShopProductEtalaseChipItemViewModel) {
        if (shopProductAdapter.isLoading) {
            return
        }
        shopProductEtalaseAdapter.selectedEtalaseId = shopProductEtalaseChipItemViewModel.etalaseId
        shopProductEtalaseAdapter.notifyDataSetChanged()
        selectedEtalaseId = shopProductEtalaseChipItemViewModel.etalaseId
        selectedEtalaseName = shopProductEtalaseChipItemViewModel.etalaseName
        updateHintRemoteConfig(selectedEtalaseName)
        if (shopInfo != null) {
            shopId = shopInfo!!.shopCore.shopID
            shopPageTracking?.clickEtalaseChip(
                    viewModel.isMyShop(shopId!!),
                    selectedEtalaseName,
                    CustomDimensionShopPage.create(shopId,
                            shopInfo!!.goldOS.isOfficial == 1, shopInfo!!.goldOS.isGold == 1))
        }
        loadInitialData()
    }

    override fun onAddEtalaseChipClicked() {}

    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopPageProductListResultViewModel

    internal var shopPageTracking: ShopPageTrackingBuyer? = null

    private var shopId: String? = null
    private var shopRef: String = ""
    private var keyword: String = ""
    private var prevAnalyticKeyword: String? = ""
    private var sortValue: String? = null
    private var attribution: String? = null

    private var selectedEtalaseList: ArrayList<ShopProductEtalaseChipItemViewModel>? = null
    private var isNeedToReloadData: Boolean = false

    private var recyclerView: RecyclerView? = null
    private var shopInfo: ShopInfo? = null

    private var selectedEtalaseId: String = ""
    private var selectedEtalaseName: String = ""

    private var remoteConfig: RemoteConfig? = null

    private var onShopProductListFragmentListener: OnShopProductListFragmentListener? = null
    private var shopPageProductListResultFragmentListener: ShopPageProductListResultFragmentListener? = null
    private var needReloadData: Boolean = false
    private val shopProductEtalaseAdapter: ShopProductEtalaseAdapter by lazy {
        ShopProductEtalaseAdapter(ShopProductEtalaseAdapterTypeFactory(this))
    }
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.shopInfoResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> onErrorGetShopInfo(it.throwable)
            }
        })

        viewModel.etalaseListData.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetEtalaseList(it.data)
                is Fail -> onErrorGetEtalaseList(it.throwable)
            }
        })
        viewModel.productResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val productList = it.data.second
                    shopPageTracking?.searchProduct(keyword, productList.isEmpty(), isMyShop, customDimensionShopPage)
                    renderProductList(productList, it.data.first)
                    isNeedToReloadData = false
                }
                is Fail -> showGetListError(it.throwable)
            }
        })
    }

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
        remoteConfig = FirebaseRemoteConfigImpl(context)
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
            keyword = savedInstanceState.getString(SAVED_KEYWORD) ?: ""
            sortValue = savedInstanceState.getString(SAVED_SORT_VALUE)
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

    fun setUpEtalaseView(view: View) {
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_etalase.layoutManager = layoutManager
        val animator = recycler_view_etalase.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        recycler_view_etalase.adapter = shopProductEtalaseAdapter
        v_etalase_more.setOnClickListener { onEtalaseMoreListClicked() }
        recycler_view_etalase.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dx > X_SCROLL_OFFSET) {
                    if (!v_etalase_more.isVisible) {
                        val anim = TranslateAnimation(
                                v_etalase_more.height.toFloat(), 0f, 0f, 0f
                        )
                        anim.duration = 100
                        anim.fillAfter = true
                        v_etalase_more.startAnimation(anim)
                        v_etalase_more.show()
                    }
                } else if (dx <= -X_SCROLL_OFFSET) {
                    if (v_etalase_more.isVisible) {
                        val anim = TranslateAnimation(
                                0f, v_etalase_more.height.toFloat(), 0f, 0f
                        )
                        anim.duration = 100
                        anim.fillAfter = true
                        v_etalase_more.startAnimation(anim)
                        v_etalase_more.hide()
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            (it as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        setUpEtalaseView(view)
    }

    private fun showEtalaseList() {
        vg_etalase_list.visible()
    }

    private fun hideEtalaseList() {
        vg_etalase_list.gone()
    }

    private fun initRecyclerView(view: View) {
        recyclerView = super.getRecyclerView(view)
        recyclerView?.let {
            val animator = it.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = (recyclerView.layoutManager as? StaggeredGridLayoutManager)
                val firstCompletelyVisibleItem = linearLayoutManager?.findFirstCompletelyVisibleItemPositions(null)?.get(0)
                        ?: 0
                app_bar_layout_etalase_list.background = if (firstCompletelyVisibleItem != 0) {
                    MethodChecker.getDrawable(context, R.drawable.card_shadow_bottom)
                } else {
                    null
                }
            }
        })
    }


    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return staggeredGridLayoutManager
    }

    override fun onSwipeRefresh() {
        hideEtalaseList()
        viewModel.etalaseListData.value = null
        viewModel.clearCache()
        shopProductEtalaseAdapter.clearAllElements()
        super.onSwipeRefresh()
    }

    private fun bindEtalaseChipData(shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel) {
        shopProductEtalaseAdapter.setElements(shopProductEtalaseListViewModel.etalaseModelList)
        val selectedEtalaseId = shopProductEtalaseListViewModel.selectedEtalaseId
        shopProductEtalaseAdapter.selectedEtalaseId = selectedEtalaseId
        shopProductEtalaseAdapter.notifyDataSetChanged()
        showEtalaseList()
    }

    // load product list first time
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
        if (shopInfo == null) {
            viewModel.getShop(shopId)
        } else {
            loadShopPageList(shopInfo!!, page)
        }
    }

    private fun loadShopPageList(shopInfo: ShopInfo, page: Int, forceNoEtalase: Boolean = false) {
        if (viewModel.isEtalaseEmpty && !forceNoEtalase) {
            viewModel.getEtalaseData(shopInfo.shopCore.shopID, isMyShop, isNeedToReloadData)
        } else {
            // continue to load ProductData
            viewModel.getShopProduct(shopInfo.shopCore.shopID, page,
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    sortValue.toIntOrZero(), selectedEtalaseId,
                    keyword, isNeedToReloadData)
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerView?.layoutManager, shopProductAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopProductAdapter.showLoading()
                loadData(page)
            }
        }
    }

    private fun renderProductList(productList: List<ShopProductViewModel>, hasNextPage: Boolean) {
        shopInfo?.let {
            if (!TextUtils.isEmpty(keyword) && prevAnalyticKeyword != keyword) {
                shopPageTracking?.searchKeyword(viewModel.isMyShop(it.shopCore.shopID),
                        keyword,
                        productList.isNotEmpty(),
                        CustomDimensionShopPage.create(it.shopCore.shopID,
                                it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
                prevAnalyticKeyword = keyword
            }
        }

        hideLoading()
        shopProductAdapter.clearAllNonDataElement()
        if (isLoadingInitialData) {
            shopProductAdapter.clearProductList()
            endlessRecyclerViewScrollListener.resetState()
        }
        shopProductAdapter.setProductListDataModel(productList)
        updateScrollListenerState(hasNextPage)

        if (shopProductAdapter.shopProductViewModelList.size == 0) {
            shopProductAdapter.addEmptyDataModel(emptyDataViewModel)
        } else {
            isLoadingInitialData = false
        }
    }

    override fun onItemClicked(baseShopProductViewModel: BaseShopProductViewModel) {
        // no op
    }

    override fun onEmptyContentItemTextClicked() {
        // no-op
    }

    override fun onEtalaseMoreListClicked() {
        shopInfo?.let {
            shopPageTracking?.clickMoreMenuChip(
                    viewModel.isMyShop(it.shopCore.shopID),
                    selectedEtalaseName,
                    CustomDimensionShopPage.create(it.shopCore.shopID,
                            it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))
            context?.let { context ->
                val bundle = Bundle()
                bundle.putString(BUNDLE_SELECTED_ETALASE_ID, selectedEtalaseId)
                bundle.putBoolean(BUNDLE_IS_SHOW_DEFAULT, true)
                bundle.putBoolean(BUNDLE_IS_SHOW_ZERO_PRODUCT, false)
                bundle.putString(BUNDLE_SHOP_ID, it.shopCore.shopID)
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                intent.putExtra(BUNDLE, bundle)
                startActivity(intent)
            }
        }
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
        shopPageTracking?.clickProductSearchResult(
                isMyShop,
                isLogin,
                selectedEtalaseName,
                "",
                CustomDimensionShopPageAttribution.create(
                        shopInfo!!.shopCore.shopID,
                        shopInfo!!.goldOS.isOfficial == 1,
                        shopInfo!!.goldOS.isGold == 1,
                        shopProductViewModel.id,
                        attribution,
                        shopRef
                ),
                shopProductViewModel,
                productPosition + 1,
                shopId

        )
        startActivity(getProductIntent(shopProductViewModel.id ?: "", attribution,
                shopPageTracking?.getListNameOfProduct(OldShopPageTrackingConstant.SEARCH, selectedEtalaseName)
                        ?: ""))
    }

    override fun onProductImpression(shopProductViewModel: ShopProductViewModel, shopTrackType: Int, productPosition: Int) {
        shopPageTracking?.impressionProductListSearchResult(
                isMyShop,
                isLogin,
                selectedEtalaseName,
                "",
                CustomDimensionShopPageAttribution.create(
                        shopInfo!!.shopCore.shopID,
                        shopInfo!!.goldOS.isOfficial == 1,
                        shopInfo!!.goldOS.isGold == 1,
                        shopProductViewModel.id,
                        attribution,
                        shopRef
                ),
                shopProductViewModel,
                productPosition + 1,
                shopId

        )
    }

    private fun getProductIntent(productId: String, attribution: String?, listNameOfProduct: String): Intent? {
        if (context != null) {
            val bundle = Bundle()
            bundle.putString("tracker_attribution", attribution)
            bundle.putString("tracker_list_name", listNameOfProduct)
            return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            return null
        }
    }

    override fun onSuccessAddWishlist(productId: String) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        shopProductAdapter.updateWishListStatus(productId, true)
    }

    override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
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

    fun clickSortButton() {
        shopInfo?.let {
            shopPageTracking?.clickSort(isMyShop,customDimensionShopPage)
            openShopProductSortPage()
        }
    }

    private fun openShopProductSortPage() {
        context?.let {
            val intent = ShopProductSortActivity.createIntent(it, sortValue)
            startActivityForResult(intent, ShopPageProductListResultFragment.REQUEST_CODE_SORT)
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
        loadInitialData()
    }

    private fun onErrorGetShopInfo(e: Throwable) {
        showGetListError(e)
    }

    private fun addToSelectedEtalaseList(etalaseId: String, etalaseName: String, useAce: Boolean,
                                         etalaseBadge: String?) {
        // only add the etalase with not-empty name. Empty name is a deleted etalase (that come from deeplink)
        if (TextUtils.isEmpty(etalaseName)) {
            return
        }
        // if etalase id is not on the list, add it
        val isAddedToCurrentEtalaseList = addEtalaseFromListMore(etalaseId, etalaseName, useAce)
        if (isAddedToCurrentEtalaseList) {
            selectedEtalaseList!!.add(0, ShopProductEtalaseChipItemViewModel(selectedEtalaseId, selectedEtalaseName,
                    ShopEtalaseTypeDef.ETALASE_CUSTOM, highlighted = false))
            if (selectedEtalaseList!!.size > ShopPageConstant.MAXIMUM_SELECTED_ETALASE_LIST) {
                selectedEtalaseList!!.removeAt(selectedEtalaseList!!.size - 1)
            }
        }
        shopProductEtalaseAdapter.selectedEtalaseId = selectedEtalaseId
        shopProductEtalaseAdapter.notifyDataSetChanged()
    }

    private fun addEtalaseToChip(etalaseId: String, etalaseName: String, useAce: Boolean) {
        // add the etalase by permutation
        // 1 2 3 4 5; after add 6 will be 1 6 2 3 4
        val shopEtalaseViewModelList = shopProductEtalaseAdapter.data
        val shopEtalaseViewModelToAdd = ShopProductEtalaseChipItemViewModel(etalaseId, etalaseName,
                ShopEtalaseTypeDef.ETALASE_CUSTOM, highlighted = false)
        // index no 0 will always be "All Etalase", so, add from index 1.
        val indexToAdd = if (shopEtalaseViewModelList.size > 1) 1 else 0
        shopEtalaseViewModelList.add(indexToAdd, shopEtalaseViewModelToAdd)
        if (shopEtalaseViewModelList.size > ETALASE_TO_SHOW) {
            shopEtalaseViewModelList.removeAt(shopEtalaseViewModelList.size - 1)
        }
    }

    private fun isEtalaseInChip(etalaseId: String?): Boolean =
            shopProductEtalaseAdapter.data.filterIsInstance<ShopProductEtalaseChipItemViewModel>().find { it.etalaseId.equals(etalaseId, ignoreCase = true) } != null


    /**
     * @return true, if add etalase to current list; false if no add needed.
     */
    private fun addEtalaseFromListMore(etalaseId: String, etalaseName: String, useAce: Boolean): Boolean {
        if (isEtalaseInChip(etalaseId)) {
            return false
        }
        addEtalaseToChip(etalaseId, etalaseName, useAce)
        return true
    }

    override fun onThreeDotsClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int) {
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = shopProductViewModel.isWishList,
                        productId = shopProductViewModel.id ?: ""
                )
        )
    }

    private fun onErrorGetEtalaseList(e: Throwable) {
        // etalase load is error
        shopProductEtalaseAdapter.clearAllElements()
        hideEtalaseList()
        // assume use ace is true, to continue load product.
        shopInfo?.let { loadShopPageList(it, defaultInitialPage, true) }

    }

    private fun onSuccessGetEtalaseList(shopEtalaseViewModelList: List<ShopProductEtalaseChipItemViewModel>) {
        var etalaseBadge: String? = null

        if (shopEtalaseViewModelList.isEmpty()) {
            selectedEtalaseId = ""
            selectedEtalaseName = ""
        } else {
            // id might come from deeplink
            if (!TextUtils.isEmpty(selectedEtalaseId)) {
                for (etalaseModel in shopEtalaseViewModelList) {
                    if (selectedEtalaseId.equals(etalaseModel.etalaseId, ignoreCase = true)) {
                        selectedEtalaseName = etalaseModel.etalaseName
                        etalaseBadge = etalaseModel.etalaseBadge
                        updateHintRemoteConfig(selectedEtalaseName)
                        break
                    }
                }
                // etalase name still empty, then we check the selectedEtalaseId with name.
                if (TextUtils.isEmpty(selectedEtalaseName)) {
                    val cleanedSelectedEtalaseId = cleanString(selectedEtalaseId)
                    for (etalaseModel in shopEtalaseViewModelList) {
                        val cleanedEtalaseName = cleanString(etalaseModel.etalaseName)
                        if (cleanedSelectedEtalaseId.equals(cleanedEtalaseName, ignoreCase = true)) {
                            selectedEtalaseId = etalaseModel.etalaseId
                            selectedEtalaseName = etalaseModel.etalaseName
                            updateHintRemoteConfig(selectedEtalaseName)
                            etalaseBadge = etalaseModel.etalaseBadge
                            break
                        }
                    }
                    // name is empty means etalase is deleted, so no need to add to chip, and make it to all etalase.
                    if (TextUtils.isEmpty(selectedEtalaseName)) {
                        selectedEtalaseId = ""
                    }
                }
            }

            // if id not exist, set default to index 0
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                val firstModel = shopEtalaseViewModelList[0]
                selectedEtalaseId = firstModel.etalaseId
                selectedEtalaseName = firstModel.etalaseName
                updateHintRemoteConfig(selectedEtalaseName)
                etalaseBadge = firstModel.etalaseBadge
            }
        }

        /// limit etalase to show in chip
        val shopEtalaseModelListToShow: List<ShopProductEtalaseChipItemViewModel>
        if (shopEtalaseViewModelList.size > ETALASE_TO_SHOW) {
            shopEtalaseModelListToShow = shopEtalaseViewModelList.subList(0, ETALASE_TO_SHOW).toMutableList()
        } else {
            shopEtalaseModelListToShow = shopEtalaseViewModelList
        }
        // update the adapter
        bindEtalaseChipData(ShopProductEtalaseListViewModel(shopEtalaseModelListToShow, selectedEtalaseId))
        addToSelectedEtalaseList(selectedEtalaseId, selectedEtalaseName, true, etalaseBadge)

        // continue to load ProductData
        shopInfo?.let { loadShopPageList(it, defaultInitialPage) }
    }

    private fun cleanString(text: String): String {
        return text.replace("[\\W_]".toRegex(), "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    selectedEtalaseId = it.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                    selectedEtalaseName = it.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                    shopPageTracking?.clickMoreMenuChip(
                            isMyShop,
                            selectedEtalaseName,
                            customDimensionShopPage
                    )
                    val useAce = it.getBooleanExtra(ShopParamConstant.EXTRA_USE_ACE, true)
                    val etalaseBadge = it.getStringExtra(ShopParamConstant.EXTRA_ETALASE_BADGE)

                    addToSelectedEtalaseList(selectedEtalaseId, selectedEtalaseName, useAce, etalaseBadge)
                    shopInfo?.let {
                        shopPageTracking?.clickMenuFromMoreMenu(viewModel.isMyShop(it.shopCore.shopID),
                                selectedEtalaseName, CustomDimensionShopPage.create(it.shopCore.shopID, isOfficialStore, isGoldMerchant))
                    }
                    needReloadData = true
                }
            }

            REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    sortValue = it.getStringExtra(ShopProductSortActivity.SORT_VALUE)
                    val sortName = data.getStringExtra(ShopProductSortActivity.SORT_NAME) ?: ""
                    shopPageTracking?.sortProduct(sortName, isMyShop, customDimensionShopPage)
                    shopPageProductListResultFragmentListener?.onSortValueUpdated(sortValue ?: "")
                    this.isLoadingInitialData = true
                    loadInitialData()
                }
            }
            else -> {
            }
        }

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object: ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        shopInfo?.let {
            //shopTrackType is always from Product
            shopPageTracking?.clickWishlistProductResultPage(
                    !productCardOptionsModel.isWishlisted,
                    isLogin,
                    selectedEtalaseName,
                    CustomDimensionShopPageProduct.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1, productCardOptionsModel.productId, shopRef))
        }

        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            onErrorAddToWishList(UserNotLoginException())
        }
        else {
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        viewModel.clearGetShopProductUseCase()

        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleWishlistActionAddToWishlist(productCardOptionsModel)
        }
        else {
            handleWishlistActionRemoveFromWishlist(productCardOptionsModel)
        }
    }

    private fun handleWishlistActionAddToWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessAddWishlist(productCardOptionsModel.productId)
        }
        else {
            onErrorAddWishList(getString(com.tokopedia.wishlist.common.R.string.msg_error_add_wishlist), productCardOptionsModel.productId)
        }
    }

    private fun handleWishlistActionRemoveFromWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessRemoveWishlist(productCardOptionsModel.productId)
        }
        else {
            onErrorRemoveWishlist(getString(com.tokopedia.wishlist.common.R.string.msg_error_remove_wishlist), productCardOptionsModel.productId)
        }
    }

    override fun onResume() {
        super.onResume()
        if (needReloadData) {
            loadInitialData()
            needReloadData = false
        }
    }

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
        viewModel.etalaseListData.removeObservers(this)
        viewModel.shopInfoResp.removeObservers(this)
        viewModel.productResponse.removeObservers(this)
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
        outState.putString(SAVED_SORT_VALUE, sortValue)
        outState.putString(SAVED_KEYWORD, keyword)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putString(SAVED_SHOP_REF, shopRef)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
    }

    private fun updateHintRemoteConfig(selectedEtalaseName: String?) {
        updateHint(selectedEtalaseName)
    }

    private fun updateHint(selectedEtalaseName: String?) {
        onShopProductListFragmentListener?.updateUIByEtalaseName(selectedEtalaseName)

    }

    companion object {

        private val REQUEST_CODE_USER_LOGIN = 100
        private val REQUEST_CODE_ETALASE = 200
        private const val X_SCROLL_OFFSET = 15

        @JvmStatic
        val REQUEST_CODE_SORT = 300

        private val LIST_SPAN_COUNT = 1
        private val GRID_SPAN_COUNT = 2

        val SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list"
        val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        val SAVED_SHOP_ID = "saved_shop_id"
        val SAVED_SHOP_REF = "saved_shop_ref"
        val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        val SAVED_KEYWORD = "saved_keyword"
        val SAVED_SORT_VALUE = "saved_sort_name"
        val SAVED_RELOAD_STATE = "saved_reload_state"
        val BUNDLE = "bundle"

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
                putBoolean(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData ?: false)
            }
        }
    }


}
