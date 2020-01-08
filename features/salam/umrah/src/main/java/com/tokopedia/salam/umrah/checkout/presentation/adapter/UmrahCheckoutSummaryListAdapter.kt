package com.tokopedia.salam.umrah.checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.CheckoutDetail
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import kotlinx.android.synthetic.main.item_umrah_checkout_list_summary.view.*

/**
 * @author by firman on 19/11/19
 */

class UmrahCheckoutSummaryListAdapter : RecyclerView.Adapter<UmrahCheckoutSummaryListAdapter.UmrahCheckoutSummaryListAdapterViewHolder>() {

    private var listCategories = emptyList<CheckoutDetail>()

    inner class UmrahCheckoutSummaryListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(checkoutDetail: CheckoutDetail) {
            with(itemView) {
                tg_umrah_checkout_summary_pay_title.text = checkoutDetail.header
                tg_umrah_checkout_summary_pay_type.text = checkoutDetail.subHeader
                tg_umrah_checkout_summary_pay_price.text = getRupiahFormat(checkoutDetail.amount)
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahCheckoutSummaryListAdapterViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahCheckoutSummaryListAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_checkout_list_summary, parent, false)
        return UmrahCheckoutSummaryListAdapterViewHolder(itemView)
    }

    fun setList(list: List<CheckoutDetail>) {
        listCategories = list
        notifyDataSetChanged()
    }

}