package com.tokopedia.home.viewModel

import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetBusinessUnitDataUseCase
import com.tokopedia.home.beranda.domain.interactor.GetBusinessWidgetTab
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardViewModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
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

            Then("Expect tabs data on live data available") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(businessUnitDataModel) && (it.list.find { it == businessUnitDataModel } as? NewBusinessUnitWidgetDataModel)?.tabList != null
                            && (it.list.find { it == businessUnitDataModel } as? NewBusinessUnitWidgetDataModel)?.backColor == "red"
                    )
                }
            }

            Then("Expect data bu available"){

            }


        }

        Scenario("Get bu tab success && bu data first") {
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
            }

            Given("load tab data returns success") {
                val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
                getBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget)
            }

            When("viewModel load business tab") {
                homeViewModel.getBusinessUnitTabData(0)
            }

            Then("Expect tabs data on live data available") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(businessUnitDataModel) && (it.list.find { it == businessUnitDataModel } as? NewBusinessUnitWidgetDataModel)?.tabList != null
                            && (it.list.find { it == businessUnitDataModel } as? NewBusinessUnitWidgetDataModel)?.backColor == "red"
                    )
                }
            }
        }
    }
})