package com.tokopedia.vouchercreation.create.view.viewmodel

import android.graphics.Bitmap
import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.create.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CreateMerchantVoucherStepsViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val initiateVoucherUseCase: InitiateVoucherUseCase
) : BaseViewModel(dispatcher) {

    private var maxPosition: Int? = null

    private val mStepPositionLiveData = MutableLiveData<Int>().apply {
        value = 0
    }
    val stepPositionLiveData : LiveData<Int>
        get() = mStepPositionLiveData

    private val mVoucherPreviewBitmapLiveData = MutableLiveData<Bitmap>()
    val voucherPreviewBitmapLiveData : LiveData<Bitmap>
        get() = mVoucherPreviewBitmapLiveData

    private val mInitiateVoucherLiveData = MutableLiveData<Result<InitiateVoucherUiModel>>()
    val initiateVoucherLiveData: LiveData<Result<InitiateVoucherUiModel>>
        get() = mInitiateVoucherLiveData

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

    fun initiateVoucherPage() {
        launchCatchError(
                block = {
                    mInitiateVoucherLiveData.value = Success(withContext(Dispatchers.IO) {
                        initiateVoucherUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mInitiateVoucherLiveData.value = Fail(it)
                }
        )
    }

}