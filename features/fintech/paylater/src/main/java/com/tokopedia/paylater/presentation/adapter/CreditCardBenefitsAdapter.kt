package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.presentation.viewholder.CreditCardBenefitItemViewHolder

class CreditCardBenefitsAdapter(private val specialBenefitList: List<String>) : RecyclerView.Adapter<CreditCardBenefitItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardBenefitItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CreditCardBenefitItemViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CreditCardBenefitItemViewHolder, position: Int) {
        holder.bindData(specialBenefitList[position])
    }

    override fun getItemCount(): Int {
        return specialBenefitList.size
    }
}