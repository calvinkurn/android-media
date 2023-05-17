package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.campaign.domain.entity.PromoVoucherDetail
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherDetailUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class PromoVoucherDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getPromoVoucherDetailUseCase: GetPromoVoucherDetailUseCase,
    private val redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase
) : BaseViewModel(dispatchers.main) {

    private val _voucherDetail = MutableLiveData<Result<PromoVoucherDetail>>()
    val voucherDetail: LiveData<Result<PromoVoucherDetail>>
        get() = _voucherDetail

    private val _redeemResult = MutableLiveData<Result<RedeemPromoVoucherResult>>()
    val redeemResult: LiveData<Result<RedeemPromoVoucherResult>>
        get() = _redeemResult

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

    fun redeemVoucher(catalogId : Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherDetail = redeemPromoVoucherUseCase.execute(catalogId)
                _redeemResult.postValue(Success(voucherDetail))
            },
            onError = { throwable ->
                _redeemResult.postValue(Fail(throwable))
            }
        )
    }

}


