package com.tokopedia.product_bundle.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
                    val bundleItems = bundleInfo.getBundleInfo.bundleInfo
                    val fragment = SingleProductBundleFragment.newInstance(bundleItems)
                }
                is Fail -> {
                    // log and show error view
                }
            }
        })
    }
}