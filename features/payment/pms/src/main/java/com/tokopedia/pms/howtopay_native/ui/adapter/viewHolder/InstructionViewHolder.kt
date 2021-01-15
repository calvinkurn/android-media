package com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.pms_hwp_item_instructions.view.*

class InstructionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val tvSerialNumber: Typography = itemView.tvSerialNumber
    private val tvInstruction: Typography = itemView.tvInstruction
    fun bind(instruction: String, isNote: Boolean) {
        if (isNote)
            tvSerialNumber.gone()
        else {
            tvSerialNumber.visible()
            tvSerialNumber.text = "${adapterPosition + 1}."
        }
        tvInstruction.text = HtmlLinkHelper(itemView.context, instruction).spannedString ?: ""
    }
}