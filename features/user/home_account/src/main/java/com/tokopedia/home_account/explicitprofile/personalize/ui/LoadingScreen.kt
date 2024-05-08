package com.tokopedia.home_account.explicitprofile.personalize.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.tokopedia.nest.components.loader.NestLoader
import com.tokopedia.nest.components.loader.NestLoaderSize
import com.tokopedia.nest.components.loader.NestLoaderType

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        NestLoader(variant = NestLoaderType.Circular(NestLoaderSize.Large))
    }
}

@Preview(device = Devices.PIXEL_3A_XL, showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}
