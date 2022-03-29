package com.tokopedia.shopdiscount.bulk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import com.tokopedia.usecase.coroutines.Result
import java.util.*
import javax.inject.Inject

class DiscountBulkApplyViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase
) : BaseViewModel(dispatchers.main) {

    private val _startDate = SingleLiveEvent<Date>()
    val startDate: LiveData<Date>
        get() = _startDate

    private var currentlySelectedStartDate = Date()
    private var currentlySelectedEndDate = Date()

    private val _benefit = MutableLiveData<Result<GetSlashPriceBenefitResponse>>()
    val benefit: LiveData<Result<GetSlashPriceBenefitResponse>>
        get() = _benefit

    fun getSlashPriceBenefit() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceBenefitUseCase.execute()
            }
            _benefit.value = Success(result)
        }, onError = {
            _benefit.value = Fail(it)
        })
    }


}