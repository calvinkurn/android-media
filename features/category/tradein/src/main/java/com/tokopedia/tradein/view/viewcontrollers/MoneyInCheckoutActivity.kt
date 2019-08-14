package com.tokopedia.tradein.view.viewcontrollers

import android.support.v4.app.Fragment
import com.tokopedia.tradein.R
import com.tokopedia.tradein.viewmodel.MoneyInCheckoutViewModel
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

class MoneyInCheckoutActivity : BaseTradeInActivity() {
    private var moneyInCheckoutViewModel: MoneyInCheckoutViewModel? = null

    override fun getViewModelType(): Class<MoneyInCheckoutViewModel> {
        return MoneyInCheckoutViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel?) {
        moneyInCheckoutViewModel = viewModel as MoneyInCheckoutViewModel
    }

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getTncFragmentInstance(TncResId: Int): Fragment? {
        return null
    }

    override fun getBottomSheetLayoutRes(): Int {
        return -1
    }

    override fun doNeedReattach(): Boolean {
        return false
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_money_in_checkout
    }
}