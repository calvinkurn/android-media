package com.tokopedia.shop.campaign.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import javax.inject.Inject

class ExclusiveLaunchVoucherListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
    private val getPromoVoucherListUseCase: GetPromoVoucherListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _vouchers = MutableLiveData<Result<List<ExclusiveLaunchVoucher>>>()
    val vouchers: LiveData<Result<List<ExclusiveLaunchVoucher>>>
        get() = _vouchers

    fun getExclusiveLaunchVouchers(promoVouchersCategorySlugs: List<String>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val merchantVouchersDeferred = async { getMerchantVoucherListUseCase.execute() }

                val promoVoucherParam = GetPromoVoucherListUseCase.Param(categorySlug = "", categorySlugs = promoVouchersCategorySlugs)
                val promoVouchersDeferred = async { getPromoVoucherListUseCase.execute(promoVoucherParam) }

                val merchantVouchers = merchantVouchersDeferred.await()
                val promoVouchers = promoVouchersDeferred.await()

                val availableMerchantVouchers = merchantVouchers.filter { it.remainingQuota > Int.ZERO && it.isMerchantLockedToProductVoucher() }
                val exclusiveLaunchVouchers = availableMerchantVouchers + promoVouchers

                _vouchers.postValue(Success(exclusiveLaunchVouchers))
            },
            onError = { throwable ->
                _vouchers.postValue(Fail(throwable))
            }
        )
    }

    fun updateVoucherAsClaimed(
        allVouchers: List<ExclusiveLaunchVoucher>,
        selectedVoucher: ExclusiveLaunchVoucher
    ): List<ExclusiveLaunchVoucher> {
        return allVouchers.map { voucher ->
            if (voucher.id == selectedVoucher.id) {
                selectedVoucher.claim()
            } else {
                voucher
            }
        }
    }

    private fun ExclusiveLaunchVoucher.claim(): ExclusiveLaunchVoucher {
        return if (this.source is ExclusiveLaunchVoucher.VoucherSource.Promo) {
            val newVoucherStatus = this.source.copy(isClaimed = true)
            this.copy(remainingQuota = Int.ZERO, source = newVoucherStatus)
        } else {
            this
        }
    }

    private fun ExclusiveLaunchVoucher.isMerchantLockedToProductVoucher(): Boolean {
        return if (this.source is ExclusiveLaunchVoucher.VoucherSource.MerchantCreated) {
            this.source.eligibleProductIds.isNotEmpty()
        } else {
            false
        }
    }
}


