package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.domain.PostFollowPartnerUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
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
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private lateinit var playInteractionViewModel: PlayInteractionViewModel

    @Before
    fun setUp() {
        playInteractionViewModel = PlayInteractionViewModel(
                mockPostLikeUseCase,
                mockPostFollowPartnerUseCase,
                userSession,
                dispatchers
        )
    }

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
}