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

private const val ITEM_COUNT_4 = 4
private const val ITEM_COUNT_6 = 6

class ShopCardViewHolder(
    itemView: View,
    fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val shopCardRecyclerViewDecorator = ShopCardItemDecorator()
    private var linearLayoutManager: LinearLayoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private var mShopCardViewModel: ShopCardViewModel? = null

    private val parentContainer = itemView.findViewById<ConstraintLayout>(R.id.parent_container)
    private val shopCardRv = itemView.findViewById<RecyclerView>(R.id.shop_card_rv)
    private val emptyStateView = itemView.findViewById<LocalLoad>(R.id.viewEmptyState)
    private val bgImage = itemView.findViewById<ImageUnify>(R.id.bg_image)

    init {
        linearLayoutManager.initialPrefetchItemCount = ITEM_COUNT_4
        shopCardRv.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        shopCardRv.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mShopCardViewModel = discoveryBaseViewModel as ShopCardViewModel
        mShopCardViewModel?.let {
            getSubComponent().inject(it)
        }
        shopCardRv.show()
        emptyStateView.hide()
        addDefaultItemDecorator()
        if (mShopCardViewModel?.shouldShowShimmer() == true) {
            addShimmer()
        }
        handleShopCardPagination()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel?.getShopCardBackgroundData()?.observe(it) { item ->
                setHolderBackgroundData(item)
            }
            mShopCardViewModel?.syncData?.observe(it) { sync ->
                if (sync) {
                    mDiscoveryRecycleAdapter.notifyDataSetChanged()
                }
            }
            mShopCardViewModel?.getShopCardItemsListData()?.observe(it) { item ->
                shopCardRv.show()
                mDiscoveryRecycleAdapter.setDataList(item)
            }

            mShopCardViewModel?.getShopLoadState()?.observe(it) { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            }

            mShopCardViewModel?.hideShimmer()?.observe(it) { shouldShowErrorState ->
                if (shouldShowErrorState) hideShimmer()
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mShopCardViewModel?.getShopCardItemsListData()?.removeObservers(it)
            mShopCardViewModel?.getShopCardBackgroundData()?.removeObservers(it)
            mShopCardViewModel?.getShopLoadState()?.removeObservers(it)
            mShopCardViewModel?.hideShimmer()?.removeObservers(it)
            mShopCardViewModel?.syncData?.removeObservers(it)
        }
    }

    private fun setHolderBackgroundData(item: ComponentsItem?) {
        try {
            if (item?.isBackgroundPresent == true) return

            if (item?.properties?.backgroundImageUrl.isNullOrEmpty()) {
                bgImage.hide()
            } else {
                bgImage.show()
                bgImage.loadImageWithoutPlaceholder(item?.properties?.backgroundImageUrl)
            }
            if (!item?.properties?.backgroundColor.isNullOrEmpty()) {
                parentContainer.setBackgroundColor(Color.parseColor(item?.properties?.backgroundColor))
            } else {
                parentContainer.setBackgroundColor(
                    MethodChecker.getColor(
                        itemView.context,
                        R.color.discovery2_dms_shop_card_bg
                    )
                )
            }
        } catch (e: Exception) {
            parentContainer.setBackgroundColor(
                MethodChecker.getColor(
                    itemView.context,
                    R.color.discovery2_dms_shop_card_bg
                )
            )
            e.printStackTrace()
        }
    }

    private fun handleShopCardPagination() {
        shopCardRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int =
                    linearLayoutManager.findFirstVisibleItemPosition()
                if (mShopCardViewModel?.isLoadingData() == false && mShopCardViewModel?.isLastPage() == false) {
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                        mShopCardViewModel?.fetchShopCardPaginatedData()
                    }
                }
            }
        })
    }

    private fun handleErrorState() {
        hideShimmer()
        mDiscoveryRecycleAdapter.notifyDataSetChanged()
        if (mShopCardViewModel?.getShopList() == null) {
            emptyStateView.run {
                title?.text =
                    context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                description?.text =
                    context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                refreshBtn?.setOnClickListener {
                    reloadComponent()
                }
                emptyStateView.show()
            }
        } else {
            emptyStateView.hide()
        }

        shopCardRv.hide()
    }

    private fun reloadComponent() {
        shopCardRv.show()
        emptyStateView.hide()
        mShopCardViewModel?.resetComponent()
        mShopCardViewModel?.fetchShopCardData()
    }

    private fun addShimmer() {
        val list: ArrayList<ComponentsItem> = ArrayList()
        for (i in 1..ITEM_COUNT_6) {
            list.add(ComponentsItem(name = ComponentNames.BannerCarouselShimmer.componentName))
        }
        mDiscoveryRecycleAdapter.setDataList(list)
    }

    private fun hideShimmer() {
        mDiscoveryRecycleAdapter.setDataList(arrayListOf())
    }

    private fun addDefaultItemDecorator() {
        if (shopCardRv.itemDecorationCount > 0) {
            shopCardRv.removeItemDecorationAt(0)
        }
        shopCardRv.addItemDecoration(shopCardRecyclerViewDecorator)
    }
}
