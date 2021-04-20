package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home.beranda.data.model.TokopointsDrawerList
import com.tokopedia.home.beranda.data.model.TokopointsDrawerListHomeData
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsListDataUseCase
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_ERROR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_SUCCESS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel.Companion.OVO_WALLET_TYPE
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
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
    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val getHomeTokopointsListDataUseCase = mockk<GetHomeTokopointsListDataUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
    private val getCoroutinePendingCashbackUseCase = mockk<GetCoroutinePendingCashbackUseCase>(relaxed = true)
    private val headerDataModel = HomeHeaderOvoDataModel()

    @Test
    fun `When getWallet and getTokopoint success on refresh then show wallet and tokopoint success state`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true

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
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )
        homeViewModel.homeLiveData.observeForever(observerHome)

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
    fun `When getWallet success and type is OVO on refresh then show OVO Wallet Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()

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
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_OVO)
    }

    @Test
    fun `When getWallet success and type is OVO and need to show topup on refresh then show OVO Wallet With Topup Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()

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
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_WITH_TOPUP)
    }

    @Test
    fun `When getWallet success and type is OTHER on refresh then show Other Wallet Type section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()

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
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.refresh(true)

        assertWalletBalanceModelState(STATE_SUCCESS)
        assertBalanceTypeExist(TYPE_WALLET_OTHER)
    }

    @Test
    fun `When getWallet data success is not linked on refresh then show pending cashback section`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()

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
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

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
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
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

        homeViewModel.onRefreshTokoCash()
        assertTokopointBalanceModelState(STATE_ERROR)
    }

    @Test
    fun `When getWallet data success on retry then show wallet widget success state`(){
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
        coEvery{ getHomeTokopointsListDataUseCase.executeOnBackground() } returns TokopointsDrawerListHomeData()
        getHomeUseCase.buildBalanceHomeData(balanceType = 2)

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsListDataUseCase = getHomeTokopointsListDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )

        homeViewModel.onRefreshTokoCash()
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

        homeViewModel.onRefreshTokoCash()
        assertWalletBalanceModelState(STATE_ERROR)
    }

    private fun assertBalanceTypeExist(balanceItemType: Int) {
        assert(
                homeViewModel.homeLiveData.value!!.checkHomeBalanceModelTypeExist(
                        balanceItemType
                )
        )
    }

    private fun assertWalletBalanceModelState(state: Int) {
        assert(
                homeViewModel.homeLiveData.value!!.checkHomeBalanceModelState(
                        isWalletBalanceModelCheck = true,
                        state = state
                )
        )
    }

    private fun assertTokopointBalanceModelState(state: Int) {
        assert(
                homeViewModel.homeLiveData.value!!.checkHomeBalanceModelState(
                        isTokopointBalanceModelCheck = true,
                        state = state
                )
        )
    }

    private fun HomeRevampUseCase.buildBalanceHomeData(balanceType: Int) {
        val walletTypeFlag = HomeFlag()
        walletTypeFlag.addFlag(
                name = HomeFlag.HAS_TOKOPOINTS_STRING,
                isActive = true,
                integerValue = balanceType
        )
        getHomeUseCase.givenGetHomeDataReturn(HomeDataModel(
                list = listOf(headerDataModel),
                homeFlag = walletTypeFlag)
        )
    }

    private fun HomeDataModel.checkHomeHeaderOvoDataModelExist(): Boolean {
        return this.list.isNotEmpty() && this.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
    }

    private fun HomeDataModel.getHomeBalanceModel(): HomeBalanceModel {
        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel
        return homeBalanceModel
    }

    private fun HomeDataModel.checkHomeBalanceModelState(
            isWalletBalanceModelCheck: Boolean = false,
            isTokopointBalanceModelCheck: Boolean = false,
            state: Int
    ): Boolean {
        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel

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

    private fun HomeDataModel.checkHomeBalanceModelTypeExist(type: Int): Boolean {
        val homeHeaderDataModel = this.list.filterIsInstance<HomeHeaderOvoDataModel>()[0]
        val homeBalanceModel = homeHeaderDataModel.headerDataModel!!.homeBalanceModel

        homeBalanceModel.balanceDrawerItemModels.forEach { (i, balanceDrawerItemModel) ->
            if (balanceDrawerItemModel.drawerItemType == type) {
                return true
            }
        }
        return false
    }
}