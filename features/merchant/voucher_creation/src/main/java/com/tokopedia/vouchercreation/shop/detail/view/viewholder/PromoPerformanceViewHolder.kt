package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.PromoPerformanceUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_promo_performance.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class PromoPerformanceViewHolder(
        itemView: View?,
        private val onTooltipClick: (String, String) -> Unit
) : AbstractViewHolder<PromoPerformanceUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_promo_performance
    }

    override fun bind(element: PromoPerformanceUiModel) {
        with(itemView) {
            tvMvcNominalTotalSpending.text = element.totalSpending
            tvMvcPromoUsedQuota.text = element.voucherUsed.toString()
            tvMvcPromoTotalQuota.text = String.format("/%d",element.voucherQuota)
            mvcImgInfo.setOnClickListener {
                setOnTooltipClick()
            }
        }
    }

    private fun setOnTooltipClick() {
        val title = itemView.context.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses)
        val content = itemView.context?.getString(R.string.mvc_detail_expenses_info).toBlankOrString()
        onTooltipClick(title, content)
    }
}