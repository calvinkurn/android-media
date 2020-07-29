package com.tokopedia.promocheckoutmarketplace.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoEligibilityHeaderViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoEmptyStateViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoListHeaderEnabledViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoRecommendationViewHolder
import javax.inject.Inject

class PromoCheckoutDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (parent.getChildViewHolder(view)) {
            is PromoRecommendationViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt() ?: 0
            }
            is PromoEligibilityHeaderViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
            }
            is PromoListHeaderEnabledViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_2)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
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