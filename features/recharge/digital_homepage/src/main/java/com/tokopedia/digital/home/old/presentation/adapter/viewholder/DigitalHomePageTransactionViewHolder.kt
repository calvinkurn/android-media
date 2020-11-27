package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageTransactionModel
import kotlinx.android.synthetic.main.layout_digital_home_transaction.view.*

class DigitalHomePageTransactionViewHolder(itemView : View?, val listener : TransactionListener?) : AbstractViewHolder<DigitalHomePageTransactionModel>(itemView){
    override fun bind(element: DigitalHomePageTransactionModel?) {
        itemView.favouriteNumber.setOnClickListener {
            listener?.onClickFavNumber()
        }

        itemView.orderList.setOnClickListener {
            listener?.onClickOrderList()
        }

        itemView.help.setOnClickListener {
            listener?.onClickHelp()
        }

        itemView.myBills.setOnClickListener {
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
