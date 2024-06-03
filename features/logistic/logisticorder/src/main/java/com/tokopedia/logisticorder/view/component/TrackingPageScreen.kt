package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.header.compose.HeaderActionButton
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyResourceIdRef
import com.tokopedia.logisticorder.uimodel.DetailModel
import com.tokopedia.logisticorder.uimodel.EtaModel
import com.tokopedia.logisticorder.uimodel.LastDriverModel
import com.tokopedia.logisticorder.uimodel.PageModel
import com.tokopedia.logisticorder.uimodel.ProofModel
import com.tokopedia.logisticorder.uimodel.TippingModel
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.uimodel.TrackingPageEvent
import com.tokopedia.logisticorder.uimodel.TrackingPageState
import com.tokopedia.logisticorder.utils.TippingConstant
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.IconSource
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.logisticorder.R as logisticorderR

@Composable
fun TrackingPageScreen(
    state: TrackingPageState,
    openWebview: (url: String) -> Unit,
    onClickTippingButton: (tipping: TippingModel) -> Unit,
    openTippingInfo: () -> Unit,
    pressBack: () -> Unit,
    callDriver: (phoneNumber: String) -> Unit,
    seeProofOfDelivery: (proof: ProofModel) -> Unit,
    copyShippingRefNumber: (shippingRefNum: String) -> Unit,
    seeEtaChangesInfo: (description: String) -> Unit,
    onEvent: (TrackingPageEvent) -> Unit
) {
    val unifyIconId = getIconUnifyResourceIdRef(iconId = IconUnify.CALL_CENTER)
    Scaffold(
        topBar = {
            NestHeader(
                type = NestHeaderType.SingleLine(
                    title = stringResource(id = logisticorderR.string.label_tracking_activity),
                    onBackClicked = pressBack,
                    optionsButton = if (!state.trackingData?.page?.contactUsUrl.isNullOrEmpty()) {
                        listOf(
                            HeaderActionButton(
                                icon = IconSource.Painter(unifyIconId),
                                onClicked = {
                                    state.trackingData?.page?.contactUsUrl?.run(
                                        openWebview
                                    )
                                }
                            )
                        )
                    } else {
                        listOf()
                    }
                )
            )
        },
        backgroundColor = NestTheme.colors.NN._0
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TrackingDetailSection(state, copyShippingRefNumber, seeEtaChangesInfo)
            DriverWidget(state.trackingData, callDriver, openTippingInfo, onClickTippingButton)
            ShippingStatusSection(state.trackingData?.trackOrder?.status)
            TargetedTicker(state.tickerData, openWebview)
            Divider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 20.dp)
            )
            TrackingHistory(state.trackingData?.trackOrder, seeProofOfDelivery)
            LiveTrackingSection(state.trackingData?.trackOrder?.detail?.trackingUrl, openWebview)
            FindNewDriverSection(state.retryAvailability, onEvent)
            Loading(state.isLoading)
        }
    }
}

@Composable
private fun Loading(isLoading: Boolean) {
    if (isLoading) {
        NestLoader(
            variant = NestLoaderType.Circular(size = NestLoaderSize.Small)
        )
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

@Preview
@Composable
private fun TrackingPagePreview() {
    val lastDriverModel = LastDriverModel(
        name = "Budi",
        phone = "6256648394543",
        licenseNumber = "B 1238 JED",
        isChanged = true
    )
    val tipping = TippingModel(
        status = TippingConstant.OPEN,
        statusTitle = "Yuk, beri tip ke driver",
        statusSubtitle = "Tip 100% diterima driver"
    )
    val tickerData = listOf(
        TickerModel.TickerItem(
            type = 3,
            title = "Pengiriman mengalami kendala",
            content = "Pengiriman terlambat karena cuaca buruk",
            linkUrl = "https://tokopedia/help"
        ),
        TickerModel.TickerItem(
            type = 2,
            title = "Pengiriman menggunakan Kurir Rekomendasi",
            content = "Informasi mengenai kurir rekomendasi",
            linkUrl = "https://tokopedia/help"
        )
    )
    val tickerModel = TickerModel(
        item = tickerData
    )
    val state = TrackingPageState(
        isLoading = false,
        tickerData = tickerModel,
        trackingData = TrackingDataModel(
            page = PageModel(
                contactUsUrl = "https://tokopedia.com/help"
            ),
            trackOrder = TrackOrderModel(
                shippingRefNum = "NORESI",
                status = "Shipping",
                detail = DetailModel(
                    serviceCode = "SER20",
                    shipperCity = "Bandung",
                    shipperName = "Penjual",
                    receiverName = "Pembeli",
                    receiverCity = "Jakarta",
                    sendDate = "10 - 11 November 2021",
                    eta = EtaModel(
                        userInfo = "17 - 23 Desember 2023"
                    ),
                    trackingUrl = "http://google.com"
                ),
                trackHistory = listOf(
                    TrackHistoryModel(
                        dateTime = "2021-11-12 22:38:55",
                        date = "2021-11-12",
                        status = "Pesanan dalam perjalanan",
                        city = "Bandung",
                        time = "22:38:55"
                    ),
                    TrackHistoryModel(
                        dateTime = "2021-11-10 22:38:55",
                        date = "2021-11-10",
                        status = "Pesanan telah di pickup",
                        city = "Surabaya",
                        time = "22:38:55"
                    )
                )
            ),
            lastDriver = lastDriverModel,
            tipping = tipping
        )
    )
    NestTheme {
        TrackingPageScreen(state, {}, {}, {}, {}, {}, {}, {}, {}, {})
    }
}
