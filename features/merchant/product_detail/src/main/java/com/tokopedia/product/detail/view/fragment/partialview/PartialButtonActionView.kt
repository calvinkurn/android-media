package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*

class PartialButtonActionView private constructor(private val view: View,
                                                  private val listener: View.OnClickListener)
    : View.OnClickListener by listener {
    var promoTopAdsClick: (() -> Unit)? = null
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

    var hasComponentLoading = false
    var isExpressCheckout = false
    var isWarehouseProduct: Boolean = false
    var hasShopAuthority: Boolean = false
    var isSpecialPrize: Boolean = false
    var preOrder: PreOrder? = PreOrder()

    companion object {
        fun build(_view: View, _listener: View.OnClickListener) = PartialButtonActionView(_view, _listener)
    }

    init {
        with(view) {
            pb_buy_now.indeterminateDrawable
                .setColorFilter(ContextCompat.getColor(context, R.color.orange_red), PorterDuff.Mode.SRC_IN)
            pb_add_to_cart.indeterminateDrawable
                .setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN)
        }
    }

    fun renderData(isWarehouseProduct: Boolean, hasShopAuthority: Boolean, preOrder: PreOrder?) {
        this.isWarehouseProduct = isWarehouseProduct
        this.hasShopAuthority = hasShopAuthority
        this.preOrder = preOrder
        renderButton()
    }

    fun renderData(isExpressCheckout:Boolean) {
        this.isExpressCheckout = isExpressCheckout
        renderButton()
    }

    private fun renderButton(){
        if (isWarehouseProduct) {
            showNoStockButton()
        } else if (hasShopAuthority) {
            showShopManageButton()
        } else if (GlobalConfig.isCustomerApp()) {
            showNewCheckoutButton(preOrder)
        }
    }

    private fun showNewCheckoutButton(preOrder: PreOrder?) {
        with(view) {
            btn_buy.visibility = View.GONE
            btn_promote_topads.visibility = View.GONE
            btn_topchat.visibility = View.VISIBLE
            tv_buy_now.text = context.getString(
                if (preOrder?.isPreOrderActive() == true) {
                    R.string.action_preorder
                } else {
                    if (isExpressCheckout) {
                        R.string.buy_now
                    } else {
                        R.string.buy
                    }
                })
            btn_buy_now.visibility = View.VISIBLE
            if (isSpecialPrize) btn_add_to_cart.gone()
            else btn_add_to_cart.visible()

            btn_buy_now.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                buyNowClick?.invoke()
            }
            btn_add_to_cart.setOnClickListener {
                if (hasComponentLoading) return@setOnClickListener
                addToCartClick?.invoke()
            }
            btn_topchat.setOnClickListener(this@PartialButtonActionView)
        }
    }

    private fun showShopManageButton() {
        with(view) {
            btn_promote_topads.setOnClickListener { promoTopAdsClick?.invoke() }
            btn_buy.visibility = View.GONE
            btn_promote_topads.visibility = View.VISIBLE
            btn_byme.visibility = View.GONE
            btn_topchat.visibility = View.GONE
            btn_buy_now.visibility = View.GONE
            btn_add_to_cart.visibility = View.GONE
        }
    }

    private fun showNoStockButton() {
        with(view) {
            tv_buy.setTextColor(ContextCompat.getColor(context, R.color.black_38))
            btn_buy.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_300))
            tv_buy.text = context.getString(R.string.no_stock)
            btn_buy.isEnabled = false
            btn_buy.visibility = View.VISIBLE
            btn_promote_topads.visibility = View.GONE
            btn_byme.visibility = View.GONE
            btn_topchat.visibility = View.GONE
            btn_buy_now.visibility = View.GONE
            btn_add_to_cart.visibility = View.GONE
        }
    }

    fun showLoadingBuy() {
        hasComponentLoading = true
        view.pb_buy_now.visible()
    }

    fun hideLoadingBuy() {
        hasComponentLoading = false
        view.pb_buy_now.hide()
    }

    fun showLoadingCart() {
        hasComponentLoading = true
        view.pb_add_to_cart.visible()
    }

    fun hideLoadingCart() {
        hasComponentLoading = false
        view.pb_add_to_cart.hide()
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
