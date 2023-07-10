package com.tokopedia.universal_sharing.view.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UniversalShareViewModel : ViewModel() {

    private var _state = MutableStateFlow(UniversalShareState.default())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UniversalShareAction>(
        replay = 50
    )

    init {
        viewModelScope.launch {
            _event
                .distinctUntilChanged()
                .collect { event ->
                    when (event) {
                        is RenderChips -> setChipList(event.data)
                        is ChipSelected -> setChipSelected(event.id)
                    }
                }
        }
    }

    fun setAction(event: UniversalShareAction) {
        _event.tryEmit(event)
    }

    private fun setChipList(data: List<Map<Int, String>>) {
        _state.update {
            it.copy(
                state = UiState.CHIP_LIST,
                chipList = data
            )
        }
    }

    private fun setChipSelected(id: Int) {
        _state.update {
            it.copy(
                state = UiState.CHIP_SELECTED,
                chipSelectedId = id
            )
        }
    }

}
