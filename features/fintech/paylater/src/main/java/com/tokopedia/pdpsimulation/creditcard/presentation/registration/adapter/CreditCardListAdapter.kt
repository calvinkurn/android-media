package com.tokopedia.pdpsimulation.creditcard.presentation.registration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardItem
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.viewholder.CreditCardItemViewHolder
import kotlinx.android.synthetic.main.credit_card_item.view.*

class CreditCardListAdapter(
        private val creditCardList: ArrayList<CreditCardItem>,
        private val creditCardBankName: String?,
        private val clickListener: (String?, String?) -> Unit,
) : RecyclerView.Adapter<CreditCardItemViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardItemViewHolder.getViewHolder(inflater, parent)
        viewHolder.view.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener(creditCardList[position].cardName, creditCardList[position].cardSlug)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CreditCardItemViewHolder, position: Int) {
        holder.bindData(creditCardList[position], creditCardBankName)
        if (position != RecyclerView.NO_POSITION) {
            holder.itemView.rvBenefitsDesc.apply {
                setHasFixedSize(true)
                val model = creditCardList[position]
                val mainBenefitList = model.mainBenefit?.split(";")?.toList()
                if (!model.mainBenefit.isNullOrEmpty() && mainBenefitList?.isNotEmpty() == true) {
                    adapter = CreditCardBenefitsAdapter(mainBenefitList, model.isSpecialOffer)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    setRecycledViewPool(viewPool)
                    (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return creditCardList.size
    }
}