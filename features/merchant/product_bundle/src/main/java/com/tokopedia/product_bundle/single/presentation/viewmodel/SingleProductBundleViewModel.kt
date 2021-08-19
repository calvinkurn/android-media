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
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_bundle.single.presentation.model.*
import com.tokopedia.product_bundle.single.presentation.model.SingleBundleInfoConstants.BUNDLE_QTY
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class SingleProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val addToCartBundleUseCase: AddToCartBundleUseCase
) : BaseViewModel(dispatcher.main) {

    private val mSingleProductBundleUiModel = MutableLiveData<SingleProductBundleUiModel>()
    val singleProductBundleUiModel: LiveData<SingleProductBundleUiModel>
        get() = mSingleProductBundleUiModel

    private val mTotalAmountUiModel = MutableLiveData<TotalAmountUiModel>()
    val totalAmountUiModel: LiveData<TotalAmountUiModel>
        get() = mTotalAmountUiModel

    private val mAddToCartResult = MutableLiveData<AddToCartBundleDataModel>()
    val addToCartResult: LiveData<AddToCartBundleDataModel>
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

    fun setBundleInfo(
        context: Context,
        bundleInfo: List<BundleInfo>,
        selectedBundleId: String,
        selectedProductId: Long
    ) {
        val bundleModel = BundleInfoToSingleProductBundleMapper
            .mapToSingleProductBundle(context, bundleInfo, selectedBundleId, selectedProductId)

        if (bundleModel.items.isEmpty()) {
            mToasterError.value = SingleProductBundleErrorEnum.ERROR_BUNDLE_IS_EMPTY
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

    fun validateAndCheckout(parentProductID: Long, selectedDataList: List<SingleProductBundleSelectedItem>) {
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
            else -> addToCart(parentProductID, selectedData.bundleId, selectedData.productId,
                selectedData.shopId, selectedData.quantity)
        }
    }

    private fun addToCart(
        parentProductID: Long,
        bundleId: String,
        productId: String,
        shopId: String,
        quantity: Int
    ) {
        launchCatchError(block = {
            val result = withContext(dispatcher.io) {
                addToCartBundleUseCase.setParams(
                    AddToCartBundleRequestParams(
                        shopId = shopId,
                        bundleId = bundleId,
                        bundleQty = BUNDLE_QTY,
                        selectedProductPdp = parentProductID.toString(),
                        listOf(
                            ProductDetail(
                                productId,
                                quantity = quantity,
                                shopId = shopId
                            )
                        )
                    )
                )
                addToCartBundleUseCase.executeOnBackground()
            }
            if (result.data.isNotEmpty()) {
                mAddToCartResult.value = result
            } else {
                mDialogError.value = SingleProductBundleDialogModel(
                    title = result.message.firstOrNull(),
                    message = result.message.lastOrNull(),
                    type = SingleProductBundleDialogModel.DialogType.DIALOG_REFRESH
                )
            }
        }, onError = {
            mDialogError.value = SingleProductBundleDialogModel(
                message = it.localizedMessage,
                type = SingleProductBundleDialogModel.DialogType.DIALOG_NORMAL
            )
        })
    }

}