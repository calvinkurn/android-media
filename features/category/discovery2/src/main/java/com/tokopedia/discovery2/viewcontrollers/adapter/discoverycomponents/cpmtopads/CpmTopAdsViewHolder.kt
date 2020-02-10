package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.usecase.coroutines.Success

class CpmTopAdsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.discovery_cpm_topads_rv)
    private val promotedText: TextView = itemView.findViewById(R.id.discovery_cpm_promoted_text)
    private val promotedBrand: TextView = itemView.findViewById(R.id.discovery_cpm_promoted_brand_name)
    private val badge: ImageView = itemView.findViewById(R.id.discovery_cpm_badge)
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    private lateinit var cpmTopAdsViewModel: CpmTopAdsViewModel

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        cpmTopAdsViewModel = discoveryBaseViewModel as CpmTopAdsViewModel
        cpmTopAdsViewModel.fetchCpmTopAdsData()
        setUpObserver()
    }

    private fun setUpObserver() {
        cpmTopAdsViewModel.getCpmTopAdsList().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    discoveryRecycleAdapter.setDataList(item.data)
                }
            }

        })
        cpmTopAdsViewModel.getPromotedText().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    promotedText.setTextAndCheckShow(item.data)
                }
            }

        })
        cpmTopAdsViewModel.getBrandName().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    promotedBrand.setTextAndCheckShow(item.data)
                }
            }

        })
        cpmTopAdsViewModel.getImageUrl().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    badge.loadImage(item.data)
                }
            }

        })
    }


}