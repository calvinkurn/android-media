package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.unifyprinciples.R as unifyR
/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(
    context: Context,
    recyclerView: RecyclerView,
) : RecyclerView.ItemDecoration() {

    private val defaultOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    private val topBottomOffset = context.resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl2)

    private val mPaint = Paint()

    private val defaultColor by lazy(LazyThreadSafetyMode.NONE) {
        MethodChecker.getColor(context, unifyR.color.Unify_Background)
    }

    private var mGuidelines = emptyList<BackgroundGuideline>()

    private val layoutManager = recyclerView.layoutManager as LinearLayoutManager

    fun setGuidelines(guidelines: List<BackgroundGuideline>) {
        mGuidelines = guidelines
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

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        mGuidelines.forEach {
            val firstChildPos = layoutManager.findFirstVisibleItemPosition()
            val lastChildPos = layoutManager.findLastVisibleItemPosition()

            val top = if (firstChildPos <= it.startIndex) {
                val finalPos = it.startIndex - firstChildPos
                val firstChild = layoutManager.getChildAt(finalPos) ?: return@forEach

                val adapterPos = parent.getChildAdapterPosition(firstChild)
                val adapter = parent.adapter as ProductSheetAdapter
                if (adapterPos > 0 &&
                    adapter.getItem(adapterPos) is ProductSheetAdapter.Item.ProductWithSection &&
                    adapter.getItem(adapterPos - 1) is ProductSheetAdapter.Item.Product) {
                    (firstChild.top - defaultOffset).coerceAtLeast(0)
                } else if (adapterPos == 0) {
                    (firstChild.top - topBottomOffset).coerceAtLeast(0)
                } else firstChild.top

            } else {
                0
            }

            val bottom = if (lastChildPos > it.endIndex) {
                val lastChild = layoutManager.getChildAt(it.endIndex - firstChildPos) ?: return@forEach
                lastChild.bottom
            } else {
                parent.bottom
            }

            mPaint.shader = null
            when (it.background) {
                is Background.Color.Gradient -> mPaint.shader = it.background.gradient
                is Background.Color.Solid -> mPaint.color = it.background.color
                else -> mPaint.color = defaultColor
            }

            c.drawRect(
                0f, top.toFloat(), parent.width.toFloat(), bottom.toFloat(), mPaint
            )
        }
    }

    sealed interface Background {
        data class Image(val image: Bitmap) : Background
        sealed interface Color : Background {
            data class Gradient(val gradient: LinearGradient) : Color
            data class Solid(val color: Int) : Color
        }
        object None : Background
    }

    data class BackgroundGuideline(
        val startIndex: Int,
        val endIndex: Int,
        val background: Background,
    )
}