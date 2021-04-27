package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.domain.PostFollowPartnerUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.PlayLikeModelBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by mzennis on 04/04/20.
 */
class PlayInteractionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockPostLikeUseCase: PostLikeUseCase = mockk(relaxed = true)
    private val mockPostFollowPartnerUseCase: PostFollowPartnerUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val likeModelBuilder = PlayLikeModelBuilder()

    private lateinit var playInteractionViewModel: PlayInteractionViewModel

    @Before
    fun setUp() {
        playInteractionViewModel = PlayInteractionViewModel(
                mockPostLikeUseCase,
                mockPostFollowPartnerUseCase,
                userSession,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when logged in, should be allowed to send chat`() {
        val eventSendChat = InteractionEvent.SendChat

        every { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventSendChat))

        playInteractionViewModel.doInteractionEvent(eventSendChat)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should not be allowed to send chat`() {
        val eventSendChat = InteractionEvent.SendChat

        every { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.NeedLoggedIn(eventSendChat))

        playInteractionViewModel.doInteractionEvent(eventSendChat)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when logged in, should be allowed to like`() {
        val eventLike = InteractionEvent.Like(shouldLike = true)

        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventLike))

        playInteractionViewModel.doInteractionEvent(eventLike)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should not be allowed to like`() {
        val eventLike = InteractionEvent.Like(shouldLike = true)

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.NeedLoggedIn(eventLike))

        playInteractionViewModel.doInteractionEvent(eventLike)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when logged in, should be allowed to follow`() {
        val eventFollow = InteractionEvent.Follow(partnerId = 123L, partnerAction = PartnerFollowAction.Follow)

        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventFollow))

        playInteractionViewModel.doInteractionEvent(eventFollow)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should not be allowed to follow`() {
        val eventFollow = InteractionEvent.Follow(partnerId = 123L, partnerAction = PartnerFollowAction.Follow)

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.NeedLoggedIn(eventFollow))

        playInteractionViewModel.doInteractionEvent(eventFollow)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when logged in, should be allowed to open cart page`() {
        val eventOpenCartPage = InteractionEvent.CartPage

        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventOpenCartPage))

        playInteractionViewModel.doInteractionEvent(eventOpenCartPage)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should not be allowed to open cart page`() {
        val eventOpenCartPage = InteractionEvent.CartPage

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.NeedLoggedIn(eventOpenCartPage))

        playInteractionViewModel.doInteractionEvent(eventOpenCartPage)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    /**
     * Will fix this later
    @Test
    fun `when logged in, should be allowed to click pinned product`() {
        val eventClickPinnedProduct = InteractionEvent.ClickPinnedProduct

        coEvery { userSession.isLoggedIn } returns true

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventClickPinnedProduct))

        playInteractionViewModel.doInteractionEvent(eventClickPinnedProduct)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }

    @Test
    fun `when not logged in, should be allowed to click pinned product`() {
        val eventClickPinnedProduct = InteractionEvent.ClickPinnedProduct

        coEvery { userSession.isLoggedIn } returns false

        val expectedResult = Event(LoginStateEvent.InteractionAllowed(eventClickPinnedProduct))

        playInteractionViewModel.doInteractionEvent(eventClickPinnedProduct)

        Assertions.assertThat(playInteractionViewModel.observableLoggedInInteractionEvent.getOrAwaitValue())
                .isEqualToComparingFieldByFieldRecursively(expectedResult)
    }
     */

    @Test
    fun `test do follow shop`() {
        coEvery { mockPostFollowPartnerUseCase.executeOnBackground() } returns true

        val expectedModel = true
        val expectedResult = Success(expectedModel)

        playInteractionViewModel.doFollow(
                shopId = 123,
                action = PartnerFollowAction.Follow
        )

        Assertions
                .assertThat(playInteractionViewModel.observableFollowPartner.getOrAwaitValue())
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test do follow shop fail`() {
        coEvery { mockPostFollowPartnerUseCase.executeOnBackground() } returns false

        val expectedResult = Success(false)

        playInteractionViewModel.doFollow(
                shopId = 123,
                action = PartnerFollowAction.Follow
        )

        Assertions
                .assertThat(playInteractionViewModel.observableFollowPartner.getOrAwaitValue())
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test do follow shop null`() {
        coEvery { mockPostFollowPartnerUseCase.executeOnBackground() } throws MessageErrorException("error")

        playInteractionViewModel.doFollow(
                shopId = 123,
                action = PartnerFollowAction.Follow
        )

        Assertions
                .assertThat(playInteractionViewModel.observableFollowPartner.getOrAwaitValue())
                .isExactlyInstanceOf(Fail::class.java)
    }

    @Test
    fun `test do like post call like use case`() {
        val likeParamUiModel = likeModelBuilder.buildParam()
        val shouldLike = true

        playInteractionViewModel.doLikeUnlike(
                likeParamUiModel,
                shouldLike = shouldLike
        )

        coVerifySequence {
            mockPostLikeUseCase.params = PostLikeUseCase.createParam(
                    likeParamUiModel.contentId.toIntOrZero(),
                    likeParamUiModel.contentType,
                    likeParamUiModel.likeType,
                    shouldLike
            )
            mockPostLikeUseCase.executeOnBackground()
        }
    }
}