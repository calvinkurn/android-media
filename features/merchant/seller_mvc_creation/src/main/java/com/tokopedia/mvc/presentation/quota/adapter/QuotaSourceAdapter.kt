package com.tokopedia.mvc.presentation.quota.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcItemQuotaSourceBinding
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota

class QuotaSourceAdapter: RecyclerView.Adapter<QuotaSourceViewHolder>() {
    private var data: List<VoucherCreationQuota.Sources> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotaSourceViewHolder {
        val binding = SmvcItemQuotaSourceBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return QuotaSourceViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: QuotaSourceViewHolder, position: Int) {
        data.getOrNull(position)?.let { holder.bind(it) }
    }

    fun setDataList(newData: List<VoucherCreationQuota.Sources>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }
}
