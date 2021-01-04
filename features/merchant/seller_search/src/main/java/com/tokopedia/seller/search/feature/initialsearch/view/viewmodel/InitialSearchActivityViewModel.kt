package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
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

    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
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
            }
            .asLiveData()

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

    fun loadTypingSearch(keyword: String) {
        queryChannel.offer(keyword)
    }

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }
}