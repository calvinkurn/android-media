package com.tokopedia.product.detail.view.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 26/01/21
 */
@SuppressLint("DeprecatedMethod")
class ProductSeparatorItemDecoration(
    private val context: Context,
    marginHorizontal: Int = 16,
    height: Float = 1f,
    @ColorRes private val color: Int = R.color.product_detail_dms_divider_decorator_color
) : RecyclerView.ItemDecoration() {

    private val marginHorizontalPx = marginHorizontal.toPx()
    private val heightPx = height.toPx()
    private val drawable by lazy {
        getDrawableDivider()?.apply {
            setDrawableTint(drawable = this)
        }
    }

    private fun setDrawableTint(drawable: Drawable) {
        try {
            val mutate = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(mutate, getColor(color))
        } catch (t: Throwable) {
            try {
                drawable.mutate().setColorFilter(getColor(color), PorterDuff.Mode.SRC_IN);
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun getDrawableDivider() = ContextCompat.getDrawable(
        context, com.tokopedia.abstraction.R.drawable.bg_line_separator_thin
    )

    private fun getColor(@ColorRes colorId: Int) = context.getColorChecker(id = colorId)

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        drawVerticalDividers(c, parent)
    }

    private fun drawVerticalDividers(
        c: Canvas,
        parent: RecyclerView
    ) {
        val leftBound = parent.left + marginHorizontalPx
        val rightBound = parent.right - marginHorizontalPx

        for (i in 0 until parent.childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val topBound: Int = child.bottom + params.bottomMargin
            val bottomBound: Int =
                (topBound + drawable?.intrinsicHeight.orZero() + heightPx).toIntSafely()

            drawable?.setBounds(leftBound, topBound, rightBound, bottomBound)
            drawable?.draw(c)
        }
    }
}
