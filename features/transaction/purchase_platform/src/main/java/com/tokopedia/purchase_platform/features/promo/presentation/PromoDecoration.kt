package com.tokopedia.purchase_platform.features.promo.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoEligibilityHeaderViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoInputViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoListHeaderViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.viewholder.PromoRecommendationViewHolder
import javax.inject.Inject


class PromoDecoration @Inject constructor() : RecyclerView.ItemDecoration() {

    lateinit var adapter: PromoCheckoutAdapter

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        when (parent.getChildViewHolder(view)) {
            is PromoRecommendationViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
            is PromoInputViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            is PromoEligibilityHeaderViewHolder -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
            is PromoListHeaderViewHolder -> {
                if ((adapter.data[position] as PromoListHeaderUiModel).uiState.isEnabled) {
                    outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_2)?.toInt() ?: 0
                    outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                } else {
                    outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                    outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                }
            }
            else -> {
                outRect.top = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
                outRect.bottom = parent.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
            }
        }

    }

}