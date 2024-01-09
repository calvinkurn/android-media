package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.BranchLinkUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class BranchLinkViewHolder(
    private val listener: BranchListener,
    itemView: View
) : AbstractViewHolder<BranchLinkUiModel>(itemView) {

    override fun bind(element: BranchLinkUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.branclink_btn)
        val tfBranch = itemView.findViewById<TextFieldUnify>(R.id.tf_branchlink)
        btn.setOnClickListener {
            DevOpsTracker.trackEntryEvent(DevopsFeature.EXTRACT_BRANCH_LINK)
            listener.onClick(tfBranch.textFieldInput.text.toString())
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_branchlink
    }

    interface BranchListener {
        fun onClick(link: String)
    }
}
