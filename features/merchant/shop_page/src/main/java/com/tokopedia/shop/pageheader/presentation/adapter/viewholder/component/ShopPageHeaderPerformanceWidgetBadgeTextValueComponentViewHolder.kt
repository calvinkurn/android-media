package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.LayoutShopPerformanceWidgetBadgeTextValueComponentBinding
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderBadgeTextValueComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_2
import com.tokopedia.unifyprinciples.Typography.Companion.DISPLAY_3
import com.tokopedia.unifyprinciples.Typography.Companion.HEADING_1
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderPerformanceWidgetBadgeTextValueComponentViewHolder(
    itemView: View,
    private val shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
    private val listener: Listener
) : AbstractViewHolder<ShopPageHeaderBadgeTextValueComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_performance_widget_badge_text_value_component
    }

    interface Listener {
        fun onShopPerformanceWidgetBadgeTextValueItemClicked(
            componentModel: ShopPageHeaderBadgeTextValueComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )

        fun onImpressionShopPerformanceWidgetBadgeTextValueItem(
            componentModel: ShopPageHeaderBadgeTextValueComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopPerformanceWidgetBadgeTextValueComponentBinding? by viewBinding()
    private val textViewFirstLine: Typography? = viewBinding?.textFirstLine
    private val imageViewFirstLine: ImageView? = viewBinding?.imageFirstLine
    private val textViewSecondLine: Typography? = viewBinding?.textSecondLine

    override fun bind(model: ShopPageHeaderBadgeTextValueComponentUiModel) {
        val textFirstLine = model.text.getOrNull(0)?.textHtml.orEmpty()
        val imageUrlFirstLine = model.text.getOrNull(0)?.icon.orEmpty()
        val appLink = model.text.getOrNull(0)?.textLink.orEmpty()
        val textSecondLine = model.text.getOrNull(1)?.textHtml.orEmpty()
        textViewFirstLine?.apply {
            text = MethodChecker.fromHtml(textFirstLine)
            if(ShopUtil.isFoldable)
                setType(DISPLAY_2)
        }
        imageViewFirstLine?.apply {
            if (imageUrlFirstLine.isNotEmpty()) {
                show()
                loadImage(imageUrlFirstLine)
            } else {
                hide()
            }
        }
        textViewSecondLine?.apply {
            text = MethodChecker.fromHtml(textSecondLine)
            if(ShopUtil.isFoldable)
                setType(DISPLAY_3)
        }
        itemView.setOnClickListener {
            listener.onShopPerformanceWidgetBadgeTextValueItemClicked(
                model,
                shopPageHeaderWidgetUiModel
            )
        }
        itemView.addOnImpressionListener(model) {
            listener.onImpressionShopPerformanceWidgetBadgeTextValueItem(
                model,
                shopPageHeaderWidgetUiModel
            )
        }
    }
}
