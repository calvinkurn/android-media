package com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupOptions
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun RescheduleBottomSheetLayout(
    currentScreen: RescheduleBottomSheetState,
    options: ReschedulePickupOptions,
    onEvent: (ReschedulePickupUiEvent) -> Unit,
    onBottomSheetClosed: () -> Unit
) {
    when (currentScreen) {
        RescheduleBottomSheetState.DAY -> RescheduleBottomSheetContent(
            items = options.dayOptions,
            onItemClicked = { onEvent(ReschedulePickupUiEvent.SelectDay(it)) },
            onBottomSheetClosed = onBottomSheetClosed
        )

        RescheduleBottomSheetState.TIME -> RescheduleBottomSheetContent(
            items = options.timeOptions,
            onItemClicked = { onEvent(ReschedulePickupUiEvent.SelectTime(it)) },
            onBottomSheetClosed = onBottomSheetClosed
        )

        RescheduleBottomSheetState.REASON -> RescheduleBottomSheetContent(
            items = options.reasonOptions,
            onItemClicked = { onEvent(ReschedulePickupUiEvent.SelectReason(it)) },
            onBottomSheetClosed = onBottomSheetClosed
        )

        RescheduleBottomSheetState.NONE -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T> RescheduleBottomSheetContent(
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
