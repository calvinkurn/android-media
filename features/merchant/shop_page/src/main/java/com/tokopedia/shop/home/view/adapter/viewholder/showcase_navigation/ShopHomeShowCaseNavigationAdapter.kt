package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationBinding
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifycomponents.toPx

class ShopHomeShowCaseNavigationAdapter(
    private val appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
    private val listener: ShopHomeShowcaseNavigationListener,
    private val overrideTheme: Boolean,
    private val colorSchema: ShopPageColorSchema
) : RecyclerView.Adapter<ShopHomeShowCaseNavigationAdapter.ShowCaseViewHolder>() {

    companion object {
        private const val SHOWCASE_CAROUSEL_SIZE_HEIGHT = 64
        private const val SHOWCASE_CAROUSEL_SIZE_WIDTH = 64
        private const val SHOWCASE_DEFAULT_SIZE_HEIGHT = 72
        private const val SHOWCASE_DEFAULT_SIZE_WIDTH = 72
    }

    private var showcases = mutableListOf<Showcase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowCaseViewHolder {
        val binding =
            ItemShopHomeShowcaseNavigationBinding.inflate(
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
        private val binding: ItemShopHomeShowcaseNavigationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            if (appearance is CarouselAppearance) {
                binding.imgBanner.layoutParams.height = SHOWCASE_CAROUSEL_SIZE_HEIGHT.toPx()
                binding.imgBanner.layoutParams.width = SHOWCASE_CAROUSEL_SIZE_WIDTH.toPx()
                binding.imgBanner.requestLayout()
            } else {
                binding.imgBanner.layoutParams.height = SHOWCASE_DEFAULT_SIZE_HEIGHT.toPx()
                binding.imgBanner.layoutParams.width = SHOWCASE_DEFAULT_SIZE_WIDTH.toPx()
                binding.imgBanner.requestLayout()
            }
        }

        fun bind(showcase: Showcase) {
            binding.tpgBannerTitle.text = showcase.name
            binding.imgBanner.loadShowcaseImage(showcase.imageUrl, appearance)
            binding.root.setOnClickListener { listener.onNavigationBannerShowcaseClick(showcase) }
            setupColors(overrideTheme, colorSchema)
        }

        private fun ImageUnify.loadShowcaseImage(
            imageUrl: String,
            appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance
        ) {
            type = when (appearance) {
                is TopMainBannerAppearance -> ImageUnify.TYPE_RECT
                is LeftMainBannerAppearance -> ImageUnify.TYPE_RECT
                else -> ImageUnify.TYPE_CIRCLE
            }

            loadImage(imageUrl)
        }

        private fun setupColors(overrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
            val lowEmphasizeColor = if (overrideTheme) {
                colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
            } else {
                ContextCompat.getColor(binding.tpgBannerTitle.context ?: return, unifycomponentsR.color.Unify_NN950)
            }

            binding.apply {
                tpgBannerTitle.setTextColor(lowEmphasizeColor)
            }
        }
    }

    inner class DiffCallback(
        private val oldItems: List<Showcase>,
        private val newItems: List<Showcase>
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

    fun submit(newShowcases: List<Showcase>) {
        val diffCallback = DiffCallback(this.showcases, newShowcases)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.showcases.clear()

        this.showcases.addAll(newShowcases)
        diffResult.dispatchUpdatesTo(this)
    }

}
