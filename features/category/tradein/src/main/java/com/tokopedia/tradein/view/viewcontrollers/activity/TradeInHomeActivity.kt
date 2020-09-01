package com.tokopedia.tradein.view.viewcontrollers.activity

import androidx.fragment.app.Fragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel

class TradeInHomeActivity: BaseTradeInActivity<TradeInHomeViewModel>() {
    private lateinit var tradeInHomeViewModel: TradeInHomeViewModel

    override fun setViewModel(viewModel: BaseViewModel) {
        tradeInHomeViewModel = viewModel as TradeInHomeViewModel
    }

    override fun getNewFragment(): Fragment? {
        TODO("Not yet implemented")
    }

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getViewModelType(): Class<TradeInHomeViewModel> {
        return TradeInHomeViewModel::class.java
    }

    override fun initInject() {
        component.inject(this)
    }
}