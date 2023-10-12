package com.tokopedia.contactus.inboxtickets.domain.usecase

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxtickets.data.ImageUpload
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

const val IMAGE_QUALITY = 70

class GetFileUseCase @Inject constructor(
    private val context: Context,
){
    @Throws(IOException::class)
    fun getFilePath(imageUpload: List<ImageUpload>?): List<String> {
        val list = ArrayList<String>()
        imageUpload?.forEach {
            val s = ImageProcessingUtil.compressImageFile(it.fileLoc.orEmpty(), IMAGE_QUALITY)
            list.add(
                try {
                    s.absolutePath
                } catch (e: IOException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    throw IOException(context.getString(R.string.contact_us_error_upload_image))
                }
            )
        }
        return list
    }
}
