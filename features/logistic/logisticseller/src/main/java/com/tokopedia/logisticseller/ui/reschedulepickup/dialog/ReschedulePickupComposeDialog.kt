package com.tokopedia.logisticseller.ui.reschedulepickup.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tokopedia.common_compose.principles.NestButton
import com.tokopedia.common_compose.principles.NestTypography
import com.tokopedia.common_compose.ui.NestTheme
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.SaveRescheduleModel

@Composable
fun RescheduleResultDialog(
    saveRescheduleModel: SaveRescheduleModel?,
    onClickDialogButton: (Boolean) -> Unit,
    onCloseDialog: (Boolean) -> Unit
) {
    saveRescheduleModel?.takeIf { it.openDialog }?.let {
        if (it.success) {
            SuccessDialog(
                etaPickup = it.etaPickup,
                onDialogDismiss = { onCloseDialog(it.success) }
            ) {
                onClickDialogButton(it.success)
            }
        } else {
            FailedDialog(error = it.message, onDialogDismiss = { onCloseDialog(it.success) }) {
                onClickDialogButton(it.success)
            }
        }
    }
}

@Composable
private fun ResultDialog(
    title: String,
    subtitle: String,
    imageId: Int,
    buttonText: String,
    onDismiss: () -> Unit,
    onDialogButtonClicked: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = imageId),
                    contentDescription = "result reschedule pickup"
                )
                NestTypography(
                    text = title,
                    textStyle = NestTheme.typography.heading2.copy(NestTheme.colors.NN._950),
                    textAlign = TextAlign.Center
                )
                NestTypography(
                    text = subtitle,
                    textStyle = NestTheme.typography.body2.copy(NestTheme.colors.NN._950),
                    textAlign = TextAlign.Center
                )
                NestButton(text = buttonText) {
                    onDialogButtonClicked()
                }
            }
        }
    }
}

@Composable
private fun SuccessDialog(
    etaPickup: String,
    onDialogDismiss: () -> Unit,
    onDialogButtonClicked: () -> Unit
) {
    ResultDialog(
        title = stringResource(id = R.string.title_reschedule_pickup_success_dialog),
        subtitle = stringResource(id = R.string.template_success_reschedule_pickup, etaPickup),
        imageId = R.drawable.ic_logisticseller_recshedulepickup_success,
        buttonText = stringResource(id = R.string.title_reschedule_pickup_button_dialog),
        onDismiss = onDialogDismiss
    ) {
        onDialogButtonClicked()
    }
}

@Composable
private fun FailedDialog(
    error: String,
    onDialogDismiss: () -> Unit,
    onDialogButtonClicked: () -> Unit
) {
    ResultDialog(
        title = stringResource(id = R.string.title_failed_reschedule_pickup_dialog),
        subtitle = error,
        imageId = R.drawable.ic_logisticseller_reschedulepickup_fail,
        buttonText = stringResource(id = R.string.title_cta_error_reschedule_pickup),
        onDismiss = onDialogDismiss
    ) {
        onDialogButtonClicked()
    }
}
