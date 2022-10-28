package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.topquest

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.HideSectionResponse
import com.tokopedia.discovery2.usecase.HideSectionUseCase
import com.tokopedia.quest_widget.constants.QuestUrls
import com.tokopedia.quest_widget.util.LiveDataResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopQuestViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: TopQuestViewModel by lazy {
        spyk(TopQuestViewModel(application, componentsItem, 99))
    }
    private val useCase: HideSectionUseCase by lazy {
        spyk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun `test for components passed`(){
        assert(viewModel.components === componentsItem)
    }

    /**************************** test for onResume() *******************************************/
    @Test
    fun `test for onResume when shouldUpdate is true`() {
        viewModel.shouldUpdate = true
        viewModel.onResume()

        assert(viewModel.updateQuestData.value == true)
        assert(!viewModel.shouldUpdate)
    }

    @Test
    fun `test for onResume when loggedInUpdate is true`() {
        viewModel.loggedInUpdate = true
        viewModel.onResume()

        assert(!viewModel.loggedInUpdate)
        assert(viewModel.shouldUpdate)
    }
    /**************************** end of onResume() *******************************************/

    /**************************** test for loggedInCallback() *******************************************/
    @Test
    fun `test for loggedInCallback navigation data`(){
        viewModel.loggedInCallback()

        assert(viewModel.navigateData.value == QuestUrls.QUEST_URL)

    }
    @Test
    fun `test for loggedInCallback loggedInUpdate set`(){
        viewModel.loggedInUpdate = false
        viewModel.loggedInCallback()
        assert(viewModel.loggedInUpdate)
    }
    /**************************** end for loggedInCallback() *******************************************/

    /**************************** test for handleWidgetStatus() *******************************************/
    @Test
    fun `test for handleWidgetStatus Error should hide widget`(){
        viewModel.hideSectionUseCase  = useCase
        every { useCase.checkForHideSectionHandling(componentsItem) } returns HideSectionResponse(false,"")
        viewModel.handleWidgetStatus(LiveDataResult.STATUS.ERROR)
        assert(viewModel.shouldHideWidget.value == true)
    }

    @Test
    fun `test for handleWidgetStatus Empty State`(){
        viewModel.hideSectionUseCase  = useCase
        every { useCase.checkForHideSectionHandling(componentsItem) } returns HideSectionResponse(false,"")
        viewModel.handleWidgetStatus(LiveDataResult.STATUS.EMPTY_DATA)
        assert(viewModel.shouldHideWidget.value == true)
    }

    @Test
    fun `test for handleWidgetStatus Success`(){
        viewModel.hideSectionUseCase  = useCase
        every { useCase.checkForHideSectionHandling(componentsItem) } returns HideSectionResponse(false,"")
        viewModel.handleWidgetStatus(LiveDataResult.STATUS.SUCCESS)
        assert(viewModel.shouldHideWidget.value == false)
    }

    @Test
    fun `test for handleWidgetStatus Error hide section not required`(){
        viewModel.hideSectionUseCase  = useCase
        every { useCase.checkForHideSectionHandling(componentsItem) } returns HideSectionResponse(false,"")
        viewModel.handleWidgetStatus(LiveDataResult.STATUS.ERROR)
        assert(viewModel.getSyncPageLiveData().value != true)
        assert(viewModel.hideSectionLD.value.isNullOrEmpty())
    }
    @Test
    fun `test for handleWidgetStatus Error hide section required`(){
        viewModel.hideSectionUseCase  = useCase
        every { useCase.checkForHideSectionHandling(componentsItem) } returns HideSectionResponse(true,"5")
        viewModel.handleWidgetStatus(LiveDataResult.STATUS.ERROR)
        assert(viewModel.getSyncPageLiveData().value == true)
        assert(viewModel.hideSectionLD.value == "5")
    }

    /**************************** end for handleWidgetStatus() *******************************************/



}