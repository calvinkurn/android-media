package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LocalLoad

class ProductCardCarouselViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mProductCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.products_rv)
    private var mHeaderView: FrameLayout = itemView.findViewById(R.id.header_view)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mProductCarouselComponentViewModel: ProductCardCarouselViewModel
    private val carouselRecyclerViewDecorator = CarouselProductCardItemDecorator()
    private var carouselEmptyState: LocalLoad? = null
    private var errorHolder: FrameLayout = itemView.findViewById(R.id.filter_error_view)

    init {
        linearLayoutManager.initialPrefetchItemCount = PREFETCH_ITEM_COUNT
        mProductCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(true)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
        carouselEmptyState = itemView.findViewById(R.id.viewEmptyState)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductCarouselComponentViewModel = discoveryBaseViewModel as ProductCardCarouselViewModel
        getSubComponent().inject(mProductCarouselComponentViewModel)
        if (mDiscoveryRecycleAdapter.itemCount == 0 || mProductCarouselComponentViewModel.getProductList().isNullOrEmpty()) {
            addShimmer()
        }
        mProductCarouselRecyclerView.show()
        carouselEmptyState?.hide()
        errorHolder.gone()
        addDefaultItemDecorator()
        handleCarouselPagination()
    }

    private fun handleCarouselPagination() {
        mProductCarouselRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
                if (!mProductCarouselComponentViewModel.isLoadingData() && !mProductCarouselComponentViewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0 && totalItemCount >= mProductCarouselComponentViewModel.getPageSize()) {
                        mProductCarouselComponentViewModel.fetchCarouselPaginatedProducts()
                    }
                }
            }
        })
    }

    private fun addCardHeader(componentsItem: ComponentsItem?) {
        mHeaderView.removeAllViews()
        checkHeaderVisibility(componentsItem)
    }

    private fun checkHeaderVisibility(componentsItem: ComponentsItem?) {
        componentsItem?.data?.firstOrNull()?.let {
            if (!it.title.isNullOrEmpty() || !it.subtitle.isNullOrEmpty()) {
                mHeaderView.addView(CustomViewCreator.getCustomViewObject(itemView.context,
                        ComponentsList.LihatSemua, componentsItem, fragment))
            }
        }
    }

    private fun addDefaultItemDecorator() {
        if (mProductCarouselRecyclerView.itemDecorationCount > 0)
            mProductCarouselRecyclerView.removeItemDecorationAt(0)
        mProductCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            mProductCarouselComponentViewModel.getProductCardHeaderData().observe(lifecycle, { component ->
                addCardHeader(component)
            })
            mProductCarouselComponentViewModel.getProductCarouselItemsListData().observe(lifecycle, { item ->
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            mProductCarouselComponentViewModel.syncData.observe(lifecycle, { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
            mProductCarouselComponentViewModel.getProductCardMaxHeight().observe(lifecycle, { height ->
                setMaxHeight(height)
            })
            mProductCarouselComponentViewModel.getProductLoadState().observe(lifecycle, {
                if (it) handleErrorState()
            })
            mProductCarouselComponentViewModel.atcFailed.observe(lifecycle, { position ->
                if (position >= 0) {
                    mDiscoveryRecycleAdapter.getViewModelAtPosition(position)?.let { discoveryBaseViewModel ->
                        if (discoveryBaseViewModel is MasterProductCardItemViewModel) {
                            discoveryBaseViewModel.handleATCFailed()
                        }
                    }
                }
            })
        }
    }

    private fun setMaxHeight(height: Int) {
        val carouselLayoutParams = mProductCarouselRecyclerView.layoutParams
        carouselLayoutParams?.height = height
        mProductCarouselRecyclerView.layoutParams = carouselLayoutParams
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductCarouselComponentViewModel.getProductCarouselItemsListData().removeObservers(it)
            mProductCarouselComponentViewModel.getProductCardMaxHeight().removeObservers(it)
            mProductCarouselComponentViewModel.getProductLoadState().removeObservers(it)
            mProductCarouselComponentViewModel.getProductCardHeaderData().removeObservers(it)
            mProductCarouselComponentViewModel.atcFailed.removeObservers(it)
        }
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    private fun handleErrorState() {
        addShimmer()
        mDiscoveryRecycleAdapter.notifyDataSetChanged()
        if (mHeaderView.childCount > 0)
            mHeaderView.removeAllViews()

        if (mProductCarouselComponentViewModel.getProductList() == null) {
            carouselEmptyState?.run {
                title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                refreshBtn?.setOnClickListener {
                    reloadComponent()
                }
                carouselEmptyState?.visible()
                mProductCarouselRecyclerView.gone()
                errorHolder.gone()
            }
        } else if (mProductCarouselComponentViewModel.getProductList()?.isEmpty() == true
            && mProductCarouselComponentViewModel.areFitterApplied()) {
            if (errorHolder.childCount > 0) {
                errorHolder.removeAllViews()
            }
            errorHolder.addView(
                CustomViewCreator.getCustomViewObject(
                    itemView.context, ComponentsList.ProductListEmptyState,
                    mProductCarouselComponentViewModel.getErrorStateComponent(), fragment
                )
            )
            errorHolder.show()
            carouselEmptyState?.gone()
            mProductCarouselRecyclerView.gone()
        }
    }

    private fun reloadComponent() {
        mProductCarouselRecyclerView.visible()
        carouselEmptyState?.gone()
        mProductCarouselComponentViewModel.resetComponent()
        mProductCarouselComponentViewModel.fetchProductCarouselData()
    }

    override fun getInnerRecycleView(): RecyclerView {
        return mProductCarouselRecyclerView
    }

    companion object{
        const val PREFETCH_ITEM_COUNT = 4
    }
}
