package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.TokofoodProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

class ProductCardViewHolder(
    private val binding: TokofoodProductCardLayoutBinding,
    private val clickListener: OnProductCardItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnProductCardItemClickListener {
        fun onProductCardClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>)
        fun onAtcButtonClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>)
        fun onAddNoteButtonClicked(productId: String, orderNote: String, cardPositions: Pair<Int, Int>)
        fun onDeleteButtonClicked(cartId: String, productId: String, cardPositions: Pair<Int, Int>)
        fun onIncreaseQtyButtonClicked(productId: String, quantity: Int, cardPositions: Pair<Int, Int>)
        fun onDecreaseQtyButtonClicked(productId: String, quantity: Int, cardPositions: Pair<Int, Int>)
        fun onImpressProductCard(productListItem: ProductListItem, position: Int)
    }

    private var context: Context? = null
    private var productListItem: ProductListItem? = null

    init {
        context = binding.root.context
        binding.root.setOnClickListener {
            // open product bottom sheet
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            productListItem?.let { productListItem ->
                clickListener.onProductCardClicked(
                    productListItem = productListItem,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
                )
            }
        }
        binding.atcButton.setOnClickListener {
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            productListItem?.let { productListItem ->
                clickListener.onAtcButtonClicked(
                    productListItem = productListItem,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
                )
            }
        }
        binding.addCatatanButton.setOnClickListener {
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            clickListener.onAddNoteButtonClicked(
                productId = productUiModel.id,
                orderNote = productUiModel.orderNote,
                cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
        binding.removeProductFromCartButton.setOnClickListener {
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            clickListener.onDeleteButtonClicked(
                cartId = productUiModel.cartId,
                productId = productUiModel.id,
                cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
        binding.qeuProductQtyEditor.setAddClickListener {
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            val quantity = binding.qeuProductQtyEditor.getValue()
            clickListener.onIncreaseQtyButtonClicked(
                productId = productUiModel.id,
                quantity = quantity,
                cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
        binding.qeuProductQtyEditor.setSubstractListener {
            val productUiModel = binding.root.getTag(R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(R.id.dataset_position) as Int
            val quantity = binding.qeuProductQtyEditor.getValue()
            binding.qeuProductQtyEditor.subtractButton.isEnabled = quantity != Int.ONE
            clickListener.onDecreaseQtyButtonClicked(
                    productId = productUiModel.id,
                    quantity = quantity,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
    }

    fun bindData(productListItem: ProductListItem, productUiModel: ProductUiModel, dataSetPosition: Int) {
        // bind product ui model and data set position
        this.productListItem = productListItem
        bindImpressionProductListener(productListItem, dataSetPosition)
        binding.root.setTag(R.id.product_ui_model, productUiModel)
        binding.root.setTag(R.id.dataset_position, dataSetPosition)

        // disable atc button if product is out of stock
        binding.atcButton.isEnabled = !productUiModel.isOutOfStock

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
        binding.productImage.isVisible = productUiModel.imageURL.isNotBlank()
        binding.productImage.setImageUrl(productUiModel.imageURL)
        binding.tpgOutOfStock.isVisible = productUiModel.isOutOfStock && productUiModel.imageURL.isNotBlank()
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
        if (productUiModel.isCustomizable) {
            binding.orderDetailLayout.hide()
            binding.atcButton.show()
        } else {
            binding.orderDetailLayout.isVisible = productUiModel.isAtc
            binding.atcButton.isVisible = !productUiModel.isAtc
        }
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
        // atc button wording e.g. Pesan or 2 Pesanan
        val customOrderCount = productUiModel.customOrderDetails.size
        if (customOrderCount.isMoreThanZero() && productUiModel.isCustomizable) {
            val orderCount = customOrderCount.toString()
            binding.atcButton.text = context?.run { this.getString(com.tokopedia.tokofood.R.string.text_orders, orderCount) }
        } else {
            binding.atcButton.text = context?.run { getString(com.tokopedia.tokofood.R.string.action_order) }
        }
    }

    private fun bindImpressionProductListener(
        productListItem: ProductListItem,
        dataSetPosition: Int
    ) {
        binding.root.addOnImpressionListener(
            productListItem,
            createViewHintListener(productListItem, dataSetPosition)
        )
    }

    private fun createViewHintListener(
        productListItem: ProductListItem,
        dataSetPosition: Int
    ): ViewHintListener {
        return object : ViewHintListener {
            override fun onViewHint() {
                clickListener.onImpressProductCard(productListItem, dataSetPosition)
            }
        }
    }
}