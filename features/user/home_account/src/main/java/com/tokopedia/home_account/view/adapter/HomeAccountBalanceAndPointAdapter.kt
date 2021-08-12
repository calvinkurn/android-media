package com.tokopedia.home_account.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.home_account.view.adapter.delegate.HomeAccountBalanceAndPointDelegate
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener

class HomeAccountBalanceAndPointAdapter(
    balanceAndPointListener: BalanceAndPointListener,
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(HomeAccountBalanceAndPointDelegate(balanceAndPointListener))
    }
}