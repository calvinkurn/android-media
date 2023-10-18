package com.tokopedia.media.picker.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.*
import androidx.annotation.ChecksSdkIntAtLeast
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.utils.TemporaryHanselable
import com.tokopedia.media.picker.utils.getVideoDuration
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import javax.inject.Inject

class MediaQueryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val param: PickerCacheManager
) : MediaQueryDataSource {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    private val isAboveAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    override fun setupMediaQuery(bucketId: Long, index: Int?, limitSize: Int?): Cursor? {
        val contentUri = buildContentUri()
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

                if (index != null && limitSize != null) {
                    // as we have an offset on recyclerView,
                    // we need to ensure the offset of query have exact threshold.
                    if (index > 0) index + 2

                    putInt(ContentResolver.QUERY_ARG_LIMIT, limitSize)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, index)
                }
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
            setupAdditionalQuery(
                limit = limitSize,
                index = index
            )
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
        return context.getVideoDuration(file)
    }

    /**
     * This query builder to set the sort, limit, and offset for contentResolver's query.
     * To gathering the media data of images nor video locally.
     *
     * @target: Android Q below
     */
    private fun setupAdditionalQuery(index: Int?, limit: Int?) =
        "${MediaStore.Images.Media.DATE_MODIFIED} DESC".let {
            if (limit != null && index != null) {
                // as we have an offset on recyclerView,
                // we need to ensure the offset of query have exact threshold.
                if (index > 0) index + 2

                "$it LIMIT $limit OFFSET $index"
            } else {
                it
            }
        }

    private fun buildContentUri(): Uri {
        val isOnlyOrIncludeVideo = param.get().isOnlyVideoFile() || param.get().isIncludeVideoFile()
        return if (isOnlyOrIncludeVideo) buildFileQuery() else buildImageQuery()
    }

    private fun buildFileQuery(): Uri {
        return if (fileQueryToggle()) {
            if (isAboveAndroidQ) {
                MediaStore.Files.getContentUri((MediaStore.VOLUME_EXTERNAL))
            } else {
                MediaStore.Files.getContentUri(VOLUME_NAME)
            }
        } else {
            MediaStore.Files.getContentUri(VOLUME_NAME)
        }
    }

    private fun buildImageQuery(): Uri {
        return if (imageQueryToggle()) {
            if (isAboveAndroidQ) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    /**
     * This function marked as temporary due as mitigation if something happen in current implementation.
     * We can easily toggle to switch into old media-store query since our bug triage impacted is not high.
     *
     * This toggle only for MediaStore Query's Images.
     */
    @TemporaryHanselable
    private fun imageQueryToggle(): Boolean {
        return true
    }

    /**
     * As we have multiple media-store query to fetching and showing the media data locally,
     * this hansel temporary function to mitigate the FILES query issues and able to revert it back.
     */
    @TemporaryHanselable
    private fun fileQueryToggle(): Boolean {
        return true
    }

    /**
     * - media_type=3 (video only)
     * - media_type=1 or media_type=3 (image and video)
     * - AND bucket_id=[bucketId]
     */
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
        return if (path.isNullOrEmpty()) {
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
    }
}
