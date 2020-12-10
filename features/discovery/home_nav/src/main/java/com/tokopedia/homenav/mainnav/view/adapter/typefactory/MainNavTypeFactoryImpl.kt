package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
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
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface

class MainNavTypeFactoryImpl(private val mainNavListener: MainNavListener,
                             private val userSession: UserSessionInterface,
                             private val remoteConfig: RemoteConfig)
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

    override fun type(initialShimmerDataModel: InitialShimmerDataModel): Int {
        return InitialShimmeringDataViewHolder.LAYOUT
    }

    override fun type(initialShimmerProfileDataModel: InitialShimmerProfileDataModel): Int {
        return InitialShimmeringProfileDataViewHolder.LAYOUT
    }

    override fun type(initialShimmerTransactionDataModel: InitialShimmerTransactionDataModel): Int {
        return InitialShimmeringTransactionDataViewHolder.LAYOUT
    }

    override fun type(errorStateBuViewModel: ErrorStateBuViewModel): Int {
        return ErrorStateBuViewHolder.LAYOUT
    }

    override fun type(errorStateOngoingTransactionModel: ErrorStateOngoingTransactionModel): Int {
        return ErrorStateOngoingTransactionViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            HomeNavMenuViewHolder.LAYOUT -> HomeNavMenuViewHolder(view, mainNavListener)
            AccountHeaderViewHolder.LAYOUT -> AccountHeaderViewHolder(view, mainNavListener, remoteConfig, userSession)
            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view, mainNavListener)
            TransactionListViewHolder.LAYOUT -> TransactionListViewHolder(view, mainNavListener)
            HomeNavTickerViewHolder.LAYOUT -> HomeNavTickerViewHolder(view, mainNavListener)
            ErrorStateBuViewHolder.LAYOUT -> ErrorStateBuViewHolder(view, mainNavListener)
            ErrorStateOngoingTransactionViewHolder.LAYOUT -> ErrorStateOngoingTransactionViewHolder(view, mainNavListener)
            InitialShimmeringDataViewHolder.LAYOUT -> InitialShimmeringDataViewHolder(view)
            InitialShimmeringProfileDataViewHolder.LAYOUT -> InitialShimmeringProfileDataViewHolder(view)
            InitialShimmeringTransactionDataViewHolder.LAYOUT -> InitialShimmeringTransactionDataViewHolder(view)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<Visitable<*>>
    }

}