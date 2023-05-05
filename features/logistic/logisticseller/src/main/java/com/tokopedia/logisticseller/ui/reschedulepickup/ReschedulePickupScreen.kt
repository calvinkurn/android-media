package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.components.NestBottomSheetShape
import com.tokopedia.common_compose.components.NestButton
import com.tokopedia.common_compose.components.NestTextField
import com.tokopedia.common_compose.components.NestTips
import com.tokopedia.common_compose.components.ticker.NestTicker
import com.tokopedia.common_compose.extensions.tag
import com.tokopedia.common_compose.principles.NestHeader
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.common_compose.utils.toAnnotatedString
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleBottomSheetLayout
import com.tokopedia.logisticseller.ui.reschedulepickup.dialog.RescheduleResultDialog
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInput
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReschedulePickupScreen(
    state: ReschedulePickupState,
    input: ReschedulePickupInput,
    onEvent: (ReschedulePickupUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val (rescheduleBottomSheetState, setRescheduleBottomSheetState) = remember {
        mutableStateOf(RescheduleBottomSheetState.NONE)
    }

    fun openBottomSheet(bottomSheetState: RescheduleBottomSheetState) {
        scope.launch {
            if (bottomSheetState != RescheduleBottomSheetState.NONE) {
                if (bottomSheetState != RescheduleBottomSheetState.TIME || input.day.isNotEmpty()) {
                    setRescheduleBottomSheetState(bottomSheetState)
                    sheetState.show()
                }
            } else {
                sheetState.hide()
            }
        }
    }

    fun dispatchEvent(event: ReschedulePickupUiEvent) {
        when (event) {
            is ReschedulePickupUiEvent.OpenBottomSheet -> {
                openBottomSheet(event.bottomSheetState)
            }
            is ReschedulePickupUiEvent.CloseBottomSheet -> {
                openBottomSheet(RescheduleBottomSheetState.NONE)
            }
            else -> {
                onEvent(event)
            }
        }
    }

    Scaffold(topBar = {
        NestHeader(
            title = stringResource(id = R.string.title_reschedule_pickup_activity),
            showBackIcon = true,
            onBackIconPressed = { onEvent(ReschedulePickupUiEvent.PressBack) }
        )
    }) {
        ModalBottomSheetLayout(
            sheetShape = NestBottomSheetShape(),
            sheetState = sheetState,
            sheetContent = {
                RescheduleBottomSheetLayout(
                    rescheduleBottomSheetState,
                    state.options
                ) { dispatchEvent(it) }
            }
        ) {
            ReschedulePickupScreenLayout(
                state = state,
                input = input,
                onEvent = { dispatchEvent(it) }
            )
        }
    }
}

@Composable
fun ReschedulePickupScreenLayout(
    state: ReschedulePickupState,
    input: ReschedulePickupInput,
    onEvent: (ReschedulePickupUiEvent) -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .tag("reschedule_pickup_layout")
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Title()
            OrderInfo(
                courier = state.info.courier,
                invoice = state.info.invoice
            )
            Divider(thickness = 8.dp, color = NestTheme.colors.NN._50)
            InputSectionTitle()
            InputSectionSubtitle(
                onSubtitleClicked = { onEvent(ReschedulePickupUiEvent.ClickSubtitle(it)) },
                applink = state.info.applink
            )
            ReschedulePickupGuide(guide = state.info.guide)
            InputDay(
                day = input.day,
                onOpenBottomSheet = {
                    onEvent(
                        ReschedulePickupUiEvent.OpenBottomSheet(
                            it
                        )
                    )
                }

            )
            InputTime(
                time = input.time,
                onOpenBottomSheet = {
                    onEvent(
                        ReschedulePickupUiEvent.OpenBottomSheet(
                            it
                        )
                    )
                }
            )
            if (state.info.summary.isNotEmpty()) {
                ReschedulePickupSummary(
                    summary = state.info.summary
                )
            }
            InputReason(
                reason = state.reason,
                onOpenBottomSheet = {
                    onEvent(
                        ReschedulePickupUiEvent.OpenBottomSheet(
                            it
                        )
                    )
                }
            )
            if (state.isCustomReason) {
                InputCustomReason(
                    customReason = input.reason,
                    onOtherReasonChanged = { onEvent(ReschedulePickupUiEvent.CustomReason(it)) },
                    error = state.customReasonError
                )
            }
            RescheduleResultDialog(
                saveRescheduleModel = state.saveRescheduleModel,
                onCloseDialog = { onEvent(ReschedulePickupUiEvent.CloseDialog(it)) }
            )
        }
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(id = R.string.title_button_reschedule_pickup),
            enabled = state.valid
        ) {
            onEvent(ReschedulePickupUiEvent.SaveReschedule)
        }
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
                icon = com.tokopedia.iconunify.R.drawable.iconunify_courier
            )
            OrderInfoItem(
                title = stringResource(id = R.string.label_title_invoice_reschedule_pick_up),
                value = invoice,
                icon = com.tokopedia.iconunify.R.drawable.iconunify_bill_all
            )
        }
    }
}

@Composable
private fun OrderInfoItem(title: String, value: String, icon: Int) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
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
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .clickable { onSubtitleClicked(applink) }
    )
}

@Composable
private fun ReschedulePickupGuide(guide: String) {
    NestTips(
        title = stringResource(id = R.string.title_tips_reschedule_pick_up),
        description = HtmlLinkHelper(
            LocalContext.current,
            guide
        ).spannedString?.toAnnotatedString() ?: "",
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
        placeholder = {
            NestTypography(text = stringResource(id = R.string.placeholder_day_reschedule_pick_up))
        },
        trailingIcon = { DropDownIcon(onClick = { onOpenBottomSheet(RescheduleBottomSheetState.DAY) }) }
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
        trailingIcon = {
            DropDownIcon(onClick = {
                onOpenBottomSheet(
                    RescheduleBottomSheetState.TIME
                )
            })
        }
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
        trailingIcon = { DropDownIcon(onClick = { onOpenBottomSheet(RescheduleBottomSheetState.REASON) }) }
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
        counter = 160,
        error = error
    )
}

@Composable
private fun ReschedulePickupSummary(summary: String) {
    NestTicker(
        text = HtmlLinkHelper(
            LocalContext.current,
            summary
        ).spannedString?.toAnnotatedString() ?: "",
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp),
        closeButtonVisibility = false
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
private fun DropDownIcon(onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = com.tokopedia.iconunify.R.drawable.iconunify_chevron_down),
            contentDescription = "drop down"
        )
    }
}
