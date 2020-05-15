package com.tokopedia.discovery2

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore


class Utils {
    
    companion object {
        const val TIME_ZONE = "Asia/Jakarta"
        const val TIMER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
        const val UTC_TIMER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z z"
        const val DEFAULT_BANNER_WIDTH = 800
        const val DEFAULT_BANNER_HEIGHT = 150
        const val BANNER_SUBSCRIPTION_DEFAULT_STATUS = -1
        const val SEARCH_DEEPLINK = "tokopedia://search-autocomplete"


        fun extractDimension(url: String?, dimension: String = "height"): Int? {
            val uri = Uri.parse(url)
            return uri?.getQueryParameter(dimension)?.toInt()
        }

        fun shareData(context: Context?, shareTxt: String?, productUri: String?, image: Bitmap?) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            if (image != null) {
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val path: String = MediaStore.Images.Media.insertImage(context?.contentResolver, image, "Image Description", null)
                val uri = Uri.parse(path)
                share.putExtra(Intent.EXTRA_STREAM, uri)
            }
            share.putExtra(Intent.EXTRA_TEXT, shareTxt+ "\n"+ productUri)
            context?.startActivity(Intent.createChooser(share, shareTxt))
        }
    }
}