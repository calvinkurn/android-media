package com.tokopedia.play.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedFrameLayout

/**
 * Created by jegul on 14/07/21
 */
class InteractiveLoadingView : RoundedFrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_interactive_loading, this)
    }
}