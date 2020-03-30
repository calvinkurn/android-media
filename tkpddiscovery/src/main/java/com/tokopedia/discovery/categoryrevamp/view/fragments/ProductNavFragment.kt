package com.tokopedia.discovery.categoryrevamp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.tkpd.library.utils.URLParser
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.*
import com.tokopedia.discovery.categoryrevamp.analytics.CategoryPageAnalytics.Companion.catAnalyticsInstance
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.utils.CategoryApiParamBuilder.Companion.categoryApiParamBuilder
import com.tokopedia.discovery.categoryrevamp.view.activity.CategoryNavActivity
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SelectedFilterListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SubCategoryListener
import com.tokopedia.discovery.categoryrevamp.viewmodel.ProductNavViewModel
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.android.synthetic.main.fragment_product_nav.*
import kotlinx.android.synthetic.main.layout_nav_no_product.*
import rx.Subscriber
import javax.inject.Inject

private const val REQUEST_ACTIVITY_SORT_PRODUCT = 102
private const val REQUEST_ACTIVITY_FILTER_PRODUCT = 103

open class ProductNavFragment : BaseBannedProductFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener,
        ProductCardListener,
        SubCategoryListener,
        WishListActionListener,
        SelectedFilterListener {

    private var isSubCategoryAvailable = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var productNavViewModel: ProductNavViewModel

    @Inject
    lateinit var removeWishlistActionUseCase: RemoveWishListUseCase

    @Inject
    lateinit var addWishlistActionUseCase: AddWishListUseCase

    @Inject
    lateinit var topAdsWishlishedUseCase: TopAdsWishlishedUseCase

    lateinit var userSession: UserSession

    private lateinit var gcmHandler: GCMHandler

    private lateinit var productTypeFactory: ProductTypeFactory

    private var subCategoryAdapter: SubCategoryAdapter? = null

    private var quickFilterAdapter: QuickFilterAdapter? = null

    private lateinit var categoryNavComponent: CategoryNavComponent

    private var productNavListAdapter: ProductNavListAdapter? = null

    private var list: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()

    private var quickFilterList = ArrayList<Filter>()
    private var mDepartmentId: String = ""
    private var mDepartmentName: String = ""

    private var pageCount = 0
    private var isPagingAllowed: Boolean = true

    private var selectedFilterAdapter: SelectedFilterAdapter? = null

    private var mSelectedFilter = HashMap<String, String>()
    private var categoryUrl: String? = null

    companion object {
        private const val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"
        private const val EXTRA_PARENT_ID = " PARENT_ID"
        private const val EXTRA_PARENT_NAME = " PARENT_NAME"
        private const val EXTRA_CATEGORY_URL = "CATEGORY_URL"

        @JvmStatic
        fun newInstance(departmentId: String, departmentName: String, categoryUrl: String?): Fragment {
            val fragment = ProductNavFragment()
            val bundle = Bundle()
            if (categoryUrl != null) {
                bundle.putString(EXTRA_CATEGORY_URL, categoryUrl)
            }
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_ID, departmentId)
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_NAME, departmentName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        categoryNavComponent.inject(this)
    }

    override fun getDataFromArguments() {
        arguments?.let {
            if (it.containsKey(EXTRA_CATEGORY_URL)) {
                categoryUrl = it.getString(EXTRA_CATEGORY_URL, "")
                mSelectedFilter = URLParser(it.getString(EXTRA_CATEGORY_URL, "")).paramKeyValueMap
            }
            mDepartmentId = it.getString(EXTRA_CATEGORY_DEPARTMENT_ID, "")
            mDepartmentName = it.getString(EXTRA_CATEGORY_DEPARTMENT_NAME, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_nav, container, false)
    }

    override fun addFragmentView() {
        view?.findViewById<View>(R.id.swipe_refresh_layout)?.show()
        view?.findViewById<View>(R.id.layout_banned_screen)?.hide()
    }

    override fun hideFragmentView() {
        view?.findViewById<View>(R.id.swipe_refresh_layout)?.hide()
    }

    override fun initFragmentView() {
        initView()
        setUpData()
        observeData()
        setUpAdapter()
        setUpNavigation()
        if (userVisibleHint) {
            setUpVisibleFragmentListener()
        }
        initSelectedFilterRecyclerView()
    }

    private fun initSelectedFilterRecyclerView() {
        selectedFilterAdapter = SelectedFilterAdapter(this)
        selectedFilterRecyclerView.adapter = selectedFilterAdapter
        val layoutManager = ChipsLayoutManager.newBuilder(context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        selectedFilterRecyclerView.layoutManager = layoutManager
        selectedFilterRecyclerView.addItemDecoration(SpacingItemDecoration(
                resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_10)
        ))
    }

    private fun setUpData() {
        if (mSelectedFilter.isNotEmpty()) {
            val filter = getSelectedFilter()
            for (element in mSelectedFilter.entries) {
                filter[element.key] = element.value
            }
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
        }
        fetchProductData(getProductListParamMap(getPage()))
        productNavViewModel.fetchSubCategoriesList(categoryApiParamBuilder.generateSubCategoryParam(mDepartmentId))
        productNavViewModel.fetchQuickFilters(categoryApiParamBuilder.generateQuickFilterParam(mDepartmentId))
    }

    private fun getProductIntent(productId: String, warehouseId: String): Intent? {
        if (context == null) {
            return null
        }

        return if (!TextUtils.isEmpty(warehouseId)) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_WITH_WAREHOUSE_ID, productId, warehouseId)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun getScreenName(): String {
        return "category page - " + getDepartMentId()
    }

    override fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                        .baseAppComponent).build()
    }

    override fun onChangeList() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        product_recyclerview.requestLayout()
    }

    override fun onChangeSingleGrid() {
        product_recyclerview.requestLayout()
    }

    private fun setUpAdapter() {
        productTypeFactory = ProductTypeFactoryImpl(this)
        productNavListAdapter = ProductNavListAdapter(productTypeFactory, list, this)
        product_recyclerview.adapter = productNavListAdapter
        product_recyclerview.layoutManager = getStaggeredGridLayoutManager()
        productNavListAdapter?.addShimmer()
        attachScrollListener()
    }

    private fun setQuickFilterAdapter(productCount: String) {
        quickFilterAdapter = QuickFilterAdapter(quickFilterList, this, productCount)
        quickfilter_recyclerview.adapter = quickFilterAdapter
        quickfilter_recyclerview.layoutManager = LinearLayoutManager(activity,
                RecyclerView.HORIZONTAL, false)
    }

    private fun attachScrollListener() {
        nested_recycler_view.setOnScrollChangeListener { v: NestedScrollView,
                                                         _: Int,
                                                         scrollY: Int,
                                                         _: Int,
                                                         oldScrollY: Int ->

            if (v.getChildAt(v.childCount - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY) {
                    if (isPagingAllowed) {
                        val numOfPages: Int = (totalCountInt - 1) / 10
                        if (getPage() < numOfPages) {
                            incrementpage()
                            fetchProductData(getProductListParamMap(getPage()))
                            productNavListAdapter?.addLoading()
                            isPagingAllowed = false
                        } else {
                            productNavListAdapter?.removeLoading()
                        }
                    }

                }
            }

        }
    }


    private fun fetchProductData(paramMap: RequestParams) {
        productNavViewModel.fetchProductListing(paramMap)
    }

    private fun observeData() {
        productNavViewModel.mProductList.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    if (productNavListAdapter?.isShimmerRunning() == true) {
                        productNavListAdapter?.removeShimmer()
                    }

                    if (it.data.isNotEmpty()) {
                        showNoDataScreen(false)
                        list.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                        productNavListAdapter?.removeLoading()
                        product_recyclerview.adapter?.notifyDataSetChanged()
                        isPagingAllowed = true
                    } else {
                        if (list.isEmpty()) {
                            showNoDataScreen(true)
                        }
                    }
                    hideRefreshLayout()
                    reloadFilter(categoryApiParamBuilder.generateFilterParams(mDepartmentId))
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    if (list.isEmpty()) {
                        showNoDataScreen(true)
                    }
                    isPagingAllowed = true
                }

            }
        })

        productNavViewModel.mProductCount.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.countText != null)
                    setTotalSearchResultCount(it.countText)
                setTotalSearchResultCountInteger(it.totalData)
                if (!TextUtils.isEmpty(it.countText)) {
                    setQuickFilterAdapter(getString(R.string.result_count_template_text, it.countText))
                } else {
                    setQuickFilterAdapter("")
                }
            }
        })

        productNavViewModel.mSubCategoryList.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    isSubCategoryAvailable = true
                    subcategory_recyclerview.show()
                    subCategoryAdapter = SubCategoryAdapter(it.data as ArrayList<SubCategoryItem>,
                            this)
                    subcategory_recyclerview.adapter = subCategoryAdapter
                    subcategory_recyclerview.layoutManager = LinearLayoutManager(activity,
                            RecyclerView.HORIZONTAL, false)
                }

                is Fail -> {
                    isSubCategoryAvailable = false
                    subcategory_recyclerview.hide()
                }
            }

        })

        productNavViewModel.getDynamicFilterData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }
            }
        })

        productNavViewModel.mQuickFilterModel.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    initQuickFilter(it.data as ArrayList)
                }
            }
        })
    }

    private fun showNoDataScreen(toShow: Boolean) {
        if (toShow) {
            layout_no_data.show()
            txt_no_data_header.text = resources.getText(R.string.category_nav_product_no_data_title)
            txt_no_data_description.text = resources.getText(R.string.category_nav_product_no_data_description)
            quickfilter_parent.hide()
            val selectedFilterFromEmptyStateListener = getSelectedFilterAsOptionList()
            if (selectedFilterFromEmptyStateListener != null && selectedFilterFromEmptyStateListener.isNotEmpty()) {
                selectedFilterRecyclerView.show()
                populateSelectedFilterToRecylerView(selectedFilterFromEmptyStateListener)
            } else {
                selectedFilterRecyclerView.hide()
            }
        } else {
            layout_no_data.hide()
            quickfilter_parent.show()
            selectedFilterRecyclerView.hide()
        }
        if (isSubCategoryAvailable) {
            subcategory_recyclerview.show()
        }
    }

    private fun populateSelectedFilterToRecylerView(selectedFilterOptionList: List<Option>) {
        selectedFilterAdapter?.setOptionList(selectedFilterOptionList)
    }

    private fun initQuickFilter(list: ArrayList<Filter>) {
        quickFilterList.clear()
        quickFilterList.addAll(list)
        quickfilter_recyclerview.adapter?.notifyDataSetChanged()

    }

    private fun reloadFilter(param: RequestParams) {
        productNavViewModel.fetchDynamicAttribute(param)
    }

    private fun initView() {
        swipe_refresh_layout.show()
        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        productNavViewModel = viewModelProvider.get(ProductNavViewModel::class.java)
    }

    private fun getProductListParamMap(start: Int): RequestParams {
        return categoryApiParamBuilder.generateProductListParam(start, mDepartmentId, userSession, gcmHandler, mSelectedFilter, getSelectedFilter(), getSelectedSort())
    }

    override fun onSwipeToRefresh() {
        reloadData()
    }

    override fun reloadData() {
        if (productNavListAdapter == null) {
            return
        }
        showRefreshLayout()
        productNavListAdapter?.clearData()
        productNavListAdapter?.addShimmer()
        resetPage()
        fetchProductData(getProductListParamMap(getPage()))
        productNavViewModel.fetchQuickFilters(categoryApiParamBuilder.generateQuickFilterParam(mDepartmentId))
    }

    override fun OnDefaultItemClicked() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.CATEGORY_BELANJA)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun OnSubCategoryClicked(id: String, categoryName: String) {
        activity?.let {
            val intent = Intent(it, CategoryNavActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_ID, id)
            intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_NAME, categoryName)
            intent.putExtra(EXTRA_PARENT_ID, mDepartmentId)
            intent.putExtra(EXTRA_PARENT_NAME, mDepartmentName)
            it.startActivity(intent)
        }
    }


    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, 1002)
        }
        if (item.isTopAds) {
            ImpresionTask(ProductNavFragment::class.java).execute(item.productClickTrackingUrl)
        }
        catAnalyticsInstance.eventClickProductList(item.id.toString(),
                mDepartmentId,
                item.name,
                CurrencyFormatHelper.convertRupiahToInt(item.price),
                adapterPosition,
                item.categoryBreadcrumb ?: "",
                categoryApiParamBuilder.getProductItemPath(item.categoryBreadcrumb
                        ?: "", mDepartmentId),
                getDimensionData()
        )
    }

    private fun getDimensionData(): String {
        return categoryApiParamBuilder.generateGTMDimensionData(getSelectedFilter(), getSelectedSort())
    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {
    }

    override fun hasThreeDots() = true

    override fun onThreeDotsClicked(productItem: ProductsItem, position: Int) {
        showProductCardOptions(
                this,
                ProductCardOptionsModel(
                        hasWishlist = true,
                        isWishlisted = productItem.wishlist,
                        productId = productItem.id.toString(),
                        isTopAds = productItem.isTopAds,
                        topAdsWishlistUrl = productItem.productWishlistTrackingUrl,
                        productPosition = position
                )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        handleProductCardOptionsActivityResult(requestCode, resultCode, data, object: ProductCardOptionsWishlistCallback {
            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                handleWishlistAction(productCardOptionsModel)
            }
        })
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        catAnalyticsInstance.eventWishistClicked(mDepartmentId, productCardOptionsModel.productId, !productCardOptionsModel.isWishlisted, isUserLoggedIn(), productCardOptionsModel.isTopAds)

        if (productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            handleWishlistActionForLoggedInUser(productCardOptionsModel)
        } else {
            launchLoginActivity()
        }
    }

    private fun handleWishlistActionForLoggedInUser(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isAddWishlist) {
            handleAddWishlistAction(productCardOptionsModel)
        } else {
            handleRemoveWishlistAction(productCardOptionsModel)
        }
    }

    private fun handleAddWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessAddWishlist(productCardOptionsModel.productId)
        }
        else {
            onErrorAddWishList(getString(com.tokopedia.wishlist.common.R.string.msg_error_add_wishlist), productCardOptionsModel.productId)
        }
    }

    private fun handleRemoveWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            onSuccessRemoveWishlist(productCardOptionsModel.productId)
        }
        else {
            onErrorRemoveWishlist(getString(com.tokopedia.wishlist.common.R.string.msg_error_remove_wishlist), productCardOptionsModel.productId)
        }
    }

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {
        catAnalyticsInstance.eventWishistClicked(mDepartmentId, productItem.id.toString(), !productItem.wishlist, isUserLoggedIn(), productItem.isTopAds)

        if (userSession.isLoggedIn) {
            disableWishlistButton(productItem.id.toString())
            if (productItem.wishlist) {
                removeWishlist(productItem.id.toString(), userSession.userId)
            } else {
                addWishlist(productItem.id.toString(), userSession.userId)
            }
        } else {
            launchLoginActivity()
        }
    }

    private fun removeWishlist(productId: String, userId: String) {
        removeWishlistActionUseCase.createObservable(productId,
                userId, this)
    }

    private fun addWishlist(productId: String, userId: String) {
        addWishlistActionUseCase.createObservable(productId, userId,
                this)

    }

    private fun launchLoginActivity() {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    override fun onProductImpressed(item: ProductsItem, adapterPosition: Int) {
    }


    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
            catAnalyticsInstance.eventQuickFilterClicked(mDepartmentId, option, true)
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
            catAnalyticsInstance.eventQuickFilterClicked(mDepartmentId, option, false)
        }
    }

    override fun isQuickFilterSelected(option: Option): Boolean {
        return getSelectedFilter().containsKey(option.key)
    }

    override fun getFilterRequestCode(): Int {
        return REQUEST_ACTIVITY_FILTER_PRODUCT
    }


    override fun getSortRequestCode(): Int {
        return REQUEST_ACTIVITY_SORT_PRODUCT
    }

    private fun getPage(): Int {
        return pageCount
    }

    private fun incrementpage() {
        pageCount += 1
    }

    private fun resetPage() {
        isPagingAllowed = true
        pageCount = 0
    }

    override fun onSortAppliedEvent(selectedSortName: String, sortValue: Int) {
        catAnalyticsInstance.eventSortApplied(getDepartMentId(),
                selectedSortName, sortValue)
    }

    override fun wishListEnabledTracker(wishListTrackerUrl: String) {
        val params = RequestParams.create()
        params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, wishListTrackerUrl)
        topAdsWishlishedUseCase.execute(params, object : Subscriber<WishlistModel>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(wishlistModel: WishlistModel) {
            }
        })
    }

    override fun onShareButtonClicked() {
    }

    override fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>, viewedTopAdsList: List<Visitable<Any>>) {
        catAnalyticsInstance.eventProductListImpression(getDepartMentId(),
                getDepartMentId(),
                viewedProductList,
                viewedTopAdsList)
    }

    override fun topAdsTrackerUrlTrigger(url: String) {
        ImpresionTask(ProductNavFragment::class.java).execute(url)
    }

    override fun onPause() {
        super.onPause()
        productNavListAdapter?.onPause()
    }

    override fun onDestroyView() {
        product_recyclerview?.let {
            it.adapter = null
            it.layoutManager = null
        }
        subcategory_recyclerview?.let {
            it.adapter = null
            it.layoutManager = null
        }
        quickfilter_recyclerview?.let {
            it.adapter = null
            it.layoutManager = null
        }
        productNavListAdapter = null
        subCategoryAdapter = null
        quickFilterAdapter = null
        super.onDestroyView()
    }

    override fun onSelectedFilterRemoved(uniqueId: String) {
        removeSelectedFilter(uniqueId)
    }

    override fun getSelectedFilterAsOptionList(): List<Option>? {
        return getOptionListFromFilterController()
    }

    private fun getOptionListFromFilterController(): List<Option> {
        return if (filterController == null) java.util.ArrayList() else OptionHelper.combinePriceFilterIfExists(filterController.getActiveFilterOptionList(),
                resources.getString(R.string.empty_state_selected_filter_price_name))
    }

    override fun getDepartMentId(): String {
        return mDepartmentId
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String) {
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessAddWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), true)
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_add_wishlist))
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String) {
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRemoveWishlist(productId: String) {
        productNavListAdapter?.updateWishlistStatus(productId.toInt(), false)
        enableWishlistButton(productId)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.msg_remove_wishlist))
    }

    private fun enableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId.toInt(), true)
    }

    private fun disableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId.toInt(), false)
    }

    private fun isUserLoggedIn(): Boolean {
        return userSession.isLoggedIn
    }
}
