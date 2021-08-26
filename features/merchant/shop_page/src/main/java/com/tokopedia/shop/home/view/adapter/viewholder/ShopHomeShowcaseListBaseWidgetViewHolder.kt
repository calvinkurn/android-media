package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeShowcaseListWidgetAdapter
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import kotlin.math.roundToInt

/**
 * author by Rafli Syam on 05/08/2021
 */
class ShopHomeShowcaseListBaseWidgetViewHolder (
        itemView: View,
        private var childWidgetAdapter: ShopHomeShowcaseListWidgetAdapter,
        private var layoutManagerType: Int,
        private var gridColumnSize: Int
) : AbstractViewHolder<ShopHomeShowcaseListSliderUiModel>(itemView) {

    companion object {
        /**
         * Base Showcase Widget layout
         */
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_base_etalase_list_widget

        const val LAYOUT_TYPE_LINEAR_HORIZONTAL = 100
        const val LAYOUT_TYPE_GRID_HORIZONTAL = 200
        const val LAYOUT_TYPE_GRID_VERTICAL = 300
        const val LAYOUT_TYPE_GRID_DEFAULT_COLUMN_SIZE = 1
        const val LAYOUT_TYPE_GRID_TWO_COLUMN_SIZE = 2
        const val LAYOUT_TYPE_GRID_THREE_COLUMN_SIZE = 3

        fun getReorderShowcasePositionForTwoRowsSlider(
                showcaseListItemData: List<ShopHomeShowcaseListItemUiModel>
        ): List<ShopHomeShowcaseListItemUiModel> {
            val listSize = showcaseListItemData.size
            val thresholds = ((listSize.toDouble() / 2).roundToInt())
            val newList = MutableList(listSize) { ShopHomeShowcaseListItemUiModel() }
            var pointer = 1
            newList[0] = showcaseListItemData.first()
            newList[listSize - 1] = showcaseListItemData.last()
            showcaseListItemData.forEachIndexed { index, showcase ->
                if (index.isMoreThanZero() && index < listSize) {
                    if (index < thresholds) {
                        newList[(index + pointer)] = showcase
                        pointer++
                    } else {
                        pointer--
                        newList[(index - pointer)] = showcase
                    }
                }
            }
            return newList
        }
    }

    private var tvCarouselTitle : TextView? = null
    private var recyclerView : RecyclerView? = null

    init {
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
            layoutManager = when (layoutManagerType) {
                LAYOUT_TYPE_LINEAR_HORIZONTAL -> {
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
                LAYOUT_TYPE_GRID_HORIZONTAL -> {
                    GridLayoutManager(context, gridColumnSize, GridLayoutManager.HORIZONTAL, false)
                }
                LAYOUT_TYPE_GRID_VERTICAL -> {
                    object : GridLayoutManager(context, gridColumnSize, GridLayoutManager.VERTICAL, false) {
                        // disable scroll if vertical grid column type
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    }
                }
                else -> LinearLayoutManager(context, layoutManagerType, false)
            }
            adapter = childWidgetAdapter
        }
        childWidgetAdapter.updateDataSet(showcaseListItemData)
    }
}