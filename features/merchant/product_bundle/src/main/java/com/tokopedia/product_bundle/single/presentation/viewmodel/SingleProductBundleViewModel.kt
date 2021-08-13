package com.tokopedia.product_bundle.single.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product_bundle.common.data.model.response.*
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_bundle.single.presentation.model.*
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

    private val mToasterError = MutableLiveData<Throwable>()
    val toasterError: LiveData<Throwable>
        get() = mToasterError

    private val mPageError = MutableLiveData<Throwable>()
    val pageError: LiveData<Throwable>
        get() = mPageError

    fun setBundleInfo(context: Context, bundleInfo: BundleInfo) {
        mSingleProductBundleUiModel.value = BundleInfoToSingleProductBundleMapper
            .mapToSingleProductBundle(context, bundleInfo)
    }

    fun updateTotalAmount(price: Double, slashPrice: Double, quantity: Int) {
        mTotalAmountUiModel.value = TotalAmountUiModel(
            price = (price * quantity).toString(),
            slashPrice = (slashPrice * quantity).toString(),
            discount = DiscountUtil.getDiscountPercentage(price, slashPrice),
            priceGap = (abs(price - slashPrice) * quantity).toString()
        )
    }

    fun checkout(selectedData: List<SingleProductBundleSelectedItem>) {
        val selectedProductId = selectedData.firstOrNull {
            it.isSelected
        }

        when {
            selectedProductId == null -> {
                // data not selected
                mToasterError.value = MessageErrorException("Oops, pilih product dulu, ya.")
            }
            selectedProductId.productId.isEmpty() -> {
                // variant not selected
                mToasterError.value = MessageErrorException("Oops, pilih varian dulu, ya.")
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
        preorder = Preorder(
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
                bundlePrice = 2000.0,
                originalPrice = 2300.0,
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
                        originalPrice = 10000.0,
                        bundlePrice = 9000.0,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(10, 1),
                        name = "child 1",
                        picURL = "https://placekitten.com/200/300"
                    ),
                    Child(
                        productID = 123452L,
                        originalPrice = 10000.0,
                        bundlePrice = 8000.0,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(10, 2),
                        name = "child 2",
                        picURL = "https://placekitten.com/200/200"
                    ),
                    Child(
                        productID = 123453L,
                        originalPrice = 10000.0,
                        bundlePrice = 9000.0,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(11, 1),
                        name = "child 3",
                        picURL = "https://placekitten.com/200/300"
                    ),
                    Child(
                        productID = 123454L,
                        originalPrice = 10000.0,
                        bundlePrice = 8000.0,
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
                bundlePrice = 2000.0,
                originalPrice = 2300.0,
                status = "SHOW"
            )
        )
    )

}