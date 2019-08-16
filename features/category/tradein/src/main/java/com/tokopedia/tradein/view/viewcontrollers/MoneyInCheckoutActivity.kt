package com.tokopedia.tradein.view.viewcontrollers

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tradein.R
import com.tokopedia.tradein.di.MoneyInCheckoutComponent
import com.tokopedia.tradein.di.MoneyInUseCaseModule
import com.tokopedia.tradein.viewmodel.MoneyInCheckoutViewModel
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

class MoneyInCheckoutActivity : BaseTradeInActivity() {
    private lateinit var moneyInCheckoutComponent: MoneyInCheckoutComponent
    private lateinit var moneyInCheckoutViewModel: MoneyInCheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        moneyInCheckoutComponent = DaggerMoneyInCheckoutComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .moneyInUseCaseModule(MoneyInUseCaseModule())
                .build()
        moneyInCheckoutComponent.inject(this)
    }

    override fun initView() {
    }

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