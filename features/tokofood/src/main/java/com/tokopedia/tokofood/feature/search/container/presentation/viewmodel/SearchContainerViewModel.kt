package com.tokopedia.tokofood.feature.search.container.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchContainerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _keywordResult = MutableSharedFlow<String>(replay = Int.ONE)

    val keywordResult: SharedFlow<String> = _keywordResult

    private val keywordFlow = MutableSharedFlow<String>(Int.ONE)

    init {
        viewModelScope.launch {
            keywordFlow
                .debounce(KEYWORD_TYPING_DEBOUNCE)
                .distinctUntilChanged()
                .collectLatest {
                    _keywordResult.emit(it)
                }
        }
    }

    fun setKeyword(keyword: String) {
        keywordFlow.tryEmit(keyword)
    }

    companion object {
        const val KEYWORD_TYPING_DEBOUNCE = 300L
    }
}