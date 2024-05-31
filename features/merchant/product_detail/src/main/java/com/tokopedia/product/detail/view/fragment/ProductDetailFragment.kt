package com.tokopedia.product.detail.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogFirstTrackId
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.AppLogParam.REQUEST_ID
import com.tokopedia.analytics.byteio.AppLogParam.TRACK_ID
import com.tokopedia.analytics.byteio.TrackStayProductDetail
import com.tokopedia.analytics.byteio.addVerticalTrackListener
import com.tokopedia.analytics.byteio.pdp.AppLogPdp
import com.tokopedia.analytics.byteio.pdp.AtcBuyType
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace
import com.tokopedia.analytics.performance.perf.bindFpsTracer
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_NEED_REFRESH
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.BOOLEAN_EXTRA_SUCCESS
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_PRODUCT_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.PATH_SRC
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.REQUEST_CODE_ADD_WISHLIST_COLLECTION
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.STRING_EXTRA_COLLECTION_ID
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.STRING_EXTRA_MESSAGE_TOASTER
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_BOTTOMSHEET
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.review.ReviewApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.common_tradein.utils.TradeInPDPHelper
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.product.preview.data.mapper.ProductPreviewSourceMapper
import com.tokopedia.content.product.preview.utils.enableRollenceContentProductPreview
import com.tokopedia.content.product.preview.view.activity.ProductPreviewActivity
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.device.info.permission.ImeiPermissionAsker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_REQUEST_CODE
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.createDefaultProgressDialog
import com.tokopedia.kotlin.extensions.view.getLocationOnScreen
import com.tokopedia.kotlin.extensions.view.hasValue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.network.utils.URLGenerator.generateURLSessionLogin
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.view.PdpFintechWidget.Companion.ACTIVATION_LINKINING_FLOW
import com.tokopedia.pdp.fintech.view.PdpFintechWidget.Companion.PARAM_CATEGORY_ID
import com.tokopedia.pdp.fintech.view.PdpFintechWidget.Companion.PARAM_PARENT_ID
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet.Companion.ACTIVATION_BOTTOMSHEET_DETAIl
import com.tokopedia.pdp.fintech.view.bottomsheet.GopayLinkBenefitBottomSheet.Companion.ACTIVATION_WEBVIEW_LINK
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.error.PlayWidgetException
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.product.detail.BuildConfig
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.PostAtcHelper
import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_AVAILABLE_VARIANT
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_IS_VARIANT_SELECTED
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_SHOP_ID
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.RQUEST_CODE_ACTIVATE_GOPAY
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.RQUEST_CODE_UPDATE_FINTECH_WIDGET
import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.product.detail.common.ProductEducationalHelper
import com.tokopedia.product.detail.common.ProductTrackingCommon
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.common.SingleClick
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.bottomsheet.OvoFlashDealsBottomSheet
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.CartRedirectionButtonsByteIOTracker
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.ICartRedirectionButtonsByteIOTracker
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.ar.ProductArInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton.Companion.buttonText
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.carttype.PostAtcLayout
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.mapIntoPromoExternalAutoApply
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.ShipmentPlus
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.postatc.PostAtcParams
import com.tokopedia.product.detail.common.showImmediately
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product.detail.common.showToasterSuccess
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.common.view.ProductDetailCoachMarkHelper
import com.tokopedia.product.detail.common.view.ProductDetailCommonBottomSheetBuilder
import com.tokopedia.product.detail.common.view.ProductDetailGalleryActivity
import com.tokopedia.product.detail.common.view.ProductDetailRestrictionHelper
import com.tokopedia.product.detail.component.shipment.ShipmentUiModel
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMerchantVoucherSummaryDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecomLayoutBasicData
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.datamodel.ShipmentPlusData
import com.tokopedia.product.detail.data.model.datamodel.TopAdsImageDataModel
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoAnnotationTrackData
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.data.model.ticker.TickerActionBs
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.ui.OneTimeMethodEvent
import com.tokopedia.product.detail.data.model.upcoming.NotifyMeUiData
import com.tokopedia.product.detail.data.util.ProductDetailAlreadyHit
import com.tokopedia.product.detail.data.util.ProductDetailAlreadySwipe
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ADD_WISHLIST
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ARG_AFFILIATE_SOURCE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ARG_AFFILIATE_SUB_IDS
import com.tokopedia.product.detail.data.util.ProductDetailConstant.CLICK_TYPE_WISHLIST
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_X_SOURCE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PLAY_CAROUSEL
import com.tokopedia.product.detail.data.util.ProductDetailConstant.REMOTE_CONFIG_DEFAULT_ENABLE_PDP_CUSTOM_SHARING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.REMOTE_CONFIG_KEY_ENABLE_PDP_CUSTOM_SHARING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.REMOVE_WISHLIST
import com.tokopedia.product.detail.data.util.ProductDetailConstant.WISHLIST_ERROR_TYPE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.WISHLIST_STATUS_KEY
import com.tokopedia.product.detail.data.util.ProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateAffiliateShareData
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateImageGeneratorData
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generatePersonalizedData
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateProductShareData
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateShareExBottomSheetArg
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateShareExInitializerArg
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateUserLocationRequestRates
import com.tokopedia.product.detail.data.util.ProductDetailMapper.zeroIfEmpty
import com.tokopedia.product.detail.data.util.ProductDetailSwipeTrackingState
import com.tokopedia.product.detail.data.util.ProductDetailTalkGoToReplyDiscussion
import com.tokopedia.product.detail.data.util.ProductDetailTalkGoToWriteDiscussion
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.VariantMapper
import com.tokopedia.product.detail.data.util.VariantMapper.generateVariantString
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.tracking.APlusContentTracking
import com.tokopedia.product.detail.tracking.BMGMTracking
import com.tokopedia.product.detail.tracking.CommonTracker
import com.tokopedia.product.detail.tracking.ContentWidgetTracker
import com.tokopedia.product.detail.tracking.ContentWidgetTracking
import com.tokopedia.product.detail.tracking.DynamicOneLinerTracking
import com.tokopedia.product.detail.tracking.GeneralInfoTracker
import com.tokopedia.product.detail.tracking.GeneralInfoTracking
import com.tokopedia.product.detail.tracking.OneLinersTracking
import com.tokopedia.product.detail.tracking.PageErrorTracker
import com.tokopedia.product.detail.tracking.PageErrorTracking
import com.tokopedia.product.detail.tracking.ProductArTrackerData
import com.tokopedia.product.detail.tracking.ProductArTracking
import com.tokopedia.product.detail.tracking.ProductDetailInfoTracking
import com.tokopedia.product.detail.tracking.ProductDetailNavigationTracker
import com.tokopedia.product.detail.tracking.ProductDetailNavigationTracking
import com.tokopedia.product.detail.tracking.ProductDetailServerLogger
import com.tokopedia.product.detail.tracking.ProductDetailStoriesWidgetTrackerBuilder
import com.tokopedia.product.detail.tracking.ProductShopReviewTracking
import com.tokopedia.product.detail.tracking.ProductSocialProofTracking
import com.tokopedia.product.detail.tracking.ProductThumbnailVariantTracking
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger.TOPADS_PDP_HIT_ADS_TRACKER
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger.TOPADS_PDP_IS_NOT_ADS
import com.tokopedia.product.detail.tracking.ShipmentTracking
import com.tokopedia.product.detail.tracking.ShopAdditionalTracking
import com.tokopedia.product.detail.tracking.ShopCredibilityTracker
import com.tokopedia.product.detail.tracking.ShopCredibilityTracking
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.activity.WholesaleActivity
import com.tokopedia.product.detail.view.adapter.diffutil.ProductDetailDiffUtilCallback
import com.tokopedia.product.detail.view.adapter.dynamicadapter.ProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.bottomsheet.ProductMediaRecomBottomSheetManager
import com.tokopedia.product.detail.view.bottomsheet.ShopStatusInfoBottomSheet
import com.tokopedia.product.detail.view.componentization.PdpComponentCallbackMediator
import com.tokopedia.product.detail.view.fragment.delegate.PdpCallbackDelegate
import com.tokopedia.product.detail.view.fragment.delegate.PdpCallbackDelegateImpl
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.fragment.partialview.TokoNowButtonData
import com.tokopedia.product.detail.view.listener.PartialButtonActionListener
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.util.PdpUiUpdater
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.util.ProductDetailErrorHelper
import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.detail.view.util.ProductDetailVariantLogic
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.viewholder.ProductSingleVariantViewHolder
import com.tokopedia.product.detail.view.viewholder.a_plus_content.APlusImageUiModel
import com.tokopedia.product.detail.view.viewholder.campaign.ui.model.UpcomingCampaignUiModel
import com.tokopedia.product.detail.view.viewholder.media.ProductMediaViewHolder
import com.tokopedia.product.detail.view.viewholder.media.tracker.MediaTracking
import com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.ProductThumbnailVariantViewHolder
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.ProductDetailViewModel
import com.tokopedia.product.detail.view.widget.NavigationTab
import com.tokopedia.product.detail.view.widget.ProductDetailNavigator.goToMvc
import com.tokopedia.product.detail.view.widget.ProductVideoCoordinator
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingBottomSheet
import com.tokopedia.product.info.util.ProductDetailBottomSheetBuilder
import com.tokopedia.product.info.util.ProductDetailInfoHelper
import com.tokopedia.product.info.view.bottomsheet.ProductDetailBottomSheetListener
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.product.util.processor.ProductDetailViewsBundler
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.recommendation_widget_common.affiliate.RecommendationNowAffiliateData
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.DEFAULT_QTY_1
import com.tokopedia.recommendation_widget_common.extension.PAGENAME_IDENTIFIER_RECOM_ATC
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationManager
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTracking
import com.tokopedia.recommendation_widget_common.widget.global.recommendationWidgetViewModel
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewItemData
import com.tokopedia.recommendation_widget_common.widget.viewtoview.ViewToViewTracker
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewBottomSheet
import com.tokopedia.referral.Constants
import com.tokopedia.referral.ReferralAction
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.shareexperience.domain.util.ShareExConstants.Rollence.ROLLENCE_SHARE_EX
import com.tokopedia.shareexperience.domain.util.ShareExConstants.Rollence.ROLLENCE_SHARE_EX_SA
import com.tokopedia.shareexperience.ui.util.ShareExInitializer
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.domain.entity.ShopPrefetchData
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.prefetch.ShopPagePrefetch
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersListener
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.stories.widget.NoAnimateAnimationStrategy
import com.tokopedia.stories.widget.NoCoachMarkStrategy
import com.tokopedia.stories.widget.StoriesWidgetManager
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.storiesManager
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_INDEFINITE
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.model.PersonalizedCampaignModel
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginAction
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import kotlinx.coroutines.flow.collectLatest
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import com.tokopedia.product.detail.common.R as productdetailcommonR

/**
 * Separator Rule
 * Without separator : ProductSnapshotViewHolder
 * Bottom separator : ProductVariantViewHolder, ProductNotifyMeViewHolder
 * Top separator : All of the view holder except above
 */
@Suppress("LateinitUsage")
open class ProductDetailFragment :
    BaseProductDetailFragment<DynamicPdpDataModel, ProductDetailAdapterFactoryImpl>(),
    ProductDetailListener,
    AtcVariantListener,
    PartialButtonActionListener,
    ProductDetailBottomSheetListener,
    PartialButtonShopFollowersListener,
    ScreenShotListener,
    PlayWidgetListener,
    PdpComponentCallbackMediator,
    PdpCallbackDelegate by PdpCallbackDelegateImpl(),
    ICartRedirectionButtonsByteIOTracker.Mediator,
    ICartRedirectionButtonsByteIOTracker by CartRedirectionButtonsByteIOTracker() {

    companion object {

        private const val DEBOUNCE_CLICK = 750
        private const val TOPADS_PERFORMANCE_CURRENT_SITE = "pdp"
        private const val FPS_TRACER_PDP = "Product Detail Scene"

        fun newInstance(
            productId: String? = null,
            warehouseId: String? = null,
            shopDomain: String? = null,
            productKey: String? = null,
            isFromDeeplink: Boolean = false,
            trackerAttribution: String? = null,
            trackerListName: String? = null,
            affiliateString: String? = null,
            affiliateUniqueId: String? = null,
            deeplinkUrl: String? = null,
            layoutId: String? = null,
            extParam: String? = null,
            query: String? = null,
            affiliateChannel: String? = null,
            campaignId: String? = null,
            variantId: String? = null,
            prefetchCacheId: String? = null,
            affiliateSubIds: Bundle? = null,
            affiliateSource: String? = null
        ) = ProductDetailFragment().also {
            it.arguments = Bundle().apply {
                productId?.let { pid -> putString(ProductDetailConstant.ARG_PRODUCT_ID, pid) }
                warehouseId?.let { whId -> putString(ProductDetailConstant.ARG_WAREHOUSE_ID, whId) }
                productKey?.let { pkey -> putString(ProductDetailConstant.ARG_PRODUCT_KEY, pkey) }
                shopDomain?.let { domain ->
                    putString(
                        ProductDetailConstant.ARG_SHOP_DOMAIN,
                        domain
                    )
                }
                trackerAttribution?.let { attribution ->
                    putString(
                        ProductDetailConstant.ARG_TRACKER_ATTRIBUTION,
                        attribution
                    )
                }
                trackerListName?.let { listName ->
                    putString(
                        ProductDetailConstant.ARG_TRACKER_LIST_NAME,
                        listName
                    )
                }
                affiliateString?.let { affiliateString ->
                    putString(
                        ProductDetailConstant.ARG_AFFILIATE_STRING,
                        affiliateString
                    )
                }
                affiliateUniqueId?.let { affiliateUniqueId ->
                    putString(
                        ProductDetailConstant.ARG_AFFILIATE_UNIQUE_ID,
                        affiliateUniqueId
                    )
                }
                affiliateChannel?.let { affiliateChannel ->
                    putString(
                        ProductDetailConstant.ARG_CHANNEL,
                        affiliateChannel
                    )
                }
                deeplinkUrl?.let { deeplinkUrl ->
                    putString(
                        ProductDetailConstant.ARG_DEEPLINK_URL,
                        deeplinkUrl
                    )
                }
                layoutId?.let { layoutId ->
                    putString(
                        ProductDetailConstant.ARG_LAYOUT_ID,
                        layoutId
                    )
                }
                extParam?.let { extParam ->
                    putString(
                        ProductDetailConstant.ARG_EXT_PARAM,
                        extParam
                    )
                }
                if (campaignId != null) {
                    putString(
                        ProductDetailConstant.ARG_CAMPAIGN_ID,
                        campaignId
                    )
                }
                if (variantId != null) {
                    putString(
                        ProductDetailConstant.ARG_VARIANT_ID,
                        variantId
                    )
                }
                if (prefetchCacheId != null) {
                    putString(
                        ProductDetailConstant.ARG_PREFETCH_CACHE_ID,
                        prefetchCacheId
                    )
                }
                putBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, isFromDeeplink)
                query?.let { qry -> putString(ProductDetailConstant.ARG_QUERY_PARAMS, qry) }

                affiliateSubIds?.let { subIds ->
                    putParcelable(ARG_AFFILIATE_SUB_IDS, subIds)
                }
                affiliateSource?.let { source -> putString(ARG_AFFILIATE_SOURCE, source) }
            }
        }
    }

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var affiliateCookieHelper: dagger.Lazy<AffiliateCookieHelper>

    private var sharedViewModel: ProductDetailSharedViewModel? = null
    private var screenshotDetector: ScreenshotDetector? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProductDetailViewModel::class.java]
    }
    private val recommendationWidgetViewModel by recommendationWidgetViewModel()

    private val nplFollowersButton: PartialButtonShopFollowersView? by lazy {
        binding?.baseBtnFollow?.root?.run {
            PartialButtonShopFollowersView.build(this, this@ProductDetailFragment)
        }
    }

    private val pdpCoachmarkHelper by lazy(LazyThreadSafetyMode.NONE) {
        context?.let {
            ProductDetailCoachMarkHelper(it)
        }
    }

    // Data
    private var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()

    // This productId is only use for backend hit
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var affiliateString: String? = null
    private var affiliateUniqueId: String = ""
    private var affiliateSubIds: MutableMap<String, String>? = null
    private var affiliateSource: String? = null
    private var deeplinkUrl: String = ""
    private var isFromDeeplink: Boolean = false
    private var layoutId: String = ""
    private var extParam: String = ""
    private var trackerAttributionPdp: String? = ""
    private var trackerListNamePdp: String? = ""
    private var warehouseId: String? = null
    private var doActivityResult = true
    private var recomWishlistItem: RecommendationItem? = null
    private var pdpUiUpdater: PdpUiUpdater? = PdpUiUpdater(mutableMapOf())
    private var alreadyPerformSellerMigrationAction = false
    private var alreadyHitSwipeTracker: ProductDetailSwipeTrackingState? = null
    private var alreadyHitVideoTracker: Boolean = false
    private var alreadyHitQtyTracker: Boolean = false
    private var shouldRefreshProductInfoBottomSheet = false
    private var shouldRefreshShippingBottomSheet = false
    private var uuid = ""
    private var urlQuery: String = ""
    private var affiliateChannel: String = ""
    private var campaignId: String = ""
    private var variantId: String = ""
    private var prefetchCacheId: String = ""
    private var hasApplogScrollListener: Boolean = false

    // Prevent several method at onResume to being called when first open page.
    private var firstOpenPage: Boolean? = null
    private var isAffiliateShareIcon = false

    // Share Experience
    private var shareExInitializer: ShareExInitializer? = null

    // View
    private lateinit var actionButtonView: PartialButtonActionView
    private var stickyLoginView: StickyLoginView? = null
    private var loadingProgressDialog: ProgressDialog? = null
    private var productVideoCoordinator: ProductVideoCoordinator? = null
    private val adapterFactory by lazy {
        ProductDetailAdapterFactoryImpl(
            listener = this,
            variantListener = this,
            userId = viewModel.userId,
            playWidgetCoordinator = PlayWidgetCoordinator(this).apply {
                setListener(this@ProductDetailFragment)
            },
            affiliateCookieHelper = affiliateCookieHelper.get(),
            pdpCallback = this
        )
    }

    private val adapter by lazy {
        val asyncDifferConfig: AsyncDifferConfig<DynamicPdpDataModel> =
            AsyncDifferConfig.Builder(ProductDetailDiffUtilCallback())
                .build()
        ProductDetailAdapter(asyncDifferConfig, this, adapterFactory)
    }

    private var infiniteRecommManager :InfiniteRecommendationManager? = null

    private var navToolbar: NavToolbar? = null

    private var buttonActionType: Int = 0
    private var isTopadsDynamicsSlottingAlreadyCharged = false

    private val tradeInDialog: DialogUnify by lazy {
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.pdp_tradein_text_request_access))
            setDescription(getString(R.string.pdp_tradein_text_permission_description))
            setPrimaryCTAText(getString(R.string.pdp_tradein_allow))
            setSecondaryCTAText(getString(R.string.pdp_tradein_back))
            setPrimaryCTAClickListener {
                viewModel.clearCacheP2Data()
                goToTradeInHome()
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
        }
    }

    private val recommendationCarouselPositionSavedState = SparseIntArray()

    private val irisSessionId by lazy {
        context?.let { IrisSession(it).getSessionId() } ?: ""
    }

    private val shareProductInstance by lazy {
        activity?.let {
            ProductShare(it, ProductShare.MODE_TEXT)
        }
    }

    private val compositeSubscription by lazy { CompositeSubscription() }

    private val mStoriesWidgetManager by storiesManager(StoriesEntryPoint.ProductDetail) {
        setScrollingParent(binding?.rvPdp)
        setAnimationStrategy(NoAnimateAnimationStrategy())
        setCoachMarkStrategy(NoCoachMarkStrategy())
        setTrackerBuilder(
            ProductDetailStoriesWidgetTrackerBuilder.create(productId.orEmpty(), getUserSession())
        )
    }

    private val productMediaRecomBottomSheetManager by lazyThreadSafetyNone {
        ProductMediaRecomBottomSheetManager(childFragmentManager, this)
    }

    private val enableContentProductPreview: Boolean
        get() = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CONTENT_PRODUCT_PREVIEW, false)

    override val rootView: Fragment
        get() = this

    override val pdpViewModel: ProductDetailViewModel
        get() = viewModel

    override val uiUpdater: PdpUiUpdater?
        get() = pdpUiUpdater

    override val queueTracker: TrackingQueue
        get() = trackingQueue

    override val recyclerViewPool: RecyclerView.RecycledViewPool?
        get() = getRecyclerView()?.recycledViewPool

    override val pdpRemoteConfig: RemoteConfig
        get() = remoteConfig

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productRecomm.init()
        if (prefetchCacheId.isEmpty()) showLoading()
        initBtnAction()

        navToolbar = view.findViewById(R.id.pdp_navtoolbar)
        navAbTestCondition({ initToolbarMainApp() }, { initToolbarSellerApp() })
        if (!viewModel.isUserSessionActive) initStickyLogin(view)
        screenshotDetector = context?.let {
            SharingUtil.createAndStartScreenShotDetector(
                it,
                this,
                this,
                addFragmentLifecycleObserver = true,
                permissionListener = shareProductInstance?.universalSharePermissionListener
            )
        }

        setPDPDebugMode()
        trackVerticalScroll()

        getRecyclerView()?.bindFpsTracer(FPS_TRACER_PDP)
    }

    private fun onClickDynamicOneLinerPromo() {
        val mvcData = viewModel.getP2()?.merchantVoucherSummary ?: return
        val p1 = viewModel.getProductInfoP1 ?: return
        goToMvc(
            shopId = p1.basic.shopID,
            productId = p1.basic.productID,
            mvcAdditionalData = mvcData.additionalData
        )
    }

    private fun goToMvc(shopId: String, productId: String, mvcAdditionalData: String) {
        val mContext = context ?: return
        val intent = TransParentActivity.getIntent(
            context = mContext,
            shopId = shopId,
            source = MvcSource.PDP,
            productId = productId,
            additionalParamJson = mvcAdditionalData
        )
        mvcLauncher.launch(intent)
    }

    private fun getPrefetchData(): ProductDetailPrefetch.Data? {
        val context = context ?: return null
        val cacheManager = SaveInstanceCacheManager(context.applicationContext, prefetchCacheId)
        return cacheManager.get(
            ProductDetailPrefetch.Data::class.java.simpleName,
            ProductDetailPrefetch.Data::class.java
        )
    }

    fun getStayAnalyticsData(): TrackStayProductDetail {
        val data = viewModel.getStayAnalyticsData()
        return data.copy(
            isSkuSelected = data.isSkuSelected ||
                pdpUiUpdater?.productSingleVariant?.mapOfSelectedVariant?.all { it.value != "0" }.orFalse()
        )
    }

    private fun setPDPDebugMode() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            val maxPressVolumeDown = 3
            var countPressVolumeDown = 0
            var currentMillis = 0L
            val interval = 1_000L // 1s

            (activity as? BaseActivity)?.addListener { keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    if (System.currentTimeMillis() - currentMillis < interval) {
                        countPressVolumeDown++

                        if (countPressVolumeDown == maxPressVolumeDown - 1) {
                            countPressVolumeDown = 0
                            showRequestIdToaster()
                        }
                    } else {
                        countPressVolumeDown = 0
                    }

                    currentMillis = System.currentTimeMillis()
                }
            }
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun showRequestIdToaster() {
        val view = binding?.root ?: return
        val requestID = viewModel.getProductInfoP1?.requestId.orEmpty()

        Toaster.build(view, "RequestID: $requestID", actionText = "Copy") {
            val clipboard = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("RequestID", requestID)
            clipboard.setPrimaryClip(clip)
        }.show()
    }

    override fun onSwipeRefresh() {
        recommendationCarouselPositionSavedState.clear()
        shouldRefreshProductInfoBottomSheet = true
        shouldRefreshShippingBottomSheet = true
        super.onSwipeRefresh()
    }

    override fun createAdapterInstance(): ProductDetailAdapter = adapter

    override fun getScreenName(): String = ""

    override fun initInjector() {
        productDaggerComponent = getComponent(ProductDetailComponent::class.java)
        productDaggerComponent?.inject(this)
    }

    override fun observeData() {
        observeP1()
        observeP2Data()
        observeP2Login()
        observeToggleFavourite()
        observeToggleNotifyMe()
        observeAddToCart()
        observeOnThumbnailVariantSelected()
        observeATCRecomData()
        observeATCTokonowResetCard()
        observeATCRecomTokonowNonLogin()
        observeATCRecomSendTracker()
        observeDiscussionData()
        observeP2Other()
        observeTopAdsImageData()
        observeVideoDetail()
        observeShippingAddressChanged()
        observeUpdateCart()
        observeMiniCart()
        observeTopAdsIsChargeData()
        observeDeleteCart()
        observePlayWidget()
        observeOneTimeMethod()
        observeProductMediaRecomData()
        observeBottomSheetEdu()
        observeAffiliateEligibility()
    }

    private fun observeBottomSheetEdu() {
        viewLifecycleOwner.observe(viewModel.showBottomSheetEdu) {
            val url = it?.appLink ?: return@observe
            viewModel.changeOneTimeMethod(
                event = OneTimeMethodEvent.ImpressGeneralEduBs(appLink = url)
            )
        }
    }

    private fun observeOneTimeMethod() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.oneTimeMethodState.collect {
                when (it.event) {
                    is OneTimeMethodEvent.ImpressRestriction -> {
                        ProductTrackingCommon.Restriction.impressLocationRestriction(
                            trackingQueue = trackingQueue,
                            data = it.event.reData,
                            userId = viewModel.userId,
                            shopId = viewModel.getProductInfoP1?.basic?.shopID ?: ""
                        )
                    }

                    is OneTimeMethodEvent.ImpressGeneralEduBs -> {
                        goToEducational(url = it.event.appLink)
                    }

                    else -> {
                        // noop
                    }
                }
            }
        }
    }

    private fun observeProductMediaRecomData() {
        viewModel.productMediaRecomBottomSheetState.observe(viewLifecycleOwner) {
            productMediaRecomBottomSheetManager.updateState(it)
        }
    }

    override fun loadData(forceRefresh: Boolean) {
        if (productId != null || (productKey != null && shopDomain != null)) {
            context?.let {
                (it as? ProductDetailActivity)?.startMonitoringPltNetworkRequest()
                EmbraceMonitoring.startMoments(ProductDetailConstant.PDP_RESULT_TRACE_P2_DATA)
                viewModel.getProductP1(
                    ProductParams(
                        productId = productId,
                        shopDomain = shopDomain,
                        productName = productKey,
                        warehouseId = warehouseId
                    ),
                    forceRefresh,
                    layoutId,
                    ChooseAddressUtils.getLocalizingAddressData(it),
                    urlQuery = urlQuery,
                    extParam = extParam,
                    prefetchData = getPrefetchData()
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            doActivityResult =
                savedInstanceState.getBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, true)
        }
        arguments?.let {
            productId = it.getString(ProductDetailConstant.ARG_PRODUCT_ID)
            warehouseId = it.getString(ProductDetailConstant.ARG_WAREHOUSE_ID)
            productKey = it.getString(ProductDetailConstant.ARG_PRODUCT_KEY)
            shopDomain = it.getString(ProductDetailConstant.ARG_SHOP_DOMAIN)
            trackerAttributionPdp = it.getString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION)
            trackerListNamePdp = it.getString(ProductDetailConstant.ARG_TRACKER_LIST_NAME)
            affiliateString = it.getString(ProductDetailConstant.ARG_AFFILIATE_STRING)
            affiliateUniqueId = it.getString(ProductDetailConstant.ARG_AFFILIATE_UNIQUE_ID, "")
            affiliateSource = it.getString(ARG_AFFILIATE_SOURCE)
            deeplinkUrl = it.getString(ProductDetailConstant.ARG_DEEPLINK_URL, "")
            isFromDeeplink = it.getBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, false)
            layoutId = it.getString(ProductDetailConstant.ARG_LAYOUT_ID, "")
            extParam = it.getString(ProductDetailConstant.ARG_EXT_PARAM, "")
            urlQuery = it.getString(ProductDetailConstant.ARG_QUERY_PARAMS, "")
            affiliateChannel = it.getString(ProductDetailConstant.ARG_CHANNEL, "")
            campaignId = it.getString(ProductDetailConstant.ARG_CAMPAIGN_ID, "")
            variantId = it.getString(ProductDetailConstant.ARG_VARIANT_ID, "")
            prefetchCacheId = it.getString(ProductDetailConstant.ARG_PREFETCH_CACHE_ID, "")

            processAffiliateSubIds(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable(
                        ARG_AFFILIATE_SUB_IDS,
                        Bundle::class.java
                    )
                } else {
                    it.getParcelable(ARG_AFFILIATE_SUB_IDS)
                }
            )
        }
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(ProductDetailSharedViewModel::class.java)
        }
        uuid = UUID.randomUUID().toString()
        firstOpenPage = true
        super.onCreate(savedInstanceState)

        ProductDetailServerLogger.logBreadCrumbFirstOpenPage(
            productId,
            shopDomain,
            productKey,
            context
        )
        assignDeviceId()
        loadData()
        registerCallback(mediator = this)
        registerCartRedirectionButtonsByteIOTracker(mediator = this)
        initializeShareEx()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // If the activity being destroyed and onActivityResult start afterward
        // Then just ignore onActivityResult with this variable
        outState.putBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, false)
    }

    override fun onPause() {
        super.onPause()
        trackVideoState()
        if (::trackingQueue.isInitialized) {
            if (alreadyHitSwipeTracker != null) {
                alreadyHitSwipeTracker = ProductDetailAlreadyHit
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadCartCounter()
        reloadUserLocationChanged()
        reloadMiniCart()
        reloadFintechWidget()
        val positionSku = (getViewHolderByPosition(0) as? ProductMediaViewHolder)?.mediaPosition
        positionSku?.let {
            if (it.second) {
                viewModel.skuPhotoViewed.add(it.first)
            } else {
                viewModel.mainPhotoViewed.add(it.first)
            }
        }
    }

    private fun reloadFintechWidget() {
        if (pdpUiUpdater == null || (
            pdpUiUpdater?.fintechWidgetMap == null &&
                pdpUiUpdater?.fintechWidgetV2Map == null
            )
        ) {
            return
        }
        if (pdpUiUpdater?.fintechWidgetMap?.isLoggedIn != viewModel.isUserSessionActive ||
            pdpUiUpdater?.fintechWidgetV2Map?.isLoggedIn != viewModel.isUserSessionActive
        ) {
            productId?.let {
                pdpUiUpdater?.updateFintechDataWithProductId(
                    it,
                    viewModel.userSessionInterface.isLoggedIn
                )
            }
            updateUi()
        }
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        hideProgressDialog()
        compositeSubscription.clear()
        super.onDestroyView()
    }

    private fun onResultVariantBottomSheet(data: ProductVariantResult) {
        if (data.shouldRefreshPreviousPage) {
            productId = data.selectedProductId
            // donot run onresume
            firstOpenPage = true
            onSwipeRefresh()
        } else {
            if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT) {
                updateCartNotification()
            } else if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_ATC_VAR_CHANGE_ADDRESS) {
                productId = data.selectedProductId
                onSuccessUpdateAddress()
                return
            }

            if (data.isFollowShop) {
                // vbs can only follow shop that NPL type
                onSuccessFavoriteShop(true, true)
            }

            pdpUiUpdater?.updateVariantSelected(data.mapOfSelectedVariantOption)
            val variantLevelOne = ProductDetailVariantLogic.determineVariant(
                data.mapOfSelectedVariantOption ?: mapOf(),
                viewModel.variantData
            )

            updateVariantDataAndUi(if (variantLevelOne != null) listOf(variantLevelOne) else listOf()) {
                if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_TRADEIN_PDP) {
                    onTradeinClickedAfter()
                }
            }

            scrollVariantToSelectedPosition()
        }
    }

    private fun activityResultAdultManager(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.let {
            AdultManager.handleActivityResult(
                it,
                requestCode,
                resultCode,
                data,
                object : AdultManager.Callback {
                    override fun onFail() {
                        it.finish()
                    }

                    override fun onVerificationSuccess(message: String?) {
                        message?.let {
                            view?.showToasterSuccess(
                                it,
                                ctaText = getString(productdetailcommonR.string.pdp_common_oke),
                                ctaListener = {}
                            )
                        }
                    }

                    override fun onLoginPreverified() {
                        if (doActivityResult) {
                            onSwipeRefresh()
                        }
                    }
                }
            )
        }
    }

    private fun activityResultTradeIn(data: Intent) {
        val deviceId = data.getStringExtra(TradeInPDPHelper.PARAM_DEVICE_ID) ?: ""
        val phoneType = data.getStringExtra(TradeInPDPHelper.PARAM_PHONE_TYPE) ?: ""
        val phonePrice = data.getStringExtra(TradeInPDPHelper.PARAM_PHONE_PRICE) ?: ""
        ProductDetailTracking.TradeIn.eventAddToCartFinalPrice(
            phoneType,
            phonePrice,
            deviceId,
            viewModel.userId,
            viewModel.getProductInfoP1
        )
        buyAfterTradeinDiagnose(deviceId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultAdultManager(requestCode, resultCode, data)

        context?.let {
            AtcVariantHelper.onActivityResultAtcVariant(it, requestCode, data) {
                onResultVariantBottomSheet(this)
            }
        }

        when (requestCode) {
            RQUEST_CODE_UPDATE_FINTECH_WIDGET, RQUEST_CODE_ACTIVATE_GOPAY -> {
                reloadFintechWidget()
            }

            ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE,
            ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST -> {
                data?.let {
                    activityResultTradeIn(it)
                }
            }

            ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT -> {
                updateCartNotification()
            }

            ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK && doActivityResult) {
                    onSwipeRefresh()
                }
            }

            ProductDetailConstant.REQUEST_CODE_LOGIN -> {
                hideProgressDialog()
                if (resultCode == Activity.RESULT_OK && doActivityResult) {
                    onSwipeRefresh()
                    stickyLoginView?.hide()
                }
                updateActionButtonShadow()

                if (resultCode == Activity.RESULT_OK && viewModel.userSessionInterface.isLoggedIn) {
                    when (viewModel.talkLastAction) {
                        is ProductDetailTalkGoToWriteDiscussion -> goToWriteActivity()
                        is ProductDetailTalkGoToReplyDiscussion -> goToReplyActivity((viewModel.talkLastAction as ProductDetailTalkGoToReplyDiscussion).questionId)
                        else -> {
                            // no-op
                        }
                    }
                }
            }

            ProductDetailConstant.REQUEST_CODE_REPORT -> {
                if (resultCode == Activity.RESULT_OK) {
                    view?.showToasterSuccess(getString(R.string.success_to_report))
                }
            }

            ProductDetailConstant.REQUEST_CODE_SHOP_INFO -> {
                if (data != null) {
                    val isFavoriteFromShopPage =
                        data.getBooleanExtra(ProductDetailConstant.SHOP_STATUS_FAVOURITE, false)
                    val isUserLoginFromShopPage =
                        data.getBooleanExtra(ProductDetailConstant.SHOP_STICKY_LOGIN, false)
                    val wasFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return

                    if (isUserLoginFromShopPage) {
                        stickyLoginView?.hide()
                    }
                    if (isFavoriteFromShopPage != wasFavorite) {
                        onSuccessFavoriteShop(true)
                    }
                }
            }

            ProductDetailConstant.REQUEST_CODE_TOP_CHAT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val favoriteData =
                        data.getStringExtra(ApplinkConst.Chat.SHOP_FOLLOWERS_CHAT_KEY)
                    if (favoriteData != null) {
                        val isFavoriteFromTopChat = favoriteData == "true"
                        val wasFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return

                        if (isFavoriteFromTopChat != wasFavorite) {
                            onSuccessFavoriteShop(true)
                        }
                    }
                }
            }

            PRODUCT_CARD_OPTIONS_REQUEST_CODE -> {
                handleProductCardOptionsActivityResult(
                    requestCode,
                    resultCode,
                    data,
                    object : ProductCardOptionsWishlistCallback {
                        override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                            handleWishlistAction(productCardOptionsModel)
                        }
                    }
                )
            }

            REQUEST_CODE_ADD_WISHLIST_COLLECTION -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val isSuccess = data.getBooleanExtra(BOOLEAN_EXTRA_SUCCESS, false)
                    val messageToaster =
                        data.getStringExtra(STRING_EXTRA_MESSAGE_TOASTER)
                    val collectionId =
                        data.getStringExtra(STRING_EXTRA_COLLECTION_ID)
                    if (messageToaster != null) {
                        if (isSuccess) {
                            view?.showToasterSuccess(
                                message = messageToaster,
                                ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist),
                                ctaListener = {
                                    if (collectionId != null) {
                                        goToWishlistCollection(collectionId)
                                    }
                                }
                            )
                        } else {
                            view?.showToasterError(messageToaster)
                        }
                    }
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun scrollVariantToSelectedPosition() {
        val singleVariant = pdpUiUpdater?.productSingleVariant ?: return

        if (singleVariant.isThumbnailType) {
            scrollThumbnailVariant()
        } else {
            scrollSingleVariant()
        }
    }

    private fun scrollSingleVariant() {
        val vh =
            getViewHolderByPosition(getComponentPositionBeforeUpdate(pdpUiUpdater?.productSingleVariant)) as? ProductSingleVariantViewHolder
        Handler(Looper.getMainLooper()).postDelayed({
            vh?.scrollToPosition(
                pdpUiUpdater?.productSingleVariant?.variantLevelOne?.getPositionOfSelected()
                    ?: -1
            )
        }, ProductDetailConstant.VARIANT_SCROLL_DELAY)
    }

    private fun trackVideoState() {
        if (!alreadyHitVideoTracker && productVideoCoordinator != null) {
            val videoTrackerData = viewModel.videoTrackerData
            val isAutoPlay =
                if (context == null) false else DeviceConnectionInfo.isConnectWifi(requireContext())

            videoTrackerData?.let {
                ProductDetailTracking.Click.eventVideoStateChange(
                    viewModel.getProductInfoP1,
                    viewModel.userId,
                    ProductDetailTracking.generateComponentTrackModel(
                        pdpUiUpdater?.mediaMap,
                        0
                    ),
                    videoTrackerData.first,
                    videoTrackerData.second,
                    isAutoPlay
                )
                alreadyHitVideoTracker = true
            }
        }
    }

    private fun reloadCartCounter() {
        activity?.run {
            if (isAdded) {
                navAbTestCondition({ setNavToolBarCartCounter() }, {
                    // no op
                })
            }
        }
    }

    private fun reloadMiniCart() {
        val hasQuantityEditor =
            viewModel.getProductInfoP1?.basic?.isTokoNow == true ||
                (viewModel.productLayout.value as? Success<List<DynamicPdpDataModel>>)
                ?.data
                ?.any { it.name().contains(PAGENAME_IDENTIFIER_RECOM_ATC) } == true

        if (viewModel.getProductInfoP1 == null ||
            context == null ||
            !hasQuantityEditor ||
            firstOpenPage == true ||
            !viewModel.isUserSessionActive
        ) {
            return
        }

        val data = viewModel.getProductInfoP1
        viewModel.getMiniCart(data?.basic?.shopID ?: "")
    }

    private fun reloadUserLocationChanged() {
        if (viewModel.getProductInfoP1 == null || context == null || firstOpenPage == null || firstOpenPage == true) return
        val isUserLocationChanged = ChooseAddressUtils.isLocalizingAddressHasUpdated(
            requireContext(),
            viewModel.getUserLocationCache()
        )
        if (isUserLocationChanged) {
            refreshPage()
            ProductDetailServerLogger.logBreadCrumbAddressChanged(context)
        }
    }

    private fun setNavToolBarCartCounter() {
        val localCacheHandler = LocalCacheHandler(context, CartConstant.CART)
        val cartCount = localCacheHandler.getInt(CartConstant.CACHE_TOTAL_CART, 0)
        navToolbar?.setBadgeCounter(
            IconList.ID_CART,
            if (cartCount > ProductDetailConstant.CART_MAX_COUNT) {
                getString(R.string.pdp_label_cart_count_max).toIntOrZero()
            } else if (!viewModel.isUserSessionActive) {
                0
            } else {
                cartCount
            }
        )
    }

    override fun onSeeMoreDescriptionClicked(
        infoData: ProductDetailInfoDataModel,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        activity?.let {
            ProductDetailTracking.Click.eventClickProductDescriptionReadMore(
                viewModel.getProductInfoP1,
                componentTrackDataModel
            )

            ProductDetailInfoHelper.showBottomSheetInfo(
                fragmentActivity = it,
                daggerComponent = productDaggerComponent,
                listener = this,
                p1Data = viewModel.getProductInfoP1,
                sizeChartImageUrl = viewModel.variantData?.sizeChart,
                infoData = infoData,
                forceRefresh = shouldRefreshProductInfoBottomSheet,
                isOpenSpecification = false
            )
            shouldRefreshProductInfoBottomSheet = false
        }
    }

    override fun onSeeMoreSpecificationClicked(
        infoData: ProductDetailInfoDataModel,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        activity?.let {
            ProductDetailTracking.Click.eventClickProductSpecificationReadMore(
                viewModel.getProductInfoP1,
                componentTrackDataModel
            )

            ProductDetailInfoHelper.showBottomSheetInfo(
                fragmentActivity = it,
                daggerComponent = productDaggerComponent,
                listener = this,
                p1Data = viewModel.getProductInfoP1,
                sizeChartImageUrl = viewModel.variantData?.sizeChart,
                infoData = infoData,
                forceRefresh = shouldRefreshProductInfoBottomSheet,
                isOpenSpecification = true
            )
            shouldRefreshProductInfoBottomSheet = false
        }
    }

    override fun onAnnotationOpenProductInfoSheet(
        extParam: String,
        trackData: ProductDetailInfoAnnotationTrackData
    ) {
        val activity = activity ?: return

        ProductDetailInfoHelper.showBottomSheetInfo(
            fragmentActivity = activity,
            daggerComponent = productDaggerComponent,
            listener = this,
            p1Data = viewModel.getProductInfoP1,
            sizeChartImageUrl = viewModel.variantData?.sizeChart,
            infoData = pdpUiUpdater?.productDetailInfoData ?: ProductDetailInfoDataModel(),
            forceRefresh = shouldRefreshProductInfoBottomSheet,
            isOpenSpecification = false,
            annotationExtParam = extParam
        )

        ProductDetailInfoTracking.onClickAnnotationGeneric(
            trackDataModel = trackData.copy(userId = viewModel.userId),
            productInfo = viewModel.getProductInfoP1
        )
    }

    override fun onAnnotationGenericImpression(trackData: ProductDetailInfoAnnotationTrackData) {
        ProductDetailInfoTracking.onImpressionAnnotationGeneric(
            trackDataModel = trackData.copy(userId = viewModel.userId),
            productInfo = viewModel.getProductInfoP1
        )
    }

    override fun getParentViewModelStoreOwner(): ViewModelStore {
        return viewModelStore
    }

    override fun getParentLifeCyclerOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    /**
     * ImpressionComponent
     */
    override fun onImpressComponent(componentTrackDataModel: ComponentTrackDataModel) {
        if (viewModel.getProductInfoP1?.cacheState?.isPrefetch == true) return

        val purchaseProtectionUrl = when (componentTrackDataModel.componentName) {
            ProductDetailConstant.PRODUCT_PROTECTION -> getPurchaseProtectionUrl()
            else -> ""
        }
        val promoId = when (componentTrackDataModel.componentName) {
            ProductDetailConstant.SHIPMENT_V2 -> getShipmentPlusText()
            else -> ""
        }

        ProductDetailTracking.Impression
            .eventImpressionComponent(
                trackingQueue = trackingQueue,
                componentTrackDataModel = componentTrackDataModel,
                productInfo = viewModel.getProductInfoP1,
                componentName = "",
                purchaseProtectionUrl = purchaseProtectionUrl,
                userId = viewModel.userId,
                lcaWarehouseId = getLcaWarehouseId(),
                promoId = promoId
            )
    }

    override fun onShopCredibilityImpressed(
        countLocation: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (countLocation.isNotEmpty()) {
            ProductDetailTracking.Impression
                .eventImpressionShopMultilocation(
                    trackingQueue = trackingQueue,
                    componentTrackDataModel = componentTrackDataModel,
                    productInfo = viewModel.getProductInfoP1,
                    shopCountLocation = countLocation,
                    userId = viewModel.userId,
                    lcaWarehouseId = getLcaWarehouseId()
                )
        }

        onImpressComponent(componentTrackDataModel)
    }

    /**
     * [ProductShopAdditionalViewHolder]
     */
    override fun onLearnButtonShopAdditionalClicked(
        componentTrackDataModel: ComponentTrackDataModel,
        eventLabel: String
    ) {
        val uspUrl = viewModel.p2Data.value?.uspImageUrl.orEmpty()

        if (uspUrl.isNotBlank()) {
            showUspBottomSheet(uspUrl = uspUrl)
            onShopAdditionalLearnButtonClickTracking(
                componentTrackDataModel = componentTrackDataModel,
                eventLabel = eventLabel
            )
        }
    }

    private fun onShopAdditionalLearnButtonClickTracking(
        componentTrackDataModel: ComponentTrackDataModel,
        eventLabel: String
    ) {
        ShopAdditionalTracking.clickLearnButton(
            component = componentTrackDataModel,
            productInfo = viewModel.getProductInfoP1,
            userId = viewModel.userId,
            eventLabel = eventLabel
        )
    }

    private fun showUspBottomSheet(uspUrl: String) {
        val ctx = context ?: return
        val tag = "bottom_sheet_unique_selling_point"

        showImmediately(childFragmentManager, tag) {
            ProductDetailCommonBottomSheetBuilder.getUspBottomSheet(
                context = ctx,
                uspTokoCabangImgUrl = uspUrl
            )
        }
    }

    /**
     * ProductShopInfoViewHolder
     */
    override fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel) {
        when (itemId) {
            R.id.shop_credibility_button_follow -> onShopFavoriteClick()
            R.id.shop_credibility_ava, R.id.shop_credibility_name -> gotoShopDetail(
                componentTrackDataModel
            )

            else -> {
            }
        }
    }

    override fun onShopMultilocClicked(componentTrackDataModel: ComponentTrackDataModel) {
        ProductDetailTracking.Click.onInformationIconMultiLocClicked(
            viewModel.getProductInfoP1,
            componentTrackDataModel,
            viewModel.userId
        )
    }

    override fun onCategoryCarouselImageClicked(
        url: String,
        categoryTitle: String,
        categoryId: String,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        ProductDetailTracking.Click.onImageCategoryCarouselClicked(
            viewModel.getProductInfoP1,
            componentTrackDataModel,
            categoryTitle,
            categoryId
        )
        goToApplink(url)
    }

    override fun onCategoryCarouselSeeAllClicked(
        url: String,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        ProductDetailTracking.Click.onSeeAllCategoryCarouselClicked(
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )
        val localizationWarehouseId = viewModel.getUserLocationCache().warehouse_id
        RouteManager.route(
            context,
            ApplinkConstInternalTokopediaNow.CATEGORY_LIST,
            localizationWarehouseId
        )
    }

    override fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (!GlobalConfig.isSellerApp()) {
            viewModel.getProductInfoP1?.basic?.category?.detail?.let {
                val categoryId = it.lastOrNull()?.id ?: ""
                val categoryName = it.lastOrNull()?.name ?: ""
                ProductDetailTracking.Click.eventCategoryClicked(
                    categoryId,
                    categoryName,
                    viewModel.getProductInfoP1,
                    componentTrackDataModel
                )
            }
            goToApplink(url)
        }
    }

    override fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        viewModel.getProductInfoP1?.basic?.menu?.let {
            val etalaseId = it.id
            val etalaseName = it.name
            ProductDetailTracking.Click.eventEtalaseClicked(
                etalaseId,
                etalaseName,
                viewModel.getProductInfoP1,
                componentTrackDataModel
            )
        }

        goToApplink(url)
    }

    override fun goToApplink(url: String) {
        RouteManager.route(context, url)
    }

    override fun goToArPage(componentTrackDataModel: ComponentTrackDataModel) {
        ProductArTracking.clickArComponent(
            data = ProductArTrackerData(
                productInfo = viewModel.getProductInfoP1,
                componentTrackDataModel = componentTrackDataModel
            )
        )
        val productId = viewModel.getProductInfoP1?.basic?.productID ?: ""
        val shopId = viewModel.getProductInfoP1?.basic?.shopID ?: ""

        val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_AR, productId)
        intent.putExtra(ProductDetailConstant.PARAM_SHOP_ID, shopId)
        startActivity(intent)
    }

    override fun goToEducational(url: String) {
        val context = context ?: return
        ProductEducationalHelper.goToEducationalBottomSheet(
            context,
            url,
            productId ?: "",
            viewModel.getProductInfoP1?.basic?.shopID ?: ""
        )
    }

    override fun showOneLinersImsCoachMark(
        view: ImageUnify?
    ) {
        pdpCoachmarkHelper?.showCoachMarkOneLiners(view)
    }

    override fun onBbiInfoClick(
        url: String,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (url.isNotEmpty()) {
            ProductDetailTracking.Click.eventClickCustomInfo(
                title,
                viewModel.userId,
                viewModel.getProductInfoP1,
                componentTrackDataModel
            )
            goToApplink(url)
        }
    }

    /**
     * ProductGeneralInfoViewHolder Listener
     */
    override fun onInfoClicked(
        appLink: String,
        name: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        when (name) {
            ProductDetailConstant.TRADE_IN -> {
                onTradeinClicked(componentTrackDataModel)
            }

            ProductDetailConstant.PRODUCT_VARIANT_INFO -> {
                if (!GlobalConfig.isSellerApp()) {
                    ProductDetailTracking.Click.eventClickVariant(
                        generateVariantString(
                            viewModel.variantData
                        ),
                        viewModel.getProductInfoP1,
                        componentTrackDataModel
                    )
                }
            }

            ProductDetailConstant.PRODUCT_WHOLESALE_INFO -> {
                val data =
                    ProductDetailMapper.mapToWholesale(viewModel.getProductInfoP1?.data?.wholesale)
                if (data != null && data.isNotEmpty()) {
                    ProductDetailTracking.Click.eventClickWholesale(
                        viewModel.getProductInfoP1,
                        componentTrackDataModel
                    )
                    context?.run {
                        startActivity(WholesaleActivity.getIntent(this, ArrayList(data)))
                    }
                }
            }

            ProductDetailConstant.PRODUCT_PROTECTION -> {
                ProductDetailTracking.Click.eventClickPDPInsuranceProtection(
                    viewModel.getProductInfoP1,
                    getPurchaseProtectionUrl(),
                    componentTrackDataModel
                )
                openFtInsuranceWebView(getPurchaseProtectionUrl())
            }

            ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO -> {
                goToApplink(appLink)
                ProductDetailTracking.Click.eventClickPDPInstallmentSeeMore(
                    viewModel.getProductInfoP1,
                    componentTrackDataModel
                )
            }

            ProductDetailConstant.INFO_OBAT_KERAS -> {
                if (appLink.isNotEmpty()) {
                    goToApplink(appLink)
                    onClickObatKeras(componentTrackDataModel)
                }
            }
        }
    }

    private fun onClickObatKeras(componentTrackDataModel: ComponentTrackDataModel) {
        val commonTracker = generateCommonTracker() ?: return
        val isTokoNow = viewModel.getProductInfoP1?.basic?.isTokoNow == true
        val data = GeneralInfoTracker(isTokoNow, componentTrackDataModel)
        GeneralInfoTracking.onClickObatKeras(commonTracker, data)
    }

    private fun onTradeinClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        val data = viewModel.getProductInfoP1
        if (pdpUiUpdater?.productSingleVariant != null) {
            if (data?.data?.variant?.isVariant == false) {
                onTradeinClickedAfter(componentTrackDataModel)
            } else {
                if (!viewModel.isUserSessionActive) {
                    doLoginWhenUserClickButton()
                    return
                }

                viewModel.variantData?.let {
                    goToAtcVariant(
                        AtcVariantHelper.generateSimpanCartRedirection(
                            productVariant = it,
                            buttonText = context?.getString(R.string.pdp_choose_variant) ?: "",
                            customCartType = ProductDetailCommonConstant.KEY_SAVE_TRADEIN_BUTTON
                        )
                    )
                }
            }
        } else {
            onTradeinClickedAfter(componentTrackDataModel)
        }
    }

    private fun onTradeinClickedAfter(componentTrackDataModel: ComponentTrackDataModel? = null) {
        if (!viewModel.isUserSessionActive) {
            doLoginWhenUserClickButton()
            return
        }

        if (viewModel.getProductInfoP1?.basic?.status == ProductStatusTypeDef.WAREHOUSE) {
            view?.showToasterError(
                getString(R.string.tradein_error_label),
                ctaText = getString(productdetailcommonR.string.pdp_common_oke)
            )
            return
        }

        if (openShipmentBottomSheetWhenError()) return

        ProductDetailTracking.Click.trackTradein(
            getTradeinData().usedPrice.toDoubleOrZero(),
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )

        tradeInDialog.show()
    }

    private fun generateCommonTracker(): CommonTracker? {
        val productInfo = viewModel.getProductInfoP1 ?: return null
        return CommonTracker(productInfo, viewModel.userId)
    }

    private fun getTradeinData(): ValidateTradeIn {
        return viewModel.p2Data.value?.validateTradeIn ?: ValidateTradeIn()
    }

    private fun getPurchaseProtectionUrl(): String {
        return viewModel.p2Data.value?.productPurchaseProtectionInfo?.ppItemDetailPage?.linkURL
            ?: ""
    }

    private fun getShipmentPlusText(): String {
        return viewModel.getP2ShipmentPlusByProductId()?.text.orEmpty()
    }

    private fun getPPTitleName(): String {
        return pdpUiUpdater?.productProtectionMap?.title ?: ""
    }

    /**
     * ProductRecommendationViewHolder Listener
     */
    override fun sendTopAdsClick(
        topAdsUrl: String,
        productId: String,
        productName: String,
        productImageUrl: String
    ) {
        context?.run {
            TopAdsUrlHitter(this::class.java.name).hitClickUrl(
                this,
                topAdsUrl,
                productId,
                productName,
                productImageUrl
            )
        }
    }

    override fun sendTopAdsImpression(
        topAdsUrl: String,
        productId: String,
        productName: String,
        productImageUrl: String
    ) {
        context?.run {
            TopAdsUrlHitter(this::class.java.name).hitImpressionUrl(
                this,
                topAdsUrl,
                productId,
                productName,
                productImageUrl
            )
        }
    }

    override fun onChannelRecommendationEmpty(channelPosition: Int, data: RecommendationWidget?) {
        data?.let {
            pdpUiUpdater?.removeEmptyRecommendation(it)
            updateUi()
        }
    }

    override fun onRecommendationBannerImpressed(
        data: RecommendationWidget,
        templateNameType: String
    ) {
        ProductDetailTracking.ImpulsiveBanner.impressImpulsiveBanner(
            widget = data,
            userId = viewModel.userId,
            productId = productId ?: "",
            templateNameType = templateNameType,
            basicData = ProductRecomLayoutBasicData(
                generalLayoutName = getPdpDataSource()?.layoutName ?: "",
                categoryName = getPdpDataSource()?.basic?.category?.name ?: "",
                categoryId = getPdpDataSource()?.basic?.category?.id ?: ""
            )
        )
    }

    override fun onRecommendationBannerClicked(
        appLink: String,
        data: RecommendationWidget,
        templateNameType: String
    ) {
        ProductDetailTracking.ImpulsiveBanner.clickImpulsiveBanner(
            widget = data,
            userId = viewModel.userId,
            productId = productId ?: "",
            templateNameType = templateNameType,
            basicData = ProductRecomLayoutBasicData(
                generalLayoutName = getPdpDataSource()?.layoutName ?: "",
                categoryName = getPdpDataSource()?.basic?.category?.name ?: "",
                categoryId = getPdpDataSource()?.basic?.category?.id ?: ""
            )
        )
        goToApplink(appLink)
    }

    override fun onRecomAddToCartClick(
        recommendationWidget: RecommendationWidget,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        itemPosition: Int
    ) {
        doActionOrLogin({
            viewModel.atcRecomNonVariant(recomItem, recomItem.minOrder)
        })
    }

    override fun onRecomAddToCartNonVariantQuantityChangedClick(
        recommendationWidget: RecommendationWidget,
        recomItem: RecommendationItem,
        quantity: Int,
        adapterPosition: Int,
        itemPosition: Int
    ) {
        pdpUiUpdater?.updateCurrentQuantityRecomItem(recomItem)
        viewModel.onAtcRecomNonVariantQuantityChanged(
            recomItem,
            quantity,
            RecommendationNowAffiliateData(
                affiliateUniqueId,
                affiliateChannel,
                recommendationWidget.affiliateTrackerId
            )
        )
    }

    override fun onRecomAddVariantClick(
        recomItem: RecommendationItem,
        adapterPosition: Int,
        itemPosition: Int
    ) {
        requireContext().let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = recomItem.productId.toString(),
                pageSource = VariantPageSource.TOKONOW_PAGESOURCE,
                isTokoNow = true,
                shopId = recomItem.shopId.toString(),
                startActivitResult = { data, _ ->
                    startActivity(data)
                }
            )
        }
    }

    override fun onChipFilterClicked(
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip,
        position: Int,
        filterPosition: Int
    ) {
        ProductDetailTracking.Click.eventClickSeeFilterAnnotation(annotationChip.recommendationFilterChip.value)
        val pid = viewModel.getProductInfoP1?.basic?.productID ?: ""
        viewModel.recommendationChipClicked(recommendationDataModel, annotationChip, pid)
    }

    override fun onSeeAllRecomClicked(
        recommendationWidget: RecommendationWidget,
        pageName: String,
        applink: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        AppLogAnalytics.setGlobalParamOnClick(enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format(recommendationWidget.pageName))
        ProductDetailTracking.Click.eventClickSeeMoreRecomWidget(
            recommendationWidget,
            pageName,
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )
        RouteManager.route(context, applink)
    }

    override fun eventRecommendationClick(
        recomItem: RecommendationItem,
        chipValue: String,
        position: Int,
        pageName: String,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        ProductDetailTracking.Click.eventRecommendationClick(
            recomItem,
            chipValue,
            false,
            position,
            viewModel.isUserSessionActive,
            pageName,
            title,
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )
    }

    override fun eventRecommendationImpression(
        recomItem: RecommendationItem,
        chipValue: String,
        position: Int,
        pageName: String,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (::trackingQueue.isInitialized) {
            ProductDetailTracking.Impression.eventRecommendationImpression(
                trackingQueue,
                position,
                recomItem,
                chipValue,
                false,
                viewModel.isUserSessionActive,
                pageName,
                title,
                viewModel.getProductInfoP1,
                componentTrackDataModel
            )
        }
    }

    override fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool? {
        return getRecyclerView()?.recycledViewPool
    }

    override fun getRecommendationCarouselSavedState(): SparseIntArray {
        return recommendationCarouselPositionSavedState
    }

    override fun loadTopads(pageName: String, queryParam: String, thematicId: String) {
        productRecomm.loadRecommendation(
            pageName = pageName,
            queryParam = queryParam,
            thematicId = thematicId
        )
    }

    override fun loadViewToView(pageName: String, queryParam: String, thematicId: String) {
        productRecomm.loadRecommendation(
            pageName = pageName,
            queryParam = queryParam,
            thematicId = thematicId,
            isViewToView = true
        )
    }

    override fun loadPlayWidget() {
        viewModel.getPlayWidgetData()
    }

    /**
     * PageErrorViewHolder
     */
    override fun onRetryClicked(forceRefresh: Boolean) {
        showLoading()
        onSwipeRefresh()
    }

    override fun goToHomePageClicked() {
        PageErrorTracking.clickBackToHomepage(generatePageErrorTrackerData())
        getProductDetailActivity()?.goToHomePageClicked()
    }

    override fun goToWebView(url: String) {
        RouteManager.route(
            context,
            String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, url)
        )
    }

    /**
     * ProductReviewViewHolder
     */
    override fun onSeeAllLastItemMediaReview(componentTrackDataModel: ComponentTrackDataModel?) {
        ProductDetailTracking.Click.onSeeAllLastItemImageReview(
            viewModel.getProductInfoP1,
            componentTrackDataModel ?: ComponentTrackDataModel()
        )
        goToReviewImagePreview()
    }

    override fun onSeeAllTextView(componentTrackDataModel: ComponentTrackDataModel?) {
        viewModel.getProductInfoP1?.run {
            ProductDetailTracking.Click.onSeeAllReviewTextView(
                this,
                viewModel.userId,
                componentTrackDataModel ?: ComponentTrackDataModel()
            )
            goToReviewDetail(basic.productID, getProductName)
        }
    }

    override fun onMediaReviewClick(
        reviewID: String,
        attachmentID: String,
        position: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        detailedMediaResult: ProductrevGetReviewMedia
    ) {
        ProductDetailTracking.Click.eventClickReviewOnBuyersImage(
            viewModel.getProductInfoP1,
            componentTrackDataModel ?: ComponentTrackDataModel(),
            reviewID
        )

        if (enableContentProductPreview && enableRollenceContentProductPreview) {
            goToProductPreviewActivityReviewSource(
                reviewId = reviewID,
                attachmentId = attachmentID
            )
        } else {
            goToReviewMediaGallery(
                position = position,
                detailedMediaResult = detailedMediaResult
            )
        }
    }

    override fun onReviewClick() {
        val productInfo = viewModel.getProductInfoP1 ?: return
        socialProofRatingTracking()
        goToReviewDetail(productInfo.basic.productID, productInfo.getProductName)
    }

    override fun onSeeReviewCredibility(
        reviewID: String,
        reviewerUserID: String,
        userStatistics: String,
        userLabel: String,
        componentTrackData: ComponentTrackDataModel
    ) {
        viewModel.getProductInfoP1?.run {
            val routed = RouteManager.route(
                context,
                Uri.parse(
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
                        reviewerUserID,
                        ReviewApplinkConst.REVIEW_CREDIBILITY_SOURCE_REVIEW_MOST_HELPFUL
                    )
                ).buildUpon()
                    .appendQueryParameter(ReviewApplinkConst.PARAM_PRODUCT_ID, basic.productID)
                    .build()
                    .toString()
            )
            if (routed) {
                ProductDetailTracking.Click.onClickReviewerName(
                    productInfoP1 = this,
                    reviewID = reviewID,
                    userId = viewModel.userId,
                    reviewerUserID = reviewerUserID,
                    statistics = userStatistics,
                    label = userLabel,
                    componentTrackData = componentTrackData
                )
            }
        }
    }

    override fun onTickerShopClicked(
        tickerTitle: String,
        tickerType: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        tickerDescription: String,
        applink: String,
        actionType: String,
        tickerActionBs: TickerActionBs?
    ) {
        trackOnTickerClicked(tickerTitle, tickerType, componentTrackDataModel, tickerDescription)
        if (actionType == "applink") {
            if (activity != null && RouteManager.isSupportApplink(activity, applink)) {
                goToApplink(applink)
            } else {
                openWebViewUrl(applink)
            }
        } else {
            if (tickerActionBs != null) {
                goToBottomSheetTicker(
                    title = tickerActionBs.title,
                    message = tickerActionBs.message,
                    reason = tickerActionBs.reason,
                    buttonText = tickerActionBs.buttonText,
                    buttonLink = tickerActionBs.buttonLink
                )
            }
        }
    }

    private fun goToBottomSheetTicker(
        title: String,
        message: String,
        reason: String,
        buttonText: String,
        buttonLink: String
    ) {
        activity?.let {
            // Make sure dont put your parameter inside constructor, it will cause crash when dont keep activity
            val shopStatusBs = ShopStatusInfoBottomSheet()
            shopStatusBs.show(
                title,
                message,
                reason,
                buttonText,
                buttonLink,
                it.supportFragmentManager
            )
        }
    }

    override fun onTickerGoToRecomClicked(
        tickerTitle: String,
        tickerType: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        tickerDescription: String
    ) {
        trackOnTickerClicked(tickerTitle, tickerType, componentTrackDataModel, tickerDescription)
        goToRecommendation()
    }

    override val mvcLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == MvcView.RESULT_CODE_OK && doActivityResult) {
                onSwipeRefresh()
            }
        }

    override fun onMerchantVoucherSummaryClicked(
        @MvcSource source: Int,
        uiModel: ProductMerchantVoucherSummaryDataModel.UiModel
    ) {
        context?.goToMvc(
            shopId = uiModel.shopId,
            productId = uiModel.productIdMVC,
            mvcAdditionalData = uiModel.additionalData,
            launcher = mvcLauncher
        )
    }

    override fun isOwner(): Boolean = viewModel.isShopOwner()

    override fun fintechRedirection(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
        redirectionUrl: String
    ) {
        if (fintechRedirectionWidgetDataClass.cta == ACTIVATION_LINKINING_FLOW &&
            fintechRedirectionWidgetDataClass.widgetBottomSheet?.show == false
        ) {
            openWebViewUrl(url = redirectionUrl)
        } else if (fintechRedirectionWidgetDataClass.cta == ACTIVATION_LINKINING_FLOW &&
            fintechRedirectionWidgetDataClass.widgetBottomSheet?.show == true
        ) {
            val bottomsheetIntent = RouteManager.getIntent(context, ApplinkConst.ACTIVATION_GOPAY)
            bottomsheetIntent.putExtra(
                ACTIVATION_BOTTOMSHEET_DETAIl,
                fintechRedirectionWidgetDataClass
            )
            bottomsheetIntent.putExtra(ACTIVATION_WEBVIEW_LINK, redirectionUrl)
            startActivityForResult(bottomsheetIntent, RQUEST_CODE_ACTIVATE_GOPAY)
        } else {
            val intent = RouteManager.getIntent(context, redirectionUrl)
            intent.putExtra(PARAM_PARENT_ID, fintechRedirectionWidgetDataClass.parentId)
            intent.putExtra(PARAM_CATEGORY_ID, fintechRedirectionWidgetDataClass.categoryId)
            startActivityForResult(intent, RQUEST_CODE_UPDATE_FINTECH_WIDGET)
        }
    }

    override fun onVideoFullScreenClicked() {
        val dynamicProductInfoData = viewModel.getProductInfoP1 ?: ProductInfoP1()

        ProductDetailTracking.Click.eventClickFullScreenVideo(
            viewModel.getProductInfoP1,
            viewModel.userId,
            ProductDetailTracking.generateComponentTrackModel(
                pdpUiUpdater?.mediaMap,
                0
            )
        )

        if (enableContentProductPreview && enableRollenceContentProductPreview) {
            goToProductPreviewActivityProductSource()
        } else {
            goToProductVideoDetailFragment(
                dynamicProductInfoData = dynamicProductInfoData
            )
        }
    }

    override fun onVideoVolumeCLicked(isMute: Boolean) {
        ProductDetailTracking.Click.eventClickVideoVolume(
            viewModel.getProductInfoP1,
            viewModel.userId,
            ProductDetailTracking.generateComponentTrackModel(pdpUiUpdater?.mediaMap, 0),
            isMute
        )
    }

    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
        viewModel.updateVideoTrackerData(stopDuration, videoDuration)
    }

    override fun getProductVideoCoordinator(): ProductVideoCoordinator? {
        return productVideoCoordinator
    }

    override fun setImageUnify(imageView: ImageView?) {
        atcAnimation.setSourceView(imageView)
    }

    /**
     * ProductSnapshotViewHolder
     */
    override fun onSwipePicture(
        type: String,
        url: String,
        position: Int,
        variantOptionId: String,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        addSwipePictureTracker(
            type = type,
            url = url,
            position = position,
            trackData = componentTrackDataModel
        )
        selectThumbVariantByMedia(variantOptionId = variantOptionId)
    }

    override fun onMediaViewed(position: Int, isVariantPhoto: Boolean) {
        if (isVariantPhoto) {
            viewModel.skuPhotoViewed.add(position)
        } else {
            viewModel.mainPhotoViewed.add(position)
        }
    }

    private fun addSwipePictureTracker(
        type: String,
        url: String,
        position: Int,
        trackData: ComponentTrackDataModel?
    ) {
        if (alreadyHitSwipeTracker != ProductDetailAlreadyHit) {
            ProductDetailTracking.Click.eventProductImageOnSwipe(
                viewModel.getProductInfoP1,
                trackData ?: ComponentTrackDataModel(),
                trackingQueue,
                type,
                url,
                position
            )
            alreadyHitSwipeTracker = ProductDetailAlreadySwipe
        }
    }

    /**
     * thumbnail variant only in variant level one
     */
    private fun selectThumbVariantByMedia(variantOptionId: String) {
        val singleVariant = pdpUiUpdater?.productSingleVariant ?: return

        if (singleVariant.isThumbnailType) {
            // finding product variant by media variant option id
            val selected = singleVariant.variantLevelOne?.variantOptions?.firstOrNull {
                it.variantId == variantOptionId
            }

            // select the variant
            viewModel.onThumbnailVariantSelected(
                uiData = singleVariant,
                variantId = selected?.variantId.orEmpty(),
                categoryKey = selected?.variantCategoryKey.orEmpty()
            )
        }
    }

    private fun scrollThumbnailVariant() {
        val singleVariant = pdpUiUpdater?.productSingleVariant ?: return
        val component = getComponentPositionBeforeUpdate(singleVariant)
        val vh = getViewHolderByPosition(component) as? ProductThumbnailVariantViewHolder
        Handler(Looper.getMainLooper()).postDelayed({
            vh?.scrollToPosition(
                position = singleVariant.variantLevelOne?.getPositionOfSelected().orZero()
            )
        }, ProductDetailConstant.VARIANT_SCROLL_DELAY)
    }

    override fun shouldShowWishlist(): Boolean {
        val isPrefetch = viewModel.getProductInfoP1?.cacheState?.isPrefetch == true
        return !viewModel.isShopOwner() && !isPrefetch
    }

    override fun onExpandProductName(componentTrackData: ComponentTrackDataModel) {
        pdpUiUpdater?.updateOnExpandProductName()
        ProductDetailTracking.Click.eventProductNameExpandClicked(
            componentTrackDataModel = componentTrackData,
            productInfo = viewModel.getProductInfoP1,
            userId = viewModel.userId
        )
    }

    override fun onMainImageClicked(
        componentTrackDataModel: ComponentTrackDataModel?,
        position: Int
    ) {
        ProductDetailTracking.Click.eventProductImageClicked(
            viewModel.getProductInfoP1,
            componentTrackDataModel ?: ComponentTrackDataModel()
        )
        onImageClicked(position)
    }

    override fun onImageClicked(position: Int) {
        if (enableContentProductPreview && enableRollenceContentProductPreview) {
            goToProductPreviewActivityProductSource(position = position)
        } else {
            goToProductDetailGallery(position)
        }
    }

    private fun goToProductVideoDetailFragment(
        dynamicProductInfoData: ProductInfoP1
    ) {
        productVideoCoordinator?.let {
            it.pauseVideoAndSaveLastPosition()
            sharedViewModel?.updateVideoDetailData(
                ProductVideoDetailDataModel(
                    it.getVideoDataModel(),
                    dynamicProductInfoData.shopTypeString,
                    dynamicProductInfoData.basic.shopID,
                    viewModel.userId,
                    dynamicProductInfoData.basic.productID
                )
            )
        }
        getProductDetailActivity()?.addNewFragment(ProductVideoDetailFragment())
    }

    private fun goToProductPreviewActivityProductSource(
        productData: ProductInfoP1 = viewModel.getProductInfoP1 ?: ProductInfoP1(),
        position: Int = 0,
        videoLastDuration: Long = productVideoCoordinator?.getCurrentPosition().orZero(),
        videoTotalDuration: Long = productVideoCoordinator?.getDuration().orZero()
    ) {
        val productId = viewModel.parentProductId ?: return
        val intent = ProductPreviewActivity.createIntent(
            context = requireContext(),
            productPreviewSourceModel = ProductPreviewSourceMapper(
                productId = productId
            ).mapProductSourceModel(
                productData = productData,
                mediaSelectedPosition = position,
                videoLastDuration = videoLastDuration,
                videoTotalDuration = videoTotalDuration
            )
        )
        startActivity(intent)
    }

    private fun goToProductDetailGallery(position: Int) {
        val dynamicProductInfoData = viewModel.getProductInfoP1 ?: ProductInfoP1()
        val items = dynamicProductInfoData.data.getGalleryItems()
        if (items.isEmpty()) return
        val intent = ProductDetailGalleryActivity.createIntent(
            context = requireContext(),
            productDetailGallery = ProductDetailGallery(
                productId = dynamicProductInfoData.basic.productID,
                userId = viewModel.userId,
                page = ProductDetailGallery.Page.ProductDetail,
                items = items,
                selectedId = position.toString()
            )
        )
        startActivity(intent)
    }

    private fun goToProductPreviewActivityReviewSource(
        reviewId: String,
        attachmentId: String
    ) {
        val productId = viewModel.parentProductId ?: return
        val intent = ProductPreviewActivity.createIntent(
            context = requireContext(),
            productPreviewSourceModel = ProductPreviewSourceMapper(
                productId = productId
            ).mapReviewSourceModel(
                reviewId = reviewId,
                attachmentId = attachmentId
            )
        )
        startActivity(intent)
    }

    private fun goToReviewMediaGallery(
        position: Int,
        detailedMediaResult: ProductrevGetReviewMedia
    ) {
        ReviewMediaGalleryRouter.routeToReviewMediaGallery(
            context = requireContext(),
            pageSource = ReviewMediaGalleryRouter.PageSource.PDP,
            productID = viewModel.getProductInfoP1?.basic?.productID.orEmpty(),
            shopID = viewModel.getProductInfoP1?.basic?.shopID.orEmpty(),
            isProductReview = true,
            isFromGallery = false,
            mediaPosition = position.inc(),
            showSeeMore = detailedMediaResult.hasNext,
            preloadedDetailedReviewMediaResult = detailedMediaResult
        ).let { startActivity(it) }
    }

    override fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel) {
        ProductDetailTracking.Click.eventClickTradeInRibbon(
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )
        scrollToPosition(getComponentPosition(pdpUiUpdater?.productTradeinMap))
    }

    override fun getProductFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun showAlertCampaignEnded() {
        val mActivity = activity ?: return

        DialogUnify(mActivity, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.campaign_expired_title))
            setDescription(getString(R.string.campaign_expired_descr))
            setPrimaryCTAText(getString(com.tokopedia.abstraction.R.string.close))
            setPrimaryCTAClickListener {
                onSwipeRefresh()
                dismiss()
            }
            show()
        }
    }

    override fun onShowProductMediaRecommendationClicked(componentTracker: ComponentTrackDataModel?) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val productMediaRecomBasicInfo = productInfo.data.productMediaRecomBasicInfo
        val basicData = productInfo.basic
        val title = productMediaRecomBasicInfo.bottomsheetTitle
        val iconText = productMediaRecomBasicInfo.iconText
        val commonTracker = CommonTracker(productInfo = productInfo, userId = viewModel.userId)

        viewModel.showProductMediaRecomBottomSheet(
            title = title,
            pageName = productMediaRecomBasicInfo.recommendation,
            productId = basicData.productID,
            isTokoNow = basicData.isTokoNow.orFalse()
        )
        MediaTracking.onOverlayRecommClicked(
            title = iconText,
            componentTrackDataModel = componentTracker,
            commonTracker = commonTracker
        )
    }

    override fun onProductMediaRecommendationImpressed(componentTracker: ComponentTrackDataModel?) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val title = productInfo.data.productMediaRecomBasicInfo.iconText
        val commonTracker = CommonTracker(productInfo = productInfo, userId = viewModel.userId)
        MediaTracking.onOverlayRecommImpressed(
            title = title,
            componentTrackDataModel = componentTracker,
            commonTracker = commonTracker
        )
    }

    private fun trackingEventSuccessRemoveFromWishlist(componentTrackDataModel: ComponentTrackDataModel) {
        ProductDetailTracking.Click.eventPDPRemoveToWishlist(
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )
    }

    private fun trackingEventSuccessAddToWishlist(componentTrackDataModel: ComponentTrackDataModel) {
        viewModel.getProductInfoP1?.let { productInfo ->
            ProductDetailTracking.Click.eventPDPAddToWishlist(
                productInfo,
                componentTrackDataModel
            )
        }
    }

    override fun onFabWishlistClicked(
        isActive: Boolean,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val productInfo = viewModel.getProductInfoP1
        if (viewModel.isUserSessionActive) {
            if (isActive) {
                productInfo?.basic?.productID?.let { productId ->
                    context?.let { context ->
                        removeWishlistV2(productId, componentTrackDataModel)
                    }
                }
            } else {
                productInfo?.basic?.productID?.let {
                    context?.let { context ->
                        addWishlistV2(componentTrackDataModel)
                    }
                }
            }
        } else {
            ProductDetailTracking.Click.eventPDPAddToWishlistNonLogin(
                viewModel.getProductInfoP1,
                componentTrackDataModel
            )
            goToLogin()
        }
    }

    override fun onDiscussionClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        socialProofTalkTracking(trackData = componentTrackDataModel)
        disscussionClicked()
    }

    override fun onDiscussionRefreshClicked() {
        viewModel.getProductInfoP1?.basic?.let {
            viewModel.getDiscussionMostHelpful(it.productID, it.shopID)
        }
    }

    override fun onDiscussionSendQuestionClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        writeDiscussion {
            val totalAvailableVariants = viewModel.variantData?.getBuyableVariantCount()
                .orZero()
                .toString()
            viewModel.getProductInfoP1?.let {
                ProductDetailTracking.Click.eventEmptyDiscussionSendQuestion(
                    it,
                    componentTrackDataModel,
                    viewModel.userId,
                    isVariantSelected = pdpUiUpdater?.productSingleVariant != null,
                    totalAvailableVariants
                )
            }
        }
    }

    override fun goToTalkReading(
        componentTrackDataModel: ComponentTrackDataModel,
        numberOfThreadsShown: String
    ) {
        viewModel.getProductInfoP1?.let {
            ProductDetailTracking.Click.eventDiscussionSeeAll(
                it,
                componentTrackDataModel,
                viewModel.userId,
                numberOfThreadsShown
            )
        }
        goToReadingActivity()
    }

    override fun goToTalkReply(
        questionId: String,
        componentTrackDataModel: ComponentTrackDataModel,
        numberOfThreadsShown: String
    ) {
        doActionOrLogin({
            viewModel.getProductInfoP1?.let {
                ProductDetailTracking.Click.eventDiscussionDetails(
                    it,
                    componentTrackDataModel,
                    viewModel.userId,
                    questionId,
                    numberOfThreadsShown
                )
            }
            goToReplyActivity(questionId)
        })
        viewModel.updateLastAction(ProductDetailTalkGoToReplyDiscussion(questionId))
    }

    private fun goToReviewDetail(productId: String, productName: String) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_REVIEW, productId)
            intent?.run {
                intent.putExtra(ProductDetailConstant.REVIEW_PRD_NM, productName)
                startActivity(intent)
            }
        }
    }

    private fun disscussionClicked() {
        goToReadingActivity()
    }

    private fun goToReviewImagePreview() {
        val productId = viewModel.getProductInfoP1?.basic?.productID ?: ""
        RouteManager.route(context, ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY, productId)
    }

    private fun observeVideoDetail() {
        activity?.let { activity ->
            sharedViewModel?.productVideoData?.observe(activity) {
                if (it.isEmpty()) return@observe
                productVideoCoordinator?.updateAndResume(it)
            }
        }
    }

    private fun observeMiniCart() {
        viewModel.miniCartData.observe(viewLifecycleOwner) {
            if (it) {
                updateButtonState()
                if (firstOpenPage == false) {
                    pdpUiUpdater?.updateRecomTokonowQuantityData(viewModel.p2Data.value?.miniCart)
                    updateUi()
                }
            }
        }
    }

    private fun observeDeleteCart() {
        viewModel.deleteCartLiveData.observe(viewLifecycleOwner) {
            actionButtonView.hideLoading()
            it.doSuccessOrFail({ message ->
                view?.showToasterSuccess(
                    message.data,
                    ctaText = getString(productdetailcommonR.string.pdp_common_oke)
                )
                updateButtonState()
            }) { throwable ->
                view?.showToasterError(
                    throwable.message ?: "",
                    ctaText = getString(productdetailcommonR.string.pdp_common_oke).uppercase()
                )
                logException(throwable)
            }
        }
    }

    private fun observeUpdateCart() {
        viewModel.updateCartLiveData.observe(viewLifecycleOwner) {
            it.doSuccessOrFail({ success ->
                view?.showToasterSuccess(
                    success.data,
                    ctaText = getString(productdetailcommonR.string.pdp_common_oke)
                )
            }) { throwable ->
                view?.showToasterError(
                    throwable.message ?: "",
                    ctaText = getString(productdetailcommonR.string.pdp_common_oke)
                )
                logException(throwable)
            }
        }
    }

    private fun observeShippingAddressChanged() {
        activity?.let { activity ->
            sharedViewModel?.isAddressChanged?.observe(activity) {
                if (it) {
                    onSuccessUpdateAddress()
                }
            }
        }
    }

    private fun observePlayWidget() {
        viewModel.playWidgetModel.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> handlePlayWidgetUiModel(it.data)
                is Fail -> pdpUiUpdater?.removeComponent(PLAY_CAROUSEL)
            }
            updateUi()
        }

        viewModel.playWidgetReminderSwitch.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> view?.showToasterSuccess(
                    if (it.data.reminded) {
                        getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                    } else {
                        getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder)
                    }
                )

                is Fail -> view?.showToasterError(
                    getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder)
                )
            }
        }
    }

    private fun handlePlayWidgetUiModel(playWidgetState: PlayWidgetState) {
        pdpUiUpdater?.updatePlayWidget(playWidgetState)
    }

    private fun onSuccessUpdateAddress() {
        view?.showToasterSuccess(getString(R.string.pdp_shipping_success_change_address))
        onSwipeRefresh()
    }

    private fun observeTopAdsImageData() {
        viewLifecycleOwner.observe(viewModel.topAdsImageView) { data ->
            data.doSuccessOrFail({
                if (!it.data.isNullOrEmpty()) {
                    pdpUiUpdater?.updateTopAdsImageData(it.data)
                    updateUi()
                } else {
                    pdpUiUpdater?.removeComponent(ProductDetailConstant.KEY_TOP_ADS)
                }
            }, {
                pdpUiUpdater?.removeComponent(ProductDetailConstant.KEY_TOP_ADS)
                logException(it)
            })
        }
    }

    private fun observeTopAdsIsChargeData() {
        viewLifecycleOwner.observe(viewModel.topAdsRecomChargeData) { data ->
            data.doSuccessOrFail(
                { topAdsData ->
                    if (!isTopadsDynamicsSlottingAlreadyCharged) {
                        context?.let {
                            TopAdsUrlHitter(it).hitImpressionUrl(
                                this::class.java.name,
                                topAdsData.data.product.image.m_url,
                                topAdsData.data.product.id,
                                topAdsData.data.product.name,
                                topAdsData.data.product.image.m_ecs
                            )

                            TopAdsUrlHitter(it).hitClickUrl(
                                this::class.java.name,
                                topAdsData.data.clickUrl,
                                topAdsData.data.product.id,
                                topAdsData.data.product.name,
                                topAdsData.data.product.image.m_ecs
                            )
                            isTopadsDynamicsSlottingAlreadyCharged = true

                            ProductTopAdsLogger.logServer(
                                TOPADS_PDP_HIT_ADS_TRACKER,
                                productId = topAdsData.data.product.id
                            )
                        }
                    } else {
                        ProductTopAdsLogger.logServer(
                            TOPADS_PDP_IS_NOT_ADS,
                            productId = topAdsData.data.product.id
                        )
                    }
                },
                {
                    logException(it)
                }
            )
        }
    }

    private fun observeP2Other() {
        viewLifecycleOwner.observe(viewModel.p2Other) {
            pdpUiUpdater?.updateDataP2General(it)
            updateUi()
            getProductDetailActivity()?.stopMonitoringP2Other()
        }
    }

    private fun observeDiscussionData() {
        viewLifecycleOwner.observe(viewModel.discussionMostHelpful) { data ->
            data.doSuccessOrFail({
                pdpUiUpdater?.updateDiscussionData(it.data.discussionMostHelpful)
                updateUi()
            }, {
                logException(it)
            })
        }
    }

    private fun observeOnThumbnailVariantSelected() {
        viewLifecycleOwner.observe(viewModel.onThumbnailVariantSelectedData) {
            updateThumbnailVariantDataAndUi(it)
        }
    }

    private fun observeATCRecomData() {
        viewLifecycleOwner.observe(viewModel.atcRecom) { data ->
            data.doSuccessOrFail({
                if (it.data.isNotEmpty()) {
                    view?.showToasterSuccess(it.data)
                }
            }, {
                view?.showToasterError(
                    it.message.orEmpty(),
                    ctaText = getString(productdetailcommonR.string.pdp_common_oke)
                )
                logException(it)
            })
        }
    }

    private fun observeATCRecomSendTracker() {
        viewLifecycleOwner.observe(viewModel.atcRecomTracker) { data ->
            data.doSuccessOrFail({
                if (viewModel.getProductInfoP1?.basic?.isTokoNow.orFalse()) {
                    ProductDetailTracking.Click.eventClickRecomAddToCart(
                        it.data,
                        viewModel.userId,
                        it.data.minOrder
                    )
                } else {
                    RecommendationCarouselTracking.sendEventAtcClick(
                        it.data,
                        viewModel.userId,
                        it.data.minOrder.coerceAtLeast(DEFAULT_QTY_1),
                        anchorProductId = viewModel.getProductInfoP1?.basic?.productID.orEmpty()
                    )
                }
            }, {})
        }
    }

    private fun observeATCTokonowResetCard() {
        viewLifecycleOwner.observe(viewModel.atcRecomTokonowResetCard) {
            pdpUiUpdater?.resetFailedRecomTokonowCard(it)
            updateUi()
        }
    }

    private fun observeATCRecomTokonowNonLogin() {
        viewLifecycleOwner.observe(viewModel.atcRecomTokonowNonLogin) {
            goToLogin()
            pdpUiUpdater?.resetFailedRecomTokonowCard(it)
        }
    }

    /**
     * update product UI on single(especially chip), optionals variant(variant on pdp), vbs changed
     */
    private fun updateVariantDataAndUi(
        variantProcessedData: List<VariantCategory>?,
        doSomethingAfterVariantUpdated: (() -> Unit)? = null
    ) {
        val singleVariant = pdpUiUpdater?.productSingleVariant
        val selectedChild = viewModel.getChildOfVariantSelected(singleVariant = singleVariant)
        val title = selectedChild?.subText.ifNullOrBlank { getVariantString() }

        pdpUiUpdater?.updateVariantData(title = title, processedVariant = variantProcessedData)
        pdpUiUpdater?.updateMediaScrollPosition(selectedChild?.optionIds?.firstOrNull())
        updateProductInfoOnVariantChanged(selectedChild)

        updateUi()
        doSomethingAfterVariantUpdated?.invoke()
    }

    private fun updateProductInfoOnVariantChanged(selectedChild: VariantChild?) {
        val updatedDynamicProductInfo = VariantMapper.updateDynamicProductInfo(
            oldData = viewModel.getProductInfoP1,
            newData = selectedChild,
            isWishlist = viewModel.getP2()?.getWishlistStatusByProductId(
                selectedChild?.productId.orEmpty()
            ).orFalse()
        )

        viewModel.updateDynamicProductInfoData(updatedDynamicProductInfo)
        productId = updatedDynamicProductInfo?.basic?.productID

        val boeData = viewModel.getBebasOngkirDataByProductId()
        productId?.let { productId ->
            pdpUiUpdater?.updateFintechDataWithProductId(
                productId,
                viewModel.userSessionInterface.isLoggedIn
            )
        }
        pdpUiUpdater?.updateDataP1(updatedDynamicProductInfo)

        pdpUiUpdater?.updatePromoPriceWithP2(
            productId = selectedChild?.productId.toString(),
            promoPriceStyle = viewModel.p2Data.value?.promoPriceStyle,
            freeOngkirImgUrl = boeData.imageURL,
            promoCodes = updatedDynamicProductInfo?.data?.promoPrice?.promoCodes ?: listOf()
        )

        pdpUiUpdater?.updateNotifyMeAndContent(
            productId = selectedChild?.productId.toString(),
            upcomingData = viewModel.getP2()?.upcomingCampaigns,
            freeOngkirImgUrl = boeData.imageURL
        )

        pdpUiUpdater?.updateTradeInRibbon(
            isEligible = viewModel.getP2()?.validateTradeIn?.isEligible.orFalse()
        )

        val selectedTicker = viewModel.p2Data.value?.getTickerByProductId(productId ?: "")
        pdpUiUpdater?.updateTicker(selectedTicker)

        pdpUiUpdater?.updateShipmentData(
            viewModel.getP2RatesEstimateByProductId(),
            viewModel.getMultiOriginByProductId().isFulfillment,
            viewModel.getProductInfoP1?.data?.isCod ?: false,
            boeData,
            viewModel.getUserLocationCache(),
            viewModel.getP2ShipmentPlusByProductId()
        )

        renderRestrictionBottomSheet(
            viewModel.p2Data.value?.restrictionInfo ?: RestrictionInfoResponse()
        )

        /*
            If the p2 data is empty, dont update the button
            this condition will be reproduceable when variant auto select is faster then p2 data from network
            if this happen, the update button will be run in onSuccessGetP2Data
         */
        if (viewModel.p2Data.value != null || viewModel.p2Data.value == null) {
            updateButtonState()
        }

        pdpUiUpdater?.updateArData(
            productId ?: "",
            viewModel.p2Data.value?.arInfo ?: ProductArInfo()
        )

        // update BMGM data
        viewModel.getP2()?.bmgm?.let {
            pdpUiUpdater?.updateBMGMSneakPeak(productId = productId.orEmpty(), bmgm = it)
        }

        viewModel.getP2()?.gwp?.let {
            pdpUiUpdater?.updateGWPSneakPeak(productId = productId.orEmpty(), gwp = it)
        }

        viewModel.getP2()?.dynamicOneLinerVariant?.let {
            pdpUiUpdater?.updateDynamicOneLinerVariantLevel(
                productId = productId.orEmpty(),
                dynamicOneLinerVariant = it
            )
        }
    }

    /**
     * update product UI on thumbnail variant changed
     */
    private fun updateThumbnailVariantDataAndUi(singleVariant: ProductSingleVariantDataModel?) {
        val selectedChild = viewModel.getChildOfVariantSelected(singleVariant = singleVariant)
        val optionId = selectedChild?.optionIds?.firstOrNull().orEmpty()
        val title = selectedChild?.subText.ifNullOrBlank { getVariantString() }
        val singleVariantUpdated = singleVariant?.copy(title = title)

        pdpUiUpdater?.updateSingleVariant(singleVariantUpdated)
        pdpUiUpdater?.updateMediaScrollPosition(optionId)

        if (selectedChild != null) {
            updateProductInfoOnVariantChanged(selectedChild)
        }

        scrollThumbnailVariant()

        updateUi()
    }

    private fun updateButtonState() {
        viewModel.getProductInfoP1?.let {
            val cartTypeData = viewModel.getCartTypeByProductId()
            val selectedMiniCartItem =
                if (it.basic.isTokoNow && cartTypeData?.availableButtonsPriority?.firstOrNull()
                    ?.isCartTypeDisabledOrRemindMe() == false
                ) {
                    viewModel.getMiniCartItem()
                } else {
                    null
                }

            val totalStockAtcVariant =
                viewModel.p2Data.value?.getTotalStockMiniCartByParentId(it.data.variant.parentID)

            val shouldShowTokoNow = it.basic.isTokoNow &&
                cartTypeData?.availableButtonsPriority?.firstOrNull()
                ?.isCartTypeDisabledOrRemindMe() == false &&
                (totalStockAtcVariant != 0 || selectedMiniCartItem != null)

            val tokonowVariantButtonData = if (shouldShowTokoNow) {
                TokoNowButtonData(
                    totalStockAtcVariant = totalStockAtcVariant ?: 0,
                    productTitle = viewModel.getProductInfoP1?.data?.name ?: "",
                    isVariant = it.data.variant.isVariant,
                    minQuantity = it.basic.minOrder,
                    maxQuantity = it.basic.maxOrder,
                    selectedMiniCart = selectedMiniCartItem
                )
            } else {
                null
            }

            actionButtonView.renderData(
                isWarehouseProduct = !it.isProductActive(),
                hasShopAuthority = viewModel.hasShopAuthority(),
                isShopOwner = viewModel.isShopOwner(),
                hasTopAdsActive = hasTopAds(),
                cartTypeData = cartTypeData,
                tokonowButtonData = tokonowVariantButtonData,
                isShopModerate = viewModel.getShopInfo().statusInfo.isOnModerationMode()
            )
        }
        showOrHideButton()
    }

    private fun observeAddToCart() {
        lifecycleScope.launchWhenStarted {
            viewModel.addToCartResultState.collectLatest { data ->
                actionButtonView.hideLoading()

                var cartId = ""
                var success = false
                var reason = ""

                data.doSuccessOrFail({
                    if (it.data.errorReporter.eligible) {
                        view?.showToasterError(
                            it.data.errorReporter.texts.submitTitle,
                            ctaText = getString(productdetailcommonR.string.pdp_common_oke)
                        )
                    } else {
                        success = true
                        onSuccessAtc(it.data)
                        ProductDetailServerLogger.logBreadCrumbAtc(
                            isSuccess = true,
                            errorMessage = it.data.getAtcErrorMessage() ?: "",
                            atcType = buttonActionType
                        )
                    }
                    cartId = it.data.data.cartId
                }, {
                    ProductDetailTracking.Impression.eventViewErrorWhenAddToCart(
                        it.message.orEmpty(),
                        viewModel.getProductInfoP1?.basic?.productID.orEmpty(),
                        viewModel.userId
                    )
                    handleAtcError(it)
                    reason = it.message.orEmpty()
                })
                sendConfirmResultByteIoTracker(cartId, reason, success)
            }
        }
    }

    private fun sendConfirmResultByteIoTracker(cartId: String, reason: String, success: Boolean) {
        val model = viewModel.getConfirmCartResultData().apply {
            isSuccess = success
            failReason = reason
            cartItemId = cartId
        }
        if (buttonActionType == ProductDetailCommonConstant.ATC_BUTTON ||
            buttonActionType == ProductDetailCommonConstant.BUY_BUTTON
        ) {
            AppLogPdp.sendConfirmCartResult(model)
        }
    }

    private fun handleAtcError(t: Throwable) {
        logException(t)
        if (t is AkamaiErrorException && t.message != null) {
            view?.showToasterError(
                t.message.orEmpty(),
                ctaText = getString(productdetailcommonR.string.pdp_common_oke)
            )
            ProductDetailServerLogger.logBreadCrumbAtc(
                false,
                t.message ?: "",
                buttonActionType
            )
        } else {
            val errorMessage = getErrorMessage(t)
            view?.showToasterError(
                errorMessage,
                ctaText = getString(productdetailcommonR.string.pdp_common_oke)
            )
            ProductDetailServerLogger.logBreadCrumbAtc(
                isSuccess = false,
                errorMessage = errorMessage,
                atcType = buttonActionType
            )
        }
    }

    /**
     * Region of PLT Monitoring
     */
    private fun startMonitoringRenderPage() {
        getProductDetailActivity()?.startMonitoringPltRenderPage()
    }

    private fun stopPLTRenderPageAndMonitoringP1() {
        getProductDetailActivity()?.stopPLTRenderPageAndMonitoringP1(
            viewModel.getProductInfoP1?.isProductVariant().orFalse()
        )
    }

    private fun observeP1() {
        viewLifecycleOwner.observe(viewModel.productLayout) { data ->
            startMonitoringRenderPage()

            data.doSuccessOrFail({
                firstOpenPage = false
                pdpUiUpdater = PdpUiUpdater(ProductDetailMapper.hashMapLayout(it.data))
                viewModel.getProductInfoP1?.let { dataP1 ->
                    onSuccessGetDataP1(dataP1)
                    ProductDetailServerLogger.logBreadCrumbSuccessGetDataP1(
                        isSuccess = true,
                        cacheState = viewModel.getProductInfoP1?.cacheState,
                        isCampaign = viewModel.getProductInfoP1?.isCampaign.orFalse()
                    )
                    ProductDetailServerLogger.logNewRelicP1Success(
                        productId = productId,
                        cacheState = viewModel.getProductInfoP1?.cacheState,
                        isCampaign = viewModel.getProductInfoP1?.isCampaign
                    )
                }

                appendInfiniteRecomm()
            }, {
                handleObserverP1Error(error = it)
            })

            getRecyclerView()?.addOneTimeGlobalLayoutListener {
                stopPLTRenderPageAndMonitoringP1()
            }
        }
    }

    private fun appendInfiniteRecomm() {
        infiniteRecommManager = InfiniteRecommendationManager(
            context = requireContext(),
            additionalAppLogParam = getAppLogAdditionalParam(),
            enableSeparator = true
        )
        getRecyclerView()?.addOneTimeGlobalLayoutListener {
            infiniteRecommManager?.let {
                val hasInfinite = viewModel.getProductInfoP1?.hasInfiniteRecommendation ?: false
                if (hasInfinite && concatAdapter?.adapters?.size != 2) {
                    concatAdapter?.addAdapter(it.adapter)
                }

                if (hasInfinite) {
                    it.requestParam = GetRecommendationRequestParam(
                        pageName = viewModel.getP1()?.infiniteRecommendationPageName.orEmpty(),
                        productIds = listOf(productId.orEmpty()),
                        queryParam = viewModel.getP1()?.infiniteRecommendationQueryParam.orEmpty()
                    )
                }
            }
        }
    }

    override fun getAppLogAdditionalParam(): AppLogAdditionalParam.PDP {
        return pdpUiUpdater?.getAppLogAdditionalParam(viewModel.getP1())
            ?: AppLogAdditionalParam.PDP()
    }

    private fun handleObserverP1Error(error: Throwable) {
        ProductDetailServerLogger.logNewRelicP1Error(error = error)
        logException(error)
        val ctx = context ?: return
        val errorModel = ProductDetailErrorHelper.getErrorType(
            context = ctx,
            t = error,
            fromDeeplink = isFromDeeplink,
            deeplinkUrl = deeplinkUrl
        )

        if (isPdpCacheableError()) {
            preparePageCacheableError(errorModel = errorModel)
        } else {
            preparePageError(errorModel)
        }
    }

    private fun preparePageError(errorModel: PageErrorDataModel) {
        val pdpLayout = viewModel.getProductInfoP1

        renderPageError(errorModel)
        ProductDetailServerLogger.logBreadCrumbSuccessGetDataP1(
            isSuccess = false,
            errorMessage = errorModel.errorMessage,
            errorCode = errorModel.errorCode,
            cacheState = pdpLayout?.cacheState,
            isCampaign = pdpLayout?.isCampaign.orFalse()
        )
    }

    private fun isPdpCacheableError(): Boolean {
        val cacheState = viewModel.getProductInfoP1?.cacheState ?: return false
        // ensure toggle remote cacheable is active and data still from cache
        return cacheState.remoteCacheableActive &&
            (cacheState.isFromCache || cacheState.cacheFirstThenCloud)
    }

    private fun preparePageCacheableError(errorModel: PageErrorDataModel) {
        val errorCode = errorModel.errorCode.toIntOrNull() ?: run {
            preparePageError(errorModel)
            return
        }
        val connectionError = errorCode == GlobalError.SERVER_ERROR ||
            errorCode == GlobalError.NO_CONNECTION
        val shouldShowToasterError = !errorModel.shouldShowTobacoError && connectionError

        if (shouldShowToasterError) {
            showToasterPageError(errorModel)
        } else {
            preparePageError(errorModel)
        }
    }

    private fun showToasterPageError(errorModel: PageErrorDataModel) {
        val view = binding?.root ?: return
        val pdpLayout = viewModel.getProductInfoP1
        binding?.swipeRefreshPdp?.isRefreshing = false

        Toaster.build(
            view = view,
            text = "Wah, internet putus. Cek paket data atau WiFi-mu, ya.",
            actionText = "Coba lagi",
            duration = LENGTH_INDEFINITE,
            type = TYPE_ERROR
        ) {
            onSwipeRefresh()
        }.show()

        ProductDetailServerLogger.logBreadCrumbSuccessGetDataP1(
            isSuccess = false,
            errorMessage = errorModel.errorMessage,
            errorCode = errorModel.errorCode,
            cacheState = pdpLayout?.cacheState,
            isCampaign = pdpLayout?.isCampaign.orFalse()
        )
    }

    private fun observeP2Login() {
        viewLifecycleOwner.observe(viewModel.p2Login) {
            topAdsGetProductManage = it.topAdsGetProductManage
            pdpUiUpdater?.updateShopFollow(it.isFollow)
            pdpUiUpdater?.updateWishlistData(it.isWishlisted)

            actionButtonView.setTopAdsButton(hasTopAds())

            updateUi()
            openBottomSheetTopAds()
            getProductDetailActivity()?.stopMonitoringP2Login()
        }
    }

    private fun observeP2Data() {
        viewLifecycleOwner.observe(viewModel.p2Data) {
            val boeData = viewModel.getBebasOngkirDataByProductId()
            val ratesData = viewModel.getP2RatesEstimateByProductId()
            val shipmentPlus = viewModel.getP2ShipmentPlusByProductId()

            shareProductInstance?.updateAffiliate(it.shopInfo.statusInfo.shopStatus)

            trackProductView(getTradeinData().isEligible, boeData.boType)

            if (viewModel.getProductInfoP1?.isFromCache == false) {
                doP2DataAfterCloud(p2Data = it)
            }

            onSuccessGetDataP2(it, boeData, ratesData, shipmentPlus)
            AppLogPdp.sendPDPEnterPage(viewModel.getProductDetailTrack())
            getProductDetailActivity()?.stopMonitoringP2Data()
            ProductDetailServerLogger.logBreadCrumbSuccessGetDataP2(
                isSuccess = it.shopInfo.shopCore.shopID.isNotEmpty()
            )

            getRecyclerView()?.addOneTimeGlobalLayoutListener {
                EmbraceMonitoring.stopMoments(ProductDetailConstant.PDP_RESULT_TRACE_P2_DATA)
            }
        }
    }

    private fun doP2DataAfterCloud(p2Data: ProductInfoP2UiData) {
        viewModel.getProductInfoP1?.let { p1 ->
            ProductDetailTracking.sendScreen(
                irisSessionId,
                p1.basic.shopID,
                p1.shopTypeString,
                p1.basic.productID
            )

            viewModel.hitAffiliateCookie(
                productInfo = p1,
                affiliateUuid = affiliateUniqueId,
                uuid = uuid,
                affiliateChannel = affiliateChannel,
                affiliateSubIds = affiliateSubIds,
                affiliateSource = affiliateSource
            )

            mStoriesWidgetManager.updateStories(
                shopIds = listOf(p1.basic.shopID),
                categoryIds = viewModel.getProductInfoP1?.basic?.category?.detail?.map { it.id }.orEmpty(),
                productIds = listOf(viewModel.getProductInfoP1?.basic?.productID.orEmpty())
            )

            handleShareAdditionalCheck(p2Data.shopInfo)

            if (p2Data.productPurchaseProtectionInfo.ppItemDetailPage.isProtectionAvailable) {
                ProductDetailTracking.Impression.eventPurchaseProtectionAvailable(
                    viewModel.userId,
                    viewModel.getProductInfoP1,
                    getPPTitleName()
                )
            }

            ProductDetailTracking.Branch.eventBranchItemView(p1, viewModel.userId)

            stickyLoginView?.loadContent()
        }
    }

    private fun observeAffiliateEligibility() {
        viewModel.resultAffiliate.observe(viewLifecycleOwner) {
            if (it is Success && it.data.eligibleCommission?.isEligible.orFalse()) {
                updateToolbarShareAffiliate()
            }
        }
    }

    private fun handleShareAdditionalCheck(shopInfo: ShopInfo) {
        if (isUsingShareEx()) {
            additionalCheckShareEx(shopInfo)
        } else {
            checkAffiliateEligibility(shopInfo)
        }
    }

    private fun additionalCheckShareEx(shopInfo: ShopInfo) {
        viewModel.getProductInfoP1?.let { dataP1 ->
            if (isShareAffiliateIconEnabled() && !GlobalConfig.isSellerApp()) {
                shareExInitializer?.additionalCheck(
                    generateShareExInitializerArg(
                        dataP1,
                        shopInfo,
                        viewModel.variantData,
                        onSuccess = {
                            if (it.isEligibleAffiliate) {
                                updateToolbarShareAffiliate()
                            }
                        }
                    )
                )
            }
        }
    }

    private fun checkAffiliateEligibility(shopInfo: ShopInfo) {
        if (isShareAffiliateIconEnabled() && !GlobalConfig.isSellerApp()) {
            viewModel.getProductInfoP1?.let { dataP1 ->
                val affiliateInput =
                    generateAffiliateShareData(dataP1, shopInfo, viewModel.variantData)
                viewModel.checkAffiliateEligibility(affiliateInput)
            }
        }
    }

    override fun removeComponent(componentName: String) {
        pdpUiUpdater?.removeComponent(componentName)
        updateUi()
    }

    override fun showArCoachMark(view: ConstraintLayout?) {
        pdpCoachmarkHelper?.showCoachMarkAr(view)
    }

    override fun hideArCoachMark() {
        pdpCoachmarkHelper?.hideCoachMark(
            key = ProductDetailCoachMarkHelper.Companion.PRODUCT_DETAIL_AR_PAGE_COACHMARK
        )
    }

    private fun openBottomSheetTopAds() {
        if (GlobalConfig.isSellerApp() && !activity?.intent?.data?.getQueryParameter(
                SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME
            ).isNullOrBlank() &&
            !alreadyPerformSellerMigrationAction && viewModel.isShopOwner()
        ) {
            alreadyPerformSellerMigrationAction = true
            rincianTopAdsClicked()
        }
    }

    private fun observeToggleFavourite() {
        viewLifecycleOwner.observe(viewModel.toggleFavoriteResult) { data ->
            setLoadingNplShopFollowers(false)
            data.doSuccessOrFail({
                onSuccessFavoriteShop(it.data.first, it.data.second)
                setupShopFavoriteToaster(it.data.second)
            }, {
                onFailFavoriteShop(it)
                logException(it)
            })
        }
    }

    private fun setupShopFavoriteToaster(isNplFollowerType: Boolean) {
        val isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        val message =
            if (isFavorite) {
                getString(productdetailcommonR.string.merchant_product_detail_success_follow_shop)
            } else {
                getString(
                    productdetailcommonR.string.merchant_product_detail_success_unfollow_shop
                )
            }

        view?.showToasterSuccess(if (isNplFollowerType) getString(productdetailcommonR.string.merchant_product_detail_success_follow_shop_npl) else message)
    }

    private fun showAtcSuccessToaster(result: AddToCartDataModel) {
        view?.showToasterSuccess(
            result.data.message.firstOrNull().orEmpty(),
            ctaText = getString(productdetailcommonR.string.pdp_common_oke)
        )
        sendTrackingATC(result.data.cartId)
        updateButtonState()
    }

    private fun onSuccessAtc(result: AddToCartDataModel) {
        val cartId = result.data.cartId

        if (viewModel.getProductInfoP1?.basic?.isTokoNow == true) {
            showAtcSuccessToaster(result)
            return
        }

        trackOnButtonClickCompleted(result)

        when (buttonActionType) {
            ProductDetailCommonConstant.OCS_BUTTON -> {
                if (result.data.success == 0) {
                    validateOvo(result)
                } else {
                    sendTrackingATC(cartId)
                    goToCheckout(
                        ShipmentFormRequest
                            .BundleBuilder()
                            .deviceId("")
                            .build()
                            .bundle
                    )
                }
            }

            ProductDetailCommonConstant.OCC_BUTTON -> {
                sendTrackingATC(cartId)
                if (result.isOccNewCheckoutPage) {
                    goToCheckout()
                } else {
                    goToOneClickCheckout()
                }
            }

            ProductDetailCommonConstant.BUY_BUTTON -> {
                sendTrackingATC(cartId)
                goToCartCheckout(cartId)
            }

            ProductDetailCommonConstant.ATC_BUTTON -> {
                sendTrackingATC(cartId)
                navToolbar?.updateNotification()
                showAddToCartDoneBottomSheet(cartDataModel = result.data)
            }

            ProductDetailCommonConstant.TRADEIN_AFTER_DIAGNOSE -> {
                // Same with OCS but should send devideId
                sendTrackingATC(cartId)
                goToCheckout(
                    ShipmentFormRequest.BundleBuilder()
                        .deviceId(viewModel.tradeinDeviceId)
                        .build()
                        .bundle
                )
            }
        }
    }

    private fun goToOneClickCheckout() {
        val p1 = viewModel.getProductInfoP1 ?: return
        val selectedPromoCodes = p1.data.promoPrice.promoCodes.mapIntoPromoExternalAutoApply()

        ProductCartHelper.goToOneClickCheckoutWithAutoApplyPromo(
            (context as ProductDetailActivity),
            ArrayList(selectedPromoCodes)
        )
    }

    private fun sendTrackingATC(cartId: String) {
        val boData = viewModel.getBebasOngkirDataByProductId()
        ProductDetailTracking.Click.eventEcommerceBuy(
            actionButton = buttonActionType,
            buttonText = viewModel.buttonActionText,
            userId = viewModel.userId,
            cartId = cartId,
            trackerAttribution = trackerAttributionPdp ?: "",
            multiOrigin = viewModel.getMultiOriginByProductId().isFulfillment,
            variantString = ProductDetailTracking.generateVariantString(
                viewModel.variantData,
                viewModel.getProductInfoP1?.basic?.productID.orEmpty()
            ),
            productInfo = viewModel.getProductInfoP1,
            boType = boData.boType,
            ratesEstimateData = viewModel.getP2RatesEstimateDataByProductId(),
            buyerDistrictId = viewModel.getUserLocationCache().district_id,
            sellerDistrictId = viewModel.getMultiOriginByProductId().districtId,
            lcaWarehouseId = getLcaWarehouseId()
        )
    }

    private fun validateOvo(result: AddToCartDataModel) {
        if (result.data.refreshPrerequisitePage) {
            onSwipeRefresh()
        } else {
            activity?.let {
                when (result.data.ovoValidationDataModel.status) {
                    ProductDetailCommonConstant.OVO_INACTIVE_STATUS -> {
                        val applink = "${result.data.ovoValidationDataModel.applink}&product_id=${
                        viewModel.getProductInfoP1?.parentProductId.orEmpty()
                        }"
                        ProductDetailTracking.Click.eventActivationOvo(
                            viewModel.getProductInfoP1?.parentProductId ?: "",
                            viewModel.userSessionInterface.userId
                        )
                        RouteManager.route(it, applink)
                    }

                    ProductDetailCommonConstant.OVO_INSUFFICIENT_BALANCE_STATUS -> {
                        val bottomSheetOvoDeals = OvoFlashDealsBottomSheet(
                            viewModel.getProductInfoP1?.parentProductId ?: "",
                            viewModel.userSessionInterface.userId,
                            result.data.ovoValidationDataModel
                        )
                        bottomSheetOvoDeals.show(it.supportFragmentManager, "Ovo Deals")
                    }

                    else -> view?.showToasterError(
                        getString(com.tokopedia.abstraction.R.string.default_request_error_unknown),
                        ctaText = getString(productdetailcommonR.string.pdp_common_oke)
                    )
                }
            }
        }
    }

    private fun goToCheckout(shipmentFormRequest: Bundle) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)
        intent.putExtras(shipmentFormRequest)
        startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
    }

    private fun goToCheckout() {
        val p1 = viewModel.getProductInfoP1 ?: return
        val selectedPromoCodes = p1.data.promoPrice.promoCodes.mapIntoPromoExternalAutoApply()

        ProductCartHelper.goToCheckoutWithAutoApplyPromo(
            (context as ProductDetailActivity),
            ArrayList(selectedPromoCodes)
        )
    }

    private fun goToCartCheckout(cartId: String) {
        val activity = activity ?: return
        ProductCartHelper.goToCartCheckout(activity, cartId)
    }

    override fun updateUi() {
        val newData =
            pdpUiUpdater?.getCurrentDataModels(viewModel.isAPlusContentExpanded()).orEmpty()

        val finalData = if (isTabletMode()) {
            manipulateDataTabletMode(newData)
        } else {
            newData
        }

        submitList(finalData)
    }

    private fun onSuccessGetDataP1(productInfo: ProductInfoP1) {
        updateProductId()

        productId?.let {
            pdpUiUpdater?.updateFintechData(
                it,
                viewModel.variantData,
                productInfo,
                viewModel.userSessionInterface.isLoggedIn
            )
        }
        renderVariant(viewModel.variantData)

        pdpUiUpdater?.updateDataP1(productInfo, true)

        pdpUiUpdater?.updateInitialAPlusContent(
            productInfo = productInfo,
            userID = viewModel.userId,
            aPlusContentExpanded = viewModel.isAPlusContentExpanded()
        )
        actionButtonView.setButtonP1(productInfo.data.preOrder)

        if ((productInfo.basic.category.isAdult && !viewModel.isUserSessionActive) ||
            (productInfo.basic.category.isAdult && !productInfo.basic.category.isKyc)
        ) {
            AdultManager.showAdultPopUp(
                this,
                AdultManager.ORIGIN_PDP,
                productInfo.basic.productID
            )
        }

        if (viewModel.getProductInfoP1?.isFromCache == false) {
            doP1DataAfterCloud()
        }

        setupProductVideoCoordinator()
        recommendationWidgetViewModel?.refresh()
        submitInitialList()
    }

    private fun doP1DataAfterCloud() {
        if (affiliateString.hasValue()) {
            viewModel.hitAffiliateTracker(affiliateString ?: "", viewModel.deviceId)
        }
    }

    private fun submitInitialList() {
        if (isRemoteCacheableActive()) {
            // when cache first then cloud is true, we want to keep refresh layout to UI
            // prevent interaction loading state for several component
            val submitInitialList =
                viewModel.getProductInfoP1?.cacheState?.cacheFirstThenCloud == false
            if (submitInitialList) {
                submitInitialList(
                    pdpUiUpdater?.getInitialItems(viewModel.isAPlusContentExpanded()).orEmpty(),
                    viewModel.getProductInfoP1?.cacheState
                )
            }
        } else {
            submitInitialList(
                pdpUiUpdater?.getInitialItems(viewModel.isAPlusContentExpanded()).orEmpty(),
                viewModel.getProductInfoP1?.cacheState
            )
        }
    }

    private fun setupProductVideoCoordinator() {
        if (pdpUiUpdater?.mediaMap?.isMediaContainsVideo() == true) {
            if (productVideoCoordinator == null) {
                productVideoCoordinator = ProductVideoCoordinator(viewLifecycleOwner)
            }
        } else {
            productVideoCoordinator = null
        }
    }

    private fun showOrHideButton() {
        actionButtonView.visibility = !viewModel.shouldHideFloatingButton()
        val shouldBeProcess = actionButtonView.visibility &&
            viewModel.getShopInfo().isShopInfoNotEmpty()

        if (shouldBeProcess) {
            val shopStatus = viewModel.getShopInfo().statusInfo.shopStatus
            val shouldShowSellerButtonByShopType =
                shopStatus != ShopStatusDef.DELETED && shopStatus != ShopStatusDef.MODERATED_PERMANENTLY
            if (viewModel.isShopOwner()) {
                actionButtonView.visibility = shouldShowSellerButtonByShopType
            } else {
                actionButtonView.visibility =
                    viewModel.getShopInfo().statusInfo.shopStatus == ShopStatusDef.OPEN
            }
        }
    }

    private fun renderRestrictionBottomSheet(data: RestrictionInfoResponse) {
        val reData = data.getReByProductId(
            viewModel.getProductInfoP1?.basic?.productID
                ?: ""
        )

        ProductDetailRestrictionHelper.renderRestrictionUi(
            reData = reData,
            isFavoriteShop = pdpUiUpdater?.shopCredibility?.isFavorite ?: false,
            isShopOwner = viewModel.isShopOwner(),
            reView = nplFollowersButton,
            impressLocationRestriction = {
                viewModel.changeOneTimeMethod(
                    OneTimeMethodEvent.ImpressRestriction(
                        reData ?: RestrictionData()
                    )
                )
            }
        )
    }

    private fun onSuccessGetDataP2(
        it: ProductInfoP2UiData,
        boeData: BebasOngkirImage,
        ratesData: P2RatesEstimate?,
        shipmentPlus: ShipmentPlus?
    ) {
        val minimumShippingPriceP2 = ratesData?.p2RatesData?.cheapestShippingPrice ?: 0.0
        if (minimumShippingPriceP2 != 0.0) {
            viewModel.shippingMinimumPrice = minimumShippingPriceP2
        }

        pdpUiUpdater?.removeComponentP2Data(
            it
        )

        renderRestrictionBottomSheet(it.restrictionInfo)
        updateButtonState()
        pdpUiUpdater?.updateShipmentData(
            ratesData,
            viewModel.getMultiOriginByProductId().isFulfillment,
            viewModel.getProductInfoP1?.data?.isCod ?: false,
            boeData,
            viewModel.getUserLocationCache(),
            shipmentPlus
        )

        pdpUiUpdater?.updateDataP2(
            context = context,
            p2Data = it,
            productId = viewModel.getProductInfoP1?.basic?.productID ?: "",
            boeImageUrl = boeData.imageURL
        )

        initNavigationTab(it)
        updateUi()
    }

    private fun initNavigationTab(data: ProductInfoP2UiData) {
        val navigation = binding?.pdpNavigation

        if (isTabletMode()) {
            navigation?.hide()
        } else {
            val items = data.navBar.items.map { item ->
                NavigationTab.Item(item.title, item.componentName) {
                    adapter.getComponentPositionByName(item.componentName)
                }
            }

            getRecyclerView()?.let { recyclerView ->
                if (items.isEmpty()) {
                    navigation?.stop(recyclerView)
                } else {
                    navigation?.start(recyclerView, items, this)
                }
            }
        }
    }

    override fun onButtonFollowNplClick() {
        val reData = viewModel.p2Data.value?.restrictionInfo?.getReByProductId(
            viewModel.getProductInfoP1?.basic?.productID ?: ""
        ) ?: return

        when {
            reData.restrictionShopFollowersType() -> {
                ProductDetailTracking.Click.eventClickFollowNpl(
                    viewModel.getProductInfoP1,
                    viewModel.userId
                )

                onShopFavoriteClick(isNplFollowType = true)
            }

            reData.restrictionCategoriesType() -> {
                reData.action.firstOrNull()?.buttonLink?.let {
                    if (it.isEmpty()) {
                        activity?.finish()
                    } else {
                        goToApplink(it)
                    }
                }
            }

            reData.restrictionGamificationType() -> {
                ProductDetailTracking.Click.onRestrictionGamificationClicked(
                    viewModel.getProductInfoP1,
                    reData,
                    viewModel.userId
                )
                reData.action.firstOrNull()?.buttonLink?.let {
                    goToApplink(it)
                }
            }

            reData.restrictionLocationType() -> {
                onRestrictionLocationClicked(reData)
            }
        }
    }

    private fun onRestrictionLocationClicked(reData: RestrictionData) {
        ProductTrackingCommon.Restriction.clickLocationRestriction(
            data = reData,
            userId = viewModel.userId,
            shopId = viewModel.getProductInfoP1?.basic?.shopID ?: ""
        )
        goToShipmentErrorAddressOrChat(Int.ZERO)
    }

    override fun logException(t: Throwable) {
        if (!BuildConfig.DEBUG) {
            val errorMessage = String.format(
                getString(R.string.on_error_p1_string_builder),
                viewModel.userSessionInterface.userId,
                t.message
            )
            FirebaseCrashlytics.getInstance().recordException(Exception(errorMessage, t))
        } else {
            t.printStackTrace()
        }
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute, state: Int) {
        if (variantOptions.isEmpty()) { // lihat semua label clicked
            goToAtcVariant()
        } else { // chip variant clicked
            onVariantContinuationToVBS(variantOptions)
            goToAtcVariant()
        }
    }

    /**
     * Update variant selected but not refresh the pdp UI
     * and also get product id which clicked to passing to VBS
     */
    private fun onVariantContinuationToVBS(variantOptions: VariantOptionWithAttribute) {
        pdpUiUpdater?.updateVariantOneLevel(variantOptions = variantOptions)
        val singleVariant = pdpUiUpdater?.productSingleVariant ?: return
        val child = viewModel.getChildOfVariantSelected(singleVariant)
        productId = child?.productId
    }

    override fun onThumbnailVariantSelected(variantId: String, categoryKey: String) {
        val singleVariantUiData = pdpUiUpdater?.productSingleVariant
        viewModel.onThumbnailVariantSelected(
            uiData = singleVariantUiData,
            variantId = variantId,
            categoryKey = categoryKey
        )

        ProductThumbnailVariantTracking.onItemClicked(
            trackingQueue = trackingQueue,
            productInfo = viewModel.getProductInfoP1,
            singleVariant = singleVariantUiData,
            componentPosition = getComponentPositionBeforeUpdate(singleVariantUiData),
            userId = viewModel.userId,
            variantId = variantId,
            variantKey = categoryKey
        )
    }

    override fun onThumbnailVariantImpress(data: VariantOptionWithAttribute, position: Int) {
        ProductThumbnailVariantTracking.onImpression(
            trackingQueue = trackingQueue,
            singleVariant = pdpUiUpdater?.productSingleVariant,
            data = data,
            position = position,
            productInfo = viewModel.getProductInfoP1,
            userId = viewModel.userId
        )
    }

    override fun onProductMediaRecomBottomSheetDismissed() {
        viewModel.dismissProductMediaRecomBottomSheet()
    }

    override fun onToggleAPlus(expanded: Boolean, trackerData: APlusImageUiModel.TrackerData) {
        viewModel.setAPlusContentExpandedState(expanded)
        pdpUiUpdater?.updateAPlusContentMediaOnExpandedStateChange(viewModel.isAPlusContentExpanded())
        updateUi()
        if (!viewModel.isAPlusContentExpanded()) {
            val seeMorePosition = adapter.getSeeMoreAPlusTogglePosition()
            if (seeMorePosition != RecyclerView.NO_POSITION) {
                binding?.pdpNavigation?.disableNavigationScrollUpListener()
                snapScrollToPosition(position = seeMorePosition)
            }
        }
        APlusContentTracking.trackClickExpandCollapseToggle(trackerData)
    }

    override fun onImpressAPlus(trackerData: APlusImageUiModel.TrackerData) {
        APlusContentTracking.trackImpressAPlusMedia(trackerData, trackingQueue)
    }

    override fun getImpressionHolders(): MutableList<String> {
        return viewModel.impressionHolders
    }

    override fun isRemoteCacheableActive(): Boolean = viewModel.getProductInfoP1
        ?.cacheState?.remoteCacheableActive.orFalse()

    private fun goToAtcVariant(customCartRedirection: Map<String, CartTypeData>? = null) {
        SingleClick.doSomethingBeforeTime(interval = DEBOUNCE_CLICK) {
            context?.let { ctx ->
                viewModel.getProductInfoP1?.let { p1 ->
                    ProductDetailTracking.Click.onSingleVariantClicked(
                        productInfo = p1,
                        variantUiData = pdpUiUpdater?.productSingleVariant,
                        variantCommonData = viewModel.variantData,
                        variantPosition = getComponentPositionBeforeUpdate(
                            pdpUiUpdater?.productSingleVariant
                        )
                    )

                    val p2Data = viewModel.p2Data.value
                    var saveAfterClose = true
                    var cartTypeData = p2Data?.cartRedirection
                    if (customCartRedirection != null) {
                        saveAfterClose = false
                        cartTypeData = customCartRedirection
                    }

                    viewModel.clearCacheP2Data()

                    AtcVariantHelper.pdpToAtcVariant(
                        context = ctx,
                        pageSource = VariantPageSource.PDP_PAGESOURCE,
                        productId = productId.orEmpty(),
                        productInfoP1 = p1,
                        warehouseId = warehouseId.orEmpty(),
                        pdpSession = p1.pdpSession,
                        isTokoNow = p1.basic.isTokoNow,
                        isShopOwner = viewModel.isShopOwner(),
                        productVariant = viewModel.variantData ?: ProductVariant(),
                        warehouseResponse = p2Data?.nearestWarehouseInfo ?: mapOf(),
                        cartRedirection = cartTypeData ?: mapOf(),
                        miniCart = p2Data?.miniCart,
                        alternateCopy = p2Data?.alternateCopy,
                        boData = p2Data?.bebasOngkir ?: BebasOngkir(),
                        rates = p2Data?.ratesEstimate ?: listOf(),
                        restrictionData = p2Data?.restrictionInfo,
                        isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: false,
                        uspImageUrl = p2Data?.uspImageUrl ?: "",
                        saveAfterClose = saveAfterClose,
                        cartViewLocation = getCartIconLocation()
                    ) { data, code ->
                        startActivityForResult(data, code)
                    }
                }
            }
        }
    }

    private fun getCartIconLocation(): Point? {
        val icon = navToolbar?.getCartIconPosition()
        return icon?.getLocationOnScreen()
    }

    private fun renderVariant(data: ProductVariant?) {
        if (data == null || !data.hasChildren || data.errorCode > 0) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.MINI_VARIANT_OPTIONS)
        } else {
            // ignore auto-select variant when first load pdp in thumbnail variant
            val selectedOptionIds = determineInitialOptionId(productId)
            val variantOneLevel = ProductDetailVariantLogic.determineVariant(
                mapOfSelectedOptionIds = selectedOptionIds,
                productVariant = data
            )
            if (variantOneLevel == null) {
                pdpUiUpdater?.removeComponent(ProductDetailConstant.MINI_VARIANT_OPTIONS)
                return
            }
            val landingSubText = viewModel.variantData?.landingSubText.ifNullOrBlank {
                getVariantString()
            }
            pdpUiUpdater?.updateVariantData(
                title = landingSubText,
                processedVariant = listOf(variantOneLevel)
            )
        }
    }

    private fun determineInitialOptionId(productId: String?): Map<String, String> {
        // thumbnail variant not auto-select when first open pdp or refresh event
        val singleVariant = pdpUiUpdater?.productSingleVariant ?: return emptyMap()
        if (singleVariant.isThumbnailType) {
            return emptyMap()
        }
        val variantData = viewModel.variantData ?: return emptyMap()
        val selectedChild = variantData.children.firstOrNull { it.productId == productId.orEmpty() }
        singleVariant.mapOfSelectedVariant = ProductDetailMapper.determineSelectedOptionIds(
            variantData,
            selectedChild
        )

        // thumbnail variant not auto-select when first open pdp or refresh event
        // but variant lvl-2 keep selected
        if (singleVariant.isThumbnailType) {
            singleVariant.mapOfSelectedVariant.keys.firstOrNull()?.let {
                singleVariant.mapOfSelectedVariant[it] = ""
            }
        }

        pdpUiUpdater?.updateSingleVariant(singleVariant)

        return singleVariant.mapOfSelectedVariant
    }

    private fun showAddToCartDoneBottomSheet(cartDataModel: DataModel) {
        val cartRedirData = viewModel.p2Data.value?.cartRedirection?.get(cartDataModel.productId)
        val postAtcLayout = cartRedirData?.postAtcLayout ?: PostAtcLayout()

        if (postAtcLayout.showPostAtc) {
            showGlobalPostATC(cartDataModel, cartDataModel.productId, postAtcLayout)
        } else {
            showAtcSuccessToaster(cartDataModel = cartDataModel)
        }
    }

    private fun showAtcSuccessToaster(cartDataModel: DataModel) {
        val message = context?.getString(
            productdetailcommonR.string.merchant_product_detail_success_atc_default
        ) ?: return

        view?.showToasterSuccess(
            message = message,
            ctaText = getString(productdetailcommonR.string.cta_text_atc_success),
            ctaListener = {
                goToCartCheckout(cartId = cartDataModel.cartId)
            }
        )
    }

    private fun showGlobalPostATC(
        cartDataModel: DataModel,
        productId: String,
        postAtcLayout: PostAtcLayout
    ) {
        val context = context ?: return

        val addonsIds = cartDataModel.addOns.groupBy(
            keySelector = { it.status },
            valueTransform = { it.id }
        )

        val addons = PostAtcParams.Addons(
            deselectedAddonsIds = addonsIds[2] ?: emptyList(),
            isFulfillment = cartDataModel.isFulfillment,
            selectedAddonsIds = addonsIds[1] ?: emptyList(),
            warehouseId = cartDataModel.warehouseId,
            quantity = cartDataModel.quantity
        )

        val postAtcParams = PostAtcParams(
            cartId = cartDataModel.cartId,
            layoutId = postAtcLayout.layoutId,
            pageSource = PostAtcParams.Source.PDP.name,
            session = postAtcLayout.postAtcSession,
            addons = addons
        )

        PostAtcHelper.start(
            context,
            productId,
            postAtcParams
        )
    }

    override fun openShipmentClickedBottomSheet(
        title: String,
        chipsLabel: List<String>,
        isCod: Boolean,
        isScheduled: Boolean,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        viewModel.getProductInfoP1?.let {
            if (isScheduled) {
                val common = CommonTracker(it, viewModel.userId)
                ShipmentTracking.sendClickLihatJadwalLainnyaOnScheduleDelivery(
                    chipsLabel,
                    common,
                    componentTrackDataModel
                )
            } else {
                ProductDetailTracking.Click.eventClickShipment(
                    viewModel.getProductInfoP1,
                    viewModel.userId,
                    componentTrackDataModel,
                    title,
                    chipsLabel,
                    isCod
                )
            }

            val boData = viewModel.getBebasOngkirDataByProductId()

            val productId = it.basic.productID

            sharedViewModel?.setRequestData(
                RatesEstimateRequest(
                    productWeight = it.basic.weight.toFloat(),
                    shopDomain = viewModel.getShopInfo().shopCore.domain,
                    origin = viewModel.getMultiOriginByProductId().getOrigin(),
                    shopId = it.basic.shopID,
                    productId = productId,
                    productWeightUnit = it.basic.weightUnit,
                    isFulfillment = viewModel.getMultiOriginByProductId().isFulfillment,
                    destination = generateUserLocationRequestRates(viewModel.getUserLocationCache()),
                    boType = boData.boType,
                    freeOngkirUrl = boData.imageURL,
                    poTime = it.data.preOrder.preorderInDays,
                    uspImageUrl = viewModel.p2Data.value?.uspImageUrl ?: "",
                    userId = viewModel.userId,
                    forceRefresh = shouldRefreshShippingBottomSheet,
                    shopTier = viewModel.getShopInfo().shopTier,
                    isTokoNow = it.basic.isTokoNow,
                    addressId = viewModel.getUserLocationCache().address_id,
                    warehouseId = viewModel.getMultiOriginByProductId().id,
                    orderValue = it.data.price.value,
                    boMetadata = viewModel.p2Data.value?.getRatesEstimateBoMetadata(productId)
                        ?: "",
                    productMetadata = viewModel.p2Data.value?.getRatesProductMetadata(productId)
                        ?: "",
                    categoryId = it.basic.category.id,
                    isScheduled = isScheduled,
                    weightWording = it.basic.weightWording
                )
            )
            shouldRefreshShippingBottomSheet = false

            showImmediately(
                fragmentManager = getProductFragmentManager(),
                tag = ProductDetailShippingBottomSheet::class.java.simpleName
            ) {
                ProductDetailShippingBottomSheet.instance(
                    buyerDistrictId = viewModel.getUserLocationCache().district_id,
                    sellerDistrictId = viewModel.getMultiOriginByProductId().districtId,
                    layoutId = layoutId
                )
            }
        }
    }

    override fun clickShippingComponentError(
        errorCode: Int,
        title: String,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        ProductDetailTracking.Click.eventClickShipmentErrorComponent(
            viewModel.getProductInfoP1,
            viewModel.userId,
            title,
            componentTrackDataModel
        )
        goToShipmentErrorAddressOrChat(errorCode)
    }

    private fun openShipmentBottomSheetWhenError(): Boolean {
        context?.let {
            val rates = viewModel.getP2RatesEstimateDataByProductId()
            val bottomSheetData = viewModel.getP2RatesBottomSheetData()

            if (rates?.p2RatesError?.isEmpty() == true || rates?.p2RatesError?.firstOrNull()?.errorCode == 0 || bottomSheetData == null) return false

            ProductDetailTracking.BottomSheetErrorShipment.impressShipmentErrorBottomSheet(
                viewModel.getProductInfoP1,
                viewModel.userId,
                bottomSheetData.title
            )
            ProductDetailCommonBottomSheetBuilder.getShippingErrorBottomSheet(
                it,
                bottomSheetData,
                rates?.p2RatesError?.firstOrNull()?.errorCode ?: 0,
                onButtonClicked = { errorCode ->
                    ProductDetailTracking.BottomSheetErrorShipment.eventClickButtonShipmentErrorBottomSheet(
                        viewModel.getProductInfoP1,
                        viewModel.userId,
                        bottomSheetData.title,
                        errorCode
                    )
                    goToShipmentErrorAddressOrChat(errorCode)
                },
                onHomeClicked = { goToHomePage() }
            ).show(childFragmentManager, ProductDetailConstant.BS_SHIPMENT_ERROR_TAG)
            return true
        } ?: return false
    }

    /**
     * Go To Home Page and Clear Back Stack
     */
    private fun goToHomePage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun goToShipmentErrorAddressOrChat(errorCode: Int) {
        if (errorCode == ProductDetailCommonConstant.SHIPPING_ERROR_WEIGHT) {
            onShopChatClicked()
        } else {
            ProductDetailBottomSheetBuilder.openChooseAddressBottomSheet(
                object :
                    ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
                    override fun onLocalizingAddressServerDown() {
                    }

                    override fun onAddressDataChanged() {
                        onSuccessUpdateAddress()
                    }

                    override fun getLocalizingAddressHostSourceBottomSheet(): String =
                        ProductDetailCommonConstant.KEY_PRODUCT_DETAIL

                    override fun onLocalizingAddressLoginSuccessBottomSheet() {
                    }

                    override fun onDismissChooseAddressBottomSheet() {
                    }
                },
                childFragmentManager
            )
        }
    }

    private fun updateCartNotification() {
        viewModel.updateCartCounerUseCase(::onSuccessUpdateCartCounter)
    }

    private fun onSuccessUpdateCartCounter(count: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART)
        cache.putInt(CartConstant.IS_HAS_CART, if (count > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, count)
        cache.applyEditor()
        if (isAdded) {
            navAbTestCondition({ setNavToolBarCartCounter() }, {
                // no op
            })
        }
    }

    private fun onClickShareProduct() {
        viewModel.getProductInfoP1?.let { productInfo ->
            handleShareProduct(productInfo)
        }
    }

    private fun handleShareProduct(
        dynamicProductInfoP1: ProductInfoP1
    ) {
        if (isUsingShareEx()) {
            openShareExBottomSheet(dynamicProductInfoP1)
        } else {
            ProductDetailTracking.Click.eventClickPdpShare(
                productId = dynamicProductInfoP1.basic.productID,
                userId = viewModel.userId,
                campaignId = zeroIfEmpty(dynamicProductInfoP1.data.campaign.campaignID),
                bundleId = "0",
                isAffiliateShareIcon = isAffiliateShareIcon
            )
            shareProduct(dynamicProductInfoP1)
        }
    }

    private fun openShareExBottomSheet(
        dynamicProductInfoP1: ProductInfoP1
    ) {
        val mediaPosition = pdpUiUpdater?.mediaMap?.indexOfSelectedVariantOptionId()?.coerceAtLeast(0).orZero()
        val productImageUrl = pdpUiUpdater?.mediaMap?.listOfMedia?.getOrNull(mediaPosition)?.urlOriginal.orEmpty()
        shareExInitializer?.openShareBottomSheet(
            generateShareExBottomSheetArg(
                productId = dynamicProductInfoP1.basic.productID,
                productUrl = dynamicProductInfoP1.basic.url,
                campaignId = dynamicProductInfoP1.data.campaign.campaignID,
                productImageUrl = productImageUrl
            )
        )
    }

    private fun shareProduct(productInfoP1: ProductInfoP1? = null, path: String? = null) {
        val productInfo = productInfoP1 ?: viewModel.getProductInfoP1
        if (productInfo != null) {
            val productData = generateProductShareData(
                productInfo = productInfo,
                userId = viewModel.userId,
                shopUrl = viewModel.getShopInfo().shopCore.url,
                bundleId = "0"
            )

            val shopInfo =
                if (viewModel.getShopInfo().isShopInfoNotEmpty()) viewModel.getShopInfo() else null
            val affiliateData =
                generateAffiliateShareData(productInfo, shopInfo, viewModel.variantData)
            val pdpImageGeneratorParam =
                generateImageGeneratorData(productInfo, viewModel.getBebasOngkirDataByProductId())
            checkAndExecuteReferralAction(productData, affiliateData, pdpImageGeneratorParam)
        }
    }

    private fun checkAndExecuteReferralAction(
        productData: ProductData,
        affiliateData: AffiliateInput,
        imageGeneratorData: PdpParamModel,
        path: String? = null
    ) {
        val fireBaseRemoteMsgGuest =
            remoteConfig.getString(RemoteConfigKey.fireBaseGuestShareMsgKey, "")
                ?: ""
        if (!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) {
            productData.productShareDescription = fireBaseRemoteMsgGuest
        }

        if (viewModel.userSessionInterface.isLoggedIn && viewModel.userSessionInterface.isMsisdnVerified) {
            val fireBaseRemoteMsg = remoteConfig.getString(RemoteConfigKey.fireBaseShareMsgKey, "")
                ?: ""
            if (!TextUtils.isEmpty(fireBaseRemoteMsg) && fireBaseRemoteMsg.contains(ProductData.PLACEHOLDER_REFERRAL_CODE)) {
                doReferralShareAction(
                    productData,
                    fireBaseRemoteMsg,
                    affiliateData,
                    imageGeneratorData
                )
                return
            }
        }
        executeProductShare(productData, affiliateData, imageGeneratorData, path)
    }

    private fun doReferralShareAction(
        productData: ProductData,
        fireBaseRemoteMsg: String,
        affiliateData: AffiliateInput,
        imageGeneratorData: PdpParamModel
    ) {
        val actionCreator = object : ActionCreator<String, Int> {
            override fun actionSuccess(actionId: Int, dataObj: String) {
                if (!TextUtils.isEmpty(dataObj) && !TextUtils.isEmpty(fireBaseRemoteMsg)) {
                    productData.productShareDescription =
                        FindAndReplaceHelper.findAndReplacePlaceHolders(
                            fireBaseRemoteMsg,
                            ProductData.PLACEHOLDER_REFERRAL_CODE,
                            dataObj
                        )
                }
                executeProductShare(productData, affiliateData, imageGeneratorData)
            }

            override fun actionError(actionId: Int, dataObj: Int?) {
                executeProductShare(productData, affiliateData, imageGeneratorData)
            }
        }
        val referralAction = ReferralAction<Context, String, Int, String, String, String, Context>()
        referralAction.doAction(
            Constants.Action.ACTION_GET_REFERRAL_CODE,
            context,
            actionCreator,
            object : ActionUIDelegate<String, String> {
                override fun waitForResult(actionId: Int, dataObj: String?) {
                    showProgressDialog()
                }

                override fun stopWaiting(actionId: Int, dataObj: String?) {
                    hideProgressDialog()
                }
            }
        )
    }

    private fun executeProductShare(
        productData: ProductData,
        affiliateData: AffiliateInput,
        imageGeneratorData: PdpParamModel,
        path: String? = null
    ) {
        val enablePdpCustomSharing = remoteConfig.getBoolean(
            REMOTE_CONFIG_KEY_ENABLE_PDP_CUSTOM_SHARING,
            REMOTE_CONFIG_DEFAULT_ENABLE_PDP_CUSTOM_SHARING
        )
        if (SharingUtil.isCustomSharingEnabled(context) && enablePdpCustomSharing) {
            val description = pdpUiUpdater?.productDetailInfoData?.getDescription()?.take(100)
                ?.replace("(\r\n|\n)".toRegex(), " ")
                ?: ""
            productData.productShareDescription = "$description..."
            executeUniversalShare(productData, affiliateData, imageGeneratorData, path)
        } else {
            executeNativeShare(productData)
        }
    }

    private fun executeNativeShare(productData: ProductData) {
        shareProductInstance?.share(productData, {
            showProgressDialog {
                shareProductInstance?.cancelShare(true)
            }
        }, {
            hideProgressDialog()
        }, true)
    }

    private fun executeUniversalShare(
        productData: ProductData,
        affiliateData: AffiliateInput,
        imageGeneratorData: PdpParamModel,
        path: String? = null
    ) {
        activity?.let {
            val imageUrls = pdpUiUpdater?.mediaMap?.listOfMedia
                ?.filter { it.type == ProductMediaDataModel.IMAGE_TYPE }
                ?.map { it.urlOriginal } ?: emptyList()
            val personalizedCampaignModel = viewModel.getProductInfoP1?.let { product ->
                generatePersonalizedData(product, viewModel.p2Data.value)
            } ?: PersonalizedCampaignModel()

            shareProductInstance?.showUniversalShareBottomSheet(
                fragmentManager = it.supportFragmentManager,
                fragment = this,
                data = productData,
                affiliateInput = affiliateData,
                isLog = true,
                view = view,
                productImgList = ArrayList(imageUrls),
                preBuildImg = {
                    showLoadingUniversalShare()
                },
                postBuildImg = { hideProgressDialog() },
                screenshotDetector,
                imageGeneratorData,
                personalizedCampaignModel,
                screenshotPath = path
            )
        }
    }

    private fun showLoadingUniversalShare() {
        activity?.let {
            if (!it.isFinishing) {
                showProgressDialog {
                    shareProductInstance?.cancelShare(true)
                }
            }
        }
    }

    override fun getBlocksPerformanceTrace(): BlocksPerformanceTrace? {
        return getProductDetailActivity()?.getBlocksPerformanceMonitoring()
    }

    override fun reportProductFromComponent(componentTrackDataModel: ComponentTrackDataModel?) {
        reportProduct({
            ProductDetailTracking.Click.eventClickReportFromComponent(
                viewModel.getProductInfoP1,
                viewModel.userId,
                componentTrackDataModel
            )
        }, componentTrackDataModel)
    }

    override fun onBuyerPhotosClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        socialProofMediaTracking(trackData = componentTrackDataModel)
        goToReviewImagePreview()
    }

    private fun reportProduct(
        trackerLogin: (() -> Unit)? = null,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        viewModel.getProductInfoP1?.run {
            ProductDetailTracking.Click.eventClickReportFromComponent(
                this,
                viewModel.userId,
                componentTrackDataModel
            )
            doActionOrLogin({
                context?.let {
                    trackerLogin?.invoke()
                    var deeplink = UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.REPORT_PRODUCT,
                        basic.productID
                    )
                    deeplink = Uri.parse(deeplink).buildUpon().appendQueryParameter(
                        ApplinkConst.DFFALLBACKURL_KEY,
                        ProductDetailMapper.generateProductReportFallback(basic.url)
                    ).toString()
                    val intent = RouteManager.getIntent(it, deeplink)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_REPORT)
                }
            }, {
                // no op
            })
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return

        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                recomWishlistItem?.isWishlist = !(recomWishlistItem?.isWishlist ?: false)
                recomWishlistItem?.let {
                    ProductDetailTracking.Click.eventAddToCartRecommendationWishlist(
                        it,
                        viewModel.userSessionInterface.isLoggedIn,
                        wishlistResult.isAddWishlist
                    )
                }

                context?.let { context ->
                    view?.let { v ->
                        if (wishlistResult.isAddWishlist) {
                            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                                wishlistResult,
                                context,
                                v
                            )
                            if (productCardOptionsModel.isTopAds) {
                                hitWishlistClickUrl(
                                    productCardOptionsModel
                                )
                            }
                        } else {
                            AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                                wishlistResult,
                                context,
                                v
                            )
                        }
                    }
                }
            } else {
                var errorMessage = getString(
                    com.tokopedia.wishlist_common.R.string.on_failed_remove_from_wishlist_msg
                )
                if (wishlistResult.isAddWishlist) {
                    errorMessage = getString(
                        com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg
                    )
                }

                view?.let { v ->
                    if (wishlistResult.messageV2.isNotEmpty()) {
                        errorMessage = wishlistResult.messageV2
                    }
                    if (wishlistResult.ctaTextV2.isNotEmpty() && wishlistResult.ctaActionV2.isNotEmpty()) {
                        context?.let { c ->
                            AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                                errorMessage,
                                wishlistResult.ctaTextV2,
                                wishlistResult.ctaActionV2,
                                v,
                                c
                            )
                        }
                    } else {
                        AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMessage, v)
                    }
                }
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }

        recomWishlistItem = null
    }

    private fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                this::class.java.simpleName,
                productCardOptionsModel.topAdsClickUrl + CLICK_TYPE_WISHLIST,
                productCardOptionsModel.productId,
                productCardOptionsModel.productName,
                productCardOptionsModel.productImageUrl
            )
        }
    }

    private fun trackProductView(isElligible: Boolean, boType: Int) {
        if (viewModel.impressionHolders.contains(ProductDetailViewsBundler.KEY)) return
        viewModel.impressionHolders.add(ProductDetailViewsBundler.KEY)

        ProductDetailTracking.Impression.eventProductView(
            productInfo = viewModel.getProductInfoP1,
            shopInfo = viewModel.getShopInfo(),
            irisSessionId = irisSessionId,
            trackerListName = trackerListNamePdp,
            trackerAttribution = trackerAttributionPdp,
            isTradeIn = isElligible,
            isDiagnosed = getTradeinData().usedPrice.toDoubleOrZero() > 0,
            multiOrigin = viewModel.getMultiOriginByProductId().isFulfillment,
            deeplinkUrl = deeplinkUrl,
            isStockAvailable = viewModel.getProductInfoP1?.getFinalStock() ?: "0",
            boType = boType,
            affiliateUniqueId = affiliateUniqueId,
            uuid = uuid,
            ratesEstimateData = viewModel.getP2RatesEstimateDataByProductId(),
            buyerDistrictId = viewModel.getUserLocationCache().district_id,
            sellerDistrictId = viewModel.getMultiOriginByProductId().districtId,
            lcaWarehouseId = getLcaWarehouseId(),
            campaignId = campaignId,
            variantId = variantId,
            offerId = viewModel.getP2()?.getOfferIdPriority(pid = productId).orEmpty(),
            nearestWarehouseId = viewModel.getMultiOriginByProductId().id
        )
    }

    private fun getLcaWarehouseId(): String {
        return viewModel.getUserLocationCache().warehouse_id
    }

    /**
     * @param url : linkUrl for insurance partner to be rendered in web-view
     */
    private fun openFtInsuranceWebView(url: String) {
        val semalessUrl = generateURLSessionLogin(url, viewModel.deviceId, viewModel.userId)
        val webViewUrl = String.format(
            Locale.getDefault(),
            "%s?titlebar=true&url=%s",
            ApplinkConst.WEBVIEW,
            semalessUrl
        )
        RouteManager.route(context, webViewUrl)
    }

    private fun sendIntentResultWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
            .putExtra(
                ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION,
                activity?.intent?.getIntExtra(
                    ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION,
                    -1
                )
            )
        resultIntent.putExtra(ProductDetailConstant.WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        resultIntent.putExtra(BOOLEAN_EXTRA_NEED_REFRESH, true)
        activity?.let { it.setResult(Activity.RESULT_OK, resultIntent) }
    }

    private fun gotoEditProduct() {
        val id = viewModel.parentProductId ?: return

        val applink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
            .buildUpon()
            .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, id)
            .appendQueryParameter(
                ApplinkConstInternalMechant.QUERY_PARAM_MODE,
                ApplinkConstInternalMechant.MODE_EDIT_PRODUCT
            )
            .build()
            .toString()
        context?.run {
            RouteManager.getIntent(this, applink)?.apply {
                startActivityForResult(this, ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT)
            }
        }
    }

    private fun initToolbarSellerApp() {
        navToolbar?.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
        navToolbar?.setToolbarTitle(getString(R.string.title_activity_product_detail))
        navToolbar?.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
        navToolbar?.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)

            setIconCustomColor(
                darkColor = getLightToolbarIconColor(),
                lightColor = getDarkToolbarIconColor()
            )

            setIcon(
                IconBuilder()
                    .addIcon(IconList.ID_SHARE) {
                        onClickShareProduct()
                    }

            )
            setToolbarPageName(ProductTrackingConstant.Category.PDP)
            show()
        }
    }

    private fun initToolbarMainApp() {
        navToolbar?.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)

            setIconCustomColor(
                darkColor = getLightToolbarIconColor(),
                lightColor = getDarkToolbarIconColor()
            )

            setIcon(
                IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.PDP))
                    .addIcon(IconList.ID_SEARCH, disableRouteManager = true) {
                        goToApplink(getLocalSearchApplink())
                    }
                    .addIcon(IconList.ID_SHARE) {
                        onClickShareProduct()
                    }
                    .addIcon(IconList.ID_CART) {}
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            )

            setToolbarPageName(ProductTrackingConstant.Category.PDP)
            setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
            show()
        }
    }

    private fun updateToolbarShareAffiliate() {
        if (isShareAffiliateIconEnabled() && !GlobalConfig.isSellerApp()) {
            isAffiliateShareIcon = true
            navToolbar?.updateIcon(IconList.ID_SHARE, IconList.ID_SHARE_AB_TEST) ?: return
        }
    }

    private fun isShareAffiliateIconEnabled(): Boolean {
        val isAbTestEnabled = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.AFFILIATE_SHARE_ICON
        ) == RollenceKey.AFFILIATE_SHARE_ICON

        return isAbTestEnabled
    }

    private fun getDarkToolbarIconColor(): Int = ContextCompat.getColor(
        requireContext(),
        com.tokopedia.unifyprinciples.R.color.Unify_Static_White
    )

    private fun getLightToolbarIconColor(): Int {
        val unifyColor = if (requireContext().isDarkMode()) {
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        } else {
            com.tokopedia.searchbar.R.color.searchbar_dms_state_light_icon
        }
        return ContextCompat.getColor(requireContext(), unifyColor)
    }

    private fun getLocalSearchApplink(): String {
        val basic = viewModel.getProductInfoP1?.basic
        val isTokoNow = basic?.isTokoNow == true
        val categoryName = basic?.category?.name.orEmpty()
        val applink = ApplinkConstInternalDiscovery.AUTOCOMPLETE + if (isTokoNow) {
            "?${SearchApiConst.NAVSOURCE}=tokonow&" +
                "${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokopediaNow.SEARCH}&" +
                "${SearchApiConst.Q}=$categoryName"
        } else {
            "?${SearchApiConst.Q}=$categoryName"
        }
        return applink
    }

    private fun initStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_pdp)
        stickyLoginView?.page = StickyLoginConstant.Page.PDP
        stickyLoginView?.lifecycleOwner = viewLifecycleOwner
        stickyLoginView?.setStickyAction(object : StickyLoginAction {
            override fun onClick() {
                goToLogin()
            }

            override fun onDismiss() {
            }

            override fun onViewChange(isShowing: Boolean) {
                updateActionButtonShadow()
            }
        })
        stickyLoginView?.hide()
    }

    private fun goToPdpSellerApp() {
        val appLink = UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        val parameterizedAppLink = Uri.parse(appLink).buildUpon()
            .appendQueryParameter(
                SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                SellerMigrationFeatureName.FEATURE_ADS_DETAIL
            )
            .build()
            .toString()
        goToSellerMigrationPage(
            SellerMigrationFeatureName.FEATURE_ADS_DETAIL,
            arrayListOf(
                ApplinkConst.PRODUCT_MANAGE,
                parameterizedAppLink
            )
        )
    }

    override fun advertiseProductClicked() {
        ProductDetailTracking.Click.eventTopAdsButtonClicked(
            viewModel.userId,
            binding?.partialLayoutButtonAction?.btnTopAds?.text.toString(),
            viewModel.getProductInfoP1
        )
        val firstAppLink =
            UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        val secondAppLink = ApplinkConstInternalTopAds.TOPADS_MP_ADS_CREATION
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(context, secondAppLink, productId)
        } else {
            if (secondAppLink.isEmpty()) {
                goToPdpSellerApp()
            } else {
                goToSellerMigrationPage(
                    SellerMigrationFeatureName.FEATURE_ADS,
                    arrayListOf(
                        ApplinkConst.PRODUCT_MANAGE,
                        firstAppLink,
                        secondAppLink
                    )
                )
            }
        }
    }

    override fun rincianTopAdsClicked() {
        ProductDetailTracking.Click.eventTopAdsButtonClicked(
            viewModel.userId,
            binding?.partialLayoutButtonAction?.btnTopAds?.text.toString(),
            viewModel.getProductInfoP1
        )
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(
                context,
                ApplinkConstInternalTopAds.TOPADS_SEE_ADS_PERFORMANCE,
                productId,
                TOPADS_PERFORMANCE_CURRENT_SITE
            )
        } else {
            goToPdpSellerApp()
        }
    }

    override fun onButtonFallbackClick(button: AvailableButton) {
        viewModel.buttonActionText = button.buttonText
        doAtc(button)
    }

    override fun buttonCartTypeClick(button: AvailableButton) {
        viewModel.buttonActionText = button.buttonText
        trackOnButtonClick(button.cartType)
        doAtc(button)
    }

    override fun topChatButtonClicked() {
        ProductDetailTracking.Click.eventButtonChatClicked(viewModel.getProductInfoP1)
        onShopChatClicked()
    }

    override fun onDeleteAtcClicked() {
        if (!viewModel.isUserSessionActive) {
            doLoginWhenUserClickButton()
            return
        }
        actionButtonView.showLoading()
        viewModel.deleteProductInCart(viewModel.getProductInfoP1?.basic?.productID ?: "")
    }

    override fun onButtonsShowed(cartTypes: List<String>) {
        trackOnButtonsShowed(cartTypes)
    }

    override fun updateQuantityNonVarTokoNow(
        quantity: Int,
        miniCart: MiniCartItem.MiniCartItemProduct,
        oldValue: Int
    ) {
        if (!viewModel.isUserSessionActive) {
            doLoginWhenUserClickButton()
            return
        }

        if (openShipmentBottomSheetWhenError()) return
        if (!alreadyHitQtyTracker) {
            alreadyHitQtyTracker = true
            ProductDetailTracking.Click.onQuantityEditorClicked(
                viewModel.getProductInfoP1?.basic?.productID.orEmpty(),
                oldValue,
                quantity
            )
        }

        viewModel.updateQuantity(quantity, miniCart)
    }

    override fun editProductButtonClicked() {
        val shopInfo = viewModel.getShopInfo()
        val productInfo = viewModel.getProductInfoP1
        if (shopInfo.isShopInfoNotEmpty() && shopInfo.isAllowManage == 1) {
            if (productInfo?.basic?.status != ProductStatusTypeDef.PENDING) {
                ProductDetailTracking.Click.eventEditProductClick(
                    viewModel.isUserSessionActive,
                    viewModel.getProductInfoP1,
                    ComponentTrackDataModel()
                )
                gotoEditProduct()
            } else {
                activity?.run {
                    val statusMessage = productInfo.basic.statusMessage(this)
                    if (statusMessage.isNotEmpty()) {
                        view.showToasterError(
                            getString(
                                R.string.product_is_at_status_x,
                                statusMessage
                            ),
                            ctaText = getString(com.tokopedia.abstraction.R.string.close)
                        )
                    }
                }
            }
        }
    }

    override fun getRxCompositeSubcription(): CompositeSubscription {
        return compositeSubscription
    }

    /**
     * ProductDetailInfoBottomSheet Listener
     */
    override fun getPdpDataSource(): ProductInfoP1? {
        return viewModel.getProductInfoP1
    }

    override fun goToTalkReadingBottomSheet() {
        viewModel.getProductInfoP1?.let {
            ProductDetailTracking.ProductDetailSheet.onCheckDiscussionSheetClicked(
                it,
                viewModel.userId
            )
        }
        goToReadingActivity()
    }

    override fun onDiscussionSendQuestionBottomSheetClicked() {
        writeDiscussion {
            ProductDetailTracking.ProductDetailSheet.onWriteDiscussionSheetClicked(
                viewModel.getProductInfoP1,
                viewModel.userId
            )
        }
    }

    private fun initBtnAction() {
        if (!::actionButtonView.isInitialized) {
            binding?.partialLayoutButtonAction?.baseBtnAction?.let {
                actionButtonView = PartialButtonActionView.build(it, this)
            }
        }
    }

    private fun clickButtonWhenVariantTokonow() {
        if (buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
            ProductDetailTracking.Click.eventClickOosButton(
                binding?.partialLayoutButtonAction?.btnBuyNow?.text.toString(),
                true,
                viewModel.getProductInfoP1,
                viewModel.userId
            )
            goToWishlist()
        } else {
            ProductDetailTracking.Click.eventClickAtcToVariantBottomSheet(
                viewModel.getProductInfoP1?.basic?.productID
                    ?: ""
            )
            goToAtcVariant()
        }
    }

    private fun doAtc(button: AvailableButton) {
        buttonActionType = button.atcKey
        context?.let {
            val isVariant = viewModel.getProductInfoP1?.data?.variant?.isVariant ?: false
            if (isVariant && pdpUiUpdater?.productSingleVariant != null) {
                clickButtonWhenVariantTokonow()
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.REMIND_ME_BUTTON || buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
                ProductDetailTracking.Click.eventClickOosButton(
                    binding?.partialLayoutButtonAction?.btnBuyNow?.text.toString(),
                    isVariant,
                    viewModel.getProductInfoP1,
                    viewModel.userId
                )
            }

            if (!viewModel.isUserSessionActive) {
                doLoginWhenUserClickButton()
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.REMIND_ME_BUTTON) {
                context?.let { context ->
                    addWishlistV2(null)
                }
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
                goToWishlist()
                return@let
            }

            if (openShipmentBottomSheetWhenError()) return@let

            hitAtc(button = button)
        }
    }

    private fun doLoginWhenUserClickButton() {
        ProductDetailTracking.Click.eventClickButtonNonLogin(
            buttonActionType,
            viewModel.getProductInfoP1,
            viewModel.userId,
            viewModel.getProductInfoP1?.shopTypeString ?: "",
            viewModel.buttonActionText
        )
        goToLogin()
    }

    private fun buyAfterTradeinDiagnose(deviceId: String) {
        buttonActionType = ProductDetailCommonConstant.TRADEIN_AFTER_DIAGNOSE
        viewModel.tradeinDeviceId = deviceId
        hitAtc(AvailableButton.createOCSButton())
    }

    private fun hitAtc(button: AvailableButton) {
        if (button.atcKey == ProductDetailCommonConstant.ATC_BUTTON) {
            EmbraceMonitoring.startMoments(EmbraceKey.KEY_ACT_ADD_TO_CART)
        }

        val selectedWarehouseId = viewModel.getMultiOriginByProductId().id

        viewModel.getProductInfoP1?.let { data ->
            atcAnimation.runAtcAnimation(
                binding,
                button.showAnimation
            )
            actionButtonView.showLoading()
            when (button.atcKey) {
                ProductDetailCommonConstant.OCS_BUTTON -> {
                    val addToCartOcsRequestParams = AddToCartOcsRequestParams().apply {
                        productId = data.basic.productID
                        shopId = data.basic.shopID
                        quantity = data.basic.minOrder
                        notes = ""
                        customerId = viewModel.userId
                        warehouseId = selectedWarehouseId
                        trackerAttribution = trackerAttributionPdp ?: ""
                        trackerListName = trackerListNamePdp ?: ""
                        isTradeIn = data.data.isTradeIn
                        shippingPrice = viewModel.shippingMinimumPrice
                        productName = data.getProductName
                        category = data.basic.category.name
                        price = data.finalPrice.toString()
                        userId = viewModel.userId
                        shopName = data.basic.shopName
                        trackerData = AppLogAnalytics.getEntranceInfo(AtcBuyType.OCS)
                    }
                    viewModel.addToCart(addToCartOcsRequestParams)
                }

                ProductDetailCommonConstant.OCC_BUTTON -> {
                    addToCartOcc(data, selectedWarehouseId)
                }

                else -> {
                    val addToCartRequestParams = AddToCartRequestParams().apply {
                        productId = data.basic.productID
                        shopId = data.basic.shopID
                        quantity = data.basic.minOrder
                        notes = ""
                        attribution = trackerAttributionPdp ?: ""
                        listTracker = trackerListNamePdp ?: ""
                        warehouseId = selectedWarehouseId.toString()
                        atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PDP
                        productName = data.getProductName
                        category = data.basic.category.name
                        price = data.finalPrice.toString()
                        userId = viewModel.userId
                        trackerData = AppLogAnalytics.getEntranceInfo(AtcBuyType.ATC)
                    }
                    viewModel.addToCart(addToCartRequestParams)
                }
            }
        }
    }

    private fun addToCartOcc(data: ProductInfoP1, selectedWarehouseId: String) {
        val addToCartOccRequestParams = AddToCartOccMultiRequestParams(
            carts = listOf(
                AddToCartOccMultiCartParam(
                    productId = data.basic.productID,
                    shopId = data.basic.shopID,
                    quantity = data.basic.minOrder.toString()
                ).apply {
                    warehouseId = selectedWarehouseId
                    attribution = trackerAttributionPdp ?: ""
                    listTracker = trackerListNamePdp ?: ""
                    productName = data.getProductName
                    category = data.basic.category.name
                    price = data.finalPrice.toString()
                    shopName = data.basic.shopName
                }
            ),
            userId = viewModel.userId,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PDP,
            trackerData = AppLogAnalytics.getEntranceInfo(AtcBuyType.OCC)
        )
        viewModel.addToCart(addToCartOccRequestParams)
    }

    private fun openWebViewUrl(url: String) {
        val webViewUrl = String.format(
            Locale.getDefault(),
            "%s?titlebar=false&url=%s",
            ApplinkConst.WEBVIEW,
            url
        )
        RouteManager.route(context, webViewUrl)
    }

    private fun goToWishlist() {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    private fun goToWishlistCollection(collectionId: String) {
        RouteManager.route(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, collectionId)
    }

    override fun gotoShopDetail(componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            val shopId = viewModel.getProductInfoP1?.basic?.shopID ?: return
            ProductDetailTracking.Click.eventImageShopClicked(
                viewModel.getProductInfoP1,
                shopId,
                componentTrackDataModel
            )
            val intent = RouteManager.getIntent(
                it,
                ApplinkConst.SHOP,
                shopId
            )

            val shopCredibility = pdpUiUpdater?.shopCredibility ?: return

            val prefetch = ShopPagePrefetch()
            val prefetchData = shopCredibility.toShopPrefetchData()
            prefetch.redirectToShopPageWithPrefetch(context ?: return, prefetchData, intent)

            startActivityForResult(
                intent,
                ProductDetailConstant.REQUEST_CODE_SHOP_INFO
            )
        }
    }

    private fun ProductShopCredibilityDataModel.toShopPrefetchData(): ShopPrefetchData {
        val averageShopRatingId = context?.getString(R.string.pdp_product_average_review).orEmpty()
        val shopRating = infoShopData.firstOrNull { it.desc == averageShopRatingId }?.value
        val castedShopRating = try {
            shopRating.toFloatOrZero()
        } catch (e: Exception) {
            Timber.e(e)
            0f
        }

        return ShopPrefetchData(
            shopAvatar = shopAva,
            shopName = shopName,
            shopBadge = shopTierBadgeUrl,
            shopLastOnline = shopLastActive,
            shopRating = castedShopRating,
            isFollowed = isFavorite
        )
    }

    override fun onShopTickerClicked(
        tickerDataResponse: ShopInfo.TickerDataResponse,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        if (tickerDataResponse.action.isEmpty()) return
        val productInfo = viewModel.getProductInfoP1 ?: return

        SingleClick.doSomethingBeforeTime(interval = DEBOUNCE_CLICK) {
            ShopCredibilityTracking.clickShopTicker(
                ShopCredibilityTracker.ClickShopTicker(
                    productInfo,
                    componentTrackDataModel,
                    tickerDataResponse,
                    viewModel.userId
                )
            )

            if (tickerDataResponse.action == "applink") {
                val applink = tickerDataResponse.actionLink
                if (activity != null && RouteManager.isSupportApplink(activity, applink)) {
                    goToApplink(applink)
                } else {
                    openWebViewUrl(applink)
                }
            } else {
                val bottomSheetData = tickerDataResponse.actionBottomSheet
                goToBottomSheetTicker(
                    title = bottomSheetData.title,
                    message = bottomSheetData.message,
                    reason = bottomSheetData.reason,
                    buttonText = bottomSheetData.buttonText,
                    buttonLink = bottomSheetData.buttonLink
                )
            }
        }
    }

    override fun onShopTickerImpressed(
        tickerDataResponse: ShopInfo.TickerDataResponse,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val data = ShopCredibilityTracker.ImpressionShopTicker(
            productInfo = productInfo,
            componentTrackDataModel = componentTrackDataModel,
            tickerDataResponse = tickerDataResponse,
            userId = viewModel.userId
        )
        ShopCredibilityTracking.impressShopTicker(data, trackingQueue)
    }

    private fun onShopFavoriteClick(
        componentTrackDataModel: ComponentTrackDataModel? = null,
        isNplFollowType: Boolean = false
    ) {
        if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
            doActionOrLogin({
                setLoadingNplShopFollowers(true)
                trackToggleFavoriteShop(componentTrackDataModel)
                pdpUiUpdater?.shopCredibility?.enableButtonFavorite = false
                viewModel.toggleFavorite(
                    viewModel.getProductInfoP1?.basic?.shopID.orEmpty(),
                    isNplFollowType
                )
            })
        }
    }

    private fun trackToggleFavoriteShop(componentTrackDataModel: ComponentTrackDataModel?) {
        val isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        val shopName = pdpUiUpdater?.shopCredibility?.shopName ?: ""

        if (isFavorite) {
            ProductDetailTracking.Click.eventUnfollowShop(
                viewModel.getProductInfoP1,
                componentTrackDataModel,
                shopName
            )
        } else {
            ProductDetailTracking.Click.eventFollowShop(
                viewModel.getProductInfoP1,
                componentTrackDataModel,
                shopName
            )
        }
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean, isNplFollowerType: Boolean = false) {
        val isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        if (isSuccess) {
            viewModel.clearCacheP2Data()
            pdpUiUpdater?.successUpdateShopFollow(if (isNplFollowerType) false else isFavorite)
            updateUi()
            setupNplVisibility(if (isNplFollowerType) ProductDetailConstant.HIDE_NPL_BS else isFavorite)
        }
    }

    private fun setupNplVisibility(isFavorite: Boolean) {
        val reData = viewModel.p2Data.value?.restrictionInfo?.getReByProductId(
            viewModel.getProductInfoP1?.basic?.productID
                ?: ""
        )
        if (reData?.restrictionShopFollowersType() == false) return

        nplFollowersButton?.setupVisibility =
            if (reData != null && reData.action.isNotEmpty() && !viewModel.isShopOwner()) {
                isFavorite
            } else {
                false
            }
    }

    private fun setLoadingNplShopFollowers(isLoading: Boolean) {
        val restrictionData = viewModel.p2Data.value?.restrictionInfo
        if (restrictionData?.restrictionData?.firstOrNull()
            ?.restrictionShopFollowersType() == false
        ) {
            return
        }
        if (isLoading) {
            nplFollowersButton?.startLoading()
        } else {
            nplFollowersButton?.stopLoading()
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        view?.showToasterError(
            getErrorMessage(t),
            ctaText = getString(com.tokopedia.abstraction.R.string.retry_label)
        ) {
            onShopFavoriteClick()
        }
        pdpUiUpdater?.failUpdateShopFollow()
        updateUi()
    }

    private fun onShopChatClicked() {
        if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
            val product = viewModel.getProductInfoP1 ?: return
            doActionOrLogin({
                val shop = viewModel.getShopInfo()
                activity?.let {
                    val intent = RouteManager.getIntent(
                        it,
                        ApplinkConst.TOPCHAT_ASKSELLER,
                        product.basic.shopID,
                        "",
                        ApplinkConst.Chat.Source.SOURCE_PDP,
                        shop.shopCore.name,
                        shop.shopAssets.avatar
                    )
                    VariantMapper.putChatProductInfoTo(intent, product.basic.productID)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_TOP_CHAT)
                }
            })
        }
    }

    private fun updateActionButtonShadow() {
        if (stickyLoginView?.isShowing() == true) {
            actionButtonView.setBackground(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        } else {
            val drawable = context?.let { _context ->
                ContextCompat.getDrawable(
                    _context,
                    productdetailcommonR.drawable.bg_shadow_top
                )
            }
            drawable?.let { actionButtonView.setBackground(it) }
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ProductDetailErrorHandler.getErrorMessage(it, throwable)
        }
            ?: getString(productdetailcommonR.string.merchant_product_detail_error_default)
    }

    private fun hideProgressDialog() {
        if (loadingProgressDialog != null && loadingProgressDialog?.isShowing == true) {
            loadingProgressDialog?.dismiss()
        }
    }

    private fun showProgressDialog(onCancelClicked: (() -> Unit)? = null) {
        activity?.let { parentView ->
            val dialog = createProgressDialog(parentView, onCancelClicked)
            val showProgressDialog = !parentView.isFinishing && !dialog.isShowing

            if (showProgressDialog) {
                runCatching {
                    dialog.show()
                }
            }
        }
    }

    private fun createProgressDialog(
        activity: Activity,
        onCancelClicked: (() -> Unit)?
    ): ProgressDialog {
        val dialog = loadingProgressDialog.ifNull {
            activity.createDefaultProgressDialog(
                getString(com.tokopedia.abstraction.R.string.title_loading),
                cancelable = onCancelClicked != null,
                onCancelClicked = {
                    onCancelClicked?.invoke()
                }
            )
        }

        loadingProgressDialog = dialog

        return dialog
    }

    private fun updateProductId() {
        viewModel.getProductInfoP1?.let { productInfo ->
            productId = productInfo.basic.productID
        }
    }

    private fun hasTopAds() =
        topAdsGetProductManage.data.adId.isNotEmpty() && topAdsGetProductManage.data.adId != "0"

    private fun assignDeviceId() {
        viewModel.deviceId = TradeInUtils.getDeviceId(context)
            ?: viewModel.userSessionInterface.deviceId ?: ""
    }

    private fun goToTradeInHome() {
        val selectedWarehouseId = viewModel.getMultiOriginByProductId().id

        viewModel.getProductInfoP1?.let {
            TradeInPDPHelper.pdpToTradeIn(
                context,
                shopID = viewModel.getShopInfo().shopCore.shopID,
                shopName = viewModel.getShopInfo().shopCore.name,
                shopBadge = viewModel.getShopInfo().shopTierBadgeUrl,
                shopLocation = viewModel.getShopInfo().location,
                productId = it.basic.productID,
                productName = it.data.name,
                productImage = it.data.getProductImageUrl(),
                productPrice = it.finalPrice,
                minOrder = viewModel.getProductInfoP1?.basic?.minOrder ?: 0,
                selectedWarehouseId = selectedWarehouseId,
                trackerAttributionPdp = trackerAttributionPdp ?: "",
                trackerListNamePdp = trackerListNamePdp ?: "",
                shippingMinimumPrice = viewModel.shippingMinimumPrice,
                getProductName = viewModel.getProductInfoP1?.getProductName ?: "",
                categoryName = viewModel.getProductInfoP1?.basic?.category?.name ?: ""
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.run {
            ImeiPermissionAsker.onImeiRequestPermissionsResult(
                this,
                requestCode,
                permissions,
                grantResults,
                onUserDenied = {},
                onUserDeniedAndDontAskAgain = {},
                onUserAcceptPermission = {}
            )
        }
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    private fun observeToggleNotifyMe() {
        viewLifecycleOwner.observe(viewModel.toggleTeaserNotifyMe) { data ->
            data.doSuccessOrFail({
                onSuccessToggleNotifyMe(it.data)
            }, {
                onFailNotifyMe(it)
                logException(it)
            })
        }
    }

    private fun onSuccessToggleNotifyMe(data: NotifyMeUiData) {
        viewModel.clearCacheP2Data()
        view?.showToasterSuccess(
            data.successMessage,
            ctaText = getString(productdetailcommonR.string.pdp_common_oke),
            ctaListener = {
                // noop
            }
        )
    }

    private fun onFailNotifyMe(t: Throwable) {
        val dataModel = pdpUiUpdater?.notifyMeMap
        view?.showToasterError(
            getErrorMessage(t),
            ctaText = getString(productdetailcommonR.string.pdp_common_oke)
        )
        if (dataModel != null) {
            pdpUiUpdater?.updateNotifyMeButton(dataModel.data.notifyMe)
            updateUi()
        }
    }

    override fun onNotifyMeClicked(
        data: UpcomingCampaignUiModel,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        doActionOrLogin({
            pdpUiUpdater?.notifyMeMap?.data?.notifyMe?.let { notifyMe ->
                trackToggleNotifyMe(componentTrackDataModel, notifyMe)
            }
            pdpUiUpdater?.updateNotifyMeButton(data.notifyMe)
            updateUi()
            viewModel.toggleTeaserNotifyMe(
                data.notifyMe,
                data.campaignID.toLongOrZero(),
                productId?.toLongOrZero() ?: 0
            )
        })
    }

    private fun trackToggleNotifyMe(
        componentTrackDataModel: ComponentTrackDataModel?,
        notifyMe: Boolean
    ) {
        viewModel.getProductInfoP1?.let {
            ProductDetailTracking.Click.eventNotifyMe(
                it,
                componentTrackDataModel,
                notifyMe,
                viewModel.userId
            )
        }
    }

    private fun trackOnTickerClicked(
        tickerTitle: String,
        tickerType: Int,
        componentTrackDataModel: ComponentTrackDataModel?,
        tickerDescription: String
    ) {
        ProductDetailTracking.Click.eventClickTicker(
            tickerTitle,
            tickerType,
            viewModel.getProductInfoP1,
            componentTrackDataModel,
            viewModel.userId,
            tickerDescription
        )
    }

    private fun doActionOrLogin(actionLogin: () -> Unit, actionNonLogin: (() -> Unit)? = null) {
        if (viewModel.isUserSessionActive) {
            actionLogin.invoke()
        } else {
            actionNonLogin?.invoke()
            goToLogin()
        }
    }

    private fun goToLogin() {
        showProgressDialog()
        activity?.let {
            startActivityForResult(
                RouteManager.getIntent(it, ApplinkConst.LOGIN),
                ProductDetailConstant.REQUEST_CODE_LOGIN
            )
        }
    }

    private fun goToReadingActivity() {
        viewModel.getProductInfoP1?.let {
            val intent = RouteManager.getIntent(
                context,
                Uri.parse(
                    UriUtil.buildUri(
                        ApplinkConstInternalGlobal.PRODUCT_TALK,
                        it.basic.productID
                    )
                )
                    .buildUpon()
                    .appendQueryParameter(PARAM_APPLINK_SHOP_ID, it.basic.shopID)
                    .appendQueryParameter(
                        PARAM_APPLINK_IS_VARIANT_SELECTED,
                        (pdpUiUpdater?.productSingleVariant != null).toString()
                    )
                    .appendQueryParameter(
                        PARAM_APPLINK_AVAILABLE_VARIANT,
                        viewModel.variantData?.getBuyableVariantCount().orZero().toString()
                    )
                    .build().toString()
            )
            startActivity(intent)
        }
    }

    private fun goToReplyActivity(questionID: String) {
        viewModel.getProductInfoP1?.let {
            val intent = RouteManager.getIntent(
                context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionID))
                    .buildUpon()
                    .appendQueryParameter(PARAM_APPLINK_SHOP_ID, it.basic.shopID)
                    .build().toString()
            )
            startActivity(intent)
        }
    }

    private fun goToWriteActivity() {
        viewModel.getProductInfoP1?.basic?.productID?.let {
            val intent = RouteManager.getIntent(
                context,
                Uri.parse(ApplinkConstInternalGlobal.ADD_TALK)
                    .buildUpon()
                    .appendQueryParameter(ProductDetailConstant.PARAM_PRODUCT_ID, it)
                    .appendQueryParameter(
                        PARAM_APPLINK_IS_VARIANT_SELECTED,
                        (pdpUiUpdater?.productSingleVariant != null).toString()
                    )
                    .appendQueryParameter(
                        PARAM_APPLINK_AVAILABLE_VARIANT,
                        viewModel.variantData?.getBuyableVariantCount()
                            .orZero()
                            .toString()
                    )
                    .build().toString()
            )
            startActivity(intent)
        }
    }

    private fun goToSellerMigrationPage(
        @SellerMigrationFeatureName featureName: String,
        appLinks: ArrayList<String>
    ) {
        context?.run {
            val intent = RouteManager.getIntent(
                this,
                String.format(
                    Locale.getDefault(),
                    "%s?${SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME}=%s",
                    ApplinkConst.SELLER_MIGRATION,
                    featureName
                )
            )
            intent.putStringArrayListExtra(
                SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA,
                appLinks
            )
            intent.putExtra(SellerMigrationApplinkConst.EXTRA_SCREEN_NAME, screenName)
            startActivity(intent)
        }
    }

    fun trackVerticalScroll() {
        if (hasApplogScrollListener) return
        getRecyclerView()?.addVerticalTrackListener()
        hasApplogScrollListener = true
    }

    // Will be delete soon
    override fun isNavOld(): Boolean = GlobalConfig.isSellerApp()

    override fun getFragmentTrackingQueue(): TrackingQueue? {
        return trackingQueue
    }

    private fun getVariantString(): String {
        return viewModel.variantData?.getVariantCombineIdentifier().orEmpty()
    }

    private fun navAbTestCondition(navMainApp: () -> Unit = {}, navSellerApp: () -> Unit = {}) {
        if (!GlobalConfig.isSellerApp()) {
            navMainApp.invoke()
        } else if (isNavOld()) {
            navSellerApp.invoke()
        }
    }

    private fun goToRecommendation() {
        val uri = UriUtil.buildUri(
            ProductDetailConstant.RECOM_URL,
            viewModel.getProductInfoP1?.basic?.productID
        )
        RouteManager.route(context, uri)
    }

    private fun addWishlistV2(
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        val productId = viewModel.getProductInfoP1?.basic?.productID ?: ""
        viewModel.addWishListV2(
            productId,
            object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {
                    try {
                        val errorMsg = com.tokopedia.network.utils.ErrorHandler.getErrorMessage(
                            context,
                            throwable
                        )
                        val extras = mapOf(WISHLIST_STATUS_KEY to ADD_WISHLIST).toString()
                        ProductDetailLogger.logMessage(
                            errorMsg,
                            WISHLIST_ERROR_TYPE,
                            productId,
                            viewModel.deviceId,
                            extras
                        )
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, v)
                        }
                    } catch (t: Throwable) {
                        Timber.d(t)
                    }
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    context?.let { context ->
                        val applinkCollection =
                            "$WISHLIST_COLLECTION_BOTTOMSHEET?$PATH_PRODUCT_ID=$productId&$PATH_SRC=$DEFAULT_X_SOURCE"
                        val intentBottomSheetWishlistCollection =
                            RouteManager.getIntent(context, applinkCollection)
                        val isOos = viewModel.getProductInfoP1?.getFinalStock()
                            ?.toIntOrNull() == 0
                        intentBottomSheetWishlistCollection.putExtra(
                            WishlistV2CommonConsts.IS_PRODUCT_ACTIVE,
                            !isOos
                        )
                        startActivityForResult(
                            intentBottomSheetWishlistCollection,
                            REQUEST_CODE_ADD_WISHLIST_COLLECTION
                        )
                    }
                    if (result.success) {
                        updateFabIcon(productId, true)
                        if (componentTrackDataModel != null) {
                            trackingEventSuccessAddToWishlist(componentTrackDataModel)
                        }
                    }
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {}
                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {
                }
            }
        )
    }

    private fun updateFabIcon(productId: String, isWishlisted: Boolean) {
        pdpUiUpdater?.updateWishlistData(isWishlisted)
        updateUi()
        if (isWishlisted) {
            ProductDetailTracking.Branch.eventBranchAddToWishlist(
                viewModel.getProductInfoP1,
                viewModel.userId,
                pdpUiUpdater?.productDetailInfoData?.getDescription()
                    ?: ""
            )
        }
        sendIntentResultWishlistChange(productId, isWishlisted)
        if (isProductOos()) {
            refreshPage()
        }
    }

    private fun removeWishlistV2(
        productId: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        viewModel.removeWishListV2(
            productId,
            object : WishlistV2ActionListener {
                override fun onErrorAddWishList(throwable: Throwable, productId: String) {}

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                }

                override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) {
                    try {
                        val errorMsg =
                            com.tokopedia.network.utils.ErrorHandler.getErrorMessage(
                                context,
                                throwable
                            )
                        val extras =
                            mapOf(WISHLIST_STATUS_KEY to REMOVE_WISHLIST).toString()
                        ProductDetailLogger.logMessage(
                            errorMsg,
                            WISHLIST_ERROR_TYPE,
                            productId,
                            viewModel.deviceId,
                            extras
                        )
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(errorMsg, v)
                        }
                    } catch (t: Throwable) {
                        Timber.d(t)
                    }
                }

                override fun onSuccessRemoveWishlist(
                    result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                    productId: String
                ) {
                    context?.let { context ->
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(
                                result,
                                context,
                                v
                            )
                        }
                    }
                    if (result.success) {
                        updateFabIcon(productId, false)
                        trackingEventSuccessRemoveFromWishlist(componentTrackDataModel)
                    }
                }
            }
        )
    }

    private fun isProductOos(): Boolean {
        val isOos = viewModel.getProductInfoP1?.getFinalStock()?.toIntOrNull() == 0
        val isInactive = viewModel.getProductInfoP1?.basic?.isWarehouse() ?: false && !isOos
        return isOos || isInactive
    }

    private fun writeDiscussion(tracker: () -> Unit) {
        doActionOrLogin({
            tracker.invoke()
            goToWriteActivity()
        })
        viewModel.updateLastAction(ProductDetailTalkGoToWriteDiscussion)
    }

    override fun refreshPage() {
        activity?.let {
            onSwipeRefresh()
        }
    }

    override fun onTopAdsImageViewClicked(
        model: TopAdsImageDataModel,
        applink: String?,
        bannerId: String,
        bannerName: String
    ) {
        applink?.let { goToApplink(it) }
        val position = getComponentPosition(model)
        ProductDetailTracking.Click.eventTopAdsImageViewClicked(
            trackingQueue,
            viewModel.userId,
            bannerId,
            position,
            bannerName
        )
    }

    override fun onTopAdsImageViewImpression(
        model: TopAdsImageDataModel,
        bannerId: String,
        bannerName: String
    ) {
        val position = getComponentPosition(model)
        ProductDetailTracking.Impression.eventTopAdsImageViewImpression(
            trackingQueue,
            viewModel.userId,
            bannerId,
            position,
            bannerName
        )
    }

    override fun onClickBestSeller(
        componentTrackDataModel: ComponentTrackDataModel,
        appLink: String
    ) {
        ProductDetailTracking.Click.eventClickBestSeller(
            componentTrackDataModel,
            viewModel.getProductInfoP1,
            "",
            viewModel.userId
        )
        goToApplink(appLink)
    }

    override fun onImpressionProductBundling(
        bundleId: String,
        bundleType: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        ProductDetailTracking.ProductBundling.eventImpressionProductBundling(
            viewModel.userId,
            bundleId,
            bundleType,
            viewModel.getProductInfoP1,
            componentTrackDataModel,
            trackingQueue
        )
    }

    override fun onClickActionButtonBundling(
        bundleId: String,
        bundleType: String,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val productInfoP1 = viewModel.getProductInfoP1
        ProductDetailTracking.ProductBundling.eventClickCheckBundlePage(
            bundleId,
            bundleType,
            productInfoP1,
            componentTrackDataModel
        )
    }

    override fun onClickProductInBundling(
        bundleId: String,
        bundleProductId: String,
        componentTrackDataModel: ComponentTrackDataModel,
        isOldBundlingWidget: Boolean
    ) {
        ProductDetailTracking.ProductBundling.eventClickMultiBundleProduct(
            bundleId,
            bundleProductId,
            viewModel.getProductInfoP1,
            componentTrackDataModel
        )
        if (isOldBundlingWidget) {
            val intent = ProductDetailActivity.createIntent(requireContext(), bundleProductId)
            startActivity(intent)
        }
    }

    override fun screenShotTaken(path: String) {
        shareProduct(path = path)
    }

    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        loadPlayWidget()
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        goToApplink(appLink)
    }

    override fun onWidgetError(view: PlayWidgetView, error: PlayWidgetException) {
        pdpUiUpdater?.removeComponent(PLAY_CAROUSEL)
        updateUi()
    }

    override fun onToggleReminderClicked(
        view: PlayWidgetMediumView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        doActionOrLogin({
            val playWidgetState =
                pdpUiUpdater?.contentWidgetData?.playWidgetState ?: return@doActionOrLogin
            viewModel.updatePlayWidgetToggleReminder(
                playWidgetState,
                channelId,
                reminderType
            )
        })
    }

    override fun onImpressChannelCard(
        componentTrackDataModel: ComponentTrackDataModel,
        item: PlayWidgetChannelUiModel
    ) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ContentWidgetTracking.impressChannelCard(
            trackingQueue,
            ContentWidgetTracker(
                viewModel.userId,
                productInfo,
                componentTrackDataModel,
                item
            )
        )
    }

    override fun onClickChannelCard(
        componentTrackDataModel: ComponentTrackDataModel,
        item: PlayWidgetChannelUiModel
    ) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ContentWidgetTracking.clickChannelCard(
            ContentWidgetTracker(
                viewModel.userId,
                productInfo,
                componentTrackDataModel,
                item
            )
        )
    }

    override fun onClickBannerCard(componentTrackDataModel: ComponentTrackDataModel) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ContentWidgetTracking.clickBannerCard(
            ContentWidgetTracker(
                viewModel.userId,
                productInfo,
                componentTrackDataModel
            )
        )
    }

    override fun onClickViewAll(componentTrackDataModel: ComponentTrackDataModel) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ContentWidgetTracking.clickViewAll(
            ContentWidgetTracker(
                viewModel.userId,
                productInfo,
                componentTrackDataModel
            )
        )
    }

    override fun onClickToggleReminderChannel(
        componentTrackDataModel: ComponentTrackDataModel,
        item: PlayWidgetChannelUiModel,
        isRemindMe: Boolean
    ) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ContentWidgetTracking.clickToggleReminderChannel(
            ContentWidgetTracker(
                viewModel.userId,
                productInfo,
                componentTrackDataModel,
                isRemindMe = isRemindMe
            )
        )
    }

    override fun onImpressProductDetailNavigation(labels: List<String>) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val userId = viewModel.userId
        labels.forEachIndexed { index, label ->
            ProductDetailNavigationTracking.impressNavigation(
                productInfo,
                userId,
                ProductDetailNavigationTracker(index + 1, label),
                trackingQueue
            )
        }
    }

    override fun onClickProductDetailnavigation(position: Int, label: String) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val userId = viewModel.userId
        ProductDetailNavigationTracking.clickNavigation(
            productInfo,
            userId,
            ProductDetailNavigationTracker(position, label)
        )
    }

    override fun onImpressBackToTop(label: String) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val userId = viewModel.userId
        ProductDetailNavigationTracking.impressNavigation(
            productInfo,
            userId,
            ProductDetailNavigationTracker(0, label),
            trackingQueue
        )
    }

    override fun updateNavigationTabPosition() {
        binding?.pdpNavigation?.updateItemPosition()
    }

    override fun getRemoteConfigInstance(): RemoteConfig? {
        return remoteConfig
    }

    override fun getProductInfo(): ProductInfoP1? {
        return viewModel.getProductInfoP1
    }

    override fun getTrackingQueueInstance(): TrackingQueue {
        return trackingQueue
    }

    override fun getStoriesWidgetManager(): StoriesWidgetManager {
        return mStoriesWidgetManager
    }

    override fun getUserSession(): UserSessionInterface {
        return viewModel.userSessionInterface
    }

    override fun onImpressStockAssurance(
        componentTrackDataModel: ComponentTrackDataModel,
        label: String
    ) {
        OneLinersTracking.onImpression(
            trackingQueue = trackingQueue,
            componentTrackDataModel = componentTrackDataModel,
            productInfo = viewModel.getProductInfoP1,
            userId = viewModel.userId,
            lcaWarehouseId = getLcaWarehouseId(),
            label = label
        )
    }

    override fun onClickInformationIconAtStockAssurance(
        componentTrackDataModel: ComponentTrackDataModel,
        appLink: String,
        label: String
    ) {
        goToEducational(url = appLink)

        OneLinersTracking.clickInformationButton(
            component = componentTrackDataModel,
            productInfo = viewModel.getProductInfoP1,
            eventLabel = label
        )
    }

    override fun onImpressPageNotFound() {
        PageErrorTracking.impressPageNotFound(
            generatePageErrorTrackerData()
        )
    }

    private fun generatePageErrorTrackerData() = PageErrorTracker(
        productId,
        isFromDeeplink,
        deeplinkUrl,
        shopDomain.orEmpty(),
        productKey.orEmpty()
    )

    override fun onViewToViewImpressed(
        data: ViewToViewItemData,
        title: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        ViewToViewTracker.eventImpressViewToView(
            position = itemPosition,
            product = data.recommendationData,
            androidPageName = ViewToViewTracker.PAGE_PDP,
            pageTitle = title,
            viewModel.getProductInfoP1?.basic?.productID.orEmpty(),
            viewModel.userId,
            trackingQueue
        )
    }

    override fun onViewToViewClicked(
        data: ViewToViewItemData,
        title: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        ViewToViewTracker.eventClickViewToView(
            position = itemPosition,
            product = data.recommendationData,
            androidPageName = ViewToViewTracker.PAGE_PDP,
            pageTitle = title,
            viewModel.getProductInfoP1?.basic?.productID.orEmpty(),
            viewModel.userId
        )
        val activity = activity ?: return
        showImmediately(
            childFragmentManager,
            ViewToViewBottomSheet.TAG
        ) {
            ViewToViewBottomSheet.newInstance(
                activity.classLoader,
                childFragmentManager.fragmentFactory,
                data,
                viewModel.getProductInfoP1?.basic?.productID ?: ""
            )
        }
    }

    override fun onImpressScheduledDelivery(
        labels: List<String>,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        val common = CommonTracker(productInfo, viewModel.userId)

        ShipmentTracking.sendImpressionScheduledDeliveryComponent(
            trackingQueue,
            labels,
            common,
            componentTrackDataModel
        )
    }

    override fun onClickShipmentPlusBanner(
        link: String,
        trackerData: ShipmentPlusData.TrackerData,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        ShipmentTracking.sendClickShipmentPlusBanner(
            trackerData = trackerData,
            common = generateCommonTracker(),
            component = componentTrackDataModel
        )
        val processedLink = if (link.startsWith(ProductDetailConstant.HTTP_PREFIX)) {
            UriUtil
                .buildUriAppendParam(
                    ApplinkConst.WEBVIEW,
                    mapOf(ProductDetailConstant.WEBVIEW_URL_PARAM to link)
                )
        } else {
            link
        }
        goToApplink(processedLink)
    }

    override fun onClickShipmentPlusBanner(
        link: String,
        trackerData: ShipmentUiModel.ShipmentPlusData.TrackerData,
        componentTrackDataModel: ComponentTrackDataModel?
    ) {
        ShipmentTracking.sendClickShipmentPlusBanner(
            trackerData = trackerData,
            common = generateCommonTracker(),
            component = componentTrackDataModel
        )
        val processedLink = if (link.startsWith(ProductDetailConstant.HTTP_PREFIX)) {
            UriUtil
                .buildUriAppendParam(
                    ApplinkConst.WEBVIEW,
                    mapOf(ProductDetailConstant.WEBVIEW_URL_PARAM to link)
                )
        } else {
            link
        }
        goToApplink(processedLink)
    }

    override fun onSocialProofItemImpression(socialProof: SocialProofUiModel) {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ProductSocialProofTracking.onImpression(uiModel = socialProof, productInfo = productInfo)
    }

    override fun onSocialProofItemClickTracking(
        identifier: SocialProofUiModel.Identifier,
        trackData: ComponentTrackDataModel?
    ) {
        when (identifier) {
            SocialProofUiModel.Identifier.Talk -> socialProofTalkTracking(trackData = trackData)
            SocialProofUiModel.Identifier.Media -> socialProofMediaTracking(trackData = trackData)
            SocialProofUiModel.Identifier.NewProduct -> socialProofNewProductTracking(trackData = trackData)
            SocialProofUiModel.Identifier.Rating -> socialProofRatingTracking()
            SocialProofUiModel.Identifier.ShopRating -> socialProofShopRatingTracking(trackData = trackData)
            else -> {
                // no-ops
            }
        }
    }

    private fun socialProofTalkTracking(trackData: ComponentTrackDataModel?) {
        val productInfo = viewModel.getProductInfoP1 ?: return

        ProductSocialProofTracking.onTalkClicked(
            deepLink = deeplinkUrl,
            productInfo = productInfo,
            trackDataModel = trackData
        )
    }

    private fun socialProofMediaTracking(trackData: ComponentTrackDataModel?) {
        val productInfo = viewModel.getProductInfoP1 ?: return

        ProductSocialProofTracking.onMediaClicked(
            productInfo = productInfo,
            userId = viewModel.userId,
            trackDataModel = trackData ?: ComponentTrackDataModel()
        )
    }

    private fun socialProofNewProductTracking(trackData: ComponentTrackDataModel?) {
        val productInfo = viewModel.getProductInfoP1 ?: return

        ProductSocialProofTracking.onNewProductClicked(
            productInfo = productInfo,
            trackDataModel = trackData
        )
    }

    private fun socialProofRatingTracking() {
        val productInfo = viewModel.getProductInfoP1 ?: return
        ProductSocialProofTracking.onRatingClicked(
            deepLink = deeplinkUrl,
            productInfo = productInfo
        )
    }

    private fun socialProofShopRatingTracking(trackData: ComponentTrackDataModel?) {
        val productInfo = viewModel.getProductInfoP1 ?: return

        ProductSocialProofTracking.onShopRatingClicked(
            productInfo = productInfo,
            trackDataModel = trackData
        )
    }

    override fun onShopReviewSeeMore(
        appLink: String,
        eventLabel: String,
        trackData: ComponentTrackDataModel?
    ) {
        val productInfo = viewModel.getProductInfoP1 ?: return

        goToApplink(url = appLink)
        ProductShopReviewTracking.onItemClicked(
            productInfo = productInfo,
            trackDataModel = trackData,
            eventLabel = eventLabel
        )
    }

    override fun onClickDynamicOneLiner(
        title: String,
        url: String,
        component: ComponentTrackDataModel
    ) {
        val commonTracker = generateCommonTracker() ?: return
        if (component.componentName == ProductDetailConstant.PRODUCT_DYNAMIC_ONELINER_PROMO) {
            onClickDynamicOneLinerPromo()
        } else {
            goToApplink(url)
        }
        DynamicOneLinerTracking.onClickDynamicOneliner(
            title,
            commonTracker,
            component
        )
    }

    override fun onBMGMClicked(title: String, offerId: String, component: ComponentTrackDataModel) {
        val commonTracker = generateCommonTracker() ?: return

        BMGMTracking.onClicked(
            title = title,
            offerId = offerId,
            commonTracker = commonTracker,
            component = component,
            trackingQueue = trackingQueue
        )
    }

    private fun initializeShareEx() {
        if (isUsingShareEx()) {
            context?.let {
                shareExInitializer = ShareExInitializer(it)
            }
        }
    }

    /**
     * This function only for transition period
     * from old share to share 2.0
     */
    private fun isUsingShareEx(): Boolean {
        val rollenceKey = if (!GlobalConfig.isSellerApp()) {
            ROLLENCE_SHARE_EX
        } else {
            ROLLENCE_SHARE_EX_SA
        }
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            rollenceKey,
            ""
        ) == rollenceKey
    }

    private fun processAffiliateSubIds(bundle: Bundle?) {
        if (bundle == null || bundle.isEmpty) return

        affiliateSubIds = mutableMapOf()
        bundle.keySet().forEach {
            affiliateSubIds!![it] = bundle.getString(it, "")
        }
    }

    override fun getCartRedirectionButtonsByteIOTrackerViewModel() = viewModel

    override fun getCartRedirectionButtonsByteIOTrackerActionType() = buttonActionType
}
