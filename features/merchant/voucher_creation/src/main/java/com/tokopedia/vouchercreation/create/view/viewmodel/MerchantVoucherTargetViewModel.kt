package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.create.view.customview.VoucherTargetCardItemView
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class MerchantVoucherTargetViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private const val PROMO_CODE_MIN_LENGTH = 5
        private const val PROMO_CODE_MAX_LENGTH = 10
    }

    private val mVoucherTargetListData = MutableLiveData<List<VoucherTargetItemUiModel>>()
    val voucherTargetListData : LiveData<List<VoucherTargetItemUiModel>>
        get() = mVoucherTargetListData

    fun setVoucherTargetListData(list: List<VoucherTargetItemUiModel>) {
        mVoucherTargetListData.value = list
    }

    fun validatePromoCode(promoCode: String) {
        getPromoCodeValidation(promoCode)
    }

    private fun getPromoCodeValidation(promoCode: String) {
        if (promoCode.length in PROMO_CODE_MIN_LENGTH..PROMO_CODE_MAX_LENGTH) {
            mVoucherTargetListData.value = listOf(
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardItemView.TARGET_PUBLIC_TYPE,
                            isEnabled = false,
                            isHavePromoCard = false),
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardItemView.TARGET_SPECIAL_TYPE,
                            isEnabled = true,
                            isHavePromoCard = true,
                            promoCode = promoCode)
            )
        }
    }

}