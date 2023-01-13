package com.tokopedia.catalog_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.content.ContextCompat
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
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogLihatSemuaPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_homepage.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogLihatSemuaPageFragment : BaseDaggerFragment(), CatalogLibraryListener {

    private var catalogLihatPageRecyclerView: RecyclerView? = null
    private var shimmerLayout: ScrollView? = null
    private var sortAsc: Typography? = null
    private var sortDesc: Typography? = null

    companion object {
        const val CATALOG_LIHAT_PAGE_FRAGMENT_TAG = "CATALOG_LIHAT_PAGE_FRAGMENT_TAG"
        const val DEFAULT_ASC_SORT_ORDER = "0"
        const val DESC_SORT_ORDER = "1"
        const val DEVICE = ""
        const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
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
        shimmerLayout = view.findViewById(R.id.shimmer_layout)
        sortAsc = view.findViewById(R.id.sort_order_0)
        sortDesc = view.findViewById(R.id.sort_order_1)
        sortDesc?.setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        )

        initHeaderTitle(view)
        activity?.let {
            lihatViewModel?.getLihatSemuaPageData(DEFAULT_ASC_SORT_ORDER, DEVICE)
            showShimmer()
        }
        sortAsc?.setOnClickListener {
            lihatViewModel?.getLihatSemuaPageData(DEFAULT_ASC_SORT_ORDER, DEVICE)
            sortDesc?.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                )
            )
        }
        sortDesc?.setOnClickListener {
            lihatViewModel?.getLihatSemuaPageData(DESC_SORT_ORDER, DEVICE)
            sortAsc?.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                )
            )
        }
        setupRecyclerView(view)
        setObservers()
    }

    private fun initHeaderTitle(view: View) {
        view.findViewById<HeaderUnify>(R.id.lihat_semua_page_header).apply {
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
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
        catalogLihatPageRecyclerView?.show()
        val newData = catalogLibraryUiUpdater.mapOfData.values.toList()
        submitList(newData)
    }

    private fun submitList(visitable: List<BaseCatalogLibraryDataModel>) {
        catalogLibraryAdapter.submitList(visitable)
    }

    private fun onError(e: Throwable) {
        shimmerLayout?.hide()
        catalogLihatPageRecyclerView?.hide()
        if (e is UnknownHostException ||
            e is SocketTimeoutException
        ) {
            global_error_page.setType(GlobalError.NO_CONNECTION)
        } else {
            global_error_page.setType(GlobalError.SERVER_ERROR)
        }

        global_error_page.show()
        global_error_page.setOnClickListener {
            catalogLihatPageRecyclerView?.show()
            shimmerLayout?.show()
            global_error_page.hide()
            lihatViewModel?.getLihatSemuaPageData(DEFAULT_ASC_SORT_ORDER, DEVICE)
        }
    }

    override fun onCategoryItemClicked(categoryName: String?) {
        super.onCategoryItemClicked(categoryName)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(CatalogLandingPageFragment::class.java.simpleName)
            .replace(
                R.id.parent_view,
                CatalogLandingPageFragment.newInstance(categoryName),
                CatalogLandingPageFragment.CATALOG_LANDING_PAGE_FRAGMENT_TAG
            ).commit()
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
}
