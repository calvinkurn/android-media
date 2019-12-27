package com.tokopedia.saldodetails.viewholder

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class SaldoListEmptyViewHolder(itemView: View) : AbstractViewHolder<EmptyModel>(itemView) {

    override fun bind(element: EmptyModel) {

    }

    companion object {

        @JvmField
        val LAYOUT = com.tokopedia.saldodetails.R.layout.saldo_transaction_list_empty
    }
}
