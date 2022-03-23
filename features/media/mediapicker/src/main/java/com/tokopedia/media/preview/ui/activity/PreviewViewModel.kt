package com.tokopedia.media.preview.ui.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.utils.file.PublicFolderUtil
import java.io.File

class PreviewViewModel : ViewModel() {

    fun dispatchFileToPublic(context: Context, filePath: String) {
        val mimeType = if (isVideoFormat(filePath)) "video/mp4" else "image/jpeg"
        val currentTimeStamp = System.currentTimeMillis()

        val file = File(filePath)

        PublicFolderUtil.putFileToPublicFolder(
            context = context,
            localFile = file,
            outputFileName = "Tkpd_${currentTimeStamp}_${file.name}",
            mimeType = mimeType,
        )
    }

}