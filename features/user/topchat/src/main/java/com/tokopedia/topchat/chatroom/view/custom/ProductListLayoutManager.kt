package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.roundToInt

class ProductListLayoutManager : LinearLayoutManager {

    private val extraSpaceChildMultiplier = 1
    private val childWidthMultiplier = 0.73
    private var maxChildWidth = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        lp?.let { child ->
            val newWidth = (width * childWidthMultiplier).roundToInt()
            maxChildWidth = max(maxChildWidth, newWidth)
            child.width = newWidth
        }
        return true
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        return super.getExtraLayoutSpace(state) + calculateExtraSpace()
    }

    private fun calculateExtraSpace(): Int {
        return extraSpaceChildMultiplier * maxChildWidth
    }
}