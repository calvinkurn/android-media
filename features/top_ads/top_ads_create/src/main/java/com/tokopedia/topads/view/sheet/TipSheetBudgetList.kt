package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_create_fragment_budget_sheet_tip.view.*

/**
 * Author errysuprayogi on 07,May,2019
 */
class TipSheetBudgetList : BottomSheetUnify() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.topads_create_fragment_budget_sheet_tip, null)
        setChild(contentView)
        setTitle(getString(R.string.tip_biaya_iklan))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.imageView2.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.topads_create_ic_checklist))
        view.imageView3.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.topads_create_ic_checklist))
        view.imageView4.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.topads_create_ic_checklist))
    }

    companion object {

        fun newInstance(): TipSheetBudgetList {
            return TipSheetBudgetList()
        }
    }
}
