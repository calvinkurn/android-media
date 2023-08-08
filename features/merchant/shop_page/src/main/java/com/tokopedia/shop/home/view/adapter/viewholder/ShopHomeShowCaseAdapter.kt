package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseBinding
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel

class ShopHomeShowCaseAdapter(
    private val listener: ShopHomeShowcaseListener
) : RecyclerView.Adapter<ShopHomeShowCaseAdapter.ShowCaseViewHolder>() {

    private var showcases =
        mutableListOf<ShopHomeShowcaseUiModel.Tab.Showcase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowCaseViewHolder {
        val binding =
            ItemShopHomeShowcaseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ShowCaseViewHolder(binding)
    }

    override fun getItemCount() = showcases.size

    override fun onBindViewHolder(holder: ShowCaseViewHolder, position: Int) {
        holder.bind(showcases[position])
    }

    inner class ShowCaseViewHolder(
        private val binding: ItemShopHomeShowcaseBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(showcase: ShopHomeShowcaseUiModel.Tab.Showcase) {
            binding.tpgBannerTitle.text = showcase.name
            binding.imgBanner.loadImage(showcase.imageUrl)
            binding.root.setOnClickListener { listener.onShowcaseClick(showcase) }
        }
    }

    inner class DiffCallback(
        private val oldItems: List<ShopHomeShowcaseUiModel.Tab.Showcase>,
        private val newItems: List<ShopHomeShowcaseUiModel.Tab.Showcase>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }

    }

    fun submit(newShowcases: List<ShopHomeShowcaseUiModel.Tab.Showcase>) {
        val diffCallback = DiffCallback(this.showcases, newShowcases)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.showcases.clear()

        this.showcases.addAll(newShowcases)
        diffResult.dispatchUpdatesTo(this)
    }

}
