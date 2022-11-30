package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcItemOtherPeriodBinding

class OtherPeriodAdapter: RecyclerView.Adapter<OtherPeriodAdapter.ViewHolder>() {

    private var data: List<String> = emptyList()
    //private var onClickListener: (voucherStatuses: List<VoucherStatus>, statusText: String) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemOtherPeriodBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun setDataList(newData: List<String>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    inner class ViewHolder(
        private val binding: SmvcItemOtherPeriodBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                tfText.text = item
            }
        }
    }
}
