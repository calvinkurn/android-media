package com.tokopedia.recharge_credit_card.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_credit_card.R
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBank
import com.tokopedia.recharge_credit_card.getColorFromResources

class CreditCardBankAdapter(private val listBank: List<RechargeCCBank>) : RecyclerView.Adapter<CreditCardBankAdapter.ItemViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_cc_list_bank, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listBank.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindView(listBank[position], position)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var itemBankCC: TextView = view.findViewById(R.id.item_bank_cc)

        fun bindView(rechargeCCBank: RechargeCCBank, position: Int) {
            if (position.rem(2) == 0) {
                itemBankCC.text = rechargeCCBank.name
                itemBankCC.setTextColor(context.resources.getColorFromResources(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                itemBankCC.setBackgroundColor(context.resources.getColorFromResources(context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
            } else {
                itemBankCC.text = rechargeCCBank.name
                itemBankCC.setTextColor(context.resources.getColorFromResources(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                itemBankCC.setBackgroundColor(context.resources.getColorFromResources(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }
        }
    }
}