package com.tokopedia.privacycenter.ui.searchhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.DeleteSearchHistoryParam
import com.tokopedia.privacycenter.data.ItemSearch
import com.tokopedia.privacycenter.domain.DeleteSearchHistoryResult
import com.tokopedia.privacycenter.domain.DeleteSearchHistoryUseCase
import com.tokopedia.privacycenter.domain.SearchHistoryUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class SearchHistoryViewModel @Inject constructor(
    private val searchHistoryUseCase: SearchHistoryUseCase,
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _listSearchHistory = MutableLiveData<PrivacyCenterStateResult<List<ItemSearch>>>()
    val listSearchHistory: LiveData<PrivacyCenterStateResult<List<ItemSearch>>> get() = _listSearchHistory

    private val _deleteSearchHistory = SingleLiveEvent<DeleteSearchHistoryResult>()
    val deleteSearchHistory: SingleLiveEvent<DeleteSearchHistoryResult> get() = _deleteSearchHistory

    init {
        getSearchHistory()
    }

    fun getSearchHistory() {
        _listSearchHistory.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            _listSearchHistory.value = searchHistoryUseCase(Unit)
        }, {
            _listSearchHistory.value = PrivacyCenterStateResult.Fail(it)
        })
    }

    fun deleteSearchHistory(position: Int = -1, clearAll: Boolean = false, itemSearch: ItemSearch? = null) {
        val parameter = if (clearAll) {
            DeleteSearchHistoryParam(clearAll = true)
        } else {
            DeleteSearchHistoryParam(
                clearAll = false,
                query = itemSearch?.title.toString(),
                type = itemSearch?.type.toString(),
                id = itemSearch?.id.toString(),
                position = position
            )
        }

        launchCatchError(coroutineContext, {
            _deleteSearchHistory.value = deleteSearchHistoryUseCase(parameter)
        }, {
            _deleteSearchHistory.value = DeleteSearchHistoryResult.Failed(position, clearAll, it)
        })
    }
}
