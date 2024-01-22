package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.scp.auth.common.utils.ScpUtils.getIsEnableSharedPrefScpLogin
import com.scp.auth.common.utils.ScpUtils.setIsEnableSharedPrefScpLogin
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ForceScpLoginUiModel
import com.tokopedia.developer_options.tracker.DevOpsTracker
import com.tokopedia.developer_options.tracker.DevopsFeature
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ForceScpLoginViewHolder(
    itemView: View
) : AbstractViewHolder<ForceScpLoginUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_force_scp_login
    }

    override fun bind(element: ForceScpLoginUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.force_scp_login_cb)
        cb.isChecked = itemView.context.getIsEnableSharedPrefScpLogin()
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            DevOpsTracker.trackEntryEvent(DevopsFeature.USE_SCP)
            itemView.context.setIsEnableSharedPrefScpLogin(state)
        }
    }
}
