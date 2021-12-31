package com.tokopedia.picker.data.loader

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.tokopedia.picker.ui.PickerParam

class AlbumDataSource {

    private val columns = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        COLUMN_URI,
        COLUMN_COUNT,
    )

    private val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
        "COUNT(*) AS $COLUMN_COUNT",
    )

    private val projection29 = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.MIME_TYPE,
    )

    private val selectionNoMediaQuery = """
        ${MediaStore.MediaColumns.SIZE} > 0 AND 
        ${MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME} NOT LIKE '${APP_DIRECTORY_PREFIX} %')
        GROUP BY (bucket_id
    """.trimIndent()

    private val selectionQuery = """
        (${MediaStore.Files.FileColumns.MEDIA_TYPE} = %s OR 
        ${MediaStore.Files.FileColumns.MEDIA_TYPE} = %s) AND 
        $selectionNoMediaQuery
    """.trimIndent()

    private val selection29NoMediaQuery = """
        ${MediaStore.MediaColumns.SIZE} > 0
    """.trimIndent()

    private val selection29Query = """
        (${MediaStore.Files.FileColumns.MEDIA_TYPE} = %s OR 
        ${MediaStore.Files.FileColumns.MEDIA_TYPE} = %s) AND 
        $selection29NoMediaQuery
    """.trimIndent()

    private val selectionForSingleMediaTypeQuery = """
        ${MediaStore.Files.FileColumns.MEDIA_TYPE} = %s AND 
        $selectionNoMediaQuery
    """.trimIndent()

    private val selectionForSingleMediaType29NoMediaQuery = """
        ${MediaStore.MediaColumns.SIZE} > 0 AND 
        ${MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME} NOT LIKE '${APP_DIRECTORY_PREFIX} %' 
    """.trimIndent()

    private val selectionForSingleMediaType29Query = """
        ${MediaStore.Files.FileColumns.MEDIA_TYPE} = %s AND 
        $selectionForSingleMediaType29NoMediaQuery  
    """.trimIndent()

    private val selectionArgs = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    private val bucketOrderBy = "datetaken DESC"

    fun instance(context: Context, param: PickerParam): Cursor? {
        val selection = if (param.isOnlyVideo) {
            if (android10Above()) {
                selectionForSingleMediaType29Query.format(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                )
            } else {
                selectionForSingleMediaTypeQuery.format(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                )
            }
        } else if (!param.isOnlyVideo && !param.isIncludeVideo) {
            if (android10Above()) {
                selectionForSingleMediaType29Query.format(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                )
            } else {
                selectionForSingleMediaTypeQuery.format(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                )
            }
        } else {
            if (android10Above()) {
                selection29Query.format(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                )
            } else {
                selectionQuery.format(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                )
            }
        }

        return query(
            context = context,
            param = param,
            selection = selection
        )
    }

    private fun query(
        context: Context,
        param: PickerParam,
        selection: String
    ): Cursor? {
        return context.contentResolver
            .query(
                sourceUri(param),
                if (android10Above()) {
                    projection29
                } else {
                    projection
                },
                selection,
                null,
                bucketOrderBy
            )
    }

    private fun sourceUri(param: PickerParam): Uri {
        return if (param.isOnlyVideo || param.isIncludeVideo) {
            MediaStore.Files.getContentUri(VOLUME_NAME)
        } else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    @SuppressLint("AnnotateVersionCheck")
    private fun android10Above() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    companion object {
        private const val APP_DIRECTORY_PREFIX = "Tokopedia"
        private const val VOLUME_NAME = "external"

        // custom columns
        private const val COLUMN_URI = "uri"
        private const val COLUMN_COUNT = "count"
    }

}