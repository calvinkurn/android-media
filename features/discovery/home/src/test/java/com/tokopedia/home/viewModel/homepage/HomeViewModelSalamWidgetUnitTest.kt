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
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelSalamWidgetUnitTest {
    private val getSalamWidgetUseCase = mockk<GetSalamWidgetUseCase>(relaxed = true)
    private val declineSalamWidgetUseCase = mockk<DeclineSalamWIdgetUseCase>(relaxed = true)
    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private val homeViewModel: HomeViewModel = createHomeViewModel(getSalamWidgetUseCase = getSalamWidgetUseCase, declineSalamWidgetUseCase = declineSalamWidgetUseCase, getHomeUseCase = getHomeUseCase)

    @Test
    fun `Get SalamWidget Success`(){
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

        // Add Salam to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(salamDataModel)
                )
        )

        // salam data returns success
        getSalamWidgetUseCase.givenGetSalamWidgetUseCase(
                salamWidget = salamWidget
        )

        // viewmodel load salam data
        homeViewModel.getSalamWidget()

        // Expect the salam data available
        homeViewModel.salamWidgetLiveData.observeOnce {
            assert(it.peekContent().salamWidget.mainText.isNotEmpty() && it.peekContent().salamWidget.mainText.equals(salamWidget.salamWidget.mainText))
        }

        // Salam valid and submited to live data home
        homeViewModel.insertSalamWidget(salamWidget)

        // Expect the reminder salam available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.contains(salamDataModel) &&
                    (it.list.find { it == salamDataModel } as? ReminderWidgetModel)?.source == ReminderEnum.SALAM)
        }
    }

    @Test
    fun `Get SalamWidget Failed`(){
        val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)

        // Add Salam Widget to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(salamDataModel)
                )
        )

        // salam data returns success
        getSalamWidgetUseCase.givenGetSalamWidgetThrowReturn()

        // viewmodel load salam data
        homeViewModel.getSalamWidget()

        // Expect the reminder salam not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(salamDataModel))
        }
    }

    @Test
    fun `No Salam Data Available`() {
        val salamDataModel = ReminderWidgetModel(source = ReminderEnum.SALAM)

        // Not Add Salam Widget to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        // viewmodel load salam data
        homeViewModel.getSalamWidget()

        // Expect the reminder salam not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(salamDataModel))
        }
    }

    @Test
    fun `Salam Widget Available`(){
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

        // Add SalamWidget to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(salamDataModel)
                )
        )

        // insert salam to home data
        homeViewModel.insertSalamWidget(salamWidget)

        // salam data available in home data
        homeViewModel.homeLiveData.observeOnce {
            assert(it.list.contains(salamDataModel) &&
                    (it.list.find { it == salamDataModel } as? ReminderWidgetModel)?.source == ReminderEnum.SALAM &&
                    (it.list.find { it == salamDataModel } as? ReminderWidgetModel)?.data?.reminders == reminderWidget.reminders
            )
        }
    }

    @Test
    fun `SalamWidget Not Available`(){
        val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)
        val salamWidget = SalamWidget()

        // Add SalamWidget to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(salamDataModel)
                )
        )

        // insert null salam to home data
        homeViewModel.insertSalamWidget(salamWidget)

        // Expect the reminder salam not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(salamDataModel))
        }
    }

    @Test
    fun `Salam Decline`(){

        val salamDataModel = ReminderWidgetModel(source=ReminderEnum.SALAM)

        val requestParams = mapOf(
                DeclineSalamWIdgetUseCase.PARAM_WIDGET_ID to 1
        )

        val declineSalamWidget = SalamWidget(
                SalamWidgetData(
                        id = 1
                )
        )

        // Add SalamWidget to HomeDataModel
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(salamDataModel)
                )
        )

        // salam decline use case
        declineSalamWidgetUseCase.givenDeclineSalamWidgetUseCase(
                declineSalamWidget
        )

        // decline salam
        homeViewModel.declineSalamItem(requestParams)

        // Expect the reminder salam not available in home live data
        homeViewModel.homeLiveData.observeOnce {
            assert(!it.list.contains(salamDataModel))
        }
    }
}