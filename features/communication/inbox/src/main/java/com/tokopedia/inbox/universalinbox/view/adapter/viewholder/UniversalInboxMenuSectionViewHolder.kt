package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSectionUiModel
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxSectionItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxMenuSectionViewHolder(itemView: View): BaseViewHolder(itemView) {

    private val binding: UniversalInboxSectionItemBinding? by viewBinding()

    fun bind(uiModel: UniversalInboxMenuSectionUiModel) {
        binding?.inboxTvSection?.text = uiModel.text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_section_item
    }
}
