package com.tokopedia.imagepreview

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object ImagePreviewUtils{

    fun getUri(context: Context, outputMediaFile: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context,
                    context.applicationContext.packageName + ".provider", outputMediaFile)
        } else {
            Uri.fromFile(outputMediaFile)
        }
    }

}