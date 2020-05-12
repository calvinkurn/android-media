package com.tokopedia.vouchercreation.create.view.viewmodel

import android.graphics.Bitmap
import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class CreateMerchantVoucherStepsViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private var maxPosition: Int? = null

    private val mStepPositionLiveData = MutableLiveData<Int>()
    val stepPositionLiveData : LiveData<Int>
        get() = mStepPositionLiveData

    private val mVoucherPreviewBitmapLiveData = MutableLiveData<Bitmap>()
    val voucherPreviewBitmapLiveData : LiveData<Bitmap>
        get() = mVoucherPreviewBitmapLiveData

    fun setStepPosition(@IntRange(from = 0) stepPosition: Int) {
        val max = maxPosition
        val canSetStepPosition = mStepPositionLiveData.value != stepPosition || max == null
        if (!canSetStepPosition) {
            return
        }
        if (max != null && stepPosition <= max) {
            mStepPositionLiveData.value = stepPosition
        }
    }

    fun setNextStep() {
        mStepPositionLiveData.value?.let { position ->
            val max = maxPosition
            if (max != null) {
                if (position < max) {
                    mStepPositionLiveData.value = position + 1
                }
            } else {
                mStepPositionLiveData.value = position + 1
            }
        }
    }

    fun setBackStep() {
        mStepPositionLiveData.value?.let { position ->
            if (position > 0) {
                mStepPositionLiveData.value = position - 1
            }
        }
    }

    fun setMaxPosition(@IntRange(from = 0) max: Int) {
        maxPosition = max
    }

    fun setVoucherPreviewBitmap(bitmap: Bitmap) {
        mVoucherPreviewBitmapLiveData.value = bitmap
    }

}