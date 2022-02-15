package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.catalog.model.util.CatalogUtil
import com.tokopedia.catalog.ui.bottomsheet.CatalogComponentBottomSheet
import com.tokopedia.catalog.viewmodel.CatalogProductComparisonViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.SearchBarUnify
import javax.inject.Inject

class CatalogProductComparisonFragment : BaseViewModelFragment<CatalogProductComparisonViewModel>() , CatalogDetailListener{

    private var currentPageNumber = PAGE_FIRST
    private var catalogId = ""
    private var catalogName = ""
    private var brand  = ""
    private var categoryId = ""
    private var searchKeyword = ""
    private var recommendedCatalogId = ""

    @Inject
    lateinit var viewModelFactoryProvider : ViewModelProvider.Factory

    @Inject
    lateinit var catalogProductComparisonViewModel: CatalogProductComparisonViewModel

    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var recyclerView : RecyclerView? = null

    private val catalogAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { CatalogDetailAdapterFactoryImpl(this) }
    private val catalogDetailAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel> = AsyncDifferConfig.Builder(
            CatalogDetailDiffUtil())
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
        setUpViews(view)
        setUpEmptyState(view)
        makeApiCall(PAGE_FIRST)
    }

    private fun extractArguments() {
        catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
        catalogName = requireArguments().getString(ARG_EXTRA_CATALOG_NAME, "")
        brand = requireArguments().getString(ARG_EXTRA_CATALOG_BRAND, "")
        categoryId = requireArguments().getString(ARG_EXTRA_CATALOG_CATEGORY_ID, "")
        recommendedCatalogId = requireArguments().getString(ARG_EXTRA_RECOMMENDED_CATALOG_ID, "")
    }

    private fun setUpViews(view : View){
        setUpRecyclerView(view)
        setUpSearchView(view)
    }

    private fun setUpRecyclerView(view : View) {
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

    private fun setUpSearchView(view : View){
        CatalogUtil.setSearchListener(context,view, ::onSearchKeywordEntered , ::onClearSearch)
    }

    private fun setUpEmptyState(view : View){
        view.findViewById<GlobalError>(R.id.global_error)?.run {
            errorIllustration.hide()
            errorSecondaryAction.gone()
            setButtonFull(true)
            errorTitle.text = getString(R.string.catalog_no_products_title)
            errorDescription.text = getString(R.string.catalog_search_product_zero_count_text)
            errorAction.hide()
            errorAction.setOnClickListener {
                makeApiCall(PAGE_FIRST)
            }
        }
    }

    private fun makeApiCall(page : Int) {
        catalogProductComparisonViewModel.getComparisonProducts(recommendedCatalogId,
            catalogId,brand,
            categoryId,LIMIT,page.toString(),searchKeyword)
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(hasNextPage)
                    makeApiCall(page)
            }
        }
    }

    private fun setObservers() {
        observeShimmerData()
        observerDataItems()
        observerHasMoreItems()
        observeErrorMessage()
    }

    private fun observeShimmerData() {
        catalogProductComparisonViewModel.getShimmerData().observe(this, { shimmerData ->
            recyclerView?.show()
            catalogDetailAdapter.submitList(shimmerData)
            catalogDetailAdapter.notifyDataSetChanged()
        })
    }

    private fun observerDataItems() {
        catalogProductComparisonViewModel.getDataItems().observe(this ,{ dataList ->
            if (dataList.isNotEmpty()) {
               onFetchData(dataList)
            } else {
                onEmptyData()
            }
        })
    }

    private fun observerHasMoreItems() {
        catalogProductComparisonViewModel.getHasMoreItems().observe(this, { hasMoreItems ->
            if(hasMoreItems) {
                currentPageNumber ++
                loadMoreTriggerListener?.setHasNextPage(true)
            }
            else loadMoreTriggerListener?.setHasNextPage(false)
        })

    }

    private fun observeErrorMessage() {
        catalogProductComparisonViewModel.getErrorMessage().observe(this, { errorMessage ->
            recyclerView?.hide()
            showErrorGroup()
            showEmptyState()
        })
    }

    private fun onFetchData(dataList : ArrayList<BaseCatalogDataModel>){
        hideErrorGroup()
        recyclerView?.show()
        catalogDetailAdapter.submitList(dataList)
        catalogDetailAdapter.notifyDataSetChanged()
        loadMoreTriggerListener?.updateStateAfterGetData()
        currentPageNumber++
    }

    private fun onSearchKeywordEntered(){
        resetPage()
        val searchText = view?.findViewById<SearchBarUnify>(R.id.catalog_product_search)?.searchBarTextField?.text.toString()
        if(searchText.isNotBlank()){
            searchKeyword = searchText
            makeApiCall(PAGE_FIRST)
        }
    }

    private fun onClearSearch() {
        resetPage()
        searchKeyword = ""
        makeApiCall(PAGE_FIRST)
    }

    private fun resetPage(){
        currentPageNumber = PAGE_FIRST
        loadMoreTriggerListener?.resetState()
    }

    private fun onEmptyData(){
        showErrorGroup()
        showEmptyState()
        recyclerView?.hide()
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
                        recommendedCatalogId : String):
                CatalogProductComparisonFragment {
            return CatalogProductComparisonFragment().apply {
                val bundle = Bundle()
                bundle.putString(ARG_EXTRA_CATALOG_ID, catalogId)
                bundle.putString(ARG_EXTRA_CATALOG_NAME, catalogName)
                bundle.putString(ARG_EXTRA_CATALOG_BRAND, brand)
                bundle.putString(ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
                bundle.putString(ARG_EXTRA_RECOMMENDED_CATALOG_ID, recommendedCatalogId)
                arguments = bundle
            }
        }
    }

    override fun changeComparison(comparedCatalogId: String) {
        dismissBottomSheet()
        (parentFragment as? CatalogComponentBottomSheet)?.changeComparison(comparedCatalogId)
    }

    private fun dismissBottomSheet () {
        (parentFragment as? CatalogComponentBottomSheet)?.dismissCatalogComponentBottomSheet()
    }
}