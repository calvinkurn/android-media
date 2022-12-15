package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.CatalogLibraryComponent
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.model.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogLandingPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_homepage.global_error_page
import kotlinx.android.synthetic.main.fragment_category_landing_page.*
import kotlinx.android.synthetic.main.fragment_catalog_homepage.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLandingPageFragment: Fragment(),
    HasComponent<CatalogLibraryComponent>, CatalogLibraryListener {

    private var globalError : GlobalError? = null
    private var categoryName = ""
    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private val landingPageViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(CatalogLandingPageViewModel::class.java)
        }
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }
    private val catalogLandingPageAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDataModel> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLandingRecyclerView: RecyclerView? = null

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf()).also {
            it.setUpForLandingPage()
        }

    companion object {
        const val CATALOG_LANDING_PAGE_FRAGMENT_TAG = "CATALOG_LANDING_PAGE_FRAGMENT_TAG"
        private const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
        fun newInstance(categoryName: String?): CatalogLandingPageFragment {
            return CatalogLandingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_NAME, categoryName)
                }
            }
        }
        private const val VIRAL_SORT_TYPE = "5"
        private const val TOP_FIVE_SORT_TYPE = "6"
        private const val TOP_FIVE_TOTAL_ROWS = "5"
        private const val VIRAL_TOTAL_ROWS = "1"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        extractArguments()
        initViews(view)
        initData()
    }

    private fun extractArguments() {
        categoryName = arguments?.getString(ARG_CATEGORY_NAME, "") ?: ""
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        initHeaderTitle(view)
        setupRecyclerView(view)
    }

    private fun initData() {
        setObservers()
        updateUi()
        getDataFromViewModel()
    }

    private fun setupRecyclerView(view: View) {
        catalogLandingRecyclerView = view.findViewById(R.id.catalog_landing_rv)
        catalogLandingRecyclerView?.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = catalogLandingPageAdapter
            setHasFixedSize(true)
        }
    }

    private fun setObservers() {
        landingPageViewModel?.catalogLandingPageLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    // TODO if data not received for an API . Remove its shimmer component
                    it.data.listOfComponents.forEach { component ->
                        catalogLibraryUiUpdater.updateModel(component)
                    }
                    updateUi()
                }

                is Fail -> {
                    onError(it.throwable)
                }
            }
        }
    }

    override fun getComponent(): CatalogLibraryComponent {
        return DaggerCatalogLibraryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun initHeaderTitle(view: View){
        view.findViewById<HeaderUnify>(R.id.clp_header).apply {
            headerTitle = categoryName
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun updateUi() {
        catalogLandingRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDataModel>) {
        catalogLandingPageAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        catalogLandingRecyclerView?.hide()
        if (e is UnknownHostException
            || e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }

        globalError?.show()
        globalError?.setOnClickListener {
            catalogLandingRecyclerView?.show()
            global_error_page.hide()
            addShimmer()
            updateUi()
            getDataFromViewModel()
        }
    }

    private fun getDataFromViewModel() {
        landingPageViewModel?.getCatalogTopFiveData(categoryName, TOP_FIVE_SORT_TYPE, TOP_FIVE_TOTAL_ROWS)
        landingPageViewModel?.getCatalogMostViralData(categoryName, VIRAL_SORT_TYPE, VIRAL_TOTAL_ROWS)
        landingPageViewModel?.getCatalogListData(categoryName, "", "10")
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.apply {
            updateModel(CatalogShimmerDataModel(CatalogLibraryConstant.CATALOG_TOP_FIVE, CatalogLibraryConstant.CATALOG_TOP_FIVE, CatalogLibraryConstant.CATALOG_SHIMMER_TOP_FIVE))
            updateModel(CatalogShimmerDataModel(CatalogLibraryConstant.CATALOG_MOST_VIRAL, CatalogLibraryConstant.CATALOG_MOST_VIRAL, CatalogLibraryConstant.CATALOG_SHIMMER_VIRAL))
            updateModel(CatalogShimmerDataModel(CatalogLibraryConstant.CATALOG_LIST, CatalogLibraryConstant.CATALOG_LIST, CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS))
        }
    }

    override fun onProductCardClicked(applink: String?) {
        super.onProductCardClicked(applink)
        RouteManager.route(context, applink)
    }
}
