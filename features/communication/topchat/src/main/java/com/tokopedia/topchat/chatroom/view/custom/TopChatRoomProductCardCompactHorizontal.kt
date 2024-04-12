package com.tokopedia.topchat.chatroom.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.topchat.databinding.TopchatChatroomProductCardCompactHorizontalBinding

class TopChatRoomProductCardCompactHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TopchatChatroomProductCardCompactHorizontalBinding

    init {
        binding = TopchatChatroomProductCardCompactHorizontalBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun bind(product: ProductCardModel) {
        bindImage(product)
        bindTitle(product)
        bindPrice(product)
        bindDiscount(product)
        bindRating(product)
    }

    private fun bindImage(product: ProductCardModel) {
        binding.topchatChatroomIvProductCompactHorizontal.loadImage(
            product.imageUrl
        ) {
            this.setRoundedRadius(8f)
        }
    }

    private fun bindTitle(product: ProductCardModel) {
        binding.topchatChatroomTvProductCompactHorizontalTitle.text = product.name
    }

    private fun bindPrice(product: ProductCardModel) {
        binding.topchatChatroomTvProductCompactHorizontalPrice.text = product.price
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: ProductCardModel) {
        val slashPrice = product.slashedPrice
        val discount = product.discountPercentage
        if (slashPrice.isNotBlank() && discount > 0) {
            bindSlashPrice(slashPrice)
            bindDiscountText(discount)
            binding.topchatChatroomClProductCompactHorizontalDiscount.visible()
        } else {
            binding.topchatChatroomClProductCompactHorizontalDiscount.gone()
        }
    }

    private fun bindSlashPrice(slashPrice: String) {
        binding.topchatChatroomTvProductCompactHorizontalSlashPrice.text = slashPrice
        binding.topchatChatroomTvProductCompactHorizontalSlashPrice.paintFlags =
            binding.topchatChatroomTvProductCompactHorizontalSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.topchatChatroomTvProductCompactHorizontalSlashPrice.showWithCondition(slashPrice.isNotBlank())
    }

    private fun bindDiscountText(discount: Int) {
        binding.topchatChatroomTvProductCompactHorizontalDiscount.text = "$discount%"
        binding.topchatChatroomTvProductCompactHorizontalDiscount.showWithCondition(discount > 0)
    }

    private fun bindRating(product: ProductCardModel) {
        val rating = product.rating
        val sold = product.labelCredibility()?.title.orEmpty()
        if (rating.isNotBlank() || sold.isNotBlank()) {
            bindRatingText(rating)
            bindSold(sold)
            binding.topchatChatroomLlProductCompactHorizontalRating.show()
        } else {
            binding.topchatChatroomLlProductCompactHorizontalRating.gone()
        }
    }

    private fun bindRatingText(rating: String) {
        if (rating.isNotBlank()) {
            binding.topchatChatroomIconProductCompactHorizontalRating.show()
            binding.topchatChatroomTvProductCompactHorizontalRating.text = rating
            binding.topchatChatroomTvProductCompactHorizontalRating.show()
        } else {
            binding.topchatChatroomIconProductCompactHorizontalRating.gone()
            binding.topchatChatroomTvProductCompactHorizontalRating.gone()
            binding.topchatChatroomProductCompactHorizontalSeparator.gone()
        }
    }

    private fun bindSold(sold: String) {
        if (sold.isNotBlank()) {
            binding.topchatChatroomProductCompactHorizontalSeparator.show()
            binding.topchatChatroomTvProductCompactHorizontalSold.text = sold
            binding.topchatChatroomTvProductCompactHorizontalSold.show()
        } else {
            binding.topchatChatroomProductCompactHorizontalSeparator.gone()
            binding.topchatChatroomTvProductCompactHorizontalSold.gone()
        }
    }

    fun cleanUp() {
        binding.topchatChatroomIvProductCompactHorizontal.clearImage()
    }
}
