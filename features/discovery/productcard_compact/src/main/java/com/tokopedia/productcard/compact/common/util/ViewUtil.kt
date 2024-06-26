package com.tokopedia.productcard.compact.common.util

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal object ViewUtil {
    private const val FORMAT_HEX_COLOR = "#%06x"
    private const val COLOR_WHITE = 0xffffff

    fun TextView.setDimenAsTextSize(id: Int) {
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id)
        )
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

    fun getDpFromDimen(context: Context, id: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id),
            context.resources.displayMetrics
        )
    }

    fun getHexColorFromIdColor(context: Context, idColor: Int) : String {
        return try {
            String.format(FORMAT_HEX_COLOR, ContextCompat.getColor(context, idColor) and COLOR_WHITE).uppercase()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            ""
        }
    }

    fun safeParseColor(color: String, defaultColor: Int): Int {
        return try {
            Color.parseColor(color)
        }
        catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            defaultColor
        }
    }

    fun ViewStub.inflateView(@LayoutRes layoutResource: Int): View {
        this.layoutResource = layoutResource
        return inflate()
    }
}
