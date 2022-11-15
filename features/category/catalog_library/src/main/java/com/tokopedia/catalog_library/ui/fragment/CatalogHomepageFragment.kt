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
import com.tokopedia.applink.RouteManager
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.adapter.CatalogLibraryDiffUtil
import com.tokopedia.catalog_library.adapter.CatalogLibraryAdapter
import com.tokopedia.catalog_library.adapter.factory.CatalogHomepageAdapterFactoryImpl
import com.tokopedia.catalog_library.di.CatalogLibraryComponent
import com.tokopedia.catalog_library.di.DaggerCatalogLibraryComponent
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryUiUpdater
import com.tokopedia.catalog_library.viewmodels.CatalogHomepageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_catalog_homepage.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogHomepageFragment : Fragment(),
    HasComponent<CatalogLibraryComponent>, CatalogLibraryListener {

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

    private var catalogHomeRecyclerView: RecyclerView? = null

    private var catalogLibraryUiUpdater: CatalogLibraryUiUpdater =
        CatalogLibraryUiUpdater(mutableMapOf())
    private var shimmerLayout: ScrollView? = null

    private var userSession: UserSession? = null

    private val categoryId = ""
    private val categoryIdentifier = ""
    private val brandIdentifier = ""
    private val keyword = ""
    private val sortType = "0"
    private val page = ""
    private val rows = ""

    companion object {
        const val CATALOG_HOME_PAGE_FRAGMENT_TAG = "CATALOG_HOME_PAGE_FRAGMENT_TAG"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        shimmerLayout = view.findViewById(R.id.shimmer_layout)

        activity?.let { observer ->
            userSession = UserSession(observer)
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
        return inflater.inflate(R.layout.fragment_catalog_homepage, container, false)
    }

    private fun setupRecyclerView(view: View) {
        catalogHomeRecyclerView = view.findViewById(R.id.catalog_home_rv)
        catalogHomeRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    override fun getComponent(): CatalogLibraryComponent {
        return DaggerCatalogLibraryComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
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
        if (e is UnknownHostException
            || e is SocketTimeoutException
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

    private fun getDataFromViewModel() {
        homepageViewModel?.getSpecialData(userSession?.userId)
        homepageViewModel?.getRelevantData()
        homepageViewModel?.getCatalogListData(categoryIdentifier, sortType, rows)
    }

    private fun showShimmer() {
        if (catalogLibraryUiUpdater.mapOfData.isEmpty())
            shimmerLayout?.show()
    }

    private fun hideShimmer() {
        if (catalogLibraryUiUpdater.mapOfData.isNotEmpty())
            shimmerLayout?.hide()
    }

    override fun onLihatSemuaTextClick() {
        super.onLihatSemuaTextClick()
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack("").replace(
            R.id.catalog_home_frag,
            CatalogLihatSemuaPageFragment(),
            CatalogLihatSemuaPageFragment.CATALOG_LIHAT_PAGE_FRAGMENT_TAG
        ).commit()

    }

    override fun onProductCardClicked(applink: String?) {
        super.onProductCardClicked(applink)
        RouteManager.route(context, applink)
    }
}
