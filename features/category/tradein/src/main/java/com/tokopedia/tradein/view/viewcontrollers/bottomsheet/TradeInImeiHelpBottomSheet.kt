package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.tradein.R
import com.tokopedia.unifyprinciples.Typography

class TradeInImeiHelpBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): TradeInImeiHelpBottomSheet {
            return TradeInImeiHelpBottomSheet()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tradein_imei_input_help_bottom_sheet, container, false)
        view.findViewById<ImageView>(R.id.close_button).setOnClickListener {
            dialog?.dismiss()
        }
        view.findViewById<Typography>(R.id.point_1_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_1))
        view.findViewById<Typography>(R.id.point_2_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_2))
        view.findViewById<Typography>(R.id.point_3_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_3))
        view.findViewById<Typography>(R.id.point_4_txt).text = MethodChecker.fromHtml(getString(R.string.tradein_imei_txt_4))
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }

}