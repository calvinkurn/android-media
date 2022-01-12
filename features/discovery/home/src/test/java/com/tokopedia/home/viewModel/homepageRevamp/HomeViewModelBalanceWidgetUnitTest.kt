package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var homeViewModel: HomeRevampViewModel

    private val mockInitialHomeHeaderDataModel = HomeHeaderDataModel()
    private val mockSuccessHomeHeaderDataModel = HomeHeaderDataModel(
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

    @ExperimentalCoroutinesApi
    @Test
    fun `When getBalanceWidgetData is not empty on refresh with force refresh true then homeDataModel contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(mockSuccessHomeHeaderDataModel))
        )
        getHomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(
                homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        homeViewModel.homeRateLimit.shouldFetch(HomeRevampViewModel.HOME_LIMITER_KEY)
        //On refresh
        homeViewModel.refreshWithThreeMinsRules(true)
        homeViewModel.refreshWithThreeMinsRules(true)

        val list = homeViewModel.homeLiveDynamicChannel.value?.list
        val homeHeaderDataModel = list?.filterIsInstance<HomeHeaderDataModel>()?.get(0)
        val homeBalanceModel = homeHeaderDataModel?.headerDataModel?.homeBalanceModel
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When getBalanceWidgetData is not empty on refresh with force refresh false then homeDataModel contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(mockInitialHomeHeaderDataModel))
        )
        getHomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(
                homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.homeRateLimit.shouldFetch(HomeRevampViewModel.HOME_LIMITER_KEY)
        homeViewModel.refreshWithThreeMinsRules(false)
        val homeBalanceModel = getHomeBalanceModel()
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When userSession is not loggedIn on refresh then homeDataModel should not contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns false

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(mockInitialHomeHeaderDataModel))
        )

        getHomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(
                homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.refreshWithThreeMinsRules(false)

        val homeBalanceModel = getHomeBalanceModel()
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When userSession is not loggedIn onRefreshTokopoint then homeDataModel should not contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns false

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(mockInitialHomeHeaderDataModel))
        )

        getHomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(
                homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.onRefreshMembership()

        val homeBalanceModel = getHomeBalanceModel()
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When onGetTokopointData is not empty onRefreshTokopoint then homeDataModel contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(mockInitialHomeHeaderDataModel))
        )
        getHomeBalanceWidgetUseCase.givenGetTokopointDataReturn(
                homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.onRefreshMembership()

        val homeBalanceModel = getHomeBalanceModel()
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When userSession is not loggedIn onRefreshWalletApp then homeDataModel should not contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns false

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(list = listOf(mockInitialHomeHeaderDataModel))
        )

        getHomeBalanceWidgetUseCase.givenGetHomeBalanceWidgetReturn(
            homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.onRefreshWalletApp()

        val homeBalanceModel = getHomeBalanceModel()
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == false)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When onGetBalanceData is not empty onRefreshWalletApp then homeDataModel contains balance item data`() {
        every { userSessionInterface.isLoggedIn } returns true

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(list = listOf(mockInitialHomeHeaderDataModel), flowCompleted = true)
        )
        getHomeBalanceWidgetUseCase.givenGetBalanceWidgetDataReturn(
            homeHeaderDataModel = mockSuccessHomeHeaderDataModel
        )
        homeViewModel = createHomeViewModel(
            userSessionInterface = userSessionInterface,
            getHomeUseCase = getHomeUseCase,
            homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        //On refresh
        homeViewModel.onRefreshWalletApp()

        val homeBalanceModel = getHomeBalanceModel()
        Assert.assertTrue(homeBalanceModel?.balanceDrawerItemModels?.isNotEmpty() == true)
    }

    @ExperimentalCoroutinesApi
    private fun getHomeBalanceModel(): HomeBalanceModel? {
        val list = homeViewModel.homeLiveDynamicChannel.value?.list
        val homeHeaderDataModel = list?.filterIsInstance<HomeHeaderDataModel>()?.get(0)
        val homeBalanceModel = homeHeaderDataModel?.headerDataModel?.homeBalanceModel
        return homeBalanceModel
    }
}