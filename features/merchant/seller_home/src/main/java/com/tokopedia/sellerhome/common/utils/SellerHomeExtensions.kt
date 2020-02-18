package com.tokopedia.sellerhome.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonElement
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

fun Context.dpToPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
)

fun Context.pxToDp(px: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        px.toFloat(),
        resources.displayMetrics
)

//GraphqlResponse extension
inline fun <reified T> GraphqlResponse.getData(): T {
    return this.getData<T>(T::class.java)
}

//log
val Any.toJson: JsonElement
    get() = Gson().toJsonTree(this)

inline fun<reified T: Any> T.logD(s: String) {
    Log.d(T::class.java.simpleName, s)
}
