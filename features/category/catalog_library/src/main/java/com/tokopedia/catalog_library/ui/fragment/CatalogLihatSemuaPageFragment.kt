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
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogLihatSemuaPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLihatSemuaPageFragment : BaseDaggerFragment(), CatalogLibraryListener {

    private var catalogLihatPageRecyclerView: RecyclerView? = null
    private var sortAsc: ChipsUnify? = null
    private var sortDesc: ChipsUnify? = null
    private var globalError: GlobalError? = null

    companion object {
        const val DEFAULT_ASC_SORT_ORDER = "0"
        const val DESC_SORT_ORDER = "1"
        fun newInstance(): CatalogLihatSemuaPageFragment {
            return CatalogLihatSemuaPageFragment()
        }
    }

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private val lihatViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get(CatalogLihatSemuaPageViewModel::class.java)
        }
    }

    private val catalogLibraryAdapterFactory by lazy(LazyThreadSafetyMode.NONE) {
        CatalogHomepageAdapterFactoryImpl(
            this
        )
    }
    private val catalogLibraryAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogLibraryDataModel> =
            AsyncDifferConfig.Builder(CatalogLibraryDiffUtil()).build()
        CatalogLibraryAdapter(asyncDifferConfig, catalogLibraryAdapterFactory)
    }

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf())

    override fun getScreenName(): String {
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
        return inflater.inflate(R.layout.fragment_catalog_lihat_semua_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setObservers()
        initData()
    }

    private fun initData() {
        lihatViewModel?.getLihatSemuaPageData(DEFAULT_ASC_SORT_ORDER)
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.lihat_semua_page_header).apply {
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun initViews(view: View) {
        globalError = view.findViewById(R.id.global_error_page)
        sortAsc = view.findViewById(R.id.chip_sort_asc)
        sortDesc = view.findViewById(R.id.chip_sort_desc)
        sortAsc?.setOnClickListener {
            sortAsc?.chipType = ChipsUnify.TYPE_SELECTED
            sortDesc?.chipType = ChipsUnify.TYPE_NORMAL
            lihatViewModel?.getLihatSemuaPageData(DEFAULT_ASC_SORT_ORDER)
        }
        sortDesc?.setOnClickListener {
            sortAsc?.chipType = ChipsUnify.TYPE_NORMAL
            sortDesc?.chipType = ChipsUnify.TYPE_SELECTED
            lihatViewModel?.getLihatSemuaPageData(DESC_SORT_ORDER)
        }
        initHeaderTitle(view)
        setupRecyclerView(view)
        addShimmer()
    }

    private fun setupRecyclerView(view: View) {
        catalogLihatPageRecyclerView = view.findViewById(R.id.catalog_lihat_semua_rv)
        catalogLihatPageRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = catalogLibraryAdapter
            setHasFixedSize(true)
        }
    }

    private fun setObservers() {
        lihatViewModel?.catalogLihatLiveDataResponse?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    catalogLibraryUiUpdater.removeModel(CatalogLibraryConstant.CATALOG_LIHAT_SEMUA)
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
        catalogLihatPageRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDataModel>) {
        catalogLibraryAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        catalogLihatPageRecyclerView?.hide()
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
        }

        globalError?.show()
        globalError?.errorAction?.setOnClickListener {
            catalogLihatPageRecyclerView?.show()
            globalError?.hide()
            lihatViewModel?.getLihatSemuaPageData(DEFAULT_ASC_SORT_ORDER)
        }
    }

    override fun onCategoryItemClicked(categoryIdentifier: String?) {
        super.onCategoryItemClicked(categoryIdentifier)
        RouteManager.route(
            context,
            "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryIdentifier"
        )
    }

    private fun addShimmer() {
        catalogLibraryUiUpdater.apply {
            updateModel(
                CatalogShimmerDataModel(
                    CatalogLibraryConstant.CATALOG_LIHAT_SEMUA,
                    CatalogLibraryConstant.CATALOG_LIHAT_SEMUA,
                    CatalogLibraryConstant.CATALOG_SHIMMER_LIHAT_SEMUA
                )
            )
            updateUi()
        }
    }
}
