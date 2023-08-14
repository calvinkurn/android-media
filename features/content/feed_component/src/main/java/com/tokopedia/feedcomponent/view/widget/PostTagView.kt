package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.util.util.doOnLayout
import com.tokopedia.feedcomponent.util.util.goneWithAnimation
import com.tokopedia.feedcomponent.util.util.hideBubbleViewWithAnimation
import com.tokopedia.feedcomponent.util.util.showBubbleViewWithAnimation
import com.tokopedia.feedcomponent.util.util.visibleWithAnimation
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

private const val PRODUCT_DOT_TIMER = 4000L
private const val PRODUCT_DOT_ONE_SEC = 1000L
private const val POSITION_TOP = 1
private const val POSITION_BOTTOM = 2
private const val POINTER_HEIGHT = 8
private const val POINTER_ACTUAL_WIDTH = 79
private const val BUBBLE_HEIGHT = 52
private const val DOT_HALF_DIMEN = 8
private const val PRICE_PADDING_WIDTH = 8F
private const val CENTER_POS_X = 0.5
private const val THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD = 0.70

class PostTagView @JvmOverloads constructor(
    context: Context,
    feedXMediaTagging: com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private var productTagDot: IconUnify
    private var productTagPointerTop: ImageView
    private var productTagPointerBottom: ImageView
    private var finalPointerView: ImageView
    private var productTagExpandedView: CardView
    private var productViewArrow: IconUnify
    private var productViewName: Typography
    private var productViewPrice: Typography
    private var productViewSlashedPrice: Typography
    private var constraintLayout: ConstraintLayout
    private var listener: TagBubbleListener? = null
    private var bubbleMarginStart: Int = 0
    private var dotMarginStart: Int = 0
    private var dotMarginTop: Int = 0
    private var postImageWidth: Int = 0
    private var postImageHeight: Int = 0
    private var position: Int = 0
    private var feedXTag: com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging = feedXMediaTagging
    private var initialBubbleVisible: Boolean
    private var view : View

    init {
        initialBubbleVisible = false
        view =
            LayoutInflater.from(context).inflate(R.layout.product_tag_detail_view, this, true)

        view.run {
            productTagDot = findViewById(R.id.product_tag_dot)
            productTagPointerTop = findViewById(R.id.product_tag_top_pointer)
            productTagPointerBottom = findViewById(R.id.product_tag_bottom_pointer)
            productTagExpandedView = findViewById(R.id.product_tag_expanded_card)
            productViewArrow = findViewById(R.id.product_arrow)
            productViewName = findViewById(R.id.product_name_text)
            productViewPrice = findViewById(R.id.product_price_text)
            productViewSlashedPrice = findViewById(R.id.product_slashed_price_text)
            constraintLayout = findViewById(R.id.parent_id)
            finalPointerView = productTagPointerTop
        }

        productTagDot.setImageResource(R.drawable.ic_product_tag_circle)
        productTagPointerTop.setImageResource(R.drawable.ic_feed_up)
        productTagPointerBottom.setImageResource(R.drawable.ic_feed_down)
    }

    fun convertPxToDp( px: Float): Float {
        return px / context.resources.displayMetrics.density
    }
    fun convertDpToPx(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun bindData(
        tagBubbleListener: TagBubbleListener?,
        products: List<FeedXProduct>,
        width: Int,
        height: Int,
        positionInFeed: Int,
        bitmap: Bitmap?,
        campaign: FeedXCampaign?
    ) {
        this.listener = tagBubbleListener
        this.dotMarginStart = (width * (feedXTag.posX)).toInt()
        this.dotMarginTop = (height * (feedXTag.posY)).toInt()
        this.postImageHeight = height
        this.postImageWidth = width
        val product = products[feedXTag.tagIndex]
        productViewName.text = product.name

        if (campaign?.isUpcoming == true) {
            productViewPrice.text = product.priceMaskedFmt
            setSlashedPriceText(product.priceFmt)
        } else if (product.isDiscount) {
            productViewPrice.text = product.priceDiscountFmt
            setSlashedPriceText(product.priceOriginalFmt)
        } else {
            productViewSlashedPrice.gone()
            productViewPrice.text = product.priceFmt
        }

        view.doOnLayout {
            var result = 0F

            var priceWidthPX: Float = productViewPrice.width.toFloat()
            var slashedPriceWidthPX: Float = productViewSlashedPrice.width.toFloat()
            var priceWidthDP = convertPxToDp(priceWidthPX)
            var slashedPriceWidthDP = convertPxToDp(slashedPriceWidthPX)

            if (priceWidthDP > 0)
                result += priceWidthPX
            if (slashedPriceWidthDP > 0)
                result += slashedPriceWidthPX + convertDpToPx(PRICE_PADDING_WIDTH)
            if (result > productViewName.maxWidth && productViewSlashedPrice.isVisible)
                productViewName.maxWidth = result.toInt()

        }


        if (feedXTag.posY > THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD) {
            this.finalPointerView = productTagPointerBottom
            position = POSITION_TOP
        } else {
            this.finalPointerView = productTagPointerTop
            position = POSITION_BOTTOM
        }
        productTagDot.setMargin(
                dotMarginStart - DOT_HALF_DIMEN.toPx(),
                dotMarginTop - DOT_HALF_DIMEN.toPx(),
                0,
                0
        )
        productTagExpandedView.setOnClickListener {
            listener?.onPostTagBubbleClick(positionInFeed, product.appLink, product , product.adClickUrl)
        }

        productTagExpandedView.doOnLayout {
            val w = productTagExpandedView.width
            bubbleMarginStart = setProductTagBubbleStartMargin(w, bitmap)

            if (position == POSITION_BOTTOM) {
                productTagExpandedView.setMargin(bubbleMarginStart,
                    dotMarginTop + POINTER_HEIGHT.toPx(),
                    0,
                    0)
            } else {
                productTagExpandedView.setMargin(bubbleMarginStart,
                    (dotMarginTop - POINTER_HEIGHT.toPx() - BUBBLE_HEIGHT.toPx()),
                    0,
                    0)
            }

            finalPointerView.setMargin(
                dotMarginStart - POINTER_ACTUAL_WIDTH / 2,
                0,
                0,
                0
            )
        }
        productTagDot.invisible()
        productTagExpandedView.invisible()
        finalPointerView.invisible()

    }

    fun toggleExpandedView(): Boolean {
        val isGoingToVisible = if (productTagDot.isVisible) {
            productTagDot.gone()
            showBubbleViewWithAnimation(productTagExpandedView, position, finalPointerView)
            true
        } else if (finalPointerView.isVisible && productTagExpandedView.isVisible) {
            hideBubbleViewWithAnimation(productTagExpandedView, position, finalPointerView)
            false
        } else if (!initialBubbleVisible) {
            val params = productTagExpandedView.layoutParams as MarginLayoutParams
            if (position == POSITION_BOTTOM) {
                params.setMargins(bubbleMarginStart, dotMarginTop + POINTER_HEIGHT.toPx(), 0, 0)
            } else {
                params.setMargins(bubbleMarginStart,
                    dotMarginTop - POINTER_HEIGHT.toPx() - BUBBLE_HEIGHT.toPx(),
                    0,
                    0)
            }
            productTagExpandedView.layoutParams = params
            showBubbleViewWithAnimation(productTagExpandedView, position, finalPointerView)
            true
        } else {
            showBubbleViewWithAnimation(productTagExpandedView, position, finalPointerView)
            true
        }
        return isGoingToVisible

    }
    fun hideExpandedViewIfShown(): Boolean {
        val isProductDotVisible = productTagDot.isVisible

        if (productTagDot.isVisible) {
            productTagDot.gone()
        } else if (finalPointerView.isVisible && productTagExpandedView.isVisible) {
            hideBubbleViewWithAnimation(productTagExpandedView, position, finalPointerView)
        }
        return isProductDotVisible

    }

    fun getExpandedViewVisibility(): Boolean {
        return productTagExpandedView.isVisible

    }

    private fun setProductTagBubbleStartMargin(
        bubbleInflatedWidth: Int,
        bitmap: Bitmap?
    ): Int {
        return if (feedXTag.posX <= CENTER_POS_X) {
            if (dotMarginStart >= bubbleInflatedWidth / 2)
                dotMarginStart + calculateGreyAreaY(bitmap) - bubbleInflatedWidth / 2
            else
                calculateGreyAreaY(bitmap)

        } else {
            val endMargin = postImageWidth - dotMarginStart
            if (endMargin >= bubbleInflatedWidth / 2)
                dotMarginStart - calculateGreyAreaY(bitmap) - bubbleInflatedWidth / 2
            else
                postImageWidth - bubbleInflatedWidth - calculateGreyAreaY(bitmap)
        }
    }
    private fun setSlashedPriceText(priceOriginal: String) {
        productViewSlashedPrice.run {
            text = priceOriginal
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            visible()
        }
    }

    fun resetView(){
        if (productTagExpandedView.isVisible && finalPointerView.isVisible){
            productTagExpandedView.hide()
            finalPointerView.hide()
        }
        if(!initialBubbleVisible){
            productTagDot.setMargin(
                    dotMarginStart - DOT_HALF_DIMEN.toPx(),
                    dotMarginTop - DOT_HALF_DIMEN.toPx(),
                    0,
                    0
            )
        }

        if (!productTagDot.isVisible)
            productTagDot.postDelayed({
                productTagDot.apply {
                    if (!(productTagExpandedView.isVisible || finalPointerView.isVisible)) {
                        visibleWithAnimation()
                        initialBubbleVisible = true
                    }
                }
            }, PRODUCT_DOT_ONE_SEC)

        productTagDot.postDelayed({
            productTagDot.apply {
                if (isVisible) {
                    goneWithAnimation()
                }
            }
        }, PRODUCT_DOT_TIMER)
    }
    private fun calculateGreyAreaY(bitmap: Bitmap?): Int {
        bitmap?.let {
            return if (bitmap.height > bitmap.width) {
                val newBitmapHeight = (postImageWidth * bitmap.width) / bitmap.height
                (postImageWidth - newBitmapHeight) / 2
            } else
                0
        }
        return 0
    }
    interface TagBubbleListener{
        fun onPostTagBubbleClick(
            positionInFeed: Int,
            redirectUrl: String,
            postTagItem: FeedXProduct,
            adClickUrl: String
        )
    }
}

