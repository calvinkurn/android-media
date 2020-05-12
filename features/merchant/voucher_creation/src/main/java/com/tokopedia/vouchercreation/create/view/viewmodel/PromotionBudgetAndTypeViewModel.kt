package com.tokopedia.vouchercreation.create.view.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.BannerVoucherUiModel
import com.tokopedia.vouchercreation.create.view.util.VoucherPreviewPainter
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

    private val mBannerVoucherLiveData = MutableLiveData<BannerVoucherUiModel>()
    val bannerVoucherLiveData : LiveData<BannerVoucherUiModel>
        get() = mBannerVoucherLiveData

    fun setBannerVoucher(uiModel: BannerVoucherUiModel) {
        mBannerVoucherLiveData.value = uiModel
    }

    fun drawBanner(painter: VoucherPreviewPainter,
                   uiModel: BannerVoucherUiModel,
                   resource: Bitmap) {
        launchCatchError(block = {
            painter.drawFull(uiModel)
        }, onError = {

        })
    }


}