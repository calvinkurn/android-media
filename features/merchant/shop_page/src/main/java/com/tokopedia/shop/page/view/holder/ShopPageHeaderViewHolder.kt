package com.tokopedia.shop.page.view.holder

import android.graphics.drawable.GradientDrawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.extension.formatToSimpleNumber
import kotlinx.android.synthetic.main.partial_shop_page_header.view.*

class ShopPageHeaderViewHolder(private val view: View, private val listener: ShopPageHeaderListener){
    private var isShopFavourited = false

    fun bind(shopInfo: ShopInfo, isMyShop: Boolean) {
        isShopFavourited = TextApiUtils.isValueTrue(shopInfo.getInfo().getShopAlreadyFavorited())
        view.shopName.text = MethodChecker.fromHtml(shopInfo.info.shopName).toString()
        if (shopInfo.info.shopTotalFavorit > 1) {
            view.shopFollower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_followers,
                    shopInfo.info.shopTotalFavorit.toDouble().formatToSimpleNumber()))
        } else { // if 0 or 1, only print as follower (without s)
            view.shopFollower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_follower,
                    shopInfo.info.shopTotalFavorit.toDouble().formatToSimpleNumber()))
        }
        view.shopFollower.setOnClickListener { listener.onFollowerTextClicked() }
        ImageHandler.loadImageCircle2(view.context, view.shopImageView, shopInfo.info.shopAvatar)
        when {
            TextApiUtils.isValueTrue(shopInfo.info.shopIsOfficial) -> displayOfficial()
            shopInfo.info.isShopIsGoldBadge -> {
                displayGoldenShop()
                displayGeneral(shopInfo)
            }
            else -> {
                view.shopLabelIcon.visibility = View.GONE
                displayGeneral(shopInfo)
            }
        }

        if (isMyShop){
            displayAsSeller(shopInfo)
        } else {
            displayAsBuyer(shopInfo)
        }

        updateViewShopStatus(shopInfo, isMyShop)
    }

    private fun updateViewShopStatus(shopInfo: ShopInfo, isMyShop: Boolean) {
        view.buttonActionAbnormal.visibility = if (isMyShop) View.VISIBLE else View.GONE
        when (shopInfo.info.shopStatus){
            ShopStatusDef.CLOSED -> showShopClosed(shopInfo)
            ShopStatusDef.MODERATED -> showShopModerated(isMyShop,false)
            ShopStatusDef.MODERATED_PERMANENTLY -> showShopModerated(isMyShop,true)
            ShopStatusDef.NOT_ACTIVE -> showShopNotActive(isMyShop)
            else -> {
                view.shopWarningTickerView.visibility = View.GONE
                view.buttonActionAbnormal.visibility = View.GONE
            }
        }
    }

    private fun showShopNotActive(isMyShop: Boolean) {
        var title = view.context.getString(R.string.shop_page_header_shop_not_active_title)
        var description = view.context.getString(R.string.shop_page_header_shop_not_active_description_seller)
        if (!isMyShop) {
            title = view.context.getString(R.string.shop_page_header_shop_not_active_title_buyer)
            description = view.context.getString(R.string.shop_page_header_shop_not_active_description_buyer)
        }
        showShopStatusTicker(R.drawable.ic_shop_deactivate,
                title, description, R.color.yellow_ticker, R.color.grey_overlay_inactive, isMyShop)

        view.buttonActionAbnormal.apply {
            text = view.context.getString(R.string.shop_info_label_see_how_to_open)
            setOnClickListener { listener.goToHowActivate() }
        }
    }

    private fun showShopModerated(isMyShop: Boolean, isPermanent: Boolean) {
        var title = if (isPermanent) { R.string.shop_page_header_shop_in_permanent_moderation }
        else {R.string.shop_page_header_shop_in_moderation}
        var description = view.context.getString(R.string.shop_page_header_shop_in_moderation_desc)

        if (!isMyShop) {
            title = if (isPermanent) { R.string.shop_page_header_shop_in_permanent_moderation_buyer }
            else {R.string.shop_page_header_shop_in_moderation_buyer}
            description = view.context.getString(R.string.shop_page_header_shop_in_moderation_desc_buyer)
        }

        showShopStatusTicker(R.drawable.ic_shop_moderated_v3,
                view.context.getString(title), description,
                R.color.yellow_ticker, R.color.red_overlay_moderated, isMyShop)

        view.buttonActionAbnormal.apply {
            text = view.context.getString(R.string.shop_info_label_open_request)
            setOnClickListener { listener.requestOpenShop() }
        }
    }

    private fun showShopClosed(shopInfo: ShopInfo) {
        val shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_DD_MM_YYYY,
                DateFormatUtils.FORMAT_D_MMMM_YYYY, shopInfo.closedInfo.until)
        showShopStatusTicker(R.drawable.ic_shop_close_v3,
                view.context.getString(R.string.shop_page_header_shop_closed_info, shopCloseUntilString),
                shopInfo.closedInfo.note, R.color.green_ticker, R.color.green_overlay_closed)
        view.buttonActionAbnormal.apply {
            text = view.context.getString(R.string.shop_info_label_open_action)
            setOnClickListener { listener.openShop() }
        }

    }

    private fun showShopStatusTicker(@DrawableRes iconRes: Int, title: String, description: String,
                                     backgroundTickerColor: Int, backgoundStatusColor: Int = backgroundTickerColor,
                                     isMyShop: Boolean = false) {
        view.shopWarningTickerView.run {
            setTitle(title)
            setDescription(description)
            setTickerColor(ContextCompat.getColor(context, backgroundTickerColor))
            setAction(if (isMyShop){
                object: ClickableSpan() {
                    override fun updateDrawState(ds: TextPaint?) {
                        super.updateDrawState(ds)
                        ds?.isUnderlineText = false
                    }

                    override fun onClick(p0: View?) {
                        listener.goToHelpCenter(ShopUrl.SHOP_HELP_CENTER)
                    }
                }
            } else null)
            visibility = View.VISIBLE
        }

        view.shopStatusImageView.background = GradientDrawable().apply {
            setColor(ContextCompat.getColor(view.context, backgoundStatusColor))
            shape = GradientDrawable.OVAL
            setStroke(view.context.resources.getDimension(R.dimen.dp_half).toInt(),
                    ContextCompat.getColor(view.context, R.color.grey_status_stroke))
        }

        view.shopStatusImageView.setImageDrawable(AppCompatResources.getDrawable(view.context, iconRes))
    }

    private fun displayAsBuyer(shopInfo: ShopInfo) {
        view.buttonManageShop.visibility = View.GONE
        view.buttonChat.visibility = View.VISIBLE
        view.buttonChat.setOnClickListener { listener.goToChatSeller() }
        updateFavoriteButton(shopInfo)
    }

    fun isShopFavourited() = isShopFavourited

    fun updateFavoriteButton(shopInfo: ShopInfo?) {
        view.buttonFollow.isEnabled = true
        view.buttonFollowed.isEnabled = true
        if (isShopFavourited){
            view.buttonFollow.visibility = View.GONE
            view.buttonFollowed.visibility = View.VISIBLE
            view.buttonFollowed.setOnClickListener{
                view.buttonFollowed.isEnabled = false
                listener.toggleFavorite(true)}
            shopInfo?.info?.isShopOfficial?.let { (this as ShopModuleRouter).sendMoEngageFavoriteEvent(shopInfo?.info?.shopName, shopInfo?.info?.shopId, shopInfo?.info?.shopLocation, it, true) }
        } else {
            view.buttonFollowed.visibility = View.GONE
            view.buttonFollow.visibility = View.VISIBLE
            view.buttonFollow.setText(view.context.getString(R.string.shop_page_label_follow))
            view.buttonFollow.setOnClickListener{
                view.buttonFollow.isEnabled = false
                listener.toggleFavorite(false)}
            shopInfo?.info?.isShopOfficial?.let { (this as ShopModuleRouter).sendMoEngageFavoriteEvent(shopInfo?.info?.shopName, shopInfo?.info?.shopId, shopInfo?.info?.shopLocation, it, false) }
        }
    }

    private fun displayAsSeller(shopInfo: ShopInfo) {
        view.buttonChat.visibility = View.GONE
        view.buttonManageShop.visibility = View.VISIBLE
        view.buttonManageShop.setOnClickListener { listener.goToManageShop() }
        view.buttonFollowed.visibility = View.GONE
        view.buttonFollow.visibility = View.VISIBLE
        view.buttonFollow.text = view.context.getString(R.string.shop_page_label_add_product)
        view.buttonFollow.setOnClickListener { listener.goToAddProduct() }
    }

    private fun displayGeneral(shopInfo: ShopInfo) {
        view.shopLabel.visibility = View.GONE
        view.shopReputationView.visibility = View.VISIBLE

        val reputaionMedalType = shopInfo.stats.shopBadgeLevel.set.toInt()
        val reputationLevel = shopInfo.stats.shopBadgeLevel.level.toInt()
        val reputationScore = shopInfo.stats.shopReputationScore

        view.shopReputationView.setValue(reputaionMedalType, reputationLevel, reputationScore)
    }

    private fun displayGoldenShop() {
        view.shopLabelIcon.visibility = View.VISIBLE
        view.shopLabelIcon.setImageResource(R.drawable.ic_badge_shop_gm)
    }

    private fun displayOfficial() {
        view.shopLabelIcon.visibility = View.VISIBLE
        view.shopLabelIcon.setImageResource(R.drawable.ic_badge_shop_official)
        view.shopLabel.visibility = View.VISIBLE
    }

    fun toggleFavourite() {
        isShopFavourited = !isShopFavourited
    }

    interface ShopPageHeaderListener {
        fun onFollowerTextClicked()
        fun goToChatSeller()
        fun goToManageShop()
        fun toggleFavorite(isFavourite: Boolean)
        fun goToAddProduct()
        fun openShop()
        fun requestOpenShop()
        fun goToHowActivate()
        fun goToHelpCenter(url: String)
    }

}