package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationIntent
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 22/09/23
 */
internal class FeedCategoryInspirationViewModel @Inject constructor(
    private val repository: FeedBrowseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedCategoryInspirationUiState.Default)
    val uiState get() = _uiState.asStateFlow()

    fun onIntent(intent: FeedCategoryInspirationIntent) {
        when (intent) {
            is FeedCategoryInspirationIntent.InitPage -> onLoadContent()
        }
    }

    private fun onLoadContent() {
        viewModelScope.launch {
            val content = repository.getCategoryInspiration()
            _uiState.update {
                it.copy(itemList = content)
            }
        }
    }
}
