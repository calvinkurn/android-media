package com.tokopedia.media.picker.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.*
import androidx.annotation.ChecksSdkIntAtLeast
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import javax.inject.Inject

class MediaQueryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val param: ParamCacheManager
) : MediaQueryDataSource {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    private val isAboveAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    override fun setupMediaQuery(bucketId: Long, offset: Int?): Cursor? {
        val contentUri = buildContentUri(offset)
        val selection = buildBucketSelection(bucketId)
        val projections = projections().values.toTypedArray()

        if (isAboveAndroidQ) {
            val args = Bundle().apply {
                putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(DATE_MODIFIED))
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }

            return context.contentResolver.query(
                contentUri,
                projections,
                args,
                null
            )
        }

        return context.contentResolver.query(
            contentUri,
            projections,
            selection,
            null,
            setupSortAndLimitQuery(offset)
        )
    }

    override fun createMedia(cursor: Cursor): Media? {
        with(cursor) {
            val path = getString(columnIndex("data"))

            val file = makeSafeFile(path) ?: return null

            if (param.get().excludeMedias().contains(file)) return null
            if (!param.get().isIncludeGifFile() && file.isGif()) return null

            val id = getLong(columnIndex("id"))
            val name = getString(columnIndex("name"))

            if (name != null) {
                return Media(id, file)
            }

            return null
        }
    }

    override fun getVideoDuration(file: PickerFile): Int {
        if (file.isVideo().not()) return 0

        return try {
            with(MediaMetadataRetriever()) {
                setDataSource(context, Uri.fromFile(file))

                val durationData = extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_DURATION
                )

                release()
                durationData.toIntOrZero()
            }
        } catch (e: Throwable) {
            0
        }
    }

    private fun setupSortAndLimitQuery(offset: Int?) =
        "${MediaStore.Images.Media.DATE_MODIFIED} DESC".let {
            if (offset != null) {
                "$it LIMIT $offset"
            } else {
                it
            }
        }

    private fun buildContentUri(offset: Int?): Uri {
        val mediaStoreUri = if (param.get().isOnlyVideoFile() || param.get().isIncludeVideoFile()) {
            MediaStore.Files.getContentUri(VOLUME_NAME)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        if (offset != null && isAboveAndroidQ) {
            return mediaStoreUri.buildUpon()
                .appendQueryParameter(QUERY_MEDIA_LIMIT, offset.toString())
                .build()
        }

        return mediaStoreUri
    }

    private fun buildBucketSelection(bucketId: Long): String {
        // set filter if the param needs to fetch a video media type
        var selection = when {
            param.get().isOnlyVideoFile() -> "($MEDIA_TYPE=$MEDIA_TYPE_VIDEO)"
            param.get().isIncludeVideoFile() -> "($MEDIA_TYPE=$MEDIA_TYPE_IMAGE or $MEDIA_TYPE=$MEDIA_TYPE_VIDEO)"
            else -> ""
        }

        // set the bucketId if any
        if (bucketId != BUCKET_ALL_MEDIA_ID) {
            // add "AND" as query concat operator
            if (selection.isNotEmpty()) selection += " AND "

            // add bucketId
            selection += "${MediaStore.Images.Media.BUCKET_ID}=$bucketId"
        }

        return selection.trim()
    }

    private fun makeSafeFile(path: String?): PickerFile? {
        return if (path == null || path.isEmpty()) {
            null
        } else try {
            path.asPickerFile()
        } catch (ignored: Exception) {
            null
        }
    }

    companion object {
        const val BUCKET_ALL_MEDIA_ID = -1L

        private const val VOLUME_NAME = "external"
        private const val QUERY_MEDIA_LIMIT = "limit"
    }
}
