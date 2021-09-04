package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.Context
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.util.FileUtil
import timber.log.Timber

interface MediaImporter {
    fun importMedia(context: Context): MediaImporterData
    fun importMediaFromInternalDir(context: Context): List<Asset>

    fun getDate(dateAdded: Long, dateTaken: Long, fileName: String): Long {
        try {
            return when {
                dateTaken != 0L -> dateTaken
                dateAdded != 0L -> dateAdded
                else -> FileUtil.getDateTaken(fileName)
            }
        } catch (th: Throwable) {
            Timber.e(th)
        }
        return 0L
    }
}