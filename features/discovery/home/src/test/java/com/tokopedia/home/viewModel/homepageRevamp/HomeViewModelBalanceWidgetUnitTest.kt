package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerList
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsListDataUseCase
import com.tokopedia.navigation_common.usecase.GetWalletAppBalanceUseCase
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balance
import com.tokopedia.navigation_common.usecase.pojo.walletapp.Balances
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletAppData
import com.tokopedia.navigation_common.usecase.pojo.walletapp.WalletappGetBalance
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
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
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.navigation_common.model.wallet.WalletStatus
import com.tokopedia.navigation_common.usecase.GetWalletEligibilityUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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
    private val getHomeTokopointsListDataUseCase = mockk<GetHomeTokopointsListDataUseCase>(relaxed = true)
    private val getWalletEligibilityUseCase = mockk<GetWalletEligibilityUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
    private val getWalletAppBalanceUseCase = mockk<GetWalletAppBalanceUseCase>(relaxed = true)
    private val getCoroutinePendingCashbackUseCase = mockk<GetCoroutinePendingCashbackUseCase>(relaxed = true)
    private val headerDataModel = HomeHeaderDataModel()

    private val WALLET_CODE_PEMUDA = "PEMUDA"

    @Test
    fun `When get eligibility returns false on refresh then balance widget contains 4 item`() {
        every { userSessionInterface.isLoggedIn } returns true

        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
            TokopointsDrawerList(
                "false",
                listOf(
                    TokopointsDrawer(type = "TokoPoints"),
                    TokopointsDrawer(type = "Coupon"),
                    TokopointsDrawer(type = "BBO")
                )
            )
        )
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(Balance(
                            walletCode = WALLET_CODE_PEMUDA
                        ))
                    )
                )
            )
        )
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase,
            getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )
        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(true)

        //On refresh
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)

        assertBalanceTypeExist(TYPE_WALLET_APP_LINKED)
        assertBalanceTypeExist(TYPE_TOKOPOINT)
        assertBalanceTypeExist(TYPE_FREE_ONGKIR)
        assertBalanceTypeExist(TYPE_COUPON)
    }

    @Test
    fun `When get eligibility returns true on refresh then balance widget contains 2 item`() {
        every { userSessionInterface.isLoggedIn } returns true

        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = true
        )
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
            TokopointsDrawerList(
                "false",
                listOf(
                    TokopointsDrawer(type = "TokoPoints"),
                    TokopointsDrawer(type = "Coupon"),
                    TokopointsDrawer(type = "BBO")
                )
            )
        )
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(Balance(
                            walletCode = WALLET_CODE_PEMUDA
                        ))
                    )
                )
            )
        )
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase,
            getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )
        //On refresh
        homeViewModel.refresh(true)

        assert1x2WalletBalanceModelState(STATE_SUCCESS)
        assert1x2TokopointBalanceModelState(STATE_SUCCESS)
    }

    @Test
    fun `When get walletapp and getTokopoint success on refresh then show walletapp and tokopoint success state`(){
        every { userSessionInterface.isLoggedIn } returns true

        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
            TokopointsDrawerList(
                "false",
                listOf(
                    TokopointsDrawer(type = "TokoPoints"),
                    TokopointsDrawer(type = "Coupon"),
                    TokopointsDrawer(type = "BBO")
                )
            )
        )
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(Balance(
                            walletCode = WALLET_CODE_PEMUDA
                        ))
                    )
                )
            )
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase,
            getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )
        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(true)

        //On refresh
        homeViewModel.refresh(true)

        assertTokopointBalanceModelState(STATE_SUCCESS)
        assertWalletBalanceModelState(STATE_SUCCESS)

        assertBalanceTypeExist(TYPE_WALLET_APP_LINKED)
        assertBalanceTypeExist(TYPE_TOKOPOINT)
        assertBalanceTypeExist(TYPE_FREE_ONGKIR)
        assertBalanceTypeExist(TYPE_COUPON)
    }

    @Test
    fun `When get walletapp returns code PEMUDA on refresh then show walletapp success state`(){
        every { userSessionInterface.isLoggedIn } returns true

        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(Balance(
                            walletCode = "PEMUDA"
                        ))
                    )
                )
            )
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase
        )
        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(true)

        //On refresh
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_APP_LINKED)
    }

    @Test
    fun `When get walletapp returns code not PEMUDA on refresh then show walletapp error state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(Balance(
                            walletCode = "OTHER"
                        ))
                    )
                )
            )
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase
        )
        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(true)

        //On refresh
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_ERROR)
    }

    @Test
    fun `When get old wallet and getTokopoint success on refresh then show wallet and tokopoint success state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
                TokopointsDrawerList(
                        "false",
                        listOf(
                                TokopointsDrawer(type = "TokoPoints"),
                                TokopointsDrawer(type = "Coupon"),
                                TokopointsDrawer(type = "BBO")
                        )
                )
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel(
                walletType = OVO_WALLET_TYPE,
                link = true
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletAppBalanceUseCase = getWalletAppBalanceUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )
        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(false)

        //On refresh
        homeViewModel.refresh(true)

        assertTokopointBalanceModelState(STATE_SUCCESS)
        assertWalletBalanceModelState(STATE_SUCCESS)

        assertBalanceTypeExist(TYPE_WALLET_OVO)
        assertBalanceTypeExist(TYPE_TOKOPOINT)
        assertBalanceTypeExist(TYPE_FREE_ONGKIR)
        assertBalanceTypeExist(TYPE_COUPON)
    }

    @Test
    fun `When get walletapp success and type is linked show Wallet App Linked Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = true,
                        balance = listOf(Balance(
                            walletCode = WALLET_CODE_PEMUDA
                        ))
                    )
                )
            )
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(true)
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_APP_LINKED)
    }

    @Test
    fun `When get walletapp success and type is not linked show Wallet App Not Linked Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletAppBalanceUseCase.executeOnBackground() } returns WalletAppData(
            WalletappGetBalance(
                listOf(
                    Balances(
                        isLinked = false,
                        balance = listOf(Balance(amountFmt = "ahayy"))
                    )
                )
            )
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
            getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getWalletAppBalanceUseCase = getWalletAppBalanceUseCase,
            getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(true)
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_APP_NOT_LINKED)
    }

    @Test
    fun `When get old wallet success and type is OVO on refresh then show OVO Wallet Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel(
                link = true,
                walletType = HomeBalanceModel.OVO_WALLET_TYPE
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(false)
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_OVO)
    }

    @Test
    fun `When get old wallet success and type is OVO and need to show topup on refresh then show OVO Wallet With Topup Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel(
                link = true,
                walletType = HomeBalanceModel.OVO_WALLET_TYPE,
                isShowTopup = true
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(false)
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_WITH_TOPUP)
    }

    @Test
    fun `When get old wallet success and type is OTHER on refresh then show Other Wallet Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel(
                link = true,
                walletType = ""
        )

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(false)
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_OTHER)
    }

    @Test
    fun `When get old wallet data success is not linked on refresh then show pending cashback section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel(
                walletType = OVO_WALLET_TYPE,
                link = false
        )

        coEvery { getCoroutinePendingCashbackUseCase.executeOnBackground() } returns PendingCashback()

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.setWalletAppRollence(false)
        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_PENDING_CASHBACK)
    }

    @Test
    fun `When getWallet data failed on refresh then show wallet widget with error state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()

        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws Exception()

        coEvery { getCoroutinePendingCashbackUseCase.executeOnBackground() } returns PendingCashback()

        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_ERROR)
    }

    @Test
    fun `When getTokopoint data success on refresh then show all tokopoints widget`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
                TokopointsDrawerList(
                        "false",
                        listOf(
                                TokopointsDrawer(type = "TokoPoints"),
                                TokopointsDrawer(type = "Coupon"),
                                TokopointsDrawer(type = "BBO")
                        )
                )
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        homeViewModel.refresh(true)

        assertTokopointBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_TOKOPOINT)
        assertBalanceTypeExist(TYPE_COUPON)
        assertBalanceTypeExist(TYPE_FREE_ONGKIR)
    }

    @Test
    fun `When getTokopoint data with rewards success on refresh then show all tokopoints widget with rewards section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
                TokopointsDrawerList(
                        "false",
                        listOf(
                                TokopointsDrawer(type = "TokoPoints"),
                                TokopointsDrawer(type = "Coupon"),
                                TokopointsDrawer(type = "BBO"),
                                TokopointsDrawer(type = "Rewards")
                        )
                )
        )
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        homeViewModel.refresh(true)

        assertTokopointBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_TOKOPOINT)
        assertBalanceTypeExist(TYPE_REWARDS)
        assertBalanceTypeExist(TYPE_FREE_ONGKIR)
    }

    @Test
    fun `When getTokopoint data with rewards failed on refresh then show all tokopoints widget with error state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } throws Exception()
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.refresh(true)

        assertTokopointBalanceModelState(STATE_ERROR)
    }

    @Test
    fun `When getTokopoint data success on retry then show all tokopoints widget success state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData(
                TokopointsDrawerList(
                        "false",
                        listOf(
                                TokopointsDrawer(type = "TokoPoints"),
                                TokopointsDrawer(type = "Coupon"),
                                TokopointsDrawer(type = "BBO"),
                                TokopointsDrawer(type = "Rewards")
                        )
                )
        )
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.onRefreshTokoPoint()
        assertTokopointBalanceModelState(STATE_SUCCESS)
    }

    @Test
    fun `When getTokopoint data failed on retry then show all tokopoint widget with error state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } throws Exception()
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.onRefreshTokoCash()
        assertTokopointBalanceModelState(STATE_ERROR)
    }

    @Test
    fun `When getWallet data success on retry then show wallet widget success state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getWalletEligibilityUseCase.executeOnBackground() } returns WalletStatus(
            isGoPointsEligible = false
        )
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
                getWalletEligibilityUseCase = getWalletEligibilityUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.onRefreshTokoCash()
        assertWalletBalanceModelState(STATE_SUCCESS)
    }

    @Test
    fun `When getWallet data failed on retry then show wallet widget error state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws Exception()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        //TODO fix this for unit test
//        homeViewModel.onRefreshTokoCash()
        assertWalletBalanceModelState(STATE_ERROR)
    }

    private fun assertBalanceTypeExist(balanceItemType: Int) {
        assert(
                homeViewModel.homeLiveDynamicChannel.value!!.checkHomeBalanceModelTypeExist(
                        balanceItemType
                )
        )
    }

    private fun assertWalletBalanceModelState(state: Int) {
        assert(
                homeViewModel.homeLiveDynamicChannel.value!!.checkHomeBalanceModelState(
                        isWalletBalanceModelCheck = true,
                        state = state
                )
        )
    }

    private fun assertTokopointBalanceModelState(state: Int) {
        assert(
                homeViewModel.homeLiveDynamicChannel.value!!.checkHomeBalanceModelState(
                        isTokopointBalanceModelCheck = true,
                        state = state
                )
        )
    }

    private fun assert1x2WalletBalanceModelState(state: Int) {
        assert(
            homeViewModel.homeLiveDynamicChannel.value!!.check1x2HomeBalanceModelState(
                isWalletBalanceModelCheck = true,
                state = state
            )
        )
    }

    private fun assert1x2TokopointBalanceModelState(state: Int) {
        assert(
            homeViewModel.homeLiveDynamicChannel.value!!.check1x2HomeBalanceModelState(
                isTokopointBalanceModelCheck = true,
                state = state
            )
        )
    }

    private fun HomeDynamicChannelUseCase.buildBalanceHomeData(balanceType: Int) {
        val walletTypeFlag = HomeFlag()
        walletTypeFlag.addFlag(
                name = HomeFlag.HAS_TOKOPOINTS_STRING,
                isActive = true,
                integerValue = balanceType
        )
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(
                list = listOf(headerDataModel),
                homeFlag = walletTypeFlag)
        )
    }

    private fun HomeDynamicChannelModel.checkHomeHeaderOvoDataModelExist(): Boolean {
        //TODO fix this for unit test and delete return false
//        return this.list.isNotEmpty() && this.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
        return false
    }

    private fun HomeDynamicChannelModel.getHomeBalanceModel(): HomeBalanceModel {
        //TODO fix this for unit test
//        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
//        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel
        return homeBalanceModel
    }

    private fun HomeDynamicChannelModel.checkHomeBalanceModelState(
            isWalletBalanceModelCheck: Boolean = false,
            isTokopointBalanceModelCheck: Boolean = false,
            state: Int
    ): Boolean {
        //TODO fix this for unit test
//        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
//        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel

        var walletAssert = true
        var tokopointAssert = true

        if (isWalletBalanceModelCheck) {
            homeBalanceModel.balanceDrawerItemModels.forEach { i, balanceDrawerItemModel ->
                when (balanceDrawerItemModel.drawerItemType) {
                        TYPE_WALLET_OVO,
                        TYPE_WALLET_OTHER,
                        TYPE_WALLET_PENDING_CASHBACK,
                        TYPE_WALLET_WITH_TOPUP -> {
                            walletAssert = balanceDrawerItemModel.state == state
                        }
                }
            }
        }

        if (isTokopointBalanceModelCheck) {
            homeBalanceModel.balanceDrawerItemModels.forEach { i, balanceDrawerItemModel ->
                when (balanceDrawerItemModel.drawerItemType) {
                    TYPE_TOKOPOINT,
                    TYPE_COUPON,
                    TYPE_REWARDS,
                    TYPE_FREE_ONGKIR -> {
                        tokopointAssert = balanceDrawerItemModel.state == state
                    }
                }
            }
        }
        return walletAssert && tokopointAssert
    }

    private fun HomeDynamicChannelModel.checkHomeBalanceModelTypeExist(type: Int): Boolean {
        //TODO fix this for unit test
//        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
//        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel

        homeBalanceModel.balanceDrawerItemModels.forEach { (i, balanceDrawerItemModel) ->
            if (balanceDrawerItemModel.drawerItemType == type) {
                return true
            }
        }
        return false
    }

    private fun HomeDynamicChannelModel.check1x2HomeBalanceModelState(
        isWalletBalanceModelCheck: Boolean = false,
        isTokopointBalanceModelCheck: Boolean = false,
        state: Int,
        firstTokopointDrawerType: Int = TYPE_TOKOPOINT
    ): Boolean {
        //TODO fix this for unit test
//        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
//        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel

        var walletAssert = true
        var tokopointAssert = true

        if (isWalletBalanceModelCheck) {
            homeBalanceModel.balanceDrawerItemModels.forEach { i, balanceDrawerItemModel ->
                when (balanceDrawerItemModel.drawerItemType) {
                    TYPE_WALLET_OVO,
                    TYPE_WALLET_OTHER,
                    TYPE_WALLET_PENDING_CASHBACK,
                    TYPE_WALLET_WITH_TOPUP -> {
                        walletAssert = balanceDrawerItemModel.state == state
                    }
                }
            }
            walletAssert =
                (homeBalanceModel.balanceDrawerItemModels[0]!!.drawerItemType == TYPE_WALLET_APP_LINKED) ||
                        homeBalanceModel.balanceDrawerItemModels[0]!!.drawerItemType == TYPE_WALLET_APP_NOT_LINKED
        }

        if (isTokopointBalanceModelCheck) {
            tokopointAssert =
                (homeBalanceModel.balanceDrawerItemModels[1]!!.drawerItemType == firstTokopointDrawerType)
        }
        return walletAssert && tokopointAssert
    }
}