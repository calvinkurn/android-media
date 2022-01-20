package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.ImageGeneratorRequestData
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.usecase.CreateCouponProductUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import kotlinx.coroutines.Dispatchers
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

    private val _createCoupon = SingleLiveEvent<Result<Int>>()
    val createCoupon: LiveData<Result<Int>>
        get() = _createCoupon

    private val _couponEligibility = MutableLiveData<Result<InitiateVoucherUiModel>>()
    val couponEligibility: LiveData<Result<InitiateVoucherUiModel>> = _couponEligibility

    private val _couponImageUrl = MutableLiveData<String?>()
    val couponImageUrl: LiveData<String?> = _couponImageUrl

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
                    val params = createCouponProductUseCase.createRequestParam(
                        couponInformation,
                        couponSettings,
                        couponProducts,
                        token
                    )
                    createCouponProductUseCase.params = params
                    createCouponProductUseCase.executeOnBackground()
                }
                _createCoupon.setValue(Success(result))
            },
            onError = {
                _createCoupon.setValue(Fail(it))
            }
        )
    }

    fun generateImage(sourceId: String, products : List<CouponProduct>) {
        val product = products[0]

        val imageParams = arrayListOf(
            ImageGeneratorRequestData(ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_URL, product.imageUrl),
            ImageGeneratorRequestData(ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE, product.price.toString()),
            ImageGeneratorRequestData(ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_RATING, product.rating.toString())
        )

        launchCatchError(
            block = {
                val result = withContext(Dispatchers.IO) {
                    val imageGeneratorUseCase = ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
                    val param = ImageGeneratorUseCase.createParam(sourceId, imageParams)
                    imageGeneratorUseCase.params = param
                    imageGeneratorUseCase.executeOnBackground()
                }
                _couponImageUrl.value = result
            }, onError = {
                _couponImageUrl.value = null
            })

    }

    fun checkVoucherEligibility() {
        launchCatchError(block = {
            initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
            initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(IS_UPDATE_MODE)
            val result = withContext(dispatchers.io) { initiateVoucherUseCase.executeOnBackground() }
            _couponEligibility.value = Success(result)
        }, onError = {
            _couponEligibility.value = Fail(it)
        })
    }
}