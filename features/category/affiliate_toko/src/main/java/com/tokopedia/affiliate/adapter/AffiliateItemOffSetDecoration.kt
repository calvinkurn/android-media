package com.tokopedia.affiliate.adapter

import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toPx


/** for Staggered
 * space in px
 **/
class AffiliateItemOffSetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).leftMargin = 0
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).rightMargin = 0
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin = 0
        (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).bottomMargin = 0
        val position = parent.getChildAdapterPosition(view)
        if(position < 0)
            return
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
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
}