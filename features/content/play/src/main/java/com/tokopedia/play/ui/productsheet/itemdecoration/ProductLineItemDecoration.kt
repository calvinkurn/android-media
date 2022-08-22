package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
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
            adapter.getItem(position) is ProductSheetAdapter.Item.ProductWithSection) {
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
                    adapter.getItem(adapterPos) is ProductSheetAdapter.Item.ProductWithSection) {
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
                parent.height
            }

            mPaint.shader = null

            when (it.background) {
                is Background.Color -> {
                    setColorBackground(
                        canvas = c,
                        sectionTop = top.toFloat(),
                        sectionBottom = bottom.toFloat(),
                        parent = parent,
                        background = it.background,
                    )
                }
                is Background.Image -> {
                    setImageBackground(
                        canvas = c,
                        sectionTop = top.toFloat(),
                        sectionBottom = bottom.toFloat(),
                        parent = parent,
                        background = it.background,
                    )
                }
            }
        }
    }

    private fun setColorBackground(
        canvas: Canvas,
        sectionTop: Float,
        sectionBottom: Float,
        parent: RecyclerView,
        background: Background.Color,
    ) {
        when (background) {
            is Background.Color.Gradient -> mPaint.shader = background.gradient
            is Background.Color.Solid -> mPaint.color = background.color
        }
        canvas.save()

        if (sectionBottom < parent.height) {
            canvas.translate(0f, sectionBottom - parent.height)
            canvas.drawRect(
                0f,
                0f,
                parent.width.toFloat(),
                parent.height.toFloat(),
                mPaint
            )
        } else {
            canvas.translate(0f, sectionTop)
            canvas.drawRect(
                0f,
                0f,
                parent.width.toFloat(),
                parent.height.toFloat(),
                mPaint
            )
        }

        canvas.restore()
    }

    private fun setImageBackground(
        canvas: Canvas,
        sectionTop: Float,
        sectionBottom: Float,
        parent: RecyclerView,
        background: Background.Image,
    ) {
        if (background.image.width <= 0) return
        canvas.drawBitmap(
            background.image,
            Matrix().apply {
                val scaledRatio = parent.width.toFloat() / background.image.width
                preScale(scaledRatio, scaledRatio)

                val scaledHeight = background.image.height * scaledRatio

                if (sectionBottom < scaledHeight) {
                    postTranslate(0f, sectionBottom - scaledHeight)
                } else {
                    postTranslate(0f, sectionTop)
                }
            },
            mPaint,
        )
    }

    sealed interface Background {
        data class Image(val image: Bitmap) : Background
        sealed interface Color : Background {
            data class Gradient(val gradient: LinearGradient) : Color
            data class Solid(val color: Int) : Color
        }
    }

    data class BackgroundGuideline(
        val startIndex: Int,
        val endIndex: Int,
        val background: Background,
    )
}