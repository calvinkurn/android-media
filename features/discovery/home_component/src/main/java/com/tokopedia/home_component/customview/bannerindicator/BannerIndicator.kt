package com.tokopedia.home_component.customview.bannerindicator

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by dhaba
 */
class BannerIndicator : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val text = Typography(context)
        text.text = "Example Text"
        this.addView(text)
    }
}
