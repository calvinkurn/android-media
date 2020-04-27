package com.tokopedia.play.ui.toolbar

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.PartnerInfoUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by jegul on 29/01/20
 */
class ToolbarComponentTest {

    private lateinit var component: ToolbarComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = ToolbarComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when video property changed`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Buffering
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verify { component.uiView.setLiveBadgeVisibility(mockVideoProp.type.isLive) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test set channel title`() = runBlockingTest(testDispatcher) {
        val channelTitle = "Channel Title"
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetChannelTitle(channelTitle))
        verify { component.uiView.setTitle(channelTitle) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test set partner info`() = runBlockingTest(testDispatcher) {
        val mockPartnerInfo = PartnerInfoUiModel(
                id = 1,
                name = "Toko",
                type = PartnerType.SHOP,
                isFollowed = true,
                isFollowable = true
        )
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPartnerInfo(mockPartnerInfo))
        verify { component.uiView.setPartnerInfo(mockPartnerInfo) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test on video stream changed`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = VideoStreamUiModel(
                uriString = "https://www.google.com/video.mp4",
                channelType = PlayChannelType.VOD,
                isActive = false
        )
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream))
        verify { component.uiView.setLiveBadgeVisibility(mockVideoStream.channelType.isLive) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when keyboard is shown`() = runBlockingTest(testDispatcher) {
        val keyboardState = true
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(keyboardState))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when keyboard is hidden`() = runBlockingTest(testDispatcher) {
        val keyboardState = false
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(keyboardState))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when channel freeze`() = runBlockingTest(testDispatcher) {
        val mockEvent = PlayRoomEvent.Freeze("", "", "", "")
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockEvent))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when no more action`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNoMoreAction)
        verify { component.uiView.hideActionMore() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test follow and unfollow partner`() = runBlockingTest(testDispatcher) {
        val shouldFollowPartner = true
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.FollowPartner(shouldFollowPartner))
        verify { component.uiView.setFollowStatus(shouldFollowPartner) }
        confirmVerified(component.uiView)
    }

    class ToolbarComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : ToolbarComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): ToolbarView {
            return mockk(relaxed = true)
        }
    }
}