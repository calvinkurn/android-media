package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.TokofoodProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

class ProductCardViewHolder(
        private val binding: TokofoodProductCardLayoutBinding,
        private val clickListener: OnProductCardItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnProductCardItemClickListener {
        fun onProductCardClicked(productUiModel: ProductUiModel)
        fun onAtcButtonClicked(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>)
        fun onAddNoteButtonClicked(orderNote: String, cardPositions: Pair<Int, Int>)
        fun onDeleteButtonClicked()
        fun onIncreaseQtyButtonClicked()
        fun onDecreaseQtyButtonClicked()
    }

    private var context: Context? = null

    init {
        context = binding.root.context
        binding.root.setOnClickListener {
            // open product bottom sheet
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            clickListener.onProductCardClicked(productUiModel)
        }
        binding.atcButton.setOnClickListener {
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            clickListener.onAtcButtonClicked(
                    productUiModel = productUiModel,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
        binding.addCatatanButton.setOnClickListener {
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            clickListener.onAddNoteButtonClicked(
                    orderNote = productUiModel.orderNote,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
            )
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

    fun bindData(productUiModel: ProductUiModel, dataSetPosition: Int) {
        // bind product ui model and data set position
        binding.root.setTag(R.id.product_ui_model, productUiModel)
        binding.root.setTag(R.id.dataset_position, dataSetPosition)
        context?.run {
            // disabled condition
            if (productUiModel.isShopClosed) {
                val greyColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN400)
                binding.productName.setTextColor(greyColor)
                binding.productSummary.setTextColor(greyColor)
                binding.productPrice.setTextColor(greyColor)
                binding.qeuProductQtyEditor.isEnabled = false
                binding.atcButton.isEnabled = false
            }
            // product is already added to cart
            if (productUiModel.isAtc) {
                val greenColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
                binding.productCell.setCardBackgroundColor(greenColor)
            } else {
                val whiteColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
                binding.productCell.setCardBackgroundColor(whiteColor)
            }
        }
        // product card attributes
        binding.productImage.setImageUrl(productUiModel.imageURL)
        binding.tpgOutOfStock.isVisible = productUiModel.isOutOfStock
        binding.customIndicatorLabel.isVisible = productUiModel.isCustomizable
        binding.productName.text = productUiModel.name
        binding.productSummary.text = productUiModel.description
        binding.productPrice.text = productUiModel.priceFmt
        binding.slashPriceInfo.isVisible = productUiModel.isSlashPriceVisible
        binding.productSlashPrice.isVisible = productUiModel.isSlashPriceVisible
        if (productUiModel.isSlashPriceVisible) {
            binding.productSlashPrice.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = productUiModel.slashPriceFmt
            }
        }

        binding.orderDetailLayout.isVisible = productUiModel.isAtc
        binding.atcButton.isVisible = !productUiModel.isAtc

        if (binding.orderDetailLayout.isVisible) {
            context?.run {
                if (productUiModel.orderNote.isBlank()) {
                    val addNoteIcon = ContextCompat.getDrawable(this, R.drawable.ic_add_note)
                    binding.iuAddNote.setImageDrawable(addNoteIcon)
                } else {
                    val addNoteIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_note)
                    binding.iuAddNote.setImageDrawable(addNoteIcon)
                }
            }
            // set order detail quantity
            binding.qeuProductQtyEditor.setValue(productUiModel.orderQty)
        }
    }
}