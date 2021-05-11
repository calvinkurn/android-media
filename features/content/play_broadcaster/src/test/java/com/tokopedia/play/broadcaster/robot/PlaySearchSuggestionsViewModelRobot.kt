package com.tokopedia.play.broadcaster.robot

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlaySearchSuggestionsViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest

/**
 * Created by jegul on 11/05/21
 */
class PlaySearchSuggestionsViewModelRobot(
        private val dispatcher: CoroutineTestDispatchers,
        getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        userSession: UserSessionInterface,
        playBroadcastMapper: PlayBroadcastMapper,
) : Robot {

    private val viewModel: PlaySearchSuggestionsViewModel = PlaySearchSuggestionsViewModel(
            dispatcher = dispatcher,
            getProductsInEtalaseUseCase = getProductsInEtalaseUseCase,
            userSession = userSession,
            playBroadcastMapper = playBroadcastMapper
    )

    fun getSuggestionList(): NetworkResult<List<SearchSuggestionUiModel>> = viewModel.observableSuggestionList.getOrAwaitValue()

    fun loadSuggestions(keyword: String) = dispatcher.coroutineDispatcher.runBlockingTest {
        viewModel.loadSuggestionsFromKeyword(keyword)
        advanceUntilIdle()
    }
}

fun givenPlaySearchSuggestionsViewModel(
        dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
        getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true),
        userSession: UserSessionInterface = mockk(relaxed = true),
        playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer()),
        fn: PlaySearchSuggestionsViewModelRobot.() -> Unit = {},
) : PlaySearchSuggestionsViewModelRobot {
    return PlaySearchSuggestionsViewModelRobot(
            dispatcher = dispatcher,
            getProductsInEtalaseUseCase = getProductsInEtalaseUseCase,
            userSession = userSession,
            playBroadcastMapper = playBroadcastMapper
    ).apply(fn)
}