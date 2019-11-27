package com.tokopedia.salam.umrah.checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.UmrahTermCondition
import kotlinx.android.synthetic.main.item_umrah_checkout_bottom_sheet_term_condition.view.*

/**
 * @author by firman on 19/11/19
 */

class UmrahCheckoutTermConditionAdapter : RecyclerView.Adapter<UmrahCheckoutTermConditionAdapter.UmrahCheckoutTermConditionAdapterViewHolder>() {

    private var listCategories = emptyList<UmrahTermCondition>()

    inner class UmrahCheckoutTermConditionAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(umrahTermCondition: UmrahTermCondition) {
            with(itemView) {
                tg_umrah_checkout_title_term.text = umrahTermCondition.header
                tg_umrah_checkout_desc_term.text = umrahTermCondition.content
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahCheckoutTermConditionAdapterViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahCheckoutTermConditionAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_checkout_bottom_sheet_term_condition, parent, false)
        return UmrahCheckoutTermConditionAdapterViewHolder(itemView)
    }

    fun setList(list: List<UmrahTermCondition>) {
        listCategories = list
        notifyDataSetChanged()
    }

}