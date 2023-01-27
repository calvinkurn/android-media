package com.tokopedia.cart.view.bottomsheet

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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.databinding.LayoutBottomsheetCartBundlingBinding
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.common.ProductServiceWidgetConstant
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product_bundle.common.data.constant.BundlingPageSource
import com.tokopedia.productbundlewidget.model.GetBundleParamBuilder
import com.tokopedia.productbundlewidget.model.WidgetType
import com.tokopedia.unifycomponents.BottomSheetUnify
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBottomsheetCartBundlingBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        val data = arguments?.getParcelable<CartBundlingBottomSheetData>(KEY_DATA)
        if (data != null) {
            renderContent(data)
        } else {
            dismiss()
        }
    }

    private fun renderContent(data: CartBundlingBottomSheetData) {
        setTitle(data.title)
        binding?.descriptionLabel?.text = MethodChecker.fromHtml(data.description)
        val bundleParam = GetBundleParamBuilder()
            .setBundleId(data.bundleIds)
            .setWidgetType(WidgetType.TYPE_3)
            .setPageSource(BundlingPageSource.CART_PAGE)
            .build()
        if (data.bottomTicker.isNotBlank()) {
            binding?.cardBottomTicker?.apply {
                // Margin adjustment for card view padding in bundle widget recycler view item
                setMargin(
                    marginLeft + BUNDLING_WIDGET_MARGIN_START_ADJUSTMENT.toPx(), marginTop,
                    marginRight, marginBottom
                )
            }
            binding?.bottomTickerLabel?.text = MethodChecker.fromHtml(data.bottomTicker)
            binding?.bottomTickerLabel?.movementMethod = LinkMovementMethod.getInstance()
            binding?.cardBottomTicker?.visible()
        } else {
            binding?.cardBottomTicker?.gone()
        }
        binding?.productBundleWidget?.startActivityResult { intent, requestCode ->
            startActivityForResult(intent, requestCode)
        }
        binding?.productBundleWidget?.getBundleData(bundleParam)
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
}
