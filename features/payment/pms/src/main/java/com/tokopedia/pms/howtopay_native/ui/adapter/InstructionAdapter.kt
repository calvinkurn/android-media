package com.tokopedia.pms.howtopay_native.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder.InstructionViewHolder
import java.util.ArrayList

class InstructionAdapter(val instructions : ArrayList<String>)
    : RecyclerView.Adapter<InstructionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pms_hwp_item_instructions,
                parent, false)
        return InstructionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return instructions.size
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.bind(instructions[position])
    }
}