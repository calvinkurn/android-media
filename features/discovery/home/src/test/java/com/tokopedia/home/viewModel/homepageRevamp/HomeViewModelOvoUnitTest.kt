package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsDataUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelOvoUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val getHomeTokopointsDataUseCase = mockk<GetHomeTokopointsDataUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase>(relaxed = true)
    private val getCoroutinePendingCashbackUseCase = mockk<GetCoroutinePendingCashbackUseCase>(relaxed = true)
    private val headerDataModel = HomeHeaderOvoDataModel()

    @Test
    fun `Test Tokopoint Only Get success data tokopoint`(){
        val headerDataModel = HomeHeaderOvoDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.refresh(true)

        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokocash
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokopoint
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.tokopointsDrawerHomeData != null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test refresh data`(){
        val headerDataModel = HomeHeaderOvoDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        ).apply{
            setNeedToShowGeolocationComponent(true)
            setGeolocationPermission(true)
        }
        homeViewModel.setNewBalanceWidget(false)

        homeViewModel.refresh(true)

        homeViewModel.isRequestNetworkLiveData.observeOnce {
            assert(it != null)
        }

        homeViewModel.sendLocationLiveData.observeOnce {
            assert(it != null)
        }
    }

    @Test
    fun `Test Tokopoint Only Get timeout exception`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val headerDataModel = HomeHeaderOvoDataModel()
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } throws TimeoutException()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.refresh(true)

        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokocash
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokopoint
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.tokopointsDrawerHomeData == null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test PendingCashback Only Get success data PendingCashback`(){
        val headerDataModel = HomeHeaderOvoDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getCoroutinePendingCashbackUseCase.executeOnBackground() } returns PendingCashback()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
                getCoroutinePendingCashbackUseCase = getCoroutinePendingCashbackUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.getTokocashPendingBalance()

        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update cashback data
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.cashBackData != null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test PendingCashback Only Get timeout exception`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val headerDataModel = HomeHeaderOvoDataModel()
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getCoroutinePendingCashbackUseCase.executeOnBackground() } throws TimeoutException()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.getTokocashPendingBalance()

        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Tokocash Only Get success data Tokocash`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.refresh(true)

        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokocash
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokopoint
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.homeHeaderWalletActionData != null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Tokocash Only Get timeout exception`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws TimeoutException()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.refresh(true)
        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokocash
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            // update tokopoint
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.homeHeaderWalletActionData == null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokopoint Get success data Tokopoint but non login`(){
        val headerDataModel = HomeHeaderOvoDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.onRefreshTokoPoint()
        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokopoint Get timeout exception but non login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val headerDataModel = HomeHeaderOvoDataModel()
        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } throws TimeoutException()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.onRefreshTokoPoint()

        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Test Refresh Tokopoint Get success data Tokopoint login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val headerDataModel = HomeHeaderOvoDataModel()
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.onRefreshTokoPoint()
        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.tokopointsDrawerHomeData != null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokopoint Get timeout exception login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val headerDataModel = HomeHeaderOvoDataModel()
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } throws TimeoutException()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.onRefreshTokoPoint()
        verifyOrder {

            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.tokopointsDrawerHomeData == null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokocash Get success data Tokocash but non login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        // Set non login
        every { userSessionInterface.isLoggedIn } returns false
        // Success data Tokocash
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()

        // Populate with only header data
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.onRefreshTokoCash()

        // Check data observer
        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokocash Get timeout exception but non login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        every { userSessionInterface.isLoggedIn } returns false
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws TimeoutException()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.onRefreshTokoCash()
        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokocash Get success data Tokocash login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel(isShowAnnouncement = true, applinks = "asd")
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
                getCoroutineWalletBalanceUseCase = getCoroutineWalletBalanceUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)
        homeViewModel.onRefreshTokoCash()
        homeViewModel.popupIntroOvoLiveData.observeOnce { assert(it != null) }
        verifyOrder {
            // check on home data initial first channel is header data model
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.homeHeaderWalletActionData != null
            })
        }
        confirmVerified(observerHome)
    }

    @Test
    fun `Test Refresh Tokocash Get timeout exception login`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws TimeoutException()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(headerDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                userSessionInterface = userSessionInterface,
                getHomeUseCase = getHomeUseCase,
                getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase
        )
        homeViewModel.setNewBalanceWidget(false)
        homeViewModel.homeLiveData.observeForever(observerHome)

        homeViewModel.onRefreshTokoCash()

        verifyOrder {

            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().isNotEmpty()
            })
            observerHome.onChanged(match {
                it.list.isNotEmpty() && it.list.filterIsInstance<HomeHeaderOvoDataModel>().firstOrNull()?.headerDataModel?.homeHeaderWalletActionData == null
            })
        }
        confirmVerified(observerHome)
    }
}

