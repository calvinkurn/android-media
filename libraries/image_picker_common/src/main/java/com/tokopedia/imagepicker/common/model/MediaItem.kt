package com.tokopedia.imagepicker.common.model

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.tokopedia.imagepicker.common.PhotoMetadataUtils.getPath
import com.tokopedia.utils.image.ImageProcessingUtil


class MediaItem(val id: Long,
                @Deprecated("should use content Uri instead")
                val path: String,
                val mimeType: String,
                val size: Long,
                val duration: Long,
                val videoResolution: String?,
                var _width: Long = 0,
                var _height: Long = 0) {
    val contentUri: Uri

    fun getWidth(context: Context):Long{
        calculateWidthAndHeight(context);
        return _width;
    }

    fun getHeight(context: Context):Long{
        calculateWidthAndHeight(context);
        return _height;
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun calculateWidthAndHeight(context: Context) {
        if (_width == 0L || _height == 0L) {
            val widthHeight: Pair<Int, Int> = getWidthAndHeight(context)
            _width = widthHeight.first.toLong()
            _height = widthHeight.second.toLong()
        }
    }

    private fun getWidthAndHeight(context: Context): Pair<Int, Int> {
        var widthHeight = ImageProcessingUtil.getWidthAndHeight(contentUri.path.toString())
        if (widthHeight.first == 0 || widthHeight.second == 0) {
            val imagePath = getPath(context.contentResolver, contentUri)
            widthHeight = ImageProcessingUtil.getWidthAndHeight(imagePath)
        }
        return widthHeight
    }

    val minimumVideoResolution: Int
        get() {
            val resolution = videoResolution
            if (resolution == null || resolution.isEmpty()) return 0
            val minResolution = resolution.split(X.toRegex()).toTypedArray()
            return if (minResolution.size == 2) {
                try {
                    Math.min(minResolution[0].toInt(), minResolution[1].toInt())
                } catch (e: Exception) {
                    0
                }
            } else {
                0
            }
        }

    val isImage: Boolean
        get() = mimeType == MimeType.JPEG.toString() || mimeType == MimeType.PNG.toString() || mimeType == MimeType.GIF.toString() || mimeType == MimeType.BMP.toString() || mimeType == MimeType.WEBP.toString()

    val isGif: Boolean
        get() = mimeType == MimeType.GIF.toString()

    val isVideo: Boolean
        get() = mimeType == MimeType.MPEG.toString() || mimeType == MimeType.MP4.toString() || mimeType == MimeType.QUICKTIME.toString() || mimeType == MimeType.THREEGPP.toString() || mimeType == MimeType.THREEGPP2.toString() || mimeType == MimeType.MKV.toString() || mimeType == MimeType.WEBM.toString() || mimeType == MimeType.TS.toString() || mimeType == MimeType.AVI.toString()

    companion object {
        const val X = "x"

        @JvmStatic
        fun valueOf(cursor: Cursor): MediaItem {
            val resolution = cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION)
            val mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE))
            var videoDuration: Long = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                videoDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            }
            return MediaItem(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)),
                    mimeType ?: "",
                    cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                    videoDuration,
                    if (resolution > 0) cursor.getString(resolution) else "",
                    cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)),
                    cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT))
            )
        }
    }

    init {
        this.contentUri = ContentUris.withAppendedId(when {
            isImage -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            isVideo -> {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            else -> {
                MediaStore.Files.getContentUri("external")
            }
        }, id)
    }
}