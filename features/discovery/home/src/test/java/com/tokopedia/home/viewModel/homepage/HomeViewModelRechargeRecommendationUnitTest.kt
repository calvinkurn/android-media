package com.tokopedia.home.viewModel.homepage

import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
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
class HomeViewModelRechargeRecommendationUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Get Recharge Recommendation"){
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val getRechargeRecommendationUseCase by memoized<GetRechargeRecommendationUseCase>()
        val declineRechargeRecommendationUseCase by memoized<DeclineRechargeRecommendationUseCase>()
        val getHomeUseCase by memoized<HomeUseCase>()

        //Method getRechargeRecommendation()

        Scenario("Get Recharge Recommendation Success"){
            val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)
            val rechargeRecommendation = RechargeRecommendation(
                    "1",
                    listOf(
                            RechargeRecommendationData(
                                    "1",
                                    "Main Text",
                                    "Sub Text",
                                    "tokopedia://recharge",
                                    "tokopedia://link",
                                    "tokopedia.com/image.png",
                                    "Judul",
                                    "#000000",
                                   " Silahkan Bayar Sekarang"
                            )
                    )
            )

            Given("Add Recharge Recommendation to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(rechargeDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            Given("recharge data returns success"){
                getRechargeRecommendationUseCase.givenGetRechargeRecommendationUseCase(
                        rechargeRecommendation = rechargeRecommendation
                )
            }

            When("viewmodel load recharge data"){
                homeViewModel.getRechargeRecommendation()
            }

            Then("Expect the recharge data available"){
                homeViewModel.rechargeRecommendationLiveData.observeOnce {
                    assert(it.peekContent().UUID.isNotEmpty() && it.peekContent().UUID.equals(rechargeRecommendation.UUID))
                }
            }

            When("Recharge valid and submited to live data home"){
                homeViewModel.insertRechargeRecommendation(rechargeRecommendation)
            }

            Then("Expect the reminder recharge available in home live data"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(rechargeDataModel) &&
                            (it.list.find { it == rechargeDataModel } as? ReminderWidgetModel)?.source == ReminderEnum.RECHARGE)
                }
            }
        }

        Scenario("Get Recharge Recommendation Failed"){
            val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)

            Given("Add Recharge Recommendation to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(rechargeDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            Given("recharge data returns success"){
                getRechargeRecommendationUseCase.givenGetRechargeRecommendationThrowReturn()
            }

            When("viewmodel load recharge data"){
                homeViewModel.getRechargeRecommendation()
            }

            Then("Expect the reminder recharge not available in home live data"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(rechargeDataModel))
                }
            }
        }

        Scenario("No Recharge Data Available") {
            val rechargeDataModel = ReminderWidgetModel(source = ReminderEnum.RECHARGE)

            Given("Not Add Recharge Recommendation to HomeDataModel") {
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf()
                        )
                )
            }

            Given("home viewmodel") {
                homeViewModel = createHomeViewModel()
            }

            When("viewmodel load recharge data") {
                homeViewModel.getRechargeRecommendation()
            }

            Then("Expect the reminder recharge not available in home live data") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(rechargeDataModel))
                }
            }
        }

        //Method insertRechargeRecommendation(data: RechargeRecommendation)

        Scenario("Recharge Recommendation Available"){
            val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)
            val rechargeRecommendation = RechargeRecommendation(
                    "1",
                    listOf(
                            RechargeRecommendationData(
                                    "1",
                                    "Main Text",
                                    "Sub Text",
                                    "tokopedia://recharge",
                                    "tokopedia://link",
                                    "tokopedia.com/image.png",
                                    "Judul",
                                    "#000000",
                                    " Silahkan Bayar Sekarang"
                            )
                    )
            )

            val reminderWidget = ReminderWidget(
                    listOf(
                            ReminderData(
                                    "tokopedia://recharge",
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

            Given("Add Recharge Recommendation to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(rechargeDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            When("insert recharge to home data"){
                homeViewModel.insertRechargeRecommendation(rechargeRecommendation)
            }

            Then("recharge data available in home data"){
                homeViewModel.homeLiveData.observeOnce {
                    assert(it.list.contains(rechargeDataModel) &&
                            (it.list.find { it == rechargeDataModel } as? ReminderWidgetModel)?.source == ReminderEnum.RECHARGE &&
                            (it.list.find { it == rechargeDataModel } as? ReminderWidgetModel)?.data?.reminders == reminderWidget.reminders
                    )
                }
            }

        }

        Scenario("Recharge Recommendation Not Available"){
            val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)
            val rechargeRecommendation = RechargeRecommendation(
                    "",
                    listOf()
            )

            Given("Add Recharge Recommendation to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(rechargeDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            When("insert null recharge to home data"){
                homeViewModel.insertRechargeRecommendation(rechargeRecommendation)
            }

            Then("Expect the reminder recharge not available in home live data") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(rechargeDataModel))
                }
            }

        }

        //Method declineRechargeRecommendationItem(requestParams: Map<String, String>)

        Scenario("Recharge Decline"){

            val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)

            val requestParams = mapOf(
                    DeclineRechargeRecommendationUseCase.PARAM_UUID to "1",
                    DeclineRechargeRecommendationUseCase.PARAM_CONTENT_ID to "1")

            val declineRechargeRecommendation = DeclineRechargeRecommendation(
                    isError = false,
                    message = "Not Error"
            )

            Given("Add Recharge Recommendation to HomeDataModel"){
                getHomeUseCase.givenGetHomeDataReturn(
                        HomeDataModel(
                                list = listOf(rechargeDataModel)
                        )
                )
            }

            Given("home viewmodel"){
                homeViewModel = createHomeViewModel()
            }

            Given("recharge decline use case"){
                declineRechargeRecommendationUseCase.givenDeclineRechargeRecommendationUseCase(
                        declineRechargeRecommendation
                )
            }

            When("decline recharge"){
                homeViewModel.declineRechargeRecommendationItem(requestParams)
            }

            Then("Expect the reminder recharge not available in home live data") {
                homeViewModel.homeLiveData.observeOnce {
                    assert(!it.list.contains(rechargeDataModel))
                }
            }
        }

    }
})

