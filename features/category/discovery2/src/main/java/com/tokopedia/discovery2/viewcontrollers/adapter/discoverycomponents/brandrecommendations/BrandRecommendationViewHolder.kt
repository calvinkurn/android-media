package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class BrandRecommendationViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.brand_recom_rv)
    private val brandRecomTitle: Typography = itemView.findViewById(R.id.brand_recom_title) as Typography
    private  var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    private lateinit var brandRecommendationViewModel: BrandRecommendationViewModel

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }
    
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationViewModel = discoveryBaseViewModel as BrandRecommendationViewModel
        setUpObserver()
    }

    private fun setUpObserver() {
        brandRecommendationViewModel.componentData.observe(fragment.viewLifecycleOwner, Observer { item ->
            if (!item.title.isNullOrEmpty()) setTitle(item.title)
        })

        brandRecommendationViewModel.listData.observe(fragment.viewLifecycleOwner,Observer { item ->
            discoveryRecycleAdapter.setDataList(item)
        })
    }


    private fun setTitle(title: String) {
        brandRecomTitle.show()
        brandRecomTitle.text=title
    }


}