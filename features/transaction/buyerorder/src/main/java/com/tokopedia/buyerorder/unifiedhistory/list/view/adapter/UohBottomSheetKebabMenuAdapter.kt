package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.list.data.Order
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import kotlinx.android.synthetic.main.bottomsheet_kebab_uoh_item.view.*
import kotlinx.android.synthetic.main.bottomsheet_option_uoh_item.view.*

/**
 * Created by fwidjaja on 05/07/20.
 */
class UohBottomSheetKebabMenuAdapter(private var listener: ActionListener): RecyclerView.Adapter<UohBottomSheetKebabMenuAdapter.ViewHolder>()  {
    var uohKebabMenuList = mutableListOf<UohListOrder.Data.UohOrders.Order.Metadata.DotMenu>()
    var _orderData: UohListOrder.Data.UohOrders.Order? = null
    var _orderIndex: Int = -1

    interface ActionListener {
        fun onKebabItemClick(index: Int, orderData: UohListOrder.Data.UohOrders.Order, orderIndex: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_kebab_uoh_item, parent, false))
    }

    override fun getItemCount(): Int {
        return uohKebabMenuList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.label_kebab_option?.text = uohKebabMenuList[position].label
        holder.itemView.rl_kebab_item?.setOnClickListener {
            _orderData?.let { it1 -> listener.onKebabItemClick(index = position, orderData = it1, orderIndex = _orderIndex) }
        }
    }

    fun addList(orderData: UohListOrder.Data.UohOrders.Order) {
        _orderData = orderData
        uohKebabMenuList.clear()
        uohKebabMenuList.addAll(orderData.metadata.dotMenus)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}