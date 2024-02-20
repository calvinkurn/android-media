package com.tokopedia.shop.home.view.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductChangeGridSectionUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ShopFestivityRvItemDecoration(
    private val context: Context,
    private val bodyBackgroundHexColor: String,
    private val isOverrideTheme: Boolean
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val RV_PADDING = 12f
        private const val INT_TWO = 2
    }
    private var backgroundDrawableWhite: Drawable? = null
    private val deviceWidth by lazy { getScreenWidth() }
    private val defaultBackgroundColor by lazy { MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0) }

    init {
        val bgColor = if (isOverrideTheme) {
            ShopUtil.parseColorFromHexString(bodyBackgroundHexColor)
        } else {
            MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN0)
        }
        (
            MethodChecker.getDrawable(
                context,
                R.drawable.bg_festivity_widget_background_white
            ) as? GradientDrawable
            )?.let {
            it.setColor(bgColor)
            backgroundDrawableWhite = it
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        try {
            if (parent.layoutManager == null || backgroundDrawableWhite == null) {
                return
            }
            val childCount = parent.childCount
            val adapter = (parent.adapter as? ShopHomeAdapter)
            for (i in 0 until childCount) {
                val child: View? = parent.getChildAt(i)
                child?.let {
                    val widgetPosition = parent.getChildAdapterPosition(it)
                    val adapterData = adapter?.data
                    when (val widgetUiModel = adapterData?.getOrNull(widgetPosition)) {
                        is ShopHomeCarousellProductUiModel -> {
                            if (!widgetUiModel.isFestivity) {
                                drawOverlayColorBehindRecyclerViewItem(it, canvas)
                            }
                        }
                        is BaseShopHomeWidgetUiModel -> {
                            if (!widgetUiModel.isFestivity) {
                                drawWhiteBackgroundFromLeftParentToRightParent(it, parent, canvas)
                            }
                        }
                        is ThematicWidgetUiModel -> {
                            if (!widgetUiModel.isFestivity) {
                                drawWhiteBackgroundFromLeftParentToRightParent(it, parent, canvas)
                            }
                        }
                        is ShopHomeProductEtalaseTitleUiModel -> {
                            drawOverlayColorBehindRecyclerViewItem(it, canvas)
                        }
                        is ShopProductSortFilterUiModel -> {
                            drawOverlayColorBehindRecyclerViewItem(it, canvas)
                        }
                        is ShopHomeProductChangeGridSectionUiModel -> {
                            drawOverlayColorBehindRecyclerViewItem(it, canvas)
                        }
                        is ShopHomeProductUiModel -> {
                            drawOverlayColorBehindRecyclerViewItem(it, canvas)
                            checkForGapAtTheEndOfProductList(widgetPosition, adapter, parent, canvas)
                        }
                        else -> {
                            drawWhiteBackgroundFromLeftParentToRightParent(it, parent, canvas)
                        }
                    }
                }
            }
        } catch (_: Throwable) {}
    }

    private fun checkForGapAtTheEndOfProductList(widgetPosition: Int, adapter: ShopHomeAdapter, parent: RecyclerView, canvas: Canvas) {
        if (widgetPosition == adapter.itemCount - Int.ONE && adapter.productListViewModel.size > Int.ONE) {
            val lastLeftChild = findLastLeftChild(parent)
            val lastRightChild = findLastRightChild(parent)
            val anchoredChild =
                if (lastLeftChild?.bottom.orZero() < lastRightChild?.bottom.orZero()) {
                    lastLeftChild
                } else {
                    lastRightChild
                }
            anchoredChild?.let {
                drawWhiteBackgroundToFillGapOnLastProduct(it, parent, canvas)
            }
        }
    }

    private fun findLastRightChild(parent: RecyclerView): View? {
        val childCount = parent.childCount
        for (i in childCount - Int.ONE downTo Int.ZERO) {
            val child: View? = parent.getChildAt(i)
            if (child?.right.orZero() + parent.paddingRight == parent.right) {
                return child
            }
        }
        return null
    }

    private fun findLastLeftChild(parent: RecyclerView): View? {
        val childCount = parent.childCount
        for (i in childCount - Int.ONE downTo Int.ZERO) {
            val child: View? = parent.getChildAt(i)
            if (child?.left.orZero() - parent.paddingLeft == parent.left) {
                return child
            }
        }
        return null
    }

    private fun drawOverlayColorBehindRecyclerViewItem(child: View, canvas: Canvas) {
        val bounds = Rect()
        child.getHitRect(bounds)
        
        bounds.top = bounds.top - child.marginTop
        bounds.left = 0
        bounds.right = deviceWidth
        bounds.bottom = bounds.bottom + child.marginBottom

        val backgroundColor = when {
            isOverrideTheme && bodyBackgroundHexColor.isNotEmpty() -> {
                ShopUtil.parseColorOrFallback(
                    hexColor = bodyBackgroundHexColor,
                    fallbackColor = defaultBackgroundColor
                )
            }
            isOverrideTheme && bodyBackgroundHexColor.isEmpty() -> defaultBackgroundColor
            else -> defaultBackgroundColor
        }
        
        val paint = Paint().apply { 
            color = backgroundColor 
        }
        
        canvas.drawRect(bounds, paint)
    }

    private fun drawWhiteBackgroundFromLeftParentToRightParent(child: View, parent: RecyclerView, canvas: Canvas) {
        val top = child.top - child.marginTop
        val right = parent.right
        val bottom = child.bottom + child.marginBottom
        val left = parent.left
        backgroundDrawableWhite?.setBounds(left, top, right, bottom)
        backgroundDrawableWhite?.draw(canvas)
    }

    private fun drawWhiteBackgroundToFillGapOnLastProduct(child: View, parent: RecyclerView, canvas: Canvas) {
        val rvPadding = RV_PADDING.dpToPx()
        val midParent = parent.right / INT_TWO
        val top = child.bottom
        val right = if (child.left >= midParent) {
            child.right + rvPadding
        } else {
            child.right
        }
        val bottom = parent.bottom
        val left = if (child.left < midParent) {
            child.left - rvPadding
        } else {
            child.left
        }
        backgroundDrawableWhite?.setBounds(left.toInt(), top, right.toInt(), bottom)
        backgroundDrawableWhite?.draw(canvas)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) = outRect.set(0, 0, 0, 0)
}
