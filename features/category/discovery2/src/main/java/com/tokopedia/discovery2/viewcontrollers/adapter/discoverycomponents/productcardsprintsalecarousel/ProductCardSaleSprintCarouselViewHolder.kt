package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class ProductCardSaleSprintCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.tokopoints_rv)
    private  var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    private lateinit var productCardSprintSaleCarouselViewModel: ProductCardSprintSaleCarouselViewModel

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }
    
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        productCardSprintSaleCarouselViewModel = discoveryBaseViewModel as ProductCardSprintSaleCarouselViewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        productCardSprintSaleCarouselViewModel.listData.observe(fragment.viewLifecycleOwner,Observer { item ->
            discoveryRecycleAdapter.setDataList(item)
        })
    }



}