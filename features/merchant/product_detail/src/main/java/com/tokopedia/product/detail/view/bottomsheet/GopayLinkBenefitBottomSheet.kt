package com.tokopedia.product.detail.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetBottomsheet
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class GopayLinkBenefitBottomSheet:BottomSheetUnify() {

    private var parentView: View? = null
    private val childLayoutRes = R.layout.bottom_sheet_pdp_widget_gopay_activation
    private  var  bottomSheetDetail : WidgetBottomsheet? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initArgument()
        this.isFullpage = true
        this.isDragable = true
        this.isHideable = true
    }

    private fun initArgument() {
        arguments?.let {
            bottomSheetDetail = it.getParcelable(BOTTOMSHEET_DETAIl)
        }
    }

    fun showBottomSheet(supportFragmentManager: FragmentManager,bundle: Bundle)
    {
        val gopayLinkBenefitBottomSheet = GopayLinkBenefitBottomSheet()
        gopayLinkBenefitBottomSheet.arguments = bundle
        gopayLinkBenefitBottomSheet.show(supportFragmentManager,"GopayLinkBenefitBottomSheet")


    }
    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }


    companion object{
        const val BOTTOMSHEET_DETAIl = "BottomSheetDetail"
    }
}