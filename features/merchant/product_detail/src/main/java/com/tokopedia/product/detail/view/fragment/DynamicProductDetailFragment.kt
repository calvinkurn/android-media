package com.tokopedia.product.detail.view.fragment

import android.animation.Animator
import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradeInResponse
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.dialog.IAccessRequestListener
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.device.info.permission.ImeiPermissionAsker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.BuildConfig
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.TradeinResponse
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductNotifyMeDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPdpActivity
import com.tokopedia.product.detail.view.activity.*
import com.tokopedia.product.detail.view.adapter.dynamicadapter.DynamicProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.DynamicProductDetailHashMap
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.util.ProductDetailErrorHelper
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.widget.*
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.sharedata.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.referral.Constants
import com.tokopedia.referral.ReferralAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.topads.detail_sheet.TopAdsDetailSheet
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.variant_common.model.VariantOptionWithAttribute
import com.tokopedia.variant_common.util.VariantCommonMapper
import com.tokopedia.variant_common.view.ProductVariantListener
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DynamicProductDetailFragment : BaseListFragment<DynamicPdpDataModel, DynamicProductDetailAdapterFactoryImpl>(), DynamicProductDetailListener, ProductVariantListener, IAccessRequestListener {

    companion object {
        fun newInstance(productId: String? = null,
                        warehouseId: String? = null,
                        shopDomain: String? = null,
                        productKey: String? = null,
                        isFromDeeplink: Boolean = false,
                        isAffiliate: Boolean = false,
                        trackerAttribution: String? = null,
                        trackerListName: String? = null,
                        affiliateString: String? = null,
                        deeplinkUrl: String? = null) =
                DynamicProductDetailFragment().also {
                    it.arguments = Bundle().apply {
                        productId?.let { pid -> putString(ProductDetailConstant.ARG_PRODUCT_ID, pid) }
                        warehouseId?.let { whId -> putString(ProductDetailConstant.ARG_WAREHOUSE_ID, whId) }
                        productKey?.let { pkey -> putString(ProductDetailConstant.ARG_PRODUCT_KEY, pkey) }
                        shopDomain?.let { domain -> putString(ProductDetailConstant.ARG_SHOP_DOMAIN, domain) }
                        trackerAttribution?.let { attribution -> putString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION, attribution) }
                        trackerListName?.let { listName -> putString(ProductDetailConstant.ARG_TRACKER_LIST_NAME, listName) }
                        affiliateString?.let { affiliateString -> putString(ProductDetailConstant.ARG_AFFILIATE_STRING, affiliateString) }
                        deeplinkUrl?.let { deeplinkUrl -> putString(ProductDetailConstant.ARG_DEEPLINK_URL, deeplinkUrl) }
                        putBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, isFromDeeplink)
                        putBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, isAffiliate)
                    }
                }
    }

    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    @Inject
    lateinit var trackingQueue: TrackingQueue
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DynamicProductDetailViewModel::class.java)
    }

    //Listener function
    private lateinit var initToolBarMethod: () -> Unit
    //Data
    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null
    private var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()
    private lateinit var remoteConfig: RemoteConfig

    // This productId is only use for backend hit
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var shouldShowCodP1 = false
    private var shouldShowCodP3 = false
    private var isAffiliate = false
    private var affiliateString: String? = null
    private var deeplinkUrl: String = ""
    private var isFromDeeplink: Boolean = false
    private var userInputNotes = ""
    private var userInputQuantity = 0
    private var delegateTradeInTracking = false
    private var trackerAttributionPdp: String? = ""
    private var trackerListNamePdp: String? = ""
    private var warehouseId: String? = null
    private var isTopdasLoaded: Boolean = false
    private var shouldRenderSticky = true
    private var doActivityResult = true
    private var shouldFireVariantTracker = true
    private var pdpHashMapUtil: DynamicProductDetailHashMap? = null

    //View
    private lateinit var bottomSheet: ValuePropositionBottomSheet
    private lateinit var varToolbar: Toolbar
    private lateinit var actionButtonView: PartialButtonActionView
    private lateinit var stickyLoginView: StickyLoginView
    private lateinit var topAdsDetailSheet: TopAdsDetailSheet
    private var shouldShowCartAnimation = false
    private var loadingProgressDialog: ProgressDialog? = null
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(this, this) }
    private val dynamicAdapter by lazy { DynamicProductDetailAdapter(adapterFactory, this) }
    private var menu: Menu? = null
    private var tradeinDialog: ProductAccessRequestDialogFragment? = null
    private val recommendationCarouselPositionSavedState = SparseIntArray()

    private val irisSessionId by lazy {
        context?.let { IrisSession(it).getSessionId() } ?: ""
    }

    //Performance Monitoring
    lateinit var performanceMonitoringP1: PerformanceMonitoring
    lateinit var performanceMonitoringP2: PerformanceMonitoring
    lateinit var performanceMonitoringP2General: PerformanceMonitoring
    lateinit var performanceMonitoringP2Login: PerformanceMonitoring
    lateinit var performanceMonitoringFull: PerformanceMonitoring

    private var enableCheckImeiRemoteConfig = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dynamic_product_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::remoteConfig.isInitialized) {
            viewModel.enableCaching = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_MAIN_APP_ENABLED_CACHE_PDP, true)
            enableCheckImeiRemoteConfig = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CHECK_IMEI_PDP, false)
        }

        initTradein()
        initPerformanceMonitoring()
        initRecyclerView(view)
        initBtnAction()
        initToolbar()
        initStickyLogin(view)

        if (isAffiliate) {
            actionButtonView.gone()
            base_btn_affiliate_dynamic.visible()
            loadingAffiliateDynamic.visible()
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onSwipeRefresh() {
        pdpHashMapUtil?.productNewVariantDataModel?.mapOfSelectedVariant = mutableMapOf()
        recommendationCarouselPositionSavedState.clear()
        isLoadingInitialData = true
        isTopdasLoaded = false
        actionButtonView.visibility = false
        updateStickyContent()
        loadProductData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            doActivityResult = savedInstanceState.getBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, true)
            userInputNotes = savedInstanceState.getString(ProductDetailConstant.SAVED_NOTE, "")
            userInputQuantity = savedInstanceState.getInt(ProductDetailConstant.SAVED_QUANTITY, 1)
        }
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ProductDetailConstant.ARG_PRODUCT_ID)
            warehouseId = it.getString(ProductDetailConstant.ARG_WAREHOUSE_ID)
            productKey = it.getString(ProductDetailConstant.ARG_PRODUCT_KEY)
            shopDomain = it.getString(ProductDetailConstant.ARG_SHOP_DOMAIN)
            trackerAttributionPdp = it.getString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION)
            trackerListNamePdp = it.getString(ProductDetailConstant.ARG_TRACKER_LIST_NAME)
            affiliateString = it.getString(ProductDetailConstant.ARG_AFFILIATE_STRING)
            isAffiliate = it.getBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, false)
            deeplinkUrl = it.getString(ProductDetailConstant.ARG_DEEPLINK_URL, "")
            isFromDeeplink = it.getBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, false)
        }
        activity?.run {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }
        context?.let {
            pdpHashMapUtil = DynamicProductDetailHashMap(it, mapOf(ProductDetailConstant.PRODUCT_SNAPSHOT to ProductSnapshotDataModel()))
        }
        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // If the activity being destroyed and onActivityResult start afterward
        // Then just ignore onActivityResult with this variable
        outState.putBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, false)
        outState.putString(ProductDetailConstant.SAVED_NOTE, userInputNotes)
        outState.putInt(ProductDetailConstant.SAVED_QUANTITY, userInputQuantity)
    }

    override fun onPause() {
        super.onPause()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
    }

    override fun onResume() {
        super.onResume()
        reloadCartCounter()
    }

    private fun reloadCartCounter() {
        activity?.run {
            if (isAdded) {
                menu?.let {
                    if (it.size() > 2) {
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                        setBadgeMenuCart(menuCart)
                    }
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // handling menu toolbar / cart counter / settings / etc

        activity?.let {
            handlingMenuPreparation(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed(); true
            }
            R.id.action_share -> {
                shareProduct(); true
            }
            R.id.action_cart -> {
                gotoCart(); true
            }
            R.id.action_report -> {
                reportProduct(); true
            }
            R.id.action_warehouse -> {
                warehouseProduct(); true
            }
            R.id.action_etalase -> {
                moveProductToEtalase(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<DynamicPdpDataModel, DynamicProductDetailAdapterFactoryImpl> {
        return dynamicAdapter
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_pdp
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipeRefreshPdp
    }

    override fun getAdapterTypeFactory(): DynamicProductDetailAdapterFactoryImpl = adapterFactory

    override fun onItemClicked(t: DynamicPdpDataModel) {
        //No OP
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerProductDetailComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build().inject(this)
        }
    }

    override fun loadData(page: Int) {
        loadProductData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_detail_dark, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        initToolBarMethod.invoke()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            AdultManager.handleActivityResult(activity!!, requestCode, resultCode, data)
        }
        when (requestCode) {
            ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE,
            ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST -> {
                data?.let {
                    val deviceId = data.getStringExtra(TradeInParams.PARAM_DEVICE_ID)
                    val phoneType = data.getStringExtra(TradeInParams.PARAM_PHONE_TYPE)
                            ?: "none/other"
                    val phonePrice = data.getStringExtra(TradeInParams.PARAM_PHONE_PRICE)
                    buyAfterTradeinDiagnose(deviceId, phoneType, phonePrice)
                }
            }
            ProductDetailConstant.REQUEST_CODE_ETALASE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                    val selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                    val dynamicProductInfoData = viewModel.getDynamicProductInfoP1
                            ?: DynamicProductInfoP1()
                    if (dynamicProductInfoData.basic.productID.isNotEmpty() && !selectedEtalaseName.isNullOrEmpty()) {
                        showProgressDialog(onCancelClicked = { viewModel.cancelEtalaseUseCase() })
                        viewModel.moveProductToEtalase(dynamicProductInfoData.basic.productID, selectedEtalaseId, selectedEtalaseName)
                    }
                }
            }
            ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK && doActivityResult) {
                    onSwipeRefresh()
                }
            }
            ProductDetailConstant.REQUEST_CODE_LOGIN -> {
                if (resultCode == Activity.RESULT_OK && doActivityResult) {
                    onSwipeRefresh()
                }
                shouldRenderSticky = true
                updateStickyContent()
            }
            ProductDetailConstant.REQUEST_CODE_REPORT -> {
                if (resultCode == Activity.RESULT_OK)
                    showToastSuccess(getString(R.string.success_to_report))
            }
            ProductDetailConstant.REQUEST_CODE_IMAGE_PREVIEW -> {
                if (data != null) {
                    val isWishlisted = data.getBooleanExtra(ImagePreviewPdpActivity.RESPONSE_CODE_IMAGE_RPEVIEW, false)
                    pdpHashMapUtil?.snapShotMap?.isWishlisted = isWishlisted
                    dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
                }
            }
            ProductDetailConstant.REQUEST_CODE_SHOP_INFO -> {
                if (data != null) {
                    val isFavorite = data.getBooleanExtra(ProductDetailConstant.SHOP_STATUS_FAVOURITE, false)
                    val isUserLogin = data.getBooleanExtra(ProductDetailConstant.SHOP_STICKY_LOGIN, false)
                    val favorite = pdpHashMapUtil?.getShopInfo?.shopInfo?.favoriteData?.alreadyFavorited == 1

                    shouldRenderSticky = true
                    if (isUserLogin) updateStickyContent()

                    if (isFavorite != favorite) {
                        onSuccessFavoriteShop(true)
                    }
                }
            }
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        viewModel.productLayout.removeObservers(this)
        viewModel.p2ShopDataResp.removeObservers(this)
        viewModel.p2General.removeObservers(this)
        viewModel.p2Login.removeObservers(this)
        viewModel.productInfoP3resp.removeObservers(this)
        viewModel.loadTopAdsProduct.removeObservers(this)
        viewModel.moveToWarehouseResult.removeObservers(this)
        viewModel.moveToEtalaseResult.removeObservers(this)
        viewModel.updatedImageVariant.removeObservers(this)
        viewModel.initialVariantData.removeObservers(this)
        viewModel.onVariantClickedData.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun gotoVideoPlayer(videos: List<Video>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, videos.map { it.url }, index))
            } else {
                // Handle if user didn't have any apps to open Youtube * Usually rooted phone
                try {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse(ProductDetailConstant.URL_YOUTUBE + videos[index].url)))
                } catch (e: Throwable) {
                }
            }
        }
    }

    override fun getApplicationContext(): Application? {
        return activity?.application
    }

    override fun getLifecycleFragment(): Lifecycle {
        return lifecycle
    }

    private val onViewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_topchat -> {
                DynamicProductDetailTracking.Click.eventButtonChatClicked(viewModel.getDynamicProductInfoP1)
                onShopChatClicked()
            }
            R.id.btn_apply_leasing -> doAtc(ProductDetailConstant.LEASING_BUTTON)
            else -> {
            }
        }
    }

    override fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>, componentTrackDataModel: ComponentTrackDataModel) {
        context?.let {
            startActivity(ProductFullDescriptionTabActivity.createIntent(it,
                    data, listOfCatalog))
            activity?.overridePendingTransition(R.anim.pull_up, 0)
            DynamicProductDetailTracking.Click.eventClickProductDescriptionReadMore(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        }
    }

    override fun onShipmentSocialProofClicked(componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventSocialProfShippingClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        onShipmentClicked()
    }

    /**
     * ImpressionComponent
     */
    override fun onImpressComponent(componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Impression.eventEcommerceDynamicComponent(trackingQueue, componentTrackDataModel, viewModel.getDynamicProductInfoP1)
    }

    /**
     * ProductShopInfoViewHolder
     */
    override fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel) {
        when (itemId) {
            R.id.btn_favorite -> onShopFavoriteClick()
            R.id.send_msg_shop -> {
                DynamicProductDetailTracking.Click.eventButtonChatShopClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                onShopChatClicked()
            }
            R.id.shop_ava, R.id.shop_name -> gotoShopDetail(componentTrackDataModel)
            else -> {

            }
        }
    }

    /**
     * ProductInfoViewHolder
     */
    override fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (!GlobalConfig.isSellerApp()) {

            viewModel.getDynamicProductInfoP1?.basic?.category?.detail?.let {
                val categoryId = it.lastOrNull()?.id ?: ""
                val categoryName = it.lastOrNull()?.name ?: ""
                DynamicProductDetailTracking.Click.eventCategoryClicked(categoryId, categoryName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
            RouteManager.route(context, url)
        }
    }

    override fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        val etalaseId = viewModel.getDynamicProductInfoP1?.basic?.menu?.id ?: ""
        val etalaseName = viewModel.getDynamicProductInfoP1?.basic?.menu?.name ?: ""
        val shopId = viewModel.shopInfo?.shopCore?.shopID ?: ""

        val intent = RouteManager.getIntent(context, if (etalaseId.isNotEmpty()) {
            UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, shopId, etalaseId)
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopId)
        })
        DynamicProductDetailTracking.Click.eventEtalaseClicked(etalaseId, etalaseName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)

        startActivity(intent)
    }

    override fun goToApplink(url: String) {
        RouteManager.route(context, url)
    }

    /**
     * ProductGeneralInfoViewHolder Listener
     */
    override fun onInfoClicked(name: String, componentTrackDataModel: ComponentTrackDataModel) {
        when (name) {
            ProductDetailConstant.PRODUCT_SHIPPING_INFO -> {
                val productP3Resp = viewModel.productInfoP3resp.value ?: ProductInfoP3()

                DynamicProductDetailTracking.Click.eventShippingRateEstimationClicked(
                        productP3Resp.addressModel?.postalCode ?: "",
                        productP3Resp.addressModel?.districtName ?: "",
                        viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                onShipmentClicked()
            }
            ProductDetailConstant.TRADE_IN -> {
                DynamicProductDetailTracking.Click.trackTradein(viewModel.tradeInParams.usedPrice, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                doAtc(ProductDetailConstant.TRADEIN_BUTTON)
            }
            ProductDetailConstant.PRODUCT_INSTALLMENT_INFO -> {
                DynamicProductDetailTracking.Click.eventClickPDPInstallmentSeeMore(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                openFtInstallmentBottomSheet(viewModel.installmentData ?: FinancingDataResponse())
            }
            ProductDetailConstant.PRODUCT_VARIANT_INFO -> {
                DynamicProductDetailTracking.Click.eventClickVariant(generateVariantString(), viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
            ProductDetailConstant.PRODUCT_WHOLESALE_INFO -> {
                val data = DynamicProductDetailMapper.mapToWholesale(viewModel.getDynamicProductInfoP1?.data?.wholesale)
                if (data != null && data.isNotEmpty()) {
                    context?.run {
                        startActivity(WholesaleActivity.getIntent(this, ArrayList(data)))
                    }
                }
            }
        }
    }

    /**
     * ProductRecommendationViewHolder Listener
     */
    override fun onSeeAllRecomClicked(pageName: String, applink: String, componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickSeeMoreRecomWidget(pageName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        RouteManager.route(context, applink)
    }

    override fun eventRecommendationClick(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventRecommendationClick(
                recomItem, position, viewModel.isUserSessionActive, pageName, title, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
    }

    override fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (::trackingQueue.isInitialized) {
            DynamicProductDetailTracking.Impression.eventRecommendationImpression(
                    trackingQueue, position, recomItem, viewModel.isUserSessionActive, pageName, title,
                    viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        }
    }

    override fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool? {
        return getRecyclerView(view).recycledViewPool
    }

    override fun getRecommendationCarouselSavedState(): SparseIntArray {
        return recommendationCarouselPositionSavedState
    }

    override fun loadTopads() {
        if (pdpHashMapUtil?.listProductRecomMap?.isNotEmpty() == true && !isTopdasLoaded) {
            isTopdasLoaded = true
            viewModel.loadRecommendation()
        }
    }

    /**
     * PageErrorViewHolder
     */
    override fun onRetryClicked(forceRefresh: Boolean) {
        onSwipeRefresh()
    }

    override fun goToHomePageClicked() {
        (activity as? ProductDetailActivity)?.goToHomePageClicked()
    }

    override fun goToWebView(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    /**
     * ProductOpenShopViewHolder Listener
     */
    override fun openShopClicked() {
        activity?.let {
            if (viewModel.isUserSessionActive) {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.OPEN_SHOP)
                        ?: return@let
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
    }

    /**
     * ProductReviewViewHolder
     */
    override fun onSeeAllReviewClick(componentTrackDataModel: ComponentTrackDataModel?) {
        context?.let {
            val productId = viewModel.getDynamicProductInfoP1?.basic?.getProductId() ?: 0
            DynamicProductDetailTracking.Click.eventClickReviewOnSeeAllImage(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                    ?: ComponentTrackDataModel())
            ImageReviewGalleryActivity.moveTo(activity, productId)
        }
    }

    override fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int, componentTrackDataModel: ComponentTrackDataModel?) {
        context?.let {
            DynamicProductDetailTracking.Click.eventClickReviewOnBuyersImage(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                    ?: ComponentTrackDataModel(), listOfImage[position].reviewId)
            val listOfImageReview: List<String> = listOfImage.map {
                it.imageUrlLarge ?: ""
            }

            ImageReviewGalleryActivity.moveTo(context, ArrayList(listOfImageReview), position)
        }
    }

    override fun onReviewClick() {
        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Iris.eventReviewClickedIris(this, deeplinkUrl, viewModel.shopInfo?.shopCore?.name
                    ?: "")
            DynamicProductDetailTracking.Moengage.sendMoEngageClickReview(this, viewModel.shopInfo?.shopCore?.name
                    ?: "")
            context?.let {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_REVIEW, basic.productID)
                intent?.run {
                    intent.putExtra("x_prd_nm", getProductName)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventClickReviewOnMostHelpfulReview(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                ?: ComponentTrackDataModel(),
                reviewId)
        context?.let { ImageReviewGalleryActivity.moveTo(it, ArrayList(listOfImages), position) }
    }

    /**
     * ProductMerchantVoucherViewHolder
     */
    override fun isOwner(): Boolean {
        return viewModel.getDynamicProductInfoP1?.basic?.getShopId()?.let {
            viewModel.isShopOwner(it)
        } ?: false
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int, dataTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            //TOGGLE_MVC_OFF
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.getShopId().orZero().toString()
            DynamicProductDetailTracking.Click.eventClickMerchantVoucherUse(merchantVoucherViewModel, shopId, position, viewModel.getDynamicProductInfoP1, dataTrackDataModel)
            showSnackbarClose(getString(R.string.title_voucher_code_copied))
        }
    }

    override fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                val voucherId = merchantVoucherViewModel.voucherId
                DynamicProductDetailTracking.Click.eventClickMerchantVoucherSeeDetail(voucherId, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                val intent = MerchantVoucherDetailActivity.createIntent(it, voucherId,
                        merchantVoucherViewModel, basic.shopID)
                startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
            }
        }
    }

    override fun onSeeAllMerchantVoucherClick(componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                DynamicProductDetailTracking.Click.eventClickMerchantVoucherSeeAll(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                val intent = MerchantVoucherListActivity.createIntent(it, basic.shopID,
                        viewModel.shopInfo?.shopCore?.name)
                startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER)
            }
        }
    }

    /**
     * ProductSnapshotViewHolder
     */
    override fun onSwipePicture(swipeDirection: String, position: Int, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventProductImageOnSwipe(viewModel.getDynamicProductInfoP1, swipeDirection, position, componentTrackDataModel
                ?: ComponentTrackDataModel())
    }

    override fun onImageClickedTrack(componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventProductImageClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                ?: ComponentTrackDataModel())
    }

    override fun onImageClicked(position: Int) {
        val isWishlisted = pdpHashMapUtil?.snapShotMap?.isWishlisted ?: false
        val dynamicProductInfoData = viewModel.getDynamicProductInfoP1 ?: DynamicProductInfoP1()
        activity?.let {
            val intent = ImagePreviewPdpActivity.createIntent(it, dynamicProductInfoData.basic.productID, isWishlisted,
                    dynamicProductInfoData.data.getImagePath(), null, position)
            startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_IMAGE_PREVIEW)
        }
    }

    override fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickTradeInRibbon(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        scrollToPosition(dynamicAdapter.getTradeinPosition(pdpHashMapUtil?.productTradeinMap))
    }

    override fun clickAccept() {
        val tradeinResponse = viewModel.p2ShopDataResp.value?.tradeinResponse ?: TradeinResponse()
        if (tradeinResponse.validateTradeInPDP.usedPrice > 0) {
            goToHargaFinal()
        } else {
            goToTradeInHome()
        }
    }

    override fun clickDeny() {
    }

    override fun getProductFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun showAlertCampaignEnded() {
        dynamicAdapter.notifySnapshot(pdpHashMapUtil?.snapShotMap)
        activity?.let {
            Dialog(it, Dialog.Type.LONG_PROMINANCE).apply {
                setTitle(getString(R.string.campaign_expired_title))
                setDesc(getString(R.string.campaign_expired_descr))
                setBtnCancel(getString(R.string.close))
                setOnCancelClickListener {
                    onSwipeRefresh()
                    dismiss()
                }
            }.show()
        }
    }

    override fun onFabWishlistClicked(isActive: Boolean, componentTrackDataModel: ComponentTrackDataModel) {
        val shopInfo = viewModel.shopInfo
        val productInfo = viewModel.getDynamicProductInfoP1
        if (viewModel.isUserSessionActive) {
            val productP3value = viewModel.productInfoP3resp.value
            if (shopInfo != null && shopInfo.isAllowManage == 1) {
                if (productInfo?.basic?.status != ProductStatusTypeDef.PENDING) {
                    DynamicProductDetailTracking.Click.onEditProductClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    gotoEditProduct()
                } else {
                    activity?.run {
                        val statusMessage = productInfo.basic.statusMessage(this)
                        if (statusMessage.isNotEmpty()) {
                            ToasterError.showClose(this, getString(R.string.product_is_at_status_x, statusMessage))
                        }
                    }
                }
            } else if (productP3value != null) {
                if (isActive) {
                    productInfo?.basic?.productID?.let {
                        viewModel.removeWishList(it,
                                onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                onErrorRemoveWishList = this::onErrorRemoveWishList)
                        DynamicProductDetailTracking.Click.eventPDPRemoveToWishlist(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    }

                } else {
                    productInfo?.basic?.productID?.let {
                        viewModel.addWishList(it,
                                onSuccessAddWishlist = this::onSuccessAddWishlist,
                                onErrorAddWishList = this::onErrorAddWishList)
                        productInfo.let {
                            DynamicProductDetailTracking.Moengage.eventPDPWishlistAppsFyler(it)
                        }
                        DynamicProductDetailTracking.Click.eventPDPAddToWishlist(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    }
                }
                if (isAffiliate && productInfo?.basic?.productID?.isNotEmpty() == true) {
                    productDetailTracking.eventClickWishlistOnAffiliate(
                            viewModel.userId,
                            productInfo.basic.productID)
                }
            }
        } else {
            DynamicProductDetailTracking.Click.eventPDPAddToWishlistNonLogin(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            context?.run {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
    }

    override fun onDiscussionClicked(componentTrackDataModel: ComponentTrackDataModel?) {

        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Iris.eventDiscussionClickedIris(this, deeplinkUrl, viewModel.shopInfo?.shopCore?.name
                    ?: "", componentTrackDataModel ?: ComponentTrackDataModel())
            DynamicProductDetailTracking.Moengage.sendMoEngageClickDiskusi(this, viewModel.shopInfo?.shopCore?.name
                    ?: "")
        }

        disscussionClicked()
    }

    override fun onLastDiscussionClicked(talkId: String, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventLastDicussionClicked(talkId, componentTrackDataModel
                ?: ComponentTrackDataModel(), viewModel.getDynamicProductInfoP1)
        disscussionClicked()
    }

    private fun disscussionClicked() {
        activity?.let {
            val intent = RouteManager.getIntent(
                    it,
                    ApplinkConstInternalGlobal.PRODUCT_TALK,
                    viewModel.getDynamicProductInfoP1?.basic?.productID
            )
            startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_TALK_PRODUCT)
        }
    }

    /**
     * Value Proposition Clicked
     */
    override fun onValuePropositionClicked(view: Int) {
        val title: String
        val desc: String
        val url: String
        when (view) {
            R.id.container_ready -> {
                title = getString(R.string.value_proposition_title_ready)
                desc = getString(R.string.value_proposition_desc_ready)
                url = ProductDetailConstant.URL_VALUE_PROPOSITION_READY
            }
            R.id.container_ori -> {
                title = getString(R.string.value_proposition_title_original)
                desc = getString(R.string.value_proposition_desc_original)
                url = ProductDetailConstant.URL_VALUE_PROPOSITION_ORI
            }
            R.id.container_guarantee_7_days -> {
                title = getString(R.string.value_proposition_title_guarantee_7_days)
                desc = getString(R.string.value_proposition_desc_guarantee_7_days)
                url = ProductDetailConstant.URL_VALUE_PROPOSITION_GUARANTEE_7_DAYS
            }
            else -> {
                title = getString(R.string.value_proposition_title_guarantee)
                desc = getString(R.string.value_proposition_desc_guarantee)
                url = ProductDetailConstant.URL_VALUE_PROPOSITION_GUARANTEE
            }
        }

        bottomSheet = ValuePropositionBottomSheet.newInstance(title, desc, url)
        fragmentManager?.let {
            bottomSheet.show(it, "pdp_bs")
        }
    }

    private fun observeData() {
        observeP1()
        observeP2Login()
        observeP2Shop()
        observeP2General()
        observeP3()
        observeToggleFavourite()
        observeToggleNotifyMe()
        observeMoveToWarehouse()
        observeMoveToEtalase()
        observeRecommendationProduct()
        observeImageVariantPartialyChanged()
        observeAddToCart()
        observeInitialVariantData()
        observeonVariantClickedData()
    }

    private fun observeImageVariantPartialyChanged() {
        viewModel.updatedImageVariant.observe(viewLifecycleOwner, Observer {
            val mediaList = it.second.toMutableList()
            val processedVariant = it.first
            pdpHashMapUtil?.productNewVariantDataModel?.listOfVariantCategory = processedVariant

            if (it.second.isNotEmpty()) {
                pdpHashMapUtil?.updateImageAfterClickVariant(mediaList)
                viewModel.getDynamicProductInfoP1 = VariantMapper.updateMediaToCurrentP1Data(viewModel.getDynamicProductInfoP1, mediaList)
                dynamicAdapter.notifyVariantSection(pdpHashMapUtil?.productNewVariantDataModel, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
                dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap, ProductDetailConstant.PAYLOAD_VARIANT_SELECTED)
            } else {
                dynamicAdapter.notifyVariantSection(pdpHashMapUtil?.productNewVariantDataModel, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
            }
        })
    }

    private fun observeonVariantClickedData() {
        viewModel.onVariantClickedData.observe(viewLifecycleOwner, Observer {
            val selectedChildAndPosition = VariantCommonMapper.selectedProductData(viewModel.variantData
                    ?: ProductVariantCommon())
            val selectedChild = selectedChildAndPosition?.second
            val updatedDynamicProductInfo = VariantMapper.updateDynamicProductInfo(viewModel.getDynamicProductInfoP1, selectedChild, viewModel.listOfParentMedia)

            pdpHashMapUtil?.productNewVariantDataModel?.listOfVariantCategory = it
            viewModel.multiOrigin[selectedChild?.productId.toString()]?.let {
                viewModel.selectedMultiOrigin = it
                pdpHashMapUtil?.snapShotMap?.nearestWarehouseDataModel = ProductSnapshotDataModel.NearestWarehouseDataModel(it.warehouseInfo.id, it.price, it.stockWording)
            }

            productId = updatedDynamicProductInfo?.basic?.productID
            viewModel.getDynamicProductInfoP1 = updatedDynamicProductInfo
            pdpHashMapUtil?.updateDataP1(updatedDynamicProductInfo)

            actionButtonView.renderData(viewModel.getDynamicProductInfoP1?.basic?.isActive() == false,
                    viewModel.p2Login.value?.isExpressCheckoutType ?: false,
                    hasTopAds())

            renderFullfillment()
            dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap)
            dynamicAdapter.notifyVariantSection(pdpHashMapUtil?.productNewVariantDataModel, 1)
        })
    }

    private fun observeInitialVariantData() {
        viewModel.initialVariantData.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                dynamicAdapter.clearElement(pdpHashMapUtil?.productNewVariantDataModel)
            } else {
                pdpHashMapUtil?.productNewVariantDataModel?.listOfVariantCategory = it
                dynamicAdapter.notifyVariantSection(pdpHashMapUtil?.productNewVariantDataModel, null)
            }
        })
    }

    private fun observeAddToCart() {
        viewModel.addToCartLiveData.observe(viewLifecycleOwner, Observer {
            hideProgressDialog()
            when (it) {
                is Success -> {
                    if (it.data.errorReporter.eligible) {
                        logException(Throwable(it.data.errorReporter.texts.submitTitle))
                        showDialogErrorAtc(it.data)
                    } else {
                        onSuccessAtc(it.data.data.cartId.toString())
                    }
                }
                is Fail -> {
                    logException(it.throwable)
                    showToasterError(it.throwable.message ?: "")
                }
            }
        })
    }

    private fun onSuccessAtc(cartId: String) {
        DynamicProductDetailTracking.Click.eventEcommerceBuy(viewModel.buttonActionType,
                viewModel.buttonActionText,
                viewModel.userId,
                viewModel.shopInfo?.goldOS?.shopTypeString ?: "",
                viewModel.shopInfo?.shopCore?.name ?: "",
                cartId,
                trackerAttributionPdp ?: "",
                viewModel.selectedMultiOrigin.warehouseInfo.isFulfillment,
                DynamicProductDetailTracking.generateVariantString(viewModel.variantData, viewModel.getDynamicProductInfoP1?.basic?.productID
                        ?: ""),
                viewModel.getDynamicProductInfoP1)

        when (viewModel.buttonActionType) {
            ProductDetailConstant.OCS_BUTTON -> {
                goToCheckout(ShipmentFormRequest
                        .BundleBuilder()
                        .build()
                        .bundle)
            }
            ProductDetailConstant.BUY_BUTTON -> {
                goToCartCheckout(cartId)
            }
            ProductDetailConstant.ATC_BUTTON -> {
                showAddToCartDoneBottomSheet()
            }
            ProductDetailConstant.TRADEIN_AFTER_DIAGNOSE -> {
                // Same with OCS but should send devideId
                goToCheckout(ShipmentFormRequest.BundleBuilder()
                        .deviceId(viewModel.tradeinDeviceId)
                        .build()
                        .bundle)
            }
        }
    }

    private fun goToCheckout(shipmentFormRequest: Bundle) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)
        intent.putExtras(shipmentFormRequest)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun goToCartCheckout(cartId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.CART)
        intent?.run {
            putExtra(ApplinkConst.Transaction.EXTRA_CART_ID, cartId)
            startActivity(intent)
        }
    }

    private fun observeP1() {
        viewModel.productLayout.observe(viewLifecycleOwner, Observer {
            if (::performanceMonitoringP1.isInitialized)
                performanceMonitoringP1.stopTrace()
            when (it) {
                is Success -> {
                    context?.let { context ->
                        pdpHashMapUtil = DynamicProductDetailHashMap(context, DynamicProductDetailMapper.hashMapLayout(it.data))
                    }
                    onSuccessGetDataP1(it.data)
                }
                is Fail -> {
                    logException(it.throwable)
                    renderPageError(it.throwable)
                }
            }
        })
    }

    private fun observeP2Login() {
        viewModel.p2Login.observe(this, Observer {
            topAdsGetProductManage = it.topAdsGetProductManage
            it.pdpAffiliate?.let { renderAffiliate(it) }
            actionButtonView.renderData(viewModel.getDynamicProductInfoP1?.basic?.isActive() == false,
                    it.isExpressCheckoutType, hasTopAds())

            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringP2Login.stopTrace()

            pdpHashMapUtil?.updateDataP2Login(it)
            dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        })
    }

    private fun observeP2Shop() {
        viewModel.p2ShopDataResp.observe(this, Observer {
            if (!viewModel.isUserSessionActive && ::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()
            performanceMonitoringP2.stopTrace()

            onSuccessGetDataP2Shop(it)
        })
    }

    private fun observeP2General() {
        viewModel.p2General.observe(this, Observer {
            if (::performanceMonitoringP2General.isInitialized)
                performanceMonitoringP2General.stopTrace()

            onSuccessGetDataP2General(it)
        })
    }

    private fun observeP3() {
        viewModel.productInfoP3resp.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()

            trackProductView(viewModel.tradeInParams.isEligible == 1)
            onSuccessGetDataP3Resp(it)
        })
    }

    private fun observeToggleFavourite() {
        viewModel.toggleFavoriteResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessFavoriteShop(it.data)
                is Fail -> onFailFavoriteShop(it.throwable)
            }
        })
    }

    private fun observeMoveToWarehouse() {
        viewModel.moveToWarehouseResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessWarehouseProduct()
                is Fail -> onErrorWarehouseProduct(it.throwable)
            }
        })
    }

    private fun observeMoveToEtalase() {
        viewModel.moveToEtalaseResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessMoveToEtalase()
                is Fail -> onErrorMoveToEtalase(it.throwable)
            }
        })
    }

    private fun observeRecommendationProduct() {
        viewModel.loadTopAdsProduct.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    pdpHashMapUtil?.updateRecomData(it.data)
                    dynamicAdapter.notifyRecomAdapter(pdpHashMapUtil?.listProductRecomMap)

                }
                is Fail -> dynamicAdapter.removeRecommendation(pdpHashMapUtil?.listProductRecomMap)
            }
        })
    }

    private fun onSuccessGetDataP1(data: List<DynamicPdpDataModel>) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            updateProductId()
            et_search.hint = String.format(getString(R.string.pdp_search_hint), productInfo.basic.category.name)
            pdpHashMapUtil?.updateDataP1(productInfo)
            viewModel.listOfParentMedia = productInfo.data.media.toMutableList()
            shouldShowCodP1 = productInfo.data.isCOD
            actionButtonView.isLeasing = productInfo.basic.isLeasing
            actionButtonView.renderData(!productInfo.basic.isActive(),
                    (viewModel.isShopOwner(productInfo.basic.getShopId())
                            || viewModel.shopInfo?.allowManage == true),
                    productInfo.data.preOrder)

            if (productInfo.basic.category.isAdult) {
                AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_PDP, productInfo.basic.productID)
            }

            actionButtonView.visibility = !isAffiliate
            if (affiliateString.hasValue()) {
                viewModel.hitAffiliateTracker(affiliateString
                        ?: "", viewModel.deviceId)
            }

            activity?.invalidateOptionsMenu()
            renderList(data)
        }
    }

    private fun onSuccessGetDataP2Shop(it: ProductInfoP2ShopData) {
        viewModel.getDynamicProductInfoP1?.let { p1 ->
            actionButtonView.renderData(!p1.basic.isActive(),
                    (viewModel.isShopOwner(p1.basic.getShopId())
                            || it.shopInfo?.allowManage ?: false),
                    p1.data.preOrder)
            actionButtonView.visibility = !isAffiliate && it.shopInfo?.statusInfo?.shopStatus == 1

            viewModel.shopInfo?.let { shopInfo ->
                DynamicProductDetailTracking.Moengage.sendMoEngageOpenProduct(p1, shopInfo.shopCore.name)
                DynamicProductDetailTracking.Moengage.eventAppsFylerOpenProduct(p1)

                DynamicProductDetailTracking.sendScreen(
                        irisSessionId,
                        shopInfo.shopCore.shopID,
                        shopInfo.goldOS.shopTypeString,
                        p1.basic.productID)
            }
        }

        val tradeinResponse = it.tradeinResponse?.validateTradeInPDP
                ?: ValidateTradeInResponse()

        if (!tradeinResponse.isEligible) {
            dynamicAdapter.removeGeneralInfo(pdpHashMapUtil?.productTradeinMap)
        } else {
            pdpHashMapUtil?.updateDataTradein(tradeinResponse)
        }

        pdpHashMapUtil?.updateDataP2Shop(it)
        adapter.notifyDataSetChanged()
    }

    private fun onSuccessGetDataP2General(it: ProductInfoP2General) {
        viewModel.installmentData = it.productFinancingCalculationData
        if (it.latestTalk.id.isEmpty()) {
            dynamicAdapter.removeDiscussionSection(pdpHashMapUtil?.productDiscussionMap)
        }

        if (it.vouchers.isNullOrEmpty()) {
            dynamicAdapter.removeMerchantVoucherSection(pdpHashMapUtil?.productMerchantVoucherMap)
        } else {
            if (!viewModel.isUserSessionActive || !isOwner()) {
                DynamicProductDetailTracking.Impression.eventImpressionMerchantVoucherUse(
                        viewModel.getDynamicProductInfoP1?.basic?.shopID.toIntOrZero(),
                        it.vouchers, viewModel.getDynamicProductInfoP1)
            }
        }

        if (it.helpfulReviews.isEmpty() && it.rating.totalRating == 0) {
            dynamicAdapter.removeMostHelpfulReviewSection(pdpHashMapUtil?.productMostHelpfulMap)
        }

        if (!it.shopCommitment.isNowActive) {
            dynamicAdapter.removeGeneralInfo(pdpHashMapUtil?.orderPriorityMap)
        }

        if (it.productPurchaseProtectionInfo.ppItemDetailPage?.isProtectionAvailable == false) {
            dynamicAdapter.removeGeneralInfo(pdpHashMapUtil?.productProtectionMap)
        }

        it.productFinancingRecommendationData.let { financingData ->
            if (financingData.response.data.partnerCode.isNotBlank()) {
                pdpHashMapUtil?.updateDataInstallment(financingData, viewModel.getDynamicProductInfoP1?.data?.isOS == true)
            } else {
                dynamicAdapter.removeGeneralInfo(pdpHashMapUtil?.productInstallmentInfoMap)
            }
        }

        onSuccessGetProductVariantInfo(it.variantResp)
        pdpHashMapUtil?.updateDataP2General(it)
        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Branch.eventBranchItemView(this, viewModel.userId, pdpHashMapUtil?.productInfoMap?.data?.find { content ->
                content.row == "bottom"
            }?.listOfContent?.firstOrNull()?.subtitle ?: "")
        }

        adapter.notifyDataSetChanged()
    }

    private fun onSuccessGetDataP3Resp(it: ProductInfoP3) {
        val nearestWarehouse = viewModel.selectedMultiOrigin

        shouldShowCodP3 = it.userCod
        pdpHashMapUtil?.productShipingInfoMap?.let { productShippingInfoMap ->
            if (it.ratesModel?.getServicesSize() == 0) {
                dynamicAdapter.removeGeneralInfo(productShippingInfoMap)
            } else {
                pdpHashMapUtil?.updateDataP3(it)
            }
            dynamicAdapter.notifyShipingInfo(productShippingInfoMap)
        }


        pdpHashMapUtil?.snapShotMap?.nearestWarehouseDataModel = ProductSnapshotDataModel.NearestWarehouseDataModel(nearestWarehouse.warehouseInfo.id, nearestWarehouse.price, nearestWarehouse.stockWording)
        pdpHashMapUtil?.snapShotMap?.shouldShowCod = shouldShowCodP1 && shouldShowCodP3

        renderFullfillment()
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap, ProductDetailConstant.PAYLOAD_P3)
    }

    private fun renderFullfillment() {
        val fullFillmentText = if (!viewModel.selectedMultiOrigin.warehouseInfo.isFulfillment) {
            ""
        } else {
            context?.getString(R.string.multiorigin_desc) ?: ""
        }

        pdpHashMapUtil?.productFullfilmentMap?.run {
            data.first().subtitle = fullFillmentText
        }
        dynamicAdapter.notifyGeneralInfo(pdpHashMapUtil?.productFullfilmentMap)
    }

    private fun logException(t: Throwable) {
        try {
            if (!BuildConfig.DEBUG) {
                val errorMessage = String.format(
                        getString(R.string.on_error_p1_string_builder),
                        viewModel.userSessionInterface.userId,
                        viewModel.userSessionInterface.email,
                        t.message
                )
                Crashlytics.logException(Exception(errorMessage))
            }
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    private fun renderPageError(t: Throwable) {
        context?.let { ctx ->
            dynamicAdapter.clearAllElements()
            dynamicAdapter.addElement(ProductDetailErrorHelper.getErrorType(ctx, t, isFromDeeplink, deeplinkUrl))
            if (swipeToRefresh != null) {
                swipeToRefresh.isEnabled = false
            }
            actionButtonView.visibility = false
        }
    }

    private fun showSnackbarClose(string: String) {
        view?.let {
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).apply {
                setAction(getString(R.string.close)) { dismiss() }
                setActionTextColor(Color.WHITE)
            }.show()
        }
    }

    private fun goToTradein() {
        tradeinDialog?.show(childFragmentManager, "ACCESS REQUEST")
    }

    override fun onVariantGuideLineClicked(url: String) {
        activity?.let {
            DynamicProductDetailTracking.Click.onVariantGuideLineClicked(viewModel.getDynamicProductInfoP1, pdpHashMapUtil?.productNewVariantDataModel,
                    dynamicAdapter.getVariantPosition(pdpHashMapUtil?.productNewVariantDataModel))
            startActivity(ImagePreviewActivity.getCallingIntent(it, arrayListOf(url)))
        }
    }

    override fun getStockWording(): String {
        val updatedNearestWarehouse = pdpHashMapUtil?.snapShotMap?.getNearestWarehouse()?.nearestWarehouseStockWording
        val isPartialySelected = pdpHashMapUtil?.productNewVariantDataModel?.isPartialySelected()
                ?: false

        return if (isPartialySelected) {
            ""
        } else {
            updatedNearestWarehouse ?: ""
        }
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
        pdpHashMapUtil?.productNewVariantDataModel?.let {
            it.mapOfSelectedVariant[variantOptions.variantCategoryKey] = variantOptions.variantId
        }
        val isPartialySelected = pdpHashMapUtil?.productNewVariantDataModel?.isPartialySelected()
                ?: false

        if (!isPartialySelected && shouldFireVariantTracker) {
            shouldFireVariantTracker = false
            DynamicProductDetailTracking.Click.onVariantLevel1Clicked(
                    viewModel.getDynamicProductInfoP1,
                    pdpHashMapUtil?.productNewVariantDataModel,
                    viewModel.variantData,
                    dynamicAdapter.getVariantPosition(pdpHashMapUtil?.productNewVariantDataModel))
        }

        viewModel.onVariantClicked(viewModel.variantData, pdpHashMapUtil?.productNewVariantDataModel?.mapOfSelectedVariant, isPartialySelected, variantOptions.level,
                variantOptions.imageOriginal)
    }

    private fun onSuccessGetProductVariantInfo(data: ProductVariantCommon?) {
        if (data == null || !data.hasChildren) {
            dynamicAdapter.clearElement(pdpHashMapUtil?.productNewVariantDataModel)
            return
        }
        pdpHashMapUtil?.productNewVariantDataModel?.mapOfSelectedVariant = VariantCommonMapper.mapVariantIdentifierToHashMap(data)
        viewModel.processVariant(data, pdpHashMapUtil?.productNewVariantDataModel?.mapOfSelectedVariant)
    }

    private fun showAddToCartDoneBottomSheet() {
        viewModel.getDynamicProductInfoP1?.let {
            val addToCartDoneBottomSheet = AddToCartDoneBottomSheet()
            val productName = it.getProductName
            val productImageUrl = it.data.getFirstProductImage()
            val addedProductDataModel = AddToCartDoneAddedProductDataModel(
                    it.basic.productID,
                    productName,
                    productImageUrl,
                    it.data.variant.isVariant,
                    it.data.isFreeOngkir.isActive,
                    it.data.isFreeOngkir.imageURL
            )
            val bundleData = Bundle()
            bundleData.putParcelable(AddToCartDoneBottomSheet.KEY_ADDED_PRODUCT_DATA_MODEL, addedProductDataModel)
            addToCartDoneBottomSheet.arguments = bundleData
            addToCartDoneBottomSheet.setDismissListener {
                shouldShowCartAnimation = true
                updateCartNotification()
            }
            fragmentManager?.let {
                addToCartDoneBottomSheet.show(
                        it, "TAG"
                )
            }
        }
    }

    private fun onShipmentClicked() {
        if (viewModel.isUserSessionActive) {
            val productP3value = viewModel.productInfoP3resp.value
            if (!productP3value?.ratesModel?.services.isNullOrEmpty()) {
                gotoRateEstimation()
            } else {
                goToCourier()
            }
        } else {
            goToCourier()
        }

        activity?.overridePendingTransition(0, 0)
    }

    private fun updateCartNotification() {
        viewModel.updateCartCounerUseCase(::onSuccessUpdateCartCounter)
    }

    private fun onSuccessUpdateCartCounter(count: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART);
        cache.putInt(CartConstant.IS_HAS_CART, if (count > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, count)
        cache.applyEditor()
        if (isAdded) {
            initToolBarMethod()
        }
    }

    private fun onErrorSubmitHelpTicket(e: Throwable?) {
        hideProgressDialog()
        view?.also {
            showToasterError(ErrorHandler.getErrorMessage(context, e))
        }
    }

    private fun onSuccessSubmitHelpTicket(result: SubmitTicketResult) {
        hideProgressDialog()
        if (result.status) {
            activity?.also {
                val successTicketDialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
                successTicketDialog.apply {
                    setTitle(result.texts.submitTitle)
                    setDescription(result.texts.submitDescription)
                    setPrimaryCTAText(result.texts.successButton)
                    setPrimaryCTAClickListener {
                        this.dismiss()
                    }
                    show()
                }
            }
        } else {
            view?.also {
                showToasterError(result.message)
            }
        }
    }


    private fun shareProduct() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.let { productInfo ->
                DynamicProductDetailTracking.Click.eventClickPdpShare(productInfo)

                val productData = ProductData(
                        productInfo.data.price.value.getCurrencyFormatted(),
                        "${productInfo.data.isCashback.percentage}%",
                        MethodChecker.fromHtml(productInfo.getProductName).toString(),
                        productInfo.data.price.currency,
                        productInfo.basic.url,
                        viewModel.shopInfo?.shopCore?.url ?: "",
                        viewModel.shopInfo?.shopCore?.name ?: "",
                        productInfo.basic.productID,
                        productInfo.data.getProductImageUrl() ?: ""
                )
                checkAndExecuteReferralAction(productData)
            }
        }
    }

    private fun checkAndExecuteReferralAction(productData: ProductData) {
        val userSession = UserSession(activity)
        val remoteConfig = FirebaseRemoteConfigImpl(context)

        val fireBaseRemoteMsgGuest = remoteConfig.getString(RemoteConfigKey.fireBaseGuestShareMsgKey, "")
        if (!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) productData.productShareDescription = fireBaseRemoteMsgGuest

        if (userSession.isLoggedIn && userSession.isMsisdnVerified) {
            val fireBaseRemoteMsg = remoteConfig.getString(RemoteConfigKey.fireBaseShareMsgKey, "")
            if (!TextUtils.isEmpty(fireBaseRemoteMsg) && fireBaseRemoteMsg.contains(ProductData.PLACEHOLDER_REFERRAL_CODE)) {
                doReferralShareAction(productData, fireBaseRemoteMsg)
                return
            }
        }
        executeProductShare(productData)
    }

    private fun doReferralShareAction(productData: ProductData, fireBaseRemoteMsg: String) {
        val actionCreator = object : ActionCreator<String, Int> {
            override fun actionSuccess(actionId: Int, dataObj: String) {
                if (!TextUtils.isEmpty(dataObj) && !TextUtils.isEmpty(fireBaseRemoteMsg)) {
                    productData.productShareDescription = FindAndReplaceHelper.findAndReplacePlaceHolders(fireBaseRemoteMsg,
                            ProductData.PLACEHOLDER_REFERRAL_CODE, dataObj)
                    DynamicProductDetailTracking.Moengage.sendMoEngagePDPReferralCodeShareEvent()
                }
                executeProductShare(productData)
            }

            override fun actionError(actionId: Int, dataObj: Int?) {
                executeProductShare(productData)
            }
        }
        val referralAction = ReferralAction<Context, String, Int, String, String, String, Context>()
        referralAction.doAction(Constants.Action.ACTION_GET_REFERRAL_CODE, context, actionCreator, object : ActionUIDelegate<String, String> {
            override fun waitForResult(actionId: Int, dataObj: String?) {
                showProgressDialog()
            }

            override fun stopWaiting(actionId: Int, dataObj: String?) {
                hideProgressDialog()
            }
        })

    }

    private fun executeProductShare(productData: ProductData) {
        activity?.let {
            val productShare = ProductShare(it, ProductShare.MODE_TEXT)
            productShare.share(productData, {
                showProgressDialog()
            }, {
                hideProgressDialog()
            })
        }
    }

    /**
     * Event than happen after owner successfully move the warehoused product back to etalase
     */
    private fun onSuccessMoveToEtalase() {
        hideProgressDialog()
        showToastSuccess(getString(R.string.success_move_etalase))
        onSwipeRefresh()
    }

    private fun onErrorMoveToEtalase(throwable: Throwable) {
        hideProgressDialog()
        showToastError(throwable)
    }

    private fun onErrorWarehouseProduct(throwable: Throwable) {
        hideProgressDialog()
        showToastError(throwable)
    }

    private fun onSuccessWarehouseProduct() {
        hideProgressDialog()
        showToastSuccess(getString(R.string.success_warehousing_product))
        onSwipeRefresh()
    }

    private fun reportProduct() {
        viewModel.getDynamicProductInfoP1?.run {
            if (viewModel.isUserSessionActive) {
                context?.let {
                    val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.REPORT_PRODUCT,
                            basic.productID)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_REPORT)
                }

                productDetailTracking.eventReportLogin()
            } else {
                context?.run {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            ProductDetailConstant.REQUEST_CODE_LOGIN)
                }
                productDetailTracking.eventReportNoLogin()
            }
        }

    }

    private fun warehouseProduct() {
        viewModel.getDynamicProductInfoP1?.basic?.productID?.let {
            showProgressDialog(onCancelClicked = { viewModel.cancelWarehouseUseCase() })
            viewModel.moveProductToWareHouse(it)
        }
    }

    private fun moveProductToEtalase() {
        context?.run {
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.shopID ?: ""
            if (shopId.isNotEmpty()) {
                val etalaseId = viewModel.getDynamicProductInfoP1?.basic?.menu?.id ?: ""
                val shopEtalasePickerIntent = ShopEtalasePickerActivity.createIntent(this,
                        shopId, etalaseId, false, true)
                startActivityForResult(shopEtalasePickerIntent, ProductDetailConstant.REQUEST_CODE_ETALASE)
            }
        }
    }

    private fun loadProductData(forceRefresh: Boolean = false) {
        if (productId != null || (productKey != null && shopDomain != null)) {
            viewModel.getProductP1(ProductParams(productId = productId, shopDomain = shopDomain, productName = productKey, warehouseId = warehouseId), forceRefresh)
        }
    }

    private fun gotoRateEstimation() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            viewModel.shopInfo?.let { shopInfo ->
                context?.let { context ->
                    startActivity(RatesEstimationDetailActivity.createIntent(
                            context,
                            shopInfo.shopCore.domain,
                            productInfo.basic.weight.toFloat(),
                            productInfo.basic.weightUnit,
                            if (viewModel.selectedMultiOrigin.warehouseInfo.isFulfillment)
                                viewModel.selectedMultiOrigin.warehouseInfo.getOrigin() else null,
                            productInfo.data.isFreeOngkir.isActive,
                            shopInfo.shopCore.shopID,
                            productInfo.basic.productID
                    ))
                }
            }
        }
    }

    private fun goToCourier() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.let { productInfo ->
                viewModel.shopInfo?.let { shopInfo ->
                    startActivity(CourierActivity.createIntent(it,
                            productInfo.basic.productID,
                            shopInfo.shipments,
                            shopInfo.bbInfo
                    ))
                }
            }
        }
    }

    private fun handlingMenuPreparation(menu: Menu?) {
        if (menu == null) return

        val menuShare = menu.findItem(R.id.action_share)
        val menuCart = menu.findItem(R.id.action_cart)
        val menuReport = menu.findItem(R.id.action_report)
        val menuWarehouse = menu.findItem(R.id.action_warehouse)
        val menuEtalase = menu.findItem(R.id.action_etalase)

        if (viewModel.getDynamicProductInfoP1 == null) {
            menuShare.isVisible = false
            menuShare.isEnabled = false
            menuCart.isVisible = true
            menuCart.isEnabled = true
            menuReport.isVisible = false
            menuReport.isEnabled = false
            menuWarehouse.isVisible = false
            menuWarehouse.isEnabled = false
            menuEtalase.isEnabled = false
            menuEtalase.isVisible = false
        } else {
            menuShare.isVisible = true
            menuShare.isEnabled = true

            viewModel.getDynamicProductInfoP1?.run {
                val isOwned = viewModel.isShopOwner(basic.getShopId())
                val isSellerApp = GlobalConfig.isSellerApp()

                val isValidCustomer = !isOwned && !isSellerApp
                menuCart.isVisible = isValidCustomer
                menuCart.isEnabled = isValidCustomer

                if (isValidCustomer) setBadgeMenuCart(menuCart)

                menuReport.isVisible = !isOwned && (basic.status != ProductStatusTypeDef.WAREHOUSE)
                menuReport.isEnabled = !isOwned && (basic.status != ProductStatusTypeDef.WAREHOUSE)
                menuWarehouse.isVisible = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.WAREHOUSE, ProductStatusTypeDef.PENDING))
                menuWarehouse.isEnabled = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.WAREHOUSE, ProductStatusTypeDef.PENDING))
                menuEtalase.isVisible = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.ACTIVE, ProductStatusTypeDef.PENDING))
                menuEtalase.isEnabled = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.ACTIVE, ProductStatusTypeDef.PENDING))
            }
        }
    }

    private fun initRecyclerView(view: View) {
        context?.let {
            getRecyclerView(view).layoutManager = CenterLayoutManager(it, LinearLayoutManager.VERTICAL, false)
        }
        getRecyclerView(view).itemAnimator = null
    }

    private fun renderAffiliate(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate) {
        if (isAffiliate) {
            base_btn_affiliate_dynamic.visible()
            loadingAffiliateDynamic.gone()
            getCommissionPdp.visible()
            commissionPdp.visible()
            commissionPdp.text = pdpAffiliate.commissionValueDisplay
            btn_affiliate_pdp.setOnClickListener { onAffiliateClick(pdpAffiliate, false) }
            actionButtonView.gone()
        } else if (!GlobalConfig.isSellerApp()) {
            base_btn_affiliate_dynamic.gone()
            actionButtonView.byMeClick = this::onAffiliateClick
            actionButtonView.showByMe(true, pdpAffiliate)
            actionButtonView.visibility = viewModel.shopInfo?.statusInfo?.shopStatus == 1
        }
    }

    private fun onAffiliateClick(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate,
                                 isRegularPdp: Boolean) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            activity?.let {
                DynamicProductDetailTracking.Click.eventClickAffiliate(viewModel.userId, productInfo.basic.getShopId(), isRegularPdp,
                        viewModel.getDynamicProductInfoP1)
                if (viewModel.isUserSessionActive) {
                    RouteManager.getIntent(it,
                            ApplinkConst.AFFILIATE_CREATE_POST,
                            pdpAffiliate.productId.toString(),
                            pdpAffiliate.adId.toString())
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .let(::startActivity)
                    it.setResult(Activity.RESULT_OK)
                    it.finish()
                } else {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            ProductDetailConstant.REQUEST_CODE_LOGIN)
                }
            }
        }
        return
    }

    private fun trackProductView(isElligible: Boolean) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            viewModel.shopInfo?.let { shopInfo ->
                DynamicProductDetailTracking.Impression.eventEnhanceEcommerceProductDetail(irisSessionId, trackerListNamePdp, productInfo, shopInfo, trackerAttributionPdp,
                        isElligible, viewModel.tradeInParams.usedPrice > 0, viewModel.selectedMultiOrigin.warehouseInfo.isFulfillment, deeplinkUrl, viewModel.getDynamicProductInfoP1?.getFinalStock(viewModel.selectedMultiOrigin.stock.toString())
                        ?: "0")
                return
            }
        }
        delegateTradeInTracking = true
    }

    private fun openFtInstallmentBottomSheet(installmentData: FinancingDataResponse) {
        val pdpInstallmentBottomSheet = FtPDPInstallmentBottomSheet()

        val productInfo = viewModel.getDynamicProductInfoP1
        val shopInfo = viewModel.shopInfo

        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(FinancingDataResponse::class.java.simpleName, installmentData, TimeUnit.HOURS.toMillis(1))
            val bundleData = Bundle()

            bundleData.putString(FtPDPInstallmentBottomSheet.KEY_PDP_FINANCING_DATA, cacheManager.id!!)
            bundleData.putFloat(FtPDPInstallmentBottomSheet.KEY_PDP_PRODUCT_PRICE, productInfo?.data?.price?.value?.toFloat()
                    ?: 0f)
            bundleData.putBoolean(FtPDPInstallmentBottomSheet.KEY_PDP_IS_OFFICIAL, shopInfo?.goldOS?.isOfficial == 1)

            pdpInstallmentBottomSheet.arguments = bundleData
            pdpInstallmentBottomSheet.show(childFragmentManager, "FT_TAG")
        }
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoringP1 = PerformanceMonitoring.start(ProductDetailConstant.PDP_P1_TRACE)
        performanceMonitoringP2 = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_TRACE)
        performanceMonitoringP2General = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_GENERAL_TRACE)

        if (viewModel.isUserSessionActive) {
            performanceMonitoringP2Login = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_LOGIN_TRACE)
            performanceMonitoringFull = PerformanceMonitoring.start(ProductDetailConstant.PDP_P3_TRACE)
        }
    }

    private fun generateVariantString(): String {
        return try {
            viewModel.p2General.value?.variantResp?.variant?.map { it.name }?.joinToString(separator = ", ")
                    ?: ""
        } catch (e: Throwable) {
            ""
        }
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        pdpHashMapUtil?.snapShotMap?.isWishlisted = false
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        sendIntentResultWishlistChange(productId ?: "", false)
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        pdpHashMapUtil?.snapShotMap?.isWishlisted = true
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil?.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        DynamicProductDetailTracking.Branch.eventBranchAddToWishlist(viewModel.getDynamicProductInfoP1, (UserSession(activity)).userId, pdpHashMapUtil?.productInfoMap?.data?.find { content ->
            content.row == "bottom"
        }?.listOfContent?.firstOrNull()?.subtitle ?: "")
        sendIntentResultWishlistChange(productId ?: "", true)
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun sendIntentResultWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(ProductDetailConstant.WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity!!.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun gotoEditProduct() {
        val id = viewModel.getDynamicProductInfoP1?.parentProductId ?: return
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_ITEM, id)
            intent?.run { startActivityForResult(this, ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT) }
        }
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.label_oke_pdp), clickListener = View.OnClickListener {})
        }
    }

    private fun initToolbar() {
        if (GlobalConfig.isSellerApp()) {
            val linearLayout = view?.findViewById<ViewGroup>(R.id.layout_search)
            linearLayout?.hide()
        }

        varToolbar = search_pdp_toolbar
        initToolBarMethod = ::initToolbarLight
        activity?.let {
            varToolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        et_search.setOnClickListener {
            DynamicProductDetailTracking.Click.eventSearchToolbarClicked(viewModel.getDynamicProductInfoP1)
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }
        et_search.hint = String.format(getString(R.string.pdp_search_hint), "")
    }

    private fun initStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_pdp)
        stickyLoginView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateStickyState()
        }
        stickyLoginView.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), ProductDetailConstant.REQUEST_CODE_LOGIN)
            stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.PDP)
        }
        stickyLoginView.setOnDismissListener(View.OnClickListener {
            stickyLoginView.dismiss(StickyLoginConstant.Page.PDP)
            stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.PDP)
            updateStickyState()
        })

        updateStickyContent()
    }

    private fun updateStickyState() {
        if (this.tickerDetail == null) {
            stickyLoginView.visibility = View.GONE
            return
        }

        val isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_PDP, true)
        if (!isCanShowing) {
            stickyLoginView.visibility = View.GONE
            return
        }

        val userSession = UserSession(activity)
        if (userSession.isLoggedIn) {
            stickyLoginView.visibility = View.GONE
            return
        }

        this.tickerDetail?.let { stickyLoginView.setContent(it) }
        stickyLoginView.show(StickyLoginConstant.Page.PDP)
        stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.PDP)
    }

    private fun updateStickyContent() {
        if (shouldRenderSticky) {
            viewModel.getStickyLoginContent(
                    onSuccess = {
                        this.tickerDetail = it
                        updateStickyState()
                        updateActionButtonShadow()
                        shouldRenderSticky = false
                    },
                    onError = {
                        stickyLoginView.visibility = View.GONE
                    }
            )
        }
    }

    private fun initBtnAction() {
        context?.let {
            topAdsDetailSheet = TopAdsDetailSheet.newInstance(it)
        }

        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
        }

        actionButtonView.rincianTopAdsClick = {
            context?.let {
                topAdsDetailSheet.show(topAdsGetProductManage.data.adId)
            }
        }

        topAdsDetailSheet.detailTopAdsClick = {
            viewModel.shopInfo?.let { shopInfo ->
                val applink = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopInfo.shopCore.shopID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, viewModel.getDynamicProductInfoP1?.basic?.productID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                if (GlobalConfig.isSellerApp()) TopAdsSourceOption.SA_PDP else TopAdsSourceOption.MA_PDP).build().toString()

                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, applink), ProductDetailFragment.REQUEST_CODE_EDIT_PRODUCT)
                }
            }
        }

        actionButtonView.promoTopAdsClick = {
            viewModel.shopInfo?.let { shopInfo ->
                val applink = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopInfo?.shopCore?.shopID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, viewModel.getDynamicProductInfoP1?.basic?.productID
                                ?: "")
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                if (GlobalConfig.isSellerApp()) TopAdsSourceOption.SA_PDP else TopAdsSourceOption.MA_PDP).build().toString()

                context?.let { RouteManager.route(it, applink) }
            }
        }

        actionButtonView.addToCartClick = {
            viewModel.buttonActionText = it
            viewModel.getDynamicProductInfoP1?.let {
                doAtc(ProductDetailConstant.ATC_BUTTON)
            }
        }

        actionButtonView.buyNowClick = {
            viewModel.buttonActionText = it
            // buy now / buy / preorder
            viewModel.getDynamicProductInfoP1?.let {
                if (viewModel.p2Login.value?.isOcsCheckoutType == true) {
                    doAtc(ProductDetailConstant.OCS_BUTTON)
                } else {
                    doAtc(ProductDetailConstant.BUY_BUTTON)
                }
            }
        }
    }

    private fun doAtc(buttonAction: Int) {
        viewModel.buttonActionType = buttonAction
        context?.let {
            val isVariant = viewModel.getDynamicProductInfoP1?.data?.variant?.isVariant ?: false
            val isPartialySelected = pdpHashMapUtil?.productNewVariantDataModel?.isPartialySelected() ?: false

            if (!viewModel.isUserSessionActive) {
                doLoginWhenUserClickButton()
                return@let
            }

            if (viewModel.buttonActionType == ProductDetailConstant.TRADEIN_BUTTON && viewModel.getDynamicProductInfoP1?.basic?.status == ProductStatusTypeDef.WAREHOUSE) {
                showToasterError(getString(R.string.tradein_error_label))
                return@let
            }

            if (isVariant && isPartialySelected) {
                showErrorVariantUnselected()
                return@let
            }

            when (viewModel.buttonActionType) {
                ProductDetailConstant.LEASING_BUTTON -> {
                    goToLeasing()
                    return@let
                }
                ProductDetailConstant.TRADEIN_BUTTON -> {
                    goToTradein()
                    return@let
                }
            }

            if (viewModel.getDynamicProductInfoP1?.checkImei(enableCheckImeiRemoteConfig) == true) {
                activity?.run {
                    ImeiPermissionAsker.checkImeiPermission(this, {
                        showImeiPermissionBottomSheet()
                    }, { hitAtc(buttonAction) }) { onNeverAskAgain() }
                }
            } else hitAtc(buttonAction)
        }
    }

    private fun doLoginWhenUserClickButton() {
        activity?.let {
            DynamicProductDetailTracking.Click.eventClickButtonNonLogin(viewModel.buttonActionType, viewModel.getDynamicProductInfoP1, viewModel.userId, viewModel.shopInfo?.goldOS?.shopTypeString
                    ?: "", viewModel.buttonActionText)
            startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), ProductDetailConstant.REQUEST_CODE_LOGIN)
        }
    }

    private fun showErrorVariantUnselected() {
        DynamicProductDetailTracking.Click.onVariantErrorPartialySelected(viewModel.getDynamicProductInfoP1, viewModel.buttonActionType)
        scrollToPosition(dynamicAdapter.getVariantPosition(pdpHashMapUtil?.productNewVariantDataModel))
        showToasterError(getString(R.string.add_to_cart_error_variant))
    }

    private fun buyAfterTradeinDiagnose(deviceId: String, phoneType: String, phonePrice: String) {
        //TODOTRACKTRADEIN
        viewModel.buttonActionType = ProductDetailConstant.TRADEIN_AFTER_DIAGNOSE
        viewModel.tradeinDeviceId = deviceId
        hitAtc(ProductDetailConstant.OCS_BUTTON)
    }

    private fun hitAtc(actionButton: Int) {
        val selectedWarehouseId = viewModel.selectedMultiOrigin.warehouseInfo.id.toIntOrZero()

        viewModel.getDynamicProductInfoP1?.let { data ->
            showProgressDialog()
            when (actionButton) {
                ProductDetailConstant.OCS_BUTTON -> {
                    val addToCartOcsRequestParams = AddToCartOcsRequestParams().apply {
                        productId = data.basic.productID.toLongOrNull() ?: 0
                        shopId = data.basic.shopID.toIntOrZero()
                        quantity = data.basic.minOrder
                        notes = ""
                        warehouseId = selectedWarehouseId
                        trackerAttribution = trackerAttributionPdp ?: ""
                        trackerListName = trackerListNamePdp ?: ""
                        isTradeIn = data.data.isTradeIn
                    }
                    viewModel.addToCart(addToCartOcsRequestParams)
                }
                else -> {
                    val addToCartRequestParams = AddToCartRequestParams().apply {
                        productId = data.basic.productID.toLongOrNull() ?: 0
                        shopId = data.basic.shopID.toIntOrZero()
                        quantity = data.basic.minOrder
                        notes = ""
                        attribution = trackerAttributionPdp ?: ""
                        listTracker = trackerListNamePdp ?: ""
                        warehouseId = selectedWarehouseId
                    }
                    viewModel.addToCart(addToCartRequestParams)
                }
            }
        }
    }

    private fun goToLeasing() {
        viewModel.getDynamicProductInfoP1?.run {
            val selectedProductId = basic.productID

            if (!viewModel.isUserSessionActive) {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS)
                return@run
            }

            DynamicProductDetailTracking.Click.eventClickApplyLeasing(
                    viewModel.getDynamicProductInfoP1,
                    viewModel.generateVariantString()
            )

            val urlApplyLeasingWithProductId = String.format(
                    ProductDetailCommonConstant.URL_APPLY_LEASING,
                    selectedProductId
            )

            val webViewUrl = String.format(
                    "%s?titlebar=false&url=%s",
                    ApplinkConst.WEBVIEW,
                    urlApplyLeasingWithProductId
            )
            RouteManager.route(context, webViewUrl)
        }
    }

    private fun gotoShopDetail(componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.shopID ?: return
            DynamicProductDetailTracking.Click.eventImageShopClicked(viewModel.getDynamicProductInfoP1, shopId, componentTrackDataModel)
            startActivityForResult(RouteManager.getIntent(it,
                    ApplinkConst.SHOP, shopId),
                    ProductDetailConstant.REQUEST_CODE_SHOP_INFO)
        }
    }

    private fun onShopFavoriteClick(componentTrackDataModel: ComponentTrackDataModel? = null) {
        val shop = viewModel.shopInfo ?: return
        activity?.let {
            if (viewModel.isUserSessionActive) {
                trackToggleFavoriteShop(componentTrackDataModel)
                pdpHashMapUtil?.getShopInfo?.toogleFavorite = false
                dynamicAdapter.notifyShopInfo(pdpHashMapUtil?.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE)
                viewModel.toggleFavorite(shop.shopCore.shopID)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun trackToggleFavoriteShop(componentTrackDataModel: ComponentTrackDataModel?) {
        val favorite = pdpHashMapUtil?.getShopInfo?.shopInfo?.favoriteData ?: return
        val shopName = pdpHashMapUtil?.getShopInfo?.shopInfo?.shopCore?.name ?: ""

        if (favorite.alreadyFavorited == 1)
            DynamicProductDetailTracking.Click.eventUnfollowShop(viewModel.getDynamicProductInfoP1, componentTrackDataModel,
                    shopName)
        else
            DynamicProductDetailTracking.Click.eventFollowShop(viewModel.getDynamicProductInfoP1, componentTrackDataModel,
                    shopName)
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean) {
        val favorite = pdpHashMapUtil?.getShopInfo?.shopInfo?.favoriteData ?: return
        if (isSuccess) {
            val newFavorite =
                    // If was favorited then change to un-favorited
                    if (favorite.alreadyFavorited == 1)
                        ShopInfo.FavoriteData(0, favorite.totalFavorite - 1)
                    else
                        ShopInfo.FavoriteData(1, favorite.totalFavorite + 1)
            pdpHashMapUtil?.getShopInfo?.shopInfo = pdpHashMapUtil?.getShopInfo?.shopInfo?.copy(favoriteData = newFavorite)
            pdpHashMapUtil?.getShopInfo?.isFavorite = favorite.alreadyFavorited != 1
            pdpHashMapUtil?.getShopInfo?.toogleFavorite = true
            dynamicAdapter.notifyShopInfo(pdpHashMapUtil?.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        context?.let {
            ToasterError.make(view, ProductDetailErrorHandler.getErrorMessage(it, t))
                    .setAction(R.string.retry_label) { onShopFavoriteClick() }
        }
        pdpHashMapUtil?.getShopInfo?.toogleFavorite = true
        dynamicAdapter.notifyShopInfo(pdpHashMapUtil?.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
    }

    private fun onShopChatClicked() {
        val shop = viewModel.shopInfo ?: return
        val product = viewModel.getDynamicProductInfoP1 ?: return
        activity?.let {
            if (viewModel.isUserSessionActive) {
                val intent = RouteManager.getIntent(it,
                        ApplinkConst.TOPCHAT_ASKSELLER,
                        shop.shopCore.shopID, "",
                        "product", shop.shopCore.name, shop.shopAssets.avatar)
                viewModel.putChatProductInfoTo(intent, product.basic.productID, product)
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                varToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                varToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                        setBadgeMenuCart(menuCart)
                    }
                }

                varToolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_product_more_dark)
            }
        }
    }

    private fun setBadgeMenuCart(menuCart: MenuItem) {
        activity?.run {
            val actionView = menuCart.actionView
            val cartImageView = actionView.cart_image_view
            val lottieCartView = actionView.cart_lottie_view
            cartImageView.setOnClickListener {
                gotoCart()
            }
            if (shouldShowCartAnimation) {
                if (actionView is SquareHFrameLayout) {
                    if (lottieCartView.visibility != View.VISIBLE) {
                        lottieCartView.addAnimatorListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator?) {}

                            override fun onAnimationEnd(animator: Animator?) {
                                showBadgeMenuCart(cartImageView, lottieCartView, true)
                                shouldShowCartAnimation = false
                            }

                            override fun onAnimationCancel(animator: Animator?) {}

                            override fun onAnimationStart(animator: Animator?) {}
                        })
                        cartImageView.visibility = View.INVISIBLE
                        lottieCartView.visibility = View.VISIBLE
                        if (!lottieCartView.isAnimating) {
                            lottieCartView.playAnimation()
                        }
                    }
                }
            } else {
                showBadgeMenuCart(cartImageView, lottieCartView, false)
            }
        }
    }

    private fun gotoCart() {
        activity?.let {
            if (viewModel.isUserSessionActive) {
                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
            DynamicProductDetailTracking.Click.eventCartToolbarClicked(viewModel.generateVariantString(),
                    viewModel.getDynamicProductInfoP1)
        }
    }

    private fun showBadgeMenuCart(cartImageView: ImageView, lottieCartView: LottieAnimationView, animate: Boolean) {
        activity?.run {
            val localCacheHandler = LocalCacheHandler(context, "CART")
            val cartCount = localCacheHandler.getInt("CACHE_TOTAL_CART", 0)

            val icon = ContextCompat.getDrawable(this, cartImageView.tag as Int)
            if (icon is LayerDrawable) {
                val badge = CountDrawable(this)
                badge.setCount(if (cartCount > ProductDetailConstant.CART_MAX_COUNT) {
                    getString(R.string.pdp_label_cart_count_max)
                } else if (!viewModel.isUserSessionActive) {
                    "0"
                } else {
                    cartCount.toString()
                })
                icon.mutate()
                icon.setDrawableByLayerId(R.id.ic_cart_count, badge)
                cartImageView.setImageDrawable(icon)
                if (animate) {
                    val alphaAnimation = AlphaAnimation(ProductDetailConstant.CART_ALPHA_ANIMATION_FROM, ProductDetailConstant.CART_ALPHA_ANIMATION_TO)
                    val scaleAnimation = ScaleAnimation(ProductDetailConstant.CART_SCALE_ANIMATION_FROM, ProductDetailConstant.CART_SCALE_ANIMATION_TO, ProductDetailConstant.CART_SCALE_ANIMATION_FROM, ProductDetailConstant.CART_SCALE_ANIMATION_TO, Animation.RELATIVE_TO_SELF, ProductDetailConstant.CART_SCALE_ANIMATION_PIVOT, Animation.RELATIVE_TO_SELF, ProductDetailConstant.CART_SCALE_ANIMATION_PIVOT)
                    scaleAnimation.fillAfter = false
                    val animationSet = AnimationSet(false)
                    animationSet.addAnimation(alphaAnimation)
                    animationSet.addAnimation(scaleAnimation)
                    animationSet.duration = ProductDetailConstant.CART_ANIMATION_DURATION
                    animationSet.fillAfter = false
                    animationSet.fillBefore = false
                    animationSet.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {}

                        override fun onAnimationEnd(p0: Animation?) {
                            lottieCartView.clearAnimation()
                            cartImageView.clearAnimation()
                            lottieCartView.visibility = View.INVISIBLE
                            cartImageView.visibility = View.VISIBLE
                        }

                        override fun onAnimationStart(p0: Animation?) {}
                    })
                    lottieCartView.startAnimation(animationSet)
                } else {
                    lottieCartView.visibility = View.INVISIBLE
                    cartImageView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateActionButtonShadow() {
        if (stickyLoginView.isShowing()) {
            actionButtonView.setBackground(R.color.white)
        } else {
            val drawable = context?.let { _context -> ContextCompat.getDrawable(_context, R.drawable.bg_shadow_top) }
            drawable?.let { actionButtonView.setBackground(it) }
        }
    }

    private fun showToastError(throwable: Throwable) {
        activity?.run {
            Toaster.make(findViewById(android.R.id.content),
                    ProductDetailErrorHandler.getErrorMessage(this, throwable),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR, "Oke", clickListener = View.OnClickListener {}
            )
        }
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            ToasterNormal.make(findViewById(android.R.id.content),
                    message,
                    ToasterNormal.LENGTH_LONG)
                    .show()
        }
    }

    private fun hideProgressDialog() {
        loadingProgressDialog?.dismiss()
    }

    private fun scrollToPosition(position: Int) {
        if (position >= 0) {
            getRecyclerView(view).post {
                try {
                    getRecyclerView(view).smoothScrollToPosition(position)
                } catch (e: Throwable) {

                }
            }
        }
    }

    private fun showProgressDialog(onCancelClicked: (() -> Unit)? = null) {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = activity?.createDefaultProgressDialog(
                    getString(R.string.title_loading),
                    cancelable = onCancelClicked != null,
                    onCancelClicked = {
                        onCancelClicked?.invoke()
                    })
        }
        loadingProgressDialog?.run {
            if (!isShowing) {
                show()
            }
        }
    }

    private fun updateProductId() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            productId = productInfo.basic.productID
        }
    }

    private fun hasTopAds() =
            topAdsGetProductManage.data.adId.isNotEmpty() && topAdsGetProductManage.data.adId != "0"

    private fun setupTradeinDialog(): ProductAccessRequestDialogFragment {
        val accessDialog = ProductAccessRequestDialogFragment()
        accessDialog.setBodyText(getString(com.tokopedia.common_tradein.R.string.tradein_text_permission_description))
        accessDialog.setTitle(getString(com.tokopedia.common_tradein.R.string.tradein_text_request_access))
        accessDialog.setNegativeButton("")
        accessDialog.setListener(this)
        return accessDialog
    }

    private fun initTradein() {
        viewModel.deviceId = TradeInUtils.getDeviceId(context)
                ?: viewModel.userSessionInterface.deviceId
        tradeinDialog = setupTradeinDialog()
    }

    private fun goToHargaFinal() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.FINAL_PRICE)
        val tradeinParam = viewModel.tradeInParams
        viewModel.getDynamicProductInfoP1?.let {
            tradeinParam.setPrice(it.data.price.value)
            tradeinParam.productId = it.basic.getProductId()
            tradeinParam.productName = it.data.name
        }

        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeinParam)
        startActivityForResult(intent, ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE)
    }

    private fun goToTradeInHome() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.TRADEIN)
        val tradeinParam = viewModel.tradeInParams

        viewModel.getDynamicProductInfoP1?.let {
            tradeinParam.setPrice(it.data.price.value)
            tradeinParam.productId = it.basic.getProductId()
            tradeinParam.productName = it.data.name
        }
        intent.putExtra(TradeInParams.PARAM_PERMISSION_GIVEN, true)
        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeinParam)
        startActivityForResult(intent, ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST)
    }

    private fun showDialogErrorAtc(result: AddToCartDataModel) {
        activity?.also { activity ->
            val createTicketDialog = DialogUnify(activity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            createTicketDialog.apply {
                setTitle(result.errorReporter.texts.submitTitle)
                setDescription(result.errorReporter.texts.submitDescription)
                setSecondaryCTAText(result.errorReporter.texts.cancelButton)
                setSecondaryCTAClickListener {
                    this.dismiss()
                    productDetailTracking.eventClickCloseOnHelpPopUpAtc()
                }
                setPrimaryCTAText(result.errorReporter.texts.submitButton)
                setPrimaryCTAClickListener {
                    this.dismiss()
                    productDetailTracking.eventClickReportOnHelpPopUpAtc()
                    showProgressDialog()
                    viewModel.hitSubmitTicket(result, this@DynamicProductDetailFragment::onErrorSubmitHelpTicket, this@DynamicProductDetailFragment::onSuccessSubmitHelpTicket)
                }
                show()
            }
            productDetailTracking.eventViewHelpPopUpWhenAtc()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.run {
            ImeiPermissionAsker.onImeiRequestPermissionsResult(this, requestCode, permissions, grantResults,
                onUserDenied = {}, onUserDeniedAndDontAskAgain = {}, onUserAcceptPermission = {}
            )
        }
    }

    private fun onNeverAskAgain() {
        DynamicProductDetailTracking.Click.eventClickBuyAskForImei(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
        activity?.run {
            CheckImeiBottomSheet.showPermissionDialog(this) {
                DynamicProductDetailTracking.Click.eventClickGiveAccessPhoneStatePermission(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
                showRationaleDialog()
            }
        }
    }

    private fun showRationaleDialog(){
        DynamicProductDetailTracking.Click.eventClickBuyAskForImei(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO, viewModel.userId, viewModel.getDynamicProductInfoP1)
        CheckImeiRationaleDialog.showRationaleDialog(activity, {
            DynamicProductDetailTracking.Click.eventClickGoToSetting(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO, viewModel.userId, viewModel.getDynamicProductInfoP1)
        }, {
            DynamicProductDetailTracking.Click.eventClickLater(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO, viewModel.userId, viewModel.getDynamicProductInfoP1)
        })
    }

    private fun observeToggleNotifyMe() {
        viewModel.toggleTeaserNotifyMe.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Fail -> onFailNotifyMe(it.throwable)
            }
        })
    }

    private fun onFailNotifyMe(t: Throwable) {
        val dataModel = pdpHashMapUtil?.notifyMeMap
        context?.let {
            showToastError(t)
        }
        if (dataModel != null) {
            pdpHashMapUtil?.notifyMeMap?.notifyMe = !dataModel.notifyMe
            dynamicAdapter.notifyNotifyMe(pdpHashMapUtil?.notifyMeMap, ProductDetailConstant.PAYLOAD_NOTIFY_ME)
        }
    }

    override fun onNotifyMeClicked(data: ProductNotifyMeDataModel, componentTrackDataModel: ComponentTrackDataModel) {
        try {
            activity?.let {
                if (viewModel.isUserSessionActive) {
                    val action = if (data.notifyMe) ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER else
                        ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER
                    pdpHashMapUtil?.notifyMeMap?.notifyMe?.let { notifyMe -> trackToggleNotifyMe(componentTrackDataModel, notifyMe) }
                    pdpHashMapUtil?.notifyMeMap?.notifyMe = !data.notifyMe
                    dynamicAdapter.notifyNotifyMe(pdpHashMapUtil?.notifyMeMap, ProductDetailConstant.PAYLOAD_NOTIFY_ME)
                    viewModel.toggleTeaserNotifyMe(data.campaignID.toInt(), productId?.toInt()
                            ?: 0, action, ProductDetailCommonConstant.VALUE_TEASER_SOURCE)
                } else {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            ProductDetailConstant.REQUEST_CODE_LOGIN)
                }
            }
        } catch (ex: Exception) {

        }
    }

    private fun trackToggleNotifyMe(componentTrackDataModel: ComponentTrackDataModel?, notifyMe: Boolean) {
        viewModel.getDynamicProductInfoP1?.let {
            DynamicProductDetailTracking.Click.eventNotifyMe(it, componentTrackDataModel, notifyMe)
        }
    }

    private fun showImeiPermissionBottomSheet() {
        DynamicProductDetailTracking.Click.eventClickBuyAskForImei(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
        activity?.run {
            CheckImeiBottomSheet.showPermissionDialog(this) {
                DynamicProductDetailTracking.Click.eventClickGiveAccessPhoneStatePermission(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
                ImeiPermissionAsker.askImeiPermissionFragment(this@DynamicProductDetailFragment)
            }
        }
    }

    override fun showAlertUpcomingEnded() {
        activity?.let {
            onSwipeRefresh()
        }
    }
}