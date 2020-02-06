package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.DigitalHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DigitalHomePageViewModel  @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val digitalHomePageUseCase: DigitalHomePageUseCase)
    : BaseViewModel(dispatcher) {

    private val _digitalHomePageList = MutableLiveData<List<DigitalHomePageItemModel>>()
    val digitalHomePageList: LiveData<List<DigitalHomePageItemModel>>
        get() = _digitalHomePageList
    private val _isAllError = MutableLiveData<Boolean>()
    val isAllError: LiveData<Boolean>
        get() = _isAllError

    fun getInitialList() {
        val list: List<DigitalHomePageItemModel> = digitalHomePageUseCase.getEmptyList()
        _digitalHomePageList.value = list
        _isAllError.value = false
    }

    fun getData(queryList: Map<String, String>, isLoadFromCloud: Boolean) {
        digitalHomePageUseCase.isFromCloud = isLoadFromCloud
        if (digitalHomePageUseCase.setQueries(queryList)) {
            launch(Dispatchers.IO) {
                val data = digitalHomePageUseCase.executeOnBackground()
                if (data.isNotEmpty()) {
                    _digitalHomePageList.postValue(data)
                } else {
                    _isAllError.value = true
                }
            }
        } else {
            _isAllError.value = true
        }
    }

    companion object {
        const val CATEGORY_ORDER = DigitalHomePageUseCase.CATEGORY_ORDER
    }
}
