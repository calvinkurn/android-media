package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FtCalculationPartnerData

class FtPDPInstallmentCalculationAdapter(var data: ArrayList<FtCalculationPartnerData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.pdp_installment_layout, parent, false)
        return InstallmentItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(vHolder: RecyclerView.ViewHolder, position: Int) {

        val item = data[position]

        if(vHolder is InstallmentItemViewHolder) {
            vHolder.tvInstallmentTitle.text = item.partnerName
        }
    }


    inner class InstallmentItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val tvInstallmentTitle: TextView = view.findViewById(R.id.tv_installment_title)
    }
}