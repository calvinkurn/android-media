package com.tokopedia.merchantvoucher.voucherList.bottomsheet

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.merchantvoucher.R

/**
 * Created by fwidjaja on 10/03/19.

 */

open class TotalBenefitBottomSheetFragment : BottomSheetDialogFragment() {
    private var mTitle: String? = null

    companion object {
        @JvmStatic
        fun newInstance(): TotalBenefitBottomSheetFragment {
            return TotalBenefitBottomSheetFragment()
        }
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
        View.inflate(context, R.layout.bottomsheet_total_benefit, container)

        val textViewTitle = view.findViewById<TextView>(com.tokopedia.design.R.id.tv_title)
        textViewTitle.text = mTitle
    }
}