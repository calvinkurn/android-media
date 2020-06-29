package com.tokopedia.gamification.giftbox.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class LidImagesDownloader : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    private val drawableList = ArrayList<Bitmap>()

    fun downloadImages(context: Context, urlList: List<String>, imageCallback: ((images: ArrayList<Bitmap>?) -> Unit)) {
        val job = launch {
            try {
                for (url in urlList) {
                    val drawable = Glide.with(context)
                            .asBitmap()
                            .load(url)
                            .submit()
                            .get()
                    drawableList.add(drawable)
                }

                withContext(Dispatchers.Main) {
                    if (urlList.size == drawableList.size) {
                        imageCallback.invoke(drawableList)
                    } else {
                        imageCallback.invoke(null)
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    imageCallback.invoke(null)
                }
            }
        }
    }


}