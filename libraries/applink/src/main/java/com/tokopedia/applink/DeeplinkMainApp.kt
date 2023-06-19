package com.tokopedia.applink

import com.tokopedia.applink.Hotlist.DeeplinkMapperHotlist
import com.tokopedia.applink.account.DeeplinkMapperAccount
import com.tokopedia.applink.category.DeeplinkMapperCategory
import com.tokopedia.applink.category.DeeplinkMapperMoneyIn
import com.tokopedia.applink.chatbot.DeeplinkMapperChatbot
import com.tokopedia.applink.common.DeeplinkMapperExternal
import com.tokopedia.applink.communication.DeeplinkMapperCommunication
import com.tokopedia.applink.contactus.DeeplinkMapperContactUs
import com.tokopedia.applink.content.DeeplinkMapperContent
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digitaldeals.DeeplinkMapperDeals
import com.tokopedia.applink.entertaiment.DeeplinkMapperEntertainment
import com.tokopedia.applink.etalase.DeepLinkMapperEtalase
import com.tokopedia.applink.feed.DeepLinkMapperFeed
import com.tokopedia.applink.find.DeepLinkMapperFind
import com.tokopedia.applink.fintech.DeeplinkMapperFintech
import com.tokopedia.applink.gamification.DeeplinkMapperGamification
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.imagepicker.DeeplinkMapperImagePicker
import com.tokopedia.applink.inbox.DeeplinkMapperInbox
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalDilayaniTokopedia
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalMedia
import com.tokopedia.applink.internal.ApplinkConstInternalOperational
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.logistic.DeeplinkMapperLogistic
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.model.DLPv2
import com.tokopedia.applink.model.MatchPattern
import com.tokopedia.applink.model.StartsWith
import com.tokopedia.applink.model.legacy.DLP
import com.tokopedia.applink.model.or
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.DeeplinkMapperPromo
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperWishlist
import com.tokopedia.applink.recommendation.DeeplinkMapperRecommendation
import com.tokopedia.applink.salam.DeeplinkMapperSalam
import com.tokopedia.applink.search.DeeplinkMapperSearch
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.applink.shopscore.ShopScoreDeepLinkMapper
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.applink.tokonow.DeeplinkMapperTokopediaNow
import com.tokopedia.applink.travel.DeeplinkMapperTravel
import com.tokopedia.applink.user.DeeplinkMapperUser

object DeeplinkMainApp {
    val deeplinkPatternTokopediaSchemeListv2: Map<String, MutableList<DLPv2>> = mapOf(
        "account" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperAccount::getAccountInternalApplink)
        ),
        "add-phone" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "add-pin-onboarding" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "addname" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.MANAGE_NAME)
        ),
        "affiliate" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperCategory::getRegisteredNavigationAffiliate),
            DLPv2.matchPattern("help", DeeplinkMapperCategory::getRegisteredNavigationAffiliate),
            DLPv2.matchPattern(
                "transaction-history",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLPv2.matchPattern(
                "edu-page",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLPv2.matchPattern(
                "shoplist-dipromosikan-affiliate",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLPv2.matchPattern(
                "discopage-list",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLPv2.matchPattern(
                "promosikan",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLPv2.matchPattern(
                "onboarding",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLPv2.startsWith(
                "create_post_v2",
                DeeplinkMapperContent::getContentCreatePostDeepLink
            ),
        ),
        "amp" to mutableListOf(
            DLPv2.startsWith(
                "find",
                DeepLinkMapperFind::getRegisteredFind
            )
        ),
        "belanja" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "browser" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalGlobal.BROWSER)
        ),
        "buka-toko-online-gratis" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION)
        ),
        "buyer" to mutableListOf(
            DLPv2.matchPattern("payment", ApplinkConstInternalPayment.PMS_PAYMENT_LIST),
            DLPv2.startsWith(
                "cancellationrequest",
                DeeplinkMapperOrder::getBuyerCancellationRequestInternalAppLink
            ),
            DLPv2(
                MatchPattern("order")
                    .or(MatchPattern("confirmed"))
                    .or(MatchPattern("processed"))
                    .or(MatchPattern("shipping-confirm"))
                    .or(MatchPattern("shipped"))
                    .or(MatchPattern("delivered"))
                    .or(StartsWith("history"))
                    .or(MatchPattern("ongoing-order"))
            ) { c, _, d, _ ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(c, d)
            }
        ),
        "cart" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "catalog" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperCategory::getRegisteredNavigationCatalog)
        ),
        "catalog-library" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperCategory::getRegisteredNavigationCategory)
        ),
        "category" to mutableListOf(
            DLPv2.startsWith("tradein", DeeplinkMapperCategory::getRegisteredTradeinNavigation),
            DLPv2.goTo(DeeplinkMapperCategory::getRegisteredCategoryNavigation)
        ),
        "category-explore" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperCategory::getRegisteredNavigationExploreCategory)
        ),
        "changeinactivephone" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "chat" to mutableListOf(
            DLPv2.matchPattern(
                "settings/templatechat",
                ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE
            )
        ),
        "chatbot" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperChatbot::getChatbotDeeplink)
        ),
        "chatsettings" to mutableListOf(
            DLPv2.matchPattern(
                "bubble-activation",
                DeeplinkMapperCommunication::getRegisteredNavigationBubbleActivation
            )
        ),
        "checkout" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "contact-us" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperContactUs::getNavigationContactUs)
        ),
        "content" to mutableListOf(
            DLPv2.matchPattern(
                "explore/{tab_name}/{category_id}",
                DeeplinkMapperHome::getRegisteredNavigationHomeContentExplore
            ),
            DLPv2.matchPattern("{post_id}", DeeplinkMapperContent::getKolDeepLink)
        ),
        "customercare" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalOperational.INTERNAL_INBOX_LIST)
        ),
        "deals" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.goTo(DeeplinkMapperDeals::getRegisteredNavigationDeals)
        ),
        "digital" to mutableListOf(
            DLPv2.startsWith("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "dilayani-tokopedia" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalDilayaniTokopedia.HOME)
        ),
        "discovery" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalCategory::getDiscoveryDeeplink)
        ),
        "epharmacy" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperCategory::getRegisteredNavigationCategory)
        ),
        "events" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.goTo(DeeplinkMapperEntertainment::getRegisteredNavigationEvents)
        ),
        "explicit-profile" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE)
        ),
        "feed" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperHome::getRegisteredNavigationHomeFeed),
            DLPv2.matchPattern(
                "explore",
                DeeplinkMapperContent::getRegisteredNavigationHomeFeedExplore
            ),
            DLPv2.matchPattern(
                "video",
                DeeplinkMapperContent::getRegisteredNavigationHomeFeedVideo
            ),
            DLPv2.startsWith(
                "following",
                DeeplinkMapperContent::getRegisteredNavigationHomeFeedFollowing
            ),
            DLPv2.startsWith(
                "creation-product-search",
                DeeplinkMapperContent::getContentCreatePostDeepLink
            ),
            DLPv2.startsWith(
                "creation-shop-search",
                DeeplinkMapperContent::getContentCreatePostDeepLink
            )
        ),
        "feedcommunicationdetail" to mutableListOf(
            DLPv2.goTo(DeepLinkMapperFeed::getRegisteredFeed)
        ),
        "feedplaylivedetail" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalFeed.INTERNAL_PLAY_LIVE_DETAILS)
        ),
        "find" to mutableListOf(
            DLPv2.goTo(DeepLinkMapperFind::getRegisteredFind)
        ),
        "fintech" to mutableListOf(
            DLPv2.startsWith("paylater", ApplinkConstInternalFintech.PAYLATER),
            DLPv2.startsWith("activate_gopay", ApplinkConstInternalFintech.ACTIVATE_GOPAY),
            DLPv2.startsWith("opt-checkout", ApplinkConstInternalFintech.OCC_CHECKOUT),
            DLPv2(
                MatchPattern("home-credit/selfie")
                    .or(MatchPattern("home-credit/ktp"))
            ) { _, uri, _, _ ->
                DeeplinkMapperFintech.getRegisteredNavigationForHomeCreditRegister(
                    uri
                )
            },
            DLPv2(
                MatchPattern("home-credit/selfie/{type}")
                    .or(MatchPattern("home-credit/ktp/{type}"))
            ) { _, uri, _, _ ->
                DeeplinkMapperFintech.getRegisteredNavigationForHomeCreditRegister(
                    uri
                )
            }
        ),
        "food" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.goTo(DeeplinkMapperTokoFood::mapperInternalApplinkTokoFood)
        ),
        "gamification" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "gamification2" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "gamification_gift_60s" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "gamification_gift_daily" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "giftcards" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "gifting" to mutableListOf(
            DLPv2.matchPattern("{addon_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMechant.MERCHANT_GIFTING, idList?.getOrNull(0))
            }
        ),
        "gofood" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperTokoFood::mapperInternalApplinkTokoFood)
        ),
        "gojek-account-link" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW)
        ),
        "gold-merchant-statistic-dashboard" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "home" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperHome::getRegisteredNavigationHome)
        ),
        "hot" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperHotlist::getRegisteredHotlist)
        ),
        "hotel" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.goTo(DeeplinkMapperTravel::getRegisteredNavigationTravel)
        ),
        "howtopay" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY)
        ),
        "image-picker" to mutableListOf(
            DLPv2.matchPattern("v2", DeeplinkMapperImagePicker::getImagePickerV2Deeplink)
        ),
        "inbox" to mutableListOf(
            DLPv2.goTo(ApplinkConsInternalHome.HOME_INBOX)
        ),
        "inputinactivenumber" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "insurance" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "internal-feedback" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalGlobal.FEEDBACK_FORM)
        ),
        "jump" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperHome::getRegisteredExplore)
        ),
        "kereta" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "kyc" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.KYC_INFO_BASE)
        ),
        "kyc-form" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalUserPlatform.KYC_FORM_BASE)
        ),
        "layanan-finansial" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperFintech::getRegisteredNavigationForLayanan)
        ),
        "login" to mutableListOf(
            DLPv2.startsWith("qr", ApplinkConstInternalUserPlatform.QR_LOGIN),
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.LOGIN)
        ),
        "marketplace" to mutableListOf(
            DLPv2.startsWith("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.startsWith(
                "buyer-order-extension",
                ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION
            ),
            DLPv2.startsWith(
                "buyer-partial-order-fulfillment",
                ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_PARTIAL_ORDER_FULFILLMENT
            ),
            DLPv2.startsWith("onboarding", ApplinkConstInternalMarketplace.ONBOARDING)
        ),
        "media-editor" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR)
        ),
        "media-picker" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER)
        ),
        "media-picker-preview" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW)
        ),
        "merchant-voucher" to mutableListOf(
            DLPv2.matchPattern("list", ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE)
        ),
        "modaltoko" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "money_in" to mutableListOf(
            DLPv2.startsWith(
                "device_validation",
                DeeplinkMapperMoneyIn::getRegisteredNavigationMoneyIn
            )
        ),
        "my-shop" to mutableListOf(
            DLPv2.matchPattern(
                "etalase/list",
                ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
            )
        ),
        "navigation" to mutableListOf(
            DLPv2.matchPattern("main", ApplinkConsInternalNavigation.MAIN_NAVIGATION)
        ),
        "new-wishlist" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperWishlist::getRegisteredNavigationWishlist)
        ),
        "notif-center" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperInbox::getRegisteredNavigationNotifcenter)
        ),
        "notification" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperInbox::getRegisteredNavigationNotifcenter)
        ),
        "notification-troubleshooter" to mutableListOf(
            DLPv2.matchPattern(
                "",
                ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER
            )
        ),
        "now" to mutableListOf(
            DLPv2.matchPattern(
                "",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowHome
            ),
            DLPv2.startsWith(
                "search",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowSearch
            ),
            DLPv2.startsWith(
                "category",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowCategory
            ),
            DLPv2.startsWith("repurchase-page", ApplinkConstInternalTokopediaNow::REPURCHASE),
            DLPv2.matchPattern(
                "recipe/detail/{recipe_id}",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeDetail
            ),
            DLPv2.startsWith(
                "recipe/bookmarks",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeBookmark
            ),
            DLPv2.matchPattern(
                "recipe",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeHome
            ),
            DLPv2.startsWith(
                "recipe/search",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeSearch
            ),
            DLPv2.startsWith(
                "recipe/autocomplete",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeAutoComplete
            )
        ),
        "occ" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "official-store" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperHome::getRegisteredNavigationHomeOfficialStore)
        ),
        "official-stores" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperHome::getRegisteredNavigationHomeOfficialStore)
        ),
        "order" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "order-details" to mutableListOf(
            DLPv2.startsWith(
                "umroh",
                DeeplinkMapperSalam::getRegisteredNavigationSalamUmrahOrderDetail
            )
        ),
        "order_list" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "otp" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.COTP)
        ),
        "otp-verify" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.OTP_PUSH_NOTIF_RECEIVER)
        ),
        "ovoqrthanks" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperFintech::getRegisteredNavigationForFintech)
        ),
        "p" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperCategory::getRegisteredCategoryNavigation)
        ),
        "payment" to mutableListOf(
            DLPv2.matchPattern(
                "credit-card/add",
                ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ),
            DLPv2.matchPattern("gopayKyc", ApplinkConstInternalPayment.GOPAY_KYC),
            DLPv2.matchPattern("thankyou", ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE)
        ),
        "people" to mutableListOf(
            DLPv2.matchPattern(
                "{user_id}",
                DeeplinkMapperContent::getProfileDeeplink
            )
        ),
        "pesawat" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLPv2.matchPattern("", ApplinkConstInternalTravel.DASHBOARD_FLIGHT)
        ),
        "pin-point-picker-result" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperLogistic::getRegisteredNavigationPinpointWebview)
        ),
        "play" to mutableListOf(
            DLPv2.matchPattern("{channel_id}", DeeplinkMapperContent::getRegisteredNavigation)
        ),
        "play-broadcaster" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperContent::getRegisteredNavigation)
        ),
        "play-notif-video" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalGlobal.YOUTUBE_VIDEO)
        ),
        "play-shorts" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperContent::getRegisteredNavigation)
        ),
        "plus" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "post-atc" to mutableListOf(
            DLPv2.matchPattern("{productId}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.POST_ATC,
                    idList?.getOrNull(0) ?: ""
                )
            }
        ),
        "power_merchant" to mutableListOf(
            DLPv2.startsWith("subscribe", PowerMerchantDeepLinkMapper::getPowerMerchantAppLink),
            DLPv2.matchPattern(
                "benefit_package",
                ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE
            ),
            DLPv2.startsWith(
                "interrupt",
                PowerMerchantDeepLinkMapper::getInternalAppLinkPmProInterrupt
            )
        ),
        "privacy-center" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser),
            DLPv2.matchPattern("dsar", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "product" to mutableListOf(
            DLPv2.matchPattern("edit/{product_id}") { _, _, _, idList ->
                DeepLinkMapperProductManage.getEditProductInternalAppLink(
                    idList?.getOrNull(0) ?: ""
                )
            },
            DLPv2.matchPattern(
                "{id}/review",
                DeeplinkMapperMerchant::getRegisteredNavigationProductDetailReview
            ),
            DLPv2.matchPattern(
                "{id}/review/gallery",
                DeeplinkMapperMerchant::getRegisteredNavigationProductDetailReviewGallery
            ),
            DLPv2.matchPattern(
                "{product_id}",
                DeeplinkMapperMarketplace::getTokopediaInternalProduct
            ),
            DLPv2.matchPattern("{product_id}/talk") { _, _, _, idList ->
                DeeplinkMapper.getRegisteredNavigationProductTalk(idList?.getOrNull(0))
            }
        ),
        "product-bundle" to mutableListOf(
            DLPv2.matchPattern("{product_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE,
                    idList?.getOrNull(0)
                )
            }
        ),
        "product-edu" to mutableListOf(
            DLPv2.matchPattern("{type}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL_EDUCATIONAL,
                    idList?.lastOrNull() ?: ""
                )
            }
        ),
        "product-order-history" to mutableListOf(
            DLPv2.matchPattern("{shop_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.ORDER_HISTORY,
                    idList?.getOrNull(0)
                )
            },
            DLPv2.goTo(DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "product-review" to mutableListOf(
            DLPv2.startsWith(
                "create",
                DeeplinkMapperMerchant::getRegisteredNavigationProductReview
            ),
            DLPv2.matchPattern("bulk-create", ApplinkConstInternalMarketplace.BULK_CREATE_REVIEW)
        ),
        "productar" to mutableListOf(
            DLPv2.matchPattern("{product_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_AR, idList?.getOrNull(0))
            }
        ),
        "productpickerfromshop" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperContent::getContentCreatePostDeepLink)
        ),
        "profilecompletion" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.PROFILE_COMPLETION)
        ),
        "promo" to mutableListOf(
            DLPv2.matchPattern("") { _, _, _, _ ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            },
            DLPv2.matchPattern("{slug}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalPromo.PROMO_DETAIL,
                    idList?.getOrNull(0)
                )
            },
        ),
        "promoNative" to mutableListOf(
            DLPv2.matchPattern("") { _, _, _, _ ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            }
        ),
        "recharge" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "register-init" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalUserPlatform.INIT_REGISTER)
        ),
        "registration" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalUserPlatform.INIT_REGISTER)
        ),
        "rekomendasi" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperRecommendation::getRegisteredNavigationRecommendation)
        ),
        "resetpassword" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)
        ),
        "resolution" to mutableListOf(
            DLPv2.matchPattern(
                "success-create",
                ApplinkConstInternalOperational::buildApplinkResolution
            )
        ),
        "review" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperMerchant::getRegisteredNavigationReputation)
        ),
        "review-reminder" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalSellerapp.REVIEW_REMINDER)
        ),
        "rewards" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperPromo::getRegisteredNavigationTokopoints)
        ),
        "s" to mutableListOf(
            DLPv2.startsWith("umroh", DeeplinkMapperSalam::getRegisteredNavigationSalamUmrah)
        ),
        "saldo" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        ),
        "saldo-intro" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalGlobal.SALDO_INTRO)
        ),
        "scanqr" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalMarketplace.QR_SCANNEER)
        ),
        "search" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperSearch::getRegisteredNavigationSearch)
        ),
        "search-autocomplete" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperSearch::getRegisteredNavigationSearch)
        ),
        "seller" to mutableListOf(
            DLPv2.startsWith(
                "product/manage",
                DeepLinkMapperProductManage::getProductListInternalAppLink
            ),
            DLPv2.startsWith("order", DeeplinkMapperOrder::getRegisteredNavigationOrder),
            DLPv2.startsWith(
                "reschedulepickup",
                DeeplinkMapperLogistic::getReschedulePickupDeeplink
            ),
            DLPv2.startsWith("new-order", AppLinkMapperSellerHome::getSomNewOrderAppLink),
            DLPv2.startsWith("ready-to-ship", AppLinkMapperSellerHome::getSomReadyToShipAppLink),
            DLPv2.startsWith(
                "delivered",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerDelivered
            ),
            DLPv2.startsWith(
                "waitingpickup",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerWaitingPickup
            ),
            DLPv2.startsWith(
                "waitingawb",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerWaitingAwb
            ),
            DLPv2.startsWith(
                "awbinvalid",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerAwbInvalid
            ),
            DLPv2.startsWith(
                "awbchange",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerAwbChange
            ),
            DLPv2.startsWith(
                "retur",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerRetur
            ),
            DLPv2.startsWith(
                "complaint",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerComplaint
            ),
            DLPv2.startsWith("history", AppLinkMapperSellerHome::getSomAllOrderAppLink),
            DLPv2.startsWith("shipped", AppLinkMapperSellerHome::getSomShippedAppLink),
            DLPv2.startsWith("shipment", AppLinkMapperSellerHome::getSomReadyToShipAppLink),
            DLPv2.startsWith("finished", AppLinkMapperSellerHome::getSomDoneAppLink),
            DLPv2.startsWith("cancelled", AppLinkMapperSellerHome::getSomCancelledAppLink),
            DLPv2.startsWith(
                "cancellationrequest",
                AppLinkMapperSellerHome::getSomCancellationRequestAppLink
            ),
            DLPv2.startsWith("status", AppLinkMapperSellerHome::getSomShippedAppLink),
            DLPv2.matchPattern(
                "setting/shipping-editor",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            ),
            DLPv2.matchPattern(
                "setting/custom-product-logistic",
                ApplinkConstInternalLogistic.CUSTOM_PRODUCT_LOGISTIC
            ),
            DLPv2.matchPattern(
                "setting/cod-activation",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_COD
            ),
            DLPv2.matchPattern(
                "setting/shop-address",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS
            ),
            DLPv2.matchPattern(
                "setting/operational-hours",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS
            ),
            DLPv2.startsWith("seller-center", DeeplinkMapperMerchant::getRegisteredSellerCenter)
        ),
        "seller-review-detail" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalMarketplace.SELLER_REVIEW_DETAIL)
        ),
        "sellerinfo" to mutableListOf(
            DLPv2.startsWith("detail", DeeplinkMapperMerchant::getSellerInfoDetailApplink)
        ),
        "setting" to mutableListOf(
            DLPv2.startsWith("editaddress", DeeplinkMapperLogistic::getEditAddressDeeplink),
            DLPv2.matchPattern("shop/note", ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES),
            DLPv2.matchPattern("shop/info", ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO),
            DLPv2.matchPattern("profile", DeeplinkMapperUser::getRegisteredNavigationUser),
            DLPv2.matchPattern("address", ApplinkConstInternalLogistic.MANAGE_ADDRESS),
            DLPv2.matchPattern("payment", ApplinkConstInternalUserPlatform.PAYMENT_SETTING),
            DLPv2.matchPattern("account", ApplinkConstInternalUserPlatform.ACCOUNT_SETTING),
        ),
        "settings" to mutableListOf(
            DLPv2.matchPattern(
                "notification",
                ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            ),
            DLPv2.matchPattern("bankaccount", ApplinkConstInternalGlobal.SETTING_BANK),
            DLPv2.matchPattern("haspassword", ApplinkConstInternalUserPlatform.HAS_PASSWORD)
        ),
        "share_address" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperLogistic::getRegisteredNavigationShareAddress)
        ),
        "sharing" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperExternal::getRegisteredNavigation)
        ),
        "shipping" to mutableListOf(
            DLPv2.matchPattern(
                "tracking/{order_id}",
                DeeplinkMapperLogistic::getRegisteredNavigationOrder
            ),
            DLPv2.matchPattern(
                "pod/{order_id}",
                DeeplinkMapperLogistic::getRegisteredNavigationPod
            )
        ),
        "shop" to mutableListOf(
            DLPv2.matchPattern("{shop_id}/etalase-list") { _, _, _, idList ->
                DeepLinkMapperEtalase.getEtalaseListInternalAppLink(idList?.getOrNull(0) ?: "")
            },
            DLPv2.matchPattern("{shop_id}/talk") { _, _, _, idList ->
                DeeplinkMapper.getRegisteredNavigationShopTalk(idList?.getOrNull(0))
            },
            DLPv2.matchPattern("{shop_id}") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/home") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_HOME,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/product") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/feed") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_FEED,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/review") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_REVIEW,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/follower") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST_WITH_SHOP_ID,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/settings") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/info") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_INFO,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/note") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx, uri, deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_NOTE,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id_or_domain}/etalase/{etalase_id_or_alias}") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST,
                        idList?.getOrNull(0),
                        idList?.getOrNull(1)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLPv2.matchPattern("{shop_id}/operational-hour") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET,
                    idList?.getOrNull(0)
                )
            },
            DLPv2.matchPattern("{shop_id}/voucher/{voucher_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_MVC_LOCKED_TO_PRODUCT,
                    idList?.getOrNull(0),
                    idList?.getOrNull(1)
                )
            }
        ),
        "shop-admin" to mutableListOf(
            DLPv2.matchPattern("invitation-page", ApplinkConstInternalMarketplace.ADMIN_INVITATION),
            DLPv2.matchPattern(
                "redirection-page",
                ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
            ),
            DLPv2.startsWith(
                "accepted-page",
                ShopAdminDeepLinkMapper::getInternalAppLinkAdminAccepted
            )
        ),
        "shop-penalty" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalMarketplace.SHOP_PENALTY)
        ),
        "shop-penalty-detail" to mutableListOf(
            DLPv2.matchPattern("", ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL)
        ),
        "shop-score-detail" to mutableListOf(
            DLPv2.goTo(ShopScoreDeepLinkMapper::getInternalAppLinkShopScore)
        ),
        "snapshot" to mutableListOf(
            DLPv2.startsWith("order", DeeplinkMapperOrder::getSnapshotOrderInternalAppLink)
        ),
        "talk" to mutableListOf(
            DLPv2.goTo(DeeplinkMapper::getRegisteredNavigationTalk)
        ),
        "telephony-masking" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalUserPlatform.TELEPHONY_MASKING)
        ),
        "telkomselomni" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "tokochat" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperCommunication::getRegisteredNavigationTokoChat)
        ),
        "tokopoints" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperPromo::getRegisteredNavigationTokopoints)
        ),
        "topads" to mutableListOf(
            DLPv2.startsWith(
                "create-manual-ads",
                ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS
            )
        ),
        "topchat" to mutableListOf(
            DLPv2.goTo(DeeplinkMapper::getRegisteredNavigationTopChat)
        ),
        "topchatold" to mutableListOf(
            DLPv2.goTo(DeeplinkMapper::getRegisteredNavigationTopChat)
        ),
        "travelent" to mutableListOf(
            DLPv2.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "travelentertainment" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "universal-page" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperSearch::getRegisteredNavigationSearch)
        ),
        "user-identification-only" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperUser::getRegisteredUserNavigation)
        ),
        "webview" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalGlobal.WEBVIEW_BASE)
        ),
        "webviewbackhome" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalGlobal.WEBVIEW_BACK_HOME)
        ),
        "webviewdownload" to mutableListOf(
            DLPv2.goTo(ApplinkConstInternalGlobal.WEBVIEW_DOWNLOAD)
        ),
        "wishlist" to mutableListOf(
            DLPv2.matchPattern("", DeeplinkMapperWishlist::getRegisteredNavigationWishlist),
            DLPv2.matchPattern("collection/{collection_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL,
                    idList?.getOrNull(0)
                )
            }
        ),
        "www.tokopedia.com" to mutableListOf(
            DLPv2.goTo(DeeplinkMapperContent::getWebHostWebViewLink)
        )

    )
}