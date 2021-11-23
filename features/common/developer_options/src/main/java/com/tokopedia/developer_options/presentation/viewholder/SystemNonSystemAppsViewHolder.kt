package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel
import com.tokopedia.unifycomponents.UnifyButton

class SystemNonSystemAppsViewHolder(
    itemView: View,
    private val listener: SystemNonSystemAppsListener
): AbstractViewHolder<SystemNonSystemAppsUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_system_and_non_system_apps
    }

    override fun bind(element: SystemNonSystemAppsUiModel?) {
        val systemBtn = itemView.findViewById<UnifyButton>(R.id.system_apps)
        val nonSystemBtn = itemView.findViewById<UnifyButton>(R.id.non_system_apps)
        systemBtn.setOnClickListener {
            listener.onClickSystemAppsBtn()
        }
        nonSystemBtn.setOnClickListener {
            listener.onClickNonSystemAppsBtn()
        }
    }

    interface SystemNonSystemAppsListener {
        fun onClickSystemAppsBtn()
        fun onClickNonSystemAppsBtn()
    }
}