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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.RegularMerchant
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.*
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.layout_seller_menu_shop_info.view.*
import kotlinx.android.synthetic.main.layout_seller_menu_shop_info_success.view.*
import kotlinx.android.synthetic.main.setting_balance.view.*
import kotlinx.android.synthetic.main.setting_partial_others_local_load.view.*
import kotlinx.android.synthetic.main.setting_shop_status_pm.view.*
import kotlinx.android.synthetic.main.setting_shop_status_regular.view.*

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

        private val GREEN_TIP = R.drawable.setting_tip_bar_enabled
        private val GREEN_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_G500
        private val GREY_TIP = R.drawable.setting_tip_bar_disabled
        private val GREY_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        private val RED_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_R500
        private val GREY_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant_inactive
        private val GREEN_POWER_MERCHANT_ICON = R.drawable.ic_power_merchant

        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
    }

    private val context by lazy { itemView.context }

    private val shopScoreImpressHolder = ImpressHolder()

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
            shopScoreLayout.addOnImpressionListener(shopScoreImpressHolder) {
                listener?.onScoreImpressed()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun setShopTotalFollowers(shopTotalFollowersUiModel: ShopFollowersUiModel) {
        val shouldShowFollowers = shopTotalFollowersUiModel.shopFollowers != Constant.INVALID_NUMBER_OF_FOLLOWERS
        val followersVisibility = if (shouldShowFollowers) View.VISIBLE else View.GONE
        itemView.successShopInfoLayout.shopFollowers?.run {
            visibility = followersVisibility
            text = "${shopTotalFollowersUiModel.shopFollowers} ${context.resources.getString(R.string.setting_followers)}"
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
        val shopType = shopStatusUiModel.shopType
        val itemView: View = LayoutInflater.from(context).inflate(shopType.shopTypeLayoutRes, null, false)
        val shopStatusLayout: View? = when (shopType) {
            is RegularMerchant -> {
                itemView.apply {
                    setRegularMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    rightRectangle.setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }
            }
            is PowerMerchantStatus -> {
                itemView.apply {
                    setPowerMerchantShopStatus(shopType)
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                    setOnClickListener {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                        sellerMenuTracker?.sendEventClickShopType()
                    }
                }
            }
            is ShopType.OfficialStore -> {
                itemView.apply {
                    sendSettingShopInfoImpressionTracking(shopStatusUiModel, trackingListener::sendImpressionDataIris)
                }
            }
        }

        val paddingTop = itemView.resources.getDimensionPixelSize(R.dimen.spacing_lvl3)
        val paddingBottom = itemView.resources.getDimensionPixelSize(R.dimen.setting_status_padding_bottom)
        itemView.setPadding(0, paddingTop, 0, paddingBottom)

        shopStatusLayout?.let { view ->
            (this.itemView.shopStatus as LinearLayout).run {
                removeAllViews()
                addView(view)
            }
        }
    }

    private fun View.setRegularMerchantShopStatus(regularMerchant: RegularMerchant): View {
        regularMerchantStatus.run {
            text = when (regularMerchant) {
                is RegularMerchant.NeedUpgrade -> context.resources.getString(R.string.setting_upgrade)
                is RegularMerchant.NeedVerification -> context.resources.getString(R.string.setting_verifikasi)
            }
        }

        return this
    }

    private fun View.setPowerMerchantShopStatus(powerMerchantStatus: PowerMerchantStatus): View {
        var statusText = context.resources.getString(R.string.setting_on_verification)
        var textColor = GREY_TEXT_COLOR
        var statusDrawable = GREY_TIP
        var powerMerchantDrawableIcon = GREY_POWER_MERCHANT_ICON
        when (powerMerchantStatus) {
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