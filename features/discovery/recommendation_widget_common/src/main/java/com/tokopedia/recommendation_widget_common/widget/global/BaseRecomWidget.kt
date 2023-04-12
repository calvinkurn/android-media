package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

abstract class BaseRecomWidget: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    abstract fun bind(model: RecomVisitable)
}
