package com.tokopedia.updateinactivephone.features.accountlist

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel

class AccountListAdapterDelegate(
        private val listener: AccountListViewHolder.Listener
) : TypedAdapterDelegate<AccountListDataModel.UserDetailDataModel, AccountListDataModel.UserDetailDataModel, AccountListViewHolder>(
    AccountListViewHolder.LAYOUT
) {

    override fun onBindViewHolder(item: AccountListDataModel.UserDetailDataModel, holder: AccountListViewHolder) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): AccountListViewHolder {
        return AccountListViewHolder(basicView, listener)
    }

}