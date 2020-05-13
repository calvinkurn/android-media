package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.create.data.source.VoucherTargetStaticDataSource
import com.tokopedia.vouchercreation.create.domain.usecase.PromoCodeValidationUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.VoucherTargetValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.VoucherTargetItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class MerchantVoucherTargetViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val voucherTargetValidationUseCase: VoucherTargetValidationUseCase
) : BaseViewModel(dispatcher) {

    private val mVoucherTargetListData = MutableLiveData<List<VoucherTargetItemUiModel>>()
    val voucherTargetListData : LiveData<List<VoucherTargetItemUiModel>>
        get() = mVoucherTargetListData

    private val mPrivateVoucherPromoCode = MutableLiveData<String>()
    val privateVoucherPromoCode : LiveData<String>
        get() = mPrivateVoucherPromoCode

    private val mShouldReturnToInitialValue = MutableLiveData<Boolean>()
    val shouldReturnToInitialValue : LiveData<Boolean>
        get() = mShouldReturnToInitialValue

    fun setDefaultVoucherTargetListData() {
        mVoucherTargetListData.value = VoucherTargetStaticDataSource.getVoucherTargetItemUiModelList()
    }

    fun setPromoCode(promoCode: String) {
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
                        promoCode = promoCode)
        )
        mPrivateVoucherPromoCode.value = promoCode
    }

}