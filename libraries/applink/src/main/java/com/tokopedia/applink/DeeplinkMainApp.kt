package com.tokopedia.applink

import android.content.Context
import android.net.Uri
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
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
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
            DLP.goTo { deeplink: String ->
                DeeplinkMapperAccount.getAccountInternalApplink(deeplink)
            }
        ),
        "add-phone" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            }
        ),
        "add-pin-onboarding" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            }
        ),
        "addname" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.MANAGE_NAME
            }
        ),
        "addon" to mutableListOf(
            DLP.matchPattern("{addon_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMechant.MERCHANT_ADDON, idList?.getOrNull(0))
            }
        ),
        "affiliate" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("help") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("transaction-history") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("edu-page") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("shoplist-dipromosikan-affiliate") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("discopage-list") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("promosikan") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("onboarding") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.matchPattern("performa") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationAffiliate(deeplink)
            },
            DLP.startsWith("create_post_v2") { deeplink: String ->
                DeeplinkMapperContent.getContentCreatePostDeepLink(deeplink)
            }
        ),
        "amp" to mutableListOf(
            DLP.startsWith("find") { deeplink: String ->
                DeepLinkMapperFind.getRegisteredFind(deeplink)
            }
        ),
        "belanja" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "browser" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.BROWSER }
        ),
        "buka-toko-online-gratis" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION
            }
        ),
        "buyer" to mutableListOf(
            DLP.matchPattern("payment") { _: String ->
                ApplinkConstInternalPayment.PMS_PAYMENT_LIST
            },
            DLP.startsWith("cancellationrequest") { _: String ->
                DeeplinkMapperOrder.getBuyerCancellationRequestInternalAppLink()
            },
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
            DLP.goTo { deeplink: String ->
                DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace(deeplink)
            }
        ),
        "catalog" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationCatalog(deeplink)
            }
        ),
        "catalog-library" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationCategory(deeplink)
            }
        ),
        "category" to mutableListOf(
            DLP.startsWith("tradein") { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredTradeinNavigation(deeplink)
            },
            DLP.goTo { uri: Uri ->
                DeeplinkMapperCategory.getRegisteredCategoryNavigation(uri)
            }
        ),
        "category-explore" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationExploreCategory(deeplink)
            }
        ),
        "changeinactivephone" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            }
        ),
        "chat" to mutableListOf(
            DLP.matchPattern("settings/templatechat") { _: String ->
                ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE
            }
        ),
        "chatbot" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperChatbot.getChatbotDeeplink(deeplink)
            }
        ),
        "chatsettings" to mutableListOf(
            DLP.matchPattern("bubble-activation") { deeplink: String ->
                DeeplinkMapperCommunication.getRegisteredNavigationBubbleActivation(deeplink)
            }
        ),
        "checkout" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace(deeplink)
            }
        ),
        "contact-us" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContactUs.getNavigationContactUs(deeplink)
            }
        ),
        "content" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContent.getNavContentFromAppLink(deeplink)
            }
        ),
        "customercare" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
            }
        ),
        "deals" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperDeals.getRegisteredNavigationDeals(context, deeplink)
            }
        ),
        "digital" to mutableListOf(
            DLP.startsWith("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperDigital.getRegisteredNavigationDigital(context, deeplink)
            }
        ),
        "dilayani-tokopedia" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalDilayaniTokopedia.HOME
            }
        ),
        "discovery" to mutableListOf(
            DLP.goTo { deeplink: String ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(deeplink)
            }
        ),
        "epharmacy" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperCategory.getRegisteredNavigationCategory(deeplink)
            }
        ),
        "events" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperEntertainment.getRegisteredNavigationEvents(context, deeplink)
            }
        ),
        "explicit-profile" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.EXPLICIT_PROFILE
            }
        ),
        "feed" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContent.getNavContentFromAppLink(deeplink)
            }
        ),
        "feedcommunicationdetail" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeepLinkMapperFeed.getRegisteredFeed(deeplink)
            }
        ),
        "feedplaylivedetail" to mutableListOf(
            DLP.goTo { _: String ->
                ApplinkConstInternalFeed.INTERNAL_PLAY_LIVE_DETAILS
            }
        ),
        "find" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeepLinkMapperFind.getRegisteredFind(deeplink)
            }
        ),
        "fintech" to mutableListOf(
            DLP.startsWith("paylater") { _: String ->
                ApplinkConstInternalFintech.PAYLATER
            },
            DLP.startsWith("activate_gopay") { _: String ->
                ApplinkConstInternalFintech.ACTIVATE_GOPAY
            },
            DLP.startsWith("opt-checkout") { _: String ->
                ApplinkConstInternalFintech.OCC_CHECKOUT
            },
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
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.goTo { context: Context, uri: Uri ->
                DeeplinkMapperTokoFood.mapperInternalApplinkTokoFood(context, uri)
            }
        ),
        "gamification" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperGamification.getGamificationDeeplink(deeplink)
            }
        ),
        "gamification2" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperGamification.getGamificationDeeplink(deeplink)
            }
        ),
        "gamification_gift_60s" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperGamification.getGamificationDeeplink(deeplink)
            }
        ),
        "gamification_gift_daily" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperGamification.getGamificationDeeplink(deeplink)
            }
        ),
        "giftcards" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "gifting" to mutableListOf(
            DLP.matchPattern("{addon_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMechant.MERCHANT_GIFTING, idList?.getOrNull(0))
            }
        ),
        "gofood" to mutableListOf(
            DLP.goTo { context: Context, uri: Uri ->
                DeeplinkMapperTokoFood.mapperInternalApplinkTokoFood(context, uri)
            }
        ),
        "gojek-account-link" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW
            }
        ),
        "gold-merchant-statistic-dashboard" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace(deeplink)
            }
        ),
        "goto-kyc" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            }
        ),
        "home" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperHome.getRegisteredNavigationHome(context, deeplink)
            }
        ),
        "hot" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperHotlist.getRegisteredHotlist(deeplink)
            }
        ),
        "hotel" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperTravel.getRegisteredNavigationTravel(context, deeplink)
            }
        ),
        "howtopay" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalPayment.INTERNAL_HOW_TO_PAY
            }
        ),
        "image-picker" to mutableListOf(
            DLP.matchPattern("v2") { deeplink: String ->
                DeeplinkMapperImagePicker.getImagePickerV2Deeplink(deeplink)
            }
        ),
        "inbox" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperCommunication.getRegisteredNavigationInbox(context, deeplink)
            }
        ),
        "inputinactivenumber" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            }
        ),
        "insurance" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "internal-feedback" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalGlobal.FEEDBACK_FORM
            }
        ),
        "jump" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperHome.getRegisteredExplore(deeplink)
            }
        ),
        "kereta" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "kyc" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.KYC_INFO_BASE
            }
        ),
        "kyc-form" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalUserPlatform.KYC_FORM_BASE }
        ),
        "layanan-finansial" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperFintech.getRegisteredNavigationForLayanan(deeplink)
            }
        ),
        "login" to mutableListOf(
            DLP.startsWith("qr") { _: String ->
                ApplinkConstInternalUserPlatform.QR_LOGIN
            },
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.LOGIN
            }
        ),
        "marketplace" to mutableListOf(
            DLP.startsWith("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.startsWith("buyer-order-extension") { _: String ->
                ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION
            },
            DLP.startsWith("buyer-partial-order-fulfillment") { _: String ->
                ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_PARTIAL_ORDER_FULFILLMENT
            },
            DLP.startsWith("onboarding") { _: String ->
                ApplinkConstInternalMarketplace.ONBOARDING
            }
        ),
        "medali" to mutableListOf(
            DLP.matchPattern(ApplinkConst.ScpRewards.CELEBRATION_BASE) { _, uri, _, _ ->
                getInternalDeeplinkForScpCelebration(
                    uri
                )
            },
            DLP.startsWith(ApplinkConst.ScpRewards.CELEBRATION_BOTTOMSHEET) { _, uri, _, _ ->
                getCelebrationBottomsheetDeeplink(
                    uri
                )
            },
            DLP.startsWith(ApplinkConst.ScpRewards.SCP_TOASTER) { ctx, uri, _, _ ->
                invokeScpToasterUniversalAppLink(
                    ctx,
                    uri
                )
            },
            DLP.startsWith(ApplinkConst.ScpRewards.MEDAL_DETAIL_BASE) { _, uri, _, _ ->
                getInternalDeeplinkForScpMedalDetail(
                    uri
                )
            },
            DLP.startsWith(ApplinkConst.ScpRewards.SEE_MORE_MEDAL) { _, uri, _, _ ->
                getInternalDeeplinkForScpMedalCabinetSeeMore(
                    uri
                )
            },
            DLP.matchPattern("") { _, uri, _, _ -> getInternalDeeplinkForScpMedalCabinet(uri) }
        ),
        "media-editor" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalMedia.INTERNAL_MEDIA_EDITOR
            }
        ),
        "media-picker" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
            }
        ),
        "media-picker-preview" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER_PREVIEW
            }
        ),
        "merchant-voucher" to mutableListOf(
            DLP.matchPattern("list") { _: String ->
                ApplinkConstInternalSellerapp.SELLER_MVC_LIST_ACTIVE
            }
        ),
        "modaltoko" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "money_in" to mutableListOf(
            DLP.startsWith("device_validation") { deeplink: String ->
                DeeplinkMapperMoneyIn.getRegisteredNavigationMoneyIn(deeplink)
            }
        ),
        "my-shop" to mutableListOf(
            DLP.matchPattern("etalase/list") { _: String ->
                ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
            }
        ),
        "navigation" to mutableListOf(
            DLP.matchPattern("main") { _: String ->
                ApplinkConsInternalNavigation.MAIN_NAVIGATION
            }
        ),
        "new-wishlist" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperWishlist.getRegisteredNavigationWishlist()
            }
        ),
        "notif-center" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperInbox.getRegisteredNavigationNotifcenter()
            }
        ),
        "notification" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperInbox.getRegisteredNavigationNotifcenter()
            }
        ),
        "notification-troubleshooter" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER
            }
        ),
        "now" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowHome(deeplink)
            },
            DLP.startsWith("search") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowSearch(deeplink)
            },
            DLP.startsWith("category") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowCategory(deeplink)
            },
            DLP.startsWith("repurchase-page") { _: String ->
                ApplinkConstInternalTokopediaNow.REPURCHASE
            },
            DLP.matchPattern("recipe/detail/{recipe_id}") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeDetail(deeplink)
            },
            DLP.startsWith("recipe/bookmarks") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeBookmark(deeplink)
            },
            DLP.matchPattern("recipe") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeHome(deeplink)
            },
            DLP.startsWith("recipe/search") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeSearch(deeplink)
            },
            DLP.startsWith("recipe/autocomplete") { deeplink: String ->
                DeeplinkMapperTokopediaNow.getRegisteredNavigationTokopediaNowRecipeAutoComplete(deeplink)
            },
            DLP.startsWith("see-all-category") { _: String ->
                ApplinkConstInternalTokopediaNow.SEE_ALL_CATEGORY
            }
        ),
        "occ" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace(deeplink)
            }
        ),
        "official-store" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperHome.getRegisteredNavigationHomeOfficialStore(deeplink)
            }
        ),
        "official-stores" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperHome.getRegisteredNavigationHomeOfficialStore(deeplink)
            }
        ),
        "order" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "order-details" to mutableListOf(
            DLP.startsWith("umroh") { deeplink: String ->
                DeeplinkMapperSalam.getRegisteredNavigationSalamUmrahOrderDetail(deeplink)
            }
        ),
        "order_list" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "otp" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.COTP
            }
        ),
        "otp-verify" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.OTP_PUSH_NOTIF_RECEIVER
            }
        ),
        "ovoqrthanks" to mutableListOf(
            DLP.goTo { uri: Uri ->
                DeeplinkMapperFintech.getRegisteredNavigationForFintech(uri)
            }
        ),
        "p" to mutableListOf(
            DLP.goTo { uri: Uri ->
                DeeplinkMapperCategory.getRegisteredCategoryNavigation(uri)
            }
        ),
        "payment" to mutableListOf(
            DLP.matchPattern("credit-card/add") { _: String ->
                ApplinkConstInternalPayment.PAYMENT_ADD_CREDIT_CARD
            },
            DLP.matchPattern("gopayKyc") { _: String ->
                ApplinkConstInternalPayment.GOPAY_KYC
            },
            DLP.matchPattern("thankyou") { _: String ->
                ApplinkConstInternalPayment.PAYMENT_THANK_YOU_PAGE
            }
        ),
        "people" to mutableListOf(
            DLP.matchPattern("{user_id}") { deeplink: String ->
                DeeplinkMapperContent.getProfileDeeplink(deeplink)
            },
            DLP.matchPattern("settings/{user_id}") { deeplink: String ->
                DeeplinkMapperContent.getProfileDeeplink(deeplink)
            }
        ),
        "pesawat" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            },
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalTravel.DASHBOARD_FLIGHT
            }
        ),
        "pin-point-picker-result" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperLogistic.getRegisteredNavigationPinpointWebview(deeplink)
            }
        ),
        "play" to mutableListOf(
            DLP.matchPattern("{channel_id}") { deeplink: String ->
                DeeplinkMapperContent.getRegisteredNavigation(deeplink)
            }
        ),
        "play-broadcaster" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContent.getRegisteredNavigation(deeplink)
            }
        ),
        "play-notif-video" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.YOUTUBE_VIDEO }
        ),
        "play-shorts" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContent.getRegisteredNavigation(deeplink)
            }
        ),
        "plus" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
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
            DLP.startsWith("subscribe") { context: Context, uri: Uri ->
                PowerMerchantDeepLinkMapper.getPowerMerchantAppLink(context, uri)
            },
            DLP.matchPattern("benefit_package") { _: String ->
                ApplinkConstInternalMarketplace.PM_BENEFIT_PACKAGE
            },
            DLP.startsWith("interrupt") { deeplink: String ->
                PowerMerchantDeepLinkMapper.getInternalAppLinkPmProInterrupt(deeplink)
            }
        ),
        "privacy-center" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            },
            DLP.matchPattern("dsar") { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            }
        ),
        "product" to mutableListOf(
            DLP.matchPattern("edit/{product_id}") { _, _, _, idList ->
                DeepLinkMapperProductManage.getEditProductInternalAppLink(
                    idList?.getOrNull(0) ?: ""
                )
            },
            DLP.matchPattern("{id}/review") { uri: Uri ->
                DeeplinkMapperMerchant.getRegisteredNavigationProductDetailReview(uri)
            },
            DLP.matchPattern("{id}/review/gallery") { uri: Uri ->
                DeeplinkMapperMerchant.getRegisteredNavigationProductDetailReviewGallery(uri)
            },
            DLP.matchPattern("{product_id}") { uri: Uri, idList: List<String>? ->
                DeeplinkMapperMarketplace.getTokopediaInternalProduct(uri, idList)
            },
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
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "product-review" to mutableListOf(
            DLP.startsWith("create") { uri: Uri ->
                DeeplinkMapperMerchant.getRegisteredNavigationProductReview(uri)
            },
            DLP.matchPattern("bulk-create") { _: String ->
                ApplinkConstInternalMarketplace.BULK_CREATE_REVIEW
            }
        ),
        "productar" to mutableListOf(
            DLP.matchPattern("{product_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_AR, idList?.getOrNull(0))
            }
        ),
        "productpickerfromshop" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContent.getContentCreatePostDeepLink(deeplink)
            }
        ),
        "profilecompletion" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.PROFILE_COMPLETION
            }
        ),
        "promo" to mutableListOf(
            DLP.matchPattern("") { _, _, _, _ ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            },
            DLP.matchPattern("{slug}") { _, _, _, idList ->
                UriUtil.buildUri(
                    ApplinkConstInternalPromo.PROMO_DETAIL,
                    idList?.getOrNull(0)
                )
            }
        ),
        "promoNative" to mutableListOf(
            DLP.matchPattern("") { _, _, _, _ ->
                ApplinkConstInternalCategory.getDiscoveryDeeplink(ApplinkConst.DISCOVERY_DEALS)
            }
        ),
        "recharge" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperDigital.getRegisteredNavigationDigital(context, deeplink)
            }
        ),
        "register-init" to mutableListOf(
            DLP.goTo { _: String ->
                ApplinkConstInternalUserPlatform.INIT_REGISTER
            }
        ),
        "registration" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalUserPlatform.INIT_REGISTER
            }
        ),
        "rekomendasi" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperRecommendation.getRegisteredNavigationRecommendation(deeplink)
            }
        ),
        "resetpassword" to mutableListOf(
            DLP.goTo { _: String ->
                ApplinkConstInternalUserPlatform.FORGOT_PASSWORD
            }
        ),
        "resolution" to mutableListOf(
            DLP.matchPattern("success-create") { uri: Uri ->
                ApplinkConstInternalOperational.buildApplinkResolution(uri)
            }
        ),
        "review" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperMerchant.getRegisteredNavigationReputation(deeplink)
            }
        ),
        "review-reminder" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalSellerapp.REVIEW_REMINDER
            }
        ),
        "rewards" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperPromo.getRegisteredNavigationTokopoints(deeplink)
            }
        ),
        "s" to mutableListOf(
            DLP.startsWith("umroh") { deeplink: String ->
                DeeplinkMapperSalam.getRegisteredNavigationSalamUmrah(deeplink)
            }
        ),
        "saldo" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalGlobal.SALDO_DEPOSIT
            }
        ),
        "saldo-intro" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalGlobal.SALDO_INTRO
            }
        ),
        "scanqr" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalMarketplace.QR_SCANNEER }
        ),
        "search" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperSearch.getRegisteredNavigationSearch(deeplink)
            }
        ),
        "search-autocomplete" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperSearch.getRegisteredNavigationSearch(deeplink)
            }
        ),
        "seller" to mutableListOf(
            DLP.startsWith("product/manage") { deeplink: String ->
                DeepLinkMapperProductManage.getProductListInternalAppLink(deeplink)
            },
            DLP.startsWith("order") { context: Context, uri: Uri, deeplink: String ->
                DeeplinkMapperOrder.getRegisteredNavigationOrder(context, uri, deeplink)
            },
            DLP.startsWith("reschedulepickup") { uri: Uri ->
                DeeplinkMapperLogistic.getReschedulePickupDeeplink(uri)
            },
            DLP.startsWith("new-order") { uri: Uri ->
                AppLinkMapperSellerHome.getSomNewOrderAppLink(uri)
            },
            DLP.startsWith("ready-to-ship") { uri: Uri ->
                AppLinkMapperSellerHome.getSomReadyToShipAppLink(uri)
            },
            DLP.startsWith("delivered") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerDelivered()
            },
            DLP.startsWith("waitingpickup") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingPickup()
            },
            DLP.startsWith("waitingawb") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerWaitingAwb()
            },
            DLP.startsWith("awbinvalid") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbInvalid()
            },
            DLP.startsWith("awbchange") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerAwbChange()
            },
            DLP.startsWith("retur") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerRetur()
            },
            DLP.startsWith("complaint") { _: String ->
                DeeplinkMapperOrder.getRegisteredNavigationMainAppSellerComplaint()
            },
            DLP.startsWith("history") { uri: Uri ->
                AppLinkMapperSellerHome.getSomAllOrderAppLink(uri)
            },
            DLP.startsWith("shipped") { uri: Uri ->
                AppLinkMapperSellerHome.getSomShippedAppLink(uri)
            },
            DLP.startsWith("shipment") { uri: Uri ->
                AppLinkMapperSellerHome.getSomReadyToShipAppLink(uri)
            },
            DLP.startsWith("finished") { uri: Uri ->
                AppLinkMapperSellerHome.getSomDoneAppLink(uri)
            },
            DLP.startsWith("cancelled") { uri: Uri ->
                AppLinkMapperSellerHome.getSomCancelledAppLink(uri)
            },
            DLP.startsWith("cancellationrequest") { uri: Uri ->
                AppLinkMapperSellerHome.getSomCancellationRequestAppLink(uri)
            },
            DLP.startsWith("status") { uri: Uri ->
                AppLinkMapperSellerHome.getSomShippedAppLink(uri)
            },
            DLP.matchPattern("setting/shipping-editor") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
            },
            DLP.matchPattern("setting/custom-product-logistic") { _: String ->
                ApplinkConstInternalLogistic.CUSTOM_PRODUCT_LOGISTIC
            },
            DLP.matchPattern("setting/cod-activation") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_COD
            },
            DLP.matchPattern("setting/shop-address") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS
            },
            DLP.matchPattern("setting/operational-hours") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS
            },
            DLP.startsWith("seller-center") { _: String ->
                DeeplinkMapperMerchant.getRegisteredSellerCenter()
            }
        ),
        "seller-review-detail" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalMarketplace.SELLER_REVIEW_DETAIL }
        ),
        "sellerinfo" to mutableListOf(
            DLP.startsWith("detail") { uri: Uri ->
                DeeplinkMapperMerchant.getSellerInfoDetailApplink(uri)
            }
        ),
        "setting" to mutableListOf(
            DLP.startsWith("editaddress") { deeplink: String ->
                DeeplinkMapperLogistic.getEditAddressDeeplink(deeplink)
            },
            DLP.matchPattern("shop/note") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES
            },
            DLP.matchPattern("shop/info") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO
            },
            DLP.matchPattern("profile") { deeplink: String ->
                DeeplinkMapperUser.getRegisteredNavigationUser(deeplink)
            },
            DLP.matchPattern("address") { _: String ->
                ApplinkConstInternalLogistic.MANAGE_ADDRESS
            },
            DLP.matchPattern("payment") { _: String ->
                ApplinkConstInternalUserPlatform.PAYMENT_SETTING
            },
            DLP.matchPattern("account") { _: String ->
                ApplinkConstInternalUserPlatform.ACCOUNT_SETTING
            }
        ),
        "settings" to mutableListOf(
            DLP.matchPattern("notification") { _: String ->
                ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            },
            DLP.matchPattern("bankaccount") { _: String ->
                ApplinkConstInternalGlobal.SETTING_BANK
            },
            DLP.matchPattern("haspassword") { _: String ->
                ApplinkConstInternalUserPlatform.HAS_PASSWORD
            }
        ),
        "share_address" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperLogistic.getRegisteredNavigationShareAddress(deeplink)
            }
        ),
        "sharing" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperExternal.getRegisteredNavigation(deeplink)
            }
        ),
        "shipping" to mutableListOf(
            DLP.matchPattern("tracking/{order_id}") { deeplink: String ->
                DeeplinkMapperLogistic.getRegisteredNavigationOrder(deeplink)
            },
            DLP.matchPattern("pod/{order_id}") { deeplink: String ->
                DeeplinkMapperLogistic.getRegisteredNavigationPod(deeplink)
            }
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
            DLP.matchPattern("invitation-page") { _: String ->
                ApplinkConstInternalMarketplace.ADMIN_INVITATION
            },
            DLP.matchPattern("redirection-page") { _: String ->
                ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
            },
            DLP.startsWith("accepted-page") { uri: Uri ->
                ShopAdminDeepLinkMapper.getInternalAppLinkAdminAccepted(uri)
            }
        ),
        "shop-nib" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalMechant.SHOP_NIB_CUSTOMER_APP
            }
        ),
        "shop-penalty" to mutableListOf(
            DLP.matchPattern("") { context, _, _, _ ->
                ShopScoreDeepLinkMapper.getInternalApplinkPenalty(context)
            }
        ),
        "shop-penalty-detail" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL
            }
        ),
        "shop-score-detail" to mutableListOf(
            DLP.goTo { uri: Uri ->
                ShopScoreDeepLinkMapper.getInternalAppLinkShopScore(uri)
            }
        ),
        "snapshot" to mutableListOf(
            DLP.startsWith("order") { deeplink: String ->
                DeeplinkMapperOrder.getSnapshotOrderInternalAppLink(deeplink)
            }
        ),
        "talk" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapper.getRegisteredNavigationTalk(deeplink)
            }
        ),
        "telephony-masking" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalUserPlatform.TELEPHONY_MASKING }
        ),
        "telkomselomni" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperDigital.getRegisteredNavigationDigital(context, deeplink)
            }
        ),
        "tokochat" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperCommunication.getRegisteredNavigationTokoChat(context, deeplink)
            }
        ),
        "tokopoints" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperPromo.getRegisteredNavigationTokopoints(deeplink)
            }
        ),
        "topads" to mutableListOf(
            DLP.startsWith("create-manual-ads") { _: String ->
                ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS
            }
        ),
        "topchat" to mutableListOf(
            DLP.goTo { uri: Uri, deeplink: String ->
                DeeplinkMapper.getRegisteredNavigationTopChat(uri, deeplink)
            }
        ),
        "topchatold" to mutableListOf(
            DLP.goTo { uri: Uri, deeplink: String ->
                DeeplinkMapper.getRegisteredNavigationTopChat(uri, deeplink)
            }
        ),
        "travelent" to mutableListOf(
            DLP.matchPattern("order") { context: Context, deeplink: String ->
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
            }
        ),
        "travelentertainment" to mutableListOf(
            DLP.goTo { context: Context, deeplink: String ->
                DeeplinkMapperDigital.getRegisteredNavigationDigital(context, deeplink)
            }
        ),
        "universal-page" to mutableListOf(
            DLP.matchPattern("") { deeplink: String ->
                DeeplinkMapperSearch.getRegisteredNavigationSearch(deeplink)
            }
        ),
        "user-identification-only" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperUser.getRegisteredUserNavigation(deeplink)
            }
        ),
        "webview" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.WEBVIEW_BASE }
        ),
        "webviewbackhome" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.WEBVIEW_BACK_HOME }
        ),
        "webviewdownload" to mutableListOf(
            DLP.goToLink { ApplinkConstInternalGlobal.WEBVIEW_DOWNLOAD }
        ),
        "wishlist" to mutableListOf(
            DLP.matchPattern("") { _: String ->
                DeeplinkMapperWishlist.getRegisteredNavigationWishlist()
            },
            DLP.matchPattern("collection/{collection_id}") { uri: Uri, idList: List<String>? ->
                DeeplinkMapperMarketplace.getRegisteredWishlistCollectionDetail(uri, idList)
            }
        ),
        "www.tokopedia.com" to mutableListOf(
            DLP.goTo { deeplink: String ->
                DeeplinkMapperContent.getWebHostWebViewLink(deeplink)
            }
        ),
        "youtube-player" to mutableListOf(
            DLP.matchPattern("{video_id}") { _, _, _, idList ->
                UriUtil.buildUri(ApplinkConstInternalGlobal.YOUTUBE_PLAYER, idList?.getOrNull(0))
            }
        )

    )
}
