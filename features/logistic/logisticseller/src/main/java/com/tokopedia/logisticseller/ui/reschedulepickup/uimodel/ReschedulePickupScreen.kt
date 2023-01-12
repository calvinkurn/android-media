package com.tokopedia.logisticseller.ui.reschedulepickup.uimodel

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestTicker
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.R

@Composable
fun ReschedulePickupScreen() {
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
                    Column {
                        NestTypography(
                            text = stringResource(id = R.string.label_title_courier_reschedule_pick_up),
                            textStyle = NestTheme.typography.body3
                        )
                        NestTypography(
                            text = "todo",
                            textStyle = NestTheme.typography.heading6
                        )
                    }
                }
                Row {
                    Column {
                        NestTypography(
                            text = stringResource(id = R.string.label_title_invoice_reschedule_pick_up),
                            textStyle = NestTheme.typography.body3
                        )
                        NestTypography(
                            text = "todo",
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
            modifier = Modifier.padding(16.dp)
        )
        NestTypography(
            text = "todo",
            textStyle = NestTheme.typography.body3,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TipsUnifyCompose(
            title = "title",
            description = "description",
            modifier = Modifier.padding(16.dp)
        )
        TextFieldUnifyCompose(
            value = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = {
                NestTypography(
                    text =
                    stringResource(id = R.string.label_day_reschedule_pick_up)
                )
            },
            placeholder = {
                NestTypography(text = stringResource(id = R.string.placeholder_day_reschedule_pick_up))
            },
            trailingIcon = { TrailingIconTextField() }
        )
        TextFieldUnifyCompose(
            value = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = {
                NestTypography(text = stringResource(id = R.string.label_time_reschedule_pick_up))
            },
            placeholder = {
                NestTypography(text = stringResource(id = R.string.placeholder_time_reschedule_pick_up))
            },
            trailingIcon = { TrailingIconTextField() }
        )
        TextFieldUnifyCompose(
            value = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = {
                NestTypography(text = stringResource(id = R.string.label_reason_reschedule_pickup))
            },
            trailingIcon = { TrailingIconTextField() }
        )
        NestTicker(
            text = "ticker",
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        TextFieldUnifyCompose(
            value = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = {
                NestTypography(text = stringResource(id = R.string.label_detail_reason_reschedule_pickup))
            }
        )
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.title_button_reschedule_pickup)
        ) {
//
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
    onValueChanged: (TextFieldValue) -> Unit = {},
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = TextFieldValue(text = value),
        onValueChange = onValueChanged,
        modifier = modifier,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        label = label

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
        backgroundColor = NestTheme.colors.NN._50
    ) {
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

@Preview
@Composable
fun ReschedulePickupScreenPreview() {
    ReschedulePickupScreen()
}
