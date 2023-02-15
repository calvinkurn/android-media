package com.tokopedia.media.picker.data

import android.database.Cursor
import android.provider.MediaStore
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.picker.common.utils.wrapper.PickerFile

interface MediaQueryDataSource {

    /**
     * The projections data of media-store to curate the file attributes .
     */
    fun projections(): Map<String, String> {
        return mapOf(
            "id" to MediaStore.Images.Media._ID,
            "name" to MediaStore.Images.Media.DISPLAY_NAME,
            "data" to MediaStore.Images.Media.DATA,
            "bucket_name" to MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            "bucket_id" to MediaStore.Images.Media.BUCKET_ID
        )
    }

    fun Cursor.columnIndex(key: String): Int {
        return getColumnIndex(projections()[key])
    }

    /**
     * This method will create the media-store query to fetching any media types (image & video)
     * on local device.
     *
     * @param bucketId The directory id
     * @param offset A limit of media to fetch
     * @return [Cursor]
     */
    fun setupMediaQuery(bucketId: Long, offset: Int? = null): Cursor?

    /**
     * If the media succeed to catch, this method will create a [Media] object to define a valid file.
     *
     * @param cursor A cursor comes from [setupMediaQuery]
     * @return [Media]
     */
    fun createMedia(cursor: Cursor): Media?

    fun getVideoDuration(file: PickerFile): Int
}
