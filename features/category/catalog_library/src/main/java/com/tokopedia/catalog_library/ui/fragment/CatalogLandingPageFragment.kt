package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.CatalogLibraryComponent
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
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
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLandingPageFragment: Fragment(),
    HasComponent<CatalogLibraryComponent>, CatalogLibraryListener {

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
    private var shimmerLayout: ScrollView? = null

    private val categoryIdentifier = ""
    private val sortType = "0"
    private val rows = ""

    companion object {
        const val CATALOG_LANDING_PAGE_FRAGMENT_TAG = "CATALOG_LANDING_PAGE_FRAGMENT_TAG"
        const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
        fun newInstance(categoryName: String?): CatalogLandingPageFragment {
            val fragment = CatalogLandingPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_CATEGORY_NAME, categoryName)
            fragment.arguments = bundle
            return CatalogLandingPageFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        initHeaderTitle(view)
//        view.findViewById<HeaderUnify>(R.id.clp_header)
//        val headerUnify = HeaderUnify(view.context)
        //clp_header.headerTitle = arguments?.getString(ARG_CATEGORY_NAME, "").toString()
        activity?.let {
            getDataFromViewModel()
            showShimmer()
        }
        setupRecyclerView(view)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_landing_page, container, false)
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
        //view.findViewById<HeaderUnify>(R.id.clp_header)
        val headerUnify = HeaderUnify(view.context)
        val bundle = Bundle()
        clp_header.headerTitle = arguments?.getString(ARG_CATEGORY_NAME, "").toString()
    }

    private fun updateUi() {
        hideShimmer()
        catalogLandingRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDataModel>) {
        catalogLandingPageAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        shimmerLayout?.hide()
        catalogLandingRecyclerView?.hide()
        if (e is UnknownHostException
            || e is SocketTimeoutException
        ) {
            global_error_page.setType(GlobalError.NO_CONNECTION)
        } else {
            global_error_page.setType(GlobalError.SERVER_ERROR)
        }

        global_error_page.show()
        global_error_page.setOnClickListener {
            catalogLandingRecyclerView?.show()
            shimmerLayout?.show()
            global_error_page.hide()
            getDataFromViewModel()
        }
    }

    private fun getDataFromViewModel() {
        landingPageViewModel?.getCatalogTopFiveData("handphone", "6", "5")
        landingPageViewModel?.getCatalogMostViralData("handphone", "5", "1")
        landingPageViewModel?.getCatalogListData("handphone", "", "")
    }

    private fun showShimmer() {
        if (catalogLibraryUiUpdater.mapOfData.isEmpty())
            shimmerLayout?.show()
    }

    private fun hideShimmer() {
        if (catalogLibraryUiUpdater.mapOfData.isNotEmpty())
            shimmerLayout?.hide()
    }

}
