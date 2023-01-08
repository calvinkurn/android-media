package com.tokopedia.product.addedit.detail.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.BottomsheetServiceFeeLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ServiceFeeBottomSheet : BottomSheetUnify() {

    companion object {

        private const val TAG = "ServiceFeeBottomSheet"

        @JvmStatic
        fun createInstance(): ServiceFeeBottomSheet {
            return ServiceFeeBottomSheet()
        }
    }

    private var binding: BottomsheetServiceFeeLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        setupServiceFeeContentText()
        val viewBinding = BottomsheetServiceFeeLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet() {
        context?.run { setTitle(this.getString(R.string.label_service_fee)) }
        isFullpage = false
        clearContentPadding = true
    }

    private fun setupServiceFeeContentText() {
        Typography.setUnifyTypographyOSO(true)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible && !isAdded) {
            show(fragmentManager, TAG)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
