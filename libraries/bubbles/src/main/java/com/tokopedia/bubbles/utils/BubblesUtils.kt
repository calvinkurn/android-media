package com.tokopedia.bubbles.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tokopedia.media.loader.getBitmapFromUrl
import com.tokopedia.bubbles.R as bubblesR

object BubblesUtils {

    private const val BITMAP_TIMEOUT: Long = 10_000

    fun getBitmap(context: Context,
                  url: String,
                  imageWidth: Int,
                  imageHeight: Int): Bitmap? {
        return try {
            url.getBitmapFromUrl(context, timeout = getBitmapTimeout())
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
            BitmapFactory.decodeResource(context.resources, bubblesR.drawable.default_toped_20_user)
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
