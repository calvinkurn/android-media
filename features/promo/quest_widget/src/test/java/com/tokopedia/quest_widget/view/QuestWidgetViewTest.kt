package com.tokopedia.quest_widget.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.quest_widget.data.QuestWidgetList
import com.tokopedia.quest_widget.data.ResultStatus
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class QuestWidgetViewTest{

    private val userSession: UserSession = mockk(relaxed = true)
    private val questWidgetUseCase: QuestWidgetUseCase = mockk(relaxed = true)
    @ExperimentalCoroutinesApi
    val workerDispatcher= TestCoroutineDispatcher()

    lateinit var questViewModel: QuestWidgetViewModel
    val channel = 0
    val channelSlug = ""
    val page = "myreward"

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        questViewModel = spyk(QuestWidgetViewModel(workerDispatcher, questWidgetUseCase))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `not logged in`(){
        coEvery { userSession.isLoggedIn } returns false
        questViewModel.getWidgetList(channel, channelSlug, page, userSession)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status, LiveDataResult.STATUS.NON_LOGIN)
    }

    @Test
    fun `empty page not allowed`(){
          questViewModel.page = ""
        coEvery { userSession.isLoggedIn } returns true

        questViewModel.getWidgetList(channel, channelSlug, page, userSession)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun `empty page allowed`(){
          coEvery { userSession.isLoggedIn } returns true
        questViewModel.getWidgetList(channel, channelSlug,"", userSession)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status,LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun `get quest widget data with null response`(){
        coEvery { questWidgetUseCase.getResponse(any()) } returns null
        questViewModel.getQuestWidgetData(channel, channelSlug, page)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status,LiveDataResult.STATUS.EMPTY_DATA)
    }

    @Test
    fun `get quest widget data with not 200 status code`(){
        val widgetData : WidgetData = mockk()
        val widgetList = QuestWidgetList(questWidgetList = listOf())
        every { widgetData.questWidgetList } returns widgetList
        coEvery { questWidgetUseCase.getResponse(any()) } returns widgetData
        questViewModel.getQuestWidgetData(channel, channelSlug, page)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status,LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun `get quest widget data with no eligibility`(){
        val widgetData : WidgetData = mockk()
        val widgetList = QuestWidgetList(questWidgetList = listOf(), resultStatus = ResultStatus(code = "200"), isEligible = false)
        every { widgetData.questWidgetList } returns widgetList
        coEvery { questWidgetUseCase.getResponse(any()) } returns widgetData
        questViewModel.getQuestWidgetData(channel, channelSlug, page)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status,LiveDataResult.STATUS.EMPTY_DATA)
    }

    @Test
    fun `get quest widget data with eligibility`(){
        val widgetData : WidgetData = mockk()
        val widgetList = QuestWidgetList(questWidgetList = listOf(), resultStatus = ResultStatus(code = "200"), isEligible = true)
        every { widgetData.questWidgetList } returns widgetList
        coEvery { questWidgetUseCase.getResponse(any()) } returns widgetData
        questViewModel.getQuestWidgetData(channel, channelSlug, page)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status,LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun `get quest widget data with error`(){
        coEvery { questWidgetUseCase.getResponse(any()) } throws Exception("error")
        questViewModel.getQuestWidgetData(channel, channelSlug, page)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status,LiveDataResult.STATUS.ERROR)
    }
}
