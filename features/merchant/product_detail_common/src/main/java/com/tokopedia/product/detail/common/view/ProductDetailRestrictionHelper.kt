package com.tokopedia.product.detail.common.view

import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.unifycomponents.toPx

/**
 * Created by Yehezkiel on 02/11/21
 */
object ProductDetailRestrictionHelper {

    private const val RESTRICTION_CATEGORIES_PADDING_BOTTOM = 8
    private const val CAMPAIGN_TIER_PADDING_BOTTOM = 16

    fun renderRestrictionUi(reData: RestrictionData?,
                            isShopOwner: Boolean,
                            isFavoriteShop: Boolean,
                            reView: PartialButtonShopFollowersView?) {

        if (reData == null) {
            reView?.setupVisibility = false
            return
        }

        when {
            reData.restrictionExclusiveType() -> {
                renderExclusiveBottomSheet(reData = reData,
                        nplExclusiveView = reView)
            }
            reData.restrictionShopFollowersType() -> {
                renderNplButtonShopFollowers(
                        reData = reData,
                        isFavoriteShop = isFavoriteShop,
                        isShopOwner = isShopOwner,
                        shopFollowersView = reView
                )
            }
            reData.restrictionCategoriesType() -> {
                renderCommonRestrictionView(
                        reData = reData,
                        isShopOwner = isShopOwner,
                        shopFollowersView = reView
                )
            }
            reData.restrictionGamificationType() -> {
                renderCommonRestrictionView(
                        reData = reData,
                        isShopOwner = isShopOwner,
                        shopFollowersView = reView
                )
            }
            else -> {
                reView?.setupVisibility = false
            }
        }
    }

    //region common restriction view such as categories and gamification
    private fun renderCommonRestrictionView(reData: RestrictionData,
                                            isShopOwner: Boolean,
                                            shopFollowersView: PartialButtonShopFollowersView?) {
        val shouldShow = !reData.isEligible
        if (shouldShow && !isShopOwner) {
            if (shopFollowersView?.view?.isShown == false) {
                shopFollowersView.view.translationY = 100.toPx().toFloat()
            }

            val title = reData.action.firstOrNull()?.title ?: ""
            val desc = reData.action.firstOrNull()?.description ?: ""
            val badgeUrl = reData.action.firstOrNull()?.badgeURL ?: ""
            val buttonLabel = reData.action.firstOrNull()?.buttonText ?: ""
            shopFollowersView?.renderView(title = title,
                    desc = desc,
                    alreadyFollowShop = false,
                    centerImage = true,
                    buttonLabel = buttonLabel,
                    hideButton = buttonLabel.isEmpty(),
                    iconUrl = badgeUrl,
                    customPaddingBottom = RESTRICTION_CATEGORIES_PADDING_BOTTOM)
        }
        shopFollowersView?.setupVisibility = shouldShow && !isShopOwner
    }
    //endregion

    //region npl shop followers
    private fun renderNplButtonShopFollowers(reData: RestrictionData,
                                             isFavoriteShop: Boolean,
                                             isShopOwner: Boolean,
                                             shopFollowersView: PartialButtonShopFollowersView?) {
        val alreadyFollowShop = reData.isEligible
        val shouldShowRe = !alreadyFollowShop && !isFavoriteShop //show when user not follow the shop
        if (shouldShowRe && !isShopOwner && reData.restrictionShopFollowersType()) {
            if (shopFollowersView?.view?.isShown == false) {
                shopFollowersView.view.translationY = 100.toPx().toFloat()
            }

            val title = reData.action.firstOrNull()?.title ?: ""
            val desc = reData.action.firstOrNull()?.description ?: ""
            shopFollowersView?.renderView(title = title,
                    desc = desc,
                    alreadyFollowShop = alreadyFollowShop)
        }
        setupNplVisibility(shouldShowRe, isShopOwner, reData, shopFollowersView)
    }

    private fun setupNplVisibility(isFavorite: Boolean,
                                   isShopOwner: Boolean,
                                   selectedReData: RestrictionData,
                                   shopFollowersView: PartialButtonShopFollowersView?) {
        shopFollowersView?.setupVisibility = if (selectedReData.action.isNotEmpty() && !isShopOwner) {
            isFavorite
        } else {
            false
        }
    }
    //endregion

    //region npl shop exclusive
    private fun renderExclusiveBottomSheet(reData: RestrictionData,
                                           nplExclusiveView: PartialButtonShopFollowersView?) {
        val isExclusiveType = reData.restrictionExclusiveType()
        if (reData.action.isNotEmpty()) {
            val title = reData.action.firstOrNull()?.title ?: ""
            val desc = reData.action.firstOrNull()?.description ?: ""
            val badgeUrl = reData.action.firstOrNull()?.badgeURL ?: ""
            nplExclusiveView?.renderView(title = title,
                    alreadyFollowShop = false,
                    desc = desc,
                    iconUrl = badgeUrl,
                    hideButton = true,
                    maxLine = 2,
                    customPaddingBottom = CAMPAIGN_TIER_PADDING_BOTTOM,
                    centerImage = true)
        }
        setupExclusiveVisibility(reData.isNotEligibleExclusive(), isExclusiveType, nplExclusiveView)
    }

    private fun setupExclusiveVisibility(shouldVisible: Boolean, isExclusiveType: Boolean,
                                         nplExclusiveView: PartialButtonShopFollowersView?) {
        nplExclusiveView?.setupVisibility = shouldVisible && isExclusiveType
    }
    //endregion
}