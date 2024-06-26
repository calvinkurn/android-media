package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ManualAdsConfirmationCommonSheet : BottomSheetUnify() {

    private var contentView : View ?= null
    var manualClick: (() -> Unit)? = null
    var dismissed: (() -> Unit)? = null

    private var manualAdsBtnClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        contentView = View.inflate(context,
            R.layout.topads_common_autoads_bottom_sheet_layout_confirmation_manual_ads, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        contentView?.findViewById<Typography>(R.id.subtitle)?.text =
            Html.fromHtml(getString(R.string.topads_common_confirmation_switch_manual_description))
        contentView?.findViewById<UnifyButton>(R.id.btn_start_manual_ads)?.setOnClickListener {
            manualAdsBtnClicked = true
            dismiss()
            manualClick?.invoke()
        }
        contentView?.findViewById<UnifyButton>(R.id.cancel_btn_start_manual_ads)?.setOnClickListener { dismiss() }

        setOnDismissListener {
            if (!manualAdsBtnClicked) {
                dismissed?.invoke()
                manualAdsBtnClicked = false
            }
        }
    }

    companion object {

        fun newInstance(): ManualAdsConfirmationCommonSheet {
            return ManualAdsConfirmationCommonSheet()
        }
    }
}
