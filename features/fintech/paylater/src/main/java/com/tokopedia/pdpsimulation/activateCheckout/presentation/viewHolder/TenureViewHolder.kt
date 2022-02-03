package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment.ActivationCheckoutFragment
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class TenureViewHolder(itemView: View, val tenureSelectListner: ActivationListner) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(
        tenureDetail: TenureDetail,
        tenureSelectedModel: TenureSelectedModel,
        currentPosition: Int
    ) {

        itemView.apply {
             changeViewColor(tenureSelectListner.isDisable())
            if (tenureDetail.isSelected) {
                individualTenureItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
                radioSelector.isChecked = true
            } else {
                individualTenureItemContainer.cardType = CardUnify.TYPE_BORDER
                radioSelector.isChecked = false
            }
            paymentDetailHeader.text = tenureDetail.chip_title
            paymentDetailSubHeader.text = tenureDetail.description
            individualTenureItemContainer.setOnClickListener {
                tenureSelectListner.selectedTenure(tenureSelectedModel, currentPosition)
            }
        }
    }

    private fun changeViewColor(disable: Boolean) {
        itemView.apply {
            if(disable)
            {
                paymentDetailHeader.isEnabled = false
               paymentDetailSubHeader.isEnabled = false
                radioSelector.isEnabled = false

            }
            else
            {
               paymentDetailHeader.isEnabled = true
                paymentDetailSubHeader.isEnabled = true
                radioSelector.isEnabled = true


            }
        }

    }


    companion object {
        private val LAYOUT_ID = R.layout.paylater_activation_individual_tenure

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            tenureSelectListner: ActivationListner
        ) =
            TenureViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), tenureSelectListner
            )
    }
}