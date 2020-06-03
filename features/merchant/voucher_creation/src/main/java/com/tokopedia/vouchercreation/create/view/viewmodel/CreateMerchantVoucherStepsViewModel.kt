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
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import com.tokopedia.vouchercreation.create.domain.usecase.BasicShopInfoUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class CreateMerchantVoucherStepsViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val initiateVoucherUseCase: InitiateVoucherUseCase,
        private val basicShopInfoUseCase: BasicShopInfoUseCase,
        private val userSession: UserSessionInterface
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

    private val mBasicShopInfoLiveData = MutableLiveData<Result<ShopInfo>>()
    val basicShopInfoLiveData : LiveData<Result<ShopInfo>>
        get() = mBasicShopInfoLiveData

    fun setStepPosition(@VoucherCreationStep stepPosition: Int) {
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
                        getInitiateVoucher()
                    })
                },
                onError = {
                    mInitiateVoucherLiveData.value = Fail(it)
                }
        )
    }

    fun initiateEditDuplicateVoucher() {
        launchCatchError(
                block = {
                    withContext(Dispatchers.IO) {
                        val shopInfo = async { getBasicInfo() }
                        val initiateVoucher = async { getInitiateVoucher() }
                        shopInfo.await().let { shopInfoModel ->
                            initiateVoucher.await().let { initiateVoucherUiModel ->
                                mBasicShopInfoLiveData.postValue(Success(shopInfoModel))
                                mInitiateVoucherLiveData.postValue(Success(initiateVoucherUiModel))
                            }
                        }
                    }
                },
                onError = {
                    mInitiateVoucherLiveData.value = Fail(it)
                }
        )
    }

    private suspend fun getBasicInfo(): ShopInfo {
        val userId = userSession.userId.toInt()
        basicShopInfoUseCase.params = BasicShopInfoUseCase.createRequestParams(userId)
        return basicShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getInitiateVoucher(): InitiateVoucherUiModel = initiateVoucherUseCase.executeOnBackground()

}