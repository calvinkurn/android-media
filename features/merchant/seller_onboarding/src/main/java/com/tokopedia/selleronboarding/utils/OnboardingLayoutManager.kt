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

    private var currentItemPos = 0

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        println("dx : $dx")
        println("isMeasuring : ${state?.isMeasuring}")
        println("currentItemPos : $currentItemPos")
        val isScrollLeft = dx < 0
        println("isScrollLeft : $isScrollLeft")
        if (isScrollLeft) { //scroll to left
            setLeftAlpha(getChildAt(currentItemPos.minus(1)))
            setRightAlpha(getChildAt(currentItemPos))
        } else { //scroll to right
            setRightAlpha(getChildAt(currentItemPos.plus(1)))
            setLeftAlpha(getChildAt(currentItemPos))
        }
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            currentItemPos = findFirstCompletelyVisibleItemPosition()
        }
    }

    private fun setRightAlpha(view: View?) = view?.let { v ->
        val maxDist = (v.width / 3).toFloat()
        val right = getDecoratedRight(v)
        val left = getDecoratedLeft(v)
        val childCenter = left + (left - right) / 2
        val center = width / 2

        val mView = v.tvHeaderText ?: return@let
        mView.alpha = ((abs(center - childCenter) - maxDist) / maxDist)
    }

    private fun setLeftAlpha(view: View?) = view?.let { v ->
        val maxDist = (v.width / 3).toFloat()
        val right = getDecoratedRight(v)
        val left = getDecoratedLeft(v)
        val childCenter = right + (right - left) / 2
        val center = width / 2

        val mView = v.tvHeaderText ?: return@let
        mView.alpha = ((abs(center - childCenter) - maxDist) / maxDist)
    }
}