package com.tokopedia.product.detail.view.fragment

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.view.activity.ProductFullDescriptionTabActivity
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.DynamicProductDetailHashMap
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.widget.SquareHFrameLayout
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.tradein.model.TradeInParams
import com.tokopedia.tradein.view.customview.TradeInTextView
import com.tokopedia.tradein.viewmodel.TradeInBroadcastReceiver
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.et_search
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.search_pdp_toolbar
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import kotlinx.android.synthetic.main.partial_product_detail_header.*
import javax.inject.Inject

class DynamicProductDetailFragment : BaseListFragment<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>(), DynamicProductDetailListener {

    companion object {

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

    private var userInputNotes = ""
    private var userInputQuantity = 0
    private var userInputVariant: String? = null
    private var delegateTradeInTracking = false
    private var trackerAttribution: String? = ""
    private var trackerListName: String? = ""


    //View
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(::onPictureProductClicked, childFragmentManager, this) }
    private var menu: Menu? = null
    private lateinit var varToolbar: Toolbar
    private lateinit var actionButtonView: PartialButtonActionView
    private lateinit var stickyLoginView: StickyLoginView
    private lateinit var pdpHashMapUtil: DynamicProductDetailHashMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dynamic_product_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePartialView()
        initView()
        initializeSearchToolbar()
        initializeStickyLogin(view)

        if (isAffiliate) {
            actionButtonView.gone()
            base_btn_affiliate.visible()
            loadingAffiliate.visible()
        }

        tradeInBroadcastReceiver = TradeInBroadcastReceiver()
        tradeInBroadcastReceiver.setBroadcastListener {
            if (it) {
                if (tv_trade_in_promo != null) {
                    pdpHashMapUtil.snapShotMap.shouldShowTradein = true
                }
            } else {
                removeTradeinSection()
            }
            trackProductView(it)
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(tradeInBroadcastReceiver, IntentFilter(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE))
        }

        initActionButton()
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

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_pdp
    }

    override fun getAdapterTypeFactory(): DynamicProductDetailAdapterFactoryImpl = adapterFactory

    override fun onItemClicked(t: DynamicPDPDataModel) {
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
        productId = "15266959"
        if (productId != null || (productKey != null && shopDomain != null)) {
            viewModel.getProductP1(ProductParams(productId, shopDomain, productKey), true)
        }
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
                    pdpHashMapUtil = DynamicProductDetailHashMap(DynamicProductDetailMapper.hashMapLayout(it.data))
                    renderList(it.data)
                }
                is Fail -> {
                    showToasterError(it.throwable.message ?: "")
                }
            }
        })

        viewModel.productInfoP1.observe(this, Observer {
            when (it) {
                is Success -> {
                    renderTradein(it.data)
                    shouldShowCodP1 = it.data.productInfo.shouldShowCod
                    pdpHashMapUtil.updateDataP1(it.data)
                    adapter.notifyDataSetChanged()
                }
                is Fail -> {
                    showToasterError(it.throwable.message ?: "")
                }
            }
        })

        viewModel.p2Login.observe(this, Observer {
            Toast.makeText(context, "${it.cartType}", Toast.LENGTH_LONG).show()
        })

        viewModel.p2ShopDataResp.observe(this, Observer {
            shouldShowCodP2Shop = it.shopCod
            pdpHashMapUtil.updateDataP2Shop(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.p2General.observe(this, Observer {
            if (it.latestTalk.id.isEmpty()) {
                removeDiscussionSection()
            }

            if (it.imageReviews.isEmpty()) {
                removeImageReviewSection()
            }

            if (it.helpfulReviews.isEmpty()) {
                removeMostHelpfulReviewSection()
            }

            pdpHashMapUtil.updateDataP2General(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.productInfoP3resp.observe(this, Observer {
            shouldShowCodP3 = it.userCod
            pdpHashMapUtil.snapShotMap.shouldShowCod =
                    shouldShowCodP1 && shouldShowCodP2Shop && shouldShowCodP3
            adapter.notifyDataSetChanged()
        })
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

    override fun onDiscussionClicked() {
        productDetailTracking.eventTalkClicked()

        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConst.PRODUCT_TALK, pdpHashMapUtil.productInfoMap.productInfo?.basic?.id.toString())
            startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_TALK_PRODUCT)
        }
        pdpHashMapUtil.productInfoMap.productInfo?.run {
            productDetailTracking.sendMoEngageClickDiskusi(this,
                    (pdpHashMapUtil.productInfoMap.shopInfo?.goldOS?.isOfficial ?: 0) > 0,
                    pdpHashMapUtil.productInfoMap.shopInfo?.shopCore?.name ?: "")
        }
    }

    private fun trackProductView(isElligible: Boolean) {
        pdpHashMapUtil.snapShotMap.productInfoP1?.let { productInfo ->
            pdpHashMapUtil.snapShotMap.shopInfo?.let { shopInfo ->
                productDetailTracking.eventEnhanceEcommerceProductDetail(trackerListName, productInfo, shopInfo, trackerAttribution,
                        isElligible, tradeInParams.usedPrice > 0, viewModel.multiOrigin.isFulfillment)
                return
            }
        }
        delegateTradeInTracking = true
    }


    private fun renderTradein(productInfoP1: ProductInfoP1) {
        tradeInParams = TradeInParams()
        tradeInParams.categoryId = productInfoP1.productInfo.category.id.toIntOrZero()
        tradeInParams.deviceId = (activity?.application as ProductDetailRouter).getDeviceId(activity as Context)
        tradeInParams.userId = if (viewModel.userId.isNotEmpty())
            viewModel.userId.toIntOrZero()
        else
            0
        tradeInParams.setPrice(productInfoP1.productInfo.basic.price.toInt())
        tradeInParams.productId = productInfoP1.productInfo.basic.id
        tradeInParams.shopId = productInfoP1.productInfo.basic.shopID
        tradeInParams.productName = productInfoP1.productInfo.basic.name
        val preorderstatus = productInfoP1.productInfo.isPreorderActive
        if (preorderstatus)
            tradeInParams.isPreorder = preorderstatus
        else
            tradeInParams.isPreorder = false
        tradeInParams.isOnCampaign = productInfoP1.productInfo.campaign.isActive

        pdpHashMapUtil.productTradeinMap.tradeInParams = tradeInParams
    }

    private fun initActionButton() {
        actionButtonView.promoTopAdsClick = {
            pdpHashMapUtil.snapShotMap.shopInfo.let { shopInfo ->
                val applink = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopInfo?.shopCore?.shopID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, pdpHashMapUtil.snapShotMap.productInfoP1?.basic?.id?.toString())
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                if (GlobalConfig.isSellerApp()) TopAdsSourceOption.SA_PDP else TopAdsSourceOption.MA_PDP).build().toString()

                context?.let { RouteManager.route(it, applink) }
            }
        }


        actionButtonView.addToCartClick = {
            pdpHashMapUtil.snapShotMap.productInfoP1?.let {
                productDetailTracking.eventClickAddToCart(it.basic.id.toString(),
                        it.variant.isVariant)
                goToNormalCheckout(ATC_ONLY)
            }
        }
        actionButtonView.buyNowClick = {
            // buy now / buy / preorder
            pdpHashMapUtil.snapShotMap.productInfoP1?.let {
                productDetailTracking.eventClickBuy(it.basic.id.toString(),
                        it.variant.isVariant)
                doBuy()
            }
        }
    }

    private fun goToNormalCheckout(@ProductAction action: Int = ATC_AND_BUY) {
        context?.let {
            val shopInfo = pdpHashMapUtil.snapShotMap.shopInfo
            pdpHashMapUtil.snapShotMap.productInfoP1?.run {
                val isOcsCheckoutType = (viewModel.p2Login.value)?.isOcsCheckoutType
                        ?: false
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, basic.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, basic.price)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_CONDITION, basic.condition)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, category.id)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, category.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, basic.shopID.toString())
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, parentProductId)
                    putExtra(ApplinkConst.Transaction.EXTRA_NOTES, userInputNotes)
                    putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, userInputQuantity)
                    putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, userInputVariant)
                    putExtra(ApplinkConst.Transaction.EXTRA_ACTION, action)
                    putExtra(ApplinkConst.Transaction.TRACKER_ATTRIBUTION, trackerAttribution)
                    putExtra(ApplinkConst.Transaction.TRACKER_LIST_NAME, trackerListName)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_TYPE, shopInfo?.goldOS?.shopTypeString)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopInfo?.shopCore?.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_OCS, isOcsCheckoutType)
                    putExtra(ApplinkConst.Transaction.EXTRA_IS_LEASING, basic.isLeasing)
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


    private fun removeDiscussionSection() {
        adapter.clearElement(pdpHashMapUtil.productDiscussionMap)
    }

    private fun removeImageReviewSection() {
        adapter.clearElement(pdpHashMapUtil.productImageReviewMap)
    }

    private fun removeMostHelpfulReviewSection() {
        adapter.clearElement(pdpHashMapUtil.productMostHelpfulMap)
    }

    private fun removeTradeinSection() {
        adapter.clearElement(pdpHashMapUtil.productTradeinMap)
    }

    override fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>) {
        context?.let {
            startActivity(ProductFullDescriptionTabActivity.createIntent(it,
                    data, listOfCatalog))
            activity?.overridePendingTransition(R.anim.pull_up, 0)
            productDetailTracking.eventClickProductDescriptionReadMore(data.basicId)
        }
    }

    /**
     * ProductImageReviewViewHolder Listener
     */
    override fun onSeeAllReviewClick() {
        context?.let {
            val productId = pdpHashMapUtil.snapShotMap.productInfoP1?.basic?.id ?: 0
            productDetailTracking.eventClickReviewOnSeeAllImage(productId)
            RouteManager.route(it, ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY, productId.toString())
        }
    }

    override fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int) {
        context?.let {
            val productId = pdpHashMapUtil.snapShotMap.productInfoP1?.basic?.id ?: 0
            productDetailTracking.eventClickReviewOnBuyersImage(productId, listOfImage[position].reviewId)
            val listOfImageReview: List<String> = listOfImage.map {
                it.imageUrlLarge ?: ""
            }

            ImageReviewGalleryActivity.moveTo(context, ArrayList(listOfImageReview), position)
        }
    }

    override fun onReviewClick() {
        pdpHashMapUtil.snapShotMap.productInfoP1?.run {
            productDetailTracking.eventReviewClicked()
            productDetailTracking.sendMoEngageClickReview(this, pdpHashMapUtil.snapShotMap.shopInfo?.goldOS?.isOfficial == 1, pdpHashMapUtil.snapShotMap.shopInfo?.shopCore?.name
                    ?: "")
            context?.let {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_REVIEW, basic.id.toString())
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
        productDetailTracking.eventClickReviewOnMostHelpfulReview(pdpHashMapUtil.snapShotMap.productInfoP1?.basic?.id, reviewId)
        context?.let { ImageReviewGalleryActivity.moveTo(it, ArrayList(listOfImages), position) }
    }

    /**
     * ProductMerchantVoucherViewHolder
     */
    override fun isOwner(): Boolean {
        return pdpHashMapUtil.snapShotMap.productInfoP1?.basic?.shopID?.let {
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
            pdpHashMapUtil.snapShotMap.productInfoP1?.run {
                productDetailTracking.eventClickMerchantVoucherSeeDetail(basic.id)
                val intent = MerchantVoucherDetailActivity.createIntent(it, merchantVoucherViewModel.voucherId,
                        merchantVoucherViewModel, basic.shopID.toString())
                startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
            }
        }
    }

    override fun onSeeAllMerchantVoucherClick() {
        activity?.let {
            pdpHashMapUtil.snapShotMap.productInfoP1?.run {
                productDetailTracking.eventClickMerchantVoucherSeeAll(basic.id)

                val intent = MerchantVoucherListActivity.createIntent(it, basic.shopID.toString(),
                        pdpHashMapUtil.snapShotMap.shopInfo?.shopCore?.name)
                startActivityForResult(intent, ProductDetailFragment.REQUEST_CODE_MERCHANT_VOUCHER)
            }
        }
    }

    /**
     * ProductTradeinViewHolder
     */
    private fun showSnackbarClose(string: String) {
        Snackbar.make(coordinator, string, Snackbar.LENGTH_LONG).apply {
            setAction(getString(R.string.close)) { dismiss() }
            setActionTextColor(Color.WHITE)
        }.show()
    }

    override fun onTradeinClicked(tradeInParams: TradeInParams) {
        goToNormalCheckout(TRADEIN_BUY)
        tradeInParams?.let {
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
    }

    private fun showToasterError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        varToolbar = search_pdp_toolbar
        initToolBarMethod = ::initToolbarLight
        renderPartialData()
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

    private fun renderPartialData() {
        actionButtonView.visibility = true
        actionButtonView.renderData(false)
    }

    private fun initializePartialView() {
        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
        }
    }

    private fun onApplyLeasingClicked() {
//        productInfo?.run {
//            productDetailTracking.eventClickApplyLeasing(
//                    parentProductId,
//                    variant.isVariant
//            )
//            goToNormalCheckout(APPLY_CREDIT)
//        }
    }

    private fun gotoShopDetail() {
//        activity?.let {
//            val shopId = productInfo?.basic?.shopID?.toString() ?: return
//            startActivityForResult(RouteManager.getIntent(it,
//                    ApplinkConst.SHOP, shopId),
//                    ProductDetailFragment.REQUEST_CODE_SHOP_INFO)
//        }
    }

    private fun onShopFavoriteClick() {
//        val shop = shopInfo ?: return
//        activity?.let {
//            if (productInfoViewModel.isUserSessionActive()) {
//                productShopView.toggleClickableFavoriteBtn(false)
//                productInfoViewModel.toggleFavorite(shop.shopCore.shopID,
//                        this::onSuccessFavoriteShop, this::onFailFavoriteShop)
//            } else {
//                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
//                        ProductDetailFragment.REQUEST_CODE_LOGIN)
//            }
//        }
    }

    private fun onShopChatClicked() {
//        val shop = shopInfo ?: return
//        val product = productInfo ?: return
//        activity?.let {
//            if (productInfoViewModel.isUserSessionActive()) {
//                val intent = RouteManager.getIntent(it,
//                        ApplinkConst.TOPCHAT_ASKSELLER,
//                        shop.shopCore.shopID, "",
//                        "product", shop.shopCore.name, shop.shopAssets.avatar)
//                productInfoViewModel.putChatProductInfoTo(intent, productId, productInfo, userInputVariant)
//                startActivity(intent)
//            } else {
//                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
//                        ProductDetailFragment.REQUEST_CODE_LOGIN)
//            }
//        }
//        productDetailTracking.eventSendMessage()
//        productDetailTracking.eventSendChat(productId ?: "")
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


    private fun onPictureProductClicked(position: Int) {
        startActivity(ImagePreviewActivity.getCallingIntent(context!!,
                viewModel.getImageUriPaths(),
                null,
                position))
    }
}