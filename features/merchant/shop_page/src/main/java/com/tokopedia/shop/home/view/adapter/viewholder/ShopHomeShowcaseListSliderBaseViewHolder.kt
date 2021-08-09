package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeShowcaseListSliderAdapter
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel

/**
 * author by Rafli Syam on 05/08/2021
 */
class ShopHomeShowcaseListSliderBaseViewHolder (
        itemView: View,
        childSliderAdapter: ShopHomeShowcaseListSliderAdapter,
        layoutType: Int
) : AbstractViewHolder<ShopHomeShowcaseListSliderUiModel>(itemView) {

    companion object {
        /**
         * Base slider layout
         */
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_base_etalase_list_slider

        const val LAYOUT_TYPE_LINEAR_HORIZONTAL = LinearLayoutManager.HORIZONTAL
        const val LAYOUT_TYPE_GRID_HORIZONTAL = GridLayoutManager.HORIZONTAL
        private const val LAYOUT_TYPE_GRID_COLUMN_SIZE = 2
    }

    private var tvCarouselTitle : TextView? = null
    private var recyclerView : RecyclerView? = null
    private var childAdapter : ShopHomeShowcaseListSliderAdapter? = null
    private var layoutManagerType = layoutType

    init {
        childAdapter = childSliderAdapter
        initView()
    }

    override fun bind(element: ShopHomeShowcaseListSliderUiModel) {
        tvCarouselTitle?.text = element.header.title
        initRecyclerView(element.showcaseListItem)
    }

    private fun initView() {
        tvCarouselTitle = itemView.findViewById(R.id.tvShowcaseSectionTitle)
        recyclerView = itemView.findViewById(R.id.rvShowcaseListWidget)
    }

    private fun initRecyclerView(showcaseListItemData: List<ShopHomeShowcaseListItemUiModel>) {
        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = if (layoutManagerType == LAYOUT_TYPE_GRID_HORIZONTAL) {
                GridLayoutManager(context, LAYOUT_TYPE_GRID_COLUMN_SIZE, GridLayoutManager.HORIZONTAL, false)
            } else {
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            adapter = childAdapter
        }
        childAdapter?.updateDataSet(showcaseListItemData)
    }
}