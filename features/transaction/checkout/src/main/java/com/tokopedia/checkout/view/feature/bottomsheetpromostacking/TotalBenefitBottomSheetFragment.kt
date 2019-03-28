package com.tokopedia.checkout.view.feature.bottomsheetpromostacking

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel
import kotlinx.android.synthetic.main.bottom_sheet_total_benefit.*

/**
 * Created by fwidjaja on 10/03/19.

 */

open class TotalBenefitBottomSheetFragment : BottomSheetDialogFragment() {
    private var mTitle: String? = null
    private var benefitUiModel = BenefitSummaryInfoUiModel()
    private var benefitAdapter = TotalBenefitBottomSheetAdapter()


    companion object {
        @JvmStatic
        fun newInstance(): TotalBenefitBottomSheetFragment {
            return TotalBenefitBottomSheetFragment()
        }
    }

    fun setBenefit(benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel) {
        this.benefitUiModel = benefitSummaryInfoUiModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTitle = activity!!.getString(R.string.totalbenefit_bottomsheet_title)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.widget_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<FrameLayout>(R.id.bottomsheet_container)
        View.inflate(context, R.layout.bottom_sheet_total_benefit, container)

        val textViewTitle = view.findViewById<TextView>(com.tokopedia.design.R.id.tv_title)
        textViewTitle.text = mTitle

        benefitAdapter.setList(benefitUiModel.summaries)
        rv_total_benefit.layoutManager = LinearLayoutManager(activity)
        rv_total_benefit.adapter = benefitAdapter

        val tvTotalLabel: TextView = view.findViewById(R.id.label_total)
        tvTotalLabel.text = benefitUiModel.finalBenefitText

        val tvTotalAmount: TextView = view.findViewById(R.id.label_total_amount)
        tvTotalAmount.text = benefitUiModel.finalBenefitAmount

        val layoutTitle = view.findViewById<View>(com.tokopedia.design.R.id.layout_title)
        layoutTitle.setOnClickListener {
            dismiss()
        }
    }
}