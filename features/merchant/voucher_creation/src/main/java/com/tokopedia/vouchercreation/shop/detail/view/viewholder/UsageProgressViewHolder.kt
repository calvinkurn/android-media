package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcUsageProgressBinding
import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.shop.detail.model.UsageProgressUiModel

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class UsageProgressViewHolder(
        itemView: View?,
        private val tooltipListener: (String, String) -> Unit
) : AbstractViewHolder<UsageProgressUiModel>(itemView) {

    companion object {
        private const val PERCENT = 100
        private const val MVC_PROGRESS_HEIGHT = 6
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_usage_progress
    }

    private var binding: ItemMvcUsageProgressBinding? by viewBinding()

    override fun bind(element: UsageProgressUiModel) {
        binding?.apply {
            val progressBarValue = (element.confirmedQuota / element.quota) * PERCENT
            progressMvcUsage.setValue(progressBarValue, true)
            progressMvcUsage.progressBarHeight = progressMvcUsage.context.pxToDp(MVC_PROGRESS_HEIGHT).toInt()

            tvMvcUsedQuota.text = element.confirmedQuota.toString()
            tvMvcTotalQuota.text = String.format(tvMvcTotalQuota.context.getString(R.string.mvc_detail_total_quota).toBlankOrString(), element.quota.toString())

            mvcImgInfo.setOnClickListener {
                setOnTooltipClick()
            }

            when(element.targetType) {
                VoucherTargetType.PUBLIC -> {
                    tvMvcTickerUsage.text =
                        String.format(tvMvcTickerUsage.context.getString(R.string.mvc_detail_promo_usage_used), element.bookedQuota.toString()).parseAsHtml()
                    tvMvcTickerUsage.visible()
                }
                VoucherTargetType.PRIVATE -> {
                    tvMvcTickerUsage.gone()
                }
                else -> {
                    tvMvcTickerUsage.gone()
                }
            }

        }
    }

    private fun setOnTooltipClick() {
        val title = itemView.context.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses)
        val content = itemView.context?.getString(R.string.mvc_detail_expenses_info).toBlankOrString()
        tooltipListener(title, content)
    }
}