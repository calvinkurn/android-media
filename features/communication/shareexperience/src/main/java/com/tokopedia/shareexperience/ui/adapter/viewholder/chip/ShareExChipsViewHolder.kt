package com.tokopedia.shareexperience.ui.adapter.viewholder.chip

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.ui.model.ShareExChipsUiModel
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceChipsItemBinding
import com.tokopedia.shareexperience.ui.adapter.ShareExChipsAdapter
import com.tokopedia.utils.view.binding.viewBinding

class ShareExChipsViewHolder(itemView: View): AbstractViewHolder<ShareExChipsUiModel>(itemView) {

    private val binding: ShareexperienceChipsItemBinding? by viewBinding()

    override fun bind(element: ShareExChipsUiModel) {
        binding?.shareexRvChip?.updateData(element.listChip)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_chips_item
    }
}
