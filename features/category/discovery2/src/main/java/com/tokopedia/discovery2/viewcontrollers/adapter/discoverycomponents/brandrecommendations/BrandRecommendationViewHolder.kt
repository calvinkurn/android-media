package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class BrandRecommendationViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.brand_recom_rv)
    private val brandRecomTitle: Typography = itemView.findViewById(R.id.brand_recom_title) as Typography
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var brandRecommendationViewModel: BrandRecommendationViewModel

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationViewModel = discoveryBaseViewModel as BrandRecommendationViewModel
        brandRecommendationViewModel.getListData()?.let {
            if (it.isNotEmpty()) {
                sendBrandRecommendationImpressionGtm(it.firstOrNull()?.data ?: ArrayList())
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifecycle ->
            brandRecommendationViewModel.getComponentDataLiveData().observe(lifecycle, { item ->
                if (!item.title.isNullOrEmpty()) setTitle(item.title)
            })

            brandRecommendationViewModel.getListDataLiveData().observe(lifecycle, { item ->
                if (item.isNotEmpty()) {
                    discoveryRecycleAdapter.setDataList(item as? ArrayList<ComponentsItem>)
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            brandRecommendationViewModel.getComponentDataLiveData().removeObservers(it)
            brandRecommendationViewModel.getListDataLiveData().removeObservers(it)
        }
    }

    private fun sendBrandRecommendationImpressionGtm(item: List<DataItem>) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerImpression(item, brandRecommendationViewModel.getComponentPosition())
    }

    private fun setTitle(title: String) {
        brandRecomTitle.show()
        brandRecomTitle.text = title
    }
}
