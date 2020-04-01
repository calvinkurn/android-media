package com.tokopedia.play.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R

/**
 * Created by jegul on 17/12/19
 */
class ChatScrollDownView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val vIndicatorRed: View

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_chat_scroll_down, this, true)
        vIndicatorRed = view.findViewById(R.id.v_indicator_red)
    }

    fun showIndicatorRed(isShow: Boolean) {
        if (isShow) vIndicatorRed.show() else vIndicatorRed.gone()
    }
}