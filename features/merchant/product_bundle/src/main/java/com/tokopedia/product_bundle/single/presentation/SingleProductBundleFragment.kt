package com.tokopedia.product_bundle.single.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dialog.DialogUnify.Companion.HORIZONTAL_ACTION
import com.tokopedia.dialog.DialogUnify.Companion.NO_IMAGE
import com.tokopedia.dialog.DialogUnify.Companion.SINGLE_ACTION
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.EXTRA_BUNDLE_ID
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
import com.tokopedia.product_bundle.single.presentation.model.SingleBundleInfoConstants.BUNDLE_EMPTY_IMAGE_URL
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleDialogModel
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleErrorEnum
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem
import com.tokopedia.product_bundle.single.presentation.viewmodel.SingleProductBundleViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class SingleProductBundleFragment(
    private val parentProductID: String = "",
    private val bundleInfo: List<BundleInfo> = emptyList(),
    private val selectedBundleId: String = "",
    private val selectedProductId: Long = 0L,
    private val emptyVariantProductIds: List<String> = emptyList(),
    private val pageSource: String = ""
) : BaseDaggerFragment(), BundleItemListener {

    @Inject
    lateinit var viewModel: SingleProductBundleViewModel

    private var tvBundleSold: Typography? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var totalAmount: TotalAmount? = null
    private var geBundlePage: GlobalError? = null
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

        setupTotalSold(view)
        setupRecyclerViewItems(view)
        setupTotalAmount(view)
        setupGlobalError(view)

        observeSingleProductBundleUiModel()
        observeTotalAmountUiModel()
        observeAddToCartResult()
        observeToasterError()
        observeDialogError()
        observePageError()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
            val selectedProductVariant = adapter.getSelectedProductVariant() ?: ProductVariant()
            adapter.setSelectedVariant(selectedProductId, viewModel.getVariantText(selectedProductVariant, selectedProductId))
            Toaster.build(requireView(), getString(R.string.single_bundle_success_variant_added), Toaster.LENGTH_LONG).show()
        }
    }

    override fun getScreenName() = SingleProductBundleFragment::class.java.simpleName

    override fun initInjector() {
        DaggerSingleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .build()
                .inject(this)
    }

    override fun onVariantSpinnerClicked(selectedVariant: ProductVariant?) {
        selectedVariant?.let {
            AtcVariantNavigation.showVariantBottomSheet(this, it)
        }
    }

    override fun onBundleItemSelected(
        originalPrice: Double,
        discountedPrice: Double,
        quantity: Int,
        preorderDurationWording: String?
    ) {
        viewModel.updateTotalAmount(originalPrice, discountedPrice, quantity)
        updateTotalPO(preorderDurationWording)
    }

    override fun onDataChanged(
        selectedData: List<SingleProductBundleSelectedItem>,
        selectedProductVariant: ProductVariant?
    ) {
        val selectedProductId = selectedData.firstOrNull {
            it.isSelected
        }?.productId
        if (selectedProductId != null && selectedProductVariant != null) {
            val selectedVariantText = viewModel.getVariantText(selectedProductVariant, selectedProductId.toString())
            adapter.setSelectedVariant(selectedProductId, selectedVariantText)
        }
    }

    private fun observeSingleProductBundleUiModel() {
        viewModel.singleProductBundleUiModel.observe(viewLifecycleOwner, {
            swipeRefreshLayout?.isRefreshing = false
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
            if (pageSource == PAGE_SOURCE_CART) {
                val intent = Intent()
                intent.putExtra(EXTRA_BUNDLE_ID, it.requestParams.bundleId)
                activity?.setResult(Activity.RESULT_OK, intent)
            } else {
                RouteManager.route(context, ApplinkConst.CART)
            }
            activity?.finish()
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
            Toaster.build(requireView(), errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(R.string.action_oke)).show()
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

            DialogUnify(requireContext(), dialogAction, NO_IMAGE).apply {
                setDescription(dialogStruct.message.orEmpty())
                setPrimaryCTAText(primaryText)
                setSecondaryCTAText(getString(R.string.action_back))
                dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
                setSecondaryCTAClickListener { dismiss() }
                setPrimaryCTAClickListener {
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
            swipeRefreshLayout?.isVisible = !isError
            tvBundleSold?.isVisible = !isError
            totalAmount?.isVisible = !isError
        })
    }

    private fun setupTotalSold(view: View) {
        tvBundleSold = view.findViewById(R.id.tv_bundle_sold)
        updateTotalPO(null)
    }

    private fun setupRecyclerViewItems(view: View) {
        val rvBundleItems: RecyclerView = view.findViewById(R.id.rv_bundle_items)
        rvBundleItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBundleItems.adapter = adapter

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout?.isEnabled = false
    }

    private fun setupTotalAmount(view: View) {
        totalAmount = view.findViewById(R.id.total_amount)
        totalAmount?.apply {
            setLabelOrder(TotalAmount.Order.TITLE, TotalAmount.Order.AMOUNT, TotalAmount.Order.SUBTITLE)
            val defaultPrice = context.getString(R.string.single_bundle_default_price)
            updateTotalAmount(
                price = defaultPrice,
                slashPrice = defaultPrice,
                priceGap = defaultPrice
            )
            amountCtaView.setOnClickListener {
                viewModel.validateAndCheckout(parentProductID, adapter.getSelectedData())
            }
        }
    }

    private fun setupGlobalError(view: View) {
        geBundlePage = view.findViewById(R.id.ge_bundle_page)
        geBundlePage?.apply {
            errorIllustration.loadImageWithoutPlaceholder(BUNDLE_EMPTY_IMAGE_URL)
            errorTitle.text = getString(R.string.single_bundle_error_bundle)
            errorDescription.text = getString(R.string.single_bundle_error_bundle_desc)
            errorAction.text = getString(R.string.action_back_to_pdp)
            errorAction.setOnClickListener {
                activity?.finish()
            }
        }
    }

    private fun updateTotalPO(totalPOWording: String?) {
        tvBundleSold?.isVisible = totalPOWording != null
        tvBundleSold?.text = getString(R.string.preorder_prefix, totalPOWording)
    }

    private fun updateTotalAmount(price: String, discount: Int = 0, slashPrice: String, priceGap: String) {
        totalAmount?.apply {
            amountView.text = price
            setTitleText(getString(R.string.text_discount_in_percentage, discount), slashPrice)
            setSubtitleText(context.getString(R.string.text_saving), priceGap)
        }
    }

    private fun refreshPage() {
        val productBundleActivity = requireActivity() as ProductBundleActivity
        productBundleActivity.refreshPage()
    }

    companion object {
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