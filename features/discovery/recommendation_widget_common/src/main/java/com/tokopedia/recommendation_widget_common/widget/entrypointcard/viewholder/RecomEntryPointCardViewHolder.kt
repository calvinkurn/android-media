package com.tokopedia.recommendation_widget_common.widget.entrypointcard.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomEntryPointCardBinding
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.model.RecomEntryPointCardUiModel

class RecomEntryPointCardViewHolder(
    view: View,
    private val listener: Listener
) : BaseRecommendationForYouViewHolder<RecomEntryPointCardUiModel>(
    view,
    RecomEntryPointCardUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recom_entry_point_card

        private const val mingHeightCard = 320f
    }

    private val binding = ItemRecomEntryPointCardBinding.bind(itemView)

    private var item: RecomEntryPointCardUiModel? = null
    override fun bind(element: RecomEntryPointCardUiModel) {
        this.item = element
        setBackgroundCardColor(element.backgroundColor)
        setProductName(element.title)
        setProductSubtitle(element.subTitle)
        setProductImageUrl(element.imageUrl)
        setLabelTitle(element.labelState)
        setLabelIcon(element.labelState.iconUrl)
        setOnCardClickListener(element)
        setMinHeightEntryPointCard()
    }

    override fun bindPayload(newItem: RecomEntryPointCardUiModel?) {
        newItem?.run {
            if (item?.backgroundColor != backgroundColor) {
                setBackgroundCardColor(backgroundColor)
            }
            if (item?.title != title) {
                setProductName(title)
            }
            if (item?.subTitle != subTitle) {
                setProductSubtitle(subTitle)
            }
            if (item?.imageUrl != imageUrl) {
                setProductImageUrl(imageUrl)
            }
            if (item?.labelState != labelState) {
                setLabelTitle(labelState)
            }
            if (item?.labelState?.iconUrl != labelState.iconUrl) {
                setLabelIcon(labelState.iconUrl)
            }
            if (item != this) {
                setOnCardClickListener(this)
            }
            setMinHeightEntryPointCard()
        }
    }

    private fun setOnCardClickListener(element: RecomEntryPointCardUiModel) {
        itemView.setOnClickListener {
            listener.onEntryPointCardClickListener(element, bindingAdapterPosition)
        }
    }

    private fun setOnCardImpressionListener(element: RecomEntryPointCardUiModel) {
        itemView.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onEntryPointCardImpressionListener(element, bindingAdapterPosition)
                }
            }
        )
    }

    private fun setBackgroundCardColor(bgColor: List<String>) {
        binding.entryPointCard.setGradientBackground(bgColor)
    }

    private fun setProductName(title: String) {
        binding.tvProductName.shouldShowWithAction(title.isNotBlank()) {
            binding.tvProductName.text = title
        }
    }

    private fun setProductSubtitle(subTitle: String) {
        binding.tvProductSubtitle.shouldShowWithAction(subTitle.isNotBlank()) {
            binding.tvProductSubtitle.text = subTitle
        }
    }

    private fun setProductImageUrl(productImageUrl: String) {
        binding.imgProductCard.loadImage(productImageUrl)
    }

    private fun setLabelTitle(labelState: RecomEntryPointCardUiModel.LabelState) {
        with(binding.tvLabelState) {
            shouldShowWithAction(labelState.title.isNotBlank()) {
                text = labelState.title
                setTextColor(Color.parseColor(labelState.textColor))
            }
        }
    }

    private fun setLabelIcon(labelStateIconUrl: String) {
        with(binding.imgLabelState) {
            shouldShowWithAction(labelStateIconUrl.isNotBlank()) {
                loadImage(labelStateIconUrl)
            }
        }
    }

    private fun setMinHeightEntryPointCard() {
        with(itemView) {
            val minHeightInPx = convertDpToPixel(mingHeightCard, context)
            if (height < minHeightInPx) {
                val widthSpec =
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
                val heightSpec =
                    View.MeasureSpec.makeMeasureSpec(minHeightInPx, View.MeasureSpec.EXACTLY)

                measure(widthSpec, heightSpec)
                layout(Int.ZERO, Int.ZERO, measuredWidth, measuredHeight)
            }
        }
    }

    private fun View.setGradientBackground(colorArray: List<String>) {
        try {
            if (colorArray.size > Int.ONE) {
                val colors = IntArray(colorArray.size)
                for (i in colorArray.indices) {
                    colors[i] = Color.parseColor(colorArray[i])
                }
                val gradient = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
                gradient.cornerRadius = 0f
                this.background = gradient
            } else if (colorArray.size == Int.ONE) {
                this.setBackgroundColor(Color.parseColor(colorArray[0]))
            }
        } catch (e: Exception) {
        }
    }

    interface Listener {
        fun onEntryPointCardImpressionListener(item: RecomEntryPointCardUiModel, position: Int)
        fun onEntryPointCardClickListener(item: RecomEntryPointCardUiModel, position: Int)
    }
}
