package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.BankCardListItem
import com.tokopedia.paylater.domain.model.CreditCardItem
import com.tokopedia.paylater.presentation.viewholder.CreditCardRegistrationViewHolder
import kotlinx.android.synthetic.main.base_payment_register_item.view.*

class CreditCardRegistrationAdapter(
        private val bankList: ArrayList<BankCardListItem>,
        val clickListener: (ArrayList<CreditCardItem>, String?, String?) -> Unit,
) : RecyclerView.Adapter<CreditCardRegistrationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardRegistrationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardRegistrationViewHolder.getViewHolder(inflater, parent)
        initListeners(viewHolder)
        return viewHolder
    }

    private fun initListeners(viewHolder: CreditCardRegistrationViewHolder) {
        viewHolder.view.ivActionArrow.setOnClickListener {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
                val bankModel = bankList[viewHolder.adapterPosition]
                val cardList = bankModel.cardList
                cardList?.let { clickListener(it, bankModel.bankName, bankModel.bankSlug) }
            }
        }
    }

    override fun onBindViewHolder(holder: CreditCardRegistrationViewHolder, position: Int) {
        holder.bindData(bankList[position])
    }

    override fun getItemCount(): Int {
        return bankList.size
    }
}