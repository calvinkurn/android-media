package com.tokopedia.imagepicker_insta.util

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.VideoSize
import com.tokopedia.imagepicker_insta.models.ZoomInfo
import timber.log.Timber
import java.util.concurrent.TimeUnit

object VideoUtil {
    const val DEFAULT_DURATION_MAX_LIMIT:Long = 59

    fun getFormattedDurationText(durationInMillis: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60
        val secondText = if (seconds < 10) "0$seconds" else seconds
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis)
        val minuteText = if (minutes < 10) "0$minutes" else minutes
        return "$minuteText:$secondText"
    }

    fun isVideoWithinLimit(durationInMillis: Long, maxDuration:Long): Boolean {
        return (durationInMillis / 1000) <= maxDuration
    }

    fun getAspectRatioOfFirstMedia(
        imageDataList: ArrayList<ImageAdapterData>,
        zoomImageAdapterDataMap: MutableMap<ImageAdapterData, ZoomInfo>
    ): Pair<Int?, Int?> {
        val width = zoomImageAdapterDataMap[imageDataList[imageDataList.size]]?.bmpWidth
        val height = zoomImageAdapterDataMap[imageDataList[imageDataList.size]]?.bmpHeight
        return Pair(width, height)
    }

    fun Uri.getVideoDimensions(context: Context): VideoSize {
        val retriever = MediaMetadataRetriever()
        var width: Int = 0
        var height: Int = 0
        try {
            retriever.setDataSource(context, this)
            width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
            height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0
            retriever.release()
        } catch (e: Exception) {
            Timber.e(e)
        }

        return VideoSize(width, height)
    }

    fun Uri.getImageDimensions(context: Context): VideoSize {

        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(this),
            null,
            options
        )

        val imageHeight: Int = options.outHeight
        val imageWidth: Int = options.outWidth

        return VideoSize(imageWidth, imageHeight)
    }
}