package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.relativeWeekDay
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.getRelativeDate
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import kotlinx.android.synthetic.main.item_dynamic_pdp_shop_info.view.*

class PartialDynamicShopInfoView(val view: View, private val listener: DynamicProductDetailListener) {

    companion object {
        private const val TRUE_VALUE = 1
    }

    fun renderShop(shop: ShopInfo, isOwned: Boolean = false, componentTrackDataModel: ComponentTrackDataModel) {
        with(view) {
            shop_name.text = com.tokopedia.abstraction.common.utils.view.MethodChecker.fromHtml(shop.shopCore.name)
            ImageHandler.LoadImage(shop_ava, shop.shopAssets.avatar)

            var templateLocOnline = "${shop.location} "
            if (shop.shopLastActive.isNotBlank()) {
                try {
                    val cal = java.util.Calendar.getInstance()
                    cal.timeInMillis = shop.shopLastActive.toLong() * 1000
                    templateLocOnline += context.getString(R.string.template_shop_last_login, cal.time.relativeWeekDay)
                } catch (t: Throwable) {
                }
            }
            shop_location_online.text = templateLocOnline
            if (shop.isAllowManage == TRUE_VALUE) btn_favorite.visible() else btn_favorite.gone()
            val drawable = if (shop.goldOS.isOfficial == TRUE_VALUE) {
                androidx.core.content.ContextCompat.getDrawable(context, R.drawable.ic_official_store_product)
            } else if (shop.goldOS.isGold == TRUE_VALUE && shop.goldOS.isGoldBadge == TRUE_VALUE) {
                androidx.core.content.ContextCompat.getDrawable(context, R.drawable.ic_power_merchant)
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

            updateFavorite(shop.favoriteData.alreadyFavorited == TRUE_VALUE)

            shop_name.setOnClickListener {
                listener.onShopInfoClicked(it.id, componentTrackDataModel)
            }
            shop_ava.setOnClickListener {
                listener.onShopInfoClicked(it.id, componentTrackDataModel)
            }
            btn_favorite.setOnClickListener {
                listener.onShopInfoClicked(it.id, componentTrackDataModel)
            }
            send_msg_shop.setOnClickListener {
                listener.onShopInfoClicked(it.id, componentTrackDataModel)
            }
            visible()
        }
    }

    private fun adjustSendMsgAndFavoriteTextSize() {
        with(view) {
            val screenDensityDpi = resources.displayMetrics.densityDpi
            if (screenDensityDpi <= android.util.DisplayMetrics.DENSITY_HIGH) {
                send_msg_shop.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 10f)
                btn_favorite.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 10f)
                send_msg_shop.setPadding(0, 4, 0, 4)
                btn_favorite.setPadding(0, 4, 0, 4)
            } else {
                send_msg_shop.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 12f)
                btn_favorite.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 12f)
            }
        }
    }

    fun renderShopBadge(shopBadge: ShopBadge) {
        ImageHandler.LoadImage(view.l_medal, shopBadge.badge)
    }

    fun renderShopFeature(shopFeatureData: ShopFeatureData) {
        with(view) {
            shop_feature.shouldShowWithAction(shopFeatureData.value) {
                shop_feature.text = shopFeatureData.title
            }
        }
    }

    fun updateFavorite(isFavorite: Boolean) {
        with(view) {
            if (isFavorite) {
                btn_favorite.text = context.getString(R.string.label_favorited)
                btn_favorite.setTextColor(androidx.core.content.ContextCompat.getColor(context, R.color.tkpd_main_green))
                btn_favorite.background = androidx.core.content.ContextCompat.getDrawable(context, R.drawable.bg_button_white_border)
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(
                        androidx.core.content.ContextCompat.getDrawable(context, R.drawable.ic_check_green_24), null, null, null)
            } else {
                btn_favorite.text = context.getString(R.string.label_follow)
                btn_favorite.setTextColor(androidx.core.content.ContextCompat.getColor(context, R.color.dark_primary))
                btn_favorite.background = androidx.core.content.ContextCompat.getDrawable(context, R.drawable.bg_button_green)
                btn_favorite.setCompoundDrawablesWithIntrinsicBounds(
                        androidx.core.content.ContextCompat.getDrawable(context, R.drawable.ic_plus_add_white_24), null, null, null)
            }
        }
    }

    fun toggleClickableFavoriteBtn(enable: Boolean) {
        view.btn_favorite.isClickable = enable
    }
}