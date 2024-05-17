package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.logisticorder.uimodel.ProofModel
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.tag
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.logisticorder.R as logisticorderR

@Composable
fun TrackingHistory(
    trackHistory: TrackOrderModel?,
    seeProofOfDelivery: (proof: ProofModel) -> Unit
) {
    trackHistory?.let { model ->
        val list = model.trackHistory
        if (list.isNotEmpty() && !model.invalid && model.change != 0) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                repeat(list.size) { index ->
                    TrackingHistoryItem(
                        list[index],
                        index == 0,
                        index == list.size - 1,
                        seeProofOfDelivery
                    )
                }
            }
        }
    }
}

@Composable
fun TrackingHistoryItem(
    trackHistoryModel: TrackHistoryModel,
    isFirst: Boolean,
    isLast: Boolean,
    seeProofOfDelivery: (proof: ProofModel) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (day, time, description, courier, circle, line, pod, endSpacing) = createRefs()
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
                .constrainAs(day) {
                    start.linkTo(circle.end, margin = 8.dp)
                    end.linkTo(time.start)
                    top.linkTo(circle.top)
                    bottom.linkTo(circle.bottom)
                    width = Dimension.fillToConstraints
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
                .constrainAs(description) {
                    end.linkTo(parent.end)
                    start.linkTo(day.start)
                    top.linkTo(day.bottom, margin = 5.dp)
                    width = Dimension.fillToConstraints
                }
                .tag("tracking_history"),
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
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    top.linkTo(description.bottom, margin = 5.dp)
                    visibility =
                        if (trackHistoryModel.partnerName.isNotEmpty()) Visibility.Visible else Visibility.Gone
                },
            textStyle = NestTheme.typography.body3.copy(color = NestTheme.colors.NN._950),
            text = "Kurir: ${trackHistoryModel.partnerName}"
        )
        Spacer(
            modifier = Modifier
                .height(4.dp)
                .constrainAs(endSpacing) {
                    top.linkTo(pod.bottom)
                    start.linkTo(day.start)
                }
        )
        Box(
            Modifier
                .constrainAs(line) {
                    top.linkTo(circle.bottom)
                    start.linkTo(circle.start)
                    end.linkTo(circle.end)
                    bottom.linkTo(endSpacing.bottom)
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
                    top.linkTo(courier.bottom, margin = 4.dp, goneMargin = 4.dp)
                    start.linkTo(description.start)
                    visibility =
                        if (trackHistoryModel.proof.imageId.isNotEmpty()) Visibility.Visible else Visibility.Gone
                }
                .tag("img_proof")
                .clickable { seeProofOfDelivery(trackHistoryModel.proof) },
            source = ImageSource.Remote(
                trackHistoryModel.proof.imageUrl,
                customHeaders = mapOf("Accounts-Authorization" to "Bearer ${trackHistoryModel.proof.accessToken}")
            )
        )
    }
}


@Preview
@Composable
private fun TrackingHistoryNormalPreview() {
    val data = TrackOrderModel(
        trackHistory = listOf(
            TrackHistoryModel(
                dateTime = "2021-11-12 22:38:55",
                date = "2021-11-12",
                status = "Kurir Toped sudah ditugaskan dan pesananan akan segera diantar ke pembeli",
                city = "Bandung",
                time = "22:38:55",
                partnerName = "JNT",
                proof = ProofModel(imageId = "bbb")
            ),
            TrackHistoryModel(
                dateTime = "2021-11-12 22:38:55",
                date = "2021-11-12",
                status = "Pesanan sampai di sorting center JAKARTA SELATAN HUB  ",
                city = "Bandung",
                time = "22:38:55",
                partnerName = "JNT"
            ),
            TrackHistoryModel(
                dateTime = "2021-11-10 22:38:55",
                date = "2021-11-10",
                status = "Pesanan dalam perjalanan",
                city = "Surabaya",
                time = "22:38:55"
            )
        )
    )

    NestTheme {
        TrackingHistory(trackHistory = data, seeProofOfDelivery = {})
    }
}

