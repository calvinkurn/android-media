package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.Space
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecomEntityCardUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomEntityCardBinding
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.modeAware
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class RecomEntityCardViewHolder(
    view: View,
    private val listener: GlobalRecomListener
) : BaseRecommendationViewHolder<RecomEntityCardUiModel>(
    view,
    RecomEntityCardUiModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = recommendation_widget_commonR.layout.item_recom_entity_card

        private const val mingHeightCard = 320f
        internal const val SQUARE_IMAGE_RATIO = "1:1"
    }

    private val binding = ItemRecomEntityCardBinding.bind(itemView)

    init {
        binding.entryPointCard.animateOnPress = CardUnify2.ANIMATE_BOUNCE
    }

    override fun bind(element: RecomEntityCardUiModel) {
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
        setMinHeightEntryPointCard()
        setOnCardImpressionListener(element)
        setOnCardClickListener(element)
    }

    private fun setOnCardClickListener(element: RecomEntityCardUiModel) {
        binding.entryPointCard.setOnClickListener {
            listener.onContentCardClicked(element.toModel(), bindingAdapterPosition)
        }
    }

    private fun setOnCardImpressionListener(element: RecomEntityCardUiModel) {
        binding.entryPointCard.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onContentCardImpressed(element.toModel(), bindingAdapterPosition)
                }
            }
        )
    }

    private fun setBackgroundCardColor(bgColor: List<String>) {
        binding.clEntryPointCard.setGradientBackground(bgColor)
    }

    private fun setProductName(title: String) {
        binding.tvProductName.shouldShowWithAction(title.isNotBlank()) {
            binding.tvProductName.setTextColor(getTextColorLightModeAware())
            binding.tvProductName.text = title
        }
    }

    private fun setProductSubtitle(subTitle: String) {
        binding.tvProductSubtitle.shouldShowWithAction(subTitle.isNotBlank()) {
            binding.tvProductSubtitle.setTextColor(getTextColorLightModeAware())
            binding.tvProductSubtitle.text = subTitle
        }
    }

    private fun getTextColorLightModeAware(): Int {
        val ctx = itemView.context.modeAware(ColorMode.LIGHT_MODE) ?: itemView.context
        return ContextCompat.getColor(ctx, unifyprinciplesR.color.Unify_NN950)
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
        with(binding.entryPointCard) {
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
}
