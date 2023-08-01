package com.tokopedia.cartrevamp.view.bottomsheet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.cart.databinding.LayoutBottomsheetCartBundlingBinding
import com.tokopedia.cartrevamp.view.bottomsheet.CartBundlingBottomSheetListener
import com.tokopedia.cartrevamp.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.common.ProductServiceWidgetConstant
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product_bundle.common.data.constant.BundlingPageSource
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.GetBundleParamBuilder
import com.tokopedia.productbundlewidget.model.WidgetType
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CartBundlingBottomSheet : BottomSheetUnify() {

    init {
        isDragable = false
        isHideable = true
        showCloseIcon = true
        showHeader = true
    }

    companion object {
        private const val BUNDLING_WIDGET_MARGIN_START_ADJUSTMENT = -6
        private const val TAG = "CartBundlingBottomSheet"
        private const val KEY_DATA = "key_data"

        fun newInstance(data: CartBundlingBottomSheetData): CartBundlingBottomSheet {
            return CartBundlingBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_DATA, data)
                }
            }
        }
    }

    private var binding by autoClearedNullable<LayoutBottomsheetCartBundlingBinding>()
    private var listener: CartBundlingBottomSheetListener? = null
    private var data: CartBundlingBottomSheetData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBottomsheetCartBundlingBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        data = arguments?.getParcelable(KEY_DATA)
        data?.let {
            renderContent(it)
        } ?: dismiss()
    }

    private fun renderContent(data: CartBundlingBottomSheetData) {
        if (data.bundleIds.isEmpty()) {
            dismiss()
        }
        setTitle(data.title)
        context?.let {
            binding?.descriptionLabel?.text = HtmlLinkHelper(it, data.description).spannedString
            binding?.descriptionLabel?.visible()
        }
        val bundleParam = GetBundleParamBuilder()
            .setBundleId(data.bundleIds)
            .setWidgetType(WidgetType.TYPE_3)
            .setPageSource(BundlingPageSource.CART_RECOMMENDATION_PAGE)
            .build()
        if (data.bottomTicker.isNotBlank()) {
            binding?.cardBottomTicker?.apply {
                // Margin adjustment for card view padding in bundle widget recycler view item
                setMargin(
                    marginLeft + BUNDLING_WIDGET_MARGIN_START_ADJUSTMENT.toPx(),
                    marginTop,
                    marginRight,
                    marginBottom
                )
            }
            context?.let {
                val linkHelper = HtmlLinkHelper(it, data.bottomTicker)
                binding?.bottomTickerLabel?.text = linkHelper.spannedString
                binding?.bottomTickerLabel?.movementMethod = LinkMovementMethod.getInstance()
                linkHelper.urlList.forEach { urlLinkManager ->
                    urlLinkManager.setOnClickListener {
                        RouteManager.route(it, urlLinkManager.linkUrl)
                    }
                }
            }
            binding?.cardBottomTicker?.visible()
        } else {
            binding?.cardBottomTicker?.gone()
        }
        binding?.productBundleWidget?.startActivityResult { intent, requestCode ->
            startActivityForResult(intent, requestCode)
        }
        binding?.productBundleWidget?.setListener(object : ProductBundleWidgetListener {
            override fun onMultipleBundleActionButtonClicked(
                selectedBundle: BundleDetailUiModel,
                productDetails: List<BundleProductUiModel>
            ) {
                super.onMultipleBundleActionButtonClicked(selectedBundle, productDetails)
                listener?.onMultipleBundleActionButtonClicked(selectedBundle)
            }

            override fun onSingleBundleActionButtonClicked(
                selectedBundle: BundleDetailUiModel,
                bundleProducts: BundleProductUiModel,
                bundlePosition: Int
            ) {
                super.onSingleBundleActionButtonClicked(
                    selectedBundle,
                    bundleProducts,
                    bundlePosition
                )
                listener?.onSingleBundleActionButtonClicked(selectedBundle)
            }

            override fun impressionMultipleBundle(
                selectedMultipleBundle: BundleDetailUiModel,
                bundlePosition: Int
            ) {
                super.impressionMultipleBundle(selectedMultipleBundle, bundlePosition)
                listener?.impressionMultipleBundle(selectedMultipleBundle)
            }

            override fun impressionSingleBundle(
                selectedBundle: BundleDetailUiModel,
                selectedProduct: BundleProductUiModel,
                bundleName: String,
                bundlePosition: Int
            ) {
                super.impressionSingleBundle(
                    selectedBundle,
                    selectedProduct,
                    bundleName,
                    bundlePosition
                )
                listener?.impressionSingleBundle(selectedBundle)
            }

            override fun onError(it: Throwable) {
                renderError()
            }

            override fun onBundleEmpty() {
                renderError()
            }
        })
        binding?.productBundleWidget?.getBundleData(bundleParam)
        binding?.productBundleWidget?.visible()
        binding?.layoutGlobalError?.gone()
    }

    private fun renderError() {
        binding?.descriptionLabel?.gone()
        binding?.productBundleWidget?.gone()
        binding?.cardBottomTicker?.gone()
        binding?.layoutGlobalError?.setType(GlobalError.SERVER_ERROR)
        binding?.layoutGlobalError?.setActionClickListener {
            data?.let {
                renderContent(it)
            } ?: dismiss()
        }
        binding?.layoutGlobalError?.visible()
    }

    fun setListener(listener: CartBundlingBottomSheetListener) {
        this.listener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProductServiceWidgetConstant.PRODUCT_BUNDLE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            dismiss()
            listener?.onNewBundleProductAddedToCart()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}
