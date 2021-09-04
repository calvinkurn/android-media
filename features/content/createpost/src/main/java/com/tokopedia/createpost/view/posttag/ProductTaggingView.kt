package com.tokopedia.createpost.view.posttag

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.listener.CreateContentPostCOmmonLIstener
import com.tokopedia.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.util.util.doOnLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography


private const val POSITION_TOP = 1
private const val POSITION_BOTTOM = 2
private const val POINTER_HEIGHT = 8
private const val BUBBLE_HEIGHT = 52
private const val DELETE_ICON_WIDTH = 32
private const val DOT_HALF_DIMEN = 8
private const val CENTER_POS_X = 0.5
private const val THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD = 0.75

class ProductTaggingView @JvmOverloads constructor(
    context: Context?,
    feedXMediaTagging: FeedXMediaTagging,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private var productTagPointerTop: ImageView
    private var productTagPointerBottom: ImageView
    private var finalPointerView: ImageView
    private var productTagExpandedView: CardView
    private var productTagViewDelete: IconUnify
    private var productViewName: Typography
    private var productViewPrice: Typography
    private var productViewSlashedPrice: Typography
    private var constraintLayout: ConstraintLayout
    private var listener: CreateContentPostCOmmonLIstener? = null
    private var bubbleMarginStart: Int = 0
    private var dotMarginStart: Int = 0
    private var dotMarginTop: Int = 0
    private var postImageWidth: Int = 0
    private var postImageHeight: Int = 0
    private var position: Int = 0
    private var feedXTag: FeedXMediaTagging = feedXMediaTagging

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.content_creation_product_tagging_view, this, true)

        view.run {
            productTagPointerTop = findViewById(R.id.product_tag_top_pointer)
            productTagPointerBottom = findViewById(R.id.product_tag_bottom_pointer)
            productTagExpandedView = findViewById(R.id.product_tag_expanded_card)
            productTagViewDelete = findViewById(R.id.product_tag_clear)
            productViewName = findViewById(R.id.product_tag_name_text)
            productViewPrice = findViewById(R.id.product_tag_price_text)
            productViewSlashedPrice = findViewById(R.id.product_tag_slashed_price_text)
            constraintLayout = findViewById(R.id.parent_id)
            finalPointerView = productTagPointerTop
        }
    }

    fun bindData(
        listener: CreateContentPostCOmmonLIstener?,
        products: List<RelatedProductItem>,
        width: Int,
        height: Int,
        index: Int
    ) {
        this.listener = listener
        this.dotMarginStart = (width * (feedXTag.posX)).toInt()
        this.dotMarginTop = (height * (feedXTag.posY)).toInt()
        this.postImageHeight = height
        this.postImageWidth = width

        val product = products[index]

        productViewName.text = product.name
        productViewSlashedPrice.gone()
        productViewPrice.text = product.price

        if (feedXTag.posY > THRESHOLD_POS_Y_TO_INFLATE_TAGGING_BUBBLE_DOWNWARD) {
            this.finalPointerView = productTagPointerBottom
            position = POSITION_TOP
        } else {
            this.finalPointerView = productTagPointerTop
            position = POSITION_BOTTOM
        }
        productTagExpandedView.setOnClickListener {
            if (productTagViewDelete.isVisible)
                productTagViewDelete.gone()
            else
                productTagViewDelete.visible()
        }
        productTagViewDelete.setOnClickListener {
            listener?.deleteItemFromProductTagList(feedXTag.tagIndex)
            hideExpandedView()
        }
        finalPointerView.setMargin(
            dotMarginStart - DOT_HALF_DIMEN.toPx(),
            0,
            0,
            0
        )
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
        productTagExpandedView.visible()
        finalPointerView.visible()

    }

    private fun setProductTagBubbleStartMargin(
        bubbleInflatedWidth: Int,
    ): Int {
        return if (feedXTag.posX <= CENTER_POS_X) {
            if (dotMarginStart >= bubbleInflatedWidth / 2)
                dotMarginStart - bubbleInflatedWidth / 2
            else
                0
        } else {
            val endMargin = postImageWidth - dotMarginStart
            if (endMargin >= bubbleInflatedWidth / 2)
                dotMarginStart - bubbleInflatedWidth / 2 - DELETE_ICON_WIDTH.toPx()
            else
                postImageWidth - bubbleInflatedWidth - DELETE_ICON_WIDTH.toPx()
        }
    }
    private fun showExpandedView() {
        productTagExpandedView.doOnLayout {
            val w = productTagExpandedView.width
            bubbleMarginStart = setProductTagBubbleStartMargin(w)
            if (position == POSITION_BOTTOM) {
                setMargin(bubbleMarginStart, dotMarginTop + POINTER_HEIGHT.toPx(), 0, 0)
            } else {
                setMargin(bubbleMarginStart, dotMarginTop - BUBBLE_HEIGHT.toPx(), 0, 0)
            }
        }

        productTagExpandedView.visible()
        finalPointerView.visible()
    }

     fun hideExpandedView() {
        productTagExpandedView.invisible()
        finalPointerView.invisible()
    }


}


