package com.tokopedia.flashsale.management.ekstension

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flashsale.management.util.AppExecutors
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

fun <T> Deferred<T>.thenOnUI(uiFunction: (T) -> Unit){
    GlobalScope.launch(AppExecutors.uiContext) {
        uiFunction(this@thenOnUI.await())
    }
}

fun ImageView.loadUrl(url: String, radius: Float){
    ImageHandler.loadImageRounded2(context, this, url, radius)
}

val View.isVisible
    get() = visibility == View.VISIBLE

fun View.visible() {visibility = View.VISIBLE}
fun View.gone() {visibility = View.GONE}