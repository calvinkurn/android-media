package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

class CpmTopAdsViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {


    private var adsBannerView: TopAdsBannerView = itemView.findViewById(R.id.cpm_ads_banner)
    private lateinit var cpmTopAdsViewModel: CpmTopAdsViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        cpmTopAdsViewModel = discoveryBaseViewModel as CpmTopAdsViewModel

    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        cpmTopAdsViewModel.getCpmData().observe(fragment.viewLifecycleOwner, Observer { item ->
            adsBannerView.displayAds(item)
        })
    }


    // OLD
//    private val recyclerView: RecyclerView = itemView.findViewById(R.id.discovery_cpm_topads_rv)
//    private val promotedText: TextView = itemView.findViewById(R.id.discovery_cpm_promoted_text)
//    private val promotedBrand: TextView = itemView.findViewById(R.id.discovery_cpm_promoted_brand_name)
//    private val badge: ImageView = itemView.findViewById(R.id.discovery_cpm_badge)
//    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter
//
//    private lateinit var cpmTopAdsViewModel: CpmTopAdsViewModel
//
//    init {
//        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
//        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
//        recyclerView.adapter = discoveryRecycleAdapter
//    }
//
//    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
//        cpmTopAdsViewModel = discoveryBaseViewModel as CpmTopAdsViewModel
//        addShimmer()
//    }
//
//    private fun addShimmer() {
//        val list: ArrayList<ComponentsItem> = ArrayList()
//        val width = fragment.context?.resources?.getDimensionPixelSize(R.dimen.dp_250) ?: 0
//        val height = fragment.context?.resources?.getDimensionPixelSize(R.dimen.shop_card_height)
//                ?: 0
//        list.add(ComponentsItem(name = "shimmer", shimmerWidth = width, shimmerHeight = height))
//        list.add(ComponentsItem(name = "shimmer", shimmerWidth = width, shimmerHeight = height))
//        discoveryRecycleAdapter.setDataList(list)
//    }
//
//    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
//        super.setUpObservers(lifecycleOwner)
//        cpmTopAdsViewModel.getCpmTopAdsList().observe(fragment.viewLifecycleOwner, Observer { item ->
//            when (item) {
//                is Success -> {
//                    discoveryRecycleAdapter.setDataList(item.data)
//                }
//            }
//
//        })
//        cpmTopAdsViewModel.getPromotedText().observe(fragment.viewLifecycleOwner, Observer { item ->
//            when (item) {
//                is Success -> {
//                    promotedText.setTextAndCheckShow(item.data)
//                }
//            }
//
//        })
//        cpmTopAdsViewModel.getBrandName().observe(fragment.viewLifecycleOwner, Observer { item ->
//            when (item) {
//                is Success -> {
//                    promotedBrand.setTextAndCheckShow(item.data)
//                }
//            }
//
//        })
//        cpmTopAdsViewModel.getImageUrl().observe(fragment.viewLifecycleOwner, Observer { item ->
//            when (item) {
//                is Success -> {
//                    badge.loadImage(item.data)
//                }
//            }
//
//        })
//    }
}