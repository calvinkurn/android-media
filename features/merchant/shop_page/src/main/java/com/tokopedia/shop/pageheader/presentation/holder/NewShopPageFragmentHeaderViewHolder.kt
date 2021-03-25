package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopActionButtonWidgetChatButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopActionButtonWidgetFollowButtonComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetBadgeTextValueComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.ShopPerformanceWidgetImageOnlyComponentViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderBasicInfoWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopRequestUnmoderateBottomSheet
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.new_partial_new_shop_page_header.view.*
import kotlinx.android.synthetic.main.new_partial_new_shop_page_header.view.choose_address_widget
import kotlinx.android.synthetic.main.new_partial_new_shop_page_header.view.choosee_address_widget_bottom_shadow
import kotlinx.android.synthetic.main.new_partial_new_shop_page_header.view.tickerShopStatus
import kotlinx.android.synthetic.main.partial_new_shop_page_header.view.*

class NewShopPageFragmentHeaderViewHolder(private val view: View, private val listener: ShopPageFragmentViewHolderListener,
                                          private val shopPageTracking: ShopPageTrackingBuyer?,
                                          private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
                                          private val context: Context,
                                          private val shopHeaderBasicInfoWidgetListener: ShopHeaderBasicInfoWidgetViewHolder.Listener,
                                          private val shopPerformanceWidgetBadgeTextValueListener: ShopPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
                                          private val shopPerformanceWidgetImageOnlyListener: ShopPerformanceWidgetImageOnlyComponentViewHolder.Listener,
                                          private val shopActionButtonWidgetChatButtonComponentListener: ShopActionButtonWidgetChatButtonComponentViewHolder.Listener,
                                          private val shopActionButtonWidgetFollowButtonComponentListener: ShopActionButtonWidgetFollowButtonComponentViewHolder.Listener,
                                          private val shopPlayWidgetListener: ShopHeaderPlayWidgetViewHolder.Listener,
                                          private val chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener
) {
    private var isShopFavorite = false

    //    private val shopPageProfileBadgeView: AppCompatImageView
//        get() = view.shop_page_main_profile_badge.takeIf {
//            isUsingNewNavigation()
//        }?: view.shop_page_main_profile_badge_old
//    private val locationImageIcon : Int
//        get() = R.drawable.ic_shop_location.takeIf {
//            isUsingNewNavigation()
//        } ?: R.drawable.ic_shop_location_old
//    private val followerImageIcon : Int
//        get() = R.drawable.ic_shop_follower.takeIf {
//            isUsingNewNavigation()
//        } ?: R.drawable.ic_shop_follower_old
//    private val followButton : UnifyButton
//        get() = view.shop_page_follow_unfollow_button.takeIf {
//            isUsingNewNavigation()
//        } ?: view.shop_page_follow_unfollow_button_old
    private val chooseAddressWidget: ChooseAddressWidget?
        get() = view.choose_address_widget
    private var coachMark: CoachMark2? = null

    private var shopPageHeaderAdapter: ShopPageHeaderAdapter? = null

    fun updateChooseAddressWidget(){
        chooseAddressWidget?.updateWidget()
    }

    fun setupChooseAddressWidget(remoteConfig: RemoteConfig) {
        chooseAddressWidget?.apply {
            val isRollOutUser = ChooseAddressUtils.isRollOutUser(view.context)
            val isRemoteConfigChooseAddressWidgetEnabled = remoteConfig.getBoolean(
                    ShopPageConstant.ENABLE_SHOP_PAGE_HEADER_CHOOSE_ADDRESS_WIDGET,
                    true
            )
            if (isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled) {
                show()
                bindChooseAddress(chooseAddressWidgetListener)
                view.choosee_address_widget_bottom_shadow?.show()
            } else {
                view.choosee_address_widget_bottom_shadow?.hide()
                hide()
            }
        }
    }

    fun setShopHeaderWidgetData(listWidget: List<ShopHeaderWidgetUiModel>) {
        shopPageHeaderAdapter = ShopPageHeaderAdapter(ShopPageHeaderAdapterTypeFactory(
                shopHeaderBasicInfoWidgetListener,
                shopPerformanceWidgetBadgeTextValueListener,
                shopPerformanceWidgetImageOnlyListener,
                shopActionButtonWidgetChatButtonComponentListener,
                shopActionButtonWidgetFollowButtonComponentListener,
                shopPageTrackingSGCPlayWidget,
                shopPlayWidgetListener
        ))
        view.rv_shop_page_header_widget?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.rv_shop_page_header_widget?.itemAnimator = null
        view.rv_shop_page_header_widget?.adapter = shopPageHeaderAdapter
        shopPageHeaderAdapter?.setData(listWidget)
    }

    fun setFollowStatus(followStatus: FollowStatus?) {
        isShopFavorite = followStatus?.status?.userIsFollowing == true
        followStatus?.let {
            shopPageHeaderAdapter?.setFollowButtonData(
                    it.followButton?.buttonLabel.orEmpty(),
                    it.followButton?.voucherIconURL.orEmpty(),
                    isShopFavorite
            )
        }
    }

    fun updateFollowStatus(followShop: FollowShop) {
        isShopFavorite = followShop.isFollowing == true
        shopPageHeaderAdapter?.setFollowButtonData(
                label = followShop.buttonLabel.orEmpty(),
                isFollowing = isShopFavorite
        )
    }


    //    fun bind(shopPageHeaderDataModel: ShopPageHeaderDataModel, isMyShop: Boolean, remoteConfig: RemoteConfig) {
//        view.shop_page_follow_unfollow_button?.hide()
//        view.shop_page_follow_unfollow_button_old?.hide()
//        view.shop_page_main_profile_follower.setOnClickListener { listener.onFollowerTextClicked(isShopFavorite) }
//        val shopLocation = shopPageHeaderDataModel.location
//        if(shopLocation.isNotEmpty()){
//            view.shop_page_main_profile_location_icon.setImageResource(locationImageIcon)
//            view.shop_page_main_profile_location_icon.show()
//            view.shop_page_main_profile_location.show()
//            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_location, shopLocation, view.shop_page_main_profile_location.context.getString(R.string.content_desc_shop_page_main_profile_location))
//        }else{
//            view.shop_page_main_profile_location_icon.hide()
//            view.shop_page_main_profile_location.hide()
//            view.shop_page_main_profile_location.text = shopLocation
//        }
//        ImageHandler.loadImageCircle2(view.context, view.shop_page_main_profile_image, shopPageHeaderDataModel.avatar)
//        if (isMyShop) {
//            view.shop_page_main_profile_background.setOnClickListener {
//                listener.onShopCoverClicked(
//                        shopPageHeaderDataModel.isOfficial,
//                        shopPageHeaderDataModel.isGoldMerchant
//                )
//            }
//        }
//        when {
//            shopPageHeaderDataModel.isOfficial -> displayOfficial()
//            shopPageHeaderDataModel.isGoldMerchant -> displayGoldenShop()
//            else -> {
//                shopPageProfileBadgeView.visibility = View.GONE
//            }
//        }
//        TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_name, MethodChecker.fromHtml(shopPageHeaderDataModel.shopName).toString(), view.shop_page_main_profile_name.context.getString(R.string.content_desc_shop_page_main_profile_name))
//        if (isMyShop) {
//            setupSgcPlayWidget(shopPageHeaderDataModel)
//        }
//
//        if (shopPageHeaderDataModel.isFreeOngkir)
//            showLabelFreeOngkir(remoteConfig)
//        else
//            view.shop_page_main_profile_free_ongkir.hide()
//
//        if(isUsingNewNavigation() && !isMyShop) {
//            view.shop_page_chevron_shop_info.show()
//            view.shop_page_chevron_shop_info.setOnClickListener {
//                listener.openShopInfo()
//            }
//            view.shop_page_main_profile_name.setOnClickListener {
//                listener.openShopInfo()
//            }
//        } else {
//            view.shop_page_chevron_shop_info.setOnClickListener(null)
//            view.shop_page_main_profile_name.setOnClickListener (null)
//            view.shop_page_chevron_shop_info.hide()
//        }
//    }
//
//    fun setupFollowButton(isMyShop: Boolean){
//        if (isMyShop) {
//            hideFollowButton()
//        } else {
//            showFollowButton()
//            view.play_seller_widget_container.visibility = View.GONE
//            updateFavoriteButton()
//        }
//    }
//
//    private fun hideFollowButton(){
//        followButton.visibility = View.GONE
//    }
//
//    fun showFollowButton(){
//        followButton.visibility = View.VISIBLE
//    }
//
//    private fun setupTextContentSgcWidget(){
//        if(view.shop_page_sgc_title.text.isBlank()) {
//            val text = context.getString(R.string.shop_page_play_widget_title)
//            view.shop_page_sgc_title.text = MethodChecker.fromHtml(text)
//        }
//    }
//
    fun setupSgcPlayWidget(shopPageHeaderDataModel: ShopPageHeaderDataModel){
        shopPageHeaderAdapter?.setPlayWidgetData(shopPageHeaderDataModel)
    }
//
//    fun setShopName(shopName: String) {
//        val name = MethodChecker.fromHtml(shopName)
//        val shopNameTv = view.shop_page_main_profile_name
//        if (shopNameTv.text != name) {
//            shopNameTv.text = name
//        }
//    }
//
//    private fun showLabelFreeOngkir(remoteConfig: RemoteConfig) {
//        val labelTitle = remoteConfig.getString(LABEL_SHOP_PAGE_FREE_ONGKIR_TITLE, LABEL_FREE_ONGKIR_DEFAULT_TITLE)
//        if (labelTitle.isNotEmpty()) {
//            view.shop_page_main_profile_free_ongkir.show()
//            view.shop_page_main_profile_free_ongkir.text = labelTitle
//        }
//    }
//
//    fun updateFavoriteData(favoriteData: ShopInfo.FavoriteData) {
//        isShopFavorite = TextApiUtils.isValueTrue(favoriteData.alreadyFavorited.toString())
//        view.shop_page_main_profile_follower_icon.setImageResource(followerImageIcon)
//        view.shop_page_main_profile_follower_icon.show()
//        view.shop_page_main_profile_follower.show()
//        if (favoriteData.totalFavorite > 1) {
//            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_follower, MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_followers,
//                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber())).toString(), view.context.getString(R.string.content_desc_shop_page_main_profile_follower))
//        } else { // if 0 or 1, only print as follower (without s)
//            TextAndContentDescriptionUtil.setTextAndContentDescription(view.shop_page_main_profile_follower, MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_follower,
//                    favoriteData.totalFavorite.toDouble().formatToSimpleNumber())).toString(), view.context.getString(R.string.content_desc_shop_page_main_profile_follower))
//        }
//        updateFavoriteButton()
//    }
//
    fun updateShopTicker(shopPageHeaderDataModel: ShopPageHeaderDataModel, isMyShop: Boolean) {
        shopPageHeaderDataModel.let {
            when {
                shouldShowShopStatusTicker(it.statusTitle, it.statusMessage) -> {
                    showShopStatusTicker(it, isMyShop)
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

    private fun hideShopStatusTicker() {
        view.tickerShopStatus.hide()
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

    fun isShopFavourited() = isShopFavorite

    fun isFollowButtonPlaceHolderAvailable(): Boolean {
        return shopPageHeaderAdapter?.isFollowButtonPlaceholderAvailable() ?: false
    }

    fun setLoadingFollowButton(isLoading: Boolean) {
        shopPageHeaderAdapter?.setLoadingFollowButton(isLoading)
    }

    fun showCoachMark(
            followStatusData: FollowStatus?,
            shopId: String,
            userId: String
    ){
        val coachMarkList = arrayListOf<CoachMark2Item>().apply {
            getShopFollowButtonCoachMarkItem(followStatusData)?.let{
                add(it)
            }
            getChooseAddressWidgetCoachMarkItem()?.let{
                add(it)
            }
        }
        if(coachMarkList.isNotEmpty()){
            coachMark = CoachMark2(context)
            coachMark?.isOutsideTouchable = true
            coachMark?.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    checkCoachMarkImpression(
                            onCoachMarkFollowButtonImpressed = {
                                listener.saveFirstTimeVisit()
                                shopPageTracking?.impressionCoachMarkFollowUnfollowShop(shopId, userId)
                            },
                            onCoachMarkChooseAddressWidgetImpressed = {
                                ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(view.context)
                            }
                    )
                }
            })
            coachMark?.showCoachMark(coachMarkList)
            checkCoachMarkImpression(
                    onCoachMarkFollowButtonImpressed = {
                        listener.saveFirstTimeVisit()
                        shopPageTracking?.impressionCoachMarkFollowUnfollowShop(shopId, userId)
                    },
                    onCoachMarkChooseAddressWidgetImpressed = {
                        ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(view.context)
                    }
            )
        }
    }

    private fun getChooseAddressWidgetCoachMarkItem(): CoachMark2Item? {
        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(view.context)
        return if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
            chooseAddressWidget?.let {
                CoachMark2Item(
                        it,
                        view.context?.getString(R.string.shop_page_choose_address_widget_coachmark_title).orEmpty(),
                        view.context?.getString(R.string.shop_page_choose_address_widget_coachmark_description).orEmpty()
                )
            }
        } else {
            null
        }
    }

    private fun checkCoachMarkImpression(
            onCoachMarkFollowButtonImpressed: () -> Unit,
            onCoachMarkChooseAddressWidgetImpressed: () -> Unit
    ) {
        coachMark?.coachMarkItem?.getOrNull(coachMark?.currentIndex.orZero())?.let {
            when (it.anchorView.id) {
                shopPageHeaderAdapter?.getFollowButtonView()?.id -> {
                    onCoachMarkFollowButtonImpressed.invoke()
                }
                chooseAddressWidget?.id -> {
                    onCoachMarkChooseAddressWidgetImpressed.invoke()
                }
                else -> {}
            }
        }
    }

    private fun getShopFollowButtonCoachMarkItem(
            followStatusData: FollowStatus?
    ): CoachMark2Item? {
        val buttonFollowView = shopPageHeaderAdapter?.getFollowButtonView()
        val coachMarkText = followStatusData?.followButton?.coachmarkText.orEmpty()
        return if (!coachMarkText.isBlank() && listener.isFirstTimeVisit() == false && buttonFollowView != null) {
            CoachMark2Item(
                    anchorView = buttonFollowView,
                    title = "",
                    description = MethodChecker.fromHtml(coachMarkText)
            )
        } else {
            null
        }
    }

    fun isPlayWidgetPlaceHolderAvailable(): Boolean {
        return shopPageHeaderAdapter?.isPlayWidgetPlaceholderAvailable() ?: false
    }

    fun updateShopName(shopName: String) {
        shopPageHeaderAdapter?.setShopName(MethodChecker.fromHtml(shopName).toString())
    }

//    /**
//     * Fetch the animation from http URL and play the animation
//     */
//    private fun setLottieAnimationFromUrl(animationUrl: String) {
//        context?.let {
//            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, animationUrl)
//
//            lottieCompositionLottieTask.addListener { result ->
//                view.lottie?.setComposition(result)
//                view.lottie?.playAnimation()
//            }
//
//            lottieCompositionLottieTask.addFailureListener { }
//        }
//    }

}