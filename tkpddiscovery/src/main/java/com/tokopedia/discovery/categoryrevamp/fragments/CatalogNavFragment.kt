package com.tokopedia.discovery.categoryrevamp.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.adapters.CatalogNavListAdapter
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.di.CategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.di.DaggerCategoryNavComponent
import com.tokopedia.discovery.categoryrevamp.viewmodel.CatalogNavViewModel
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_category_nav.*
import javax.inject.Inject

class CatalogNavFragment : BaseCategorySectionFragment(), BaseCategoryAdapter.OnItemChangeView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var catalogNavViewModel: CatalogNavViewModel

    var categoryHeaderModel: CategoryHeaderModel? = null

    var list: ArrayList<Visitable<CatalogTypeFactory>> = ArrayList()

    lateinit var catalogTypeFactory: CatalogTypeFactory

    lateinit var catalogNavListAdapter: CatalogNavListAdapter

    lateinit var categoryNavComponent: CategoryNavComponent

    companion object {
        private val EXTRA_CATEGORY_HEADER_MODEL = "categoryheadermodel"
        @JvmStatic
        fun newInstance(categoryHeaderModel: CategoryHeaderModel): Fragment {
            val fragment = CatalogNavFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_CATEGORY_HEADER_MODEL, categoryHeaderModel)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getAdapter(): BaseCategoryAdapter? {
        return catalogNavListAdapter
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        categoryNavComponent = DaggerCategoryNavComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryNavComponent.inject(this)
        if (arguments != null && arguments!!.containsKey(EXTRA_CATEGORY_HEADER_MODEL)) {
            categoryHeaderModel = arguments!!.getParcelable(EXTRA_CATEGORY_HEADER_MODEL)
        }


        initView()
        setUpAdapter()
        observeData()
        setUpNavigation()
    }

    private fun observeData() {

        catalogNavViewModel.mCatalog.observe(this, Observer {


            when (it) {
                is Success -> {
                    list.addAll(it.data.items as ArrayList<Visitable<CatalogTypeFactory>>)
                    catalog_recyclerview.adapter?.notifyDataSetChanged()
                }

                is Fail -> {

                }

            }


        })

    }

    private fun setUpAdapter() {
        catalogTypeFactory = CatalogTypeFactoryImpl()
        catalogNavListAdapter = CatalogNavListAdapter(catalogTypeFactory, list, this)
        catalog_recyclerview.adapter = catalogNavListAdapter
        catalog_recyclerview.layoutManager = getStaggeredGridLayoutManager()

    }

    private fun initView() {
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            catalogNavViewModel = viewModelProvider.get(CatalogNavViewModel::class.java)
            catalogNavViewModel.fetchCatalogDetail(getCatalogListParams())
        }
    }

    private fun getCatalogListParams(): RequestParams {

        val catalogMap = RequestParams()
        catalogMap.putString(CategoryNavConstants.QUERY, "")
        catalogMap.putString(CategoryNavConstants.SOURCE, "directory")
        catalogMap.putString(CategoryNavConstants.ST, "catalog")
        catalogMap.putInt(CategoryNavConstants.ROWS, 10)
        catalogMap.putObject("filter", AceFilterInput("", "", categoryHeaderModel?.departementId
                ?: ""))
        return catalogMap

    }

    override fun onChangeList() {
        catalog_recyclerview.requestLayout()
    }

    override fun onChangeDoubleGrid() {
        catalog_recyclerview.requestLayout()
    }

    override fun onChangeSingleGrid() {
        catalog_recyclerview.requestLayout()
    }


    data class AceFilterInput(var pmin: String, var pmax: String, var sc: String)

}
