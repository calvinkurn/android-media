package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.DefaultComponentViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.unifyprinciples.Typography

class NavigationChipsItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val childCategory: Typography = itemView.findViewById(R.id.child_category)
    private lateinit var childCategoriesItemViewModel: DefaultComponentViewModel
    private var positionForParentAdapter: Int = -1

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        childCategoriesItemViewModel = discoveryBaseViewModel as DefaultComponentViewModel
        childCategoriesItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            val itemData = item.data?.firstOrNull()
            positionForParentAdapter = itemData?.positionForParentItem ?: -1
            itemData?.let {
                it.title?.let { title ->
                    childCategory.text = title
                    setClick(item)
                }
            }
            if(adapterPosition == 0)
                itemView.setMargin(itemView.context.resources.getDimensionPixelSize(R.dimen.dp_12),0,0,0)
        })
    }

    private fun setClick(componentsItem: ComponentsItem) {
        childCategory.setOnClickListener {
            componentsItem.data?.firstOrNull()?.let { it1 ->
                RouteManager.route(itemView.context, it1.applinks)
                sendChipClickEvent(it1)
            }
        }
    }

    private fun sendChipClickEvent(chipData: DataItem) {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackClickNavigationChips(chipData, positionForParentAdapter)
    }
}