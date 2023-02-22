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
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogHomepageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogHomepageFragment : ProductsBaseFragment(), CatalogLibraryListener {

    private var catalogHomeRecyclerView: RecyclerView? = null
    private var globalError: GlobalError? = null

    companion object {
        fun getInstance(): CatalogHomepageFragment {
            return CatalogHomepageFragment()
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private val homepageViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(CatalogHomepageViewModel::class.java)
        }
    }
    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }

    private val catalogHomeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDataModel> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf()).also {
            it.setUpForHomePage()
        }

    override var baseRecyclerView: RecyclerView?
        get() = catalogHomeRecyclerView
        set(value) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_catalog_homepage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromViewModel()
        initViews(view)
        setUpBase()
        setObservers()
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        initHeaderTitle(view)
        setupRecyclerView(view)
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.catalog_home_header).apply {
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun getDataFromViewModel() {
        homepageViewModel?.getSpecialData()
        homepageViewModel?.getRelevantData()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerCatalogLibraryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun setupRecyclerView(view: View) {
        catalogHomeRecyclerView = view.findViewById(R.id.catalog_home_rv)
        catalogHomeRecyclerView?.apply {
            layoutManager = getLinearLayoutManager()
            adapter = catalogHomeAdapter
            setHasFixedSize(true)
        }
        updateUi()
    }

    private fun setObservers() {
        homepageViewModel?.catalogLibraryLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
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

    private fun updateUi() {
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDataModel>) {
        catalogHomeAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }
        catalogHomeRecyclerView?.hide()
        globalError?.show()
        globalError?.errorAction?.setOnClickListener {
            catalogHomeRecyclerView?.show()
            globalError?.hide()
            getDataFromViewModel()
        }
    }

    override fun onLihatSemuaTextClick(applink: String) {
        super.onLihatSemuaTextClick(applink)
        RouteManager.route(context, applink)
    }

    override fun onProductCardClicked(applink: String?) {
        super.onProductCardClicked(applink)
        RouteManager.route(context, applink)
    }

    override fun onCategoryItemClicked(categoryIdentifier: String?) {
        super.onCategoryItemClicked(categoryIdentifier)
        RouteManager.route(
            context,
            "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryIdentifier"
        )
    }

    override fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDataModel>) {
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT)
        catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        productsList.forEach { component ->
            catalogLibraryUiUpdater.updateModel(component)
        }

        if (productsList.isEmpty()) {
            catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT_LOAD)
        } else {
            catalogLibraryUiUpdater.updateModel(CatalogProductLoadMoreDataModel())
        }
        updateUi()
    }

    override fun onShimmerAdded() {
    }

    override fun onErrorFetchingProducts(throwable: Throwable) {
        // TODO shimmer remove or show whole error logic
        onError(throwable)
    }
}
