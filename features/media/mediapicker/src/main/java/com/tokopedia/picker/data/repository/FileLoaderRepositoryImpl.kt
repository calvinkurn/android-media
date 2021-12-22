package com.tokopedia.picker.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.tokopedia.picker.data.entity.Directory
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.utils.isGifFormat
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class FileLoaderRepositoryImpl constructor(
    private val context: Context
) : FileLoaderRepository {

    private var executor: ExecutorService? = null

    override fun loadFiles(
        config: PickerParam,
        listener: FileLoaderRepository.LoaderListener
    ) {
        val isFolderMode = config.isFolderMode
        val includeVideo = config.isIncludeVideo
        val onlyVideo = config.isOnlyVideo
        val includeAnimation = config.isIncludeAnimation
        val excludedImages = config.excludedImages

        executors().execute(
            FileLoaderRunnable(
                context.applicationContext,
                isFolderMode,
                onlyVideo,
                includeVideo,
                includeAnimation,
                excludedImages,
                listener
            )
        )
    }

    override fun abort() {
        executor?.shutdown()
        executor = null
    }

    private fun executors(): ExecutorService {
        return executor?: Executors.newSingleThreadExecutor()
    }

    class FileLoaderRunnable(
        private val context: Context,
        private val isFolderMode: Boolean,
        private val onlyVideo: Boolean,
        private val includeVideo: Boolean,
        private val includeAnimation: Boolean,
        private val excludedImages: List<File>?,
        private val listener: FileLoaderRepository.LoaderListener
    ) : Runnable {

        private val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        @SuppressLint("InlinedApi")
        private fun queryData(limit: Int? = null): Cursor? {
            val useNewApi = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            val sourceUri = if (limit != null && useNewApi) {
                getSourceUri().buildUpon()
                    .appendQueryParameter(QUERY_LIMIT, limit.toString())
                    .build()
            } else {
                getSourceUri()
            }

            val type = MediaStore.Files.FileColumns.MEDIA_TYPE

            val selection = when {
                onlyVideo -> "${type}=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
                includeVideo -> "$type=${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE} OR $type=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
                // Empty because we query from image media store
                else -> ""
            }

            if (useNewApi) {
                val args = Bundle().apply {
                    // Sort function
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED)
                    )
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )
                    // Selection
                    putString(
                        ContentResolver.QUERY_ARG_SQL_SELECTION,
                        selection
                    )
                    // Limit
                    if (limit != null) {
                        putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    }
                }

                return context.contentResolver.query(sourceUri, projection, args, null)
            }

            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC".let {
                if (limit != null) "$it LIMIT $limit" else it
            }

            return context.contentResolver.query(
                sourceUri, projection,
                selection, null, sortOrder
            )
        }

        private fun getSourceUri(): Uri {
            return if (onlyVideo || includeVideo) {
                MediaStore.Files.getContentUri("external")
            } else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        private fun cursorToImage(cursor: Cursor): Media? {
            val path = cursor.getString(cursor.getColumnIndex(projection[2]))
            val file = makeSafeFile(path) ?: return null
            if (excludedImages != null && excludedImages.contains(file)) return null

            // Exclude GIF when we don't want it
            if (!includeAnimation) {
                if (isGifFormat(path)) return null
            }

            val id = cursor.getLong(cursor.getColumnIndex(projection[0]))
            val name = cursor.getString(cursor.getColumnIndex(projection[1]))

            if (name != null) {
                return Media(id, name, path)
            }

            return null
        }

        private fun processData(cursor: Cursor?) {
            if (cursor == null) {
                listener.onFailed(NullPointerException())
                return
            }

            val result: MutableList<Media> = ArrayList()
            val folderMap: MutableMap<String, Directory> = mutableMapOf()

            if (cursor.moveToFirst()) {
                do {
                    val image = cursorToImage(cursor)

                    if (image != null) {
                        result.add(image)

                        // Load folders
                        if (!isFolderMode) continue
                        var bucket = cursor.getString(cursor.getColumnIndex(projection[3]))

                        if (bucket == null) {
                            val parent = File(image.path).parentFile
                            bucket = if (parent != null) parent.name else DEFAULT_FOLDER_NAME
                        }

                        if (bucket != null) {
                            var folder = folderMap[bucket]
                            if (folder == null) {
                                folder = Directory(bucket)

                                folderMap[bucket] = folder
                            }
                            folder.medias.add(image)
                        }
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()

            val folders = folderMap.values.toList()
            listener.onFileLoaded(result, folders)
        }

        override fun run() {
            // We're gonna load two times for faster load if the devices has many images
            val cursor = queryData(FIRST_LIMIT)
            val isLoadDataAgain = cursor?.count == FIRST_LIMIT
            processData(cursor)

            if (isLoadDataAgain) {
                processData(queryData())
            }
        }

        companion object {
            private const val DEFAULT_FOLDER_NAME = "SDCARD"
            private const val FIRST_LIMIT = 1_000

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

}