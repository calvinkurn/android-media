package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.remoteconfig.RemoteConfigKey.LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestResult
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.util.ShopUtil.isUsingNewNavigation
import com.tokopedia.shop.common.util.loadLeftDrawable
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.shop.extension.formatToSimpleNumber
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopRequestUnmoderateBottomSheet
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
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
    private var coachMark: CoachMark2? = null

    private val playSgcWidgetContainer = view.findViewById<CardView>(R.id.play_sgc_widget_container)
    private val playSgcLetsTryLiveTypography = view.findViewById<Typography>(R.id.play_sgc_lets_try_live)
    private val playSgcBtnStartLiveLottieAnimationView = view.findViewById<LottieAnimationView>(R.id.play_sgc_btn_start_live)

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
            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_location, shopLocation, view.shop_page_main_profile_location.context.getString(R.string.content_desc_shop_page_main_profile_location))
        }else{
            view.shop_page_main_profile_location_icon.hide()
            view.shop_page_main_profile_location.hide()
            view.shop_page_main_profile_location.text = shopLocation
        }
        view.shop_page_main_profile_image.loadImageCircle(shopPageHeaderDataModel.avatar)
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
        if (isMyShop) setupSgcPlayWidget(shopPageHeaderDataModel)

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
            playSgcWidgetContainer.visibility = View.GONE
            followButton.isLoading = true
        }
    }

    private fun hideFollowButton(){
        followButton.visibility = View.GONE
    }

    private fun showFollowButton(){
        followButton.visibility = View.VISIBLE
        followButton.setOnClickListener {
            if (!followButton.isLoading) {
                removeCompoundDrawableFollowButton()
                followButton.isLoading = true
                listener.setFollowStatus(isShopFavorite)
            }
        }
    }

    private fun setupSgcPlayWidget(dataModel: ShopPageHeaderDataModel) {
        if (allowLiveStreaming(dataModel)) {
            playSgcWidgetContainer.show()
            setupTextContentSgcWidget()
            setLottieAnimationFromUrl(context.getString(R.string.shop_page_lottie_sgc_url))
            shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = dataModel.shopId)
            playSgcBtnStartLiveLottieAnimationView.setOnClickListener {
                shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = dataModel.shopId)
                listener.onStartLiveStreamingClicked()
            }
        } else {
            playSgcWidgetContainer.hide()
        }
    }

    private fun allowLiveStreaming(dataModel: ShopPageHeaderDataModel) = dataModel.broadcaster.streamAllowed && GlobalConfig.isSellerApp()

    private fun setupTextContentSgcWidget() {
        if (playSgcLetsTryLiveTypography.text.isBlank()) playSgcLetsTryLiveTypography.text = MethodChecker.fromHtml(context.getString(R.string.shop_page_play_widget_title))
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
        view.tickerShopStatus.setHtmlDescription(
                if(shopPageHeaderDataModel.shopStatus == ShopStatusDef.MODERATED) {
                    generateShopModerateTickerDescription(shopPageHeaderDataModel.statusMessage)
                } else {
                    shopPageHeaderDataModel.statusMessage
                }
        )
        view.tickerShopStatus.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                // set tracker data based on shop status
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
                if(linkUrl == context.getString(R.string.shop_page_header_request_unmoderate_appended_text_dummy_url)) {
                    // linkUrl is from appended moderate description, show bottomsheet to request open moderate
                    listener.setShopUnmoderateRequestBottomSheet(ShopRequestUnmoderateBottomSheet.createInstance().apply {
                        init(listener)
                    })
                } else {
                    // original url, open web view
                    listener.onShopStatusTickerClickableDescriptionClicked(linkUrl)
                }
            }

            override fun onDismiss() {}

        })
        if (isMyShop) {
            view.tickerShopStatus.closeButtonVisibility = View.GONE
        } else {
            view.tickerShopStatus.closeButtonVisibility = View.VISIBLE
        }
    }

    private fun generateShopModerateTickerDescription(originalStatusMessage: String) : String {
        // append action text to request open moderation
        val appendedText = context.getString(
                R.string.shop_page_header_request_unmoderate_appended_text,
                context.getString(R.string.shop_page_header_request_unmoderate_appended_text_dummy_url),
                context.getString(R.string.new_shop_page_header_shop_close_description_seller_clickable_text)
        )
        return originalStatusMessage + appendedText
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

    private fun changeColorButton() {
        if (isShopFavorite) {
            followButton.buttonVariant = UnifyButton.Variant.GHOST
            followButton.buttonType = UnifyButton.Type.ALTERNATE
        } else {
            followButton.buttonVariant = UnifyButton.Variant.FILLED
            followButton.buttonType = UnifyButton.Type.MAIN
        }
    }

    fun setFollowStatus(followStatus: FollowStatus?, shopId: String, userId: String) {
        followButton.isLoading = false
        isShopFavorite = followStatus?.status?.userIsFollowing == true
        followStatus?.let {
            followButton.text = it.followButton?.buttonLabel
            val voucherUrl = it.followButton?.voucherIconURL
            val coachMarkText = it.followButton?.coachmarkText
            if (!voucherUrl.isNullOrBlank()) {
                followButton.loadLeftDrawable(
                        context = context,
                        url = voucherUrl,
                        convertIntoSize = 50
                )
                shopPageTracking?.impressionVoucherFollowUnfollowShop(shopId, userId)
            } else {
                removeCompoundDrawableFollowButton()
            }
            if (!coachMarkText.isNullOrBlank() && listener.isFirstTimeVisit() == false) {
                setCoachMark(coachMarkText)
                listener.saveFirstTimeVisit()
                shopPageTracking?.impressionCoachMarkFollowUnfollowShop(shopId, userId)
            }
        }
        changeColorButton()
    }

    fun isCoachMarkDismissed(): Boolean? {
        return coachMark?.isDismissed
    }

    fun dismissCoachMark(shopId: String, userId: String) {
        coachMark?.dismissCoachMark()
        shopPageTracking?.impressionCoachMarkDissapearFollowUnfollowShop(shopId, userId)
    }

    fun removeCompoundDrawableFollowButton() {
        if (!followButton.compoundDrawables.isNullOrEmpty()) {
            followButton.removeDrawable()
        }
    }

    private fun setCoachMark(description: String) {
        val coachMarkItem = ArrayList<CoachMark2Item>()
        coachMarkItem.add(
                CoachMark2Item(
                        anchorView = followButton,
                        title = "",
                        description = MethodChecker.fromHtml(description)
                )
        )
        coachMark = CoachMark2(context)
        coachMark?.showCoachMark(coachMarkItem)
    }

    fun showShopReputationBadges(shopBadge: ShopBadge) {
        view.image_view_shop_reputation_badge.show()
        view.image_view_shop_reputation_badge.loadIcon(shopBadge.badgeHD)
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
        LottieCompositionFactory.fromUrl(context, animationUrl).addListener { result ->
            playSgcBtnStartLiveLottieAnimationView.setComposition(result)
            playSgcBtnStartLiveLottieAnimationView.playAnimation()
        }
    }

    fun updateFollowStatus(followShop: FollowShop) {
        followButton.isLoading = false
        followButton.text = followShop.buttonLabel
        isShopFavorite = followShop.isFollowing == true
        removeCompoundDrawableFollowButton()
        changeColorButton()
    }

    fun setLoadingFollowButton(isLoading: Boolean) {
        followButton.isLoading = isLoading
    }

    interface ShopPageFragmentViewHolderListener {
        fun onFollowerTextClicked(shopFavourited: Boolean)
        fun setFollowStatus(isFollowing: Boolean)
        fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean)
        fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence)
        fun openShopInfo()
        fun onStartLiveStreamingClicked()
        fun saveFirstTimeVisit()
        fun isFirstTimeVisit(): Boolean?
        fun onSendRequestOpenModerate(optionValue : String)
        fun onCompleteSendRequestOpenModerate()
        fun onCompleteCheckRequestModerateStatus(moderateStatusResult : ShopModerateRequestResult)
        fun setShopUnmoderateRequestBottomSheet(bottomSheet: ShopRequestUnmoderateBottomSheet)
    }


}