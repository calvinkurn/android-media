package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
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
    private var hasComponentLoading = false
    private var isExpressCheckout = false
    private var isWarehouseProduct: Boolean = false
    private var hasShopAuthority: Boolean = false
    private var hasTopAdsActive: Boolean = false
    private var isShopOwner: Boolean = false
    private var preOrder: PreOrder? = PreOrder()
    private var onSuccessGetCartType = false
    private var cartTypeData: CartTypeData? = null
    private var miniCartItem: MiniCartItem? = null
    private var isVariant: Boolean = false
    private var minQuantity: Int = 0
    private var alternateButtonVariant: String = ""
    private var textWatchers: TextWatcher? = null
    private var localQuantity: Int = 0

    private val qtyButtonPdp = view.findViewById<QuantityEditorUnify>(R.id.qty_editor_pdp)

    companion object {
        fun build(_view: View, _buttonListener: PartialButtonActionListener) = PartialButtonActionView(_view, _buttonListener)
    }

    fun setButtonP1(preOrder: PreOrder?) {
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
                   alternateButtonVariant: String = "",
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
        this.alternateButtonVariant = alternateButtonVariant
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
        } else if (isVariant && alternateButtonVariant.isNotEmpty()) {
            renderNormalButtonCartRedirection(alternateButtonVariant)
        } else {
            renderNormalButtonCartRedirection()
        }

        val unavailableButton = cartTypeData?.unavailableButtons ?: listOf()
        btn_topchat.showWithCondition(ProductDetailCommonConstant.KEY_CHAT !in unavailableButton)
        btn_topchat.setOnClickListener {
            buttonListener.topChatButtonClicked()
        }

    }

    private fun renderNormalButtonCartRedirection(alternateButtonVariant: String = "") = with(view) {
        qtyButtonPdp.hide()
        val availableButton = cartTypeData?.availableButtons?.map {
            if (alternateButtonVariant.isNotEmpty()) {
                it.copy(text = alternateButtonVariant)
            } else {
                it
            }
        } ?: listOf()

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

        btn_buy_now.generateTheme(availableButton.getOrNull(0)?.color ?: "")
        btn_add_to_cart.generateTheme(availableButton.getOrNull(1)?.color ?: "")
    }

    private fun renderQuantityButton(quantity: Int) = with(view) {
        localQuantity = quantity
        btn_buy_now?.hide()
        btn_add_to_cart?.hide()
        qtyButtonPdp?.run {
            minValue = minQuantity
            setValue(localQuantity)

            if (textWatchers != null) {
                editText.removeTextChangedListener(textWatchers)
            }

            textWatchers = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().toIntOrZero() < minQuantity) {
                        setValue(minQuantity)
                    } else if (s.toString().toIntOrZero() > maxValue) {
                        setValue(maxValue)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (localQuantity != s.toString().toIntOrZero() && s.toString().isNotEmpty()) {
                        localQuantity = s.toString().toIntOrZero()
                        buttonListener.updateQuantityNonVarTokoNow(getValue(), miniCartItem
                                ?: MiniCartItem()
                        )
                        setValue(localQuantity)
                        //fire again to update + and - button
                    }
                }
            }
            editText.addTextChangedListener(textWatchers)
            show()
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
            qtyButtonPdp.hide()
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
            qtyButtonPdp.hide()

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
            qtyButtonPdp.hide()
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
