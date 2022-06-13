package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView


import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_autocomplete_fav_num.view.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class NumberListAdapter(private val callback: OnClientNumberClickListener, var clientNumbers: List<TopupBillsSearchNumberDataModel>) : RecyclerView.Adapter<NumberListAdapter.ItemHolder>() {

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

    fun setNumbers(clientNumbers: List<TopupBillsSearchNumberDataModel>) {
        this.clientNumbers = clientNumbers
    }

    fun getNumbers(): List<TopupBillsSearchNumberDataModel> {
        return this.clientNumbers
    }

    interface OnClientNumberClickListener {
        fun onClientNumberClicked(orderClientNumber: TopupBillsSearchNumberDataModel)
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(orderClientNumber: TopupBillsSearchNumberDataModel) {
            with (itemView) {
                text_name.text = orderClientNumber.clientNumber
                if (orderClientNumber.clientName.isNotEmpty()) {
                    text_number.text = orderClientNumber.clientName
                    text_number.show()
                } else {
                    text_number.hide()
                }
                setOnClickListener { callback.onClientNumberClicked(orderClientNumber) }
            }
        }

    }

}
