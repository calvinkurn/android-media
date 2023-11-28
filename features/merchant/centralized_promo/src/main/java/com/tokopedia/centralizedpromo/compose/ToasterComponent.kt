package com.tokopedia.centralizedpromo.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.tokopedia.centralizedpromo.R
import com.tokopedia.centralizedpromo.view.model.CentralizedPromoEvent
import com.tokopedia.centralizedpromo.view.viewmodel.CentralizedPromoComposeViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.delay

@Composable
fun ShowToaster(
    viewModel: CentralizedPromoComposeViewModel
) {
    val showToaster = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.toasterState.collect {
            showToaster.value = it
        }
    }

    if (showToaster.value) {
        ToasterView(viewModel::sendEvent)
        LaunchedEffect(showToaster) {
            delay(5000)
            showToaster.value = false
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

