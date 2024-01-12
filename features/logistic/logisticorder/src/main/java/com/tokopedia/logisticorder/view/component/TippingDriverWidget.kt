package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.TippingModel
import com.tokopedia.logisticorder.utils.TippingConstant
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource

@Composable
fun TippingDriverWidget(
    modifier: Modifier,
    tipping: TippingModel,
    onClickTippingButton: (model: TippingModel) -> Unit
) {
    if (tipping.eligibleForTipping) {
        Card(
            modifier = modifier,
            backgroundColor = if (tipping.status == TippingConstant.OPEN) colorResource(id = R.color.dms_background_tipping_gojek_open) else NestTheme.colors.NN._0
        ) {
            ConstraintLayout {
                val (tippingLogo, tippingText, tippingDescription, tippingButton, tippingBg) = createRefs()
                val verticalChain = createVerticalChain(
                    tippingText,
                    tippingDescription,
                    chainStyle = ChainStyle.Packed
                )
                constrain(verticalChain) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                NestImage(
                    modifier = Modifier.constrainAs(tippingBg) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        visibility =
                            if (tipping.status == TippingConstant.OPEN) Visibility.Visible else Visibility.Gone
                    },
                    contentScale = ContentScale.FillHeight,
                    source = ImageSource.Painter(R.drawable.background_tipping_gojek)
                )
                NestImage(
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .constrainAs(tippingLogo) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, margin = 12.dp)
                            bottom.linkTo(parent.bottom)
                            visibility =
                                if (tipping.status == TippingConstant.OPEN) Visibility.Visible else Visibility.Gone
                        },
                    type = NestImageType.Rect(),
                    source = ImageSource.Remote(TokopediaImageUrl.ICON_OPEN_TIPPING_GOJEK)
                )
                NestTypography(
                    modifier = Modifier.constrainAs(tippingText) {
                        top.linkTo(tippingLogo.top)
                        start.linkTo(tippingLogo.end, margin = 12.dp, goneMargin = 12.dp)
                        end.linkTo(tippingButton.start)
                        width = Dimension.fillToConstraints
                    },
                    text = tipping.statusTitle,
                    textStyle = NestTheme.typography.display2.copy(
                        color = if (tipping.status == TippingConstant.OPEN) NestTheme.colors.NN._0 else NestTheme.colors.NN._950,
                        fontWeight = FontWeight.Bold
                    )
                )
                NestTypography(
                    modifier = Modifier.constrainAs(tippingDescription) {
                        top.linkTo(tippingText.bottom)
                        start.linkTo(tippingText.start)
                        end.linkTo(tippingButton.start)
                        width = Dimension.fillToConstraints
                    },
                    text = tipping.statusSubtitle,
                    textStyle = NestTheme.typography.paragraph3.copy(color = if (tipping.status == TippingConstant.OPEN) NestTheme.colors.NN._0 else NestTheme.colors.NN._950)
                )
                NestButton(
                    modifier = Modifier.constrainAs(tippingButton) {
                        end.linkTo(parent.end, margin = 12.dp)
                        top.linkTo(tippingLogo.top, margin = 12.dp, goneMargin = 12.dp)
                        start.linkTo(tippingText.end)
                        bottom.linkTo(tippingLogo.bottom, margin = 12.dp, goneMargin = 12.dp)
                    },
                    text = tipping.buttonText,
                    onClick = { onClickTippingButton(tipping) },
                    variant = if (tipping.status == TippingConstant.OPEN) ButtonVariant.GHOST_ALTERNATE else ButtonVariant.GHOST
                )
            }
        }
    }
}

private val TippingModel.buttonText: String
    @Composable
    get() {
        return when (status) {
            TippingConstant.SUCCESS_PAYMENT, TippingConstant.SUCCESS_TIPPING -> stringResource(R.string.btn_tipping_success_text)
            TippingConstant.WAITING_PAYMENT -> stringResource(R.string.btn_tipping_waiting_payment_text)
            TippingConstant.REFUND_TIP -> stringResource(R.string.btn_tipping_refund_text)
            else -> stringResource(R.string.btn_tipping_open_text)
        }
    }

@Preview
@Composable
private fun TippingDriverWidgetOpenPreview() {
    val tipping = TippingModel(
        status = TippingConstant.OPEN,
        statusTitle = "Yuk, beri tip ke driver",
        statusSubtitle = "Tip 100% diterima driver"
    )

    NestTheme {
        TippingDriverWidget(modifier = Modifier.fillMaxWidth(), tipping = tipping, onClickTippingButton = {})
    }
}

@Preview
@Composable
private fun TippingDriverWidgetWaitingPaymentPreview() {
    val tipping = TippingModel(
        status = TippingConstant.WAITING_PAYMENT,
        statusTitle = "Lakukan pembayaran",
        statusSubtitle = "Tip menunggu pembayaranmu"
    )

    NestTheme {
        TippingDriverWidget(modifier = Modifier.fillMaxWidth(), tipping = tipping, onClickTippingButton = {})
    }
}

@Preview
@Composable
private fun TippingDriverWidgetSuccessPaymentPreview() {
    val tipping = TippingModel(
        status = TippingConstant.SUCCESS_PAYMENT,
        statusTitle = "Tip akan diberikan!",
        statusSubtitle = "BNI Virtual Account Rp100.000"
    )

    NestTheme {
        TippingDriverWidget(modifier = Modifier.fillMaxWidth(), tipping = tipping, onClickTippingButton = {})
    }
}

@Preview
@Composable
private fun TippingDriverWidgetSuccessTippingPreview() {
    val tipping = TippingModel(
        status = TippingConstant.SUCCESS_TIPPING,
        statusTitle = "Tip kamu sudah diberikan!",
        statusSubtitle = "BNI Virtual Account Rp100.000"
    )

    NestTheme {
        TippingDriverWidget(modifier = Modifier.fillMaxWidth(), tipping = tipping, onClickTippingButton = {})
    }
}

@Preview
@Composable
private fun TippingDriverWidgetRefundPreview() {
    val tipping = TippingModel(
        status = TippingConstant.REFUND_TIP,
        statusTitle = "Tip dikembalikan",
        statusSubtitle = "Karena pesanan dibatalkan"
    )

    NestTheme {
        TippingDriverWidget(modifier = Modifier.fillMaxWidth(), tipping = tipping, onClickTippingButton = {})
    }
}
