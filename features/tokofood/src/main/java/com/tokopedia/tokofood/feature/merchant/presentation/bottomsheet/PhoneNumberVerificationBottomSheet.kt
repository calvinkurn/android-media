package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodBottomSheet
import com.tokopedia.tokofood.databinding.BottomsheetPhoneNumberVerificationLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class PhoneNumberVerificationBottomSheet : BottomSheetUnify() {

    interface OnButtonCtaClickListener {
        fun onButtonCtaClickListener(appLink: String)
    }

    companion object {
        private const val BUNDLE_KEY_BOTTOM_SHEET_DATA = "BOTTOM_SHEET_DATA"
        private const val TAG = "PhoneNumberVerificationBottomSheet"

        @JvmStatic
        fun createInstance(bottomSheetData: CartTokoFoodBottomSheet): PhoneNumberVerificationBottomSheet {
            return PhoneNumberVerificationBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_BOTTOM_SHEET_DATA, bottomSheetData)
                }
            }
        }
    }

    private var binding: BottomsheetPhoneNumberVerificationLayoutBinding? = null
    private var clickListener: OnButtonCtaClickListener? = null

    private val bottomSheetData: CartTokoFoodBottomSheet by lazy {
        arguments?.getParcelable(BUNDLE_KEY_BOTTOM_SHEET_DATA) ?: CartTokoFoodBottomSheet()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetPhoneNumberVerificationLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.iuBottomSheetImage?.loadImage(bottomSheetData.imageUrl)
        binding?.tpgBottomSheetTitle?.text = bottomSheetData.title
        binding?.tpgDescription?.text = bottomSheetData.description
        val buttonData = bottomSheetData.buttons.firstOrNull()
        binding?.buttonCta?.text = buttonData?.text
        binding?.buttonCta?.setOnClickListener {
            clickListener?.onButtonCtaClickListener(buttonData?.link.orEmpty())
            dismiss()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun setClickListener(clickListener: OnButtonCtaClickListener) {
        this.clickListener = clickListener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }
}