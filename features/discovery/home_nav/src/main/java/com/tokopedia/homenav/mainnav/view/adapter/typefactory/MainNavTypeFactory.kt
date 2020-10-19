package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavItemViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.TransactionListItemViewModel

interface MainNavTypeFactory {

    fun type(accountHeaderViewModel: AccountHeaderViewModel): Int

    fun type(mainNavItemViewModel: MainNavItemViewModel) : Int

    fun type(separatorViewModel: SeparatorViewModel) : Int

    fun type(transactionListItemViewModel: TransactionListItemViewModel) : Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}