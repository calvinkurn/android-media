package com.tokopedia.media.picker.ui.activity.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.mapper.toUiModel
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.ui.PickerParam
import com.tokopedia.media.picker.ui.uimodel.AlbumUiModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val album: AlbumRepository,
    private val dispatcher: CoroutineDispatchers
) : ViewModel() {

    private var _albums = MutableLiveData<List<AlbumUiModel>>()
    val albums: LiveData<List<AlbumUiModel>> get() = _albums

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetch(param: PickerParam) {
        _isLoading.value = true
        viewModelScope.launch(dispatcher.io) {
            val albums = album(param)

            withContext(dispatcher.main) {
                _isLoading.value = false
                _albums.value = albums.toUiModel()
            }
        }
    }

}