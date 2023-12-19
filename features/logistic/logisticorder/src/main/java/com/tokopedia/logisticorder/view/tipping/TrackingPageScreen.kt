package com.tokopedia.logisticorder.view.tipping

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TrackingDetail()
            Divider(
                thickness = 8.dp
            )
            DriverWidget()
            ShippingStatusSection()
            Divider(
                thickness = 4.dp
            )
            TrackingHistory()
//            EmptyTracking()
            LiveTrackingButton()
            FindNewDriverSection()
        }
    }
}

@Composable
fun ShippingStatusSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        NestTypography(
            text = stringResource(logisticorderR.string.label_tracking_status),
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950)
        )
        NestTypography(
            text = "Shipping",
            textStyle = NestTheme.typography.heading4.copy(color = NestTheme.colors.NN._950)
        )
    }
}

@Composable
fun FindNewDriverSection() {
    Column {
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            variant = ButtonVariant.GHOST_ALTERNATE,
            text = stringResource(id = logisticorderR.string.find_new_driver),
            onClick = { /*TODO*/ }
        )
        NestTypography(text = "Tunggu xx:xx untuk mencari driver baru")
    }
}

@Composable
fun LiveTrackingButton() {
    NestButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        text = stringResource(id = logisticorderR.string.label_live_tracking),
        onClick = { /*TODO*/ }
    )
}

@Composable
fun EmptyTracking() {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
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
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        repeat(1) {
            TrackingHistoryItem()
        }
    }
}

@Composable
fun TrackingHistoryItem() {
    ConstraintLayout(modifier = Modifier.height(IntrinsicSize.Min)) {
        val (day, time, description, courier, circle, line) = createRefs()
        Box(
            Modifier
                .clip(CircleShape)
                .size(24.dp, 24.dp)
                .background(NestTheme.colors.GN._500)
                .constrainAs(circle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            NestIcon(iconId = IconUnify.CHECK)
        }
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(day) {
                    start.linkTo(circle.end)
                    top.linkTo(circle.top)
                    bottom.linkTo(circle.bottom)
                },
            text = "Rabu"
        )
        NestTypography(
            modifier = Modifier.constrainAs(time) {
                end.linkTo(parent.end)
                top.linkTo(day.top)
            },
            text = "14.30 WIB"
        )

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(description) {
                    start.linkTo(day.start)
                    top.linkTo(day.bottom)
                },
            text = "Transit di DC Cakung"
        )

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(courier) {
                    start.linkTo(day.start)
                    top.linkTo(description.bottom)
                },
            text = "Kurir: "
        )
        Box(
            Modifier
                .constrainAs(line) {
                    top.linkTo(circle.bottom)
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    height = Dimension.matchParent
                }
                .width(1.dp)
                .background(NestTheme.colors.NN._950)
                .fillMaxHeight()

        )
    }
}

@Composable
fun DriverWidget() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        NestTypography(
            text = stringResource(id = logisticorderR.string.driver_section_tracking_title),
            textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold)
        )
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
            NestImage(
                source = ImageSource.Painter(logisticorderR.drawable.background_tipping_gojek)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    NestImage(
                        modifier = Modifier
                            .size(48.dp, 48.dp),
                        type = NestImageType.Circle,
                        source = ImageSource.Remote("https://images.tokopedia.net/img/tokofood/gofood.png")
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        NestTypography(
                            text = "tipping text",
                            textStyle = NestTheme.typography.display2.copy(
                                color = NestTheme.colors.NN._0,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        NestTypography(
                            text = "tipping description",
                            textStyle = NestTheme.typography.paragraph3.copy(color = NestTheme.colors.NN._0)
                        )
                    }
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
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row {
            NestImage(
                modifier = Modifier.size(48.dp, 48.dp),
                type = NestImageType.Circle,
                source = ImageSource.Remote("https://images.tokopedia.net/img/tokofood/gofood.png")
            ) {
                NestImage(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(48.dp, 48.dp),
                    type = NestImageType.Circle,
                    source = ImageSource.Painter(logisticorderR.drawable.ic_find_driver)
                )
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Row {
                    NestTypography(text = "Nama driver")
                    NestIcon(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(13.dp, 13.dp),
                        iconId = IconUnify.INFORMATION
                    )
                }
                NestTypography(text = "Driver phone")
            }
        }

        Box(
            modifier = Modifier
                .size(36.dp, 36.dp)
                .border(1.dp, NestTheme.colors.NN._300, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            NestIcon(modifier = Modifier.size(20.dp, 20.dp), iconId = IconUnify.CALL)
        }
    }
}

@Composable
fun TrackingDetail() {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val (ref, shippingDate, serviceCode, seller, buyer, eta) = createRefs()
        val startGuideline = createGuidelineFromStart(0.5f)
        TrackingDetailsItem(
            modifier = Modifier.constrainAs(ref) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            title = stringResource(id = logisticorderR.string.label_reference_number),
            "486118",
            valueStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950)
        )
        TrackingDetailsItem(
            modifier = Modifier.constrainAs(shippingDate) {
                top.linkTo(ref.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(startGuideline)
                width = Dimension.fillToConstraints
            },
            stringResource(logisticorderR.string.label_delivery_date),
            "1 February 2020"
        )
        TrackingDetailsItem(
            modifier = Modifier.constrainAs(serviceCode) {
                top.linkTo(shippingDate.top)
                start.linkTo(startGuideline)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            stringResource(logisticorderR.string.label_service_code),
            "REG15"
        )
        // this one need html parser
        TrackingDetailsItem(
            modifier = Modifier.constrainAs(seller) {
                top.linkTo(shippingDate.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(startGuideline)
                width = Dimension.fillToConstraints
            },
            stringResource(logisticorderR.string.label_seller_courier_tracking),
            "Nama Penjual",
            "Bandung"
        )
        TrackingDetailsItem(
            modifier = Modifier.constrainAs(buyer) {
                top.linkTo(seller.top)
                start.linkTo(startGuideline)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            stringResource(logisticorderR.string.label_buyer),
            "Saiful Jamil",
            "Grogol Petamburan Jakarta"
        )
        TrackingDetailsItem(
            modifier = Modifier.constrainAs(eta) {
                top.linkTo(seller.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            stringResource(logisticorderR.string.tracking_label_eta),
            "17 - 19 April 2020"
        )
    }
}

@Composable
fun TrackingDetailsItem(
    modifier: Modifier,
    title: String,
    vararg value: String,
    valueStyle: TextStyle = NestTheme.typography.heading6.copy(color = NestTheme.colors.NN._950)
) {
    Column(modifier = modifier) {
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950)
        )
        value.forEach {
            NestTypography(modifier = Modifier.fillMaxWidth(), text = it, textStyle = valueStyle)
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
