package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_CATEGORIES
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_STATUS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_STATUS_TRANSACTION
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_TRANSACTIONS
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.bottomsheet_option_uoh_item.view.*

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetOptionAdapter(private var listener: ActionListener): RecyclerView.Adapter<UohBottomSheetOptionAdapter.ViewHolder>()  {
    var uohItemMapKeyList = arrayListOf<HashMap<String, String>>()
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
        val valueMap = uohItemMapKeyList[position].values.first().toString()
        val keyMap = uohItemMapKeyList[position].keys.first().toString()

        if (filterType == UohConsts.TYPE_FILTER_STATUS && valueMap.equals(ALL_TRANSACTIONS, true)) {
            holder.itemView.label_option.text = ALL_STATUS_TRANSACTION
        } else {
            holder.itemView.label_option.text = valueMap
        }
        holder.itemView.setOnClickListener {
            selectItem(position, keyMap, valueMap)
        }

        holder.itemView.rb_option.setOnClickListener {
            selectItem(position, keyMap, valueMap)
        }

        // holder.itemView.rb_option.setOnCheckedChangeListener(null)

        if (!isReset) {
            if (selectedKey.isEmpty() && selectedRadio == -1) {
                if (filterType == UohConsts.TYPE_FILTER_DATE && position == 2) {
                    holder.itemView.rb_option.isChecked = true
                } else if (filterType == UohConsts.TYPE_FILTER_STATUS && keyMap.equals(ALL_TRANSACTIONS, true)) {
                    holder.itemView.rb_option.isChecked = true
                } else if (filterType == UohConsts.TYPE_FILTER_CATEGORY && keyMap.equals(ALL_CATEGORIES, true)) {
                    holder.itemView.rb_option.isChecked = true
                }
            } else {
                if (keyMap.equals(selectedKey, true) && selectedRadio == -1) {
                    holder.itemView.rb_option.isChecked = true
                } else {
                    holder.itemView.rb_option.isChecked = position == selectedRadio
                }
            }
        } else {
            if (selectedKey.isEmpty() && selectedRadio == -1) {
                if (filterType == UohConsts.TYPE_FILTER_DATE) {
                    if (position == 0) {
                        holder.itemView.rb_option.isChecked = true
                    }
                } else if (filterType == UohConsts.TYPE_FILTER_STATUS) {
                    if (valueMap == ALL_TRANSACTIONS) {
                        holder.itemView.rb_option.isChecked = true
                    }
                } else if (filterType == UohConsts.TYPE_FILTER_CATEGORY) {
                    if (valueMap == UohConsts.ALL_CATEGORIES_TRANSACTION) {
                        holder.itemView.rb_option.isChecked = true
                    }
                }
            } else {
                if (keyMap.equals(selectedKey, true) && selectedRadio == -1) {
                    holder.itemView.rb_option.isChecked = true
                } else {
                    holder.itemView.rb_option.isChecked = position == selectedRadio
                }
            }
        }

        /*if ((filterType == UohConsts.TYPE_FILTER_DATE && position == 2 && selectedRadio == -1 && selectedKey.isEmpty() && !isReset)) {
            holder.itemView.rb_option.isChecked = true
        } else if (arrayKeys[position].equals(selectedKey, true) && selectedRadio == -1 && !isReset) {
            holder.itemView.rb_option.isChecked = true
        } else if (isReset) {
            if (filterType == UohConsts.TYPE_FILTER_DATE) {
                if (position == 0) {
                    holder.itemView.rb_option.isChecked = true
                }
            } else if (filterType == UohConsts.TYPE_FILTER_STATUS) {
                if (arrayValues[position] == UohConsts.ALL_TRANSACTIONS && selectedRadio == -1) {
                    holder.itemView.rb_option.isChecked = true
                }
            } else if (filterType == UohConsts.TYPE_FILTER_CATEGORY) {
                if (arrayValues[position] == UohConsts.ALL_CATEGORIES_TRANSACTION && selectedRadio == -1) {
                    holder.itemView.rb_option.isChecked = true
                }
            }
        } else if (filterType == UohConsts.TYPE_FILTER_CATEGORY && arrayValues[position] == UohConsts.ALL_CATEGORIES_TRANSACTION && selectedRadio == -1) {
            holder.itemView.rb_option.isChecked = true

        } else {
            holder.itemView.rb_option.isChecked = position == selectedRadio
        }
        holder.itemView.rb_option.setOnCheckedChangeListener { _, _ ->
            selectItem(position, arrayKeys, arrayValues, holder)
        }*/
    }

    private fun selectItem(position: Int, keyMap: String, valueMap: String) {
        isReset = false
        selectedRadio = position
        listener.onOptionItemClick(keyMap, valueMap, filterType)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}