package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.databinding.WidgetPriceCtaSellerOfferingBinding
import com.tokopedia.catalogcommon.uimodel.PriceCtaSellerOfferingUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.catalogcommon.R as catalogcommonR


class PriceCtaSellerOfferingViewHolder(itemView: View) :
    AbstractViewHolder<PriceCtaSellerOfferingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = catalogcommonR.layout.widget_price_cta_seller_offering
    }

    private val binding by viewBinding<WidgetPriceCtaSellerOfferingBinding>()

    override fun bind(element: PriceCtaSellerOfferingUiModel) {
        binding?.apply {
            composeView.setBackgroundColor(element.widgetBackgroundColor.orZero())
            composeView.setContent {
                NestTheme {
                    PriceCtaSellerOfferingCompose(
                        element.price,
                        element.textTitleColor.orZero(),
                        element.textPriceColor.orZero(),
                        element.iconColor.orZero()
                    )
                }
            }
        }
    }
}

@Composable
fun PriceCtaSellerOfferingCompose(
    price: String,
    textTitleColor: Int,
    textPriceColor: Int,
    iconColor: Int
) {

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                NestTypography(
                    text = stringResource(id = catalogcommonR.string.title_price_cta_seller_offering),
                    textStyle = NestTheme.typography.display2.copy(
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = textTitleColor)
                    ),
                    maxLines = 1
                )
                NestTypography(
                    modifier = Modifier.padding(top = 5.dp),
                    text = stringResource(
                        id = catalogcommonR.string.catalog_prefix_price_cta_seller_offering,
                        price
                    ),
                    textStyle = NestTheme.typography.display2.copy(
                        fontWeight = FontWeight.Normal,
                        color = colorResource(id = textPriceColor)
                    ),
                    maxLines = 1
                )
            }
            NestIcon(
                iconId = IconUnify.CHEVRON_RIGHT,
                colorLightEnable = colorResource(id = iconColor),
                colorNightEnable = colorResource(id = iconColor)
            )
        }
        Divider()
    }
}
