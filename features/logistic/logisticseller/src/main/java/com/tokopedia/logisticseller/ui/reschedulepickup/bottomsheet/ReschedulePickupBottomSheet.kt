package com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tokopedia.common_compose.components.NestBottomSheet
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupOptions
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent

@Composable
fun RescheduleBottomSheetLayout(
    currentScreen: RescheduleBottomSheetState,
    options: ReschedulePickupOptions,
    onEvent: (ReschedulePickupUiEvent) -> Unit
) {
    NestBottomSheet(
        getBottomSheetTitle(currentScreen),
        onClosePressed = { onEvent(ReschedulePickupUiEvent.CloseBottomSheet) }
    ) {
        when (currentScreen) {
            RescheduleBottomSheetState.DAY -> RescheduleBottomSheetContent(
                items = options.dayOptions,
                onItemClicked = { onEvent(ReschedulePickupUiEvent.SelectDay(it)) },
                onBottomSheetClosed = { onEvent(ReschedulePickupUiEvent.CloseBottomSheet) }
            )
            RescheduleBottomSheetState.TIME -> RescheduleBottomSheetContent(
                items = options.timeOptions,
                onItemClicked = { onEvent(ReschedulePickupUiEvent.SelectTime(it)) },
                onBottomSheetClosed = { onEvent(ReschedulePickupUiEvent.CloseBottomSheet) }
            )
            RescheduleBottomSheetState.REASON -> RescheduleBottomSheetContent(
                items = options.reasonOptions,
                onItemClicked = { onEvent(ReschedulePickupUiEvent.SelectReason(it)) },
                onBottomSheetClosed = { onEvent(ReschedulePickupUiEvent.CloseBottomSheet) }
            )
            RescheduleBottomSheetState.NONE -> onEvent(ReschedulePickupUiEvent.CloseBottomSheet)
        }
    }
}

@Composable
fun getBottomSheetTitle(currentScreen: RescheduleBottomSheetState): String {
    return when (currentScreen) {
        RescheduleBottomSheetState.DAY -> stringResource(id = R.string.title_reschedule_day_bottomsheet)
        RescheduleBottomSheetState.TIME -> stringResource(id = R.string.title_reschedule_time_bottomsheet)
        RescheduleBottomSheetState.REASON -> stringResource(id = R.string.title_reschedule_reason_bottomsheet)
        RescheduleBottomSheetState.NONE -> ""
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> RescheduleBottomSheetContent(
    items: List<T>,
    onItemClicked: (T) -> Unit,
    onBottomSheetClosed: () -> Unit
) {
    LazyColumn {
        items(count = items.size) {
            val item = items[it]
            ListItem(
                modifier = Modifier.clickable {
                    onItemClicked(item)
                    onBottomSheetClosed()
                },
                text = {
                    NestTypography(
                        "$item",
                        textStyle = NestTheme.typography.paragraph3.copy(
                            color = NestTheme.colors.NN._950
                        )
                    )
                }
            )
        }
    }
}
