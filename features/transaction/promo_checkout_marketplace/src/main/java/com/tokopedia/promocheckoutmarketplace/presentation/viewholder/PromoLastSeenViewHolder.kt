package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.PromoCheckoutLastSeenListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoLastSeenItemUiModel
import com.tokopedia.unifyprinciples.Typography

class PromoLastSeenViewHolder(view: View, val actionListener: PromoCheckoutLastSeenListener) : RecyclerView.ViewHolder(view) {

    private val labelPromoItemTitle by lazy {
        view.findViewById<Typography>(R.id.label_promo_item_title)
    }
    private val labelPromoCode by lazy {
        view.findViewById<Typography>(R.id.label_promo_code)
    }

    fun bindData(data: PromoLastSeenItemUiModel) {
        labelPromoItemTitle.text = data.uiData.promoTitle
        labelPromoCode.text = data.uiData.promoCode
        itemView.setOnClickListener {
            actionListener.onClickItem(data)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_promo_last_seen
    }

}