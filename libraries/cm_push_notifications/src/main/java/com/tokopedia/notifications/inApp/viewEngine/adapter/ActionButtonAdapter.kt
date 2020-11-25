package com.tokopedia.notifications.inApp.viewEngine.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.viewEngine.adapter.viewholder.ActionButtonViewHolder

open class ActionButtonAdapter(
        private val data: CMInApp,
        private val onClick: (CMButton, CMInApp) -> Unit
): RecyclerView.Adapter<ActionButtonViewHolder>() {

    private val buttons = data.getCmLayout().button

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionButtonViewHolder {
        return ActionButtonViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: ActionButtonViewHolder, position: Int) {
        holder.bind(data, buttons?.get(position))
    }

    override fun getItemCount(): Int = buttons?.size?: 1

}