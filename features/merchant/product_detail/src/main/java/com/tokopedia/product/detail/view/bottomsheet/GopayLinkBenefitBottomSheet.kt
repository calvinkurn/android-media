package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class GopayLinkBenefitBottomSheet:BottomSheetUnify() {

    private var parentView: View? = null
    private val childLayoutRes = R.layout.bottom_sheet_pdp_widget_gopay_activation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        this.isFullpage = true
        this.isDragable = true
        this.isHideable = true
    }
    fun showBottomSheet(supportFragmentManager: FragmentManager):GopayLinkBenefitBottomSheet
    {
        val gopayLinkBenefitBottomSheet = GopayLinkBenefitBottomSheet()
        gopayLinkBenefitBottomSheet.show(supportFragmentManager,"GopayLinkBenefitBottomSheet")
        return gopayLinkBenefitBottomSheet

    }
    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }
}