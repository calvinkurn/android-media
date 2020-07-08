package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.extension.formatToSimpleNumber
import com.tokopedia.shop.pageheader.util.RoundedBackgroundColorSpan
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.partial_new_shop_page_header.view.*
import kotlinx.android.synthetic.main.partial_new_shop_page_seller_play_widget.view.*

class ShopPageFragmentHeaderViewHolder(private val view: View, private val listener: ShopPageFragmentViewHolderListener,
                                       private val shopPageTracking: ShopPageTrackingBuyer?,
                                       private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
                                       private val context: Context) {
    private var isShopFavourited = false

    companion object {
        private const val LABEL_FREE_ONGKIR_DEFAULT_TITLE = "Toko ini Bebas Ongkir"
    }

    fun bind(shopInfo: ShopInfo, broadcasterConfig: Broadcaster.Config?, isMyShop: Boolean, remoteConfig: RemoteConfig) {
        view.shop_page_main_profile_name.text = MethodChecker.fromHtml(shopInfo.shopCore.name).toString()
        view.shop_page_main_profile_follower.setOnClickListener { listener.onFollowerTextClicked(isShopFavourited) }
        view.shop_page_main_profile_location.text = shopInfo.location

        ImageHandler.loadImageCircle2(view.context, view.shop_page_main_profile_image, shopInfo.shopAssets.avatar)
        if (isMyShop) {
            view.shop_page_main_profile_background.setOnClickListener {
                listener.onShopCoverClicked(
                        TextApiUtils.isValueTrue(shopInfo.goldOS.isOfficial.toString()),
                        shopInfo.goldOS.isGoldBadge == 1
                )
            }
        }
        when {
            TextApiUtils.isValueTrue(shopInfo.goldOS.isOfficial.toString()) -> displayOfficial()
            shopInfo.goldOS.isGoldBadge == 1 -> {
                displayGoldenShop()
            }
            else -> {
                view.shop_page_main_profile_badge.visibility = View.GONE
            }
        }
        if (isMyShop) {
            displayAsSeller()
        } else {
            displayAsBuyer()
        }
        setupSgcPlayWidget(shopInfo, isMyShop, broadcasterConfig)

        if (shopInfo.freeOngkir.isActive)
            showLabelFreeOngkir(remoteConfig)
        else
            view.shop_page_main_profile_free_ongkir.hide()
    }

    private fun setupTextContentSgcWidget(){
        if(view.shop_page_sgc_title.text.isBlank()) {
            val text = context.getString(R.string.shop_page_play_widget_title)
            val spannable = SpannableString(text)
            spannable.setSpan(
                    StyleSpan(BOLD),
                    11, 25,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(
                    RelativeSizeSpan(0.9f),
                    26, 30,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(
                    RoundedBackgroundColorSpan(ContextCompat.getColor(context, R.color.color_beta_badge), 4f, 10f),
                    26, 30,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            view.shop_page_sgc_title.text = spannable
        }
    }

    private fun setupSgcPlayWidget(shopInfo: ShopInfo, isMyShop: Boolean, broadcasterConfig: Broadcaster.Config?){
        view.play_seller_widget_container.visibility = if(isMyShop && broadcasterConfig?.streamAllowed == true) View.VISIBLE else View.GONE
        if(isMyShop){
            setupTextContentSgcWidget()
            shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopInfo.shopCore.shopID, customDimensionShopPage = CustomDimensionShopPage.create(shopInfo))
            view.container_lottie?.setOnClickListener {
                shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopInfo.shopCore.shopID, customDimensionShopPage = CustomDimensionShopPage.create(shopInfo))
                RouteManager.route(view.context, "tokopedia-android-internal://play-broadcaster")
            }
        }
    }

    private fun showLabelFreeOngkir(remoteConfig: RemoteConfig) {
        val labelTitle = remoteConfig.getString(LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE, LABEL_FREE_ONGKIR_DEFAULT_TITLE)
        if (labelTitle.isNotEmpty()) {
            view.shop_page_main_profile_free_ongkir.show()
            view.shop_page_main_profile_free_ongkir.text = labelTitle
        }
    }

    fun updateFavoriteData(favoriteData: ShopInfo.FavoriteData) {
        isShopFavourited = TextApiUtils.isValueTrue(favoriteData.alreadyFavorited.toString())
        if (favoriteData.totalFavorite > 1) {
            view.shop_page_main_profile_follower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_followers,
                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber()))
        } else { // if 0 or 1, only print as follower (without s)
            view.shop_page_main_profile_follower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_follower,
                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber()))
        }
        updateFavoriteButton()
    }

    fun updateShopTicker(shopInfo: ShopInfo, shopOperationalHourStatus: ShopOperationalHourStatus, isMyShop: Boolean) {
        when {
            shouldShowShopStatusTicker(shopInfo.statusInfo.statusTitle, shopInfo.statusInfo.statusMessage) -> {
                showShopStatusTicker(shopInfo, isMyShop)
            }
            shouldShowShopStatusTicker(shopOperationalHourStatus.tickerTitle, shopOperationalHourStatus.tickerMessage) -> {
                showShopOperationalHourStatusTicker(shopOperationalHourStatus)
            }
            else -> {
                hideShopStatusTicker()
            }
        }
    }

    private fun shouldShowShopStatusTicker(title: String, message: String): Boolean {
        return  title.isNotEmpty() && message.isNotEmpty()
    }

    private fun showShopStatusTicker(shopInfo: ShopInfo, isMyShop: Boolean = false) {
        view.tickerShopStatus.show()
        view.tickerShopStatus.tickerTitle = shopInfo.statusInfo.statusTitle
        view.tickerShopStatus.setHtmlDescription(shopInfo.statusInfo.statusMessage)
        view.tickerShopStatus.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                when (shopInfo.statusInfo.shopStatus) {
                    ShopStatusDef.CLOSED -> {
                        shopPageTracking?.sendOpenShop()
                        shopPageTracking?.clickOpenOperationalShop(CustomDimensionShopPage
                                .create(shopInfo.shopCore.shopID,
                                        shopInfo.goldOS.isOfficial == 1,
                                        shopInfo.goldOS.isGold == 1))
                    }
                    ShopStatusDef.NOT_ACTIVE -> {
                        shopPageTracking?.clickHowToActivateShop(CustomDimensionShopPage
                                .create(shopInfo.shopCore.shopID, shopInfo.goldOS.isOfficial == 1,
                                        shopInfo.goldOS.isGold == 1))
                    }
                }
                listener.onShopStatusTickerClickableDescriptionClicked(linkUrl)
            }

            override fun onDismiss() {}

        })
        if (isMyShop) {
            view.tickerShopStatus.closeButtonVisibility = View.GONE
        } else {
            view.tickerShopStatus.closeButtonVisibility = View.VISIBLE
        }
    }

    private fun showShopOperationalHourStatusTicker(shopOperationalHourStatus: ShopOperationalHourStatus) {
        view.tickerShopStatus.show()
        view.tickerShopStatus.tickerType = Ticker.TYPE_ANNOUNCEMENT
        view.tickerShopStatus.tickerTitle = shopOperationalHourStatus.tickerTitle
        view.tickerShopStatus.setHtmlDescription(shopOperationalHourStatus.tickerMessage)
        view.tickerShopStatus.closeButtonVisibility = View.VISIBLE
    }

    private fun hideShopStatusTicker() {
        view.tickerShopStatus.hide()
    }

    private fun displayAsBuyer() {
        view.shop_page_follow_unfollow_button.visibility = View.VISIBLE
        updateFavoriteButton()
    }

    fun isShopFavourited() = isShopFavourited

    fun updateFavoriteButton() {
        view.shop_page_follow_unfollow_button.isLoading = false
        view.shop_page_follow_unfollow_button.setOnClickListener {
            if (!view.shop_page_follow_unfollow_button.isLoading) {
                view.shop_page_follow_unfollow_button.isLoading = true
                listener.toggleFavorite(!isShopFavourited)
            }
        }
        if (isShopFavourited) {
            view.shop_page_follow_unfollow_button.buttonVariant = UnifyButton.Variant.GHOST
            view.shop_page_follow_unfollow_button.buttonType = UnifyButton.Type.ALTERNATE
            view.shop_page_follow_unfollow_button.text = context.getString(R.string.shop_header_action_following)

        } else {
            view.shop_page_follow_unfollow_button.buttonVariant = UnifyButton.Variant.FILLED
            view.shop_page_follow_unfollow_button.buttonType = UnifyButton.Type.MAIN
            view.shop_page_follow_unfollow_button.text = context.getString(R.string.shop_header_action_follow)
        }
    }

    private fun displayAsSeller() {
        view.shop_page_follow_unfollow_button.visibility = View.GONE
    }

    fun showShopReputationBadges(shopBadge: ShopBadge) {
        view.image_view_shop_reputation_badge.show()
        ImageHandler.LoadImage(view.image_view_shop_reputation_badge, shopBadge.badgeHD)
    }

    private fun displayGoldenShop() {
        view.shop_page_main_profile_badge.visibility = View.VISIBLE
        view.shop_page_main_profile_badge.setImageDrawable(GMConstant.getGMDrawable(context))
    }

    private fun displayOfficial() {
        view.shop_page_main_profile_badge.visibility = View.VISIBLE
        view.shop_page_main_profile_badge.setImageResource(R.drawable.ic_badge_shop_official)
    }


    fun toggleFavourite() {
        isShopFavourited = !isShopFavourited
    }

    interface ShopPageFragmentViewHolderListener {
        fun onFollowerTextClicked(shopFavourited: Boolean)
        fun toggleFavorite(isFavourite: Boolean)
        fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean)
        fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence)
    }


}