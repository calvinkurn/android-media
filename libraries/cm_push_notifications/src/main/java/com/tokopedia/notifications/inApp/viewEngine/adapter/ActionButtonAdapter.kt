package com.tokopedia.notifications.inApp.viewEngine.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMButton
import com.tokopedia.notifications.inApp.viewEngine.adapter.viewholder.ActionButtonViewHolder

open class ActionButtonAdapter(
        private val buttons: List<CMButton>,
        private val onClick: () -> Unit
): RecyclerView.Adapter<ActionButtonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionButtonViewHolder {
        return ActionButtonViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: ActionButtonViewHolder, position: Int) {
        holder.bind(buttons[position])
    }

    override fun getItemCount(): Int = buttons.size

}