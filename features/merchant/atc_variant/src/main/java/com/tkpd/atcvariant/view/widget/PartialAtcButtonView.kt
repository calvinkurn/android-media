package com.tkpd.atcvariant.view

import android.view.View
import com.tkpd.atcvariant.R
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton.Companion.buttonText
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton.Companion.orEmpty
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.generateTheme
import com.tokopedia.product.detail.common.generateTopchatButtonPdp
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.product.detail.common.R as productdetailcommonR

/**
 * Created by Yehezkiel on 17/05/21
 */
class PartialAtcButtonView private constructor(
    val view: View,
    private val buttonListener: PartialAtcButtonListener
) {

    private val btnBuy = view.findViewById<UnifyButton>(R.id.btn_buy_variant)
    private val btnAtc = view.findViewById<UnifyButton>(R.id.btn_atc_variant)
    private val btnChat = view.findViewById<UnifyButton>(R.id.btn_chat_variant)

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

    init {
        btnChat.generateTopchatButtonPdp()
    }

    fun renderButtonView(
        isProductBuyable: Boolean,
        isShopOwner: Boolean,
        cartTypeData: CartTypeData? = null
    ) {
        this.isShopOwner = isShopOwner
        val onSuccessGetCartType =
            cartTypeData != null && cartTypeData.availableButtons.isNotEmpty()
        renderButton(onSuccessGetCartType, isShopOwner, isProductBuyable, cartTypeData)
        btnBuy.isParallelLoading = btnBuy.isVisible && btnAtc.isVisible
        btnAtc.isParallelLoading = btnBuy.isVisible && btnAtc.isVisible
    }

    fun showLoading() {
        setButtonToLoading(isLoading = true)
    }

    fun hideLoading() {
        setButtonToLoading(isLoading = false)
    }

    private fun setButtonToLoading(isLoading: Boolean) {
        btnAtc.setLoading(isLoading = isLoading)
        btnBuy.setLoading(isLoading = isLoading)
    }

    private fun UnifyButton.setLoading(isLoading: Boolean) {
        if (!isVisible) return

        this.isLoading = isLoading
    }

    private fun renderButton(
        isCartRedirectionNotEmpty: Boolean,
        isShopOwner: Boolean,
        isProductBuyable: Boolean,
        cartRedirectionData: CartTypeData? = null
    ) {
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
        val unavailableButton = cartRedirectionData?.unavailableButtons ?: listOf()

        buttonListener.onButtonShowed(availableButton.map { it.cartType }.take(2))

        btnChat?.run {
            shouldShowWithAction(ProductDetailCommonConstant.KEY_CHAT !in unavailableButton) {
                setOnClickListener {
                    buttonListener.onChatButtonClick()
                }
            }
        }

        btnBuy.run {
            val firstButton = availableButton.firstOrNull().orEmpty
            showWithCondition(firstButton.cartType.isNotBlank())
            generateTheme(firstButton.color)
            text = firstButton.buttonText

            setOnClickListener {
                if (isLoading) return@setOnClickListener

                buttonListener.buttonCartTypeClick(firstButton)
            }
        }

        btnAtc.run {
            val secondButton = availableButton.getOrNull(Int.ONE).orEmpty
            showWithCondition(secondButton.cartType.isNotBlank())
            generateTheme(secondButton.color)
            text = secondButton.buttonText

            setOnClickListener {
                if (isLoading) return@setOnClickListener

                buttonListener.buttonCartTypeClick(secondButton)
            }
        }
    }

    private fun renderFallbackCheckoutButton() {
        btnBuy.run {
            val button = AvailableButton.createBuyNowButton(context = context)
            show()
            generateTheme(button.color)
            text = button.text
            setOnClickListener {
                buttonListener.onButtonFallbackClick(button)
            }
        }

        btnAtc.run {
            val button = AvailableButton.createAddToCartButton(context = context)
            show()
            generateTheme(button.color)
            text = button.text
            setOnClickListener {
                buttonListener.onButtonFallbackClick(button)
            }
        }

        btnChat.show()
    }

    private fun showNoStockButton() {
        btnBuy.run {
            text = context.getString(productdetailcommonR.string.empty_stock_atc)
            show()
            generateTheme(ProductDetailCommonConstant.KEY_BUTTON_DISABLE)
        }

        btnChat.show()
        btnAtc.hide()
    }
}

interface PartialAtcButtonListener {
    fun buttonCartTypeClick(actionButton: AvailableButton)
    fun onChatButtonClick()
    fun onButtonShowed(buttonCartTypes: List<String>)

    // Listener for fallback button
    fun onButtonFallbackClick(actionButton: AvailableButton)
}
