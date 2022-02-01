package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.TenureSelectListner
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class TenureViewHolder(itemView: View,val tenureSelectListner: TenureSelectListner):RecyclerView.ViewHolder(itemView) {


    fun bindData(tenureDetail: TenureDetail, tenureSelectedModel: TenureSelectedModel) {
        itemView.apply {
            paymentDetailHeader.text = tenureDetail.chip_title
            paymentDetailSubHeader.text = tenureDetail.description
            individualTenureItemContainer.setOnClickListener {
                tenureSelectListner.selectedTenure(tenureSelectedModel)
            }
        }
    }


    companion object {
        private val LAYOUT_ID = R.layout.paylater_activation_individual_tenure

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup,tenureSelectListner: TenureSelectListner) =
            TenureViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false),tenureSelectListner
            )
    }
}