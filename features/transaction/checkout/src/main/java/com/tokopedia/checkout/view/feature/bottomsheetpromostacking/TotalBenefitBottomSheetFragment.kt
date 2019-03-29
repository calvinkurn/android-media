package com.tokopedia.checkout.view.feature.bottomsheetpromostacking

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel

/**
 * Created by fwidjaja on 10/03/19.

 */

open class TotalBenefitBottomSheetFragment : BottomSheets() {
    private var benefitUiModel = BenefitSummaryInfoUiModel()
    private var benefitAdapter = TotalBenefitBottomSheetAdapter()

    private lateinit var rvTotalBenefit: RecyclerView
    private lateinit var tvTotalLabel: TextView
    private lateinit var tvTotalAmount: TextView
    private var bottomsheetView: View? = null


    companion object {
        @JvmStatic
        fun newInstance(): TotalBenefitBottomSheetFragment {
            return TotalBenefitBottomSheetFragment()
        }
    }

    fun setBenefit(benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel) {
        this.benefitUiModel = benefitSummaryInfoUiModel
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_total_benefit
    }

    override fun title(): String {
        return getString(R.string.totalbenefit_bottomsheet_title)
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
    }
}