package com.tokopedia.sellerpersona.view.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType
import com.tokopedia.sellerpersona.view.compose.model.ResultUiEvent
import com.tokopedia.sellerpersona.view.compose.model.UiState
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel

/**
 * Created by @ilhamsuaib on 12/07/23.
 */

@Composable
internal fun ResultLoadingState() {
    NestLoader(
        variant = NestLoaderType.Circular(
            size = NestLoaderSize.Small
        )
    )
}

@Composable
internal fun PersonaResultScreen(
    state: UiState<PersonaDataUiModel>,
    onEvent: (ResultUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

    }
}