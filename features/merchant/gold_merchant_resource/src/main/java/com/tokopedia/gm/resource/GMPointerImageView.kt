package com.tokopedia.gm.resource

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class GMPointerImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr){

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageDrawable(GMConstant.getGMPointerDrawable(context))
    }


}