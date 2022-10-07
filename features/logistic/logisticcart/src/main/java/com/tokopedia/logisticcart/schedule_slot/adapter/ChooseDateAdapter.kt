package com.tokopedia.logisticcart.schedule_slot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.schedule_slot.uimodel.ButtonDateUiModel
import com.tokopedia.logisticcart.schedule_slot.utils.ScheduleSlotListener
import com.tokopedia.unifyprinciples.Typography

class ChooseDateAdapter(private val items: List<ButtonDateUiModel>, private val listener: ScheduleSlotListener) : RecyclerView.Adapter<ChooseDateAdapter.ChooseDateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseDateAdapter.ChooseDateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(com.tokopedia.logisticcart.R.layout.item_date_card, parent, false)
        return ChooseDateViewHolder(view)
    }

    private var selectedItem: ButtonDateUiModel? = null

    override fun onBindViewHolder(holder: ChooseDateAdapter.ChooseDateViewHolder, position: Int) {
        val data = items[position]
        holder.title.text = data.title
        holder.date.text = data.date

        if (data.isEnabled) {
            holder.ticker.visibility = View.GONE
            holder.itemView.setOnClickListener {
                selectedItem = data
                notifyDataSetChanged()
            }
            // todo need to adjust here
            if (selectedItem == data) {
                holder.container.background = ContextCompat.getDrawable(holder.itemView.context, com.tokopedia.logisticcart.R.drawable.bg_card_date_selected)
            } else {
                holder.container.background = ContextCompat.getDrawable(holder.itemView.context, com.tokopedia.logisticcart.R.drawable.bg_card_date)
            }
        } else {
            holder.ticker.visibility = View.VISIBLE
            holder.container.background = ContextCompat.getDrawable(holder.itemView.context, com.tokopedia.logisticcart.R.drawable.bg_card_disabled)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ChooseDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_title_date)
        val date = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_date)
        val ticker = view.findViewById<Typography>(com.tokopedia.logisticcart.R.id.tv_label_out_of_date)
        val container = view.findViewById<ConstraintLayout>(com.tokopedia.logisticcart.R.id.container_date_item)
    }
}
