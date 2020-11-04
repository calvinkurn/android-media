package com.tokopedia.shopwidget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.shop_widget_layout.view.*
import kotlin.math.cos
import kotlin.math.roundToInt

class ShopWidgetView: BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.shop_widget_layout, this)
    }

    fun setData(shopWidgetModel: ShopWidgetModel, shopWidgetListener: ShopWidgetListener) {
        bindTitle(shopWidgetModel)
        bindCards(shopWidgetModel, shopWidgetListener)
    }

    private fun bindTitle(shopWidgetModel: ShopWidgetModel) {
        shopWidgetTitle?.shouldShowWithAction(shopWidgetModel.title.isNotEmpty()) {
            shopWidgetTitle?.text = shopWidgetModel.title
        }
    }

    private fun bindCards(shopWidgetModel: ShopWidgetModel, shopWidgetListener: ShopWidgetListener) {
        shopWidgetRecyclerView?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = createAdapter(shopWidgetModel.shopCardList, shopWidgetListener)

            if (it.itemDecorationCount == 0) {
                it.addItemDecoration(createItemDecoration())
            }
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun createAdapter(
            shopCardList: List<ShopWidgetModel.ShopCardModel>,
            shopWidgetListener: ShopWidgetListener
    ): ShopCardAdapter {
        val typeFactory = ShopCardAdapterTypeFactory(shopWidgetListener)
        val shopCardAdapter = ShopCardAdapter(typeFactory)
        shopCardAdapter.addAll(shopCardList)

        return shopCardAdapter
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return ShopCardItemDecoration(
                context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16) ?: 0,
                context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16) ?: 0,
                context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16) ?: 0,
                context?.resources?.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_4) ?: 0
        )
    }

    private class ShopCardItemDecoration(
            private val left: Int,
            private val top: Int,
            private val right: Int,
            private val bottom: Int
    ): RecyclerView.ItemDecoration() {

        private var cardViewHorizontalOffset = 0
        private var cardViewVerticalOffset = 0

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (view is CardView) {
                setCardViewOffsets(view)
                setOutRectOffSetForCardView(outRect, view, parent)
            }
        }

        private fun setCardViewOffsets(cardView: CardView) {
            cardViewHorizontalOffset = cardView.getHorizontalShadowOffset()
            cardViewVerticalOffset = cardView.getVerticalShadowOffset()
        }

        private fun CardView.getVerticalShadowOffset(): Int {
            val maxElevation = this.maxCardElevation
            val radius = this.radius

            return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt()
        }

        private fun CardView.getHorizontalShadowOffset(): Int {
            val maxElevation = this.maxCardElevation
            val radius = this.radius

            return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt()
        }

        private fun setOutRectOffSetForCardView(outRect: Rect, cardView: CardView, parent: RecyclerView) {
            outRect.left = getLeftOffset(cardView, parent)
            outRect.top = getTopOffset()
            outRect.right = getRightOffset(cardView, parent)
            outRect.bottom = getBottomOffset()
        }

        private fun getLeftOffset(cardView: CardView, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(cardView) == 0) {
                left - (cardViewHorizontalOffset / 2)
            } else {
                (left / 4) - (cardViewHorizontalOffset / 2)
            }
        }

        private fun getTopOffset(): Int {
            return top - cardViewVerticalOffset
        }

        private fun getRightOffset(cardView: CardView, parent: RecyclerView): Int {
            return if (parent.getChildAdapterPosition(cardView) == (parent.adapter?.itemCount ?: 0) - 1) {
                right - (cardViewHorizontalOffset / 2)
            } else {
                (right / 4) - (cardViewHorizontalOffset / 2)
            }
        }

        private fun getBottomOffset(): Int {
            return bottom - (cardViewVerticalOffset / 2)
        }
    }
}