package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R

/**
 * @author by milhamj on 2019-09-14.
 */
class CreatePostFabView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.layout_create_post_fab, this)
    }
}