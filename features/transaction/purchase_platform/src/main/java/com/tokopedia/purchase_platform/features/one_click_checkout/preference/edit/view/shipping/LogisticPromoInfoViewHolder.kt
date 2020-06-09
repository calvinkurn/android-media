package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.LogisticPromoInfo

class LogisticPromoInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val ivLogisticPromoInfo: ImageView = itemView.findViewById(R.id.iv_logistic_promo_info)

    fun bind(logisticPromoInfo: LogisticPromoInfo) {
        ImageHandler.LoadImage(ivLogisticPromoInfo, logisticPromoInfo.imageUrl)
    }

}