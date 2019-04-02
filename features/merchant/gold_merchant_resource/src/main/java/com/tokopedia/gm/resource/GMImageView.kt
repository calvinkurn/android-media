package com.tokopedia.gm.resource

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.tokopedia.gm.resource.GMConstant.getGMDrawable

class GMImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr){

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageDrawable(getGMDrawable(context))
    }


}