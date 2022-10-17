package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistNewUseCase
import com.tokopedia.feedplus.data.pojo.FeedTabs
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
import com.tokopedia.feedplus.domain.usecase.GetContentFormForFeedUseCase
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @Before
    fun setup() {
        viewModel = FeedPlusContainerViewModel(testDispatcher, mockRepo)
    }

//    @Test
//    fun `check dynamic tab` () {
//        val expected = FeedTabs.Response(feedTabs = FeedTabs(feedData = emptyList()))
//        coEvery { mockTab.executeOnBackground() } returns expected
//
//        viewModel.getWhitelist(true)
//
//        val result = viewModel.whitelistResp.getOrAwaitValue().toString()
//
//        println(result)
//
//    }

}
