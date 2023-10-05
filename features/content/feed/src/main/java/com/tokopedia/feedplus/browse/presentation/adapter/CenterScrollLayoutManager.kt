package com.tokopedia.feedplus.browse.presentation.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by meyta.taliti on 04/09/23.
 */
class CenterScrollLayoutManager(
    private val context: Context,
    orientation: Int,
    reverseLayout: Boolean
): LinearLayoutManager(context, orientation, reverseLayout) {

    override fun scrollToPosition(position: Int) {
        val width = context.resources.displayMetrics.widthPixels
        val offset = width/2
        super.scrollToPositionWithOffset(position, offset)
    }
}
