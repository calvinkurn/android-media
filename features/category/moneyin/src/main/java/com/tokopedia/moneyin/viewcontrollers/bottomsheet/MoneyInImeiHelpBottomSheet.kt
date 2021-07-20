package com.tokopedia.moneyin.viewcontrollers.bottomsheet

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.moneyin.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class MoneyInImeiHelpBottomSheet : BottomSheetUnify() {

    init {
        showCloseIcon = true
        showKnob = false
        isDragable = false
        isFullpage = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
    }
    companion object {
        fun newInstance(): MoneyInImeiHelpBottomSheet {
            return MoneyInImeiHelpBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(getContentView())
    }

    private fun getContentView(): View {
        val view = View.inflate(requireContext(), R.layout.tradein_imei_input_help_bottom_sheet, null)
        setTitle(getString(R.string.tradein_check_imei))
        view.findViewById<Typography>(R.id.point_1_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_1))
        view.findViewById<Typography>(R.id.point_2_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_2))
        view.findViewById<Typography>(R.id.point_3_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_3))
        view.findViewById<Typography>(R.id.point_4_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_4))
        return view
    }

}