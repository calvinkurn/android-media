package com.tokopedia.gm.resource

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class GMBadgeTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){

    override fun onFinishInflate() {
        super.onFinishInflate()
        setText(GMConstant.getGMBadgeTitleResource(context))
    }
}