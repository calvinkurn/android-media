package com.tokopedia.mediauploader.ui.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.tokopedia.mediauploader.DebugMediaLoaderEvent
import com.tokopedia.mediauploader.DebugMediaUploaderViewModelContract
import com.tokopedia.picker.common.MediaPicker

@Composable
fun launchMediaPicker(
    viewModel: DebugMediaUploaderViewModelContract,
    content: () -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
) {
    if (it.data != null) {
        val result = MediaPicker.result(it.data)

        if (result.originalPaths.isNotEmpty()) {
            viewModel.setAction(DebugMediaLoaderEvent.FileChosen(
                result.originalPaths.first()
            ))
        }

        content()
    }
}
