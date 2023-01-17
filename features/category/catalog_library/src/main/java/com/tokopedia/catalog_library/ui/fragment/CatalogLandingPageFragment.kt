package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogProductLoadMoreDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.SORT_TYPE_TOP_FIVE
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.SORT_TYPE_VIRAL
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.TOTAL_ROWS_TOP_FIVE
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.TOTAL_ROWS_VIRAL
import com.tokopedia.catalog_library.model.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogLandingPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_homepage.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLandingPageFragment : ProductsBaseFragment(), CatalogLibraryListener {

    private var globalError: GlobalError? = null
    private var categoryIdentifierStr = ""
    private var categoryName = ""
    private var catalogLandingRecyclerView: RecyclerView? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val landingPageViewModel: CatalogLandingPageViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(CatalogLandingPageViewModel::class.java)
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

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf()).also {
            it.setUpForLandingPage()
        }

    companion object {
        const val ARG_CATEGORY_IDENTIFIER = "ARG_CATEGORY_IDENTIFIER"
        fun newInstance(categoryIdentifier: String?): CatalogLandingPageFragment {
            return CatalogLandingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_IDENTIFIER, categoryIdentifier)
                }
            }
        }
    }

    override var baseRecyclerView: RecyclerView?
        get() = catalogLandingRecyclerView
        set(value) {}

    override fun getScreenName(): String {
        // TODO send Screen Name if any GTM
        return ""
    }

    override fun initInjector() {
        DaggerCatalogLibraryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
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
        extractArguments()
        initViews(view)
        setCategory(categoryIdentifierStr)
        setUpBase()
        initData()
    }

    private fun extractArguments() {
        categoryIdentifierStr = arguments?.getString(ARG_CATEGORY_IDENTIFIER, "") ?: ""
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
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
            layoutManager = getLinearLayoutManager()
            adapter = catalogLandingPageAdapter
            setHasFixedSize(true)
        }
    }

    private fun setObservers() {
        landingPageViewModel.catalogLandingPageLiveDataResponse.observe(viewLifecycleOwner) {
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
            landingPageViewModel.categoryName.observe(viewLifecycleOwner) { categoryName ->
                this.categoryName = categoryName
                view?.let { v -> initHeaderTitle(v) }
            }
        }
    }

    private fun initHeaderTitle(view: View) {
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
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }

        globalError?.show()
        globalError?.errorAction?.setOnClickListener {
            catalogLandingRecyclerView?.show()
            global_error_page.hide()
            addShimmer()
            updateUi()
            getDataFromViewModel()
        }
    }

    private fun getDataFromViewModel() {
        landingPageViewModel.getCatalogTopFiveData(
            categoryIdentifierStr,
            SORT_TYPE_TOP_FIVE,
            TOTAL_ROWS_TOP_FIVE
        )
        landingPageViewModel.getCatalogMostViralData(
            categoryIdentifierStr,
            SORT_TYPE_VIRAL,
            TOTAL_ROWS_VIRAL
        )
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.apply {
            updateModel(CatalogShimmerDataModel(CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE, CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE, CatalogLibraryConstant.CATALOG_SHIMMER_TOP_FIVE))
            updateModel(
                CatalogShimmerDataModel(
                    CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                    CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                    CatalogLibraryConstant.CATALOG_SHIMMER_VIRAL
                )
            )
            updateModel(
                CatalogShimmerDataModel(
                    CatalogLibraryConstant.CATALOG_PRODUCT,
                    CatalogLibraryConstant.CATALOG_PRODUCT,
                    CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
                )
            )
        }
    }

    override fun onProductCardClicked(applink: String?) {
        super.onProductCardClicked(applink)
        RouteManager.route(context, applink)
    }

    override fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDataModel>) {
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT)
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        productsList.forEach { component ->
            catalogLibraryUiUpdater.updateModel(component)
        }

        if(productsList.isEmpty()){
            catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        }else {
            catalogLibraryUiUpdater.updateModel(CatalogProductLoadMoreDataModel())
        }

//        val shimmerDataModel = CatalogShimmerDataModel(
//            CatalogLibraryConstant.CATALOG_PRODUCT,
//            CatalogLibraryConstant.CATALOG_PRODUCT,
//            CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
//        )
//        catalogLibraryUiUpdater.updateModel(shimmerDataModel)
        updateUi()
    }

    override fun onShimmerAdded() {
    }

    override fun onErrorFetchingProducts(throwable: Throwable) {
        onError(throwable)
    }
}
