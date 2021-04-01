package com.tokopedia.smartbills.util

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAccordionAdapter
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder
import kotlinx.android.synthetic.main.view_smart_bills_need_action.view.*
import kotlinx.android.synthetic.main.view_smart_bills_need_paid.view.*

object RechargeSmartBillsAccordionView {

    fun getAccordionwithAction(itemView: View, section: Section, checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                               detailListener: SmartBillsViewHolder.DetailListener): View{
        val view = LayoutInflater.from(itemView.context).inflate(R.layout.view_smart_bills_need_action, null)
        view.rv_smart_bills_need_action.showAdapterAccordion(itemView, section, checkableListener, detailListener)
        return view
    }

    fun getAccordionwithPaid(itemView: View, section: Section, checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                             detailListener: SmartBillsViewHolder.DetailListener): View{
        val view = LayoutInflater.from(itemView.context).inflate(R.layout.view_smart_bills_need_paid, null)
        view.rv_smart_bills_need_paid.showAdapterAccordion(itemView, section, checkableListener, detailListener)
        return view
    }

    fun View.disableView() {
        isEnabled = false
        isClickable = false
    }

    fun RecyclerView.addDecorationItemCheckable(){
        addItemDecoration(object : DividerItemDecoration(context) {
            override fun getDimenPaddingLeft(): Int {
                return R.dimen.smart_bills_divider_left_padding
            }
        })
    }

    fun RecyclerView.showAdapterAccordion(itemView: View,section: Section,
                                          checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                                          detailListener: SmartBillsViewHolder.DetailListener){
        val adapterAccordion = SmartBillsAccordionAdapter(checkableListener, detailListener)
        adapterAccordion.addList(section.bills)
        layoutManager = LinearLayoutManager(itemView.context)
        adapter = adapterAccordion
        addDecorationItemCheckable()
    }
}