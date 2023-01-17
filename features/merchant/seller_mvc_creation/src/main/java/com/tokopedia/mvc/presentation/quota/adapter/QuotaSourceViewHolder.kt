package com.tokopedia.mvc.presentation.quota.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.utils.extension.applyRoundedRectangle
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemQuotaSourceBinding
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota

class QuotaSourceViewHolder(
    private val binding: SmvcItemQuotaSourceBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: VoucherCreationQuota.Sources) {
        with(binding) {
            tfText.text = item.name
            tfQuotaUsage.text = item.used.toString()
            tfQuotaTotal.text = root.context.getString(
                R.string.smvc_quota_info_quota_total_format,
                item.total
            )
            tfDate.text = item.expired
            viewBg.applyRoundedRectangle(com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        }
    }
}
