package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.R

class ShareExLineSeparatorViewHolder(
    itemView: View
): AbstractViewHolder<ShareExSeparatorUiModel>(itemView) {
    override fun bind(element: ShareExSeparatorUiModel) {
        // no-op
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_separator_item
    }
}
