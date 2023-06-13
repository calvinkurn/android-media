package com.tokopedia.media.picker.ui.activity.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.mapper.toUiModel
import com.tokopedia.media.picker.data.repository.BucketAlbumRepository
import com.tokopedia.picker.common.uimodel.AlbumUiModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlbumViewModel @Inject constructor(
    private val albumRepository: BucketAlbumRepository,
    private val dispatcher: CoroutineDispatchers
) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAlbums() = albumRepository()
        .flowOn(dispatcher.io)
        .onStart { _isLoading.value = true }
        .onCompletion { _isLoading.value = false }
}
