package com.tokopedia.imagepicker_insta.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object CursorUtil {

    fun getPhotoCursor(context: Context, query: String?, args: Array<String?>?): Cursor? {
        val projection = arrayOf(
            MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA, MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.SIZE
        )
        val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        if (isStoragePermissionGranted()) {
            return context.contentResolver
                .query(images, projection, query, args, MediaStore.Images.Media.DATE_TAKEN + " ASC");
        }
        return null
    }

    fun isStoragePermissionGranted(): Boolean {
        return true
    }

    fun getVideoCursor(context: Context, query: String?, args: Array<String?>?): Cursor? {
        val projection = arrayOf(
            MediaStore.Video.Media._ID, MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DATA, MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE, MediaStore.Video.Media.SIZE
        )
        val videos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        if (isStoragePermissionGranted()) {
            return context.contentResolver
                .query(videos, projection, query, args, MediaStore.Video.Media.DATE_TAKEN + " ASC");
        }
        return null
    }

    fun getVideoFileCursor(context: Context, query: String?, args: Array<String?>?, uri: Uri?): Cursor? {
        val projection = arrayOf(
            MediaStore.Video.Media._ID, MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DATA, MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE, MediaStore.Video.Media.SIZE
        )

        if (isStoragePermissionGranted() && uri != null) {
            return context.contentResolver
                .query(uri, projection, query, args, MediaStore.Video.Media.DATE_TAKEN + " ASC");
        }
        return null
    }
}