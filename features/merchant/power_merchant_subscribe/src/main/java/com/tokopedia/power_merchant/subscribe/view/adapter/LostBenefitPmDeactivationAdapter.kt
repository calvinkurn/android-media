package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemLostBenefitPmDeactivationBinding
import com.tokopedia.power_merchant.subscribe.view.model.LostBenefitPmDeactivationUiModel

class LostBenefitPmDeactivationAdapter(
    private val lostBenefitPmDeactivationList: List<LostBenefitPmDeactivationUiModel>
): RecyclerView.Adapter<LostBenefitPmDeactivationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLostBenefitPmDeactivationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = lostBenefitPmDeactivationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (lostBenefitPmDeactivationList.isNotEmpty()) {
            holder.bind(lostBenefitPmDeactivationList[position])
        }
    }

    inner class ViewHolder(private val binding: ItemLostBenefitPmDeactivationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LostBenefitPmDeactivationUiModel) {
            with(binding) {
                tvLostBenefitPmDeactivationTitle.text = item.title
                tvLostBenefitPmDeactivationDesc.text = item.desc
                icLostBenefitPmDeactivation.loadImage(item.imgUrl)
            }
        }
    }
}
