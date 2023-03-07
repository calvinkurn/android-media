package com.tokopedia.play.ui.productsheet.itemdecoration

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play_common.R as playCommonR
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 03/03/20
 */
class ProductLineItemDecoration(
    context: Context,
    recyclerView: RecyclerView,
) : RecyclerView.ItemDecoration() {

    private val offset2 = context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_2)
    private val offset4 = context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_4)
    private val offset8 = context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_8)
    private val offset14 = context.resources.getDimensionPixelOffset(playCommonR.dimen.play_dp_14)
    private val offset16 = context.resources.getDimensionPixelOffset(unifyR.dimen.unify_space_16)

    private val mPaint = Paint()

    private var mGuidelines = emptyList<BackgroundGuideline>()

    private val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    private val adapter = recyclerView.adapter as ProductSheetAdapter

    private var mOffsetRectForDraw = Rect()

    fun setGuidelines(guidelines: List<BackgroundGuideline>) {
        mGuidelines = guidelines
    }

    fun release() {
        mGuidelines = emptyList()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        val adapter = parent.adapter as ProductSheetAdapter

        if (position < 0) return

        when (adapter.getItem(position)) {
            is ProductSheetAdapter.Item.Section -> setSectionOffset(outRect, adapter, position)
            is ProductSheetAdapter.Item.Product -> setProductOffset(outRect, adapter, position)
            else -> {}
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        mGuidelines.forEach {
            val firstChildPos = layoutManager.findFirstVisibleItemPosition()
            val lastChildPos = layoutManager.findLastVisibleItemPosition()

            val top = if (firstChildPos <= it.startIndex) {
                val firstChild = layoutManager.getChildAt(
                    it.startIndex - firstChildPos
                ) ?: return@forEach

                when (adapter.getItem(firstChildPos)) {
                    is ProductSheetAdapter.Item.Section -> {
                        setSectionOffset(mOffsetRectForDraw, adapter, firstChildPos)
                    }
                    is ProductSheetAdapter.Item.Product -> {
                        setSectionOffset(mOffsetRectForDraw, adapter, firstChildPos)
                    }
                    else -> mOffsetRectForDraw.setEmpty()
                }

                (firstChild.top - mOffsetRectForDraw.top).coerceAtLeast(0)
            } else {
                0
            }

            val bottom = if (lastChildPos > it.endIndex) {
                val lastChild = layoutManager.getChildAt(
                    it.endIndex - firstChildPos
                ) ?: return@forEach

                lastChild.bottom
            } else {
                parent.height
            }

            mPaint.reset()

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

    private fun setSectionOffset(
        outRect: Rect,
        adapter: ProductSheetAdapter,
        position: Int,
    ) {
        //the previous item of a section is always a product
        outRect.top = if (position == 0) 0 else offset14
        outRect.left = offset16
        outRect.right = offset16
    }

    private fun setProductOffset(
        outRect: Rect,
        adapter: ProductSheetAdapter,
        position: Int,
    ) {
        //the previous item of a section is always a product
        if (position == 0) outRect.top = offset8
        else if (position > 0) {
            outRect.top = if (adapter.getItem(position - 1) is ProductSheetAdapter.Item.Section) {
                offset2
            } else 0 //per product spacing
        }

        if (position == adapter.itemCount - 1) outRect.bottom = offset4

        outRect.left = offset14
        outRect.right = offset14
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
