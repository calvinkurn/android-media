package com.tokopedia.pdpsimulation.paylater.presentation.registration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper

class PayLaterPaymentMethodAdapter(
        private val productDataList: ArrayList<PayLaterItemProductData>,
        private val applicationStatusList: ArrayList<PayLaterApplicationDetail>, private val clickListener: (PayLaterItemProductData, PayLaterApplicationDetail?) -> Unit,
) : RecyclerView.Adapter<PayLaterPaymentMethodViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterPaymentMethodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterPaymentMethodViewHolder.getViewHolder(inflater, parent, clickListener)
    }

    override fun onBindViewHolder(holder: PayLaterPaymentMethodViewHolder, position: Int) {
        holder.bindData(productDataList[position], PayLaterPartnerTypeMapper.getPayLaterApplicationDataForPartner(productDataList[position], applicationStatusList))
    }

    override fun getItemCount(): Int {
        return productDataList.size
    }
}