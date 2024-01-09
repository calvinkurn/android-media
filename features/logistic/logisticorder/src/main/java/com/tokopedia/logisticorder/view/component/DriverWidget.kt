package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.TippingModel
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme

@Composable
fun DriverWidget(
    trackingDataModel: TrackingDataModel?,
    callDriver: (phoneNumber: String) -> Unit,
    openTippingInfo: () -> Unit,
    onClickTippingButton: (tipping: TippingModel) -> Unit
) {
    trackingDataModel?.takeIf { it.hasDriverInfo }?.let {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
        ) {
            val (title, driverInfo, tipping, divider) = createRefs()
            val startGuideline = createGuidelineFromStart(20.dp)
            val endGuideline = createGuidelineFromEnd(20.dp)
            NestTypography(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline)
                },
                text = stringResource(id = R.string.driver_section_tracking_title),
                textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold)
            )
            DriverInfoWidget(
                modifier = Modifier.constrainAs(driverInfo) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                },
                it.lastDriver,
                callDriver,
                openTippingInfo
            )
            TippingDriverWidget(
                modifier = Modifier.constrainAs(tipping) {
                    top.linkTo(driverInfo.bottom, margin = 16.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                },
                it.tipping,
                onClickTippingButton
            )
            Divider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(tipping.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                thickness = 8.dp
            )
        }
    }
}

private val TrackingDataModel?.hasDriverInfo: Boolean
    get() {
        return this?.run { tipping.eligibleForTipping || lastDriver.name.isNotEmpty() } ?: false
    }
