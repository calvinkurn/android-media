package com.tokopedia.product_bundle.multiple.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_IS_VARIANT_CHANGED
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_NEW_BUNDLE_ID
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_OLD_BUNDLE_ID
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_CART
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.extension.setBackgroundToWhite
import com.tokopedia.product_bundle.common.extension.setSubtitleText
import com.tokopedia.product_bundle.common.extension.setTitleText
import com.tokopedia.product_bundle.common.util.AtcVariantNavigation
import com.tokopedia.product_bundle.common.util.Utility
import com.tokopedia.product_bundle.multiple.di.DaggerMultipleProductBundleComponent
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter.ProductBundleDetailItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_bundle.tracking.MultipleProductBundleTracking
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.roundToInt

class MultipleProductBundleFragment : BaseDaggerFragment(),
    ProductBundleMasterItemClickListener,
    ProductBundleDetailItemClickListener {

    companion object {
        private const val PRODUCT_BUNDLE_INFO = "PRODUCT_BUNDLE_INFO"
        private const val EMPTY_VARIANT_PRODUCT_IDS = "EMPTY_VARIANT_PRODUCT_IDS"
        private const val SELECTED_BUNDLE_ID = "SELECTED_BUNDLE_ID"
        private const val SELECTED_PRODUCT_IDS = "SELECTED_PRODUCT_IDS"
        private const val PAGE_SOURCE = "PAGE_SOURCE"
        private const val LOGIN_REQUEST_CODE = 1122
        @JvmStatic
        fun newInstance(productBundleInfo: List<BundleInfo>,
                        emptyVariantProductIds: List<String>,
                        selectedBundleId: String,
                        selectedProductIds: List<String>,
                        pageSource: String) =
            MultipleProductBundleFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PRODUCT_BUNDLE_INFO, ArrayList(productBundleInfo))
                    putStringArrayList(EMPTY_VARIANT_PRODUCT_IDS, ArrayList(emptyVariantProductIds))
                    putString(SELECTED_BUNDLE_ID, selectedBundleId)
                    putStringArrayList(SELECTED_PRODUCT_IDS, ArrayList(selectedProductIds))
                    putString(PAGE_SOURCE, pageSource)
                }
            }
    }

    private val viewModel by lazy {
        ViewModelProvider(this.requireActivity()).get(ProductBundleViewModel::class.java)
    }

    private var processDayView: Typography? = null
    private var productBundleOverView: TotalAmount? = null
    private var layoutBundlePage: ViewGroup? = null
    private var geBundlePage: GlobalError? = null

    // product bundle master components
    private var productBundleMasterView: RecyclerView? = null
    private var productBundleMasterAdapter: ProductBundleMasterAdapter? = null

    // product bundle detail components
    private var productBundleDetailView: RecyclerView? = null
    private var productBundleDetailAdapter: ProductBundleDetailAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_product_bundle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setBackgroundToWhite()

        // get data from activity
        var productBundleInfo: ArrayList<BundleInfo>? = null
        var selectedBundleId: Long? = null
        var selectedVariantIds: List<String> = emptyList()
        var emptyVariantProductIds: List<String> = emptyList()
        if (arguments != null) {
            productBundleInfo = arguments?.getParcelableArrayList(PRODUCT_BUNDLE_INFO)
            selectedBundleId = arguments?.getString(SELECTED_BUNDLE_ID)?.toLongOrNull()
            emptyVariantProductIds = arguments?.getStringArrayList(EMPTY_VARIANT_PRODUCT_IDS).orEmpty()
            selectedVariantIds = arguments?.getStringArrayList(SELECTED_PRODUCT_IDS).orEmpty()
            viewModel.pageSource = arguments?.getString(PAGE_SOURCE) ?: ""
        }

        // setup multiple product bundle views
        processDayView = view.findViewById(R.id.tv_po_process_day)
        setupProductBundleMasterView(view)
        setupProductBundleDetailView(view, emptyVariantProductIds)
        setupProductBundleOverView(view)
        setupGlobalError(view)
        setupToolbarActions()

        // render product bundle info
        productBundleInfo?.run {
            if (this.isNotEmpty()) {
                productBundleInfo.forEach { bundleInfo ->
                    // skip the bundle if not available
                    if (!viewModel.isProductBundleAvailable(bundleInfo)) return@forEach
                    // map product bundle info to product bundle master and details
                    val bundleMaster = viewModel.mapBundleInfoToBundleMaster(bundleInfo)
                    val warehouseId = bundleInfo.warehouseID.toString()
                    val bundleDetail = viewModel.mapBundleItemsToBundleDetails(warehouseId, bundleInfo.bundleItems)
                    // update product bundle map
                    viewModel.updateProductBundleMap(bundleMaster, bundleDetail)
                }
                // get product bundle master from the map - single source of truth
                val productBundleMasters = viewModel.getProductBundleMasters()
                // get selected product bundle master - return the first bundle master if no selection exist
                val selectedProductBundleMaster = viewModel.getSelectedBundle(selectedBundleId, productBundleMasters)
                selectedProductBundleMaster?.run {
                    // render product bundle master chips
                    productBundleMasterAdapter?.setProductBundleMasters(productBundleMasters, this.bundleId)
                    // set selected product variants to bundle details
                    viewModel.setSelectedVariants(selectedVariantIds,this)
                    // set selected bundle master to live data to render details
                    viewModel.setSelectedProductBundleMaster(this)
                    // update the process day
                    renderProcessDayView(processDayView, preOrderStatus, processDay.toInt(), processTypeNum)
                    // update totalView atc button text
                    updateProductBundleOverViewButtonText(preOrderStatus)
                }
                //show error if bundle is empty
                showGlobalError(productBundleMasters.isEmpty())
            }
        }
        observeLiveData()
    }

    override fun getScreenName(): String {
        return getString(R.string.product_bundle_page_title)
    }

    override fun initInjector() {
        DaggerMultipleProductBundleComponent.builder()
            .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
            .build()
            .inject(this)
    }

    private fun observeLiveData() {
        // observe selected product bundle master
        viewModel.selectedProductBundleMaster.observe(viewLifecycleOwner, { productBundleMaster ->
            productBundleMaster?.let {
                val productBundleDetails =  viewModel.getProductBundleDetails(productBundleMaster)
                productBundleDetails?.run {
                    productBundleDetailAdapter?.setProductBundleDetails(this)
                    // render product bundle overview section
                    updateProductBundleOverView(productBundleOverView = productBundleOverView, productBundleDetails = productBundleDetails)
                }
            }
        })
        // observe add to cart result
        viewModel.addToCartResult.observe(viewLifecycleOwner, { atcResult ->
            atcResult?.let {
                if (viewModel.pageSource == PAGE_SOURCE_CART) {
                    val intent = Intent()
                    val oldBundleId = viewModel.selectedBundleId.toString()
                    intent.putExtra(EXTRA_OLD_BUNDLE_ID, oldBundleId)
                    intent.putExtra(EXTRA_NEW_BUNDLE_ID, atcResult.requestParams.bundleId)
                    intent.putExtra(EXTRA_IS_VARIANT_CHANGED,
                        atcResult.responseResult.data.isNotEmpty()) // will empty if there is no GQL hit
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                } else {
                    RouteManager.route(context, ApplinkConst.CART)
                }
            }
        })
        // observe product bundle quota issue
        viewModel.atcDialogMessages.observe(viewLifecycleOwner, { atcDialogMessages ->
            val title = atcDialogMessages.first
            val message = atcDialogMessages.second
            if (title.isNotBlank() && message.isNotBlank()) {
                showAtcDialog(title, message)
            }
        })
        // observe atc validation error message
        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            errorMessage?.run {
                // show error message
                Toaster.build(requireView(), errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.action_oke)).setAnchorView(productBundleOverView?.bottomContentView).show()
            }
        })
    }

    private fun setupProductBundleMasterView(view: View) {
        productBundleMasterView = view.findViewById(R.id.rv_product_bundle_master)
        productBundleMasterAdapter = ProductBundleMasterAdapter(this)
        productBundleMasterView?.let {
            it.adapter = productBundleMasterAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductBundleDetailView(view: View, emptyVariantProductIds: List<String>) {
        productBundleDetailView = view.findViewById(R.id.rv_product_bundle_detail)
        productBundleDetailAdapter = ProductBundleDetailAdapter(this, emptyVariantProductIds)
        productBundleDetailView?.let {
            it.adapter = productBundleDetailAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupProductBundleOverView(view: View) {
        productBundleOverView = view.findViewById(R.id.ta_product_bundle_sum)
        productBundleOverView?.setLabelOrder(
            TotalAmount.Order.TITLE,
            TotalAmount.Order.AMOUNT,
            TotalAmount.Order.SUBTITLE
        )
        productBundleOverView?.amountCtaView?.width = resources
            .getDimension(R.dimen.atc_button_width).toInt()
        productBundleOverView?.amountCtaView?.setOnClickListener {
            val isUserLoggedIn = viewModel.isUserLoggedIn()
            if (isUserLoggedIn) {
                // get the selected product bundle
                val selectedProductBundleMaster = viewModel.getSelectedProductBundleMaster()
                val selectedBundleDetails = viewModel.getSelectedProductBundleDetails()
                // validate add to cart input
                val isAddToCartInputValid = viewModel.validateAddToCartInput(
                    selectedProductBundleMaster, selectedBundleDetails)
                if (isAddToCartInputValid) {
                    // collect required data
                    val bundleId = selectedProductBundleMaster.bundleId
                    val selectedProductIds = viewModel.getSelectedProductIds(viewModel.getSelectedProductBundleDetails())
                    val userId = viewModel.getUserId()
                    val shopId = selectedProductBundleMaster.shopId
                    // map product bundle details to product details
                    val productDetails = viewModel.mapBundleDetailsToProductDetails(
                        userId,
                        shopId,
                        selectedBundleDetails
                    )
                    // track the buy button click
                    MultipleProductBundleTracking.trackMultipleBuyClick(
                        bundleId = bundleId.toString(),
                        productId = selectedProductIds
                    )
                    // add product bundle to cart
                    viewModel.addProductBundleToCart(
                        parentProductId = viewModel.parentProductID,
                        bundleId = bundleId,
                        shopId = shopId.toLong(),
                        productDetails = productDetails
                    )
                }
            } else goToLoginPage()
        }
    }

    private fun setupGlobalError(view: View) {
        layoutBundlePage = view.findViewById(R.id.layout_bundle_page)
        geBundlePage = view.findViewById(R.id.ge_bundle_page)
        geBundlePage?.apply {
            errorIllustration.loadImageWithoutPlaceholder(ProductBundleConstants.BUNDLE_EMPTY_IMAGE_URL)
            errorTitle.text = getString(R.string.single_bundle_error_bundle)
            errorDescription.text = getString(R.string.single_bundle_error_bundle_desc)
            errorAction.text = if (viewModel.pageSource == PAGE_SOURCE_CART) {
                getString(R.string.action_back_to_cart)
            } else {
                getString(R.string.action_back_to_pdp)
            }
            errorAction.setOnClickListener {
                activity?.finish()
            }
        }
        showGlobalError(false)
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_product_bundle)?.apply {
            setNavigationOnClickListener {
                // track back button click
                val bundleId = viewModel.getSelectedProductBundleMaster().bundleId.toString()
                val selectedProductIds = viewModel.getSelectedProductIds(viewModel.getSelectedProductBundleDetails())
                MultipleProductBundleTracking.trackMultipleBackClick(bundleId, selectedProductIds)
                // finish the activity
                activity?.finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderProcessDayView(processDayView:Typography?,
                                     preOrderStatus: String,
                                     processDay: Int,
                                     processTypeNum: Int) {
        if (viewModel.isPreOrderActive(preOrderStatus)) {
            val timeUnitWording = viewModel.getPreOrderTimeUnitWording(processTypeNum)
            processDayView?.text = getString(R.string.preorder_prefix, "$processDay $timeUnitWording")
            processDayView?.visible()
        }
        else processDayView?.gone()
    }

    private fun updateProductBundleOverView(productBundleOverView: TotalAmount?, productBundleDetails: List<ProductBundleDetail>) {
        val totalPrice = viewModel.calculateTotalPrice(productBundleDetails)
        val totalBundlePrice = viewModel.calculateTotalBundlePrice(productBundleDetails)
        val totalDiscount = viewModel.calculateDiscountPercentage(totalPrice, totalBundlePrice)
        val totalSaving = viewModel.calculateTotalSaving(totalPrice, totalBundlePrice)
        val totalDiscountText = String.format(getString(R.string.text_discount_in_percentage), totalDiscount)
        val totalPriceText = Utility.formatToRupiahFormat(totalPrice.roundToInt())
        val totalBundlePriceText = Utility.formatToRupiahFormat(totalBundlePrice.roundToInt())
        val totalSavingText = Utility.formatToRupiahFormat(totalSaving.roundToInt())
        productBundleOverView?.setTitleText(totalDiscountText, totalPriceText)
        productBundleOverView?.amountView?.text = totalBundlePriceText
        productBundleOverView?.setSubtitleText(getString(R.string.text_saving), totalSavingText)
    }

    private fun updateProductBundleOverViewButtonText(preOrderStatus: String) {
        productBundleOverView?.amountCtaView?.text = if (viewModel.isPreOrderActive(preOrderStatus)) {
            getString(R.string.action_preorder)
        } else {
            getString(R.string.action_buy_bundle)
        }
    }

    private fun showGlobalError(isVisible: Boolean) {
        layoutBundlePage?.showWithCondition(!isVisible)
        geBundlePage?.showWithCondition(isVisible)
    }

    private fun showAtcDialog(title: String, message: String) {
        DialogUnify(requireContext(), HORIZONTAL_ACTION, NO_IMAGE).apply {
            setTitle(title)
            setDescription(message)
            setPrimaryCTAText(getString(R.string.action_select_another_bundle))
            setSecondaryCTAText(getString(R.string.action_back))
            dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            setSecondaryCTAClickListener { activity?.finish() }
            setPrimaryCTAClickListener {
                refreshPage()
                dismiss()
            }
        }.show()
    }

    private fun refreshPage() {
        val productBundleActivity = requireActivity() as ProductBundleActivity
        productBundleActivity.refreshPage()
    }

    private fun goToLoginPage() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConst.LOGIN)
        startActivityForResult(intent, LOGIN_REQUEST_CODE)
    }

    private fun goToProductDetailPage(productId: String) {
        RouteManager.route(context, ApplinkConst.PRODUCT_INFO, productId)
    }

    override fun onProductBundleMasterItemClicked(adapterPosition: Int, productBundleMaster: ProductBundleMaster) {
        // update selected bundle state to view model
        viewModel.setSelectedProductBundleMaster(viewModel.getProductBundleMasters()[adapterPosition])
        // deselect the rest of selection except the selected one
        productBundleMasterAdapter?.deselectUnselectedItems(adapterPosition)
        // update the process day
        with(productBundleMaster) {
            // track product bundle master selection
            MultipleProductBundleTracking.trackMultipleBundleOptionClick(
                bundleId = bundleId.toString(),
                productId = viewModel.getSelectedProductIds(viewModel.getSelectedProductBundleDetails())
            )
            // update the process day
            renderProcessDayView(processDayView, preOrderStatus, processDay.toInt(), processTypeNum)
            // update totalView atc button text
            updateProductBundleOverViewButtonText(preOrderStatus)
        }
    }

    override fun onProductNameViewClicked(selectedProductBundleDetail: ProductBundleDetail) {
        // track product name view click event
        val selectedProductId = viewModel.getSelectedProductIdFromBundleDetail(selectedProductBundleDetail)
        val bundleId = viewModel.getSelectedProductBundleMaster().bundleId.toString()
        val selectedProductIds = viewModel.getSelectedProductIds(viewModel.getSelectedProductBundleDetails())
        MultipleProductBundleTracking.trackMultiplePreviewProductClick(
            selectedProductId = selectedProductId,
            bundleId = bundleId,
            productId = selectedProductIds
        )
        // open product detail page
        goToProductDetailPage(selectedProductId)
    }

    override fun onProductVariantSpinnerClicked(selectedProductBundleDetail: ProductBundleDetail) {
        val selectedProductId = selectedProductBundleDetail.productId.toString()
        val variantProductId = selectedProductBundleDetail.selectedVariantId?: ""
        val bundleId = viewModel.getSelectedProductBundleMaster().bundleId.toString()
        val variantValue = selectedProductBundleDetail.selectedVariantText
        val selectedProductVariant = selectedProductBundleDetail.productVariant
        val selectedProductIds = viewModel.getSelectedProductIds(viewModel.getSelectedProductBundleDetails())
        selectedProductVariant?.let { productVariant ->
            // track product variant selection
            MultipleProductBundleTracking.trackMultipleSelectVariantClick(
                selectedProductId = selectedProductId,
                bundleId = bundleId,
                variantLevel = viewModel.getVariantLevel(productVariant).toString(),
                variantTitle = viewModel.getVariantTitle(productVariant),
                variantValue = variantValue,
                variantProductId = variantProductId,
                productId = selectedProductIds
            )
            // open variant bottom sheet
            AtcVariantNavigation.showVariantBottomSheet(this, productVariant, variantProductId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
            mapOfSelectedVariantOption?.run {
                if (this.isNotEmpty()) {
                    // update product bundle map with variant selections
                    val selectedBundleMaster = viewModel.getSelectedProductBundleMaster()
                    val updatedBundleDetail = viewModel.updateProductBundleDetail(
                        selectedBundleMaster = selectedBundleMaster,
                        parentProductId = parentProductId.toLongOrZero(),
                        selectedVariantId = selectedProductId.toLongOrZero()
                    )
                    // update product bundle detail variant selection
                    updatedBundleDetail?.let { productBundleDetailAdapter?.setVariantSelection(parentProductId.toLongOrZero(), it) }
                    // update total amount view
                    val selectedBundleDetails = viewModel.getSelectedProductBundleDetails()
                    updateProductBundleOverView(productBundleOverView, selectedBundleDetails)
                }
            }
            Toaster.build(
                requireView(),
                getString(R.string.single_bundle_success_variant_added),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.action_oke)
            ).setAnchorView(productBundleOverView?.bottomContentView).show()
        }
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            productBundleOverView?.amountCtaView?.performClick()
        }
    }
}