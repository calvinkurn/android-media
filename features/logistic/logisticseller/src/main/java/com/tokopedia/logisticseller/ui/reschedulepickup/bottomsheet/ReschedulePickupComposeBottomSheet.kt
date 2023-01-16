package com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupOptions

@Composable
fun RescheduleBottomSheetLayout(
    currentScreen: RescheduleBottomSheetState,
    onCloseBottomSheet: () -> Unit,
    options: ReschedulePickupOptions,
    onDayChosen: (RescheduleDayOptionModel) -> Unit,
    onTimeChosen: (RescheduleTimeOptionModel) -> Unit,
    onReasonChosen: (RescheduleReasonOptionModel) -> Unit
) {
    BottomSheetCompose(getBottomSheetTitle(currentScreen), onCloseBottomSheet) {
        when (currentScreen) {
            RescheduleBottomSheetState.DAY -> RescheduleBottomSheetContent(
                items = options.dayOptions,
                onItemClicked = { onDayChosen(it) },
                onBottomSheetClosed = onCloseBottomSheet
            )
            RescheduleBottomSheetState.TIME -> RescheduleBottomSheetContent(
                items = options.timeOptions,
                onItemClicked = { onTimeChosen(it) },
                onBottomSheetClosed = onCloseBottomSheet
            )
            RescheduleBottomSheetState.REASON -> RescheduleBottomSheetContent(
                items = options.reasonOptions,
                onItemClicked = { onReasonChosen(it) },
                onBottomSheetClosed = onCloseBottomSheet
            )
            RescheduleBottomSheetState.NONE -> onCloseBottomSheet()
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
                    Text(text = "$item")
                }
            )
        }
    }
}
