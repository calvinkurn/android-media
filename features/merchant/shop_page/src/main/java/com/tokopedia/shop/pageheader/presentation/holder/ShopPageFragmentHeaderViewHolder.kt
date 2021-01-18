package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey.LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.util.ShopUtil.isUsingNewNavigation
import com.tokopedia.shop.extension.formatToSimpleNumber
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.partial_new_shop_page_header.view.*

class ShopPageFragmentHeaderViewHolder(private val view: View, private val listener: ShopPageFragmentViewHolderListener,
                                       private val shopPageTracking: ShopPageTrackingBuyer?,
                                       private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
                                       private val context: Context) {
    private var isShopFavorite = false
    private val shopPageProfileBadgeView: AppCompatImageView
        get() = view.shop_page_main_profile_badge.takeIf {
            isUsingNewNavigation()
        }?: view.shop_page_main_profile_badge_old
    private val locationImageIcon : Int
        get() = R.drawable.ic_shop_location.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_shop_location_old
    private val followerImageIcon : Int
        get() = R.drawable.ic_shop_follower.takeIf {
            isUsingNewNavigation()
        } ?: R.drawable.ic_shop_follower_old
    private val followButton : UnifyButton
        get() = view.shop_page_follow_unfollow_button.takeIf {
            isUsingNewNavigation()
        } ?: view.shop_page_follow_unfollow_button_old

    companion object {
        private const val LABEL_FREE_ONGKIR_DEFAULT_TITLE = "Toko ini Bebas Ongkir"
    }

    fun bind(shopPageHeaderDataModel: ShopPageHeaderDataModel, isMyShop: Boolean, remoteConfig: RemoteConfig) {
        view.shop_page_follow_unfollow_button?.hide()
        view.shop_page_follow_unfollow_button_old?.hide()
        view.shop_page_main_profile_follower.setOnClickListener { listener.onFollowerTextClicked(isShopFavorite) }
        val shopLocation = shopPageHeaderDataModel.location
        if(shopLocation.isNotEmpty()){
            view.shop_page_main_profile_location_icon.setImageResource(locationImageIcon)
            view.shop_page_main_profile_location_icon.show()
            view.shop_page_main_profile_location.show()
            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_location, shopLocation, view.shop_page_main_profile_location.context.getString(R.string.content_desc_shop_page_main_profile_location));
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
                shopPageProfileBadgeView.visibility = View.GONE
            }
        }
        TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_name, MethodChecker.fromHtml(shopPageHeaderDataModel.shopName).toString(), view.shop_page_main_profile_name.context.getString(R.string.content_desc_shop_page_main_profile_name))
        if (isMyShop) {
            setupSgcPlayWidget(shopPageHeaderDataModel)
        }

        if (shopPageHeaderDataModel.isFreeOngkir)
            showLabelFreeOngkir(remoteConfig)
        else
            view.shop_page_main_profile_free_ongkir.hide()

        if(isUsingNewNavigation() && !isMyShop) {
            view.shop_page_chevron_shop_info.show()
            view.shop_page_chevron_shop_info.setOnClickListener {
                listener.openShopInfo()
            }
            view.shop_page_main_profile_name.setOnClickListener {
                listener.openShopInfo()
            }
        } else {
            view.shop_page_chevron_shop_info.setOnClickListener(null)
            view.shop_page_main_profile_name.setOnClickListener (null)
            view.shop_page_chevron_shop_info.hide()
        }
    }

    fun setupFollowButton(isMyShop: Boolean){
        if (isMyShop) {
            hideFollowButton()
        } else {
            showFollowButton()
            view.play_seller_widget_container.visibility = View.GONE
            updateFavoriteButton()
        }
    }

    private fun hideFollowButton(){
        followButton.visibility = View.GONE
    }

    fun showFollowButton(){
        followButton.visibility = View.VISIBLE
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
            listener.onStartLiveStreamingClicked()
        }
    }

    fun setShopName(shopName: String) {
        val name = MethodChecker.fromHtml(shopName)
        val shopNameTv = view.shop_page_main_profile_name
        if (shopNameTv.text != name) {
            shopNameTv.text = name
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
        isShopFavorite = TextApiUtils.isValueTrue(favoriteData.alreadyFavorited.toString())
        view.shop_page_main_profile_follower_icon.setImageResource(followerImageIcon)
        view.shop_page_main_profile_follower_icon.show()
        view.shop_page_main_profile_follower.show()
        if (favoriteData.totalFavorite > 1) {
            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_follower, MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_followers,
                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber())).toString(), view.context.getString(R.string.content_desc_shop_page_main_profile_follower))
        } else { // if 0 or 1, only print as follower (without s)
            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_follower, MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_follower,
                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber())).toString(), view.context.getString(R.string.content_desc_shop_page_main_profile_follower))
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

    fun isShopFavourited() = isShopFavorite

    fun updateFavoriteButton() {
        followButton.isLoading = false
        followButton.setOnClickListener {
            if (!followButton.isLoading) {
                followButton.isLoading = true
                listener.toggleFavorite(!isShopFavorite)
            }
        }
        if (isShopFavorite) {
            followButton.buttonVariant = UnifyButton.Variant.GHOST
            followButton.buttonType = UnifyButton.Type.ALTERNATE
            followButton.text = context.getString(R.string.shop_header_action_following)

        } else {
            followButton.buttonVariant = UnifyButton.Variant.FILLED
            followButton.buttonType = UnifyButton.Type.MAIN
            followButton.text = context.getString(R.string.shop_header_action_follow)
        }
    }

    fun showShopReputationBadges(shopBadge: ShopBadge) {
        view.image_view_shop_reputation_badge.show()
        ImageHandler.LoadImage(view.image_view_shop_reputation_badge, shopBadge.badgeHD)
    }

    private fun displayGoldenShop() {
        shopPageProfileBadgeView.visibility = View.VISIBLE
        shopPageProfileBadgeView.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.gm.common.R.drawable.ic_power_merchant))
    }

    private fun displayOfficial() {
        shopPageProfileBadgeView.visibility = View.VISIBLE
        shopPageProfileBadgeView.setImageResource(com.tokopedia.design.R.drawable.ic_badge_shop_official)
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

    fun setFavoriteValue(isShopFavorite: Boolean) {
        this.isShopFavorite = isShopFavorite
    }

    fun toggleFavourite() {
        isShopFavorite = !isShopFavorite
    }

    interface ShopPageFragmentViewHolderListener {
        fun onFollowerTextClicked(shopFavourited: Boolean)
        fun toggleFavorite(isFavourite: Boolean)
        fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean)
        fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence)
        fun openShopInfo()
        fun onStartLiveStreamingClicked()
    }


}