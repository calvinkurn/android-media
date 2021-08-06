package com.tokopedia.home_account.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentFragmentBinding
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.view.HomeAccountUserViewModel
import com.tokopedia.home_account.view.adapter.HomeAccountFundsAndInvestmentAdapter
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class FundsAndInvestmentFragment : BaseDaggerFragment(), WalletListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(HomeAccountUserViewModel::class.java) }

    private val binding by viewBinding(FundsAndInvestmentFragmentBinding::bind)
    private var adapter: HomeAccountFundsAndInvestmentAdapter? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.funds_and_investment_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onClickWallet(type: String) {
        when (type) {
            AccountConstants.WALLET.OVO -> {}
            AccountConstants.WALLET.GOPAY -> {}
        }
    }

    private fun initView() {
        binding?.fundsAndInvestmentRv?.layoutManager = LinearLayoutManager(context)
        binding?.fundsAndInvestmentRv?.adapter = adapter
    }

    companion object {
        fun newInstance(bundle: Bundle?): Fragment {
            return FundsAndInvestmentFragment().apply {
                arguments = bundle
            }
        }
    }
}