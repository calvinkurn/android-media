package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success

class CategoryNavigationViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {

    private lateinit var categoryNavigationViewModel: CategoryNavigationViewModel

    private val recyclerView: RecyclerView = itemView.findViewById(R.id.horizontal_rv)
    private val imageView: ImageView = itemView.findViewById(R.id.discovery_horizontal_rv_background)
    private val title: Typography = itemView.findViewById(R.id.horizontal_rv_title) as Typography
    private var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    init {
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = discoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        categoryNavigationViewModel = discoveryBaseViewModel as CategoryNavigationViewModel
        getSubComponent().inject(categoryNavigationViewModel)
        categoryNavigationViewModel.getTitle().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    title.setTextAndCheckShow(item.data)
                }
            }
        })

        categoryNavigationViewModel.getImageUrl().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    if (item.data.isEmpty()) {
                        imageView.invisible()
                    } else {
                        imageView.loadImageWithoutPlaceholder(item.data)
                    }
                }
            }
        })

        categoryNavigationViewModel.getListData().observe(fragment.viewLifecycleOwner, Observer { item ->
            when (item) {
                is Success -> {
                    (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackCategoryNavigationImpression(item.data)
                    discoveryRecycleAdapter.setDataList(item.data)
                }
            }
        })

    }

}