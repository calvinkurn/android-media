package com.tokopedia.product_bundle.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.product_bundle.common.data.model.response.*
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.single.presentation.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import javax.inject.Inject

class ProductBundleActivity : BaseSimpleActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ProductBundleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun getNewFragment(): Fragment? {
        return SingleProductBundleFragment.newInstance(listOf(generateBundleInfo1(), generateBundleInfo2()))
    }

    private fun initInjector() {
        DaggerProductBundleComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    /*
    Begin of Dummy model function generator
    */

    fun generateBundleInfo1() = BundleInfo(
        name = "Singel bundle",
        status = "1",
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
                productID = 123453L,
                name = "Bundle 3",
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
                    )
                ),
                children = listOf(
                    Child(
                        productID = 123451L,
                        originalPrice = 10000,
                        bundlePrice = 10000,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(10),
                        name = "child 1",
                        picURL = "https://placekitten.com/200/300"
                    ),
                    Child(
                        productID = 123452L,
                        originalPrice = 10000,
                        bundlePrice = 300,
                        stock = 10,
                        minOrder = 1,
                        optionIds = listOf(11),
                        name = "child 2",
                        picURL = "https://placekitten.com/200/200"
                    )
                )
            )
        )
    )

    fun generateBundleInfo2() = BundleInfo(
        name = "Singel bundle",
        status = "1",
        preorder = PreOrder(
            status = "INACTIVE",
            processTypeNum = 1,
            processTime = 19
        ),
        bundleItems = listOf(
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