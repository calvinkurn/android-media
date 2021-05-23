package com.tokopedia.sellerorder.requestpickup.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.confirm_req_pickup_notes_item.view.*

/**
 * Created by fwidjaja on 2019-11-14.
 */
class SomConfirmReqPickupCourierNotesAdapter : RecyclerView.Adapter<SomConfirmReqPickupCourierNotesAdapter.ViewHolder>() {
    var listCourierNotes = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.confirm_req_pickup_notes_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listCourierNotes.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_index.text = "${position + 1}."
        holder.itemView.label_notes.text = listCourierNotes[position]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}