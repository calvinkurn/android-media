package com.tokopedia.updateinactivephone.features.accountlist

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel

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