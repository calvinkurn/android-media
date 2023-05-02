package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import javax.inject.Inject

class DisplayVoucherViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _selectedDisplayVoucherType = MutableLiveData<ImageRatio>()
    val selectedDisplayVoucherType: LiveData<ImageRatio>
        get() = _selectedDisplayVoucherType

    private val _couponImage = MutableLiveData<Bitmap>()
    val couponImage: LiveData<Bitmap>
        get() = _couponImage

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun setSelectedVoucherChip(couponType: ImageRatio) {
        _selectedDisplayVoucherType.postValue(couponType)
    }

    fun previewImage(
        voucherConfiguration: VoucherConfiguration,
        parentProductIds: List<Long>,
        imageRatio: ImageRatio
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getCouponImagePreviewUseCase.execute(
                    checkIsAdding(voucherConfiguration),
                    voucherConfiguration,
                    parentProductIds,
                    imageRatio
                )
                _couponImage.postValue(BitmapFactory.decodeByteArray(result, Int.ZERO, result.size))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun checkIsAdding(configuration: VoucherConfiguration): Boolean {
        return configuration.voucherId.isZero()
    }
}
