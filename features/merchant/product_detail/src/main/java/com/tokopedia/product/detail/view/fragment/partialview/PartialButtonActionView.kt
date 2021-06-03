package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.drawable.Drawable
import android.view.View
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.view.listener.PartialButtonActionListener
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*


class PartialButtonActionView private constructor(val view: View,
                                                  private val buttonListener: PartialButtonActionListener) {
    var visibility: Boolean = false
        set(value) {
            field = value
            with(view) {
                if (value) base_btn_action.visible() else base_btn_action.gone()
            }
        }
    var hasComponentLoading = false
    var isExpressCheckout = false
    var isWarehouseProduct: Boolean = false
    var hasShopAuthority: Boolean = false
    var hasTopAdsActive: Boolean = false
    var isShopOwner: Boolean = false
    var preOrder: PreOrder? = PreOrder()
    var onSuccessGetCartType = false
    var cartTypeData: CartTypeData? = null
    var miniCartItem: MiniCartItem? = null
    var isVariant: Boolean = false
    var minQuantity: Int = 0

    private val qtyButtonPdp = view.findViewById<QuantityEditorUnify>(R.id.qty_editor_pdp)

    companion object {
        fun build(_view: View, _buttonListener: PartialButtonActionListener) = PartialButtonActionView(_view, _buttonListener)
    }

    fun setButtonP1(preOrder: PreOrder?, isLeasing: Boolean) {
        this.preOrder = preOrder
    }

    fun setTopAdsButton(hasTopAdsActive: Boolean) {
        this.hasTopAdsActive = hasTopAdsActive
        updateTopAdsButton()
    }

    fun renderData(isWarehouseProduct: Boolean,
                   hasShopAuthority: Boolean,
                   isShopOwner: Boolean,
                   hasTopAdsActive: Boolean,
                   isVariant: Boolean,
                   minQuantity: Int = 1,
                   cartTypeData: CartTypeData? = null,
                   miniCartItem: MiniCartItem? = null) {

        this.isWarehouseProduct = isWarehouseProduct
        this.hasShopAuthority = hasShopAuthority
        this.hasTopAdsActive = hasTopAdsActive
        this.cartTypeData = cartTypeData
        this.isShopOwner = isShopOwner
        this.onSuccessGetCartType = cartTypeData != null && cartTypeData.availableButtons.isNotEmpty()
        this.miniCartItem = miniCartItem
        this.isVariant = isVariant
        this.minQuantity = minQuantity
        renderButton()
    }

    private fun updateTopAdsButton() {
        if (hasShopAuthority) {
            showShopManageButton()
        }
    }

    private fun renderButton() {
        if (hasShopAuthority) {
            showShopManageButton()
        } else if (!GlobalConfig.isSellerApp() && onSuccessGetCartType) {
            showCartTypeButton()
        } else if (!GlobalConfig.isSellerApp() && !onSuccessGetCartType) {
            if (isWarehouseProduct) {
                showWarehouseButton()
            } else {
                showNewCheckoutButton()
            }
        }
    }

    private fun showCartTypeButton() = with(view) {
        hideButtonEmptyAndTopAds()

        if (!isVariant && miniCartItem != null) {
            renderQuantityButton(miniCartItem?.quantity ?: 1)
        } else if (isVariant && miniCartItem != null) {

        } else {
            renderNormalButtonCartRedirection()
        }
    }

    private fun renderNormalButtonCartRedirection(shouldChangeText: Boolean = false) = with(view) {
        val unavailableButton = cartTypeData?.unavailableButtons ?: listOf()
        val availableButton = cartTypeData?.availableButtons ?: listOf()

        btn_topchat.showWithCondition(ProductDetailCommonConstant.KEY_CHAT !in unavailableButton)

        btn_buy_now.showWithCondition(availableButton.firstOrNull() != null)
        btn_add_to_cart.showWithCondition(availableButton.getOrNull(1) != null)

        btn_buy_now.text = availableButton.getOrNull(0)?.text ?: ""
        btn_add_to_cart.text = availableButton.getOrNull(1)?.text ?: ""

        btn_buy_now.setOnClickListener {
            buttonListener.buttonCartTypeClick(availableButton.getOrNull(0)?.cartType
                    ?: "", btn_buy_now.text.toString(), availableButton.getOrNull(0)?.showRecommendation
                    ?: false)
        }

        btn_add_to_cart.setOnClickListener {
            buttonListener.buttonCartTypeClick(availableButton.getOrNull(1)?.cartType
                    ?: "", btn_add_to_cart.text.toString(), availableButton.getOrNull(1)?.showRecommendation
                    ?: false)
        }

        btn_topchat.setOnClickListener {
            buttonListener.topChatButtonClicked()
        }

        btn_buy_now.generateTheme(availableButton.getOrNull(0)?.color ?: "")
        btn_add_to_cart.generateTheme(availableButton.getOrNull(1)?.color ?: "")
    }

    private fun renderQuantityButton(quantity:Int) = with(view) {
        btn_buy_now?.hide()
        btn_add_to_cart?.hide()
        qtyButtonPdp?.run {
            minValue = minQuantity
            setValue(quantity)
            show()
            setAddClickListener {
                buttonListener.updateQuantityNonVarTokoNow(getValue(), miniCartItem ?: MiniCartItem())
            }
            setSubstractListener {
                buttonListener.updateQuantityNonVarTokoNow(getValue(), miniCartItem ?: MiniCartItem())
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

    private fun showNewCheckoutButton() {
        with(view) {
            hideButtonEmptyAndTopAds()
            btn_topchat.visibility = View.VISIBLE
            btn_buy_now.text = context.getString(
                    if (preOrder?.isPreOrderActive() == true) {
                        R.string.action_preorder
                    } else {
                        if (isExpressCheckout) {
                            com.tokopedia.product.detail.common.R.string.buy_now
                        } else {
                            R.string.buy
                        }
                    })
            btn_add_to_cart.text = context.getString(com.tokopedia.product.detail.common.R.string.plus_product_to_cart)
            btn_buy_now.visible()
            btn_add_to_cart.visible()

            btn_buy_now.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buttonListener.buyNowClick(btn_buy_now.text.toString())
            }
            btn_add_to_cart.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buttonListener.addToCartClick(btn_add_to_cart.text.toString())
            }

            btn_buy_now.generateTheme(ProductDetailCommonConstant.KEY_BUTTON_SECONDARY)
            btn_add_to_cart.generateTheme(ProductDetailCommonConstant.KEY_BUTTON_PRIMARY)
            btn_topchat.setOnClickListener {
                buttonListener.topChatButtonClicked()
            }
        }
    }

    private fun hideButtonEmptyAndTopAds() = with(view) {
        btn_empty_stock.hide()
        seller_button_container.hide()
    }

    private fun showShopManageButton() {
        with(view) {
            btn_empty_stock.hide()
            btn_topchat.hide()
            btn_buy_now.hide()
            btn_add_to_cart.hide()

            seller_button_container.show()
            if (hasTopAdsActive) {
                btn_top_ads.setOnClickListener { buttonListener.rincianTopAdsClicked() }
                btn_top_ads.text = context.getString(R.string.rincian_topads)
            } else {
                btn_top_ads.setOnClickListener { buttonListener.advertiseProductClicked() }
                btn_top_ads.text = context.getString(R.string.promote_topads)
            }
            btn_edit_product.setOnClickListener { buttonListener.editProductButtonClicked() }
        }
    }

    private fun showNoStockButton() {
        with(view) {
            seller_button_container.hide()
            btn_empty_stock.show()
            btn_topchat.showWithCondition(!isShopOwner)
            btn_topchat.setOnClickListener { buttonListener.topChatButtonClicked() }
        }
    }

    private fun showWarehouseButton() {
        if (isShopOwner) {
            showShopManageButton()
        } else {
            showNoStockButton()
        }
    }

    fun gone() {
        view.base_btn_action.gone()
    }

    fun setBackground(resource: Int) {
        view.base_btn_action.setBackgroundResource(resource)
    }

    fun setBackground(drawable: Drawable) {
        view.base_btn_action.background = drawable
    }
}
