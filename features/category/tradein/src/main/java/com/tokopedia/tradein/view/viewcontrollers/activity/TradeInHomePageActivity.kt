package com.tokopedia.tradein.view.viewcontrollers.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.tradein.TradeInAnalytics
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInHomePageFragment
import com.tokopedia.tradein.viewmodel.TradeInHomePageVM
import javax.inject.Inject

class TradeInHomePageActivity : BaseViewModelActivity<TradeInHomePageVM>() {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var tradeInAnalytics: TradeInAnalytics

    private lateinit var viewModel: TradeInHomePageVM
//    private lateinit var laku6TradeIn: Laku6TradeIn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setObservers()
    }

    private fun init() {
//        setLaku6()

        //init sessionid
//        viewModel.initSessionId(laku6TradeIn)
    }


    private fun setObservers() {
        viewModel.askUserLogin.observe(this, Observer {
            if (it != null && it == TradeinConstants.LOGIN_REQUIRED) {
                startActivityForResult(
                    RouteManager.getIntent(this, ApplinkConst.LOGIN),
                    LOGIN_REQUEST
                )
            } else {
                RouteManager.route(this, TradeinConstants.Deeplink.TRADEIN_DISCOVERY_INFO_URL)
                tradeInAnalytics.openEducationalScreen()
            }
        })
    }

    override fun initInject() {
        DaggerTradeInComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun getNewFragment(): Fragment? {
        return TradeInHomePageFragment.getFragmentInstance()
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomePageVM> {
        return TradeInHomePageVM::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomePageVM
    }


}