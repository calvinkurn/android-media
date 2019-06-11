package com.tokopedia.shop.page.view.holder

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.view.ContextThemeWrapper
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.constant.ShopUrl
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.extension.formatToSimpleNumber
import kotlinx.android.synthetic.main.partial_shop_page_header.view.*

class ShopPageHeaderViewHolder(private val view: View, private val listener: ShopPageHeaderListener,
                               private val shopPageTracking: ShopPageTrackingBuyer,
                               private val context: Context){
    private var isShopFavourited = false
    private var isShopRequestedModerate = false

    companion object {
        private const val IS_MODERATED = 1
        private const val MODERATE_OPTION_ONE = 0
        private const val MODERATE_OPTION_TWO = 1
    }

    fun bind(shopInfo: ShopInfo, isMyShop: Boolean) {
        isShopFavourited = TextApiUtils.isValueTrue(shopInfo.favoriteData.alreadyFavorited.toString())
        view.shopName.text = MethodChecker.fromHtml(shopInfo.shopCore.name).toString()
        if (shopInfo.favoriteData.totalFavorite > 1) {
            view.shopFollower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_followers,
                    shopInfo.favoriteData.totalFavorite.toDouble().formatToSimpleNumber()))
        } else { // if 0 or 1, only print as follower (without s)
            view.shopFollower.text = MethodChecker.fromHtml(view.context.getString(R.string.shop_page_header_total_follower,
                    shopInfo.favoriteData.totalFavorite.toDouble().formatToSimpleNumber()))
        }
        view.shopFollower.setOnClickListener { listener.onFollowerTextClicked() }
        ImageHandler.loadImageCircle2(view.context, view.shopImageView, shopInfo.shopAssets.avatar)
        when {
            TextApiUtils.isValueTrue(shopInfo.goldOS.isOfficial.toString()) -> displayOfficial()
            shopInfo.goldOS.isGoldBadge == 1 -> {
                displayGoldenShop()
            }
            else -> {
                view.shopLabelIcon.visibility = View.GONE
            }
        }

        if (isMyShop){
            displayAsSeller()
        } else {
            displayAsBuyer()
        }
    }

    fun updateViewModerateStatus(moderateStatus: Int, shopInfo: ShopInfo, isMyShop: Boolean) {
        isShopRequestedModerate = moderateStatus == IS_MODERATED
        updateViewShopStatus(shopInfo, isMyShop)
    }

    private fun updateViewShopStatus(shopInfo: ShopInfo, isMyShop: Boolean) {
        view.buttonActionAbnormal.visibility = if (isMyShop && !isShopRequestedModerate) View.VISIBLE else View.GONE
        when (shopInfo.statusInfo.shopStatus){
            ShopStatusDef.CLOSED -> showShopClosed(shopInfo)
            ShopStatusDef.MODERATED -> showShopModerated(isMyShop,false,shopInfo)
            ShopStatusDef.MODERATED_PERMANENTLY -> showShopModerated(isMyShop,true,shopInfo)
            ShopStatusDef.NOT_ACTIVE -> showShopNotActive(isMyShop, shopInfo)
            else -> {
                view.buttonActionAbnormal.visibility = View.GONE
                hideShopStatusTicker()
            }
        }
    }

    private fun showShopNotActive(isMyShop: Boolean, shopInfo: ShopInfo) {
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
            setOnClickListener {
                shopPageTracking.clickHowToActivateShop(CustomDimensionShopPage
                        .create(shopInfo.shopCore.shopID, shopInfo.goldOS.isOfficial == 1,
                                shopInfo.goldOS.isGold == 1))
                listener.goToHowActivate()
            }
        }
        shopPageTracking.impressionHowToActivateShop(CustomDimensionShopPage
                .create(shopInfo.shopCore.shopID, shopInfo.goldOS.isOfficial == 1,
                        shopInfo.goldOS.isGold == 1))
    }

    private fun showShopModerated(isMyShop: Boolean, isPermanent: Boolean, shopInfo: ShopInfo) {
        var title = if (isPermanent) {
            R.string.shop_page_header_shop_in_permanent_moderation
        } else {
            R.string.shop_page_header_shop_in_moderation
        }
        var description = view.context.getString(R.string.shop_page_header_shop_in_moderation_desc)
        val shopId = shopInfo.shopCore.shopID.toInt()

        if (!isMyShop) {
            title = if (isPermanent) {
                R.string.shop_page_header_shop_in_permanent_moderation_buyer
            } else {
                R.string.shop_page_header_shop_in_moderation_buyer
            }
            description = view.context.getString(R.string.shop_page_header_shop_in_moderation_desc_buyer)
        }

        showShopStatusTicker(R.drawable.ic_shop_moderated_v3,
                view.context.getString(title), description,
                R.color.yellow_ticker, R.color.red_overlay_moderated, isMyShop)

        view.buttonActionAbnormal.apply {
            text = view.context.getString(R.string.shop_info_label_open_request)
            setOnClickListener {
                initDialog(shopId)
            }
        }
    }

    private fun initDialog(shopId: Int) {
        val moderateOptionOne = context.getString(R.string.moderate_shop_option_1)
        val moderateOptionTwo = context.getString(R.string.moderate_shop_option_2)
        val arrayOption = arrayOf(moderateOptionOne, moderateOptionTwo)
        var moderateNotes = ""
        val customThemeDialog = ContextThemeWrapper(context, R.style.AlertDialogTheme)
        val dialog = AlertDialog.Builder(customThemeDialog)

        dialog.setTitle(context.getString(R.string.moderate_shop_title))
                .setSingleChoiceItems(arrayOption, 0, null)
                .setPositiveButton(R.string.title_ok) { dialog, which ->
                    var selectedModerateOption = (dialog as AlertDialog).listView.checkedItemPosition
                    if (selectedModerateOption == MODERATE_OPTION_ONE) {
                        moderateNotes = moderateOptionOne
                    } else if (selectedModerateOption == MODERATE_OPTION_TWO) {
                        moderateNotes = moderateOptionTwo
                    }
                    if (moderateNotes.isNotEmpty()) {
                        listener.requestOpenShop(shopId, moderateNotes)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.button_cancel) { dialog, which ->
                    dialog.cancel()
                }
        dialog.show()
    }

    private fun showShopClosed(shopInfo: ShopInfo) {
        val shopCloseUntilString = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_DD_MM_YYYY,
                DateFormatUtils.FORMAT_D_MMMM_YYYY, shopInfo.closedInfo.closeUntil)
        showShopStatusTicker(R.drawable.ic_shop_close_v3,
                view.context.getString(R.string.shop_page_header_shop_closed_info, shopCloseUntilString),
                shopInfo.closedInfo.note, R.color.green_ticker, R.color.green_overlay_closed)
        view.buttonActionAbnormal.apply {
            text = view.context.getString(R.string.shop_info_label_open_action)
            setOnClickListener {
                listener.openShop()
                shopPageTracking.clickOpenOperationalShop(CustomDimensionShopPage
                        .create(shopInfo.shopCore.shopID,
                                shopInfo.goldOS.isOfficial == 1,
                                shopInfo.goldOS.isGold == 1))
            }
        }
        shopPageTracking.impressionOpenOperationalShop(CustomDimensionShopPage
                .create(shopInfo.shopCore.shopID,
                shopInfo.goldOS.isOfficial == 1,
                shopInfo.goldOS.isGold == 1))
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

    private fun hideShopStatusTicker(){
        view.shopWarningTickerView.visibility = View.GONE
        view.shopStatusImageView.background = null
        view.shopStatusImageView.setImageDrawable(null)
    }

    private fun displayAsBuyer() {
        view.buttonManageShop.visibility = View.GONE
        view.buttonChat.visibility = View.VISIBLE
        view.buttonChat.setOnClickListener { listener.goToChatSeller() }
        updateFavoriteButton()
    }

    fun isShopFavourited() = isShopFavourited

    fun updateFavoriteButton() {
        view.buttonFollow.isEnabled = true
        view.buttonFollowed.isEnabled = true
        if (isShopFavourited){
            view.buttonFollow.visibility = View.GONE
            view.buttonFollowed.visibility = View.VISIBLE
            view.buttonFollowed.setOnClickListener{
                view.buttonFollowed.isEnabled = false
                listener.toggleFavorite(false)}
        } else {
            view.buttonFollowed.visibility = View.GONE
            view.buttonFollow.visibility = View.VISIBLE
            view.buttonFollow.setText(view.context.getString(R.string.shop_page_label_follow))
            view.buttonFollow.setOnClickListener{
                view.buttonFollow.isEnabled = false
                listener.toggleFavorite(true)}
        }
    }

    private fun displayAsSeller() {
        view.buttonChat.visibility = View.GONE
        view.buttonManageShop.visibility = View.VISIBLE
        view.buttonManageShop.setOnClickListener { listener.goToManageShop() }
        view.buttonFollowed.visibility = View.GONE
        view.buttonFollow.visibility = View.VISIBLE
        view.buttonFollow.text = view.context.getString(R.string.shop_page_label_add_product)
        view.buttonFollow.setOnClickListener { listener.goToAddProduct() }
    }

    fun displayGeneral(shopBadge: ShopBadge, isNonOfficial: Boolean) {
        if (isNonOfficial) {
            view.shopLabel.visibility = View.GONE
            view.shopReputationView.visibility = View.VISIBLE

            ImageHandler.loadImage(view.context, view.shopReputationView, shopBadge.badgeHD, -1)
        } else {
            view.shopLabel.visibility = View.VISIBLE
            view.shopReputationView.visibility = View.GONE
        }
    }

    private fun displayGoldenShop() {
        view.shopLabelIcon.visibility = View.VISIBLE
        view.shopLabelIcon.setImageDrawable(GMConstant.getGMDrawable(context))
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
        fun requestOpenShop(shopId: Int, moderateNotes: String)
        fun goToHowActivate()
        fun goToHelpCenter(url: String)
    }


}