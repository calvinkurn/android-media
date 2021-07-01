package com.tokopedia.pms.howtopay_native.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder.InstructionViewHolder
import java.util.*

class InstructionAdapter(private val instructions: ArrayList<String>,
                         private val note: String?)
    : RecyclerView.Adapter<InstructionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pms_hwp_item_instructions,
                parent, false)
        return InstructionViewHolder(view)
    }

    override fun getItemCount(): Int {
        note?.let {
            return instructions.size + 1
        } ?: run {
            return instructions.size
        }
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        if (position <= instructions.size - 1) {
            holder.bind(instructions[position], false)
        } else {
            holder.bind(note ?: "", true)
        }
    }
}