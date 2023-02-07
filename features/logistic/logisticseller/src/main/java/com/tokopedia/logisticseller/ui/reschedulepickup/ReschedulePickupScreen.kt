package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.extensions.tag
import com.tokopedia.common_compose.principles.AppBar
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestTextField
import com.tokopedia.common_compose.principles.NestTicker
import com.tokopedia.common_compose.principles.NestTips
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.NestBottomSheetShape
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleBottomSheetLayout
import com.tokopedia.logisticseller.ui.reschedulepickup.dialog.RescheduleResultDialog
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInput
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReschedulePickupScreen(
    state: State<ReschedulePickupState>,
    input: ReschedulePickupInput,
    onDayChosen: (RescheduleDayOptionModel) -> Unit,
    onTimeChosen: (RescheduleTimeOptionModel) -> Unit,
    onReasonChosen: (RescheduleReasonOptionModel) -> Unit,
    onSubtitleClicked: (String) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    onSaveReschedule: () -> Unit,
    onBottomSheetClosed: () -> Unit,
    onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit,
    onClickDialogButton: (Boolean) -> Unit,
    onCloseDialog: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    // to set the current sheet to null when the bottom sheet closes
    if (!sheetState.isVisible) {
        onBottomSheetClosed()
    }

    val closeSheet: () -> Unit = {
        scope.launch {
            sheetState.hide()
            onBottomSheetClosed()
        }
    }

    val openSheet: (RescheduleBottomSheetState) -> Unit = {
        scope.launch {
            if (input.day.isEmpty() && it == RescheduleBottomSheetState.TIME) return@launch
            onOpenBottomSheet(it)
            sheetState.show()
        }
    }

    Scaffold(topBar = { AppBar(title = stringResource(id = R.string.title_reschedule_pickup_activity)) }) {
        ModalBottomSheetLayout(
            sheetShape = NestBottomSheetShape(),
            sheetState = sheetState,
            sheetContent = {
                RescheduleBottomSheetLayout(
                    state.value.bottomSheet,
                    closeSheet,
                    state.value.options,
                    onDayChosen,
                    onTimeChosen,
                    onReasonChosen
                )
            }
        ) {
            ReschedulePickupScreenLayout(
                state = state,
                input = input,
                onSubtitleClicked = onSubtitleClicked,
                onOtherReasonChanged = onOtherReasonChanged,
                onSaveReschedule = onSaveReschedule,
                onOpenBottomSheet = openSheet,
                onClickDialogButton = onClickDialogButton,
                onCloseDialog = onCloseDialog
            )
        }
    }
}

@Composable
fun ReschedulePickupScreenLayout(
    state: State<ReschedulePickupState>,
    input: ReschedulePickupInput,
    onSubtitleClicked: (String) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    onSaveReschedule: () -> Unit,
    onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit,
    onClickDialogButton: (Boolean) -> Unit,
    onCloseDialog: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .tag("reschedule_pickup_layout")
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Title()
        OrderInfo(
            courier = state.value.info.courier,
            invoice = state.value.info.invoice
        )
        Divider(thickness = 8.dp)
        InputSectionTitle()
        InputSectionSubtitle(
            onSubtitleClicked = onSubtitleClicked,
            applink = state.value.info.applink
        )
        ReschedulePickupGuide(guide = state.value.info.guide)
        InputDay(
            day = input.day,
            onOpenBottomSheet = { onOpenBottomSheet(RescheduleBottomSheetState.DAY) }

        )
        InputTime(
            time = input.time,
            onOpenBottomSheet = {
                onOpenBottomSheet(
                    RescheduleBottomSheetState.TIME
                )
            }
        )
        if (state.value.info.summary.isNotEmpty()) {
            ReschedulePickupSummary(
                summary = state.value.info.summary
            )
        }
        InputReason(
            reason = state.value.reason,
            onOpenBottomSheet = { onOpenBottomSheet(RescheduleBottomSheetState.REASON) }
        )
        if (state.value.isCustomReason) {
            InputCustomReason(
                customReason = input.reason,
                onOtherReasonChanged = { onOtherReasonChanged(it) },
                error = state.value.customReasonError
            )
        }
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(id = R.string.title_button_reschedule_pickup),
            enabled = state.value.valid
        ) {
            onSaveReschedule()
        }
        RescheduleResultDialog(
            saveRescheduleModel = state.value.saveRescheduleModel,
            onClickDialogButton = onClickDialogButton,
            onCloseDialog = onCloseDialog
        )
    }
}

@Composable
private fun Title() {
    NestTypography(
        text = stringResource(id = R.string.label_title_order_detail_reschedule_pick_up),
        textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun OrderInfo(courier: String, invoice: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OrderInfoItem(
                title = stringResource(id = R.string.label_title_courier_reschedule_pick_up),
                value = courier,
                icon = Icons.Outlined.LocalShipping
            )
            OrderInfoItem(
                title = stringResource(id = R.string.label_title_invoice_reschedule_pick_up),
                value = invoice,
                icon = Icons.Outlined.Receipt
            )
        }
    }
}

@Composable
private fun OrderInfoItem(title: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "icon order info $title"
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            NestTypography(
                text = title,
                textStyle = NestTheme.typography.body3
            )
            NestTypography(
                text = value,
                textStyle = NestTheme.typography.heading6
            )
        }
    }
}

@Composable
private fun InputSectionTitle() {
    NestTypography(
        text = stringResource(id = R.string.label_title_reschedule_pick_up),
        textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun InputSectionSubtitle(
    onSubtitleClicked: (String) -> Unit,
    applink: String
) {
    NestTypography(
        text = Subtitle(),
        textStyle = NestTheme.typography.body3,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable { onSubtitleClicked(applink) }
    )
}

@Composable
private fun ReschedulePickupGuide(guide: String) {
    NestTips(
        title = stringResource(id = R.string.title_tips_reschedule_pick_up),
        description = guide,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun InputDay(onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit, day: String) {
    NestTextField(
        value = day,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onOpenBottomSheet(RescheduleBottomSheetState.DAY) },
        label = {
            NestTypography(
                text =
                stringResource(id = R.string.label_day_reschedule_pick_up)
            )
        },
        enabled = false,
//        placeholder = {
//            NestTypography(text = stringResource(id = R.string.placeholder_day_reschedule_pick_up))
//        },
        trailingIcon = { DropDownIcon() }
    )
}

@Composable
private fun InputTime(onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit, time: String) {
    NestTextField(
        value = time,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onOpenBottomSheet(
                    RescheduleBottomSheetState.TIME
                )
            },
        enabled = false,
        label = {
            NestTypography(text = stringResource(id = R.string.label_time_reschedule_pick_up))
        },
        placeholder = {
            NestTypography(text = stringResource(id = R.string.placeholder_time_reschedule_pick_up))
        },
        trailingIcon = { DropDownIcon() }
    )
}

@Composable
private fun InputReason(onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit, reason: String) {
    NestTextField(
        value = reason,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onOpenBottomSheet(RescheduleBottomSheetState.REASON) },
        label = {
            NestTypography(text = stringResource(id = R.string.label_reason_reschedule_pickup))
        },
        enabled = false,
        trailingIcon = { DropDownIcon() }
    )
}

@Composable
private fun InputCustomReason(
    customReason: String,
    onOtherReasonChanged: (String) -> Unit,
    error: String?
) {
    NestTextField(
        value = customReason,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        label = {
            NestTypography(text = stringResource(id = R.string.label_detail_reason_reschedule_pickup))
        },
        onValueChanged = { onOtherReasonChanged(it) },
        isError = !error.isNullOrEmpty(),
        supportingText = { error?.let { NestTypography(text = it) } }
    )
}

@Composable
private fun ReschedulePickupSummary(summary: String) {
    NestTicker(
        text = summary,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun Subtitle(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = NestTheme.colors.NN._600)) {
            append(stringResource(id = R.string.label_subtitle_reschedule_pick_up_annotate))
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = NestTheme.colors.GN._500
            )
        ) {
            append(stringResource(id = R.string.label_app_link_subtitle_reschedule_pick_up_annotate))
        }
    }
}

@Composable
fun DropDownIcon() {
    IconButton(
        onClick = {}
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = "drop down"
        )
    }
}
