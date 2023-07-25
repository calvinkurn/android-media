package com.tokopedia.people.views.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.people.R
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
class UserReviewItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val dividerHeight = context.resources.getDimensionPixelOffset(R.dimen.user_profile_dp_1)

    private val mPaint = Paint().apply {
        color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)

            c.drawRect(
                Rect(child.left, child.bottom, parent.width, child.bottom + dividerHeight),
                mPaint
            )
        }
    }
}
