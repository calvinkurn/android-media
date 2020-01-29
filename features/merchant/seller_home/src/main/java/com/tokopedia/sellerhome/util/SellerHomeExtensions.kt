package com.tokopedia.sellerhome.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.tokopedia.graphql.data.model.GraphqlResponse

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

//Context Extensions
fun Context.getResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getResDrawable(@DrawableRes drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawable)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

//GraphqlResponse extension
inline fun <reified T> GraphqlResponse.getData(): T {
    return this.getData<T>(T::class.java)
}

