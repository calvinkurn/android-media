package com.tokopedia.tokofood.purchase.promopage.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.purchase.promopage.presentation.viewholder.TokoFoodPromoEligibilityHeaderViewHolder
import com.tokopedia.tokofood.purchase.promopage.presentation.viewholder.TokoFoodPromoHeaderViewHolder
import com.tokopedia.tokofood.purchase.promopage.presentation.viewholder.TokoFoodPromoItemViewHolder
import com.tokopedia.tokofood.purchase.promopage.presentation.viewholder.TokoFoodPromoTickerViewHolder
import javax.inject.Inject

class TokoFoodPromoDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

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
            is TokoFoodPromoHeaderViewHolder -> {
                outRect.top = space16
                outRect.bottom = 0
            }
            is TokoFoodPromoTickerViewHolder -> {
                outRect.top = space16
                outRect.bottom = 0
            }
            is TokoFoodPromoItemViewHolder -> {
                outRect.top = space16
                outRect.bottom = 0
            }
            is TokoFoodPromoEligibilityHeaderViewHolder -> {
                outRect.top = space16
                outRect.bottom = 0
            }
            else -> {
                outRect.top = 0
                outRect.bottom = 0
            }
        }
    }

}