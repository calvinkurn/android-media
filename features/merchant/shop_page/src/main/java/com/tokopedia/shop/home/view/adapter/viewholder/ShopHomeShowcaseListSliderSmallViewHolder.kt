package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeEtalaseListSliderSmallBinding
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListSliderUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * author by Rafli Syam on 05/08/2021
 */
class ShopHomeShowcaseListSliderSmallViewHolder(
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
    private val viewBinding: ItemShopHomeEtalaseListSliderSmallBinding? by viewBinding()
    private var showcaseItemImage: ImageUnify? = null
    private var showcaseItemName: Typography? = null

    init {
        showcaseItemImage = viewBinding?.imgShowcaseItemSliderSmall
        showcaseItemName = viewBinding?.tvShowcaseNameItemSliderSmall
    }

    fun bind(
        element: ShopHomeShowcaseListItemUiModel,
        shopHomeShowcaseListSliderUiModel: ShopHomeShowcaseListSliderUiModel,
        parentPosition: Int
    ) {
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
            itemWidgetListener.onShowcaseListWidgetItemImpression(element, adapterPosition)
        }
        itemView.setOnClickListener {
            itemWidgetListener.onShowcaseListWidgetItemClicked(
                shopHomeShowcaseListSliderUiModel,
                element,
                adapterPosition,
                parentPosition
            )
        }
        configColorTheme(shopHomeShowcaseListSliderUiModel)
    }

    private fun configColorTheme(shopHomeShowcaseListSliderUiModel: ShopHomeShowcaseListSliderUiModel) {
        if (shopHomeShowcaseListSliderUiModel.header.isOverrideTheme) {
            configReimaginedColor(shopHomeShowcaseListSliderUiModel.header.colorSchema)
        } else {
            configDefaultColor()
        }
    }

    private fun configDefaultColor() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            unifyprinciplesR.color.Unify_NN950_96
        )
        showcaseItemName?.setTextColor(titleColor)
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        showcaseItemName?.setTextColor(titleColor)
    }
}
