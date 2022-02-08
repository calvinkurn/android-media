package com.tokopedia.media.picker.data.loader

import android.annotation.SuppressLint
import android.content.ContentResolver.*
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.*
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.common.PickerParam
import com.tokopedia.media.picker.utils.isGifFormat
import java.io.File

open class LoaderDataSource(private val context: Context) {

    @SuppressLint("AnnotateVersionCheck")
    private val isNewApi = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.BUCKET_ID,
    )

    @SuppressLint("InlinedApi")
    fun query(param: PickerParam, bucketId: Long, limit: Int? = null): Cursor? {
        val sourceUri = if (limit != null && isNewApi) {
            sourceUri(param).buildUpon()
                .appendQueryParameter(QUERY_LIMIT, limit.toString())
                .build()
        } else {
            sourceUri(param)
        }

        val selection = selection(param, bucketId)

        if (isNewApi) {
            val args = Bundle().apply {
                putStringArray(QUERY_ARG_SORT_COLUMNS, arrayOf(DATE_MODIFIED))
                putInt(QUERY_ARG_SORT_DIRECTION, QUERY_SORT_DIRECTION_DESCENDING)
                putString(QUERY_ARG_SQL_SELECTION, selection)
                if (limit != null) putInt(QUERY_ARG_LIMIT, limit)
            }

            return context.contentResolver.query(sourceUri, projection, args, null)
        }

        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC".let {
            if (limit != null) "$it LIMIT $limit" else it
        }

        return context.contentResolver.query(sourceUri, projection, selection, null, sortOrder)
    }

    fun medias(cursor: Cursor, param: PickerParam): Media? {
        val path = cursor.getString(cursor.getColumnIndex(projection[2]))
        val file = makeSafeFile(path) ?: return null

        if (param.excludeMedias().contains(file)) return null

        // Exclude GIF when we don't want it
        if (!param.isIncludeGifFile()) {
            if (isGifFormat(path)) return null
        }

        val id = cursor.getLong(cursor.getColumnIndex(projection[0]))
        val name = cursor.getString(cursor.getColumnIndex(projection[1]))

        if (name != null) {
            return Media(id, name, path)
        }

        return null
    }

    private fun selection(param: PickerParam, bucketId: Long): String {
        var selection = when {
            param.isOnlyVideoFile() -> "($MEDIA_TYPE=$MEDIA_TYPE_VIDEO)"
            param.isIncludeVideoFile() -> "($MEDIA_TYPE=$MEDIA_TYPE_IMAGE OR $MEDIA_TYPE=$MEDIA_TYPE_VIDEO)"
            else -> ""
        }

        if (bucketId != -1L) {
            selection += " ${
                if (selection.isNotEmpty()) "AND" else ""
            } ${MediaStore.Images.Media.BUCKET_ID}=$bucketId"
        }

        return selection.trim()
    }

    private fun sourceUri(param: PickerParam): Uri {
        return if (param.isOnlyVideoFile() || param.isIncludeVideoFile()) {
            MediaStore.Files.getContentUri(VOLUME_NAME)
        } else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    protected fun Cursor.getLong(key: String): Long {
        return getLong(getColumnIndex(key))
    }

    protected fun Cursor.getString(key: String): String? {
        return getString(getColumnIndex(key))
    }

    companion object {
        private const val VOLUME_NAME = "external"
        private const val QUERY_LIMIT = "limit"

        private fun makeSafeFile(path: String?): File? {
            return if (path == null || path.isEmpty()) {
                null
            } else try {
                File(path)
            } catch (ignored: Exception) {
                null
            }
        }
    }

}