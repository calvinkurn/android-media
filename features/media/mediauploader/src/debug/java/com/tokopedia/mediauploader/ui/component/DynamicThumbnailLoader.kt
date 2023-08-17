package com.tokopedia.mediauploader.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile

@Composable
@Suppress("OPT_IN_USAGE")
fun LoadImage(file: String) {
    val context = LocalContext.current
    val isVideoFile = file.asPickerFile().isVideo()

    AsyncImage(
        file,
        "local image",
        ImageLoader.Builder(context).components {
            add(VideoFrameDecoder.Factory())
        }.build(),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    )
}

@Preview
@Composable
fun LoadImagePreview() {
    LoadImage(file = "")
}
