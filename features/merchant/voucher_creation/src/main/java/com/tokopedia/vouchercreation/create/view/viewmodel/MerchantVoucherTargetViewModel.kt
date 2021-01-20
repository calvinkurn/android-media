package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.NonNullLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.vouchercreation.create.data.source.VoucherTargetStaticDataSource
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.domain.usecase.validation.VoucherTargetValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.uimodel.validation.VoucherTargetValidation
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.VoucherTargetItemUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MerchantVoucherTargetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherTargetValidationUseCase: VoucherTargetValidationUseCase
) : BaseViewModel(dispatchers.main) {

        private val mVoucherTargetListData = MutableLiveData<List<VoucherTargetItemUiModel>>()
        val voucherTargetListData : LiveData<List<VoucherTargetItemUiModel>>
            get() = mVoucherTargetListData

        private val mPrivateVoucherPromoCode = MutableLiveData<String>()
        val privateVoucherPromoCode : LiveData<String>
            get() = mPrivateVoucherPromoCode

        private val mShouldReturnToInitialValue = MutableLiveData<Boolean>()
        val shouldReturnToInitialValue : LiveData<Boolean>
            get() = mShouldReturnToInitialValue

        private val mVoucherTargetValidationLiveData = MutableLiveData<Result<VoucherTargetValidation>>()
        val voucherTargetValidationLiveData : LiveData<Result<VoucherTargetValidation>>
            get() = mVoucherTargetValidationLiveData

        private val mVoucherTargetTypeLiveData = NonNullLiveData(VoucherTargetType.PUBLIC)
        val voucherTargetTypeLiveData : LiveData<Int>
            get() = mVoucherTargetTypeLiveData

        fun setDefaultVoucherTargetListData() {
            mVoucherTargetListData.value = VoucherTargetStaticDataSource.getVoucherTargetItemUiModelList()
        }

        fun setPromoCode(promoCode: String, promoCodePrefix: String) {
            mShouldReturnToInitialValue.value = false
            mVoucherTargetListData.value = listOf(
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardType.PUBLIC,
                            isEnabled = false,
                            isHavePromoCard = false),
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardType.PRIVATE,
                            isEnabled = true,
                            isHavePromoCard = true,
                            promoCode = promoCodePrefix + promoCode)
            )
            mPrivateVoucherPromoCode.value = promoCode
        }

        fun setActiveVoucherTargetType(@VoucherTargetType targetType: Int) {
            mVoucherTargetTypeLiveData.value = targetType
        }

        fun setReloadVoucherTargetData(@VoucherTargetType targetType: Int,
                                       promoCode: String,
                                       promoCodePrefix: String) {
            mPrivateVoucherPromoCode.value = promoCode
            mVoucherTargetTypeLiveData.value = targetType
            mShouldReturnToInitialValue.value = targetType == VoucherTargetType.PRIVATE
            mVoucherTargetListData.value = listOf(
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardType.PUBLIC,
                            isEnabled = targetType == VoucherTargetType.PUBLIC,
                            isHavePromoCard = false),
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardType.PRIVATE,
                            isEnabled = targetType == VoucherTargetType.PRIVATE,
                            isHavePromoCard = targetType == VoucherTargetType.PRIVATE && promoCode.isNotEmpty(),
                            promoCode = promoCodePrefix + promoCode)
            )
        }

        fun validateVoucherTarget(promoCode: String,
                                  couponName: String) {
            launchCatchError(
                    block = {
                        withContext(dispatchers.io) {
                            val code =
                                    if (mVoucherTargetTypeLiveData.value == VoucherTargetType.PUBLIC) {
                                        ""
                                    } else {
                                        promoCode
                                    }
                            voucherTargetValidationUseCase.params = VoucherTargetValidationUseCase.createRequestParam(mVoucherTargetTypeLiveData.value, code, couponName)
                            mVoucherTargetValidationLiveData.postValue(Success(voucherTargetValidationUseCase.executeOnBackground()))
                        }
                    },
                    onError = {
                        mVoucherTargetValidationLiveData.value = Fail(it)
                    }
            )
        }

}