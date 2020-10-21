package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.AccountHeaderViewHolder
import com.tokopedia.homenav.base.diffutil.holder.CommonNavItemViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.SeparatorViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.TransactionListViewHolder
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.base.viewmodel.CommonNavItemViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.TransactionListItemViewModel

class MainNavTypeFactoryImpl(private val mainNavListener: MainNavListener) : HomeNavTypeFactory, MainNavTypeFactory {

    override fun type(accountHeaderViewModel: AccountHeaderViewModel): Int {
        return AccountHeaderViewHolder.LAYOUT
    }

    override fun type(commonNavItemViewModel: CommonNavItemViewModel): Int {
        return CommonNavItemViewHolder.LAYOUT
    }

    override fun type(separatorViewModel: SeparatorViewModel): Int {
        return SeparatorViewHolder.LAYOUT
    }

    override fun type(transactionListItemViewModel: TransactionListItemViewModel): Int {
        return TransactionListViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        return when (viewType) {
            AccountHeaderViewHolder.LAYOUT -> AccountHeaderViewHolder(view, mainNavListener)
            CommonNavItemViewHolder.LAYOUT -> CommonNavItemViewHolder(view)
            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view, mainNavListener)
            TransactionListViewHolder.LAYOUT -> TransactionListViewHolder(view, mainNavListener)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<HomeNavVisitable>
    }

}