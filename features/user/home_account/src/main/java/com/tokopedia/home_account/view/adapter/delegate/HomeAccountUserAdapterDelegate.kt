package com.tokopedia.home_account.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.view.adapter.HomeAccountBalanceAndPointAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.adapter.viewholder.ProfileViewHolder
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserAdapterDelegate(
    val listener: HomeAccountUserListener,
    val tokopediaPlusListener: TokopediaPlusListener,
    private val balanceAndPointAdapter: HomeAccountBalanceAndPointAdapter?,
    private val memberAdapter: HomeAccountMemberAdapter?,
) : TypedAdapterDelegate<ProfileDataView, Any, ProfileViewHolder>(ProfileViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: ProfileDataView, holder: ProfileViewHolder) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = true
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ProfileViewHolder {
        return ProfileViewHolder(basicView, listener, tokopediaPlusListener, balanceAndPointAdapter, memberAdapter)
    }
}