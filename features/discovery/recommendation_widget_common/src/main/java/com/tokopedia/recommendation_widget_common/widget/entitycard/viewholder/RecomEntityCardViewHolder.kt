package com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.Space
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomEntityCardBinding
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.recommendation_widget_common.widget.entitycard.model.RecomEntityCardUiModel

class RecomEntityCardViewHolder(
    view: View,
    private val listener: Listener
) : BaseRecommendationForYouViewHolder<RecomEntityCardUiModel>(
    view,
    RecomEntityCardUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recom_entity_card

        private const val mingHeightCard = 320f
        internal const val SQUARE_IMAGE_RATIO = "1:1"
    }

    private val binding = ItemRecomEntityCardBinding.bind(itemView)

    private var item: RecomEntityCardUiModel? = null
    override fun bind(element: RecomEntityCardUiModel) {
        this.item = element
        setBackgroundCardColor(element.backgroundColor)
        setProductName(element.title)
        setProductSubtitle(element.subTitle)
        setProductImageUrl(element.imageUrl)
        setLabelTitle(element.labelState)
        setLabelIcon(element.labelState.iconUrl)
        setupImageRatio(
            binding.clEntryPointCard,
            binding.imgEntryPointCard,
            binding.entryPointCardSpace,
            SQUARE_IMAGE_RATIO
        )
        setOnCardImpressionListener(element)
        setOnCardClickListener(element)
        setMinHeightEntryPointCard()
    }

    override fun bindPayload(newItem: RecomEntityCardUiModel?) {
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
                setOnCardImpressionListener(this)
                setOnCardClickListener(this)
            }
            setMinHeightEntryPointCard()
        }
    }

    private fun setOnCardClickListener(element: RecomEntityCardUiModel) {
        itemView.setOnClickListener {
            listener.onEntityCardClickListener(element, bindingAdapterPosition)
        }
    }

    private fun setOnCardImpressionListener(element: RecomEntityCardUiModel) {
        itemView.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onEntityCardImpressionListener(element, bindingAdapterPosition)
                }
            }
        )
    }

    private fun setBackgroundCardColor(bgColor: List<String>) {
        binding.clEntryPointCard.setGradientBackground(bgColor)
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
        binding.imgEntryPointCard.loadImage(productImageUrl)
    }

    private fun setLabelTitle(labelState: RecomEntityCardUiModel.LabelState) {
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

    private fun ConstraintLayout.setGradientBackground(colorArray: List<String>) {
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
    }

    private fun setupImageRatio(
        constraintLayoutEntryPointCard: ConstraintLayout?,
        imgEntryPointCard: ImageView?,
        mediaAnchorProduct: Space?,
        ratio: String
    ) {
        constraintLayoutEntryPointCard.applyConstraintSet {
            imgEntryPointCard?.id?.let { id ->
                it.setDimensionRatio(id, ratio)
            }
            mediaAnchorProduct?.id?.let { id ->
                it.setDimensionRatio(id, ratio)
            }
        }
    }

    private fun ConstraintLayout?.applyConstraintSet(configureConstraintSet: (ConstraintSet) -> Unit) {
        this?.let {
            val constraintSet = ConstraintSet()

            constraintSet.clone(it)
            configureConstraintSet(constraintSet)
            constraintSet.applyTo(it)
        }
    }

    interface Listener {
        fun onEntityCardImpressionListener(item: RecomEntityCardUiModel, position: Int)
        fun onEntityCardClickListener(item: RecomEntityCardUiModel, position: Int)
    }
}
