package com.tokopedia.home_account.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SpanningLinearLayoutManager : LinearLayoutManager {

    private val horizontalSpace: Int
        get() = width - paddingRight - paddingLeft

    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop
    private var itemSpacing: Int = 0
    private var minWidth = 0

    private var context: Context? = null

    constructor(context: Context?, itemSpacing: Int = 0) : super(context) {
        this.itemSpacing = itemSpacing
        this.context = context
    }

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean, itemSpacing: Int = 0, minWidth: Int = 0) : super(context, orientation, reverseLayout) {
        this.itemSpacing = itemSpacing
        this.context = context
        this.minWidth = minWidth
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(c: Context, attrs: AttributeSet): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    private fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        val width = Math.round(horizontalSpace / itemCount.toDouble()).toInt() - itemSpacing
        if (orientation == HORIZONTAL) {
            if(minWidth > 0) {
                context?.run {
                    if (dpFromPx(this, width.toFloat()) >= minWidth) {
                        layoutParams.width = width
                    }
                }
            }else {
                layoutParams.width = width
            }
        } else if (orientation == VERTICAL) {
            layoutParams.height = width
        }
        return layoutParams
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }
}