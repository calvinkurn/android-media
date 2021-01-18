package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.CreditCardPdpInfoContent
import com.tokopedia.paylater.presentation.viewholder.CreditCardItemViewHolder

class CreditCardTncAdapter : RecyclerView.Adapter<CreditCardItemViewHolder>() {
    private var pdpInfoContentList: ArrayList<CreditCardPdpInfoContent> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardItemViewHolder.getViewHolder(inflater, parent)

        return viewHolder
    }

    override fun onBindViewHolder(holder: CreditCardItemViewHolder, position: Int) {

    }

    /*override fun getItemViewType(position: Int): Int {
        return when(pdpInfoContentList[position].contentType) {
            DATA_TYPE_BULLET ->
        }
    }*/

    fun setData(pdpInfoContentList: ArrayList<CreditCardPdpInfoContent>) {
        this.pdpInfoContentList = pdpInfoContentList
    }

    override fun getItemCount(): Int {
        return pdpInfoContentList.size
    }

    companion object {
        const val DATA_TYPE_BULLET = "bullet"
        const val DATA_TYPE_MIN_TRANSACTION = "table_min_transaction"
        const val DATA_TYPE_SERVICE_FEE = "table_service_fee"
    }
}