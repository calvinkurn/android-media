package com.tokopedia.gamification.giftbox.presentation

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.media.loader.getBitmapFromUrl
import javax.inject.Inject

class LidImagesDownloaderUseCase @Inject constructor() {

    fun downloadImages(context: Context, urlList: List<String>): List<Bitmap> {
        val drawableList = ArrayList<Bitmap>()
        for (url in urlList) {
            url.getBitmapFromUrl(context,10000)?.let {
                drawableList.add(it)
            }
        }
        return drawableList
    }

    //Glide was not giving callback, so we have to come up with this
    fun downloadBgImage(context: Context, url: String): Bitmap? {
        return url.getBitmapFromUrl(context)
    }
}
