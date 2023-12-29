package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.databinding.ItemAutocompleteFavNumBinding
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class NumberListAdapter(private val callback: OnClientNumberClickListener, var clientNumbers: List<TopupBillsSearchNumberDataModel>) : RecyclerView.Adapter<NumberListAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = ItemAutocompleteFavNumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
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

    inner class ItemHolder(
        private val binding: ItemAutocompleteFavNumBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(orderClientNumber: TopupBillsSearchNumberDataModel) {
            with(binding) {
                textName.text = orderClientNumber.clientNumber
                if (orderClientNumber.clientName.isNotEmpty()) {
                    textNumber.text = orderClientNumber.clientName
                    textNumber.show()
                } else {
                    textNumber.hide()
                }
                root.setOnClickListener { callback.onClientNumberClicked(orderClientNumber) }
            }
        }
    }
}
