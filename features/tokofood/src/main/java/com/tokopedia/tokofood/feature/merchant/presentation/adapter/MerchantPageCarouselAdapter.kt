package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.databinding.ItemMerchantInfoCarouselBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.CarouselData
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder.OnCarouselItemClickListener

class MerchantPageCarouselAdapter(
        private val clickListener: OnCarouselItemClickListener
) : RecyclerView.Adapter<MerchantCarouseItemViewHolder>() {

    private var carouselData: List<CarouselData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantCarouseItemViewHolder {
        val binding = ItemMerchantInfoCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MerchantCarouseItemViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MerchantCarouseItemViewHolder, position: Int) {
        holder.bindData(carouselData[position])
    }

    override fun getItemCount(): Int {
        return carouselData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCarouselData(carouselData: List<CarouselData>) {
        this.carouselData = carouselData
        notifyDataSetChanged()
    }
}