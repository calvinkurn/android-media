package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.*
import com.tokopedia.sellerhome.settings.view.bottomsheet.SettingsFreeShippingBottomSheet
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.*
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_other_menu.view.*
import kotlinx.android.synthetic.main.fragment_other_menu.view.shopInfoLayout
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_balance_topads.view.*
import kotlinx.android.synthetic.main.setting_partial_main_info_success.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_error.view.*
import kotlinx.android.synthetic.main.setting_partial_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_shop_status_pm.view.*
import kotlinx.android.synthetic.main.setting_shop_status_regular.view.*
import timber.log.Timber

class OtherMenuViewHolder(private val itemView: View,
                          private val context: Context,
                          private val listener: Listener,
                          private val trackingListener: SettingTrackingListener) {

    companion object {
        private val GREEN_TIP = R.drawable.setting_tip_bar_enabled
        private val GREEN_TEXT_COLOR = R.color.setting_green
        private val GREY_TIP = R.drawable.setting_tip_bar_disabled
        private val GREY_TEXT_COLOR = R.color.setting_grey_text
        private val RED_TEXT_COLOR = R.color.setting_red_text
        private val GREY_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant_inactive
        private val GREEN_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant

        private const val HEADER_ICON_WIDTH_PERCENTAGE = 0.117
        private const val HEADER_ICON_X_POSITION = 0.867
        private const val HEADER_ICON_RATIO = 0.9
    }

    fun onSuccessGetSettingShopInfoData(uiModel: SettingShopInfoUiModel) {
        val successLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_success, null, false)
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(successLayout)
            setOnClickAction()
        }
        uiModel.run {
            setShopName(shopName)
            setShopAvatar(shopAvatarUiModel)
            setShopStatusType(shopStatusUiModel)
            setSaldoBalance(saldoBalanceUiModel)
            setKreditTopadsBalance(topadsBalanceUiModel)
            setShopBadge(shopBadgeUiModel)
            setShopTotalFollowers(shopFollowersUiModel)
        }
    }

    fun onLoadingGetSettingShopInfoData() {
        val loadingLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_loading, null, false)
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(loadingLayout)
        }
    }

    fun onErrorGetSettingShopInfoData() {
        val errorLayout = LayoutInflater.from(context).inflate(R.layout.setting_partial_shop_info_error, null, false)
        errorLayout?.settingLocalLoad?.run {
            title?.text = context.resources.getString(R.string.setting_error_message)
            description?.text = context.resources.getString(R.string.setting_error_description)
            refreshBtn?.setOnClickListener {
                listener.onRefreshShopInfo()
            }
        }
        (itemView.shopInfoLayout as? LinearLayout)?.run {
            removeAllViews()
            addView(errorLayout)
        }
    }

    private fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        itemView.shopInfoLayout.shopBadges?.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
            setOnClickListener {
                listener.onShopBadgeClicked()
                shopBadgeUiModel.sendSettingShopInfoClickTracking()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        itemView.shopInfoLayout.shopFollowers?.run {
            text = "${shopTotalFollowersUiModel.shopFollowers} ${context.resources.getString(R.string.setting_followers)}"
            setOnClickListener {
                shopTotalFollowersUiModel.sendSettingShopInfoClickTracking()
                listener.onFollowersCountClicked()
            }
        }
    }

    fun setupFreeShippingLayout(
        fm: FragmentManager?,
        user: UserSessionInterface
    ) {
        itemView.shopInfoLayout.freeShippingLayout?.apply {
            val freeShippingBottomSheet = SettingsFreeShippingBottomSheet.createInstance()

            setOnClickListener {
                freeShippingBottomSheet.show(fm)
                SettingFreeShippingTracker.trackFreeShippingClick(user)
            }
            show()

            SettingFreeShippingTracker.trackFreeShippingImpression(user)
        }
    }

    fun hideFreeShippingLayout() {
        itemView.shopInfoLayout.freeShippingLayout?.hide()
    }

    private fun setShopName(shopName: String) {
        itemView.run {
            shopInfoLayout.shopName?.run {
                text = shopName
                setOnClickListener {
                    listener.onShopInfoClicked()
                    sendClickShopNameTracking()
                }
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        itemView.shopInfoLayout.shopImage?.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(shopAvatarUiModel, trackingListener::sendImpressionDataIris)
            setOnClickListener {
                listener.onShopInfoClicked()
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
                listener.onSaldoClicked()
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
                listener.onKreditTopadsClicked()
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
                    listener.onTopAdsTooltipClicked(isTopAdsUser)
                }
            }
        }
    }

    private fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
        val shopType = shopStatusUiModel.shopType
        showShopStatusHeader(shopType)
        val layoutInflater = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View = when(shopType) {
            is RegularMerchant -> {
                listener.onStatusBarNeedDarkColor(true)
                layoutInflater.apply {
                    setRegularMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    rightRectangle.setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is PowerMerchantStatus -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.apply {
                    setPowerMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        shopStatusUiModel.sendSettingShopInfoClickTracking()
                    }
                }
            }
            is ShopType.OfficialStore -> {
                listener.onStatusBarNeedDarkColor(false)
                layoutInflater.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                }
            }
        }
        (itemView.shopStatus as LinearLayout).run {
            removeAllViews()
            addView(shopStatusLayout)
        }
    }

    private fun showShopStatusHeader(shopType: ShopType) {
        if (shopType is RegularMerchant) {
            itemView.shopStatusHeader?.setImageDrawable(ContextCompat.getDrawable(context, shopType.shopTypeHeaderRes))
        } else {
            try {
                itemView.shopStatusHeader?.setImageBitmap(shopType.shopTypeHeaderIconRes?.let { getShopStatusHeaderBitmap(shopType.shopTypeHeaderRes, it) })
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    private fun getShopStatusHeaderBitmap(@DrawableRes shopTypeHeaderRes: Int,
                                          @DrawableRes shopTypeHeaderIconRes: Int): Bitmap? {
        return try {
            val iconPaint = Paint().apply {
                isAntiAlias = true
                isFilterBitmap = true
                isDither = true
            }

            ResourcesCompat.getDrawable(context.resources, shopTypeHeaderRes, null)?.toBitmap()?.let { baseBitmap ->
                val baseBitmapCopy = baseBitmap.copy(Bitmap.Config.ARGB_8888, true)
                ResourcesCompat.getDrawable(context.resources, shopTypeHeaderIconRes, null)?.toBitmap()?.let { iconBitmap ->
                    val iconWidth = (baseBitmapCopy.width * HEADER_ICON_WIDTH_PERCENTAGE).toInt()
                    val iconHeight = (iconWidth * HEADER_ICON_RATIO).toInt()
                    val iconX = (baseBitmapCopy.width * HEADER_ICON_X_POSITION).toInt()
                    Canvas(baseBitmapCopy).run {
                        val iconBitmapRect = Rect().apply {
                            set(iconX - iconWidth, baseBitmapCopy.height - iconHeight, iconX, baseBitmapCopy.height)
                        }
                        drawBitmap(iconBitmap, null, iconBitmapRect, iconPaint)
                    }
                    baseBitmapCopy
                }
            }
        }
        catch (ex: Exception) {
            null
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
                statusDrawable = GREEN_TIP }
            is PowerMerchantStatus.NotActive -> {
                statusText = context.resources.getString(R.string.setting_not_active)
                textColor = RED_TEXT_COLOR }
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
        shopInfoLayout?.run {
            settingShopNext.setOnClickListener {
                listener.onShopInfoClicked()
                sendShopInfoClickNextButtonTracking()
            }
        }
    }

    interface Listener {
        fun onShopInfoClicked()
        fun onShopBadgeClicked()
        fun onFollowersCountClicked()
        fun onSaldoClicked()
        fun onKreditTopadsClicked()
        fun onRefreshShopInfo()
        fun onStatusBarNeedDarkColor(isDefaultDark: Boolean)
        fun onTopAdsTooltipClicked(isTopAdsActive: Boolean)
    }

}