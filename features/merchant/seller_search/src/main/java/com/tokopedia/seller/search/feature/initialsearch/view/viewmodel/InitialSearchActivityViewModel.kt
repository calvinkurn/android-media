package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.search.common.domain.GetSellerSearchPlaceholderUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InitialSearchActivityViewModel @Inject constructor(
        private val getPlaceholderUseCase: GetSellerSearchPlaceholderUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val searchPlaceholder: LiveData<Result<String>>
        get() = _searchPlaceholder

    private val _searchPlaceholder = MutableLiveData<Result<String>>()

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String>
        get() = _searchKeyword


    private val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    init {
        getSearchKeyword()
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

    private fun getSearchKeyword() {
        viewModelScope.launch {
            queryChannel.asFlow()
                    .debounce(DEBOUNCE_DELAY_MILLIS)
                    .distinctUntilChanged()
                    .collectLatest {
                        _searchKeyword.value = it
                    }
        }
    }

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }
}