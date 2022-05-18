package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LocalLoad
import java.lang.Exception

class ShopCardViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var constraint: ConstraintLayout = itemView.findViewById(R.id.parent_container)
    private var mBgImage: ImageUnify = itemView.findViewById(R.id.bg_image)
    private var mShopCardRecyclerView: RecyclerView = itemView.findViewById(R.id.shop_card_rv)
    private var shopEmptyState: LocalLoad? = null
    private val shopCardRecyclerViewDecorator = ShopCardItemDecorator()
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
        if (mShopCardViewModel.shouldShowShimmer()) {
            addShimmer()
        }
        handleShopCardPagination()
    }


    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel.getShopCardBackgroundData().observe(it, { item ->
                setHolderBackgroundData(item)
            })
            mShopCardViewModel.syncData.observe(it, { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            })
            mShopCardViewModel.getShopCardItemsListData().observe(it, { item ->
                mShopCardRecyclerView.show()
                mDiscoveryRecycleAdapter.setDataList(item)
            })

            mShopCardViewModel.getShopLoadState().observe(it, { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            })

            mShopCardViewModel.hideShimmer().observe(it, { shouldShowErrorState ->
                if (shouldShowErrorState) hideShimmer()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel.getShopCardItemsListData().removeObservers(it)
            mShopCardViewModel.getShopCardBackgroundData().removeObservers(it)
            mShopCardViewModel.getShopLoadState().removeObservers(it)
            mShopCardViewModel.hideShimmer().removeObservers(it)
            mShopCardViewModel.syncData.removeObservers(it)
        }
    }

    private fun setHolderBackgroundData(item: ComponentsItem?) {
        try {
            if (item?.properties?.backgroundImageUrl.isNullOrEmpty()) {
                mBgImage.hide()
            } else {
                mBgImage.show()
                mBgImage.loadImageWithoutPlaceholder(item?.properties?.backgroundImageUrl)
            }
            if (!item?.properties?.backgroundColor.isNullOrEmpty()) {
                constraint.setBackgroundColor(Color.parseColor(item?.properties?.backgroundColor))
            } else {
                constraint.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.discovery2_dms_shop_card_bg))
            }
        } catch (e: Exception) {
            constraint.setBackgroundColor(MethodChecker.getColor(itemView.context, R.color.discovery2_dms_shop_card_bg))
            e.printStackTrace()
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
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                        mShopCardViewModel.fetchShopCardPaginatedData()
                    }
                }
            }
        })
    }

    private fun handleErrorState() {
        hideShimmer()
        mDiscoveryRecycleAdapter.notifyDataSetChanged()
        if (mShopCardViewModel.getShopList() == null) {
            shopEmptyState?.run {
                title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                refreshBtn?.setOnClickListener {
                    reloadComponent()
                }
                shopEmptyState?.show()
            }
        } else {
            shopEmptyState?.hide()
        }

        mShopCardRecyclerView.hide()
    }

    private fun reloadComponent() {
        mShopCardRecyclerView.show()
        shopEmptyState?.hide()
        mShopCardViewModel.resetComponent()
        mShopCardViewModel.fetchShopCardData()
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        for (i in 1..6) {
            list.add(ComponentsItem(name = ComponentNames.BannerCarouselShimmer.componentName))
        }
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    private fun hideShimmer() {
        mDiscoveryRecycleAdapter.setDataList(arrayListOf())
    }

    private fun addDefaultItemDecorator() {
        if (mShopCardRecyclerView.itemDecorationCount > 0)
            mShopCardRecyclerView.removeItemDecorationAt(0)
        mShopCardRecyclerView.addItemDecoration(shopCardRecyclerViewDecorator)
    }

}