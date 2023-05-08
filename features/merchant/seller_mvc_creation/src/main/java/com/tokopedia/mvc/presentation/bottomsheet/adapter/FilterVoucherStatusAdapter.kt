package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.databinding.SmvcItemFilterVoucherStatusBinding
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherStatusFilter

class FilterVoucherStatusAdapter: RecyclerView.Adapter<FilterVoucherStatusAdapter.ViewHolder>() {

    private var data: List<VoucherStatusFilter> = emptyList()
    private var onClickListener: (voucherStatuses: List<VoucherStatus>, statusText: String) -> Unit = { _, _ -> }
    private var selectedStatusText = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemFilterVoucherStatusBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding, onClickListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu, selectedStatusText)
        }
    }

    fun setDataList(newData: List<VoucherStatusFilter>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setOnClickListener(listener: (voucherStatuses: List<VoucherStatus>, statusText: String) -> Unit) {
        onClickListener = listener
    }

    fun setSelected(statusText: String) {
        selectedStatusText = statusText
        notifyItemRangeChanged(Int.ZERO, data.size)
    }

    inner class ViewHolder(
        private val binding: SmvcItemFilterVoucherStatusBinding,
        private val onClickListener: (voucherStatuses: List<VoucherStatus>, statusText: String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VoucherStatusFilter, selectedStatusText: String) {
            with(binding) {
                tfText.text = root.context.getString(item.captionRes)
                root.setOnClickListener { onClickListener(item.types, tfText.text.toString() ) }
                iconCheck.isVisible = tfText.text == selectedStatusText
            }
        }
    }
}
