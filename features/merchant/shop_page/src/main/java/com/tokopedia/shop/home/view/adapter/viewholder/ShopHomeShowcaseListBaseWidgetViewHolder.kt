package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeBaseEtalaseListWidgetBinding
import com.tokopedia.shop.home.view.adapter.ShopHomeShowcaseListWidgetAdapter
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.roundToInt
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * author by Rafli Syam on 05/08/2021
 */
class ShopHomeShowcaseListBaseWidgetViewHolder(
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
            val medianListSizeRounded = ((listSize.toDouble() / 2)).roundToInt()
            return mutableListOf<ShopHomeShowcaseListItemUiModel>().apply {
                for (cur in 0 until medianListSizeRounded) {
                    showcaseListItemData.getOrNull(cur)?.let {
                        add(it)
                    }
                    showcaseListItemData.getOrNull(cur + medianListSizeRounded)?.let {
                        add(it)
                    }
                }
            }
        }
    }
    private val viewBinding: ItemShopHomeBaseEtalaseListWidgetBinding? by viewBinding()
    private var tvCarouselTitle: TextView? = null
    private var recyclerView: RecyclerView? = null

    init {
        initView()
        initRecyclerView()
    }

    override fun bind(element: ShopHomeShowcaseListSliderUiModel) {
        tvCarouselTitle?.apply {
            val title = element.header.title
            text = title
            if (title.isNotEmpty()) {
                show()
            } else {
                hide()
            }
        }
        childWidgetAdapter.setShopHomeShowcaseListSliderUiModel(element)
        childWidgetAdapter.setParentPosition(adapterPosition)
        childWidgetAdapter.updateDataSet(element.showcaseListItem)
        setWidgetImpressionListener(element)
        configColorTheme(element)
    }

    private fun configColorTheme(element: ShopHomeShowcaseListSliderUiModel) {
        if (element.header.isOverrideTheme) {
            configReimaginedColor(element.header.colorSchema)
        } else {
            configDefaultColor()
        }
    }

    private fun configDefaultColor() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN950_96
        )
        tvCarouselTitle?.setTextColor(titleColor)
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        tvCarouselTitle?.setTextColor(titleColor)
    }

    private fun setWidgetImpressionListener(model: ShopHomeShowcaseListSliderUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            childWidgetAdapter.showcaseListWidgetListener.onShowcaseListWidgetImpression(model, adapterPosition)
        }
    }

    private fun initView() {
        tvCarouselTitle = viewBinding?.tvShowcaseSectionTitle
        recyclerView = viewBinding?.rvShowcaseListWidget
    }

    private fun initRecyclerView() {
        recyclerView?.apply {
            isNestedScrollingEnabled = false
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
    }
}
