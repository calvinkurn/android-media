package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class PlaySearchSuggestionsViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
        private val userSession: UserSessionInterface
): ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableSuggestionList: LiveData<List<SearchSuggestionUiModel>>
        get() = _observableSuggestionList
    private val _observableSuggestionList = MutableLiveData<List<SearchSuggestionUiModel>>()

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
            val searchSuggestions = getSearchSuggestions(it)
            _observableSuggestionList.value = searchSuggestions
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

            PlayBroadcastUiMapper.mapSearchSuggestionList(keyword, suggestionList)
        }
    }

    companion object {
        private const val SEARCH_SUGGESTIONS_PER_PAGE = 30
    }
}