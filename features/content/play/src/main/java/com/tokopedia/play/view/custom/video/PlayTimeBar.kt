package com.tokopedia.play.view.custom.video

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.ui.DefaultTimeBar

/**
 * Created by kenny.hadisaputra on 31/03/22
 */
class PlayTimeBar : DefaultTimeBar {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        timebarAttrs: AttributeSet?
    ) : super(context, attrs, defStyleAttr, timebarAttrs)

    init {

    }
}