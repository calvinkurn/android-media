package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

class ShopCardlViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var constraint: ConstraintLayout = itemView.findViewById(R.id.parent_container)
    private var mBgImage: ImageUnify = itemView.findViewById(R.id.bg_image)
    private var mShopCardRecyclerView: RecyclerView = itemView.findViewById(R.id.shop_card_rv)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mShopCardViewModel: ShopCardViewModel

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mShopCardRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mShopCardRecyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardViewModel = discoveryBaseViewModel as ShopCardViewModel
        getSubComponent().inject(mShopCardViewModel)
        addDefaultItemDecorator()
        handleShopCardPagination()
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel.getShopCardBackgroundData().observe(it, Observer { item ->
                mBgImage.loadImage(item?.properties?.backgroundImageUrl)
//                constraint.setBackgroundColor(Color.parseColor(item?.properties?.backgroundColor))
            })

            mShopCardViewModel.getShopCardItemsListData().observe(it, Observer { item ->
                mDiscoveryRecycleAdapter.setDataList(item)
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel.getShopCardItemsListData().removeObservers(it)
        }
    }

    private fun handleShopCardPagination() {
        mShopCardRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
                if (!mShopCardViewModel.isLoadingData() && !mShopCardViewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0 && totalItemCount >= mShopCardViewModel.getPageSize()) {
                        mShopCardViewModel.fetchShopCardPaginatedData()
                    }
                }
            }
        })
    }

    private fun addDefaultItemDecorator() {
        if (mShopCardRecyclerView.itemDecorationCount > 0)
            mShopCardRecyclerView.removeItemDecorationAt(0)
//        mBannerCarouselRecyclerView.addItemDecoration(bannerRecyclerViewDecorator)
    }

//    private fun sendBannerCarouselImpression(item: List<DataItem>) {
//        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackCarouselBannerImpression(item)
//    }

}