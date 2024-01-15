package com.tokopedia.pms.howtopay.ui.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.databinding.PmsHwpItemInstructionsBinding
import com.tokopedia.unifycomponents.HtmlLinkHelper

class InstructionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PmsHwpItemInstructionsBinding.bind(view)
    fun bind(instruction: String, isNote: Boolean) {
        binding.run {
            if (isNote) {
                tvSerialNumber.gone()
            } else {
                tvSerialNumber.visible()
                tvSerialNumber.text = "${adapterPosition + 1}."
            }
            tvInstruction.text = HtmlLinkHelper(itemView.context, instruction).spannedString ?: ""
        }
    }
}
