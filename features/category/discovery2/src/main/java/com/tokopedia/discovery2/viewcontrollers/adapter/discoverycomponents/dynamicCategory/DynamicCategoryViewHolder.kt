package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips.ChipsFilterViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.DiscoveryHomeFactory
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
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
            for (data in item) {
                val dynamicCategoryView: View =
                        LayoutInflater.from(fragment.context).inflate(R.layout.dynamic_category_item_layout, dynamicCategoryParent, false)
                val dynamicCategoryHeader: Typography = dynamicCategoryView.findViewById(R.id.dynamic_category_title)
                val dynamicCategoryRecyclerView: RecyclerView = dynamicCategoryView.findViewById(R.id.dynamic_category_recyclerView)
                val dynamicCategoryRowData = data.data?.get(0)
                dynamicCategoryRowData?.title?.let {
                    dynamicCategoryHeader.text = it
                }
                val categoryRowItems = getCategoryRowItems(dynamicCategoryRowData?.categoryRows)
                dynamicCategoryRecyclerView.apply {
                    adapter = DiscoveryRecycleAdapter(fragment)
                    layoutManager = GridLayoutManager(fragment.context, 4)
                    (adapter as DiscoveryRecycleAdapter).setDataList(categoryRowItems)
                }
                dynamicCategoryParent.addView(dynamicCategoryView)
            }
        })

    }

    private fun getCategoryRowItems(categoryRows: List<DataItem>?): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        categoryRows?.forEach {
            val componentsItem = ComponentsItem()
            componentsItem.name = "dynamic_category_item"
            val dataItem = mutableListOf<DataItem>()
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list

    }


}