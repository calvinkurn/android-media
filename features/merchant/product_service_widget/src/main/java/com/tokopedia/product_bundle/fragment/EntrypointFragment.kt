package com.tokopedia.product_bundle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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
import com.tokopedia.product_bundle.common.data.mapper.ProductBundleApplinkMapper.DEFAULT_VALUE_WAREHOUSE_ID
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
        private const val EXTRA_BUNDLE_ID = "BUNDLE_ID"
        private const val EXTRA_SELECTED_PRODUCT_ID = "SELECTED_PRODUCT_ID"
        private const val EXTRA_SOURCE = "SOURCE"
        private const val EXTRA_PARENT_PRODUCT_ID = "PARENT_PRODUCT_ID"
        private const val EXTRA_WAREHOUSE_ID = "PARENT_WAREHOUSE_ID"

        const val tagFragment = "TAG_FRAGMENT"

        @JvmStatic
        fun newInstance(bundleId: Long, selectedProductsId: ArrayList<String>, source: String, parentProductId: Long, warehouseId: String): EntrypointFragment {
            val fragment = EntrypointFragment()
            val bundle = Bundle().apply {
                putLong(EXTRA_BUNDLE_ID, bundleId)
                putStringArrayList(EXTRA_SELECTED_PRODUCT_ID, selectedProductsId)
                putString(EXTRA_SOURCE, source)
                putLong(EXTRA_PARENT_PRODUCT_ID, parentProductId)
                putString(EXTRA_WAREHOUSE_ID, warehouseId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    private var bundleId: Long = 0
    private var selectedProductIds: List<String> = emptyList()
    private var source: String = ""
    private var warehouseId: String = ""
    private var layoutShimmer: ViewGroup? = null
    private var layoutError: GlobalError? = null
    private var isFirstLoadData = true

    @Inject
    lateinit var viewModel: ProductBundleViewModel

    override fun initInjector() {
        DaggerProductBundleComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initApplinkValues()
        setupToolbarActions()
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
        loadBundleData()
        observePageState()
        observeGetBundleInfoResult()
    }

    override fun getScreenName() = null

    private fun loadBundleData() {
        viewModel.parentProductID.let {
            viewModel.getBundleInfo(it, warehouseId)
        }
    }

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
        viewModel.getBundleInfo(viewModel.parentProductID, warehouseId)
    }

    private fun initApplinkValues() {
        arguments?.apply {
            bundleId = getLong(EXTRA_BUNDLE_ID)
            selectedProductIds = getStringArrayList(EXTRA_SELECTED_PRODUCT_ID).orEmpty()
            source = getString(EXTRA_SOURCE).orEmpty()
            warehouseId = getString(EXTRA_WAREHOUSE_ID) ?: DEFAULT_VALUE_WAREHOUSE_ID
            viewModel.parentProductID = getLong(EXTRA_PARENT_PRODUCT_ID)
            viewModel.selectedBundleId = bundleId
            viewModel.selectedProductIds = selectedProductIds
        }
    }

    private fun observePageState() {
        viewModel.pageState.observe(viewLifecycleOwner, { state ->
            when (state) {
                ProductBundleState.LOADING -> showShimmering()
                ProductBundleState.ERROR -> showError()
                else -> {}
            }
        })
    }

    private fun observeGetBundleInfoResult() {
        viewModel.getBundleInfoResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val productBundleData = result.data
                    val bundleInfo = productBundleData.getBundleInfo?.bundleInfo
                    val longSelectedProductIds = selectedProductIds.map { it.toLong() }
                    val inventoryError = InventoryErrorMapper.mapToInventoryError(result, bundleId, longSelectedProductIds)
                    val emptyVariantProductIds = inventoryError.emptyVariantProductIds.map { it.toString() } // product that stock variant is 0
                    if (!bundleInfo.isNullOrEmpty()) {
                        val productBundleFragment = when {
                            inventoryError.type == InventoryErrorType.BUNDLE_EMPTY -> {
                                showEmpty()
                                this
                            }
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
                                    pageSource = source,
                                    parentProductId = viewModel.parentProductID
                                )
                            }
                        }
                        isFirstLoadData = false
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.parent_view, productBundleFragment, tagFragment)
                            .commit()
                    } else {
                        showEmpty()
                    }
                    if (layoutError?.isShown == false && isFirstLoadData) {
                        showInventoryErrorDialog(inventoryError)
                    }
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
        if (viewModel.pageSource == ProductBundleConstants.PAGE_SOURCE_CART || viewModel.pageSource == ProductBundleConstants.PAGE_SOURCE_MINI_CART) {
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