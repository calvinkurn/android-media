package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.HideSectionResponse
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.HideSectionUseCase
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import io.mockk.MockKAnnotations
import io.mockk.OfTypeMatcher
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscoveryPlayWidgetViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context: Context = mockk()
    private val playWidgetTool: PlayWidgetTools = mockk()
    private var viewModel: DiscoveryPlayWidgetViewModel =
        spyk(
            DiscoveryPlayWidgetViewModel(
                application,
                componentsItem,
                99
            )
        ).apply { playWidgetTools = playWidgetTool }

    private val hideSectionUseCase: HideSectionUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        mockkConstructor(UserSession::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        every { application.applicationContext } returns context
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkStatic(::getComponent)
        unmockkConstructor(UserSession::class)
        unmockkConstructor(URLParser::class)
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`() {
        assert(viewModel.application === application)
    }

    /**************************** test for hitPlayWidgetService() *******************************************/
    @Test
    fun `test for hitPlayWidgetService`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)
        every { componentsItem.data } returns list
        val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
        viewModel.playWidgetTools = playWidgetTools
        val playWidget: PlayWidget = mockk(relaxed = true)
        coEvery { playWidgetTools.getWidgetFromNetwork(any()) } returns playWidget
        val playWidgetState: PlayWidgetState = mockk(relaxed = true)
        coEvery { playWidgetTools.mapWidgetToModel(any()) } returns playWidgetState

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value, playWidgetState)
    }

    @Test
    fun `test for hitPlayWidgetService when throws error`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)
        every { componentsItem.data } throws Exception("error")

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value, null)
    }

    @Test
    fun `test for hitPlayWidgetService when playWidgetPlayID is null`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(DiscoveryPlayWidgetViewModel(application, componentsItem, 99))
        viewModel.hideSectionUseCase = hideSectionUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = null)
        list.add(item)

        val hideSectionResponse = HideSectionResponse(true, "21")
        coEvery { hideSectionUseCase.checkForHideSectionHandling(any()) } returns hideSectionResponse

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.hideSectionLD.value, "21")
    }

    @Test
    fun `test for hitPlayWidgetService when playWidgetPlayID and sectionId is null`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(DiscoveryPlayWidgetViewModel(application, componentsItem, 99))
        viewModel.hideSectionUseCase = hideSectionUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = null)
        list.add(item)

        val hideSectionResponse = HideSectionResponse(true, "")
        coEvery { hideSectionUseCase.checkForHideSectionHandling(any()) } returns hideSectionResponse

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.hideSectionLD.value, null)
    }

    @Test
    fun `test for hitPlayWidgetService when playWidgetPlayID throws error`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(DiscoveryPlayWidgetViewModel(application, componentsItem, 99))
        viewModel.hideSectionUseCase = hideSectionUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)

        val hideSectionResponse = HideSectionResponse(true, "21")
        coEvery { hideSectionUseCase.checkForHideSectionHandling(any()) } returns hideSectionResponse
        every { componentsItem.data } throws Exception("error")

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.hideSectionLD.value, "21")
    }

    @Test
    fun `test for hitPlayWidgetService when playWidgetPlayID throws error and sectionId is null`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(DiscoveryPlayWidgetViewModel(application, componentsItem, 99))
        viewModel.hideSectionUseCase = hideSectionUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)

        val hideSectionResponse = HideSectionResponse(true, "")
        coEvery { hideSectionUseCase.checkForHideSectionHandling(any()) } returns hideSectionResponse
        every { componentsItem.data } throws Exception("error")

        viewModel.hitPlayWidgetService()

        TestCase.assertEquals(viewModel.hideSectionLD.value, null)
    }

    /**************************** test for updatePlayWidgetTotalView() *******************************************/
    @Test
    fun `test for updatePlayWidgetTotalView`() {
        `test for hitPlayWidgetService`()
        val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
        viewModel.playWidgetTools = playWidgetTools
        val playWidgetState: PlayWidgetState = mockk(relaxed = true)
        coEvery { playWidgetTools.updateTotalView(any(), any(), any()) } returns playWidgetState
        viewModel.updatePlayWidgetTotalView("4", "3")

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value, playWidgetState)

    }

    /**************************** test for getPlayWidgetData() *******************************************/
    @Test
    fun `test for getPlayWidgetData when dataPresent is false`() {

        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)
        every { componentsItem.data } returns list
        val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
        viewModel.playWidgetTools = playWidgetTools
        val playWidget: PlayWidget = mockk(relaxed = true)
        coEvery { playWidgetTools.getWidgetFromNetwork(any()) } returns playWidget
        val playWidgetState: PlayWidgetState = mockk(relaxed = true)
        coEvery { playWidgetTools.mapWidgetToModel(any()) } returns playWidgetState

        viewModel.getPlayWidgetData()

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value, playWidgetState)

    }

    /**************************** test for updatePlayWidgetReminder() *******************************************/
    @Test
    fun `test for updatePlayWidgetReminder`() {
        val list = ArrayList<DataItem>()
        val item = DataItem(playWidgetPlayID = "2")
        list.add(item)
        every { componentsItem.data } returns list
        val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
        viewModel.playWidgetTools = playWidgetTools
        val playWidget: PlayWidget = mockk(relaxed = true)
        coEvery { playWidgetTools.getWidgetFromNetwork(any()) } returns playWidget
        val playWidgetState: PlayWidgetState = mockk(relaxed = true)
        coEvery { playWidgetTools.mapWidgetToModel(any()) } returns playWidgetState

        viewModel.getPlayWidgetData()

        viewModel.updatePlayWidgetReminder("4", true)

        TestCase.assertEquals(viewModel.getPlayWidgetUILiveData().value != null, true)

    }

    @Test
    fun `test for shouldUpdatePlayWidgetToggleReminder when user not logged in`() {
        val channelId = "abc"
        val playWidgetReminder: PlayWidgetReminderType = mockk()
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false
        viewModel.shouldUpdatePlayWidgetToggleReminder(channelId, playWidgetReminder)
        assert(viewModel.reminderLoginEvent.value == true)
    }

    @Test
    fun `test for shouldUpdatePlayWidgetToggleReminder when user is logged in and updateToggleReminder throws Error`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            ).apply { playWidgetTools = playWidgetTool }
        val channelId = "abc"
        val playWidgetReminder: PlayWidgetReminderType = PlayWidgetReminderType.Reminded
        val mockPlayWidgetStateReminded: PlayWidgetState = mockk()
        val mockPlayWidgetStateNotReminded: PlayWidgetState = mockk()
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.Reminded
            )
        } returns mockPlayWidgetStateReminded
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.NotReminded
            )
        } returns mockPlayWidgetStateNotReminded
        coEvery { playWidgetTool.updateToggleReminder(any(), any(), any()) } throws Error()
        viewModel.shouldUpdatePlayWidgetToggleReminder(channelId, playWidgetReminder)
        assert(viewModel.getPlayWidgetUILiveData().value === mockPlayWidgetStateNotReminded)
        assert(viewModel.reminderObservable.value is Fail)
    }

    @Test
    fun `test for shouldUpdatePlayWidgetToggleReminder when user is logged in and updateToggleReminder is Success`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            ).apply { playWidgetTools = playWidgetTool }
        val channelId = "abc"
        val playWidgetReminderType: PlayWidgetReminderType = PlayWidgetReminderType.Reminded
        val playWidgetReminder: PlayWidgetReminder = mockk()
        val mockPlayWidgetStateReminded: PlayWidgetState = mockk()
        val mockPlayWidgetStateNotReminded: PlayWidgetState = mockk()
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.Reminded
            )
        } returns mockPlayWidgetStateReminded
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.NotReminded
            )
        } returns mockPlayWidgetStateNotReminded
        coEvery {
            playWidgetTool.updateToggleReminder(
                any(),
                any(),
                any()
            )
        } returns playWidgetReminder
        coEvery { playWidgetTool.mapWidgetToggleReminder(playWidgetReminder, any()) } returns true
        viewModel.shouldUpdatePlayWidgetToggleReminder(channelId, playWidgetReminderType)
        assert(viewModel.getPlayWidgetUILiveData().value === mockPlayWidgetStateReminded)
        assert(viewModel.reminderObservable.value is Success)
    }

    @Test
    fun `test for shouldUpdatePlayWidgetToggleReminder when user is logged in and updateToggleReminder is Fail`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            ).apply { playWidgetTools = playWidgetTool }
        val channelId = "abc"
        val playWidgetReminderType: PlayWidgetReminderType = PlayWidgetReminderType.Reminded
        val playWidgetReminder: PlayWidgetReminder = mockk()
        val mockPlayWidgetStateReminded: PlayWidgetState = mockk()
        val mockPlayWidgetStateNotReminded: PlayWidgetState = mockk()
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.Reminded
            )
        } returns mockPlayWidgetStateReminded
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.NotReminded
            )
        } returns mockPlayWidgetStateNotReminded
        coEvery {
            playWidgetTool.updateToggleReminder(
                any(),
                any(),
                any()
            )
        } returns playWidgetReminder
        coEvery { playWidgetTool.mapWidgetToggleReminder(playWidgetReminder, any()) } returns false
        viewModel.shouldUpdatePlayWidgetToggleReminder(channelId, playWidgetReminderType)
        assert(viewModel.getPlayWidgetUILiveData().value === mockPlayWidgetStateNotReminded)
        assert(viewModel.reminderObservable.value is Fail)
    }

    @Test
    fun `test for isPlayWidgetToolsInitialized with non initialized`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            )
        assert(!viewModel.isPlayWidgetToolsInitialized())
    }

    @Test
    fun `test for isPlayWidgetToolsInitialized with initialized`() {
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            )
        viewModel.playWidgetTools = playWidgetTool
        assert(viewModel.isPlayWidgetToolsInitialized())
    }


    @Test
    fun `test for loggedInCallback when user we don't have any reminder available`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            )
        val playWidgetTool: PlayWidgetTools = mockk()
        viewModel.playWidgetTools = playWidgetTool
        viewModel.loggedInCallback()
        verify(inverse = true) { playWidgetTool.updateActionReminder(any(), any(), any()) }
    }

    @Test
    fun `test for loggedInCallback when user we have any reminder data available`() {
        val channelId = "abc"
        val viewModel: DiscoveryPlayWidgetViewModel =
            spyk(
                DiscoveryPlayWidgetViewModel(
                    application,
                    componentsItem,
                    99
                )
            )
        val playWidgetTool: PlayWidgetTools = mockk()
        viewModel.playWidgetTools = playWidgetTool
        val playWidgetReminderType: PlayWidgetReminderType = PlayWidgetReminderType.Reminded
        val playWidgetReminder: PlayWidgetReminder = mockk()
        val mockPlayWidgetStateReminded: PlayWidgetState = mockk()
        val mockPlayWidgetStateNotReminded: PlayWidgetState = mockk()
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false
        viewModel.shouldUpdatePlayWidgetToggleReminder(channelId, playWidgetReminderType)
        assert(viewModel.reminderLoginEvent.value == true)


        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.Reminded
            )
        } returns mockPlayWidgetStateReminded
        every {
            playWidgetTool.updateActionReminder(
                any(),
                channelId,
                PlayWidgetReminderType.NotReminded
            )
        } returns mockPlayWidgetStateNotReminded
        coEvery {
            playWidgetTool.updateToggleReminder(
                any(),
                any(),
                any()
            )
        } returns playWidgetReminder
        coEvery { playWidgetTool.mapWidgetToggleReminder(playWidgetReminder, any()) } returns true

//        Now that we have data available and get loggedin user back through callback
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        viewModel.loggedInCallback()
        verify { playWidgetTool.updateActionReminder(any(), any(), any()) }
        assert(viewModel.reminderObservable.value is Success)
    }


}
