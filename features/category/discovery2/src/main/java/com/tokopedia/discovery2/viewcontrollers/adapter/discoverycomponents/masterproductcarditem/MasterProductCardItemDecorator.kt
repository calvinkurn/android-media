package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.CardUnify

internal class MasterProductCardItemDecorator() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if(position<0)
            return
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        val type = parent.adapter?.getItemViewType(position)
        when (type) {
            ComponentsList.ProductCardRevampItem.ordinal -> {
                if (spanIndex.isZero()) {
                    //settings for left column
                    val left = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt()
                    setMargins(view, left = left)
                } else {
                    //settings for right column
                    val right = parent.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt()
                    setMargins(view, right = right)
                }
            }
            ComponentsList.CalendarWidgetItem.ordinal -> {
                if (spanIndex.isZero()) {
                    //settings for left column
                    val left = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    setMarginsCalendar(view, left = left)
                } else {
                    //settings for right column
                    val right = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    setMarginsCalendar(view, right = right)
                }
            }
        }
    }

    private fun setMargins(view: View?, left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
        val cardView: CardView? = view?.findViewById(R.id.cardViewProductCard)
        val params = cardView?.layoutParams as? FrameLayout.LayoutParams

        params?.let {
            it.rightMargin = right
            it.leftMargin = left
            it.bottomMargin = bottom
            it.topMargin = top
            cardView.layoutParams = it
        }
    }

    private fun setMarginsCalendar(view: View?, left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
        val cardView: CardUnify? = view?.findViewById(R.id.calendar_card_unify)
        val params = cardView?.layoutParams as? StaggeredGridLayoutManager.LayoutParams

        params?.let {
            it.rightMargin = right
            it.leftMargin = left
            it.bottomMargin = bottom
            it.topMargin = top
            cardView.layoutParams = it
        }
    }
}