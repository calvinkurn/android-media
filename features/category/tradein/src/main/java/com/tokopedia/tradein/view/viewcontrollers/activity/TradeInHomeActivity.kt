package com.tokopedia.tradein.view.viewcontrollers.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInAddressFragment
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel
import javax.inject.Inject

const val TRADEIN_DISCOVERY_INFO = "tokopedia://discovery/tukar-tambah-edukasi"

class TradeInHomeActivity : BaseViewModelActivity<TradeInHomeViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        RouteManager.route(this, TRADEIN_DISCOVERY_INFO)
    }

    private fun setFragment() {
        viewModel.tradeInParams?.apply {
            supportFragmentManager.beginTransaction()
                    .replace(parentViewResourceID, TradeInAddressFragment.getFragmentInstance(origin, weight, productName), tagFragment)
                    .commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getTradeInParams()
        setFragment()
    }

    private fun getTradeInParams() {
        viewModel.tradeInParams = intent.getParcelableExtra(TradeInParams::class.java.simpleName)
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomeViewModel
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomeViewModel> {
        return TradeInHomeViewModel::class.java
    }

    override fun initInject() {
        DaggerTradeInComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }
}