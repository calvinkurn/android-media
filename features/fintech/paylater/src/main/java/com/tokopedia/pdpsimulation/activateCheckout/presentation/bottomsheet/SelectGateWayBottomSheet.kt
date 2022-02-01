package com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class SelectGateWayBottomSheet: BottomSheetUnify() {
    private var parentView: View? = null
    private val childLayoutRes = R.layout.activation_gateway_brand


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }
    fun showBottomSheet(supportFragmentManager: FragmentManager)
    {
        show(supportFragmentManager,"Activation Journey Gateway Branf")
    }
    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }
}