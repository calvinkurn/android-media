package com.tokopedia.play.view.custom.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.play.R
import com.tokopedia.play_common.view.RoundedFrameLayout

/**
 * Created by jegul on 05/07/21
 */
class InteractiveFinishedView : RoundedFrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val tvInteractiveFinishInfo: TextView

    init {
        val view = View.inflate(context, R.layout.view_interactive_finish, this)

        tvInteractiveFinishInfo = view.findViewById(R.id.tv_interactive_finish_info)
    }

    fun setInfo(info: String) {
        tvInteractiveFinishInfo.text = info
    }
}