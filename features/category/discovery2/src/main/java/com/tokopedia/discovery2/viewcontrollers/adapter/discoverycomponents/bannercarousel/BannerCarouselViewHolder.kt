package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class BannerCarouselViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var titleTextView: TextView = itemView.findViewById(R.id.title_tv)
    private var lihatSemuaTextView: TextView = itemView.findViewById(R.id.lihat_semua_tv)
    private var mBannerCarouselRecyclerView: RecyclerView = itemView.findViewById(R.id.list_rv)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mBannerCarouselComponentViewModel: BannerCarouselViewModel
    private val bannerRecyclerViewDecorator = BannerCarouselItemDecorator()

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mBannerCarouselRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mBannerCarouselRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mBannerCarouselComponentViewModel = discoveryBaseViewModel as BannerCarouselViewModel
        addDefaultItemDecorator()
        lihatSemuaTextView.setOnClickListener {
            RouteManager.route(fragment.activity, mBannerCarouselComponentViewModel.getLihatUrl())
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackBannerCarouselLihat()
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
            mBannerCarouselComponentViewModel.getComponents().observe(it, Observer { component ->
                component.data?.let { bannerList ->
                    if (bannerList.isNotEmpty()) {
                        sendBannerCarouselImpression(bannerList)
                    }
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mBannerCarouselComponentViewModel.getComponentData().removeObservers(it)
            mBannerCarouselComponentViewModel.getTitleLiveData().removeObservers(it)
            mBannerCarouselComponentViewModel.getComponents().removeObservers(it)
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
        mBannerCarouselRecyclerView.addItemDecoration(bannerRecyclerViewDecorator)
    }

    private fun sendBannerCarouselImpression(item: List<DataItem>) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackCarouselBannerImpression(item)
    }

}