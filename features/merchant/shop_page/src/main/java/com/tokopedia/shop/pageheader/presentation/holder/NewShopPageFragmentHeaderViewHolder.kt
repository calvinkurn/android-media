package com.tokopedia.shop.pageheader.presentation.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.shop.common.constant.ShopPageConstant.ShopTickerType
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.databinding.NewShopPageFragmentContentLayoutBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.adapter.ShopPageHeaderAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component.*
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderBasicInfoWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget.ShopHeaderPlayWidgetViewHolder
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopRequestUnmoderateBottomSheet
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageTickerData
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class NewShopPageFragmentHeaderViewHolder(
    private val viewBindingShopContentLayout: NewShopPageFragmentContentLayoutBinding?,
    private val listener: ShopPageFragmentViewHolderListener,
    private val shopPageTracking: ShopPageTrackingBuyer?,
    private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
    private val context: Context,
    private val shopHeaderBasicInfoWidgetListener: ShopHeaderBasicInfoWidgetViewHolder.Listener,
    private val shopPerformanceWidgetBadgeTextValueListener: ShopPerformanceWidgetBadgeTextValueComponentViewHolder.Listener,
    private val shopPerformanceWidgetImageOnlyListener: ShopPerformanceWidgetImageOnlyComponentViewHolder.Listener,
    private val shopActionButtonWidgetChatButtonComponentListener: ShopActionButtonWidgetChatButtonComponentViewHolder.Listener,
    private val shopActionButtonWidgetFollowButtonComponentListener: ShopActionButtonWidgetFollowButtonComponentViewHolder.Listener,
    private val shopActionButtonWidgetNoteButtonComponentListener: ShopActionButtonWidgetNoteButtonComponentViewHolder.Listener,
    private val shopPlayWidgetListener: ShopHeaderPlayWidgetViewHolder.Listener,
    private val chooseAddressWidgetListener: ChooseAddressWidget.ChooseAddressWidgetListener,
    private val shopPerformanceWidgetImageTextListener: ShopPerformanceWidgetImageTextComponentViewHolder.Listener
) {
    private var isShopFavorite = false
    private var isUserNeverFollow = false
    private val chooseAddressWidget: ChooseAddressWidget?
        get() = viewBindingShopContentLayout?.layoutPartialShopPageHeader?.chooseAddressWidget
    private var coachMark: CoachMark2? = null

    private var shopPageHeaderAdapter: ShopPageHeaderAdapter? = null
    private val chooseAddressWidgetBottomShadow: View? = viewBindingShopContentLayout?.layoutPartialShopPageHeader?.chooseeAddressWidgetBottomShadow
    private val rvShopPageHeaderWidget: RecyclerView? = viewBindingShopContentLayout?.layoutPartialShopPageHeader?.rvShopPageHeaderWidget
    private val tickerShopStatus: Ticker? = viewBindingShopContentLayout?.layoutPartialShopPageHeader?.tickerShopStatus

    fun updateChooseAddressWidget() {
        chooseAddressWidget?.updateWidget()
    }

    fun hideChooseAddressWidget() {
        chooseAddressWidget?.hide()
    }

    fun setupChooseAddressWidget(remoteConfig: RemoteConfig, isMyShop: Boolean) {
        chooseAddressWidget?.apply {
            val isRollOutUser = true
            val isRemoteConfigChooseAddressWidgetEnabled = remoteConfig.getBoolean(
                ShopPageConstant.ENABLE_SHOP_PAGE_HEADER_CHOOSE_ADDRESS_WIDGET,
                true
            )
            if (isRollOutUser && isRemoteConfigChooseAddressWidgetEnabled && !isMyShop) {
                show()
                bindChooseAddress(chooseAddressWidgetListener)
                chooseAddressWidgetBottomShadow?.show()
            } else {
                chooseAddressWidgetBottomShadow?.hide()
                hide()
            }
        }
    }

    fun setShopHeaderWidgetData(listWidget: List<ShopHeaderWidgetUiModel>) {
        shopPageHeaderAdapter = ShopPageHeaderAdapter(
            ShopPageHeaderAdapterTypeFactory(
                shopHeaderBasicInfoWidgetListener,
                shopPerformanceWidgetBadgeTextValueListener,
                shopPerformanceWidgetImageOnlyListener,
                shopActionButtonWidgetChatButtonComponentListener,
                shopActionButtonWidgetFollowButtonComponentListener,
                shopActionButtonWidgetNoteButtonComponentListener,
                shopPageTrackingSGCPlayWidget,
                shopPlayWidgetListener,
                shopPerformanceWidgetImageTextListener
            )
        )
        rvShopPageHeaderWidget?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvShopPageHeaderWidget?.itemAnimator = null
        rvShopPageHeaderWidget?.adapter = shopPageHeaderAdapter
        shopPageHeaderAdapter?.setData(listWidget)
    }

    fun setFollowStatus(followStatus: FollowStatus?) {
        isShopFavorite = followStatus?.status?.userIsFollowing == true
        isUserNeverFollow = followStatus?.status?.userNeverFollow == true
        followStatus?.let {
            shopPageHeaderAdapter?.setFollowButtonData(
                it.followButton?.buttonLabel.orEmpty(),
                it.followButton?.voucherIconURL.orEmpty(),
                isShopFavorite,
                isUserNeverFollow
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

    fun setupSgcPlayWidget(shopPageHeaderDataModel: ShopPageHeaderDataModel) {
        shopPageHeaderAdapter?.setPlayWidgetData(shopPageHeaderDataModel)
    }

    fun updateShopTicker(tickerData: ShopPageTickerData, isMyShop: Boolean) {
        when {
            shouldShowShopStatusTicker(tickerData.shopInfo.statusInfo.statusTitle, tickerData.shopInfo.statusInfo.statusMessage) -> {
                showShopStatusTicker(tickerData.shopInfo, isMyShop)
            }
            shouldShowShopStatusTicker(tickerData.shopOperationalHourStatus.tickerTitle, tickerData.shopOperationalHourStatus.tickerMessage) -> {
                showShopOperationalHourStatusTicker(tickerData.shopOperationalHourStatus, isMyShop)
            }
            else -> {
                hideShopStatusTicker()
            }
        }
    }

    private fun shouldShowShopStatusTicker(title: String, message: String): Boolean {
        return !(title.isEmpty() && message.isEmpty())
    }

    private fun showShopOperationalHourStatusTicker(shopOperationalHourStatus: ShopOperationalHourStatus, isMyShop: Boolean = false) {
        tickerShopStatus?.show()
        tickerShopStatus?.tickerType = if (isMyShop) {
            Ticker.TYPE_WARNING
        } else {
            Ticker.TYPE_ANNOUNCEMENT
        }
        tickerShopStatus?.tickerTitle = HtmlLinkHelper(context, shopOperationalHourStatus.tickerTitle).spannedString.toString()
        tickerShopStatus?.setHtmlDescription(shopOperationalHourStatus.tickerMessage)
        tickerShopStatus?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener.onShopStatusTickerClickableDescriptionClicked(linkUrl)
            }

            override fun onDismiss() {}
        })
        if (isMyShop) {
            tickerShopStatus?.closeButtonVisibility = View.GONE
        } else {
            tickerShopStatus?.closeButtonVisibility = View.VISIBLE
        }
    }

    private fun showShopStatusTicker(shopInfo: ShopInfo, isMyShop: Boolean = false) {
        val statusTitle = shopInfo.statusInfo.statusTitle
        val shopStatus = shopInfo.statusInfo.shopStatus
        val shopTickerType = shopInfo.statusInfo.tickerType
        val statusMessage = shopInfo.statusInfo.statusMessage
        val shopId = shopInfo.shopCore.shopID
        val isOfficialStore = shopInfo.goldOS.isOfficialStore()
        val isGoldMerchant = shopInfo.goldOS.isGoldMerchant()
        tickerShopStatus?.show()
        tickerShopStatus?.tickerType = when (shopTickerType) {
            ShopTickerType.INFO -> Ticker.TYPE_ANNOUNCEMENT
            ShopTickerType.WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_WARNING
        }
        tickerShopStatus?.tickerTitle = MethodChecker.fromHtml(statusTitle).toString()
        tickerShopStatus?.setHtmlDescription(
            if (shopStatus == ShopStatusDef.MODERATED && isMyShop) {
                generateShopModerateTickerDescription(statusMessage)
            } else {
                statusMessage
            }
        )
        tickerShopStatus?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                // set tracker data based on shop status
                when (shopStatus) {
                    ShopStatusDef.CLOSED -> {
                        shopPageTracking?.sendOpenShop()
                        shopPageTracking?.clickOpenOperationalShop(
                            CustomDimensionShopPage
                                .create(
                                    shopId,
                                    isOfficialStore,
                                    isGoldMerchant
                                )
                        )
                    }
                    ShopStatusDef.NOT_ACTIVE -> {
                        shopPageTracking?.clickHowToActivateShop(
                            CustomDimensionShopPage
                                .create(
                                    shopId,
                                    isOfficialStore,
                                    isGoldMerchant
                                )
                        )
                    }
                }
                if (linkUrl == context.getString(R.string.shop_page_header_request_unmoderate_appended_text_dummy_url)) {
                    // linkUrl is from appended moderate description, show bottomsheet to request open moderate
                    listener.setShopUnmoderateRequestBottomSheet(
                        ShopRequestUnmoderateBottomSheet.createInstance().apply {
                            init(listener)
                        }
                    )
                } else {
                    // original url, open web view
                    listener.onShopStatusTickerClickableDescriptionClicked(linkUrl)
                }
            }

            override fun onDismiss() {}
        })

        // special handling for shop status incubated
        if (shopInfo.statusInfo.shopStatus == ShopStatusDef.INCUBATED) {
            // always show ticker close button if shop is incubated
            tickerShopStatus?.closeButtonVisibility = View.VISIBLE
        } else {
            // default general condition for shop ticker
            if (isMyShop) {
                tickerShopStatus?.closeButtonVisibility = View.GONE
            } else {
                tickerShopStatus?.closeButtonVisibility = View.VISIBLE
            }
        }
    }

    private fun hideShopStatusTicker() {
        tickerShopStatus?.hide()
    }

    private fun generateShopModerateTickerDescription(originalStatusMessage: String): String {
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
    ) {
        val coachMarkList = arrayListOf<CoachMark2Item>().apply {
            getShopFollowButtonCoachMarkItem(followStatusData)?.let {
                add(it)
            }
            getChooseAddressWidgetCoachMarkItem()?.let {
                add(it)
            }
        }
        if (coachMarkList.isNotEmpty()) {
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
                            ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(context)
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
                    ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(context)
                }
            )
        }
    }

    private fun getChooseAddressWidgetCoachMarkItem(): CoachMark2Item? {
        val isNeedToShowCoachMark = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(context)
        return if (isNeedToShowCoachMark == true && chooseAddressWidget?.isShown == true) {
            chooseAddressWidget?.let {
                CoachMark2Item(
                    it,
                    context.getString(R.string.shop_page_choose_address_widget_coachmark_title).orEmpty(),
                    context.getString(R.string.shop_page_choose_address_widget_coachmark_description).orEmpty()
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
        if (shopName.isNotEmpty())
            shopPageHeaderAdapter?.setShopName(MethodChecker.fromHtml(shopName).toString())
    }
}
