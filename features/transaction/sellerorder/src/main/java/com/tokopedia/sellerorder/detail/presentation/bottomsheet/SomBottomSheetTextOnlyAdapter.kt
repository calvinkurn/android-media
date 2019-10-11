package com.tokopedia.sellerorder.detail.presentation.bottomsheet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.bottomsheet_text_item.view.*

/**
 * Created by fwidjaja on 2019-10-08.
 */
class SomBottomSheetTextOnlyAdapter(private var listener: ActionListener): RecyclerView.Adapter<SomBottomSheetTextOnlyAdapter.ViewHolder>() {
    var listKey = mutableListOf<HashMap<String, String>>()

    interface ActionListener {
        fun onBottomSheetItemClick(key: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_text_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listKey.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listKey[position].forEach { map ->
            holder.itemView.label.text = map.value
            holder.itemView.setOnClickListener {
                listener.onBottomSheetItemClick(map.key)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}