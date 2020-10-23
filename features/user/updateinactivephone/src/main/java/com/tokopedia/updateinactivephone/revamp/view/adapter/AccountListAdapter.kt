package com.tokopedia.updateinactivephone.revamp.view.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.updateinactivephone.revamp.domain.data.UserDetailDataModel
import com.tokopedia.updateinactivephone.revamp.view.adapter.delegate.AccountListAdapterDelegate
import com.tokopedia.updateinactivephone.revamp.view.viewholder.AccountListViewHolder

class AccountListAdapter(
        listener: AccountListViewHolder.Listener
): BaseAdapter<UserDetailDataModel>() {

    init {
        delegatesManager.addDelegate(AccountListAdapterDelegate(listener))
    }

    fun addAccountList(list: MutableList<UserDetailDataModel>) {
        setItems(list)
    }
}