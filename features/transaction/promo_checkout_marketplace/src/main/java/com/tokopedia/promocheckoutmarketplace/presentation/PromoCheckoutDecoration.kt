package com.tokopedia.promocheckoutmarketplace.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.*
import javax.inject.Inject

class PromoCheckoutDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var space16 = -1
    private var space12 = -1
    private var space8 = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (space16 == -1) {
            space16 = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0
            space12 = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_12)?.toInt() ?: 0
            space8 = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt() ?: 0
        }

        when (parent.getChildViewHolder(view)) {
            is PromoInputViewHolder -> {
                outRect.top = space8
                outRect.bottom = space16
            }
            is PromoEligibilityHeaderViewHolder -> {
                outRect.top = 0
                outRect.bottom = space16
            }
            is PromoRecommendationViewHolder -> {
                outRect.top = 0
                outRect.bottom = space12
            }
            is PromoTabViewHolder -> {
                outRect.top = 0
                outRect.bottom = space16
            }
            is PromoListHeaderEnabledViewHolder -> {
                outRect.top = 0
                outRect.bottom = space16
            }
            is PromoListItemViewHolder -> {
                outRect.top = 0
                outRect.bottom = space12
            }
            is PromoEmptyStateViewHolder -> {
                outRect.top = space8
                outRect.bottom = 0
            }
            else -> {
                outRect.top = 0
                outRect.bottom = 0
            }
        }

    }

}