package com.tokopedia.bubbles.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.config.GlobalConfig
import java.util.concurrent.TimeUnit

object BubblesUtils {

    private const val BITMAP_TIMEOUT: Long = 3

    fun getBitmap(context: Context,
                  url: String,
                  imageWidth: Int,
                  imageHeight: Int): Bitmap {
        return try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .submit(imageWidth, imageHeight)
                .get(BITMAP_TIMEOUT, TimeUnit.SECONDS)
        } catch (e: Exception) {
            BitmapFactory.decodeResource(context.resources, GlobalConfig.LAUNCHER_ICON_RES_ID)
        }
    }

}
