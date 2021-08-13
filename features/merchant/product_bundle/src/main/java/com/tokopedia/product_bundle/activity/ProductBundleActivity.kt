package com.tokopedia.product_bundle.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product_bundle.common.data.model.response.*
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.multiple.presentation.fragment.MultipleProductBundleFragment
import com.tokopedia.product_bundle.single.presentation.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        intent.data?.run {
            val pathSegments = this.pathSegments
            val productId = pathSegments.firstOrNull() ?: "0"
            // call getBundleInfo
            viewModel.getBundleInfo(productId.toLongOrZero())
        }

        observeGetBundleInfoResult()
    }

    override fun getNewFragment(): Fragment {
        return MultipleProductBundleFragment.newInstance()
    }

    private fun initInjector() {
        DaggerProductBundleComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeGetBundleInfoResult() {
        viewModel.getBundleInfoResult.observe(this, { result ->
            when (result) {
                is Success -> {
                    val bundleInfo = result.data
                    val bundleItems = bundleInfo.getBundleInfo.bundleInfo.bundleItems
                    val fragment = MultipleProductBundleFragment.newInstance()
                }
                is Fail -> {
                    // log and show error view
                }
            }
        })
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
                bundlePrice = 2000.0,
                originalPrice = 2300.0,
                status = "SHOW"
            )
        )
    )
}