package com.tokopedia.shop.home.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.autocompletecomponent.R

internal fun ImageView.loadImageRounded(
    url: String,
    roundingRadius: Int = context.resources.getDimensionPixelSize(R.dimen.dp_8)
) {
    Glide.with(context)
        .load(url)
        .transform(CenterCrop(), RoundedCorners(roundingRadius))
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)
}
