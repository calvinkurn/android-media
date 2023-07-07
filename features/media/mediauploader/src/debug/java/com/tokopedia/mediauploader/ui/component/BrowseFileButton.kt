package com.tokopedia.mediauploader.ui.component

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.picker.common.MediaPicker

@Composable
inline fun BrowseFileButton(
    modifier: Modifier = Modifier,
    crossinline onClick: (Intent) -> Unit
) {
    val context = LocalContext.current

    Button(
        modifier = modifier.padding(6.dp),
        shape = RoundedCornerShape(65.dp),
        colors = ButtonDefaults.buttonColors(
            NestTheme.colors.NN._0
        ),
        onClick = {
            onClick(MediaPicker.intentWithGalleryFirst(context) {
                singleSelectionMode()
                maxVideoFileSize(262144000)
                maxVideoDuration(180000)
                minVideoDuration(1000)
            })
        }
    ) {
        Text(
            text = "Browse",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun BrowseFileButtonPreview() {
    BrowseFileButton(onClick = {})
}
