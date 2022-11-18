package com.tokopedia.product_bundle.single.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_bundle.single.presentation.constant.SingleBundleInfoConstants.BUNDLE_QTY
import com.tokopedia.product_bundle.single.presentation.model.BundleInfoToSingleProductBundleMapper
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleDialogModel
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleErrorEnum
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleUiModel
import com.tokopedia.product_bundle.single.presentation.model.TotalAmountUiModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class SingleProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val addToCartBundleUseCase: AddToCartBundleUseCase,
        private val userSession: UserSessionInterface
) : BaseViewModel(dispatcher.main) {

    private val mSingleProductBundleUiModel = MutableLiveData<SingleProductBundleUiModel>()
    val singleProductBundleUiModel: LiveData<SingleProductBundleUiModel>
        get() = mSingleProductBundleUiModel

    private val mTotalAmountUiModel = MutableLiveData<TotalAmountUiModel>()
    val totalAmountUiModel: LiveData<TotalAmountUiModel>
        get() = mTotalAmountUiModel

    private val mAddToCartResult = MutableLiveData<AddToCartDataResult>()
    val addToCartResult: LiveData<AddToCartDataResult>
        get() = mAddToCartResult

    private val mToasterError = MutableLiveData<SingleProductBundleErrorEnum>()
    val toasterError: LiveData<SingleProductBundleErrorEnum>
        get() = mToasterError

    private val mDialogError = MutableLiveData<SingleProductBundleDialogModel>()
    val dialogError: LiveData<SingleProductBundleDialogModel>
        get() = mDialogError

    private val mPageError = MutableLiveData<SingleProductBundleErrorEnum>()
    val pageError: LiveData<SingleProductBundleErrorEnum>
        get() = mPageError

    private val mThrowableError = MutableLiveData<Throwable>()
    val throwableError: LiveData<Throwable>
        get() = mThrowableError

    fun isUserLoggedIn(): Boolean {
        return userSession.userId.isNotBlank()
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun setBundleInfo(
        context: Context,
        bundleInfo: List<BundleInfo>,
        selectedBundleId: String,
        selectedProductId: Long,
        emptyVariantProductIds: List<String>
    ) {
        val bundleModel = BundleInfoToSingleProductBundleMapper
            .mapToSingleProductBundle(context, bundleInfo, selectedBundleId, selectedProductId,
                emptyVariantProductIds)

        if (bundleModel.items.isEmpty()) {
            mPageError.value = SingleProductBundleErrorEnum.ERROR_BUNDLE_IS_EMPTY
        } else {
            mSingleProductBundleUiModel.value = bundleModel
            mPageError.value = SingleProductBundleErrorEnum.NO_ERROR
        }
    }

    fun getVariantText(selectedProductVariant: ProductVariant, selectedProductId: String): String {
        var resultText = ""
        val variant = selectedProductVariant.variants
        val variantChild = selectedProductVariant.getChildByProductId(selectedProductId)

        if (variantChild != null) {
            resultText = getVariantText(variant, variantChild.optionIds)
        }

        return resultText
    }

    fun getVariantText(productVariant: List<Variant>, optionIds: List<String>): String {
        val resultText = mutableListOf<String?>()
        optionIds.forEachIndexed { index, optionId ->
            val option = productVariant[index].options.find {
                it.id == optionId
            }
            if (option != null) {
                resultText.add(option.value)
            }
        }
        return resultText.joinToString(", ")
    }

    fun updateTotalAmount(originalPrice: Double, discountedPrice: Double, quantity: Int) {
        mTotalAmountUiModel.value = TotalAmountUiModel(
            price = CurrencyFormatUtil.convertPriceValueToIdrFormat(discountedPrice * quantity, false),
            slashPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalPrice * quantity, false),
            discount = DiscountUtil.getDiscountPercentage(originalPrice, discountedPrice),
            priceGap = CurrencyFormatUtil.convertPriceValueToIdrFormat(abs(originalPrice - discountedPrice) * quantity, false)
        )
    }

    fun validateAndAddToCart(
        pageSource: String,
        parentProductID: String,
        selectedBundleId: String,
        selectedProductId: String,
        selectedDataList: List<SingleProductBundleSelectedItem>
    ) {
        val selectedData = selectedDataList.firstOrNull {
            it.isSelected
        }

        when {
            selectedData == null -> {
                // data not selected
                mToasterError.value = SingleProductBundleErrorEnum.ERROR_BUNDLE_NOT_SELECTED
                return
            }
            selectedData.productId.isEmpty() -> {
                // variant not selected
                mToasterError.value = SingleProductBundleErrorEnum.ERROR_VARIANT_NOT_SELECTED
                return
            }
            (pageSource == ProductBundleConstants.PAGE_SOURCE_CART || pageSource == ProductBundleConstants.PAGE_SOURCE_MINI_CART) &&
                    selectedData.bundleId == selectedBundleId &&
                    selectedData.productId == selectedProductId -> {
                // selected bundleId is not changed
                mAddToCartResult.value = AddToCartDataResult(
                    requestParams = AddToCartBundleRequestParams( bundleId = selectedBundleId ),
                    responseResult = AddToCartBundleDataModel()
                )
            }
            else -> addToCart(parentProductID, selectedData.bundleId, selectedData.productId,
                selectedData.shopId, selectedData.quantity, selectedData.warehouseId)
        }
    }

    private fun addToCart(
        parentProductID: String,
        bundleId: String,
        productId: String,
        shopId: String,
        quantity: Int,
        warehouseId: String
    ) {
        val customerId = userSession.userId
        launchCatchError(block = {
            val atcParams = AddToCartBundleRequestParams(
                shopId = shopId,
                bundleId = bundleId,
                bundleQty = BUNDLE_QTY,
                selectedProductPdp = parentProductID,
                listOf(
                    ProductDetail(
                        customerId = customerId,
                        isProductParent = productId == parentProductID,
                        productId = productId,
                        quantity = quantity,
                        shopId = shopId,
                        warehouseId = warehouseId
                    )
                )
            )
            val result = withContext(dispatcher.io) {
                addToCartBundleUseCase.setParams(atcParams)
                addToCartBundleUseCase.executeOnBackground()
            }

            result.validateResponse(
                    onSuccess = {
                        mAddToCartResult.value = AddToCartDataResult(
                                requestParams = atcParams,
                                responseResult = result.addToCartBundleDataModel
                        )
                    },
                    onFailedWithMessages = {
                        mDialogError.value = SingleProductBundleDialogModel(
                                title = it.firstOrNull(),
                                message = it.lastOrNull(),
                                type = SingleProductBundleDialogModel.DialogType.DIALOG_REFRESH
                        )
                    },
                    onFailedWithException = {
                        mThrowableError.value = it
                    }
            )
        }, onError = {
            mThrowableError.value = it
        })
    }
}