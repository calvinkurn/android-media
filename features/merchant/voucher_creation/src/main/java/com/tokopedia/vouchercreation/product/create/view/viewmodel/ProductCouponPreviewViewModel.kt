package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.usecase.CreateCouponProductUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val createCouponProductUseCase: CreateCouponProductUseCase,
    private val initiateVoucherUseCase: InitiateVoucherUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val IS_UPDATE_MODE = false
    }

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _createCoupon = MutableLiveData<Result<Int>>()
    val createCoupon: LiveData<Result<Int>>
        get() = _createCoupon

    private val _voucherEligibility = MutableLiveData<Result<InitiateVoucherUiModel>>()
    val voucherEligibility: LiveData<Result<InitiateVoucherUiModel>> get() = _voucherEligibility

    fun validateCoupon(couponSettings: CouponSettings? , couponInformation: CouponInformation?, couponProducts: List<CouponProduct>) {
        if (couponSettings == null) {
            _areInputValid.value = false
            return
        }

        if (couponInformation == null) {
            _areInputValid.value = false
            return
        }

        if (couponProducts.isEmpty()) {
            _areInputValid.value = false
            return
        }

        _areInputValid.value = true
    }


    fun createCoupon(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token: String
    ) {

        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    val params = createCouponProductUseCase.createRequestParam(couponInformation, couponSettings, couponProducts, token)
                    createCouponProductUseCase.params = params
                    createCouponProductUseCase.executeOnBackground()
                }
                _createCoupon.value = Success(result)
            },
            onError = {
                _createCoupon.value = Fail(it)
            }
        )
    }


   /* fun uploadCoupon(bannerBitmap: Bitmap,
                      squareBitmap: Bitmap,
                      createVoucherParam: CreateVoucherParam) {
        launchCatchError(
            block = {
                val bannerImagePath = async {
                    saveBannerVoucherUseCase.bannerBitmap = bannerBitmap
                    saveBannerVoucherUseCase.executeOnBackground()}
                val squareImagePath = async {
                    saveSquareVoucherUseCase.squareBitmap = squareBitmap
                    saveSquareVoucherUseCase.executeOnBackground()
                }
                uploadAndCreateVoucher(bannerImagePath.await(), squareImagePath.await(), createVoucherParam)
            },
            onError = {
                mCreateVoucherResponseLiveData.value = Fail(it)
            })
    }*/


    fun checkVoucherEligibility() {
        launchCatchError(block = {
            initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
            initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(IS_UPDATE_MODE)
            val result = withContext(dispatchers.io) { initiateVoucherUseCase.executeOnBackground() }
            _voucherEligibility.value = Success(result)
        }, onError = {
            _voucherEligibility.value = Fail(it)
        })
    }
}