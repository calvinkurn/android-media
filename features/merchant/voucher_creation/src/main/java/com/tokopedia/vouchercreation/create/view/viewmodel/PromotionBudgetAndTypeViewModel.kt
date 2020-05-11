package com.tokopedia.vouchercreation.create.view.viewmodel

import android.graphics.Bitmap
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

    companion object {
        private const val BANNER_BASE_URL = "https://ecs7.tokopedia.net/img/merchant-coupon/banner/v3/base_image/banner.jpg"

    }

    private val mBannerBitmapLiveData = MutableLiveData<Bitmap>()
    val bannerBitmapLiveData : LiveData<Bitmap>
        get() = mBannerBitmapLiveData

    private val mBannerVoucherLiveData = MutableLiveData<BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>>()
    val bannerVoucherLiveData : LiveData<BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>>
        get() = mBannerVoucherLiveData

    fun setBannerVoucher(uiModel: BannerVoucherUiModel<PromotionTypeBudgetTypeFactory>) {
        mBannerVoucherLiveData.value = uiModel
    }

}