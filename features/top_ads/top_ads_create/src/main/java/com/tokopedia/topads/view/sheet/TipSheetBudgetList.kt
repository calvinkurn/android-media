package com.tokopedia.topads.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_fragment_budget_sheet_tip.*
import kotlinx.android.synthetic.main.topads_create_fragment_budget_sheet_tip.view.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class TipSheetBudgetList : BottomSheetUnify() {

    private var dialog: BottomSheetDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_create_fragment_budget_sheet_tip, null)
        setChild(contentView)
        setTitle(getString(R.string.seputar_biaya_iklan))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.imageView2.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_checklist))
        view.imageView3.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_checklist))
        view.imageView4.setImageDrawable(view.context.getResDrawable(R.drawable.topads_create_ic_checklist))
    }

    companion object {

        fun newInstance(): TipSheetBudgetList {
            return TipSheetBudgetList()
        }
    }
}
