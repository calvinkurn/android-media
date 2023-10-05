package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel

class UniversalInboxMenuSeparatorViewHolder(
    itemView: View
) : AbstractViewHolder<UniversalInboxMenuSeparatorUiModel>(itemView) {

    override fun bind(element: UniversalInboxMenuSeparatorUiModel?) {
        // No-op
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_separator_item
    }
}
