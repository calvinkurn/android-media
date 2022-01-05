package com.tokopedia.promocheckoutmarketplace.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.*
import javax.inject.Inject

class PromoCheckoutDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var verticalSpaceHeight = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (verticalSpaceHeight == -1) {
            verticalSpaceHeight = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt()
                    ?: 0
        }

        when (parent.getChildViewHolder(view)) {
            is PromoInputViewHolder -> {
                outRect.top = (verticalSpaceHeight * 0.5).toInt()
                outRect.bottom = verticalSpaceHeight
            }
            is PromoEligibilityHeaderViewHolder -> {
                outRect.top = 0
                outRect.bottom = verticalSpaceHeight
            }
            is PromoRecommendationViewHolder -> {
                outRect.top = 0
                outRect.bottom = (verticalSpaceHeight * 0.75).toInt()
            }
            is PromoTabViewHolder -> {
                outRect.top = 0
                outRect.bottom = verticalSpaceHeight
            }
            is PromoListHeaderEnabledViewHolder -> {
                outRect.top = 0
                outRect.bottom = verticalSpaceHeight
            }
            is PromoListItemViewHolder -> {
                outRect.top = 0
                outRect.bottom = (verticalSpaceHeight * 0.75).toInt()
            }
            is PromoEmptyStateViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
            }
            else -> {
                outRect.top = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
            }
        }

    }

}