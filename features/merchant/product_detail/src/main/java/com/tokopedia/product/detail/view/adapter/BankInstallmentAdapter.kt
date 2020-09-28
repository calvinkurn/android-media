package com.tokopedia.product.detail.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import kotlinx.android.synthetic.main.item_shop_shipment.view.*

class BankInstallmentAdapter(private val isOs: Boolean,
                             private val installments: MutableList<InstallmentBank> = mutableListOf())
    : RecyclerView.Adapter<BankInstallmentAdapter.BankInstallmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankInstallmentViewHolder {
        return BankInstallmentViewHolder(parent.inflateLayout(R.layout.item_shop_shipment))
    }

    override fun getItemCount(): Int = installments.size

    override fun onBindViewHolder(holder: BankInstallmentViewHolder, position: Int) {
        holder.bind(installments[position])
    }

    fun setInstallments(installment: List<InstallmentBank>){
        installments.clear()
        installments.addAll(installment)
        notifyDataSetChanged()
    }

    inner class BankInstallmentViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(installment: InstallmentBank){
            with(itemView){
                courier_item_image.loadImage(installment.icon)
                val item = installment.installmentList.first()
                courier_item_name.text = (if (isOs) item.osMonthlyPrice else item.monthlyPrice).getCurrencyFormatted()
                courier_item_info.text = context.getString(R.string.template_installment,
                        item.minAmount.getCurrencyFormatted())
            }
        }
    }
}