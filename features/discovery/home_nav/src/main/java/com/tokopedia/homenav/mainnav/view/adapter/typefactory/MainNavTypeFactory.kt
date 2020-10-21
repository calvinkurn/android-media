package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.base.viewmodel.MainNavItemViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.TransactionListItemViewModel

interface MainNavTypeFactory : HomeNavTypeFactory {

    fun type(accountHeaderViewModel: AccountHeaderViewModel): Int

    fun type(separatorViewModel: SeparatorViewModel) : Int

    fun type(transactionListItemViewModel: TransactionListItemViewModel) : Int

}