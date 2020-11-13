package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.holder.HomeNavGlobalErrorViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavMenuViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTickerViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTitleViewHolder
import com.tokopedia.homenav.base.viewmodel.HomeNavGlobalErrorViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTickerViewModel
import com.tokopedia.homenav.base.viewmodel.HomeNavTitleViewModel
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.*
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.*
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

    override fun type(visitable: HomeNavTickerViewModel): Int {
        return HomeNavTickerViewHolder.LAYOUT
    }

    override fun type(initialShimmerAccountDataModel: InitialShimmerAccountDataModel): Int {
        return InitialShimmeringAccountViewHolder.LAYOUT
    }

    override fun type(initialShimmerTransactionDataModel: InitialShimmerTransactionDataModel): Int {
        return InitialShimmeringTransactionViewHolder.LAYOUT
    }

    override fun type(initialShimmerBuListDataModel: InitialShimmerBuListDataModel): Int {
        return InitialShimmeringBuListViewHolder.LAYOUT
    }

    override fun type(initialShimmerMenuDataModel: InitialShimmerMenuDataModel): Int {
        return InitialShimmeringUserMenuViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            HomeNavMenuViewHolder.LAYOUT -> HomeNavMenuViewHolder(view, mainNavListener)
            AccountHeaderViewHolder.LAYOUT -> AccountHeaderViewHolder(view, mainNavListener, userSession)
            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view, mainNavListener)
            TransactionListViewHolder.LAYOUT -> TransactionListViewHolder(view, mainNavListener)
            HomeNavTickerViewHolder.LAYOUT -> HomeNavTickerViewHolder(view)
            InitialShimmeringAccountViewHolder.LAYOUT -> InitialShimmeringAccountViewHolder(view)
            InitialShimmeringTransactionViewHolder.LAYOUT -> InitialShimmeringTransactionViewHolder(view)
            InitialShimmeringBuListViewHolder.LAYOUT -> InitialShimmeringBuListViewHolder(view)
            InitialShimmeringUserMenuViewHolder.LAYOUT -> InitialShimmeringUserMenuViewHolder(view)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<Visitable<*>>
    }

}