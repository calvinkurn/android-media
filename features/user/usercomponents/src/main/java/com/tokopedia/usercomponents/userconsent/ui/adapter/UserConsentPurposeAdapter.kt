package com.tokopedia.usercomponents.userconsent.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.usercomponents.userconsent.common.UserConsentPurposeUiModel

class UserConsentPurposeAdapter(
    listener: UserConsentPurposeViewHolder.UserConsentPurposeListener
): BaseAdapter<UserConsentPurposeUiModel>() {

    var listCheckBoxView: MutableList<CheckboxUnify> = mutableListOf()

    init {
        delegatesManager.addDelegate(UserConsentPurposeAdapterDelegate(listener))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemView is CheckboxUnify) {
            listCheckBoxView.add(holder.itemView as CheckboxUnify)
        }

        super.onBindViewHolder(holder, position)
    }
}