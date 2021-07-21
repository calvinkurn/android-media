package com.tokopedia.selleronboarding.old.utils

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholder_sob_old_onboarding.view.*
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 12/04/20
 */

class OnboardingLayoutManager(
        context: Context,
        orientation: Int,
        reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {

    companion object {
        private const val SLIDE_1 = 0
        private const val SLIDE_2 = 1
        private const val SLIDE_3 = 2
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        setTextHeaderAlphaOnSwipe(dx)
        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    /**
     * Return the child view at the given index
     * or return null if view not found/index out of bounds
     * */
    private fun getViewAt(index: Int): View? = getChildAt(index)

    private fun setTextHeaderAlphaOnSwipe(dx: Int) {
        val isScrollLeft = dx < 0
        if (isScrollLeft) {
            setLeftAlpha(getViewAt(SLIDE_1))
            setRightAlpha(getViewAt(SLIDE_2))
        } else {
            for (i in 0..itemCount.minus(1)) {
                setLeftAlpha(getViewAt(i))
                when (i) {
                    1 -> setRightAlpha(getViewAt(SLIDE_3))
                    else -> setRightAlpha(getViewAt(SLIDE_2))
                }
            }
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