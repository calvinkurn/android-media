package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
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
    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

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

        assert(homeViewModel.homeDataModel.list.find{ it::class.java == rechargeDataModel::class.java } == null)
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

        // Expect recharge bu widget not available in home live data
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
                                "",
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

        // Not Add Recharge BU Widget to HomeDataModel
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

        // Expect recharge bu widget not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(rechargeDataModel))
        }
    }

    @Test
    fun `Get Recharge Recommendation Failed`(){
        val rechargeDataModel = RechargeBUWidgetDataModel(data = RechargePerso(), channel = ChannelModel(id = "1", groupId = "1"))

        // Add Recharge BU Widget to HomeDataModel
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

        assert(!homeViewModel.homeDataModel.list.contains(rechargeDataModel))
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
                                "",
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

        // Add Recharge BU Widget to HomeDataModel
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

        // Expect recharge bu widget available in home live data
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? RechargeBUWidgetDataModel)?.data?.title == "Title" &&
                    (homeDataModel.list.find { it::class.java == rechargeDataModel::class.java } as? RechargeBUWidgetDataModel)?.data?.items?.size == rechargePerso.items.size
            )
        }

    }

}

