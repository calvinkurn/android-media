package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import kotlinx.android.synthetic.main.bottomsheet_option_uoh_item.view.*

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetOptionAdapter(private var listener: ActionListener): RecyclerView.Adapter<UohBottomSheetOptionAdapter.ViewHolder>()  {
    var uohItemMapKeyList = HashMap<String, String>()
    var filterType = -1
    var selectedRadio = -1

    interface ActionListener {
        fun onOptionItemClick(option: String, filterType: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_option_uoh_item, parent, false))
    }

    override fun getItemCount(): Int {
        return uohItemMapKeyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrayValues = uohItemMapKeyList.values.toMutableList()
        val arrayKeys = uohItemMapKeyList.keys.toMutableList()
        holder.itemView.label_option.text = arrayValues[position]
        holder.itemView.setOnClickListener {
            selectItem(position, arrayKeys)
        }

        holder.itemView.rb_option.setOnCheckedChangeListener(null)
        holder.itemView.rb_option.isChecked = position == selectedRadio
        holder.itemView.rb_option.setOnCheckedChangeListener { _, _ ->
            selectItem(position, arrayKeys)
        }
    }

    private fun selectItem(position: Int, arrayKeys: MutableList<String>) {
        selectedRadio = position
        listener.onOptionItemClick(arrayKeys[position], filterType)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}