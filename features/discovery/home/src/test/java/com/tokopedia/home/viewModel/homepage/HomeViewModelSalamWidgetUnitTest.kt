package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.DeclineSalamWIdgetUseCase
import com.tokopedia.home.beranda.domain.interactor.GetSalamWidgetUseCase
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidgetData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.model.ReminderWidget
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@ExperimentalCoroutinesApi
class HomeViewModelSalamWidgetUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get Salam Widget"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getSalamWidgetUseCase by memoized<GetSalamWidgetUseCase>()
        val declineSalamWIdgetUseCase by memoized<DeclineSalamWIdgetUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()

        //Method getSalamWidget()

        Scenario("Get SalamWidget Success"){
            val salamDataModel = ReminderWidgetModel(source= ReminderEnum.SALAM)
            val salamWidget = SalamWidget(
                    SalamWidgetData(
                            "tokopedia://salam",
                            "#000000",
                            "Silahkan Bayar Sekarang",
                            1,
                            "tokopedia.com/image.png",
                            "tokopedia://link",
                            "Main Text",
                            "Sub Text",
                            "Judul"
                    )
            )

            Given("Add Salam to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(salamDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            Given("salam data returns success"){
                getSalamWidgetUseCase.givenGetSalamWidgetUseCase(
                        salamWidget = salamWidget
                )
            }

            When("viewmodel load salam data"){
                homeViewModel.getSalamWidget()
            }

            Then("Expect the salam data available"){
                homeViewModel.salamWidgetLiveData.observeOnce {
                    assert(it.peekContent().salamWidget.mainText.isNotEmpty() && it.peekContent().salamWidget.mainText.equals(salamWidget.salamWidget.mainText))
                }
            }

            When("Salam valid and submited to live data home"){
                homeViewModel.insertSalamWidget(salamWidget)
            }

            Then("Expect the reminder salam available in home live data"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(salamDataModel) &&
                            (it.list.find { it == salamDataModel } as? ReminderWidgetModel)?.source == ReminderEnum.SALAM)
                }
            }

        }

        Scenario("Get SalamWidget Failed"){
            val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)

            Given("Add Salam Widget to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(salamDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            Given("salam data returns success"){
                getSalamWidgetUseCase.givenGetSalamWidgetThrowReturn()
            }

            When("viewmodel load salam data"){
                homeViewModel.getSalamWidget()
            }

            Then("Expect the reminder salam not available in home live data"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(salamDataModel))
                }
            }
        }

        Scenario("No Salam Data Available") {
            val salamDataModel = ReminderWidgetModel(source = ReminderEnum.SALAM)

            Given("Not Add Salam Widget to HomeDataModel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf()
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }

            When("viewmodel load salam data") {
                homeViewModel.getSalamWidget()
            }

            Then("Expect the reminder salam not available in home live data") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(salamDataModel))
                }
            }
        }

        //Method insertSalamWidget(data: SalamWidget)

        Scenario("Salam Widget Available"){
            val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)
            val salamWidget = SalamWidget(
                    SalamWidgetData(
                            "tokopedia://salam",
                            "#000000",
                            "Silahkan Bayar Sekarang",
                            1,
                            "tokopedia.com/image.png",
                            "tokopedia://link",
                            "Main Text",
                            "Sub Text",
                            "Judul"
                    )
            )

            val reminderWidget = ReminderWidget(
                    listOf(
                            ReminderData(
                                    "tokopedia://salam",
                                    "#000000",
                                    "Silahkan Bayar Sekarang",
                                    "1",
                                    "tokopedia.com/image.png",
                                    "tokopedia://link",
                                    "Main Text",
                                    "Sub Text",
                                    "Judul",
                                    "1"
                            )
                    )
            )

            Given("Add SalamWidget to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(salamDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            When("insert salam to home data"){
                homeViewModel.insertSalamWidget(salamWidget)
            }

            Then("salam data available in home data"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(salamDataModel) &&
                            (it.list.find { it == salamDataModel } as? ReminderWidgetModel)?.source == ReminderEnum.SALAM &&
                            (it.list.find { it == salamDataModel } as? ReminderWidgetModel)?.data?.reminders == reminderWidget.reminders
                    )
                }
            }

        }

        Scenario("SalamWidget Not Available"){
            val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)
            val salamWidget = SalamWidget()

            Given("Add SalamWidget to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(salamDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            When("insert null salam to home data"){
                homeViewModel.insertSalamWidget(salamWidget)
            }

            Then("Expect the reminder salam not available in home live data") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(salamDataModel))
                }
            }

        }

        //Method declineSalam(requestParams: Map<String, Int>)

        Scenario("Salam Decline"){

            val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)

            val requestParams = mapOf(
                    DeclineSalamWIdgetUseCase.PARAM_WIDGET_ID to 1
            )

            val declineSalamWidget = SalamWidget(
                   SalamWidgetData(
                           iD = 1
                   )
            )

            Given("Add SalamWidgetto HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(salamDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            Given("salam decline use case"){
                declineSalamWIdgetUseCase.givenDeclineSalamWidgetUseCase(
                        declineSalamWidget
                )
            }

            When("decline salam"){
                homeViewModel.declineSalamItem(requestParams)
            }

            Then("Expect the reminder salam not available in home live data") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(salamDataModel))
                }
            }
        }


    }
})