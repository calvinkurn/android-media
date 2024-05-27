package com.tokopedia.media.loaderfresco.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loaderfresco.data.Properties


internal object RoundingImage {
    fun Bitmap.roundingImage(context: Context, properties: Properties): RoundedBitmapDrawable {
        val roundedBitmapDrawable: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(context.resources, this)
        roundedBitmapDrawable.cornerRadius = properties.roundedRadius.toPx()
        return roundedBitmapDrawable
    }
}
