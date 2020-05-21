package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.unifyprinciples.Typography


class DynamicCategoryViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val dynamicCategoryParent: LinearLayout = itemView.findViewById(R.id.dynamic_category_parent)
    private lateinit var dynamicCategoryViewModel: DynamicCategoryViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        dynamicCategoryViewModel = discoveryBaseViewModel as DynamicCategoryViewModel
        setUpObservers()
    }

    private fun setUpObservers() {
        dynamicCategoryViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            dynamicCategoryParent.removeAllViews()
            var dataIndex = 0
            for (data in item) {
                val dynamicCategoryView: View =
                        LayoutInflater.from(fragment.context).inflate(R.layout.dynamic_category_item_layout, dynamicCategoryParent, false)
                val dynamicCategoryHeader: Typography = dynamicCategoryView.findViewById(R.id.dynamic_category_title)
                val dynamicCategoryRecyclerView: RecyclerView = dynamicCategoryView.findViewById(R.id.dynamic_category_recyclerView)
                data.data?.firstOrNull()?.let { dynamicCategoryRowData ->
                    dynamicCategoryHeader.text = dynamicCategoryRowData.title
                    val categoryRowItems = dynamicCategoryRowData.categoryRows?.let {
                        DiscoveryDataMapper.discoveryDataMapper.mapDynamicCategoryListToComponentList(it, ComponentNames.DynamicCategoryItem.componentName,
                                dynamicCategoryRowData.title ?: "", dataIndex++)
                    }
                    dynamicCategoryRecyclerView.apply {
                        adapter = DiscoveryRecycleAdapter(fragment)
                        layoutManager = GridLayoutManager(fragment.context, 4)
                        (adapter as DiscoveryRecycleAdapter).setDataList(categoryRowItems)
                    }
                    sendGtmEvent(dynamicCategoryRowData)
                }
                dynamicCategoryParent.addView(dynamicCategoryView)
            }
        })

    }

    private fun sendGtmEvent(categoryRowData: DataItem) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackImpressionIconDynamicComponent(categoryRowData.title
                ?: "",
                categoryRowData.categoryRows ?: ArrayList())
    }

}