package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.util.TokofoodExt
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.common.util.TokofoodExt.setupEditText
import com.tokopedia.tokofood.databinding.TokofoodProductCardLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.unifycomponents.CardUnify

class ProductCardViewHolder(
    private val binding: TokofoodProductCardLayoutBinding,
    private val clickListener: OnProductCardItemClickListener,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnProductCardItemClickListener {
        fun onProductCardClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>)
        fun onAtcButtonClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>)
        fun onAddNoteButtonClicked(productId: String, orderNote: String, cardPositions: Pair<Int, Int>)
        fun onDeleteButtonClicked(cartId: String, productId: String, cardPositions: Pair<Int, Int>)
        fun onUpdateProductQty(cartId: String, quantity: Int, cardPositions: Pair<Int, Int>)
        fun onIncreaseQtyButtonClicked(cartId: String, quantity: Int, cardPositions: Pair<Int, Int>)
        fun onDecreaseQtyButtonClicked(cartId: String, quantity: Int, cardPositions: Pair<Int, Int>)
        fun onImpressProductCard(productListItem: ProductListItem, position: Int)
    }

    private var context: Context? = null
    private var productListItem: ProductListItem? = null

    init {
        context = binding.root.context
        binding.root.setOnClickListener {
            // open product bottom sheet
            val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
            productListItem?.let { productListItem ->
                clickListener.onProductCardClicked(
                    productListItem = productListItem,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
                )
            }
        }
        binding.atcButton.setOnClickListener {
            val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
            productListItem?.let { productListItem ->
                clickListener.onAtcButtonClicked(
                    productListItem = productListItem,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
                )
            }
        }
        binding.addCatatanButton.setOnClickListener {
            val productUiModel = binding.root.getTag(com.tokopedia.tokofood.R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
            clickListener.onAddNoteButtonClicked(
                productId = productUiModel.id,
                orderNote = productUiModel.orderNote,
                cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
        binding.removeProductFromCartButton.setOnClickListener {
            val productUiModel = binding.root.getTag(com.tokopedia.tokofood.R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
            clickListener.onDeleteButtonClicked(
                cartId = productUiModel.cartId,
                productId = productUiModel.id,
                cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }

        binding.qeuProductQtyEditor.setupEditText()
        binding.qeuProductQtyEditor.maxValue = TokofoodExt.MAXIMUM_QUANTITY
        binding.qeuProductQtyEditor.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val productUiModel = binding.root.getTag(com.tokopedia.tokofood.R.id.product_ui_model) as ProductUiModel
                val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
                val quantity = binding.qeuProductQtyEditor.getValue().orZero()
                if (quantity != productUiModel.orderQty && quantity >= Int.ONE) {
                    clickListener.onUpdateProductQty(
                            cartId = productUiModel.cartId,
                            quantity = quantity,
                            cardPositions = Pair(dataSetPosition, adapterPosition)
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.qeuProductQtyEditor.setAddClickListener {
            val productUiModel = binding.root.getTag(com.tokopedia.tokofood.R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
            val quantity = binding.qeuProductQtyEditor.getValue()
            clickListener.onIncreaseQtyButtonClicked(
                cartId = productUiModel.cartId,
                quantity = quantity,
                cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
        binding.qeuProductQtyEditor.setSubstractListener {
            val productUiModel = binding.root.getTag(com.tokopedia.tokofood.R.id.product_ui_model) as ProductUiModel
            val dataSetPosition = binding.root.getTag(com.tokopedia.tokofood.R.id.dataset_position) as Int
            val quantity = binding.qeuProductQtyEditor.getValue()
            binding.qeuProductQtyEditor.subtractButton.isEnabled = quantity != Int.ONE
            clickListener.onDecreaseQtyButtonClicked(
                    cartId = productUiModel.cartId,
                    quantity = quantity,
                    cardPositions = Pair(dataSetPosition, adapterPosition)
            )
        }
    }

    fun bindData(productListItem: ProductListItem, productUiModel: ProductUiModel, dataSetPosition: Int) {
        // bind product ui model and data set position
        this.productListItem = productListItem
        bindImpressionProductListener(productListItem, dataSetPosition)
        binding.root.setTag(com.tokopedia.tokofood.R.id.product_ui_model, productUiModel)
        binding.root.setTag(com.tokopedia.tokofood.R.id.dataset_position, dataSetPosition)

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
                binding.productCell.cardType = CardUnify.TYPE_SHADOW_ACTIVE
            } else {
                binding.productCell.cardType = CardUnify.TYPE_SHADOW
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
                    val addNoteIcon = ContextCompat.getDrawable(this, com.tokopedia.tokofood.R.drawable.ic_add_note)
                    binding.iuAddNote.setImageDrawable(addNoteIcon)
                } else {
                    val addNoteIcon = ContextCompat.getDrawable(this, com.tokopedia.tokofood.R.drawable.ic_edit_note)
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
        binding.root.addAndReturnImpressionListener(productListItem, tokofoodScrollChangedListener) {
            clickListener.onImpressProductCard(productListItem, dataSetPosition)
        }
    }

}
