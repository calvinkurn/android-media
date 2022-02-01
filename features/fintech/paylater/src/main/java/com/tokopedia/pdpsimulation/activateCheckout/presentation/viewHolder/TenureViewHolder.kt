package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class TenureViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    init {
        itemView.apply {
            individualTenureItemContainer.setOnClickListener {

            }
        }
    }

    fun bindData(tenureDetail: TenureDetail) {
        itemView.apply {
            paymentDetailHeader.text = tenureDetail.chip_title
            paymentDetailSubHeader.text = tenureDetail.description
        }
    }


    companion object {
        private val LAYOUT_ID = R.layout.paylater_activation_individual_tenure

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            TenureViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
            )
    }
}