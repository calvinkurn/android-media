package com.tokopedia.checkout.view.feature.bottomsheetpromostacking

import android.app.Dialog
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 10/03/19.

 */

open class TotalBenefitBottomSheetFragment : BottomSheets() {
    private var benefitUiModel = BenefitSummaryInfoUiModel()
    private var benefitAdapter = TotalBenefitBottomSheetAdapter()

    private lateinit var rvTotalBenefit: RecyclerView
    private lateinit var tvTotalLabel: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var cardTicker: CardView
    private var bottomsheetView: View? = null

    companion object {
        val BENEFIT_TYPE_DISCOUNT = "discount"
        val BENEFIT_TYPE_CASHBACK = "cashback"

        @JvmStatic
        fun newInstance(): TotalBenefitBottomSheetFragment {
            return TotalBenefitBottomSheetFragment()
        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    fun setBenefit(benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel) {
        this.benefitUiModel = benefitSummaryInfoUiModel
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_total_benefit
    }

    override fun getBaseLayoutResourceId(): Int {
        return R.layout.bottomsheet_round_base_layout
    }

    override fun title(): String {
        return getString(R.string.totalbenefit_bottomsheet_title)
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        dialog?.run {
            findViewById<FrameLayout>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        }
    }

    override fun initView(view: View) {
        bottomsheetView = view

        benefitAdapter.setList(benefitUiModel.summaries)

        rvTotalBenefit = view.findViewById(R.id.rv_total_benefit)
        rvTotalBenefit.layoutManager = LinearLayoutManager(activity)
        rvTotalBenefit.adapter = benefitAdapter

        tvTotalLabel = view.findViewById(R.id.label_total)
        tvTotalLabel.text = benefitUiModel.finalBenefitText

        tvTotalAmount = view.findViewById(R.id.label_total_amount)
        tvTotalAmount.text = benefitUiModel.finalBenefitAmountStr

        cardTicker = view.findViewById(R.id.cardTicker)
        cardTicker.visibility = View.GONE
        for (summary: SummariesUiModel in benefitUiModel.summaries) {
            if (summary.type.equals(BENEFIT_TYPE_CASHBACK)) {
                cardTicker.visibility = View.VISIBLE
            }
        }
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