package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopofferherobrand

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.databinding.ItemDiscoveryShopOfferHeroBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.collections.ArrayList

class ShopOfferHeroBrandViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    companion object {
        const val PREFETCH_ITEM_COUNT = 4
    }

    internal val mLayoutManager: LinearLayoutManager
        by lazy { LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false) }

    private val binding: ItemDiscoveryShopOfferHeroBrandLayoutBinding?
        by viewBinding()

    private val mAdapter: DiscoveryRecycleAdapter
        by lazy { DiscoveryRecycleAdapter(fragment) }

    private var viewModel: ShopOfferHeroBrandViewModel? = null

    init {
        binding?.setupRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ShopOfferHeroBrandViewModel
        viewModel?.let {
            getSubComponent().inject(it)
        }

        if (mAdapter.itemCount.isZero() || viewModel?.getProductList().isNullOrEmpty()) {
            addShimmer()
        }

        binding?.apply {
            rvProductCarousel.show()
            localLoad.hide()
            errorHolder.gone()
        }
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.setupRecyclerView() {
        mAdapter.setHasStableIds(true)
        rvProductCarousel.layoutManager = mLayoutManager
        rvProductCarousel.adapter = mAdapter
        mLayoutManager.initialPrefetchItemCount = PREFETCH_ITEM_COUNT
        addItemDecorator()
        handlePagination()
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.handlePagination() {
        if (viewModel?.shouldShowViewAllCard() == true) return

        rvProductCarousel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = mLayoutManager.childCount
                    val totalItemCount: Int = mLayoutManager.itemCount
                    val firstVisibleItemPosition: Int = mLayoutManager.findFirstVisibleItemPosition()
                    viewModel?.let { mProductCarouselComponentViewModel ->
                        if (!mProductCarouselComponentViewModel.isLoadingData() && mProductCarouselComponentViewModel.hasNextPage()) {
                            if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0 && totalItemCount >= mProductCarouselComponentViewModel.getPageSize()) {
                                mProductCarouselComponentViewModel.fetchCarouselPaginatedProducts()
                            }
                        }
                    }
                }
            }
        )
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.addItemDecorator() {
        if (rvProductCarousel.itemDecorationCount.isMoreThanZero()) {
            rvProductCarousel.removeAllItemDecoration()
        }
        rvProductCarousel.addItemDecoration(CarouselProductCardItemDecorator())
    }

    private fun ItemDiscoveryShopOfferHeroBrandLayoutBinding.addCardHeader(componentsItem: ComponentsItem?) {
//        headerView.removeAllViews()
//        val component = componentsItem?.data?.firstOrNull()
//        headerView.showIfWithBlock(!component?.title.isNullOrEmpty() || !component?.subtitle.isNullOrEmpty()) {
//            headerView.addView(
//                CustomViewCreator.getCustomViewObject(
//                    itemView.context,
//                    ComponentsList.LihatSemua,
//                    componentsItem,
//                    fragment
//                )
//            )
//        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            viewModel?.header?.observe(lifecycle) { component ->
//                addCardHeader(component)
            }
            viewModel?.productList?.observe(lifecycle) { item ->
                when (item) {
                    is Success -> mAdapter.setDataList(item.data)
                    is Fail -> handleErrorState()
                }

            }
            viewModel?.syncData?.observe(lifecycle) { sync ->
                if (sync) {
                    mAdapter.notifyDataSetChanged()
                }
            }
            viewModel?.productMaxHeight?.observe(lifecycle) { height ->
                setMaxHeight(height)
            }
        }
    }

    private fun setMaxHeight(height: Int) {
        binding?.apply {
            val carouselLayoutParams = rvProductCarousel.layoutParams
            carouselLayoutParams?.height = height
            rvProductCarousel.layoutParams = carouselLayoutParams
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (lifecycleOwner == null) return
        lifecycleOwner?.let {
            viewModel?.productList?.removeObservers(it)
            viewModel?.productMaxHeight?.removeObservers(it)
            viewModel?.header?.removeObservers(it)
        }
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mAdapter.setDataList(list)
    }

    private fun handleErrorState() {
        binding?.apply {
            addShimmer()
            mAdapter.notifyDataSetChanged()
            if (headerView.childCount > 0) {
                headerView.removeAllViews()
            }

            if (viewModel?.getProductList() == null) {
                localLoad.run {
                    title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                    description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                    refreshBtn?.setOnClickListener {
                        reloadComponent()
                    }
                    localLoad?.visible()
                    rvProductCarousel.gone()
                    errorHolder.gone()
                }
            } else if (viewModel?.getProductList()?.isEmpty() == true &&
                viewModel?.areFiltersApplied() == true
            ) {
                if (errorHolder.childCount > 0) {
                    errorHolder.removeAllViews()
                }
                errorHolder.addView(
                    viewModel?.getErrorStateComponent()?.let {
                        CustomViewCreator.getCustomViewObject(
                            itemView.context,
                            ComponentsList.ProductListEmptyState,
                            it,
                            fragment
                        )
                    }
                )
                errorHolder.show()
                localLoad.gone()
                rvProductCarousel.gone()
            }
        }
    }

    private fun reloadComponent() {
        binding?.rvProductCarousel?.visible()
        binding?.localLoad?.gone()
        viewModel?.resetComponent()
        viewModel?.fetchProductCarouselData()
    }
}
