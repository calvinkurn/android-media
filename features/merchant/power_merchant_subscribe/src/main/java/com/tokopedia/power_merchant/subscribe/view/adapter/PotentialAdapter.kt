package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmPotentialBinding
import com.tokopedia.power_merchant.subscribe.view.model.PotentialItemUiModel

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PotentialAdapter(
    private val items: List<PotentialItemUiModel>,
    private val itemWidth: Int
) : RecyclerView.Adapter<PotentialAdapter.PotentialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PotentialViewHolder {
        val binding = ItemPmPotentialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PotentialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PotentialViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class PotentialViewHolder(private val binding: ItemPmPotentialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PotentialItemUiModel) {
            with(binding) {
                itemPmPotential.layoutParams.width = itemWidth
                itemPmPotential.requestLayout()
                imgPmPotentialItem.loadImage(item.imgUrl)
                tvPmPotentialItemDescription.text = item.description.parseAsHtml()
            }
        }
    }
}