package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetBusinessUnitDataUseCase
import com.tokopedia.home.beranda.domain.interactor.GetBusinessWidgetTab
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
@Suppress("NAME_SHADOWING")
class HomeViewModelBusinessUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get Tab Data") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getBusinessWidgetTab by memoized<GetBusinessWidgetTab>()
        val getBusinessUnitDataUseCase by memoized<GetBusinessUnitDataUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()

        Scenario("Get bu tab success && bu data success") {
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val businessUnitDataModel = NewBusinessUnitWidgetDataModel()

            Given("dynamic banner") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(businessUnitDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Given("load tab data return success") {
                val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
                getBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget)
            }

            Given("load data bu return success"){
                getBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseReturn(listOf(
                        BusinessUnitItemDataModel(
                                content = HomeWidget.ContentItemTab(),
                                itemPosition = 0
                        )
                ))
            }

            When("viewModel load business tab") {
                homeViewModel.getBusinessUnitTabData(0)
            }

            When("viewModel load business data") {
                homeViewModel.getBusinessUnitData(1, 0)
            }

            Then("Expect tabs data on live data available") {
                verifyOrder {
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).tabList == null
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).tabList != null &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList != null
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).tabList != null &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList != null &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList?.first()?.list != null
                    })
                }
                confirmVerified(observerHome)
            }
        }

        Scenario("Get bu tab success && bu data first error") {
            val businessUnitDataModel = NewBusinessUnitWidgetDataModel()
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            Given("dynamic banner") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(businessUnitDataModel)
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
                homeViewModel.homeLiveData.observeForever(observerHome)
            }

            Given("load tab data returns success") {
                val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
                getBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget)
            }

            Given("load data bu return error"){
                getBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseThrowReturn()
            }

            When("viewModel load business tab") {
                homeViewModel.getBusinessUnitTabData(0)
            }

            When("viewModel load business data") {
                homeViewModel.getBusinessUnitData(1, 0)
            }

            Then("Expect tabs data on live data available") {
                verifyOrder {
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).tabList == null
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).tabList != null &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList != null
                    })
                    observerHome.onChanged(match {
                        it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).tabList != null &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList != null &&
                                (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList?.first()?.list?.isEmpty() == true
                    })
                }
                confirmVerified(observerHome)
            }
        }
    }
})