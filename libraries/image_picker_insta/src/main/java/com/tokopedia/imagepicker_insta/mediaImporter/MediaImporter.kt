package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.Context
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.util.FileUtil
import timber.log.Timber
import java.io.File

interface MediaImporter {
    suspend fun importMedia(context: Context): MediaImporterData
    suspend fun importMediaFromInternalDir(context: Context): List<Asset>

    fun getCreateAtForInternalFile(file: File):Long{
        return file.lastModified()/1000L
    }
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