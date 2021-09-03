package com.tokopedia.product_bundle.multiple.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_BUNDLE_ID
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
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject
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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ProductBundleViewModel::class.java)
    }

    private var processDayView: Typography? = null
    private var productBundleOverView: TotalAmount? = null

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
        // render product bundle info
        productBundleInfo?.run {
            if (this.isNotEmpty()) {
                productBundleInfo.forEach { bundleInfo ->
                    // skip the bundle if not available
                    if (!viewModel.isProductBundleAvailable(bundleInfo)) return@forEach
                    // map product bundle info to product bundle master and details
                    val bundleMaster = viewModel.mapBundleInfoToBundleMaster(bundleInfo)
                    val bundleDetail = viewModel.mapBundleItemsToBundleDetails(bundleInfo.bundleItems)
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
                }
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
        viewModel.selectedProductBundleMaster.observe(viewLifecycleOwner, Observer { productBundleMaster ->
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
                    intent.putExtra(EXTRA_BUNDLE_ID, atcResult.requestParams.bundleId)
                    activity?.setResult(Activity.RESULT_OK, intent)
                } else {
                    RouteManager.route(context, ApplinkConst.CART)
                }
                activity?.finish()
            }
        })
        // observe product bundle quota issue
        viewModel.isBundleOutOfStock.observe(viewLifecycleOwner, { isOutOfStock ->
            isOutOfStock?.let {
                if (isOutOfStock) showProductBundleOutOfStockDialog()
            }
        })
        // observe atc validation error message
        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            errorMessage?.run {
                // show error message
                Toaster.build(requireView(), errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(R.string.action_oke)).show()
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
        productBundleOverView?.amountCtaView?.setOnClickListener {
            // get the selected product bundle
            val selectedProductBundleMaster = viewModel.getSelectedProductBundleMaster()
            val selectedBundleDetails = viewModel.getSelectedProductBundleDetails()
            // validate add to cart input - result = pair<isValid,errorMessage>
            val isAddToCartInputValid = viewModel.validateAddToCartInput(selectedBundleDetails)
            if (isAddToCartInputValid) {
                // map product bundle details to product details
                val userId = viewModel.getUserId()
                val shopId = selectedProductBundleMaster.shopId
                val productDetails = viewModel.mapBundleDetailsToProductDetails(userId, shopId, selectedBundleDetails)
                // add product bundle to cart
                val bundleId = selectedProductBundleMaster.bundleId
                viewModel.addProductBundleToCart(
                    parentProductId = viewModel.parentProductID,
                    bundleId = bundleId,
                    shopId = shopId.toLong(),
                    productDetails  = productDetails
                )
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
            processDayView?.text = "$processDay $timeUnitWording"
            processDayView?.visible()
        }
        else processDayView?.invisible()
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

    private fun showProductBundleOutOfStockDialog() {
        DialogUnify(requireContext(), HORIZONTAL_ACTION, NO_IMAGE).apply {
            setTitle(getString(R.string.error_bundle_out_of_stock_dialog_title))
            setDescription(getString(R.string.error_bundle_out_of_stock_dialog_description))
            setPrimaryCTAText(getString(R.string.action_select_another_bundle))
            setSecondaryCTAText(getString(R.string.action_back))
            dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            setSecondaryCTAClickListener { dismiss() }
            setPrimaryCTAClickListener {
                refreshPage()
            }
        }.show()
    }

    private fun refreshPage() {
        val productBundleActivity = requireActivity() as ProductBundleActivity
        productBundleActivity.refreshPage()
    }

    override fun onProductBundleMasterItemClicked(adapterPosition: Int, productBundleMaster: ProductBundleMaster) {
        // update selected bundle state to view model
        viewModel.setSelectedProductBundleMaster(viewModel.getProductBundleMasters()[adapterPosition])
        // deselect the rest of selection except the selected one
        productBundleMasterAdapter?.deselectUnselectedItems(adapterPosition)
        // update the process day
        with(productBundleMaster) {
            renderProcessDayView(processDayView, preOrderStatus, processDay.toInt(), processTypeNum)
        }
    }

    override fun onProductVariantSpinnerClicked(selectedProductVariant: ProductVariant?) {
        selectedProductVariant?.let {
            AtcVariantNavigation.showVariantBottomSheet(this, it)
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
            Toaster.build(requireView(), getString(R.string.single_bundle_success_variant_added), Toaster.LENGTH_LONG).show()
        }
    }
}