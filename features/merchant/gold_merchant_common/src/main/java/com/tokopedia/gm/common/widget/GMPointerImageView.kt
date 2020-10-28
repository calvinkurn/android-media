package com.tokopedia.gm.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.tokopedia.gm.common.R

class GMPointerImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr){

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pointer_power_merchant))
    }
}