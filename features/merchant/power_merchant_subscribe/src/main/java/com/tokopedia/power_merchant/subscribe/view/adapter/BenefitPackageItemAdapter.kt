package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemBenefitPackageListBinding
import com.tokopedia.power_merchant.subscribe.view.model.BenefitItem

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
        val binding = ItemBenefitPackageListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = benefitItemList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = benefitItemList.size

    inner class ViewHolder(private val binding: ItemBenefitPackageListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BenefitItem) {
            with(binding) {
                icBenefitPackageItem.loadImage(data.imageUrL)
                tvBenefitPackageItem.text = data.title
                cardItemBenefitPackage.setCardBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                    )
                )
            }
        }
    }
}