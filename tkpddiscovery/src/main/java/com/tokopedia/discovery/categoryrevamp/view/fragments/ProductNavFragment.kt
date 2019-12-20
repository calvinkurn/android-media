package com.tokopedia.discovery.categoryrevamp.view.fragments


import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.*
import com.tokopedia.discovery.categoryrevamp.analytics.CategoryPageAnalytics.Companion.catAnalyticsInstance
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.utils.ParamMapToUrl
import com.tokopedia.discovery.categoryrevamp.view.activity.CategoryNavActivity
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SelectedFilterListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SubCategoryListener
import com.tokopedia.discovery.categoryrevamp.viewmodel.ProductNavViewModel
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.itemdecoration.LinearHorizontalSpacingDecoration
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
import kotlinx.android.synthetic.main.layout_nav_banned_layout.*
import kotlinx.android.synthetic.main.layout_nav_no_product.*
import rx.Subscriber
import javax.inject.Inject


open class ProductNavFragment : BaseCategorySectionFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener,
        ProductCardListener,
        SubCategoryListener,
        WishListActionListener,
        SelectedFilterListener {

    var isSubCategoryAvailable = false

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

    fun enableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId?.toInt() ?: 0, true)
    }

    fun disableWishlistButton(productId: String) {
        productNavListAdapter?.setWishlistButtonEnabled(productId?.toInt() ?: 0, false)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var productNavViewModel: ProductNavViewModel

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

    var productNavListAdapter: ProductNavListAdapter? = null

    var list: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()

    var quickFilterList = ArrayList<Filter>()
    var mDepartmentId: String = ""
    var mDepartmentName: String = ""

    var pageCount = 0
    var isPagingAllowed: Boolean = true
    private var bannedData: Data? = null

    private var selectedFilterAdapter: SelectedFilterAdapter? = null

    var mSelectedFilter = HashMap<String, String>()
    var categoryUrl: String? = null

    private val REQUEST_ACTIVITY_SORT_PRODUCT = 102
    private val REQUEST_ACTIVITY_FILTER_PRODUCT = 103
    private val KEY_ADVERTISINGID = "KEY_ADVERTISINGID"
    private val ADVERTISINGID = "ADVERTISINGID"
    private val QUERY_APP_CLIENT_ID = "?appClientId="

    companion object {
        private val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"
        private val EXTRA_BANNED_DATA = "BANNED_DATA"
        private val EXTRA_CATEGORY_URL = "CATEGORY_URL"

        @JvmStatic
        fun newInstance(data: Data, categoryUrl: String?): Fragment {
            val fragment = ProductNavFragment()
            val bundle = Bundle()
            if (categoryUrl != null) {
                bundle.putString(EXTRA_CATEGORY_URL, categoryUrl)
            } else {
                bundle.putString(EXTRA_CATEGORY_DEPARTMENT_ID, data.id.toString())
            }
            bundle.putParcelable(EXTRA_BANNED_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bannedData = it.getParcelable(EXTRA_BANNED_DATA) as Data?
            mDepartmentId = bannedData?.id.toString()
            mDepartmentName = bannedData?.name ?: ""
            if (it.containsKey(EXTRA_CATEGORY_URL)) {
                categoryUrl = it.getString(EXTRA_CATEGORY_URL, "")
                mSelectedFilter = URLParser(it.getString(EXTRA_CATEGORY_URL, "")).paramKeyValueMap
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_nav, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryNavComponent.inject(this)
        initView()
        if (bannedData == null || bannedData?.isBanned == 0) {
            setUpData()
            observeData()
            setUpAdapter()
            setUpNavigation()
            if (userVisibleHint) {
                setUpVisibleFragmentListener()
            }
        } else {
            showBannedDataScreen()
        }
        initSelectedFilterRecyclerView()
    }

    protected fun initSelectedFilterRecyclerView() {
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
        productNavViewModel.fetchSubCategoriesList(getSubCategoryParam())
        productNavViewModel.fetchQuickFilters(getQuickFilterParams())
    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun getScreenName(): String {
        return "category page - " + getDepartMentId();
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
                                                         scrollX: Int,
                                                         scrollY: Int,
                                                         oldScrollX: Int,
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
        productNavViewModel.mProductList.observe(this, Observer {

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
                    reloadFilter(createFilterParam())
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

        productNavViewModel.mProductCount.observe(this, Observer {
            it?.let {
                if(it.countText!=null)
                    setTotalSearchResultCount(it.countText)
                setTotalSearchResultCountInteger(it.totalData)
                if (!TextUtils.isEmpty(it.countText)) {
                    setQuickFilterAdapter(getString(R.string.result_count_template_text, it.countText))
                } else {
                    setQuickFilterAdapter("")
                }
            }
        })

        productNavViewModel.mSubCategoryList.observe(this, Observer {

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

        productNavViewModel.getDynamicFilterData().observe(this, Observer {
            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }
            }
        })


        productNavViewModel.mQuickFilterModel.observe(this, Observer {

            when (it) {
                is Success -> {
                    initQuickFilter(it.data as ArrayList)
                }

                is Fail -> {
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

    protected fun populateSelectedFilterToRecylerView(selectedFilterOptionList: List<Option>) {
        selectedFilterAdapter?.setOptionList(selectedFilterOptionList)
    }

    private fun showBannedDataScreen() {
        layout_banned_screen.show()
        swipe_refresh_layout.hide()
        observeSeamlessLogin()
        catAnalyticsInstance.eventBukaView(bannedData?.appRedirection.toString(), mDepartmentId)
        if (bannedData != null && bannedData?.displayButton == true && CategoryNavActivity.isBannedNavigationEnabled(activity as Context)) {
            category_btn_banned_navigation.show()
            category_btn_banned_navigation.setOnClickListener() {
                catAnalyticsInstance.eventBukaClick(bannedData?.appRedirection.toString(), mDepartmentId)
                val localCacheHandler = LocalCacheHandler(activity, ADVERTISINGID)
                val adsId = localCacheHandler.getString(KEY_ADVERTISINGID)
                var url = Uri.parse(bannedData?.appRedirection).toString()
                if (adsId != null && adsId.trim().isNotEmpty()) {
                    url = url.plus(QUERY_APP_CLIENT_ID + adsId)
                    productNavViewModel.openBrowserSeamlessly(url)
                }
            }
        }
        txt_header.text = bannedData?.bannedMsgHeader
        txt_sub_header.text = bannedData?.bannedMessage

    }

    private fun observeSeamlessLogin() {
        productNavViewModel.mSeamlessLogin.observe(this, Observer {
            when (it) {
                is Success -> {
                    openUrlSeamlessly(it.data)
                }

                is Fail -> {
                    onSeamlessError()
                }
            }
        })
    }

    private fun initQuickFilter(list: ArrayList<Filter>) {
        quickFilterList.clear()
        quickFilterList.addAll(list)
        quickfilter_recyclerview.adapter?.notifyDataSetChanged()

    }


    private fun createFilterParam(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = mDepartmentId
        paramMap.putString(CategoryNavConstants.SOURCE, "search_product")
        paramMap.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        paramMap.putString(CategoryNavConstants.Q, "")
        paramMap.putString(CategoryNavConstants.SOURCE, "directory")
        return paramMap
    }

    private fun reloadFilter(param: RequestParams) {
        productNavViewModel.fetchDynamicAttribute(param)
    }

    private fun initView() {
        layout_banned_screen.visibility = View.GONE
        swipe_refresh_layout.visibility = View.VISIBLE
        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        productNavViewModel = viewModelProvider.get(ProductNavViewModel::class.java)
    }

    private fun getQuickFilterParams(): RequestParams {
        val quickFilterParam = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = mDepartmentId
        quickFilterParam.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        quickFilterParam.putString(CategoryNavConstants.SOURCE, "quick_filter")
        return quickFilterParam
    }

    private fun getSubCategoryParam(): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(CategoryNavConstants.IDENTIFIER, mDepartmentId)
        subCategoryMap.putBoolean(CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
    }

    private fun getProductListParamMap(start: Int): RequestParams {

        val param = RequestParams.create()


        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.putString(CategoryNavConstants.START, (start * 10).toString())
        searchProductRequestParams.putString(CategoryNavConstants.SC, mDepartmentId)
        searchProductRequestParams.putString(CategoryNavConstants.DEVICE, "android")
        searchProductRequestParams.putString(CategoryNavConstants.UNIQUE_ID, getUniqueId())
        searchProductRequestParams.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        searchProductRequestParams.putString(CategoryNavConstants.ROWS, "10")
        searchProductRequestParams.putString(CategoryNavConstants.SOURCE, "search_product")
        if (mSelectedFilter.isNotEmpty()) {
            searchProductRequestParams.putAllString(mSelectedFilter)
            mSelectedFilter.clear()
        } else {
            searchProductRequestParams.putAllString(getSelectedFilter())
        }
        searchProductRequestParams.putAllString(getSelectedSort())
        param.putString("product_params", createParametersForQuery(searchProductRequestParams.parameters))


        val topAdsRequestParam = RequestParams.create()
        topAdsRequestParam.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        topAdsRequestParam.putString(CategoryNavConstants.DEVICE, "android")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_SRC, "directory")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_PAGE, start.toString())
        topAdsRequestParam.putString(CategoryNavConstants.KEY_EP, "product")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_ITEM, "2")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_F_SHOP, "1")
        topAdsRequestParam.putString(CategoryNavConstants.KEY_DEPT_ID, mDepartmentId)

        topAdsRequestParam.putAllString(getSelectedSort())
        if (mSelectedFilter.isNotEmpty()) {
            topAdsRequestParam.putAllString(mSelectedFilter)
            mSelectedFilter.clear()
        } else {
            topAdsRequestParam.putAllString(getSelectedFilter())
        }

        param.putString("top_params", createParametersForQuery(topAdsRequestParam.parameters))
        return param
    }


    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthHelper.getMD5Hash(userSession.userId)
        else
            AuthHelper.getMD5Hash(gcmHandler.registrationId)
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

        productNavViewModel.fetchQuickFilters(getQuickFilterParams())

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
            ImpresionTask().execute(item.productClickTrackingUrl)
        }
        catAnalyticsInstance.eventClickProductList(item.id.toString(),
                mDepartmentId,
                item.name,
                CurrencyFormatHelper.convertRupiahToInt(item.price),
                adapterPosition,
                item.categoryBreadcrumb ?: "",
                getProductItemPath(item.categoryBreadcrumb ?: "", mDepartmentId))

    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {
    }

    private fun getProductItemPath(path: String, id: String): String {
        if (path.isNotEmpty()) {
            return "category/$path - $id"
        }
        return ""
    }

    override fun onWishlistButtonClicked(productItem: ProductsItem, position: Int) {

        if (userSession.isLoggedIn) {
            disableWishlistButton(productItem.id.toString())
            if (productItem.wishlist) {
                removeWishlist(productItem.id.toString(), userSession.userId, position)
                catAnalyticsInstance.eventWishistClicked(mDepartmentId, productItem.id.toString(), false)
            } else {
                addWishlist(productItem.id.toString(), userSession.userId, position)
                catAnalyticsInstance.eventWishistClicked(mDepartmentId, productItem.id.toString(), true)
            }
        } else {
            launchLoginActivity(productItem.id.toString())
        }

    }

    private fun removeWishlist(productId: String, userId: String, adapterPosition: Int) {
        removeWishlistActionUseCase.createObservable(productId,
                userId, this)
    }

    private fun addWishlist(productId: String, userId: String, adapterPosition: Int) {
        addWishlistActionUseCase.createObservable(productId, userId,
                this)

    }

    private fun launchLoginActivity(productId: String) {
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
        ImpresionTask().execute(url)
    }


    override fun onPause() {
        super.onPause()
        productNavListAdapter?.onPause()
    }

    override fun onDestroyView() {
        product_recyclerview.adapter = null
        product_recyclerview.layoutManager = null

        subcategory_recyclerview.adapter = null
        subcategory_recyclerview.layoutManager = null

        quickfilter_recyclerview.adapter = null
        quickfilter_recyclerview.layoutManager = null

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

    private fun openUrlSeamlessly(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun onSeamlessError() {
        layout_banned_screen.show()
        txt_header.text = getString(R.string.category_server_error_header)
        txt_sub_header.text = getString(R.string.try_again)
    }
}
