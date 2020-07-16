package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel
import com.tokopedia.common_wallet.pendingcashback.view.PendingCashback
import com.tokopedia.home.beranda.data.model.KeywordSearchData
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData
import com.tokopedia.home.beranda.data.model.TokopointsDrawerHomeData
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutinePendingCashbackUseCase
import com.tokopedia.home.beranda.domain.interactor.GetCoroutineWalletBalanceUseCase
import com.tokopedia.home.beranda.domain.interactor.GetHomeTokopointsDataUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.util.concurrent.TimeoutException

class HomeViewModelOvoUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test Tokopoint Only"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
        val getHomeUseCase by memoized<HomeUseCase>()
        val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase>()

        Scenario("Get success data tokopoint"){
            val headerDataModel = HeaderDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data tokopoint"){
                coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokopoint"){
                homeViewModel.refresh(true)
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokocash
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokopoint
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.tokopointsDrawerHomeData != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val headerDataModel = HeaderDataModel()
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data tokopoint"){
                coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokopoint"){
                homeViewModel.refresh(true)
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokocash
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokopoint
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.tokopointsDrawerHomeData == null
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }

    Feature("Test PendingCashback Only"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
        val getHomeUseCase by memoized<HomeUseCase>()
        val getCoroutinePendingCashbackUseCase by memoized<GetCoroutinePendingCashbackUseCase>()

        Scenario("Get success data PendingCashback"){
            val headerDataModel = HeaderDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data PendingCashback"){
                coEvery{ getCoroutinePendingCashbackUseCase.executeOnBackground() } returns PendingCashback()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get PendingCashback"){
                homeViewModel.getTokocashPendingBalance()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update cashback data
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.cashBackData != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val headerDataModel = HeaderDataModel()
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data tokopoint"){
                coEvery{ getCoroutinePendingCashbackUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Pending cashback"){
                homeViewModel.getTokocashPendingBalance()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }

    Feature("Test Tokocash Only"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
        val getHomeUseCase by memoized<HomeUseCase>()
        val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase>()
        val headerDataModel = HeaderDataModel()
        Scenario("Get success data Tokocash"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data Tokocash"){
                coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.refresh(true)
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokocash
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokopoint
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.homeHeaderWalletActionData != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data Tokocash"){
                coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.refresh(true)
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokocash
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    // update tokopoint
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.homeHeaderWalletActionData == null
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }

    Feature("Test Refresh Tokopoint"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
        val getHomeUseCase by memoized<HomeUseCase>()
        val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase>()

        Scenario("Get success data Tokopoint but non login"){
            val headerDataModel = HeaderDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data Tokocash"){
                coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.onRefreshTokoPoint()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception but non login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val headerDataModel = HeaderDataModel()
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data Tokocash"){
                coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.onRefreshTokoPoint()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get success data Tokopoint login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val headerDataModel = HeaderDataModel()
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns true
            }
            Given("Success data Tokocash"){
                coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } returns TokopointsDrawerHomeData()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokopoint"){
                homeViewModel.onRefreshTokoPoint()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.tokopointsDrawerHomeData != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val headerDataModel = HeaderDataModel()
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns true
            }
            Given("Success data Tokopoint"){
                coEvery{ getHomeTokopointsDataUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokopoint"){
                homeViewModel.onRefreshTokoPoint()
            }

            Then("Check data observer"){
                verifyOrder {

                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.tokopointsDrawerHomeData == null
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }

    Feature("Test Refresh Tokocash"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val userSessionInterface by memoized<UserSessionInterface> { mockk(relaxed = true) }
        val getHomeUseCase by memoized<HomeUseCase>()
        val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase>()
        val headerDataModel = HeaderDataModel()
        Scenario("Get success data Tokocash but non login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data Tokocash"){
                coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.onRefreshTokoCash()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception but non login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns false
            }
            Given("Success data Tokocash"){
                coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.onRefreshTokoCash()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get success data Tokocash login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns true
            }
            Given("Success data Tokocash"){
                coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } returns WalletBalanceModel()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.onRefreshTokoCash()
            }

            Then("Check data observer"){
                verifyOrder {
                    // check on home data initial first channel is header data model
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.homeHeaderWalletActionData != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception login"){
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("Set non login"){
                every { userSessionInterface.isLoggedIn } returns true
            }
            Given("Success data Tokocash"){
                coEvery{ getCoroutineWalletBalanceUseCase.executeOnBackground() } throws TimeoutException()
            }

            Given("Populate with only header data"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(headerDataModel)
                        )
                )
            }

            Given("home viewModel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            When("Get Tokocash"){
                homeViewModel.onRefreshTokoCash()
            }

            Then("Check data observer"){
                verifyOrder {

                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().isNotEmpty()
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().firstOrNull()?.homeHeaderWalletActionData == null
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})