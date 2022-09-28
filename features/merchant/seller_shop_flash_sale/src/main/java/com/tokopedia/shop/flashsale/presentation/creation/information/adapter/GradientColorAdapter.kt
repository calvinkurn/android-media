package com.tokopedia.shop.flashsale.presentation.creation.information.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemColorBinding
import com.tokopedia.shop.flashsale.common.extension.setBackgroundFromGradient
import com.tokopedia.shop.flashsale.domain.entity.Gradient

class GradientColorAdapter : RecyclerView.Adapter<GradientColorAdapter.GradientViewHolder>() {

    private var onGradientClicked: (Gradient) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<Gradient>() {
        override fun areItemsTheSame(oldItem: Gradient, newItem: Gradient): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItem: Gradient, newItem: Gradient): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradientViewHolder {
        val binding =
            SsfsItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GradientViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: GradientViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun setOnGradientClicked(onGradientClicked: (Gradient) -> Unit) {
        this.onGradientClicked = onGradientClicked
    }

    inner class GradientViewHolder(
        private val binding: SsfsItemColorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gradient: Gradient) {
            binding.imgColor.setBackgroundFromGradient(gradient)
            binding.imgCheckmark.isVisible = gradient.isSelected
            binding.root.setOnClickListener { onGradientClicked(gradient) }
        }
    }

    fun submit(newGradients: List<Gradient>) {
        differ.submitList(newGradients)
    }

    fun snapshot(): List<Gradient> {
        return differ.currentList
    }
}