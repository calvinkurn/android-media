package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.BenefitItem
import kotlinx.android.synthetic.main.item_benefit_package_list.view.*

class BenefitPackageItemAdapter :
    RecyclerView.Adapter<BenefitPackageItemAdapter.ViewHolder>() {

    private val benefitItemList = mutableListOf<BenefitItem>()

    fun setBenefitItemList(data: List<BenefitItem>) {
        if (data.isNullOrEmpty()) return
        benefitItemList.clear()
        benefitItemList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_benefit_package_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = benefitItemList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = benefitItemList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: BenefitItem) {
            with(itemView) {
                icBenefitPackageItem.loadImage(data.imageUrL)
                tvBenefitPackageItem.text = data.title
            }
        }
    }
}