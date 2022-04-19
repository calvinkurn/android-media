package com.tokopedia.home_account.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by Yoris Prayogo on 05/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class SwipeRecyclerView: RecyclerView {

    var swipeLayout: SwipeRefreshLayout? = null

    constructor(context: Context) :
            super(context)

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onScrolled(dx: Int, dy: Int) {
        val topRowVerticalPosition = if (childCount == 0) 0 else getChildAt(0).getTop()
        swipeLayout?.isEnabled = topRowVerticalPosition >= 0
    }
}