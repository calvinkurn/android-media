package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.QuestWidgetModel
import com.tokopedia.quest_widget.data.QuestData
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class HomeViewModelQuestWidgetTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `test when quest widget delete then quest widget not in the list of visitables`(){
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDataModel(
                isCache = false,
                list = listOf(QuestWidgetModel(channelModel = ChannelModel(id = "1",groupId = "1"), questData = QuestData()))
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.deleteQuestWidget()
        assert( homeViewModel.homeLiveData.value?.list?.any { it is QuestWidgetModel } == false )

    }

}