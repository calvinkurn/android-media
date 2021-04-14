package com.tokopedia.home.account.presentation.listener

import com.tokopedia.home.account.presentation.viewmodel.*
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * @author okasurya on 7/26/18.
 */
interface AccountItemListener {

    fun onProfileClicked(element: BuyerCardViewModel)

    fun onProfileCompletionClicked(element: BuyerCardViewModel)

    fun onBuyerTokopointClicked(element: String , title: String)

    fun onBuyerVoucherClicked(element: String , title: String)

    fun onByMeClicked()

    fun onClickMemberDetail()

    fun onTokopediaPayLinkClicked()

    fun onMenuGridItemClicked(item: MenuGridItemViewModel)

    fun onMenuGridBackgroundItemClicked(item: MenuGridIconNotificationItemViewModel)

    fun onMenuGridLinkClicked(item: MenuGridViewModel)

    fun onMenuGridBackgroundLinkClicked(item: MenuGridIconNotificationViewModel)

    fun onInfoCardClicked(item: InfoCardViewModel, adapterPosition: Int)

    fun onMenuListClicked(item: MenuListViewModel)

    fun onTokopediaPayLeftItemClicked(label: String, applink: String, bsData: TokopediaPayBSModel,
                                      isLinked: Boolean, walletType: String)

    fun onTokopediaPayCentreItemClicked(label: String, applink: String, bsData: TokopediaPayBSModel,
                                      isLinked: Boolean, walletType: String)

    fun onTokopediaPayRightItemClicked(isRightSaldo: Boolean, label: String, vccStatus: String, applink: String, bsData: TokopediaPayBSModel)

    fun onTopadsInfoClicked()

    fun onGMInfoClicked()

    fun onSellerCenterInfoClicked()

    fun onOpenShopClicked()

    fun onLearnMoreSellerClicked()

    fun onKycLinkClicked(verificationStatus: Int)

    fun onTickerLinkClicked(url: String)

    fun onTickerClosed()

    fun onTopAdsMenuClicked()

    fun onSellerAccountCardClicked()

    fun onShopStatusInfoButtonClicked()

    fun onProductRecommendationClicked(product: RecommendationItem, adapterPosition: Int, widgetTitle: String)

    fun onProductRecommendationImpression(product: RecommendationItem, adapterPosition: Int)

    fun onProductRecommendationWishlistClicked(product: RecommendationItem, wishlistStatus: Boolean, callback: ((Boolean, Throwable?) -> Unit))

    fun onProductRecommendationThreeDotsClicked(product: RecommendationItem, adapterPosition: Int)

    fun onPowerMerchantSettingClicked()

    fun onTokomemberClicked(url: String, title:String)

    fun onAccountItemImpression(data: HashMap<String, Any>)

    fun getTrackingQueue(): TrackingQueue

    fun getRemoteConfig(): RemoteConfig

    fun onIconWarningNameClick(element: BuyerCardViewModel)
}
