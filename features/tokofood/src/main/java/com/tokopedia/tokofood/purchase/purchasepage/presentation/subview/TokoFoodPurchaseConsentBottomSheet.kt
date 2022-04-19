package com.tokopedia.tokofood.purchase.purchasepage.presentation.subview

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.tokofood.databinding.LayoutBottomSheetPurchaseConsentBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokoFoodPurchaseConsentBottomSheet(val listener: Listener) : BottomSheetUnify() {

    private var viewBinding: LayoutBottomSheetPurchaseConsentBinding? = null

    interface Listener {
        fun onContinueButtonClicked()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initializeView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            renderBottomSheet(it)
        }
    }

    private fun initializeView(): LayoutBottomSheetPurchaseConsentBinding {
        val viewBinding = LayoutBottomSheetPurchaseConsentBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initializeBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomSheetPurchaseConsentBinding) {
        showCloseIcon = true
        showHeader = false
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setChild(viewBinding.root)
    }

    private fun renderBottomSheet(viewBinding: LayoutBottomSheetPurchaseConsentBinding) {
        with(viewBinding) {
            checkboxConsentAgreement.setOnCheckedChangeListener { _, isChecked ->
                buttonContinue.isEnabled = isChecked
            }
            checkboxConsentAgreement.isChecked = true
            buttonContinue.setOnClickListener {
                listener.onContinueButtonClicked()
            }
        }
    }
}