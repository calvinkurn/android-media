package com.tokopedia.common

// import android.graphics.Color
import android.graphics.PorterDuff
// import android.view.View
// import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.tokopedia.unifycomponents.Label
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.R as unifycomponentsR

// internal val View.isVisible: Boolean
//    get() = visibility == View.VISIBLE
//
// internal fun View.doIfVisible(action: (View) -> Unit) {
//    if (this.isVisible) {
//        action(this)
//    }
// }
//
// internal fun View.getDimensionPixelSize(@DimenRes id: Int): Int {
//    return this.context.resources.getDimensionPixelSize(id)
// }
//
// internal fun getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue: Boolean): Int {
//    return if (blankSpaceConfigValue) {
//        View.INVISIBLE
//    } else {
//        View.GONE
//    }
// }
//
// internal fun <T : View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
//    if (this == null) return
//
//    if (shouldShow) {
//        this.visibility = View.VISIBLE
//        action(this)
//    } else {
//        this.visibility = View.GONE
//    }
// }
//
//
// internal fun safeParseColor(color: String, defaultColor: Int): Int {
//    return try {
//        Color.parseColor(color)
//    } catch (throwable: Throwable) {
//        throwable.printStackTrace()
//        defaultColor
//    }
// }

fun Label.forceLightRed() {
    setTextColor(ContextCompat.getColor(context, R.color.dms_static_light_RN500))
    val drawable = ContextCompat.getDrawable(context, unifycomponentsR.drawable.label_bg)
    drawable?.setColorFilter(context.resources.getColor(R.color.dms_static_light_RN100), PorterDuff.Mode.SRC_ATOP)

    setBackgroundDrawable(drawable)
}
