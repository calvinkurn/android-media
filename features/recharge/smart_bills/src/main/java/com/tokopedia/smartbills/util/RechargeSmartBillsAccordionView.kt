package com.tokopedia.smartbills.util

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.databinding.ViewSmartBillsNeedActionBinding
import com.tokopedia.smartbills.databinding.ViewSmartBillsNeedPaidBinding
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAccordionAdapter
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsAccordionViewHolder
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.ACTION_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.PAID_TYPE

object RechargeSmartBillsAccordionView {

    fun getAccordionwithAction(
        itemView: View,
        section: Section,
        checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
        detailListener: SmartBillsViewHolder.DetailListener,
        refreshAccordion: SmartBillsAccordionViewHolder.SBMAccordionListener,
        accordionType: Int
    ): View {
        val binding = ViewSmartBillsNeedActionBinding.inflate(LayoutInflater.from(itemView.context))
        binding.rvSmartBillsNeedAction.showAdapterAccordion(itemView, section, checkableListener, detailListener, accordionType)
        binding.sbmLocalLoadAction.refreshBtn?.setOnClickListener {
            binding.sbmLocalLoadAction.progressState = !binding.sbmLocalLoadAction.progressState
            refreshAccordion.onRefreshAccordion(section.title)
            binding.sbmLocalLoadAction.progressState = !binding.sbmLocalLoadAction.progressState
        }
        return binding.root
    }

    fun getAccordionwithPaid(
        itemView: View,
        section: Section,
        checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
        detailListener: SmartBillsViewHolder.DetailListener,
        accordionType: Int
    ): View {
        val binding = ViewSmartBillsNeedPaidBinding.inflate(LayoutInflater.from(itemView.context))
        binding.rvSmartBillsNeedPaid.showAdapterAccordion(itemView, section, checkableListener, detailListener, accordionType)
        return binding.root
    }

    fun View.disableView() {
        isEnabled = false
        isClickable = false
    }

    fun RecyclerView.addDecorationItemCheckable(margin: Int) {
        addItemDecoration(object : DividerItemDecoration(context) {
            override fun getDimenPaddingLeft(): Int {
                return margin
            }
        })
    }

    fun RecyclerView.showAdapterAccordion(
        itemView: View,
        section: Section,
        checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
        detailListener: SmartBillsViewHolder.DetailListener,
        accordionType: Int
    ) {
        val adapterAccordion = SmartBillsAccordionAdapter(checkableListener, detailListener, accordionType)
        adapterAccordion.addList(section.bills)
        layoutManager = LinearLayoutManager(itemView.context)
        adapter = adapterAccordion
        if (accordionType == ACTION_TYPE) {
            addDecorationItemCheckable(R.dimen.smart_bills_divider_left_padding)
        } else if (accordionType == PAID_TYPE) {
            addDecorationItemCheckable(com.tokopedia.unifyprinciples.R.dimen.unify_space_64)
        }
    }
}
