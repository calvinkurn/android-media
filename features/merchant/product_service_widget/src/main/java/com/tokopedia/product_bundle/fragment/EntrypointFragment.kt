package com.tokopedia.product_bundle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.BUNDLE_EMPTY_IMAGE_URL
import com.tokopedia.product_bundle.common.data.mapper.InventoryError
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorMapper
import com.tokopedia.product_bundle.common.data.mapper.InventoryErrorType
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper
import com.tokopedia.product_bundle.common.data.model.uimodel.ProductBundleState
import com.tokopedia.product_bundle.common.di.DaggerProductBundleComponent
import com.tokopedia.product_bundle.common.extension.setBackgroundToWhite
import com.tokopedia.product_bundle.common.extension.showUnifyDialog
import com.tokopedia.product_bundle.multiple.presentation.fragment.MultipleProductBundleFragment
import com.tokopedia.product_bundle.single.presentation.fragment.SingleProductBundleFragment
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.product_service_widget.R
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class EntrypointFragment : BaseDaggerFragment() {

    companion object {
        const val tagFragment = "TAG_FRAGMENT"
    }

    private var bundleId: Long = 0
    private var selectedProductIds: List<String> = emptyList()
    private var source: String = ""
    private var layoutShimmer: ViewGroup? = null
    private var layoutError: GlobalError? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ProductBundleViewModel::class.java)
    }

    override fun initInjector() {
        DaggerProductBundleComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initApplinkValues()

        viewModel.parentProductID.let {
            viewModel.getBundleInfo(it)
        }

        setupToolbarActions()

        observePageState()
        observeGetBundleInfoResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entrypoint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setBackgroundToWhite()
        setupShimmer(view)
        setupGlobalError(view)
    }

    override fun getScreenName() = null

    private fun setupShimmer(view: View) {
        val totalAmountShimmer: TotalAmount? = view.findViewById(R.id.total_amount)
        totalAmountShimmer?.isTotalAmountLoading = true
        layoutShimmer = view.findViewById(R.id.layout_shimmer)
    }

    private fun setupGlobalError(view: View) {
        layoutError = view.findViewById(R.id.layout_error)
        layoutError?.apply {
            errorAction.setOnClickListener {
                refreshPage()
            }
            hide()
        }
    }

    private fun GlobalError.showError() {
        setType(GlobalError.SERVER_ERROR)
    }

    private fun GlobalError.showEmpty() {
        setType(GlobalError.PAGE_FULL)
        errorIllustration.loadImageWithoutPlaceholder(BUNDLE_EMPTY_IMAGE_URL)
        errorTitle.text = getString(R.string.single_bundle_error_bundle)
        errorDescription.text = getString(R.string.error_bundle_desc)
        errorAction.text = getString(R.string.action_back_to_recent_page)
        errorAction.setOnClickListener {
            activity?.finish()
        }
    }

    private fun showShimmering() {
        layoutShimmer?.show()
        layoutError?.hide()
    }

    private fun showError() {
        layoutShimmer?.hide()
        layoutError?.show()
        layoutError?.showError()
    }

    private fun showEmpty() {
        layoutShimmer?.hide()
        layoutError?.show()
        layoutError?.showEmpty()
    }

    private fun refreshPage() {
        viewModel.resetBundleMap()
        viewModel.getBundleInfo(viewModel.parentProductID)
    }

    private fun initApplinkValues() {
        var data = activity?.intent?.data
        data?.let {
            data = RouteManager.getIntent(context, activity?.intent?.data.toString()).data
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
                ProductBundleState.LOADING -> showShimmering()
                ProductBundleState.ERROR -> showError()
                else -> {}
            }
        }
    }

    private fun observeGetBundleInfoResult() {
        viewModel.getBundleInfoResult.observe(this, { result ->
            when (result) {
                is Success -> {
                    val productBundleData = result.data
                    val bundleInfo = productBundleData.getBundleInfo?.bundleInfo
                    val longSelectedProductIds = selectedProductIds.map { it.toLong() }
                    val inventoryError = InventoryErrorMapper.mapToInventoryError(result, bundleId, longSelectedProductIds)
                    val emptyVariantProductIds = inventoryError.emptyVariantProductIds.map { it.toString() } // product that stock variant is 0
                    if (!bundleInfo.isNullOrEmpty()) {
                        val productBundleFragment = when {
                            viewModel.isSingleProductBundle(bundleInfo.orEmpty()) -> {
                                val selectedProductId = longSelectedProductIds.firstOrNull().orZero()
                                SingleProductBundleFragment.newInstance(viewModel.parentProductID.toString(), bundleInfo.orEmpty(),
                                    bundleId.toString(), selectedProductId, emptyVariantProductIds,
                                    source)
                            }
                            else -> {
                                MultipleProductBundleFragment.newInstance(
                                    productBundleInfo = bundleInfo.orEmpty(),
                                    emptyVariantProductIds = emptyVariantProductIds,
                                    selectedBundleId = bundleId.toString(),
                                    selectedProductIds = selectedProductIds,
                                    source
                                )
                            }
                        }
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.parent_view, productBundleFragment, tagFragment)
                            .commit()
                    } else {
                        showEmpty()
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
        if (viewModel.pageSource == ProductBundleConstants.PAGE_SOURCE_CART) {
            when (errorResult.type) {
                InventoryErrorType.OTHER_BUNDLE_AND_VARIANT_AVAILABLE -> {
                    title = getString(R.string.dialog_error_title_empty)
                    message = getString(R.string.dialog_error_message_other_bundle_and_variant_available)
                    buttonText = getString(R.string.dialog_error_action_change_variant)
                    activity?.showUnifyDialog(title, message, buttonText)
                }
                InventoryErrorType.OTHER_BUNDLE_AVAILABLE -> {
                    title = getString(R.string.dialog_error_title_empty_bundle)
                    message = getString(R.string.dialog_error_message_other_bundle_available)
                    buttonText = getString(R.string.dialog_error_action_change_bundle)
                    activity?.showUnifyDialog(title, message, buttonText)
                }
                InventoryErrorType.OTHER_VARIANT_AVAILABLE -> {
                    title = getString(R.string.dialog_error_title_empty)
                    message = getString(R.string.dialog_error_message_other_variant_available)
                    buttonText = getString(R.string.dialog_error_action_change_variant)
                    activity?.showUnifyDialog(title, message, buttonText)
                }
                else -> { /* no-op */ }
            }
        } else {
            if (errorResult.type != InventoryErrorType.NO_ERROR) {
                title = getString(R.string.dialog_error_title_empty_pdp)
                message = getString(R.string.error_bundle_out_of_stock_dialog_description)
                buttonText = getString(R.string.dialog_error_action_change_bundle)
                activity?.showUnifyDialog(title, message, buttonText)
            }
        }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_product_bundle)?.apply {
            headerTitle = getString(R.string.product_bundle_page_title)
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }
}