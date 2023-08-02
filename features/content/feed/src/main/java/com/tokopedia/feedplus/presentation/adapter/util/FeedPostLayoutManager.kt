package com.tokopedia.feedplus.presentation.adapter.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kenny.hadisaputra on 05/07/23
 */
class FeedPostLayoutManager(
    context: Context?
) : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {

    private val helper = OrientationHelper.createOrientationHelper(this, RecyclerView.VERTICAL)

    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        extraLayoutSpace[0] = 2 * helper.totalSpace
        extraLayoutSpace[1] = 2 * helper.totalSpace
    }
}
