@file:SuppressLint("EntityFieldAnnotation")

package com.tokopedia.recommendation_widget_common.infinite.foryou.content

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomEntityCardBinding
import com.tokopedia.recommendation_widget_common.viewutil.convertDpToPixel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifyprinciples.modeAware
import timber.log.Timber

class ContentCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private val binding = ItemRecomEntityCardBinding.inflate(LayoutInflater.from(context))
    private var listener: Listener? = null

    init {
        addView(binding.root)

        binding.entryPointCard.animateOnPress = CardUnify2.ANIMATE_BOUNCE
    }

    fun setupView(model: ContentCardModel) {
        if (listener == null) Timber.w(Throwable("RecomEntityCardView: You haven't set the listener yet."))

        setBackgroundCardColor(model.backgroundColor)
        setProductName(model.title)
        setProductSubtitle(model.subTitle)
        setProductImageUrl(model.imageUrl)
        setLabelTitle(model.labelState)
        setLabelIcon(model.labelState.iconUrl)
        setupImageRatio(
            binding.clEntryPointCard,
            binding.imgEntryPointCard,
            binding.entryPointCardSpace,
            SQUARE_IMAGE_RATIO
        )
        setMinHeightEntryPointCard()
        setOnCardImpressionListener(model)
        setOnCardClickListener(model)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    private fun setOnCardClickListener(element: ContentCardModel) {
        binding.entryPointCard.setOnClickListener {
            listener?.onContentCardClicked(element)
        }
    }

    private fun setOnCardImpressionListener(element: ContentCardModel) {
        binding.entryPointCard.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener?.onContentCardImpressed(element)
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
        val ctx = context.modeAware(ColorMode.LIGHT_MODE) ?: context
        return ContextCompat.getColor(ctx, unifyprinciplesR.color.Unify_NN950)
    }

    private fun setProductImageUrl(productImageUrl: String) {
        binding.imgEntryPointCard.loadImage(productImageUrl)
    }

    private fun setLabelTitle(labelState: ContentCardModel.LabelState) {
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
            val minHeightInPx = convertDpToPixel(MIN_HEIGHT_CARD, context)
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
        fun onContentCardImpressed(item: ContentCardModel)
        fun onContentCardClicked(item: ContentCardModel)
    }

    companion object {
        private const val MIN_HEIGHT_CARD = 320f
        internal const val SQUARE_IMAGE_RATIO = "1:1"
    }
}
