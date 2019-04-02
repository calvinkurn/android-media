package com.tokopedia.affiliatecommon.view.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.abstraction.base.view.widget.TouchViewPager

/**
 * @author by milhamj on 9/21/18.
 */
class WrapContentViewPager : TouchViewPager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}