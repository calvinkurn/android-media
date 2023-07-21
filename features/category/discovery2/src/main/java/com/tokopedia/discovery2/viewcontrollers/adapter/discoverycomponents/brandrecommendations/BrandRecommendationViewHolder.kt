package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
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
    private var brandRecommendationViewModel: BrandRecommendationViewModel? = null
    private val displayMetrics = Utils.getDisplayMetric(fragment.context)

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        brandRecommendationViewModel = discoveryBaseViewModel as BrandRecommendationViewModel
        brandRecommendationViewModel?.mapBrandRecomItems()
        brandRecommendationViewModel?.getListData()?.let {
            if (it.isNotEmpty()) {
                sendBrandRecommendationImpressionGtm(it)
            }
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifecycle ->
            brandRecommendationViewModel?.getComponentDataLiveData()?.observe(lifecycle) { item ->
                if (!item.title.isNullOrEmpty()) setTitle(item.title)
            }

            brandRecommendationViewModel?.getListDataLiveData()?.observe(lifecycle) { item ->
                if (item.isNotEmpty()) {
                    val noOfItems: Double = if (item.size > STATIC_WIDGET_ITEM_LIMIT) CAROUSEL_WIDGET_ITEM_LIMIT else STATIC_WIDGET_ITEM_LIMIT.toDouble()
                    val widthOfSingleChild = ((displayMetrics.widthPixels - itemView.context.resources.getDimensionPixelSize(R.dimen.brand_recom_carousel_gap)) / noOfItems)
                    val heightOfRecyclerView = (widthOfSingleChild + itemView.context.resources.getDimensionPixelSize(R.dimen.dp_16)).toInt()
                    if (recyclerView.layoutParams.height != heightOfRecyclerView) {
                        val params = recyclerView.layoutParams
                        params.height = heightOfRecyclerView
                        recyclerView.layoutParams = params
                    }
                    discoveryRecycleAdapter.setDataList(item as? ArrayList<ComponentsItem>)
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            brandRecommendationViewModel?.getComponentDataLiveData()?.removeObservers(it)
            brandRecommendationViewModel?.getListDataLiveData()?.removeObservers(it)
        }
    }

    private fun sendBrandRecommendationImpressionGtm(item: List<ComponentsItem>) {
        brandRecommendationViewModel?.let {
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBrandRecommendationImpression(
                item,
                it.getComponentPosition(),
                it.getComponentID()
            )
        }
    }

    private fun setTitle(title: String) {
        brandRecomTitle.show()
        brandRecomTitle.text = title
    }

    companion object {
        const val STATIC_WIDGET_ITEM_LIMIT = 4
        const val CAROUSEL_WIDGET_ITEM_LIMIT = 4.5
    }
}
