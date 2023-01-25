package com.tokopedia.shop.home.view.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel

class ShopFestivityRvItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var backgroundDrawableWhite: Drawable? = null

    init {
        backgroundDrawableWhite = MethodChecker.getDrawable(context, R.drawable.bg_festivity_widget_background_white)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || backgroundDrawableWhite == null) {
            return
        }
        val childCount = parent.childCount
        val adapter = (parent.adapter as? ShopHomeAdapter)
        for (i in 0 until childCount) {
            val child: View? = parent.getChildAt(i)
            child?.let {
                val widgetPosition = parent.getChildAdapterPosition(it)
                when(val widgetUiModel = adapter?.data?.getOrNull(widgetPosition)){
                    is BaseShopHomeWidgetUiModel -> {
                        if(!widgetUiModel.isFestivity){
                            drawWhiteBackground(it, parent, canvas)
                        }
                    }
                    is ThematicWidgetUiModel -> {
                        if(!widgetUiModel.isFestivity){
                            drawWhiteBackground(it, parent, canvas)
                        }
                    }
                    else -> {
                        drawWhiteBackground(it, parent, canvas)
                    }
                }
            }
        }
    }

    private fun drawWhiteBackground(child: View, parent: RecyclerView, canvas: Canvas) {
        val top = child.top - child.marginTop
        val right = parent.right
        val bottom = child.bottom + child.marginBottom
        val left = parent.left
        backgroundDrawableWhite?.setBounds(left, top, right, bottom)
        backgroundDrawableWhite?.draw(canvas)
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) = outRect.set(0, 0, 0, 0)

}
