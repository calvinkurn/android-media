package com.tokopedia.logisticseller.ui.confirmshipping.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.nest.components.NestBottomSheetScreen
import com.tokopedia.nest.components.rememberNestBottomSheetState
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.targetedticker.domain.TickerModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmShippingScreen(
    state: ConfirmShippingState,
    source: ConfirmShippingMode,
    pressBack: () -> Unit,
    onClickBarcodeIcon: () -> Unit,
    onSwitchChanged: (Boolean) -> Unit,
    onClickCourier: (courier: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment) -> Unit,
    onClickService: (service: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage) -> Unit,
    onChangeRefNum: (String) -> Unit,
    openWebview: (url: String) -> Unit,
    onSubmit: () -> Unit,
) {
    val sheetState = rememberNestBottomSheetState()
    val scope = rememberCoroutineScope()
    val (bottomSheetState, setBottomSheetState) = remember {
        mutableStateOf(ConfirmShippingBottomSheetState.NONE)
    }

    fun setBottomSheetContentState(newState: ConfirmShippingBottomSheetState) {
        scope.launch {
            if (newState != ConfirmShippingBottomSheetState.NONE) {
                setBottomSheetState(newState)
                sheetState.bottomSheetState.expand()
            } else {
                sheetState.bottomSheetState.collapse()
                setBottomSheetState(newState)
            }
        }
    }

    NestBottomSheetScreen(
        title = getBottomSheetTitle(bottomSheetState),
        state = sheetState,
        showCloseIcon = true,
        isHideable = true,
        bottomSheetContent = {
            ConfirmShippingBottomSheet(
                bsState = bottomSheetState,
                pageState = state,
                onBottomSheetClosed = { setBottomSheetContentState(ConfirmShippingBottomSheetState.NONE) },
                onChooseCourier = onClickCourier,
                onChooseService = onClickService
            )
        }
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
                onChangeRefNum = onChangeRefNum,
                openWebview = openWebview,
                onSubmit = onSubmit,
                onOpenBottomSheet = ::setBottomSheetContentState
            )
        }
    }
}

@Composable
private fun ConfirmShippingMode.toHeaderTitle(): String {
    return when (this) {
        ConfirmShippingMode.CHANGE_COURIER -> stringResource(id = R.string.title_som_change_courier)
        ConfirmShippingMode.CONFIRM_SHIPPING -> stringResource(id = R.string.title_som_confirm_shipping)
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

        val tickerData = listOf(
            TickerModel.TickerItem(
                type = 3,
                title = "Pengiriman mengalami kendala",
                content = "Pengiriman terlambat karena cuaca buruk",
                linkUrl = "https://tokopedia/help"
            ),
            TickerModel.TickerItem(
                type = 2,
                title = "Pengiriman menggunakan Kurir Rekomendasi",
                content = "Informasi mengenai kurir rekomendasi",
                linkUrl = "https://tokopedia/help"
            )
        )
        val tickerModel = TickerModel(
            item = tickerData
        )


        val state = ConfirmShippingState(
            referenceNumber = "TKP-82845",
            mode = ConfirmShippingMode.CHANGE_COURIER,
            loading = false,
            courierList = courierList,
            chosenCourier = courierList.first(),
            chosenService = courierList.first().listShipmentPackage.first(),
            tickerData = tickerModel
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
            openWebview = {},
            onSubmit = {})
    }
}
