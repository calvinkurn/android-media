package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeBudgetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class PromotionBudgetAndTypeViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher){

    private val mBannerVoucherLiveData = MutableLiveData<BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>>()
    val bannerVoucherLiveData : LiveData<BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>>
        get() = mBannerVoucherLiveData

    fun setBannerVoucher(uiModel: BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>) {
        mBannerVoucherLiveData.value = uiModel
    }

}