package com.tokopedia.play.ui.engagement.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R.dimen as unifyR

/**
 * @author by astidhiyaa on 14/09/22
 */
class EngagementItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight =
        context.resources.getDimensionPixelOffset(unifyR.unify_space_4)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = dividerHeight
    }
}
