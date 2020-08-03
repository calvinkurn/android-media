package com.tokopedia.gamification.giftbox.presentation

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import javax.inject.Inject

class LidImagesDownloaderUseCase @Inject constructor() {

    fun downloadImages(context: Context, urlList: List<String>): List<Bitmap> {
        val drawableList = ArrayList<Bitmap>()
        for (url in urlList) {
            val drawable = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .timeout(10000)
                    .submit()
                    .get()
            drawableList.add(drawable)
        }
        return drawableList
    }

    //Glide was not giving callback, so we have to come up with this
    fun downloadBgImage(context: Context, url: String): Bitmap? {
        return Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
    }
}