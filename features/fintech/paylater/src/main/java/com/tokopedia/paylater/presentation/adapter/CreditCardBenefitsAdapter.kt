package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.presentation.viewholder.CreditCardBenefitItemViewHolder
import com.tokopedia.paylater.presentation.viewholder.CreditCardItemViewHolder

class CreditCardBenefitsAdapter(
) : RecyclerView.Adapter<CreditCardBenefitItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardBenefitItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardBenefitItemViewHolder.getViewHolder(inflater, parent)
        viewHolder.view.setOnClickListener {
            //val model = bankList[viewHolder.adapterPosition]
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CreditCardBenefitItemViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 2
    }
}