package com.tokopedia.shop.product.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.ListTitleTypeDef
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel
import com.tokopedia.shop.product.di.component.DaggerShopProductComponent
import com.tokopedia.shop.product.di.module.ShopProductModule
import com.tokopedia.shop.product.view.adapter.EtalaseChipAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseListViewHolder
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductDedicatedListView
import com.tokopedia.shop.product.view.model.BaseShopProductViewModel
import com.tokopedia.shop.product.view.model.ShopProductEtalaseListViewModel
import com.tokopedia.shop.product.view.model.ShopProductViewModel
import com.tokopedia.shop.sort.view.activity.ShopProductSortActivity
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.wishlist.common.listener.WishListActionListener

import javax.inject.Inject

import com.tokopedia.shop.common.constant.ShopPageConstant.ETALASE_TO_SHOW
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.view.viewmodel.ShopProductListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_product_list_new.*

class ShopProductListFragment : BaseListFragment<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(),
        WishListActionListener, BaseEmptyViewHolder.Callback, ShopProductClickedListener,
        ShopProductEtalaseListViewHolder.OnShopProductEtalaseListViewHolderListener,
        EtalaseChipAdapter.OnEtalaseChipAdapterListener {

    private val shopProductAdapter: ShopProductAdapter by lazy { adapter as ShopProductAdapter }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopProductListViewModel

    internal var shopPageTracking: ShopPageTrackingBuyer? = null

    private var shopModuleRouter: ShopModuleRouter? = null

    private var shopId: String? = null
    private var keyword: String = ""
    private var prevAnalyticKeyword: String? = ""
    private var sortValue: String? = null
    private var attribution: String? = null

    private var selectedEtalaseList: ArrayList<ShopEtalaseViewModel>? = null

    private var recyclerView: RecyclerView? = null
    private var shopInfo: ShopInfo? = null

    private var selectedEtalaseId: String = ""
    private var selectedEtalaseName: String = ""

    private var remoteConfig: RemoteConfig? = null

    private var onShopProductListFragmentListener: OnShopProductListFragmentListener? = null
    private var needReloadData: Boolean = false
    private val etalaseChipAdapter: EtalaseChipAdapter by lazy {
        EtalaseChipAdapter(null, null, this)
    }
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false

    interface OnShopProductListFragmentListener {
        fun updateUIByShopName(shopName: String)
        fun updateUIByEtalaseName(etalaseName: String?)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.shopInfoResp.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> onErrorGetShopInfo(it.throwable)
            }
        })

        viewModel.etalaseResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetEtalaseList(it.data)
                is Fail -> onErrorGetEtalaseList(it.throwable)
            }
        })
        viewModel.productResponse.observe(this, Observer {
            when(it){
                is Success -> renderProductList(it.data.second, it.data.first)
                is Fail -> showGetListError(it.throwable)
            }
        })
    }

    override fun getAdapterTypeFactory(): ShopProductAdapterTypeFactory {
        return ShopProductAdapterTypeFactory(null,
                this, null, this,
                this, null,
                true, 0, ShopTrackProductTypeDef.PRODUCT
        )
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory> {
        return ShopProductAdapter(adapterTypeFactory)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_list_search
        if (TextUtils.isEmpty(keyword)) {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                emptyModel.title = getString(R.string.shop_product_empty_title_desc)
            } else {
                emptyModel.title = getString(R.string.shop_product_empty_title_etalase_desc)
            }
        } else {
            if (TextUtils.isEmpty(selectedEtalaseId)) {
                emptyModel.title = getString(R.string.shop_product_empty_product_title_no_etalase)
            } else {
                emptyModel.title = getString(R.string.shop_product_empty_product_title_etalase)
            }
        }
        if (TextUtils.isEmpty(selectedEtalaseId)) {
            emptyModel.content = getString(R.string.shop_product_empty_product_title_owner_no_etalase)
        } else {
            emptyModel.content = getString(R.string.shop_product_empty_product_title_owner_etalase)
        }
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
            }
        } else {
            selectedEtalaseList = savedInstanceState.getParcelableArrayList(SAVED_SELECTED_ETALASE_LIST)
            selectedEtalaseId = savedInstanceState.getString(SAVED_SELECTED_ETALASE_ID) ?: ""
            selectedEtalaseName = savedInstanceState.getString(SAVED_SELECTED_ETALASE_NAME) ?: ""
            keyword = savedInstanceState.getString(SAVED_KEYWORD) ?: ""
            sortValue = savedInstanceState.getString(SAVED_SORT_VALUE)
            shopId = savedInstanceState.getString(SAVED_SHOP_ID)
        }

        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
        context?.let { shopPageTracking = ShopPageTrackingBuyer(TrackingQueue(it)) }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopProductListViewModel::class.java)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.swipe_refresh_layout)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_product_list_new, container, false)
    }

    fun setUpEtalaseView(view: View) {
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_etalase.layoutManager = layoutManager
        val animator = recycler_view_etalase.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
        recycler_view_etalase.adapter = etalaseChipAdapter
        v_etalase_more.setOnClickListener { onEtalaseMoreListClicked() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(view)
        super.onViewCreated(view, savedInstanceState)
        setUpEtalaseView(view)
        bottom_action_view.setButton1OnClickListener {
            shopInfo?.let { shopInfo ->
                shopPageTracking?.clickSort( viewModel.isMyShop(shopInfo.shopCore.shopID),
                        CustomDimensionShopPage.create(shopInfo.shopCore.shopID,
                                shopInfo.goldOS.isOfficial == 1, shopInfo.goldOS.isGold == 1))
            }
            val intent = ShopProductSortActivity.createIntent(activity, sortValue)
            this@ShopProductListFragment.startActivityForResult(intent, REQUEST_CODE_SORT)
        }

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
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy < 0) { // going up
                        if (shopProductAdapter.shopProductViewModelList.size > 0) {
                            bottom_action_view.show()
                        }
                    } else if (dy > 0) { // going down
                        bottom_action_view.hide()
                    }
                }
            })
        }
    }


    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val gridLayoutManager = GridLayoutManager(activity, GRID_SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (shopProductAdapter.getItemViewType(position) == ShopProductViewHolder.GRID_LAYOUT) {
                    LIST_SPAN_COUNT
                } else {
                    GRID_SPAN_COUNT
                }
            }
        }
        return gridLayoutManager
    }

    override fun onSwipeRefresh() {
        hideEtalaseList()
        viewModel.etalaseResponse.value = null
        viewModel.clearEtalaseCache()
        etalaseChipAdapter.etalaseViewModelList = null
        shopProductAdapter.setShopEtalaseTitle(null, null)
        super.onSwipeRefresh()
    }

    fun updateDataByChangingKeyword(keyword: String) {
        if (remoteConfig?.getBoolean(RemoteConfigKey.SHOP_ETALASE_TOGGLE) == true) {
            if (keyword.isNotEmpty() && !this.keyword.equals(keyword, ignoreCase = true)) {
                selectedEtalaseId = ""
                etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId)
                etalaseChipAdapter.notifyDataSetChanged()
            }
        }

        if (!this.keyword.equals(keyword, ignoreCase = true)) {
            this.keyword = keyword
            loadInitialData()
        }
    }

    override fun onEtalaseChipClicked(shopEtalaseViewModel: ShopEtalaseViewModel) {
        if (shopProductAdapter.isLoading) {
            return
        }
        selectedEtalaseId = shopEtalaseViewModel.etalaseId
        selectedEtalaseName = shopEtalaseViewModel.etalaseName
        updateHintRemoteConfig(selectedEtalaseName)
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId)
        etalaseChipAdapter.notifyDataSetChanged()
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, shopEtalaseViewModel.etalaseBadge)
        shopInfo?.let {
            shopPageTracking?.clickEtalaseChip(viewModel.isMyShop(it.shopCore.shopID),
                    selectedEtalaseName, CustomDimensionShopPage.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                    it.goldOS.isGold == 1))
        }
        // no need ro rearraged, just notify the adapter to reload product list by etalase id

        loadInitialData()
    }

    private fun bindEtalaseChipData(shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel) {
        etalaseChipAdapter.etalaseViewModelList = shopProductEtalaseListViewModel.etalaseModelList
        val selectedEtalaseId = shopProductEtalaseListViewModel.selectedEtalaseId
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId)
        etalaseChipAdapter.notifyDataSetChanged()
        showEtalaseList()
    }

    // load product list first time
    public override fun loadInitialData() {
        bottom_action_view.gone()
        bottom_action_view.hide(false)
        isLoadingInitialData = true
        shopProductAdapter.clearProductList()
        shopProductAdapter.clearAllNonDataElement()
        showLoading()
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
            viewModel.getShopEtalase(shopInfo.shopCore.shopID)
        } else {
            // continue to load ProductData
            viewModel.getShopProduct(shopInfo.shopCore.shopID, page,
                    ShopPageConstant.DEFAULT_PER_PAGE,
                    sortValue.toIntOrZero(), selectedEtalaseId,
                    keyword)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_shop_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share) {
            onShareShop()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onShareShop() {
        if (activity != null && shopInfo != null){
            (activity!!.application as ShopModuleRouter).goToShareShop(activity, shopId, shopInfo!!.shopCore.url,
                    getString(R.string.shop_label_share_formatted,
                            shopInfo!!.shopCore.name, shopInfo!!.location))
        }
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerView?.layoutManager, shopProductAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    private fun renderProductList(list: List<ShopProductViewModel>, hasNextPage: Boolean) {
        shopInfo?.let {
            if (list.isNotEmpty()) {
                shopPageTracking?.impressionProductList(
                        viewModel.isMyShop(it.shopCore.shopID),
                        if (TextUtils.isEmpty(keyword)) ListTitleTypeDef.ETALASE else ListTitleTypeDef.SEARCH_RESULT,
                        selectedEtalaseName, CustomDimensionShopPageAttribution.create(it.shopCore.shopID,
                        it.goldOS.isOfficial == 1, it.goldOS.isGold == 1, "", attribution),
                        list, shopProductAdapter.shopProductViewModelList.size, shopId, it.shopCore.name
                )
            }
            if (!TextUtils.isEmpty(keyword) && prevAnalyticKeyword != keyword) {
                shopPageTracking?.searchKeyword(viewModel.isMyShop(it.shopCore.shopID),
                        keyword,
                        list.isNotEmpty(),
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
        shopProductAdapter.addProductList(list)
        updateScrollListenerState(hasNextPage)

        if (shopProductAdapter.shopProductViewModelList.size == 0) {
            bottom_action_view.gone()
            bottom_action_view.hide()
            shopProductAdapter.addElement(emptyDataViewModel)
        } else {
            if (isLoadingInitialData) {
                bottom_action_view.visible()
                bottom_action_view.show()
            }
            isLoadingInitialData = false
        }
        shopProductAdapter.notifyDataSetChanged()
    }

    override fun onItemClicked(baseShopProductViewModel: BaseShopProductViewModel) {
        // no op
    }

    override fun onEmptyContentItemTextClicked() {
        // no-op
    }

    override fun onEtalaseMoreListClicked() {
        shopInfo?.let {
            shopPageTracking?.clickMoreMenuChip(viewModel.isMyShop(it.shopCore.shopID),
                    CustomDimensionShopPage.create(it.shopCore.shopID,
                            it.goldOS.isOfficial == 1, it.goldOS.isGold == 1))

            activity?.let { activity ->
                val shopEtalaseIntent = ShopEtalasePickerActivity.createIntent(activity, it.shopCore.shopID, selectedEtalaseId,
                        true, false)
                startActivityForResult(shopEtalaseIntent, REQUEST_CODE_ETALASE)
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

    override fun onGetListErrorWithEmptyData(throwable: Throwable) {
        super.onGetListErrorWithEmptyData(throwable)
        bottom_action_view.hide(false)
    }

    override fun onProductClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int,
                                  productPosition: Int) {
        shopInfo?.let {
            // shopTrackType is always from product
            shopPageTracking?.clickProductPicture(
                    viewModel.isMyShop(it.shopCore.shopID),
                    if (TextUtils.isEmpty(keyword)) ListTitleTypeDef.ETALASE else ListTitleTypeDef.SEARCH_RESULT,
                    selectedEtalaseName,
                    CustomDimensionShopPageAttribution.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1, shopProductViewModel.id, attribution),
                    shopProductViewModel, productPosition, shopId, it.shopCore.name)
        }

        //attribution & shopPageTracking.getListNameOfProduct(ShopPageTrackingConstant.SEARCH, selectedEtalaseName)

        startActivity(getProductIntent(shopProductViewModel.id, attribution,
                shopPageTracking?.getListNameOfProduct(ShopPageTrackingConstant.SEARCH, selectedEtalaseName) ?: ""))
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
        shopProductAdapter.updateWishListStatus(productId, true)
    }

    override fun onErrorRemoveWishlist(errorMessage: String, productId: String) {
        NetworkErrorHelper.showCloseSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        shopProductAdapter.updateWishListStatus(productId, false)
    }

    override fun onErrorAddWishList(errorMessage: String, productId: String) {
        onErrorAddToWishList(MessageErrorException(errorMessage))
    }

    private fun onErrorAddToWishList(e: Throwable) {
        if (!viewModel.isLogin) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_USER_LOGIN)
            return
        }
        NetworkErrorHelper.showCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onEmptyButtonClicked() {
        RouteManager.route(activity, ApplinkConst.PRODUCT_ADD)
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        this.shopId = shopInfo.shopCore.shopID
        this.isOfficialStore = shopInfo.goldOS.isOfficial == 1
        this.isGoldMerchant = shopInfo.goldOS.isGold == 1
        onShopProductListFragmentListener?.updateUIByShopName(shopInfo.shopCore.name)
        loadInitialData()
    }

    private fun onErrorGetShopInfo(e: Throwable) {
        showGetListError(e)
    }

    private fun addToSelectedEtalaseList(etalaseId: String?, etalaseName: String?, useAce: Boolean,
                                         etalaseBadge: String?) {
        // only add the etalase with not-empty name. Empty name is a deleted etalase (that come from deeplink)
        if (TextUtils.isEmpty(etalaseName)) {
            return
        }
        // if etalase id is not on the list, add it
        val isAddedToCurrentEtalaseList = addEtalaseFromListMore(etalaseId, etalaseName, useAce)
        if (isAddedToCurrentEtalaseList) {
            selectedEtalaseList!!.add(0, ShopEtalaseViewModel(selectedEtalaseId, selectedEtalaseName, useAce,
                    ShopEtalaseTypeDef.ETALASE_CUSTOM, false))
            if (selectedEtalaseList!!.size > ShopPageConstant.MAXIMUM_SELECTED_ETALASE_LIST) {
                selectedEtalaseList!!.removeAt(selectedEtalaseList!!.size - 1)
            }
        }
        etalaseChipAdapter.setSelectedEtalaseId(selectedEtalaseId)
        etalaseChipAdapter.notifyDataSetChanged()
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge)
    }

    private fun addEtalaseToChip(etalaseId: String?, etalaseName: String?, useAce: Boolean) {
        // add the etalase by permutation
        // 1 2 3 4 5; after add 6 will be 1 6 2 3 4
        val shopEtalaseViewModelList = etalaseChipAdapter.etalaseViewModelList
        val shopEtalaseViewModelToAdd = ShopEtalaseViewModel(etalaseId, etalaseName, useAce,
                ShopEtalaseTypeDef.ETALASE_CUSTOM, false)
        // index no 0 will always be "All Etalase", so, add from index 1.
        val indexToAdd = if (shopEtalaseViewModelList.size > 1) 1 else 0
        shopEtalaseViewModelList.add(indexToAdd, shopEtalaseViewModelToAdd)
        if (shopEtalaseViewModelList.size > ETALASE_TO_SHOW) {
            shopEtalaseViewModelList.removeAt(shopEtalaseViewModelList.size - 1)
        }
    }

    private fun isEtalaseInChip(etalaseId: String?): Boolean =
        etalaseChipAdapter.etalaseViewModelList.find{ it.etalaseId.equals(etalaseId, ignoreCase = true) } != null


    /**
     * @return true, if add etalase to current list; false if no add needed.
     */
    private fun addEtalaseFromListMore(etalaseId: String?, etalaseName: String?, useAce: Boolean): Boolean {
        if (isEtalaseInChip(etalaseId)) {
            return false
        }
        addEtalaseToChip(etalaseId, etalaseName, useAce)
        return true
    }

    override fun onWishListClicked(shopProductViewModel: ShopProductViewModel, @ShopTrackProductTypeDef shopTrackType: Int) {
        shopInfo?.let {
            //shopTrackType is always from Product
            shopPageTracking?.clickWishlist(!shopProductViewModel.isWishList,
                    if (TextUtils.isEmpty(keyword)) ListTitleTypeDef.ETALASE else ListTitleTypeDef.SEARCH_RESULT,
                    selectedEtalaseName,
                    CustomDimensionShopPageProduct.create(it.shopCore.shopID, it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1, shopProductViewModel.id))
        }
        if (!viewModel.isLogin){
            onErrorAddToWishList(UserNotLoginException())
            return
        }
        if (shopProductViewModel.isWishList) {
            viewModel.removeWishList(shopProductViewModel.id, this)
        } else {
            viewModel.addWishList(shopProductViewModel.id, this)
        }
    }

    private fun onErrorGetEtalaseList(e: Throwable) {
        // etalase load is error
        etalaseChipAdapter.etalaseViewModelList = null
        hideEtalaseList()
        shopProductAdapter.setShopEtalaseTitle(null, null)
        // assume use ace is true, to continue load product.
        shopInfo?.let { loadShopPageList(it, defaultInitialPage, true) }

    }

    private fun onSuccessGetEtalaseList(shopEtalaseViewModelList: List<ShopEtalaseViewModel>) {
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
        val shopEtalaseModelListToShow: List<ShopEtalaseViewModel>
        if (shopEtalaseViewModelList.size > ETALASE_TO_SHOW) {
            shopEtalaseModelListToShow = shopEtalaseViewModelList.subList(0, ETALASE_TO_SHOW)
        } else {
            shopEtalaseModelListToShow = shopEtalaseViewModelList
        }
        // update the adapter
        bindEtalaseChipData(ShopProductEtalaseListViewModel(shopEtalaseModelListToShow, selectedEtalaseId))
        addToSelectedEtalaseList(selectedEtalaseId, selectedEtalaseName, true, etalaseBadge)
        shopProductAdapter.setShopEtalaseTitle(selectedEtalaseName, etalaseBadge)

        // continue to load ProductData
        shopInfo?.let { loadShopPageList(it, defaultInitialPage)}
    }

    private fun cleanString(text: String): String {
        return text.replace("[\\W_]".toRegex(), "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                selectedEtalaseId = data!!.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                val useAce = data.getBooleanExtra(ShopParamConstant.EXTRA_USE_ACE, true)
                val etalaseBadge = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_BADGE)

                addToSelectedEtalaseList(selectedEtalaseId, selectedEtalaseName, useAce, etalaseBadge)
                shopInfo?.let {
                    shopPageTracking?.clickMenuFromMoreMenu(viewModel.isMyShop(it.shopCore.shopID),
                    selectedEtalaseName, CustomDimensionShopPage.create(it.shopCore.shopID, isOfficialStore, isGoldMerchant))
                }
                needReloadData = true
            }

            REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                val sortId = data!!.getStringExtra(ShopProductSortActivity.SORT_ID)
                sortValue = data.getStringExtra(ShopProductSortActivity.SORT_NAME)
                this.isLoadingInitialData = true
                loadInitialData()
                shopInfo?.let {
                    shopPageTracking?.clickSortBy(viewModel.isMyShop(it.shopCore.shopID),
                            sortValue, CustomDimensionShopPage.create(it.shopCore.shopID, isOfficialStore, isGoldMerchant))
                }
            }
            else -> {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
        viewModel.etalaseResponse.removeObservers(this)
        viewModel.shopInfoResp.removeObservers(this)
        viewModel.productResponse.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopProductListFragmentListener = context as OnShopProductListFragmentListener
        shopModuleRouter = context.applicationContext as ShopModuleRouter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_SELECTED_ETALASE_LIST, selectedEtalaseList)
        outState.putString(SAVED_SELECTED_ETALASE_ID, selectedEtalaseId)
        outState.putString(SAVED_SELECTED_ETALASE_NAME, selectedEtalaseName)
        outState.putString(SAVED_SORT_VALUE, sortValue)
        outState.putString(SAVED_KEYWORD, keyword)
        outState.putString(SAVED_SHOP_ID, shopId)
        outState.putBoolean(SAVED_SHOP_IS_OFFICIAL, isOfficialStore)
        outState.putBoolean(SAVED_SHOP_IS_GOLD_MERCHANT, isGoldMerchant)
    }

    private fun updateHintRemoteConfig(selectedEtalaseName: String?) {
        if (remoteConfig?.getBoolean(RemoteConfigKey.SHOP_ETALASE_TOGGLE) != true) {
            updateHint(selectedEtalaseName)
        }
    }

    private fun updateHint(selectedEtalaseName: String?) {
        onShopProductListFragmentListener?.updateUIByEtalaseName(selectedEtalaseName)

    }

    companion object {

        private val REQUEST_CODE_USER_LOGIN = 100
        private val REQUEST_CODE_ETALASE = 200
        private val REQUEST_CODE_SORT = 300

        private val LIST_SPAN_COUNT = 1
        private val GRID_SPAN_COUNT = 2

        val SAVED_SELECTED_ETALASE_LIST = "saved_etalase_list"
        val SAVED_SELECTED_ETALASE_ID = "saved_etalase_id"
        val SAVED_SELECTED_ETALASE_NAME = "saved_etalase_name"
        val SAVED_SHOP_ID = "saved_shop_id"
        val SAVED_SHOP_IS_OFFICIAL = "saved_shop_is_official"
        val SAVED_SHOP_IS_GOLD_MERCHANT = "saved_shop_is_gold_merchant"
        val SAVED_KEYWORD = "saved_keyword"
        val SAVED_SORT_VALUE = "saved_sort_name"

        @JvmStatic
        fun createInstance(shopId: String,
                           keyword: String?,
                           etalaseId: String?,
                           sort: String?,
                           attribution: String?): ShopProductListFragment = ShopProductListFragment().also {
                it.arguments = Bundle().apply {
                    putString(ShopParamConstant.EXTRA_SHOP_ID, shopId)
                    putString(ShopParamConstant.EXTRA_PRODUCT_KEYWORD, keyword ?: "")
                    putString(ShopParamConstant.EXTRA_ETALASE_ID, etalaseId ?: "")
                    putString(ShopParamConstant.EXTRA_SORT_ID, sort ?: "")
                    putString(ShopParamConstant.EXTRA_ATTRIBUTION, attribution ?: "")
                }
            }
    }


}
