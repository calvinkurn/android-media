package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiEvent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository,
    private val uiEventManager: UiEventManager<FeedBrowseUiEvent>
): ViewModel() {

    private val _uiState = MutableStateFlow<FeedBrowseUiState?>(null)
    val uiState get() = _uiState.asStateFlow()

    val uiEvent: Flow<FeedBrowseUiEvent?>
        get() = uiEventManager.event

    init {
        viewModelScope.launch {
            val title = repository.getTitle()
            val widgets = repository.getSlots()
            _uiState.value = FeedBrowseUiState(
                title = title,
                widgets = widgets
            )

            val widget = repository.getCards("type:promotion")
            val prevValue = _uiState.value
            _uiState.value = prevValue?.copy(
                widgets = List(prevValue.widgets.size) {
                    widget
                }
            )
        }
    }
}
