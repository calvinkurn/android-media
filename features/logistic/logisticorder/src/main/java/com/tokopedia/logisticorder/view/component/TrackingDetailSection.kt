package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.DetailModel
import com.tokopedia.logisticorder.uimodel.EtaModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.uimodel.TrackingPageState
import com.tokopedia.logisticorder.utils.toHyphenIfEmptyOrNull
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.date.DateUtil

@Composable
fun TrackingDetailSection(
    state: TrackingPageState,
    copyShippingRefNumber: (shippingRefNum: String) -> Unit,
    seeEtaChangesInfo: (description: String) -> Unit
) {
    if (!state.isLoading && state.trackingData != null) {
        val data = state.trackingData
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
        ) {
            val (ref, shippingDate, serviceCode, seller, buyer, eta, divider) = createRefs()
            val secondColumnStartGuideline = createGuidelineFromStart(0.5f)
            val startGuideline = createGuidelineFromStart(20.dp)
            val endGuideline = createGuidelineFromEnd(20.dp)
            TrackingDetailsItemWithIcon(
                modifier = Modifier.constrainAs(ref) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                title = stringResource(id = R.string.label_reference_number),
                value = data.trackOrder.shippingRefNum.toHyphenIfEmptyOrNull(),
                icon = IconUnify.COPY,
                iconSize = 16.dp,
                showIcon = true,
                valueStyle = NestTheme.typography.heading5.copy(color = NestTheme.colors.NN._950)
            ) { copyShippingRefNumber(data.trackOrder.shippingRefNum) }
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(shippingDate) {
                    top.linkTo(ref.bottom, margin = 8.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(secondColumnStartGuideline)
                    width = Dimension.fillToConstraints
                },
                stringResource(R.string.label_delivery_date),
                data.trackOrder.detail.deliveryDate
            )
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(serviceCode) {
                    top.linkTo(shippingDate.top)
                    start.linkTo(secondColumnStartGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                },
                stringResource(R.string.label_service_code),
                data.trackOrder.detail.serviceCode.toHyphenIfEmptyOrNull()
            )
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(seller) {
                    top.linkTo(shippingDate.bottom, margin = 8.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(secondColumnStartGuideline)
                    width = Dimension.fillToConstraints
                },
                stringResource(R.string.label_seller_courier_tracking),
                HtmlLinkHelper(
                    LocalContext.current,
                    data.trackOrder.detail.shipperName
                ).spannedString?.toAnnotatedString() ?: "",
                data.trackOrder.detail.shipperCity
            )
            TrackingDetailsItem(
                modifier = Modifier.constrainAs(buyer) {
                    top.linkTo(seller.top)
                    start.linkTo(secondColumnStartGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                },
                stringResource(R.string.label_buyer),
                data.trackOrder.detail.receiverName,
                data.trackOrder.detail.receiverCity
            )
            TrackingDetailsItemWithIcon(
                modifier = Modifier.constrainAs(eta) {
                    top.linkTo(seller.bottom, margin = 8.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                },
                title = stringResource(R.string.tracking_label_eta),
                value = data.trackOrder.detail.eta.userInfo,
                icon = IconUnify.INFORMATION,
                iconSize = 13.dp,
                showIcon = data.trackOrder.detail.eta.isChanged,
                onIconClicked = { seeEtaChangesInfo(data.trackOrder.detail.eta.userUpdatedInfo) }
            )
            Divider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(eta.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.start)
                    width = Dimension.matchParent
                },
                thickness = 8.dp
            )
        }
    }
}

@Composable
fun TrackingDetailsItemWithIcon(
    modifier: Modifier,
    title: String,
    value: String,
    icon: Int,
    iconSize: Dp,
    showIcon: Boolean,
    valueStyle: TextStyle = NestTheme.typography.heading6.copy(color = NestTheme.colors.NN._950),
    onIconClicked: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (titleLayout, valueLayout, iconLayout) = createRefs()
        NestTypography(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(titleLayout) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            text = title,
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950)
        )
        NestTypography(
            modifier = Modifier
                .constrainAs(valueLayout) {
                    top.linkTo(titleLayout.bottom)
                    start.linkTo(parent.start)
                },
            text = value,
            textStyle = valueStyle
        )
        NestIcon(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(iconSize, iconSize)
                .constrainAs(iconLayout) {
                    start.linkTo(valueLayout.end)
                    top.linkTo(valueLayout.top)
                    bottom.linkTo(valueLayout.bottom)
                    visibility = if (showIcon) Visibility.Visible else Visibility.Gone
                }
                .clickable { onIconClicked() },
            iconId = icon
        )
    }
}

@Composable
fun TrackingDetailsItem(
    modifier: Modifier,
    title: String,
    vararg value: CharSequence
) {
    Column(modifier = modifier) {
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950)
        )
        value.filter { it.isNotEmpty() }.forEach {
            NestTypography(
                modifier = Modifier.fillMaxWidth(),
                text = it,
                textStyle = NestTheme.typography.heading6.copy(color = NestTheme.colors.NN._950)
            )
        }
    }
}

private val DetailModel.deliveryDate: CharSequence
    get() {
        var date: String = sendDate
        if (sendDate.isNotEmpty()) {
            date =
                DateUtil.formatDate(DATE_FORMAT_FROM_BE, DATE_FORMAT_UI, sendDate)
        }
        return date.toHyphenIfEmptyOrNull()
    }

private const val DATE_FORMAT_FROM_BE = "yyyy-MM-dd"
private const val DATE_FORMAT_UI = "dd MMMM yyyy"

@Preview
@Composable
private fun TrackingDetailSectionNormalPreview() {
    val etaModel = EtaModel(
        userInfo = "23 Juli, maks. 00:00 WIB"
    )
    val trackingDataModel = TrackingDataModel(
        trackOrder = TrackOrderModel(
            shippingRefNum = "TKPD-91274021",
            detail = DetailModel(
                shipperCity = "Kota Administrasi Jakarta Timur",
                shipperName = "Tokopedia NOW ",
                receiverCity = "Kota Administrasi Jakarta Timur",
                receiverName = "Fakhira Devina",
                sendDateTime = "2022-07-22 19:07:52",
                sendDate = "2022-07-22",
                sendTime = "19:07:52",
                serviceCode = "GTL",
                eta = etaModel
            )
        )
    )
    val state = TrackingPageState(
        isLoading = false,
        trackingData = trackingDataModel
    )

    TrackingDetailSection(state = state, copyShippingRefNumber = {}, seeEtaChangesInfo = {})
}

@Preview
@Composable
private fun TrackingDetailSectionWithEtaChangedPreview() {
    val etaModel = EtaModel(
        userInfo = "23 Juli, maks. 00:00 WIB",
        isChanged = true
    )
    val trackingDataModel = TrackingDataModel(
        trackOrder = TrackOrderModel(
            shippingRefNum = "TKPD-91274021",
            detail = DetailModel(
                shipperCity = "Kota Administrasi Jakarta Timur",
                shipperName = "Tokopedia NOW ",
                receiverCity = "Kota Administrasi Jakarta Timur",
                receiverName = "Fakhira Devina",
                sendDateTime = "2022-07-22 19:07:52",
                sendDate = "2022-07-22",
                sendTime = "19:07:52",
                serviceCode = "GTL",
                eta = etaModel
            )
        )
    )
    val state = TrackingPageState(
        isLoading = false,
        trackingData = trackingDataModel
    )

    TrackingDetailSection(state = state, copyShippingRefNumber = {}, seeEtaChangesInfo = {})
}
