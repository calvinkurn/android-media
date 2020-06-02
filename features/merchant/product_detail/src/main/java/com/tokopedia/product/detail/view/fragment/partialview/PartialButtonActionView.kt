package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.drawable.Drawable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*


class PartialButtonActionView private constructor(val view: View,
                                                  private val listener: View.OnClickListener)
    : View.OnClickListener by listener {
    var promoTopAdsClick: (() -> Unit)? = null
    var rincianTopAdsClick: (() -> Unit)? = null
    var buyNowClick: ((String) -> Unit)? = null
    var addToCartClick: ((String) -> Unit)? = null
    var buttonCartTypeClick: ((String, String, Boolean) -> Unit)? = null

    var byMeClick: ((TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate, Boolean) -> Unit)? = null
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
    var isLeasing: Boolean = false
    var hasTopAdsActive: Boolean = false
    var isShopOwner: Boolean = false
    var preOrder: PreOrder? = PreOrder()
    var onSuccessGetCartType = false
    var showByMe = false
    var cartTypeData: CartTypeData? = null

    companion object {
        fun build(_view: View, _listener: View.OnClickListener) = PartialButtonActionView(_view, _listener)
    }

    fun setButtonP1(preOrder: PreOrder?, isLeasing: Boolean) {
        this.preOrder = preOrder
        this.isLeasing = isLeasing
    }

    fun setTopAdsButton(hasTopAdsActive: Boolean) {
        this.hasTopAdsActive = hasTopAdsActive
    }

    fun renderData(isWarehouseProduct: Boolean, hasShopAuthority: Boolean, isShopOwner: Boolean, hasTopAdsActive: Boolean, cartTypeData: CartTypeData? = null) {
        this.isWarehouseProduct = isWarehouseProduct
        this.hasShopAuthority = hasShopAuthority
        this.hasTopAdsActive = hasTopAdsActive
        this.cartTypeData = cartTypeData
        this.isShopOwner = isShopOwner
        this.onSuccessGetCartType = cartTypeData != null && cartTypeData.availableButtons.isNotEmpty()
        renderButton()
    }

    private fun renderButton() {
        if (isWarehouseProduct) {
            showNoStockButton()
        } else if (hasShopAuthority) {
            showShopManageButton()
        } else if (!GlobalConfig.isSellerApp() && onSuccessGetCartType) {
            showCartTypeButton()
        } else if (!GlobalConfig.isSellerApp() && !onSuccessGetCartType) {
            showNewCheckoutButton()
        }
    }

    private fun showCartTypeButton() = with(view) {
        hideButtonEmptyAndTopAds()
        resetTopChatLayoutParams()

        val unavailableButton = cartTypeData?.unavailableButtons ?: listOf()
        val availableButton = cartTypeData?.availableButtons ?: listOf()

        btn_topchat.showWithCondition(ProductDetailConstant.KEY_CHAT !in unavailableButton)
        btn_byme.showWithCondition(showByMe && ProductDetailConstant.KEY_BYME !in unavailableButton)

        btn_buy_now.showWithCondition(availableButton.firstOrNull() != null)
        btn_add_to_cart.showWithCondition(availableButton.getOrNull(1) != null)

        btn_buy_now.text = availableButton.getOrNull(0)?.text ?: ""
        btn_add_to_cart.text = availableButton.getOrNull(1)?.text ?: ""

        btn_buy_now.setOnClickListener {
            buttonCartTypeClick?.invoke(availableButton.getOrNull(0)?.cartType
                    ?: "", btn_buy_now.text.toString(), availableButton.getOrNull(0)?.showRecommendation
                    ?: false)
        }

        btn_add_to_cart.setOnClickListener {
            buttonCartTypeClick?.invoke(availableButton.getOrNull(1)?.cartType
                    ?: "", btn_add_to_cart.text.toString(), availableButton.getOrNull(1)?.showRecommendation
                    ?: false)
        }

        btn_topchat.setOnClickListener(this@PartialButtonActionView)
        btn_apply_leasing.setOnClickListener(this@PartialButtonActionView)
        btn_buy_now.generateTheme(availableButton.getOrNull(0)?.color ?: "")
        btn_add_to_cart.generateTheme(availableButton.getOrNull(1)?.color ?: "")
    }

    private fun UnifyButton.generateTheme(colorDescription: String) {
        if (colorDescription == ProductDetailConstant.KEY_BUTTON_PRIMARY) {
            this.buttonVariant = UnifyButton.Variant.FILLED
            this.buttonType = UnifyButton.Type.TRANSACTION
        } else {
            this.buttonVariant = UnifyButton.Variant.GHOST
            this.buttonType = UnifyButton.Type.TRANSACTION
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
                            R.string.buy_now
                        } else {
                            R.string.buy
                        }
                    })
            btn_add_to_cart.text = context.getString(R.string.plus_product_to_cart)
            btn_buy_now.visibility = View.VISIBLE
            btn_add_to_cart.visible()
            if (isLeasing) {
                btn_apply_leasing.visibility = View.VISIBLE
                btn_add_to_cart.visibility = View.GONE
                btn_buy_now.visibility = View.GONE
                changeTopChatLayoutParamsToHandleLeasingLayout()
            } else {
                resetTopChatLayoutParams()
                btn_apply_leasing.visibility = View.GONE
            }
            btn_buy_now.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buyNowClick?.invoke(btn_buy_now.text.toString())
            }
            btn_add_to_cart.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                addToCartClick?.invoke(btn_add_to_cart.text.toString())
            }

            btn_buy_now.generateTheme(ProductDetailConstant.KEY_BUTTON_SECONDARY)
            btn_add_to_cart.generateTheme(ProductDetailConstant.KEY_BUTTON_PRIMARY)
            btn_topchat.setOnClickListener(this@PartialButtonActionView)
            btn_apply_leasing.setOnClickListener(this@PartialButtonActionView)
        }
    }

    private fun resetTopChatLayoutParams() {
        with(view) {
            val topChatParams = btn_topchat.layoutParams as ConstraintLayout.LayoutParams
            topChatParams.startToEnd = btn_byme.id
            topChatParams.startToStart = UNSET
            topChatParams.rightToLeft = btn_buy_now.id
            topChatParams.endToStart = btn_buy_now.id
            btn_topchat.layoutParams = topChatParams
        }
    }

    private fun changeTopChatLayoutParamsToHandleLeasingLayout() {
        with(view) {
            val topChatParams = btn_topchat.layoutParams as ConstraintLayout.LayoutParams
            topChatParams.startToEnd = UNSET
            topChatParams.startToStart = PARENT_ID
            topChatParams.rightToLeft = btn_apply_leasing.id
            topChatParams.endToStart = btn_apply_leasing.id
            btn_topchat.layoutParams = topChatParams
        }
    }

    private fun changeTopChatLayoutParamsToHandleWarehouseButton() = with(view) {
        val topChatParams = btn_topchat.layoutParams as ConstraintLayout.LayoutParams
        topChatParams.startToEnd = UNSET
        topChatParams.startToStart = PARENT_ID
        topChatParams.rightToLeft = btn_empty_stock.id
        topChatParams.endToStart = btn_empty_stock.id
        btn_topchat.layoutParams = topChatParams
    }

    private fun hideButtonEmptyAndTopAds() = with(view) {
        btn_empty_stock.hide()
        btn_top_ads.hide()
    }

    private fun showShopManageButton() {
        with(view) {
            btn_empty_stock.hide()
            btn_topchat.hide()
            btn_top_ads.show()
            if (hasTopAdsActive) {
                btn_top_ads.setOnClickListener { rincianTopAdsClick?.invoke() }
                btn_top_ads.text = context.getString(R.string.rincian_topads)
                btn_top_ads.buttonVariant = UnifyButton.Variant.GHOST
            } else {
                btn_top_ads.setOnClickListener { promoTopAdsClick?.invoke() }
                btn_top_ads.text = context.getString(R.string.promote_topads)
                btn_top_ads.buttonVariant = UnifyButton.Variant.FILLED
            }
        }
    }

    private fun showNoStockButton() {
        with(view) {
            changeTopChatLayoutParamsToHandleWarehouseButton()
            btn_byme.hide()
            btn_top_ads.hide()
            btn_empty_stock.show()
            btn_topchat.showWithCondition(!isShopOwner)
            btn_topchat.setOnClickListener(this@PartialButtonActionView)
        }
    }

    fun gone() {
        view.base_btn_action.gone()
    }

    fun visible() {
        view.base_btn_action.visible()
    }

    fun showByMe(show: Boolean, pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate) {
        with(view) {
            if (show && !isWarehouseProduct) {
                showByMe = true
                btn_byme.setOnClickListener { byMeClick?.invoke(pdpAffiliate, true) }
                btn_byme.visible()
            } else {
                showByMe = false
                btn_byme.gone()
            }
        }
    }

    fun setBackground(resource: Int) {
        view.base_btn_action.setBackgroundResource(resource)
    }

    fun setBackground(drawable: Drawable) {
        view.base_btn_action.background = drawable
    }
}
