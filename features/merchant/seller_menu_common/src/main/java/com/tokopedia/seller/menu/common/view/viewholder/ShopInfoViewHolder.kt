package com.tokopedia.seller.menu.common.view.viewholder

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendClickShopNameTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.analytics.sendShopInfoClickNextButtonTracking
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_balance_topads.view.*
import kotlinx.android.synthetic.main.layout_seller_menu_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_partial_others_local_load.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_shop_status_pm.view.*
import kotlinx.android.synthetic.main.setting_shop_status_regular.view.*

class ShopInfoViewHolder(
    itemView: View,
    private val listener: ShopInfoListener?,
    private val trackingListener: SettingTrackingListener,
    private val userSession: UserSessionInterface?
): AbstractViewHolder<ShopInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_seller_menu_shop_info

        private val GREEN_TIP = R.drawable.setting_tip_bar_enabled
        private val GREEN_TEXT_COLOR = R.color.setting_green
        private val GREY_TIP = R.drawable.setting_tip_bar_disabled
        private val GREY_TEXT_COLOR = R.color.setting_grey_text
        private val RED_TEXT_COLOR = R.color.setting_red_text
        private val GREY_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant_inactive
        private val GREEN_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant
    }

    private val context by lazy { itemView.context }

    override fun bind(uiModel: ShopInfoUiModel) {
        itemView.setOnClickAction()

        with(uiModel.data) {
            itemView.apply {
                when {
                    partialResponseStatus.first && partialResponseStatus.second -> {
                        showNameAndAvatar()

                        shopStatusUiModel?.let { setShopStatusType(it) }
                        saldoBalanceUiModel?.let { setSaldoBalance(it) }
                        topadsBalanceUiModel?.let { setKreditTopadsBalance(it) }
                        shopBadgeUiModel?.let { setShopBadge(it) }
                        shopFollowersUiModel?.let { setShopTotalFollowers(it) }

                        dot?.visible()
                        localLoadOthers?.gone()
                        shopStatus?.visible()
                        saldoBalance?.visible()
                        topAdsBalance?.visible()
                    }
                    partialResponseStatus.first -> {
                        showNameAndAvatar()

                        shopStatusUiModel?.let { setShopStatusType(it) }
                        shopBadgeUiModel?.let { setShopBadge(it) }
                        shopFollowersUiModel?.let { setShopTotalFollowers(it) }

                        dot?.visible()
                        shopStatus?.visible()
                        localLoadOthers?.run {
                            setup()
                            visible()
                        }
                        saldoBalance?.gone()
                        topAdsBalance?.gone()
                    }
                    partialResponseStatus.second -> {
                        showNameAndAvatar()

                        saldoBalanceUiModel?.let { setSaldoBalance(it) }
                        topadsBalanceUiModel?.let { setKreditTopadsBalance(it) }

                        dot?.gone()
                        shopStatus?.gone()
                        localLoadOthers?.run {
                            setup()
                            visible()
                        }
                        saldoBalance?.visible()
                        topAdsBalance?.visible()
                    }
                }
            }
        }
    }

    private fun showNameAndAvatar() {
        setShopName(userSession?.shopName.orEmpty())
        setShopAvatar(ShopAvatarUiModel(userSession?.shopAvatar.orEmpty()))
    }

    private fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        itemView.successShopInfoLayout.shopBadges?.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
            setOnClickListener {
                listener?.onShopBadgeClicked()
                shopBadgeUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        itemView.successShopInfoLayout.shopFollowers?.run {
            text = "${shopTotalFollowersUiModel.shopFollowers} ${context.resources.getString(R.string.setting_followers)}"
            setOnClickListener {
                shopTotalFollowersUiModel.sendSettingShopInfoClickTracking()
                listener?.onFollowersCountClicked()
            }
        }
    }

    private fun setShopName(shopName: String) {
        itemView.run {
            successShopInfoLayout.shopName?.run {
                text = shopName
                setOnClickListener {
                    listener?.onShopInfoClicked()
                    sendClickShopNameTracking()
                }
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        itemView.successShopInfoLayout.shopImage?.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(shopAvatarUiModel, trackingListener::sendImpressionDataIris)
            setOnClickListener {
                listener?.onShopInfoClicked()
                shopAvatarUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        itemView.saldoBalance.run {
            balanceTitle?.text = context.resources.getString(R.string.setting_balance)
            balanceValue?.text = saldoBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(saldoBalanceUiModel, trackingListener::sendImpressionDataIris)
            balanceValue.setOnClickListener {
                listener?.onSaldoClicked()
                saldoBalanceUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    private fun setKreditTopadsBalance(topadsBalanceUiModel: TopadsBalanceUiModel) {
        itemView.topAdsBalance.run {
            topadsBalanceTitle?.text = context.resources.getString(R.string.setting_topads_credits)
            topadsBalanceValue?.text = topadsBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(topadsBalanceUiModel, trackingListener::sendImpressionDataIris)
            topadsBalanceValue.setOnClickListener {
                listener?.onKreditTopadsClicked()
                topadsBalanceUiModel.sendSettingShopInfoClickTracking()
            }
            val isTopAdsUser = topadsBalanceUiModel.isTopAdsUser
            val topAdsTooltipDrawable =
                if (isTopAdsUser) {
                    ContextCompat.getDrawable(context, R.drawable.ic_topads_active)
                } else {
                    ContextCompat.getDrawable(context, R.drawable.ic_topads_inactive)
                }
            topAdsStatusTooltip.run {
                setImageDrawable(topAdsTooltipDrawable)
                setOnClickListener {
                    listener?.onTopAdsTooltipClicked(isTopAdsUser)
                }
            }
        }
    }

    private fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
        val shopType = shopStatusUiModel.shopType
        val itemView: View = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View? = when(shopType) {
            is RegularMerchant -> {
                listener?.onStatusBarNeedDarkColor(true)
                itemView.apply {
                    setRegularMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    rightRectangle.setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is PowerMerchantStatus -> {
                listener?.onStatusBarNeedDarkColor(false)
                itemView.apply {
                    setPowerMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is ShopType.OfficialStore -> {
                listener?.onStatusBarNeedDarkColor(false)
                itemView.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                }
            }
        }

        shopStatusLayout?.let { view ->
            (this.itemView.shopStatus as LinearLayout).run {
                removeAllViews()
                addView(view)
            }
        }
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant) : View {
        regularMerchantStatus.run {
            text = when(regularMerchant) {
                is RegularMerchant.NeedUpgrade -> context.resources.getString(R.string.setting_upgrade)
                is RegularMerchant.NeedVerification -> context.resources.getString(R.string.setting_verifikasi)
            }
        }

        return this
    }

    private fun View.setPowerMerchantShopStatus(powerMerchantStatus: PowerMerchantStatus) : View {
        var statusText = context.resources.getString(R.string.setting_on_verification)
        var textColor = GREY_TEXT_COLOR
        var statusDrawable = GREY_TIP
        var powerMerchantDrawableIcon = GREY_POWER_MERCHANT_ICON
        when(powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                statusText = context.resources.getString(R.string.setting_active)
                textColor = GREEN_TEXT_COLOR
                powerMerchantDrawableIcon = GREEN_POWER_MERCHANT_ICON
                statusDrawable = GREEN_TIP
            }
            is PowerMerchantStatus.NotActive -> {
                statusText = context.resources.getString(R.string.setting_not_active)
                textColor = RED_TEXT_COLOR
            }
            is PowerMerchantStatus.OnVerification -> {
                powerMerchantText?.text = context.resources.getString(R.string.regular_merchant)
            }
        }
        powerMerchantStatusText?.text = statusText
        powerMerchantStatusText?.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerMerchantLeftStatus?.background = ResourcesCompat.getDrawable(resources, statusDrawable, null)
        } else {
            powerMerchantLeftStatus?.let {
                (it as? ImageView)?.setImageDrawable(ResourcesCompat.getDrawable(resources, statusDrawable, null))
            }
        }
        powerMerchantIcon?.setImageDrawable(ResourcesCompat.getDrawable(resources, powerMerchantDrawableIcon, null))
        return this
    }

    private fun View.setOnClickAction() {
        successShopInfoLayout?.run {
            settingShopNext.setOnClickListener {
                listener?.onShopInfoClicked()
                sendShopInfoClickNextButtonTracking()
            }
        }
    }

    private fun LocalLoad.setup() {
        title?.text = context.resources.getString(R.string.setting_error_message)
        description?.text = context.resources.getString(R.string.setting_error_description)
        refreshBtn?.setOnClickListener {
            listener?.onRefreshShopInfo()
        }
    }

    interface ShopInfoListener {
        fun onShopInfoClicked()
        fun onShopBadgeClicked()
        fun onFollowersCountClicked()
        fun onSaldoClicked()
        fun onRefreshShopInfo()
        fun onKreditTopadsClicked()
        fun onStatusBarNeedDarkColor(isDefaultDark: Boolean)
        fun onTopAdsTooltipClicked(isTopAdsActive: Boolean)
    }
}