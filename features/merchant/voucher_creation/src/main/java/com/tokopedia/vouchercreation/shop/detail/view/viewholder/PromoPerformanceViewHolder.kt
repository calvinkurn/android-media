package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherPromoPerformanceBinding
import com.tokopedia.vouchercreation.shop.detail.model.PromoPerformanceUiModel
import java.util.Locale

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

    private var binding: ItemMvcVoucherPromoPerformanceBinding? by viewBinding()

    override fun bind(element: PromoPerformanceUiModel) {
        binding?.apply {
            tvMvcNominalTotalSpending.text = element.totalSpending
            tvMvcPromoUsedQuota.text = element.voucherUsed.toString()
            tvMvcPromoTotalQuota.text = String.format(Locale.getDefault(),"/%d",element.voucherQuota)
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
