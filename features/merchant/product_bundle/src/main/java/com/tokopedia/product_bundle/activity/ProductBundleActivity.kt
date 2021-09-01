package com.tokopedia.product_bundle.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.mapper.InventoryError
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorMapper
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorType
import com.tokopedia.product_bundle.common.data.model.uimodel.ProductBundleState
import com.tokopedia.product_bundle.common.extension.showUnifyDialog
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.multiple.presentation.fragment.MultipleProductBundleFragment
import com.tokopedia.product_bundle.single.presentation.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductBundleActivity : BaseSimpleActivity() {

    private var productId: String = "0"
    private var bundleId: Long = 0
    private var selectedProductIds: List<String> = emptyList()
    private var source: String = ""

    companion object {
        private const val BUNDLE_ID = "bundleId"
        private const val SELECTED_PRODUCT_IDS = "selectedProductIds"
        private const val SOURCE = "source"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ProductBundleViewModel::class.java)
    }

    private val entryPointFragment = EntrypointFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        var data = intent.data
        data?.let {
//          Applink sample = tokopedia-android-internal/product-bundle/2147881200/?bundleId=3&selectedProductIds=12,45,67&source=cart&cartIds=1,2,3
            data = RouteManager.getIntent(this, intent.data.toString()).data
            val pathSegments = it.pathSegments.orEmpty()
            getProductIdFromUri(it, pathSegments)
            getBundleIdFromUri(it)
            getSelectedProductIdsFromUri(it)
            getPageSourceFromUri(it)
        }
        viewModel.getBundleInfo(productId.toLongOrZero())
        entryPointFragment.setProductId(productId)
        entryPointFragment.setPageSource(source)

        setupToolbarActions()

        observePageState()
        observeGetBundleInfoResult()
    }

    override fun getLayoutRes() = R.layout.activity_product_bundle

    override fun getNewFragment(): Fragment {
        return entryPointFragment
    }

    private fun initInjector() {
        DaggerProductBundleComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun getProductIdFromUri(uri: Uri, pathSegments: List<String>) {
        productId = if (pathSegments.size >= 2) {
            uri.pathSegments.getOrNull(1).orEmpty()
        } else {
            "0" // TODO: Please handle this default case if productId is 0
        }
    }

    private fun getBundleIdFromUri(uri: Uri) {
        try {
            bundleId = if (uri.getQueryParameter(BUNDLE_ID) == null) 0
            else uri.getQueryParameter(BUNDLE_ID).toLongOrZero()
        } catch (e: Exception) { }
    }

    private fun getSelectedProductIdsFromUri(uri: Uri) {
        try {
            uri.getQueryParameter(SELECTED_PRODUCT_IDS)?.let {
                if (it.isNotEmpty())
                    selectedProductIds = it.split(",").orEmpty()
            }
        } catch (e: Exception) { }
    }

    private fun getPageSourceFromUri(uri: Uri) {
        if (uri.getQueryParameter(SOURCE) != null) {
            source = uri.getQueryParameter(SOURCE).orEmpty()
        }
    }

    private fun observePageState() {
        viewModel.pageState.observe(this) { state ->
            when (state) {
                ProductBundleState.LOADING -> entryPointFragment.showShimmering()
                ProductBundleState.ERROR -> entryPointFragment.showError()
                else -> {}
            }
        }
    }

    private fun observeGetBundleInfoResult() {
        viewModel.getBundleInfoResult.observe(this, { result ->
            when (result) {
                is Success -> {
                    val productBundleData = result.data
                    val bundleInfo = productBundleData.getBundleInfo.bundleInfo
                    val longSelectedProductIds = selectedProductIds.map { it.toLong() }
                    val inventoryError = InventoryErrorMapper.mapToInventoryError(result, bundleId, longSelectedProductIds)
                    val emptyVariantProductIds = inventoryError.emptyVariantProductIds.map { it.toString() } // product that stock variant is 0
                    if (bundleInfo.isNotEmpty()) {
                        val productBundleFragment = when {
                            inventoryError.type == InventoryErrorType.BUNDLE_EMPTY -> {
                                entryPointFragment.showEmpty()
                                entryPointFragment
                            }
                            viewModel.isSingleProductBundle(bundleInfo) -> {
                                val selectedProductId = longSelectedProductIds.firstOrNull().orZero()
                                SingleProductBundleFragment.newInstance(productId, bundleInfo,
                                    bundleId.toString(), selectedProductId, emptyVariantProductIds,
                                    source)
                            }
                            else -> {
                                MultipleProductBundleFragment.newInstance(bundleInfo, emptyVariantProductIds)
                            }
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(parentViewResourceID, productBundleFragment, tagFragment)
                            .commit()
                    }
                    showInventoryErrorDialog(inventoryError)
                }
                is Fail -> {
                    // TODO: log error
                }
            }
        })
    }

    private fun showInventoryErrorDialog(errorResult: InventoryError) {
        val title: String
        val message: String
        val buttonText: String
        when (errorResult.type) {
            InventoryErrorType.OTHER_BUNDLE_AND_VARIANT_AVAILABLE -> {
                title = getString(R.string.dialog_error_title_empty)
                message = getString(R.string.dialog_error_message_other_bundle_and_variant_available)
                buttonText = getString(R.string.dialog_error_action_change_variant)
                showUnifyDialog(title, message, buttonText)
            }
            InventoryErrorType.OTHER_BUNDLE_AVAILABLE -> {
                title = getString(R.string.dialog_error_title_empty_bundle)
                message = getString(R.string.dialog_error_message_other_bundle_available)
                buttonText = getString(R.string.dialog_error_action_change_bundle)
                showUnifyDialog(title, message, buttonText)
            }
            InventoryErrorType.OTHER_VARIANT_AVAILABLE -> {
                title = getString(R.string.dialog_error_title_empty)
                message = getString(R.string.dialog_error_message_other_variant_available)
                buttonText = getString(R.string.dialog_error_action_change_variant)
                showUnifyDialog(title, message, buttonText)
            }
            else -> { /* no-op */ }
        }
    }

    private fun setupToolbarActions() {
        findViewById<HeaderUnify>(R.id.toolbar_product_bundle)?.apply {
            headerTitle = getString(R.string.product_bundle_page_title)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    fun refreshPage() {
        val productId = viewModel.parentProductID
        viewModel.getBundleInfo(productId)
    }
}