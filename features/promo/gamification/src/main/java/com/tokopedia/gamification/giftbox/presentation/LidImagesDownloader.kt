package com.tokopedia.gamification.giftbox.presentation

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
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
                            .timeout(10000)
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

    //Glide was not giving callback, so we have to come up with this
    fun downloadBgImage(context: Context, url: String, imageCallback: ((bmp: Bitmap?) -> Unit)) {
        val job = launch {
            try {
                withTimeout(6000L) {

                    val drawable = Glide.with(context)
                            .asBitmap()
                            .load(url)
                            .submit()
                            .get()

                    withContext(Dispatchers.Main) {
                        imageCallback.invoke(drawable)
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