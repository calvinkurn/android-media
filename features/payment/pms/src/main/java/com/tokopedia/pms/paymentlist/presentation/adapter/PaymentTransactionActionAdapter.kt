package com.tokopedia.pms.paymentlist.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.TransactionActionType
import kotlinx.android.synthetic.main.item_transaction_action_list_item.view.*

class PaymentTransactionActionAdapter(
    var actionList: ArrayList<TransactionActionType>,
    private val invokeAction: (TransactionActionType) -> Unit
) : RecyclerView.Adapter<PaymentTransactionActionAdapter.PaymentActionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentActionViewHolder {
        return PaymentActionViewHolder(
            LayoutInflater.from(parent.context).inflate(LAYOUT_ID, parent, false), invokeAction
        )
    }

    override fun onBindViewHolder(holder: PaymentActionViewHolder, position: Int) {
        holder.bind(actionList[position])
    }

    override fun getItemCount() = actionList.size

    class PaymentActionViewHolder(
        val view: View,
        val invokeAction: (TransactionActionType) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        fun bind(model: TransactionActionType) {
            view.tvActionTitle.text = model.actionName
            view.ivAction.setImage(model.actionIcon)
            view.setOnClickListener { invokeAction(model) }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.item_transaction_action_list_item
    }
}


