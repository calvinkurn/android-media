package com.tokopedia.logisticorder.view.tipping

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.tokopedia.header.compose.HeaderActionButton
import com.tokopedia.header.compose.HeaderIconSource.Painter
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.iconunify.getIconUnifyResourceIdRef
import com.tokopedia.imageassets.TokopediaImageUrl.ICON_OPEN_TIPPING_GOJEK
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.DetailModel
import com.tokopedia.logisticorder.uimodel.EtaModel
import com.tokopedia.logisticorder.uimodel.LastDriverModel
import com.tokopedia.logisticorder.uimodel.TippingModel
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.uimodel.TrackingPageState
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.utils.TippingConstant
import com.tokopedia.logisticorder.utils.TippingConstant.OPEN
import com.tokopedia.logisticorder.utils.toHyphenIfEmptyOrNull
import com.tokopedia.logisticorder.view.TrackingPageFragment.Companion.INVALID_ORDER_STATUS
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.components.NestImageType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.logisticorder.R as logisticorderR

private val TrackOrderModel.emptyTrackingTitle: String
    @Composable get() {
        return if (invalid) {
            stringResource(id = logisticorderR.string.warning_courier_invalid)
        } else if (orderStatus == INVALID_ORDER_STATUS || change == 0 || trackHistory.isEmpty()) {
            stringResource(id = logisticorderR.string.warning_no_courier_change)
        } else {
            ""
        }
    }
private val TippingModel.buttonText: String
    @Composable
    get() {
        return when (status) {
            TippingConstant.SUCCESS_PAYMENT, TippingConstant.SUCCESS_TIPPING -> stringResource(R.string.btn_tipping_success_text)
            TippingConstant.WAITING_PAYMENT -> stringResource(logisticorderR.string.btn_tipping_waiting_payment_text)
            TippingConstant.REFUND_TIP -> stringResource(logisticorderR.string.btn_tipping_refund_text)
            else -> stringResource(logisticorderR.string.btn_tipping_open_text)
        }
    }
private val TrackingDataModel?.hasDriverInfo: Boolean
    get() {
        return this?.run { tipping.eligibleForTipping || lastDriver.name.isNotEmpty() } ?: false
    }
private val DetailModel.deliveryDate: CharSequence
    get() {
        var date: String = sendDate
        if (sendDate.isNotEmpty()) {
            date =
                DateUtil.formatDate("yyyy-MM-dd", "dd MMMM yyyy", sendDate)
        }
        return date.toHyphenIfEmptyOrNull()
    }

@Composable
fun TrackingPageScreen(state: TrackingPageState) {
    val unifyIconId = getIconUnifyResourceIdRef(iconId = IconUnify.CALL_CENTER)
    val icon = painterResource(id = unifyIconId)
    Scaffold(topBar = {
        NestHeader(
            type = NestHeaderType.SingleLine(
                title = stringResource(id = logisticorderR.string.label_tracking_activity),
                onBackClicked = {},
                optionsButton = if (!state.trackingData?.page?.contactUsUrl.isNullOrEmpty()) {
                    listOf(
                        HeaderActionButton(
                            icon = Painter(icon)
                        )
                    )
                } else {
                    listOf()
                }
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
            TrackingDetail(state)
            Divider(
                thickness = 8.dp
            )
            DriverWidget(state.trackingData)
            ShippingStatusSection(state.trackingData?.trackOrder?.status)
            Divider(
                thickness = 4.dp
            )
            TrackingHistory(state.trackingData?.trackOrder)
            LiveTrackingButton(state.trackingData?.trackOrder?.detail?.trackingUrl)
            FindNewDriverSection(state.retryAvailability)
        }
    }
}

@Composable
fun ShippingStatusSection(status: String?) {
    if (!status.isNullOrEmpty()) {
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
                text = status,
                textStyle = NestTheme.typography.heading4.copy(color = NestTheme.colors.NN._950)
            )
        }
    }
}

@Composable
fun FindNewDriverSection(retryAvailability: RetryAvailabilityResponse?) {
    retryAvailability?.let { model ->
        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            if (model.retryAvailability.showRetryButton) {
                NestButton(
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.GHOST_ALTERNATE,
                    text = stringResource(id = logisticorderR.string.find_new_driver),
                    onClick = { /*TODO*/ }
                )
            }
            if (model.retryAvailability.deadlineRetryUnixtime.toLong() > 0L) {
                val deadline = model.retryAvailability.deadlineRetryUnixtime.toLong()
                val now = System.currentTimeMillis() / 1000L
                val remainingTime = deadline - now
                // todo update time in millis
                if (remainingTime > 0) {
                    val timeInMillis = remainingTime * 1000
                    val formattedTime = DateUtils.formatElapsedTime(timeInMillis / 1000)
                    NestTypography(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Tunggu $formattedTime untuk mencari driver baru",
                        textStyle = NestTheme.typography.body2.copy(textAlign = TextAlign.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun LiveTrackingButton(trackingUrl: String?) {
    trackingUrl?.takeIf { it.isNotEmpty() }?.run {
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = stringResource(id = logisticorderR.string.label_live_tracking),
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun EmptyTracking(history: TrackOrderModel) {
    ConstraintLayout(modifier = Modifier.padding(16.dp)) {
        val (icon, description, step1, step2, step3) = createRefs()
        NestImage(
            modifier = Modifier.constrainAs(icon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            source = ImageSource.Painter(logisticorderR.drawable.info)
        )
        NestTypography(
            modifier = Modifier.constrainAs(description) {
                top.linkTo(icon.top)
                start.linkTo(icon.end, margin = 4.dp)
            },
            text = history.emptyTrackingTitle
        )

        NestTypography(
            modifier = Modifier.constrainAs(step1) {
                top.linkTo(description.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            },
            text = stringResource(id = R.string.empty_notes_1)
        )

        NestTypography(
            modifier = Modifier.constrainAs(step2) {
                top.linkTo(step1.bottom, margin = 4.dp)
                start.linkTo(parent.start)
            },
            text = stringResource(id = R.string.empty_notes_2)
        )

        NestTypography(
            modifier = Modifier.constrainAs(step3) {
                top.linkTo(step2.bottom, margin = 4.dp)
                start.linkTo(parent.start)
            },
            text = stringResource(id = R.string.empty_notes_3)
        )
    }
}

@Composable
fun TrackingHistory(trackHistory: TrackOrderModel?) {
    trackHistory?.let { model ->
        val list = model.trackHistory
        if (list.isNotEmpty() && !model.invalid && model.orderStatus != INVALID_ORDER_STATUS && model.change != 0) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                repeat(list.size) { index ->
                    TrackingHistoryItem(list[index], index == 0, index == list.size - 1)
                }
            }
        } else {
            EmptyTracking(model)
        }
    }
}

@Composable
fun TrackingHistoryItem(trackHistoryModel: TrackHistoryModel, isFirst: Boolean, isLast: Boolean) {
    ConstraintLayout {
        val (day, time, description, courier, circle, line, pod) = createRefs()
        val circleColor = if (isFirst) NestTheme.colors.GN._500 else NestTheme.colors.NN._50
        Box(
            Modifier
                .clip(CircleShape)
                .size(24.dp, 24.dp)
                .background(circleColor)
                .constrainAs(circle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            NestIcon(
                iconId = IconUnify.CHECK,
                colorLightEnable = NestTheme.colors.NN._0,
                colorNightEnable = NestTheme.colors.NN._0
            )
        }
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(day) {
                    start.linkTo(circle.end, margin = 8.dp)
                    top.linkTo(circle.top)
                    bottom.linkTo(circle.bottom)
                },
            text = DateUtil.formatDate("yyyy-MM-dd", "EEEE, dd MMM yyyy", trackHistoryModel.date),
            textStyle = NestTheme.typography.heading5.copy(color = if (isFirst) NestTheme.colors.GN._500 else NestTheme.colors.NN._950)
        )
        NestTypography(
            modifier = Modifier.constrainAs(time) {
                end.linkTo(parent.end)
                top.linkTo(day.top)
            },
            text = "${DateUtil.formatDate("HH:mm:ss", "HH:mm", trackHistoryModel.time)} WIB",
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950)
        )

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(description) {
                    start.linkTo(day.start)
                    top.linkTo(day.bottom, margin = 5.dp)
                },
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950),
            text = HtmlLinkHelper(
                LocalContext.current,
                trackHistoryModel.status
            ).spannedString?.toAnnotatedString() ?: ""
        )

        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(courier) {
                    start.linkTo(day.start)
                    top.linkTo(description.bottom, margin = 5.dp)
                    visibility =
                        if (trackHistoryModel.partnerName.isNotEmpty()) Visibility.Visible else Visibility.Gone
                },
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950),
            text = "Kurir: ${trackHistoryModel.partnerName}"
        )
        Box(
            Modifier
                .constrainAs(line) {
                    top.linkTo(circle.bottom)
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    bottom.linkTo(pod.bottom)
                    height = Dimension.fillToConstraints
                    visibility = if (isLast) Visibility.Gone else Visibility.Visible
                }
                .width(1.dp)
                .background(NestTheme.colors.NN._50)
        )
        NestImage(
            modifier = Modifier
                .size(58.dp, 58.dp)
                .constrainAs(pod) {
                    top.linkTo(courier.bottom, margin = 10.dp)
                    start.linkTo(description.start)
                    bottom.linkTo(parent.bottom)
                    visibility =
                        if (trackHistoryModel.proof.imageId.isNotEmpty()) Visibility.Visible else Visibility.Gone
                },
            // todo todo
            source = ImageSource.Remote(
                LogisticImageDeliveryHelper.getDeliveryImage(
                    trackHistoryModel.proof.imageId,
                    orderId,
                    LogisticImageDeliveryHelper.IMAGE_SMALL_SIZE,
                    userSession.userId,
                    LogisticImageDeliveryHelper.DEFAULT_OS_TYPE,
                    userSession.deviceId
                )
            )
        )
    }
}

@Composable
fun DriverWidget(trackingDataModel: TrackingDataModel?) {
    trackingDataModel?.takeIf { it.hasDriverInfo }?.let {
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
            DriverInfoLayout(it.lastDriver)
            TippingLayout(it.tipping)
        }
    }
}

@Composable
fun TippingLayout(tipping: TippingModel) {
    if (tipping.eligibleForTipping) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = if (tipping.status == OPEN) colorResource(id = logisticorderR.color.dms_background_tipping_gojek_open) else NestTheme.colors.NN._0
        ) {
            ConstraintLayout {
                val (tippingLogo, tippingText, tippingDescription, tippingButton, tippingBg) = createRefs()
                NestImage(
                    modifier = Modifier.constrainAs(tippingBg) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        visibility =
                            if (tipping.status == OPEN) Visibility.Visible else Visibility.Gone
                    },
                    contentScale = ContentScale.FillHeight,
                    source = ImageSource.Painter(logisticorderR.drawable.background_tipping_gojek)
                )
                NestImage(
                    modifier = Modifier
                        .size(48.dp, 48.dp)
                        .constrainAs(tippingLogo) {
                            top.linkTo(parent.top, margin = 12.dp)
                            start.linkTo(parent.start, margin = 12.dp)
                            bottom.linkTo(parent.bottom, margin = 12.dp)
                            visibility =
                                if (tipping.status == OPEN) Visibility.Visible else Visibility.Gone
                        },
                    type = NestImageType.Circle,
                    source = ImageSource.Remote(ICON_OPEN_TIPPING_GOJEK)
                )
                NestTypography(
                    modifier = Modifier.constrainAs(tippingText) {
                        top.linkTo(tippingLogo.top)
                        start.linkTo(tippingLogo.end, margin = 12.dp)
                        end.linkTo(tippingButton.start)
                        width = Dimension.fillToConstraints
                    },
                    text = tipping.statusTitle,
                    textStyle = NestTheme.typography.display2.copy(
                        color = if (tipping.status == OPEN) NestTheme.colors.NN._0 else NestTheme.colors.NN._950,
                        fontWeight = FontWeight.Bold
                    )
                )
                NestTypography(
                    modifier = Modifier.constrainAs(tippingDescription) {
                        top.linkTo(tippingText.bottom)
                        start.linkTo(tippingLogo.end, margin = 12.dp)
                        end.linkTo(tippingButton.start)
                        width = Dimension.fillToConstraints
                    },
                    text = tipping.statusSubtitle,
                    textStyle = NestTheme.typography.paragraph3.copy(color = if (tipping.status == OPEN) NestTheme.colors.NN._0 else NestTheme.colors.NN._950)
                )
                NestButton(
                    modifier = Modifier.constrainAs(tippingButton) {
                        end.linkTo(parent.end, margin = 12.dp)
                        top.linkTo(parent.top)
                        start.linkTo(tippingText.end)
                        bottom.linkTo(parent.bottom)
                    },
                    text = tipping.buttonText,
                    onClick = { /*TODO*/ },
                    variant = if (tipping.status == OPEN) ButtonVariant.GHOST_INVERTED else ButtonVariant.GHOST
                )
            }
        }
    }
}

@Composable
fun DriverInfoLayout(lastDriver: LastDriverModel) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (driverImage, driverName, driverLicense, driverInfo, callButton) = createRefs()
        NestImage(
            modifier = Modifier
                .size(48.dp, 48.dp)
                .constrainAs(driverImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            type = NestImageType.Circle,
            source = if (lastDriver.photo.isNotEmpty()) {
                ImageSource.Remote(lastDriver.photo)
            } else {
                ImageSource.Painter(
                    logisticorderR.drawable.ic_find_driver
                )
            }
        ) {
            NestImage(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(48.dp, 48.dp),
                type = NestImageType.Circle,
                source = ImageSource.Painter(logisticorderR.drawable.ic_find_driver)
            )
        }
        NestTypography(
            modifier = Modifier.constrainAs(driverName) {
                top.linkTo(parent.top)
                start.linkTo(driverImage.end, margin = 8.dp)
            },
            textStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950),
            text = lastDriver.name.ifEmpty { stringResource(id = R.string.driver_not_found_title) }
        )
        // todo clickable
        NestIcon(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(13.dp, 13.dp)
                .constrainAs(driverInfo) {
                    start.linkTo(driverName.end)
                    top.linkTo(driverName.top)
                    bottom.linkTo(driverName.bottom)
                    visibility =
                        if (lastDriver.name.isNotEmpty()) Visibility.Visible else Visibility.Gone
                },
            iconId = IconUnify.INFORMATION
        )
        NestTypography(
            modifier = Modifier.constrainAs(driverLicense) {
                top.linkTo(driverName.bottom)
                start.linkTo(driverName.start)
            },
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950),
            text = if (lastDriver.name.isNotEmpty()) lastDriver.licenseNumber else stringResource(id = R.string.driver_not_found_subtitle)
        )
        // todo clickable
        if (lastDriver.phone.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(36.dp, 36.dp)
                    .border(1.dp, NestTheme.colors.NN._300, CircleShape)
                    .constrainAs(callButton) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                contentAlignment = Alignment.Center
            ) {
                NestIcon(modifier = Modifier.size(20.dp, 20.dp), iconId = IconUnify.CALL)
            }
        }
    }
}

@Composable
fun TrackingDetail(state: TrackingPageState) {
    if (!state.isLoading && state.trackingData != null) {
        val data = state.trackingData
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
                data.trackOrder.shippingRefNum.toHyphenIfEmptyOrNull(),
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
                data.trackOrder.detail.deliveryDate
            )
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(serviceCode) {
                    top.linkTo(shippingDate.top)
                    start.linkTo(startGuideline)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                stringResource(logisticorderR.string.label_service_code),
                data.trackOrder.detail.serviceCode.toHyphenIfEmptyOrNull()
            )
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(seller) {
                    top.linkTo(shippingDate.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(startGuideline)
                    width = Dimension.fillToConstraints
                },
                stringResource(logisticorderR.string.label_seller_courier_tracking),
                HtmlLinkHelper(
                    LocalContext.current,
                    data.trackOrder.detail.shipperName
                ).spannedString?.toAnnotatedString() ?: "",
                data.trackOrder.detail.shipperCity
            )
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(buyer) {
                    top.linkTo(seller.top)
                    start.linkTo(startGuideline)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                stringResource(logisticorderR.string.label_buyer),
                data.trackOrder.detail.receiverName,
                data.trackOrder.detail.receiverCity
            )
            EtaWidget(
                modifier = Modifier.constrainAs(eta) {
                    top.linkTo(seller.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                data.trackOrder.detail.eta
            )
        }
    }
}

@Composable
fun EtaWidget(modifier: Modifier, etaModel: EtaModel) {
    ConstraintLayout(modifier = modifier) {
        val (title, eta, icon) = createRefs()
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            text = stringResource(logisticorderR.string.tracking_label_eta),
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950)
        )
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(eta) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                },
            text = etaModel.userInfo,
            textStyle = NestTheme.typography.heading6.copy(color = NestTheme.colors.NN._950)
        )
        // todo bottom sheet
        NestIcon(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(13.dp, 13.dp)
                .constrainAs(icon) {
                    start.linkTo(eta.end)
                    top.linkTo(eta.top)
                    bottom.linkTo(eta.bottom)
                    visibility = if (etaModel.isChanged) Visibility.Visible else Visibility.Gone
                },
            iconId = IconUnify.INFORMATION
        )
    }
}

@Composable
fun TrackingDetailsItem(
    modifier: Modifier,
    title: String,
    vararg value: CharSequence,
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
    val state = TrackingPageState(
        isLoading = false,
        trackingData = TrackingDataModel(
            trackOrder = TrackOrderModel(
                shippingRefNum = "NORESI",
                detail = DetailModel(
                    serviceCode = "SER20",
                    shipperCity = "Bandung",
                    shipperName = "Penjual",
                    receiverName = "Pembeli",
                    receiverCity = "Jakarta",
                    sendDate = "10 - 11 November 2021",
                    eta = EtaModel(
                        userInfo = "17 - 23 Desember 2023"
                    )
                )
            )
        )
    )
    NestTheme {
        TrackingPageScreen(state)
    }
}
