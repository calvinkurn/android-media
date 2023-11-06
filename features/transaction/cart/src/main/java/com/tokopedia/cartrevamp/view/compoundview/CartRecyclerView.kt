package com.tokopedia.cartrevamp.view.compoundview

import android.content.Context
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cartrevamp.view.customview.ViewBinderHelper
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.toIntSafely

class CartRecyclerView : RecyclerView {

    private lateinit var viewBinderHelper: ViewBinderHelper

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setViewBinderHelper(viewBinderHelper: ViewBinderHelper) {
        this.viewBinderHelper = viewBinderHelper
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (viewBinderHelper.openCount > 0) {
            val layouts = viewBinderHelper.getOpenedLayout()
            for (layout in layouts) {
                val start = layout.right - 100.dpToPx(layout.context.resources.displayMetrics)
                val end = layout.right
                val top = layout.top
                val bottom = layout.bottom
                val region = Region(start, top, end, bottom)
                if (region.contains(e?.x.toIntSafely(), e?.y.toIntSafely())) {
                    return false
                }
            }
            viewBinderHelper.closeAll()
            return true
        }
        return super.onInterceptTouchEvent(e)
    }
}
