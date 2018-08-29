package com.tokopedia.topads.freeclaim.view.widget

import android.content.Context
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topads.freeclaim.R
import kotlinx.android.synthetic.main.widget_ticker_free_claim.view.*

class TopAdsWidgetFreeClaim @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    init {
        instatiateView()
    }

    private fun instatiateView() {
        inflate(context, R.layout.widget_ticker_free_claim, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        content_text.movementMethod = LinkMovementMethod.getInstance()
        invalidate()
        requestLayout()
    }

    fun setContent(spanned: Spanned){
        content_text.setText(spanned)
        invalidate()
        requestLayout()
    }
}