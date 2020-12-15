package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.RechargePersoItem
import com.tokopedia.recharge_component.model.WidgetSource
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by Resa on 15/12/20.
 */

class HomeViewModelRechargeBUWidgetUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getRechargeBUWidgetUseCase = mockk<GetRechargeBUWidgetUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel

//    @Test
//    fun `Recharge Decline`(){
//        val rechargeDataModel = ReminderWidgetModel(source=ReminderEnum.RECHARGE)
//
//        val requestParams = mapOf(
//                DeclineRechargeRecommendationUseCase.PARAM_UUID to "1",
//                DeclineRechargeRecommendationUseCase.PARAM_CONTENT_ID to "1")
//
//        val declineRechargeRecommendation = DeclineRechargeRecommendation(
//                isError = false,
//                message = "Not Error"
//        )
//
//        // Add Recharge Recommendation to HomeDataModel
//        getHomeUseCase.givenGetHomeDataReturn(
//                HomeDataModel(
//                        list = listOf(rechargeDataModel)
//                )
//        )
//
//        // recharge decline use case
//        declineRechargeRecommendationUseCase.givenDeclineRechargeRecommendationUseCase(
//                declineRechargeRecommendation
//        )
//
//        homeViewModel = createHomeViewModel(
//                getRechargeRecommendationUseCase = getRechargeRecommendationUseCase,
//                getHomeUseCase = getHomeUseCase,
//                declineRechargeRecommendationUseCase = declineRechargeRecommendationUseCase
//        )
//
//        // decline recharge
//        homeViewModel.declineRechargeRecommendationItem(requestParams)
//
//        // Expect the reminder recharge not available in home live data
//        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
//            assert(homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
//        }
//    }

    @Test
    fun `Recharge BU Widget Not Available`(){
        val rechargeDataModel = RechargeBUWidgetDataModel(channel = ChannelModel(id = "1", groupId = "1"))
        val rechargePerso = RechargePerso()

        // Add Recharge BU Widget to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
                getHomeUseCase = getHomeUseCase
        )

        // insert null recharge to home data
        homeViewModel.insertRechargeBUWidget(rechargePerso)

        // Expect the recharge bu widget not available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
        }

    }

    @Test
    fun `Remove recharge recommendation`(){
        val rechargeDataModel = RechargeBUWidgetDataModel(channel = ChannelModel(id = "1", groupId = "1"))
        val rechargePerso = RechargePerso()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
                getHomeUseCase = getHomeUseCase
        )

        // insert null recharge to home data
        homeViewModel.insertRechargeBUWidget(rechargePerso)

        // Expect the recharge bu widget not available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
        }

    }

    @Test
    fun `Recharge Recommendation Available`(){
        val rechargeDataModel = RechargeBUWidgetDataModel(channel = ChannelModel(id = "1", groupId = "1"))
        val rechargePerso = RechargePerso(
                "Title",
                items = listOf(
                        RechargePersoItem(
                                "1",
                                "Title",
                                "tokopedia.com/image.png",
                                "full",
                                "#000000",
                                "Subtitle",
                                "",
                                "Label1",
                                "",
                                "Label2",
                                "Label3",
                                "tokopedia://link",
                                "https://www.tokopedia.com",
                                listOf()
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
                getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
                getHomeUseCase = getHomeUseCase
        )

        // insert recharge to home data
        homeViewModel.insertRechargeBUWidget(rechargePerso)

        // recharge data available in home data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? RechargeBUWidgetDataModel)?.data?.title == "Title" &&
                    (homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? RechargeBUWidgetDataModel)?.data?.items?.size == rechargePerso.items.size
            )
        }
    }

    @Test
    fun `No Recharge Data Available`() {
        val rechargeDataModel = RechargeBUWidgetDataModel(channel = ChannelModel(id = "1", groupId = "1"))

        // Not Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        homeViewModel = createHomeViewModel(
                getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
                getHomeUseCase = getHomeUseCase
        )

        // viewmodel load recharge data
        homeViewModel.getRechargeBUWidget(WidgetSource.TOPUP_BILLS)

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(rechargeDataModel))
        }
    }

    @Test
    fun `Get Recharge Recommendation Failed`(){
        val rechargeDataModel = RechargeBUWidgetDataModel(channel = ChannelModel(id = "1", groupId = "1"))

        // Add Recharge Recommendation to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(rechargeDataModel)
                )
        )

        // recharge data returns success
        getRechargeBUWidgetUseCase.givenGetRechargeBUWidgetThrowReturn()

        homeViewModel = createHomeViewModel(
                getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
                getHomeUseCase = getHomeUseCase
        )

        // viewmodel load recharge data
        homeViewModel.getRechargeBUWidget(WidgetSource.TOPUP_BILLS)

        // Expect the reminder recharge not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(rechargeDataModel))
        }
    }

    @Test
    fun `Get Recharge Recommendation Success`(){
        val rechargeDataModel = RechargeBUWidgetDataModel(channel = ChannelModel(id = "1", groupId = "1"))
        val rechargePerso = RechargePerso(
                "Title",
                items = listOf(
                        RechargePersoItem(
                                "1",
                                "Title",
                                "tokopedia.com/image.png",
                                "full",
                                "#000000",
                                "Subtitle",
                                "",
                                "Label1",
                                "",
                                "Label2",
                                "Label3",
                                "tokopedia://link",
                                "https://www.tokopedia.com",
                                listOf()
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
        getRechargeBUWidgetUseCase.givenGetRechargeBUWidgetUseCase(
                rechargePerso = rechargePerso
        )

        homeViewModel = createHomeViewModel(
                getRechargeBUWidgetUseCase = getRechargeBUWidgetUseCase,
                getHomeUseCase = getHomeUseCase
        )

        // viewmodel load recharge data
        homeViewModel.getRechargeBUWidget(WidgetSource.TOPUP_BILLS)

        // Expect the recharge data available
        homeViewModel.rechargeBUWidgetLiveData.observeOnce {
            assert(it.peekContent().title.isNotEmpty() && it.peekContent().title == rechargePerso.title)
        }

        // Recharge valid and submited to live data home
        homeViewModel.insertRechargeBUWidget(rechargePerso)

        // Expect the reminder recharge available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? RechargeBUWidgetDataModel)?.data?.title == "Title" &&
                    (homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? RechargeBUWidgetDataModel)?.data?.items?.size == rechargePerso.items.size
            )
        }

    }

}

