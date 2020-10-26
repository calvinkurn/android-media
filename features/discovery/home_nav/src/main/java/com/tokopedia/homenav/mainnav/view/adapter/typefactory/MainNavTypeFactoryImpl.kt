package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.holder.HomeNavGlobalErrorViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavMenuViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTitleViewHolder
import com.tokopedia.homenav.base.viewmodel.HomeNavGlobalErrorViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTitleViewModel
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.AccountHeaderViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.SeparatorViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.TransactionListViewHolder
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.SeparatorViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.TransactionListItemViewModel
import com.tokopedia.user.session.UserSessionInterface

class MainNavTypeFactoryImpl(private val mainNavListener: MainNavListener,
                             private val userSession: UserSessionInterface)
    : HomeNavTypeFactory, MainNavTypeFactory {

    override fun type(accountHeaderViewModel: AccountHeaderViewModel): Int {
        return AccountHeaderViewHolder.LAYOUT
    }

    override fun type(homeNavMenuViewModel: HomeNavMenuViewModel): Int {
        return HomeNavMenuViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavTitleViewModel): Int {
        return HomeNavTitleViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavGlobalErrorViewModel): Int {
        return HomeNavGlobalErrorViewHolder.LAYOUT
    }

    override fun type(separatorViewModel: SeparatorViewModel): Int {
        return SeparatorViewHolder.LAYOUT
    }

    override fun type(transactionListItemViewModel: TransactionListItemViewModel): Int {
        return TransactionListViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<HomeNavVisitable> {
        return when (viewType) {
            HomeNavMenuViewHolder.LAYOUT -> HomeNavMenuViewHolder(view, mainNavListener)
            AccountHeaderViewHolder.LAYOUT -> AccountHeaderViewHolder(view, mainNavListener, userSession)
            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view, mainNavListener)
            TransactionListViewHolder.LAYOUT -> TransactionListViewHolder(view, mainNavListener)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<HomeNavVisitable>
    }

}