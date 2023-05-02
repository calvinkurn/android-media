package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

/**
 * @author by astidhiyaa on 24/09/22
 */
@ExperimentalCoroutinesApi
class FeedPlusContainerTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private lateinit var viewModel: FeedPlusContainerViewModel

    private val mockRepo: FeedPlusRepository = mockk(relaxed = true)
    private val mockUserSession : UserSessionInterface = mockk(relaxed = true)
    private val mockPlayShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = FeedPlusContainerViewModel(
            testDispatcher,
            mockRepo,
            mockUserSession,
            mockPlayShortsEntryPointRemoteConfig
        )
    }
}
