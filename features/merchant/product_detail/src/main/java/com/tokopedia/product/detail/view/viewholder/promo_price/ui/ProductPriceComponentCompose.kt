package com.tokopedia.product.detail.view.viewholder.promo_price.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource.Remote
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper

private const val NORMAL_PROMO_UI = 1
private const val EMPTY_PROMO_UI = 0

@Composable
fun ProductDetailPriceComponent(
    promoPriceData: PromoPriceUiModel?,
    normalPromoUiModel: Price?,
    priceComponentType: Int,
    normalPriceBoUrl: String,
    onPromoPriceClicked: () -> Unit = {}
) {
    NestTheme {
        if (priceComponentType == NORMAL_PROMO_UI || priceComponentType == EMPTY_PROMO_UI) {
            NormalPriceComponent(normalPromoUiModel, normalPriceBoUrl)
        } else {
            PromoPriceCard(promoPriceData, onPromoPriceClicked)
        }
    }
}

@Composable
fun NormalPriceComponent(
    uiModel: Price?,
    freeOngkirImageUrl: String = ""
) {
    val data = uiModel ?: return
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        crossAxisAlignment = FlowCrossAxisAlignment.Center
    ) {
        NestTypography(
            text = data.priceFmt,
            textStyle = NestTheme.typography.heading2.copy(
                color = NestTheme.colors.NN._950,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(end = 4.dp)
                .tag("txt_main_price")
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (data.slashPriceFmt.isNotEmpty()) {
                NestTypography(
                    modifier = Modifier.tag("text_slash_price"),
                    text = data.slashPriceFmt,
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._600,
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }
            if (data.discPercentage.isNotEmpty()) {
                NestTypography(
                    data.discPercentage,
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.RN._500,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
            if (freeOngkirImageUrl.isNotEmpty()) {
                NestImage(
                    source = Remote(freeOngkirImageUrl, customUIError = {
                    }),
                    type = NestImageType.Rect(0.dp),
                    modifier = Modifier
                        .height(18.dp)
                        .wrapContentWidth()
                        .padding(start = 4.dp)
                        .tag("img_free_ongkir")
                )
            }
        }
    }
}

@Composable
fun PromoPriceHeader(
    mainIconUrl: String,
    promoPriceFmt: String,
    promoSubtitle: String,
    mainTextColor: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (mainIconUrl.isNotEmpty()) {
            NestImage(
                source = Remote(
                    mainIconUrl,
                    customUIError = {
                    }
                ),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
        }

        NestTypography(
            promoPriceFmt,
            textStyle = NestTheme.typography.display1.copy(
                fontWeight = FontWeight.Bold,
                color = mainTextColor.color
            )
        )
        NestTypography(
            promoSubtitle,
            textStyle = NestTheme.typography.small.copy(
                color = mainTextColor.color
            ),
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp, top = 4.dp)
                .weight(1F),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        NestIcon(
            iconId = IconUnify.CHEVRON_RIGHT,
            modifier = Modifier.size(20.dp),
            colorLightEnable = mainTextColor.color,
            colorNightEnable = mainTextColor.color
        )
    }
}

@Composable
fun PromoPriceFooter(
    priceAdditionalFmt: String,
    slashPriceFmt: String,
    boLogo: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val (normalPrice, slashPrice, boImage) = createRefs()
        val context = LocalContext.current
        if (priceAdditionalFmt.isNotEmpty()) {
            NestTypography(
                HtmlLinkHelper(context, priceAdditionalFmt).spannedString?.toAnnotatedString()
                    ?: "",
                modifier = Modifier.constrainAs(normalPrice) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.NN._950
                )
            )
        }

        if (boLogo.isNotEmpty()) {
            NestImage(
                source = Remote(source = boLogo, customUIError = {
                }),
                type = NestImageType.Rect(0.dp),
                modifier = Modifier
                    .constrainAs(boImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.absoluteRight)
                    }
                    .height(18.dp)
                    .tag("img_free_ongkir")
            )
        }

        if (slashPriceFmt.isNotEmpty()) {
            NestTypography(
                slashPriceFmt,
                modifier = Modifier
                    .constrainAs(slashPrice) {
                        linkTo(
                            start = normalPrice.end,
                            top = normalPrice.top,
                            bottom = normalPrice.bottom,
                            end = boImage.start,
                            horizontalBias = 0F
                        )
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 4.dp, end = 4.dp),
                textStyle = NestTheme.typography.small.copy(
                    color = NestTheme.colors.NN._400,
                    textDecoration = TextDecoration.LineThrough
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PromoPriceCard(
    uiModel: PromoPriceUiModel?,
    onPromoPriceClicked: () -> Unit = {}
) {
    val data = uiModel ?: return
    ConstraintLayout(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .clickable {
                onPromoPriceClicked.invoke()
            }
            .background(
                data.cardBackgroundColor.color,
                RoundedCornerShape(10.dp)
            )
    ) {
        val (superGraphic, header, divider, footer) = createRefs()

        if (data.superGraphicIconUrl.isNotEmpty()) {
            NestImage(
                source = Remote(source = data.superGraphicIconUrl, customUIError = {
                }),
                type = NestImageType.Rect(0.dp),
                modifier = Modifier.constrainAs(superGraphic) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.absoluteRight)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
                contentScale = ContentScale.Fit
            )
        }

        PromoPriceHeader(
            mainIconUrl = data.mainIconUrl,
            promoPriceFmt = data.promoPriceFmt,
            promoSubtitle = data.promoSubtitle,
            mainTextColor = data.mainTextColor,
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        DashDivider(
            separatorColor = data.separatorColor,
            modifier = Modifier.constrainAs(divider) {
                top.linkTo(header.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )

        PromoPriceFooter(
            priceAdditionalFmt = data.priceAdditionalFmt,
            slashPriceFmt = data.slashPriceFmt,
            boLogo = data.boIconUrl,
            modifier = Modifier.constrainAs(footer) {
                top.linkTo(divider.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun DashDivider(separatorColor: String, modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10F, 10F), 0f)
    val separatorColorCompose = separatorColor.color

    Canvas(modifier.fillMaxWidth()) {
        drawLine(
            color = separatorColorCompose,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = 1f
        )
    }
}

val String.color
    @Composable
    get() = try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Throwable) {
        NestTheme.colors.NN._0
    }

@SuppressLint("RememberReturnType")
@Composable
@Preview
fun PromoPriceCardPreview() {
    NestTheme {
        Box(
            modifier = Modifier
                .background(NestTheme.colors.NN._0)
                .fillMaxSize()
        ) {
//            Testing()
        }
    }
}
