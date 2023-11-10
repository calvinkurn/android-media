package com.ilhamsuaib.darkmodeconfig.view.component

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

@Composable
internal fun GifImageView(
    source: String,
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = {
            ImageUnify(it).apply {
                cornerRadius = 20
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        update = { imageUnify ->
            Glide.with(imageUnify.context)
                .asGif()
                .load(source)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageUnify)
        }
    )
}