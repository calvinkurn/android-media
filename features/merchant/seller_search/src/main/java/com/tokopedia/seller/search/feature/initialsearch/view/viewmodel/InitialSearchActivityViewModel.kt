package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.search.common.domain.GetSellerSearchPlaceholderUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class InitialSearchActivityViewModel @Inject constructor(
        private val getPlaceholderUseCase: GetSellerSearchPlaceholderUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val searchPlaceholder: LiveData<Result<String>>
        get() = _searchPlaceholder

    private val _searchPlaceholder = MutableLiveData<Result<String>>()

    private val _searchResult = MutableLiveData<Result<String>>()
    val searchResult: LiveData<Result<String>>
        get() = _searchResult


    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    init {
        getKeywordSearch()
    }

    fun getSearchPlaceholder() {
        launchCatchError(block = {
            val placeholder = withContext(dispatchers.io) {
                getPlaceholderUseCase.executeOnBackground()
                        .response
                        .sentence
            }
            _searchPlaceholder.value = Success(placeholder)
        }) {
            _searchPlaceholder.value = Fail(it)
        }
    }

    fun getTypingSearch(keyword: String) {
        queryChannel.offer(keyword)
    }

    private fun getKeywordSearch() {
        launchCatchError(block =  {
            queryChannel.asFlow()
                    .debounce(DEBOUNCE_DELAY_MILLIS)
                    .distinctUntilChanged()
                    .mapLatest {
                        try {
                            Success(it)
                        } catch (e: Throwable) {
                            Fail(e)
                        }
                    }.catch {
                        emit(Fail(it))
                    }.collectLatest {
                        _searchResult.value = it
                    }
        }) {
            _searchResult.value = Fail(it)
        }
    }

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }
}