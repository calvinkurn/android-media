package com.tokopedia.purchase_platform.features.promo.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.*

class PromoDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (parent.getChildViewHolder(view)) {
            is PromoRecommendationViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
            is PromoInputViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
            is PromoEligibleHeaderViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_2)?.toInt() ?: 0
            }
            is PromoListHeaderViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_2)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            is PromoIneligibleHeaderViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            else -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
        }

    }

}