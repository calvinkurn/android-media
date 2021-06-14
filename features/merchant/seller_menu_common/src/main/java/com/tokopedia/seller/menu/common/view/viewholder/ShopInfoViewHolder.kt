package com.tokopedia.seller.menu.common.view.viewholder

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.PMProURL
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.layout_seller_menu_shop_info.view.*
import kotlinx.android.synthetic.main.layout_seller_menu_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_partial_others_local_load.view.*
import java.util.*

class ShopInfoViewHolder(
        itemView: View,
        private val listener: ShopInfoListener?,
        private val trackingListener: SettingTrackingListener,
        private val userSession: UserSessionInterface?,
        private val sellerMenuTracker: SellerMenuTracker?
) : AbstractViewHolder<ShopInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_seller_menu_shop_info

        private val GREY_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N700_68

        private val TEAL_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_T500
        private val YELLOW_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_Y400

        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        private const val TAB_PM_PARAM = "tab"
        private const val TAB_PM = "pm"
        private const val TAB_PM_PRO = "pm_pro"
    }

    private val context by lazy { itemView.context }

    override fun bind(uiModel: ShopInfoUiModel) {
        with(uiModel.shopInfo) {
            itemView.apply {
                when {
                    partialResponseStatus.first && partialResponseStatus.second -> {
                        showNameAndAvatar()

                        shopStatusUiModel?.let { setShopStatusType(it) }
                        saldoBalanceUiModel?.let { setSaldoBalance(it) }
                        shopBadgeUiModel?.let { setShopBadge(it) }
                        shopFollowersUiModel?.let {
                            setShopTotalFollowers(it)
                            setDotVisibility(it.shopFollowers)
                        }

                        localLoadOthers?.gone()
                        shopStatus?.visible()
                        saldoBalance?.visible()
                    }
                    partialResponseStatus.first -> {
                        showNameAndAvatar()

                        shopStatusUiModel?.let { setShopStatusType(it) }
                        shopBadgeUiModel?.let { setShopBadge(it) }
                        shopFollowersUiModel?.let {
                            setShopTotalFollowers(it)
                            setDotVisibility(it.shopFollowers)
                        }

                        shopStatus?.visible()
                        localLoadOthers?.run {
                            setup()
                            visible()
                        }
                        saldoBalance?.gone()
                    }
                    partialResponseStatus.second -> {
                        showNameAndAvatar()

                        saldoBalanceUiModel?.let { setSaldoBalance(it) }

                        dot?.gone()
                        shopStatus?.gone()
                        localLoadOthers?.run {
                            setup()
                            visible()
                        }
                        saldoBalance?.visible()
                    }
                }
                showShopScore(uiModel)
            }
        }
    }

    private fun setDotVisibility(shopFollowers: Long) {
        val shouldShowFollowers = shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val dotVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        itemView.successShopInfoLayout?.dot?.visibility = dotVisibility
    }

    private fun showNameAndAvatar() {
        setShopName(userSession?.shopName.orEmpty())
        setShopAvatar(ShopAvatarUiModel(userSession?.shopAvatar.orEmpty()))
    }

    private fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        itemView.successShopInfoLayout.shopBadges?.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
        }
    }

    private fun showShopScore(uiModel: ShopInfoUiModel) {
        val shopAgeSixty = 60
        with(itemView) {
            if (uiModel.shopAge < shopAgeSixty) {
                shopScore.text = getString(R.string.seller_menu_shop_score_empty_label)
                shopScore.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                shopScoreMaxLabel?.hide()
            } else {
                shopScore.text = uiModel.shopScore.toString()
                shopScore.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                shopScoreMaxLabel?.show()
            }
            shopScoreLayout.setOnClickListener {
                listener?.onScoreClicked()
            }
            listener?.onScoreImpressed()
        }
    }

    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        val shouldShowFollowers = shopTotalFollowersUiModel.shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val followersVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        itemView.successShopInfoLayout.shopFollowers?.run {
            visibility = followersVisibility
            text = StringBuilder("${shopTotalFollowersUiModel.shopFollowers} ${context.resources.getString(R.string.setting_followers)}")
            setOnClickListener {
                shopTotalFollowersUiModel.sendSettingShopInfoClickTracking()
                goToShopFavouriteList()
            }
        }
        itemView.successShopInfoLayout.dot.visibility = followersVisibility
    }

    private fun setShopName(shopName: String) {
        itemView.run {
            successShopInfoLayout.shopName?.run {
                text = MethodChecker.fromHtml(shopName)
                setOnClickListener {
                    goToShopPage()
                    sellerMenuTracker?.sendEventClickShopName()
                }
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        itemView.successShopInfoLayout.shopImage?.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(shopAvatarUiModel, trackingListener::sendImpressionDataIris)
            setOnClickListener {
                goToShopPage()
                sellerMenuTracker?.sendEventClickShopPicture()
            }
        }
    }

    private fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        itemView.saldoBalance.run {
            balanceTitle?.text = context.resources.getString(R.string.setting_balance)
            balanceValue?.text = saldoBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(saldoBalanceUiModel, trackingListener::sendImpressionDataIris)
            listOf(balanceTitle, balanceValue).forEach {
                it.setOnClickListener {
                    listener?.onSaldoClicked()
                    sellerMenuTracker?.sendEventClickSaldoBalance()
                }
            }
        }
    }

    private fun setShopStatusType(shopStatusUiModel: ShopStatusUiModel) {
        val shopType = shopStatusUiModel.userShopInfoWrapper.shopType
        val itemView: View? = shopType?.shopTypeLayoutRes?.let {
            LayoutInflater.from(context).inflate(it, null, false)
        }
        val shopStatusLayout: View? = when (shopType) {
            is RegularMerchant -> {
                itemView?.apply {
                    setRegularMerchantShopStatus(shopType, shopStatusUiModel)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }
            }
            is PowerMerchantStatus -> {
                itemView?.apply {
                    setPowerMerchantShopStatus(shopType, shopStatusUiModel)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                    }
                }
            }
            is ShopType.OfficialStore -> {
                itemView?.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                }
            }

            is PowerMerchantProStatus.Advanced -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }

            }
            is PowerMerchantProStatus.Expert -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }
            }
            is PowerMerchantProStatus.Ultimate -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }
            }
            is PowerMerchantProStatus.InActive -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }
            }
            else -> null
        }

        val paddingTop = itemView?.resources?.getDimensionPixelSize(R.dimen.spacing_lvl3)
        val paddingBottom = itemView?.resources?.getDimensionPixelSize(R.dimen.setting_status_padding_bottom)
        if (paddingTop != null && paddingBottom != null) {
            itemView.setPadding(0, paddingTop, 0, paddingBottom)
        }

        shopStatusLayout?.let { view ->
            this.itemView.shopStatus?.run {
                removeAllViews()
                addView(view)
            }
        }
    }

    private fun goToPowerMerchantSubscribe(tab: String) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTab = Uri.parse(appLink).buildUpon().appendQueryParameter(TAB_PM_PARAM, tab).build().toString()
        context?.let { RouteManager.route(context, appLinkPMTab) }
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant, shopStatusUiModel: ShopStatusUiModel): View {
        val userShopInfo = shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel
        val txStatsRM = findViewById<Typography>(R.id.tx_stats_rm)
        val txTotalStatsRM = findViewById<Typography>(R.id.tx_total_stats_rm)
        val regularMerchantStatus = findViewById<Typography>(R.id.regularMerchantStatus)

        regularMerchantStatus.run {
            text = when (regularMerchant) {
                is RegularMerchant.NeedUpgrade -> context.resources.getString(R.string.setting_upgrade)
            }
        }

        val thresholdTransaction = 110
        val maxTransaction = 100
        val totalTransaction = userShopInfo?.totalTransaction ?: 0
        if (totalTransaction >= thresholdTransaction) {
            hideTransactionSection()
        } else {
            if (userShopInfo?.periodTypePmPro == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                showTransactionSection()
                if (totalTransaction > maxTransaction) {
                    txStatsRM.text = MethodChecker.fromHtml(getString(R.string.transaction_passed))
                    txTotalStatsRM.hide()
                } else {
                    if (userShopInfo.isBeforeOnDate) {
                        txStatsRM.text = context?.getString(R.string.transaction_on_date)
                    } else {
                        txStatsRM.text = context?.getString(R.string.transaction_since_joining)
                    }
                    txTotalStatsRM.show()
                    txTotalStatsRM.text = getString(R.string.total_transaction, totalTransaction.toString())
                }
            } else {
                hideTransactionSection()
            }
        }
        return this
    }

    private fun View.hideTransactionSection() {
        findViewById<View>(R.id.divider_stats_rm)?.hide()
        findViewById<Typography>(R.id.tx_stats_rm)?.hide()
        findViewById<Typography>(R.id.tx_total_stats_rm)?.hide()
    }

    private fun View.showTransactionSection() {
        findViewById<View>(R.id.divider_stats_rm)?.show()
        findViewById<View>(R.id.divider_stats_rm)?.setBackgroundResource(R.drawable.ic_divider_stats_rm)
        findViewById<Typography>(R.id.tx_stats_rm)?.show()
        findViewById<Typography>(R.id.tx_total_stats_rm)?.show()
    }

    private fun View.setPowerMerchantShopStatus(powerMerchantStatus: PowerMerchantStatus, statusUiModel: ShopStatusUiModel): View {
        val upgradePMTextView: Typography = findViewById(R.id.upgradePMText)
        val powerMerchantStatusTextView: Typography = findViewById(R.id.powerMerchantStatusText)
        val powerMerchantText: Typography = findViewById(R.id.powerMerchantText)
        val periodType = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.periodTypePmPro
        when (powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                if (periodType == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                    upgradePMTextView.show()
                } else if (periodType == Constant.COMMUNICATION_PERIOD_PM_PRO) {
                    upgradePMTextView.hide()
                }
                powerMerchantText.text = getString(R.string.power_merchant_upgrade)

                powerMerchantStatusTextView.hide()
            }
            is PowerMerchantStatus.NotActive -> {
                powerMerchantStatusTextView.show()
                upgradePMTextView.hide()
                powerMerchantText.text = getString(R.string.power_merchant_status)

                powerMerchantStatusTextView.setOnClickListener {
                    goToPowerMerchantSubscribe(TAB_PM_PRO)
                    sellerMenuTracker?.sendEventClickShopType()
                }
            }
        }
        return this
    }

    private fun View.setPowerMerchantProStatus(shopStatusUiModel: ShopStatusUiModel, powerMerchantStatus: PowerMerchantProStatus): View {
        val goldOS = shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel
        val ivBgPMPro = findViewById<ShapeableImageView>(R.id.iv_bg_pm_pro)
        val powerMerchantProStatusText = findViewById<Typography>(R.id.powerMerchantProStatusText)
        val powerMerchantProIcon = findViewById<IconUnify>(R.id.powerMerchantProIcon)

        when (powerMerchantStatus) {
            is PowerMerchantProStatus.Advanced -> {
                ivBgPMPro.loadImage(PMProURL.BG_ADVANCE)
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, GREY_TEXT_COLOR))
                powerMerchantProStatusText.text = goldOS?.pmProGradeName?.capitalize(Locale.getDefault())
                        ?: ""
            }
            is PowerMerchantProStatus.Expert -> {
                ivBgPMPro.loadImage(PMProURL.BG_EXPERT)
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, TEAL_TEXT_COLOR))
                powerMerchantProStatusText.text = goldOS?.pmProGradeName?.capitalize(Locale.getDefault())
                        ?: ""
            }
            is PowerMerchantProStatus.Ultimate -> {
                ivBgPMPro.loadImage(PMProURL.BG_ULTIMATE)
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, YELLOW_TEXT_COLOR))
                powerMerchantProStatusText.text = goldOS?.pmProGradeName?.capitalize(Locale.getDefault())
                        ?: ""
            }
            is PowerMerchantProStatus.InActive -> {
                powerMerchantProStatusText.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                powerMerchantProStatusText.text = getString(R.string.setting_not_active)
            }
        }

        val roundedRadius = 16F
        ivBgPMPro.shapeAppearanceModel = ivBgPMPro.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, roundedRadius)
                .build()
        powerMerchantProIcon.loadImage(if (goldOS?.badge?.isBlank() == true) PMProURL.ICON_URL else goldOS?.badge)
        return this
    }

    private fun LocalLoad.setup() {
        title?.text = context.resources.getString(R.string.setting_error_message)
        description?.text = context.resources.getString(R.string.setting_error_description)
        refreshBtn?.setOnClickListener {
            listener?.onRefreshShopInfo()
        }
    }

    private fun goToShopFavouriteList() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST)
        intent.putExtra(EXTRA_SHOP_ID, userSession?.shopId)
        context.startActivity(intent)
    }

    private fun goToShopPage() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PAGE, userSession?.shopId)
    }

    interface ShopInfoListener {
        fun onScoreClicked()
        fun onScoreImpressed()
        fun onSaldoClicked()
        fun onRefreshShopInfo()
    }
}