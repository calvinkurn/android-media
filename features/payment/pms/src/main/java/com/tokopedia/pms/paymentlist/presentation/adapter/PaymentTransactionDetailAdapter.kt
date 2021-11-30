package com.tokopedia.pms.paymentlist.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.VaTransactionItem
import com.tokopedia.pms.paymentlist.presentation.bottomsheet.PaymentTransactionDetailSheet
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.item_transaction_detail_item.view.*

class PaymentTransactionDetailAdapter(
    private var transactionList: ArrayList<VaTransactionItem>,
    private val clickListener: (Int, String?, VaTransactionItem) -> Unit
) :
    RecyclerView.Adapter<PaymentTransactionDetailAdapter.PaymentTransactionDetailViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentTransactionDetailViewHolder {
        return PaymentTransactionDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(LAYOUT_ID, parent, false), clickListener
        )
    }

    override fun onBindViewHolder(holder: PaymentTransactionDetailViewHolder, position: Int) {
        holder.bind(transactionList[position], position)
    }

    override fun getItemCount() = transactionList.size

    class PaymentTransactionDetailViewHolder(
        val view: View,
        val clickListener: (Int, String?, VaTransactionItem) -> Unit
    ) :
        RecyclerView.ViewHolder(view) {
        fun bind(vaTransactionItem: VaTransactionItem, position: Int) {
            view.apply {
                tvTransactionTitle.text = context.getString(
                    R.string.pms_payment_item_heading,
                    position + 1,
                    vaTransactionItem.productName
                )
                tvTransactionAmount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    vaTransactionItem.amount, false
                )
                tvCancelTransaction.setOnClickListener {
                    clickListener(
                        PaymentTransactionDetailSheet.CANCEL_TRANSACTION,
                        context.getString(
                            R.string.pms_payment_item_heading,
                            position + 1,
                            vaTransactionItem.productName
                        ),
                        vaTransactionItem
                    )
                }
                goToHowToPay.setOnClickListener {
                    clickListener(
                        PaymentTransactionDetailSheet.OPEN_DETAIL,
                        null,
                        vaTransactionItem
                    )
                }
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.item_transaction_detail_item
    }
}