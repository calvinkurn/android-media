package com.tokopedia.autocompletecomponent.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.media.loader.loadImage

internal fun ImageView.loadImageRounded(
    url: String,
    roundingRadius: Int = context.resources.getDimensionPixelSize(R.dimen.dp_6),
){
    this.loadImage(url) {
        transforms(listOf(CenterCrop(), RoundedCorners(roundingRadius)))
    }
}
