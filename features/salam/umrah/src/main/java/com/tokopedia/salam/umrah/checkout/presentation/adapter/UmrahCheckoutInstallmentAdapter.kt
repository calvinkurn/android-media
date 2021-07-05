package com.tokopedia.salam.umrah.checkout.presentation.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.Schemes
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter
import kotlinx.android.synthetic.main.item_umrah_checkout_payment_option_radio.view.*

/**
 * @author by firman on 5/11/19
 */

class UmrahCheckoutInstallmentAdapter (context: Context, val umrahInstallmentListener: UmrahInstallmentListener ): RecyclerView.Adapter<UmrahCheckoutInstallmentAdapter.UmrahCheckoutInstallmentViewHolder>(){

    private val umrahCheckoutDueDateAdapter by lazy { UmrahCheckoutDueDateAdapter() }


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listBottomSheet = emptyList<Schemes>()
    private val startCounter = 1
    var lastCheckedPosition = 0


    override fun getItemCount(): Int {
        return listBottomSheet.size
    }

    override fun onBindViewHolder(holder: UmrahCheckoutInstallmentViewHolder, position: Int) {
        val current = listBottomSheet[position]
        holder.bind(current)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahCheckoutInstallmentViewHolder {
        val item = inflater.inflate(R.layout.item_umrah_checkout_payment_option_radio, parent, false)
        return UmrahCheckoutInstallmentViewHolder(item)
    }

    inner class UmrahCheckoutInstallmentViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(data:  Schemes){
            with(itemView) {
                radio_umrah_checkout_bottom_sheet_payment_option.isChecked = lastCheckedPosition == adapterPosition

                val installmentTitle = "${data.title} - ${CurrencyFormatter.getRupiahFormat(data.price)}"

                val spannable = SpannableString(installmentTitle)

                 spannable.setSpan(ForegroundColorSpan(resources.getColor(com.tokopedia.unifyprinciples.R.color.Yellow_Y400)), data.title.length,
                        installmentTitle.length , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                tv_umrah_checkout_bottom_sheet_payment_option.setText(spannable, TextView.BufferType.SPANNABLE)
                rl_umrah_checkout_down_payment.setOnClickListener {
                    umrahInstallmentListener.onInstallment(position)
                    lastCheckedPosition = adapterPosition
                    notifyDataSetChanged()
                    radio_umrah_checkout_bottom_sheet_payment_option.isChecked = !radio_umrah_checkout_bottom_sheet_payment_option.isChecked
                }

                if(data.terms.isNotEmpty()){
                    if(lastCheckedPosition==adapterPosition) {
                        rl_umrah_chekout_payment_option.visibility = View.VISIBLE

                        rv_umrah_checkout_list_due_date.apply {
                            umrahCheckoutDueDateAdapter.counter = startCounter
                            adapter = umrahCheckoutDueDateAdapter
                            layoutManager = LinearLayoutManager(
                                    context,
                                    RecyclerView.VERTICAL, false
                            )

                            umrahCheckoutDueDateAdapter.setList(data.terms)
                        }
                    }else{
                        rl_umrah_chekout_payment_option.visibility = View.GONE
                    }
                }
            }
        }
    }


    fun setList(list: List<Schemes>){
        listBottomSheet = list
        notifyDataSetChanged()
    }

    interface UmrahInstallmentListener {
        fun onInstallment(position: Int)
    }
}