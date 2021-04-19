package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetBusinessUnitDataUseCase
import com.tokopedia.home.beranda.domain.interactor.GetBusinessWidgetTab
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.NewBusinessUnitWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelBusinessUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private val getBusinessWidgetTab = mockk<GetBusinessWidgetTab>(relaxed = true)
    private val getBusinessUnitDataUseCase = mockk<GetBusinessUnitDataUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    @Test
    fun `Get Tab Data success && bu data success`(){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel()

        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(businessUnitDataModel)
                )
        )

        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                getBusinessUnitDataUseCase = getBusinessUnitDataUseCase,
                getBusinessWidgetTab = getBusinessWidgetTab
        )
        homeViewModel.homeLiveData.observeForever(observerHome)

        // load tab data return success
        val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
        getBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget)

        // load data bu return success
        getBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseReturn(listOf(
                BusinessUnitItemDataModel(
                        content = HomeWidget.ContentItemTab(),
                        itemPosition = 0,
                        tabPosition = 0,
                        tabName = "Keuangan"
                )
        ))

        // viewModel load business tab
        homeViewModel.getBusinessUnitTabData(0)

        // viewModel load business data
        homeViewModel.getBusinessUnitData(1, 0, "Keuangan")

        // Expect tabs data on live data available
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

    @Test
    fun `Update BU Data success`(){
        runBlocking {
            val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
            val businessUnitDataModel = NewBusinessUnitWidgetDataModel()
            val homeDataModel = HomeDataModel(
                    list = listOf(businessUnitDataModel)
            )

            val channel = Channel<HomeDataModel>()
            // dynamic banner
            coEvery { getHomeUseCase.getHomeData() } returns flow{
                channel.consumeAsFlow().collect{
                    emit(it)
                }
            }
            launch{
                channel.send(homeDataModel.copy())
            }
            homeViewModel = createHomeViewModel(
                    getHomeUseCase = getHomeUseCase,
                    getBusinessUnitDataUseCase = getBusinessUnitDataUseCase,
                    getBusinessWidgetTab = getBusinessWidgetTab
            )
            homeViewModel.homeLiveData.observeForever(observerHome)

            // load tab data return success
            val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
            getBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget)

            // load data bu return success
            getBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseReturn(listOf(
                    BusinessUnitItemDataModel(
                            content = HomeWidget.ContentItemTab(),
                            itemPosition = 0,
                            tabPosition = 0,
                            tabName = "Keuangan"
                    )
            ))

            // viewModel load business tab
            homeViewModel.getBusinessUnitTabData(0)

            // viewModel load business data
            homeViewModel.getBusinessUnitData(1, 0, "Keuangan")

            // Expect tabs data on live data available
            homeViewModel.homeLiveData.value?.let{
                assert(it.list.isNotEmpty() && it.list.first() is NewBusinessUnitWidgetDataModel &&
                        (it.list.first() as NewBusinessUnitWidgetDataModel).tabList != null &&
                        (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList != null &&
                        (it.list.first() as NewBusinessUnitWidgetDataModel).contentsList?.first()?.list != null)
            }
        }
    }

    @Test
    fun `Get bu tab success && bu data first error`(){
        val businessUnitDataModel = NewBusinessUnitWidgetDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        // dynamic banner
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(businessUnitDataModel)
                )
        )
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                getBusinessUnitDataUseCase = getBusinessUnitDataUseCase,
                getBusinessWidgetTab = getBusinessWidgetTab
        )
        homeViewModel.homeLiveData.observeForever(observerHome)

        // load tab data returns success
        val homeWidget = HomeWidget(tabBusinessList = listOf(HomeWidget.TabItem(1, "")), widgetHeader = HomeWidget.WidgetHeader("red"))
        getBusinessWidgetTab.givenGetBusinessWidgetTabUseCaseReturn(homeWidget)

        // load data bu return error
        getBusinessUnitDataUseCase.givenGetBusinessUnitDataUseCaseThrowReturn()

        // viewModel load business tab
        homeViewModel.getBusinessUnitTabData(0)

        // viewModel load business data
        homeViewModel.getBusinessUnitData(1, 0, "Keuangan")

        // Expect tabs data on live data available
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