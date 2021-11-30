package com.tokopedia.smartbills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder

class SmartBillsAccordionAdapter(
        private val checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
        private val detailListener: SmartBillsViewHolder.DetailListener,
        private val accordionType: Int):
        RecyclerView.Adapter<SmartBillsViewHolder>() {

    var bills = emptyList<RechargeBills>()

    override fun getItemCount(): Int {
        return bills.size
    }

    override fun onBindViewHolder(holder: SmartBillsViewHolder, position: Int) {
        holder.bind(bills[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartBillsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_smart_bills_item,
                parent, false)
        return SmartBillsViewHolder(view, checkableListener, detailListener, accordionType)
    }

    fun addList(bills: List<RechargeBills>){
        this.bills = bills
    }
}