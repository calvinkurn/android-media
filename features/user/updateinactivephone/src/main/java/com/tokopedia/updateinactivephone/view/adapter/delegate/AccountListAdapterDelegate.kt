package com.tokopedia.updateinactivephone.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.view.viewholder.AccountListViewHolder

class AccountListAdapterDelegate(
        private val listener: AccountListViewHolder.Listener
) : TypedAdapterDelegate<AccountListDataModel.UserDetailDataModel, AccountListDataModel.UserDetailDataModel, AccountListViewHolder>(R.layout.item_inactive_phone_account_list) {

    override fun onBindViewHolder(item: AccountListDataModel.UserDetailDataModel, holderList: AccountListViewHolder) {
        holderList.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): AccountListViewHolder {
        return AccountListViewHolder(basicView, listener)
    }

}