package com.tokopedia.product.addedit.detail.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.BottomsheetPriceSuggestionInfoLayoutBinding
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.unifycomponents.BottomSheetUnify

class PriceSuggestionInfoBottomSheet : BottomSheetUnify() {

    companion object {

        private const val TAG = "PriceSuggestionInfoBottomSheet"
        private const val BUNDLE_KEY_IS_EDITING = "IS_EDITING"
        private const val BUNDLE_KEY_PRODUCT_ID = "PRODUCT_ID"
        private const val LEARN_MORE_URL = "https://seller.tokopedia.com/edu/cara-menentukan-harga-produk/"
        private const val FEEDBACK_FORM_URL = "https://docs.google.com/forms/d/e/1FAIpQLSd4b2W65iTR4xP-ylCoieVXpOZLW7AfvgyuYq56eaNCThpwtQ/viewform"

        @JvmStatic
        fun createInstance(isEditing: Boolean, productId: String): PriceSuggestionInfoBottomSheet {
            return PriceSuggestionInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BUNDLE_KEY_IS_EDITING, isEditing)
                    putString(BUNDLE_KEY_PRODUCT_ID, productId)
                }
            }
        }
    }

    private var binding: BottomsheetPriceSuggestionInfoLayoutBinding? = null

    private val isEditing: Boolean by lazy {
        arguments?.getBoolean(BUNDLE_KEY_IS_EDITING) ?: false
    }

    private val productId: String by lazy {
        arguments?.getString(BUNDLE_KEY_PRODUCT_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetPriceSuggestionInfoLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        setTitle(getString(R.string.label_price_recommendation))
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(binding)
    }

    private fun setupViews(binding: BottomsheetPriceSuggestionInfoLayoutBinding?) {
        binding?.tpgCtaLearnMore?.setOnClickListener {
            ProductEditMainTracking.sendClickPriceSuggestionPopUpLearnMoreEvent(isEditing, productId)
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, LEARN_MORE_URL))
        }
        binding?.tpgCtaFeedback?.setOnClickListener {
            ProductEditMainTracking.sendClickPriceSuggestionPopUpGiveFeedbackEvent(isEditing, productId)
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, FEEDBACK_FORM_URL))
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }
}