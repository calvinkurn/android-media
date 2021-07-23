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

        setCornerRadius(
                resources.getDimension(com.tokopedia.play_common.R.dimen.play_interactive_common_radius)
        )

        val loaderRadius = resources.getDimension(R.dimen.play_interactive_loader_radius)
        findViewById<RoundedFrameLayout>(R.id.fl_placeholder_1).setCornerRadius(loaderRadius)
        findViewById<RoundedFrameLayout>(R.id.fl_placeholder_2).setCornerRadius(loaderRadius)
        findViewById<RoundedFrameLayout>(R.id.fl_placeholder_3).setCornerRadius(loaderRadius)
        findViewById<RoundedFrameLayout>(R.id.fl_placeholder_4).setCornerRadius(loaderRadius)
    }
}