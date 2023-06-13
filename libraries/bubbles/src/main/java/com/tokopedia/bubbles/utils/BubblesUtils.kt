package com.tokopedia.bubbles.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.concurrent.TimeUnit

object BubblesUtils {

    private const val BITMAP_TIMEOUT: Long = 10

    fun getBitmap(context: Context,
                  url: String,
                  imageWidth: Int,
                  imageHeight: Int): Bitmap? {
        return try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .submit(imageWidth, imageHeight)
                .get(getBitmapTimeout(), TimeUnit.SECONDS)
        } catch (e: Exception) {
            getBitmapWhenError(context, imageWidth, imageHeight)
        }
    }

    private fun getBitmapTimeout(): Long {
        return BITMAP_TIMEOUT
    }

    private fun getBitmapWhenError(context: Context, imageWidth: Int, imageHeight: Int): Bitmap? {
        return getAlternateBitmap(context) ?: getEmptyBitmap(imageWidth, imageHeight)
    }

    private fun getAlternateBitmap(context: Context): Bitmap? {
        return try {
            BitmapFactory.decodeResource(context.resources, com.tokopedia.bubbles.R.drawable.default_toped_20_user)
        } catch (ex: Exception) {
            null
        }
    }

    private fun getEmptyBitmap(imageWidth: Int, imageHeight: Int): Bitmap? {
        return try {
            Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
        } catch (ex: Exception) {
            null
        }
    }

}
