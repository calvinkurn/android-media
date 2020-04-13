package com.tokopedia.selleronboarding.utils

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholder_sob_onboarding.view.*
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 12/04/20
 */

class OnboardingLayoutManager(
        context: Context,
        orientation: Int,
        reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        for (i in 0..childCount) {
            updateChildrenAlpha(getChildAt(i))
        }
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
    }

    private fun updateChildrenAlpha(view: View?) = view?.let { v ->
        val maxDist = (v.width / 3).toFloat()
        val right = getDecoratedRight(v)
        val left = getDecoratedLeft(v)
        val childCenter = left + (left - right) / 2
        val center = width / 2

        val mView = v.tvHeaderText ?: return@let
        mView.alpha = ((abs(center - childCenter) - maxDist) / maxDist)
    }

    private fun updateChildrenAlpha1(view: View?) = view?.let { v ->
        val maxDist = (v.width / 3).toFloat()
        val right = getDecoratedRight(v)
        val left = getDecoratedLeft(v)
        val childCenter = right + (left - right) / 2
        val center = width / 2

        val mView = v.tvHeaderText ?: return@let
        mView.alpha = ((abs(center - childCenter) - maxDist) / maxDist)
    }
}