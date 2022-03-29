package com.tokopedia.shopdiscount.bulk

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import java.util.*
import javax.inject.Inject

class DiscountBulkApplyViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    private val _startDate = SingleLiveEvent<Date>()
    val startDate: LiveData<Date>
        get() = _startDate

    private var currentlySelectedStartDate = Date()
    private var currentlySelectedEndDate = Date()


}