package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseBinding
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx

class ShopHomeShowCaseAdapter(
    private val widgetStyle: ShopHomeShowcaseUiModel.WidgetStyle,
    private val listener: ShopHomeShowcaseListener
) : RecyclerView.Adapter<ShopHomeShowCaseAdapter.ShowCaseViewHolder>() {

    companion object {
        private const val SHOWCASE_CAROUSEL_SIZE_HEIGHT = 64
        private const val SHOWCASE_CAROUSEL_CIRCLE_SIZE_WIDTH = 64
        private const val SHOWCASE_DEFAULT_SIZE_HEIGHT = 72
        private const val SHOWCASE_DEFAULT_CIRCLE_SIZE_WIDTH = 72
    }

    private var showcases = mutableListOf<ShopHomeShowcaseUiModel.Tab.Showcase>()

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

        init {
            if (widgetStyle == ShopHomeShowcaseUiModel.WidgetStyle.CIRCLE) {
                binding.imgBanner.layoutParams.height = SHOWCASE_CAROUSEL_SIZE_HEIGHT.toPx()
                binding.imgBanner.layoutParams.width = SHOWCASE_CAROUSEL_CIRCLE_SIZE_WIDTH.toPx()
                binding.imgBanner.requestLayout()
            } else {
                binding.imgBanner.layoutParams.height = SHOWCASE_DEFAULT_SIZE_HEIGHT.toPx()
                binding.imgBanner.layoutParams.width = SHOWCASE_DEFAULT_CIRCLE_SIZE_WIDTH.toPx()
                binding.imgBanner.requestLayout()
            }
        }

        fun bind(showcase: ShopHomeShowcaseUiModel.Tab.Showcase) {
            binding.tpgBannerTitle.text = showcase.name
            binding.imgBanner.loadShowcaseImage(showcase.imageUrl, widgetStyle)
            binding.root.setOnClickListener { listener.onNavigationBannerShowcaseClick(showcase) }
        }

        private fun ImageUnify.loadShowcaseImage(imageUrl: String, widgetStyle: ShopHomeShowcaseUiModel.WidgetStyle) {
            type = if (widgetStyle == ShopHomeShowcaseUiModel.WidgetStyle.ROUNDED_CORNER) {
                ImageUnify.TYPE_RECT
            } else {
                ImageUnify.TYPE_CIRCLE
            }

            loadImage(imageUrl)
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
