package com.tokopedia.tokofood.feature.purchase.promopage.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder.TokoFoodPromoEligibilityHeaderViewHolder
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder.TokoFoodPromoHeaderViewHolder
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder.TokoFoodPromoItemViewHolder
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder.TokoFoodPromoTickerViewHolder
import javax.inject.Inject

class TokoFoodPromoDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    private var space16 = -Int.ONE
    private var space12 = -Int.ONE
    private var space8 = -Int.ONE

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (space16 == -Int.ONE) {
            space16 = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0
            space12 = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_12)?.toInt() ?: Int.ZERO
            space8 = parent.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)?.toInt() ?: Int.ZERO
        }

        when (parent.getChildViewHolder(view)) {
            is TokoFoodPromoHeaderViewHolder -> {
                outRect.top = space16
                outRect.bottom = Int.ZERO
            }
            is TokoFoodPromoTickerViewHolder -> {
                outRect.top = space16
                outRect.bottom = Int.ZERO
            }
            is TokoFoodPromoItemViewHolder -> {
                outRect.top = space16
                outRect.bottom = Int.ZERO
            }
            is TokoFoodPromoEligibilityHeaderViewHolder -> {
                outRect.top = space16
                outRect.bottom = Int.ZERO
            }
            else -> {
                outRect.top = Int.ZERO
                outRect.bottom = Int.ZERO
            }
        }
    }

}