package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.bottomsheet_reject_item.view.*

/**
 * Created by fwidjaja on 2019-10-08.
 */
class SomBottomSheetRejectOrderAdapter(private var listener: ActionListener, private var hasRadioBtn: Boolean): RecyclerView.Adapter<SomBottomSheetRejectOrderAdapter.ViewHolder>() {
    var mapKey = HashMap<String, String>()

    interface ActionListener {
        fun onBottomSheetItemClick(key: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_reject_item, parent, false))
    }

    override fun getItemCount(): Int {
        return mapKey.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrayValues = mapKey.values.toMutableList()
        val arrayKeys = mapKey.keys.toMutableList()
        holder.itemView.label_reject.text = arrayValues[position]

        if (!hasRadioBtn) {
            holder.itemView.rb_reject.visibility = View.GONE
        } else {
            holder.itemView.rb_reject.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            listener.onBottomSheetItemClick(arrayKeys[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}