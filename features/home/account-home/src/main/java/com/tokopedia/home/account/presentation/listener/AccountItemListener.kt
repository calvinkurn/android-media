package com.tokopedia.home.account.presentation.listener

import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * @author okasurya on 7/26/18.
 */
interface AccountItemListener {

    fun onProfileClicked(element: BuyerCardViewModel)

    fun onProfileCompletionClicked(element: BuyerCardViewModel)

    fun onBuyerTokopointClicked(element: BuyerCardViewModel)

    fun onBuyerVoucherClicked(element: BuyerCardViewModel)

    fun onByMeClicked()

    fun onTokopediaPayLinkClicked()

    fun onMenuGridItemClicked(item: MenuGridItemViewModel)

    fun onMenuGridLinkClicked(item: MenuGridViewModel)

    fun onInfoCardClicked(item: InfoCardViewModel, adapterPosition: Int)

    fun onMenuListClicked(item: MenuListViewModel)

    fun onShopAvatarClicked(element: ShopCardViewModel)

    fun onShopNameClicked(element: ShopCardViewModel)

    fun onAddProductClicked()

    fun onTokopediaPayLeftItemClicked(label: String, applink: String, bsData: TokopediaPayBSModel,
                                      isLinked: Boolean, walletType: String)

    fun onTokopediaPayRightItemClicked(isRightSaldo: Boolean, label: String, vccStatus: String, applink: String, bsData: TokopediaPayBSModel)

    fun onDepositClicked(element: SellerSaldoViewModel)

    fun onTopadsInfoClicked()

    fun onGMInfoClicked()

    fun onSellerCenterInfoClicked()

    fun onOpenShopClicked()

    fun onLearnMoreSellerClicked()

    fun onKycLinkClicked(verificationStatus: Int)

    fun onTickerLinkClicked(url: String)

    fun onTickerClosed()

    fun onTopAdsMenuClicked()

    fun onShopStatusInfoButtonClicked()

    fun onProductRecommendationClicked(product: RecommendationItem, adapterPosition: Int)

    fun onProductRecommendationImpression(product: RecommendationItem)

    fun onProductRecommendationWishlistClicked(product: RecommendationItem, wishlistStatus: Boolean, callback: ((Boolean, Throwable) -> Unit))
}
