package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
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
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.productcard.reimagine.ProductCardModel.Companion as ProductCardModelReimagine
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ReimagineListViewStrategy(
    private val productCardView: ViewGroup,
): ProductCardStrategy {
    private val context: Context?
        get() = productCardView.context

    private fun <T: View?> lazyView(@IdRes id: Int) = productCardView.lazyView<T>(id)

    private val renderer = ProductCardRenderer(productCardView, ProductCardType.List)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)
    private val videoIdentifier by lazyView<ImageView?>(R.id.productCardVideoIdentifier)
    private val threeDots by lazyView<ImageView?>(R.id.productCardThreeDots)
    private val video: VideoPlayerController by lazyThreadSafetyNone {
        VideoPlayerController(productCardView, R.id.productCardVideo, R.id.productCardImage)
    }

    override fun getVideoPlayerController(): VideoPlayerController = video

    override fun additionalMarginStart(): Int = cardContainer?.marginStart ?: 0

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        View.inflate(context, R.layout.product_card_reimagine_list_layout, productCardView)

        CompatPaddingUtils(context, productCardView.layoutParams, attrs).updateMargin()

        cardContainer?.run {
            elevation = 0f
            radius = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        setProductModel(ProductCardModelReimagine.from(productCardModel))
    }

    fun setProductModel(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        renderer.setProductModel(productCardModel)

        renderVideo(productCardModel)
        renderThreeDots(productCardModel)
    }

    private fun renderVideo(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        videoIdentifier?.showWithCondition(productCardModel.showVideoIdentifier())
        video.setVideoURL(productCardModel.videoUrl)
    }

    private fun renderThreeDots(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
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

    override fun recycle() {
        imageView?.glideClear()
        video.clear()
    }
}
