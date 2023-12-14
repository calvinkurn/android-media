package com.tokopedia.logisticorder.view.tipping

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
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
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.logisticorder.R as logisticorderR

@Composable
fun TrackingPageScreen() {
    val unifyIconId = getIconUnifyResourceIdRef(iconId = IconUnify.CALL_CENTER)
    val icon = painterResource(id = unifyIconId)
    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = stringResource(id = logisticorderR.string.label_tracking_activity),
                onBackClicked = {},
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
                .verticalScroll(rememberScrollState())
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
            EmptyTracking()
            LiveTrackingButton()
            FindNewDriverSection()
        }
    }
}

@Composable
fun FindNewDriverSection() {
    Column {
        NestButton(modifier = Modifier.fillMaxWidth(),
            variant = ButtonVariant.GHOST_ALTERNATE,
            text = stringResource(id = logisticorderR.string.find_new_driver),
            onClick = { /*TODO*/ })
        NestTypography(text = "Tunggu xx:xx untuk mencari driver baru")
    }
}

@Composable
fun LiveTrackingButton() {
    NestButton(modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = logisticorderR.string.label_live_tracking),
        onClick = { /*TODO*/ })
}

@Composable
fun EmptyTracking() {
    Column {
        Row {
            NestImage(source = ImageSource.Painter(logisticorderR.drawable.info))
            NestTypography(text = stringResource(id = logisticorderR.string.warning_no_courier_change))
        }
        repeat(3) {
            NestTypography(text = "test")
        }
    }
}

@Composable
fun TrackingHistory() {
    Column {
        repeat(3) {
            TrackingHistoryItem()
        }
    }
}

@Composable
fun TrackingHistoryItem() {
    Column(Modifier.height(IntrinsicSize.Max), verticalArrangement = Arrangement.Center) {
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
                    .padding(start = 12.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(NestTheme.colors.GN._500)
            )
            Column {
                NestTypography(text = "Senin")
                NestTypography(text = "Senin")
                NestTypography(text = "14.30 WIB")
                NestTypography(text = "14.30 WIB")
            }
        }
    }
}

@Composable
fun DriverWidget() {
    Column(Modifier.fillMaxWidth()) {
        NestTypography(text = stringResource(id = logisticorderR.string.driver_section_tracking_title))
        DriverInfoLayout()
        TippingLayout()
    }
}

@Composable
fun TippingLayout() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = colorResource(id = logisticorderR.color.dms_background_tipping_gojek_open)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            NestImage(source = ImageSource.Painter(logisticorderR.drawable.background_tipping_gojek))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                NestImage(source = ImageSource.Remote("https://images.tokopedia.net/img/tokofood/gofood.png"))
                Column {
                    NestTypography(text = "tipping text")
                    NestTypography(text = "tipping description")
                }
                NestButton(
                    text = stringResource(id = logisticorderR.string.card_tipping_btn_default_text),
                    onClick = { /*TODO*/ },
                    variant = ButtonVariant.GHOST_ALTERNATE
                )
            }
        }
    }
}

@Composable
fun DriverInfoLayout() {
    Row(Modifier.fillMaxWidth()) {
        NestImage(
            type = NestImageType.Circle,
            source = ImageSource.Remote("https://images.tokopedia.net/img/tokofood/gofood.png")
        ) {
            NestImage(ImageSource.Painter(logisticorderR.drawable.ic_find_driver))
        }

        Column {
            Row {
                NestTypography(text = "Nama driver")
                NestIcon(iconId = IconUnify.INFORMATION)
            }
            NestTypography(text = "Driver phone")
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(BorderStroke(1.dp, NestTheme.colors.NN._300))
        ) {
            NestIcon(iconId = IconUnify.CALL)
        }
    }
}

@Composable
fun TrackingDetail() {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TrackingDetailsItem(title = stringResource(id =logisticorderR.string.label_reference_number), "486118")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TrackingDetailsItem(
                stringResource(logisticorderR.string.label_delivery_date), "1 February 2020"
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
            stringResource(logisticorderR.string.tracking_label_eta), "17 - 19 April 2020"
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
