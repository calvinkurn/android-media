package com.tokopedia.search.result.product.inspirationcarousel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselChipsItemBinding
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class InspirationCarouselChipsItemViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationCarouselListener,
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_carousel_chips_item
    }
    private var binding: SearchInspirationCarouselChipsItemBinding? by viewBinding()

    fun bind(
        inspirationCarouselAdapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        inspirationCarouselOption: Option,
    ) {
        val binding = binding ?: return
        val chipsUnify = binding.inspirationCarouselChipsUnify
        chipsUnify.chipType = getChipType(inspirationCarouselOption)
        chipsUnify.chipSize = ChipsUnify.SIZE_SMALL
        chipsUnify.chipText = inspirationCarouselOption.title
        chipsUnify.showIcon(inspirationCarouselOption)
        chipsUnify.setOnClickListener {
            onChipsClicked(
                    inspirationCarouselAdapterPosition,
                    inspirationCarouselViewModel,
                    inspirationCarouselOption
            )
        }
    }

    private fun getChipType(inspirationCarouselOption: Option) =
            if (inspirationCarouselOption.isChipsActive) ChipsUnify.TYPE_SELECTED
            else ChipsUnify.TYPE_NORMAL

    private fun ChipsUnify.showIcon(option: Option) {
        chip_image_icon.shouldShowWithAction(option.isShowChipsIcon()) {
            when {
                option.hexColor.isNotEmpty() -> showIcon(option.hexColor)
                option.chipImageUrl.isNotEmpty() -> showImageUrl(option.chipImageUrl)
            }
        }
    }

    private fun ChipsUnify.showIcon(hexColor: String) {
        val colorSampleDrawable = createColorSampleDrawable(itemView.context, hexColor)
        chip_image_icon.setImageDrawable(colorSampleDrawable)
    }

    private fun createColorSampleDrawable(context: Context, colorString: String): GradientDrawable {
        val gradientDrawable = GradientDrawable()

        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        gradientDrawable.setStroke(2, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
        gradientDrawable.setColor(safeParseColor(colorString))

        return gradientDrawable
    }

    private fun safeParseColor(color: String): Int {
        return try {
            Color.parseColor(color)
        }
        catch (throwable: Throwable) {
            Timber.w(throwable)
            0
        }
    }

    private fun ChipsUnify.showImageUrl(url: String) {
        chip_image_icon.setImageUrl(url)
    }

    private fun onChipsClicked(
        inspirationCarouselAdapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        inspirationCarouselOption: Option,
    ) {
        if (inspirationCarouselOption.isChipsActive) return

        inspirationCarouselListener.onInspirationCarouselChipsClicked(
                inspirationCarouselAdapterPosition,
                inspirationCarouselViewModel,
                inspirationCarouselOption,
        )
    }
}
