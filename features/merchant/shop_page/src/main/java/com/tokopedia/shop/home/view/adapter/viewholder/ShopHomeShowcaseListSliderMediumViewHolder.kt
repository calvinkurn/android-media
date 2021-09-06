package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * author by Rafli Syam on 03/08/2021
 */
class ShopHomeShowcaseListSliderMediumViewHolder (
        itemView: View,
        private val itemWidgetListener: ShopHomeShowcaseListWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        /**
         * Slider medium layout types
         */
        @LayoutRes
        val ITEM_SLIDER_MEDIUM_LAYOUT = R.layout.item_shop_home_etalase_list_slider_medium
    }

    private var showcaseItemImage: ImageUnify? = null
    private var showcaseItemName: Typography? = null

    init {
        showcaseItemImage = itemView.findViewById(R.id.img_showcase_item_slider_medium)
        showcaseItemName = itemView.findViewById(R.id.tv_showcase_name_item_slider_medium)
    }

    fun bind(element: ShopHomeShowcaseListItemUiModel) {
        // try catch to avoid crash ImageUnify on loading image with Glide
        try {
            if (showcaseItemImage?.context?.isValidGlideContext() == true) {
                element.imageUrl.let { showcaseItemImage?.setImageUrl(it) }
            }
        } catch (e: Throwable) {
        }

        if (element.isShowEtalaseName) {
            showcaseItemName?.visible()
            showcaseItemName?.text = element.name
        } else {
            showcaseItemName?.gone()
        }

        itemView.addOnImpressionListener(element) {
            itemWidgetListener.onShowcaseListWidgetItemImpression(element, (adapterPosition+1))
        }
        itemView.setOnClickListener {
            itemWidgetListener.onShowcaseListWidgetItemClicked(element, (adapterPosition+1))
        }
    }
}