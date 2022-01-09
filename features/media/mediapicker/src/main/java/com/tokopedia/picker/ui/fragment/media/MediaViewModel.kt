package com.tokopedia.picker.ui.fragment.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.ui.PickerParam
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private var _files = MutableLiveData<List<Media>>()
    val files: LiveData<List<Media>> get() = _files

    private var _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun fetch(bucketId: Long, param: PickerParam) {
        viewModelScope.launch {
            val result = repository(bucketId, param)

            withContext(dispatcher.main) {
                _files.value = result
            }
        }
    }

}