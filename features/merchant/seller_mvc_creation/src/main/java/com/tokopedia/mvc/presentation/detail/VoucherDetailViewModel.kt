package com.tokopedia.mvc.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.presentation.bottomsheet.ThreeDotsMenuBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject
import kotlin.math.roundToInt

class VoucherDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DEFAULT_PERCENTAGE_NORMALIZATION = 100
    }

    private var _voucherDetail = MutableLiveData<Result<VoucherDetailData>>()
    val voucherDetail: LiveData<Result<VoucherDetailData>>
        get() = _voucherDetail

    fun getVoucherDetail(voucherId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MerchantPromotionGetMVDataByIDUseCase.Param(voucherId)
                val response = merchantPromotionGetMVDataByIDUseCase.execute(param)
                _voucherDetail.postValue(Success(response))
            },
            onError = { error ->
                _voucherDetail.postValue(Fail(error))
            }
        )
    }

    fun getSpendingEstimation(data: VoucherDetailData): String {
        val voucherDiscount = data.voucherDiscountAmount
        val voucherQuota = data.voucherQuota
        return (voucherDiscount * voucherQuota).getCurrencyFormatted()
    }

    fun getPercentage(value: Long, total: Long): Int {
        return if (total.isZero()) {
            Int.ZERO
        } else {
            ((value.toDouble() / total.toDouble()) * DEFAULT_PERCENTAGE_NORMALIZATION).roundToInt()
        }
    }

    fun getThreeDotsBottomSheetType(data: VoucherDetailData): Int {
        return when (data.voucherStatus) {
            VoucherStatus.NOT_STARTED -> {
                ThreeDotsMenuBottomSheet.TYPE_CANCEL
            }
            VoucherStatus.ONGOING -> {
                ThreeDotsMenuBottomSheet.TYPE_STOP
            }
            else -> {
                ThreeDotsMenuBottomSheet.TYPE_DEFAULT
            }
        }
    }
}
