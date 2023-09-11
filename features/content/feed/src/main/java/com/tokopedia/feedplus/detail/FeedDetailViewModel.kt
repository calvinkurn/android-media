package com.tokopedia.feedplus.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.detail.data.FeedDetailRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 07/09/23.
 */
class FeedDetailViewModel @Inject constructor(
    private val repository: FeedDetailRepository
) : ViewModel() {

    val titleLiveData: LiveData<String>
        get() = _titleLiveData
    private val _titleLiveData = MutableLiveData<String>()

    fun getTitle(source: String) {
        viewModelScope.launch {
            val response = repository.getTitle(source)
            _titleLiveData.value = response
        }
    }
}
