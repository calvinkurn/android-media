package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.PopularBrandsVM
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PopularBrandsFragment : BaseDaggerFragment(), CatalogLibraryListener {

    private var popularBrandsRecyclerView: RecyclerView? = null
    private var globalError: GlobalError? = null

    companion object {
        fun newInstance(): PopularBrandsFragment {
            return PopularBrandsFragment()
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private val popularBrandsVM by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(PopularBrandsVM::class.java)
        }
    }
    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }
    private val popularBrandsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDM> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf())

    override fun getScreenName(): String {
        TODO("Not yet implemented")
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
        return inflater.inflate(R.layout.fragment_popular_brands, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setObservers()
        getData()
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        initHeaderTitle(view)
        setupRecyclerView(view)
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.popular_brands_header).apply {
            headerTitle = getString(R.string.brand_terpopuler)
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }
    private fun setupRecyclerView(view: View) {
        popularBrandsRecyclerView = view.findViewById(R.id.category_landing_rv)
        popularBrandsRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = popularBrandsAdapter
            setHasFixedSize(true)
        }
    }

    private fun getData() {
        popularBrandsVM?.getBrandsWithCatalogs()
    }

    private fun setObservers() {
        popularBrandsVM?.brandsWithCatalogsLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
                        catalogLibraryUiUpdater.updateModel(component)
                    }
                    updateUi()
                }

                is Fail -> {
//                    onError(it.throwable)
                }
            }
        }
    }

    private fun updateUi() {
        popularBrandsRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDM>) {
        popularBrandsAdapter.submitList(visitable)
    }
}
