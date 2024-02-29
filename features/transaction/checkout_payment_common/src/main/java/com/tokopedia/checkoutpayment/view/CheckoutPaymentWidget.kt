package com.tokopedia.checkoutpayment.view

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tokopedia.checkoutpayment.R
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
import com.tokopedia.purchase_platform.common.utils.setOnClickDebounceListener

@Composable
fun CheckoutPaymentWidget(
    data: CheckoutPaymentWidgetData,
    modifier: Modifier = Modifier,
    onRetryClickedListener: () -> Unit = {}
) {
    if (data.state != CheckoutPaymentWidgetState.None) {
        NestTheme {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                NestDivider(size = NestDividerSize.Large, modifier = Modifier.fillMaxWidth())
                when (data.state) {
                    is CheckoutPaymentWidgetState.Loading -> {
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
                    }

                    CheckoutPaymentWidgetState.Error -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NestTheme.colors.YN._50)
                                .padding(start = 16.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                                .setOnClickDebounceListener {
                                    onRetryClickedListener()
                                }
                        ) {
                            NestTypography(
                                text = data.errorMessage,
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
                    }

                    CheckoutPaymentWidgetState.Normal -> {
                        NestTypography(
                            text = stringResource(id = R.string.checkout_payment_method_label),
                            textStyle = NestTheme.typography.display3.copy(
                                color = NestTheme.colors.NN._950,
                                fontWeight = FontWeight.W800
                            ),
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
                        )
                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 28.dp, bottom = 12.dp)
                        ) {
                            NestImage(
                                ImageSource.Remote(data.logoUrl),
                                modifier = Modifier
                                    .size(28.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            ) {
                                Row {
                                    NestTypography(
                                        text = data.title,
                                        textStyle = NestTheme.typography.display3.copy(
                                            color = if (data.isTitleRed) NestTheme.colors.RN._500 else NestTheme.colors.NN._950
                                        ),
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    NestTypography(
                                        text = data.subtitle,
                                        textStyle = NestTheme.typography.display3.copy(
                                            color = NestTheme.colors.NN._600
                                        ),
                                        modifier = Modifier
                                            .padding(start = 4.dp),
                                        maxLines = 1
                                    )
                                }
                                NestTypography(
                                    text = data.description,
                                    modifier = Modifier.padding(top = 2.dp),
                                    textStyle = NestTheme.typography.display3.copy(
                                        color = if (data.isDescriptionRed) NestTheme.colors.RN._500 else NestTheme.colors.NN._600
                                    )
                                )
                            }
                            NestIcon(
                                iconId = IconUnify.CHEVRON_RIGHT,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterVertically),
                                colorLightEnable = NestTheme.colors.NN._500,
                                colorNightEnable = NestTheme.colors.NN._500
                            )
                        }
                    }

                    else -> {
                        /* no-op */
                    }
                }
            }
        }
    }
}

@LightAndDarkModePreview
@Composable
fun CheckoutPaymentWidgetPreview() {
    Column {
        CheckoutPaymentWidget(
            CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Loading)
        )
        CheckoutPaymentWidget(
            CheckoutPaymentWidgetData(
                state = CheckoutPaymentWidgetState.Error,
                errorMessage = "Pembayaran gagal dimuat, silahkan coba lagi."
            )
        )
        CheckoutPaymentWidget(
            CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.None)
        )
        CheckoutPaymentWidget(
            CheckoutPaymentWidgetData(
                state = CheckoutPaymentWidgetState.Normal,
                title = "Bank nama panjang sekali",
                subtitle = "(isi saldo rekening panjang sekali)",
                description = "cicilan sangat panjang hingga butuh 2 baris utk menuliskannya semua"
            )
        )
        CheckoutPaymentWidget(
            CheckoutPaymentWidgetData(
                state = CheckoutPaymentWidgetState.Normal,
                title = "Bank nama panjang sekali",
                isTitleRed = true,
                subtitle = "(isi saldo rekening panjang sekali)",
                description = "cicilan sangat panjang hingga butuh 2 baris utk menuliskannya semua",
                isDescriptionRed = true
            )
        )
    }
}
