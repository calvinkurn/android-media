package com.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedplus.view.repository.FeedDetailRepository
import com.tokopedia.feedplus.view.subscriber.FeedDetailViewState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @author by astidhiyaa on 27/09/22
 */
@ExperimentalCoroutinesApi
class FeedDetailViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private lateinit var viewModel: FeedDetailViewModel

    private val mockRepo: FeedDetailRepository = mockk(relaxed = true)

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel =
            FeedDetailViewModel(feedDetailRepository = mockRepo, userSession = mockUserSession)
    }

    @Test
    fun `get detail page 1`() {
        //given
        val expected = FeedDetailViewState.LoadingState(isLoading = true, loadingMore = false)

        //when
        viewModel.getFeedDetail("", 1, "", "")

        //then
//        viewModel.getFeedDetailLiveData().getOrAwaitValue().assertType<FeedDetailViewState.LoadingState> {  }
    }


}
