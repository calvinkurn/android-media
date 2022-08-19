package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.unifyprinciples.R as unifyR
/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(
    context: Context,
    private val recyclerView: RecyclerView,
) : RecyclerView.ItemDecoration() {

    private val defaultOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    private val topBottomOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl2)

    private val mPaint = Paint()

    private val startIndex = 0
    private val endIndex = 49

    private val layoutManager = recyclerView.layoutManager as LinearLayoutManager

    fun setGradient(gradient: List<String>) {
        val shader = LinearGradient(
            0f, 0f, 0f, recyclerView.height.toFloat(),
            Color.parseColor(gradient.first()), Color.parseColor(gradient[1]),
            Shader.TileMode.CLAMP,
        )
        mPaint.shader = shader
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        val adapter = parent.adapter as ProductSheetAdapter
        if (position > 0 &&
            adapter.getItem(position) is ProductSheetAdapter.Item.ProductWithSection &&
                adapter.getItem(position - 1) is ProductSheetAdapter.Item.Product) {
            outRect.top = defaultOffset
        } else if (position == 0) {
            outRect.top = topBottomOffset
        }

        if(position == itemCount - 1) outRect.bottom = topBottomOffset
    }

    /**
     * Shader shader = new LinearGradient(0, 0, 0, 40, Color.WHITE, Color.BLACK, TileMode.CLAMP);
    Paint paint = new Paint();
    paint.setShader(shader);
    canvas.drawRect(new RectF(0, 0, 100, 40), paint);
     */

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val firstChildPos = layoutManager.findFirstVisibleItemPosition()
        val lastChildPos = layoutManager.findLastVisibleItemPosition()

        val top = if (firstChildPos < startIndex) {
            val firstChild = layoutManager.getChildAt(startIndex - firstChildPos) ?: return
            firstChild.top
        } else {
            0
        }

        val bottom = if (lastChildPos > endIndex) {
            val lastChild = layoutManager.getChildAt(endIndex - firstChildPos) ?: return
            lastChild.bottom
        } else {
            parent.bottom
        }

        c.drawRect(
            0f, top.toFloat(), parent.width.toFloat(), bottom.toFloat(), mPaint
        )
    }
}