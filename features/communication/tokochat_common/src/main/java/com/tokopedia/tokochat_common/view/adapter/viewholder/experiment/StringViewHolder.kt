package com.tokopedia.tokochat_common.view.adapter.viewholder.experiment

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.ItemTokochatStringBinding
import com.tokopedia.tokochat_common.view.uimodel.StringUiModel
import com.tokopedia.utils.view.binding.viewBinding

class StringViewHolder(itemView: View): BaseViewHolder(itemView) {

    private val binding: ItemTokochatStringBinding? by viewBinding()

    fun bind(string: StringUiModel) {
        binding?.stringTv?.text = string.string
    }

    companion object {
        val LAYOUT = R.layout.item_tokochat_string
    }
}
