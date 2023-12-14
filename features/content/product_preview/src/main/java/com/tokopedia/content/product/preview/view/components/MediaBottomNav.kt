package com.tokopedia.content.product.preview.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by astidhiyaa on 23/11/23
 */
@Composable
fun MediaBottomNav(
    product: BottomNavUiModel,
    onAtcClicked: () -> Unit = {}
) {
    NestTheme(darkTheme = true) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(colorResource(id = unifyprinciplesR.color.Unify_Static_Black))
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 16.dp
                )
        ) {
            val (title, ogPrice, slashedPrice, discountTag, atcBtn) = createRefs()
            NestTypography(
                text = product.title,
                maxLines = 1,
                textStyle = NestTheme.typography.small.copy(
                    color = colorResource(id = R.color.product_preview_dms_bottom_title)
                ),
                modifier = Modifier.constrainAs(title) {
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(atcBtn.start, 8.dp)
                }
            )

            // Final Price
            NestTypography(
                text = product.price.finalPrice,
                maxLines = 1,
                textStyle = NestTheme.typography.heading5.copy(
                    color = colorResource(id = unifyprinciplesR.color.Unify_NN0)
                ),
                modifier = Modifier
                    .heightIn(0.dp, 208.dp)
                    .constrainAs(ogPrice) {
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                        start.linkTo(title.start)
                        top.linkTo(title.bottom, 4.dp)
                    }
            )

            // Slashed price & discount [if there's a discount]
            AnimatedVisibility(visible = product.price is BottomNavUiModel.DiscountedPrice) {
                NestTypography(
                    text = (product.price as BottomNavUiModel.DiscountedPrice).discountedPrice,
                    maxLines = 1,
                    textStyle = NestTheme.typography.small.copy(
                        color = colorResource(id = unifyprinciplesR.color.Unify_NN400),
                        textDecoration = TextDecoration.LineThrough
                    ),
                    modifier = Modifier
                        .constrainAs(slashedPrice) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            start.linkTo(ogPrice.end, 4.dp)
                            bottom.linkTo(ogPrice.bottom)
                            top.linkTo(ogPrice.top)
                        }
                )
                NestTypography(
                    text = product.price.discountPercentage,
                    maxLines = 1,
                    textStyle = NestTheme.typography.small.copy(
                        color = colorResource(id = unifyprinciplesR.color.Unify_RN500),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .constrainAs(discountTag) {
                            width = Dimension.wrapContent
                            height = Dimension.wrapContent
                            start.linkTo(slashedPrice.end, 2.dp)
                            bottom.linkTo(slashedPrice.bottom)
                            top.linkTo(slashedPrice.top)
                            end.linkTo(atcBtn.start, 8.dp)
                        }
                )
            }

            val btnWording = when (product.buttonState) {
                BottomNavUiModel.ButtonState.Active -> R.string.bottom_atc_wording
                BottomNavUiModel.ButtonState.Inactive -> R.string.bottom_remind_wording
                BottomNavUiModel.ButtonState.OOS -> R.string.bottom_oos_wording
                else -> R.string.bottom_atc_wording
            }
            NestButton(
                text = stringResource(id = btnWording),
                onClick = onAtcClicked,
                variant = ButtonVariant.GHOST_INVERTED,
                size = ButtonSize.SMALL,
                isClickable = product.buttonState != BottomNavUiModel.ButtonState.OOS,
                modifier = Modifier
                    .constrainAs(atcBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}

@Preview
@Composable
fun MediaBottomNavPreview() {
    MediaBottomNav(
        product = BottomNavUiModel.Empty
    ) { }
}
