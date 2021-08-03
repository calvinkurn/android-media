package com.tokopedia.product_bundle.single.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.Child
import com.tokopedia.product_bundle.common.data.model.response.Selection
import com.tokopedia.product_bundle.common.data.model.response.VariantOption
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleUiModel
import javax.inject.Inject

class SingleProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mSingleProductBundleUiModel = MutableLiveData<SingleProductBundleUiModel>()
    val singleProductBundleUiModel: LiveData<SingleProductBundleUiModel>
        get() = mSingleProductBundleUiModel

    fun getBundleData() {
        mSingleProductBundleUiModel.value = SingleProductBundleUiModel(
                50,
                List(10) { SingleProductBundleItem(
                        "Paket isi 3",
                        "Women’s Breathable Low-cut Short Socks Cotton Blend",
                        "Rp300.000",
                        "Rp200.000",
                        45,
                        "https://placekitten.com/200/300"
                )},
                "Rp100.000",
                "Rp90.000",
                "Rp10.000",
                10
        )
    }

    fun generateBundleItem(): BundleItem {
        return BundleItem(
            productID = 123450L,
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
        )
    }

    fun generateCartRedirection(): Map<String, CartTypeData> {
        return mapOf(
            "123451" to CartTypeData(
                productId = "123451",
                availableButtons = listOf(
                    AvailableButton(
                        text = "Henlo1",
                        color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN
                    )
                )
            ),
            "123452" to CartTypeData(
                productId = "123452",
                availableButtons = listOf(
                    AvailableButton(
                        text = "Henlo2",
                        color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN
                    )
                )
            ),
            "123453" to CartTypeData(
                productId = "123453",
                availableButtons = listOf(
                    AvailableButton(
                        text = "Henlo3",
                        color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN
                    )
                )
            ),
            "123454" to CartTypeData(
                productId = "123454",
                availableButtons = listOf(
                    AvailableButton(
                        text = "Henlo4",
                        color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN
                    )
                )
            ),
        )
    }

}