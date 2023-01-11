package com.tokopedia.logisticseller.ui.reschedulepickup.uimodel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.common_compose.principles.NestButton
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
            Column {
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
