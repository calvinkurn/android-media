package com.tokopedia.purchase_platform.features.checkout.subfeature.promo_benefit

import android.app.Dialog
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.purchase_platform.R
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 10/03/19.

 */

open class TotalBenefitBottomSheetFragment : BottomSheets() {

    private var bottomsheetView: View? = null

    companion object {
        @JvmStatic
        fun newInstance(): TotalBenefitBottomSheetFragment {
            return TotalBenefitBottomSheetFragment()
        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_total_benefit
    }

    override fun getBaseLayoutResourceId(): Int {
        return R.layout.bottomsheet_round_base_layout
    }

    override fun title(): String {
        return getString(R.string.label_cashback)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog?.run {
            findViewById<FrameLayout>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        }
    }

    override fun initView(view: View) {
        bottomsheetView = view
    }

    override fun configView(parentView: View?) {
        val textViewTitle = parentView?.findViewById<Typography>(R.id.tv_title)
        textViewTitle?.text = title()

        val resetButton = parentView?.findViewById<TextView>(R.id.tv_reset)
        if (resetButton != null) {
            if (!TextUtils.isEmpty(resetButtonTitle())) {
                resetButton.text = resetButtonTitle()
                resetButton.visibility = View.VISIBLE
            }
            resetButton.setOnClickListener { view -> onResetButtonClicked() }
        }

        val layoutTitle = parentView?.findViewById<View>(R.id.layout_title)
        layoutTitle?.setOnClickListener { v -> onCloseButtonClick() }

        val closeButton = parentView?.findViewById<View>(R.id.btn_close)
        closeButton?.setOnClickListener { view -> dismiss() }

        val frameParent = parentView?.findViewById<FrameLayout>(com.tokopedia.design.R.id.bottomsheet_container)
        val subView = View.inflate(context, layoutResourceId, null)
        initView(subView)
        frameParent?.addView(subView)

        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener { onCloseButtonClick() }
    }


}