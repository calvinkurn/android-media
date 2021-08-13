package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation

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
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography

class CategoryNavigationItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var categoryNavigationItemViewModel: CategoryNavigationItemViewModel

    private val imageView: ImageView = itemView.findViewById(R.id.img_sub_category)
    private val title: Typography = itemView.findViewById(R.id.txt_sub_category) as Typography

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        categoryNavigationItemViewModel = discoveryBaseViewModel as CategoryNavigationItemViewModel
        categoryNavigationItemViewModel.getComponentData().observe(fragment.viewLifecycleOwner, Observer { item ->

            item.data?.get(0)?.let {
                imageView.loadImageWithoutPlaceholder(it.imageUrlMobile ?: "")
                setClick(it)

                title.text = it.name
            }
        })

    }

    private fun setClick(data: DataItem) {
        if (!data.applinks.isNullOrEmpty()) {
            itemView.setOnClickListener {
                RouteManager.route(itemView.context, data.applinks)
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryNavigationClick(data, adapterPosition)
            }
        }
    }


}