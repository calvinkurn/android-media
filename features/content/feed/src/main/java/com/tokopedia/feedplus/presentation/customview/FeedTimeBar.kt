package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.DefaultTimeBar

/**
 * Created by kenny.hadisaputra on 28/03/23
 */
class FeedTimeBar : DefaultTimeBar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(false)
    }
}
