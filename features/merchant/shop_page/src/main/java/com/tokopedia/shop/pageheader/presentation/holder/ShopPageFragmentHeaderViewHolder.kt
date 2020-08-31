package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.view.View
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.extension.formatToSimpleNumber
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.partial_new_shop_page_header.view.*

class ShopPageFragmentHeaderViewHolder(private val view: View, private val listener: ShopPageFragmentViewHolderListener,
                                       private val shopPageTracking: ShopPageTrackingBuyer?,
                                       private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
                                       private val context: Context) {
    private var isShopFavorite = false

    companion object {
        private const val LABEL_FREE_ONGKIR_DEFAULT_TITLE = "Toko ini Bebas Ongkir"
    }

    fun bind(shopPageHeaderDataModel: ShopPageHeaderDataModel, isMyShop: Boolean, remoteConfig: RemoteConfig) {
        view.shop_page_main_profile_name.text = MethodChecker.fromHtml(shopPageHeaderDataModel.shopName).toString()
        view.shop_page_main_profile_follower.setOnClickListener { listener.onFollowerTextClicked(isShopFavorite) }
        val shopLocation = shopPageHeaderDataModel.location
        if(shopLocation.isNotEmpty()){
            view.shop_page_main_profile_location_icon.show()
            view.shop_page_main_profile_location.show()
            view.shop_page_main_profile_location.text = shopLocation
        }else{
            view.shop_page_main_profile_location_icon.hide()
            view.shop_page_main_profile_location.hide()
            view.shop_page_main_profile_location.text = shopLocation
        }
        ImageHandler.loadImageCircle2(view.context, view.shop_page_main_profile_image, shopPageHeaderDataModel.avatar)
        if (isMyShop) {
            view.shop_page_main_profile_background.setOnClickListener {
                listener.onShopCoverClicked(
                        shopPageHeaderDataModel.isOfficial,
                        shopPageHeaderDataModel.isGoldMerchant
                )
            }
        }
        when {
            shopPageHeaderDataModel.isOfficial -> displayOfficial()
            shopPageHeaderDataModel.isGoldMerchant -> displayGoldenShop()
            else -> {
                view.shop_page_main_profile_badge.visibility = View.GONE
            }
        }
        if (isMyShop) {
            displayAsSeller(shopPageHeaderDataModel)
        } else {
            displayAsBuyer()
        }

        if (shopPageHeaderDataModel.isFreeOngkir)
            showLabelFreeOngkir(remoteConfig)
        else
            view.shop_page_main_profile_free_ongkir.hide()
    }

    private fun setupTextContentSgcWidget(){
        if(view.shop_page_sgc_title.text.isBlank()) {
            val text = context.getString(R.string.shop_page_play_widget_title)
            view.shop_page_sgc_title.text = MethodChecker.fromHtml(text)
        }
    }

    private fun setupSgcPlayWidget(shopPageHeaderDataModel: ShopPageHeaderDataModel){
        view.play_seller_widget_container.visibility = if(shopPageHeaderDataModel.broadcaster.streamAllowed && GlobalConfig.isSellerApp()) View.VISIBLE else View.GONE
        setupTextContentSgcWidget()
        setLottieAnimationFromUrl(context.getString(R.string.shop_page_lottie_sgc_url))
        if(shopPageHeaderDataModel.broadcaster.streamAllowed) shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
        view.container_lottie?.setOnClickListener {
            shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopPageHeaderDataModel.shopId)
            RouteManager.route(view.context, ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER)
        }
    }

    fun showShopPageHeaderContent() {
        hideLoaderLoading()
        view.shop_page_header_content.show()
    }

    fun showShopPageHeaderContentError() {
        hideLoaderLoading()
        view.shop_page_header_content.invisible()
    }

    fun showShopPageHeaderContentLoading() {
        showLoaderLoading()
        view.shop_page_header_content.hide()
    }

    private fun showLoaderLoading(){
        view.loader_profile_image.show()
        view.first_rect_loader_view.show()
        view.second_rect_loader_view.show()
        view.third_rect_loader_view.show()
    }

    private fun hideLoaderLoading(){
        view.loader_profile_image.hide()
        view.first_rect_loader_view.hide()
        view.second_rect_loader_view.hide()
        view.third_rect_loader_view.hide()
    }

    private fun showLabelFreeOngkir(remoteConfig: RemoteConfig) {
        val labelTitle = remoteConfig.getString(LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE, LABEL_FREE_ONGKIR_DEFAULT_TITLE)
        if (labelTitle.isNotEmpty()) {
            view.shop_page_main_profile_free_ongkir.show()
            view.shop_page_main_profile_free_ongkir.text = labelTitle
        }
    }

    fun updateFavoriteData(favoriteData: ShopInfo.FavoriteData) {
        isShopFavorite = TextApiUtils.isValueTrue(favoriteData.alreadyFavorited.toString())
        view.shop_page_main_profile_follower_icon.show()
        view.shop_page_main_profile_follower.show()
        if (favoriteData.totalFavorite > 1) {
            view.shop_page_main_profile_follower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_followers,
                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber()))
        } else { // if 0 or 1, only print as follower (without s)
            view.shop_page_main_profile_follower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_follower,
                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber()))
        }
        updateFavoriteButton()
    }

    fun updateShopTicker(shopPageHeaderDataModel: ShopPageHeaderDataModel?, shopOperationalHourStatus: ShopOperationalHourStatus, isMyShop: Boolean) {
        shopPageHeaderDataModel?.let { it ->
            when {
                shouldShowShopStatusTicker(it.statusTitle, it.statusMessage) -> {
                    showShopStatusTicker(it, isMyShop)
                }
                shouldShowShopStatusTicker(shopOperationalHourStatus.tickerTitle, shopOperationalHourStatus.tickerMessage) -> {
                    showShopOperationalHourStatusTicker(shopOperationalHourStatus)
                }
                else -> {
                    hideShopStatusTicker()
                }
            }
        }
    }

    private fun shouldShowShopStatusTicker(title: String, message: String): Boolean {
        return title.isNotEmpty() && message.isNotEmpty()
    }

    private fun showShopStatusTicker(shopPageHeaderDataModel: ShopPageHeaderDataModel, isMyShop: Boolean = false) {
        view.tickerShopStatus.show()
        view.tickerShopStatus.tickerTitle = MethodChecker.fromHtml(shopPageHeaderDataModel.statusTitle).toString()
        view.tickerShopStatus.setHtmlDescription(shopPageHeaderDataModel.statusMessage)
        view.tickerShopStatus.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                when (shopPageHeaderDataModel.shopStatus) {
                    ShopStatusDef.CLOSED -> {
                        shopPageTracking?.sendOpenShop()
                        shopPageTracking?.clickOpenOperationalShop(CustomDimensionShopPage
                                .create(
                                        shopPageHeaderDataModel.shopId,
                                        shopPageHeaderDataModel.isOfficial,
                                        shopPageHeaderDataModel.isGoldMerchant
                                ))
                    }
                    ShopStatusDef.NOT_ACTIVE -> {
                        shopPageTracking?.clickHowToActivateShop(CustomDimensionShopPage
                                .create(
                                        shopPageHeaderDataModel.shopId,
                                        shopPageHeaderDataModel.isOfficial,
                                        shopPageHeaderDataModel.isGoldMerchant
                                ))
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
        view.play_seller_widget_container.visibility = View.GONE
        updateFavoriteButton()
    }

    fun isShopFavourited() = isShopFavorite

    fun updateFavoriteButton() {
        view.shop_page_follow_unfollow_button.isLoading = false
        view.shop_page_follow_unfollow_button.setOnClickListener {
            if (!view.shop_page_follow_unfollow_button.isLoading) {
                view.shop_page_follow_unfollow_button.isLoading = true
                listener.toggleFavorite(!isShopFavorite)
            }
        }
        if (isShopFavorite) {
            view.shop_page_follow_unfollow_button.buttonVariant = UnifyButton.Variant.GHOST
            view.shop_page_follow_unfollow_button.buttonType = UnifyButton.Type.ALTERNATE
            view.shop_page_follow_unfollow_button.text = context.getString(R.string.shop_header_action_following)

        } else {
            view.shop_page_follow_unfollow_button.buttonVariant = UnifyButton.Variant.FILLED
            view.shop_page_follow_unfollow_button.buttonType = UnifyButton.Type.MAIN
            view.shop_page_follow_unfollow_button.text = context.getString(R.string.shop_header_action_follow)
        }
    }

    private fun displayAsSeller(shopPageHeaderDataModel: ShopPageHeaderDataModel) {
        view.shop_page_follow_unfollow_button.visibility = View.GONE
        setupSgcPlayWidget(shopPageHeaderDataModel)
    }

    fun showShopReputationBadges(shopBadge: ShopBadge) {
        view.image_view_shop_reputation_badge.show()
        ImageHandler.LoadImage(view.image_view_shop_reputation_badge, shopBadge.badgeHD)
    }

    private fun displayGoldenShop() {
        view.shop_page_main_profile_badge.visibility = View.VISIBLE
        view.shop_page_main_profile_badge.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_power_merchant))
    }

    private fun displayOfficial() {
        view.shop_page_main_profile_badge.visibility = View.VISIBLE
        view.shop_page_main_profile_badge.setImageResource(com.tokopedia.design.R.drawable.ic_badge_shop_official)
    }

    /**
     * Fetch the animation from http URL and play the animation
     */
    private fun setLottieAnimationFromUrl(animationUrl: String) {
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, animationUrl)

            lottieCompositionLottieTask.addListener { result ->
                view.lottie?.setComposition(result)
                view.lottie?.playAnimation()
            }

            lottieCompositionLottieTask.addFailureListener { }
        }
    }

    fun toggleFavourite() {
        isShopFavorite = !isShopFavorite
    }

    interface ShopPageFragmentViewHolderListener {
        fun onFollowerTextClicked(shopFavourited: Boolean)
        fun toggleFavorite(isFavourite: Boolean)
        fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean)
        fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence)
    }


}