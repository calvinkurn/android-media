package com.tokopedia.logisticcart.schedule_slot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.unifyprinciples.Typography
import java.util.Locale

class ChooseDateAdapter(private val items: List<ButtonDateUiModel>, private val listener: ScheduleSlotListener) : RecyclerView.Adapter<ChooseDateAdapter.ChooseDateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseDateAdapter.ChooseDateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(com.tokopedia.checkout.R.layout.item_date_card, parent, false)
        return ChooseDateViewHolder(view)
    }

    private var selectedItem: ButtonDateUiModel? = null

    override fun onBindViewHolder(holder: ChooseDateAdapter.ChooseDateViewHolder, position: Int) {
        val data = items[position]
        holder.title.text = String.format(Locale.getDefault(), "%s\n%s", data.title, data.date)

        if (selectedItem == data) {
            holder.container.background = holder.itemView.context.resources.getDrawable(com.tokopedia.checkout.R.drawable.bg_card_date_selected)
        } else { 
            holder.container.background = holder.itemView.context.resources.getDrawable(com.tokopedia.checkout.R.drawable.bg_card_date)
        }

        holder.itemView.setOnClickListener {
            selectedItem = data
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ChooseDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<Typography>(com.tokopedia.checkout.R.id.tv_date)
        val container = view.findViewById<ConstraintLayout>(com.tokopedia.checkout.R.id.container_date_item)
    }
}
