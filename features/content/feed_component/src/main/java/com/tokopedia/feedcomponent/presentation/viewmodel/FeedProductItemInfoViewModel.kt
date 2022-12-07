package com.tokopedia.feedcomponent.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * ViewModel for Product Item Info BottomSheet
 *
 * IMO
 * This "presentation" package later on will be used instead of "view"
 * because the "view" package have many tech debts in structure and naming
 *
 */
class FeedProductItemInfoViewModel @Inject constructor(
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    private val _merchantVoucherSummary =
        MutableLiveData<Result<TokopointsCatalogMVCSummary>>()
    val merchantVoucherSummary: LiveData<Result<TokopointsCatalogMVCSummary>>
        get() = _merchantVoucherSummary

    fun fetchMerchantVoucherSummary(shopId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            val resultStatus = response.data?.resultStatus

            if (resultStatus?.code != "200") {
                throw ResponseErrorException(resultStatus?.message?.get(0).toString())
            }

            response.data?.let {
                _merchantVoucherSummary.postValue(Success(it))
            }
        }) {
            _merchantVoucherSummary.postValue(Fail(it))
        }
    }
}
