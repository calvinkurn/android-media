package com.tokopedia.checkoutpayment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.components.loader.NestShimmerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.purchase_platform.common.utils.LightAndDarkModePreview

data class CheckoutPaymentWidgetData(
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@Composable
fun CheckoutPaymentWidget(data: CheckoutPaymentWidgetData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        NestDivider(size = NestDividerSize.Large, modifier = Modifier.fillMaxWidth())
        if (data.isLoading) {
            NestLoader(
                variant = NestLoaderType.Shimmer(NestShimmerType.Line),
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, bottom = 12.dp)
                    .height(12.dp)
                    .width(143.dp)
            )
            Row(
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
            ) {
                NestLoader(
                    variant = NestLoaderType.Shimmer(NestShimmerType.Circle),
                    modifier = Modifier.size(28.dp)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    NestLoader(
                        variant = NestLoaderType.Shimmer(NestShimmerType.Line),
                        modifier = Modifier
                            .height(12.dp)
                            .width(48.dp)
                    )
                    NestLoader(
                        variant = NestLoaderType.Shimmer(NestShimmerType.Line),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(12.dp)
                            .width(143.dp)
                    )
                }
            }
        } else if (data.isError) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NestTheme.colors.YN._50)
                    .padding(start = 16.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
            ) {
                NestTypography(
                    text = "Pembayaran gagal ditampilkan. Coba lagi, yuk! Pembayaran gagal ditampilkan. Coba lagi, yuk!",
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._950
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                NestIcon(
                    iconId = IconUnify.RELOAD,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
            ) {
                NestTypography(
                    text = stringResource(id = R.string.checkout_payment_method_label),
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.NN._950,
                        fontWeight = FontWeight.W800
                    )
                )
                NestTypography(
                    text = "Ganti",
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth(),
                    textStyle = NestTheme.typography.display3.copy(
                        color = NestTheme.colors.GN._500,
                        fontWeight = FontWeight.W800,
                        textAlign = TextAlign.End
                    )
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 28.dp, bottom = 12.dp)
            ) {
                NestImage(
                    ImageSource.Remote(""),
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterVertically)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    NestTypography(
                        text = "BCA",
                        textStyle = NestTheme.typography.display3.copy(
                            color = NestTheme.colors.NN._950
                        )
                    )
                    NestTypography(
                        text = "Cicil 3x Rp234.123.566 * Cicil 3x Rp234.123.566 * Cicil 3x Rp234.123.566",
                        modifier = Modifier.padding(top = 2.dp),
                        textStyle = NestTheme.typography.display3.copy(
                            color = NestTheme.colors.NN._600
                        )
                    )
                }
            }
        }
    }
}

@LightAndDarkModePreview
@Composable
fun CheckoutPaymentWidgetPreview() {
    NestTheme {
        Column {
            CheckoutPaymentWidget(
                CheckoutPaymentWidgetData(isLoading = true)
            )
            CheckoutPaymentWidget(
                CheckoutPaymentWidgetData(isError = true)
            )
            CheckoutPaymentWidget(
                CheckoutPaymentWidgetData()
            )
        }
    }
}
