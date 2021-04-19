package com.tokopedia.media.loader.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.load.engine.GlideException

// wrapper of GlideException
typealias MediaException = GlideException

// default rounded for loadImageRounded()
const val DEFAULT_ROUNDED = 5.0f

// resource ID reader and convert it into drawable
fun drawableFromId(context: Context, id: Int): Drawable? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        context.resources.getDrawable(id, context.applicationContext.theme)
    } else {
        AppCompatResources.getDrawable(context, id)
    }
}