package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailAdapter
import com.tokopedia.catalog.adapter.CatalogDetailDiffUtil
import com.tokopedia.catalog.adapter.decorators.DividerItemDecorator
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactoryImpl
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogFullSpecificationDataModel
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.model.util.CatalogUiUpdater
import com.tokopedia.catalog.model.util.nestedrecyclerview.NestedRecyclerView
import com.tokopedia.catalog.ui.activity.CatalogGalleryActivity
import com.tokopedia.catalog.ui.bottomsheet.CatalogPreferredProductsBottomSheet
import com.tokopedia.catalog.ui.bottomsheet.CatalogSpecsAndDetailBottomSheet
import com.tokopedia.catalog.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.share.DefaultShare
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_catalog_detail_page.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CatalogDetailPageFragment : Fragment(),
        HasComponent<CatalogComponent>, CatalogDetailListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var catalogDetailPageViewModel: CatalogDetailPageViewModel

    private var catalogUiUpdater: CatalogUiUpdater? = CatalogUiUpdater(mutableMapOf())
    private var fullSpecificationDataModel = CatalogFullSpecificationDataModel(arrayListOf())

    private var catalogId: String = ""

    private var navToolbar: NavToolbar? = null
    private var cartLocalCacheHandler: LocalCacheHandler? = null

    private var catalogPageRecyclerView: NestedRecyclerView? = null
    private var shimmerLayout : ScrollView? = null
    private var mBottomSheetBehavior : BottomSheetBehavior<FrameLayout>? = null

    private val catalogAdapterFactory by lazy { CatalogDetailAdapterFactoryImpl(this) }

    private val catalogDetailAdapter by lazy {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel> = AsyncDifferConfig.Builder(CatalogDetailDiffUtil())
                .build()
        CatalogDetailAdapter(requireActivity(),this,catalogId,asyncDifferConfig, catalogAdapterFactory
        )
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        var isBottomSheetOpen = false

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
        initViews()
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        }
        activity?.let { observer ->
            val viewModelProvider = ViewModelProvider(observer, viewModelFactory)
            catalogDetailPageViewModel = viewModelProvider.get(CatalogDetailPageViewModel::class.java)
            catalogDetailPageViewModel.getProductCatalog(catalogId)
            showShimmer()
        }

        setupRecyclerView(view)
        setObservers()
        setUpBottomSheet()
    }

    fun setCatalogId(catalogId: String){
        this.catalogId = catalogId
        requireArguments().putString(ARG_EXTRA_CATALOG_ID,catalogId)
    }

    private fun initViews() {
        shimmerLayout = view?.findViewById(R.id.shimmer_layout)
        activity?.let {
            cartLocalCacheHandler = LocalCacheHandler(it, CatalogConstant.CART_LOCAL_CACHE_NAME)
        }
        initNavToolbar()
    }

    private fun setUpBottomSheet(){
        requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.bottom_sheet_fragment_container, CatalogPreferredProductsBottomSheet.newInstance(catalogId)
        ).commit()

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_fragment_container)
        mBottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        isBottomSheetOpen = false
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        isBottomSheetOpen = true
                    }
                }
            }
        })
    }

    private fun setObservers() {
        catalogDetailPageViewModel.getCatalogResponseData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.listOfComponents.forEach { component ->
                        catalogUiUpdater?.updateModel(component)
                    }
                    fullSpecificationDataModel = it.data.fullSpecificationDataModel
                    updateUi()
                }
                is Fail -> {
                    onError(it.throwable)
                }
            }

        })
    }


    private fun onError(e: Throwable) {
        shimmerLayout?.hide()
        catalogPageRecyclerView?.hide()
        bottom_sheet_fragment_container.hide()
        if (e is UnknownHostException
                || e is SocketTimeoutException) {
            global_error.setType(GlobalError.NO_CONNECTION)
        } else {
            global_error.setType(GlobalError.SERVER_ERROR)
        }

        global_error.show()
        global_error.setOnClickListener {
            catalogPageRecyclerView?.show()
            shimmerLayout?.show()
            bottom_sheet_fragment_container.show()
            global_error.hide()
            catalogDetailPageViewModel.getProductCatalog(catalogId)
        }
    }

    private fun initNavToolbar() {
        navToolbar = view?.findViewById(R.id.catalog_navtoolbar)
        navToolbar?.apply {
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_SHARE) {
                                generateCatalogShareData(catalogId)
                            }
                            .addIcon(IconList.ID_CART) {}
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            setBadgeCounter(IconList.ID_CART, getCartCounter())
            show()
        }
    }

    private fun getCartCounter(): Int {
        return cartLocalCacheHandler?.getInt(CatalogConstant.TOTAL_CART_CACHE_KEY, 0).orZero()
    }

    private fun showShimmer(){
        if(catalogUiUpdater?.mapOfData?.size ?: 0 == 0)
            shimmerLayout?.show()
    }

    private fun hideShimmer(){
        if(catalogUiUpdater?.mapOfData?.size ?: 0 > 0)
            shimmerLayout?.hide()
    }

    private fun updateUi() {
        hideShimmer()
        catalogPageRecyclerView?.show()
        bottom_sheet_fragment_container.show()
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
            setHasFixedSize(true)
            setNestedCanScroll(true)
            itemAnimator = null
            ContextCompat.getDrawable(context, R.drawable.catalog_divider)?.let {
                addItemDecoration(DividerItemDecorator(it))
            }
            adapter = catalogDetailAdapter
        }
    }

    private fun viewMoreClicked(openPage : String) {
        val catalogSpecsAndDetailView = CatalogSpecsAndDetailBottomSheet.newInstance(
                catalogUiUpdater?.productInfoMap?.description ?: "",
                fullSpecificationDataModel.fullSpecificationsList
                ,openPage
        )
        catalogSpecsAndDetailView.show(childFragmentManager, "")
    }

    private fun generateCatalogShareData(catalogId: String) {
        val shareData =  LinkerData.Builder.getLinkerBuilder()
                .setId(catalogId)
                .setName(getString(R.string.catalog_message_share_catalog))
                .setType(LinkerData.CATALOG_TYPE)
                .setTextContent(getString(R.string.catalog_share_text_content))
                .setUri(CatalogConstant.CATALOG_URL)
                .build()

        shareData.type = LinkerData.CATALOG_TYPE
        DefaultShare(activity, shareData).show()
    }

    private fun showImage(currentItem: Int) {
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
        showImage(position)
    }

    override fun onViewMoreSpecificationsClick() {
        viewMoreClicked(CatalogSpecsAndDetailBottomSheet.SPECIFICATION)
    }

    override fun hideFloatingLayout() {
        bottom_sheet_fragment_container.hide()
    }

    override fun showFloatingLayout() {
        bottom_sheet_fragment_container.show()
    }

    override fun onViewMoreDescriptionClick() {
        viewMoreClicked(CatalogSpecsAndDetailBottomSheet.DESCRIPTION)
    }

    fun onBackPressed(){
        mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
    }

    override val childsFragmentManager: FragmentManager?
        get() = childFragmentManager


    override val windowHeight: Int
        get() = if (activity != null) {
            catalog_layout.height
        } else {
            0
        }

}
