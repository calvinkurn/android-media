package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.graphics.Color
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
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LocalLoad

class ShopCardlViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var constraint: ConstraintLayout = itemView.findViewById(R.id.parent_container)
    private var mBgImage: ImageUnify = itemView.findViewById(R.id.bg_image)
    private var mShopCardRecyclerView: RecyclerView = itemView.findViewById(R.id.shop_card_rv)
    private var shopEmptyState: LocalLoad? = null
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mShopCardViewModel: ShopCardViewModel

    init {
        linearLayoutManager.initialPrefetchItemCount = 4
        mShopCardRecyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mShopCardRecyclerView.adapter = mDiscoveryRecycleAdapter
        shopEmptyState = itemView.findViewById(R.id.viewEmptyState)
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardViewModel = discoveryBaseViewModel as ShopCardViewModel
        getSubComponent().inject(mShopCardViewModel)
        mShopCardRecyclerView.show()
        shopEmptyState?.hide()
        addDefaultItemDecorator()
        handleShopCardPagination()
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel.getShopCardBackgroundData().observe(it, Observer { item ->
                mBgImage.loadImage(item?.properties?.backgroundImageUrl)
                constraint.setBackgroundColor(Color.parseColor("#5C5CFF"))
            })

            mShopCardViewModel.getShopCardItemsListData().observe(it, Observer { item ->
                mDiscoveryRecycleAdapter.setDataList(item)
            })

            mShopCardViewModel.getShopLoadState().observe(it, Observer{
                if (it) handleErrorState()
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

    private fun handleErrorState() {
        mDiscoveryRecycleAdapter.notifyDataSetChanged()
        if (constraint.childCount > 0)
            constraint.removeAllViews()

        if (mShopCardViewModel.getProductList() == null) {
            shopEmptyState?.run {
                title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                refreshBtn?.setOnClickListener {
                    reloadComponent()
                }
                shopEmptyState?.visible()
                mShopCardRecyclerView.gone()
            }
        }
    }

    private fun reloadComponent() {
        mShopCardRecyclerView.visible()
        shopEmptyState?.gone()
        mShopCardViewModel.resetComponent()
        mShopCardViewModel.fetchShopCardData()
    }

    private fun addDefaultItemDecorator() {
        if (mShopCardRecyclerView.itemDecorationCount > 0)
            mShopCardRecyclerView.removeItemDecorationAt(0)
//        mBannerCarouselRecyclerView.addItemDecoration(bannerRecyclerViewDecorator)
    }


}