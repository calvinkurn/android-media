package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CarouselBannerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class CarouselBannerViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView), CarouselBannerView.OnPromoAllClickListener {

    private val carouselBannerTitle: Typography = itemView.findViewById(R.id.title)
    private val carouselBannerView: CarouselBannerView = itemView.findViewById(R.id.carousel_banner_view)
    private lateinit var carouselBannerViewModel: CarouselBannerViewModel
    private var carouselBannerRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)

    init {
        carouselBannerView.setAdapter(carouselBannerRecycleAdapter)
        carouselBannerView.setOnAllPromoClickListener(this)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        carouselBannerViewModel = discoveryBaseViewModel as CarouselBannerViewModel
        setUpObservers()
    }

    private fun setUpObservers() {
        carouselBannerViewModel.componentData.observe(fragment.viewLifecycleOwner, Observer { item ->
            if (!item.title.isNullOrEmpty()) {
                carouselBannerTitle.show()
                setTitle(item.title)
            } else {
                carouselBannerTitle.hide()
            }
        })

        carouselBannerViewModel.listData.observe(fragment.viewLifecycleOwner, Observer { item ->
            carouselBannerRecycleAdapter.setDataList(item)
            carouselBannerView.setDataItems(item)
            carouselBannerView.buildView()
        })
    }

    private fun setTitle(title: String) {
        carouselBannerTitle.text = title
    }

    override fun onPromoAllClick() {
        // need to get the member variable upon which the click will direct to
    }

}