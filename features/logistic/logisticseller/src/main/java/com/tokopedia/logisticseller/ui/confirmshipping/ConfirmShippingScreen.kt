package com.tokopedia.logisticseller.ui.confirmshipping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestTextField
import com.tokopedia.nest.components.NestTextFieldProperty
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.targetedticker.ui.compose.TargetedTickerWidgetCompose
import com.tokopedia.unifycomponents.compose.NestSwitch
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifyprinciples.stringToUnifyColor
import kotlin.math.log
import com.tokopedia.logisticseller.R as logisticsellerR

@Composable
fun ConfirmShippingScreen(
    reference: String,
    enableChangeCourier: Boolean,
    courierSelection: String,
    serviceSelection: String,
    tickerData: TickerModel?,
    pressBack: () -> Unit,
    onClickBarcodeIcon: () -> Unit,
    onSwitchChanged: (Boolean) -> Unit,
    onClickCourier: () -> Unit,
    onClickService: () -> Unit
) {
    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = stringResource(id = logisticsellerR.string.title_som_confirm_shipping),
                onBackClicked = pressBack
            )
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                TargetedTickerWidgetCompose(tickerData = tickerData, openWebview = {})
                InputReference(
                    modifier = Modifier,
                    reference = reference,
                    onClickBarcodeIcon = onClickBarcodeIcon
                )
                ChangeCourierSection(
                    modifier = Modifier,
                    enable = enableChangeCourier,
                    onSwitchChanged = onSwitchChanged,
                    courierSelection = courierSelection,
                    serviceSelection = serviceSelection,
                    onClickCourier = onClickCourier,
                    onClickService = onClickService
                )
            }
            NestButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = logisticsellerR.string.btn_konfirmasi),
                onClick = { /*TODO*/ })
        }
    }
}

@Composable
private fun InputReference(modifier: Modifier, reference: String, onClickBarcodeIcon: () -> Unit) {
    NestTextField(modifier = Modifier
        .fillMaxWidth()
        .then(modifier),
        value = reference,
        helper = stringResource(id = logisticsellerR.string.tf_no_resi_message),
        label = stringResource(id = logisticsellerR.string.nomor_resi),
        placeholder = stringResource(id = logisticsellerR.string.tf_no_resi_placeholder),
        icon1 = {
            NestImage(
                modifier = Modifier.clickable { onClickBarcodeIcon() },
                source = ImageSource.Painter(source = logisticsellerR.drawable.ic_scanbarcode)
            )
        }
    )
}

@Composable
private fun ChangeCourierSection(
    modifier: Modifier,
    enable: Boolean,
    onSwitchChanged: (Boolean) -> Unit,
    courierSelection: String,
    serviceSelection: String,
    onClickCourier: () -> Unit,
    onClickService: () -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (switch, title, courier, service) = createRefs()
        NestTypography(modifier = Modifier.constrainAs(title) {
            start.linkTo(parent.start)
            top.linkTo(switch.top)
            bottom.linkTo(switch.bottom)
        }, text = stringResource(id = logisticsellerR.string.change_courier_label))
        NestSwitch(
            isChecked = enable,
            onCheckedChanged = onSwitchChanged,
            modifier = Modifier.constrainAs(switch) {
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 16.dp)
            })
        if (enable) {
            ChangeCourierOptionItem(
                modifier = Modifier.constrainAs(courier) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
                title = stringResource(id = logisticsellerR.string.title_courier),
                value = courierSelection,
                onClick = onClickCourier
            )
            ChangeCourierOptionItem(
                modifier = Modifier.constrainAs(service) {
                    top.linkTo(courier.bottom)
                    start.linkTo(parent.start)
                },
                title = stringResource(id = logisticsellerR.string.courier_service_label),
                value = serviceSelection,
                onClick = onClickService
            )
        }
    }
}

@Composable
private fun ChangeCourierOptionItem(
    modifier: Modifier,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    ConstraintLayout(modifier.fillMaxWidth()) {
        val (tvTitle, tvValue, button, divider) = createRefs()
        NestTypography(text = title, modifier = Modifier.constrainAs(tvTitle) {
            top.linkTo(button.top)
            bottom.linkTo(button.bottom)
            start.linkTo(parent.start)
        })
        NestTypography(text = value, modifier = Modifier.constrainAs(tvValue) {
            top.linkTo(button.top)
            bottom.linkTo(button.bottom)
            end.linkTo(button.start)
        })
        NestIcon(iconId = IconUnify.CHEVRON_RIGHT, modifier = Modifier
            .constrainAs(button) {
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 4.dp)
            }
            .clickable { onClick() })
        NestDivider(size = NestDividerSize.Small, modifier = Modifier
            .constrainAs(divider) {
                top.linkTo(button.bottom, margin = 4.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth())
    }
}

@Preview
@Composable
fun ConfirmShippingScreenPreview() {
    NestTheme {
        ConfirmShippingScreen(
            pressBack = { /*TODO*/ },
            onClickBarcodeIcon = { /*TODO*/ },
            reference = "TKP-82845",
            tickerData = null,
            onSwitchChanged = {},
            enableChangeCourier = true,
            courierSelection = "JNE",
            serviceSelection = "Reguler",
            onClickCourier = { /*TODO*/ }) {

        }
    }
}
