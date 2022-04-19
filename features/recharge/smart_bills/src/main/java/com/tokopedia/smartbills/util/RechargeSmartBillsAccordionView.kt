package com.tokopedia.smartbills.util

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAccordionAdapter
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsAccordionViewHolder
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.ACTION_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.PAID_TYPE
import kotlinx.android.synthetic.main.view_smart_bills_need_action.view.*
import kotlinx.android.synthetic.main.view_smart_bills_need_paid.view.*

object RechargeSmartBillsAccordionView {

    fun getAccordionwithAction(itemView: View, section: Section,
                               checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                               detailListener: SmartBillsViewHolder.DetailListener,
                               refreshAccordion: SmartBillsAccordionViewHolder.SBMAccordionListener,
                               accordionType: Int): View{
        val view = LayoutInflater.from(itemView.context).inflate(R.layout.view_smart_bills_need_action, null)
        view.rv_smart_bills_need_action.showAdapterAccordion(itemView, section, checkableListener, detailListener, accordionType)
        view.sbm_local_load_action.refreshBtn?.setOnClickListener {
            view.sbm_local_load_action.progressState = !view.sbm_local_load_action.progressState
            refreshAccordion.onRefreshAccordion(section.title)
            view.sbm_local_load_action.progressState = !view.sbm_local_load_action.progressState
        }
        return view
    }

    fun getAccordionwithPaid(itemView: View, section: Section,
                             checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                             detailListener: SmartBillsViewHolder.DetailListener,
                             accordionType: Int): View{
        val view = LayoutInflater.from(itemView.context).inflate(R.layout.view_smart_bills_need_paid, null)
        view.rv_smart_bills_need_paid.showAdapterAccordion(itemView, section, checkableListener, detailListener, accordionType)
        return view
    }

    fun View.disableView() {
        isEnabled = false
        isClickable = false
    }

    fun RecyclerView.addDecorationItemCheckable(margin: Int){
        addItemDecoration(object : DividerItemDecoration(context) {
            override fun getDimenPaddingLeft(): Int {
                return margin
            }
        })
    }

    fun RecyclerView.showAdapterAccordion(itemView: View,section: Section,
                                          checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                                          detailListener: SmartBillsViewHolder.DetailListener,
                                          accordionType: Int){
        val adapterAccordion = SmartBillsAccordionAdapter(checkableListener, detailListener, accordionType)
        adapterAccordion.addList(section.bills)
        layoutManager = LinearLayoutManager(itemView.context)
        adapter = adapterAccordion
        if(accordionType == ACTION_TYPE)
            addDecorationItemCheckable(R.dimen.smart_bills_divider_left_padding)
        else if (accordionType == PAID_TYPE)
            addDecorationItemCheckable(com.tokopedia.unifyprinciples.R.dimen.unify_space_64)


    }
}