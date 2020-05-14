package com.tokopedia.salam.umrah.checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.Term
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil
import kotlinx.android.synthetic.main.item_umrah_checkout_payment_option_due_date.view.*

/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutDueDateAdapter : RecyclerView.Adapter<UmrahCheckoutDueDateAdapter.UmrahCheckoutDueDateAdapterViewHolder>() {

    private var listCategories = emptyList<Term>()
    var counter = 1

    inner class UmrahCheckoutDueDateAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(umrahTerm: Term) {
            with(itemView) {
                ll_list_payment_option_installment.show()
                tg_umrah_list_installment_due_date.text = UmrahDateUtil.getTime(UmrahDateUtil.DATE_WITH_YEAR_FULL_MONTH_FORMAT, umrahTerm.dueDate)
                tg_umrah_list_installment_number.text = counter.toString()
                tg_umrah_list_installment_price.text = CurrencyFormatter.getRupiahFormat(umrahTerm.price)
                counter += 1
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahCheckoutDueDateAdapterViewHolder, position: Int) {
            holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahCheckoutDueDateAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_checkout_payment_option_due_date, parent, false)
        return UmrahCheckoutDueDateAdapterViewHolder(itemView)
    }

    fun setList(list: List<Term>) {
        listCategories = list.drop(1)
        notifyDataSetChanged()
    }

}