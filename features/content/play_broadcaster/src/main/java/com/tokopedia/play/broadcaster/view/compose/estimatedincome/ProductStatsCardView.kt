package com.tokopedia.play.broadcaster.view.compose.estimatedincome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.card.NestCard
import com.tokopedia.nest.components.card.NestCardType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.play.broadcaster.ui.model.stats.ProductStatsUiModel
import com.tokopedia.play.broadcaster.R

/**
 * Created by Jonathan Darwin on 04 March 2024
 */
@Composable
fun ProductStatsCardView(
    productStats: ProductStatsUiModel
) {
    NestCard(
        type = NestCardType.NoBorder,
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(12.dp), clip = false)
            .border(BorderStroke(1.dp, MaterialTheme.colors.surface), RoundedCornerShape(12.dp))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (
                imgProduct,
                txtProduct,
                estimatedIncomeSection,
                visitSection,
                addToCartSection,
                soldSection
            ) = createRefs()

            NestImage(
                source = ImageSource.Remote(productStats.imageUrl),
                modifier = Modifier
                    .size(48.dp)
                    .constrainAs(imgProduct) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            
            NestTypography(
                text = productStats.name,
                textStyle = NestTheme.typography.body3,
                modifier = Modifier.constrainAs(txtProduct) {
                    start.linkTo(imgProduct.end, 16.dp)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
            )

            ProductStatsSection(
                label = stringResource(id = R.string.play_broadcaster_live_stats_estimated_income_label),
                text = productStats.estimatedIncomeFmt,
                modifier = Modifier.constrainAs(estimatedIncomeSection) {
                    top.linkTo(txtProduct.bottom, 16.dp)
                    start.linkTo(txtProduct.start)
                    end.linkTo(visitSection.start, 16.dp)

                    width = Dimension.fillToConstraints
                }
            )

            ProductStatsSection(
                label = stringResource(id = R.string.play_broadcaster_live_stats_visit_label),
                text = productStats.visitPdpFmt,
                modifier = Modifier.constrainAs(visitSection) {
                    top.linkTo(estimatedIncomeSection.top)
                    start.linkTo(estimatedIncomeSection.end)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
            )

            ProductStatsSection(
                label = stringResource(id = R.string.play_broadcaster_live_stats_add_to_cart_label),
                text = productStats.addToCartFmt,
                modifier = Modifier.constrainAs(addToCartSection) {
                    top.linkTo(estimatedIncomeSection.bottom, 16.dp)
                    start.linkTo(estimatedIncomeSection.start)
                    end.linkTo(estimatedIncomeSection.end)

                    width = Dimension.fillToConstraints
                }
            )

            ProductStatsSection(
                label = stringResource(id = R.string.play_broadcaster_live_stats_total_sold_label),
                text = productStats.productSoldQtyFmt,
                modifier = Modifier.constrainAs(soldSection) {
                    top.linkTo(addToCartSection.top)
                    start.linkTo(visitSection.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Composable
private fun ProductStatsSection(
    label: String,
    text: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
    ) {
        NestTypography(
            text = label,
            textStyle = NestTheme.typography.small.copy(
                color = NestTheme.colors.NN._600
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        NestTypography(
            text = text,
            textStyle = NestTheme.typography.paragraph3.copy(
                color = NestTheme.colors.NN._950,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
@Preview
private fun ProductStatsCardViewPreview() {
    NestTheme {
        ProductStatsCardView(
            productStats = ProductStatsUiModel(
                id = "123",
                name = "Product Name",
                imageUrl = "",
                addToCartFmt = "1",
                paymentVerifiedFmt = "2",
                visitPdpFmt = "3",
                productSoldQtyFmt = "4",
                estimatedIncomeFmt = "Rp5.000.000",
            )
        )
    }
}
