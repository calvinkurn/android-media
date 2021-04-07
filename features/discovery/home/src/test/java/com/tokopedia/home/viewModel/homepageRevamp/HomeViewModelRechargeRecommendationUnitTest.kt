package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.viewModel.homepage.givenDeclineRechargeRecommendationUseCase
import com.tokopedia.home.viewModel.homepage.givenGetRechargeRecommendationThrowReturn
import com.tokopedia.home.viewModel.homepage.givenGetRechargeRecommendationUseCase
import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.model.ReminderWidget
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelRechargeRecommendationUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getRechargeRecommendationUseCase = mockk<GetRechargeRecommendationUseCase>(relaxed = true)
    private val declineRechargeRecommendationUseCase = mockk<DeclineRechargeRecommendationUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `Recharge Decline`(){
        val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)

        val requestParams = mapOf(
                DeclineRechargeRecommendationUseCase.PARAM_UUID to "1",
                DeclineRechargeRecommendationUseCase.PARAM_CONTENT_ID to "1")

        val declineRechargeRecommendation = DeclineRechargeRecommendation(
                isError = false,
                message = "Not Error"
        )

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )

        // recharge decline use case
        declineRechargeRecommendationUseCase.givenDeclineRechargeRecommendationUseCase(
                declineRechargeRecommendation
        )

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )

        // decline recharge
        homeViewModel.declineRechargeRecommendationItem(requestParams)

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
        }
    }

    @Test
    fun `Recharge Recommendation Not Available`(){
        val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)
        val rechargeRecommendation = RechargeRecommendation(
                "",
                listOf()
        )

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )

        // insert null recharge to home data
        homeViewModel.insertRechargeRecommendation(rechargeRecommendation)

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
        }

    }
    @Test
    fun `Remove recharge recommendation`(){
        val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)
        val rechargeRecommendation = RechargeRecommendation(
                "",
                listOf()
        )

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )

        // insert null recharge to home data
        homeViewModel.insertRechargeRecommendation(rechargeRecommendation)

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
        }

    }

    @Test
    fun `Recharge Recommendation Available`(){
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
                                "",
                                " Silahkan Bayar Sekarang"
                        )
                )
        )

        val reminderWidget = ReminderWidget("1",
                listOf(
                        ReminderData(
                                "tokopedia://recharge",
                                listOf(""),
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

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )

        // insert recharge to home data
        homeViewModel.insertRechargeRecommendation(rechargeRecommendation)

        // recharge data available in home data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? ReminderWidgetModel)?.source == ReminderEnum.RECHARGE &&
                    (homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? ReminderWidgetModel)?.data?.reminders?.size == reminderWidget.reminders.size
            )
        }
    }

    @Test
    fun `No Recharge Data Available`() {
        val rechargeDataModel = ReminderWidgetModel(source = ReminderEnum.RECHARGE)

        // Not Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )

        // viewmodel load recharge data
        homeViewModel.getRechargeRecommendation()

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(rechargeDataModel))
        }
    }

    @Test
    fun `Get Recharge Recommendation Failed`(){
        val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )

        // recharge data returns success
        getRechargeRecommendationUseCase.givenGetRechargeRecommendationThrowReturn()

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )
        // viewmodel load recharge data
        homeViewModel.getRechargeRecommendation()

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(rechargeDataModel))
        }
    }

    @Test
    fun `Get Recharge Recommendation Success`(){
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
                                "",
                                " Silahkan Bayar Sekarang"
                        )
                )
        )

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )


        // recharge data returns success
        getRechargeRecommendationUseCase.givenGetRechargeRecommendationUseCase(
                rechargeRecommendation = rechargeRecommendation
        )

        homeViewModel = createHomeViewModel(
                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
                getHomeUseCase = getHomeUseCase,
                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
        )

        // viewmodel load recharge data
        homeViewModel.getRechargeRecommendation()

        // Expect the recharge data available
        homeViewModel.rechargeRecommendationLiveData.observeOnce {
            assert(it.peekContent().UUID.isNotEmpty() && it.peekContent().UUID.equals(rechargeRecommendation.UUID))
        }

        // Recharge valid and submited to live data home
        homeViewModel.insertRechargeRecommendation(rechargeRecommendation)

        // Expect the reminder recharge available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? ReminderWidgetModel)?.source == ReminderEnum.RECHARGE)
        }

    }

}

