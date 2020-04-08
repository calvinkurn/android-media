package com.tokopedia.purchase_platform.features.promo.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoEligibilityHeaderViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoEmptyStateViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoListHeaderEnabledViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoRecommendationViewHolder
import javax.inject.Inject

class PromoCheckoutDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (parent.getChildViewHolder(view)) {
            is PromoRecommendationViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
            is PromoEligibilityHeaderViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            is PromoListHeaderEnabledViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_2)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            is PromoEmptyStateViewHolder -> {
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