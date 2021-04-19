package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class PlaySearchSuggestionsViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        private val userSession: UserSessionInterface,
        private val playBroadcastMapper: PlayBroadcastMapper
): ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableSuggestionList: LiveData<NetworkResult<List<SearchSuggestionUiModel>>>
        get() = _observableSuggestionList
    private val _observableSuggestionList = MutableLiveData<NetworkResult<List<SearchSuggestionUiModel>>>()

    private val searchChannel = BroadcastChannel<String>(Channel.CONFLATED)

    init {
        scope.launch { initSearchChannel() }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun loadSuggestionsFromKeyword(keyword: String) {
        searchChannel.offer(keyword)
    }

    private suspend fun initSearchChannel() = withContext(dispatcher.main) {
        searchChannel.asFlow().debounce(500).collect {
            _observableSuggestionList.value = try {
                val searchSuggestions = getSearchSuggestions(it)
                NetworkResult.Success(searchSuggestions)
            } catch (e: Throwable) {
                NetworkResult.Fail(e, onRetry = { searchChannel.offer(it) })
            }
        }
    }

    private suspend fun getSearchSuggestions(keyword: String) = withContext(dispatcher.io) {
        return@withContext if (keyword.isEmpty()) emptyList() else {
            val suggestionList = getProductsInEtalaseUseCase.apply {
                params = GetProductsInEtalaseUseCase.createParams(
                        shopId = userSession.shopId,
                        page = 1,
                        perPage = SEARCH_SUGGESTIONS_PER_PAGE,
                        keyword = keyword
                )
            }.executeOnBackground()

            playBroadcastMapper.mapSearchSuggestionList(keyword, suggestionList)
        }
    }

    companion object {
        private const val SEARCH_SUGGESTIONS_PER_PAGE = 30
    }
}