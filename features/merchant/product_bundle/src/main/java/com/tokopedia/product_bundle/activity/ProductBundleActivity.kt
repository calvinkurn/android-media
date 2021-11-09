package com.tokopedia.product_bundle.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_CART
import com.tokopedia.product_bundle.common.data.mapper.InventoryError
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorMapper
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorType
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.common.data.model.uimodel.ProductBundleState
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.common.extension.showUnifyDialog
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.multiple.presentation.fragment.MultipleProductBundleFragment
import com.tokopedia.product_bundle.single.presentation.fragment.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductBundleActivity : BaseSimpleActivity() {

    private var bundleId: Long = 0
    private var selectedProductIds: List<String> = emptyList()
    private var source: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProductBundleViewModel::class.java)
    }

    private val entryPointFragment = EntrypointFragment()

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initApplinkValues()

        viewModel.parentProductID.let {
            viewModel.getBundleInfo(it)
            entryPointFragment.setProductId(it.toString())
            entryPointFragment.setPageSource(source)
        }

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

    private fun initApplinkValues() {
        var data = intent.data
        data?.let {
            data = RouteManager.getIntent(this, intent.data.toString()).data
            val pathSegments = it.pathSegments.orEmpty()
            bundleId = ProductBundleApplinkMapper.getBundleIdFromUri(it)
            selectedProductIds = ProductBundleApplinkMapper.getSelectedProductIdsFromUri(it)
            source = ProductBundleApplinkMapper.getPageSourceFromUri(it)
            viewModel.parentProductID = ProductBundleApplinkMapper.getProductIdFromUri(it, pathSegments)
            viewModel.selectedBundleId = bundleId
            viewModel.selectedProductIds = selectedProductIds
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
                                SingleProductBundleFragment.newInstance(viewModel.parentProductID.toString(), bundleInfo,
                                    bundleId.toString(), selectedProductId, emptyVariantProductIds,
                                    source)
                            }
                            else -> {
                                MultipleProductBundleFragment.newInstance(
                                    productBundleInfo = bundleInfo,
                                    emptyVariantProductIds = emptyVariantProductIds,
                                    selectedBundleId = bundleId.toString(),
                                    selectedProductIds = selectedProductIds,
                                    source
                                )
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
        if (viewModel.pageSource == PAGE_SOURCE_CART) {
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
        } else {
            if (errorResult.type != InventoryErrorType.NO_ERROR) {
                title = getString(R.string.dialog_error_title_empty_pdp)
                message = getString(R.string.error_bundle_out_of_stock_dialog_description)
                buttonText = getString(R.string.dialog_error_action_change_bundle)
                showUnifyDialog(title, message, buttonText)
            }
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
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, entryPointFragment, tagFragment)
            .commit()
        val productId = viewModel.parentProductID
        viewModel.resetBundleMap()
        viewModel.getBundleInfo(productId)
    }
}