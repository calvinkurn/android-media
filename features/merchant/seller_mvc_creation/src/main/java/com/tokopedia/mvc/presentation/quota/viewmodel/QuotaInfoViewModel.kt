package com.tokopedia.mvc.presentation.quota.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import javax.inject.Inject

class QuotaInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _quotaInfo = MutableLiveData<VoucherCreationQuota>()
    val quotaInfo: LiveData<VoucherCreationQuota> get() = _quotaInfo

    fun setQuotaInfo(voucherCreationQuota: VoucherCreationQuota) {
        _quotaInfo.value = voucherCreationQuota
    }

}
