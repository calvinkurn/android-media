package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.CompatPaddingUtils
import com.tokopedia.productcard.reimagine.ProductCardRenderer
import com.tokopedia.productcard.reimagine.ProductCardType.Grid
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ReimagineGridViewStrategy(
    private val productCardView: ViewGroup,
): ProductCardStrategy {

    private val context: Context?
        get() = productCardView.context

    private fun <T: View?> lazyView(@IdRes id: Int) = productCardView.lazyView<T>(id)

    private val renderer = ProductCardRenderer(productCardView, Grid)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)
    private val nameText by lazyView<Typography?>(R.id.productCardName)
    private val videoIdentifier by lazyView<ImageView?>(R.id.productCardVideoIdentifier)
    private val threeDots by lazyView<ImageView?>(R.id.productCardThreeDots)
    private val video: VideoPlayerController by lazyThreadSafetyNone {
        VideoPlayerController(productCardView, R.id.productCardVideo, R.id.productCardImage)
    }
    private val guidelineStart by lazyView<Guideline?>(R.id.productCardGuidelineStartContent)
    private val guidelineEnd by lazyView<Guideline?>(R.id.productCardGuidelineEndContent)
    private val guidelineBottom by lazyView<Guideline?>(R.id.productCardGuidelineBottomContent)

    private var useCompatPadding = false

    override fun additionalMarginStart(): Int = cardContainer?.marginStart ?: 0

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        View.inflate(context, R.layout.product_card_reimagine_grid_layout, productCardView)

        initAttributes(attrs)

        cardContainer?.run {
            elevation = 0f
            radius = context.getPixel(R.dimen.product_card_reimagine_image_radius).toFloat()
            cornerRadius = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }

        nameText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, 12.toPx().toFloat())
    }

    private fun initAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return

        return try {
            useCompatPadding = typedArray.getBoolean(R.styleable.ProductCardView_useCompatPadding, false)
        } catch(_: Throwable) {

        } finally {
            typedArray.recycle()
        }
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        setProductModel(ProductCardModelReimagine.from(productCardModel))
    }

    fun setProductModel(productCardModel: ProductCardModelReimagine) {
        renderer.setProductModel(productCardModel)

        renderVideo(productCardModel)
        renderThreeDots(productCardModel)
        renderContentPadding(productCardModel)

        CompatPaddingUtils(productCardView, useCompatPadding, productCardModel).updatePadding()
    }

    private fun renderVideo(productCardModel: ProductCardModelReimagine) {
        videoIdentifier?.showWithCondition(productCardModel.showVideoIdentifier())
        video.setVideoURL(productCardModel.videoUrl)
    }

    private fun renderThreeDots(productCardModel: ProductCardModelReimagine) {
        threeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            productCardView.post {
                threeDots?.expandTouchArea(
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16)
                )
            }
        }
    }

    private fun renderContentPadding(productCardModel: ProductCardModelReimagine) {
        renderContentPaddingHorizontal(productCardModel)
        renderContentPaddingBottom(productCardModel)
    }

    private fun renderContentPaddingHorizontal(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        val paddingHorizontal =
            if (productCardModel.isInBackground)
                context?.getPixel(R.dimen.product_card_reimagine_content_guideline_padding_in_background)
                    ?: 0
            else 0

        guidelineStart?.setGuidelineBegin(paddingHorizontal)
        guidelineEnd?.setGuidelineEnd(paddingHorizontal)
    }

    private fun renderContentPaddingBottom(productCardModel: ProductCardModelReimagine) {
        val paddingBottomConstraintLayout =
            if (productCardModel.isInBackground) 0
            else context?.getPixel(R.dimen.product_card_reimagine_padding_bottom) ?: 0

        cardConstraintLayout?.let {
            it.setPadding(
                it.paddingStart,
                it.paddingTop,
                it.paddingEnd,
                paddingBottomConstraintLayout
            )
        }

        val paddingBottomGuideline =
            if (productCardModel.isInBackground) context?.getPixel(R.dimen.product_card_reimagine_padding_bottom)
                ?: 0
            else 0

        guidelineBottom?.setGuidelineEnd(paddingBottomGuideline)
    }

    override fun recycle() {
        imageView?.glideClear()
        video.clear()
    }

    override fun setImageProductViewHintListener(
        impressHolder: ImpressHolder,
        viewHintListener: ViewHintListener
    ) {
        imageView?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        cardContainer?.setOnClickListener(l)
    }

    override fun setAddToCartOnClickListener(l: View.OnClickListener?) {
        productCardView
            .findViewById<View?>(R.id.productCardAddToCart)
            ?.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: View.OnLongClickListener?) {
        cardContainer?.setOnLongClickListener(l)
    }

    override fun setThreeDotsOnClickListener(l: View.OnClickListener?) {
        threeDots?.setOnClickListener(l)
    }

    override fun getVideoPlayerController(): VideoPlayerController = video
}
