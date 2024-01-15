package com.tokopedia.home_account.ui.fundsAndInvestment

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.analytics.TokopediaCardAnalytics
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.ui.fundsAndInvestment.ui.FundsAndInvestmentScreen
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FundsAndInvestmentComposeActivity : BaseSimpleActivity(),
    HasComponent<HomeAccountUserComponents> {

    @Inject
    lateinit var viewModel: FundsAndInvestmentComposeViewModel

    @Inject
    lateinit var homeAccountAnalytic: HomeAccountAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initAnalytics()
        toolbar.hide()
        setView()
    }

    private fun setView() {
        setContent {
            FundsAndInvestmentScreen(
                userId = userSession.userId,
                uiState = viewModel.uiState,
                onItemClicked = {
                    onItemClicked(it)
                },
                onBackClicked = {
                    finish()
                },
                onReloadData = { isRefreshData ->
                    viewModel.getCentralizedUserAssetConfig(isRefreshData)
                }
            )
        }
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
        val intent = RouteManager.getIntent(this, applink)
        startActivity(intent)
    }

    override fun getScreenName(): String {
        return AccountConstants.Analytics.Screen.SCREEN_FUNDS_AND_INVESTMENT
    }

    private fun initAnalytics() {
        homeAccountAnalytic.trackScreen(screenName)
        homeAccountAnalytic.eventViewAssetPage()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): HomeAccountUserComponents =
        ActivityComponentFactory.instance.createHomeAccountComponent(this, application)

}
