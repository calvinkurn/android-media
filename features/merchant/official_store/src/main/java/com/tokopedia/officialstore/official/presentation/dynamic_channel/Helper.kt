@file:JvmName("Helper")
package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.AppCompatImageView
import com.bumptech.glide.request.target.BitmapImageViewTarget
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

internal object OfficialStoreImageHelper {
    fun getRoundedImageViewTarget(
            imageView: AppCompatImageView,
            cornerRadius: Float
    ): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap?) {
                val roundedBitmap = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                roundedBitmap.cornerRadius = cornerRadius

                imageView.setImageDrawable(roundedBitmap)
            }
        }
    }
}

internal object OfficialStoreDateHelper {
    fun getExpiredTime(expiredTimeString: String): Date {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ").parse(expiredTimeString)
        } catch (e: ParseException) {
            e.printStackTrace()
            Date()
        }
    }

    fun isTimeExpired(currentServerTime: Long, expiredTime: Date): Boolean {
        val serverTime = Date(System.currentTimeMillis())
        serverTime.time = serverTime.time + currentServerTime

        return serverTime.after(expiredTime)
    }
}