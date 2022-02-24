package com.tokopedia.catalog.viewholder.products

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailAdapter
import com.tokopedia.catalog.adapter.CatalogDetailDiffUtil
import com.tokopedia.catalog.adapter.factory.CatalogDetailAdapterFactoryImpl
import com.tokopedia.catalog.di.DaggerCatalogComponent
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogForYouModel
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.viewmodel.CatalogDetailProductListingViewModel
import com.tokopedia.catalog.viewmodel.CatalogForYouViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import javax.inject.Inject

class CatalogForYouContainerViewHolder(private val view : View,
                                       private val lifecycleOwner : FragmentActivity,
                                       private val catalogId : String = "",
                                       private val categoryId : String = "",
                                       private val brand : String = "",
                                       private val catalogProductCardListener: CatalogProductCardListener?
): AbstractViewHolder<CatalogForYouContainerDataModel>(view) , CatalogDetailListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val myLayoutManger = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private val catalogAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { CatalogDetailAdapterFactoryImpl(this) }
    private val catalogDetailAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseCatalogDataModel> = AsyncDifferConfig.Builder(
            CatalogDetailDiffUtil()
        )
            .build()
        CatalogDetailAdapter(lifecycleOwner,this,catalogId,asyncDifferConfig, catalogAdapterFactory
        )
    }

    private var catalogForYouViewModel : CatalogForYouViewModel? = null
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var recyclerView : RecyclerView? = null
    private val firstPage = 1
    private val rowLimit = 10

    companion object {
        val LAYOUT = R.layout.item_catalog_for_you_container
    }

    override fun bind(element: CatalogForYouContainerDataModel) {
        injectViewModel()
        setUpRecyclerView(itemView)
        setObservers()
        if(!initialApiCall())
            makeApiCall(catalogForYouViewModel?.page ?: firstPage)
    }

    private fun injectViewModel(){
        val component = DaggerCatalogComponent.builder()
            .baseAppComponent((lifecycleOwner.applicationContext as BaseMainApplication)
                .baseAppComponent).build()
        component.inject(this)
        val viewModelProvider = ViewModelProvider(lifecycleOwner, viewModelFactory)
        catalogForYouViewModel = viewModelProvider.get(CatalogForYouViewModel::class.java)
    }

    private fun initialApiCall() : Boolean {
        return catalogForYouViewModel?.lastScrollIndex ?: 0 < catalogForYouViewModel?.getLoadedItemsSize() ?: 0
    }

    private fun setUpRecyclerView(view : View) {
        view.findViewById<RecyclerView>(R.id.catalog_for_you_rv)?.let { rV ->
            recyclerView = rV
            recyclerView?.apply {
                layoutManager = myLayoutManger
                loadMoreTriggerListener = getEndlessRecyclerViewListener(myLayoutManger)
                adapter = catalogDetailAdapter
                loadMoreTriggerListener?.let { addOnScrollListener(it) }
                if(catalogForYouViewModel?.lastScrollIndex ?: 0 > 0){
                    (layoutManager as? LinearLayoutManager)?.scrollToPosition(
                        catalogForYouViewModel?.lastScrollIndex ?: 0)
                }
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
        catalogForYouViewModel?.getShimmerData()?.observe(lifecycleOwner, { shimmerData ->
            recyclerView?.show()
            catalogDetailAdapter.submitList(shimmerData)
            catalogDetailAdapter.notifyDataSetChanged()
        })
    }

    private fun observerDataItems() {
        catalogForYouViewModel?.getDataItems()?.observe(lifecycleOwner ,{ dataList ->
            if (dataList.isNotEmpty()) {
                onFetchData(dataList)
            }else {
                handleError()
            }
        })
    }

    private fun observerHasMoreItems() {
        catalogForYouViewModel?.getHasMoreItems()?.observe(lifecycleOwner, { hasMoreItems ->
            if(hasMoreItems) loadMoreTriggerListener?.setHasNextPage(true)
            else loadMoreTriggerListener?.setHasNextPage(false)
        })

    }

    private fun observeErrorMessage() {
        catalogForYouViewModel?.getError()?.observe(lifecycleOwner, { errorMessage ->
            handleError()
        })
    }

    private fun onFetchData(dataList : ArrayList<BaseCatalogDataModel>){
        recyclerView?.show()
        catalogDetailAdapter.submitList(dataList)
        catalogDetailAdapter.notifyDataSetChanged()
        loadMoreTriggerListener?.updateStateAfterGetData()
    }

    private fun handleError(){
        itemView.findViewById<LinearLayout>(R.id.catalog_for_you_container_parent).hide()
    }

    private fun makeApiCall(page : Int) {
        if(catalogForYouViewModel?.isLoading == false) {
            catalogForYouViewModel?.getComparisonProducts(
                "",
                catalogId, brand,
                categoryId, rowLimit, page, ""
            )
        }
    }

    override fun onCatalogForYouClick(adapterPosition : Int, catalogComparison: CatalogComparisonProductsResponse.CatalogComparisonList.CatalogComparison) {
        catalogProductCardListener?.onCatalogForYouClick(adapterPosition, catalogComparison)
    }

    override fun onCatalogForYouImpressed(model: CatalogForYouModel, adapterPosition: Int) {
        catalogProductCardListener?.onCatalogForYouImpressed(model,adapterPosition)
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(hasNextPage)
                    makeApiCall(catalogForYouViewModel?.page ?: firstPage)
            }
        }
    }

    fun removeObservers(){
        catalogForYouViewModel?.lastScrollIndex = (recyclerView?.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: 0
        catalogForYouViewModel?.getDataItems()?.removeObservers(lifecycleOwner)
        catalogForYouViewModel?.getHasMoreItems()?.removeObservers(lifecycleOwner)
        catalogForYouViewModel?.getError()?.removeObservers(lifecycleOwner)
        catalogForYouViewModel?.getShimmerData()?.removeObservers(lifecycleOwner)
    }
}