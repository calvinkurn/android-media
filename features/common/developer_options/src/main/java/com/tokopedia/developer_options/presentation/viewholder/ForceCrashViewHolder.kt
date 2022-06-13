package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import com.tokopedia.developer_options.presentation.model.ForceCrashUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ForceCrashViewHolder(
    itemView: View
): AbstractViewHolder<ForceCrashUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_force_crash
    }

    override fun bind(element: ForceCrashUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.force_crash_btn)
        btn.setOnClickListener {
            throw DeveloperOptionActivity.DeveloperOptionException("Throw Runtime Exception")
        }
    }
}