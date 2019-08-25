package com.tokopedia.discovery.categoryrevamp.view.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.ProductNavListAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.QuickFilterAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.SubCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.ProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.ProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.view.activity.CategoryNavActivity
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.QuickFilterListener
import com.tokopedia.discovery.categoryrevamp.view.interfaces.SubCategoryListener
import com.tokopedia.discovery.categoryrevamp.viewmodel.ProductNavViewModel
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.data.Filter
import com.tokopedia.discovery.common.data.Option
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_product_nav.*
import javax.inject.Inject


class ProductNavFragment : BaseCategorySectionFragment(),
        BaseCategoryAdapter.OnItemChangeView,
        QuickFilterListener,
        ProductCardListener,
        SubCategoryListener {

    override fun OnSubCategoryClicked(id: String, categoryName: String) {
        activity?.let {
            val intent = Intent(it, CategoryNavActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_ID, id)
            intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_NAME, categoryName)
//            if (removeAnimation) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            it.startActivity(intent)
        }
    }


    override fun onItemClicked(item: ProductsItem, adapterPosition: Int) {
        val intent = getProductIntent(item.id.toString(), item.categoryID.toString())

        if (intent != null) {
            intent.putExtra(SearchConstant.Wishlist.WISHLIST_STATUS_UPDATED_POSITION, adapterPosition)
            startActivityForResult(intent, 1002)
        }
    }

    override fun onLongClick(item: ProductsItem, adapterPosition: Int) {
        Log.d("ProductNavFragment", "onLongClick")
    }

    override fun onWishlistButtonClicked(productItem: ProductsItem) {
        Log.d("ProductNavFragment", "onWishlistButtonClicked")
    }

    override fun onProductImpressed(item: ProductsItem, adapterPosition: Int) {
        Log.d("ProductNavFragment", "onProductImpressed")
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


    override fun onQuickFilterSelected(option: Option) {
        if (!isQuickFilterSelected(option)) {
            val filter = getSelectedFilter()
            filter[option.key] = option.value
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
        } else {
            val filter = getSelectedFilter()
            filter.remove(option.key)
            applyFilterToSearchParameter(filter)
            setSelectedFilter(filter)
            reloadData()
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


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var productNavViewModel: ProductNavViewModel

    lateinit var userSession: UserSession

    private lateinit var gcmHandler: GCMHandler

    private lateinit var productTypeFactory: ProductTypeFactory

    private lateinit var subCategoryAdapter: SubCategoryAdapter

    private lateinit var quickFilterAdapter: QuickFilterAdapter

    private lateinit var categoryNavComponent: CategoryNavComponent

    var productNavListAdapter: ProductNavListAdapter? = null

    var list: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()

    var mDepartmentId: String = ""
    var mDepartmentName: String = ""

    var start = 0

    private val REQUEST_ACTIVITY_SORT_PRODUCT = 102
    private val REQUEST_ACTIVITY_FILTER_PRODUCT = 103

    companion object {
        // private val EXTRA_CATEGORY_HEADER_MODEL = "categoryheadermodel"
        private val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"


        @JvmStatic
        fun newInstance(departmentid: String, departmentName: String): Fragment {
            val fragment = ProductNavFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_ID, departmentid)
            bundle.putString(EXTRA_CATEGORY_DEPARTMENT_NAME, departmentName)
            fragment.arguments = bundle
            return fragment
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
        arguments?.let {
            if (it.containsKey(EXTRA_CATEGORY_DEPARTMENT_ID)) {
                mDepartmentId = it.getString(EXTRA_CATEGORY_DEPARTMENT_ID, "")
                mDepartmentName = it.getString(EXTRA_CATEGORY_DEPARTMENT_NAME, "")
            }
        }
        initView()
        observeData()
        setUpAdapter()
        setUpNavigation()
        if (userVisibleHint) {
            setUpVisibleFragmentListener()
        }
    }


    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun getScreenName(): String {
        return ""
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
        /* getStaggeredGridLayoutManager()?.let {
             staggeredGridLayoutLoadMoreTriggerListener = getEndlessRecyclerViewListener(it)
         }*/
        /* staggeredGridLayoutLoadMoreTriggerListener?.let {
             product_recyclerview.addOnScrollListener(it)
         }*/
    }

    /* private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager):
             EndlessRecyclerViewScrollListener {
         return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
             override fun onLoadMore(page: Int, totalItemsCount: Int) {
                 fetchProductData(getParamMap(page))
                 productNavListAdapter?.addLoading()
             }
         }
     }*/

    private fun adc() {

        nested_recycler_view.setOnScrollChangeListener { v: NestedScrollView,
                                                         scrollX: Int,
                                                         scrollY: Int,
                                                         oldScrollX: Int,
                                                         oldScrollY: Int ->

            if (v.getChildAt(v.childCount - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                        scrollY > oldScrollY) {
                    start++
                    Log.d("setOnScrollChangeListener", start.toString())
                    fetchProductData(getParamMap(start))
                    productNavListAdapter?.addLoading()

                }
            }

        }

        /* nested_recycler_view.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)(v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
             if (v.getChildAt(v.getChildCount() - 1) != null) {
                 if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                         scrollY > oldScrollY) {
                     //code to fetch more data for endless scrolling
                 }
             }
         });*/
    }


    private fun fetchProductData(paramMap: RequestParams) {
        productNavViewModel.fetchProductList(paramMap)
    }

    private fun observeData() {
        productNavViewModel.mProductList.observe(this, Observer {

            when (it) {
                is Success -> {
                    layout_no_data.visibility = View.GONE
                    list.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                    productNavListAdapter?.removeLoading()
                    product_recyclerview.adapter?.notifyDataSetChanged()
                    // staggeredGridLayoutLoadMoreTriggerListener?.updateStateAfterGetData()
                    hideRefreshLayout()
                    reloadFilter(createFilterParam())
                }

                is Fail -> {
                    productNavListAdapter?.removeLoading()
                    hideRefreshLayout()
                    layout_no_data.visibility = View.VISIBLE
                }

            }
        })

        productNavViewModel.mProductCount.observe(this, Observer {
            it?.let {
                setTotalSearchResultCount(it)
                txt_product_count.text = it
            }
        })

        productNavViewModel.mSubCategoryList.observe(this, Observer {

            when (it) {
                is Success -> {
                    subcategory_recyclerview.visibility = View.VISIBLE
                    subCategoryAdapter = SubCategoryAdapter(it.data as ArrayList<SubCategoryItem>,
                            this)
                    subcategory_recyclerview.adapter = subCategoryAdapter
                    subcategory_recyclerview.layoutManager = LinearLayoutManager(activity,
                            RecyclerView.HORIZONTAL, false)
                }

                is Fail -> {
                    subcategory_recyclerview.visibility = View.GONE
                }
            }

        })

        productNavViewModel.getDynamicFilterData().observe(this, Observer {

            when (it) {
                is Success -> {
                    renderDynamicFilter(it.data.data)
                }

                is Fail -> {
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

    private fun initQuickFilter(quickFilterList: ArrayList<Filter>) {
        quickFilterAdapter = QuickFilterAdapter(quickFilterList, this)
        quickfilter_recyclerview.adapter = quickFilterAdapter
        quickfilter_recyclerview.layoutManager = LinearLayoutManager(activity,
                RecyclerView.HORIZONTAL, false)
    }


    private fun createFilterParam(): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = mDepartmentId
        paramMap.putString(CategoryNavConstants.SOURCE, "search_product")
        paramMap.putObject(CategoryNavConstants.FILTER, daFilterQueryType)
        return paramMap
    }

    private fun reloadFilter(param: RequestParams) {
        productNavViewModel.fetchDynamicAttribute(param)
    }

    private fun initView() {

        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)

        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            productNavViewModel = viewModelProvider.get(ProductNavViewModel::class.java)
            fetchProductData(getParamMap(start))
            productNavViewModel.fetchSubCategoriesList(getSubCategoryParam())
            productNavViewModel.fetchQuickFilters(getQuickFilterParams())
        }
        adc()
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

    private fun getParamMap(start: Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CategoryNavConstants.START, (start * 10).toString())
        requestParams.putString(CategoryNavConstants.SC, mDepartmentId)
        requestParams.putString(CategoryNavConstants.DEVICE, "android")
        requestParams.putString(CategoryNavConstants.UNIQUE_ID, getUniqueId())
        requestParams.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        requestParams.putString(CategoryNavConstants.ROWS, "10")
        requestParams.putString(CategoryNavConstants.SOURCE, "search_product")
        requestParams.putAllString(getSelectedSort())
        requestParams.putAllString(getSelectedFilter())
        return requestParams
    }

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthUtil.md5(userSession.userId)
        else
            AuthUtil.md5(gcmHandler.registrationId)
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
        // staggeredGridLayoutLoadMoreTriggerListener?.resetState()
        start = 0
        fetchProductData(getParamMap(start))

        quickFilterAdapter.clearData()
        productNavViewModel.fetchQuickFilters(getQuickFilterParams())

    }
}
