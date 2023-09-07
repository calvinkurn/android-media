package com.tokopedia.applink

import android.net.Uri
import com.tokopedia.applink.communication.DeeplinkMapperCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.model.DLP
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.DeeplinkMapperPromo
import com.tokopedia.applink.sellersearch.SellerSearchDeeplinkMapper
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.applink.shopscore.ShopScoreDeepLinkMapper
import com.tokopedia.applink.statistic.DeepLinkMapperStatistic

object DeeplinkSellerApp {
    val deeplinkPatternTokopediaSchemeListv2: Map<String, MutableList<DLP>> = mapOf(
        "browser" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.BROWSER }
        ),
        "campaign-list" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.CAMPAIGN_LIST }
        ),
        "centralized-promo" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.CENTRALIZED_PROMO }
        ),
        "chatsettings" to mutableListOf(
            DLP.matchPattern("bubble-activation") { _, _, deeplink, _ ->
                DeeplinkMapperCommunication.getRegisteredNavigationBubbleActivation(
                    deeplink
                )
            }
        ),
        "create-voucher" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForCreateShopVoucher()
            }
        ),
        "create-voucher-product" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForCreateVoucherProduct()
            }
        ),
        "gold-merchant-statistic-dashboard" to mutableListOf(
            DLP.matchPattern("") { _, uri, _, _ ->
                DeepLinkMapperStatistic.getStatisticAppLink(uri)
            }
        ),
        "home" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.SELLER_HOME }
        ),
        "play-broadcaster" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalContent.INTERNAL_PLAY_BROADCASTER }
        ),
        "power_merchant" to mutableListOf(
            DLP.matchPattern("subscribe") { ctx, uri, _, _ ->
                PowerMerchantDeepLinkMapper.getPowerMerchantAppLink(ctx, uri)
            },
            DLP.matchPattern("benefit-package") { _: String ->
                ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE
            }
        ),
        "product" to mutableListOf(
            DLP.matchPattern("add") { _: String ->
                ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
            },
            DLP.matchPattern("manage") { _, _, deeplink, _ ->
                DeepLinkMapperProductManage.getProductListInternalAppLink(
                    deeplink
                )
            }
        ),
        "review-reminder" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.REVIEW_REMINDER }
        ),
        "seller-mvc" to mutableListOf(
            DLP.matchPattern("create/{voucher_type}") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerMvcCreate(
                    deeplink
                )
            },
            DLP.matchPattern("detail/{voucher_id}") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerMvcDetail(
                    deeplink
                )
            },
            DLP.matchPattern("intro") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerMvcIntro()
            },
            DLP.matchPattern("list") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST
            },
            DLP.matchPattern("list/active") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE
            },
            DLP.matchPattern("list/history") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_HISTORY
            },
            DLP.matchPattern("list/upcoming") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_UPCOMING
            },
            DLP.matchPattern("list/ongoing") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ONGOING
            },
            DLP.matchPattern("redirection-page") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_REDIRECTION_PAGE
            }
        ),
        "seller-persona" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.SELLER_PERSONA }
        ),
        "seller-search" to mutableListOf(
            DLP.goTo { context, _, _, _ ->
                SellerSearchDeeplinkMapper.getInternalApplinkSellerSearch(context)
            }
        ),
        "setting" to mutableListOf(
            DLP.matchPattern("shipping-editor") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            }
        ),
        "shop" to mutableListOf(
            DLP.matchPattern("operational-hours") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS
            },
            DLP.matchPattern("{shop_id}/feed") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationShopFeed(
                    deeplink
                )
            },
            DLP.matchPattern("{shop_id}/settings") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationShopPageSettingSellerApp(
                    deeplink
                )
            },
            DLP.matchPattern("showcase-create") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForCreateShowcase(
                    deeplink
                )
            }
        ),
        "shop-admin" to mutableListOf(
            DLP.matchPattern("invitation-page") { _: String ->
                ApplinkConstInternalMarketplace.ADMIN_INVITATION
            },
            DLP.matchPattern("accepted-page") { _, uri, _, _ ->
                ShopAdminDeepLinkMapper.getInternalAppLinkAdminAccepted(
                    uri
                )
            },
            DLP.matchPattern("redirection-page") { _: String ->
                ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
            }
        ),
        "shop-discount" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.SHOP_DISCOUNT }
        ),
        "shop-flash-sale" to mutableListOf(
            DLP.goTo { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerShopFlashSale(
                    deeplink
                )
            }
        ),
        "shop-nib" to mutableListOf(
            DLP.goToLink { DeeplinkMapperMerchant.getRegisteredNavigationForSellerShopNib() }
        ),
        "shop-score-detail" to mutableListOf(
            DLP.goTo { _, uri, _, _ ->
                ShopScoreDeepLinkMapper.getInternalAppLinkShopScore(
                    uri
                )
            }
        ),
        "stock-reminder" to mutableListOf(
            DLP.matchPattern("{productId}/{isVariant}") { _, _, deeplink, _ ->
                DeepLinkMapperProductManage.getStockReminderInternalAppLink(
                    deeplink
                )
            }
        ),
        "tokomember" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalSellerapp.TOKOMEMBER
            },
            DLP.matchPattern("program-list") { _: String ->
                ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_LIST
            },
            DLP.matchPattern("coupon-list") { _: String ->
                ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_LIST
            },
            DLP.matchPattern("program-creation") { _: String ->
                ApplinkConstInternalSellerapp.TOKOMEMBER_PROGRAM_CREATION
            },
            DLP.matchPattern("program-extension") { _, uri, deeplink, _ ->
                val trimmedDeeplink = UriUtil.trimDeeplink(uri, deeplink)
                val trimmedUri = Uri.parse(trimmedDeeplink)
                DeeplinkMapperPromo.getDeeplinkForProgramExtension(trimmedUri)
            },
            DLP.matchPattern("coupon-creation") { _: String ->
                ApplinkConstInternalSellerapp.TOKOMEMBER_COUPON_CREATION
            }
        ),
        "tokopedia-flash-sale" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSale()
            },
            DLP.matchPattern("campaign-detail/{campaign_id}") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleCampaignDetail(
                    deeplink
                )
            },
            DLP.matchPattern("upcoming") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleUpcoming()
            },
            DLP.matchPattern("registered") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleRegistered()
            },
            DLP.matchPattern("ongoing") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleOngoing()
            },
            DLP.matchPattern("finished") { _: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationForSellerTokopediaFlashSaleFinished()
            }

        ),
        "topads" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
            },
            DLP.matchPattern("history-credit") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_HISTORY_CREDIT
            },
            DLP.matchPattern("buy") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT
            },
            DLP.matchPattern("add-credit") { _: String ->
                ApplinkConstInternalTopAds.TOP_ADS_ADD_CREDIT
            },
            DLP.matchPattern("create-autoads") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE
            },
            DLP.matchPattern("create-ads") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_CREATE_ADS
            },
            DLP.matchPattern("creation-onboard") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_CREATION_ONBOARD
            },
            DLP.matchPattern("topads-onboarding") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_ONBOARDING
            },
            DLP.matchPattern("headline-ad-creation") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
            },
            DLP.matchPattern("headline-ad-detail") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_HEADLINE_DETAIL
            },
            DLP.matchPattern("edit-autoads") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            },
            DLP.matchPattern("auto-topup") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_AUTO_TOPUP
            },
            DLP.matchPattern("ad-picker") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER
            },
            DLP.matchPattern("dashboard") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
            }
        ),
        "voucher-detail" to mutableListOf(
            DLP.matchPattern("") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForShopVoucherDetail(
                    deeplink
                )
            }
        ),
        "voucher-list" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_REDIRECTION_PAGE
            },
            DLP.matchPattern("active") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE
            },
            DLP.matchPattern("history") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_HISTORY
            }
        ),
        "voucher-product-list" to mutableListOf(
            DLP.matchPattern("") { _, _, deeplink, _ ->
                DeeplinkMapperMerchant.getRegisteredNavigationForVoucherProductList(
                    deeplink
                )
            }
        ),
        "webview" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.WEBVIEW_BASE }
        ),
        "welcome" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalSellerapp.WELCOME }
        )
    )
}
