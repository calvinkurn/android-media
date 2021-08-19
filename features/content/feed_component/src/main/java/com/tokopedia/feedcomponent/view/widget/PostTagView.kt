package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

private const val PRODUCT_DOT_TIMER = 4000L
private const val PRODUCT_DOT_ONE_SEC = 1000L
private const val POSITION_TOP = 1
private const val POSITION_BOTTOM = 2
private const val POINTER_HEIGHT = 8
private const val BUBBLE_HEIGHT = 52
private const val DOT_HALF_DIMEN = 8

class PostTagView @JvmOverloads constructor(
    context: Context,
    feedXMediaTagging: FeedXMediaTagging,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private var productTagDot: ImageView
    private var productTagPointerTop: ImageView
    private var productTagPointerBottom: ImageView
    private var finalPointerView: ImageView
    private var productTagExpandedView: CardView
    private var productViewArrow: IconUnify
    private var productViewName: Typography
    private var productViewPrice: Typography
    private var productViewSlashedPrice: Typography
    private var constraintLayout: ConstraintLayout
    private var listener: DynamicPostViewHolder.DynamicPostListener? = null
    private var bubbleMarginStart: Int = 0
    private var dotMarginStart: Int = 0
    private var dotMarginTop: Int = 0
    private var postImageWidth: Int = 0
    private var postImageHeight: Int = 0
    private var position: Int = 0
    private var feedXTag: FeedXMediaTagging
    private var initialBubbleVisible: Boolean

    init {
        (context as LifecycleOwner).lifecycle.addObserver(this)
        this.feedXTag = feedXMediaTagging
        initialBubbleVisible = false
        val view =
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
    }
    fun bindData(
        dynamicPostListener: DynamicPostViewHolder.DynamicPostListener?,
        products: List<FeedXProduct>,
        width: Int,
        height: Int,
        positionInFeed: Int
    ) {
        this.listener = dynamicPostListener
        this.dotMarginStart = convertDpToPixel((width * feedXTag.posX), context)
        this.dotMarginTop = convertDpToPixel((height * (1 - feedXTag.posY)), context)
        this.postImageHeight = height
        this.postImageWidth = convertDpToPixel(width.toFloat(), context)
        val product = products[feedXTag.tagIndex]

        productViewName.text = product.name

        if (product.isDiscount) {
            productViewPrice.text = product.priceDiscountFmt
            setSlashedPriceText(product.priceOriginalFmt)
        } else {
            productViewSlashedPrice.gone()
            productViewPrice.text = product.priceFmt
        }

        if (feedXTag.posY < 0.25) {
            this.finalPointerView = productTagPointerBottom
            position = POSITION_TOP
        } else {
            this.finalPointerView = productTagPointerTop
            position = POSITION_BOTTOM
        }
        productTagDot.setMargin(
            dotMarginStart,
            dotMarginTop,
            0,
            0
        )
        productTagExpandedView.setOnClickListener {
            listener?.onPostTagBubbleClick(positionInFeed, product.appLink, product)
        }

        productTagExpandedView.doOnLayout {
            val w = productTagExpandedView.width
            bubbleMarginStart = setProductTagBubbleStartMargin(w)

            if (position == POSITION_BOTTOM) {
                productTagExpandedView.setMargin(bubbleMarginStart,
                    dotMarginTop + POINTER_HEIGHT.toPx(),
                    0,
                    0)
            } else {
                productTagExpandedView.setMargin(bubbleMarginStart,
                    (dotMarginTop - DOT_HALF_DIMEN.toPx()) - BUBBLE_HEIGHT.toPx(),
                    0,
                    0)
            }

            finalPointerView.setMargin(
                dotMarginStart - DOT_HALF_DIMEN.toPx(),
                0,
                0,
                0
            )
        }
        productTagDot.invisible()
        productTagExpandedView.invisible()
        finalPointerView.invisible()

    }

    fun showExpandedView(): Boolean {
        val isProductDotVisible = productTagDot.isVisible

        if (productTagDot.isVisible) {
            productTagDot.gone()
            finalPointerView.visible()
            productTagExpandedView.visible()
        } else if (finalPointerView.isVisible && productTagExpandedView.isVisible) {
            finalPointerView.gone()
            productTagExpandedView.gone()
        } else if (!initialBubbleVisible) {
            val params = productTagExpandedView.layoutParams as MarginLayoutParams
            if (position == POSITION_BOTTOM) {
                params.setMargins(bubbleMarginStart, dotMarginTop + POINTER_HEIGHT.toPx(), 0, 0)
            } else {
                params.setMargins(bubbleMarginStart, dotMarginTop - BUBBLE_HEIGHT.toPx(), 0, 0)

            }
            productTagExpandedView.layoutParams = params
            finalPointerView.visible()
            productTagExpandedView.visible()
        } else {
            finalPointerView.visible()
            productTagExpandedView.visible()
        }
        return isProductDotVisible

    }

    fun getExpandedViewVisibility(): Boolean {
        return productTagExpandedView.isVisible

    }

    private fun setProductTagBubbleStartMargin(
        bubbleInflatedWidth: Int
    ): Int {
        return if (feedXTag.posX <= 0.5) {
            if (dotMarginStart >= bubbleInflatedWidth / 2)
                dotMarginStart - bubbleInflatedWidth / 2
            else
                0

        } else {
            val endMargin = postImageWidth - dotMarginStart
            if (endMargin >= bubbleInflatedWidth / 2)
                dotMarginStart - bubbleInflatedWidth / 2
            else
                postImageWidth - bubbleInflatedWidth
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
                dotMarginStart,
                dotMarginTop,
                0,
                0
            )
        }

        if (!productTagDot.isVisible)
            productTagDot.postDelayed({
                productTagDot.apply {
                    if (!(productTagExpandedView.isVisible || finalPointerView.isVisible))
                        visible()
                    initialBubbleVisible = true
                }
            }, PRODUCT_DOT_ONE_SEC)

        productTagDot.postDelayed({
            productTagDot.apply {
                if (isVisible)
                    gone()
            }
        }, PRODUCT_DOT_TIMER)
    }
}

