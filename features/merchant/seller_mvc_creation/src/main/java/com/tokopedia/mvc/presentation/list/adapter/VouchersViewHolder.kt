package com.tokopedia.mvc.presentation.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvc.databinding.SmvcItemVoucherBinding

class VouchersViewHolder(private val binding: SmvcItemVoucherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(title: String) {
        val numberText = "${adapterPosition.inc()}."
        binding.headerContent.tfStatusTitle.text = numberText + title
    }
}
