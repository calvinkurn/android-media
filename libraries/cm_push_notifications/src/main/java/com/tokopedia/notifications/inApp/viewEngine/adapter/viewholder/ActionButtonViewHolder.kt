package com.tokopedia.notifications.inApp.viewEngine.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.mapper.InAppButtonMapper.mapBtnSize
import com.tokopedia.notifications.data.mapper.InAppButtonMapper.mapBtnType
import com.tokopedia.notifications.data.mapper.InAppButtonMapper.mapBtnVariant
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.unifycomponents.UnifyButton
import kotlin.LazyThreadSafetyMode.NONE

open class ActionButtonViewHolder(
        view: View,
        private val onClick: (CMButton, CMInApp) -> Unit
): RecyclerView.ViewHolder(view) {

    private val btnAction: UnifyButton = view.findViewById(R.id.btnAction)

    fun bind(cmInApp: CMInApp, button: CMButton?) {
        if (button == null) return
        btnLayout(button)

        btnAction.text = button.getTxt()
        btnAction.setOnClickListener {
            onClick(button, cmInApp)
        }
    }

    private fun btnLayout(button: CMButton) {
        btnAction.buttonType = mapBtnType(button.unifyType)
        btnAction.buttonSize = mapBtnSize(button.unifySize)
        btnAction.buttonVariant = mapBtnVariant(button.unifyVariant)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.cm_layout_inapp_action_button

        fun create(
                parent: ViewGroup,
                onClick: (CMButton, CMInApp) -> Unit
        ): ActionButtonViewHolder {
            val layout = LayoutInflater
                    .from(parent.context)
                    .inflate(LAYOUT, parent, false)
            return ActionButtonViewHolder(layout, onClick)
        }

    }

}