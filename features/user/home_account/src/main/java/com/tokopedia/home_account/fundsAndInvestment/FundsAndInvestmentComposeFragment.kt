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
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.analytics.TokopediaCardAnalytics
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.fundsAndInvestment.ui.FundsAndInvestmentLayout
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FundsAndInvestmentComposeFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModel: FundsAndInvestmentComposeViewModel

    @Inject
    lateinit var homeAccountAnalytic: HomeAccountAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FundsAndInvestmentLayout(
                    userId = userSession.userId,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeAccountAnalytic.trackScreen(screenName)
        homeAccountAnalytic.eventViewAssetPage()
    }

    private fun onItemClicked(item: WalletUiModel) {
        if (item.id == AccountConstants.WALLET.CO_BRAND_CC) {
            TokopediaCardAnalytics.sendClickPaymentWidgetOnLihatSemuaPagePyEvent(
                eventLabel = item.statusName,
                userId = userSession.userId
            )
        }

        homeAccountAnalytic.eventClickAssetPage(
            item.id,
            item.isActive,
            item.isFailed
        )

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
