package com.tokopedia.logisticseller.ui.confirmshipping.ui

import androidx.compose.runtime.Composable
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.ListBottomSheetContent

enum class ConfirmShippingBottomSheetState {
    NONE, COURIER, SERVICE
}

fun getBottomSheetTitle(bsState: ConfirmShippingBottomSheetState): String {
    return when (bsState) {
        ConfirmShippingBottomSheetState.COURIER -> LogisticSellerConst.TITLE_KURIR_PENGIRIMAN

        ConfirmShippingBottomSheetState.SERVICE -> LogisticSellerConst.TITLE_JENIS_LAYANAN

        else -> ""
    }
}

@Composable
fun ConfirmShippingBottomSheet(
    bsState: ConfirmShippingBottomSheetState,
    pageState: ConfirmShippingState,
    onBottomSheetClosed: () -> Unit,
    onChooseCourier: (SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment) -> Unit,
    onChooseService: (SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage) -> Unit,
) {
    when (bsState) {
        ConfirmShippingBottomSheetState.COURIER -> {
            ListBottomSheetContent(
                items = pageState.courierList.orEmpty(),
                onBottomSheetClosed = onBottomSheetClosed,
                onItemClicked = onChooseCourier
            )
        }

        ConfirmShippingBottomSheetState.SERVICE -> {
            ListBottomSheetContent(
                items = pageState.chosenCourier?.listShipmentPackage.orEmpty(),
                onBottomSheetClosed = onBottomSheetClosed,
                onItemClicked = onChooseService
            )
        }

        else -> {}
    }
}
