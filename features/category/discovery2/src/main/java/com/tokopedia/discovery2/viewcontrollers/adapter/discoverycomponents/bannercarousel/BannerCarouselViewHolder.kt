package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class BannerCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var titleTextView: TextView = itemView.findViewById(R.id.title_tv)
    private var lihatSemuaTextView: TextView = itemView.findViewById(R.id.lihat_semua_tv)
    private var mBannerCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.list_rv)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mBannerCarouselComponentViewModel: BannerCarouselViewModel
    private val carouselRecyclerViewDecorator = CarouselProductCardItemDecorator()

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mBannerCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mBannerCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter

    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mBannerCarouselComponentViewModel = discoveryBaseViewModel as BannerCarouselViewModel
//        addShimmer()
        addDefaultItemDecorator()
        lihatSemuaTextView.setOnClickListener {
            RouteManager.route(fragment.activity, mBannerCarouselComponentViewModel.getLihatUrl())

        }
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mBannerCarouselComponentViewModel.getComponentData().observe(it, Observer { item ->
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            mBannerCarouselComponentViewModel.getTitleLiveData().observe(it, Observer { item ->
                updateHeaderUI(item)
            })
        }
    }

    private fun updateHeaderUI(title: String) {
        if (title.isEmpty()) {
            titleTextView.hide()
            lihatSemuaTextView.hide()
        } else {
            titleTextView.show()
            titleTextView.text = title
            if (mBannerCarouselComponentViewModel.getLihatUrl().isEmpty()) {
                lihatSemuaTextView.hide()
            } else {
                lihatSemuaTextView.show()
            }
        }
    }

    private fun addDefaultItemDecorator() {
        if (mBannerCarouselRecyclerView.itemDecorationCount > 0)
            mBannerCarouselRecyclerView.removeItemDecorationAt(0)
        mBannerCarouselRecyclerView.addItemDecoration(carouselRecyclerViewDecorator)
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        list.add(ComponentsItem(name = ComponentNames.ShimmerProductCard.componentName))
        mDiscoveryRecycleAdapter.setDataList(list)
    }


}