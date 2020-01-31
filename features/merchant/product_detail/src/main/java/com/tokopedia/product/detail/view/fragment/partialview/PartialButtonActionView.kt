package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat.setTextAppearance
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.PreOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance
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

    var hasComponentLoading = false
    var isExpressCheckout = false
    var isWarehouseProduct: Boolean = false
    var hasShopAuthority: Boolean = false
    var isLeasing: Boolean = false
    var hasTopAdsActive: Boolean = false
    var preOrder: PreOrder? = PreOrder()

    companion object {
        fun build(_view: View, _listener: View.OnClickListener) = PartialButtonActionView(_view, _listener)

        private const val TOPCHAT_VARIANT_WHITE = "Icon White"
        private const val TOPCHAT_VARIANT_GREEN = "Icon Green"
        private const val TOPCHAT_VARIANT_GREEN_DOT = "Icon Green Dot"
        private const val KEY_AB_TOPCHAT = "TopChat Icon at PDP 2"
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

    fun renderData(isExpressCheckout: Boolean, hasTopAdsActive: Boolean) {
        this.isExpressCheckout = isExpressCheckout
        this.hasTopAdsActive = hasTopAdsActive
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
            container_btn_promote_topads.visibility = View.GONE
            btn_topchat.visibility = View.VISIBLE
            bindAbTestChatButton(btn_topchat)
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
            btn_add_to_cart.visible()
            if(isLeasing){
                btn_apply_leasing.visibility  = View.VISIBLE
                btn_add_to_cart.visibility = View.GONE
                btn_buy_now.visibility = View.GONE
                changeTopChatLayoutParamsToHandleLeasingLayout()
            }else{
                btn_apply_leasing.visibility  = View.GONE
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
            val greenColor = ContextCompat.getColor(imageView.context, R.color.Green_G500)
            imageView.setColorFilter(greenColor, PorterDuff.Mode.SRC_ATOP)
        } else {
            imageView.clearColorFilter()
        }
    }

    private fun resetTopChatLayoutParams() {
        with(view){
            val topChatParams = btn_topchat.layoutParams as ConstraintLayout.LayoutParams
            topChatParams.startToEnd = btn_byme.id
            topChatParams.startToStart = UNSET
            topChatParams.rightToLeft = btn_buy_now.id
            topChatParams.endToStart = btn_buy_now.id
            btn_topchat.layoutParams = topChatParams
        }
    }

    private fun changeTopChatLayoutParamsToHandleLeasingLayout() {
        with(view){
            val topChatParams = btn_topchat.layoutParams as ConstraintLayout.LayoutParams
            topChatParams.startToEnd = UNSET
            topChatParams.startToStart = PARENT_ID
            topChatParams.rightToLeft = btn_apply_leasing.id
            topChatParams.endToStart = btn_apply_leasing.id
            btn_topchat.layoutParams = topChatParams
        }

    }

    private fun showShopManageButton() {
        with(view) {
            btn_buy.visibility = View.GONE
            container_btn_promote_topads.visibility = View.VISIBLE
            btn_byme.visibility = View.GONE
            btn_topchat.visibility = View.GONE
            btn_buy_now.visibility = View.GONE
            btn_add_to_cart.visibility = View.GONE
            if (hasTopAdsActive) {
                btn_promote_topads.setOnClickListener { rincianTopAdsClick?.invoke() }
                btn_promote_topads.text = context.getString(R.string.rincian_topads)
                btn_promote_topads.setBackgroundResource(R.drawable.bg_rounded_grey_outline)
                setTextAppearance(btn_promote_topads, R.style.BtnTopAdsPDPRincian)
            } else {
                btn_promote_topads.setOnClickListener { promoTopAdsClick?.invoke() }
                btn_promote_topads.text = context.getString(R.string.promote_topads)
                btn_promote_topads.setBackgroundResource(R.drawable.bg_rounded_green)
                setTextAppearance(btn_promote_topads, R.style.BtnTopAdsPDPIklankan)
            }
        }
    }

    private fun showNoStockButton() {
        with(view) {
            tv_buy.setTextColor(ContextCompat.getColor(context, R.color.black_38))
            btn_buy.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_300))
            tv_buy.text = context.getString(R.string.no_stock)
            btn_buy.isEnabled = false
            btn_buy.visibility = View.VISIBLE
            container_btn_promote_topads.visibility = View.GONE
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
