package com.tokopedia.updateinactivephone.revamp.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.domain.data.UserDetailDataModel
import com.tokopedia.updateinactivephone.revamp.view.viewholder.AccountListViewHolder

class AccountListAdapterDelegate(
        private val listener: AccountListViewHolder.Listener
) : TypedAdapterDelegate<UserDetailDataModel, UserDetailDataModel, AccountListViewHolder>(R.layout.item_inactive_phone_account_list) {

    override fun onBindViewHolder(item: UserDetailDataModel, holderList: AccountListViewHolder) {
        holderList.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): AccountListViewHolder {
        return AccountListViewHolder(basicView, listener)
    }

}