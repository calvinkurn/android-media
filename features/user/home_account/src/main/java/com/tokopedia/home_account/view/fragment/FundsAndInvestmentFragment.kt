package com.tokopedia.home_account.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.databinding.HomeAccountFundsAndInvestmentBinding
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.view.adapter.HomeAccountFundsAndInvestmentAdapter
import com.tokopedia.home_account.view.adapter.factory.FundsAndInvestmentItemFactory
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class FundsAndInvestmentFragment : BaseDaggerFragment(), WalletListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val binding by viewBinding(HomeAccountFundsAndInvestmentBinding::bind)

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeAccountFundsAndInvestmentAdapter(FundsAndInvestmentItemFactory(this))
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    override fun onClickWallet(type: String) {
        when (type) {
            AccountConstants.WALLET.OVO -> {}
            AccountConstants.WALLET.GOPAY -> {}
        }
    }

    companion object {
        fun newInstance(bundle: Bundle?): Fragment {
            return FundsAndInvestmentFragment().apply {
                arguments = bundle
            }
        }
    }
}