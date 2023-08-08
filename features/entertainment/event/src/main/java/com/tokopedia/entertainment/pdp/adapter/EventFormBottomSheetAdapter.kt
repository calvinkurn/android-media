package com.tokopedia.entertainment.pdp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.databinding.ItemEventFormBottomsheetListBinding

class EventFormBottomSheetAdapter(val formBottomSheetListener: Listener) : RecyclerView.Adapter<EventFormBottomSheetAdapter.EventFormBottomSheetViewHolder>(){

    private var listString = emptyList<String>()

    inner class EventFormBottomSheetViewHolder(val binding: ItemEventFormBottomsheetListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            with(binding) {
                tgEventFormBottomSheet.text = title
                llEventFormBottomSheet.setOnClickListener {
                    formBottomSheetListener.getActiveKeyPosition(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: EventFormBottomSheetViewHolder, position: Int) {
        holder.bind(listString[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventFormBottomSheetViewHolder {
        val binding = ItemEventFormBottomsheetListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventFormBottomSheetViewHolder(binding)
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
