package com.tokopedia.minicart.cartlist.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class MiniCartAccordionViewHolder(private val view: View,
                                  private val listener: MiniCartListActionListener)
    : AbstractViewHolder<MiniCartAccordionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_accordion
    }

    private val textAccordion: Typography? by lazy {
        view.findViewById(R.id.text_accordion)
    }
    private val imageChevron: ImageUnify? by lazy {
        view.findViewById(R.id.image_chevron)
    }

    override fun bind(element: MiniCartAccordionUiModel) {
        if (element.isCollapsed) {
            textAccordion?.text = element.showMoreWording
            imageChevron?.rotation = 0f
        } else {
            textAccordion?.text = element.showLessWording
            imageChevron?.rotation = 180f
        }
        itemView.setOnClickListener {
            listener.onToggleShowHideUnavailableItemsClicked()
        }
    }

}