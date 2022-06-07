package com.tokopedia.shop.flashsale.presentation.creation.campaign_information.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemColorBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.extension.toColor
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.seller_shop_flash_sale.R

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

    fun submit(items: List<Gradient>) {
        this.gradients = items.toMutableList()
        notifyItemRangeChanged(Constant.ZERO, items.size)
    }

    inner class GradientViewHolder(
        private val binding: SsfsItemColorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gradient: Gradient) {
            val colors = intArrayOf(gradient.first.toColor(), gradient.first.toColor())
            val drawable =
                GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            drawable.cornerRadius = binding.imgColor.context.resources.getDimension(R.dimen.sfs_corner_radius)
            binding.imgColor.background = drawable

            binding.root.setOnClickListener { onGradientClicked(gradient) }
        }
    }

    fun setOnGradientClicked(onGradientClicked: (Gradient) -> Unit) {
        this.onGradientClicked = onGradientClicked
    }
}