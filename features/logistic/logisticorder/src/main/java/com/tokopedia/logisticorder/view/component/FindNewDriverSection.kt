package com.tokopedia.logisticorder.view.component

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.uimodel.TrackingPageEvent
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.view.OrderAnalyticsOrderTracking
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import kotlinx.coroutines.delay

@Composable
fun FindNewDriverSection(
    retryAvailability: RetryAvailabilityResponse?,
    onEvent: (TrackingPageEvent) -> Unit
) {
    retryAvailability?.let { model ->
        var clicked by remember { mutableStateOf(false) }

        Column(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            if (model.retryAvailability.showRetryButton && model.retryAvailability.availabilityRetry) {
                OrderAnalyticsOrderTracking.eventViewButtonCariDriver(retryAvailability.retryAvailability.orderId)
                NestButton(
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.GHOST_ALTERNATE,
                    isEnabled = !clicked,
                    text = stringResource(id = R.string.find_new_driver),
                    onClick = {
                        clicked = true
                        OrderAnalyticsOrderTracking.eventClickButtonCariDriver(retryAvailability.retryAvailability.orderId)
                        onEvent(TrackingPageEvent.FindNewDriver)
                    }
                )
            }
            if (model.retryAvailability.availabilityRetry.not() && model.retryAvailability.deadlineRetryUnixtime.toLong() > 0L) {
                val deadline = model.retryAvailability.deadlineRetryUnixtime.toLong()
                val now = System.currentTimeMillis() / 1000L
                val remainingSeconds = deadline - now
                if (remainingSeconds > 0) {
                    OrderAnalyticsOrderTracking.eventViewLabelTungguRetry(
                        DateUtils.formatElapsedTime(now),
                        model.retryAvailability.orderId
                    )
                    var timeInMillis by remember {
                        mutableStateOf(remainingSeconds * 1000)
                    }
                    LaunchedEffect(key1 = timeInMillis) {
                        while (timeInMillis > 0) {
                            delay(1000L)
                            timeInMillis -= 1000L
                            if (timeInMillis < 0) timeInMillis = 0
                        }
                        if (timeInMillis == 0L) {
                            onEvent(TrackingPageEvent.Refresh)
                        }
                    }
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
