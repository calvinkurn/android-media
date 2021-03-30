package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.util.RechargeSmartBillsAccordionView.getAccordionwithAction
import com.tokopedia.smartbills.util.RechargeSmartBillsAccordionView.getAccordionwithPaid
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.addAccordionData
import kotlinx.android.synthetic.main.view_smart_bills_item_accordion.view.*

class SmartBillsAccordionViewHolder(view: View,
                                    val checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
                                    val detailListener: SmartBillsViewHolder.DetailListener
                            ): AbstractViewHolder<Section>(view){

    override fun bind(element: Section) {
        with(itemView){
            val view = when(element.type){
                ACTION_TYPE -> getAccordionwithAction(itemView, element, checkableListener, detailListener)
                PAID_TYPE ->  getAccordionwithPaid(itemView, element, checkableListener, detailListener)
                else -> getAccordionwithAction(itemView, element, checkableListener, detailListener)
            }
            accordion_smart_bills.addGroup(addAccordionData(view, element))
        }
    }

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_item_accordion
        const val ACTION_TYPE = 2
        const val PAID_TYPE = 3
    }
}