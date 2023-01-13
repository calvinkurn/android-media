package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInput
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState

@Composable
fun ReschedulePickupScreen(
    state: State<ReschedulePickupState>,
    input: ReschedulePickupInput,
    onDayClicked: (List<RescheduleDayOptionModel>) -> Unit,
    onTimeClicked: (List<RescheduleTimeOptionModel>) -> Unit,
    onReasonClicked: (List<RescheduleReasonOptionModel>) -> Unit,
    onSubtitleClicked: (String) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    onSaveReschedule: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NestTypography(
            text = stringResource(id = R.string.label_title_order_detail_reschedule_pick_up),
            textStyle = NestTheme.typography.heading5,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "icon kurir")
                    Column {
                        NestTypography(
                            text = stringResource(id = R.string.label_title_courier_reschedule_pick_up),
                            textStyle = NestTheme.typography.body3
                        )
                        NestTypography(
                            text = state.value.info.courier,
                            textStyle = NestTheme.typography.heading6
                        )
                    }
                }
                Row {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "icon invoice"
                    )
                    Column {
                        NestTypography(
                            text = stringResource(id = R.string.label_title_invoice_reschedule_pick_up),
                            textStyle = NestTheme.typography.body3
                        )
                        NestTypography(
                            text = state.value.info.invoice,
                            textStyle = NestTheme.typography.heading6
                        )
                    }
                }
            }
        }
        Divider(thickness = 8.dp)
        NestTypography(
            text = stringResource(id = R.string.label_title_reschedule_pick_up),
            textStyle = NestTheme.typography.heading5,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )
        NestTypography(
            text = constructRescheduleSubtitle(),
            textStyle = NestTheme.typography.body3,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable { onSubtitleClicked(state.value.info.applink) }
        )
        TipsUnifyCompose(
            title = stringResource(id = R.string.title_tips_reschedule_pick_up),
            description = state.value.info.guide,
            modifier = Modifier.padding(16.dp)
        )
        TextFieldUnifyCompose(
            value = input.day,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onDayClicked(state.value.options.dayOptions) },
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
        TextFieldUnifyCompose(
            value = input.time,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onTimeClicked(state.value.options.timeOptions) },
            enabled = false,
            label = {
                NestTypography(text = stringResource(id = R.string.label_time_reschedule_pick_up))
            },
            placeholder = {
                NestTypography(text = stringResource(id = R.string.placeholder_time_reschedule_pick_up))
            },
            trailingIcon = { TrailingIconTextField() }
        )
        TextFieldUnifyCompose(
            value = state.value.reason,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onReasonClicked(state.value.options.reasonOptions) },
            label = {
                NestTypography(text = stringResource(id = R.string.label_reason_reschedule_pickup))
            },
            enabled = false,
            trailingIcon = { TrailingIconTextField() }
        )
        if (state.value.info.summary.isNotEmpty()) {
            NestTicker(
                text = state.value.info.summary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
        if (state.value.isCustomReason) {
            TextFieldUnifyCompose(
                value = input.reason,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = {
                    NestTypography(text = stringResource(id = R.string.label_detail_reason_reschedule_pickup))
                },
                onValueChanged = { onOtherReasonChanged(it) },
                isError = state.value.customReasonError != null
            )
        }
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.title_button_reschedule_pickup),
            enabled = state.value.valid
        ) {
            onSaveReschedule()
        }
    }
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
            Icon(
                imageVector = Icons.Filled.AccountBox,
                contentDescription = "tips logo",
                modifier = Modifier.align(
                    Alignment.TopEnd
                )
            )
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
