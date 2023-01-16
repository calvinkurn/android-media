package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.Lightbulb
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
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestTicker
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.BottomSheetRoundedCorner
import com.tokopedia.logisticseller.ui.reschedulepickup.bottomsheet.RescheduleBottomSheetLayout
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
    onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit
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

    ModalBottomSheetLayout(
        sheetShape = BottomSheetRoundedCorner(),
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
            onOpenBottomSheet = openSheet
        )
    }
}

@Composable
fun ReschedulePickupScreenLayout(
    state: State<ReschedulePickupState>,
    input: ReschedulePickupInput,
    onSubtitleClicked: (String) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    onSaveReschedule: () -> Unit,
    onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ReschedulePickupTitle()
        ReschedulePickupOrderInfo(
            courier = state.value.info.courier,
            invoice = state.value.info.invoice
        )
        Divider(thickness = 8.dp)
        ReschedulePickupInputSectionTitle()
        ReschedulePickupInputSectionSubtitle(
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
                isError = state.value.customReasonError != null
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
    }
}

@Composable
private fun ReschedulePickupTitle() {
    NestTypography(
        text = stringResource(id = R.string.label_title_order_detail_reschedule_pick_up),
        textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun ReschedulePickupOrderInfo(courier: String, invoice: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OrderInfoItem(
                title = stringResource(id = R.string.label_title_courier_reschedule_pick_up),
                value = courier,
                icon = Icons.Filled.LocalShipping
            )
            OrderInfoItem(
                title = stringResource(id = R.string.label_title_invoice_reschedule_pick_up),
                value = invoice,
                icon = Icons.Filled.Receipt
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
private fun ReschedulePickupInputSectionTitle() {
    NestTypography(
        text = stringResource(id = R.string.label_title_reschedule_pick_up),
        textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun ReschedulePickupInputSectionSubtitle(
    onSubtitleClicked: (String) -> Unit,
    applink: String
) {
    NestTypography(
        text = constructRescheduleSubtitle(),
        textStyle = NestTheme.typography.body3,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable { onSubtitleClicked(applink) }
    )
}

@Composable
private fun ReschedulePickupGuide(guide: String) {
    TipsUnifyCompose(
        title = stringResource(id = R.string.title_tips_reschedule_pick_up),
        description = guide,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun InputDay(onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit, day: String) {
    TextFieldUnifyCompose(
        value = day,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
        trailingIcon = { TrailingIconTextField() }
    )
}

@Composable
private fun InputTime(onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit, time: String) {
    TextFieldUnifyCompose(
        value = time,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
        trailingIcon = { TrailingIconTextField() }
    )
}

@Composable
private fun InputReason(onOpenBottomSheet: (RescheduleBottomSheetState) -> Unit, reason: String) {
    TextFieldUnifyCompose(
        value = reason,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onOpenBottomSheet(RescheduleBottomSheetState.REASON) },
        label = {
            NestTypography(text = stringResource(id = R.string.label_reason_reschedule_pickup))
        },
        enabled = false,
        trailingIcon = { TrailingIconTextField() }
    )
}

@Composable
private fun InputCustomReason(
    customReason: String,
    onOtherReasonChanged: (String) -> Unit,
    isError: Boolean
) {
    TextFieldUnifyCompose(
        value = customReason,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = {
            NestTypography(text = stringResource(id = R.string.label_detail_reason_reschedule_pickup))
        },
        onValueChanged = { onOtherReasonChanged(it) },
        isError = isError
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
private fun constructRescheduleSubtitle(): AnnotatedString {
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
fun TrailingIconTextField() {
    IconButton(
        onClick = {}
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "drop down"
        )
    }
}

@Composable
fun TextFieldUnifyCompose(
    value: String,
    onValueChanged: (String) -> Unit = {},
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false
//    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._950),
        modifier = modifier,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        label = label,
        enabled = enabled,
        isError = isError
//        supportingText = supportingText
    )
}

@Composable
fun TipsUnifyCompose(
    title: String? = null,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = NestTheme.colors.NN._200),
        backgroundColor = if (isSystemInDarkTheme()) NestTheme.colors.NN._200 else NestTheme.colors.NN._50
    ) {
        Box {
            Box(
                modifier = Modifier
                    .background(NestTheme.colors.NN._100, shape = CircleShape)
                    .align(
                        Alignment.TopEnd
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = "tips logo",
                    modifier = Modifier.padding(10.dp),
                    tint = NestTheme.colors.NN._300
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                title?.run {
                    NestTypography(
                        text = this,
                        textStyle = NestTheme.typography.paragraph3.copy(fontWeight = FontWeight.Bold)
                    )
                }
                description?.run {
                    NestTypography(text = this, textStyle = NestTheme.typography.small)
                }
            }
        }
    }
}
