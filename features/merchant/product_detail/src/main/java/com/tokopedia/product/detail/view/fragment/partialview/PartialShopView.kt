package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import kotlinx.android.synthetic.main.partial_product_shop_info.view.*

class PartialShopView private constructor(private val view: View){
    companion object {
        private const val TRUE_VALUE = 1
        fun build(_view: View) = PartialShopView(_view)
    }

    fun renderShop(shop: ShopInfo, isOwned: Boolean = false){
        with(view){
            shop_name.text = MethodChecker.fromHtml(shop.shopCore.name)
            ImageHandler.loadImage2(shop_ava, shop.shopAssets.avatar,
                    R.drawable.ic_shop_default_empty)

            var templateLocOnline = "${shop.location} "
            if (shop.shopLastActive.isNotBlank()){
                templateLocOnline += context.getString(R.string.template_shop_last_login, shop.shopLastActive)
            }
            shop_location_online.text = templateLocOnline
            if (shop.isAllowManage == TRUE_VALUE) btn_favorite.visible() else btn_favorite.gone()
            val drawable = if (shop.goldOS.isOfficial == TRUE_VALUE){
                ContextCompat.getDrawable(context, R.drawable.ic_official_store)
            } else if (shop.goldOS.isGold == TRUE_VALUE && shop.goldOS.isGoldBadge == TRUE_VALUE){
                ContextCompat.getDrawable(context, R.drawable.ic_power_merchant)
            } else {
                null
            }

            if (drawable == null) iv_badge.gone()
            else {
                iv_badge.setImageDrawable(drawable)
                iv_badge.visible()
            }
            adjustSendMsgAndFavoriteTextSize()
            if (isOwned) {
                send_msg_shop.gone()
                btn_favorite.gone()
            } else {
                send_msg_shop.visible()
                btn_favorite.visible()
            }

            //val screenDensityDpi = resources.displayMetrics.densityDpi
            if (shop.favoriteData.alreadyFavorited == TRUE_VALUE){
                btn_favorite.text = context.getString(R.string.label_favorited)
                btn_favorite.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green))
                btn_favorite.background = ContextCompat.getDrawable(context, R.drawable.bg_button_white_border)
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.ic_check_green_24), null, null, null)
            } else {
                btn_favorite.text = context.getString(R.string.label_favorite)
                btn_favorite.setTextColor(ContextCompat.getColor(context, R.color.dark_primary))
                btn_favorite.background = ContextCompat.getDrawable(context, R.drawable.bg_button_green)
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(context, R.drawable.ic_plus_add_white_24), null, null, null)
            }
            visible()
        }
    }

    private fun adjustSendMsgAndFavoriteTextSize() {
        with(view) {
            val screenDensityDpi = resources.displayMetrics.densityDpi
            if (screenDensityDpi <= DisplayMetrics.DENSITY_HIGH) {
                send_msg_shop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                btn_favorite.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                send_msg_shop.setPadding(0, 4, 0, 4)
                btn_favorite.setPadding(0, 4, 0, 4)
            } else {
                send_msg_shop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                btn_favorite.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            }
        }
    }
}