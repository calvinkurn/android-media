package com.tokopedia.mediauploader.ui.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.tokopedia.picker.common.MediaPicker

@Composable
fun launchMediaPicker(
    fileChosen: (String) -> Unit,
    browsed: (Boolean) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
) {
    if (it.data != null) {
        val result = MediaPicker.result(it.data)

        if (result.originalPaths.isNotEmpty()) {
            fileChosen(result.originalPaths.first())
            browsed(true)
        }

        browsed(false)
    }
}
