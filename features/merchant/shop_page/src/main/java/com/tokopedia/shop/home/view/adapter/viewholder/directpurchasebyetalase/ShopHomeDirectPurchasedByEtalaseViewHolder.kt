package com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.ColorPallete
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.LayoutShopHomeDirectPurchaseByEtalaseBinding
import com.tokopedia.shop.home.view.customview.directpurchase.DirectPurchaseWidgetView
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.viewholder.ShopDirectPurchaseByEtalaseUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.viewallcard.ViewAllCard

class ShopHomeDirectPurchasedByEtalaseViewHolder(
    itemView: View,
    private val shopHomeListener: ShopHomeListener,
) : AbstractViewHolder<ShopDirectPurchaseByEtalaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_direct_purchase_by_etalase

    }

    private val viewBinding: LayoutShopHomeDirectPurchaseByEtalaseBinding? by viewBinding()
    private val containerPlaceholder: View? =
        viewBinding?.layoutPlaceholder?.containerPlaceholder
    private val directPurchaseWidget: DirectPurchaseWidgetView? =
        viewBinding?.shopDirectPurchaseWidget

    override fun bind(element: ShopDirectPurchaseByEtalaseUiModel) {
        if(element.isWidgetShowPlaceholder()){
            showPlaceholderView()
        } else {
            setDirectPurchaseWidgetData(element)
        }

    }

    private fun showPlaceholderView() {
        containerPlaceholder?.show()
        directPurchaseWidget?.hide()
    }

    private fun setDirectPurchaseWidgetData(element: ShopDirectPurchaseByEtalaseUiModel) {
        containerPlaceholder?.hide()
        directPurchaseWidget?.apply {
            setListener(shopHomeListener.getShopPageHomeFragment())
            if (element.header.isOverrideTheme) {
                setColor(
                    ColorPallete(
                        primaryTextColor = element.header.colorSchema.getColorSchema(
                            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
                        )?.value.orEmpty(),
                        secondaryTextColor = element.header.colorSchema.getColorSchema(
                            ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
                        )?.value.orEmpty(),
                        slashedTextColor = element.header.colorSchema.getColorSchema(
                            ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
                        )?.value.orEmpty(),
                        buttonAccent = element.header.colorSchema.getColorSchema(
                            ShopPageColorSchema.ColorSchemaName.CTA_TEXT_LINK_COLOR
                        )?.value.orEmpty(),
                        white = ShopUtil.getColorHexString(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White),
                        darkGrey = ShopUtil.getColorHexString(context, R.color.dms_static_Unify_NN600_light),
                    )
                )
                val isDarkPattern =
                    shopHomeListener.getPatternColorType() == ShopPageHeaderLayoutUiModel.ColorType.DARK.value
                if (isDarkPattern) {
                    setSeeAllCardModeType(ViewAllCard.MODE_INVERT)
                } else {
                    setSeeAllCardModeType(ViewAllCard.MODE_NORMAL)
                }
                setAdaptiveLabelDiscount(false)
            } else {
                setColor(ColorPallete())
                setSeeAllCardModeType(ViewAllCard.MODE_NORMAL)
                setAdaptiveLabelDiscount(true)
            }
            setData(element.widgetData)
        }
    }

}
