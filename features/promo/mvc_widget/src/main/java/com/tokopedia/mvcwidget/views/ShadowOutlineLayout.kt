package com.tokopedia.mvcwidget.views

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView

class ShadowOutlineLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        outlineProvider = ShadowOutline()
        clipToOutline = true
    }
}

class ShadowOutline : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        if (view != null && outline != null) {
            outline.setRect(0, 0, view.width, view.height)
        }
    }
}