package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.presentation.viewholder.CreditCardRegistrationViewHolder
import kotlinx.android.synthetic.main.base_payment_register_item.view.*

class CreditCardRegistrationAdapter(
        private val bankList: ArrayList<CreditCardBank>,
        val clickListener: (CreditCardBank) -> Unit,
) : RecyclerView.Adapter<CreditCardRegistrationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardRegistrationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardRegistrationViewHolder.getViewHolder(inflater, parent)
        viewHolder.view.ivActionArrow.setOnClickListener {
            val model = bankList[viewHolder.adapterPosition]
            clickListener(model)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CreditCardRegistrationViewHolder, position: Int) {
        holder.bindData(bankList[position])
    }

    override fun getItemCount(): Int {
        return bankList.size
    }
}