package com.tokopedia.updateinactivephone.view.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.view.adapter.delegate.AccountListAdapterDelegate
import com.tokopedia.updateinactivephone.view.viewholder.AccountListViewHolder

class AccountListAdapter(
        listener: AccountListViewHolder.Listener
) : BaseAdapter<AccountListDataModel.UserDetailDataModel>() {

    init {
        delegatesManager.addDelegate(AccountListAdapterDelegate(listener))
    }

    fun addAccountList(list: MutableList<AccountListDataModel.UserDetailDataModel>) {
        setItems(list)
    }
}