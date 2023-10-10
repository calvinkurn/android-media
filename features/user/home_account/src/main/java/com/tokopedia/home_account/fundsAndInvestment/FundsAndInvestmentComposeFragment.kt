package com.tokopedia.home_account.fundsAndInvestment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.fundsAndInvestment.ui.FundsAndInvestmentLayout
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import javax.inject.Inject

class FundsAndInvestmentComposeFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModel: FundsAndInvestmentComposeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FundsAndInvestmentLayout(
                    uiState = viewModel.uiState,
                    onItemClicked = {
                        onItemClicked(it)
                    },
                    onBackClicked = {
                        activity?.finish()
                    },
                    onReloadData = { isRefreshData ->
                        viewModel.getCentralizedUserAssetConfig(isRefreshData = isRefreshData)
                    }
                )
            }
        }
    }

    private fun onItemClicked(item: WalletUiModel) {
        if (item.isFailed) {
            viewModel.refreshItem(item)
        } else {
            goToApplink(item.applink)
        }
    }

    private fun goToApplink(applink: String) {
        val intent = RouteManager.getIntent(context, applink)
        startActivity(intent)
    }

    override fun getScreenName(): String =
        AccountConstants.Analytics.Screen.SCREEN_FUNDS_AND_INVESTMENT

    override fun initInjector() {
        getComponent(HomeAccountUserComponents::class.java).inject(this)
    }

    companion object {
        fun newInstance() = FundsAndInvestmentComposeFragment()
    }
}
