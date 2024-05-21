package com.tokopedia.catalogcommon.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.WidgetPriceCtaSellerOfferingBinding
import com.tokopedia.catalogcommon.listener.PriceCtaSellerOfferingListener
import com.tokopedia.catalogcommon.uimodel.PriceCtaSellerOfferingUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.catalogcommon.R as catalogcommonR

class PriceCtaSellerOfferingViewHolder(itemView: View, val listener: PriceCtaSellerOfferingListener?) :
    AbstractViewHolder<PriceCtaSellerOfferingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = catalogcommonR.layout.widget_price_cta_seller_offering
    }

    private val binding by viewBinding<WidgetPriceCtaSellerOfferingBinding>()

    override fun bind(element: PriceCtaSellerOfferingUiModel) {
        binding?.apply {
            tvTitle.setTextColor(MethodChecker.getColor(itemView.context, element.textTitleColor.orZero()))
            tvPriceRange.setTextColor(MethodChecker.getColor(itemView.context, element.textPriceColor.orZero()))
            tvPriceRange.text = itemView.context.getString(catalogcommonR.string.catalog_prefix_price_cta_seller_offering, element.price)
            ivButtonRight.setImage(
                IconUnify.CHEVRON_RIGHT,
                newLightEnable = MethodChecker.getColor(itemView.context, element.iconColor.orZero()),
                newDarkEnable = MethodChecker.getColor(itemView.context, element.iconColor.orZero())
            )
            ivButtonRight.setOnClickListener {
                listener?.onSellerOfferingButtonRightClicked(productId = element.productId, shopId = element.shopId)
            }

            val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_rounded_border_dynamic_color)
            if (drawable is GradientDrawable) {
                element.widgetBackgroundColor?.let { drawable.setColor(it) }
                clInfo.background = drawable
            }
        }
    }
}
