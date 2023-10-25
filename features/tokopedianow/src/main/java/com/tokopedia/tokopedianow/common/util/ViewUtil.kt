package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.LayoutRes

object ViewUtil {
    fun TextView.setDimenAsTextSize(id: Int) {
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id)
        )
    }

    fun getDpFromDimen(context: Context, id: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id),
            context.resources.displayMetrics
        )
    }

    fun safeParseColor(color: String, defaultColor: Int): Int {
        return try {
            Color.parseColor(color)
        }
        catch (throwable: Throwable) {
            throwable.printStackTrace()
            defaultColor
        }
    }

    fun ViewStub.inflateView(@LayoutRes layoutResource: Int): View {
        this.layoutResource = layoutResource
        return inflate()
    }
}

fun View.doOnPreDraw(block: View.() -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            block(this@doOnPreDraw)
            return true
        }
    })
}

fun View?.addViewOnScreenObserver(onViewVisibleOnScreen: () -> Unit) {
    this?.let {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val visibleOnScreen = checkViewOnScreen(this@addViewOnScreenObserver)
                if(visibleOnScreen) {
                    onViewVisibleOnScreen.invoke()
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }
}

fun checkViewOnScreen(view: View): Boolean {
    val rect = Rect()
    return view.getGlobalVisibleRect(rect)
}
