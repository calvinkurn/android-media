package com.tokopedia.dilayanitokopedia.common.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by irpan on 12/09/22.
 */
class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    private var isScrollEnabled = true

    fun setScrollEnabled(flag: Boolean) {
        isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}
