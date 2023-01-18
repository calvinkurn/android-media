package com.tokopedia.cart.view.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.cart.databinding.LayoutBottomsheetCartBundlingCrossSellBinding
import com.tokopedia.cart.view.uimodel.CartBundlingBottomSheetData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
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
        private const val MARGIN_START_ADJUSTMENT_BUNDLE_WIDGET = -6
        private const val TAG = "CartBundlingBottomSheet"
        private const val KEY_DATA = "key_data"

        fun newInstance(
            data: CartBundlingBottomSheetData,
        ): CartBundlingBottomSheet {
            return CartBundlingBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_DATA, data)
                }
            }
        }
    }

    private var binding by autoClearedNullable<LayoutBottomsheetCartBundlingCrossSellBinding>()
    private val data by lazy {
        requireNotNull(arguments?.getParcelable<CartBundlingBottomSheetData>(KEY_DATA))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBottomsheetCartBundlingCrossSellBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        setTitle(data.title)
        binding?.descriptionLabel?.text = MethodChecker.fromHtml(data.description)
        val bundleParam = GetBundleParamBuilder()
            .setBundleId(data.bundleIds)
            .setWidgetType(WidgetType.TYPE_3)
            .build()
        if (data.bottomTicker.isNotBlank()) {
            binding?.cardBottomTicker?.apply {
                // Margin adjustment for card view padding in bundle widget recycler view item
                setMargin(marginLeft + MARGIN_START_ADJUSTMENT_BUNDLE_WIDGET.toPx(), marginTop, marginRight, marginBottom)
            }
            binding?.bottomTickerLabel?.text = MethodChecker.fromHtml(data.bottomTicker)
            binding?.bottomTickerLabel?.movementMethod = LinkMovementMethod.getInstance()
            binding?.cardBottomTicker?.visible()
        } else {
            binding?.cardBottomTicker?.gone()
        }
        binding?.productBundleWidget?.getBundleData(bundleParam)
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }
}
