package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
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
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class HomeViewModelOvoUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Test Tokopoint Only"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val getHomeUseCase by memoized<HomeUseCase>()
        val getHomeTokopointsDataUseCase by memoized<GetHomeTokopointsDataUseCase>()
        val getCoroutinePendingCashbackUseCase by memoized<GetCoroutinePendingCashbackUseCase>()
        val getCoroutineWalletBalanceUseCase by memoized<GetCoroutineWalletBalanceUseCase>()
        val headerDataModel = HeaderDataModel()
        Scenario("Get success data tokopoint"){
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
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().getOrNull(0)?.tokoPointDrawerData != null
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.filterIsInstance<HeaderDataModel>().getOrNull(0)?.tokoPointDrawerData != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get timeout exception search hint"){

        }
    }
})