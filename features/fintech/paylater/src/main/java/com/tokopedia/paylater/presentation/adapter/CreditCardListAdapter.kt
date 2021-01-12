package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.presentation.viewholder.CreditCardItemViewHolder
import kotlinx.android.synthetic.main.credit_card_item.view.*

class CreditCardListAdapter(
        private val bankList: ArrayList<CreditCardBank>,
        private val clickListener : (String) -> Unit
) : RecyclerView.Adapter<CreditCardItemViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = CreditCardItemViewHolder.getViewHolder(inflater, parent)
        viewHolder.itemView.rvBenefitsDesc.apply {
            setHasFixedSize(true)
            adapter = CreditCardBenefitsAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setRecycledViewPool(viewPool)
            (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
        }
        viewHolder.view.setOnClickListener {
            clickListener("url here")
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CreditCardItemViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 4
    }
}