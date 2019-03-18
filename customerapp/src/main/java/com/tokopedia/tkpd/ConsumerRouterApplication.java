package com.tokopedia.tkpd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.crashlytics.android.Crashlytics;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionDataProvider;
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.affiliate.AffiliateRouter;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper;
import com.tokopedia.analytics.mapper.TkpdAppsFlyerRouter;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkDelegate;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.ApplinkUnsupported;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.browse.common.DigitalBrowseRouter;
import com.tokopedia.cacheapi.domain.interactor.CacheApiClearAllUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.common.IndiSession;
import com.tokopedia.changepassword.ChangePasswordRouter;
import com.tokopedia.changephonenumber.ChangePhoneNumberRouter;
import com.tokopedia.changephonenumber.view.activity.ChangePhoneNumberWarningActivity;
import com.tokopedia.chatbot.ChatbotRouter;
import com.tokopedia.checkout.CartConstant;
import com.tokopedia.checkout.domain.usecase.AddToCartUseCase;
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.feature.cartlist.CartActivity;
import com.tokopedia.checkout.view.feature.cartlist.CartFragment;
import com.tokopedia.checkout.view.feature.emptycart.EmptyCartFragment;
import com.tokopedia.checkout.view.feature.shipment.ShipmentActivity;
import com.tokopedia.cod.view.CodActivity;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.createticket.activity.ContactUsActivity;
import com.tokopedia.contactus.createticket.activity.ContactUsCreateTicketActivity;
import com.tokopedia.contactus.home.view.ContactUsHomeActivity;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.Router;
import com.tokopedia.core.analytics.AnalyticsEventTrackingHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.container.AppsflyerContainer;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.screen.IndexScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.BrandsWebViewActivity;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.manage.people.address.activity.ChooseAddressActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.peoplefave.fragment.PeopleFavoritedShopFragment;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.OtpRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.loyaltytokopoint.ILoyaltyRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.share.DefaultShare;
import com.tokopedia.core.util.AccessTokenRefresh;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.DataMapper;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.SessionRefresh;
import com.tokopedia.core.util.ShareSocmedHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel;
import com.tokopedia.design.utils.DateLabelUtils;
import com.tokopedia.digital.categorylist.view.activity.DigitalCategoryListActivity;
import com.tokopedia.digital.common.constant.DigitalCache;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.newcart.presentation.activity.DigitalCartActivity;
import com.tokopedia.digital.product.view.activity.DigitalProductActivity;
import com.tokopedia.digital.product.view.model.DigitalCategoryDetailPassData;
import com.tokopedia.digital.tokocash.TopupTokoCashFragment;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.model.DealDetailPassData;
import com.tokopedia.discovery.DiscoveryRouter;
import com.tokopedia.discovery.intermediary.view.IntermediaryActivity;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.district_recommendation.domain.mapper.TokenMapper;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.district_recommendation.view.DistrictRecommendationActivity;
import com.tokopedia.district_recommendation.view.shopsettings.DistrictRecommendationShopSettingsActivity;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.ScanQrCodeRouter;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.expresscheckout.router.ExpressCheckoutInternalRouter;
import com.tokopedia.expresscheckout.router.ExpressCheckoutRouter;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.di.FeedPlusComponent;
import com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment;
import com.tokopedia.fingerprint.util.FingerprintConstant;
import com.tokopedia.fingerprint.view.FingerPrintDialog;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.contactus.model.FlightContactUsPassData;
import com.tokopedia.flight.orderlist.view.FlightOrderListFragment;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.flight.review.domain.FlightCheckVoucherCodeUseCase;
import com.tokopedia.flight.review.domain.FlightVoucherCodeWrapper;
import com.tokopedia.flight.review.view.model.FlightCheckoutViewModel;
import com.tokopedia.gallery.ImageReviewGalleryActivity;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gm.subscribe.GMSubscribeInternalRouter;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.groupchat.GroupChatModuleRouter;
import com.tokopedia.groupchat.channel.view.fragment.ChannelFragment;
import com.tokopedia.groupchat.chatroom.data.ChatroomUrl;
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics;
import com.tokopedia.groupchat.room.view.activity.PlayActivity;
import com.tokopedia.home.HomeInternalRouter;
import com.tokopedia.home.IHomeRouter;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.analytics.data.model.UserAttributeData;
import com.tokopedia.home.account.di.AccountHomeInjection;
import com.tokopedia.home.account.di.AccountHomeInjectionImpl;
import com.tokopedia.home.account.presentation.activity.StoreSettingActivity;
import com.tokopedia.home.beranda.data.model.TokopointHomeDrawerData;
import com.tokopedia.home.beranda.data.model.UserTier;
import com.tokopedia.home.beranda.helper.StartSnapHelper;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.homecredit.view.fragment.FragmentCardIdCamera;
import com.tokopedia.homecredit.view.fragment.FragmentSelfieIdCamera;
import com.tokopedia.imageuploader.ImageUploaderRouter;
import com.tokopedia.inbox.common.ResolutionRouter;
import com.tokopedia.inbox.rescenter.create.activity.CreateResCenterActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inboxv2.view.activity.ResoInboxActivity;
import com.tokopedia.instantloan.di.module.InstantLoanChuckRouter;
import com.tokopedia.instantloan.router.InstantLoanRouter;
import com.tokopedia.instantloan.view.activity.InstantLoanActivity;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.model.Configuration;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.following_list.view.activity.KolFollowingListActivity;
import com.tokopedia.kol.feature.post.view.fragment.KolPostShopFragment;
import com.tokopedia.kyc.KYCRouter;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.LinkerRouter;
import com.tokopedia.linker.interfaces.ShareCallback;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.LinkerShareResult;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.loginphone.checkloginphone.view.activity.CheckLoginPhoneNumberActivity;
import com.tokopedia.loginphone.checkloginphone.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.loginphone.checkregisterphone.view.activity.CheckRegisterPhoneNumberActivity;
import com.tokopedia.loginphone.choosetokocashaccount.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.loginphone.common.LoginPhoneNumberRouter;
import com.tokopedia.loginphone.verifyotptokocash.view.activity.TokoCashOtpActivity;
import com.tokopedia.loginregister.LoginRegisterPhoneRouter;
import com.tokopedia.loginregister.LoginRegisterRouter;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.logisticaddaddress.features.manage.ManagePeopleAddressActivity;
import com.tokopedia.logisticaddaddress.router.IAddressRouter;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticgeolocation.pinpoint.GeolocationActivity;
import com.tokopedia.logisticuploadawb.ILogisticUploadAwbRouter;
import com.tokopedia.logisticuploadawb.UploadAwbLogisticActivity;
import com.tokopedia.loyalty.LoyaltyRouter;
import com.tokopedia.loyalty.broadcastreceiver.TokoPointDrawerBroadcastReceiver;
import com.tokopedia.loyalty.common.PopUpNotif;
import com.tokopedia.loyalty.common.TokoPointDrawerData;
import com.tokopedia.loyalty.di.component.DaggerTokopointComponent;
import com.tokopedia.loyalty.di.component.TokopointComponent;
import com.tokopedia.loyalty.di.module.ServiceApiModule;
import com.tokopedia.loyalty.domain.usecase.GetTokopointUseCase;
import com.tokopedia.loyalty.router.ITkpdLoyaltyModuleRouter;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.activity.LoyaltyActivity;
import com.tokopedia.loyalty.view.activity.PromoDetailActivity;
import com.tokopedia.loyalty.view.activity.PromoListActivity;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.fragment.LoyaltyNotifFragmentDialog;
import com.tokopedia.merchantvoucher.MerchantVoucherModuleRouter;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.mitratoppers.MitraToppersRouterInternal;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.presentation.activity.InboxMainActivity;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.normalcheckout.router.NormalCheckoutRouter;
import com.tokopedia.notifcenter.NotifCenterRouter;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.notifications.CMRouter;
import com.tokopedia.nps.NpsRouter;
import com.tokopedia.nps.presentation.view.dialog.AdvancedAppRatingDialog;
import com.tokopedia.nps.presentation.view.dialog.SimpleAppRatingDialog;
import com.tokopedia.officialstore.activity.OldReactNativeOfficialStoreActivity;
import com.tokopedia.officialstore.fragment.ReactNativeOfficialStoreFragment;
import com.tokopedia.oms.OmsModuleRouter;
import com.tokopedia.oms.domain.PostVerifyCartWrapper;
import com.tokopedia.otp.OtpModuleRouter;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.ovo.OvoPayWithQrRouter;
import com.tokopedia.ovo.view.PaymentQRSummaryActivity;
import com.tokopedia.payment.activity.TopPayActivity;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.payment.setting.PaymentSettingInternalRouter;
import com.tokopedia.payment.setting.util.PaymentSettingRouter;
import com.tokopedia.phoneverification.PhoneVerificationRouter;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.phoneverification.view.activity.PhoneVerificationProfileActivity;
import com.tokopedia.phoneverification.view.activity.ReferralPhoneNumberVerificationActivity;
import com.tokopedia.product.manage.item.common.di.component.DaggerProductComponent;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.common.di.module.ProductModule;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity;
import com.tokopedia.product.manage.item.main.base.data.model.ProductPictureViewModel;
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity;
import com.tokopedia.product.manage.item.utils.ProductEditModuleRouter;
import com.tokopedia.product.manage.item.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.product.manage.item.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.product.manage.list.view.activity.ProductManageActivity;
import com.tokopedia.profile.ProfileModuleRouter;
import com.tokopedia.profile.view.activity.ProfileActivity;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutRouter;
import com.tokopedia.promocheckout.detail.view.activity.PromoCheckoutDetailMarketplaceActivity;
import com.tokopedia.promocheckout.list.view.activity.PromoCheckoutListMarketplaceActivity;
import com.tokopedia.recentview.RecentViewInternalRouter;
import com.tokopedia.recentview.RecentViewRouter;
import com.tokopedia.referral.ReferralAction;
import com.tokopedia.referral.ReferralRouter;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.saldodetails.router.SaldoDetailsInternalRouter;
import com.tokopedia.saldodetails.router.SaldoDetailsRouter;
import com.tokopedia.searchbar.SearchBarRouter;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.TkpdSeller;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.common.logout.TkpdSellerLogout;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.etalase.utils.EtalaseUtils;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.variant.view.activity.ProductVariantDashboardActivity;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;
import com.tokopedia.seller.shop.common.di.component.DaggerShopComponent;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.common.di.module.ShopModule;
import com.tokopedia.seller.shopsettings.shipping.EditShippingActivity;
import com.tokopedia.session.addchangeemail.view.activity.AddEmailActivity;
import com.tokopedia.session.addchangepassword.view.activity.AddPasswordActivity;
import com.tokopedia.session.changename.view.activity.ChangeNameActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.sessioncommon.data.loginphone.ChooseTokoCashAccountViewModel;
import com.tokopedia.settingbank.BankRouter;
import com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.ShopPageInternalRouter;
import com.tokopedia.shop.common.router.ShopSettingRouter;
import com.tokopedia.shop.open.ShopOpenInternalRouter;
import com.tokopedia.shop.open.ShopOpenRouter;
import com.tokopedia.shop.settings.ShopSettingsInternalRouter;
import com.tokopedia.talk.common.TalkRouter;
import com.tokopedia.talk.inboxtalk.view.activity.InboxTalkActivity;
import com.tokopedia.talk.producttalk.view.activity.TalkProductActivity;
import com.tokopedia.talk.shoptalk.view.activity.ShopTalkActivity;
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity;
import com.tokopedia.tkpd.applink.AppLinkWebsiteActivity;
import com.tokopedia.tkpd.applink.ApplinkUnsupportedImpl;
import com.tokopedia.tkpd.campaign.view.ShakeDetectManager;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepository;
import com.tokopedia.tkpd.deeplink.data.repository.DeeplinkRepositoryImpl;
import com.tokopedia.tkpd.deeplink.domain.interactor.MapUrlUseCase;
import com.tokopedia.tkpd.drawer.NoOpDrawerHelper;
import com.tokopedia.tkpd.fcm.appupdate.FirebaseRemoteAppUpdate;
import com.tokopedia.tkpd.flight.FlightGetProfileInfoData;
import com.tokopedia.tkpd.flight.FlightVoucherCodeWrapperImpl;
import com.tokopedia.tkpd.flight.di.DaggerFlightConsumerComponent;
import com.tokopedia.tkpd.flight.di.FlightConsumerComponent;
import com.tokopedia.tkpd.flight.presentation.FlightPhoneVerificationActivity;
import com.tokopedia.tkpd.goldmerchant.GoldMerchantRedirectActivity;
import com.tokopedia.tkpd.home.SimpleHomeActivity;
import com.tokopedia.tkpd.home.analytics.HomeAnalytics;
import com.tokopedia.tkpd.home.favorite.view.FragmentFavorite;
import com.tokopedia.tkpd.onboarding.NewOnboardingActivity;
import com.tokopedia.tkpd.qrscanner.QrScannerActivity;
import com.tokopedia.tkpd.react.DaggerReactNativeComponent;
import com.tokopedia.tkpd.react.ReactNativeComponent;
import com.tokopedia.tkpd.redirect.RedirectCreateShopActivity;
import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.tkpd.tkpdreputation.TkpdReputationInternalRouter;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.review.shop.view.ReviewShopInfoActivity;
import com.tokopedia.tkpd.tokocash.GetBalanceTokoCashWrapper;
import com.tokopedia.tkpd.tokocash.datepicker.DatePickerUtil;
import com.tokopedia.tkpd.train.TrainGetBuyerProfileInfoMapper;
import com.tokopedia.tkpd.utils.FingerprintModelGenerator;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.tracking.ProductPageTracking;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.di.ReactNativeModule;
import com.tokopedia.tkpdreactnative.router.ReactNativeRouter;
import com.tokopedia.tokocash.TokoCashComponentInstance;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;
import com.tokopedia.tokocash.common.di.TokoCashComponent;
import com.tokopedia.tokocash.historytokocash.presentation.model.PeriodRangeModelData;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;
import com.tokopedia.tokocash.qrpayment.presentation.activity.NominalQrPaymentActivity;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.topads.common.TopAdsWebViewRouter;
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.topads.sdk.base.TopAdsRouter;
import com.tokopedia.topads.sourcetagging.util.TopAdsAppLinkUtil;
import com.tokopedia.topchat.chatlist.activity.InboxChatActivity;
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity;
import com.tokopedia.topchat.common.TopChatRouter;
import com.tokopedia.trackingoptimizer.TrackingOptimizerRouter;
import com.tokopedia.train.checkout.presentation.model.TrainCheckoutViewModel;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.di.utils.TrainComponentUtils;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.util.TrainAnalytics;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.passenger.presentation.viewmodel.ProfileBuyerInfo;
import com.tokopedia.train.reviewdetail.domain.TrainCheckVoucherUseCase;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.transaction.orders.UnifiedOrderListRouter;
import com.tokopedia.transaction.orders.orderlist.view.activity.OrderListActivity;
import com.tokopedia.transaction.others.CreditCardFingerPrintUseCase;
import com.tokopedia.transaction.purchase.activity.PurchaseActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderDetailActivity;
import com.tokopedia.transaction.purchase.detail.activity.OrderHistoryActivity;
import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;
import com.tokopedia.transaction.wallet.WalletActivity;
import com.tokopedia.transactiondata.entity.request.CheckoutRequest;
import com.tokopedia.transactiondata.entity.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transactiondata.entity.response.cod.Data;
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData;
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam;
import com.tokopedia.updateinactivephone.activity.ChangeInactiveFormRequestActivity;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.withdraw.WithdrawRouter;
import com.tokopedia.withdraw.view.activity.WithdrawActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.hansel.hanselsdk.Hansel;
import okhttp3.Interceptor;
import okhttp3.Response;
import permissions.dispatcher.PermissionRequest;
import retrofit2.Converter;
import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_DESCRIPTION;
import static com.tokopedia.kyc.Constants.Keys.KYC_CARDID_CAMERA;
import static com.tokopedia.kyc.Constants.Keys.KYC_SELFIEID_CAMERA;
import static com.tokopedia.remoteconfig.RemoteConfigKey.APP_ENABLE_SALDO_SPLIT;


/**
 * @author normansyahputa on 12/15/16.
 */
public abstract class ConsumerRouterApplication extends MainApplication implements
        TkpdCoreRouter,
        SellerModuleRouter,
        IDigitalModuleRouter,
        PdpRouter,
        OtpRouter,
        IPaymentModuleRouter,
        TransactionRouter,
        IReactNativeRouter,
        ReactApplication,
        TkpdInboxRouter,
        IWalletRouter,
        LoyaltyRouter,
        ReputationRouter,
        SessionRouter,
        AbstractionRouter,
        FlightModuleRouter,
        LogisticRouter,
        FeedModuleRouter,
        IHomeRouter,
        DiscoveryRouter,
        DigitalModuleRouter,
        TokoCashRouter,
        AffiliateRouter,
        KolRouter,
        GroupChatModuleRouter,
        ApplinkRouter,
        ShopModuleRouter,
        LoyaltyModuleRouter,
        ITkpdLoyaltyModuleRouter,
        ICheckoutModuleRouter,
        IAddressRouter,
        GamificationRouter,
        ProfileModuleRouter,
        ReactNativeRouter,
        ImageUploaderRouter,
        ContactUsModuleRouter,
        ITransactionOrderDetailRouter,
        ILogisticUploadAwbRouter,
        NetworkRouter,
        InstantLoanChuckRouter,
        InstantLoanRouter,
        TopChatRouter,
        TokopointRouter,
        SearchBarRouter,
        GlobalNavRouter,
        AccountHomeRouter,
        OtpModuleRouter,
        DealsModuleRouter,
        OmsModuleRouter,
        ProductEditModuleRouter,
        TopAdsWebViewRouter,
        BankRouter,
        ShopSettingRouter,
        ChangePasswordRouter,
        TrainRouter,
        WithdrawRouter,
        NotifCenterRouter,
        EventModuleRouter,
        ChallengesModuleRouter,
        com.tokopedia.tkpdpdp.ProductDetailRouter,
        MitraToppersRouter,
        PaymentSettingRouter,
        DigitalBrowseRouter,
        ChangePhoneNumberRouter,
        PhoneVerificationRouter,
        TalkRouter,
        TkpdAppsFlyerRouter,
        ScanQrCodeRouter,
        UnifiedOrderListRouter,
        RecentViewRouter,
        MerchantVoucherModuleRouter,
        LoginRegisterRouter,
        LoginPhoneNumberRouter,
        LinkerRouter,
        TopAdsDashboardRouter,
        NpsRouter,
        DigitalRouter,
        TrackingPromoCheckoutRouter,
        TopAdsRouter,
        CMRouter,
        ReferralRouter,
        SaldoDetailsRouter,
        ILoyaltyRouter,
        ChatbotRouter,
        TrackingOptimizerRouter,
        LoginRegisterPhoneRouter,
        ExpressCheckoutRouter,
        ResolutionRouter,
        OvoPayWithQrRouter,
        KYCRouter,
        NormalCheckoutRouter,
        com.tokopedia.product.detail.ProductDetailRouter{


    private final static int IRIS_ROW_LIMIT = 50;
    private final static long IRIS_TIME_MINUTES = 15;

    private static final String EXTRA = "extra";

    @Inject
    ReactNativeHost reactNativeHost;
    @Inject
    ReactUtils reactUtils;

    private DaggerProductComponent.Builder daggerProductBuilder;
    private DaggerReactNativeComponent.Builder daggerReactNativeBuilder;
    private DaggerFlightConsumerComponent.Builder daggerFlightBuilder;
    private EventComponent eventComponent;
    private DealsComponent dealsComponent;
    private FlightConsumerComponent flightConsumerComponent;
    private ProductComponent productComponent;
    private DaggerShopComponent.Builder daggerShopBuilder;
    private ShopComponent shopComponent;
    private ReactNativeComponent reactNativeComponent;
    private RemoteConfig remoteConfig;
    private TokoCashComponent tokoCashComponent;
    private TokopointComponent tokopointComponent;

    private CacheManager cacheManager;

    private Iris mIris;

    @Override
    public void onCreate() {
        super.onCreate();
        Hansel.init(this);
        initializeDagger();
        initDaggerInjector();
        initFirebase();
        initRemoteConfig();
        GraphqlClient.init(getApplicationContext());
        NetworkClient.init(getApplicationContext());
        initCMPushNotification();
        initIris();
    }

    private void initDaggerInjector() {
        getReactNativeComponent().inject(this);
    }

    private void initIris() {
        mIris = Iris.Companion.init(this);

        boolean irisEnable = getBooleanRemoteConfig(RemoteConfigKey.IRIS_GTM_ENABLED_TOGGLE, true);

        mIris.setService(new Configuration(
                IRIS_ROW_LIMIT,
                IRIS_TIME_MINUTES,
                irisEnable
        ));
    }

    @Override
    public Iris getIris() {
        return mIris;
    }

    private FlightConsumerComponent getFlightConsumerComponent() {
        if (flightConsumerComponent == null) {
            flightConsumerComponent = daggerFlightBuilder.build();
        }
        return flightConsumerComponent;
    }

    @Override
    public void goToProfileShop(Context context, String userId) {
        context.startActivity(
                getTopProfileIntent(context, userId)
        );
    }


    private void initializeDagger() {
        daggerProductBuilder = DaggerProductComponent.builder().productModule(new ProductModule());
        daggerReactNativeBuilder = DaggerReactNativeComponent.builder()
                .appComponent(getApplicationComponent())
                .reactNativeModule(new ReactNativeModule(this));
        daggerShopBuilder = DaggerShopComponent.builder().shopModule(new ShopModule());

        daggerFlightBuilder = DaggerFlightConsumerComponent.builder().appComponent(getApplicationComponent());

        tokoCashComponent = TokoCashComponentInstance.getComponent(this);

        FeedPlusComponent feedPlusComponent =
                DaggerFeedPlusComponent.builder()
                        .kolComponent(KolComponentInstance.getKolComponent(this))
                        .build();

        eventComponent = DaggerEventComponent.builder()
                .baseAppComponent((this).getBaseAppComponent())
                .eventModule(new EventModule(this))
                .build();

        tokopointComponent = DaggerTokopointComponent.builder()
                .baseAppComponent((this).getBaseAppComponent())
                .serviceApiModule(new ServiceApiModule())
                .build();

        dealsComponent = DaggerDealsComponent.builder()
                .baseAppComponent((this).getBaseAppComponent())
                .build();
    }

    private void initFirebase() {
        if (com.tokopedia.config.GlobalConfig.DEBUG) {
            try {
                FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
                builder.setApplicationId("1:692092518182:android:f4cc247c743f7921");
                FirebaseApp.initializeApp(this, builder.build());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
    }

    @Override
    public ProductComponent getProductComponent() {
        if (productComponent == null) {
            productComponent = daggerProductBuilder.appComponent(getApplicationComponent()).build();
        }
        return productComponent;
    }

    @Override
    public ShopComponent getShopComponent() {
        if (shopComponent == null) {
            shopComponent = daggerShopBuilder.appComponent(getApplicationComponent()).build();
        }
        return shopComponent;
    }

    @Override
    public void goToHome(Context context) {
        Intent intent = getHomeIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public Intent getIntentCreateShop(Context context) {
        return ShopOpenRouter.getIntentCreateEditShop(context);
    }

    @Override
    public Intent getSplashScreenIntent(Context context) {
        return new Intent(context, ConsumerSplashScreen.class);
    }

    @Override
    public Class getDeepLinkClass() {
        return DeepLinkActivity.class;
    }

    @Override
    public Intent getIntentManageShop(Context context) {
        return TkpdSeller.getIntentManageShop(context);
    }

    @Override
    public android.app.Fragment getFragmentShopSettings() {
        return TkpdSeller.getFragmentShopSettings();
    }

    @Override
    public android.app.Fragment getFragmentSellingNewOrder() {
        return TkpdSeller.getFragmentSellingNewOrder();
    }

    @Override
    public Class getSellingActivityClass() {
        return TkpdSeller.getSellingActivityClass();
    }

    @Override
    public Intent getActivitySellingTransactionNewOrder(Context context) {
        return TkpdSeller.getActivitySellingTransactionNewOrder(context);
    }

    public Intent getActivitySellingTransactionConfirmShipping(Context context) {
        return TkpdSeller.getActivitySellingTransactionConfirmShipping(context);
    }

    public Intent getActivitySellingTransactionShippingStatus(Context context) {
        return TkpdSeller.getActivitySellingTransactionShippingStatus(context);
    }

    public Intent getActivitySellingTransactionList(Context context) {
        return TkpdSeller.getActivitySellingTransactionList(context);
    }

    public Intent getActivitySellingTransactionOpportunity(Context context, String query) {
        return TkpdSeller.getActivitySellingTransactionOpportunity(context, query);
    }

    @Override
    public void openImagePreview(Context context, ArrayList<String> images,
                                 int position) {
        ImageReviewGalleryActivity.Companion.moveTo(context, images, position);
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#isSupportApplink(String)}
     *
     * @param appLinks
     * @return
     */
    @Deprecated
    @Override
    public boolean isSupportedDelegateDeepLink(String appLinks) {
        return isSupportApplink(appLinks);
    }

    public Intent getIntentDeepLinkHandlerActivity() {
        return new Intent(this, DeeplinkHandlerActivity.class);
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#goToApplinkActivity(Activity, String, Bundle)}
     */
    @Deprecated
    @Override
    public void actionNavigateByApplinksUrl(Activity activity, String applinks, Bundle bundle) {
        goToApplinkActivity(activity, applinks, bundle);
    }

    @Override
    public String getBaseUrlDomainPayment() {
        return ConsumerAppBaseUrl.BASE_PAYMENT_URL_DOMAIN;
    }

    @Override
    public String getGeneratedOverrideRedirectUrlPayment(String originUrl) {
        Uri originUri = Uri.parse(originUrl);
        Uri.Builder uriBuilder = Uri.parse(originUrl).buildUpon();
        if (!originUri.isOpaque()) {
            if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_FLAG_APP))) {
                uriBuilder.appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_FLAG_APP,
                        AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_FLAG_APP
                );
            }
            if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_DEVICE))) {
                uriBuilder.appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_DEVICE,
                        AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE
                );
            }
            if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_UTM_SOURCE))) {
                uriBuilder.appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_UTM_SOURCE,
                        AuthUtil.DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_UTM_SOURCE
                );
            }
            if (!TextUtils.isEmpty(originUri.getQueryParameter(AuthUtil.WEBVIEW_FLAG_PARAM_APP_VERSION))) {
                uriBuilder.appendQueryParameter(
                        AuthUtil.WEBVIEW_FLAG_PARAM_APP_VERSION, GlobalConfig.VERSION_NAME
                );
            }
        }
        return uriBuilder.build().toString().trim();
    }

    @Override
    public Map<String, String> getGeneratedOverrideRedirectHeaderUrlPayment(String originUrl) {
        String urlQuery = Uri.parse(originUrl).getQuery();
        return AuthUtil.generateWebviewHeaders(
                Uri.parse(originUrl).getPath(),
                urlQuery != null ? urlQuery : "",
                "GET",
                AuthUtil.KEY.KEY_WSV4);
    }

    @Override
    public boolean getEnableFingerprintPayment() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        return remoteConfig.getBoolean(FingerprintConstant.ENABLE_FINGERPRINT_MAINAPP);
    }

    @Override
    public DrawerHelper getDrawer(AppCompatActivity activity,
                                  SessionHandler sessionHandler,
                                  LocalCacheHandler drawerCache,
                                  GlobalCacheManager globalCacheManager) {
        return new NoOpDrawerHelper(activity);
    }

    @Override
    public void goToManageProduct(Context context) {
        Intent intent = new Intent(context, ProductManageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void goToEtalaseList(Context context) {
        Intent intent = ShopSettingsInternalRouter.getShopSettingsEtalaseActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToDraftProductList(Context context) {
        Intent intent = new Intent(context, ProductDraftListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void clearEtalaseCache() {
        EtalaseUtils.clearEtalaseCache(getApplicationContext());
    }

    @Override
    public Intent goToEditProduct(Context context, boolean isEdit, String productId) {
        return ProductEditActivity.Companion.createInstance(context, productId);
    }

    @Override
    public void resetAddProductCache(Context context) {
        EtalaseUtils.clearEtalaseCache(context);
        EtalaseUtils.clearDepartementCache(context);
    }

    @Override
    public void goToWallet(Context context, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(WalletActivity.EXTRA_URL, url);
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void goToMerchantRedirect(Context context) {
        Intent intent = new Intent(context, GoldMerchantRedirectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void goToGmSubscribeMembershipRedirect(Context context) {
        Intent intent = new Intent(context, GoldMerchantRedirectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void goToGMSubscribe(Context context) {
        Intent intent = GMSubscribeInternalRouter.getGMSubscribeHomeIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void goToCreateMerchantRedirect(Context context) {
        Intent intent = RedirectCreateShopActivity.getCallingIntent(context);
        context.startActivity(intent);
    }

    @Override
    public void getUserInfo(RequestParams empty, ProfileCompletionSubscriber profileSubscriber) {
        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(this);
        String authKey = SessionHandler.getAccessToken(this);
        authKey = sessionHandler.getTokenType(this) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);


        ProfileSourceFactory profileSourceFactory =
                new ProfileSourceFactory(
                        this,
                        accountsService,
                        new GetUserInfoMapper(),
                        null,
                        sessionHandler
                );

        GetUserInfoUseCase getUserInfoUseCase = new GetUserInfoUseCase(
                new ProfileRepositoryImpl(profileSourceFactory)
        );

        getUserInfoUseCase.execute(GetUserInfoUseCase.generateParam(), profileSubscriber);
    }

    @Override
    public Observable<ProfileInfo> getProfile() {
        return FlightGetProfileInfoData
                .newInstance(getFlightConsumerComponent())
                .inject()
                .getProfileInfoPrefillBooking();
    }

    @Override
    public Observable<ProfileBuyerInfo> getProfileInfo() {
        return getProfile().map(new TrainGetBuyerProfileInfoMapper());
    }

    @Override
    public Interceptor getChuckInterceptor() {
        return getAppComponent().chuckInterceptor();
    }


    @Override
    public Intent getTrainOrderListIntent(Context context) {
        return getWebviewActivityWithIntent(context, TrainUrl.TRAIN_ORDER_LIST);
    }

    @Override
    public void sendAnalyticsUserAttribute(UserAttributeData userAttributeData) {
        HomeAnalytics.setUserAttribute(this, userAttributeData);
    }

    @Override
    public void setPromoPushPreference(Boolean newValue) {
        TrackingUtils.setMoEngagePushPreference(this, newValue);
    }

    @Override
    public Intent getIntentOfLoyaltyActivityWithCoupon(Activity activity, String platform,
                                                       String reservationId, String reservationCode) {
        return LoyaltyActivity.newInstanceTrainCouponActive(activity, platform, "11",
                reservationId, reservationCode);
    }

    @Override
    public Observable<PendingCashback> getPendingCashbackUseCase() {
        SessionHandler sessionHandler = new SessionHandler(this);
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putString("msisdn", sessionHandler.getPhoneNumber());
        return tokoCashComponent.getPendingCasbackUseCase().createObservable(requestParams);
    }

    @Override
    public Interceptor getAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }

    @Override
    public Intent getLoyaltyWithCoupon(Activity activity, String platform, String categoryId, String cartId) {
        return LoyaltyActivity.newInstanceCouponActive(activity, platform, categoryId, cartId);
    }

    @Override
    public Intent getLoyaltyWithCouponTabSelected(Activity activity, String platform, String categoryId, String cartId) {
        return LoyaltyActivity.newInstanceCouponActiveAndSelected(activity, platform, categoryId, cartId);
    }

    @Override
    public FlightVoucherCodeWrapper getFlightVoucherCodeWrapper() {
        return new FlightVoucherCodeWrapperImpl();
    }

    @Override
    public Intent getHomeHotlistIntent(Context context) {
        return MainParentActivity.start(context);
    }

    @Override
    public Intent checkoutModuleRouterGetHomeIntent(Context context) {
        return MainParentActivity.getApplinkIntent(context, null);
    }

    @Override
    public Intent getExpressCheckoutIntent(Activity activity, AtcRequestParam atcRequestParam) {
        return ExpressCheckoutInternalRouter.createIntent(activity, atcRequestParam);
    }

    @Override
    public Intent getInboxReputationIntent(Context context) {
        return InboxReputationActivity.getCallingIntent(context);
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass,
                                                Bundle data, String notifTitle) {
        mNotificationPass.mIntent = NotificationUtils.configureGeneralIntent(getInboxReputationIntent(this));
        mNotificationPass.classParentStack = InboxReputationActivity.class;
        mNotificationPass.title = notifTitle;
        mNotificationPass.ticker = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        mNotificationPass.description = data.getString(ARG_NOTIFICATION_DESCRIPTION);
        return mNotificationPass;
    }

    @Override
    public Fragment getReputationHistoryFragment() {
        return SellerReputationFragment.createInstance();
    }

    @Override
    public Fragment getShopReputationFragment(String shopId, String shopDomain) {
        return TkpdReputationInternalRouter.getReviewShopFragment(shopId, shopDomain);
    }

    @Override
    public Intent getProductReputationIntent(Context context, String productId, String productName) {
        return TkpdReputationInternalRouter.getProductReviewIntent(context, productId, productName);
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#goToApplinkActivity(Activity, String, Bundle)}
     */
    @Deprecated
    @Override
    public void actionAppLink(Context context, String linkUrl) {
        goToApplinkActivity(context, linkUrl);
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#goToApplinkActivity(Activity, String, Bundle)}
     */
    @Deprecated
    @Override
    public void actionApplink(Activity activity, String linkUrl) {
        goToApplinkActivity(activity, linkUrl, new Bundle());
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#goToApplinkActivity(Activity, String, Bundle)}
     */
    @Deprecated
    @Override
    public void actionApplinkFromActivity(Activity activity, String linkUrl) {
        goToApplinkActivity(activity, linkUrl, new Bundle());
    }

    /**
     * Use {@link com.tokopedia.applink.RouteManager} or {@link ApplinkRouter#goToApplinkActivity(Activity, String, Bundle)}
     */
    @Deprecated
    @Override
    public void actionApplink(Activity activity, String linkUrl, String extra) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA, extra);
        goToApplinkActivity(activity, linkUrl, bundle);
    }

    @Override
    public void actionOpenGeneralWebView(Activity activity, String mobileUrl) {
        activity.startActivity(BannerWebView.getCallingIntent(activity, mobileUrl));
    }

    @Override
    public void sendEventImpressionPromoList(List<Object> dataLayerSinglePromoCodeList, String title) {
        TrackingUtils.eventImpressionPromoList(this, dataLayerSinglePromoCodeList, title);
    }

    @Override
    public void eventClickPromoListItem(List<Object> dataLayerSinglePromoCodeList, String title) {
        TrackingUtils.eventClickPromoListItem(this, dataLayerSinglePromoCodeList, title);
    }

    @Override
    public Intent getBrandsWebViewIntent(Context context, String url) {
        return BrandsWebViewActivity.newInstance(context, url);
    }

    @Override
    public void onLogout(AppComponent appComponent) {
        new GlobalCacheManager().delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV);
        new CacheApiClearAllUseCase().executeSync();
        TkpdSellerLogout.onLogOut(appComponent);
    }

    @Override
    public Intent getLoginIntent(Context context) {
        Intent intent = LoginActivity.getCallingIntent(context);
        return intent;
    }

    @Override
    public boolean isInstantLoanEnabled() {
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SHOW_INSTANT_LOAN, true);
    }

    @Override
    public void sendEventTrackingOrderDetail(Map<String, Object> eventTracking) {
        UnifyTracking.sendGTMEvent(this, eventTracking);
        CommonUtils.dumper(eventTracking.toString());
    }

    public void sendScreenName(@NonNull String screenName) {
        ScreenTracking.screen(this, screenName);
    }

    @Override
    public Intent getRegisterIntent(Context context) {
        Intent intent = RegisterInitialActivity.getCallingIntent(context);
        return intent;
    }

    @Override
    public Intent instanceIntentCartDigitalProduct(DigitalCheckoutPassData passData) {
        return DigitalCartActivity.newInstance(this, passData);
    }


    @Override
    public Intent instanceIntentDigitalCategoryList() {
        return DigitalCategoryListActivity.newInstance(this);
    }

    @Override
    public Intent getHomeIntent(Context context) {
        return MainParentActivity.start(context);
    }

    @Override
    public Intent getContactUsIntent(Activity activity, FlightContactUsPassData passData) {
        return ContactUsCreateTicketActivity.getCallingIntent(
                activity,
                passData.getToolbarTitle(),
                passData.getSolutionId(),
                passData.getOrderId(),
                passData.getDescriptionTitle(),
                passData.getAttachmentTitle(),
                passData.getDescription()
        );
    }

    @Override
    public Intent getDefaultContactUsIntent(Activity activity) {
        Bundle extras = new Bundle();
        extras.putString(ContactUsConstant.EXTRAS_PARAM_URL,
                URLGenerator.generateURLContactUs(TkpdBaseURL.BASE_CONTACT_US, activity));
        Intent intent = ContactUsHomeActivity.getContactUsHomeIntent(activity, extras);
        return intent;
    }

    @Override
    public Intent getDefaultContactUsIntent(Activity activity, String url, String toolbarTitle) {
        Bundle extras = new Bundle();
        extras.putString(ContactUsConstant.EXTRAS_PARAM_URL,
                URLGenerator.generateURLContactUs(Uri.encode(url), activity));
        extras.putString(ContactUsActivity.EXTRAS_PARAM_TOOLBAR_TITLE, toolbarTitle);
        Intent intent = ContactUsHomeActivity.getContactUsHomeIntent(activity, extras);
        return intent;
    }

    @Override
    public String getTrackingClientId() {
        return TrackingUtils.getClientID(getAppContext());
    }

    @Override
    public Intent getDealDetailIntent(Activity activity,
                                      String slug,
                                      boolean enableBuy,
                                      boolean enableRecommendation,
                                      boolean enableShare,
                                      boolean enableLike) {
        return DealDetailsActivity.getCallingIntent(activity,
                new DealDetailPassData.Builder()
                        .slug(slug)
                        .enableBuy(enableBuy)
                        .enableRecommendation(enableRecommendation)
                        .enableShare(enableShare)
                        .enableLike(enableLike)
                        .build()
        );
    }

    @Override
    public String getBranchAutoApply(Activity activity) {
        return null;
    }

    @Override
    public Intent getDefaultContactUsIntent(Activity activity, String url) {
        Bundle extras = new Bundle();
        extras.putString(ContactUsConstant.EXTRAS_PARAM_URL,
                URLGenerator.generateURLContactUs(Uri.encode(url), activity));
        Intent intent = ContactUsHomeActivity.getContactUsHomeIntent(activity, extras);
        return intent;
    }

    @Override
    public Intent getPhoneVerifIntent(Activity activity) {
        return FlightPhoneVerificationActivity.getCallingIntent(activity);
    }

    @Override
    public Intent getSellerHomeIntent(Activity activity) {
        return null;
    }

    @Override
    public Intent getLoginGoogleIntent(Context context) {
        return LoginActivity.getAutoLoginGoogle(context);
    }

    @Override
    public Intent getLoginFacebookIntent(Context context) {
        return LoginActivity.getAutoLoginFacebook(context);

    }

    @Override
    public Intent getLoginWebviewIntent(Context context, String name, String url) {
        return LoginActivity.getAutoLoginWebview(context, name, url);
    }

    @Override
    public Intent getPhoneVerificationActivityIntent(Context context) {
        return PhoneVerificationActivationActivity.getIntent(context, false, false);
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        return MainParentActivity.class;
    }

    @Override
    public String getAfUniqueId() {
        return TrackingUtils.getAfUniqueId(MainApplication.getAppContext());
    }

    @Override
    public String getAdsId() {
        AnalyticsCacheHandler analHandler = new AnalyticsCacheHandler(new GlobalCacheManager());
        return analHandler.getAdsId();
    }

    @Override
    public Intent getReferralPhoneNumberActivityIntent(Activity activity) {
        return ReferralPhoneNumberVerificationActivity.getCallingIntent(activity);
    }

    @Override
    public void goToUserPaymentList(Activity activity) {
        activity.startActivity(PaymentSettingInternalRouter.getSettingListPaymentActivityIntent(activity));
    }

    @Override
    public void goToOrderHistory(Context context, String orderId, int userMode) {
        Intent intent = OrderHistoryActivity.createInstance(context, orderId, userMode);
        context.startActivity(intent);
    }

    @Override
    public Intent goToOrderDetail(Context context, String orderId) {
        return OrderDetailActivity.createSellerInstance(context, orderId);

    }

    @Override
    public void sendAddWishlistEmitter(String productId, String userId) {
        reactUtils.sendAddWishlistEmitter(productId, userId);
    }

    @Override
    public void sendRemoveWishlistEmitter(String productId, String userId) {
        reactUtils.sendRemoveWishlistEmitter(productId, userId);
    }

    @Override
    public void sendRemoveFavoriteEmitter(String shopId, String userId) {
        reactUtils.sendRemoveFavoriteEmitter(shopId, userId);
    }

    @Override
    public void sendLoginEmitter(String userId) {
        reactUtils.sendLoginEmitter(userId);
    }

    @Override
    public void sendAddFavoriteEmitter(String shopId, String userId) {
        reactUtils.sendAddFavoriteEmitter(shopId, userId);
    }

    private ReactNativeComponent getReactNativeComponent() {
        if (reactNativeComponent == null)
            reactNativeComponent = daggerReactNativeBuilder.build();
        return reactNativeComponent;
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        if (reactNativeHost == null) initDaggerInjector();
        return reactNativeHost;
    }

    @Override
    public Intent getAskBuyerIntent(Context context, String toUserId, String customerName,
                                    String customSubject, String customMessage, String source,
                                    String avatar) {
        return TopChatRoomActivity.getAskBuyerIntent(context, toUserId, customerName,
                customMessage, source, avatar);
    }

    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                     String customSubject, String customMessage, String source, String avatar) {

        return TopChatRoomActivity.getAskSellerIntent(context, toShopId, shopName,
                customMessage, source, avatar);

    }


    @Override
    public Intent getAskUserIntent(Context context, String userId, String userName, String source,
                                   String avatar) {

        return TopChatRoomActivity.getAskUserIntent(context, userId, userName, source, avatar);


    }

    @Override
    public Intent getAskSellerIntent(Context context, String toShopId, String shopName,
                                     String customSubject, String source) {
        return TopChatRoomActivity.getAskSellerIntent(context, toShopId, shopName, customSubject,
                source, "");
    }

    @Override
    public void goToGMSubscribe(Activity activity) {
        Intent intent = new Intent(activity, GoldMerchantRedirectActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public Intent getGMHomeIntent(Context context) {
        return GMSubscribeInternalRouter.getGMSubscribeHomeIntent(context);
    }

    @Override
    public void navigateAppLinkWallet(Context context,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        context.startActivity(getIntentAppLinkWallet(context, appLinkScheme, alternateRedirectUrl));
    }

    @Override
    public void navigateAppLinkWallet(Activity activity,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        activity.startActivityForResult(
                getIntentAppLinkWallet(activity, appLinkScheme, alternateRedirectUrl), requestCode
        );
    }

    @Override
    public void navigateAppLinkWallet(android.app.Fragment fragment,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        fragment.startActivityForResult(
                getIntentAppLinkWallet(
                        fragment.getActivity(), appLinkScheme, alternateRedirectUrl
                ), requestCode
        );
    }

    @Override
    public void navigateAppLinkWallet(Fragment fragmentSupport,
                                      int requestCode,
                                      String appLinkScheme,
                                      String alternateRedirectUrl,
                                      Bundle bundlePass) {
        fragmentSupport.startActivityForResult(
                getIntentAppLinkWallet(fragmentSupport.getActivity(),
                        appLinkScheme, alternateRedirectUrl
                ), requestCode
        );
    }

    /**
     * @param context
     * @param appLinkScheme
     * @param alternateRedirectUrl
     * @return
     */

    private Intent getIntentAppLinkWallet(Context context, String appLinkScheme,
                                          String alternateRedirectUrl) {

        return appLinkScheme == null || appLinkScheme.isEmpty() ?
                getWebviewActivityWithIntent(context, alternateRedirectUrl)
                : isSupportedDelegateDeepLink(appLinkScheme)
                ? getApplinkIntent(context, appLinkScheme).setData(Uri.parse(appLinkScheme))
                : getWebviewActivityWithIntent(context, appLinkScheme);
    }

    @Override
    public String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    public Observable<GMFeaturedProductDomainModel> getFeaturedProduct() {
        GMFeaturedProductDomainModel gmFeaturedProductDomainModel = new GMFeaturedProductDomainModel();
        gmFeaturedProductDomainModel.setData(new ArrayList<GMFeaturedProductDomainModel.Datum>());
        return Observable.just(gmFeaturedProductDomainModel);
    }

    public void goToAddProduct(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ProductAddNameCategoryActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    public boolean isInMyShop(Context context, String shopId) {
        return context != null && new SessionHandler(context).getShopID().trim().equalsIgnoreCase(shopId.trim());
    }

    @Override
    public Intent goToDatePicker(Activity activity,
                                 List<PeriodRangeModelData> periodRangeModelData,
                                 long startDate, long endDate,
                                 int datePickerSelection, int datePickerType) {
        return DatePickerUtil.getDatePickerIntent(activity, startDate, endDate, convert(periodRangeModelData),
                datePickerSelection, datePickerType);
    }

    public static List<PeriodRangeModel> convert(List<PeriodRangeModelData> periodRangeModelDatas) {
        List<PeriodRangeModel> periodRangeModels = new ArrayList<>();
        for (PeriodRangeModelData periodRangeModelData : periodRangeModelDatas) {
            periodRangeModels.add(new PeriodRangeModel(
                    periodRangeModelData.getStartDate(),
                    periodRangeModelData.getEndDate(),
                    periodRangeModelData.getLabel()
            ));
        }
        return periodRangeModels;
    }

    @Override
    public Intent getForgotPasswordIntent(Context context, String email) {
        return ForgotPasswordActivity.getCallingIntent(context, email);
    }

    @Override
    public Intent getAutomaticResetPasswordIntent(Context context, String email) {
        return ForgotPasswordActivity.getAutomaticResetPasswordIntent(context, email);
    }

    @Override
    public Intent getInboxMessageIntent(Context context) {
        return InboxChatActivity.getCallingIntent(context);
    }

    @Override
    public String getRangeDateFormatted(Context context, long startDate, long endDate) {
        return DateLabelUtils.getRangeDateFormatted(context, startDate, endDate);
    }

    @Override
    public WalletUserSession getTokoCashSession() {
        return new WalletUserSessionImpl(this);
    }

    @Override
    public Intent getWebviewActivityWithIntent(Context context, String url) {
        return SimpleWebViewWithFilePickerActivity.getIntent(context,
                url);
    }

    @Override
    public Intent getWebviewActivityWithIntent(Context context, String url, String title) {
        return SimpleWebViewWithFilePickerActivity.getIntentWithTitle(context, url, title);
    }

    @Override
    public Intent openWebViewGimicURLIntentFromExploreHome(Context context, String url, String title) {
        Intent intent = SimpleWebViewWithFilePickerActivity.getIntent(context, url);
        intent.putExtra(BannerWebView.EXTRA_TITLE, title);
        return intent;
    }

    @Override
    public String getUserEmailProfil() {
        SessionHandler sessionHandler = new SessionHandler(this);
        return sessionHandler.getEmail();
    }

    @Override
    public Fragment getTopupTokoCashFragment() {
        return TopupTokoCashFragment.newInstance();
    }

    @Override
    public void invalidateCategoryMenuData() {

    }

    @Override
    public Intent getResolutionCenterIntent(Context context) {
        return InboxResCenterActivity.createIntent(context);
    }

    @Override
    public Intent getResolutionCenterIntentBuyer(Context context) {
        return ResoInboxActivity.newBuyerInstance(context);
    }

    @Override
    public Intent getResolutionCenterIntentSeller(Context context) {
        return ResoInboxActivity.newSellerInstance(context);
    }

    @Override
    public Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName) {
        return DetailResChatActivity.newBuyerInstance(context, resoId, shopName);
    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId) {
        return CreateResCenterActivity.getCreateResCenterActivityIntent(context, orderId);
    }

    @Override
    public Intent getCreateResCenterActivityIntent(Context context, String orderId, int troubleId, int solutionId) {
        return CreateResCenterActivity.getCreateResCenterActivityIntent(context, orderId, troubleId, solutionId);
    }

    @Override
    public void onForceLogout(Activity activity) {
        SessionHandler sessionHandler = new SessionHandler(activity);
        sessionHandler.forceLogout();
        invalidateCategoryMenuData();
        Intent intent = CustomerRouter.getSplashScreenIntent(getBaseContext());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showTimezoneErrorSnackbar() {
        ServerErrorHandler.showTimezoneErrorSnackbar();
    }

    @Override
    public void showMaintenancePage() {
        ServerErrorHandler.showMaintenancePage();
    }

    @Override
    public void sendForceLogoutAnalytics(Response response) {
        ServerErrorHandler.sendForceLogoutAnalytics(response.request().url().toString());
    }

    @Override
    public void showForceLogoutTokenDialog(String response) {
        ServerErrorHandler.showForceLogoutDialog();
        ServerErrorHandler.sendForceLogoutTokenAnalytics(response);
    }

    @Override
    public void showServerError(Response response) {
        ServerErrorHandler.sendErrorNetworkAnalytics(response.request().url().toString(), response.code());
    }

    @Override
    public void gcmUpdate() throws IOException {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        SessionRefresh sessionRefresh = new SessionRefresh(accessTokenRefresh.refreshToken());
        sessionRefresh.gcmUpdate();
    }

    @Override
    public void refreshToken() throws IOException {
        AccessTokenRefresh accessTokenRefresh = new AccessTokenRefresh();
        accessTokenRefresh.refreshToken();
    }

    @Override
    public void sendEventTracking(Map<String, Object> eventTracking) {
        UnifyTracking.eventClearEnhanceEcommerce(this);
        UnifyTracking.sendGTMEvent(this, eventTracking);
    }

    @Override
    public void sendEnhanceECommerceTracking(Map<String, Object> events) {
        TrackingUtils.eventTrackingEnhancedEcommerce(this, events);
    }

    @Override
    public CacheManager getGlobalCacheManager() {
        if (cacheManager == null) {
            cacheManager = new GlobalCacheManager();
        }
        return cacheManager;
    }

    @Override
    public long getLongConfig(String flightAirport) {
        return remoteConfig.getLong(flightAirport);
    }

    @Override
    public boolean isPromoNativeEnable() {
        return remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_NATIVE_PROMO_LIST);
    }

    @Override
    public Intent getLoginIntent() {
        Intent intent = LoginActivity.getCallingIntent(this);
        return intent;
    }

    @Override
    public SnapHelper getSnapHelper() {
        return new StartSnapHelper();
    }

    @Override
    public RecyclerView.ItemDecoration getSpacingItemDecorationHome(int spacing, int displayMode) {
        return new SpacingItemDecoration(spacing, displayMode);
    }

    @Override
    public Intent getTopPayIntent(Activity activity, FlightCheckoutViewModel flightCheckoutViewModel) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setPaymentId(flightCheckoutViewModel.getPaymentId());
        paymentPassData.setTransactionId(flightCheckoutViewModel.getTransactionId());
        paymentPassData.setRedirectUrl(flightCheckoutViewModel.getRedirectUrl());
        paymentPassData.setCallbackFailedUrl(flightCheckoutViewModel.getCallbackFailedUrl());
        paymentPassData.setCallbackSuccessUrl(flightCheckoutViewModel.getCallbackSuccessUrl());
        paymentPassData.setQueryString(flightCheckoutViewModel.getQueryString());
        return TopPayActivity.createInstance(activity, paymentPassData);
    }

    @Override
    public Intent getTopPayIntent(Activity activity, TrainCheckoutViewModel trainCheckoutViewModel) {
        PaymentPassData paymentPassData = new PaymentPassData();
        paymentPassData.setPaymentId(trainCheckoutViewModel.getTransactionId());
        paymentPassData.setTransactionId(trainCheckoutViewModel.getTransactionId());
        paymentPassData.setRedirectUrl(trainCheckoutViewModel.getRedirectURL());
        paymentPassData.setCallbackFailedUrl(trainCheckoutViewModel.getCallbackURLFailed());
        paymentPassData.setCallbackSuccessUrl(trainCheckoutViewModel.getCallbackURLSuccess());
        paymentPassData.setQueryString(trainCheckoutViewModel.getQueryString());
        return TopPayActivity.createInstance(activity, paymentPassData);
    }

    @Override
    public int getTopPayPaymentSuccessCode() {
        return TopPayActivity.PAYMENT_SUCCESS;
    }

    @Override
    public int getTopPayPaymentFailedCode() {
        return TopPayActivity.PAYMENT_FAILED;
    }

    @Override
    public int getTopPayPaymentCancelCode() {
        return TopPayActivity.PAYMENT_CANCELLED;
    }

    @Override
    public Intent getBannerWebViewIntent(Activity activity, String url) {
        return BannerWebView.getCallingIntent(activity, url);
    }

    @Override
    public Intent getBannerWebViewOnAllPromoClickFromHomeIntent(Activity activity,
                                                                String url,
                                                                String title) {
        return BannerWebView.getCallingIntentWithTitle(activity, url, title);
    }

    @Override
    public boolean isTrainNativeEnable() {
        return remoteConfig.getBoolean(TrainRouter.TRAIN_ENABLE_REMOTE_CONFIG);
    }

    @Override
    public Intent getWebviewActivity(Activity activity, String url) {
        return AppLinkWebsiteActivity.newInstance(activity, url);
    }

    @Override
    public DialogFragment getLoyaltyTokoPointNotificationDialogFragment(PopUpNotif popUpNotif) {
        return LoyaltyNotifFragmentDialog.newInstance(popUpNotif);
    }

    @Override
    public String applink(Activity activity, String deeplink) {
        DeeplinkRepository deeplinkRepository = new DeeplinkRepositoryImpl(this, new GlobalCacheManager());
        MapUrlUseCase mapUrlUseCase = new MapUrlUseCase(deeplinkRepository);
        Uri uri = Uri.parse(deeplink);
        final List<String> linkSegments = uri.getPathSegments();
        StringBuilder finalSegments = new StringBuilder();
        for (int i = 0; i < linkSegments.size(); i++) {
            if (i != linkSegments.size() - 1) {
                finalSegments.append(linkSegments.get(i)).append("/");
            } else {
                finalSegments.append(linkSegments.get(i));
            }
        }
        return mapUrlUseCase.getData(mapUrlUseCase.setRequestParam(finalSegments.toString())).applink;
    }

    public Intent getKolFollowingPageIntent(Context context, int userId) {
        return KolFollowingListActivity.getCallingIntent(context, userId);
    }

    @Override
    public Intent getChangePhoneNumberIntent(Context context, String email, String phoneNumber) {
        return ChangePhoneNumberWarningActivity.newInstance(context, email, phoneNumber);
    }

    @Override
    public Observable<TokoCashData> getTokoCashBalance() {
        return new GetBalanceTokoCashWrapper(tokoCashComponent.getBalanceTokoCashUseCase())
                .processGetBalance();
    }

    @Override
    public Intent getNominalActivityIntent(Context context, String qrcode, InfoQrTokoCash infoQrTokoCash) {
        return NominalQrPaymentActivity.newInstance(context, qrcode, infoQrTokoCash);
    }

    @Override
    public Intent getOvoActivityIntent(Context context) {
        return new Intent(context,PaymentQRSummaryActivity.class);
    }

    @Override
    public Observable<BalanceTokoCash> getBalanceTokoCash() {
        return tokoCashComponent.getBalanceTokoCashUseCase().createObservable(com.tokopedia.usecase.RequestParams.EMPTY);
    }

    @Override
    public Observable<com.tokopedia.digital.categorylist.data.cloud.entity.tokocash.TokoCashData> getDigitalTokoCashBalance() {
        return new GetBalanceTokoCashWrapper(tokoCashComponent.getBalanceTokoCashUseCase())
                .processDigitalGetBalance();
    }

    @Override
    public Observable<HomeHeaderWalletAction> getWalletBalanceHomeHeader() {
        return new GetBalanceTokoCashWrapper(tokoCashComponent.getBalanceTokoCashUseCase())
                .getWalletBalanceHomeHeader();
    }

    @Override
    public void showForceLogoutDialog() {
        ServerErrorHandler.showForceLogoutDialog();
    }

    @Override
    public Observable<InfoQrTokoCash> getInfoQrTokoCashUseCase(com.tokopedia.usecase.RequestParams requestParams) {
        return tokoCashComponent.getInfoQrTokocashUseCase().createObservable(requestParams);
    }

    @Override
    public String getExtraBroadcastReceiverWallet() {
        return "Broadcast Wallet";
    }

    @Override
    public Observable<WalletModel> getTokoCashAccountBalance() {
        return new GetBalanceTokoCashWrapper(tokoCashComponent.getBalanceTokoCashUseCase(),
                tokoCashComponent.getPendingCasbackUseCase())
                .getTokoCashAccountBalance();
    }

    @Override
    public void sendEventTracking(String event, String category, String action, String label) {
        UnifyTracking.sendGTMEvent(ConsumerRouterApplication.this, new EventTracking(event, category, action, label).getEvent());
    }

    @Override
    public void sendMoEngageOpenShopEventTracking(String screenName) {
        TrackingUtils.sendMoEngageCreateShopEvent(ConsumerRouterApplication.this, screenName);
    }

    @Override
    public void navigateToChooseAddressActivityRequest(Fragment var1, Intent var2, int var3) {
        Intent instance = ChooseAddressActivity.createInstance(var1.getContext());
        var1.startActivityForResult(instance, var3);
    }

    @Override
    public void navigateToEditAddressActivityRequest(final Fragment fragment, final int requestCode, Token token) {
        fragment.startActivityForResult(DistrictRecommendationActivity.createInstanceIntent(fragment.getActivity(),
                token),
                requestCode);
    }

    @Override
    public Intent navigateToGeoLocationActivityRequest(Context context, LocationPass locationPass) {
        return GeolocationActivity.createInstance(context, locationPass, false);
    }

    @Override
    public Intent getGeolocationIntent(Context context, LocationPass locationPass) {
        return GeolocationActivity.createInstance(context, locationPass, true);
    }

    @Override
    public Intent getPhoneVerificationActivationIntent(Context context) {
        return PhoneVerificationActivationActivity.getCallingIntent(context);
    }

    @Override
    public Intent getPhoneVerificationProfileIntent(Context context) {
        return PhoneVerificationProfileActivity.getCallingIntent(context);
    }

    @Override
    public BroadcastReceiver getTokoPointBroadcastReceiver() {
        return new TokoPointDrawerBroadcastReceiver();
    }

    @Override
    public Observable<TokoPointDrawerData> getTokopointUseCase() {
        com.tokopedia.usecase.RequestParams params = com.tokopedia.usecase.RequestParams.create();
        params.putString(GetTokopointUseCase.KEY_PARAM,
                CommonUtils.loadRawString(getResources(), com.tokopedia.loyalty.R.raw.tokopoints_query));
        return this.tokopointComponent.getTokopointUseCase().createObservable(params);
    }

    @Override
    public Observable<TokopointHomeDrawerData> getTokopointUseCaseForHome() {
        com.tokopedia.usecase.RequestParams params = com.tokopedia.usecase.RequestParams.create();
        params.putString(GetTokopointUseCase.KEY_PARAM,
                GraphqlHelper.loadRawString(getResources(), com.tokopedia.loyalty.R.raw.tokopoints_query));
        return this.tokopointComponent.getTokopointUseCase().createObservable(params)
                .map(new Func1<TokoPointDrawerData, TokopointHomeDrawerData>() {
                    @Override
                    public TokopointHomeDrawerData call(TokoPointDrawerData tokoPointDrawerData) {
                        UserTier userTier = new UserTier(
                                tokoPointDrawerData.getUserTier().getTierNameDesc(),
                                tokoPointDrawerData.getUserTier().getTierImageUrl(),
                                tokoPointDrawerData.getUserTier().getRewardPointsStr()
                        );

                        return new TokopointHomeDrawerData(
                                tokoPointDrawerData.getOffFlag(),
                                tokoPointDrawerData.getHasNotif(),
                                userTier,
                                tokoPointDrawerData.getUserTier().getRewardPointsStr(),
                                tokoPointDrawerData.getMainPageUrl(),
                                tokoPointDrawerData.getMainPageTitle(),
                                tokoPointDrawerData.getSumCoupon(),
                                tokoPointDrawerData.getSumCouponStr()
                        );
                    }
                });
    }

    public GetShopInfoUseCase getShopInfo() {
        return getShopComponent().getShopInfoUseCase();
    }

    @Override
    public Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest, boolean isOneClickShipment) {
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject(AddToCartUseCase.PARAM_ADD_TO_CART, addToCartRequest);
        if (isOneClickShipment) {
            return CartComponentInjector.newInstance(this).getAddToCartUseCaseOneClickShipment()
                    .createObservable(requestParams)
                    .map(this::mapAddToCartResult);
        } else {
            return CartComponentInjector.newInstance(this).getAddToCartUseCase()
                    .createObservable(requestParams)
                    .map(this::mapAddToCartResult);
        }
    }

    @NotNull
    @Override
    public Observable<CheckoutData> checkoutProduct(@NotNull CheckoutRequest checkoutRequest, boolean isOneClickShipment, boolean isExpressCheckout) {
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject(CheckoutUseCase.PARAM_CARTS, checkoutRequest);
        requestParams.putBoolean(CheckoutUseCase.PARAM_ONE_CLICK_SHIPMENT, isOneClickShipment);
        requestParams.putBoolean(CheckoutUseCase.PARAM_IS_EXPRESS, isExpressCheckout);
        return CartComponentInjector.newInstance(this).getCheckoutUseCase()
                .createObservable(requestParams);
    }

    @NotNull
    @Override
    public Observable<String> updateAddress(com.tokopedia.usecase.RequestParams requestParams) {
        return CartComponentInjector.newInstance(this).getEditAddressUseCase().createObservable(requestParams);
    }

    @NonNull
    private AddToCartResult mapAddToCartResult(AddToCartDataResponse addToCartDataResponse) {
        List<String> messageList = addToCartDataResponse.getMessage();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageList.size(); i++) {
            String string = messageList.get(i);
            stringBuilder.append(string);
            stringBuilder.append(" ");
        }
        return new AddToCartResult.Builder()
                .message(stringBuilder.toString())
                .success(addToCartDataResponse.getSuccess() == 1)
                .cartId(addToCartDataResponse.getData() != null
                        ? String.valueOf(addToCartDataResponse.getData().getCartId())
                        : "")
                .build();
    }

    @Override
    public Intent getCartIntent(Activity activity) {
        Intent intent = new Intent(activity, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void getDynamicShareMessage(Context dataObj, ActionCreator<String, Integer> actionCreator, ActionUIDelegate<String, String> actionUIDelegate) {
        ReferralAction<Context, String, Integer, String, String, String, Context> referralAction = new ReferralAction<>();
        referralAction.doAction(com.tokopedia.referral.Constants.Action.ACTION_GET_REFERRAL_CODE, dataObj,
                actionCreator, actionUIDelegate);
    }

    @Override
    public boolean isToggleBuyAgainOn() {
        return remoteConfig.getBoolean(RemoteConfigKey.MAIN_APP_ENABLE_BUY_AGAIN, true);
    }

    @NonNull
    @Override
    public Intent getCheckoutIntent(@NonNull Context context) {
        return ShipmentActivity.createInstance(context);
    }

    @Override
    public Intent getAddToCartIntent(Context context, String productId, String price, String
            imageSource) {
        String EXTRA_PRODUCT_CART = "EXTRA_PRODUCT_CART";

        ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                .setProductId(productId)
                .setPrice(price)
                .setImageUri(imageSource)
                .build();

        Intent intent = new Intent(context, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public Observable<String> getAtcObsr() {
        FlightAddToCartUseCase useCase = new FlightAddToCartUseCase(null, null);
        return useCase.createObservable(null)
                .map(new Func1<CartEntity, String>() {
                    @Override
                    public String call(CartEntity cartEntity) {
                        return cartEntity.toString();
                    }
                });
    }

    @Override
    public Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(
            Context context, String platform, String category, String defaultSelectedTab
    ) {
        return LoyaltyActivity.newInstanceCouponActive(context, platform, category, defaultSelectedTab);
    }

    @Override
    public Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartListIntent(
            boolean couponActive, String additionalStringData, int pageTracking
    ) {
        return PromoCheckoutListMarketplaceActivity.Companion.newInstance(getAppContext(), couponActive, "", false, pageTracking);
    }

    @Override
    public Intent checkoutModuleRouterGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
            boolean couponActive, String additionalStringData, boolean isOneClickShipment, int pageTracking
    ) {
        return PromoCheckoutListMarketplaceActivity.Companion.newInstance(getAppContext(), couponActive, "", isOneClickShipment, pageTracking);
    }

    @Override
    public Intent checkoutModuleRouterGetRecentViewIntent() {
        return RecentViewInternalRouter.getRecentViewIntent(getAppContext());
    }

    @Override
    public Intent getPromoCheckoutDetailIntentWithCode(String promoCode, boolean promoCouponActive, boolean oneClickShipment, int pageTracking) {
        return PromoCheckoutDetailMarketplaceActivity.Companion.createIntent(getAppContext(), promoCode, true, oneClickShipment, pageTracking);
    }

    @Override
    public Intent getPromoCheckoutListIntentWithCode(String promoCode, boolean promoCouponActive, boolean oneClickShipment, int pageTracking) {
        return PromoCheckoutListMarketplaceActivity.Companion.newInstance(getAppContext(), promoCouponActive, promoCode, oneClickShipment, pageTracking);
    }

    @Override
    public ChuckInterceptor loyaltyModuleRouterGetCartCheckoutChuckInterceptor() {
        return getAppComponent().chuckInterceptor();
    }

    @Override
    public Converter.Factory loyaltyModuleRouterGetStringResponseConverter() {
        return new StringResponseConverter();
    }

    @Override
    public Intent checkoutModuleRouterGetTransactionSummaryIntent() {
        return TransactionPurchaseRouter.createIntentTxSummary(getAppContext());
    }

    @Override
    public void checkoutModuleRouterResetBadgeCart() {
        CartBadgeNotificationReceiver.resetBadgeCart(getAppContext());
    }

    @Override
    public String checkoutModuleRouterGetAutoApplyCouponBranchUtil() {
        PersistentCacheManager persistentCacheManager = new PersistentCacheManager(context, TkpdCache.CACHE_PROMO_CODE);
        return persistentCacheManager.getString(TkpdCache.Key.KEY_CACHE_PROMO_CODE, "");
    }

    @Override
    public Intent checkoutModuleRouterGetShopInfoIntent(String shopId) {
        return ShopPageInternalRouter.getShopPageIntent(getAppContext(), shopId);
    }

    @Override
    public Intent checkoutModuleRouterGetWhislistIntent() {
        return SimpleHomeActivity.newWishlistInstance(this);
    }

    @Override
    public Interceptor checkoutModuleRouterGetCartCheckoutChuckInterceptor() {
        return getAppComponent().chuckInterceptor();
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    public PublicKey checkoutModuleRouterGeneratePublicKey() {
        return FingerPrintDialog.generatePublicKey(getAppContext());
    }

    @Override
    public String checkoutModuleRouterGetPublicKey(PublicKey publicKey) {
        return FingerPrintDialog.getPublicKey(publicKey);
    }

    @Override
    public void goToPurchasePage(Activity activity) {
        activity.startActivity(PurchaseActivity.newInstance(activity));
        if (!(activity instanceof MainParentActivity)) {
            activity.finish();
        }
    }

    @Override
    public boolean checkoutModuleRouterGetEnableFingerprintPayment() {
        return getEnableFingerprintPayment();
    }

    @Override
    public FingerprintInterceptor checkoutModuleRouterGetCartCheckoutFingerPrintInterceptor() {
        return getAppComponent().fingerprintInterceptor();
    }

    @Override
    public Converter.Factory checkoutModuleRouterGetWS4TkpdResponseConverter() {
        return new TkpdResponseConverter();
    }

    @Override
    public Converter.Factory checkoutModuleRouterGetStringResponseConverter() {
        return new StringResponseConverter();
    }

    @Override
    public Intent getContactUsIntent(Context context) {
        return ContactUsHomeActivity.getContactUsHomeIntent(context, new Bundle());
    }

    @Override
    public void openIntermediaryActivity(Activity activity, String depID, String title) {
        IntermediaryActivity.moveTo(activity, depID, title);
    }

    @Override
    public void onDigitalItemClickFromExploreHome(
            Activity activity, String appLink, String categoryId, String name, String url) {
        DigitalCategoryDetailPassData passData = new DigitalCategoryDetailPassData.Builder()
                .appLinks(appLink)
                .categoryId(categoryId)
                .categoryName(name)
                .url(url)
                .build();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DigitalProductActivity.EXTRA_CATEGORY_PASS_DATA, passData);
        goToApplinkActivity(activity, appLink, bundle);
    }

    @Override
    public Intent getActivityShopCreateEdit(Context context) {
        Intent intent = SellerRouter.getActivityShopCreateEdit(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public Fragment getKolPostShopFragment(String shopId, String createPostUrl) {
        return KolPostShopFragment.newInstance(shopId, createPostUrl);
    }

    @Override
    public void goToShareShop(Activity activity, String shopId, String shopUrl, String shareLabel) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.SHOP_TYPE)
                .setName(getString(R.string.message_share_shop))
                .setTextContent(shareLabel)
                .setUri(shopUrl)
                .setId(shopId)
                .build();
        new DefaultShare(activity, shareData).show();
    }

    @Override
    public void goToManageShop(Context context) {
        context.startActivity(StoreSettingActivity.createIntent(context));
    }

    @Override
    public void goToEditShopNote(Context context) {
        Intent intent = ShopSettingsInternalRouter.getShopSettingsNotesActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToAddProduct(Context context) {
        if (context != null && context instanceof Activity) {
            Intent intent = new Intent(context, ProductAddNameCategoryActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void goToWebview(Context context, String url) {
        Intent intent = new Intent(this, BannerWebView.class);
        intent.putExtra(BannerWebView.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public void goToChatSeller(Context context, String shopId, String shopName, String avatar) {
        UserSessionInterface userSession = new UserSession(this);
        if (userSession.isLoggedIn()) {
            UnifyTracking.eventShopSendChat(context);
            Intent intent = getAskSellerIntent(this, shopId, shopName, "", TkpdInboxRouter.SHOP);
            context.startActivity(intent);
        } else {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getLoginIntent(context);
            ((Activity) context).startActivityForResult(intent, 100);
        }
    }

    public void init() {
        ShakeDetectManager.getShakeDetectManager().init();
    }

    @Override
    public void registerShake(String screenName, Activity activity) {
        ShakeDetectManager.getShakeDetectManager().registerShake(screenName, activity);
    }

    @Override
    public void unregisterShake() {
        ShakeDetectManager.getShakeDetectManager().unregisterShake();
    }

    @Override
    public Intent getKolCommentActivity(Context context, int postId, int rowNumber) {
        return KolCommentActivity.getCallingIntent(context, postId, rowNumber);
    }

    @Override
    public Intent getGroupChatIntent(Context context, String channelUrl) {
        return PlayActivity.getCallingIntent(context, channelUrl);
    }

    @Override
    public Intent getWithdrawIntent(Context context, boolean isSeller) {
        return WithdrawActivity.getCallingIntent(context, isSeller);
    }

    @Override
    public Fragment getChannelFragment(Bundle bundle) {
        return ChannelFragment.createInstance();
    }

    @Override
    public String getChannelFragmentTag() {
        return ChannelFragment.class.getSimpleName();
    }

    @Override
    public Intent getInboxChannelsIntent(Context context) {
        return InboxChatActivity.getChannelCallingIntent(context);
    }

    @Override
    public void openRedirectUrl(Activity activity, String url) {
        if (isSupportedDelegateDeepLink(url)) {
            actionApplinkFromActivity(activity, url);
        } else {
            activity.startActivity(getWebviewActivity(activity, url));
        }
    }

    @Override
    public Intent getShopPageIntent(Context context, String shopId) {
        return ShopPageInternalRouter.getShopPageIntent(context, shopId);
    }

    @Override
    public Intent getShopPageIntentByDomain(Context context, String domain) {
        return ShopPageInternalRouter.getShopPageIntentByDomain(context, domain);
    }

    @Override
    public Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        return ShopPageInternalRouter.getShoProductListIntent(context, shopId, keyword, etalaseId);
    }

    @Override
    public Intent getInstantLoanIntent(Context context) {
        return InstantLoanActivity.Companion.createIntent(context);
    }

    public Intent getOpenShopIntent(Context context) {
        return ShopOpenInternalRouter.getOpenShopIntent(context);
    }

    @Override
    public void logInvalidGrant(Response response) {
        AnalyticsLog.logInvalidGrant(this, legacyGCMHandler(), legacySessionHandler(), response.request().url().toString());

    }

    @Override
    public void openTokoPoint(Context context, String url) {
        context.startActivity(TokoPointWebviewActivity.getIntent(context, url));
    }

    @Override
    public void openTokopointWebview(Context context, String url, String title) {
        context.startActivity(TokoPointWebviewActivity.getIntentWithTitle(context, url, title));
    }

    public Intent getPromoDetailIntent(Context context, String slug) {
        return PromoDetailActivity.getCallingIntent(context, slug);
    }

    @Override
    public File writeImage(String filePath, int qualityProcentage) {
        return FileUtils.writeImageToTkpdPath(filePath, qualityProcentage);
    }

    @Override
    public void startAddProduct(Activity activity, String shopId) {
        goToAddProduct(activity);
    }

    @Override
    public boolean isIndicatorVisible() {
        return remoteConfig.getBoolean(TkpdInboxRouter.INDICATOR_VISIBILITY, false);
    }

    @Override
    public void sendTrackingGroupChatLeftNavigation() {
        getAnalyticTracker().sendEventTracking(GroupChatAnalytics.EVENT_NAME_CLICK_NAVIGATION_DRAWER,
                GroupChatAnalytics.EVENT_CATEGORY_LEFT_NAVIGATION,
                GroupChatAnalytics.EVENT_ACTION_CLICK_GROUP_CHAT,
                ""
        );
    }

    @Override
    public String getNotificationPreferenceConstant() {
        return Constants.Settings.NOTIFICATION_GROUP_CHAT;
    }

    @Override
    public void generateBranchLink(String channelId, String title, String contentMessage,
                                   String imgUrl, String shareUrl,
                                   Activity activity, final ShareListener listener) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setId(channelId)
                .setName(title)
                .setTextContent(title)
                .setDescription(contentMessage)
                .setImgUri(imgUrl)
                .setUri(shareUrl)
                .setType(LinkerData.GROUPCHAT_TYPE)
                .build();

        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                DataMapper.getLinkerShareData(shareData), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        listener.onGenerateLink(linkerShareData.getShareContents(), linkerShareData.getShareUri());
                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                }));
    }

    @Override
    public void updateMarketplaceCartCounter(TransactionRouter.CartNotificationListener listener) {
        CartComponentInjector.newInstance(this)
                .getGetMarketPlaceCartCounterUseCase()
                .executeWithSubscriber(this, listener);
    }

    @Override
    public void goToOldProductDetailPage(@NotNull Context context, @Nullable String productId,
                                         @Nullable String shopDomain, @Nullable String productKey,
                                         @Nullable String trackerAttribution, @Nullable String trackerListName) {
        ProductPass productPass = ProductPass.Builder.aProductPass()
                .setProductId(productId)
                .setShopDomain(shopDomain)
                .setProductKey(productKey)
                .setTrackerAttribution(trackerAttribution)
                .setTrackerListName(trackerListName)
                .build();
        context.startActivity(ProductInfoActivity.createInstance(context, productPass));
    }

    @Override
    public void shareGroupChat(Activity activity, String channelId, String title, String contentMessage, String imgUrl,
                               String shareUrl, String userId, String sharing) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setId(channelId)
                .setName(title)
                .setTextContent(contentMessage)
                .setDescription(contentMessage)
                .setImgUri(imgUrl)
                .setOgImageUrl(imgUrl)
                .setOgTitle(title)
                .setUri(shareUrl)
                .setSource(userId) // just using existing variable
                .setPrice(sharing) // here too
                .setType(LinkerData.GROUPCHAT_TYPE)
                .build();
        new DefaultShare(activity, shareData).show();
    }

    @Override
    public void shareFeed(Activity activity, String detailId, String url, String title, String
            imageUrl, String description) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setId(detailId)
                .setName(title)
                .setDescription(description)
                .setImgUri(imageUrl)
                .setUri(url)
                .setType(LinkerData.FEED_TYPE)
                .build();
        new DefaultShare(activity, shareData).show();
    }

    @Override
    public void sendAnalyticsGroupChat(String url, String error) {
        if (remoteConfig.getBoolean("groupchat_analytics", false)) {
            AnalyticsLog.logGroupChatWebSocketError(getAppContext(), url, error);
        }
    }

    @Override
    public Observable<TKPDMapParam<String, Object>> verifyEventPromo(com.tokopedia.usecase.RequestParams requestParams) {
        boolean isEventOMS = remoteConfig.getBoolean("event_oms_android", false);
        if (!isEventOMS) {
            return eventComponent.getVerifyCartWrapper().verifyPromo(requestParams);
        } else {
            return new PostVerifyCartWrapper(this, eventComponent.getPostVerifyCartUseCase())
                    .verifyPromo(requestParams);
        }
    }

    @Override
    public Observable<TKPDMapParam<String, Object>> verifyDealPromo(com.tokopedia.usecase.RequestParams requestParams) {
        return new PostVerifyCartWrapper(this, dealsComponent.getPostVerifyCartUseCase())
                .verifyPromo(requestParams);
    }

    @Override
    public void sharePromoLoyalty(Activity activity, PromoData promoData) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(ShareData.PROMO_TYPE)
                .setId(promoData.getSlug())
                .setName(promoData.getTitle())
                .setTextContent(promoData.getTitle()
                        + getString(com.tokopedia.loyalty.R.string.share_promo_additional_text))
                .setUri(promoData.getLink())
                .build();
        new DefaultShare(activity, shareData).show();
    }

    @Override
    public void shareDeal(Context context, String uri, String name, String imageUrl, String desktopUrl) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType("")
                .setName(name)
                .setUri(uri)
                .setDesktopUrl(desktopUrl)
                .setImgUri(imageUrl)
                .build();

        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                DataMapper.getLinkerShareData(shareData), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, linkerShareData.getUrl());
                        Intent intent = Intent.createChooser(share, getString(R.string.share_link));
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(intent);

                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                }));

    }

    @Override
    public String getUserPhoneNumber() {
        return SessionHandler.getPhoneNumber();
    }

    @Override
    public boolean isSupportApplink(String appLink) {
        return DeeplinkHandlerActivity.getApplinkDelegateInstance().supportsUri(appLink);
    }

    @Override
    public ApplinkUnsupported getApplinkUnsupported(Activity activity) {
        return new ApplinkUnsupportedImpl(activity);
    }

    @Override
    public ApplinkDelegate applinkDelegate() {
        return DeeplinkHandlerActivity.getApplinkDelegateInstance();
    }

    @Override
    public void goToCreateTopadsPromo(Context context, String productId, String shopId, String source) {
        Intent topadsIntent = context.getPackageManager()
                .getLaunchIntentForPackage(CustomerAppConstants.TOP_SELLER_APPLICATION_PACKAGE);
        UserSessionInterface userSession = new UserSession(this);
        if (topadsIntent != null) {
            goToApplinkActivity(context, TopAdsAppLinkUtil.createAppLink(userSession.getUserId(),
                    productId, shopId, source));
        } else {
            goToCreateMerchantRedirect(context);
            UnifyTracking.eventTopAdsSwitcher(this, AppEventTracking.Category.SWITCHER);
        }
    }

    @Override
    public void setCartCount(Context context, int count) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CartConstant.CART);
        localCacheHandler.putInt(CartConstant.CACHE_TOTAL_CART, count);
        localCacheHandler.applyEditor();
    }

    @Override
    public void sendAnalyticsFirstTime() {
        TrackingUtils.activityBasedAFEvent(this, HomeRouter.IDENTIFIER_HOME_ACTIVITY);
    }

    @Override
    public int getCartCount(Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, CartConstant.CART);
        return localCacheHandler.getInt(CartConstant.CACHE_TOTAL_CART, 0);
    }

    @Override
    public void goToApplinkActivity(Context context, String applink) {
        if (context != null) {
            if (context instanceof Activity) {
                goToApplinkActivity((Activity) context, applink, new Bundle());
            } else {
                Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
                intent.setData(Uri.parse(applink));
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void goToApplinkActivity(Activity activity, String applink, Bundle bundle) {
        if (activity != null) {
            ApplinkDelegate deepLinkDelegate = DeeplinkHandlerActivity.getApplinkDelegateInstance();
            Intent intent = activity.getIntent();
            intent.setData(Uri.parse(applink));
            intent.putExtras(bundle);
            deepLinkDelegate.dispatchFrom(activity, intent);
        }
    }

    @Override
    public Intent getApplinkIntent(Context context, String applink) {
        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        intent.setData(Uri.parse(applink));

        if (context instanceof Activity) {
            try {
                return DeeplinkHandlerActivity.getApplinkDelegateInstance().getIntent((Activity) context, applink);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return intent;
    }

    @Override
    public Observable<VoucherViewModel> checkFlightVoucher(String voucherCode, String cartId, String isCoupon) {
        FlightRepository flightRepository = FlightComponentInstance.getFlightComponent(this).flightRepository();
        FlightCheckVoucherCodeUseCase checkVoucherCodeUseCase = new FlightCheckVoucherCodeUseCase(flightRepository);
        return checkVoucherCodeUseCase.createObservable(checkVoucherCodeUseCase.createRequestParams(cartId, voucherCode, isCoupon))
                .map(new Func1<AttributesVoucher, VoucherViewModel>() {
                    @Override
                    public VoucherViewModel call(AttributesVoucher attributesVoucher) {
                        VoucherViewModel voucherViewModel = new VoucherViewModel();
                        voucherViewModel.setCode(attributesVoucher.getVoucherCode());
                        voucherViewModel.setMessage(attributesVoucher.getMessage());
                        voucherViewModel.setRawDiscount((long) attributesVoucher.getDiscountAmountPlain());
                        voucherViewModel.setRawCashback((long) attributesVoucher.getCashbackAmountPlain());
                        voucherViewModel.setSuccess(true);
                        return voucherViewModel;
                    }
                });
    }

    @Override
    public Observable<VoucherViewModel> checkTrainVoucher(String trainReservationId,
                                                          String trainReservationCode,
                                                          String galaCode) {
        TrainRepository trainRepository = TrainComponentUtils.getTrainComponent(this).trainRepository();
        TrainCheckVoucherUseCase trainCheckVoucherUseCase = new TrainCheckVoucherUseCase(trainRepository);
        return trainCheckVoucherUseCase.createObservable(trainCheckVoucherUseCase.createRequestParams(
                trainReservationId, trainReservationCode, galaCode
        )).map(trainCheckVoucherModel -> new VoucherViewModel(
                true,
                trainCheckVoucherModel.getMessage(),
                null,
                trainCheckVoucherModel.getVoucherCode(),
                ((long) trainCheckVoucherModel.getDiscountAmountPlain()),
                ((long) trainCheckVoucherModel.getCashbackAmountPlain())
        ));
    }

    @Override
    public void trainSendTrackingOnClickUseVoucherCode(String voucherCode) {
        TrainAnalytics trainAnalytics = new TrainAnalytics(getAnalyticTracker(), new TrainDateUtil());
        trainAnalytics.eventClickUseVoucherCode(voucherCode);
    }

    @Override
    public void trainSendTrackingOnCheckVoucherCodeError(String errorMessage) {
        TrainAnalytics trainAnalytics = new TrainAnalytics(getAnalyticTracker(), new TrainDateUtil());
        trainAnalytics.eventVoucherError(errorMessage);
    }

    @Override
    public Intent getPromoListIntent(Activity activity, String menuId, String subMenuId) {
        return PromoListActivity.newInstance(activity, menuId, subMenuId);
    }

    @Override
    public Intent getPromoListIntent(Activity activity) {
        return PromoListActivity.newInstance(activity);
    }

    @Override
    public Intent getChangeNameIntent(Context context) {
        return ChangeNameActivity.newInstance(context);
    }

    @Override
    public Intent getAddEmailIntent(Context context) {
        return AddEmailActivity.newInstance(context);
    }

    @Override
    public Intent getAddPasswordIntent(Context context) {
        return AddPasswordActivity.newInstance(context);
    }

    @Override
    public boolean isSaldoNativeEnabled() {
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.SALDO_PRIORITAS_NATIVE_ANDROID,
                true);
    }

    @Override
    public Intent getTopProfileIntent(Context context, String userId) {
        return ProfileActivity.Companion.createIntent(context, userId);
    }

    @Override
    public String getDesktopLinkGroupChat() {
        return ChatroomUrl.DESKTOP_URL;
    }

    @Override
    public void gotoTopAdsDashboard(Context context) {
        Intent intent = new Intent(context, TopAdsDashboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public String getContactUsBaseURL() {
        return TkpdBaseURL.ContactUs.URL_HELP;
    }

    @Override
    public Intent getChatBotIntent(Context context, String messageId) {
        return RouteManager.getIntent(context, ApplinkConst.CHATBOT
                .replace(String.format("{%s}", ApplinkConst.Chat.MESSAGE_ID), messageId));
    }

    public UseCase<String> setCreditCardSingleAuthentication() {
        return new CreditCardFingerPrintUseCase();
    }

    @Override
    public Intent getHelpUsIntent(Context context) {
        return ContactUsHomeActivity.getContactUsHomeIntent(context, new Bundle());
    }

    @Override
    public Intent transactionOrderDetailRouterGetIntentUploadAwb(String urlUpload) {
        return UploadAwbLogisticActivity.newInstance(this, urlUpload);
    }

    @Override
    public String logisticUploadRouterGetApplicationBuildFlavor() {
        return BuildConfig.FLAVOR;
    }

    @Override
    public Intent getDistrictRecommendationIntent(Activity activity, com.tokopedia.logisticdata.data.entity.address.Token token, boolean isFromMarketplaceCart) {
        if (isFromMarketplaceCart)
            return DistrictRecommendationActivity.createInstanceFromMarketplaceCart(activity, new TokenMapper().convertTokenModel(token));
        else
            return DistrictRecommendationActivity.createInstanceIntent(activity, new TokenMapper().convertTokenModel(token));
    }

    @Override
    public Intent getCodPageIntent(Context context, Data data) {
        return CodActivity.newIntent(context, data);
    }

    @Override
    public Intent getGeoLocationActivityIntent(Context context, LocationPass locationMap, boolean isFromMarketplaceCart) {
        return GeolocationActivity.createInstance(context, locationMap, isFromMarketplaceCart);
    }

    @Override
    public void goToShopReview(Context context, String shopId, String shopDomain) {
        ReputationTracking tracking = new ReputationTracking(this);
        UserSessionInterface userSession = new UserSession(this);
        tracking.eventClickSeeMoreReview(getString(R.string.review), shopId, userSession.getShopId().equals(shopId));
        context.startActivity(ReviewShopInfoActivity.createIntent(context, shopId, shopDomain));
    }

    @Override
    public void goToShopDiscussion(Context context, String shopId) {
        context.startActivity(ShopTalkActivity.Companion.createIntent(context, shopId));
    }

    @Override
    public void goToManageShipping(Context context) {
        context.startActivity(new Intent(context, EditShippingActivity.class));
    }

    @Override
    public void goToEditShop(Context context) {
        Intent intent = ShopSettingsInternalRouter.getShopSettingsBasicInfoActivity(context);
        UnifyTracking.eventManageShopInfo(context);

        context.startActivity(intent);
    }

    @Override
    public FingerprintModel getFingerprintModel() {
        return FingerprintModelGenerator.generateFingerprintModel(this);
    }

    @Override
    public void doRelogin(String newAccessToken) {
        SessionRefresh sessionRefresh = new SessionRefresh(newAccessToken);
        try {
            sessionRefresh.gcmUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLoginInactivePhoneLinkEnabled() {
        return remoteConfig.getBoolean("mainapp_login_inactive_phone_no_link", true)
                && android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public Intent getChangePhoneNumberRequestIntent(Context context, String userId, String oldPhoneNumber) {
        return ChangeInactiveFormRequestActivity.createIntentWithUserId(context, userId, oldPhoneNumber);
    }

    @Override
    public @NonNull
    Intent getManageShopEtalaseIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsEtalaseActivity(context);
    }

    @Override
    public @NonNull
    Intent getManageShopNotesIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsNotesActivity(context);
    }

    @Override
    public @NonNull
    Intent getManageShopBasicDataIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsBasicInfoActivity(context);
    }

    @Override
    public @NonNull
    Intent getManageShopLocationIntent(@NonNull Context context) {
        return ShopSettingsInternalRouter.getShopSettingsLocationActivity(context);
    }

    @Override
    public @NonNull
    Intent getDistrictRecommendationIntent(@NonNull Activity activity) {
        return DistrictRecommendationShopSettingsActivity.createInstance(activity);
    }

    /**
     * Global Nav Router
     */

    @Override
    public Intent gotoWishlistPage(Context context) {
        Intent intent = new Intent(context, SimpleHomeActivity.class);
        intent.putExtra(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
        return intent;
    }

    @Override
    public Intent gotoSearchPage(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    public Intent gotoQrScannerPage(boolean needResult) {
        return QrScannerActivity.newInstance(this, needResult);
    }


    @Override
    public Fragment getHomeFragment(boolean scrollToRecommendList) {
        return HomeInternalRouter.getHomeFragment(scrollToRecommendList);
    }

    @Override
    public Fragment getFeedPlusFragment(Bundle bundle) {
        return FeedPlusContainerFragment.newInstance(bundle);
    }

    @Override
    public Fragment getCartFragment(Bundle bundle) {
        return CartFragment.newInstance(bundle, CartFragment.class.getSimpleName());
    }

    @Override
    public Fragment getOfficialStoreFragment(Bundle bundle) {
        return ReactNativeOfficialStoreFragment.createInstance();
    }

    @Override
    public Fragment getEmptyCartFragment(String autoApplyMessage, String state, String titleDesc) {
        return EmptyCartFragment.newInstance(autoApplyMessage, EmptyCartFragment.class.getSimpleName(), state, titleDesc);
    }

    @Override
    public void goToHelpCenter(Context context) {
        context.startActivity(getHelpUsIntent(context));
    }

    @Override
    public Intent getManageProfileIntent(Context context) {
        return new Intent(context, ManagePeopleProfileActivity.class);
    }

    @Override
    public Intent getManageAddressIntent(Context context) {
        return new Intent(context, ManagePeopleAddressActivity.class);
    }

    @Override
    public void goToShopEditor(Context context) {
        UnifyTracking.eventManageShopInfo(context);
        Intent intent = ShopSettingsInternalRouter.getShopSettingsBasicInfoActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToManageShopShipping(Context context) {
        UnifyTracking.eventManageShopShipping(context);
        context.startActivity(new Intent(context, EditShippingActivity.class));
    }

    @Override
    public void goToManageShopEtalase(Context context) {
        UnifyTracking.eventManageShopEtalase(context);
        Intent intent = ShopSettingsInternalRouter.getShopSettingsEtalaseActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goTotManageShopNotes(Context context) {
        UnifyTracking.eventManageShopNotes(context);
        Intent intent = ShopSettingsInternalRouter.getShopSettingsNotesActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToManageShopLocation(Context context) {
        UnifyTracking.eventManageShopLocation(context);
        Intent intent = ShopSettingsInternalRouter.getShopSettingsLocationActivity(context);
        context.startActivity(intent);
    }

    @Override
    public void goToManageShopProduct(Context context) {
        goToManageProduct(context);
    }

    @Override
    public void goToManageCreditCard(Context context) {
        if (context instanceof Activity)
            goToUserPaymentList((Activity) context);
    }

    @Override
    public void goToTokoCash(String appLinkBalance, Activity activity) {
        WalletRouterUtil.navigateWallet(
                activity.getApplication(),
                activity,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkBalance,
                "",
                new Bundle()
        );
    }

    @Override
    public void goToSaldo(Context context) {

        if (remoteConfig.getBoolean(APP_ENABLE_SALDO_SPLIT, false)) {
            startSaldoDepositIntent(context);
        } else {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                    ApplinkConst.WebViewUrl.SALDO_DETAIL));
        }

        AnalyticsEventTrackingHelper.homepageSaldoClick(getApplicationContext(), SaldoDetailsInternalRouter.getSaldoClassName());
    }

    @Override
    public void startSaldoDepositIntent(Context context) {
        SaldoDetailsInternalRouter.startSaldoDepositIntent(context);
    }

    public Intent getInboxChatIntent(Context context) {
        return InboxChatActivity.getCallingIntent(context);
    }

    public Intent getInboxReviewIntent(Context context) {
        return InboxReputationActivity.getCallingIntent(context);
    }


    public Intent getInboxHelpIntent(Context context) {
        return InboxListActivity.getCallingIntent(context);
    }

    @Override
    public Intent getInboxTalkCallingIntent(Context context) {
        return InboxTalkActivity.Companion.createIntent(context);
    }

    @Override
    public Intent getInboxTicketCallingIntent(Context context) {
        return new Intent(context, InboxListActivity.class);
    }

    @Override
    public ApplicationUpdate getAppUpdate(Context context) {
        return new FirebaseRemoteAppUpdate(context);
    }

    @Override
    public Intent getOnBoardingIntent(Activity activity) {
        return new Intent(activity, NewOnboardingActivity.class);
    }


    @Override
    public String getStringRemoteConfig(String key) {
        return remoteConfig.getString(key, "");
    }

    @Override
    public String getStringRemoteConfig(String key, String defaultValue) {
        return remoteConfig.getString(key, defaultValue);
    }

    @Override
    public long getLongRemoteConfig(String key, long defaultValue) {
        return remoteConfig.getLong(key, defaultValue);
    }

    @Override
    public boolean getBooleanRemoteConfig(String key, boolean defaultValue) {
        return remoteConfig.getBoolean(key, defaultValue);
    }

    @Override
    public void sendOpenHomeEvent() {
        TrackingUtils.sendMoEngageOpenHomeEvent(getAppContext());
    }

    @Override
    public Intent getOldOfficialStore(Context context) {
        return OldReactNativeOfficialStoreActivity.getIntent(context);
    }

    @Override
    public void sendMoEngageOpenFeedEvent(boolean isEmptyFeed) {
        TrackingUtils.sendMoEngageOpenFeedEvent(getAppContext(), isEmptyFeed);
    }

    @Override
    public void setStringRemoteConfigLocal(String key, String value) {
        remoteConfig.setString(key, value);
    }

    @Override
    public AccountHomeInjection getAccountHomeInjection() {
        return new AccountHomeInjectionImpl();
    }

    @Override
    public Fragment getFavoriteFragment() {
        return FragmentFavorite.newInstance();
    }

    @Override
    public void sendIndexScreen(Activity activity, String screenName) {
        IndexScreenTracking.sendScreen(activity, () -> screenName);
    }

    public void doLogoutAccount(Activity activity) {
        new GlobalCacheManager().deleteAll();
        Router.clearEtalase(activity);
        DbManagerImpl.getInstance().removeAllEtalase();
        TrackingUtils.eventMoEngageLogoutUser(activity);
        SessionHandler.clearUserData(activity);
        NotificationModHandler notif = new NotificationModHandler(activity);
        notif.dismissAllActivedNotifications();
        NotificationModHandler.clearCacheAllNotification(activity);

        Intent intent;
        if (GlobalConfig.isSellerApp()) {
            intent = getHomeIntent(activity);
        } else if (GlobalConfig.isPosApp()) {
            intent = getLoginIntent(activity);
        } else {
            invalidateCategoryMenuData();
            intent = HomeRouter.getHomeActivity(activity);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        AppWidgetUtil.sendBroadcastToAppWidget(activity);
        new IndiSession(activity).doLogout();
        refreshFCMTokenFromForegroundToCM();
    }

    @Override
    public Intent getProfileSettingIntent(Context context) {
        return ManagePeopleProfileActivity.createIntent(context);
    }

    @Override
    public Intent createIntentProductVariant(Context context, ArrayList<ProductVariantByCatModel> productVariantByCatModelList,
                                             ProductVariantViewModel productVariant, int productPriceCurrency, double productPrice,
                                             int productStock, boolean officialStore, String productSku,
                                             boolean needRetainImage, ProductPictureViewModel productSizeChart, boolean hasOriginalVariantLevel1,
                                             boolean hasOriginalVariantLevel2, boolean hasWholesale) {
        return ProductVariantDashboardActivity.getIntent(context, productVariantByCatModelList, productVariant,
                productPriceCurrency, productPrice, productStock, officialStore, productSku, needRetainImage, productSizeChart,
                hasOriginalVariantLevel1, hasOriginalVariantLevel2, hasWholesale);
    }

    @Override
    public Intent getManageProductIntent(Context context) {
        return new Intent(context, ProductManageActivity.class);
    }

    @Override
    public Intent createIntentProductEtalase(Context context, int etalaseId) {
        return EtalasePickerActivity.createInstance(context, etalaseId);
    }

    @Override
    public Intent getCategoryPickerIntent(Context context, int categoryId) {
        return CategoryPickerActivity.createIntent(context, categoryId);
    }

    @Override
    public Intent getOrderListIntent(Context context) {
        return OrderListActivity.getInstance(context);
    }

    @Override
    public Fragment getFlightOrderListFragment() {
        return FlightOrderListFragment.createInstance();
    }

    public void onShowRationale(Context context, PermissionRequest request, String permission) {
        RequestPermissionUtil.onShowRationale(context, request, permission);
    }

    public void onPermissionDenied(Context context, String permission) {
        RequestPermissionUtil.onPermissionDenied(context, permission);
    }

    public void onNeverAskAgain(Context context, String permission) {
        RequestPermissionUtil.onNeverAskAgain(context, permission);
    }


    @Override
    public void logoutToHome(Activity activity) {
        //From DialogLogoutFragment
        if (activity != null) {
            new GlobalCacheManager().deleteAll();
            Router.clearEtalase(activity);
            DbManagerImpl.getInstance().removeAllEtalase();
            TrackingUtils.eventMoEngageLogoutUser(this);
            SessionHandler.clearUserData(activity);
            NotificationModHandler notif = new NotificationModHandler(activity);
            notif.dismissAllActivedNotifications();
            NotificationModHandler.clearCacheAllNotification(activity);

            invalidateCategoryMenuData();
            onLogout(getApplicationComponent());
            mIris.setUserId("");

            Intent intent = getHomeIntent(activity);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            AppWidgetUtil.sendBroadcastToAppWidget(activity);
        }
    }

    @Override
    public boolean isEnableInterestPick() {
        return remoteConfig.getBoolean("mainapp_enable_interest_pick", Boolean.TRUE);
    }

    @Override
    public Intent getSettingBankIntent(Context context) {
        return SettingBankActivity.Companion.createIntent(context);
    }

    @Override
    public void generateBranchUrlForChallenge(Activity context, String url, String title, String channel, String og_url, String og_title, String og_desc, String og_image, String deepLink, final BranchLinkGenerateListener listener) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(LinkerData.INDI_CHALLENGE_TYPE)
                .setName(title)
                .setUri(url)
                .setSource(channel)
                .setOgUrl(og_url)
                .setOgTitle(og_title)
                .setDescription(og_desc)
                .setOgImageUrl(og_image)
                .setDeepLink(deepLink)
                .build();

        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                DataMapper.getLinkerShareData(shareData), new ShareCallback() {
                    @Override
                    public void urlCreated(LinkerShareResult linkerShareData) {
                        listener.onGenerateLink(linkerShareData.getShareContents(), linkerShareData.getShareUri());
                    }

                    @Override
                    public void onError(LinkerError linkerError) {

                    }
                }));
    }

    @Override
    public void shareBranchUrlForChallenge(Activity context, String packageName, String url, String shareContents) {
        ShareSocmedHandler.ShareBranchUrl(context, packageName, "text/plain", url, shareContents);
    }

    @Override
    public void sendMoengageEvents(String eventName, Map<String, Object> values) {
        TrackingUtils.sendMoEngageEvents(this, eventName, values);
    }

    @Override
    public Intent getMitraToppersActivityIntent(Context context) {
        return MitraToppersRouterInternal.getMitraToppersActivityIntent(context);
    }

    @Override
    public void sendEventTrackingWithShopInfo(String event, String category, String action, String label, String shopId, boolean isGoldMerchant, boolean isOfficialStore) {
        // ignore
    }

    public boolean isFeedShopPageEnabled() {
        return remoteConfig.getBoolean("mainapp_enable_feed_shop_page", Boolean.TRUE);
    }

    @Override
    public String getResourceUrlAssetPayment() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(getApplicationContext());
        String baseUrl = remoteConfig.getString(RemoteConfigKey.IMAGE_HOST,
                TkpdBaseURL.Payment.DEFAULT_HOST);

        final String resourceUrl = baseUrl + TkpdBaseURL.Payment.CDN_IMG_ANDROID_DOMAIN;
        return resourceUrl;
    }

    @Override
    public Intent getIntentOtpPageVerifCreditCard(Context context, String phoneNumber) {
        return VerificationActivity.getCallingIntent(context, phoneNumber, RequestOtpUseCase.OTP_TYPE_VERIFY_AUTH_CREDIT_CARD,
                false, RequestOtpUseCase.MODE_SMS);
    }

    @Override
    public Intent getProductTalk(Context context, String productId) {
        return TalkProductActivity.Companion.createIntent(context, productId);
    }

    @Override
    public void eventClickFilterReview(Context context,
                                       String filterName,
                                       String productId) {
        ProductPageTracking.eventClickFilterReview(
                context,
                filterName,
                productId
        );
    }

    @Override
    public void eventImageClickOnReview(Context context,
                                        String productId,
                                        String reviewId) {
        ProductPageTracking.eventClickImageOnReviewList(
                context,
                productId,
                reviewId
        );
    }

    @Override
    public Intent getShopTalkIntent(Context context, String shopId) {
        return ShopTalkActivity.Companion.createIntent(context, shopId);
    }

    @Override
    public Intent getTalkDetailIntent(Context context, String talkId, String shopId,
                                      String source) {
        return TalkDetailsActivity.getCallingIntent(talkId, shopId, context, source);
    }

    @Override
    public Fragment getFavoritedShopFragment(String userId) {
        return PeopleFavoritedShopFragment.createInstance(userId);
    }

    @Override
    public String getAppsFlyerID() {
        return TrackingUtils.getAfUniqueId(this);
    }

    @Override
    public String getUserId() {
        UserSessionInterface userSession = new UserSession(this);
        return userSession.getUserId();
    }

    public void sendAFCompleteRegistrationEvent(int userId, String methodName) {
        UnifyTracking.sendAFCompleteRegistrationEvent(this, userId, methodName);
    }

    public void onAppsFlyerInit() {
        TkpdAppsFlyerMapper.getInstance(this).mapAnalytics();
    }

    public void instabugCaptureUserStep(Activity activity, MotionEvent me) {
        InstabugInitalize.dispatchTouchEvent(activity, me);
    }

    @Override
    public boolean isAllowLogOnChuckInterceptorNotification() {
        LocalCacheHandler cache = new LocalCacheHandler(this, DeveloperOptions.CHUCK_ENABLED);
        return cache.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false);
    }

    public String getDefferedDeeplinkPathIfExists() {
        String dd4Seesion = AppsflyerContainer.getDefferedDeeplinkPathIfExists();
        return dd4Seesion;
    }

    @Override
    public void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain, String shopLocation, boolean isShopOfficaial, boolean isFollowed) {
        TrackingUtils.sendMoEngageFavoriteEvent(getAppContext(), shopName, shopID, shopDomain, shopLocation, isShopOfficaial, isFollowed);
    }

    @Override
    public void setTrackingUserId(String userId, Context applicationContext) {
        onAppsFlyerInit();
        TrackingUtils.eventPushUserID(getAppContext(), getTkpdCoreRouter().legacySessionHandler().getGTMLoginID());
        if (!BuildConfig.DEBUG && Crashlytics.getInstance() != null)
            Crashlytics.setUserIdentifier(userId);
        UserSessionInterface userSession = new UserSession(this);

        if (userSession.isLoggedIn()) {
            UserData userData = new UserData();
            userData.setUserId(userSession.getUserId());
            userData.setEmail(userSession.getEmail());
            userData.setPhoneNumber(userSession.getPhoneNumber());

            //Identity Event
            LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY, userData));

            //Login Event
            LinkerManager.getInstance().sendEvent(
                    LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGIN_VAL, userData));
        }
        mIris.setUserId(userId);
        mIris.setDeviceId(userSession.getDeviceId());
    }

    @Override
    public void setMoEUserAttributesLogin(String userId, String name, String email,
                                          String phoneNumber, boolean isGoldMerchant,
                                          String shopName, String shopId, boolean hasShop,
                                          String loginMethod) {
        TrackingUtils.setMoEUserAttributesLogin(getAppContext(),
                userId,
                name,
                email,
                phoneNumber,
                isGoldMerchant,
                shopName,
                shopId,
                hasShop,
                loginMethod
        );
    }

    @Override
    public void eventMoRegistrationStart(String label) {
        UnifyTracking.eventMoRegistrationStart(getAppContext(), label);
    }

    @Override
    public void eventMoRegister(String name, String phone) {
        UnifyTracking.eventMoRegister(getAppContext(), name, phone);
    }

    @Override
    public void sendBranchRegisterEvent(String email, String phone) {
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPhoneNumber(phone);
        LinkerManager.getInstance().sendEvent(
                LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_REGISTRATION_VAL, userData));
    }

    /**
     * App Rating - Nps
     */
    @Override
    public void showAdvancedAppRatingDialog(Activity activity, DialogInterface.OnDismissListener dismissListener) {
        AdvancedAppRatingDialog.show(activity, dismissListener);
    }

    @Override
    public void showSimpleAppRatingDialog(Activity activity) {
        SimpleAppRatingDialog.show(activity);
    }

    @Override
    public void sendEventCouponPageClosed() {
        UnifyTracking.eventCouponPageClosed(this);
    }

    @Override
    public void sendEventMyCouponClicked() {
        UnifyTracking.eventMyCouponClicked(this);
    }

    @Override
    public void sendEventCouponChosen(Context context, String title) {
        UnifyTracking.eventCouponChosen(this, title);
    }

    @Override
    public void sendEventDigitalEventTracking(Context context, String text, String failmsg) {
        UnifyTracking.eventDigitalEventTracking(this, text, failmsg);
    }

    @Override
    @NonNull
    public Intent getTopAdsDetailShopIntent(@NonNull Context context) {
        return new Intent();
    }

    @Override
    @NonNull
    public Intent getTopAdsKeywordListIntent(@NonNull Context context) {
        return new Intent();
    }

    @Override
    @NonNull
    public Intent getTopAdsAddingPromoOptionIntent(@NonNull Context context) {
        return new Intent();
    }

    @Override
    @NonNull
    public Intent getTopAdsProductAdListIntent(@NonNull Context context) {
        return new Intent();
    }

    @Override
    @NonNull
    public Intent getTopAdsGroupAdListIntent(@NonNull Context context) {
        return new Intent();
    }

    @Override
    @NonNull
    public Intent getTopAdsGroupNewPromoIntent(@NonNull Context context) {
        return new Intent();
    }

    @Override
    @NonNull
    public Intent getTopAdsKeywordNewChooseGroupIntent(@NonNull Context context, boolean isPositive, String groupId) {
        return new Intent();
    }

    @Override
    public void eventTopAdsProductClickKeywordDashboard() {
    }

    @Override
    public void eventTopAdsProductClickProductDashboard() {
    }

    @Override
    public void eventTopAdsProductClickGroupDashboard() {
    }

    @Override
    public void eventTopAdsProductAddBalance() {
        UnifyTracking.eventTopAdsProductAddBalance(getAppContext());
    }

    @Override
    public void eventTopAdsShopChooseDateCustom() {
        UnifyTracking.eventTopAdsShopChooseDateCustom(getAppContext());
    }

    @Override
    public void eventTopAdsShopDatePeriod(@NonNull String label) {
        UnifyTracking.eventTopAdsShopDatePeriod(getAppContext(), label);
    }

    @Override
    public void eventTopAdsProductStatisticBar(@NonNull String label) {
        UnifyTracking.eventTopAdsProductStatisticBar(getAppContext(), label);
    }

    @Override
    public void eventTopAdsShopStatisticBar(@NonNull String label) {
        UnifyTracking.eventTopAdsShopStatisticBar(getAppContext(), label);
    }

    @Override
    public void eventOpenTopadsPushNotification(@NonNull String label) {
        UnifyTracking.eventOpenTopadsPushNotification(getAppContext(), label);
    }

    @Override
    public void openTopAdsDashboardApplink(@NonNull Context context) {
        Intent topadsIntent = context.getPackageManager()
                .getLaunchIntentForPackage(CustomerAppConstants.TOP_SELLER_APPLICATION_PACKAGE);

        if (topadsIntent != null) {
            goToApplinkActivity(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD);
        } else {
            goToCreateMerchantRedirect(context);
        }
    }

    @NotNull
    @Override
    public Intent getCartIntent(@NotNull Context context) {
        return TransactionCartRouter.createInstanceCartActivity(context);
    }

    public void onLoginSuccess() {
        refreshFCMTokenFromForegroundToCM();
        onAppsFlyerInit();

    }

    private void initCMPushNotification() {
        CMPushNotificationManager.getInstance().init(this);
        refreshFCMTokenFromBackgroundToCM(FCMCacheManager.getRegistrationId(this), false);
    }


    @Override
    public void refreshFCMTokenFromBackgroundToCM(String token, boolean force) {
        CMPushNotificationManager.getInstance().refreshTokenFromBackground(token, force);
    }

    @Override
    public void refreshFCMFromInstantIdService(String token) {
        CMPushNotificationManager.getInstance().refreshFCMTokenFromForeground(token, true);
    }

    @Override
    public void refreshFCMTokenFromForegroundToCM() {
        CMPushNotificationManager.getInstance()
                .refreshFCMTokenFromForeground(FCMCacheManager.getRegistrationId(this), true);
    }

    @Override
    public Intent getSaldoDepositIntent(Context context) {
        return null;
    }

    @Override
    public void eventReferralAndShare(Context context, String action, String label) {
        UnifyTracking.eventReferralAndShare(context, action, label);
    }

    @Override
    public void setBranchReferralCode(String referralCode) {
        PersistentCacheManager persistentCacheManager =
                new PersistentCacheManager(context, TkpdCache.CACHE_PROMO_CODE);
        persistentCacheManager.put(TkpdCache.Key.KEY_CACHE_PROMO_CODE, referralCode);
    }

    @Override
    public void sendMoEngageReferralScreenOpen(Context context, String screenName) {
        TrackingUtils.sendMoEngageReferralScreenOpen(context, screenName);
    }

    @Override
    public void executeDefaultShare(Activity activity, HashMap<String, String> keyValueMap) {
        new DefaultShare(activity, createShareDataFromHashMap(keyValueMap)).show();
    }

    @Override
    public void executeShareSocmedHandler(Activity activity, HashMap<String, String> keyValueMap, String packageName) {
        ShareSocmedHandler.ShareSpecific(createShareDataFromHashMap(keyValueMap), activity, packageName,
                "text/plain", null, null);
    }

    @Override
    public void sendAnalyticsToGTM(Context context, String type, String channel) {
        if (type.equals(ShareData.REFERRAL_TYPE)) {
            eventReferralAndShare(context,
                    com.tokopedia.referral.Constants.Values.Companion.SELECT_CHANNEL, channel);
            TrackingUtils.sendMoEngageReferralShareEvent(context.getApplicationContext(), channel);
        } else if (type.equals(ShareData.APP_SHARE_TYPE)) {
            UnifyTracking.eventAppShareWhenReferralOff(context.getApplicationContext(), com.tokopedia.referral.Constants.Values.Companion.SELECT_CHANNEL, channel);
        } else {
            UnifyTracking.eventShare(context.getApplicationContext(), channel);
        }
    }

    private LinkerData createShareDataFromHashMap(HashMap<String, String> keyValueMap) {
        LinkerData shareData = LinkerData.Builder.getLinkerBuilder()
                .setType(keyValueMap.get(com.tokopedia.referral.Constants.Key.Companion.TYPE))
                .setId(keyValueMap.get(com.tokopedia.referral.Constants.Key.Companion.REFERRAL_CODE))
                .setName(keyValueMap.get(com.tokopedia.referral.Constants.Key.Companion.NAME))
                .setTextContent(keyValueMap.get(com.tokopedia.referral.Constants.Key.Companion.SHARING_CONTENT))
                .setUri(keyValueMap.get(com.tokopedia.referral.Constants.Key.Companion.URI))
                .setShareUrl(keyValueMap.get(com.tokopedia.referral.Constants.Key.Companion.URL))
                .build();
        return shareData;
    }


    @Override
    public Intent gotoInboxMainPage(Context context) {
        return new Intent(context, InboxMainActivity.class);
    }

    @Override
    public Intent getMaintenancePageIntent() {
        return MaintenancePage.createIntentFromNetwork(getAppContext());
    }

    @Override
    public Intent getNoTokocashAccountIntent(Context context, String phoneNumber) {
        return NotConnectedTokocashActivity.getNoTokocashAccountIntent(context, phoneNumber);
    }

    @NotNull
    @Override
    public Intent getCheckRegisterPhoneNumberIntent(@NotNull Context context) {
        return CheckRegisterPhoneNumberActivity.getCallingIntent(context);
    }

    @NotNull
    @Override
    public Intent getChooseTokocashAccountIntent(@NotNull Context context, @NotNull ChooseTokoCashAccountViewModel data) {
        return ChooseTokocashAccountActivity.getCallingIntent(context, data);
    }

    @NotNull
    @Override
    public Intent getTokoCashOtpIntent(@NotNull Context context, @NotNull String phoneNumber,
                                       boolean canUseOtherMethod, @NotNull String defaultRequestMode) {
        return TokoCashOtpActivity.getCallingIntent(context, phoneNumber, canUseOtherMethod, defaultRequestMode);
    }

    @NotNull
    @Override
    public Intent getCheckLoginPhoneNumberIntent(@NotNull Context context) {
        return CheckLoginPhoneNumberActivity.getCallingIntent(context);
    }

    @SuppressLint("MissingPermission")
    @Override
    public BaseDaggerFragment getKYCCameraFragment(ActionCreator<HashMap<String, Object>, Integer> actionCreator,
                                                   ActionDataProvider<ArrayList<String>, Object> keysListProvider, int cameraType){
        Bundle bundle = new Bundle();
        BaseDaggerFragment baseDaggerFragment = null;
        switch (cameraType) {
            case KYC_CARDID_CAMERA:
                baseDaggerFragment = FragmentCardIdCamera.newInstance();
                bundle.putSerializable(FragmentCardIdCamera.ACTION_CREATOR_ARG, actionCreator);
                bundle.putSerializable(FragmentCardIdCamera.ACTION_KEYS_PROVIDER_ARG, keysListProvider);
                baseDaggerFragment.setArguments(bundle);
            break;
            case KYC_SELFIEID_CAMERA:
                baseDaggerFragment = new FragmentSelfieIdCamera();
                bundle.putSerializable(FragmentSelfieIdCamera.ACTION_CREATOR_ARG, actionCreator);
                bundle.putSerializable(FragmentSelfieIdCamera.ACTION_KEYS_PROVIDER_ARG, keysListProvider);
                baseDaggerFragment.setArguments(bundle);
                break;
        }
        return baseDaggerFragment;
    }

    @Override
    public void openImagePreviewFromChat(@NotNull Context context, @NotNull ArrayList<String> listImage, @NotNull ArrayList<String> imageDesc, @NotNull String title, @NotNull String date) {

    }
}
