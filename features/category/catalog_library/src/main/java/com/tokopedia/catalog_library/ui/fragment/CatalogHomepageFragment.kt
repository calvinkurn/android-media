package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
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
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.model.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogHomepageViewModel
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

class CatalogHomepageFragment : ProductsBaseFragment(), CatalogLibraryListener {

    private var shimmerLayout: ScrollView? = null
    private var catalogHomeRecyclerView: RecyclerView? = null

    companion object {
        const val CATALOG_HOME_PAGE_FRAGMENT_TAG = "CATALOG_HOME_PAGE_FRAGMENT_TAG"
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
        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        initHeaderTitle(view)
        getDataFromViewModel()
        showShimmer()
        setupRecyclerView(view)
        setUpBase()
        setObservers()
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

    private fun setupRecyclerView(view: View) {
        catalogHomeRecyclerView = view.findViewById(R.id.catalog_home_rv)
        catalogHomeRecyclerView?.apply {
            // TODO :: Check if this layout manager isn't affecting on other Fragment instances
            layoutManager = getLinearLayoutManager()
            adapter = catalogHomeAdapter
            setHasFixedSize(true)
        }
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
        hideShimmer()
        catalogHomeRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDataModel>) {
        catalogHomeAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        shimmerLayout?.hide()
        catalogHomeRecyclerView?.hide()
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            global_error_page.setType(GlobalError.NO_CONNECTION)
        } else {
            global_error_page.setType(GlobalError.SERVER_ERROR)
        }

        global_error_page.show()
        global_error_page.setOnClickListener {
            catalogHomeRecyclerView?.show()
            shimmerLayout?.show()
            global_error_page.hide()
            getDataFromViewModel()
        }
    }

    private fun showShimmer() {
        if (catalogLibraryUiUpdater.mapOfData.isEmpty()) {
            shimmerLayout?.show()
        }
    }

    private fun hideShimmer() {
        if (catalogLibraryUiUpdater.mapOfData.isNotEmpty()) {
            shimmerLayout?.hide()
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

    override fun onCategoryItemClicked(categoryName: String?) {
        super.onCategoryItemClicked(categoryName)
        RouteManager.route(context, "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryName")
    }

    override fun onProductsLoaded(productsList: MutableList<BaseCatalogLibraryDataModel>) {
        productsList.forEach { component ->
            catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_PRODUCT)
            catalogLibraryUiUpdater.updateModel(component)
        }
        updateUi()
    }

    override fun onShimmerAdded() {
        val shimmerDataModel = CatalogShimmerDataModel(
            CatalogLibraryConstant.CATALOG_PRODUCT,
            CatalogLibraryConstant.CATALOG_PRODUCT,
            CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
        )
        catalogLibraryUiUpdater.updateModel(shimmerDataModel)
        updateUi()
    }

    override fun onErrorFetchingProducts(throwable: Throwable) {
        onError(throwable)
    }
}
