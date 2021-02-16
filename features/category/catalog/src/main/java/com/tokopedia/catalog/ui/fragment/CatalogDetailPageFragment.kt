package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailAdapter
import com.tokopedia.catalog.adapter.CatalogDetailDiffUtil
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactoryImpl
import com.tokopedia.catalog.analytics.CatalogDetailPageAnalytics
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.SpecificationsComponentData
import com.tokopedia.catalog.model.util.CatalogUiUpdater
import com.tokopedia.catalog.ui.activity.CatalogGalleryActivity
import com.tokopedia.catalog.ui.bottomsheet.CatalogPreferredProductsBottomSheet
import com.tokopedia.catalog.ui.bottomsheet.CatalogSpecsAndDetailBottomSheet
import com.tokopedia.catalog.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_detail_page.*
import javax.inject.Inject

class CatalogDetailPageFragment : Fragment(),
        HasComponent<CatalogComponent>, CatalogDetailListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel

    private var catalogUiUpdater: CatalogUiUpdater? = CatalogUiUpdater(mutableMapOf())

    private var catalogId: String = ""
    private lateinit var fragment: CatalogGalleryFragment

    private var navToolbar: NavToolbar? = null
    private var catalogPageRecyclerView: RecyclerView? = null
    private var shimmerLayout : ScrollView? = null

    private val catalogAdapterFactory by lazy { CatalogDetailAdapterFactoryImpl(this) }

    private val catalogDetailAdapter by lazy {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel> = AsyncDifferConfig.Builder(CatalogDetailDiffUtil())
                .build()
        CatalogDetailAdapter(asyncDifferConfig, this, catalogAdapterFactory
        )
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"

        fun newInstance(catalogId: String): CatalogDetailPageFragment {
            val fragment = CatalogDetailPageFragment()
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getComponent(): CatalogComponent {
        return DaggerCatalogComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        initNavToolbar()
        initViews()
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        }
        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            catalogDetailPageViewModel = viewModelProvider.get(CatalogDetailPageViewModel::class.java)
            catalogDetailPageViewModel.getProductCatalog(catalogId)
            showShimmer()
        }

        setupRecyclerView(view)
        setObservers()

//        Handler().postDelayed({
//            CatalogPreferredProductsBottomSheet.newInstance().show(childFragmentManager,"")
//        },3000)
    }

    private fun initViews() {
        shimmerLayout = view?.findViewById(R.id.shimmer_layout)
    }

    private fun setObservers() {
        catalogDetailPageViewModel.getCatalogResponseData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
                        catalogUiUpdater?.updateModel(component)
                    }
                    updateUi()
                }
                is Fail -> {

                }
            }

        })
    }

    fun setListener(listener: Listener) {

    }

    private fun initNavToolbar() {
        navToolbar = view?.findViewById(R.id.catalog_navtoolbar)
        navToolbar?.apply {
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_SHARE) {}
                            .addIcon(IconList.ID_CART) {}
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            //setupSearchbar(listOf(HintData(getString(R.string.pdp_search_hint, ""))))
            show()
        }
    }

    private fun showShimmer(){
        if(catalogUiUpdater?.mapOfData?.size ?: 0 == 0)
            shimmerLayout?.visibility = View.VISIBLE
    }

    private fun hideShimmer(){
        if(catalogUiUpdater?.mapOfData?.size ?: 0 > 0)
            shimmerLayout?.visibility = View.GONE
    }

    private fun updateUi() {
        hideShimmer()
        val newData = catalogUiUpdater?.mapOfData?.values?.toList()
        submitList(newData ?: listOf())
    }

    private fun submitList(visitables: List<BaseCatalogDataModel>) {
        catalogDetailAdapter.submitList(visitables)
    }

    private fun setupRecyclerView(view: View) {
        catalogPageRecyclerView = view.findViewById(R.id.catalog_detail_rv)
        catalogPageRecyclerView?.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
            adapter = catalogDetailAdapter
        }
    }

    private fun viewMoreClicked(openPage : String) {
        CatalogDetailPageAnalytics.trackEventClickSpecification()
        val catalogSpecsAndDetailView = CatalogSpecsAndDetailBottomSheet.newInstance(
                catalogUiUpdater?.productInfoMap?.description ?: "",
                catalogUiUpdater?.specificationsMap?.specificationsList ?: arrayListOf()
                ,openPage
        )
        catalogSpecsAndDetailView.show(childFragmentManager, "")
    }


    fun onBackPress() {
        if (::fragment.isInitialized && fragment.isAdded) {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.exit_bottom, R.animator.exit_bottom)
                    .remove(fragment)
                    .commit()
        } else {
            activity?.finish()
        }
    }

    private fun generateCatalogShareData(catalogUrl: String, catalogId: String): LinkerData {
        return LinkerData.Builder.getLinkerBuilder()
                .setId(catalogId)
                .setName(getString(R.string.catalog_message_share_catalog))
                .setType(LinkerData.CATALOG_TYPE)
                .setTextContent(getString(R.string.catalog_share_text_content))
                .setUri(catalogUrl)
                .build()
    }

    private fun showImage(catalogImage: CatalogImage, currentItem: Int) {
        catalogUiUpdater?.run {
            productInfoMap?.let {
                if(catalogUiUpdater?.productInfoMap?.images?.isNotEmpty() == true){
                    context?.startActivity(CatalogGalleryActivity.newIntent(context, currentItem, catalogUiUpdater!!.productInfoMap!!.images))
                }
            }
        }
    }


    interface Listener {
        fun deliverCatalogShareData(shareData: LinkerData, catalogHeading: String, departmentId: String)
    }

    override fun onProductImageClick(catalogImage: CatalogImage, position: Int) {
        showImage(catalogImage,position)
    }

    override fun onViewMoreSpecificationsClick() {
        viewMoreClicked(CatalogSpecsAndDetailBottomSheet.SPECIFICATION)
    }

    override fun onViewMoreDescriptionClick() {
        viewMoreClicked(CatalogSpecsAndDetailBottomSheet.DESCRIPTION)
    }
}
