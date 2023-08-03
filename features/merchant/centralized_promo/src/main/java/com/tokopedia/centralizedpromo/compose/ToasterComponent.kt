package com.tokopedia.centralizedpromo.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.tokopedia.centralizedpromo.R
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.delay

@Composable
fun showToaster(
    showToaster: Boolean,
    sendEvent: (CentralizedPromoEvent) -> Unit = {}
) {
    if (showToaster) {
        ToasterView(sendEvent)

        LaunchedEffect(Unit) {
            delay(5000)
            // reset after 5s so if the user try to show toaster while toaster is showing
            // it will ignored
            sendEvent(
                CentralizedPromoEvent
                    .UpdateToasterState(false)
            )
        }
    }
}

@Composable
private fun ToasterView(sendEvent: (CentralizedPromoEvent) -> Unit) {
    val view = LocalView.current
    val context = LocalContext.current

    Toaster.build(
        view,
        context.getString(R.string.centralized_promo_failed_to_get_information),
        5000,
        Toaster.TYPE_ERROR,
        context.getString(R.string.centralized_promo_reload),
        clickListener = {
            sendEvent.invoke(CentralizedPromoEvent.SwipeRefresh)
        }
    ).show()
}

