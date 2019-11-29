package com.tokopedia.product.detail.view.fragment

import android.animation.Animator
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.Menu
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.*
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.view.activity.CourierActivity
import com.tokopedia.product.detail.view.activity.ProductFullDescriptionTabActivity
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.dynamicadapter.DynamicProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.DynamicProductDetailHashMap
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.product.detail.view.widget.SquareHFrameLayout
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
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
import com.tokopedia.tradein.model.TradeInParams
import com.tokopedia.tradein.view.customview.TradeInTextView
import com.tokopedia.tradein.viewmodel.TradeInBroadcastReceiver
import com.tokopedia.transaction.common.dialog.UnifyDialog
import com.tokopedia.transaction.common.sharedata.RESULT_CODE_ERROR_TICKET
import com.tokopedia.transaction.common.sharedata.RESULT_TICKET_DATA
import com.tokopedia.transaction.common.sharedata.ticket.SubmitTicketResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import kotlinx.android.synthetic.main.partial_product_detail_header.*
import javax.inject.Inject

class DynamicProductDetailFragment : BaseListFragment<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>(), DynamicProductDetailListener {

    companion object {
        const val PAYLOAD_TOOGLE_FAVORITE = 2
        const val PAYLOAD_TOOGLE_AND_FAVORITE_SHOP = 3
    }

    private var shouldShowCartAnimation = false
    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var tradeInParams: TradeInParams
    private lateinit var tradeInBroadcastReceiver: TradeInBroadcastReceiver

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

    private var userInputNotes = ""
    private var userInputQuantity = 0
    private var userInputVariant: String? = null
    private var delegateTradeInTracking = false
    private var trackerAttribution: String? = ""
    private var trackerListName: String? = ""

    //View
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(this) }
    private val dynamicAdapter by lazy { DynamicProductDetailAdapter(adapterFactory) }
    private var menu: Menu? = null
    private lateinit var varToolbar: Toolbar
    private lateinit var actionButtonView: PartialButtonActionView
    private lateinit var stickyLoginView: StickyLoginView
    private lateinit var pdpHashMapUtil: DynamicProductDetailHashMap
    private var loadingProgressDialog: ProgressDialog? = null
    val errorBottomsheets: ErrorBottomsheets by lazy {
        ErrorBottomsheets()
    }

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
        initializePartialView()
        initView()
        initializeSearchToolbar()
        initializeStickyLogin(view)
        initActionButton()
        getRecyclerView(view).itemAnimator = null

        tradeInBroadcastReceiver = TradeInBroadcastReceiver()
        tradeInBroadcastReceiver.setBroadcastListener {
            if (it) {
                if (tv_trade_in_promo != null) {
                    pdpHashMapUtil.snapShotMap.shouldShowTradein = true
                }
            } else {
                dynamicAdapter.removeTradeinSection(pdpHashMapUtil.productTradeinMap)
            }
            trackProductView(it)
        }

        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(tradeInBroadcastReceiver, IntentFilter(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE))
        }

        if (isAffiliate) {
            actionButtonView.gone()
            base_btn_affiliate_dynamic.visible()
            loadingAffiliateDynamic.visible()
        }
    }

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        actionButtonView.visibility = false
        updateStickyContent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        activity?.run {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }
    }

    override fun onStop() {
        super.onStop()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(tradeInBroadcastReceiver)
        }
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
        loadProductData(true)
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
                    getRecyclerView(view).addOnScrollListener(onScrollListener)
                    pdpHashMapUtil = DynamicProductDetailHashMap(DynamicProductDetailMapper.hashMapLayout(it.data))
                    renderList(it.data)
                }
                is Fail -> {
                    actionButtonView.visibility = false
                    onGetListErrorWithEmptyData(it.throwable)
                    showToasterError(it.throwable.message ?: "")
                }
            }
        })

        viewModel.dynamicProductInfoP1.observe(this, Observer {
            when (it) {
                is Success -> {
                    val productP1Data = it.data
                    renderTradein(productP1Data)
                    shouldShowCodP1 = productP1Data.data.isCOD
                    pdpHashMapUtil.updateDataP1Test(it.data)
                    dynamicAdapter.notifyDataSetChanged()
                }
                is Fail -> {
                    showToasterError(it.throwable.message ?: "")
                }
            }
        })

        viewModel.productInfoP1.observe(this, Observer {
            when (it) {
                is Success -> {
//                    renderTradein(it.data)
                    shouldShowCodP1 = it.data.productInfo.shouldShowCod
//                    pdpHashMapUtil.updateDataP1(it.data)

                    actionButtonView.isLeasing = it.data.productInfo.basic.isLeasing
                    it.data.productInfo.let { data ->
                        //                        actionButtonView.renderData(!data.basic.isActive(),
//                                (viewModel.isShopOwner(data.basic.shopID)
//                                        || pdpHashMapUtil.snapShotMap.shopInfo?.allowManage == true),
//                                data.preorder)
                    }
                    actionButtonView.visibility = !isAffiliate

                    if (affiliateString.hasValue()) {
                        viewModel.hitAffiliateTracker(affiliateString
                                ?: "", viewModel.deviceId)
                    }

                    activity?.invalidateOptionsMenu()
                    adapter.notifyDataSetChanged()
                }
                is Fail -> {
                    showToasterError(it.throwable.message ?: "")
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

            pdpHashMapUtil.updateDataP2Login(it)
            it.pdpAffiliate?.let { renderAffiliate(it) }
            dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOADS_WISHLIST)

        })

        viewModel.p2ShopDataResp.observe(this, Observer {
            if (!viewModel.isUserSessionActive() && ::performanceMonitoringFull.isInitialized)
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



//            viewModel.getDynamicProductInfoP1?.let { data ->
//                pdpHashMapUtil.getShopInfo.shopInfo?.let { shopInfo ->
//                    productDetailTracking.sendMoEngageOpenProduct(data,
//                            shopInfo.goldOS.isOfficial == 1, shopInfo.shopCore.name)
//                    productDetailTracking.eventAppsFylerOpenProduct(data)

//                    productDetailTracking.sendScreen(
//                            shopInfo.shopCore.shopID,
//                            shopInfo.goldOS.shopTypeString,
//                            productId ?: "")
//
//                }
//            }


            if (delegateTradeInTracking) {
                trackProductView(tradeInParams.isEligible == 1)
                delegateTradeInTracking = false
            }

            pdpHashMapUtil.updateDataP2Shop(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.p2General.observe(this, Observer {
            performanceMonitoringP2General.stopTrace()

            if (it.latestTalk.id.isEmpty()) {
                dynamicAdapter.removeDiscussionSection(pdpHashMapUtil.productDiscussionMap)
            }

            if (it.imageReviews.isEmpty()) {
                dynamicAdapter.removeImageReviewSection(pdpHashMapUtil.productImageReviewMap)
            }

            if (it.helpfulReviews.isEmpty()) {
                dynamicAdapter.removeMostHelpfulReviewSection(pdpHashMapUtil.productMostHelpfulMap)
            }

            onSuccessGetProductVariantInfo(it.variantResp)

            pdpHashMapUtil.updateDataP2General(it)

//            viewModel.getProductInfoP1?.run {
//                productDetailTracking.eventBranchItemView(this, viewModel.userId)
//            }

            adapter.notifyDataSetChanged()
        })

        viewModel.productInfoP3resp.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()

            shouldShowCodP3 = it.userCod
            pdpHashMapUtil.snapShotMap.shouldShowCod =
                    shouldShowCodP1 && shouldShowCodP2Shop && shouldShowCodP3

            dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOADS_COD)
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
                        val createTicketDialog = UnifyDialog(activity, UnifyDialog.HORIZONTAL_ACTION, UnifyDialog.NO_HEADER)
                        createTicketDialog.apply {
                            setTitle(result.errorReporter.texts.submitTitle)
                            setDescription(result.errorReporter.texts.submitDescription)
                            setSecondary(result.errorReporter.texts.cancelButton)
                            setSecondaryOnClickListener(View.OnClickListener {
                                this.dismiss()
                                productDetailTracking.eventClickCloseOnHelpPopUpAtc()
                            })
                            setOk(result.errorReporter.texts.submitButton)
                            setOkOnClickListener(View.OnClickListener {
                                this.dismiss()
                                productDetailTracking.eventClickReportOnHelpPopUpAtc()
                                showProgressDialog()
                                viewModel.hitSubmitTicket(result, this@DynamicProductDetailFragment::onErrorSubmitHelpTicket, this@DynamicProductDetailFragment::onSuccessSubmitHelpTicket)
                            })
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
                            viewModel.productInfoP1.value = Success(ProductInfoP1().apply { productInfo = selectedProductInfo })
                            selectedWarehouse?.let {
                                viewModel.multiOrigin = it.warehouseInfo
                                viewModel.p2ShopDataResp.value = viewModel.p2ShopDataResp.value?.copy(
                                        nearestWarehouse = it
                                )
                            }

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
            ProductDetailFragment.REQUEST_CODE_ATC_EXPRESS -> {
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
            ProductDetailFragment.REQUEST_CODE_EDIT_PRODUCT -> {
                loadProductData(true)
            }
            ProductDetailFragment.REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS -> {
                doBuy()
            }
            ProductDetailFragment.REQUEST_CODE_REPORT -> {
                if (resultCode == Activity.RESULT_OK)
                    showToastSuccess(getString(R.string.success_to_report))
            }
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        viewModel.productInfoP1.removeObservers(this)
        viewModel.p2ShopDataResp.removeObservers(this)
        viewModel.p2General.removeObservers(this)
        viewModel.p2Login.removeObservers(this)
        viewModel.productInfoP3resp.removeObservers(this)
        viewModel.loadTopAdsProduct.removeObservers(this)
        viewModel.moveToWarehouseResult.removeObservers(this)
        viewModel.moveToEtalaseResult.removeObservers(this)
        viewModel.clear()
        super.onDestroy()
    }

    override fun openCategory(category: Category.Detail) {
        if (GlobalConfig.isCustomerApp()) {
            RouteManager.route(context,
                    ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL,
                    category.id)
        }
    }

    override fun gotoEtalase(etalaseId: String, shopID: Int) {
        val intent = RouteManager.getIntent(context, if (etalaseId.isNotBlank()) {
            UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, shopID.toString(), etalaseId)
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopID.toString())
        })
        startActivity(intent)
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
        if (viewModel.isUserSessionActive()) {
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
    override fun onDescriptioInfonClicked(name: String) {
        when (name) {
            "shipping_info" -> {
            }
            "cicilan_info" -> {
            }
            "variant" -> {
            }
            "wholesale" -> {
            }
        }
    }

    override fun onInfoClicked(name: String) {
        when (name) {
            "shipping_info" -> {
            }
            "cicilan_info" -> {
            }
            "variant" -> {
            }
            "wholesale" -> {
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
                recomItem, position, viewModel.isUserSessionActive(), pageName, title)
    }

    override fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String) {
        productDetailTracking.eventRecommendationImpression(
                position, recomItem, viewModel.isUserSessionActive(), pageName, title)
    }

    /**
     * ProductOpenShopViewHolder Listener
     */
    override fun openShopClicked() {
        activity?.let {
            if (viewModel.isUserSessionActive()) {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.OPEN_SHOP)
                        ?: return@let
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailFragment.REQUEST_CODE_LOGIN)
            }
        }
    }

    /**
     * ProductImageReviewViewHolder Listener
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
//            productDetailTracking.sendMoEngageClickReview(this, pdpHashMapUtil.snapShotMap.shopInfo?.goldOS?.isOfficial == 1, pdpHashMapUtil.snapShotMap.shopInfo?.shopCore?.name
//                    ?: "")
            context?.let {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_REVIEW, basic.productID)
                intent?.run {
                    intent.putExtra("x_prd_nm", basic.name)
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * ProductMostHelpfulReviewViewHolder
     */
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
                startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
            }
        }
    }

    override fun onSeeAllMerchantVoucherClick() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                productDetailTracking.eventClickMerchantVoucherSeeAll(basic.getProductId())

                val intent = MerchantVoucherListActivity.createIntent(it, basic.shopID,
                        pdpHashMapUtil.shopInfoMap?.shopInfo?.shopCore?.name)
                startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_MERCHANT_VOUCHER)
            }
        }
    }

    /**
     * ProductTradeinViewHolder
     */
    private fun showSnackbarClose(string: String) {
        view?.let {
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).apply {
                setAction(getString(R.string.close)) { dismiss() }
                setActionTextColor(Color.WHITE)
            }.show()
        }
    }

    override fun onTradeinClicked(tradeInParams: TradeInParams) {
        goToNormalCheckout(TRADEIN_BUY)
        if (tradeInParams.usedPrice > 0)
            productDetailTracking.sendGeneralEvent(" clickPDP",
                    "product detail page",
                    "click trade in widget",
                    "after diagnostic")
        else
            productDetailTracking.sendGeneralEvent(" clickPDP",
                    "product detail page",
                    "click trade in widget",
                    "before diagnostic")
    }

    /**
     * ProductSnapshotViewHolder
     */
    override fun onImageClicked(position: Int) {
        startActivity(ImagePreviewActivity.getCallingIntent(context!!,
                viewModel.getImageUriPaths(),
                null,
                position))
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
        val shopInfo = pdpHashMapUtil.getShopInfo.shopInfo
        val productInfo = viewModel.getDynamicProductInfoP1
        if (viewModel.isUserSessionActive()) {
            val productP3value = viewModel.productInfoP3resp.value
            if (shopInfo != null && shopInfo?.isAllowManage == 1) {
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
                            //                            productDetailTracking.eventPDPWishlistAppsFyler(it)
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
                        ProductDetailFragment.REQUEST_CODE_LOGIN)
            }
        }
    }

    override fun onDiscussionClicked() {
        productDetailTracking.eventTalkClicked()

        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConst.PRODUCT_TALK, pdpHashMapUtil.productInfoMap?.productInfo?.basic?.id.toString())
            startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_TALK_PRODUCT)
        }
        pdpHashMapUtil.productInfoMap?.productInfo?.run {
            productDetailTracking.sendMoEngageClickDiskusi(this,
                    (pdpHashMapUtil.getShopInfo.shopInfo?.goldOS?.isOfficial ?: 0) > 0,
                    pdpHashMapUtil.getShopInfo.shopInfo?.shopCore?.name ?: "")
        }
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
            val productName = it.basic.name
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
                val successTicketDialog = UnifyDialog(it, UnifyDialog.SINGLE_ACTION, UnifyDialog.NO_HEADER)
                successTicketDialog.apply {
                    setTitle(result.texts.submitTitle)
                    setDescription(result.texts.submitDescription)
                    setOk(result.texts.successButton)
                    setOkOnClickListener(View.OnClickListener {
                        this.dismiss()
                    })
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
                        MethodChecker.fromHtml(productInfo.basic.name).toString(),
                        productInfo.data.price.currency,
                        productInfo.basic.url,
                        pdpHashMapUtil.getShopInfo.shopInfo?.shopCore?.url ?: "",
                        pdpHashMapUtil.getShopInfo.shopInfo?.shopCore?.name ?: "",
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
            if (viewModel.isUserSessionActive()) {
                context?.let {
                    val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.REPORT_PRODUCT,
                            basic.productID)
                    startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_REPORT)
                }

                productDetailTracking.eventReportLogin()
            } else {
                context?.run {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            ProductDetailFragment.REQUEST_CODE_LOGIN)
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
                startActivityForResult(shopEtalasePickerIntent, ProductDetailFragment.REQUEST_CODE_ETALASE)
            }
        }
    }

    private fun loadProductData(forceRefresh: Boolean = false) {
        productId = "14285903"
        if (productId != null || (productKey != null && shopDomain != null)) {
            viewModel.getProductP1(ProductParams(productId, shopDomain, productKey), true)
        }
    }


    private fun gotoRateEstimation() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            pdpHashMapUtil.getShopInfo.shopInfo?.let { shopInfo ->
                context?.let { context ->
                    startActivity(RatesEstimationDetailActivity.createIntent(
                            context,
                            shopInfo.shopCore.domain,
//                            productInfo.basic.weight,
//                            productInfo.basic.weightUnit,
                            0F, "0",
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
                pdpHashMapUtil.getShopInfo.shopInfo?.let { shopInfo ->
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
            rv_pdp.addItemDecoration(DynamicPdpDividerItemDecoration(it))
        }

    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (::pdpHashMapUtil.isInitialized) {
                pdpHashMapUtil.listProductRecomMap?.first()?.let {
                    val positionShouldLoading = getRecyclerView(view).layoutManager as LinearLayoutManager
                    if (it.position != -1 && positionShouldLoading.findLastVisibleItemPosition() == it.position - 4) {
                        viewModel.loadRecommendation()
                        getRecyclerView(view).removeOnScrollListener(this)
                    }
                }
            }
        }
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
            actionButtonView.visibility = pdpHashMapUtil.shopInfoMap?.shopInfo?.statusInfo?.shopStatus == 1
        }
    }

    private fun onAffiliateClick(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate,
                                 isRegularPdp: Boolean) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            activity?.let {
                productDetailTracking.eventClickAffiliate(viewModel.userId, productInfo.basic.getShopId(),
                        pdpAffiliate.productId.toString(), isRegularPdp)
                if (viewModel.isUserSessionActive()) {
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
                            ProductDetailFragment.REQUEST_CODE_LOGIN)
                }
            }
        }
        return
    }

    private fun trackProductView(isElligible: Boolean) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            pdpHashMapUtil.shopInfoMap?.shopInfo?.let { shopInfo ->
                //                productDetailTracking.eventEnhanceEcommerceProductDetail(trackerListName, productInfo, shopInfo, trackerAttribution,
//                        isElligible, tradeInParams.usedPrice > 0, viewModel.multiOrigin.isFulfillment)
                return
            }
        }
        delegateTradeInTracking = true
    }


    private fun renderTradein(productInfoP1: DynamicProductInfoP1) {
        tradeInParams = TradeInParams()
        tradeInParams.categoryId = productInfoP1.basic.category.id.toIntOrZero()
        tradeInParams.deviceId = (activity?.application as ProductDetailRouter).getDeviceId(activity as Context)
        tradeInParams.userId = if (viewModel.userId.isNotEmpty())
            viewModel.userId.toIntOrZero()
        else
            0
        tradeInParams.setPrice(productInfoP1.data.price.value)
        tradeInParams.productId = productInfoP1.basic.getProductId()
        tradeInParams.shopId = productInfoP1.basic.getShopId()
        tradeInParams.productName = productInfoP1.basic.name

        tradeInParams.isPreorder = productInfoP1.data.preOrder.isPreOrderActive()
        tradeInParams.isOnCampaign = productInfoP1.data.campaign.isActive

        pdpHashMapUtil.productTradeinMap?.let {
            it.tradeInParams = tradeInParams
        }
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoringP1 = PerformanceMonitoring.start(ProductDetailConstant.PDP_P1_TRACE)
        performanceMonitoringP2 = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_TRACE)
        performanceMonitoringP2General = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_GENERAL_TRACE)

        if (viewModel.isUserSessionActive()) {
            performanceMonitoringP2Login = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_LOGIN_TRACE)
            performanceMonitoringFull = PerformanceMonitoring.start(ProductDetailConstant.PDP_P3_TRACE)
        }
    }

    private fun initActionButton() {
        actionButtonView.promoTopAdsClick = {
            pdpHashMapUtil.shopInfoMap?.shopInfo.let { shopInfo ->
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

    private fun goToNormalCheckout(@ProductAction action: Int = ATC_AND_BUY) {
        context?.let {
            val shopInfo = pdpHashMapUtil.shopInfoMap?.shopInfo
            viewModel.getDynamicProductInfoP1?.let {
                val isOcsCheckoutType = (viewModel.p2Login.value)?.isOcsCheckoutType
                        ?: false
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, it.basic.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, it.data.price.value)
//                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_CONDITION, basic.condition)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, it.basic.category.id)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, it.basic.category.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, it.basic.shopID)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, it.data.variant.parentID)
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
                if (::tradeInParams.isInitialized) {
                    intent.putExtra(ApplinkConst.Transaction.EXTRA_TRADE_IN_PARAMS, tradeInParams)
                }
                startActivityForResult(intent,
                        ProductDetailFragment.REQUEST_CODE_NORMAL_CHECKOUT)
            }
        }
    }


    private fun goToAtcExpress() {
        activity?.let {
            try {
                val productInfo = (viewModel.productInfoP1.value as Success).data
                val warehouseId: Int = viewModel.multiOrigin.id.toIntOrZero()
                val atcRequestParam = AtcRequestParam()
                atcRequestParam.setShopId(productInfo.productInfo.basic.shopID)
                atcRequestParam.setProductId(productInfo.productInfo.basic.id)
                atcRequestParam.setNotes(userInputNotes)
                val qty = if (userInputQuantity == 0) productInfo.productInfo.basic.minOrder else userInputQuantity
                atcRequestParam.setQuantity(qty)
                atcRequestParam.setWarehouseId(warehouseId)

                val expressCheckoutUriString = ApplinkConstInternalMarketplace.EXPRESS_CHECKOUT
                val intent = RouteManager.getIntent(it, expressCheckoutUriString)
                intent?.run {
                    putExtra(Constant.EXTRA_ATC_REQUEST, atcRequestParam)
                    putExtra(Constant.TRACKER_ATTRIBUTION, trackerAttribution)
                    putExtra(Constant.TRACKER_LIST_NAME, trackerListName)
                    startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_ATC_EXPRESS)
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
            if (viewModel.isUserSessionActive()) {
                goToAtcExpress()
            } else {
                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            ProductDetailFragment.REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS)
                }
            }
        } else {
            goToNormalCheckout()
        }
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        pdpHashMapUtil.snapShotMap.isWishlisted = false
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOADS_WISHLIST)
        sendIntentResultWishlistChange(productId ?: "", false)
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        pdpHashMapUtil.snapShotMap.isWishlisted = true
        dynamicAdapter.notifySnapshotWithPayloads(pdpHashMapUtil.snapShotMap, ProductDetailConstant.PAYLOADS_WISHLIST)
//        productDetailTracking.eventBranchAddToWishlist(pdpHashMapUtil.snapShotMap.productInfoP1, (UserSession(activity)).userId)
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
            intent?.run { startActivityForResult(this, ProductDetailFragment.REQUEST_CODE_EDIT_PRODUCT) }
        }
    }

    private fun showToasterError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        varToolbar = search_pdp_toolbar
        initToolBarMethod = ::initToolbarLight
        activity?.let {
            varToolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initializeStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_pdp)
        stickyLoginView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateStickyState()
        }
        stickyLoginView.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), ProductDetailFragment.REQUEST_CODE_LOGIN)
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
        viewModel.getStickyLoginContent(
                onSuccess = {
                    this.tickerDetail = it
                    updateStickyState()
                    updateActionButtonShadow()
                },
                onError = {
                    stickyLoginView.visibility = View.GONE
                }
        )
    }

    private fun initializeSearchToolbar() {
        et_search.setOnClickListener {
            productDetailTracking.eventClickSearchBar()
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }
        et_search.hint = String.format(getString(R.string.pdp_search_hint), "")
    }

    private fun initializePartialView() {
        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
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
                    ProductDetailFragment.REQUEST_CODE_SHOP_INFO)
        }
    }

    private fun onShopFavoriteClick() {
        val shop = pdpHashMapUtil.getShopInfo.shopInfo ?: return
        activity?.let {
            if (viewModel.isUserSessionActive()) {
                pdpHashMapUtil.getShopInfo.toogleFavorite = false
                dynamicAdapter.notifyShopInfo(pdpHashMapUtil.getShopInfo, PAYLOAD_TOOGLE_FAVORITE)
                viewModel.toggleFavorite(shop.shopCore.shopID,
                        this::onSuccessFavoriteShop, this::onFailFavoriteShop)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailFragment.REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean) {
        val favorite = pdpHashMapUtil.getShopInfo.shopInfo?.favoriteData ?: return
        if (isSuccess) {
            val newFavorite =
                    if (favorite.alreadyFavorited == 1)
                        ShopInfo.FavoriteData(0, favorite.totalFavorite - 1)
                    else
                        ShopInfo.FavoriteData(1, favorite.totalFavorite + 1)
            pdpHashMapUtil.getShopInfo.shopInfo = pdpHashMapUtil.getShopInfo.shopInfo?.copy(favoriteData = newFavorite)
            pdpHashMapUtil.getShopInfo.isFavorite = favorite.alreadyFavorited != 1
            pdpHashMapUtil.getShopInfo.toogleFavorite = true
            dynamicAdapter.notifyShopInfo(pdpHashMapUtil.getShopInfo, PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
        }
    }


    private fun onFailFavoriteShop(t: Throwable) {
        context?.let {
            ToasterError.make(view, ProductDetailErrorHandler.getErrorMessage(it, t))
                    .setAction(R.string.retry_label) { onShopFavoriteClick() }
        }
        pdpHashMapUtil.getShopInfo.toogleFavorite = true
        dynamicAdapter.notifyShopInfo(pdpHashMapUtil.getShopInfo, PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
    }


    private fun onShopChatClicked() {
        val shop = pdpHashMapUtil.shopInfoMap?.shopInfo ?: return
        val product = viewModel.getDynamicProductInfoP1 ?: return
        activity?.let {
            if (viewModel.isUserSessionActive()) {
                val intent = RouteManager.getIntent(it,
                        ApplinkConst.TOPCHAT_ASKSELLER,
                        shop.shopCore.shopID, "",
                        "product", shop.shopCore.name, shop.shopAssets.avatar)
                viewModel.putChatProductInfoTo(intent, productId, product, userInputVariant)
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        ProductDetailFragment.REQUEST_CODE_LOGIN)
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
            if (viewModel.isUserSessionActive()) {
                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        ProductDetailFragment.REQUEST_CODE_LOGIN)
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
                badge.setCount(if (cartCount > ProductDetailFragment.CART_MAX_COUNT) {
                    getString(R.string.pdp_label_cart_count_max)
                } else {
                    cartCount.toString()
                })
                icon.mutate()
                icon.setDrawableByLayerId(R.id.ic_cart_count, badge)
                cartImageView.setImageDrawable(icon)
                if (animate) {
                    val alphaAnimation = AlphaAnimation(ProductDetailFragment.CART_ALPHA_ANIMATION_FROM, ProductDetailFragment.CART_ALPHA_ANIMATION_TO)
                    val scaleAnimation = ScaleAnimation(ProductDetailFragment.CART_SCALE_ANIMATION_FROM, ProductDetailFragment.CART_SCALE_ANIMATION_TO, ProductDetailFragment.CART_SCALE_ANIMATION_FROM, ProductDetailFragment.CART_SCALE_ANIMATION_TO, Animation.RELATIVE_TO_SELF, ProductDetailFragment.CART_SCALE_ANIMATION_PIVOT, Animation.RELATIVE_TO_SELF, ProductDetailFragment.CART_SCALE_ANIMATION_PIVOT)
                    scaleAnimation.fillAfter = false
                    val animationSet = AnimationSet(false)
                    animationSet.addAnimation(alphaAnimation)
                    animationSet.addAnimation(scaleAnimation)
                    animationSet.duration = ProductDetailFragment.CART_ANIMATION_DURATION
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