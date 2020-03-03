package com.tokopedia.utils.resources

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources

object DrawableUtils {

    fun getDrawable(context: Context, resId: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            context.resources.getDrawable(resId, context.applicationContext.theme)
        else
            AppCompatResources.getDrawable(context, resId)
    }
}