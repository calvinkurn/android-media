package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.NonNullLiveData
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetUiModel
import com.tokopedia.vouchercreation.shop.create.domain.usecase.validation.VoucherTargetValidationUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.VoucherTargetValidation
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateCouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherTargetValidationUseCase: VoucherTargetValidationUseCase
) : BaseViewModel(dispatchers.main) {

    private val mCouponTargetList = MutableLiveData<List<CouponTargetUiModel>>()
    val couponTargetList: LiveData<List<CouponTargetUiModel>>
        get() = mCouponTargetList

    private val mSelectedCouponTarget = NonNullLiveData(CouponTargetEnum.PUBLIC)
    val selectedCouponTarget: LiveData<CouponTargetEnum>
        get() = mSelectedCouponTarget

    private val mCouponValidationResult = MutableLiveData<Result<VoucherTargetValidation>>()
    val couponValidationResult: LiveData<Result<VoucherTargetValidation>>
        get() = mCouponValidationResult

    fun populateCouponTarget() {
        mCouponTargetList.value = listOf(
            CouponTargetUiModel(
                R.drawable.ic_im_umum,
                R.string.mvc_create_target_public,
                R.string.mvc_create_coupon_target_public_desc,
                CouponTargetEnum.PUBLIC,
                selected = false,
            ),
            CouponTargetUiModel(
                R.drawable.ic_im_terbatas,
                R.string.mvc_create_target_private,
                R.string.mvc_create_coupon_target_private_desc,
                CouponTargetEnum.PRIVATE,
                selected = false,
            ),
        )
    }

    fun setSelectedCouponTarget(couponTarget: CouponTargetEnum) {
        mSelectedCouponTarget.value = couponTarget
    }

    fun validateCouponTarget(promoCode: String, couponName: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                voucherTargetValidationUseCase.params = VoucherTargetValidationUseCase
                    .createRequestParam(mSelectedCouponTarget.value.value, promoCode, couponName)
                voucherTargetValidationUseCase.executeOnBackground()
            }
            mCouponValidationResult.value = Success(result)
        }, onError = {
            mCouponValidationResult.value = Fail(it)
        })
    }
}