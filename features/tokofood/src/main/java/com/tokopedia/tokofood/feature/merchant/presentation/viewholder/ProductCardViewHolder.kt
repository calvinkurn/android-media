package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.MerchantProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

class ProductCardViewHolder(
        private val binding: MerchantProductCardLayoutBinding,
        private val clickListener: OnProductCardItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnProductCardItemClickListener {
        fun onProductCardClicked()
        fun onAtcButtonClicked()
        fun onAddNoteButtonClicked()
        fun onDeleteButtonClicked()
        fun onIncreaseQtyButtonClicked()
        fun onDecreaseQtyButtonClicked()
    }

    private var context: Context? = null

    init {
        context = binding.root.context
        binding.root.setOnClickListener {

            // open product bottom sheet
            // product name, description, price, slash price
            clickListener.onProductCardClicked()
        }
        binding.atcButton.setOnClickListener {
            clickListener.onAtcButtonClicked()
        }
        binding.addCatatanButton.setOnClickListener {
            // TODO: implement logic to determine add or edit
            // note
            clickListener.onAddNoteButtonClicked()
        }
        binding.removeProductFromCartButton.setOnClickListener {
            clickListener.onDeleteButtonClicked()
        }
        binding.qeuProductQtyEditor.setAddClickListener {
            clickListener.onIncreaseQtyButtonClicked()
        }
        binding.qeuProductQtyEditor.setSubstractListener {
            clickListener.onDecreaseQtyButtonClicked()
        }
    }

    fun bindData(uiModel: ProductUiModel) {

        context?.run {
            // disabled condition
            if (uiModel.isShopClosed) {
                val greyColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN400)
                binding.productName.setTextColor(greyColor)
                binding.productSummary.setTextColor(greyColor)
                binding.productPrice.setTextColor(greyColor)
                binding.qeuProductQtyEditor.isEnabled = false
                binding.atcButton.isEnabled = false
            }
            // product is already added to cart
            if (uiModel.isAtc) {
                val greenColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
                binding.productCell.setCardBackgroundColor(greenColor)
            } else {
                val whiteColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
                binding.productCell.setCardBackgroundColor(whiteColor)
            }
        }

        // product card attributes
        binding.productImage.setImageUrl(uiModel.imageURL)
        binding.tpgOutOfStock.isVisible = uiModel.isOutOfStock
        binding.customIndicatorLabel.isVisible = uiModel.isCustomizable
        binding.productName.text = uiModel.name
        binding.productSummary.text = uiModel.description
        binding.productPrice.text = uiModel.priceFmt
        binding.slashPriceInfo.isVisible = uiModel.isSlashPriceVisible
        binding.productSlashPrice.isVisible = uiModel.isSlashPriceVisible
        binding.productSlashPrice.text = uiModel.slashPrice.toString()

        // order detail layout - non variant
        binding.orderDetailLayout.isVisible = uiModel.isOrderDetailLayoutVisible
        if (binding.orderDetailLayout.isVisible) {
            context?.run {
                if (uiModel.orderNote.isBlank()) {
                    val addNoteIcon = ContextCompat.getDrawable(this, R.drawable.ic_add_note)
                    binding.iuAddNote.setImageDrawable(addNoteIcon)
                } else {
                    val addNoteIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_note)
                    binding.iuAddNote.setImageDrawable(addNoteIcon)
                }
            }
            // set order detail quantity
            binding.qeuProductQtyEditor.setValue(uiModel.orderDetail.qty)
        }
    }
}