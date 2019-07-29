package com.tokopedia.groupchat.common.design

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.support.v7.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.kotlin.extensions.view.dpToPx


/**
 * @author : Steven 15/07/19
 */
class OverflowMenuItemDecoration(var context: Context) : DividerItemDecoration(context, DividerItemDecoration.VERTICAL) {
    init {
        val drawable = MethodChecker.getDrawable(context, R.drawable.drawable_divider)
        val insetDrawable = InsetDrawable(drawable, 56.dpToPx(context.resources.displayMetrics), 0,0,0)
        setDrawable(insetDrawable)
    }
}