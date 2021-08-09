package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeShowcaseListWidgetAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * author by Rafli Syam on 05/08/2021
 */
class ShopHomeShowcaseListSliderSmallViewHolder (
        itemView: View,
        private val itemWidgetListener: ShopHomeShowcaseListWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        /**
         * Slider small layout types
         */
        @LayoutRes
        val ITEM_SLIDER_SMALL_LAYOUT = R.layout.item_shop_home_etalase_list_slider_small
    }

    private var showcaseItemImage: ImageUnify? = null
    private var showcaseItemName: Typography? = null

    init {
        showcaseItemImage = itemView.findViewById(R.id.img_showcase_item_slider_small)
        showcaseItemName = itemView.findViewById(R.id.tv_showcase_name_item_slider_small)
    }

    fun bind(element: ShopHomeShowcaseListItemUiModel) {
        // try catch to avoid crash ImageUnify on loading image with Glide
        try {
            if (showcaseItemImage?.context?.isValidGlideContext() == true) {
                element.imageUrl.let { showcaseItemImage?.setImageUrl(it) }
            }
        } catch (e: Throwable) {
        }
        showcaseItemName?.text = element.name

        itemView.setOnClickListener {
            val showcaseId = ShopHomeShowcaseListWidgetAdapter.getShowcaseIdFromApplink(element.appLink)
            itemWidgetListener.onShowcaseListWidgetItemClicked(showcaseId)
        }
    }
}