package com.tokopedia.sellerpersona.view.compose.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tokopedia.nest.components.ButtonSize
import com.tokopedia.nest.components.ButtonVariant
import com.tokopedia.nest.components.NestButton
import com.tokopedia.nest.principles.NestTypography
import com.tokopedia.nest.principles.ui.NestTheme


/**
 * Created by @ilhamsuaib on 13/07/23.
 */

@Composable
internal fun ErrorStateComponent(
    actionText: String,
    title: String,
    description: String = "",
    onActionClicked: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection),
            contentDescription = null,
            modifier = Modifier
                .requiredWidth(184.dp)
                .requiredHeight(144.dp)
        )
        Spacer(
            modifier = Modifier
                .requiredHeight(16.dp)
                .fillMaxWidth()
        )
        NestTypography(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textStyle = NestTheme.typography.heading2.copy(
                color = NestTheme.colors.NN._950, textAlign = TextAlign.Center
            )
        )
        if (description.isNotBlank()) {
            Spacer(
                modifier = Modifier
                    .requiredHeight(8.dp)
                    .fillMaxWidth()
            )
            NestTypography(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                textStyle = NestTheme.typography.paragraph2.copy(
                    color = NestTheme.colors.NN._600, textAlign = TextAlign.Center
                )
            )
        }
        Spacer(
            modifier = Modifier
                .requiredHeight(24.dp)
                .fillMaxWidth()
        )
        NestButton(modifier = Modifier.wrapContentSize(),
            text = actionText,
            variant = ButtonVariant.FILLED,
            size = ButtonSize.SMALL,
            onClick = {
                onActionClicked?.invoke()
            })
    }
}

@Preview
@Composable
fun ErrorStateComponentPreview() {
    ErrorStateComponent(
        actionText = "Muat Ulang", title = "Oops, informasi gagal ditampilkan"
    )
}