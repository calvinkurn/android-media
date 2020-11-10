package com.tokopedia.vouchercreation.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.detail.model.UsageProgressUiModel
import kotlinx.android.synthetic.main.item_mvc_usage_progress.view.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class UsageProgressViewHolder(
        itemView: View?,
        private val tooltipListener: (String, String) -> Unit
) : AbstractViewHolder<UsageProgressUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_usage_progress
    }

    override fun bind(element: UsageProgressUiModel) {
        with(itemView) {
            val progressBarValue = (element.confirmedQuota / element.quota) * 100
            progressMvcUsage?.setValue(progressBarValue, true)
            progressMvcUsage?.progressBarHeight = context.pxToDp(6).toInt()

            tvMvcUsedQuota?.text = element.confirmedQuota.toString()
            tvMvcTotalQuota?.text = String.format(context?.getString(R.string.mvc_detail_total_quota).toBlankOrString(), element.quota.toString())

            mvcImgInfo.setOnClickListener {
                setOnTooltipClick()
            }

            when(element.targetType) {
                VoucherTargetType.PUBLIC -> {
                    tvMvcTickerUsage?.text =
                            String.format(context.getString(R.string.mvc_detail_promo_usage_used), element.bookedQuota.toString()).parseAsHtml()
                    tvMvcTickerUsage?.visible()
                }
                VoucherTargetType.PRIVATE -> {
                    tvMvcTickerUsage?.gone()
                }
                else -> {
                    tvMvcTickerUsage?.gone()
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