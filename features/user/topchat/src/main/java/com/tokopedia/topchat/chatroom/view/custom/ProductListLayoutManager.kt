package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class ProductListLayoutManager: LinearLayoutManager {

    private val childWidthMultiplier = 0.73

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        lp?.let { child ->
            child.width = (width * childWidthMultiplier).roundToInt()
        }
        return true
    }
}