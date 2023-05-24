package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.PromoVoucherDetail
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherDetailUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.shop.campaign.domain.usecase.UsePromoVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class PromoVoucherDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getPromoVoucherDetailUseCase: GetPromoVoucherDetailUseCase,
    private val redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase,
    private val usePromoVoucherUseCase: UsePromoVoucherUseCase
) : BaseViewModel(dispatchers.main) {

    private val _voucherDetail = MutableLiveData<Result<PromoVoucherDetail>>()
    val voucherDetail: LiveData<Result<PromoVoucherDetail>>
        get() = _voucherDetail

    private val _redeemResult = MutableLiveData<Result<RedeemPromoVoucherResult>>()
    val redeemResult: LiveData<Result<RedeemPromoVoucherResult>>
        get() = _redeemResult

    private val _useVoucherResult = MutableLiveData<Result<Boolean>>()
    val useVoucherResult: LiveData<Result<Boolean>>
        get() = _useVoucherResult

    fun getVoucherDetail(slug: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherDetail = getPromoVoucherDetailUseCase.execute(slug)
                _voucherDetail.postValue(Success(voucherDetail))
            },
            onError = { throwable ->
                _voucherDetail.postValue(Fail(throwable))
            }
        )
    }

    fun redeemVoucher() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherDetail = _voucherDetail.getVoucherDetailOrNull() ?: return@launchCatchError
                val param = RedeemPromoVoucherUseCase.Param(voucherDetail.id, voucherDetail.isGift)
                val redeemResult = redeemPromoVoucherUseCase.execute(param)
                _redeemResult.postValue(Success(redeemResult))
            },
            onError = { throwable ->
                _redeemResult.postValue(Fail(throwable))
            }
        )
    }

    fun useVoucher(voucherCode: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val redeemedVoucherCode = _redeemResult.voucherCodeOrEmpty()
                val promoVoucherCode = if (redeemedVoucherCode.isEmpty()) {
                    voucherCode
                } else {
                    redeemedVoucherCode
                }
                val useVoucherResult = usePromoVoucherUseCase.execute(promoVoucherCode)
                _useVoucherResult.postValue(Success(useVoucherResult))
            },
            onError = { throwable ->
                _useVoucherResult.postValue(Fail(throwable))
            }
        )
    }

    private fun LiveData<Result<PromoVoucherDetail>>.getVoucherDetailOrNull(): PromoVoucherDetail? {
        val voucherDetail = this.value
        return if (voucherDetail is Success) {
            voucherDetail.data
        } else {
            null
        }
    }

    private fun LiveData<Result<RedeemPromoVoucherResult>>.voucherCodeOrEmpty(): String {
        val redeemResult = this.value
        return if (redeemResult is Success) {
           redeemResult.data.voucherCode
        } else {
            ""
        }
    }
}


