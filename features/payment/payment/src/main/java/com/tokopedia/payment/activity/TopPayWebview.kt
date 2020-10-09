package com.tokopedia.payment.activity

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import com.google.android.play.core.splitcompat.SplitCompat
import org.jetbrains.annotations.NotNull

class TopPayWebview @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {

    init {
        SplitCompat.installActivity(context)
    }
}