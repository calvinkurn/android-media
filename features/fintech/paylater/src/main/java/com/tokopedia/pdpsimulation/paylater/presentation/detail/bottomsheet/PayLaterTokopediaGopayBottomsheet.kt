package com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_card_activation_purchase.*

class PayLaterTokopediaGopayBottomsheet : BottomSheetUnify() {

    private var pdpSimulationCallback: PdpSimulationCallback? = null


    init {
        setShowListener {
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

    private val childLayoutRes = R.layout.paylater_card_activation_purchase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListners()
        sendImpressionAnalytics()
    }

    private fun sendImpressionAnalytics() {

    }

    private fun initListners() {
        btnRegister.setOnClickListener {
            sendClickAnalytics()
        }
    }

    private fun sendClickAnalytics() {

    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        private const val TAG = "PayLaterTokopediaGopayBottomsheet"

        fun show(
            bundle: Bundle,
            pdpSimulationCallback: PdpSimulationCallback,
            childFragmentManager: FragmentManager
        ) {
            val payLaterTokopediaGopayBottomsheet = PayLaterTokopediaGopayBottomsheet().apply {
                arguments = bundle
            }
            payLaterTokopediaGopayBottomsheet.show(childFragmentManager, TAG)
            payLaterTokopediaGopayBottomsheet.pdpSimulationCallback = pdpSimulationCallback
        }
    }

}