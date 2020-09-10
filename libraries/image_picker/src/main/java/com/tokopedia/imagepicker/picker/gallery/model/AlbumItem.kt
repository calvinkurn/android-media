package com.tokopedia.imagepicker.picker.gallery.model

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Parcelable
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.picker.gallery.internal.entity.Album
import com.tokopedia.imagepicker.picker.gallery.loader.AlbumLoader
import kotlinx.android.parcel.Parcelize

/**
 * Created by hangnadi on 5/22/17.
 */
@Parcelize
class AlbumItem(private val mId: String, val coverPath: Uri, val displayName: String, var count: Long) : Parcelable {
    fun getmId(): String {
        return mId
    }

    fun addCaptureCount() {
        count++
    }

    fun getDisplayName(context: Context): String {
        return if (isAll) {
            context.getString(R.string.album_name_all)
        } else displayName
    }

    val isAll: Boolean
        get() = ALBUM_ID_ALL == mId

    val isEmpty: Boolean
        get() = count == 0L

    fun intoAlbum(): Album {
        return Album(
                mId,
                coverPath,
                displayName,
                count
        )
    }

    companion object {
        @JvmField
        val ALBUM_ID_ALL: String = "-1"

        @JvmStatic
        fun valueOf(cursor: Cursor): AlbumItem {
            val clumn = cursor.getString(cursor.getColumnIndex(AlbumLoader.COLUMN_URI))
            return AlbumItem(
                    cursor.getString(cursor.getColumnIndex("bucket_id")),
                    Uri.parse(clumn ?: ""),
                    cursor.getString(cursor.getColumnIndex("bucket_display_name")),
                    cursor.getLong(cursor.getColumnIndex(AlbumLoader.COLUMN_COUNT)))
        }
    }

}