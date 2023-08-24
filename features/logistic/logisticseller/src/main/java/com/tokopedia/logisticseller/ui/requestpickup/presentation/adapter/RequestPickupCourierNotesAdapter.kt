package com.tokopedia.logisticseller.ui.requestpickup.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.databinding.ConfirmReqPickupNotesItemBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-11-14.
 */
class RequestPickupCourierNotesAdapter :
    RecyclerView.Adapter<RequestPickupCourierNotesAdapter.ViewHolder>() {
    var listCourierNotes = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.confirm_req_pickup_notes_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listCourierNotes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCourierNotes.getOrNull(position).orEmpty())
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding by viewBinding<ConfirmReqPickupNotesItemBinding>()

        @SuppressLint("SetTextI18n")
        fun bind(element: String) {
            binding?.run {
                labelIndex.text = "${adapterPosition + 1}."
                labelNotes.text = element
            }
        }
    }
}
