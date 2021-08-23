package com.tokopedia.product_bundle.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.common.util.InventoryErrorType
import com.tokopedia.product_bundle.multiple.presentation.fragment.MultipleProductBundleFragment
import com.tokopedia.product_bundle.single.presentation.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductBundleActivity : BaseSimpleActivity() {
    private var productId: String = "0"  // TODO: Please handle this default case
    private var bundleId: String = "0"  // TODO: Please handle this default case
    private var selectedProductIds: String = "0"  // TODO: Please handle this default case
    private var source: String = "0"  // TODO: Please handle this default case
    private var cartIds: String = "0"  // TODO: Please handle this default case

    companion object {
        private const val BUNDLE_ID = "bundleId"
        private const val SELECTED_PRODUCT_IDS = "selectedProductIds"
        private const val SOURCE = "source"
        private const val CART_IDS = "cartIds"
    }

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

        var data = intent.data
        data?.let {
//          Applink sample = tokopedia-android-internal/product-bundle/2147881200/?bundleId=3&selectedProductIds=12,45,67&source=cart&cartIds=1,2,3
            data = RouteManager.getIntent(this, intent.data.toString()).data
            val pathSegments = it.pathSegments.orEmpty()
            getProductIdFromUri(it, pathSegments)
            bundleId = if (it.getQueryParameter(BUNDLE_ID) == null) "" else it.getQueryParameter(BUNDLE_ID)!!
            selectedProductIds = if (data?.getQueryParameter(SELECTED_PRODUCT_IDS) == null) "" else it.getQueryParameter(SELECTED_PRODUCT_IDS)!!
            source = if (data?.getQueryParameter(SOURCE) == null) "" else it.getQueryParameter(SOURCE)!!
            cartIds = if (data?.getQueryParameter(CART_IDS) == null) "" else it.getQueryParameter(CART_IDS)!!
        }

        observeGetBundleInfoResult()
        observeInventoryError()
    }

    private fun getProductIdFromUri(it: Uri?, pathSegments: List<String>) {
        productId = if (pathSegments.size >= 2) {
            it?.pathSegments?.getOrNull(1).orEmpty()
        } else {
            "0" // TODO: Please handle this default case if productId is 0
        }
    }

    override fun getNewFragment(): Fragment {
        // TODO: add shimmering
        // TODO: manage initial fragment transaction
        return MultipleProductBundleFragment.newInstance(listOf())
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
                    val productBundleData = result.data
                    val bundleInfo = productBundleData.getBundleInfo.bundleInfo
                    if (bundleInfo.isNotEmpty()) {
                        var productBundleFragment: Fragment? = null
                        productBundleFragment = if (viewModel.isSingleProductBundle(bundleInfo)) {
                            val parentProductID = viewModel.parentProductID
                            SingleProductBundleFragment.newInstance(parentProductID, bundleInfo)
                        } else {
                            MultipleProductBundleFragment.newInstance(bundleInfo)
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(parentViewResourceID, productBundleFragment, tagFragment)
                            .commit()
                    }
                }
                is Fail -> {
                    // log and show error view
                    // TODO: add error view in activity layout
                }
            }
        })
    }

    private fun observeInventoryError() {
        viewModel.inventoryError.observe(this, { errorResult ->
            if (errorResult.type != InventoryErrorType.NO_ERROR) {
                showDialog(errorResult.type.toString())
            }
        })
    }

    private fun showDialog(message: String) {
        DialogUnify(this, HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.dialog_error_title_empty))
            setDescription(message)
            setPrimaryCTAText(getString(R.string.dialog_error_action_empty_variant))
            setSecondaryCTAText(getString(R.string.action_back))
            dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            setSecondaryCTAClickListener { finish() }
            setPrimaryCTAClickListener { dismiss() }
        }.show()
    }

    fun refreshPage() {
        val productId = viewModel.parentProductID
        viewModel.getBundleInfo(productId)
    }
}