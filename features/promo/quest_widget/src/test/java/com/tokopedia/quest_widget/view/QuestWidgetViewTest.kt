package com.tokopedia.quest_widget.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.quest_widget.constants.QuestWidgetLocations
import com.tokopedia.quest_widget.domain.QuestWidgetUseCase
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.test.assertEquals

class QuestWidgetViewTest{


    val userSession: UserSession = mockk(relaxed = true)
    val questWidgetUseCase: QuestWidgetUseCase = mockk(relaxed = true)
    val workerDispatcher: TestCoroutineDispatcher = mockk(relaxed = true)

    lateinit var questViewModel: QuestWidgetViewModel
    val channel = 0
    val channelSlug = ""
    val page = "myreward"

    @get:Rule
    var rule = InstantTaskExecutorRule()

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
    fun `is logged in`(){
        coEvery { userSession.isLoggedIn } returns true
        questViewModel.getWidgetList(channel, channelSlug, page, userSession)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status, LiveDataResult.STATUS.LOADING)
        verifyOrder {
//            assertEquals(questViewModel.questWidgetListLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
        }
    }

    @Test
    fun `not logged in`(){
        coEvery { userSession.isLoggedIn } returns false
        questViewModel.getWidgetList(channel, channelSlug, page, userSession)
        assertEquals(questViewModel.questWidgetListLiveData.value?.status, LiveDataResult.STATUS.NON_LOGIN)
    }

    @Test
    fun `reward page`(){
        coEvery { questViewModel.page } returns QuestWidgetLocations.MY_REWARD
        coVerify { questViewModel.getQuestWidgetData(channel, channelSlug, page) }
    }

    @Test
    fun `home page`(){
        coEvery { questViewModel.page } returns QuestWidgetLocations.HOME_PAGE
        coVerify { questViewModel.getQuestWidgetData(channel, channelSlug, page) }
    }

    @Test
    fun `disco page`(){
        coEvery { questViewModel.page } returns QuestWidgetLocations.DISCO
        coVerify { questViewModel.getQuestWidgetData(channel, channelSlug, page) }
    }

    @Test
    fun `location not allowed`(){
        coEvery { questViewModel.page } returns "Some random page"
        coVerify(exactly = 0) { questViewModel.getQuestWidgetData(channel, channelSlug, page) }
    }

    @Test
    fun `empty page`(){
        coEvery { questViewModel.page } returns ""
        coVerify(exactly = 0) { questViewModel.getQuestWidgetData(channel, channelSlug, page) }
    }

}