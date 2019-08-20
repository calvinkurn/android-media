package com.tokopedia.discovery.categoryrevamp.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.ProductNavListAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.SubCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.ProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.ProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.viewmodel.ProductNavViewModel
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_product_nav.*
import javax.inject.Inject


class ProductNavFragment : BaseCategorySectionFragment(), BaseCategoryAdapter.OnItemChangeView {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var productNavViewModel: ProductNavViewModel

    lateinit var userSession: UserSession

    lateinit var gcmHandler: GCMHandler

    lateinit var productTypeFactory: ProductTypeFactory

    lateinit var productNavListAdapter: ProductNavListAdapter

    lateinit var subCategoryAdapter: SubCategoryAdapter

    lateinit var categoryNavComponent: CategoryNavComponent

    var list: ArrayList<Visitable<ProductTypeFactory>> = ArrayList()

    var categoryHeaderModel: CategoryHeaderModel? = null


    companion object {
        private val EXTRA_CATEGORY_HEADER_MODEL = "categoryheadermodel"
        @JvmStatic
        fun newInstance(categoryHeaderModel: CategoryHeaderModel): Fragment {
            val fragment = ProductNavFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_CATEGORY_HEADER_MODEL, categoryHeaderModel)
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
        if (arguments != null && arguments!!.containsKey(EXTRA_CATEGORY_HEADER_MODEL)) {
            categoryHeaderModel = arguments!!.getParcelable(EXTRA_CATEGORY_HEADER_MODEL)
        }
        initView()
        observeData()
        setUpAdapter()
        setUpNavigation()
    }


    override fun getAdapter(): BaseCategoryAdapter? {
        return productNavListAdapter
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()


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
        productTypeFactory = ProductTypeFactoryImpl()
        productNavListAdapter = ProductNavListAdapter(productTypeFactory, list, this)
        product_recyclerview.adapter = productNavListAdapter
        product_recyclerview.layoutManager = getStaggeredGridLayoutManager()

    }

    private fun observeData() {
        productNavViewModel.mProductList.observe(this, Observer {

            when (it) {
                is Success -> {
                    list.addAll(it.data as ArrayList<Visitable<ProductTypeFactory>>)
                    product_recyclerview.adapter?.notifyDataSetChanged()
                }

                is Fail -> {

                }

            }
        })

        productNavViewModel.mSubCategoryList.observe(this, Observer {

            when (it) {
                is Success -> {
                    subCategoryAdapter = SubCategoryAdapter(it.data as ArrayList<SubCategoryItem>)
                    subcategory_recyclerview.adapter = subCategoryAdapter
                    subcategory_recyclerview.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                }

                is Fail -> {

                }
            }

        })


    }

    private fun initView() {

        userSession = UserSession(activity)
        gcmHandler = GCMHandler(activity)

        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            productNavViewModel = viewModelProvider.get(ProductNavViewModel::class.java)
            productNavViewModel.fetchProductList(getParamMap())
            productNavViewModel.fetchSubCategoriesList(getSubCategoryParam())
        }
    }

    private fun getSubCategoryParam(): RequestParams {
        val subCategoryMap = RequestParams()
        subCategoryMap.putString(CategoryNavConstants.IDENTIFIER, categoryHeaderModel?.departementId)
        subCategoryMap.putBoolean(CategoryNavConstants.INTERMEDIARY, false)
        subCategoryMap.putBoolean(CategoryNavConstants.SAFESEARCH, false)
        return subCategoryMap
    }

    private fun getParamMap(): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(CategoryNavConstants.START, "0")
        requestParams.putString(CategoryNavConstants.SC, categoryHeaderModel?.departementId)
        requestParams.putString(CategoryNavConstants.DEVICE, "android")
        requestParams.putString(CategoryNavConstants.UNIQUE_ID, getUniqueId())
        requestParams.putString(CategoryNavConstants.KEY_SAFE_SEARCH, "false")
        requestParams.putString(CategoryNavConstants.ROWS, "10")
        requestParams.putString(CategoryNavConstants.SOURCE, "search_product")
        return requestParams
    }

    private fun getUniqueId(): String {
        return if (userSession.isLoggedIn)
            AuthUtil.md5(userSession.userId)
        else
            AuthUtil.md5(gcmHandler.registrationId)
    }

}
