package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.viewmodel.*

interface MainNavTypeFactory {

    fun type(accountHeaderViewModel: AccountHeaderViewModel): Int

    fun type(separatorViewModel: SeparatorViewModel) : Int

    fun type(transactionListItemViewModel: TransactionListItemViewModel) : Int

    fun type(initialShimmerDataModel: InitialShimmerDataModel) : Int

    fun type(initialShimmerProfileDataModel: InitialShimmerProfileDataModel) : Int

    fun type(errorStateBuViewModel: ErrorStateBuViewModel): Int

    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<*>
}