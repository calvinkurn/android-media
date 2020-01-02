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
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.carouselproductcard.common.CarouselProductPool
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.common.data.model.product.Wholesale
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.ValidateTradeInPDP
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPdpActivity
import com.tokopedia.product.detail.view.activity.CourierActivity
import com.tokopedia.product.detail.view.activity.ProductFullDescriptionTabActivity
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.activity.WholesaleActivity
import com.tokopedia.product.detail.view.adapter.dynamicadapter.DynamicProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.DynamicProductDetailHashMap
import com.tokopedia.product.detail.view.util.ErrorHelper
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.product.detail.view.widget.FtPDPInstallmentBottomSheet
import com.tokopedia.product.detail.view.widget.SquareHFrameLayout
import com.tokopedia.product.detail.view.widget.ValuePropositionBottomSheet
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
import com.tokopedia.purchase_platform.common.sharedata.RESULT_CODE_ERROR_TICKET
import com.tokopedia.purchase_platform.common.sharedata.RESULT_TICKET_DATA
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheetsActionListenerWithRetry
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
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToLong

class DynamicProductDetailFragment : BaseListFragment<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>(), DynamicProductDetailListener {

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

    private var shouldShowCartAnimation = false
    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    @Inject
    lateinit var dynamicProductDetailTracking: DynamicProductDetailTracking
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DynamicProductDetailViewModel::class.java)
    }

    //Listener function
    private lateinit var initToolBarMethod: () -> Unit

    //Data
    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null
    private lateinit var remoteConfig: RemoteConfig
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var shouldShowCodP1 = false
    private var shouldShowCodP2Shop = false
    private var shouldShowCodP3 = false
    private var isAffiliate = false
    private var affiliateString: String? = null
    private var deeplinkUrl: String = ""
    private var userInputNotes = ""
    private var userInputQuantity = 0
    private var userInputVariant: String? = null
    private var delegateTradeInTracking = false
    private var trackerAttribution: String? = ""
    private var trackerListName: String? = ""
    private var warehouseId: String? = null
    private var isTopdasLoaded: Boolean = false
    private var shouldRenderSticky = true

    //View
    private lateinit var bottomSheet: ValuePropositionBottomSheet
    private lateinit var varToolbar: Toolbar
    private lateinit var actionButtonView: PartialButtonActionView
    private lateinit var stickyLoginView: StickyLoginView
    private lateinit var pdpHashMapUtil: DynamicProductDetailHashMap
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(this) }
    private val dynamicAdapter by lazy { DynamicProductDetailAdapter(adapterFactory, this) }
    private var menu: Menu? = null
    private var loadingProgressDialog: ProgressDialog? = null

    val errorBottomsheets: ErrorBottomsheets by lazy {
        ErrorBottomsheets()
    }

    val carouselProductPool = CarouselProductPool()

    //Performance Monitoring
    lateinit var performanceMonitoringP1: PerformanceMonitoring
    lateinit var performanceMonitoringP2: PerformanceMonitoring
    lateinit var performanceMonitoringP2General: PerformanceMonitoring
    lateinit var performanceMonitoringP2Login: PerformanceMonitoring
    lateinit var performanceMonitoringFull: PerformanceMonitoring

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dynamic_product_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        isLoadingInitialData = true
        isTopdasLoaded = false
        actionButtonView.visibility = false
        adapter.clearAllElements()
        showLoading()
        updateStickyContent()
        loadProductData(true)
        carouselProductPool.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            userInputNotes = savedInstanceState.getString(ProductDetailConstant.SAVED_NOTE, "")
            userInputQuantity = savedInstanceState.getInt(ProductDetailConstant.SAVED_QUANTITY, 1)
            userInputVariant = savedInstanceState.getString(ProductDetailConstant.SAVED_VARIANT)
        }
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ProductDetailConstant.ARG_PRODUCT_ID)
            warehouseId = it.getString(ProductDetailConstant.ARG_WAREHOUSE_ID)
            productKey = it.getString(ProductDetailConstant.ARG_PRODUCT_KEY)
            shopDomain = it.getString(ProductDetailConstant.ARG_SHOP_DOMAIN)
            trackerAttribution = it.getString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION)
            trackerListName = it.getString(ProductDetailConstant.ARG_TRACKER_LIST_NAME)
            affiliateString = it.getString(ProductDetailConstant.ARG_AFFILIATE_STRING)
            isAffiliate = it.getBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, false)
            deeplinkUrl = it.getString(ProductDetailConstant.ARG_DEEPLINK_URL, "")
        }
        activity?.run {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }
        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ProductDetailConstant.SAVED_NOTE, userInputNotes)
        outState.putInt(ProductDetailConstant.SAVED_QUANTITY, userInputQuantity)
        outState.putString(ProductDetailConstant.SAVED_VARIANT, userInputVariant)
    }

    override fun onPause() {
        super.onPause()
        productDetailTracking.sendAllQueue()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        // handling menu toolbar / cart counter / settings / etc

        activity?.let {
            handlingMenuPreparation(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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

    override fun createAdapterInstance(): BaseListAdapter<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl> {
        return dynamicAdapter
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_pdp
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipeRefreshPdp
    }

    override fun getAdapterTypeFactory(): DynamicProductDetailAdapterFactoryImpl = adapterFactory

    override fun onItemClicked(t: DynamicPDPDataModel) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_product_detail_dark, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        initToolBarMethod.invoke()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.productLayout.observe(this, Observer {
            when (it) {
                is Success -> {
                    context?.let { context ->
                        pdpHashMapUtil = DynamicProductDetailHashMap(context, DynamicProductDetailMapper.hashMapLayout(it.data))
                    }
                    viewModel.getDynamicProductInfoP1?.let { productInfo ->
                        pdpHashMapUtil.updateDataP1(productInfo)
                        // if when first time and the product is actually a variant product, then select the default variant
                        if (userInputVariant == null && productInfo.data.variant.isVariant && productInfo.data.variant.parentID != productId) {
                            userInputVariant = productId
                        }
                        shouldShowCodP1 = productInfo.data.isCOD
                        actionButtonView.isLeasing = productInfo.basic.isLeasing
                        actionButtonView.renderData(!productInfo.basic.isActive(),
                                (viewModel.isShopOwner(productInfo.basic.getShopId())
                                        || viewModel.shopInfo?.allowManage == true),
                                productInfo.data.preOrder)

                        if (productInfo.basic.category.isAdult) {
                            AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_PDP, productId
                                    ?: "")
                        }
                    }

                    actionButtonView.visibility = !isAffiliate
                    if (affiliateString.hasValue()) {
                        viewModel.hitAffiliateTracker(affiliateString
                                ?: "", viewModel.deviceId)
                    }

                    activity?.invalidateOptionsMenu()
                    renderList(it.data)
                }
                is Fail -> {
                    renderPageError(it.throwable)
                }
            }
        })

        viewModel.loadTopAdsProduct.observe(this, Observer {
            when (it) {
                is Success -> {
                    pdpHashMapUtil.updateRecomData(it.data)
                    dynamicAdapter.notifyRecomAdapter(pdpHashMapUtil.listProductRecomMap)
                }
                is Fail -> dynamicAdapter.removeRecommendation(pdpHashMapUtil.listProductRecomMap)
            }
        })

        viewModel.p2Login.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringP2Login.stopTrace()

            it.pdpAffiliate?.let { renderAffiliate(it) }
            pdpHashMapUtil.snapShotMap.apply {
                isWishlisted = it.isWishlisted
            }

            dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        })

        viewModel.p2ShopDataResp.observe(this, Observer {
            if (!viewModel.isUserSessionActive && ::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()

            performanceMonitoringP2.stopTrace()

            shouldShowCodP2Shop = it.shopCod

            viewModel.getDynamicProductInfoP1?.let { p1 ->
                actionButtonView.renderData(!p1.basic.isActive(),
                        (viewModel.isShopOwner(p1.basic.getShopId())
                                || it.shopInfo?.allowManage ?: false),
                        p1.data.preOrder)
                actionButtonView.visibility = !isAffiliate && it.shopInfo?.statusInfo?.shopStatus == 1
            }

            viewModel.getDynamicProductInfoP1?.let { data ->
                pdpHashMapUtil.getShopInfo.shopInfo?.let { shopInfo ->
                    dynamicProductDetailTracking.sendMoEngageOpenProduct(data, shopInfo.shopCore.name)
                    dynamicProductDetailTracking.eventAppsFylerOpenProduct(data)

                    dynamicProductDetailTracking.sendScreen(
                            shopInfo.shopCore.shopID,
                            shopInfo.goldOS.shopTypeString,
                            productId ?: "")
                }
            }

            if (!it.nearestWarehouse.warehouseInfo.isFulfillment) {
                dynamicAdapter.removeGeneralInfo(pdpHashMapUtil.productFullfilmentMap)
            }

            val tradeinResponse = it.tradeinResponse?.validateTradeInPDP ?: ValidateTradeInPDP()

            if (!tradeinResponse.isEligible) {
                dynamicAdapter.removeGeneralInfo(pdpHashMapUtil.productTradeinMap)
            } else {
                pdpHashMapUtil.productTradeinMap?.run {
                    pdpHashMapUtil.snapShotMap.shouldShowTradein = true
                    data.first().subtitle = if (tradeinResponse.usedPrice > 0) {
                        getString(R.string.text_price_holder, CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeinResponse.usedPrice, true))
                    } else {
                        getString(R.string.trade_in_exchange)
                    }
                }
            }

            trackProductView(viewModel.tradeInParams.isEligible == 1)

            pdpHashMapUtil.updateDataP2Shop(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.p2General.observe(this, Observer {
            performanceMonitoringP2General.stopTrace()
            viewModel.installmentData = it.productFinancingCalculationData
            if (it.latestTalk.id.isEmpty()) {
                dynamicAdapter.removeDiscussionSection(pdpHashMapUtil.productDiscussionMap)
            }

            if (it.helpfulReviews.isEmpty() && it.rating.totalRating == 0) {
                dynamicAdapter.removeMostHelpfulReviewSection(pdpHashMapUtil.productMostHelpfulMap)
            }

            if (!it.shopCommitment.isNowActive) {
                dynamicAdapter.removeGeneralInfo(pdpHashMapUtil.orderPriorityMap)
            }

            if (it.productPurchaseProtectionInfo.ppItemDetailPage?.isProtectionAvailable == false) {
                dynamicAdapter.removeGeneralInfo(pdpHashMapUtil.productProtectionMap)
            }

            it.productFinancingRecommendationData.let { financingData ->
                if (financingData.response.data.partnerCode.isNotBlank()) {
                    pdpHashMapUtil.productInstallmentInfoMap?.run {
                        data.first().subtitle = String.format(getString(R.string.new_installment_template),
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                        (if (viewModel.getDynamicProductInfoP1?.data?.isOS == true) financingData.response.data.osMonthlyPrice
                                        else financingData.response.data.monthlyPrice).roundToLong(), false))
                    }
                } else {
                    dynamicAdapter.removeGeneralInfo(pdpHashMapUtil.productInstallmentInfoMap)
                }
            }

            onSuccessGetProductVariantInfo(it.variantResp)

            pdpHashMapUtil.updateDataP2General(it)

            viewModel.getDynamicProductInfoP1?.run {
                dynamicProductDetailTracking.eventBranchItemView(this, viewModel.userId, pdpHashMapUtil.productInfoMap?.data?.find { content ->
                    content.row == "bottom"
                }?.listOfContent?.firstOrNull()?.subtitle ?: "")
            }

            adapter.notifyDataSetChanged()
        })

        viewModel.productInfoP3resp.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()

            pdpHashMapUtil.productShipingInfoMap?.run {
                data.first().subtitle = " ${getString(R.string.shipping_pattern_string, it.ratesModel?.services?.size
                        ?: 0)}${getString(R.string.ongkir_pattern_string, it.rateEstSummarizeText?.minPrice, "<b>${it.rateEstSummarizeText?.destination}</b>")}"
                dynamicAdapter.notifyShipingInfo(this)
            }

            shouldShowCodP3 = it.userCod
            pdpHashMapUtil.snapShotMap.shouldShowCod =
                    shouldShowCodP1 && shouldShowCodP2Shop && shouldShowCodP3
            dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOAD_COD)
        })

        viewModel.toggleFavoriteResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessFavoriteShop(it.data)
                is Fail -> onFailFavoriteShop(it.throwable)
            }
        })

        viewModel.moveToWarehouseResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessWarehouseProduct()
                is Fail -> onErrorWarehouseProduct(it.throwable)
            }
        })

        viewModel.moveToEtalaseResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessMoveToEtalase()
                is Fail -> onErrorMoveToEtalase(it.throwable)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            AdultManager.handleActivityResult(activity!!, requestCode, resultCode, data)
        }
        when (requestCode) {
            ProductDetailConstant.REQUEST_CODE_ETALASE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                    val selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                    if (productId != null && !selectedEtalaseName.isNullOrEmpty()) {
                        showProgressDialog(onCancelClicked = { viewModel.cancelEtalaseUseCase() })
                        viewModel.moveProductToEtalase(productId!!, selectedEtalaseId, selectedEtalaseName)
                    }
                }
            }
            ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER_DETAIL,
            ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER -> {
                if (resultCode == Activity.RESULT_OK) {
                    // no op //TOGGLE_MVC_OFF
                }
            }
            ProductDetailConstant.REQUEST_CODE_NORMAL_CHECKOUT -> {
                if (resultCode == RESULT_CODE_ERROR_TICKET && data != null) {
                    activity?.also { activity ->
                        val result = data.getParcelableExtra<AddToCartDataModel>(RESULT_TICKET_DATA)
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
                } else if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(NormalCheckoutConstant.RESULT_PRODUCT_DATA_CACHE_ID)) {
                        //refresh product by selected variant/product
                        val objectId: String = data.getStringExtra(NormalCheckoutConstant.RESULT_PRODUCT_DATA_CACHE_ID)
                        val cacheManager = SaveInstanceCacheManager(context!!, objectId)
                        val selectedProductInfo: ProductInfo? = cacheManager.get(NormalCheckoutConstant.RESULT_PRODUCT_DATA, ProductInfo::class.java)
                        val selectedWarehouse: MultiOriginWarehouse? = cacheManager.get(NormalCheckoutConstant.RESULT_SELECTED_WAREHOUSE,
                                MultiOriginWarehouse::class.java)
                        if (selectedProductInfo != null) {
                            userInputVariant = data.getStringExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID)
                            val dynamicP1Copy = DynamicProductDetailMapper.mapProductInfoToDynamicProductInfo(selectedProductInfo, viewModel.getDynamicProductInfoP1
                                    ?: DynamicProductInfoP1())
                            viewModel.getDynamicProductInfoP1 = dynamicP1Copy
                            pdpHashMapUtil.snapShotMap.apply {
                                selectedWarehouse?.let {
                                    nearestWarehouse = it
                                    viewModel.multiOrigin = it.warehouseInfo
                                }
                            }
                            pdpHashMapUtil.updateDataP1(dynamicP1Copy)
                            dynamicAdapter.notifyDataSetChanged()
                        }
                    }
                    //refresh variant
                    viewModel.p2General.value?.variantResp?.run {
                        onSuccessGetProductVariantInfo(this)
                    }
                    userInputNotes = data.getStringExtra(ApplinkConst.Transaction.EXTRA_NOTES)
                    userInputQuantity = data.getIntExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, 0)
                    if (data.hasExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)) {
                        val successMessage = data.getStringExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)
                        showAddToCartDoneBottomSheet(successMessage)
                        updateCartNotification()
                    }
                }
            }
            ProductDetailConstant.REQUEST_CODE_ATC_EXPRESS -> {
                if (resultCode == Constant.RESULT_CODE_ERROR && data != null) {
                    val message = data.getStringExtra(Constant.EXTRA_MESSAGES_ERROR)
                    if (message != null && message.isNotEmpty()) {
                        ToasterError.make(view, data.getStringExtra(Constant.EXTRA_MESSAGES_ERROR), BaseToaster.LENGTH_SHORT)
                                .show()
                    } else {
                        errorBottomsheets.setData(
                                getString(R.string.bottomsheet_title_global_error),
                                getString(R.string.bottomsheet_message_global_error),
                                getString(R.string.bottomsheet_action_global_error),
                                true
                        )
                        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListenerWithRetry {
                            override fun onActionButtonClicked() {
                                errorBottomsheets.dismiss()
                                goToNormalCheckout()
                            }

                            override fun onRetryClicked() {
                                errorBottomsheets.dismiss()
                                goToAtcExpress()
                            }
                        }
                        errorBottomsheets.show(fragmentManager, "")
                    }
                } else if (resultCode == Constant.RESULT_CODE_NAVIGATE_TO_OCS) {
                    goToNormalCheckout()
                } else if (resultCode == Constant.RESULT_CODE_NAVIGATE_TO_NCF) {
                    goToNormalCheckout()
                }
            }
            ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT -> {
                loadProductData(true)
            }
            ProductDetailConstant.REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS -> {
                doBuy()
            }
            ProductDetailConstant.REQUEST_CODE_LOGIN -> {
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
                    pdpHashMapUtil.snapShotMap.isWishlisted = isWishlisted
                    dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
                }
            }
            ProductDetailConstant.REQUEST_CODE_SHOP_INFO -> {
                if (data != null) {
                    val isFavorite = data.getBooleanExtra(ProductDetailConstant.SHOP_STATUS_FAVOURITE, false)
                    val isSticky = data.getBooleanExtra(ProductDetailConstant.SHOP_STICKY_LOGIN, false)
                    val favorite = pdpHashMapUtil.getShopInfo.shopInfo?.favoriteData?.alreadyFavorited == 1

                    shouldRenderSticky = true
                    if (isSticky) updateStickyContent()

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
        viewModel.p2ShopDataResp.removeObservers(this)
        viewModel.p2General.removeObservers(this)
        viewModel.p2Login.removeObservers(this)
        viewModel.productInfoP3resp.removeObservers(this)
        viewModel.loadTopAdsProduct.removeObservers(this)
        viewModel.moveToWarehouseResult.removeObservers(this)
        viewModel.moveToEtalaseResult.removeObservers(this)
        viewModel.clear()
        carouselProductPool.release()
        super.onDestroy()
    }

    /**
     * ProductInfoViewHolder
     */
    override fun onSubtitleInfoClicked(applink: String, etalaseId: String, shopId: Int, categoryId: String) {
        when {
            applink.startsWith(ApplinkConst.SHOP_ETALASE) -> {
                gotoEtalase(etalaseId, shopId)
            }
            else -> {
                openCategory(categoryId)
            }
        }
    }

    override fun gotoVideoPlayer(videos: List<Video>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, videos.map { it.url }, index))
            } else {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + videos[index].url)))
            }
        }
    }

    override fun getApplicationContext(): Application? {
        return activity?.application
    }

    override val onViewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_favorite -> onShopFavoriteClick()
            R.id.send_msg_shop, R.id.btn_topchat -> onShopChatClicked()
            R.id.shop_ava, R.id.shop_name -> gotoShopDetail()
            R.id.btn_apply_leasing -> onApplyLeasingClicked()
            else -> {
            }
        }
    }

    override fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>) {
        context?.let {
            startActivity(ProductFullDescriptionTabActivity.createIntent(it,
                    data, listOfCatalog))
            activity?.overridePendingTransition(R.anim.pull_up, 0)
            productDetailTracking.eventClickProductDescriptionReadMore(data.basicId)
        }
    }

    override fun onShipmentClicked() {
        productDetailTracking.eventShippingClicked(productId ?: "")
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

    /**
     * ProductGeneralInfoViewHolder Listener
     */
    override fun onInfoClicked(name: String) {
        when (name) {
            ProductDetailConstant.PRODUCT_SHIPPING_INFO -> {
                onShipmentClicked()
            }
            ProductDetailConstant.TRADE_IN -> {
                onTradeinClicked()
            }
            ProductDetailConstant.PRODUCT_INSTALLMENT_INFO -> {
                openFtInstallmentBottomSheet(viewModel.installmentData ?: FinancingDataResponse())
            }
            ProductDetailConstant.PRODUCT_VARIANT_INFO -> {
                productDetailTracking.eventClickVariant(generateVariantString(), productId ?: "")
                goToNormalCheckout(ATC_AND_BUY)
            }
            ProductDetailConstant.PRODUCT_WHOLESALE_INFO -> {
                val data = DynamicProductDetailMapper.mapToWholesale(viewModel.getDynamicProductInfoP1?.data?.wholesale)
                if (data != null && data.isNotEmpty()) {
                    context?.run {
                        startActivity(WholesaleActivity.getIntent(this,
                                (data as ArrayList<Wholesale>)))
                    }
                }
            }
        }
    }

    /**
     * ProductRecommendationViewHolder Listener
     */
    override fun onSeeAllRecomClicked(pageName: String, applink: String) {
        productDetailTracking.eventClickSeeMoreRecomWidget(pageName)
        RouteManager.route(context, applink)
    }

    override fun eventRecommendationClick(recomItem: RecommendationItem, position: Int, pageName: String, title: String) {
        productDetailTracking.eventRecommendationClick(
                recomItem, position, viewModel.isUserSessionActive, pageName, title)
    }

    override fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String) {
        productDetailTracking.eventRecommendationImpression(
                position, recomItem, viewModel.isUserSessionActive, pageName, title)
    }

    override fun getPdpCarouselPool(): CarouselProductPool {
        return carouselProductPool
    }

    override fun loadTopads() {
        if (::pdpHashMapUtil.isInitialized && pdpHashMapUtil.listProductRecomMap?.isNotEmpty() == true && !isTopdasLoaded) {
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
    override fun onSeeAllReviewClick() {
        context?.let {
            val productId = viewModel.getDynamicProductInfoP1?.basic?.getProductId() ?: 0
            productDetailTracking.eventClickReviewOnSeeAllImage(productId)
            RouteManager.route(it, ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY, productId.toString())
        }
    }

    override fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int) {
        context?.let {
            val productId = viewModel.getDynamicProductInfoP1?.basic?.getProductId() ?: 0
            productDetailTracking.eventClickReviewOnBuyersImage(productId, listOfImage[position].reviewId)
            val listOfImageReview: List<String> = listOfImage.map {
                it.imageUrlLarge ?: ""
            }

            ImageReviewGalleryActivity.moveTo(context, ArrayList(listOfImageReview), position)
        }
    }

    override fun onReviewClick() {
        viewModel.getDynamicProductInfoP1?.run {
            productDetailTracking.eventReviewClicked()
            dynamicProductDetailTracking.eventReviewClickedIris(this, deeplinkUrl, viewModel.shopInfo?.shopCore?.name
                    ?: "")
            dynamicProductDetailTracking.sendMoEngageClickReview(this, viewModel.shopInfo?.shopCore?.name
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

    override fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?) {
        productDetailTracking.eventClickReviewOnMostHelpfulReview(viewModel.getDynamicProductInfoP1?.basic?.getProductId()
                ?: 0, reviewId)
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

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
        activity?.let {
            //TOGGLE_MVC_OFF
            productDetailTracking.eventClickMerchantVoucherUse(merchantVoucherViewModel, position)
            showSnackbarClose(getString(R.string.title_voucher_code_copied))
        }
    }

    override fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                productDetailTracking.eventClickMerchantVoucherSeeDetail(basic.getProductId())
                val intent = MerchantVoucherDetailActivity.createIntent(it, merchantVoucherViewModel.voucherId,
                        merchantVoucherViewModel, basic.shopID)
                startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
            }
        }
    }

    override fun onSeeAllMerchantVoucherClick() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                productDetailTracking.eventClickMerchantVoucherSeeAll(basic.getProductId())
                val intent = MerchantVoucherListActivity.createIntent(it, basic.shopID,
                        viewModel.shopInfo?.shopCore?.name)
                startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER)
            }
        }
    }

    /**
     * ProductSnapshotViewHolder
     */

    override fun onSwipePicture(swipeDirection: String) {
        productDetailTracking.eventProductImageOnSwipe(productId, swipeDirection)
    }

    override fun onImageClicked(position: Int) {
        val isWishlisted = pdpHashMapUtil.snapShotMap.isWishlisted
        activity?.let {
            val intent = ImagePreviewPdpActivity.createIntent(it, productId
                    ?: "", isWishlisted, viewModel.getDynamicProductInfoP1?.data?.getImagePath()
                    ?: arrayListOf(), null, position)
            startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_IMAGE_PREVIEW)
        }
    }

    override fun txtTradeinClicked(adapterPosition: Int) {
        scrollToPosition(adapterPosition)
    }

    override fun getProductFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun showAlertCampaignEnded() {
        Dialog(activity, Dialog.Type.LONG_PROMINANCE).apply {
            setTitle(getString(R.string.campaign_expired_title))
            setDesc(getString(R.string.campaign_expired_descr))
            setBtnOk(getString(R.string.exp_dialog_ok))
            setBtnCancel(getString(R.string.close))
            setOnCancelClickListener { loadData(0); dismiss() }
            setOnOkClickListener { dismiss(); }
        }.show()
    }

    override fun onFabWishlistClicked(isActive: Boolean) {
        val shopInfo = viewModel.shopInfo
        val productInfo = viewModel.getDynamicProductInfoP1
        if (viewModel.isUserSessionActive) {
            val productP3value = viewModel.productInfoP3resp.value
            if (shopInfo != null && shopInfo.isAllowManage == 1) {
                if (productInfo?.basic?.status != ProductStatusTypeDef.PENDING) {
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
                    productId?.let {
                        viewModel.removeWishList(it,
                                onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                onErrorRemoveWishList = this::onErrorRemoveWishList)
                        productDetailTracking.eventPDPRemoveToWishlist(productInfo?.basic?.productID.toString())
                    }

                } else {
                    productId?.let {
                        viewModel.addWishList(it,
                                onSuccessAddWishlist = this::onSuccessAddWishlist,
                                onErrorAddWishList = this::onErrorAddWishList)
                        productInfo?.let {
                            dynamicProductDetailTracking.eventPDPWishlistAppsFyler(it)
                        }
                        productDetailTracking.eventPDPAddToWishlist(productInfo?.basic?.productID.toString())
                    }
                }
                if (isAffiliate && productId?.isNotEmpty() == true) {
                    productDetailTracking.eventClickWishlistOnAffiliate(
                            viewModel.userId,
                            productId!!
                    )
                }
            }
        } else {
            productDetailTracking.eventPDPAddToWishlistNonLogin(productInfo?.basic?.productID.toString())
            context?.run {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
    }

    override fun onDiscussionClicked() {
        productDetailTracking.eventTalkClicked()

        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConst.PRODUCT_TALK, viewModel.getDynamicProductInfoP1?.basic?.productID
                    ?: "")
            startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_TALK_PRODUCT)
        }
        viewModel.getDynamicProductInfoP1?.run {
            dynamicProductDetailTracking.eventDiscussionClickedIris(this, deeplinkUrl, viewModel.shopInfo?.shopCore?.name
                    ?: "")
            dynamicProductDetailTracking.sendMoEngageClickDiskusi(this, viewModel.shopInfo?.shopCore?.name
                    ?: "")
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
        bottomSheet.show(fragmentManager, "pdp_bs")
    }

    private fun renderPageError(t: Throwable) {
        context?.let { ctx ->
            dynamicAdapter.clearAllElements()
            dynamicAdapter.addElement(ErrorHelper.getErrorType(ctx, t))
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

    private fun onTradeinClicked() {
        goToNormalCheckout(TRADEIN_BUY)
        if (viewModel.tradeInParams.usedPrice > 0)
            productDetailTracking.trackTradeinAfterDiagnotics()
        else
            productDetailTracking.trackTradeinBeforeDiagnotics()
    }

    private fun openCategory(categoryId: String) {
        if (GlobalConfig.isCustomerApp()) {
            RouteManager.route(context,
                    ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL,
                    categoryId)
        }
    }

    private fun gotoEtalase(etalaseId: String, shopID: Int) {
        val intent = RouteManager.getIntent(context, if (etalaseId.isNotBlank()) {
            UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, shopID.toString(), etalaseId)
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopID.toString())
        })
        startActivity(intent)
    }

    private fun onSuccessGetProductVariantInfo(data: ProductVariant?) {
        if (data == null || !data.hasChildren) {
            dynamicAdapter.clearElement(pdpHashMapUtil.productVariantInfoMap)
            return
        }

        // defaulting selecting variant
        if (userInputVariant == data.parentId.toString() && data.defaultChild > 0) {
            userInputVariant = data.defaultChild.toString()
        }
        val selectedVariantListString = data.getOptionListString(userInputVariant)?.joinToString(separator = ", ")
                ?: ""
        pdpHashMapUtil.updateVariantInfo(data, selectedVariantListString)
    }

    private fun showAddToCartDoneBottomSheet(successMessage: String) {
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
            addToCartDoneBottomSheet.show(
                    fragmentManager, "TAG"
            )
        }
    }

    private fun updateCartNotification() {
        viewModel.updateCartCounerUseCase(::onSuccessUpdateCartCounter)
    }

    private fun onSuccessUpdateCartCounter(count: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART);
        cache.putInt(CartConstant.IS_HAS_CART, if (count > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, count);
        cache.applyEditor();
        if (isAdded) {
            initToolBarMethod()
        }
    }

    private fun onErrorSubmitHelpTicket(e: Throwable?) {
        hideProgressDialog()
        view?.also {
            Toaster.showError(it, ErrorHandler.getErrorMessage(context, e), BaseToaster.LENGTH_SHORT)
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
                Toaster.showError(it, result.message, BaseToaster.LENGTH_SHORT)
            }
        }
    }


    private fun shareProduct() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.let { productInfo ->
                productId?.let {
                    productDetailTracking.eventClickPdpShare(it)
                }

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
                    productDetailTracking.sendMoEngagePDPReferralCodeShareEvent()
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
        val productShare = ProductShare(activity!!, ProductShare.MODE_TEXT)
        productShare.share(productData, {
            showProgressDialog()
        }, {
            hideProgressDialog()
        })
    }

    /**
     * Event than happen after owner successfully move the warehoused product back to etalase
     */
    private fun onSuccessMoveToEtalase() {
        hideProgressDialog()
        showToastSuccess(getString(R.string.success_move_etalase))
        loadProductData(true)
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
        loadProductData(true)
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
        productId?.let {
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
                            if (viewModel.multiOrigin.isFulfillment)
                                viewModel.multiOrigin.origin else null,
                            productInfo.data.isFreeOngkir.isActive
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
            menuCart.isVisible = false
            menuCart.isEnabled = false
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
                productDetailTracking.eventClickAffiliate(viewModel.userId, productInfo.basic.getShopId(),
                        pdpAffiliate.productId.toString(), isRegularPdp)
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
                dynamicProductDetailTracking.eventEnhanceEcommerceProductDetail(trackerListName, productInfo, shopInfo, trackerAttribution,
                        isElligible, viewModel.tradeInParams.usedPrice > 0, viewModel.multiOrigin.isFulfillment, deeplinkUrl)
                return
            }
        }
        delegateTradeInTracking = true
    }

    private fun openFtInstallmentBottomSheet(installmentData: FinancingDataResponse) {

        productDetailTracking.eventClickPDPInstallmentSeeMore(productId)

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

    private fun goToNormalCheckout(@ProductAction action: Int = ATC_AND_BUY) {
        context?.let {
            val shopInfo = viewModel.shopInfo
            viewModel.getDynamicProductInfoP1?.let {
                val isOcsCheckoutType = (viewModel.p2Login.value)?.isOcsCheckoutType
                        ?: false
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, it.getProductName)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, it.data.price.value)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_CONDITION, it.basic.condition)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, it.basic.category.id)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, it.basic.category.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, it.basic.shopID)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, it.parentProductId)
                    putExtra(ApplinkConst.Transaction.EXTRA_NOTES, userInputNotes)
                    putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, userInputQuantity)
                    putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, userInputVariant)
                    putExtra(ApplinkConst.Transaction.EXTRA_ACTION, action)
                    putExtra(ApplinkConst.Transaction.TRACKER_ATTRIBUTION, trackerAttribution)
                    putExtra(ApplinkConst.Transaction.TRACKER_LIST_NAME, trackerListName)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_TYPE, shopInfo?.goldOS?.shopTypeString)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopInfo?.shopCore?.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_OCS, isOcsCheckoutType)
                    putExtra(ApplinkConst.Transaction.EXTRA_IS_LEASING, it.basic.isLeasing)
                }
                intent.putExtra(ApplinkConst.Transaction.EXTRA_TRADE_IN_PARAMS, viewModel.tradeInParams)
                startActivityForResult(intent,
                        ProductDetailConstant.REQUEST_CODE_NORMAL_CHECKOUT)
            }
        }
    }

    private fun goToAtcExpress() {
        activity?.let {
            try {
                val productInfo = viewModel.getDynamicProductInfoP1 ?: DynamicProductInfoP1()
                val warehouseId: Int = viewModel.multiOrigin.id.toIntOrZero()
                val atcRequestParam = AtcRequestParam()
                atcRequestParam.setShopId(productInfo.basic.getShopId())
                atcRequestParam.setProductId(productInfo.basic.getProductId())
                atcRequestParam.setNotes(userInputNotes)
                val qty = if (userInputQuantity == 0) productInfo.basic.minOrder else userInputQuantity
                atcRequestParam.setQuantity(qty)
                atcRequestParam.setWarehouseId(warehouseId)

                val expressCheckoutUriString = ApplinkConstInternalMarketplace.EXPRESS_CHECKOUT
                val intent = RouteManager.getIntent(it, expressCheckoutUriString)
                intent?.run {
                    putExtra(Constant.EXTRA_ATC_REQUEST, atcRequestParam)
                    putExtra(Constant.TRACKER_ATTRIBUTION, trackerAttribution)
                    putExtra(Constant.TRACKER_LIST_NAME, trackerListName)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_ATC_EXPRESS)
                    it.overridePendingTransition(R.anim.pull_up, 0)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun doBuy() {
        val isExpressCheckout = (viewModel.p2Login.value)?.isExpressCheckoutType
                ?: false
        if (isExpressCheckout) {
            if (viewModel.isUserSessionActive) {
                goToAtcExpress()
            } else {
                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            ProductDetailConstant.REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS)
                }
            }
        } else {
            goToNormalCheckout()
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
        pdpHashMapUtil.snapShotMap.isWishlisted = false
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        sendIntentResultWishlistChange(productId ?: "", false)
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        pdpHashMapUtil.snapShotMap.isWishlisted = true
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        dynamicProductDetailTracking.eventBranchAddToWishlist(viewModel.getDynamicProductInfoP1, (UserSession(activity)).userId, pdpHashMapUtil.productInfoMap?.data?.find { content ->
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
        val id = viewModel.getDynamicProductInfoP1?.data?.variant?.parentID ?: return
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_ITEM, id)
            intent?.run { startActivityForResult(this, ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT) }
        }
    }

    private fun showToasterError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initToolbar() {
        varToolbar = search_pdp_toolbar
        initToolBarMethod = ::initToolbarLight
        activity?.let {
            varToolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        et_search.setOnClickListener {
            productDetailTracking.eventClickSearchBar()
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
        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
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
            viewModel.getDynamicProductInfoP1?.let {
                productDetailTracking.eventClickAddToCart(it.basic.productID,
                        it.data.variant.isVariant)
                goToNormalCheckout(ATC_ONLY)
            }
        }
        actionButtonView.buyNowClick = {
            // buy now / buy / preorder
            viewModel.getDynamicProductInfoP1?.let {
                productDetailTracking.eventClickBuy(it.basic.productID,
                        it.data.variant.isVariant)
                doBuy()
            }
        }
    }

    private fun onApplyLeasingClicked() {
        viewModel.getDynamicProductInfoP1?.run {
            productDetailTracking.eventClickApplyLeasing(
                    data.variant.parentID,
                    data.variant.isVariant
            )
            goToNormalCheckout(APPLY_CREDIT)
        }
    }

    private fun gotoShopDetail() {
        activity?.let {
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.shopID ?: return
            startActivityForResult(RouteManager.getIntent(it,
                    ApplinkConst.SHOP, shopId),
                    ProductDetailConstant.REQUEST_CODE_SHOP_INFO)
        }
    }

    private fun onShopFavoriteClick() {
        val shop = viewModel.shopInfo ?: return
        activity?.let {
            if (viewModel.isUserSessionActive) {
                pdpHashMapUtil.getShopInfo.toogleFavorite = false
                dynamicAdapter.notifyShopInfo(pdpHashMapUtil.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE)
                viewModel.toggleFavorite(shop.shopCore.shopID)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean) {
        val favorite = pdpHashMapUtil.getShopInfo.shopInfo?.favoriteData ?: return
        if (isSuccess) {
            val newFavorite =
                    // If was favorited then change to un-favorited
                    if (favorite.alreadyFavorited == 1)
                        ShopInfo.FavoriteData(0, favorite.totalFavorite - 1)
                    else
                        ShopInfo.FavoriteData(1, favorite.totalFavorite + 1)
            pdpHashMapUtil.getShopInfo.shopInfo = pdpHashMapUtil.getShopInfo.shopInfo?.copy(favoriteData = newFavorite)
            pdpHashMapUtil.getShopInfo.isFavorite = favorite.alreadyFavorited != 1
            pdpHashMapUtil.getShopInfo.toogleFavorite = true
            dynamicAdapter.notifyShopInfo(pdpHashMapUtil.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        context?.let {
            ToasterError.make(view, ProductDetailErrorHandler.getErrorMessage(it, t))
                    .setAction(R.string.retry_label) { onShopFavoriteClick() }
        }
        pdpHashMapUtil.getShopInfo.toogleFavorite = true
        dynamicAdapter.notifyShopInfo(pdpHashMapUtil.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
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
                viewModel.putChatProductInfoTo(intent, productId, product, userInputVariant)
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailConstant.REQUEST_CODE_LOGIN)
            }
        }
        productDetailTracking.eventSendMessage()
        productDetailTracking.eventSendChat(productId ?: "")
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
            productDetailTracking.eventCartMenuClicked(viewModel.generateVariantString(), productId
                    ?: "")
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
            ToasterError.make(findViewById(android.R.id.content),
                    ProductDetailErrorHandler.getErrorMessage(this, throwable),
                    ToasterError.LENGTH_LONG)
                    .show()
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
        getRecyclerView(view).smoothScrollToPosition(position)
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

}