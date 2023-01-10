package com.tokopedia.mvc.presentation.quota.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import javax.inject.Inject

class QuotaInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    fun setQuotaInfo(voucherCreationQuota: VoucherCreationQuota) {
        println(voucherCreationQuota.toString())
    }

}
