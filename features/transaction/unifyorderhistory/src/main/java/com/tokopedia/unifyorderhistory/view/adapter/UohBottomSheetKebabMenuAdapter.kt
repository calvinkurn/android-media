package com.tokopedia.unifyorderhistory.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.databinding.BottomsheetKebabUohItemBinding
import com.tokopedia.unifyorderhistory.view.adapter.viewholder.UohBottomSheetKebabMenuViewHolder
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohKebabMenuBottomSheet

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetKebabMenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var uohKebabMenuList = mutableListOf<UohListOrder.UohOrders.Order.Metadata.DotMenu>()
    var _orderData: UohListOrder.UohOrders.Order? = null
    var _orderIndex: Int = -1
    private var actionListener: UohKebabMenuBottomSheet.UohKebabMenuBottomSheetListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = BottomsheetKebabUohItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return UohBottomSheetKebabMenuViewHolder(binding, actionListener)
    }

    override fun getItemCount(): Int {
        return uohKebabMenuList.size
    }

    fun addList(orderData: UohListOrder.UohOrders.Order) {
        _orderData = orderData
        uohKebabMenuList.clear()
        uohKebabMenuList.addAll(orderData.metadata.dotMenus)
        notifyDataSetChanged()
    }

    fun setActionListener(listener: UohKebabMenuBottomSheet.UohKebabMenuBottomSheetListener) {
        this.actionListener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UohBottomSheetKebabMenuViewHolder -> {
                _orderData?.let { holder.bind(uohKebabMenuList[position].label, position, it, _orderIndex) }
            }
        }
    }
}
