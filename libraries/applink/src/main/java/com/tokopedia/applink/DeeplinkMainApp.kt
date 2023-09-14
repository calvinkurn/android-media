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
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.logistic.DeeplinkMapperLogistic
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace
import com.tokopedia.applink.merchant.DeeplinkMapperMerchant
import com.tokopedia.applink.model.DLP
import com.tokopedia.applink.model.MatchPattern
import com.tokopedia.applink.model.StartsWith
import com.tokopedia.applink.model.or
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.promo.DeeplinkMapperPromo
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getCelebrationBottomsheetDeeplink
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getInternalDeeplinkForScpCelebration
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getInternalDeeplinkForScpMedalCabinet
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getInternalDeeplinkForScpMedalCabinetSeeMore
import com.tokopedia.applink.promo.DeeplinkMapperPromo.getInternalDeeplinkForScpMedalDetail
import com.tokopedia.applink.promo.DeeplinkMapperPromo.invokeScpToasterUniversalAppLink
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
    val deeplinkPatternTokopediaSchemeListv2: Map<String, MutableList<DLP>> = mapOf(
        "account" to mutableListOf(
            DLP.goTo(DeeplinkMapperAccount::getAccountInternalApplink)
        ),
        "add-phone" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "add-pin-onboarding" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "addname" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.MANAGE_NAME)
        ),
        "affiliate" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperCategory::getRegisteredNavigationAffiliate),
            DLP.matchPattern("help", DeeplinkMapperCategory::getRegisteredNavigationAffiliate),
            DLP.matchPattern(
                "transaction-history",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.matchPattern(
                "edu-page",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.matchPattern(
                "shoplist-dipromosikan-affiliate",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.matchPattern(
                "discopage-list",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.matchPattern(
                "promosikan",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.matchPattern(
                "onboarding",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.matchPattern(
                "performa",
                DeeplinkMapperCategory::getRegisteredNavigationAffiliate
            ),
            DLP.startsWith(
                "create_post_v2",
                DeeplinkMapperContent::getContentCreatePostDeepLink
            )
        ),
        "amp" to mutableListOf(
            DLP.startsWith(
                "find",
                DeepLinkMapperFind::getRegisteredFind
            )
        ),
        "belanja" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "browser" to mutableListOf(
            DLP.goTo(ApplinkConstInternalGlobal.BROWSER)
        ),
        "buka-toko-online-gratis" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION)
        ),
        "buyer" to mutableListOf(
            DLP.matchPattern("payment", ApplinkConstInternalPayment.PMS_PAYMENT_LIST),
            DLP.startsWith(
                "cancellationrequest",
                DeeplinkMapperOrder::getBuyerCancellationRequestInternalAppLink
            ),
            DLP(
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
            DLP.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "catalog" to mutableListOf(
            DLP.goTo(DeeplinkMapperCategory::getRegisteredNavigationCatalog)
        ),
        "catalog-library" to mutableListOf(
            DLP.goTo(DeeplinkMapperCategory::getRegisteredNavigationCategory)
        ),
        "category" to mutableListOf(
            DLP.startsWith("tradein", DeeplinkMapperCategory::getRegisteredTradeinNavigation),
            DLP.goTo(DeeplinkMapperCategory::getRegisteredCategoryNavigation)
        ),
        "category-explore" to mutableListOf(
            DLP.goTo(DeeplinkMapperCategory::getRegisteredNavigationExploreCategory)
        ),
        "changeinactivephone" to mutableListOf(
            DLP.goTo(DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "chat" to mutableListOf(
            DLP.matchPattern(
                "settings/templatechat",
                ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE
            )
        ),
        "chatbot" to mutableListOf(
            DLP.goTo(DeeplinkMapperChatbot::getChatbotDeeplink)
        ),
        "chatsettings" to mutableListOf(
            DLP.matchPattern(
                "bubble-activation",
                DeeplinkMapperCommunication::getRegisteredNavigationBubbleActivation
            )
        ),
        "checkout" to mutableListOf(
            DLP.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "contact-us" to mutableListOf(
            DLP.goTo(DeeplinkMapperContactUs::getNavigationContactUs)
        ),
        "content" to mutableListOf(
            DLP.matchPattern(
                "explore/{tab_name}/{category_id}",
                DeeplinkMapperHome::getRegisteredNavigationHomeContentExplore
            ),
            DLP.matchPattern("{source_id}", DeeplinkMapperContent::getContentFeedDeeplink)
        ),
        "customercare" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalOperational.INTERNAL_INBOX_LIST)
        ),
        "deals" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.goTo(DeeplinkMapperDeals::getRegisteredNavigationDeals)
        ),
        "digital" to mutableListOf(
            DLP.startsWith("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "dilayani-tokopedia" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalDilayaniTokopedia.HOME)
        ),
        "discovery" to mutableListOf(
            DLP.goTo(ApplinkConstInternalCategory::getDiscoveryDeeplink)
        ),
        "epharmacy" to mutableListOf(
            DLP.goTo(DeeplinkMapperCategory::getRegisteredNavigationCategory)
        ),
        "events" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.goTo(DeeplinkMapperEntertainment::getRegisteredNavigationEvents)
        ),
        "explicit-profile" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE)
        ),
        "feed" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperHome::getRegisteredNavigationHomeFeed),
            DLP.matchPattern(
                "explore",
                DeeplinkMapperContent::getRegisteredNavigationHomeFeedExplore
            ),
            DLP.matchPattern(
                "video",
                DeeplinkMapperContent::getRegisteredNavigationHomeFeedVideo
            ),
            DLP.startsWith(
                "following",
                DeeplinkMapperContent::getRegisteredNavigationHomeFeedFollowing
            ),
            DLP.startsWith(
                "creation-product-search",
                DeeplinkMapperContent::getContentCreatePostDeepLink
            ),
            DLP.startsWith(
                "creation-shop-search",
                DeeplinkMapperContent::getContentCreatePostDeepLink
            )
        ),
        "feedcommunicationdetail" to mutableListOf(
            DLP.goTo(DeepLinkMapperFeed::getRegisteredFeed)
        ),
        "feedplaylivedetail" to mutableListOf(
            DLP.goTo(ApplinkConstInternalFeed.INTERNAL_PLAY_LIVE_DETAILS)
        ),
        "find" to mutableListOf(
            DLP.goTo(DeepLinkMapperFind::getRegisteredFind)
        ),
        "fintech" to mutableListOf(
            DLP.startsWith("paylater", ApplinkConstInternalFintech.PAYLATER),
            DLP.startsWith("activate_gopay", ApplinkConstInternalFintech.ACTIVATE_GOPAY),
            DLP.startsWith("opt-checkout", ApplinkConstInternalFintech.OCC_CHECKOUT),
            DLP(
                MatchPattern("home-credit/selfie")
                    .or(MatchPattern("home-credit/ktp"))
            ) { _, uri, _, _ ->
                DeeplinkMapperFintech.getRegisteredNavigationForHomeCreditRegister(
                    uri
                )
            },
            DLP(
                MatchPattern("home-credit/selfie/{type}")
                    .or(MatchPattern("home-credit/ktp/{type}"))
            ) { _, uri, _, _ ->
                DeeplinkMapperFintech.getRegisteredNavigationForHomeCreditRegister(
                    uri
                )
            }
        ),
        "food" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.goTo(DeeplinkMapperTokoFood::mapperInternalApplinkTokoFood)
        ),
        "gamification" to mutableListOf(
            DLP.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "gamification2" to mutableListOf(
            DLP.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "gamification_gift_60s" to mutableListOf(
            DLP.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "gamification_gift_daily" to mutableListOf(
            DLP.goTo(DeeplinkMapperGamification::getGamificationDeeplink)
        ),
        "giftcards" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "gifting" to mutableListOf(
            DLP.matchPattern("{addon_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMechant.MERCHANT_GIFTING, idList?.getOrNull(0))
            }
        ),
        "gofood" to mutableListOf(
            DLP.goTo(DeeplinkMapperTokoFood::mapperInternalApplinkTokoFood)
        ),
        "gojek-account-link" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW)
        ),
        "gold-merchant-statistic-dashboard" to mutableListOf(
            DLP.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "goto-kyc" to mutableListOf(
            DLP.goTo(DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "home" to mutableListOf(
            DLP.goTo(DeeplinkMapperHome::getRegisteredNavigationHome)
        ),
        "hot" to mutableListOf(
            DLP.goTo(DeeplinkMapperHotlist::getRegisteredHotlist)
        ),
        "hotel" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.goTo(DeeplinkMapperTravel::getRegisteredNavigationTravel)
        ),
        "howtopay" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY)
        ),
        "image-picker" to mutableListOf(
            DLP.matchPattern("v2", DeeplinkMapperImagePicker::getImagePickerV2Deeplink)
        ),
        "inbox" to mutableListOf(
            DLP.goTo(DeeplinkMapperCommunication::getRegisteredNavigationInbox)
        ),
        "inputinactivenumber" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "insurance" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "internal-feedback" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalGlobal.FEEDBACK_FORM)
        ),
        "jump" to mutableListOf(
            DLP.goTo(DeeplinkMapperHome::getRegisteredExplore)
        ),
        "kereta" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "kyc" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.KYC_INFO_BASE)
        ),
        "kyc-form" to mutableListOf(
            DLP.goTo(ApplinkConstInternalUserPlatform.KYC_FORM_BASE)
        ),
        "layanan-finansial" to mutableListOf(
            DLP.goTo(DeeplinkMapperFintech::getRegisteredNavigationForLayanan)
        ),
        "login" to mutableListOf(
            DLP.startsWith("qr", ApplinkConstInternalUserPlatform.QR_LOGIN),
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.LOGIN)
        ),
        "marketplace" to mutableListOf(
            DLP.startsWith("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.startsWith(
                "buyer-order-extension",
                ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION
            ),
            DLP.startsWith(
                "buyer-partial-order-fulfillment",
                ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_PARTIAL_ORDER_FULFILLMENT
            ),
            DLP.startsWith("onboarding", ApplinkConstInternalMarketplace.ONBOARDING)
        ),
        "medali" to mutableListOf(
            DLP.matchPattern(ApplinkConst.ScpRewards.CELEBRATION_BASE) { _, uri, _, _ -> getInternalDeeplinkForScpCelebration(uri) },
            DLP.startsWith(ApplinkConst.ScpRewards.CELEBRATION_BOTTOMSHEET) { _, uri, _, _ -> getCelebrationBottomsheetDeeplink(uri) },
            DLP.startsWith(ApplinkConst.ScpRewards.SCP_TOASTER) { ctx, uri, _, _ -> invokeScpToasterUniversalAppLink(ctx, uri) },
            DLP.startsWith(ApplinkConst.ScpRewards.MEDAL_DETAIL_BASE) { _, uri, _, _ -> getInternalDeeplinkForScpMedalDetail(uri) },
            DLP.startsWith(ApplinkConst.ScpRewards.SEE_MORE_MEDAL) { _, uri, _, _ -> getInternalDeeplinkForScpMedalCabinetSeeMore(uri) },
            DLP.matchPattern("") { _, uri, _, _ -> getInternalDeeplinkForScpMedalCabinet(uri) }
        ),
        "media-editor" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR)
        ),
        "media-picker" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER)
        ),
        "media-picker-preview" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW)
        ),
        "merchant-voucher" to mutableListOf(
            DLP.matchPattern("list", ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE)
        ),
        "modaltoko" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "money_in" to mutableListOf(
            DLP.startsWith(
                "device_validation",
                DeeplinkMapperMoneyIn::getRegisteredNavigationMoneyIn
            )
        ),
        "my-shop" to mutableListOf(
            DLP.matchPattern(
                "etalase/list",
                ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
            )
        ),
        "navigation" to mutableListOf(
            DLP.matchPattern("main", ApplinkConsInternalNavigation.MAIN_NAVIGATION)
        ),
        "new-wishlist" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperWishlist::getRegisteredNavigationWishlist)
        ),
        "notif-center" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperInbox::getRegisteredNavigationNotifcenter)
        ),
        "notification" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperInbox::getRegisteredNavigationNotifcenter)
        ),
        "notification-troubleshooter" to mutableListOf(
            DLP.matchPattern(
                "",
                ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER
            )
        ),
        "now" to mutableListOf(
            DLP.matchPattern(
                "",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowHome
            ),
            DLP.startsWith(
                "search",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowSearch
            ),
            DLP.startsWith(
                "category",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowCategory
            ),
            DLP.startsWith("repurchase-page", ApplinkConstInternalTokopediaNow::REPURCHASE),
            DLP.matchPattern(
                "recipe/detail/{recipe_id}",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeDetail
            ),
            DLP.startsWith(
                "recipe/bookmarks",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeBookmark
            ),
            DLP.matchPattern(
                "recipe",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeHome
            ),
            DLP.startsWith(
                "recipe/search",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeSearch
            ),
            DLP.startsWith(
                "recipe/autocomplete",
                DeeplinkMapperTokopediaNow::getRegisteredNavigationTokopediaNowRecipeAutoComplete
            ),
            DLP.startsWith(
                "see-all-category",
                ApplinkConstInternalTokopediaNow::SEE_ALL_CATEGORY
            )
        ),
        "occ" to mutableListOf(
            DLP.goTo(DeeplinkMapperMarketplace::getRegisteredNavigationMarketplace)
        ),
        "official-store" to mutableListOf(
            DLP.goTo(DeeplinkMapperHome::getRegisteredNavigationHomeOfficialStore)
        ),
        "official-stores" to mutableListOf(
            DLP.goTo(DeeplinkMapperHome::getRegisteredNavigationHomeOfficialStore)
        ),
        "order" to mutableListOf(
            DLP.goTo(DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "order-details" to mutableListOf(
            DLP.startsWith(
                "umroh",
                DeeplinkMapperSalam::getRegisteredNavigationSalamUmrahOrderDetail
            )
        ),
        "order_list" to mutableListOf(
            DLP.goTo(DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "otp" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.COTP)
        ),
        "otp-verify" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.OTP_PUSH_NOTIF_RECEIVER)
        ),
        "ovoqrthanks" to mutableListOf(
            DLP.goTo(DeeplinkMapperFintech::getRegisteredNavigationForFintech)
        ),
        "p" to mutableListOf(
            DLP.goTo(DeeplinkMapperCategory::getRegisteredCategoryNavigation)
        ),
        "payment" to mutableListOf(
            DLP.matchPattern(
                "credit-card/add",
                ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            ),
            DLP.matchPattern("gopayKyc", ApplinkConstInternalPayment.GOPAY_KYC),
            DLP.matchPattern("thankyou", ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE)
        ),
        "people" to mutableListOf(
            DLP.matchPattern(
                "{user_id}",
                DeeplinkMapperContent::getProfileDeeplink
            ),
            DLP.matchPattern(
                "settings/{user_id}",
                DeeplinkMapperContent::getProfileDeeplink
            )
        ),
        "pesawat" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder),
            DLP.matchPattern("", ApplinkConstInternalTravel.DASHBOARD_FLIGHT)
        ),
        "pin-point-picker-result" to mutableListOf(
            DLP.goTo(DeeplinkMapperLogistic::getRegisteredNavigationPinpointWebview)
        ),
        "play" to mutableListOf(
            DLP.matchPattern("{channel_id}", DeeplinkMapperContent::getRegisteredNavigation)
        ),
        "play-broadcaster" to mutableListOf(
            DLP.goTo(DeeplinkMapperContent::getRegisteredNavigation)
        ),
        "play-notif-video" to mutableListOf(
            DLP.goTo(ApplinkConstInternalGlobal.YOUTUBE_VIDEO)
        ),
        "play-shorts" to mutableListOf(
            DLP.goTo(DeeplinkMapperContent::getRegisteredNavigation)
        ),
        "plus" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "post-atc" to mutableListOf(
            DLP.matchPattern("{productId}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.POST_ATC,
                    idList?.getOrNull(0) ?: ""
                )
            }
        ),
        "power_merchant" to mutableListOf(
            DLP.startsWith("subscribe", PowerMerchantDeepLinkMapper::getPowerMerchantAppLink),
            DLP.matchPattern(
                "benefit_package",
                ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE
            ),
            DLP.startsWith(
                "interrupt",
                PowerMerchantDeepLinkMapper::getInternalAppLinkPmProInterrupt
            )
        ),
        "privacy-center" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperUser::getRegisteredNavigationUser),
            DLP.matchPattern("dsar", DeeplinkMapperUser::getRegisteredNavigationUser)
        ),
        "product" to mutableListOf(
            DLP.matchPattern("edit/{product_id}") { _, _, _, idList ->
                DeepLinkMapperProductManage.getEditProductInternalAppLink(
                    idList?.getOrNull(0) ?: ""
                )
            },
            DLP.matchPattern(
                "{id}/review",
                DeeplinkMapperMerchant::getRegisteredNavigationProductDetailReview
            ),
            DLP.matchPattern(
                "{id}/review/gallery",
                DeeplinkMapperMerchant::getRegisteredNavigationProductDetailReviewGallery
            ),
            DLP.matchPattern(
                "{product_id}",
                DeeplinkMapperMarketplace::getTokopediaInternalProduct
            ),
            DLP.matchPattern("{product_id}/talk") { _, _, _, idList ->
                DeeplinkMapper.getRegisteredNavigationProductTalk(idList?.getOrNull(0))
            }
        ),
        "product-bundle" to mutableListOf(
            DLP.matchPattern("{product_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMechant.MERCHANT_PRODUCT_BUNDLE,
                    idList?.getOrNull(0)
                )
            }
        ),
        "product-edu" to mutableListOf(
            DLP.matchPattern("{type}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL_EDUCATIONAL,
                    idList?.lastOrNull() ?: ""
                )
            }
        ),
        "product-order-history" to mutableListOf(
            DLP.matchPattern("{shop_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.ORDER_HISTORY,
                    idList?.getOrNull(0)
                )
            },
            DLP.goTo(DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "product-review" to mutableListOf(
            DLP.startsWith(
                "create",
                DeeplinkMapperMerchant::getRegisteredNavigationProductReview
            ),
            DLP.matchPattern("bulk-create", ApplinkConstInternalMarketplace.BULK_CREATE_REVIEW)
        ),
        "productar" to mutableListOf(
            DLP.matchPattern("{product_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_AR, idList?.getOrNull(0))
            }
        ),
        "productpickerfromshop" to mutableListOf(
            DLP.goTo(DeeplinkMapperContent::getContentCreatePostDeepLink)
        ),
        "profilecompletion" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.PROFILE_COMPLETION)
        ),
        "promo" to mutableListOf(
            DLP.matchPattern("") { _, _, _, _ ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            },
            DLP.matchPattern("{slug}") { _, _, _, idList ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            }
        ),
        "promoNative" to mutableListOf(
            DLP.matchPattern("") { _, _, _, _ ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            }
        ),
        "recharge" to mutableListOf(
            DLP.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "register-init" to mutableListOf(
            DLP.goTo(ApplinkConstInternalUserPlatform.INIT_REGISTER)
        ),
        "registration" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalUserPlatform.INIT_REGISTER)
        ),
        "rekomendasi" to mutableListOf(
            DLP.goTo(DeeplinkMapperRecommendation::getRegisteredNavigationRecommendation)
        ),
        "resetpassword" to mutableListOf(
            DLP.goTo(ApplinkConstInternalUserPlatform.FORGOT_PASSWORD)
        ),
        "resolution" to mutableListOf(
            DLP.matchPattern(
                "success-create",
                ApplinkConstInternalOperational::buildApplinkResolution
            )
        ),
        "review" to mutableListOf(
            DLP.goTo(DeeplinkMapperMerchant::getRegisteredNavigationReputation)
        ),
        "review-reminder" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalSellerapp.REVIEW_REMINDER)
        ),
        "rewards" to mutableListOf(
            DLP.goTo(DeeplinkMapperPromo::getRegisteredNavigationTokopoints)
        ),
        "s" to mutableListOf(
            DLP.startsWith("umroh", DeeplinkMapperSalam::getRegisteredNavigationSalamUmrah)
        ),
        "saldo" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        ),
        "saldo-intro" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalGlobal.SALDO_INTRO)
        ),
        "scanqr" to mutableListOf(
            DLP.goTo(ApplinkConstInternalMarketplace.QR_SCANNEER)
        ),
        "search" to mutableListOf(
            DLP.goTo(DeeplinkMapperSearch::getRegisteredNavigationSearch)
        ),
        "search-autocomplete" to mutableListOf(
            DLP.goTo(DeeplinkMapperSearch::getRegisteredNavigationSearch)
        ),
        "seller" to mutableListOf(
            DLP.startsWith(
                "product/manage",
                DeepLinkMapperProductManage::getProductListInternalAppLink
            ),
            DLP.startsWith("order", DeeplinkMapperOrder::getRegisteredNavigationOrder),
            DLP.startsWith(
                "reschedulepickup",
                DeeplinkMapperLogistic::getReschedulePickupDeeplink
            ),
            DLP.startsWith("new-order", AppLinkMapperSellerHome::getSomNewOrderAppLink),
            DLP.startsWith("ready-to-ship", AppLinkMapperSellerHome::getSomReadyToShipAppLink),
            DLP.startsWith(
                "delivered",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerDelivered
            ),
            DLP.startsWith(
                "waitingpickup",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerWaitingPickup
            ),
            DLP.startsWith(
                "waitingawb",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerWaitingAwb
            ),
            DLP.startsWith(
                "awbinvalid",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerAwbInvalid
            ),
            DLP.startsWith(
                "awbchange",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerAwbChange
            ),
            DLP.startsWith(
                "retur",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerRetur
            ),
            DLP.startsWith(
                "complaint",
                DeeplinkMapperOrder::getRegisteredNavigationMainAppSellerComplaint
            ),
            DLP.startsWith("history", AppLinkMapperSellerHome::getSomAllOrderAppLink),
            DLP.startsWith("shipped", AppLinkMapperSellerHome::getSomShippedAppLink),
            DLP.startsWith("shipment", AppLinkMapperSellerHome::getSomReadyToShipAppLink),
            DLP.startsWith("finished", AppLinkMapperSellerHome::getSomDoneAppLink),
            DLP.startsWith("cancelled", AppLinkMapperSellerHome::getSomCancelledAppLink),
            DLP.startsWith(
                "cancellationrequest",
                AppLinkMapperSellerHome::getSomCancellationRequestAppLink
            ),
            DLP.startsWith("status", AppLinkMapperSellerHome::getSomShippedAppLink),
            DLP.matchPattern(
                "setting/shipping-editor",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            ),
            DLP.matchPattern(
                "setting/custom-product-logistic",
                ApplinkConstInternalLogistic.CUSTOM_PRODUCT_LOGISTIC
            ),
            DLP.matchPattern(
                "setting/cod-activation",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_COD
            ),
            DLP.matchPattern(
                "setting/shop-address",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS
            ),
            DLP.matchPattern(
                "setting/operational-hours",
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS
            ),
            DLP.startsWith("seller-center", DeeplinkMapperMerchant::getRegisteredSellerCenter)
        ),
        "seller-review-detail" to mutableListOf(
            DLP.goTo(ApplinkConstInternalMarketplace.SELLER_REVIEW_DETAIL)
        ),
        "sellerinfo" to mutableListOf(
            DLP.startsWith("detail", DeeplinkMapperMerchant::getSellerInfoDetailApplink)
        ),
        "setting" to mutableListOf(
            DLP.startsWith("editaddress", DeeplinkMapperLogistic::getEditAddressDeeplink),
            DLP.matchPattern("shop/note", ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES),
            DLP.matchPattern("shop/info", ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO),
            DLP.matchPattern("profile", DeeplinkMapperUser::getRegisteredNavigationUser),
            DLP.matchPattern("address", ApplinkConstInternalLogistic.MANAGE_ADDRESS),
            DLP.matchPattern("payment", ApplinkConstInternalUserPlatform.PAYMENT_SETTING),
            DLP.matchPattern("account", ApplinkConstInternalUserPlatform.ACCOUNT_SETTING)
        ),
        "settings" to mutableListOf(
            DLP.matchPattern(
                "notification",
                ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            ),
            DLP.matchPattern("bankaccount", ApplinkConstInternalGlobal.SETTING_BANK),
            DLP.matchPattern("haspassword", ApplinkConstInternalUserPlatform.HAS_PASSWORD)
        ),
        "share_address" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperLogistic::getRegisteredNavigationShareAddress)
        ),
        "sharing" to mutableListOf(
            DLP.goTo(DeeplinkMapperExternal::getRegisteredNavigation)
        ),
        "shipping" to mutableListOf(
            DLP.matchPattern(
                "tracking/{order_id}",
                DeeplinkMapperLogistic::getRegisteredNavigationOrder
            ),
            DLP.matchPattern(
                "pod/{order_id}",
                DeeplinkMapperLogistic::getRegisteredNavigationPod
            )
        ),
        "shop" to mutableListOf(
            DLP.matchPattern("{shop_id}/etalase-list") { _, _, _, idList ->
                DeepLinkMapperEtalase.getEtalaseListInternalAppLink(idList?.getOrNull(0) ?: "")
            },
            DLP.matchPattern("{shop_id}/talk") { _, _, _, idList ->
                DeeplinkMapper.getRegisteredNavigationShopTalk(idList?.getOrNull(0))
            },
            DLP.matchPattern("{shop_id}") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/home") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_HOME,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/product") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/feed") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_FEED,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/review") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_REVIEW,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/follower") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_FAVOURITE_LIST_WITH_SHOP_ID,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/settings") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_SETTING_CUSTOMER_APP_WITH_SHOP_ID,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/info") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_INFO,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id}/note") { ctx, uri, deeplink, idList ->
                DeeplinkMapperMarketplace.getShopPageInternalAppLink(
                    ctx,
                    uri,
                    deeplink,
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.SHOP_PAGE_NOTE,
                        idList?.getOrNull(0)
                    ),
                    idList?.getOrNull(0).orEmpty()
                )
            },
            DLP.matchPattern("{shop_id_or_domain}/etalase/{etalase_id_or_alias}") { ctx, uri, deeplink, idList ->
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
            DLP.matchPattern("{shop_id}/operational-hour") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_OPERATIONAL_HOUR_BOTTOM_SHEET,
                    idList?.getOrNull(0)
                )
            },
            DLP.matchPattern("{shop_id}/voucher/{voucher_id}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.SHOP_MVC_LOCKED_TO_PRODUCT,
                    idList?.getOrNull(0),
                    idList?.getOrNull(1)
                )
            }
        ),
        "shop-admin" to mutableListOf(
            DLP.matchPattern("invitation-page", ApplinkConstInternalMarketplace.ADMIN_INVITATION),
            DLP.matchPattern(
                "redirection-page",
                ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
            ),
            DLP.startsWith(
                "accepted-page",
                ShopAdminDeepLinkMapper::getInternalAppLinkAdminAccepted
            )
        ),
        "shop-nib" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalMechant.SHOP_NIB_CUSTOMER_APP)
        ),
        "shop-penalty" to mutableListOf(
            DLP.matchPattern("") { context, _, _, _ ->
                ShopScoreDeepLinkMapper.getInternalApplinkPenalty(context)
            }
        ),
        "shop-penalty-detail" to mutableListOf(
            DLP.matchPattern("", ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL)
        ),
        "shop-score-detail" to mutableListOf(
            DLP.goTo(ShopScoreDeepLinkMapper::getInternalAppLinkShopScore)
        ),
        "snapshot" to mutableListOf(
            DLP.startsWith("order", DeeplinkMapperOrder::getSnapshotOrderInternalAppLink)
        ),
        "talk" to mutableListOf(
            DLP.goTo(DeeplinkMapper::getRegisteredNavigationTalk)
        ),
        "telephony-masking" to mutableListOf(
            DLP.goTo(ApplinkConstInternalUserPlatform.TELEPHONY_MASKING)
        ),
        "telkomselomni" to mutableListOf(
            DLP.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "tokochat" to mutableListOf(
            DLP.goTo(DeeplinkMapperCommunication::getRegisteredNavigationTokoChat)
        ),
        "tokopoints" to mutableListOf(
            DLP.goTo(DeeplinkMapperPromo::getRegisteredNavigationTokopoints)
        ),
        "topads" to mutableListOf(
            DLP.startsWith(
                "create-manual-ads",
                ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS
            )
        ),
        "topchat" to mutableListOf(
            DLP.goTo(DeeplinkMapper::getRegisteredNavigationTopChat)
        ),
        "topchatold" to mutableListOf(
            DLP.goTo(DeeplinkMapper::getRegisteredNavigationTopChat)
        ),
        "travelent" to mutableListOf(
            DLP.matchPattern("order", DeeplinkMapperUoh::getRegisteredNavigationUohOrder)
        ),
        "travelentertainment" to mutableListOf(
            DLP.goTo(DeeplinkMapperDigital::getRegisteredNavigationDigital)
        ),
        "universal-page" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperSearch::getRegisteredNavigationSearch)
        ),
        "user-identification-only" to mutableListOf(
            DLP.goTo(DeeplinkMapperUser::getRegisteredUserNavigation)
        ),
        "webview" to mutableListOf(
            DLP.goTo(ApplinkConstInternalGlobal.WEBVIEW_BASE)
        ),
        "webviewbackhome" to mutableListOf(
            DLP.goTo(ApplinkConstInternalGlobal.WEBVIEW_BACK_HOME)
        ),
        "webviewdownload" to mutableListOf(
            DLP.goTo(ApplinkConstInternalGlobal.WEBVIEW_DOWNLOAD)
        ),
        "wishlist" to mutableListOf(
            DLP.matchPattern("", DeeplinkMapperWishlist::getRegisteredNavigationWishlist),
            DLP.matchPattern("collection/{collection_id}", DeeplinkMapperMarketplace::getRegisteredWishlistCollectionDetail)
        ),
        "www.tokopedia.com" to mutableListOf(
            DLP.goTo(DeeplinkMapperContent::getWebHostWebViewLink)
        ),
        "youtube-player" to mutableListOf(
            DLP.matchPattern("{video_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalGlobal.YOUTUBE_PLAYER, idList?.getOrNull(0))
            }
        )

    )
}
