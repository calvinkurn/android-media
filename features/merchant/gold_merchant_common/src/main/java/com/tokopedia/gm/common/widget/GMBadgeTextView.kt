package com.tokopedia.gm.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.tokopedia.gm.common.constant.GM_BADGE_TITLE

class GMBadgeTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){

    override fun onFinishInflate() {
        super.onFinishInflate()
        text = GM_BADGE_TITLE
    }
}