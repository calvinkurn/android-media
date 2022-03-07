package com.tokopedia.play.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by jegul on 11/12/19
 */
class PlayInteractionConstraintLayout : ConstraintLayout {

    private companion object {
        const val INVISIBLE_ALPHA = 0f
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return alpha == INVISIBLE_ALPHA
    }
}