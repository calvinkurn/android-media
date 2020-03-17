package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirection
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*


class PartialButtonActionView private constructor(private val view: View,
                                                  private val listener: View.OnClickListener)
    : View.OnClickListener by listener {
    var promoTopAdsClick: (() -> Unit)? = null
    var rincianTopAdsClick: (() -> Unit)? = null
    var buyNowClick: (() -> Unit)? = null
    var addToCartClick: (() -> Unit)? = null
    var byMeClick: ((TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate, Boolean) -> Unit)? = null
    var visibility: Boolean = false
        set(value) {
            field = value
            with(view) {
                if (value) base_btn_action.visible() else base_btn_action.gone()
            }
        }
    var buttonCartTypeClick: ((String, Boolean) -> Unit)? = null

    var hasComponentLoading = false
    var isExpressCheckout = false
    var isWarehouseProduct: Boolean = false
    var hasShopAuthority: Boolean = false
    var isLeasing: Boolean = false
    var hasTopAdsActive: Boolean = false
    var preOrder: PreOrder? = PreOrder()
    var onSuccessGetCartType = false
    private var cartTypeData: CartRedirection? = null

    companion object {
        fun build(_view: View, _listener: View.OnClickListener) = PartialButtonActionView(_view, _listener)

        private const val TOPCHAT_VARIANT_WHITE = "Icon White"
        private const val TOPCHAT_VARIANT_GREEN = "Icon Green"
        private const val TOPCHAT_VARIANT_GREEN_DOT = "Icon Green Dot"
        private const val KEY_AB_TOPCHAT = "TopChat Icon at PDP 2"
    }

    fun renderData(isWarehouseProduct: Boolean, hasShopAuthority: Boolean, preOrder: PreOrder?) {
        this.isWarehouseProduct = isWarehouseProduct
        this.hasShopAuthority = hasShopAuthority
        this.preOrder = preOrder
        renderButton()
    }

    fun renderData(isExpressCheckout: Boolean, hasTopAdsActive: Boolean, cartTypeData: CartRedirection) {
        this.isExpressCheckout = isExpressCheckout
        this.hasTopAdsActive = hasTopAdsActive
        this.cartTypeData = cartTypeData
        this.onSuccessGetCartType = cartTypeData.errorMessage.isEmpty() && cartTypeData.data.firstOrNull()?.availableButtons?.isNotEmpty() == true
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
        val unavailableButton = cartTypeData?.data?.firstOrNull()?.unavailableButtons ?: listOf()
        val availableButton = cartTypeData?.data?.firstOrNull()?.availableButtons ?: listOf()

        bindAbTestChatButton(btn_topchat)

        btn_topchat.showWithCondition("chat" !in unavailableButton)
        btn_buy_now.showWithCondition(availableButton.firstOrNull() != null)
        btn_add_to_cart.showWithCondition(availableButton.getOrNull(1) != null)

        btn_buy_now.text = availableButton.getOrNull(0)?.text ?: ""
        btn_add_to_cart.text = availableButton.getOrNull(1)?.text ?: ""

        btn_buy_now.setOnClickListener {
            buttonCartTypeClick?.invoke(availableButton.getOrNull(0)?.cartType
                    ?: "", availableButton.getOrNull(0)?.showRecommendation ?: false)
        }

        btn_add_to_cart.setOnClickListener {
            buttonCartTypeClick?.invoke(availableButton.getOrNull(1)?.cartType
                    ?: "", availableButton.getOrNull(1)?.showRecommendation ?: false)
        }

        btn_buy_now.generateTheme(availableButton.getOrNull(0)?.color ?: "")
        btn_add_to_cart.generateTheme(availableButton.getOrNull(1)?.color ?: "")
    }

    private fun UnifyButton.generateTheme(colorDescription: String) {
        if (colorDescription == "orange") {
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
            bindAbTestChatButton(btn_topchat)
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
            btn_add_to_cart.text = context.getString(R.string.add_product_to_cart)
            btn_buy_now.visibility = View.VISIBLE
            btn_add_to_cart.visible()
            if (isLeasing) {
                btn_apply_leasing.visibility = View.VISIBLE
                btn_add_to_cart.visibility = View.GONE
                btn_buy_now.visibility = View.GONE
                changeTopChatLayoutParamsToHandleLeasingLayout()
            } else {
                btn_apply_leasing.visibility = View.GONE
                resetTopChatLayoutParams()
            }
            btn_buy_now.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buyNowClick?.invoke()
            }
            btn_add_to_cart.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                addToCartClick?.invoke()
            }
            btn_topchat.setOnClickListener(this@PartialButtonActionView)
            btn_apply_leasing.setOnClickListener(this@PartialButtonActionView)
        }
    }

    private fun bindAbTestChatButton(imageView: AppCompatImageView) {
        val variant = RemoteConfigInstance.getInstance().abTestPlatform.getString(KEY_AB_TOPCHAT, "")
        val drawableRes = when (variant) {
            TOPCHAT_VARIANT_WHITE -> R.drawable.ic_topchat
            TOPCHAT_VARIANT_GREEN -> R.drawable.ic_topchat_variant_green
            TOPCHAT_VARIANT_GREEN_DOT -> R.drawable.ic_topchat_variant_green_dot
            else -> R.drawable.ic_topchat
        }

        imageView.setImageResource(drawableRes)

        if (variant == TOPCHAT_VARIANT_GREEN) {
            imageView.setBackgroundResource(R.drawable.variant_topchat_green)
        } else {
            imageView.setBackgroundResource(R.drawable.white_button_rounded)
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

    private fun hideButtonEmptyAndTopAds() = with(view) {
        btn_empty_stock.hide()
        btn_top_ads.hide()
    }

    private fun showShopManageButton() {
        with(view) {
            btn_empty_stock.hide()
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
            btn_empty_stock.visible()
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
            if (show) {
                btn_byme.setOnClickListener { byMeClick?.invoke(pdpAffiliate, true) }
                btn_byme.visible()
            } else btn_byme.gone()
        }
    }

    fun setBackground(resource: Int) {
        view.base_btn_action.setBackgroundResource(resource)
    }

    fun setBackground(drawable: Drawable) {
        view.base_btn_action.background = drawable
    }
}
