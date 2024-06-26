package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.WidgetShopPageHomeSliderSquareBinding
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeSliderSquareAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderSquareViewHolder(
    itemView: View,
    private val listener: ShopHomeDisplayWidgetListener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_home_slider_square
    }
    private val viewBinding: WidgetShopPageHomeSliderSquareBinding? by viewBinding()
    private var shopHomeSliderSquareAdapter: ShopHomeSliderSquareAdapter? = null
    private var rvCarouselShopPageHome: RecyclerView? = viewBinding?.rvCarouselShopPageHome
    private var tgHeaderSliderSquare: Typography? = viewBinding?.tgHeaderSliderSquare

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        shopHomeSliderSquareAdapter = ShopHomeSliderSquareAdapter(listener)
        rvCarouselShopPageHome?.apply {
            isNestedScrollingEnabled = false
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecorationShopPage(element.name))
            }
            adapter = shopHomeSliderSquareAdapter
            setRecycledViewPool(recyclerviewPoolListener.parentPool)
        }
        shopHomeSliderSquareAdapter?.displayWidgetUiModel = element
        shopHomeSliderSquareAdapter?.heightRatio = getHeightRatio(element)
        shopHomeSliderSquareAdapter?.parentPosition = adapterPosition
        shopHomeSliderSquareAdapter?.submitList(element.data)
        tgHeaderSliderSquare?.apply {
            val title = element.header.title
            if (title.isEmpty()) {
                hide()
                rvCarouselShopPageHome?.apply {
                    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
                    val marginTop = 8.toPx().takeIf { adapterPosition == 0 }
                        ?: layoutParams.topMargin
                    setMargin(
                        layoutParams.leftMargin,
                        marginTop,
                        layoutParams.rightMargin,
                        layoutParams.bottomMargin
                    )
                }
            } else {
                show()
                text = title
            }
        }
        configColorTheme(element)
    }

    private fun configColorTheme(element: ShopHomeDisplayWidgetUiModel) {
        if (element.header.isOverrideTheme) {
            setReimaginedColorConfig(element.header.colorSchema)
        } else {
            setDefaultColorConfig()
        }
    }

    private fun setReimaginedColorConfig(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        )
        setHeaderColor(titleColor)
    }

    private fun setDefaultColorConfig() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN950_96
        )
        setHeaderColor(titleColor)
    }

    private fun setHeaderColor(titleColor: Int) {
        tgHeaderSliderSquare?.setTextColor(titleColor)
    }

    private fun getIndexRatio(data: ShopHomeDisplayWidgetUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(data: ShopHomeDisplayWidgetUiModel): Float {
        val indexZero = getIndexRatio(data, 0).toFloat()
        val indexOne = getIndexRatio(data, 1).toFloat()
        return (indexOne / indexZero)
    }
}
