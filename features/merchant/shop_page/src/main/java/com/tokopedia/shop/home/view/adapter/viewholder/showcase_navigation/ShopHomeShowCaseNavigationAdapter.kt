package com.tokopedia.shop.home.view.adapter.viewholder.showcase_navigation

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationBinding
import com.tokopedia.shop.home.view.listener.ShopHomeReimagineShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.ShopHomeShowcaseNavigationBannerWidgetAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ShopHomeShowCaseNavigationAdapter(
    private val appearance: ShopHomeShowcaseNavigationBannerWidgetAppearance,
    private val uiModel: ShowcaseNavigationUiModel,
    private val listener: ShopHomeReimagineShowcaseNavigationListener,
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
        holder.bind(showcases[position], uiModel)
    }

    inner class ShowCaseViewHolder(
        private val binding: ItemShopHomeShowcaseNavigationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            if (appearance is CarouselAppearance) {
                binding.tpgBannerTitle.maxLines = 2
                binding.tpgBannerTitle.minLines = 1
                binding.tpgBannerTitle.ellipsize = TextUtils.TruncateAt.END
                binding.imgBanner.layoutParams.height = SHOWCASE_CAROUSEL_SIZE_HEIGHT.toPx()
                binding.imgBanner.layoutParams.width = SHOWCASE_CAROUSEL_SIZE_WIDTH.toPx()
                binding.imgBanner.requestLayout()
            } else {
                binding.tpgBannerTitle.maxLines = 1
                binding.tpgBannerTitle.minLines = 1
                binding.tpgBannerTitle.ellipsize = TextUtils.TruncateAt.END
                binding.imgBanner.layoutParams.height = SHOWCASE_DEFAULT_SIZE_HEIGHT.toPx()
                binding.imgBanner.layoutParams.width = SHOWCASE_DEFAULT_SIZE_WIDTH.toPx()
                binding.imgBanner.requestLayout()
            }
        }

        fun bind(showcase: Showcase, uiModel: ShowcaseNavigationUiModel) {
            binding.tpgBannerTitle.text = showcase.name
            binding.imgBanner.loadShowcaseImage(showcase.imageUrl, appearance)
            binding.root.setOnClickListener {
                listener.onNavigationBannerShowcaseClick(
                    selectedShowcase = showcase,
                    uiModel = uiModel,
                    tabCount = Int.ONE,
                    tabName = ""
                )
            }
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
            val highEmphasizeColor = if (overrideTheme) {
                colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
            } else {
                ContextCompat.getColor(binding.tpgBannerTitle.context ?: return, unifycomponentsR.color.Unify_NN950)
            }

            binding.apply {
                tpgBannerTitle.setTextColor(highEmphasizeColor)
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
