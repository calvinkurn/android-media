package com.tokopedia.paylater.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterPaymentMethodAdapter
import com.tokopedia.paylater.presentation.adapter.PayLaterPaymentRegisterAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_register_card_bottomsheet_widget.*
import kotlinx.android.synthetic.main.paylater_register_card_bottomsheet_widget.rvPayLaterRegisterSteps
import kotlinx.android.synthetic.main.paylater_signup_bottomsheet_widget.*

class PayLaterSignupBottomSheet: BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.paylater_signup_bottomsheet_widget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }


    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun initAdapter() {
        rvPayLaterPaymentMethods.adapter = PayLaterPaymentMethodAdapter {
            PayLaterRegisterBottomSheet.show(Bundle(), childFragmentManager)
        }
        rvPayLaterPaymentMethods.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setDefaultParams() {
        setTitle(DIALOG_TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    companion object {
        private const val DIALOG_TITLE = "Mau daftar PayLater apa?"

        private const val TAG = "FT_TAG"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val payLaterSignupBottomSheet =  PayLaterSignupBottomSheet().apply {
                arguments = bundle
            }
            payLaterSignupBottomSheet.show(childFragmentManager, TAG)
        }
    }
}