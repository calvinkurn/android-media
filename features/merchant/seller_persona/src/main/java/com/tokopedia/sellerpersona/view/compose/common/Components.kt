package com.tokopedia.sellerpersona.view.compose.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.tokopedia.globalerror.GlobalError


/**
 * Created by @ilhamsuaib on 13/07/23.
 */

@Composable
internal fun ErrorStateComponent(
    actionText: String,
    type: Int = GlobalError.NO_CONNECTION,
    onActionClicked: (() -> Unit)? = null
) {
    val attributeSet = remember { getAttributeSet() }
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        factory = { context ->
            val globalError = GlobalError(context, attributeSet)
            globalError.errorAction.text = actionText
            globalError.setType(type)
            return@AndroidView globalError
        },
        update = {
            it.setActionClickListener {
                onActionClicked?.invoke()
            }
        }
    )
}

@Preview
@Composable
fun ErrorStateComponentPreview() {
    ErrorStateComponent(
        actionText = "Muat Ulang",
        type = GlobalError.NO_CONNECTION
    )
}