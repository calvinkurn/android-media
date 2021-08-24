package com.tokopedia.product_bundle.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorType
import com.tokopedia.product_bundle.common.data.model.uimodel.ProductBundleState
import com.tokopedia.product_bundle.fragment.EntrypointFragment
import com.tokopedia.product_bundle.multiple.presentation.fragment.MultipleProductBundleFragment
import com.tokopedia.product_bundle.single.presentation.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductBundleActivity : BaseSimpleActivity() {

    companion object {
        const val EXTRA_PRODUCT_ID: String = "product_id"

        // TODO("remove if unused")
        fun createInstance(context: Context?, product_id: String? = null): Intent {
            val intent = Intent(context, ProductBundleActivity::class.java)
            product_id?.let {
                intent.putExtra(EXTRA_PRODUCT_ID, product_id)
            }
            return intent
        }
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
        intent.data?.run {
            val pathSegments = this.pathSegments
            val productId = pathSegments.firstOrNull() ?: "0"
            // call getBundleInfo
            viewModel.getBundleInfo(productId.toLongOrZero())
        }
        setupToolbarActions()

        observePageState()
        observeGetBundleInfoResult()
        observeInventoryError()
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

    private fun observePageState() {
        viewModel.pageState.observe(this) { state ->
            when (state) {
                ProductBundleState.LOADING -> entryPointFragment.showShimmering()
                ProductBundleState.SUCCESS -> entryPointFragment.showSuccess()
                ProductBundleState.ERROR -> entryPointFragment.showError()
                else -> entryPointFragment.showSuccess()
            }
        }
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
                    // TODO: log error
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