package com.tokopedia.mvc.presentation.quota.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class QuotaInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherQuotaUseCase: GetVoucherQuotaUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val SHORT_QUOTA_LIST_NUMBER = 3
    }

    private val _quotaInfo = MutableLiveData<VoucherCreationQuota>()
    val quotaInfo: LiveData<VoucherCreationQuota> get() = _quotaInfo

    private val _sourceList = MutableLiveData<List<VoucherCreationQuota.Sources>>()
    val sourceList: LiveData<List<VoucherCreationQuota.Sources>> get() = _sourceList

    private val _sourceListExpanded = MutableLiveData(false)
    val sourceListExpanded: LiveData<Boolean> get() = _sourceListExpanded

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    val enableExpand = Transformations.map(quotaInfo) {
        it.sources.size > SHORT_QUOTA_LIST_NUMBER
    }

    fun setQuotaInfo(voucherCreationQuota: VoucherCreationQuota) {
        _quotaInfo.value = voucherCreationQuota
    }

    fun displayShortQuotaList() {
        _quotaInfo.value?.apply {
            _sourceList.value = sources.take(SHORT_QUOTA_LIST_NUMBER)
        }
    }

    fun displayFullQuotaList() {
        _quotaInfo.value?.apply {
            _sourceList.value = sources
        }
    }

    fun toggleSourceListExpanded(isExpanded: Boolean) {
        _sourceListExpanded.value = !isExpanded
        if (isExpanded) {
            displayShortQuotaList()
        } else {
            displayFullQuotaList()
        }
    }

    fun getVoucherQuota() {
        launchCatchError(
            dispatchers.io,
            block = {
                _quotaInfo.postValue(getVoucherQuotaUseCase.execute())
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
