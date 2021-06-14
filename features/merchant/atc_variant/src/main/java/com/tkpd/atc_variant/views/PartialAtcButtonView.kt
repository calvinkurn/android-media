package com.tkpd.atc_variant.views

import android.view.View
import com.tkpd.atc_variant.R
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Yehezkiel on 17/05/21
 */
class PartialAtcButtonView private constructor(val view: View,
                                               private val buttonListener: PartialAtcButtonListener) {

    private val btnBuy = view.findViewById<UnifyButton>(R.id.btn_buy_variant)
    private val btnAtc = view.findViewById<UnifyButton>(R.id.btn_atc_variant)
    private var isShopOwner: Boolean = false

    var visibility: Boolean = false
        set(value) {
            field = value
            with(view) {
                if (value) visible() else gone()
            }
        }

    companion object {
        fun build(_view: View, _buttonListener: PartialAtcButtonListener) = PartialAtcButtonView(_view, _buttonListener)
    }

    fun renderButtonView(isProductBuyable: Boolean, isShopOwner: Boolean, cartTypeData: CartTypeData? = null) {
        this.isShopOwner = isShopOwner
        val onSuccessGetCartType = cartTypeData != null && cartTypeData.availableButtons.isNotEmpty()
        renderButton(onSuccessGetCartType, isShopOwner, isProductBuyable, cartTypeData)
    }

    private fun renderButton(isCartRedirectionNotEmpty: Boolean, isShopOwner: Boolean, isProductBuyable: Boolean, cartRedirectionData: CartTypeData? = null) {
        val shouldRenderCartRedirection = !GlobalConfig.isSellerApp() && isCartRedirectionNotEmpty
        val shouldRenderFallbackButton = !GlobalConfig.isSellerApp() && !isCartRedirectionNotEmpty

        if (isShopOwner || cartRedirectionData?.hideFloatingButton == true) {
            visibility = false
            return
        }

        if (shouldRenderCartRedirection) {
            renderCartRedirectionButton(cartRedirectionData)
        } else if (shouldRenderFallbackButton) {
            if (isProductBuyable) {
                renderFallbackCheckoutButton()
            } else {
                showNoStockButton()
            }
        }
        visibility = true
    }

    private fun renderCartRedirectionButton(cartRedirectionData: CartTypeData?) {
        val availableButton = cartRedirectionData?.availableButtons ?: listOf()

        btnBuy.run {
            val firstButton = availableButton.firstOrNull()
            showWithCondition(firstButton != null)
            generateTheme(firstButton?.color ?: "")
            val fallbackTextIfEmpty = if (firstButton?.cartType == ProductDetailCommonConstant.KEY_CHECK_WISHLIST) "Cek Wishlist" else "+Keranjang"
            val textFirstButton = availableButton.getOrNull(0)?.text ?: fallbackTextIfEmpty
            text = textFirstButton
            setOnClickListener {
                buttonListener.buttonCartTypeClick(availableButton.getOrNull(0)?.cartType
                        ?: "", text.toString(), availableButton.getOrNull(0)?.showRecommendation
                        ?: false)
            }

        }

        btnAtc.run {
            val secondButton = availableButton.getOrNull(1)
            showWithCondition(secondButton != null)
            generateTheme(secondButton?.color ?: "")
            val fallbackTextIfEmpty = if (secondButton?.cartType == ProductDetailCommonConstant.KEY_CHECK_WISHLIST) "Cek Wishlist" else "+Keranjang"
            text = availableButton.getOrNull(1)?.text ?: fallbackTextIfEmpty
            setOnClickListener {
                buttonListener.buttonCartTypeClick(availableButton.getOrNull(1)?.cartType
                        ?: "", text.toString(), availableButton.getOrNull(1)?.showRecommendation
                        ?: false)
            }
        }
    }

    private fun UnifyButton.generateTheme(colorDescription: String) {
        when (colorDescription) {
            ProductDetailCommonConstant.KEY_BUTTON_PRIMARY -> {
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonType = UnifyButton.Type.TRANSACTION
                this.isEnabled = true
            }
            ProductDetailCommonConstant.KEY_BUTTON_DISABLE -> {
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonType = UnifyButton.Type.MAIN
                this.isEnabled = false
            }
            ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN -> {
                this.buttonVariant = UnifyButton.Variant.FILLED
                this.buttonType = UnifyButton.Type.MAIN
                this.isEnabled = true
            }
            ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GREEN -> {
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.buttonType = UnifyButton.Type.MAIN
                this.isEnabled = true
            }
            ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GRAY -> {
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.buttonType = UnifyButton.Type.ALTERNATE
                this.isEnabled = true
            }
            else -> {
                this.buttonVariant = UnifyButton.Variant.GHOST
                this.buttonType = UnifyButton.Type.TRANSACTION
                this.isEnabled = true
            }
        }
    }

    private fun renderFallbackCheckoutButton() {
        btnBuy.run {
            show()
            generateTheme(ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN)
            text = context.getString(com.tokopedia.product.detail.common.R.string.buy_now)
            setOnClickListener {
                buttonListener.buyNowClick(text.toString())
            }
        }

        btnAtc.run {
            show()
            generateTheme(ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GREEN)
            text = context.getString(com.tokopedia.product.detail.common.R.string.plus_product_to_cart)
            setOnClickListener {
                buttonListener.addToCartClick(text.toString())
            }
        }
    }

    private fun showNoStockButton() {
        btnBuy.run {
            text = context.getString(com.tokopedia.product.detail.common.R.string.empty_stock_atc)
            show()
            generateTheme(ProductDetailCommonConstant.KEY_BUTTON_DISABLE)
        }

        btnAtc.hide()
    }
}

interface PartialAtcButtonListener {
    fun buttonCartTypeClick(cartType: String, buttonText: String, isAtcButton: Boolean)

    //Listener for fallback button
    fun addToCartClick(buttonText: String)
    fun buyNowClick(buttonText: String)
}