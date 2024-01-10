package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        Column(
            Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NestTypography(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.driver_section_tracking_title),
                textStyle = NestTheme.typography.display2.copy(fontWeight = FontWeight.Bold)
            )
            DriverInfoWidget(
                Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp),
                it.lastDriver,
                callDriver,
                openTippingInfo
            )
            TippingDriverWidget(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp),
                it.tipping,
                onClickTippingButton
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 8.dp
            )
        }
    }
}

private val TrackingDataModel?.hasDriverInfo: Boolean
    get() {
        return this?.run { tipping.eligibleForTipping || lastDriver.name.isNotEmpty() } ?: false
    }
