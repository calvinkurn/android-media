package com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import kotlinx.android.synthetic.main.paylater_action_step_bottomsheet_item.view.*

class PayLaterActionStepViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(partnerStep: String, showDivider: Boolean, position: Int) {
        view.apply {
            if (showDivider) dividerVertical.gone()
            tvDescription.text = MethodChecker.fromHtml(partnerStep)
            tvNumber.text = position.toString()
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_action_step_bottomsheet_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterActionStepViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}