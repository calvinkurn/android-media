package com.tokopedia.gm.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.R

class GMImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_power_merchant))
    }
}