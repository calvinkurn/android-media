package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel

class AccountHeaderViewHolder (itemView: View,
                               mainNavListener: MainNavListener
): AbstractViewHolder<AccountHeaderViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_account_header
    }

    override fun bind(element: AccountHeaderViewModel) {
    }
}