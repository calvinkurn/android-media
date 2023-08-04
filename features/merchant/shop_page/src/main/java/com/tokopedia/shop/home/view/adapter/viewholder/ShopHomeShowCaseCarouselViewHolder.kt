package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseBinding
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseCarouselBannerBinding
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseCarouselViewHolder(itemView: View) :
    AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_carousel_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
    }

    private val viewBinding: ItemShopHomeShowcaseCarouselBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.showcaseHeader.title

        val showcases = model.tabs.getOrNull(0)?.showcases ?: emptyList()
        viewBinding?.iconChevron?.isVisible = showcases.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD

        setupShowCaseRecyclerView(showcases)
    }

    private fun setupShowCaseRecyclerView(
        showcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>
    ) {
        val showCaseAdapter = ShowcaseAdapter()

        val recyclerView = viewBinding?.recyclerView
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = showCaseAdapter
        }

        showCaseAdapter.submit(showcases)
    }

    inner class ShowcaseAdapter : RecyclerView.Adapter<ShowcaseAdapter.ShowCaseViewHolder>() {

        private var showcases =
            mutableListOf<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>()

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
            fun bind(showcase: ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase) {
                binding.tpgBannerTitle.text = showcase.name
                binding.imgBanner.loadImage(showcase.imageUrl)
            }
        }

        inner class DiffCallback(
            private val oldItems: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>,
            private val newItems: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>
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

        fun submit(newShowcases: List<ShopHomeShowcaseUiModel.ShopHomeShowCaseTab.ShopHomeShowcase>) {
            val diffCallback = DiffCallback(this.showcases, newShowcases)
            val diffResult = DiffUtil.calculateDiff(diffCallback)

            this.showcases.clear()

            this.showcases.addAll(newShowcases)
            diffResult.dispatchUpdatesTo(this)
        }

    }
}
