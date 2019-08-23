package com.tokopedia.home.account.presentation.subscriber

import com.tokopedia.home.account.presentation.BuyerAccount
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel

class GetBuyerAccountSubscriber(val view: BuyerAccount.View?) :
        BaseAccountSubscriber<BuyerViewModel>(view) {

    override fun onNext(buyerViewModel: BuyerViewModel) {
        view?.let {
            it.loadBuyerData(buyerViewModel)
            it.hideLoading()
        }
    }

    override fun onCompleted() { }
}
