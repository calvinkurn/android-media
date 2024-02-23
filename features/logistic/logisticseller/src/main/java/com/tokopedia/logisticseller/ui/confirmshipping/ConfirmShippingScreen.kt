package com.tokopedia.logisticseller.ui.confirmshipping

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.constraintlayout.compose.ConstraintLayout
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestDivider
import com.tokopedia.nest.components.NestDividerSize
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestTextField
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.targetedticker.ui.compose.TargetedTickerWidgetCompose
import com.tokopedia.unifycomponents.compose.NestSwitch
import com.tokopedia.logisticseller.R as logisticsellerR

@Composable
fun ConfirmShippingScreen(
    state: ConfirmShippingState,
    source: ConfirmShippingMode,
    pressBack: () -> Unit,
    onClickBarcodeIcon: () -> Unit,
    onSwitchChanged: (Boolean) -> Unit,
    onClickCourier: (state: ConfirmShippingState) -> Unit,
    onClickService: (state: ConfirmShippingState) -> Unit,
    onChangeRefNum: (String) -> Unit,
    openWebview: (url: String) -> Unit,
    onSubmit: () -> Unit,
) {
    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = source.toHeaderTitle(),
                onBackClicked = pressBack
            )
        )
    }) {
        ConfirmShippingContent(
            paddingValues = it,
            state = state,
            onClickBarcodeIcon = onClickBarcodeIcon,
            onSwitchChanged = onSwitchChanged,
            onClickCourier = onClickCourier,
            onClickService = onClickService,
            onChangeRefNum = onChangeRefNum,
            openWebview = openWebview,
            onSubmit = onSubmit
        )
    }
}

@Composable
private fun ConfirmShippingContent(
    paddingValues: PaddingValues,
    state: ConfirmShippingState,
    onClickBarcodeIcon: () -> Unit,
    onSwitchChanged: (Boolean) -> Unit,
    onClickCourier: (state: ConfirmShippingState) -> Unit,
    onClickService: (state: ConfirmShippingState) -> Unit,
    onChangeRefNum: (String) -> Unit,
    openWebview: (url: String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TargetedTickerWidgetCompose(
                tickerData = state.tickerData,
                openWebview = openWebview
            )
            InputReference(
                modifier = Modifier,
                state = state,
                onClickBarcodeIcon = onClickBarcodeIcon,
                onChangeRefNum = onChangeRefNum,
            )
            ChangeCourierSection(
                modifier = Modifier,
                state = state,
                onSwitchChanged = onSwitchChanged,
                onClickCourier = onClickCourier,
                onClickService = onClickService
            )
        }
        NestButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = logisticsellerR.string.btn_konfirmasi),
            onClick = onSubmit
        )
    }
}

@Composable
private fun ConfirmShippingMode.toHeaderTitle(): String {
    return when (this) {
        ConfirmShippingMode.CHANGE_COURIER -> stringResource(id = logisticsellerR.string.title_som_change_courier)
        ConfirmShippingMode.CONFIRM_SHIPPING -> stringResource(id = logisticsellerR.string.title_som_confirm_shipping)
    }
}

@Composable
private fun InputReference(
    modifier: Modifier,
    state: ConfirmShippingState,
    onClickBarcodeIcon: () -> Unit,
    onChangeRefNum: (String) -> Unit
) {
    NestTextField(modifier = Modifier
        .fillMaxWidth()
        .then(modifier),
        value = state.referenceNumber,
        onValueChanged = onChangeRefNum,
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
    state: ConfirmShippingState,
    onSwitchChanged: (Boolean) -> Unit,
    onClickCourier: (state: ConfirmShippingState) -> Unit,
    onClickService: (state: ConfirmShippingState) -> Unit
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (switch, title, courier, service) = createRefs()
        NestTypography(modifier = Modifier.constrainAs(title) {
            start.linkTo(parent.start)
            top.linkTo(switch.top)
            bottom.linkTo(switch.bottom)
        }, text = stringResource(id = logisticsellerR.string.change_courier_label))
        NestSwitch(
            isChecked = state.mode == ConfirmShippingMode.CHANGE_COURIER,
            onCheckedChanged = onSwitchChanged,
            modifier = Modifier.constrainAs(switch) {
                end.linkTo(parent.end)
                top.linkTo(parent.top, margin = 16.dp)
            })
        if (state.mode == ConfirmShippingMode.CHANGE_COURIER && !state.courierList.isNullOrEmpty()) {
            ChangeCourierOptionItem(
                modifier = Modifier.constrainAs(courier) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
                title = stringResource(id = logisticsellerR.string.title_courier),
                value = state.chosenCourier?.shipmentName.orEmpty(),
                onClick = { onClickCourier(state) }
            )
            ChangeCourierOptionItem(
                modifier = Modifier.constrainAs(service) {
                    top.linkTo(courier.bottom)
                    start.linkTo(parent.start)
                },
                title = stringResource(id = logisticsellerR.string.courier_service_label),
                value = state.chosenService?.name.orEmpty(),
                onClick = { onClickService(state) }
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
        val courierList = listOf(
            SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(
                shipmentId = "1",
                shipmentName = "JNE",
                listShipmentPackage = listOf(
                    SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage(
                        name = "Reguler",
                        spId = "30"
                    ),
                    SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage(
                        name = "Kargo",
                        spId = "42"
                    )
                )
            ),
            SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment(
                shipmentId = "2",
                shipmentName = "SiCepat",
                listShipmentPackage = listOf(
                    SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage(
                        name = "Reguler",
                        spId = "44"
                    ),
                    SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage(
                        name = "Kargo",
                        spId = "99"
                    )
                )
            )
        )
        val state = ConfirmShippingState(
            referenceNumber = "TKP-82845",
            mode = ConfirmShippingMode.CHANGE_COURIER,
            loading = false,
            courierList = courierList,
            chosenCourier = courierList.first(),
            chosenService = courierList.first().listShipmentPackage.first(),
            tickerData = null
        )
        ConfirmShippingScreen(
            pressBack = { /*TODO*/ },
            onClickBarcodeIcon = { /*TODO*/ },
            state = state,
            source = ConfirmShippingMode.CHANGE_COURIER,
            onSwitchChanged = {},
            onClickCourier = { /*TODO*/ },
            onChangeRefNum = {},
            onClickService = {},
            openWebview = {}, onSubmit = {})
    }
}
