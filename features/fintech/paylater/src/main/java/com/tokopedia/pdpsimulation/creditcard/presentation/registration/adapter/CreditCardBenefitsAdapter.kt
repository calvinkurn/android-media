package com.tokopedia.pdpsimulation.creditcard.presentation.registration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.viewholder.CreditCardBenefitItemViewHolder

class CreditCardBenefitsAdapter(private val specialBenefitList: List<String>, private val specialOffer: Boolean?) : RecyclerView.Adapter<CreditCardBenefitItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardBenefitItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CreditCardBenefitItemViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CreditCardBenefitItemViewHolder, position: Int) {
        holder.bindData(specialBenefitList[position], specialOffer)
    }

    override fun getItemCount(): Int {
        return specialBenefitList.size
    }
}