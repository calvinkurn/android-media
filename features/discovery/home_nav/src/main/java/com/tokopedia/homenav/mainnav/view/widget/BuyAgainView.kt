package com.tokopedia.homenav.mainnav.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.homenav.databinding.LayoutLightProductCardAtcBinding
import com.tokopedia.homenav.R
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadImageRounded

typealias BuyAgainModel = BuyAgainView.Data

class BuyAgainView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private val binding = LayoutLightProductCardAtcBinding
        .inflate(LayoutInflater.from(context))

    private var listener: Listener? = null

    init {
        addView(binding.root)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun bind(data: Data) {
        setupClickableListener(data)

        with(data) {
            renderProductImage(bannerUrl)
            renderProductPrice(price)
            renderDiscountIfAny(discount, slashPrice)
        }
    }

    private fun setupClickableListener(data: Data) {
        binding.root.setOnClickListener {
            listener?.onProductCardClicked(data)
        }

        binding.btnAtc.setOnClickListener {
            listener?.onAtcButtonClicked(data)
        }
    }

    private fun renderProductImage(url: String) {
        val cornerRadius = context.resources.getDimension(R.dimen.corner_radius_light_product_card)
        binding.imgProduct.loadImageRounded(url, cornerRadius)
    }

    private fun renderProductPrice(price: String) {
        binding.txtPrice.text = price
    }

    private fun renderDiscountIfAny(discount: String, slashPrice: String) {
        if (discount.isEmpty()) return

        binding.txtSlashPrice.text = slashPrice
        binding.txtDiscount.text = discount

        binding.txtSlashPrice.strikethrough()
        binding.txtSlashPrice.show()
        binding.txtDiscount.show()
    }

    data class Data(
        val productName: String,
        val productId: String,
        val cartId: String,
        val shopId: String,
        val shopName: String,
        val shopType: String,
        val hasVariant: Boolean,
        val bannerUrl: String,
        val price: String,
        val priceInt: String,
        val slashPrice: String,
        val discount: String,
        val category: String,
        val categoryChildId: Int,
        val variant: String = "",
    )

    interface Listener {
        fun onProductCardClicked(model: BuyAgainModel)
        fun onAtcButtonClicked(model: BuyAgainModel)
    }
}
