package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.homenav.databinding.LayoutLightProductCardAtcBinding
import com.tokopedia.homenav.R
import com.tokopedia.media.loader.loadImageRounded

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
        setupClickableListener()

        with(data) {
            renderProductImage(bannerUrl)
            renderProductPrice(price)
            renderDiscountIfAny(discount, slashPrice)
        }
    }

    private fun setupClickableListener() {
        binding.root.setOnClickListener { listener?.onProductCardClicked() }
        binding.btnAtc.setOnClickListener { listener?.onAtcButtonClicked() }
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
    }

    data class Data(
        val productId: String,
        val bannerUrl: String,
        val price: String,
        val slashPrice: String,
        val discount: String
    )

    interface Listener {
        fun onProductCardClicked()
        fun onAtcButtonClicked()
    }
}
