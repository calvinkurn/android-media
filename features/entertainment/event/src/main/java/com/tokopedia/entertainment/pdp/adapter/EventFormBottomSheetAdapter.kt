package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import kotlinx.android.synthetic.main.item_event_form_bottomsheet_list.view.*

class EventFormBottomSheetAdapter(val formBottomSheetListener: Listener) : RecyclerView.Adapter<EventFormBottomSheetAdapter.EventFormBottomSheetViewHolder>(){

    private var listString = emptyList<String>()

    inner class EventFormBottomSheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(title: String) {
            with(itemView) {
                tg_event_form_bottom_sheet.text = title
                ll_event_form_bottom_sheet.setOnClickListener {
                    formBottomSheetListener.getActiveKeyPosition(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: EventFormBottomSheetViewHolder, position: Int) {
        holder.bind(listString[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventFormBottomSheetViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event_form_bottomsheet_list, parent, false)
        return EventFormBottomSheetViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listString.size
    }

    fun setList(list: List<String>) {
        listString = list
        notifyDataSetChanged()
    }

    interface Listener {
        fun getActiveKeyPosition(position: Int)
    }
}