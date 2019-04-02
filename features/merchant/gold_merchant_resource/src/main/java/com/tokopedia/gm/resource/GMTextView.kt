package com.tokopedia.gm.resource

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.tokopedia.gm.resource.GMConstant.getGMTitleResource

class GMTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){

    override fun onFinishInflate() {
        super.onFinishInflate()
        setText(getGMTitleResource(context))
    }
}