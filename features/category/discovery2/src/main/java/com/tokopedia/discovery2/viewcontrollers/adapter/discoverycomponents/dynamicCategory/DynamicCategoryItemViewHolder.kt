package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography

class DynamicCategoryItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val dynamicCategorySingleItemTitle: Typography = itemView.findViewById(R.id.dynamic_category_item_title)
    private val dynamicCategorySingleItemIcon: ImageView = itemView.findViewById(R.id.dynamic_category_item_icon)
    private lateinit var dynamicCategoryItemViewModel: DynamicCategoryItemViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        dynamicCategoryItemViewModel = discoveryBaseViewModel as DynamicCategoryItemViewModel
        setUpObservers()
    }

    private fun setUpObservers() {
        dynamicCategoryItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer {
            it.data?.firstOrNull()?.let { itemData ->
                dynamicCategorySingleItemIcon.loadImageWithoutPlaceholder(itemData.thumbnailUrlMobile
                        ?: "")
                setClick(itemData)
                dynamicCategorySingleItemTitle.setTextAndCheckShow(itemData.name)
            }
        })
    }

    private fun setClick(dataItem: DataItem) {
        if (!dataItem.applinks.isNullOrEmpty()) {
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, dataItem.applinks)
                sentGtmEvent(dataItem)
            }
        }
    }

    private fun sentGtmEvent(dataItem: DataItem) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickIconDynamicComponent(adapterPosition, dataItem)
    }
}