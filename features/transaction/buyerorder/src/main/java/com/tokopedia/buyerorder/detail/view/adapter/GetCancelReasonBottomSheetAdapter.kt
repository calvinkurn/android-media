package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import kotlinx.android.synthetic.main.bottomsheet_cancel_item.view.*

/**
 * Created by fwidjaja on 11/06/20.
 */

class GetCancelReasonBottomSheetAdapter(private var listener: ActionListener): RecyclerView.Adapter<GetCancelReasonBottomSheetAdapter.ViewHolder>() {
    var listReason = listOf<String>()

    interface ActionListener {
        fun onReasonClicked(reason: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_cancel_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listReason.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_cancel.text = listReason[position]

        holder.itemView.setOnClickListener {
            listener.onReasonClicked(listReason[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}