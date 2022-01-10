package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class CheckoutDetailBottomSheet:BottomSheetUnify() {

    private var parentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }
    fun show(supportFragmentManager: FragmentManager) {
        show(supportFragmentManager, "Checkout Detail BottomSheet")
    }

    private fun initView() {
        parentView = View.inflate(requireContext(), R.layout.bottom_sheet_checkout_product_detail, null)
        setChild(parentView)
    }




}