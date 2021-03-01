package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderBadgeTextValueComponentUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopPerformanceWidgetBadgeTextValueComponentViewHolder(
    itemView: View
): AbstractViewHolder<ShopHeaderBadgeTextValueComponentUiModel>(itemView) {

    companion object{
        val LAYOUT = R.layout.layout_shop_performance_widget_badge_text_value_component
    }

    private val textViewFirstLine: Typography? = itemView.findViewById(R.id.text_first_line)
    private val imageViewFirstLine: ImageView? = itemView.findViewById(R.id.image_first_line)
    private val textViewSecondLine: Typography? = itemView.findViewById(R.id.text_second_line)

    override fun bind(model: ShopHeaderBadgeTextValueComponentUiModel) {
        val textFirstLine = model.text.getOrNull(0)?.textHtml.orEmpty()
        val imageUrlFirstLine = model.text.getOrNull(0)?.icon.orEmpty()
        val textSecondLine = model.text.getOrNull(1)?.textHtml.orEmpty()
        textViewFirstLine?.text = MethodChecker.fromHtml(textFirstLine)
        imageViewFirstLine?.apply {
            if(imageUrlFirstLine.isNotEmpty()){
                show()
                loadImage(imageUrlFirstLine)
            }else{
                hide()
            }
        }
        textViewSecondLine?.text = MethodChecker.fromHtml(textSecondLine)
    }

}