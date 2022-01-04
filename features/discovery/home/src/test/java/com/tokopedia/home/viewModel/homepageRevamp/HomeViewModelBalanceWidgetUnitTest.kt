package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.data.datasource.local.HomeRoomDataSource
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerList
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsListDataUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balance
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletappGetBalance
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel.Companion.OVO_WALLET_TYPE
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Devara Fikry on 08/05/21.
 */

class HomeViewModelBalanceWidgetUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getHomeBalanceWidgetUseCase = mockk<HomeBalanceWidgetUseCase>(relaxed = true)

    private val getHomeTokopointsListDataUseCase = mockk<GetHomeTokopointsListDataUseCase>(relaxed = true)
    private val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
    private val getWalletAppBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
    private val getCoroutinePendingCashbackUseCase = mockk<GetCoroutinePendingCashbackUseCase>(relaxed = true)

    private val homeBalanceWidgetUseCase = mockk<HomeBalanceWidgetUseCase>(relaxed = true)

    private val headerDataModel = HomeHeaderDataModel()

    private val WALLET_CODE_PEMUDA = "PEMUDA"

    @Test
    fun `When getBalanceWidgetData is not empty on refresh with force refresh true then homeDataModel contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(
                                HomeHeaderDataModel(
                                        headerDataModel = HeaderDataModel(
                                                homeBalanceModel = HomeBalanceModel(
                                                        balanceType = 2,
                                                        balanceDrawerItemModels = mutableMapOf(
                                                                Pair(0, BalanceDrawerItemModel(
                                                                        drawerItemType = TYPE_WALLET_APP_LINKED
                                                                )),
                                                                Pair(1, BalanceDrawerItemModel(
                                                                        drawerItemType = TYPE_COUPON
                                                                ))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase
        )
        //On refresh
        homeViewModel.refresh(true)

        val list = homeViewModel.homeLiveDynamicChannel.value?.list
        val homeHeaderDataModel = list?.filterIsInstance<HomeHeaderDataModel>()?.get(0)
        val homeBalanceModel = homeHeaderDataModel?.headerDataModel?.homeBalanceModel
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == true)
    }

    @Test
    fun `When getBalanceWidgetData is not empty on refresh with force refresh false then homeDataModel contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns true

        val mockHomeHeaderDataModel = HomeHeaderDataModel(
                headerDataModel = HeaderDataModel(
                        homeBalanceModel = HomeBalanceModel(
                                balanceType = 2,
                                balanceDrawerItemModels = mutableMapOf(
                                        Pair(0, BalanceDrawerItemModel(
                                                drawerItemType = TYPE_WALLET_APP_LINKED
                                        )),
                                        Pair(1, BalanceDrawerItemModel(
                                                drawerItemType = TYPE_COUPON
                                        ))
                                )
                        )
                )
        )
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(
                                mockHomeHeaderDataModel
                        )
                )
        )

        getHomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(
                homeHeaderDataModel = mockHomeHeaderDataModel
        )

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.refresh(false)

        val list = homeViewModel.homeLiveDynamicChannel.value?.list
        val homeHeaderDataModel = list?.filterIsInstance<HomeHeaderDataModel>()?.get(0)
        val homeBalanceModel = homeHeaderDataModel?.headerDataModel?.homeBalanceModel
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == true)
    }
}