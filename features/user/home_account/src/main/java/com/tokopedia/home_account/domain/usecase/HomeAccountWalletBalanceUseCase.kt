package com.tokopedia.home_account.domain.usecase

import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import com.tokopedia.navigation_common.model.WalletAction
import com.tokopedia.navigation_common.model.WalletModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 20/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountWalletBalanceUseCase @Inject constructor(
        private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
        private val getPendingCasbackUseCase: GetPendingCasbackUseCase
)
    : UseCase<WalletModel>() {

    override fun createObservable(p0: RequestParams): Observable<WalletModel> {
        return getWalletBalanceUseCase.createObservable(RequestParams.EMPTY)
                .flatMap(Func1<WalletBalanceModel, Observable<WalletBalanceModel>> {
                    if (!it.link) {
                        return@Func1 Observable.zip(Observable.just(it),
                                getPendingCasbackUseCase.createObservable(RequestParams.EMPTY)) { balanceTokoCash1, pendingCashback1 ->
                            balanceTokoCash1.pendingCashback = pendingCashback1.amountText
                            balanceTokoCash1.amountPendingCashback = pendingCashback1.amount
                            balanceTokoCash1
                        }
                    }
                    return@Func1 Observable.just(it)
                }).map { mapper(it) }

    }

    fun mapper(walletBalance: WalletBalanceModel): WalletModel {
        val walletModel = WalletModel()
        walletModel.applink = walletBalance.applinks
        walletModel.text = walletBalance.titleText
        walletModel.balance = walletBalance.balance
        walletModel.isLinked = walletBalance.link

        val walletAction = WalletAction()
        walletBalance.actionBalanceModel?.let {
            walletAction.applink = it.applinks
            walletAction.text = it.labelAction
            walletAction.redirectUrl = it.redirectUrl
        }
        walletModel.action = walletAction
        walletModel.pointBalance = walletBalance.pointBalance
        walletModel.cashBalance = walletBalance.cashBalance
        walletModel.walletType = walletBalance.walletType

        walletModel.pendingCashback = walletBalance.pendingCashback
        walletModel.amountPendingCashback = walletBalance.amountPendingCashback
        return walletModel
    }
}