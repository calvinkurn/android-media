package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifyprinciples.Typography

private const val PRODUCT_DOT_TIMER = 3000L
private const val POSITION_TOP = 1
private const val POSITION_BOTTOM = 2

class PostTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver  {

    private var productTagDot: ImageView
    private var productTagPointer: ImageView
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
    private lateinit var feedXTag: FeedXMediaTagging

    init {
        (context as LifecycleOwner).lifecycle.addObserver(this)
        val view =
            LayoutInflater.from(context).inflate(R.layout.product_tag_detail_view, this, true)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        view.layoutParams = params
        view.run {
            productTagDot = findViewById(R.id.product_tag_dot)
            productTagPointer = findViewById(R.id.product_tag_top_pointer)
            productTagExpandedView = findViewById(R.id.product_tag_expanded_card)
            productViewArrow = findViewById(R.id.product_arrow)
            productViewName = findViewById(R.id.product_name_text)
            productViewPrice = findViewById(R.id.product_price_text)
            productViewSlashedPrice = findViewById(R.id.product_slashed_price_text)
            constraintLayout = findViewById(R.id.parent_id)
        }
    }
    fun bindData(
        dynamicPostListener: DynamicPostViewHolder.DynamicPostListener?,
        feedXMediaTagging: FeedXMediaTagging,
        products: List<FeedXProduct>,
        width: Int,
        height: Int,
        positionInFeed: Int
    ){
        this.listener = dynamicPostListener
        this.dotMarginStart = convertDpToPixel((width * feedXMediaTagging.posX), context)
        this.dotMarginTop = convertDpToPixel((height * (1 - feedXMediaTagging.posY)), context)
        this.postImageHeight = height
        this.postImageWidth = convertDpToPixel(width.toFloat(), context)
        this.feedXTag = feedXMediaTagging
        val product = products[feedXMediaTagging.tagIndex]

        productViewName.text = product.name

        if (product.isDiscount) {
            productViewPrice.text = product.priceDiscountFmt
            setSlashedPriceText(product.priceOriginalFmt)
        } else {
            productViewSlashedPrice.gone()
            productViewPrice.text = product.priceFmt
        }

        if (feedXTag.posY < 0.25) {
            productTagPointer.setImageDrawable(MethodChecker.getDrawable(
                context,
                R.drawable.ic_down
            ))
            position = POSITION_TOP
        } else {
            position = POSITION_BOTTOM
            productTagPointer.setImageDrawable(MethodChecker.getDrawable(
                context,
                R.drawable.ic_up
            ))
        }
        productTagDot.setMargin(
            dotMarginStart,
            dotMarginTop,
            0,
            0
        )
        productTagPointer.doOnLayout {
            productTagPointer.setMargin(
                dotMarginStart,
                dotMarginStart,
                0,
                0
            )

        }

       productTagExpandedView.doOnLayout {
           val w = productTagExpandedView.width

           bubbleMarginStart = setProductTagBubbleStartMargin(w)

           val constraintSet = ConstraintSet()
           constraintSet.clone(constraintLayout)
           if (position == POSITION_BOTTOM)
               constraintSet.connect(productTagExpandedView.id,
                   ConstraintSet.TOP,
                   productTagPointer.id,
                   ConstraintSet.BOTTOM,
                   bubbleMarginStart)
           else {
               constraintSet.connect(productTagExpandedView.id,
                   ConstraintSet.TOP,
                   ConstraintSet.PARENT_ID,
                   ConstraintSet.BOTTOM,
                   bubbleMarginStart)
               constraintSet.connect(productTagExpandedView.id,
                   ConstraintSet.BOTTOM,
                   productTagPointer.id,
                   ConstraintSet.TOP,
                   bubbleMarginStart)

               constraintSet.connect(productTagPointer.id,
                   ConstraintSet.TOP,
                   productTagExpandedView.id,
                   ConstraintSet.BOTTOM,
                   bubbleMarginStart)
           }
           constraintSet.applyTo(constraintLayout)

       }

        productTagExpandedView.setOnClickListener {
            listener?.onPostTagBubbleClick(positionInFeed, product.appLink, product)
        }
        productTagDot.postDelayed({
            productTagDot.apply {
                gone()
            }
        }, PRODUCT_DOT_TIMER)

    }
    fun showExpandedView() {
        if (productTagDot.isVisible) {
            productTagDot.gone()
            productTagPointer.visible()
            productTagExpandedView.visible()
        } else if (productTagPointer.isVisible && productTagExpandedView.isVisible) {
            productTagPointer.gone()
            productTagExpandedView.gone()
        } else {
            productTagPointer.visible()
            productTagExpandedView.visible()
        }
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
    private fun setSlashedPriceText( priceOriginal: String) {
        productViewSlashedPrice?.run {
            text = priceOriginal
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            visible()
        }
    }
}

