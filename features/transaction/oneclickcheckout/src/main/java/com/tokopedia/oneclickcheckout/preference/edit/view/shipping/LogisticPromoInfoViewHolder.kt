package com.tokopedia.oneclickcheckout.preference.edit.view.shipping

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.LogisticPromoInfo

class LogisticPromoInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val ivLogisticPromoInfo: ImageView = itemView.findViewById(R.id.iv_logistic_promo_info)

    fun bind(logisticPromoInfo: LogisticPromoInfo) {
        ImageHandler.LoadImage(ivLogisticPromoInfo, logisticPromoInfo.imageUrl)
    }

}