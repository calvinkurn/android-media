package com.tokopedia.shop.flashsale.presentation.creation.information.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemColorBinding
import com.tokopedia.shop.flashsale.common.extension.setBackgroundFromGradient
import com.tokopedia.shop.flashsale.domain.entity.Gradient

class GradientColorAdapter : RecyclerView.Adapter<GradientColorAdapter.GradientViewHolder>() {

    private var gradients: MutableList<Gradient> = mutableListOf()
    private var onGradientClicked: (Gradient) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradientViewHolder {
        val binding =
            SsfsItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GradientViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return gradients.size
    }

    override fun onBindViewHolder(holder: GradientViewHolder, position: Int) {
        holder.bind(gradients[position])
    }

    fun submit(newGradients: List<Gradient>) {
        val callback = DiffUtilCallback(gradients, newGradients)
        val diffResult = DiffUtil.calculateDiff(callback)

        diffResult.dispatchUpdatesTo(this)
        gradients.clear()
        gradients.addAll(newGradients)
    }

    fun getItems(): List<Gradient> {
        return gradients
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

    inner class DiffUtilCallback(
        private val oldList: List<Gradient>,
        private val newList: List<Gradient>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

    }

}