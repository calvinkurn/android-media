package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailAdapter
import com.tokopedia.catalog.adapter.CatalogDetailDiffUtil
import com.tokopedia.catalog.adapter.decorators.CatalogItemOffSetDecoration
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactoryImpl
import com.tokopedia.catalog.di.CatalogComponent
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.ui.bottomsheet.CatalogComponentBottomSheetListener
import com.tokopedia.catalog.viewmodel.CatalogProductComparisonViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.fragment_catalog_detail_page.*
import javax.inject.Inject

class CatalogProductComparisonFragment : BaseViewModelFragment<CatalogProductComparisonViewModel>() , CatalogDetailListener{

    private var currentPageNumber : Int = 1
    private var listSize = 0
    private var catalogId = ""
    private var catalogName = ""
    private var brand  = ""
    private var categoryId = ""
    private var recommendedCatalogId: String = ""
    private var catalogDetailListener : CatalogDetailListener? = null
    private var catalogBottomSheetListener : CatalogComponentBottomSheetListener? = null

    @Inject
    lateinit var viewModelFactoryProvider : ViewModelProvider.Factory

    @Inject
    lateinit var catalogProductComparisonViewModel: CatalogProductComparisonViewModel

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private var recyclerView : RecyclerView? = null

    private val catalogAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { CatalogDetailAdapterFactoryImpl(this) }

    private val catalogDetailAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel> = AsyncDifferConfig.Builder(
            CatalogDetailDiffUtil()
        )
            .build()
        CatalogDetailAdapter(requireActivity(),this,catalogId,asyncDifferConfig, catalogAdapterFactory
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.catalog_comparison_product_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated(view)
    }

    private fun afterViewCreated(view: View) {
        extractArguments()
        setUpRecyclerView(view)
        setUpEmptyState()
        catalogProductComparisonViewModel.getComparisonProducts(recommendedCatalogId,
            catalogId,brand,
            categoryId,LIMIT,PAGE_FIRST.toString(),"")
    }

    private fun extractArguments() {
        catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        catalogName = requireArguments().getString(ARG_EXTRA_CATALOG_NAME, "")
        brand = requireArguments().getString(ARG_EXTRA_CATALOG_BRAND, "")
        categoryId = requireArguments().getString(ARG_EXTRA_CATALOG_CATEGORY_ID, "")
        recommendedCatalogId = requireArguments().getString(ARG_EXTRA_RECOMMENDED_CATALOG_ID, "")
    }

    private fun setUpRecyclerView(view : View){
        val staggeredLayoutManager = StaggeredGridLayoutManager(GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        view.findViewById<RecyclerView>(R.id.catalog_staggered_recycler_view)?.let { rV ->
            recyclerView = rV
            recyclerView?.apply {
                addItemDecoration(CatalogItemOffSetDecoration())
                layoutManager = staggeredLayoutManager
                loadMoreTriggerListener = getEndlessRecyclerViewListener(staggeredLayoutManager)
                adapter = catalogDetailAdapter
                loadMoreTriggerListener?.let { addOnScrollListener(it) }
            }
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(hasNextPage)
                    catalogProductComparisonViewModel.getComparisonProducts(recommendedCatalogId,
                        catalogId,brand,
                        categoryId,LIMIT,page.toString(),"")
            }
        }
    }

    private fun setUpEmptyState(){
        view?.findViewById<GlobalError>(R.id.global_error)?.run {
            errorIllustration.hide()
            errorSecondaryAction.gone()
            setButtonFull(true)
            errorTitle.text = ""
            errorDescription.text = ""
            errorAction.text = ""
            errorAction.setOnClickListener {

            }
        }
    }

    private fun setObservers() {
        catalogProductComparisonViewModel.getShimmerData().observe(this, { shimmerData ->
            recyclerView?.show()
            catalogDetailAdapter.submitList(shimmerData)
            catalogDetailAdapter.notifyDataSetChanged()
        })

        catalogProductComparisonViewModel.getDataItems().observe(this ,{ dataList ->
            if (dataList.isNotEmpty()) {
                listSize += dataList.size
                hideErrorGroup()
                recyclerView?.show()
                catalogDetailAdapter.submitList(dataList)
                catalogDetailAdapter.notifyDataSetChanged()
                loadMoreTriggerListener?.updateStateAfterGetData()
                currentPageNumber++
            } else {
                showErrorGroup()
                showEmptyState()
                recyclerView?.hide()
            }
        })

        catalogProductComparisonViewModel.getHasMoreItems().observe(this, { hasMoreItems ->
            if(hasMoreItems) {
                currentPageNumber ++
                loadMoreTriggerListener?.setHasNextPage(true)
            }
            else loadMoreTriggerListener?.setHasNextPage(false)
        })

        catalogProductComparisonViewModel.getErrorMessage().observe(this, { errorMessage ->
            recyclerView?.hide()
            showErrorGroup()
            showEmptyState()
        })
    }

    private fun showEmptyState(){
        view?.findViewById<DeferredImageView>(R.id.catalog_no_product_view)?.show()
    }

    private fun showErrorGroup(){
        view?.findViewById<DeferredImageView>(R.id.catalog_no_product_view)?.show()
        view?.findViewById<GlobalError>(R.id.global_error)?.show()
    }

    private fun hideErrorGroup() {
        view?.findViewById<DeferredImageView>(R.id.catalog_no_product_view)?.hide()
        view?.findViewById<GlobalError>(R.id.global_error)?.hide()
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelFactoryProvider
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): CatalogComponent =
        DaggerCatalogComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication)
                .baseAppComponent).build()

    override fun getViewModelType(): Class<CatalogProductComparisonViewModel> {
        return CatalogProductComparisonViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        catalogProductComparisonViewModel = viewModel as CatalogProductComparisonViewModel
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_NAME = "ARG_EXTRA_CATALOG_NAME"
        private const val ARG_EXTRA_CATALOG_BRAND = "ARG_EXTRA_CATALOG_BRAND"
        private const val ARG_EXTRA_CATALOG_CATEGORY_ID = "ARG_EXTRA_CATALOG_CATEGORY_ID"
        private const val ARG_EXTRA_RECOMMENDED_CATALOG_ID = "ARG_EXTRA_RECOMMENDED_CATALOG_ID"

        private const val GRID_SPAN_COUNT: Int = 2
        private const val LIMIT = 10
        private const val PAGE_FIRST = 1

        fun newInstance(catalogName : String, catalogId: String, brand: String, categoryId : String,
                        recommendedCatalogId : String,
                        catalogListener: CatalogDetailListener? ,
                        catalogComponentBottomSheetListener: CatalogComponentBottomSheetListener?):
                CatalogProductComparisonFragment {
            return CatalogProductComparisonFragment().apply {
                val bundle = Bundle()
                bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
                bundle.putString(ARG_EXTRA_CATALOG_NAME, catalogName)
                bundle.putString(ARG_EXTRA_CATALOG_BRAND, brand)
                bundle.putString(ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
                bundle.putString(ARG_EXTRA_RECOMMENDED_CATALOG_ID, recommendedCatalogId)
                arguments = bundle
                catalogDetailListener = catalogListener
                catalogBottomSheetListener = catalogComponentBottomSheetListener
            }
        }
    }

    override fun changeComparison(comparedCatalogId: String) {
        dismissBottomSheet()
        catalogDetailListener?.changeComparison(comparedCatalogId)
    }

    private fun dismissBottomSheet () {
        catalogBottomSheetListener?.dismissCatalogComponentBottomSheet()
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