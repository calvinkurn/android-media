package com.tokopedia.shop.page.view.holder

import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.util.TextApiUtils
import com.tokopedia.shop.extension.formatToSimpleNumber
import kotlinx.android.synthetic.main.partial_shop_page_header_2.view.*

class ShopPageHeaderViewHolder(private val view: View, private val listener: ShopPageHeaderListener){

    fun bind(shopInfo: ShopInfo, isMyShop: Boolean) {
        view.shopName.text = MethodChecker.fromHtml(shopInfo.info.shopName).toString()
        view.shopFollower.text = view.context.getString(R.string.shop_page_header_total_follower,
                shopInfo.info.shopTotalFavorit.toString())
        view.shopFollower.setOnClickListener { listener.onFollowerTextClicked() }
        ImageHandler.loadImageCircle2(view.context, view.shopImageView, shopInfo.info.shopAvatar)
        when {
            TextApiUtils.isValueTrue(shopInfo.info.getShopIsOfficial()) -> displayOfficial()
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
        view.buttonManageShop.visibility = if (isMyShop) View.VISIBLE else View.GONE
        when (shopInfo.info.shopStatus){
            ShopStatusDef.CLOSED -> showShopClosed(shopInfo)
            ShopStatusDef.MODERATED -> showShopModerated(shopInfo,false)
            ShopStatusDef.MODERATED_PERMANENTLY -> showShopModerated(shopInfo,true)
            ShopStatusDef.NOT_ACTIVE -> showShopNotActive(isMyShop)
            else -> {
                view.shopWarningTickerView.visibility = View.GONE
                view.buttonManageShop.visibility = View.GONE
            }
        }
    }

    private fun showShopNotActive(isMyShop: Boolean) {
        val description = if (isMyShop) {
            view.context.getString(R.string.shop_page_header_shop_not_active_description_seller)
        } else {
            view.context.getString(R.string.shop_page_header_shop_not_active_description_buyer)
        }
        showShopStatusTicker(R.drawable.ic_info_inactive,
                view.context.getString(R.string.shop_page_header_shop_not_active_title),
                description,
                R.color.yellow_ticker)
        view.buttonManageShop.apply {
            text = view.context.getString(R.string.shop_info_label_see_how_to_open)
            setOnClickListener { listener.goToHowActivate() }
        }
    }

    private fun showShopModerated(shopInfo: ShopInfo, isPermanent: Boolean) {
        showShopStatusTicker(R.drawable.ic_info_moderation,
                view.context.getString(if (isPermanent) {
                    R.string.shop_page_header_shop_in_permanent_moderation
                } else {R.string.shop_page_header_shop_in_moderation} ),
                view.context.getString(R.string.shop_page_header_closed_reason, shopInfo.closedInfo.reason),
                R.color.yellow_ticker)
        view.buttonManageShop.apply {
            text = view.context.getString(R.string.shop_info_label_open_request)
            setOnClickListener { listener.requestOpenShop() }
        }
    }

    private fun showShopClosed(shopInfo: ShopInfo) {
        val shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_DD_MM_YYYY,
                DateFormatUtils.FORMAT_D_MMMM_YYYY, shopInfo.closedInfo.until)
        showShopStatusTicker(R.drawable.ic_shop_label_closed,
                view.context.getString(R.string.shop_page_header_shop_closed_info, shopCloseUntilString),
                shopInfo.closedInfo.note, R.color.green_ticker)
        view.buttonManageShop.apply {
            text = view.context.getString(R.string.shop_info_label_open_action)
            setOnClickListener { listener.openShop() }
        }

    }

    private fun showShopStatusTicker(@DrawableRes iconRes: Int, title: String, description: String, backgroundColor: Int) {
        view.shopWarningTickerView.run {
            setIcon(iconRes)
            setTitle(title)
            setDescription(description)
            setTickerColor(ContextCompat.getColor(context, backgroundColor))
            setAction(null, null)
            visibility = View.VISIBLE
        }
    }

    private fun displayAsBuyer(shopInfo: ShopInfo) {
        view.chatButtonText.text = view.context.getString(R.string.shop_page_chat_label)
        view.chatButton.setOnClickListener { listener.goToChatSeller() }
        updateFavoriteButton(shopInfo)
    }

    private fun updateFavoriteButton(shopInfo: ShopInfo) {
        if (TextApiUtils.isValueTrue(shopInfo.getInfo().getShopAlreadyFavorited())){
            view.favButtonText.setText(view.context.getString(R.string.shop_info_label_favourite))
        } else {
            view.favButtonText.setText(view.context.getString(R.string.shop_page_label_favorite))
        }
        view.favButton.setOnClickListener { listener.toggleFavorite() }
    }

    private fun displayAsSeller(shopInfo: ShopInfo) {
        view.chatButtonText.text = view.context.getString(R.string.shop_page_label_manage_shop)
        view.chatButton.setOnClickListener { listener.goToManageShop() }
        view.favButtonText.text = view.context.getString(R.string.shop_page_label_add_product)
        view.favButton.setOnClickListener { listener.goToAddProduct() }
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

    interface ShopPageHeaderListener {
        fun onFollowerTextClicked()
        fun goToChatSeller()
        fun goToManageShop()
        fun toggleFavorite()
        fun goToAddProduct()
        fun openShop()
        fun requestOpenShop()
        fun goToHowActivate()
    }

}