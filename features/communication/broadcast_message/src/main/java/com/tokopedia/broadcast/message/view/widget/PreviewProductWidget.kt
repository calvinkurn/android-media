package com.tokopedia.broadcast.message.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.broadcast.message.data.model.ProductPayloadMutation
import com.tokopedia.broadcast.message.R
import kotlinx.android.synthetic.main.item_product.view.*

class PreviewProductWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    private val containerView = View.inflate(getContext(), R.layout.item_product, this)

    fun setupBubble(isInLeft: Boolean = true){
        containerView.attach_product_chat_container
                .setBackgroundResource(if (isInLeft) R.drawable.preview_left_bubble_bordered
                else R.drawable.preview_right_bubble_bordered)
        invalidate()
        requestLayout()
    }

    fun bindProduct(product: ProductPayloadMutation){
        ImageHandler.LoadImage(containerView.attach_product_image, product.productProfile.imageUrl)
        containerView.attach_product_name.text = product.productProfile.name
        containerView.attach_product_price.text = product.productProfile.price
        containerView.iv_delete.visibility = View.GONE
        invalidate()
        requestLayout()
    }

}