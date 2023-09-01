package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemValidationBoBinding
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ShipperValidationBoAdapter :
    RecyclerView.Adapter<ShipperValidationBoAdapter.ShipperValidationBoViewHolder>() {

    private val descriptions = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShipperValidationBoViewHolder {
        return ShipperValidationBoViewHolder(
            ItemValidationBoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return descriptions.size
    }

    override fun onBindViewHolder(holder: ShipperValidationBoViewHolder, position: Int) {
        holder.bindData(position + 1, descriptions[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        descriptions.clear()
        descriptions.addAll(data)
        notifyDataSetChanged()
    }

    inner class ShipperValidationBoViewHolder(private val binding: ItemValidationBoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(count: Int, data: String) {
            binding.itemCount.text = count.toString()
            binding.itemValidationDescription.text = HtmlLinkHelper(binding.root.context, data).spannedString
        }
    }
}
