package com.tokopedia.picker.common.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.picker.common.utils.wrapper.PickerFile

object VideoDurationRetriever {

    private const val KEY_DURATION = MediaMetadataRetriever.METADATA_KEY_DURATION

    fun get(context: Context?, file: PickerFile?): Int {
        val uri = Uri.fromFile(file)

        return try {
            with(MediaMetadataRetriever()) {
                setDataSource(context, uri)
                val durationData = extractMetadata(KEY_DURATION)

                release()

                durationData.toIntOrZero()
            }
        } catch (e: Throwable) {
            0
        }
    }

}