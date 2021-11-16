package com.tokopedia.quest_widget.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.data.QuestWidgetListItem
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass
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


    @ExperimentalCoroutinesApi
    @Test
    fun `get listdata success`() {
        runBlockingTest {
            coEvery { userSession.isLoggedIn } returns true
            val questWidgetListItemMockk = listOf<QuestWidgetListItem>()
            val data = mockk<WidgetData> {
                every { questWidgetList } returns mockk {
                    every { questWidgetList } returns questWidgetListItemMockk
                    every { resultStatus } returns mockk()
                    every { pageDetail } returns mockk()
                    every { isEligible } returns true
                }
            }
            coEvery { questWidgetUseCase.getResponse(any()) } returns data
            questViewModel.getWidgetList(channel, channelSlug, page, userSession)
            assertEquals(
                questViewModel.questWidgetListLiveData.value?.status,
                LiveDataResult.STATUS.SUCCESS
            )
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get listdata null`() {
        runBlockingTest {
            coEvery { userSession.isLoggedIn } returns true
            val data = null
            coEvery { questWidgetUseCase.getResponse(any()) } returns data
            questViewModel.getWidgetList(channel, channelSlug, page, userSession)
            assertEquals(
                questViewModel.questWidgetListLiveData.value?.status,
                LiveDataResult.STATUS.ERROR
            )
        }
    }

    @Test
    fun `location not allowed`(){
          questViewModel.page = "Some random page"
        val questWidgetListItemMockk = listOf<QuestWidgetListItem>()
        val data = mockk<WidgetData> {
            every { questWidgetList } returns mockk {
                every { questWidgetList } returns questWidgetListItemMockk
                every { resultStatus } returns mockk()
                every { pageDetail } returns mockk()
                every { isEligible } returns true
            }
        }
        coEvery { questWidgetUseCase.getResponse(any()) } returns data
        coVerify(exactly = 0) { questViewModel.getQuestWidgetData(channel, channelSlug, page) }
        assertEquals(
            questViewModel.questWidgetListLiveData.value?.status,
            null
        )
    }

}