package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R

/**
 * Created by fwidjaja on 11/06/20.
 */

class BuyerRequestCancelBottomsheetAdapter(private var listener: ActionListener): RecyclerView.Adapter<BuyerRequestCancelBottomsheetAdapter.ViewHolder>() {
    var mapKey = HashMap<String, String>()

    interface ActionListener {
        fun onBottomSheetItemClick(key: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_cancel_item, parent, false))
    }

    override fun getItemCount(): Int {
        // return mapKey.size
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrayValues = mapKey.values.toMutableList()
        val arrayKeys = mapKey.keys.toMutableList()
        // holder.itemView.label_reject.text = arrayValues[position]

        holder.itemView.setOnClickListener {
            listener.onBottomSheetItemClick(arrayKeys[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}