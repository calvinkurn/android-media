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
import com.tokopedia.seller.menu.common.constant.SellerBaseUrl
import com.tokopedia.seller.menu.common.databinding.LayoutSellerMenuShopInfoBinding
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
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

    private val binding = LayoutSellerMenuShopInfoBinding.bind(itemView)

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

                        binding.layoutSahOtherLocalLoad.localLoadOthers.gone()
                        binding.shopStatus.visible()
                        binding.layoutSahOtherSaldo.layoutSahOtherSettingBalance.visible()
                    }
                    partialResponseStatus.first -> {
                        showNameAndAvatar()

                        shopStatusUiModel?.let { setShopStatusType(it) }
                        shopBadgeUiModel?.let { setShopBadge(it) }
                        shopFollowersUiModel?.let {
                            setShopTotalFollowers(it)
                            setDotVisibility(it.shopFollowers)
                        }

                        binding.shopStatus.visible()
                        binding.layoutSahOtherLocalLoad.localLoadOthers.run {
                            setup()
                            visible()
                        }
                        binding.layoutSahOtherSaldo.layoutSahOtherSettingBalance.gone()
                    }
                    partialResponseStatus.second -> {
                        showNameAndAvatar()

                        saldoBalanceUiModel?.let { setSaldoBalance(it) }

                        binding.successShopInfoLayout.dot.gone()
                        binding.shopStatus.gone()
                        binding.layoutSahOtherLocalLoad.localLoadOthers.run {
                            setup()
                            visible()
                        }
                        binding.layoutSahOtherSaldo.layoutSahOtherSettingBalance.visible()
                    }
                }
                showShopScore(uiModel)
            }
        }
    }

    private fun setDotVisibility(shopFollowers: Long) {
        val shouldShowFollowers = shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val dotVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        binding.successShopInfoLayout.dot.visibility = dotVisibility
    }

    private fun showNameAndAvatar() {
        setShopName(userSession?.shopName.orEmpty())
        setShopAvatar(ShopAvatarUiModel(userSession?.shopAvatar.orEmpty()))
    }

    private fun setShopBadge(shopBadgeUiModel: ShopBadgeUiModel) {
        binding.successShopInfoLayout.shopBadges.run {
            ImageHandler.LoadImage(this, shopBadgeUiModel.shopBadgeUrl)
        }
    }

    private fun showShopScore(uiModel: ShopInfoUiModel) {
        with(itemView) {
            if (uiModel.shopScore < 0) {
                binding.successShopInfoLayout.shopScore.text =
                    getString(R.string.seller_menu_shop_score_empty_label)
                binding.successShopInfoLayout.shopScore.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                    )
                )
                binding.successShopInfoLayout.shopScoreMaxLabel.hide()
            } else {
                binding.successShopInfoLayout.shopScore.text = uiModel.shopScore.toString()
                binding.successShopInfoLayout.shopScore.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
                binding.successShopInfoLayout.shopScoreMaxLabel.show()
            }
            binding.successShopInfoLayout.shopScoreLayout.setOnClickListener {
                listener?.onScoreClicked()
            }
            listener?.onScoreImpressed()
        }
    }

    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        val shouldShowFollowers =
            shopTotalFollowersUiModel.shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val followersVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        binding.successShopInfoLayout.shopFollowers.run {
            visibility = followersVisibility
            text = StringBuilder(
                "${shopTotalFollowersUiModel.shopFollowers} ${
                    context.resources.getString(R.string.setting_followers)
                }"
            )
            setOnClickListener {
                shopTotalFollowersUiModel.sendSettingShopInfoClickTracking()
                goToShopFavouriteList()
            }
        }
        binding.successShopInfoLayout.dot.visibility = followersVisibility
    }

    private fun setShopName(shopName: String) {
        binding.successShopInfoLayout.shopName.run {
            text = MethodChecker.fromHtml(shopName)
            setOnClickListener {
                goToShopPage()
                sellerMenuTracker?.sendEventClickShopName()
            }
        }
    }

    private fun setShopAvatar(shopAvatarUiModel: ShopAvatarUiModel) {
        binding.successShopInfoLayout.shopImage.run {
            urlSrc = shopAvatarUiModel.shopAvatarUrl
            sendSettingShopInfoImpressionTracking(
                shopAvatarUiModel,
                trackingListener::sendImpressionDataIris
            )
            setOnClickListener {
                goToShopPage()
                sellerMenuTracker?.sendEventClickShopPicture()
            }
        }
    }

    private fun setSaldoBalance(saldoBalanceUiModel: BalanceUiModel) {
        binding.layoutSahOtherSaldo.run {
            balanceTitle.text = context.resources.getString(R.string.setting_balance)
            balanceValue.text = saldoBalanceUiModel.balanceValue
            sendSettingShopInfoImpressionTracking(
                saldoBalanceUiModel,
                trackingListener::sendImpressionDataIris
            )
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
                    setRegularMerchantShopStatus(
                        shopType,
                        shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel
                    )
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM)
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }
            is PowerMerchantStatus -> {
                itemView?.apply {
                    setPowerMerchantShopStatus(shopType, shopStatusUiModel)
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }
            is ShopType.OfficialStore -> {
                itemView?.apply {
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }

            is PowerMerchantProStatus.Advanced -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }

            }
            is PowerMerchantProStatus.Expert -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }
            is PowerMerchantProStatus.Ultimate -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }
            is PowerMerchantProStatus.InActive -> {
                itemView?.apply {
                    setPowerMerchantProStatus(shopStatusUiModel, shopType)
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe(TAB_PM_PRO)
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }
            else -> null
        }

        val paddingTop =
            itemView?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val paddingBottom =
            itemView?.resources?.getDimensionPixelSize(R.dimen.setting_status_padding_bottom)
        if (paddingTop != null && paddingBottom != null) {
            itemView.setPadding(0, paddingTop, 0, paddingBottom)
        }

        shopStatusLayout?.let { view ->
            binding.shopStatus.run {
                removeAllViews()
                addView(view)
            }
        }
    }

    private fun goToPowerMerchantSubscribe(tab: String, isUpdate: Boolean = false) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTabBuilder =
            Uri.parse(appLink).buildUpon().appendQueryParameter(TAB_PM_PARAM, tab)
        if (isUpdate) {
            appLinkPMTabBuilder.appendQueryParameter(
                ApplinkConstInternalMarketplace.ARGS_IS_UPGRADE,
                isUpdate.toString()
            )
        }
        val appLinkPMTab = appLinkPMTabBuilder.build().toString()
        context?.let { RouteManager.route(context, appLinkPMTab) }
    }

    private fun View.setRegularMerchantShopStatus(
        regularMerchant: RegularMerchant,
        userShopInfoUiModel: UserShopInfoWrapper.UserShopInfoUiModel?
    ): View {
        val regularMerchantStatus = findViewById<Typography>(R.id.regularMerchantStatus)
        val eligiblePmIconView = findViewById<IconUnify>(R.id.iconEligiblePm)

        val pmProEligibleIcon = userShopInfoUiModel?.getPowerMerchantProEligibleIcon()
        val pmEligibleIcon = userShopInfoUiModel?.getPowerMerchantEligibleIcon()

        when {
            pmProEligibleIcon != null -> {
                when (regularMerchant) {
                    is RegularMerchant.Verified -> setRegularMerchantVerification(
                        regularMerchantStatus,
                        eligiblePmIconView,
                        pmProEligibleIcon
                    )
                    is RegularMerchant.Pending -> setRegularMerchantPending(
                        regularMerchantStatus,
                        eligiblePmIconView,
                        pmProEligibleIcon
                    )
                    is RegularMerchant.NeedUpgrade -> setRegularMerchantNeedUpgrade(
                        regularMerchantStatus,
                        eligiblePmIconView
                    )
                }
            }
            pmEligibleIcon != null -> {
                when (regularMerchant) {
                    is RegularMerchant.Verified -> setRegularMerchantVerification(
                        regularMerchantStatus,
                        eligiblePmIconView,
                        pmEligibleIcon
                    )
                    is RegularMerchant.Pending -> setRegularMerchantPending(
                        regularMerchantStatus,
                        eligiblePmIconView,
                        pmEligibleIcon
                    )
                    is RegularMerchant.NeedUpgrade -> setRegularMerchantNeedUpgrade(
                        regularMerchantStatus,
                        eligiblePmIconView
                    )
                }
            }
            else -> setRegularMerchantNeedUpgrade(regularMerchantStatus, eligiblePmIconView)
        }

        setupTransactionSection(userShopInfoUiModel)
        return this
    }

    private fun setRegularMerchantNeedUpgrade(
        regularMerchantStatus: Typography,
        eligiblePmIcon: IconUnify
    ) {
        eligiblePmIcon.hide()
        regularMerchantStatus.run {
            text = context.resources.getString(R.string.setting_upgrade)
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            )
        }
    }

    private fun setRegularMerchantPending(
        regularMerchantStatus: Typography,
        eligiblePmIconView: IconUnify,
        pmIcon: Int
    ) {
        eligiblePmIconView.run {
            show()
            setImage(pmIcon)
        }
        regularMerchantStatus.run {
            text = context.resources.getString(R.string.setting_verified)
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                )
            )
            isClickable = false
        }
    }

    private fun setRegularMerchantVerification(
        regularMerchantStatus: Typography,
        eligiblePmIconView: IconUnify,
        pmIcon: Int
    ) {
        eligiblePmIconView.run {
            show()
            setImage(pmIcon)
        }
        regularMerchantStatus.run {
            text = context.resources.getString(R.string.setting_verifikasi)
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            )
        }
    }

    private fun View.setupTransactionSection(userShopInfoUiModel: UserShopInfoWrapper.UserShopInfoUiModel?) {
        val txStatsRM = findViewById<Typography>(R.id.tx_stats_rm)
        val txTotalStatsRM = findViewById<Typography>(R.id.tx_total_stats_rm)
        val totalTransaction = userShopInfoUiModel?.totalTransaction ?: 0
        if (totalTransaction >= Constant.ShopStatus.THRESHOLD_TRANSACTION) {
            hideTransactionSection()
        } else {
            if (userShopInfoUiModel?.periodTypePmPro == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                showTransactionSection()
                if (totalTransaction > Constant.ShopStatus.MAX_TRANSACTION) {
                    txStatsRM.text =
                        MethodChecker.fromHtml(getString(R.string.transaction_passed))
                    txTotalStatsRM.hide()
                } else {
                    txStatsRM.setupStatsWordingRM(userShopInfoUiModel)
                    txTotalStatsRM.show()
                    txTotalStatsRM.text =
                        getString(R.string.total_transaction, totalTransaction.toString())
                }
            } else {
                hideTransactionSection()
            }
        }
    }

    private fun Typography.setupStatsWordingRM(userShopInfo: UserShopInfoWrapper.UserShopInfoUiModel) {
        text = if (userShopInfo.dateCreated.isBlank()) {
            context?.getString(R.string.transaction_on_date)
        } else {
            if (userShopInfo.isBeforeOnDate) {
                context?.getString(R.string.transaction_on_date)
            } else {
                context?.getString(R.string.transaction_since_joining)
            }
        }
    }

    private fun View.hideTransactionSection() {
        findViewById<View>(R.id.divider_stats_rm)?.hide()
        findViewById<Typography>(R.id.tx_stats_rm)?.hide()
        findViewById<Typography>(R.id.tx_total_stats_rm)?.hide()
        findViewById<View>(R.id.view_rm_transaction_cta)?.hide()
    }

    private fun View.showTransactionSection() {
        findViewById<View>(R.id.divider_stats_rm)?.show()
        findViewById<View>(R.id.divider_stats_rm)?.setBackgroundResource(R.drawable.ic_divider_stats_rm)
        findViewById<Typography>(R.id.tx_stats_rm)?.show()
        findViewById<Typography>(R.id.tx_total_stats_rm)?.show()
        findViewById<View>(R.id.view_rm_transaction_cta)?.run {
            show()
            setOnClickListener {
                context?.let {
                    RouteManager.route(it, SellerBaseUrl.getNewMembershipSchemeApplink())
                }
            }
        }
    }

    private fun View.setPowerMerchantShopStatus(
        powerMerchantStatus: PowerMerchantStatus,
        statusUiModel: ShopStatusUiModel
    ): View {
        val upgradePMTextView: Typography = findViewById(R.id.upgradePMText)
        val powerMerchantStatusTextView: Typography = findViewById(R.id.powerMerchantStatusText)
        val powerMerchantText: Typography = findViewById(R.id.powerMerchantText)
        val periodType = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.periodTypePmPro
        val isNewSeller = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.isNewSeller
        when (powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                if (periodType == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                    with(upgradePMTextView) {
                        val shouldShow = isNewSeller == false
                        showWithCondition(shouldShow)
                        if (shouldShow) {
                            setOnClickListener {
                                goToPowerMerchantSubscribe(TAB_PM_PRO, true)
                            }
                        }
                    }
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

    private fun View.setPowerMerchantProStatus(
        shopStatusUiModel: ShopStatusUiModel,
        powerMerchantStatus: PowerMerchantProStatus
    ): View {
        val goldOS = shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel
        val ivBgPMPro = findViewById<ShapeableImageView>(R.id.iv_bg_pm_pro)
        val powerMerchantProStatusText =
            findViewById<Typography>(R.id.powerMerchantProStatusText)
        val powerMerchantProIcon = findViewById<IconUnify>(R.id.powerMerchantProIcon)

        when (powerMerchantStatus) {
            is PowerMerchantProStatus.Advanced -> {
                ivBgPMPro.loadImage(PMProURL.BG_ADVANCE)
                powerMerchantProStatusText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        GREY_TEXT_COLOR
                    )
                )
                powerMerchantProStatusText.text =
                    goldOS?.pmProGradeName?.capitalize(Locale.getDefault())
                        ?: ""
            }
            is PowerMerchantProStatus.Expert -> {
                ivBgPMPro.loadImage(PMProURL.BG_EXPERT)
                powerMerchantProStatusText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        TEAL_TEXT_COLOR
                    )
                )
                powerMerchantProStatusText.text =
                    goldOS?.pmProGradeName?.capitalize(Locale.getDefault())
                        ?: ""
            }
            is PowerMerchantProStatus.Ultimate -> {
                ivBgPMPro.loadImage(PMProURL.BG_ULTIMATE)
                powerMerchantProStatusText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        YELLOW_TEXT_COLOR
                    )
                )
                powerMerchantProStatusText.text =
                    goldOS?.pmProGradeName?.capitalize(Locale.getDefault())
                        ?: ""
            }
            is PowerMerchantProStatus.InActive -> {
                powerMerchantProStatusText.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R600
                    )
                )
                powerMerchantProStatusText.text = getString(R.string.setting_not_active)
            }
        }

        ivBgPMPro.shapeAppearanceModel = ivBgPMPro.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, Constant.ShopStatus.ROUNDED_RADIUS)
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
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST)
        intent.putExtra(EXTRA_SHOP_ID, userSession?.shopId)
        context.startActivity(intent)
    }

    private fun goToShopPage() {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.SHOP_PAGE,
            userSession?.shopId
        )
    }

    interface ShopInfoListener {
        fun onScoreClicked()
        fun onScoreImpressed()
        fun onSaldoClicked()
        fun onRefreshShopInfo()
    }
}