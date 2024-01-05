package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ProductCardGridView: ConstraintLayout {
    private val renderer = ProductCardRenderer(this, Grid)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val imageView by lazyView<ImageUnify?>(R.id.productCardImage)
    private val preventiveThematicLabel by lazyView<Typography?>(R.id.productCardLabelPreventiveThematic)
    private val videoIdentifier by lazyView<ImageView?>(R.id.productCardVideoIdentifier)
    private val threeDots by lazyView<ImageView?>(R.id.productCardThreeDots)
    private val assignedValueLabel = ProductCardLabelAssignedValue(this)

    val video: VideoPlayerController by lazyThreadSafetyNone {
        VideoPlayerController(this, R.id.productCardVideo, R.id.productCardImage)
    }

    val additionalMarginStart: Int
        get() = cardContainer?.marginStart ?: 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        View.inflate(context, R.layout.product_card_reimagine_grid_layout, this)

        cardContainer?.run {
            elevation = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        renderer.setProductModel(productCardModel)

        renderLabelPreventiveThematic(productCardModel)
        renderVideo(productCardModel)
        renderThreeDots(productCardModel)

        assignedValueLabel.render(productCardModel.labelAssignedValue())
    }

    private fun renderLabelPreventiveThematic(productCardModel: ProductCardModel) {
        val labelView = preventiveThematicLabel ?: return
        val labelPreventiveThematic = productCardModel.labelPreventiveThematic()

        if (labelPreventiveThematic == null || productCardModel.isSafeProduct) {
            labelView.hide()
        } else {
            labelView.show()
            ProductCardLabel(labelView.background, labelView).render(labelPreventiveThematic)
        }
    }

    private fun renderVideo(productCardModel: ProductCardModel) {
        videoIdentifier?.showWithCondition(
            productCardModel.videoUrl.isNotBlank() && !productCardModel.isSafeProduct
        )
        video.setVideoURL(productCardModel.videoUrl)
    }

    private fun renderThreeDots(productCardModel: ProductCardModel) {
        threeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            post {
                threeDots?.expandTouchArea(
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_8),
                    it.getDimensionPixelSize(unifyprinciplesR.dimen.unify_space_16)
                )
            }
        }
    }

    fun addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
        imageView?.addOnImpressionListener(holder, onView)
    }

    fun setThreeDotsClickListener(onClickListener: OnClickListener) {
        threeDots?.setOnClickListener(onClickListener)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cardContainer?.setOnClickListener(l)
    }

    fun recycle() {
        imageView?.glideClear()
        video.clear()
    }
}
