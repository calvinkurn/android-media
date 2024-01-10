package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.compose.NestIcon
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.ProofModel
import com.tokopedia.logisticorder.uimodel.TrackHistoryModel
import com.tokopedia.logisticorder.uimodel.TrackOrderModel
import com.tokopedia.logisticorder.view.TrackingPageFragment
import com.tokopedia.nest.components.NestImage
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.ImageSource
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.date.DateUtil

@Composable
fun TrackingHistory(
    trackHistory: TrackOrderModel?,
    seeProofOfDelivery: (proof: ProofModel) -> Unit
) {
    trackHistory?.let { model ->
        val list = model.trackHistory
        if (list.isNotEmpty() && !model.invalid && model.orderStatus != TrackingPageFragment.INVALID_ORDER_STATUS && model.change != 0) {
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
        } else {
            EmptyTracking(model)
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
                }
                .clickable { seeProofOfDelivery(trackHistoryModel.proof) },
            source = ImageSource.Remote(
                trackHistoryModel.proof.imageUrl,
                customHeaders = mapOf("Accounts-Authorization" to "Bearer ${trackHistoryModel.proof.accessToken}")
            )
        )
    }
}

@Composable
fun EmptyTracking(history: TrackOrderModel) {
    ConstraintLayout(modifier = Modifier.padding(16.dp)) {
        val (icon, description, invalidTrackingNotes) = createRefs()
        NestImage(
            modifier = Modifier.constrainAs(icon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            source = ImageSource.Painter(R.drawable.info)
        )
        NestTypography(
            modifier = Modifier.constrainAs(description) {
                top.linkTo(icon.top)
                start.linkTo(icon.end, margin = 4.dp)
            },
            text = history.emptyTrackingTitle
        )
        if (history.invalid) {
            InvalidTrackingNotes(
                Modifier.constrainAs(invalidTrackingNotes) {
                    top.linkTo(description.bottom)
                    start.linkTo(parent.start)
                }
            )
        }
    }
}

@Composable
private fun InvalidTrackingNotes(modifier: Modifier) {
    Column(modifier) {
        InvalidTrackingNotesItem(text = stringResource(id = R.string.empty_notes_1))
        InvalidTrackingNotesItem(text = stringResource(id = R.string.empty_notes_2))
        InvalidTrackingNotesItem(text = stringResource(id = R.string.empty_notes_3))
    }
}

@Composable
private fun InvalidTrackingNotesItem(text: String) {
    Row(Modifier.padding(top = 8.dp)) {
        NestImage(source = ImageSource.Painter(source = R.drawable.circle_border_logistic))
        NestTypography(
            text = text
        )
    }
}

private val TrackOrderModel.emptyTrackingTitle: String
    @Composable get() {
        return if (invalid) {
            stringResource(id = R.string.warning_courier_invalid)
        } else if (orderStatus == TrackingPageFragment.INVALID_ORDER_STATUS || change == 0 || trackHistory.isEmpty()) {
            stringResource(id = R.string.warning_no_courier_change)
        } else {
            ""
        }
    }
