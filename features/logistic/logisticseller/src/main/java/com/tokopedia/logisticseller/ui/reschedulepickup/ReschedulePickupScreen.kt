package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleBottomSheetLayout
import com.tokopedia.logisticseller.ui.reschedulepickup.dialog.RescheduleResultDialog
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInput
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.NestBottomSheetShape
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestTextField
import com.tokopedia.nest.components.NestTips
import com.tokopedia.nest.components.ticker.NestTicker
import com.tokopedia.nest.components.ticker.NestTickerData
import com.tokopedia.nest.components.ticker.TickerType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.clickableWithoutRipple
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.nest.principles.utils.toAnnotatedString
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

    fun setBottomSheetContentState(bottomSheetState: RescheduleBottomSheetState) {
        scope.launch {
            if (bottomSheetState != RescheduleBottomSheetState.NONE) {
                if (bottomSheetState != RescheduleBottomSheetState.TIME || input.day.isNotEmpty()) {
                    setRescheduleBottomSheetState(bottomSheetState)
                    sheetState.show()
                }
            } else {
                sheetState.hide()
                setRescheduleBottomSheetState(bottomSheetState)
            }
        }
    }

    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = stringResource(id = R.string.title_reschedule_pickup_activity),
                onBackClicked = {
                    onEvent(ReschedulePickupUiEvent.PressBack)
                }
            )
        )
    }) { paddingValues ->
        ModalBottomSheetLayout(
            modifier = Modifier.padding(paddingValues),
            sheetShape = NestBottomSheetShape(),
            sheetState = sheetState,
            sheetContent = {
                RescheduleBottomSheetLayout(
                    rescheduleBottomSheetState,
                    state.options,
                    onEvent
                ) { setBottomSheetContentState(RescheduleBottomSheetState.NONE) }
            }
        ) {
            ReschedulePickupScreenLayout(
                state = state,
                input = input,
                onEvent = onEvent,
                onBottomSheetEvent = { setBottomSheetContentState(it) }
            )
        }
    }
}

@Composable
private fun ReschedulePickupScreenLayout(
    state: ReschedulePickupState,
    input: ReschedulePickupInput,
    onEvent: (ReschedulePickupUiEvent) -> Unit,
    onBottomSheetEvent: (RescheduleBottomSheetState) -> Unit
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
                onOpenBottomSheet = onBottomSheetEvent
            )
            InputTime(
                time = input.time,
                onOpenBottomSheet = onBottomSheetEvent
            )
            ReschedulePickupSummary(
                summary = state.info.summary
            )
            InputReason(
                reason = state.reason,
                onOpenBottomSheet = onBottomSheetEvent
            )
            InputCustomReason(
                customReason = input.reason,
                onOtherReasonChanged = { onEvent(ReschedulePickupUiEvent.CustomReason(it)) },
                error = state.customReasonError,
                shouldRequestFocus = state.isCustomReason
            )
            RescheduleResultDialog(
                saveRescheduleModel = state.saveRescheduleModel,
                onCloseDialog = { onEvent(ReschedulePickupUiEvent.CloseDialog(it)) }
            )
        }
        NestButton(
            text = stringResource(id = R.string.title_button_reschedule_pickup),
            {
                onEvent(ReschedulePickupUiEvent.SaveReschedule)
            },
            modifier = Modifier
                .padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
            size = ButtonSize.LARGE,
            isEnabled = state.valid
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
    val interactionSource = remember { MutableInteractionSource() }
    NestTypography(
        text = HtmlLinkHelper(
            LocalContext.current,
            stringResource(id = R.string.label_subtitle_reschedule_pick_up)
        ).spannedString?.toAnnotatedString() ?: "",
        textStyle = NestTheme.typography.body3,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            .clickableWithoutRipple(
                interactionSource = interactionSource,
                onClick = { onSubtitleClicked(applink) }
            )
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
        enabled = false,
        label = stringResource(id = R.string.label_day_reschedule_pick_up),
        placeholder = stringResource(id = R.string.placeholder_day_reschedule_pick_up),
        icon1 = { DropDownIcon(onClick = { onOpenBottomSheet(RescheduleBottomSheetState.DAY) }) }
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
        label = stringResource(id = R.string.label_time_reschedule_pick_up),
        placeholder = stringResource(id = R.string.placeholder_time_reschedule_pick_up),
        icon1 = {
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
        enabled = false,
        label = stringResource(id = R.string.label_reason_reschedule_pickup),
        icon1 = { DropDownIcon(onClick = { onOpenBottomSheet(RescheduleBottomSheetState.REASON) }) }
    )
}

@Composable
private fun InputCustomReason(
    customReason: String,
    onOtherReasonChanged: (String) -> Unit,
    error: String?,
    shouldRequestFocus: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = shouldRequestFocus, block = {
        if (shouldRequestFocus) {
            focusRequester.requestFocus()
        }
    })
    AnimatedVisibility(
        visible = shouldRequestFocus,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        NestTextField(
            value = customReason,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            label = stringResource(id = R.string.label_detail_reason_reschedule_pickup),

            error = error,

            onValueChanged = { onOtherReasonChanged(it) },
            counter = 160
        )
    }
}

@Composable
private fun ReschedulePickupSummary(summary: String) {
    AnimatedVisibility(
        visible = summary.isNotEmpty(),
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        NestTicker(
            ticker = listOf(
                NestTickerData(
                    tickerTitle = "",
                    tickerType = TickerType.ANNOUNCEMENT,
                    tickerDescription = HtmlLinkHelper(
                        LocalContext.current,
                        summary
                    ).spannedString?.toAnnotatedString() ?: ""
                )
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp),
            closeButtonVisibility = false
        )
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
