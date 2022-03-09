package com.tokopedia.product_bundle.single.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.dialog.DialogUnify.Companion.SINGLE_ACTION
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.BUNDLE_EMPTY_IMAGE_URL
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_NEW_BUNDLE_ID
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_OLD_BUNDLE_ID
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_CART
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.extension.setBackgroundToWhite
import com.tokopedia.product_bundle.common.extension.setSubtitleText
import com.tokopedia.product_bundle.common.extension.setTitleText
import com.tokopedia.product_bundle.common.util.AtcVariantNavigation
import com.tokopedia.product_bundle.single.di.DaggerSingleProductBundleComponent
import com.tokopedia.product_bundle.single.presentation.adapter.BundleItemListener
import com.tokopedia.product_bundle.single.presentation.adapter.SingleProductBundleAdapter
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleDialogModel
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleErrorEnum
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem
import com.tokopedia.product_bundle.single.presentation.viewmodel.SingleProductBundleViewModel
import com.tokopedia.product_bundle.tracking.SingleProductBundleTracking
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SingleProductBundleFragment(
    private val parentProductID: String = "",
    private val bundleInfo: List<BundleInfo> = emptyList(),
    private val selectedBundleId: String = "",
    private val selectedProductId: Long = 0L, // usually variant child productID
    private val emptyVariantProductIds: List<String> = emptyList(),
    private val pageSource: String = ""
) : BaseDaggerFragment(), BundleItemListener {

    @Inject
    lateinit var viewModel: SingleProductBundleViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var tvBundlePreorder: Typography? = null
    private var bundleListLayout: LinearLayoutCompat? = null
    private var totalAmount: TotalAmount? = null
    private var geBundlePage: GlobalError? = null
    private var loaderDialog: LoaderDialog? = null
    private var adapter = SingleProductBundleAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setBundleInfo(requireContext(), bundleInfo, selectedBundleId, selectedProductId,
            emptyVariantProductIds)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_single_product_bundle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setBackgroundToWhite()

        setupTotalPO(view)
        setupRecyclerViewItems(view)
        setupTotalAmount(view)
        setupGlobalError(view)
        setupToolbarActions()

        observeSingleProductBundleUiModel()
        observeTotalAmountUiModel()
        observeAddToCartResult()
        observeToasterError()
        observeDialogError()
        observePageError()
        observeThrowableError()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
            val selectedProductVariant = adapter.getSelectedProductVariant() ?: ProductVariant()
            adapter.setSelectedVariant(selectedProductId,
                viewModel.getVariantText(selectedProductVariant, selectedProductId))
            Toaster.build(
                requireView(),
                getString(R.string.single_bundle_success_variant_added),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.action_oke)
            ).setAnchorView(totalAmount?.bottomContentView).show()
        }
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.validateAndAddToCart(
                pageSource,
                parentProductID,
                selectedBundleId,
                selectedProductId.toString(),
                adapter.getSelectedData()
            )
        }
        hideLoadingDialog()
    }

    override fun getScreenName() = SingleProductBundleFragment::class.java.simpleName

    override fun initInjector() {
        DaggerSingleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .build()
                .inject(this)
    }

    override fun onVariantSpinnerClicked(
        selectedVariant: ProductVariant?,
        selectedProductId: String?
    ) {
        selectedVariant?.let {
            AtcVariantNavigation.showVariantBottomSheet(this, it, selectedProductId.orEmpty())
        }
    }

    override fun onBundleItemSelected(
        originalPrice: Double,
        discountedPrice: Double,
        quantity: Int,
        preorderDurationWording: String?
    ) {
        SingleProductBundleTracking.trackSingleBundleOptionClick(
            adapter.getSelectedBundleId(),
            parentProductID,
            adapter.getSelectedProductId()
        )
        viewModel.updateTotalAmount(originalPrice, discountedPrice, quantity)
        updateTotalPO(preorderDurationWording)
    }

    override fun onDataChanged(
        selectedData: List<SingleProductBundleSelectedItem>,
        selectedProductVariant: ProductVariant?
    ) {
        val selectedProductId = selectedData.firstOrNull {
            it.isSelected
        }?.productId.orEmpty()
        if (selectedProductId.isNotEmpty() && selectedProductVariant != null) {
            val selectedVariantText = viewModel.getVariantText(selectedProductVariant, selectedProductId)
            adapter.setSelectedVariant(selectedProductId, selectedVariantText)
        }
    }

    private fun observeSingleProductBundleUiModel() {
        viewModel.singleProductBundleUiModel.observe(viewLifecycleOwner, {
            // update PO and Total amount price if there is selected bundle
            val selectedItem = it.getSelectedSingleProductBundleItem()
            if (selectedItem != null) {
                updateTotalAmount(selectedItem)
                updateTotalPO(selectedItem)
            }

            // set adapter data
            adapter.setData(it.items, it.selectedItems)
        })
    }

    private fun observeTotalAmountUiModel() {
        viewModel.totalAmountUiModel.observe(viewLifecycleOwner, {
            updateTotalAmount(it.price, it.discount, it.slashPrice, it.priceGap)
        })
    }

    private fun observeAddToCartResult() {
        viewModel.addToCartResult.observe(viewLifecycleOwner, {
            hideLoadingDialog()
            if (pageSource == PAGE_SOURCE_CART) {
                val intent = Intent()
                intent.putExtra(EXTRA_OLD_BUNDLE_ID, selectedBundleId)
                intent.putExtra(EXTRA_NEW_BUNDLE_ID, it.requestParams.bundleId)
                intent.putExtra(ProductBundleConstants.EXTRA_IS_VARIANT_CHANGED,
                    it.responseResult.data.isNotEmpty()) // will empty if there is no GQL hit
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            } else {
                RouteManager.route(context, ApplinkConst.CART)
            }
        })
    }

    private fun observeToasterError() {
        viewModel.toasterError.observe(viewLifecycleOwner, { errorType ->
            val errorMessage = when (errorType) {
                SingleProductBundleErrorEnum.ERROR_BUNDLE_NOT_SELECTED ->
                    getString(R.string.single_bundle_error_bundle_not_selected)
                SingleProductBundleErrorEnum.ERROR_VARIANT_NOT_SELECTED ->
                    getString(R.string.single_bundle_error_variant_not_selected)
                SingleProductBundleErrorEnum.ERROR_BUNDLE_IS_EMPTY ->
                    getString(R.string.single_bundle_error_bundle_is_empty_long)
                else -> getString(R.string.single_bundle_error_unknown)
            }
            hideLoadingDialog()
            Toaster.build(requireView(), errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(R.string.action_oke)).setAnchorView(totalAmount?.bottomContentView).show()
        })
    }

    private fun observeDialogError() {
        viewModel.dialogError.observe(viewLifecycleOwner, { dialogStruct ->
            var dialogAction = SINGLE_ACTION
            var primaryText = getString(R.string.action_reload)
            if (dialogStruct.type == SingleProductBundleDialogModel.DialogType.DIALOG_REFRESH) {
                dialogAction = HORIZONTAL_ACTION
                primaryText = getString(R.string.action_select_another_bundle)
            }

            hideLoadingDialog()
            DialogUnify(requireContext(), dialogAction, NO_IMAGE).apply {
                setDescription(dialogStruct.message.orEmpty())
                setPrimaryCTAText(primaryText)
                setSecondaryCTAText(getString(R.string.action_back))
                dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
                setSecondaryCTAClickListener { activity?.finish() }
                setPrimaryCTAClickListener {
                    dismiss()
                    refreshPage()
                }
            }.let {
                it.setTitle(dialogStruct.title.orEmpty())
                it.show()
            }
        })
    }

    private fun observePageError() {
        viewModel.pageError.observe(viewLifecycleOwner, { errorType ->
            val isError = errorType != SingleProductBundleErrorEnum.NO_ERROR
            geBundlePage?.isVisible = isError
            bundleListLayout?.isVisible = !isError
            hideLoadingDialog()
        })
    }

    private fun observeThrowableError() {
        viewModel.throwableError.observe(viewLifecycleOwner, {
            Toaster.build(
                requireView(),
                ErrorHandler.getErrorMessage(context, it),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).setAnchorView(totalAmount?.bottomContentView).show()
            hideLoadingDialog()
            // TODO: log error
        })
    }

    private fun setupTotalPO(view: View) {
        tvBundlePreorder = view.findViewById(R.id.tv_bundle_preorder)
        updateTotalPO(null) // set null to hide
    }

    private fun setupRecyclerViewItems(view: View) {
        val rvBundleItems: RecyclerView = view.findViewById(R.id.rv_bundle_items)
        rvBundleItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBundleItems.adapter = adapter

        bundleListLayout = view.findViewById(R.id.bundle_list_layout)
    }

    private fun setupTotalAmount(view: View) {
        totalAmount = view.findViewById(R.id.total_amount)
        totalAmount?.apply {
            amountCtaView.width = resources.getDimension(R.dimen.atc_button_width).toInt()
            setLabelOrder(TotalAmount.Order.TITLE, TotalAmount.Order.AMOUNT, TotalAmount.Order.SUBTITLE)
            val defaultPrice = context.getString(R.string.single_bundle_default_price)
            updateTotalAmount(
                price = defaultPrice,
                slashPrice = defaultPrice,
                priceGap = defaultPrice
            )
            amountCtaView.setOnClickListener {
                SingleProductBundleTracking.trackSingleBuyClick(
                    adapter.getSelectedBundleId(),
                    parentProductID,
                    adapter.getSelectedProductId()
                )
                atcProductBundle()
            }
        }
    }

    private fun setupGlobalError(view: View) {
        geBundlePage = view.findViewById(R.id.ge_bundle_page)
        geBundlePage?.apply {
            errorIllustration.loadImageWithoutPlaceholder(BUNDLE_EMPTY_IMAGE_URL)
            errorTitle.text = getString(R.string.single_bundle_error_bundle)
            errorDescription.text = getString(R.string.error_bundle_desc)
            errorAction.text = getString(R.string.action_back_to_recent_page)
            errorAction.setOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_product_bundle)?.apply {
            setNavigationOnClickListener {
                SingleProductBundleTracking.trackSingleBackClick(
                    adapter.getSelectedBundleId(),
                    parentProductID,
                    adapter.getSelectedProductId()
                )
                activity?.finish()
            }
        }
    }

    // only visible when totalPOWording not null or empty
    private fun updateTotalPO(totalPOWording: String?) {
        tvBundlePreorder?.isVisible = !totalPOWording.isNullOrEmpty()
        tvBundlePreorder?.text = getString(R.string.preorder_prefix, totalPOWording)
        updateTotalAmountAtcButtonText(totalPOWording)
    }

    private fun updateTotalPO(singleProductBundleItem: SingleProductBundleItem) {
        updateTotalPO(singleProductBundleItem.preorderDurationWording)
    }

    private fun updateTotalAmount(price: String, discount: Int = 0, slashPrice: String, priceGap: String) {
        totalAmount?.apply {
            amountView.text = price
            setTitleText(getString(R.string.text_discount_in_percentage, discount), slashPrice)
            setSubtitleText(context.getString(R.string.text_saving), priceGap)
        }
    }

    private fun updateTotalAmount(singleProductBundleItem: SingleProductBundleItem) {
        singleProductBundleItem.apply {
            viewModel.updateTotalAmount(originalPrice, discountedPrice, quantity)
        }
    }

    private fun updateTotalAmountAtcButtonText(preorderDurationWording: String?) {
        totalAmount?.amountCtaView?.text = if (preorderDurationWording.isNullOrEmpty()) {
            getString(R.string.action_buy_bundle)
        } else {
            getString(R.string.action_preorder)
        }
    }

    private fun showLoadingDialog() {
        context?.let {
            loaderDialog = LoaderDialog(it)
            loaderDialog?.show()
        }
    }

    private fun hideLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
    }

    private fun refreshPage() {
        val productBundleActivity = requireActivity() as ProductBundleActivity
        productBundleActivity.refreshPage()
    }

    private fun atcProductBundle() {
        showLoadingDialog()
        if (userSession.userId.isEmpty()) {
            val intent = RouteManager.getIntent(requireContext(), ApplinkConst.LOGIN)
            startActivityForResult(intent, LOGIN_REQUEST_CODE)
        } else {
            viewModel.validateAndAddToCart(
                pageSource,
                parentProductID,
                selectedBundleId,
                selectedProductId.toString(),
                adapter.getSelectedData()
            )
        }
    }

    companion object {
        // page request codes
        const val LOGIN_REQUEST_CODE = 1122

        // page initializer
        @JvmStatic
        fun newInstance(
            parentProductID: String,
            bundleInfo: List<BundleInfo>,
            selectedBundleId: String = "",
            selectedProductId: Long = 0L,
            emptyVariantProductIds: List<String> = emptyList(),
            pageSource: String = ""
        ) =
            SingleProductBundleFragment(parentProductID, bundleInfo, selectedBundleId,
                selectedProductId, emptyVariantProductIds, pageSource)
    }
}