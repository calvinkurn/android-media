package com.tokopedia.logisticorder.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tokopedia.logisticorder.R
import com.tokopedia.nest.components.NestButton

@Composable
fun LiveTrackingSection(trackingUrl: String?, openWebview: (url: String) -> Unit) {
    trackingUrl?.takeIf { it.isNotEmpty() }?.run {
        NestButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = stringResource(id = R.string.label_live_tracking),
            onClick = { openWebview(trackingUrl) }
        )
    }
}
