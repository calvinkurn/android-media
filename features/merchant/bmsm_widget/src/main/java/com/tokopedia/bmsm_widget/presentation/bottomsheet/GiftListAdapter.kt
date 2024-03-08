package com.tokopedia.bmsm_widget.presentation.bottomsheet

import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.bmsm_widget.databinding.ItemBmsmGiftWithRibbonBinding
import com.tokopedia.bmsm_widget.databinding.ItemBmsmGiftWithoutRibbonBinding
import com.tokopedia.bmsm_widget.domain.entity.TierGift.GiftProduct
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loader.loadImage

class GiftListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val OVERLAY_CORNER_RADIUS = 12F
        private const val VIEW_TYPE_GIFT_WITHOUT_RIBBON = 0
        private const val VIEW_TYPE_GIFT_WITH_RIBBON = 1
    }

    private var onGiftClick: (Int) -> Unit = {}
    private var ribbonText = ""

    private val differCallback = object : DiffUtil.ItemCallback<GiftProduct>() {
        override fun areItemsTheSame(oldItem: GiftProduct, newItem: GiftProduct): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: GiftProduct, newItem: GiftProduct): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GIFT_WITHOUT_RIBBON ->      {
                val binding = ItemBmsmGiftWithoutRibbonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GiftWithoutRibbonViewHolder(binding)
            }
            VIEW_TYPE_GIFT_WITH_RIBBON -> {
                val binding = ItemBmsmGiftWithRibbonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GiftWithRibbonViewHolder(binding)
            }
            else -> {
                val binding = ItemBmsmGiftWithoutRibbonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GiftWithoutRibbonViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gift = differ.currentList[position]
        when (getItemViewType(position)) {
            VIEW_TYPE_GIFT_WITHOUT_RIBBON -> (holder as? GiftWithoutRibbonViewHolder)?.bind(gift)
            VIEW_TYPE_GIFT_WITH_RIBBON -> (holder as? GiftWithRibbonViewHolder)?.bind(gift)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val giftProduct = snapshot()[position]

        val showRibbonAndQuantity = ribbonText.isNotEmpty() && giftProduct.quantity.isMoreThanZero() && !giftProduct.isOutOfStock

        return if (showRibbonAndQuantity) VIEW_TYPE_GIFT_WITH_RIBBON else VIEW_TYPE_GIFT_WITHOUT_RIBBON
    }
    
    inner class GiftWithoutRibbonViewHolder(private val binding: ItemBmsmGiftWithoutRibbonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onGiftClick(bindingAdapterPosition) }
        }

        fun bind(giftProduct: GiftProduct) {
            binding.run {
                imgGift.loadImage(giftProduct.productImageUrl)
                tpgGiftName.text = giftProduct.productName

                val showQuantity = giftProduct.quantity.isMoreThanZero() && !giftProduct.isOutOfStock
                renderQuantity(showQuantity, giftProduct.quantity)

                renderOutOfStockLabel(giftProduct.isOutOfStock)
                renderImageOverlay(giftProduct.isOutOfStock)
            }
        }

        private fun renderImageOverlay(isOutOfStock: Boolean) {
            val overlay = binding.backgroundGrayOverlay
            overlay.isVisible = isOutOfStock
            overlay.outlineProvider = SemiTransparentOutlineProvider(OVERLAY_CORNER_RADIUS)
            overlay.clipToOutline = true
        }
        

        private fun renderQuantity(showQuantity: Boolean, quantity: Int) {
            binding.layoutQuantityCounter.isVisible = showQuantity
            binding.tpgQuantityCounter.text = quantity.toString()
        }

        private fun renderOutOfStockLabel(isOutOfStock: Boolean) {
            binding.tpgOutOfStock.isVisible = isOutOfStock
        }
    }

    inner class GiftWithRibbonViewHolder(private val binding: ItemBmsmGiftWithRibbonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onGiftClick(bindingAdapterPosition) }
        }

        fun bind(giftProduct: GiftProduct) {
            binding.run {
                imgGift.loadImage(giftProduct.productImageUrl)
                tpgGiftName.text = giftProduct.productName
                
                renderQuantity(giftProduct.quantity.isMoreThanZero(), giftProduct.quantity)
                renderRibbon(ribbonText)

                renderOutOfStockLabel(giftProduct.isOutOfStock)
                renderImageOverlay(giftProduct.isOutOfStock)
            }
        }

        private fun renderImageOverlay(isOutOfStock: Boolean) {
            val overlay = binding.backgroundGrayOverlay
            overlay.isVisible = isOutOfStock
            overlay.outlineProvider = SemiTransparentOutlineProvider(OVERLAY_CORNER_RADIUS)
            overlay.clipToOutline = true
        }

        private fun renderRibbon(ribbonText: String) {
            binding.ribbonView.setText(ribbonText)
        }

        private fun renderQuantity(showQuantity: Boolean, quantity: Int) {
            binding.layoutQuantityCounter.isVisible = showQuantity
            binding.tpgQuantityCounter.text = quantity.toString()
        }

        private fun renderOutOfStockLabel(isOutOfStock: Boolean) {
            binding.tpgOutOfStock.isVisible = isOutOfStock
        }
    }

    private class SemiTransparentOutlineProvider(private val cornerRadius: Float) : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setRoundRect(0, 0, view?.width ?: 0, view?.height ?: 0, cornerRadius.toPx())
        }
    }

    fun setOnGiftClick(onGiftClick: (Int) -> Unit) {
        this.onGiftClick = onGiftClick
    }

    fun snapshot(): List<GiftProduct> {
        return differ.currentList
    }

    fun submit(newProducts: List<GiftProduct>) {
        differ.submitList(newProducts)
    }

    fun setRibbonText(ribbonText: String) {
        this.ribbonText = ribbonText
    }
}
