package com.tokopedia.feedplus.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.detail.data.FeedDetailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 07/09/23.
 */
class FeedDetailViewModel @Inject constructor(
    private val repository: FeedDetailRepository
) : ViewModel() {

    private val _headerDetail = MutableStateFlow(HeaderDetailModel.DEFAULT)

    val headerDetail = _headerDetail.asStateFlow()

    fun getHeader(
        source: String,
        isShowSearchbar: Boolean
    ) {
        viewModelScope.launch {
            if (isShowSearchbar) {
                _headerDetail.update {
                    it.copy(
                        isShowSearchBar = true,
                    )
                }
            } else {
                val response = repository.getTitle(source)
                _headerDetail.update {
                    it.copy(
                        title = response
                    )
                }
            }
        }
    }
}
