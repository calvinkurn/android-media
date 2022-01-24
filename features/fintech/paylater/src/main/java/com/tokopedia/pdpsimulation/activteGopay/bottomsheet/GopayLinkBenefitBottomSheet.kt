package com.tokopedia.pdpsimulation.activteGopay.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class GopayLinkBenefitBottomSheet:BottomSheetUnify() {

    private var parentView: View? = null
    private val childLayoutRes = R.layout.pdpwidget_gopay_activation_bottomsheet


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }
    fun showBottomSheet(supportFragmentManager: FragmentManager)
    {
       show(supportFragmentManager,"GoPay_Activation_BottomSheet")
    }
    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }
}