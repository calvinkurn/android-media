package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.setPadding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.CompatPaddingUtils
import com.tokopedia.productcard.reimagine.ProductCardRenderer
import com.tokopedia.productcard.reimagine.ProductCardStockInfo
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.cart.ProductCardCartExtension
import com.tokopedia.productcard.reimagine.cta.ProductCardGenericCtaExtension
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
import com.tokopedia.productcard.R as productcardR
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ReimagineListViewStrategy(
    private val productCardView: ViewGroup,
) : ProductCardStrategy {
    private val context: Context?
        get() = productCardView.context

    private fun <T : View?> lazyView(@IdRes id: Int) = productCardView.lazyView<T>(id)

    private val renderer = ProductCardRenderer(productCardView, ProductCardType.List)
    private val cartExtension = ProductCardCartExtension(productCardView, ProductCardType.List)
    private val stockInfo = ProductCardStockInfo(productCardView)
    private val genericCtaExtension =
        ProductCardGenericCtaExtension(productCardView, ProductCardType.List)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)
    private val nameText by lazyView<Typography?>(R.id.productCardName)
    private val videoIdentifier by lazyView<ImageView?>(R.id.productCardVideoIdentifier)
    private val threeDots by lazyView<ImageView?>(R.id.productCardThreeDots)
    private val video: VideoPlayerController by lazyThreadSafetyNone {
        VideoPlayerController(productCardView, R.id.productCardVideo, R.id.productCardImage)
    }

    override fun getVideoPlayerController(): VideoPlayerController = video

    override fun additionalMarginStart(): Int = cardContainer?.marginStart ?: 0

    private var useCompatPadding = false

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        View.inflate(context, R.layout.product_card_reimagine_list_layout, productCardView)

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
            useCompatPadding =
                typedArray.getBoolean(R.styleable.ProductCardView_useCompatPadding, false)
        } catch (_: Throwable) {

        } finally {
            typedArray.recycle()
        }
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        setProductModel(ProductCardModelReimagine.from(productCardModel))
    }

    fun setProductModel(productCardModel: ProductCardModelReimagine) {
        renderer.setProductModel(productCardModel)
        stockInfo.render(productCardModel)

        genericCtaExtension.render(productCardModel)
        cartExtension.render(productCardModel)

        renderVideo(productCardModel)
        renderThreeDots(productCardModel)
        renderCardPadding(productCardModel)

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

    private fun renderCardPadding(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        val cardPadding =
            if (productCardModel.isInBackground)
                context.getPixel(productcardR.dimen.product_card_reimagine_content_guideline_padding_in_background)
            else
                0

        cardConstraintLayout?.setPadding(cardPadding)
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

    override fun setOnLongClickListener(l: View.OnLongClickListener?) {
        cardContainer?.setOnLongClickListener(l)
    }

    override fun setThreeDotsOnClickListener(l: View.OnClickListener?) {
        threeDots?.setOnClickListener(l)
    }

    override fun setAddToCartOnClickListener(l: View.OnClickListener?) {
        cartExtension.addToCartClickListener = { l?.onClick(it) }
    }

    override fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }


    override fun setGenericCtaButtonOnClickListener(l: View.OnClickListener?) {
        genericCtaExtension.ctaClickListener = { l?.onClick(it) }
    }

    override fun setGenericCtaSecondaryButtonOnClickListener(l: View.OnClickListener?) {
        genericCtaExtension.ctaSecondaryClickListener = { l?.onClick(it) }
    }

    override fun recycle() {
        imageView?.glideClear()
        video.clear()
        cartExtension.clear()
    }
}
