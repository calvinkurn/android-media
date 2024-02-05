package com.tokopedia.cartrevamp.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.cart.databinding.LayoutBottomsheetCartOnboardingBinding
import com.tokopedia.cartrevamp.view.uimodel.CartOnBoardingBottomSheetData
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CartOnBoardingBottomSheet : BottomSheetUnify() {

    init {
        showCloseIcon = false
        showHeader = false
        overlayClickDismiss = false
    }

    companion object {
        private const val TAG = "CartOnBoardingBottomSheet"
        private const val KEY_DATA = "key_data"

        fun newInstance(data: CartOnBoardingBottomSheetData): CartOnBoardingBottomSheet {
            return CartOnBoardingBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_DATA, data)
                }
            }
        }
    }

    private var binding by autoClearedNullable<LayoutBottomsheetCartOnboardingBinding>()
    private var data: CartOnBoardingBottomSheetData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBottomsheetCartOnboardingBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        data = arguments?.getParcelable(KEY_DATA)
        data?.let {
            renderContent(it)
        } ?: dismiss()
    }

    private fun renderContent(data: CartOnBoardingBottomSheetData) {
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
