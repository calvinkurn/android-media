package com.tokopedia.logisticorder.view.tipping

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.header.compose.HeaderActionButton
import com.tokopedia.header.compose.HeaderIconSource.Painter
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.iconunify.getIconUnifyResourceIdRef
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.logisticorder.R as logisticorderR

@Composable
fun TrackingPageScreen() {
    val unifyIconId = getIconUnifyResourceIdRef(iconId = IconUnify.CALL_CENTER)
    val icon = painterResource(id = unifyIconId)
    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = stringResource(id = logisticorderR.string.label_tracking_activity),
                onBackClicked = {
                },
                optionsButton = listOf(
                    HeaderActionButton(
                        icon = Painter(icon)
                    )
                )
            )
        )
    }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TrackingDetail()
            Divider(
                thickness = 8.dp
            )
            DriverWidget()
            NestTypography(text = stringResource(logisticorderR.string.label_tracking_status))
            NestTypography(text = "Shipping")
            Divider(
                thickness = 4.dp
            )
            TrackingHistory()
        }
    }
}

@Composable
fun TrackingHistory() {
    Column {
        Row {
            Box(
                Modifier
                    .clip(CircleShape)
                    .size(24.dp, 24.dp)
                    .background(NestTheme.colors.GN._500)
            ) {
                NestIcon(iconId = IconUnify.CHECK)
            }
            NestTypography(text = "Senin")
            NestTypography(text = "14.30 WIB")
        }
        Row {
            // line 2
            Box(
                Modifier
                    .clip(CircleShape)
                    .size(24.dp, 24.dp)
                    .background(NestTheme.colors.GN._500)
            ) {
                NestIcon(iconId = IconUnify.CHECK)
            }
            NestTypography(text = "Senin")
            NestTypography(text = "14.30 WIB")
        }
    }
}

@Composable
fun DriverWidget() {
    Column {
    }
}

@Composable
fun TrackingDetail() {
    Column(Modifier.fillMaxWidth()) {
        NestTypography(text = stringResource(logisticorderR.string.label_reference_number))
        NestTypography(text = "486118")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TrackingDetailsItem(
                stringResource(logisticorderR.string.label_delivery_date),
                "1 February 2020"
            )
            TrackingDetailsItem(stringResource(logisticorderR.string.label_service_code), "REG15")
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // this one need html parser
            TrackingDetailsItem(
                stringResource(logisticorderR.string.label_seller_courier_tracking),
                "Nama Penjual",
                "Bandung"
            )
            TrackingDetailsItem(
                stringResource(logisticorderR.string.label_buyer),
                "Saiful Jamil",
                "Grogol Petamburan Jakarta"
            )
        }
        TrackingDetailsItem(
            stringResource(logisticorderR.string.tracking_label_eta),
            "17 - 19 April 2020"
        )
    }
}

@Composable
fun TrackingDetailsItem(title: String, vararg value: String) {
    Column {
        NestTypography(text = title)
        value.forEach {
            NestTypography(text = it)
        }
    }
}

@Preview
@Composable
fun TrackingPagePreview() {
    NestTheme {
        TrackingPageScreen()
    }
}
