package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.bottomsheet_option_uoh_item.view.*

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetOptionAdapter(private var listener: ActionListener): RecyclerView.Adapter<UohBottomSheetOptionAdapter.ViewHolder>()  {
    var uohItemMapKeyList = HashMap<String, String>()
    var filterType = -1
    var selectedRadio = -1
    var selectedKey = ""
    var isReset = false

    interface ActionListener {
        fun onOptionItemClick(option: String, label: String, filterType: Int)
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
            selectItem(position, arrayKeys, arrayValues, holder)
        }

        holder.itemView.rb_option.setOnCheckedChangeListener(null)

        if ((filterType == UohConsts.TYPE_FILTER_DATE && position == 2 && selectedRadio == -1 && selectedKey.isEmpty() && !isReset)) {
            holder.itemView.rb_option.isChecked = true
        } else if (arrayKeys[position].equals(selectedKey, true) && selectedRadio == -1 && !isReset) {
            holder.itemView.rb_option.isChecked = true
        } else if (isReset) {
            if (filterType == UohConsts.TYPE_FILTER_DATE) {
                if (position == 0) {
                    holder.itemView.rb_option.isChecked = true
                }
            } else if (filterType == UohConsts.TYPE_FILTER_STATUS) {
                if (arrayValues[position] == UohConsts.ALL_TRANSACTIONS) {
                    holder.itemView.rb_option.isChecked = true
                }
            }
        } else {
            holder.itemView.rb_option.isChecked = position == selectedRadio
        }
        holder.itemView.rb_option.setOnCheckedChangeListener { _, _ ->
            selectItem(position, arrayKeys, arrayValues, holder)
        }
    }

    private fun selectItem(position: Int, arrayKeys: MutableList<String>, arrayValues: MutableList<String>, holder: UohBottomSheetOptionAdapter.ViewHolder) {
        isReset = false
        selectedRadio = position
        listener.onOptionItemClick(arrayKeys[position], arrayValues[position], filterType)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}