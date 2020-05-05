package com.tokopedia.vouchercreation.detail.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.PromoPerformanceUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_promo_performance.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class PromoPerformanceViewHolder(itemView: View?) : AbstractViewHolder<PromoPerformanceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_promo_performance
    }

    @SuppressLint("SetTextI18n")
    override fun bind(element: PromoPerformanceUiModel) {
        with(itemView) {
            tvMvcNominalTotalSpending.text = element.totalSpending
            tvMvcPromoUsedQuota.text = element.voucherUsed.toString()
            tvMvcPromoTotalQuota.text = "/${element.voucherQuota}"
        }
    }
}