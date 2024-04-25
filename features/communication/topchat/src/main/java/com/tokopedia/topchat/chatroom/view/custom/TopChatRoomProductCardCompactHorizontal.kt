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
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomProductCardCompactHorizontalBinding
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_DARK_GREY

class TopChatRoomProductCardCompactHorizontal @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TopchatChatroomProductCardCompactHorizontalBinding

    init {
        binding = TopchatChatroomProductCardCompactHorizontalBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun bind(product: ProductCardCompactUiModel) {
        bindImage(product)
        bindTitle(product)
        bindPrice(product)
        bindDiscount(product)
        bindRating(product)
        bindLabel(product)
    }

    private fun bindImage(product: ProductCardCompactUiModel) {
        binding.topchatChatroomIvProductCompactHorizontal.loadImage(
            product.imageUrl
        ) {
            this.setRoundedRadius(8f)
        }
    }

    private fun bindTitle(product: ProductCardCompactUiModel) {
        binding.topchatChatroomTvProductCompactHorizontalTitle.text = product.name
    }

    private fun bindPrice(product: ProductCardCompactUiModel) {
        binding.topchatChatroomTvProductCompactHorizontalPrice.text = product.price
    }

    @SuppressLint("SetTextI18n")
    private fun bindDiscount(product: ProductCardCompactUiModel) {
        val slashPrice = product.slashPrice
        val discount = product.discount
        if (slashPrice.isNotBlank() && discount.isNotBlank()) {
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

    private fun bindDiscountText(discount: String) {
        binding.topchatChatroomTvProductCompactHorizontalDiscount.text = "$discount%"
        binding.topchatChatroomTvProductCompactHorizontalDiscount.showWithCondition(discount.isNotBlank())
    }

    private fun bindRating(product: ProductCardCompactUiModel) {
        val rating = product.rating
        val sold = product.sold
        if (rating.isNotBlank() || sold.isNotBlank()) {
            binding.topchatChatroomProductCompactHorizontalSeparator.showWithCondition(
                rating.isNotBlank() && sold.isNotBlank()
            )
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
            binding.topchatChatroomTvProductCompactHorizontalSold.text = sold
            binding.topchatChatroomTvProductCompactHorizontalSold.show()
        } else {
            binding.topchatChatroomTvProductCompactHorizontalSold.gone()
        }
    }

    private fun bindLabel(product: ProductCardCompactUiModel) {
        val oosLabelGroup = product.getOosLabelGroup()
        val preOrderLabelGroup = product.getPreOrderLabelGroup()
        when {
            (oosLabelGroup != null) -> {
                binding.topchatChatroomTvProductCompactHorizontalLabel.setLabelType(HIGHLIGHT_DARK_GREY)
                binding.topchatChatroomTvProductCompactHorizontalLabel.text = oosLabelGroup.title
                binding.topchatChatroomTvProductCompactHorizontalLabel.show()
            }
            (preOrderLabelGroup != null) -> {
                binding.topchatChatroomTvProductCompactHorizontalLabel.setLabelType(HIGHLIGHT_DARK_GREY)
                binding.topchatChatroomTvProductCompactHorizontalLabel.text = preOrderLabelGroup.title
                binding.topchatChatroomTvProductCompactHorizontalLabel.show()
            }
            else -> {
                binding.topchatChatroomTvProductCompactHorizontalLabel.gone()
            }
        }
    }

    fun cleanUp() {
        binding.topchatChatroomIvProductCompactHorizontal.clearImage()
    }
}
