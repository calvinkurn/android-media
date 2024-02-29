package com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

@Composable
internal fun CampaignTimer(
    modifier: Modifier = Modifier,
    endTimeMs: Long,
    onFinish: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TimerUnifySingle(context).apply {
                timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE_ALTERNATE
                timerFormat = TimerUnifySingle.FORMAT_AUTO
                this.onFinish = onFinish
                this.isShowClockIcon = false

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = endTimeMs
                this.targetDate = calendar
            }
        }
    )
}
