package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeTransactionBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageTransactionModel

class DigitalHomePageTransactionViewHolder(itemView : View?, val listener : TransactionListener?) : AbstractViewHolder<DigitalHomePageTransactionModel>(itemView){
    override fun bind(element: DigitalHomePageTransactionModel?) {
        val bind = LayoutDigitalHomeTransactionBinding.bind(itemView)

        bind.favouriteNumber.setOnClickListener {
            listener?.onClickFavNumber()
        }

        bind.orderList.setOnClickListener {
            listener?.onClickOrderList()
        }

        bind.help.setOnClickListener {
            listener?.onClickHelp()
        }

        bind.myBills.setOnClickListener {
            listener?.onClickMyBills()
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_digital_home_transaction
    }

    interface TransactionListener{
        fun onClickFavNumber()
        fun onClickOrderList()
        fun onClickHelp()
        fun onClickMyBills()
    }
}
