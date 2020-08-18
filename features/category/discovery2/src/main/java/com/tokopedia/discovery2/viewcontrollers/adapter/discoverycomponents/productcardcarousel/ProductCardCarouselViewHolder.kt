package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ProductCardCarouselViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var mProductCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.products_rv)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mProductCarouselComponentViewModel: ProductCardCarouselViewModel
    private val carouselRecyclerViewDecorator = CarouselProductCardItemDecorator()

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mProductCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(true)
        mProductCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductCarouselComponentViewModel = discoveryBaseViewModel as ProductCardCarouselViewModel
        addShimmer()
        addDefaultItemDecorator()
    }

    private fun addDefaultItemDecorator() {
        if (mProductCarouselRecyclerView.itemDecorationCount > 0)
            mProductCarouselRecyclerView.removeItemDecorationAt(0)
        mProductCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductCarouselComponentViewModel.getProductCarouselItemsListData().observe(it, Observer { item ->
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            mProductCarouselComponentViewModel.syncData.observe(it, Observer { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (mProductCarouselComponentViewModel.getProductCarouselItemsListData().hasObservers()) {
            lifecycleOwner?.let { mProductCarouselComponentViewModel.getProductCarouselItemsListData().removeObservers(it) }
        }
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mDiscoveryRecycleAdapter.setDataList(list)
    }


    override fun getInnerRecycleView(): RecyclerView? {
        return mProductCarouselRecyclerView
    }
}