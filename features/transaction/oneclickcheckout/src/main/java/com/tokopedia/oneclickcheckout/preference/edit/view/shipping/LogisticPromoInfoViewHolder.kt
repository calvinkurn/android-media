package com.tokopedia.oneclickcheckout.preference.edit.view.shipping

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.LogisticPromoInfo
import com.tokopedia.unifycomponents.ticker.Ticker

class LogisticPromoInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(logisticPromoInfo: LogisticPromoInfo) {
        val ticker = itemView.findViewById<Ticker>(R.id.ticker_logistic_promo_info)
        ticker.setTextDescription(itemView.context.getString(R.string.new_logistic_promo_info_desc))
    }
}