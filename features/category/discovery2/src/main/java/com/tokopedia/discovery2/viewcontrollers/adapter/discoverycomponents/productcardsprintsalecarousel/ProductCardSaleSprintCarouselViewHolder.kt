package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment

class ProductCardSaleSprintCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.tokopoints_rv)
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    private lateinit var productCardSprintSaleCarouselViewModel: ProductCardSprintSaleCarouselViewModel

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardSprintSaleCarouselViewModel = discoveryBaseViewModel as ProductCardSprintSaleCarouselViewModel
        getSubComponent().inject(productCardSprintSaleCarouselViewModel)
        addShimmer()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            productCardSprintSaleCarouselViewModel.getProductCarouselItemsListData().observe(it, Observer { item ->
                discoveryRecycleAdapter.setDataList(item)
            })
            productCardSprintSaleCarouselViewModel.syncData.observe(it, Observer { sync ->
                if (sync) {
                    discoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (productCardSprintSaleCarouselViewModel.getProductCarouselItemsListData().hasObservers()) {
            lifecycleOwner?.let { productCardSprintSaleCarouselViewModel.getProductCarouselItemsListData().removeObservers(it) }
        }
    }


    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = "shimmer_product_card"))
        list.add(ComponentsItem(name = "shimmer_product_card"))
        discoveryRecycleAdapter.setDataList(list)
    }

    override fun getInnerRecycleView(): RecyclerView? {
        return recyclerView
    }

}