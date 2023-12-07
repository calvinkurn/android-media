package com.tokopedia.sellerpersona.view.compose.screen.personaresult

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType

/**
 * Created by @ilhamsuaib on 28/07/23.
 */

@Composable
internal fun ResultLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        NestLoader(
            variant = NestLoaderType.Circular(
                size = NestLoaderSize.Small
            )
        )
    }
}