package com.tkpd.atcvariant.view.bottomsheet

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tkpd.atcvariant.BuildConfig
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.PartialButtonDataModel
import com.tkpd.atcvariant.data.uidata.VariantErrorDataModel
import com.tkpd.atcvariant.di.AtcVariantComponent
import com.tkpd.atcvariant.di.DaggerAtcVariantComponent
import com.tkpd.atcvariant.util.ATC_LOGIN_REQUEST_CODE
import com.tkpd.atcvariant.util.AtcCommonMapper
import com.tkpd.atcvariant.util.BS_SHIPMENT_ERROR_ATC_VARIANT
import com.tkpd.atcvariant.view.PartialAtcButtonListener
import com.tkpd.atcvariant.view.PartialAtcButtonView
import com.tkpd.atcvariant.view.activity.AtcVariantActivity
import com.tkpd.atcvariant.view.adapter.AtcVariantAdapter
import com.tkpd.atcvariant.view.adapter.AtcVariantAdapterTypeFactoryImpl
import com.tkpd.atcvariant.view.adapter.AtcVariantDiffutil
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tkpd.atcvariant.view.viewmodel.AtcVariantViewModel
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.btm.BtmApi
import com.tokopedia.analytics.btm.Tokopedia
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EnterMethod
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
import com.tokopedia.analytics.byteio.pdp.AppLogPdp
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.PostAtcHelper
import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.REQUEST_CODE_ATC_VAR_CHANGE_ADDRESS
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.REQUEST_CODE_TRADEIN_PDP
import com.tokopedia.product.detail.common.ProductTrackingCommon
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.CartRedirectionButtonsByteIOTracker
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.ICartRedirectionButtonsByteIOTracker
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton.Companion.buttonText
import com.tokopedia.product.detail.common.data.model.carttype.PostAtcLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.mapIntoPromoExternalAutoApply
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.mapper.AtcVariantMapper
import com.tokopedia.product.detail.common.postatc.PostAtcParams
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product.detail.common.showToasterSuccess
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.common.view.ProductDetailCommonBottomSheetBuilder
import com.tokopedia.product.detail.common.view.ProductDetailGalleryActivity
import com.tokopedia.product.detail.common.view.ProductDetailRestrictionHelper
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersListener
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import kotlinx.coroutines.flow.collectLatest
import rx.subscriptions.CompositeSubscription
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR
import com.tokopedia.product.detail.common.R as productdetailcommonR

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantBottomSheet :
    BottomSheetUnify(),
    AtcVariantListener,
    PartialAtcButtonListener,
    PartialButtonShopFollowersListener,
    AtcVariantBottomSheetListener,
    HasComponent<AtcVariantComponent>,
    ICartRedirectionButtonsByteIOTracker.Mediator,
    ICartRedirectionButtonsByteIOTracker by CartRedirectionButtonsByteIOTracker() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AtcVariantViewModel::class.java)
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AtcVariantSharedViewModel::class.java)
    }

    private val localizationChooseAddressData by lazy(LazyThreadSafetyMode.NONE) {
        context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private val compositeSubscription by lazy { CompositeSubscription() }
    private val adapterFactory by lazy {
        AtcVariantAdapterTypeFactoryImpl(
            this,
            compositeSubscription,
            this
        )
    }
    private val adapter by lazy {
        val asyncDifferConfig = AsyncDifferConfig.Builder(AtcVariantDiffutil())
            .build()
        AtcVariantAdapter(asyncDifferConfig, adapterFactory)
    }
    private var viewContent: View? = null
    private var nplFollowersButton: PartialButtonShopFollowersView? = null

    private var baseAtcBtn: PartialAtcButtonView? = null
    private var rvVariantBottomSheet: RecyclerView? = null
    private var txtStock: Typography? = null
    private var buttonActionType = 0
    private var buttonText = ""
    private var alreadyHitQtyTrack = false
    private var shouldSetActivityResult = true
    private val atcAnimator by atcAnimator()

    override fun onProductImageChanged(container: ImageView) {
        atcAnimator.setSourceView(view = container)
    }

    private val aggregatorParams
        get() = sharedViewModel.aggregatorParams.value
            ?: ProductVariantBottomSheetParams()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BtmApi.registerBtmPageOnCreate(this, Tokopedia.Sku)
        component.inject(this)
        registerCartRedirectionButtonsByteIOTracker(mediator = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT -> {
                viewModel.updateActivityResult(requestCode = ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
            }

            ATC_LOGIN_REQUEST_CODE -> {
                baseAtcBtn?.hideLoading()
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.updateActivityResult(
                        requestCode = ATC_LOGIN_REQUEST_CODE,
                        shouldRefreshPreviousPage = true
                    )
                    dismiss()
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        compositeSubscription.clear()
        super.onDestroyView()
        Toaster.onCTAClick = View.OnClickListener { }
    }

    private fun initLayout() {
        isHideable = true
        clearContentPadding = true

        setTitle(context?.getString(R.string.title_activity_atc_variant) ?: "")

        setShowListener {
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {
                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                    if (p1 == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetWrapper.invalidate()
                        bottomSheetWrapper.requestLayout()
                    }
                }
            })
        }

        viewContent = View.inflate(context, R.layout.bottomsheet_atc_variant, null).also {
            baseAtcBtn = PartialAtcButtonView.build(it.findViewById(R.id.base_atc_btn), this)
            txtStock = it.findViewById(R.id.txt_variant_empty_stock)
        }
        setChild(viewContent)
        setupRv(viewContent)
    }

    private fun goToTopChat() {
        if (checkLogin()) return
        val aggregatorData = viewModel.getVariantAggregatorData()
        val intent = RouteManager.getIntent(
            context,
            ApplinkConst.TOPCHAT_ROOM_ASKSELLER,
            aggregatorData?.simpleBasicInfo?.shopID ?: ""
        )

        val productId = adapter.getHeaderDataModel()?.productId ?: ""

        AtcCommonMapper.putChatProductInfoTo(intent, productId)
        intent.putExtra(ApplinkConst.Chat.SOURCE, ApplinkConst.Chat.Source.SOURCE_PDP)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun setupRv(view: View?) {
        rvVariantBottomSheet = view?.findViewById(R.id.rv_atc_variant_bottomsheet)
        rvVariantBottomSheet?.adapter = adapter
    }

    private fun observeData() {
        observeParamsData()
        observeInitialVisitablesData()
        observeButtonState()
        observeCart()
        observeDeleteCart()
        observeUpdateCart()
        observeRestrictionData()
        observeToggleFavorite()
        observeStockCopy()
        observeVariantImagesGallery()
    }

    private fun observeVariantImagesGallery() {
        viewModel.variantImagesData.observe(viewLifecycleOwner) { data ->
            context?.let {
                val intent = ProductDetailGalleryActivity.createIntent(
                    it,
                    data
                )
                startActivity(intent)
            }
        }
    }

    private fun observeStockCopy() {
        viewModel.stockCopy.observe(viewLifecycleOwner) {
            txtStock?.shouldShowWithAction(it.isNotEmpty()) {
                if (context != null) {
                    txtStock?.text =
                        HtmlLinkHelper(requireContext(), it).spannedString
                }
            }
        }
    }

    private fun observeParamsData() {
        sharedViewModel.aggregatorParams.observeOnce(viewLifecycleOwner) {
            val previousData = getDataFromPreviousPage(it)
            // If complete data is coming from previous page, set params into this data (directly show without hit network)
            // if not just use general data from aggregatorParams (data do not complete, hit network)
            val data = if (previousData != null) {
                sharedViewModel.setAtcBottomSheetParams(previousData)
                previousData
            } else {
                it
            }

            atcAnimator.setTargetLocation(data.cartPosition)
            setupButtonAbility(data)
            viewModel.decideInitialValue(data, userSessionInterface.isLoggedIn)
        }
    }

    private fun setupButtonAbility(data: ProductVariantBottomSheetParams) {
        shouldSetActivityResult = data.saveAfterClose
    }

    private fun dismissAfterTransaction() {
        val shouldDismiss = aggregatorParams.dismissAfterTransaction
        if (shouldDismiss) {
            dismiss()
        }
    }

    private fun dismissWhenTransactionError(
        onDismissed: () -> Unit = {},
        onNothing: () -> Unit = {}
    ) {
        val shouldDismiss = aggregatorParams.dismissWhenTransactionError
        if (shouldDismiss) {
            onDismissed()
            dismiss()
        } else {
            onNothing()
        }
    }

    private fun getDataFromPreviousPage(productVariantBottomSheetParams: ProductVariantBottomSheetParams): ProductVariantBottomSheetParams? {
        context?.let { ctx ->
            val cacheManager = SaveInstanceCacheManager(
                ctx.applicationContext,
                productVariantBottomSheetParams.cacheId
            )
            return cacheManager.get(
                AtcVariantHelper.PDP_PARCEL_KEY_RESPONSE,
                ProductVariantBottomSheetParams::class.java,
                null
            )
        }

        return null
    }

    private fun observeToggleFavorite() {
        viewModel.toggleFavoriteShop.observe(viewLifecycleOwner) {
            nplFollowersButton?.stopLoading()
            when (it) {
                is Success -> {
                    showToasterSuccess(getString(productdetailcommonR.string.merchant_product_detail_success_follow_shop_npl))
                    viewModel.updateActivityResult(isFollowShop = true)
                    nplFollowersButton?.setupVisibility = false
                }

                is Fail -> {
                    onFailFavoriteShop(it.throwable)
                }
            }
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        showToasterError(
            getErrorMessage(t),
            ctaBtn = getString(abstractionR.string.retry_label)
        ) {
            onButtonFollowNplClick()
        }
    }

    private fun observeRestrictionData() {
        viewModel.restrictionData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (nplFollowersButton == null) {
                        setupReViewStub()
                    }
                    renderRestrictionBottomSheet(it.data)
                }

                is Fail -> {
                    nplFollowersButton?.setupVisibility = false
                }
            }
        }
    }

    private fun setupReViewStub() {
        val viewRe: ViewStub? = viewContent?.findViewById(R.id.base_atc_variant_re_button)
        val reView = viewRe?.inflate()
        reView?.let {
            nplFollowersButton = PartialButtonShopFollowersView.build(it, this)
        }
    }

    private fun renderRestrictionBottomSheet(reData: RestrictionData) {
        ProductDetailRestrictionHelper.renderRestrictionUi(
            reData = reData,
            isFavoriteShop = viewModel.getActivityResultData().isFollowShop,
            isShopOwner = sharedViewModel.aggregatorParams.value?.isShopOwner ?: false,
            reView = nplFollowersButton
        )
    }

    private fun showToasterSuccess(
        message: String,
        ctaText: String = "",
        ctaListener: () -> Unit = {}
    ) {
        viewContent?.rootView?.showToasterSuccess(
            message,
            R.dimen.space_toaster_offsite_atc_variant,
            ctaText = ctaText,
            ctaListener = {
                ctaListener.invoke()
            }
        )
    }

    private fun observeDeleteCart() {
        viewModel.deleteCartLiveData.observe(viewLifecycleOwner) {
            baseAtcBtn?.hideLoading()
            when (it) {
                is Success -> showToasterSuccess(it.data, getString(R.string.atc_variant_oke_label))
                is Fail -> {
                    showToasterError(getErrorMessage(it.throwable))
                    logException(it.throwable)
                }
            }
        }
    }

    private fun observeUpdateCart() {
        viewModel.updateCartLiveData.observe(viewLifecycleOwner) {
            baseAtcBtn?.hideLoading()

            when (aggregatorParams.pageSource) {
                VariantPageSource.CART_CHANGE_VARIANT.source -> handleObserveUpdateCartByClient(it)
                else -> handleObserveUpdateCartOnGeneral(result = it)
            }
        }
    }

    private fun handleObserveUpdateCartOnGeneral(result: Result<Data>) {
        when (result) {
            is Success -> {
                showToasterSuccess(result.data.message, getString(R.string.atc_variant_oke_label))
            }

            is Fail -> {
                showToasterError(getErrorMessage(result.throwable))
                logException(result.throwable)
            }
        }
    }

    private fun handleObserveUpdateCartByClient(result: Result<Data>) {
        when (result) {
            is Success -> {
                val data = result.data
                viewModel.updateActivityResult(
                    requestCode = ProductDetailCommonConstant.RC_VBS_UPDATE_VARIANT_SUCCESS,
                    atcSuccessMessage = data.message,
                    anchorCartId = data.anchorCartId.toString()
                )
                dismissAfterTransaction()
            }

            is Fail -> {
                logException(result.throwable)

                dismissWhenTransactionError(
                    onDismissed = {
                        viewModel.getActivityResultData().apply {
                            requestCode = ProductDetailCommonConstant.RC_VBS_TRANSACTION_ERROR
                            atcMessage = getErrorMessage(result.throwable)
                        }
                    },
                    onNothing = {
                        showToasterError(getErrorMessage(result.throwable))
                    }
                )
            }
        }
    }

    private fun observeInitialVisitablesData() {
        viewModel.initialData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> adapter.submitList(it.data)
                is Fail -> {
                    val throwable = it.throwable
                    showError(throwable)
                    logException(throwable)
                }
            }
        }
    }

    private fun showError(it: Throwable) {
        val errorType =
            if (it is SocketTimeoutException || it is UnknownHostException || it is ConnectException) {
                GlobalError.NO_CONNECTION
            } else {
                GlobalError.SERVER_ERROR
            }
        adapter.submitList(listOf(VariantErrorDataModel(errorType = errorType)))
    }

    private fun observeButtonState() {
        viewModel.buttonData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> renderButton(it.data)
                is Fail -> {
                    baseAtcBtn?.visibility = false
                    logException(it.throwable)
                }
            }
        }
    }

    private fun showToasterError(
        message: String,
        ctaBtn: String = "",
        ctaListener: () -> Unit = {}
    ) {
        viewContent?.rootView.showToasterError(
            message,
            ctaText = ctaBtn,
            heightOffset = R.dimen.space_toaster_offsite_atc_variant
        ) {
            ctaListener.invoke()
        }
    }

    private fun getConfirmCartAnalyticsModel(result: Result<AddToCartDataModel>): TrackConfirmCartResult {
        val cartId = (result as? Success)?.data?.data?.cartId.orEmpty()
        val success = result is Success
        val reason = (result as? Fail)?.throwable?.message.orEmpty()

        return viewModel.getConfirmCartResultModel().apply {
            isSuccess = success
            failReason = reason
            cartItemId = cartId
        }
    }

    private fun observeCart() {
        lifecycleScope.launchWhenStarted {
            viewModel.addToCartResultState.collectLatest {
                baseAtcBtn?.hideLoading()

                val model = getConfirmCartAnalyticsModel(it)
                if (buttonActionType == ProductDetailCommonConstant.ATC_BUTTON ||
                    buttonActionType == ProductDetailCommonConstant.BUY_BUTTON
                ) {
                    AppLogPdp.sendConfirmCartResult(model)
                }

                when (it) {
                    is Success -> {
                        onSuccessTransaction(it.data)
                        trackOnButtonClickCompleted(it.data)
                        dismissAfterTransaction()
                    }

                    is Fail -> {
                        it.throwable.run {
                            trackAtcError(message ?: "")
                            viewModel.updateActivityResult(atcSuccessMessage = "")
                            if (this is AkamaiErrorException && message != null) {
                                showToasterError(
                                    message ?: "",
                                    getString(R.string.atc_variant_oke_label)
                                )
                            } else {
                                showToasterError(
                                    getErrorMessage(this),
                                    getString(R.string.atc_variant_oke_label)
                                )
                            }
                            logException(this)
                        }
                    }
                }
            }
        }
    }

    private fun trackAtcError(message: String) {
        val productId = adapter.getHeaderDataModel()?.productId ?: ""

        ProductTrackingCommon.eventEcommerceAtcError(
            message,
            productId,
            userSessionInterface.userId,
            sharedViewModel.aggregatorParams.value?.pageSource
                ?: ""
        )
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return if (throwable is ResponseErrorException) {
            throwable.message
                ?: getString(productdetailcommonR.string.merchant_product_detail_error_default)
        } else if (throwable is AkamaiErrorException && throwable.message != null) {
            throwable.message
                ?: getString(productdetailcommonR.string.merchant_product_detail_error_default)
        } else {
            context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            }
                ?: getString(productdetailcommonR.string.merchant_product_detail_error_default)
        }
    }

    private fun onSuccessTransaction(result: AddToCartDataModel) {
        val cartId = result.data.cartId

        trackSuccessAtc(cartId)

        if (aggregatorParams.showQtyEditor || aggregatorParams.isTokoNow) {
            onSuccessAtcTokoNow(result.errorMessage.firstOrNull(), cartId)
            return
        }
        when (buttonActionType) {
            ProductDetailCommonConstant.OCS_BUTTON -> {
                onSuccessOcs(result)
            }

            ProductDetailCommonConstant.OCC_BUTTON -> {
                goToOcc(result.data.productId, result)
            }

            ProductDetailCommonConstant.BUY_BUTTON -> {
                ProductCartHelper.goToCartCheckout(getAtcActivity(), cartId)
            }

            ProductDetailCommonConstant.ATC_BUTTON -> {
                onSuccessAtc(result)
            }
        }
    }

    private fun goToOcc(productId: String, atcResult: AddToCartDataModel) {
        val aggregatorData = viewModel.getVariantAggregatorData() ?: return
        val selectedPromoCodes =
            aggregatorData
                .variantData
                .getChildByProductId(productId)
                ?.promoPrice
                ?.promoCodes
                ?.mapIntoPromoExternalAutoApply() ?: arrayListOf()

        if (atcResult.isOccNewCheckoutPage) {
            ProductCartHelper.goToCheckoutWithAutoApplyPromo(getAtcActivity(), ArrayList(selectedPromoCodes))
        } else {
            ProductCartHelper.goToOneClickCheckoutWithAutoApplyPromo(
                getAtcActivity(),
                ArrayList(selectedPromoCodes)
            )
        }
    }

    private fun trackSuccessAtc(cartId: String) {
        val aggregatorParams = sharedViewModel.aggregatorParams.value
        val productId = adapter.getHeaderDataModel()?.productId ?: ""
        val variantAggregatorData = viewModel.getVariantAggregatorData()
        val selectedQuantity = if (viewModel.getSelectedQuantity(productId) == 0) {
            variantAggregatorData?.variantData?.getChildByProductId(productId)?.getFinalMinOrder()
                ?: 0
        } else {
            viewModel.getSelectedQuantity(productId)
        }
        val selectedChild = variantAggregatorData?.variantData?.getChildByProductId(productId)
        val shopType = if (sharedViewModel.aggregatorParams.value?.isTokoNow == true) {
            ProductDetailCommonConstant.VALUE_TOKONOW
        } else {
            variantAggregatorData?.shopType
                ?: ""
        }
        val variantTitle =
            adapter.getHeaderDataModel()?.listOfVariantTitle?.joinToString(separator = ", ")
                ?: ""

        val ratesEstimateData =
            variantAggregatorData?.getP2RatesEstimateByProductId(productId)?.p2RatesData
        val buyerDistrictId = context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)?.district_id ?: ""
        } ?: ""
        val sellerDistrictId = viewModel.getSelectedWarehouse(productId)?.districtId ?: ""

        ProductTrackingCommon.eventEcommerceAddToCart(
            userId = userSessionInterface.userId,
            cartId = cartId,
            buttonAction = buttonActionType,
            buttonText = buttonText,
            productId = productId,
            shopId = variantAggregatorData?.simpleBasicInfo?.shopID ?: "",
            productName = selectedChild?.name ?: "",
            productPrice = selectedChild?.finalPrice ?: 0.0,
            quantity = selectedQuantity,
            variantName = variantTitle,
            isMultiOrigin = viewModel.getSelectedWarehouse(productId)?.isFulfillment ?: false,
            shopType = shopType,
            shopName = variantAggregatorData?.simpleBasicInfo?.shopName ?: "",
            categoryName = variantAggregatorData?.simpleBasicInfo?.category?.getCategoryNameFormatted()
                ?: "",
            categoryId = variantAggregatorData?.simpleBasicInfo?.category?.getCategoryIdFormatted()
                ?: "",
            bebasOngkirType = variantAggregatorData?.getBebasOngkirStringType(productId) ?: "",
            pageSource = aggregatorParams?.pageSource ?: "",
            cdListName = aggregatorParams?.trackerCdListName ?: "",
            isCod = variantAggregatorData?.isCod ?: false,
            ratesEstimateData = ratesEstimateData,
            buyerDistrictId = buyerDistrictId,
            sellerDistrictId = sellerDistrictId,
            lcaWarehouseId = localizationChooseAddressData?.warehouse_id ?: ""
        )
    }

    private fun onSuccessOcs(result: AddToCartDataModel) {
        if (result.data.success == 0) {
            val parentId = viewModel.getVariantData()?.parentId ?: ""
            ProductCartHelper.validateOvo(activity, result, parentId, userSessionInterface.userId, {
                viewModel.updateActivityResult(shouldRefreshPreviousPage = true)
                dismiss()
            }, {
                showToasterError(
                    getString(abstractionR.string.default_request_error_unknown),
                    getString(R.string.atc_variant_oke_label)
                )
            })
        } else {
            goToCheckout()
        }
    }

    private fun getAtcActivity(): Activity {
        return if (activity is AtcVariantActivity) {
            context as AtcVariantActivity
        } else {
            requireActivity()
        }
    }

    private fun onSuccessAtcTokoNow(successMessage: String?, cartId: String) {
        context?.let {
            val message = if (successMessage == null || successMessage.isEmpty()) {
                it.getString(productdetailcommonR.string.merchant_product_detail_success_atc_default)
            } else {
                successMessage
            }
            viewModel.updateActivityResult(
                atcSuccessMessage = message,
                cartId = cartId
            )
            showToasterSuccess(message, getString(R.string.atc_variant_oke_label)) {
            }
        }
    }

    private fun onSuccessAtc(cartDataModel: AddToCartDataModel) {
        val successMessage = cartDataModel.errorMessage.firstOrNull()
        val cartData = cartDataModel.data
        val context = context ?: return
        val productId = adapter.getHeaderDataModel()?.productId ?: return

        val variantAggregatorData = viewModel.getVariantAggregatorData()
        val cartRedirection = variantAggregatorData?.cardRedirection?.get(productId)
        val postAtcLayout = cartRedirection?.postAtcLayout

        var atcMessage = ""
        if (postAtcLayout?.showPostAtc == true) {
            showPostATC(context, productId, postAtcLayout, cartData)
        } else {
            val message = if (successMessage.isNullOrEmpty()) {
                context.getString(productdetailcommonR.string.merchant_product_detail_success_atc_default)
            } else {
                successMessage
            }

            showToasterSuccess(
                message,
                ctaText = getString(productdetailcommonR.string.cta_text_atc_success)
            ) {
                val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
                ProductTrackingCommon.onSeeCartVariantBottomSheetClicked(
                    message,
                    productId,
                    pageSource
                )
                ProductCartHelper.goToCartCheckout(getAtcActivity(), cartDataModel.data.cartId)
                putAppLogEnterMethod()
            }
            atcMessage = message
        }
        viewModel.updateActivityResult(
            atcSuccessMessage = atcMessage,
            requestCode = ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT,
            cartId = cartData.cartId
        )
    }

    private fun putAppLogEnterMethod() {
        if (AppLogAnalytics.getDataBeforeStep(AppLogParam.PAGE_NAME) == PageName.PDP) {
            AppLogAnalytics.putPreviousPageData(
                AppLogParam.ENTER_METHOD,
                EnterMethod.CLICK_ATC_TOASTER_PDP.str
            )
        }
    }

    private fun showPostATC(
        context: Context,
        productId: String,
        postAtcLayout: PostAtcLayout,
        cartData: DataModel
    ) {
        val addonsIds = cartData.addOns.groupBy(
            keySelector = { it.status },
            valueTransform = { it.id }
        )

        val addons = PostAtcParams.Addons(
            deselectedAddonsIds = addonsIds[2] ?: emptyList(),
            isFulfillment = cartData.isFulfillment,
            selectedAddonsIds = addonsIds[1] ?: emptyList(),
            warehouseId = cartData.warehouseId,
            quantity = cartData.quantity
        )

        val variantPageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
        val pageSource = if (variantPageSource == VariantPageSource.PDP_PAGESOURCE.source) {
            PostAtcParams.Source.PDP
        } else {
            PostAtcParams.Source.Default
        }

        val postAtcParams = PostAtcParams(
            cartId = cartData.cartId,
            layoutId = postAtcLayout.layoutId,
            pageSource = pageSource.name,
            session = postAtcLayout.postAtcSession,
            addons = addons
        )

        PostAtcHelper.start(
            context,
            productId,
            postAtcParams
        )
        dismiss()
    }

    private fun goToCheckout() {
        context?.let {
            ProductCartHelper.goToCheckout(
                it as AtcVariantActivity,
                ShipmentFormRequest
                    .BundleBuilder()
                    .deviceId("")
                    .build()
                    .bundle
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (shouldSetActivityResult) {
            viewModel.getActivityResultData().let {
                sharedViewModel.setActivityResult(it)
            }
        } else {
            (activity as? AtcVariantActivity)?.finish()
        }

        super.onDismiss(dialog)
    }

    override fun getComponent(): AtcVariantComponent {
        return DaggerAtcVariantComponent
            .builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onDeleteQuantityClicked(productId: String) {
        baseAtcBtn?.showLoading()

        viewModel.deleteProductInCart(productId)
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute, state: Int) {
        if (state == VariantConstant.STATE_SELECTED || state == VariantConstant.STATE_SELECTED_EMPTY) return
        adapter.removeTextWatcherQuantityViewHolder(rvVariantBottomSheet)
        viewModel.onVariantClicked(
            sharedViewModel.aggregatorParams.value?.isTokoNow ?: false ||
                sharedViewModel.aggregatorParams.value?.showQtyEditor ?: false,
            variantOptions.variantCategoryKey,
            variantOptions.variantId,
            variantOptions.imageOriginal,
            variantOptions.level
        )
    }

    override fun onVariantGuideLineClicked(url: String) {
        val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
        ProductTrackingCommon.onVariantGuidelineClicked(
            adapter.getHeaderDataModel()?.productId
                ?: "",
            pageSource
        )
        goToImagePreview(arrayListOf(url))
    }

    override fun onButtonFallbackClick(actionButton: AvailableButton) {
        this.buttonText = actionButton.text
        doAtc(button = actionButton)
    }

    override fun onChatButtonClick() {
        goToTopChat()
    }

    override fun onButtonShowed(buttonCartTypes: List<String>) {
        trackOnButtonsShowed(buttonCartTypes)
    }

    override fun buttonCartTypeClick(availableButton: AvailableButton) {
        val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
        val productIdPreviousPage = sharedViewModel.aggregatorParams.value?.productId ?: ""
        val parentId = viewModel.getVariantAggregatorData()?.variantData?.parentId ?: ""

        when (availableButton.cartType) {
            ProductDetailCommonConstant.KEY_SAVE_BUNDLING_BUTTON,
            ProductDetailCommonConstant.KEY_DEFAULT_CHOOSE_VARIANT -> {
                ProductTrackingCommon.eventClickPilihVariant(
                    adapter.getHeaderDataModel()?.productId.orEmpty(),
                    pageSource,
                    availableButton.cartType,
                    parentId,
                    productIdPreviousPage
                )
                onSaveButtonClicked()
            }

            ProductDetailCommonConstant.KEY_SAVE_TRADEIN_BUTTON -> {
                ProductTrackingCommon.eventClickPilihVariant(
                    adapter.getHeaderDataModel()?.productId.orEmpty(),
                    pageSource,
                    availableButton.cartType,
                    parentId,
                    productIdPreviousPage
                )
                viewModel.updateActivityResult(requestCode = REQUEST_CODE_TRADEIN_PDP)
                onSaveButtonClicked()
            }

            ProductDetailCommonConstant.KEY_CART_TYPE_OF_VARIANT_EDITOR -> {
                baseAtcBtn?.showLoading()
                viewModel.updateCart(params = aggregatorParams)
            }

            else -> {
                this.buttonText = availableButton.buttonText
                doAtc(availableButton)
            }
        }
    }

    override fun hideVariantName(): Boolean = true

    override fun shouldHideTextHabis(): Boolean {
        return sharedViewModel.aggregatorParams.value?.pageSource ==
            VariantPageSource.SHOP_COUPON_PAGESOURCE.source
    }

    private fun onSaveButtonClicked() {
        val productId = adapter.getHeaderDataModel()?.productId ?: ""
        val selectedChild = viewModel.getVariantData()?.getChildByProductId(productId)

        if (selectedChild?.isBuyable == true) {
            shouldSetActivityResult = true
            dismiss()
        } else {
            showToasterError(getString(R.string.atc_variant_select_empty_variant_message))
        }
    }

    private fun goToImagePreview(listOfImage: ArrayList<String>) {
        context?.let {
            val intent = ImagePreviewActivity.getCallingIntent(
                context = it,
                imageUris = listOfImage,
                disableDownloadButton = true
            )
            startActivity(intent)
        }
    }

    override fun onVariantImageClicked(url: String) {
        val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
        val productId = adapter.getHeaderDataModel()?.productId ?: ""
        ProductTrackingCommon.onVariantImageBottomSheetClicked(productId, pageSource)
        viewModel.onVariantImageClicked(
            url,
            productId,
            userSessionInterface.userId,
            getString(R.string.atc_variant_tag_main_image)
        )
    }

    override fun onQuantityUpdate(quantity: Int, productId: String, oldValue: Int) {
        if (!alreadyHitQtyTrack) {
            alreadyHitQtyTrack = true
            ProductTrackingCommon.onQuantityEditorClicked(
                productId,
                sharedViewModel.aggregatorParams?.value?.pageSource
                    ?: "",
                oldValue,
                quantity
            )
        }

        viewModel.updateQuantity(quantity, productId)
    }

    override fun onClickRefresh() {
        sharedViewModel.aggregatorParams.observeOnce(viewLifecycleOwner, {
            viewModel.decideInitialValue(it, userSessionInterface.isLoggedIn)
        })
    }

    private fun doAtc(button: AvailableButton) {
        buttonActionType = button.atcKey
        context?.let {
            val isPartialySelected =
                AtcVariantMapper.isPartiallySelectedOptionId(viewModel.getSelectedOptionIds())

            if (checkLogin()) return@let

            val sharedData = sharedViewModel.aggregatorParams.value
            val pageSource = sharedData?.pageSource ?: ""
            val productId = adapter.getHeaderDataModel()?.productId ?: ""

            if (buttonActionType == ProductDetailCommonConstant.REMIND_ME_BUTTON) {
                ProductTrackingCommon.onRemindMeClicked(productId, pageSource)
                // The possibilities this method being fire is when the user first open the bottom sheet with product not buyable
                doAddWishlistV2(productId)
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
                ProductTrackingCommon.onWishlistCheckClicked(productId, pageSource)
                goToWishlist()
                return@let
            }

            if (isPartialySelected) {
                showErrorVariantUnselected()
                return@let
            }

            if (openShipmentBottomSheetWhenError()) return@let

            showWaitingIndicator(button = button)

            trackOnButtonClick(button.atcKey)

            viewModel.hitAtc(
                button.atcKey,
                sharedData?.shopId.orEmpty(),
                viewModel.getVariantAggregatorData()?.simpleBasicInfo?.category?.getCategoryNameFormatted()
                    ?: "",
                userSessionInterface.userId,
                sharedData?.minimumShippingPrice ?: 0.0,
                sharedData?.trackerAttribution ?: "",
                sharedData?.trackerListNamePdp ?: "",
                sharedData?.showQtyEditor ?: false,
                viewModel.getVariantAggregatorData()?.simpleBasicInfo?.shopName ?: ""
            )
        }
    }

    private fun showWaitingIndicator(button: AvailableButton) {
        baseAtcBtn?.showLoading()

        if (button.showAnimation) {
            atcAnimator.show(onAnimateEnded = { viewModel.atcAnimationEnd() })
        } else {
            viewModel.atcAnimationEnd()
        }
    }

    private fun doAddWishlistV2(productId: String) {
        viewModel.addWishlistV2(
            productId,
            userSessionInterface.userId,
            object : WishlistV2ActionListener {
                override fun onErrorAddWishList(
                    throwable: Throwable,
                    productId: String
                ) {
                    val errorMsg = ErrorHandler.getErrorMessage(context, throwable)
                    viewContent?.rootView?.let {
                        AddRemoveWishlistV2Handler.showWishlistV2ErrorToaster(
                            errorMsg,
                            it
                        )
                    }
                }

                override fun onSuccessAddWishlist(
                    result: AddToWishlistV2Response.Data.WishlistAddV2,
                    productId: String
                ) {
                    context?.let { context ->
                        viewContent?.rootView?.let { v ->
                            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                                result,
                                context,
                                v
                            )
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

    private fun openShipmentBottomSheetWhenError(): Boolean {
        context?.let {
            val data = viewModel.ratesLiveData.value as? Success ?: return false

            ProductDetailCommonBottomSheetBuilder.getShippingErrorBottomSheet(
                it,
                data.data.errorBottomSheet,
                data.data.p2RatesData.p2RatesError.firstOrNull()?.errorCode ?: 0,
                onButtonClicked = { errorCode ->
                    goToChooseAddress(errorCode)
                },
                onHomeClicked = { goToHomePage() }
            ).show(childFragmentManager, BS_SHIPMENT_ERROR_ATC_VARIANT)
            return true
        } ?: return false
    }

    private fun goToHomePage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun goToChooseAddress(errorCode: Int) {
        if (errorCode == ProductDetailCommonConstant.SHIPPING_ERROR_WEIGHT) {
            goToTopChat()
        } else {
            openChooseAddressBottomSheet(object :
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
                })
        }
    }

    private fun onSuccessUpdateAddress() {
        viewModel.updateActivityResult(requestCode = REQUEST_CODE_ATC_VAR_CHANGE_ADDRESS)
        dismiss()
    }

    private fun openChooseAddressBottomSheet(listener: ChooseAddressBottomSheet.ChooseAddressBottomSheetListener) {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(listener)
        chooseAddressBottomSheet.show(
            childFragmentManager,
            ProductDetailCommonBottomSheetBuilder.ATC_VAR_SHIPPING_CHOOSE_ADDRESS_TAG
        )
    }

    private fun goToWishlist() {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    private fun checkLogin(): Boolean {
        var goToLogin = false
        if (!userSessionInterface.isLoggedIn) {
            baseAtcBtn?.showLoading()
            activity?.let {
                goToLogin = true
                startActivityForResult(
                    RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    ATC_LOGIN_REQUEST_CODE
                )
            }
        }
        return goToLogin
    }

    private fun showErrorVariantUnselected() {
        val variantErrorMessage =
            if (viewModel.getVariantData()?.getVariantsIdentifier()?.isEmpty() == true) {
                getString(productdetailcommonR.string.add_to_cart_error_variant)
            } else {
                getString(
                    productdetailcommonR.string.add_to_cart_error_variant_builder,
                    viewModel.getVariantData()?.getVariantsIdentifier()
                        ?: ""
                )
            }

        val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
        ProductTrackingCommon.onVariantPartiallySelected(
            variantErrorMessage,
            adapter.getHeaderDataModel()?.productId
                ?: "",
            pageSource
        )
        showToasterError(variantErrorMessage, getString(R.string.atc_variant_oke_label))
    }

    private fun renderButton(data: PartialButtonDataModel) {
        baseAtcBtn?.renderButtonView(
            data.isProductSelectedBuyable,
            data.isShopOwner,
            data.cartTypeData
        )
    }

    private fun logException(t: Throwable) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(Exception(t))
        } else {
            t.printStackTrace()
        }
    }

    private fun favoriteShop() {
        if (nplFollowersButton?.getButtonLoadingStatus() == false) {
            val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
            val productId = adapter.getHeaderDataModel()?.productId ?: ""
            val shopId = viewModel.getVariantAggregatorData()?.simpleBasicInfo?.shopID ?: ""
            ProductTrackingCommon.onFollowNplClickedVariantBottomSheet(
                productId,
                pageSource,
                shopId
            )
            viewModel.toggleFavorite(shopId)
            nplFollowersButton?.startLoading()
        }
    }

    override fun onButtonFollowNplClick() {
        val reData = (viewModel.restrictionData.value as? Success)?.data ?: return

        if (reData.restrictionShopFollowersType()) {
            favoriteShop()
        } else if (reData.restrictionCategoriesType()) {
            reData.action.firstOrNull()?.buttonLink?.let {
                if (it.isEmpty()) {
                    activity?.finish()
                } else {
                    RouteManager.route(context, it)
                }
            }
        } else if (reData.restrictionGamificationType()) {
            reData.action.firstOrNull()?.buttonLink?.let {
                RouteManager.route(context, it)
            }
        } else if (reData.restrictionLocationType()) {
            onRestrictionLocationClicked(reData)
        }
    }

    private fun onRestrictionLocationClicked(reData: RestrictionData) {
        ProductTrackingCommon.Restriction.clickLocationRestriction(
            data = reData,
            userId = userSessionInterface.userId,
            shopId = viewModel.getVariantAggregatorData()?.simpleBasicInfo?.shopID ?: "",
            pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
        )
        goToChooseAddress(Int.ZERO)
    }

    override fun onTokoCabangClicked(uspImageUrl: String) {
        context?.let {
            val isTokoNow = sharedViewModel.aggregatorParams.value?.isTokoNow == true
            val pageSource = sharedViewModel.aggregatorParams.value?.pageSource ?: ""
            val productId = adapter.getHeaderDataModel()?.productId ?: ""

            if (isTokoNow) {
                RouteManager.route(context, EDUCATIONAL_INFO)
            } else {
                val bottomSheet =
                    ProductDetailCommonBottomSheetBuilder.getUspBottomSheet(it, uspImageUrl)
                bottomSheet.show(
                    childFragmentManager,
                    ProductDetailCommonBottomSheetBuilder.TAG_USP_BOTTOM_SHEET
                )
            }

            ProductTrackingCommon.onTokoCabangClicked(productId, pageSource)
        }
    }

    override fun getCartRedirectionButtonsByteIOTrackerViewModel() = viewModel

    override fun getCartRedirectionButtonsByteIOTrackerActionType() = buttonActionType
}

interface AtcVariantBottomSheetListener {
    fun onTokoCabangClicked(uspImageUrl: String)
}
