package com.tokopedia.seller.menu.presentation.adapter.viewholder

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.gm.common.constant.PMProURL
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholderAndError
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.BalanceUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopAvatarUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopBadgeUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopFollowersUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopStatusUiModel
import com.tokopedia.seller.menu.databinding.LayoutSellerMenuShopInfoBinding
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.WebViewHelper
import java.util.*
import com.tokopedia.seller.menu.common.R as sellermenucommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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

        private val GREY_TEXT_COLOR = unifyprinciplesR.color.Unify_NN950_68

        private val TEAL_TEXT_COLOR = unifyprinciplesR.color.Unify_TN500
        private val YELLOW_TEXT_COLOR = unifyprinciplesR.color.Unify_YN400

        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        private const val TICKER_TYPE_WARNING = "warning"
        private const val TICKER_TYPE_DANGER = "danger"
        private const val STATUS_INCUBATE_OS = 6

        private const val ALLOW_OVERRIDE_URL_FORMAT = "%s?allow_override=%b&url=%s"
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
                showTicker(
                    uiModel.shopInfo.shopStatusUiModel
                        ?.userShopInfoWrapper?.userShopInfoUiModel?.statusInfoUiModel
                )
            }
        }
    }

    private fun showTicker(statusInfoUiModel: UserShopInfoWrapper.UserShopInfoUiModel.StatusInfoUiModel?) {
        binding.successTickerShopInfoLayout.run {
            val title = statusInfoUiModel?.statusTitle.orEmpty()
            val message = statusInfoUiModel?.statusMessage.orEmpty()

            if (statusInfoUiModel?.shopStatus.orZero() == STATUS_INCUBATE_OS){
                if (title.isNotEmpty() && message.isNotEmpty()) {
                    tickerShopInfo.tickerTitle = title.parseAsHtml().toString()
                    tickerShopInfo.setHtmlDescription(message)
                    val tickerType: Int = when (statusInfoUiModel?.tickerType) {
                        TICKER_TYPE_DANGER -> Ticker.TYPE_ERROR
                        TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
                        else -> Ticker.TYPE_ANNOUNCEMENT
                    }
                    tickerShopInfo.tickerType = tickerType
                    tickerShopInfo.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            goToSlaWebView(linkUrl)
                        }

                        override fun onDismiss() {
                        }

                    })
                    tickerShopInfo.show()
                } else {
                    tickerShopInfo.hide()
                }
            }else{
                tickerShopInfo.hide()
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
        binding.successShopInfoLayout.shopBadges.loadImageWithoutPlaceholderAndError(shopBadgeUiModel.shopBadgeUrl)
    }

    private fun showShopScore(uiModel: ShopInfoUiModel) {
        with(itemView) {
            if (uiModel.shopScore < 0) {
                binding.successShopInfoLayout.shopScore.text =
                    getString(R.string.seller_menu_shop_score_empty_label)
                binding.successShopInfoLayout.shopScore.setTextColor(
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_NN950_96
                    )
                )
                binding.successShopInfoLayout.shopScoreMaxLabel.hide()
            } else {
                binding.successShopInfoLayout.shopScore.text = uiModel.shopScore.toString()
                binding.successShopInfoLayout.shopScore.setTextColor(
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_GN500
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
            balanceTitle.text =
                context.resources.getString(sellermenucommonR.string.setting_balance)
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
        val itemView: View? = shopType?.getLayoutRes()?.let {
            LayoutInflater.from(context).inflate(it, null, false)
        }
        val shopStatusLayout: View? = when (shopType) {
            is PowerMerchantStatus.Active -> {
                itemView?.apply {
                    setPowerMerchantShopStatus(shopStatusUiModel)
                    setupTransactionSection(shopStatusUiModel.userShopInfoWrapper.userShopInfoUiModel)
                    sendSettingShopInfoImpressionTracking(
                        shopStatusUiModel,
                        trackingListener::sendImpressionDataIris
                    )
                    setOnClickListener {
                        goToPowerMerchantSubscribe()
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
                        goToPowerMerchantSubscribe()
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
                        goToPowerMerchantSubscribe()
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
                        goToPowerMerchantSubscribe()
                        sellerMenuTracker?.sendEventClickShopSettingNew()
                    }
                }
            }
            else -> null
        }

        val paddingTop =
            itemView?.resources?.getDimensionPixelSize(unifyprinciplesR.dimen.spacing_lvl3)
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

    private fun goToPowerMerchantSubscribe(isUpdate: Boolean = false) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTabBuilder = Uri.parse(appLink).buildUpon()
        if (isUpdate) {
            appLinkPMTabBuilder.appendQueryParameter(
                ApplinkConstInternalMarketplace.ARGS_IS_UPGRADE,
                isUpdate.toString()
            )
        }
        context?.let { RouteManager.route(context, appLink) }
    }

    private fun View.setPowerMerchantShopStatus(
        statusUiModel: ShopStatusUiModel
    ): View {
        val upgradePMTextView: Typography = findViewById(R.id.upgradePMText)
        val powerMerchantText: Typography = findViewById(R.id.powerMerchantText)
        val isKyc = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.isKyc.orFalse()
        if (isKyc) {
            val periodType = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.periodTypePmPro
            val isNewSeller = statusUiModel.userShopInfoWrapper.userShopInfoUiModel?.isNewSeller
            if (periodType == Constant.D_DAY_PERIOD_TYPE_PM_PRO) {
                with(upgradePMTextView) {
                    val shouldShow = isNewSeller == false
                    showWithCondition(shouldShow)
                    if (shouldShow) {
                        setOnClickListener {
                            goToPowerMerchantSubscribe(true)
                        }
                    }
                }
            } else if (periodType == Constant.COMMUNICATION_PERIOD_PM_PRO) {
                upgradePMTextView.hide()
            }
        } else {
            with(upgradePMTextView) {
                text = getString(sellermenucommonR.string.setting_other_not_verified)
                visible()
                setOnClickListener {
                    goToPowerMerchantSubscribe(false)
                }
            }
        }
        powerMerchantText.text =
            getString(sellermenucommonR.string.power_merchant_upgrade)
        return this
    }

    private fun View.setupTransactionSection(userShopInfoUiModel: UserShopInfoWrapper.UserShopInfoUiModel?) {
        val txTotalStatsRM = findViewById<Typography>(R.id.tx_total_stats_rm)
        val totalTransaction = userShopInfoUiModel?.totalTransaction ?: 0
        if (totalTransaction > Constant.ShopStatus.MAX_TRANSACTION_VISIBLE) {
            hideTransactionSection()
        } else {
            showTransactionSection(totalTransaction)
            if (totalTransaction > Constant.ShopStatus.MAX_TRANSACTION) {
                txTotalStatsRM.hide()
            } else {
                txTotalStatsRM.show()
                txTotalStatsRM.text =
                    getString(R.string.total_transaction, totalTransaction.toString())
            }
        }
    }

    private fun View.hideTransactionSection() {
        findViewById<View>(R.id.divider_stats_rm)?.hide()
        findViewById<Typography>(R.id.tx_stats_rm)?.hide()
        findViewById<Typography>(R.id.tx_total_stats_rm)?.hide()
        findViewById<View>(R.id.view_rm_transaction_cta)?.hide()
    }

    private fun View.showTransactionSection(totalTransaction: Long) {
        findViewById<View>(R.id.divider_stats_rm)?.show()
        findViewById<View>(R.id.divider_stats_rm)?.setBackgroundResource(R.drawable.ic_divider_stats_rm)
        findViewById<Typography>(R.id.tx_stats_rm)?.show()
        findViewById<Typography>(R.id.tx_total_stats_rm)?.show()
        findViewById<View>(R.id.view_rm_transaction_cta)?.run {
            show()
            setOnClickListener {
                listener?.onRMTransactionClicked(totalTransaction)
            }
        }
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
        }

        ivBgPMPro.shapeAppearanceModel = ivBgPMPro.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, Constant.ShopStatus.ROUNDED_RADIUS)
            .build()
        powerMerchantProIcon.loadImage(if (goldOS?.badge?.isBlank() == true) PMProURL.ICON_URL else goldOS?.badge.orEmpty())
        return this
    }

    private fun LocalLoad.setup() {
        title?.text = context.resources.getString(R.string.setting_error_message)
        description?.text = context.resources.getString(R.string.setting_error_description)
        refreshBtn?.setOnClickListener {
            listener?.onRefreshShopInfo()
        }
    }

    private fun ShopType?.getLayoutRes(): Int {
        return when (this) {
            is RegularMerchant -> R.layout.setting_shop_status_regular
            is PowerMerchantStatus -> R.layout.setting_shop_status_pm
            is PowerMerchantProStatus -> R.layout.setting_shop_status_pm_pro
            else -> R.layout.setting_shop_status_os
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

    private fun goToSlaWebView(linkUrl: CharSequence) {
        val link = linkUrl.toString()
        if (WebViewHelper.isUrlValid(linkUrl.toString())) {
            RouteManager.route(
                context,
                String.format(
                    Locale.getDefault(),
                    ALLOW_OVERRIDE_URL_FORMAT,
                    ApplinkConst.WEBVIEW,
                    false,
                    link
                )
            )
        } else {
            RouteManager.route(context, link)
        }
    }

    interface ShopInfoListener {
        fun onScoreClicked()
        fun onScoreImpressed()
        fun onSaldoClicked()
        fun onRefreshShopInfo()
        fun onRMTransactionClicked(totalTransaction: Long)
    }
}
