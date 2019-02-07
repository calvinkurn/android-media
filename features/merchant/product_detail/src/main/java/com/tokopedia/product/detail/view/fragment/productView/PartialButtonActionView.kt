package com.tokopedia.product.detail.view.fragment.productView

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.product.PreOrder
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PRD_STATE_WAREHOUSE
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*

class PartialButtonActionView private constructor(private val view: View){
    var productManagePromoteClick: (() -> Unit)? = null
    var promoTopAdsClick: (() -> Unit)? = null
    var visibility: Boolean = false
    set(value) {
        field = value
        with(view){
            if (value) base_btn_action.visible() else base_btn_action.gone()
        }
    }

    companion object {
        fun build(_view: View) = PartialButtonActionView(_view)
    }

    init {
        with(view){
            pb_buy_now.indeterminateDrawable
                    .setColorFilter(ContextCompat.getColor(context, R.color.orange_red), PorterDuff.Mode.SRC_IN)
            pb_add_to_cart.indeterminateDrawable
                    .setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN)
        }
    }

    fun renderData(productStatus: Int, hasShopAuthority: Boolean, preOrder: PreOrder?){
        if (productStatus == PRD_STATE_WAREHOUSE) {
            showNoStockButton()
        } else if (hasShopAuthority){
            showShopManageButton()
        } else if (GlobalConfig.isCustomerApp()){
            showNewCheckoutButton(preOrder)
        }

    }

    private fun showNewCheckoutButton(preOrder: PreOrder?) {
        with(view) {
            btn_buy.visibility = View.GONE
            btn_promo_hour.visibility = View.GONE
            btn_promote_topads.visibility = View.GONE
            btn_byme.visibility = View.GONE
            btn_topchat.visibility = View.VISIBLE
            tv_buy_now.text = context.getString(if (preOrder?.isActive == true && preOrder.duration > 0){
                R.string.action_preorder
            } else R.string.buy)
            btn_buy_now.visibility = View.VISIBLE
            btn_add_to_cart.visibility = View.VISIBLE
        }
    }

    private fun showShopManageButton() {
        with(view){
            btn_promo_hour.setTextColor(ContextCompat.getColor(context, R.color.grey_500))
            btn_promo_hour.setOnClickListener { productManagePromoteClick?.invoke() }
            btn_promote_topads.setOnClickListener { promoTopAdsClick?.invoke() }
            btn_buy.visibility = View.GONE
            btn_promo_hour.visibility = View.VISIBLE
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
            btn_buy.setBackgroundResource(R.drawable.btn_buy_grey)
            tv_buy.text = context.getString(R.string.no_stock)
            btn_buy.isEnabled = false
            btn_buy.visibility = View.VISIBLE
            btn_promo_hour.visibility = View.GONE
            btn_promote_topads.visibility = View.GONE
            btn_byme.visibility = View.GONE
            btn_topchat.visibility = View.GONE
            btn_buy_now.visibility = View.GONE
            btn_add_to_cart.visibility = View.GONE
        }
    }
}