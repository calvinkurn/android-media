package com.tokopedia.pms.paymentlist.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.TransactionActionModel
import kotlinx.android.synthetic.main.item_transaction_action_list_item.view.*

class PaymentTransactionActionAdapter(var actionList: ArrayList<TransactionActionModel>, private val invokeCancelTransaction:  () -> Unit): RecyclerView.Adapter<PaymentTransactionActionAdapter.PaymentActionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentActionViewHolder {
        return PaymentActionViewHolder(LayoutInflater.from(parent.context).inflate(LAYOUT_ID, parent, false), invokeCancelTransaction)
    }

    override fun onBindViewHolder(holder: PaymentActionViewHolder, position: Int) {
       //holder.bind(actionList[position])
    }

    override fun getItemCount() = 2

    class PaymentActionViewHolder(val view: View, val invokeCancelTransaction: () -> Unit): RecyclerView.ViewHolder(view) {
        fun bind(model: TransactionActionModel) {
            view.tvActionTitle.text = model.actionString
            view.ivAction.setImage(model.actionIconType)
            view.setOnClickListener { invokeCancelTransaction() }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.item_transaction_action_list_item
    }
}


