package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView


import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_autocomplete_fav_num.view.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class NumberListAdapter(private val callback: OnClientNumberClickListener, var clientNumbers: List<TopupBillsFavNumberItem>) : RecyclerView.Adapter<NumberListAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_autocomplete_fav_num, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(clientNumbers[position])
    }

    override fun getItemCount(): Int {
        return clientNumbers.size
    }

    fun setNumbers(clientNumbers: List<TopupBillsFavNumberItem>) {
        this.clientNumbers = clientNumbers
    }

    interface OnClientNumberClickListener {
        fun onClientNumberClicked(orderClientNumber: TopupBillsFavNumberItem)
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(orderClientNumber: TopupBillsFavNumberItem) {
            with (itemView) {
                text_name.text = orderClientNumber.clientNumber
                if (orderClientNumber.label.isNotEmpty()) {
                    text_number.text = orderClientNumber.label
                    text_number.show()
                } else {
                    text_number.hide()
                }
                setOnClickListener { callback.onClientNumberClicked(orderClientNumber) }
            }
        }

    }

}
