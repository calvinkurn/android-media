package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.CardUnify2

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
            ComponentsList.BannerInfiniteItem.ordinal -> {
                if (spanIndex.isZero()) {
                    //settings for left column
                    val left = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    val others = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
                    setMarginsShopBanner(view, left = left,right = others, top = others, bottom = others)
                } else {
                    //settings for right column
                    val others = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
                    val right = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    setMarginsShopBanner(view, left = others,right = right, top = others, bottom = others)
                }
            }
            ComponentsList.ShopCardItemView.ordinal -> {
                if (spanIndex.isZero()) {
                    //settings for left column
                    val left = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    val others = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                    setMarginsShopCard(view, left = left,right = others, top = others, bottom = others)
                } else {
                    //settings for right column
                    val others = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                    val right = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    setMarginsShopCard(view, left = others,right = right, top = others, bottom = others)
                }
            }
            ComponentsList.ContentCardItem.ordinal -> {
                if(spanIndex.isZero()) {
                    //settings for left column
                    val others = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                    val bottom = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                    val left = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    setMarginsContentCard(view, left = left ,right = others, top = others, bottom = bottom)
                }
                else {
                    //settings for right column
                    val others = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
                    val bottom = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                    val right = parent.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12)
                    setMarginsContentCard(view, left = others ,right = right, top = others, bottom = bottom)
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

    private fun setMarginsShopBanner(view: View?, left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
        val cardView: CardView? = view?.findViewById(R.id.banner_image_container)
        val params = cardView?.layoutParams as? StaggeredGridLayoutManager.LayoutParams

        params?.let {
            it.rightMargin = right
            it.leftMargin = left
            it.bottomMargin = bottom
            it.topMargin = top
            cardView.layoutParams = it
        }
    }

    private fun setMarginsShopCard(view: View?, left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
        val cardView: CardUnify? = view?.findViewById(R.id.parentLayout)
        val params = cardView?.layoutParams as? StaggeredGridLayoutManager.LayoutParams

        params?.let {
            it.rightMargin = right
            it.leftMargin = left
            it.bottomMargin = bottom
            it.topMargin = top
            it.width = (Utils.getDisplayMetric(view.context).widthPixels )/2
            cardView.layoutParams = it
        }
    }

    private fun setMarginsContentCard(view: View?, left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
        val cardView: ConstraintLayout? = view?.findViewById(R.id.content_card_container)
        val params = cardView?.layoutParams as? StaggeredGridLayoutManager.LayoutParams

        params?.let {
            it.rightMargin = right
            it.leftMargin = left
            it.bottomMargin = bottom
            it.topMargin = top
            it.width = (Utils.getDisplayMetric(view.context).widthPixels )/2
            cardView.layoutParams = it
        }
    }
}
