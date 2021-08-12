package com.tokopedia.product_bundle.single.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product_bundle.common.data.model.response.*
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_bundle.single.presentation.model.*
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject
import kotlin.math.abs

class SingleProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mSingleProductBundleUiModel = MutableLiveData<SingleProductBundleUiModel>()
    val singleProductBundleUiModel: LiveData<SingleProductBundleUiModel>
        get() = mSingleProductBundleUiModel

    private val mTotalAmountUiModel = MutableLiveData<TotalAmountUiModel>()
    val totalAmountUiModel: LiveData<TotalAmountUiModel>
        get() = mTotalAmountUiModel

    private val mToasterError = MutableLiveData<SingleProductBundleErrorEnum>()
    val toasterError: LiveData<SingleProductBundleErrorEnum>
        get() = mToasterError

    private val mDialogError = MutableLiveData<Pair<String, SingleProductBundleErrorEnum>>()
    val dialogError: LiveData<Pair<String, SingleProductBundleErrorEnum>>
        get() = mDialogError

    fun setBundleInfo(context: Context, bundleInfo: BundleInfo) {
        mSingleProductBundleUiModel.value = BundleInfoToSingleProductBundleMapper
            .mapToSingleProductBundle(context, bundleInfo)
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

    fun checkout(selectedData: List<SingleProductBundleSelectedItem>) {
        val selectedProductId = selectedData.firstOrNull {
            it.isSelected
        }

        val remoteErrorMessage = "ouch" // TODO("TODO implemet api call")
        val hasAnotherBundle = selectedData.size > 1 // TODO("Define as const")

        when {
            selectedProductId == null -> {
                // data not selected
                mToasterError.value = SingleProductBundleErrorEnum.ERROR_BUNDLE_NOT_SELECTED
            }
            selectedProductId.productId.isEmpty() -> {
                // variant not selected
                mToasterError.value = SingleProductBundleErrorEnum.ERROR_VARIANT_NOT_SELECTED
            }
            remoteErrorMessage.isNotEmpty() -> {
                // displaying server error
                if (hasAnotherBundle) {
                    mDialogError.value = Pair(
                        remoteErrorMessage,
                        SingleProductBundleErrorEnum.ERROR_BUNDLE_IS_EMPTY
                    )
                } else {
                    mToasterError.value = SingleProductBundleErrorEnum.ERROR_BUNDLE_IS_EMPTY
                }
            }
            else -> {
                Log.e("checkout", selectedProductId.toString())
            }
        }
    }

    /*
    Begin of Dummy model function generator
    */

    fun generateBundleInfo() = BundleInfo(
        name = "Singel bundle",
        preorder = PreOrder(
            status = "ACTIVE",
            processTypeNum = 1,
            processTime = 19
        ),
        bundleItems = listOf(
            BundleItem(
                productID = 123450L,
                name = "Bundle 1",
                picURL = "https://placekitten.com/200/300",
                minOrder = 2,
                bundlePrice = 2000,
                originalPrice = 2300,
                status = "SHOW",
                selections = listOf(
                    Selection(
                        productVariantID = 6000,
                        variantID = 29,
                        name = "ukuran",
                        identifier = "size",
                        options = listOf(
                            VariantOption(
                                10,
                                10,
                                "S",
                                ""
                            ),
                            VariantOption(
                                11,
                                10,
                                "M",
                                ""
                            ),
                        ),

                        ),
                    Selection(
                        productVariantID = 6001,
                        variantID = 1,
                        name = "warna",
                        identifier = "color",
                        options = listOf(
                            VariantOption(
                                1,
                                1,
                                "Putih",
                                ""
                            ),
                            VariantOption(
                                2,
                                1,
                                "Hitam",
                                ""
                            ),
                        ),

                        )
                ),
                children = listOf(
                    Child(
                        productID = 123451L,
                        originalPrice = 10000,
                        bundlePrice = 9000,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(10, 1),
                        name = "child 1",
                        picURL = "https://placekitten.com/200/300"
                    ),
                    Child(
                        productID = 123452L,
                        originalPrice = 10000,
                        bundlePrice = 8000,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(10, 2),
                        name = "child 2",
                        picURL = "https://placekitten.com/200/200"
                    ),
                    Child(
                        productID = 123453L,
                        originalPrice = 10000,
                        bundlePrice = 9000,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(11, 1),
                        name = "child 3",
                        picURL = "https://placekitten.com/200/300"
                    ),
                    Child(
                        productID = 123454L,
                        originalPrice = 10000,
                        bundlePrice = 8000,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(11, 2),
                        name = "child 4",
                        picURL = "https://placekitten.com/200/200"
                    ),
                )
            ),
            BundleItem(
                productID = 123451L,
                name = "Bundle 2",
                picURL = "https://placekitten.com/200/200",
                minOrder = 2,
                bundlePrice = 2000,
                originalPrice = 2300,
                status = "SHOW"
            )
        )
    )

}