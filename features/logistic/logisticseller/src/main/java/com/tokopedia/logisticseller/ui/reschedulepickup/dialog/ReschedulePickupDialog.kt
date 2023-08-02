package com.tokopedia.logisticseller.ui.reschedulepickup.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.model.SaveRescheduleModel
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.nest.principles.utils.toAnnotatedString
import com.tokopedia.unifycomponents.HtmlLinkHelper

@Composable
fun RescheduleResultDialog(
    saveRescheduleModel: SaveRescheduleModel?,
    onCloseDialog: (Boolean) -> Unit
) {
    saveRescheduleModel?.takeIf { it.openDialog }?.let {
        if (it.success) {
            SuccessDialog(
                etaPickup = it.etaPickup,
                onDialogDismiss = { onCloseDialog(it.success) }
            ) {
                onCloseDialog(it.success)
            }
        } else {
            FailedDialog(error = it.message, onDialogDismiss = { onCloseDialog(it.success) }) {
                onCloseDialog(it.success)
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
        Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(id = imageId),
                    modifier = Modifier.size(80.dp).padding(vertical = 8.dp),
                    contentDescription = "result reschedule pickup"
                )
                NestTypography(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = title,
                    textStyle = NestTheme.typography.heading2.copy(
                        NestTheme.colors.NN._950,
                        textAlign = TextAlign.Center
                    )
                )
                HtmlLinkHelper(
                    LocalContext.current,
                    subtitle
                ).spannedString?.toAnnotatedString()?.run {
                    NestTypography(
                        text = this,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textStyle = NestTheme.typography.body2.copy(
                            NestTheme.colors.NN._950,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                NestButton(text = buttonText, {
                    onDialogButtonClicked()
                }, modifier = Modifier.padding(top = 16.dp))
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
