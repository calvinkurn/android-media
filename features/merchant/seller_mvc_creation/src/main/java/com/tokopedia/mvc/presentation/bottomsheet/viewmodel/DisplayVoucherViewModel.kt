package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
        isCreateMode: Boolean,
        voucherConfiguration: VoucherConfiguration,
        parentProductIds: List<Long>,
        imageRatio: ImageRatio
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getCouponImagePreviewUseCase.execute(
                    isCreateMode,
                    voucherConfiguration,
                    parentProductIds,
                    imageRatio
                )
                _couponImage.postValue(result)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
