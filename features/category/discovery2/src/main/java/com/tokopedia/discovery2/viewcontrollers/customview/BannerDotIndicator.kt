package com.tokopedia.discovery2.viewcontrollers.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.unifyprinciples.Typography


class BannerDotIndicator(private val radius: Int, private val indicatorItemPadding: Int, private val indicatorPadding: Int,
                         @ColorInt colorActive: Int, @ColorInt colorInactive: Int, private val indicatorType: Int,
                         private val clickSeeAllInterface: ClickSeeAllInterface) : ItemDecoration() {
    private val inactivePaint: Paint = Paint()
    private val activePaint: Paint = Paint()
    private var btnAppLink: String = ""

    companion object {
        const val SLIDER_BANNER_INDICATOR = 0
        const val CAROUSEL_BANNER_INDICATOR = 1
    }

    init {
        val strokeWidth: Float = Resources.getSystem().displayMetrics.density * 1
        inactivePaint.strokeCap = Paint.Cap.ROUND
        inactivePaint.strokeWidth = strokeWidth
        inactivePaint.style = Paint.Style.FILL
        inactivePaint.isAntiAlias = true
        inactivePaint.color = colorInactive
        activePaint.strokeCap = Paint.Cap.ROUND
        activePaint.strokeWidth = strokeWidth
        activePaint.style = Paint.Style.FILL
        activePaint.isAntiAlias = true
        activePaint.color = colorActive
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter ?: return

        val itemCount = adapter.itemCount
        if (itemCount <= 1) {
            return
        }
        val indicatorPosY = parent.height - indicatorPadding * 1.5f
        drawInactiveDots(c, indicatorPadding.toFloat(), indicatorPosY, itemCount)
        val activePosition: Int = when (parent.layoutManager) {
            is GridLayoutManager -> {
                (parent.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            }
            else -> { // not supported layout manager
                return
            }
        }
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }
        parent.layoutManager!!.findViewByPosition(activePosition) ?: return
        drawActiveDot(c, indicatorPadding.toFloat(), indicatorPosY, activePosition)
        drawTextView(c, parent.context, indicatorPadding.toFloat(), indicatorPosY)
    }

    private fun drawTextView(canvas: Canvas, context: Context, posX: Float, posY: Float) {
        if (btnAppLink.isNotEmpty()) {
            context.run {
                getSeeAllButton(context, btnAppLink)?.run {
                    val layout = LinearLayout(context)
                    layout.addView(this)
                    layout.measure(width, height)
                    layout.layout(0, 0, width, height)
                    canvas.translate((canvas.width - width - posX), posY - height / 2)
                    layout.draw(canvas)
                }
            }
        }
    }

    private fun drawInactiveDots(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) { // width of item indicator including padding
        val itemWidth = radius * 2 + indicatorItemPadding.toFloat()
        var start = indicatorStartX + radius
        for (i in 0 until itemCount) {
            c.drawCircle(start, indicatorPosY, radius.toFloat(), inactivePaint)
            start += itemWidth
        }
    }

    private fun drawActiveDot(c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
                              highlightPosition: Int) { // width of item indicator including padding
        val itemWidth = radius * 2 + indicatorItemPadding.toFloat()
        val highlightStart = indicatorStartX + radius + itemWidth * highlightPosition
        c.drawCircle(highlightStart, indicatorPosY, radius.toFloat(), activePaint)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (indicatorType) {

            SLIDER_BANNER_INDICATOR -> {

            }

            CAROUSEL_BANNER_INDICATOR -> {
                outRect.bottom = indicatorPadding * 2
            }

        }
    }

    fun setBtnAppLink(btnAppLink: String) {
        this.btnAppLink = btnAppLink
    }

    private fun getSeeAllButton(context: Context?, btnAppLink: String): Typography? {
        context?.run {
            val seeAllButton = Typography(this)
            seeAllButton.apply {
                visibility = View.VISIBLE
                text = getText(R.string.lihat_semua)
                setType(6)
                setTextColor(ContextCompat.getColor(this@run, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                setOnClickListener {
                    handleAppLink(this@run, btnAppLink)
                }
            }
            return seeAllButton
        }
        return null
    }

    private fun handleAppLink(context: Context, btnAppLink: String) {
        RouteManager.route(context, btnAppLink)
        clickSeeAllInterface.onClickSeeAll()
    }

    interface ClickSeeAllInterface {
        fun onClickSeeAll()
    }
}