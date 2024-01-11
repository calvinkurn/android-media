package com.tokopedia.cartrevamp.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.cart.databinding.LayoutBottomsheetCartMultipleBoBinding
import com.tokopedia.cartrevamp.view.uimodel.CartMultipleBOBottomSheetData
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CartMultipleBOOnboardingBottomSheet : BottomSheetUnify() {

    init {
        showCloseIcon = false
        showHeader = false
        overlayClickDismiss = false
    }

    companion object {
        private const val TAG = "CartMultipleBOBottomSheet"
        private const val KEY_DATA = "key_data"

        fun newInstance(data: CartMultipleBOBottomSheetData): CartMultipleBOOnboardingBottomSheet {
            return CartMultipleBOOnboardingBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_DATA, data)
                }
            }
        }
    }

    private var binding by autoClearedNullable<LayoutBottomsheetCartMultipleBoBinding>()
    private var data: CartMultipleBOBottomSheetData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBottomsheetCartMultipleBoBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        data = arguments?.getParcelable(KEY_DATA)
        data?.let {
            renderContent(it)
        } ?: dismiss()
    }

    private fun renderContent(data: CartMultipleBOBottomSheetData) {
        binding?.apply {
            iuBebasOngkirInfo.loadImage(data.imageUrl)
            tvTitle.text = data.title
            tvDescription.text = data.description
            btnAction.text = data.buttonText
            btnAction.setOnClickListener {
                dismiss()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        super.show(fragmentManager, TAG)
    }
}
