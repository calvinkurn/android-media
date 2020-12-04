package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_PRODUCTS
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.ALL_STATUS_TRANSACTION
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.DALAM_PROSES
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.SEMUA_TRANSAKSI_BERLANGSUNG
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts.TRANSAKSI_BERLANGSUNG
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohFilterBundle
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.bottomsheet_option_uoh_item.view.*

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetOptionAdapter(private var listener: ActionListener): RecyclerView.Adapter<UohBottomSheetOptionAdapter.ViewHolder>()  {
    var uohItemMapKeyList = arrayListOf<HashMap<String, String>>()
    var filterBundleList = arrayListOf<UohFilterBundle>()
    var filterType = -1
    var selectedRadio = -1
    var selectedKey = ""
    var isReset = false
    var labelType = -1

    interface ActionListener {
        fun onOptionItemClick(label: String, value: String, filterType: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_option_uoh_item, parent, false))
    }

    override fun getItemCount(): Int {
        return filterBundleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (filterBundleList[position].type == 0) {
            holder.itemView.sublabel_option.gone()
            holder.itemView.divider_sublabel.gone()
            holder.itemView.label_option.visible()
            holder.itemView.divider_label.visible()
            if (filterType == UohConsts.TYPE_FILTER_STATUS && filterBundleList[position].value.isEmpty()) {
                holder.itemView.label_option.text = ALL_STATUS_TRANSACTION
            } else if (filterType == UohConsts.TYPE_FILTER_STATUS && filterBundleList[position].value.equals(TRANSAKSI_BERLANGSUNG, true)) {
                holder.itemView.label_option.text = SEMUA_TRANSAKSI_BERLANGSUNG
            } else {
                holder.itemView.label_option.text = filterBundleList[position].value
            }
        } else if (filterBundleList[position].type == 1) {
            holder.itemView.label_option.gone()
            holder.itemView.divider_label.gone()
            holder.itemView.sublabel_option.visible()
            holder.itemView.divider_sublabel.visible()
            holder.itemView.sublabel_option.text = filterBundleList[position].value
        }

        holder.itemView.setOnClickListener {
            selectItem(position)
        }

        holder.itemView.rb_option.setOnClickListener {
            selectItem(position)
        }

        if (!isReset) {
            if (selectedKey.isEmpty() && selectedRadio == -1) {
                if (filterType == UohConsts.TYPE_FILTER_DATE && filterBundleList[position].key == "0") {
                    holder.itemView.rb_option.isChecked = true
                } else if (filterType == UohConsts.TYPE_FILTER_STATUS && filterBundleList[position].key.isEmpty()) {
                    holder.itemView.rb_option.isChecked = true
                } else if (filterType == UohConsts.TYPE_FILTER_CATEGORY && filterBundleList[position].value.equals(ALL_PRODUCTS, true)) {
                    holder.itemView.rb_option.isChecked = true
                }
            } else {
                if (filterBundleList[position].key.equals(selectedKey, true) && selectedRadio == -1) {
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
                    if (filterBundleList[position].value == ALL_STATUS_TRANSACTION) {
                        holder.itemView.rb_option.isChecked = true
                    }
                } else if (filterType == UohConsts.TYPE_FILTER_CATEGORY) {
                    if (filterBundleList[position].value == ALL_PRODUCTS) {
                        holder.itemView.rb_option.isChecked = true
                    }
                }
            } else {
                if (filterBundleList[position].key.equals(selectedKey, true) && selectedRadio == -1) {
                    holder.itemView.rb_option.isChecked = true
                } else {
                    holder.itemView.rb_option.isChecked = position == selectedRadio
                }
            }
        }
    }

    private fun selectItem(position: Int) {
        isReset = false
        selectedRadio = position
        listener.onOptionItemClick(filterBundleList[position].key, filterBundleList[position].value, filterType)

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}