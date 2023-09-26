package com.tokopedia.digital_checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.AtcErrorButton
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalCache
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.DigitalPromoCheckoutPageConst.EXTRA_COUPON_ACTIVE
import com.tokopedia.digital_checkout.data.DigitalPromoCheckoutPageConst.EXTRA_PROMO_DIGITAL_MODEL
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CollectionPointMetadata
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.digital_checkout.databinding.FragmentDigitalCheckoutPageBinding
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCartDetailInfoAdapter
import com.tokopedia.digital_checkout.presentation.adapter.DigitalMyBillsAdapter
import com.tokopedia.digital_checkout.presentation.adapter.vh.MyBillsActionListener
import com.tokopedia.digital_checkout.presentation.bottomsheet.DigitalPlusMoreInfoBottomSheet
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.presentation.widget.DigitalCartInputPriceWidget
import com.tokopedia.digital_checkout.presentation.widget.DigitalCheckoutSimpleWidget
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DeviceUtil.generateATokenRechargeCheckout
import com.tokopedia.digital_checkout.utils.DigitalCheckoutUtil
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.digital_checkout.utils.PromoDataUtil.mapToStatePromoCheckout
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.build
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.DataElements
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.resources.common.R as CommonRes

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartFragment :
    BaseDaggerFragment(),
    MyBillsActionListener,
    DigitalCartInputPriceWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var digitalAnalytics: DigitalAnalytics

    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var gson: Gson

    @Suppress("LateinitUsage")
    private lateinit var cartDetailInfoAdapter: DigitalCartDetailInfoAdapter

    @Suppress("LateinitUsage")
    private lateinit var myBillsAdapter: DigitalMyBillsAdapter

    private val viewModel by viewModels<DigitalCartViewModel> { viewModelFactory }
    private val addToCartViewModel by viewModels<DigitalAddToCartViewModel> { viewModelFactory }
    private var binding by autoClearedNullable<FragmentDigitalCheckoutPageBinding>()

    private var cartPassData: DigitalCheckoutPassData? = null
    private var digitalSubscriptionParams: DigitalSubscriptionParams = DigitalSubscriptionParams()
    private var isATCFailed: Boolean = false

    private var renderCrossSellConsentJob: Job? = null
    private var renderProductConsentJob: Job? = null
    private var productCollectionPointMetadata = CollectionPointMetadata()

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartPassData = arguments?.getParcelable(ARG_PASS_DATA)
        val subParams: DigitalSubscriptionParams? =
            arguments?.getParcelable(ARG_SUBSCRIPTION_PARAMS)
        if (subParams != null) {
            digitalSubscriptionParams = subParams
        }
    }

    override fun initInjector() {
        getComponent(DigitalCheckoutComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalCheckoutPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // if user activate don't keep activities
        if (savedInstanceState != null) {
            viewModel.setPromoData(
                savedInstanceState.getParcelable(EXTRA_STATE_PROMO_DATA)
                    ?: PromoData()
            )
            viewModel.requestCheckoutParam =
                savedInstanceState.getParcelable(EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER)
                    ?: DigitalCheckoutDataParameter()
            cartPassData?.needGetCart = true
            isATCFailed = savedInstanceState.getBoolean(EXTRA_IS_ATC_ERROR)
        } else {
            viewModel.requestCheckoutParam = DigitalCheckoutDataParameter()
        }

        viewModel.requestCheckoutParam.deviceId =
            cartPassData?.deviceId ?: DEFAULT_ANDROID_DEVICE_ID

        initViews()
        requestDataWithAuth()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // save userInputView value for don't keep activities
        viewModel.requestCheckoutParam.userInputPriceValue = binding?.inputPriceHolderView?.getPriceInput()

        outState.putParcelable(EXTRA_STATE_PROMO_DATA, viewModel.promoData.value)
        outState.putParcelable(
            EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER,
            viewModel.requestCheckoutParam
        )
        outState.putBoolean(EXTRA_IS_ATC_ERROR, isATCFailed)
        super.onSaveInstanceState(outState)
    }

    private fun requestDataWithAuth() {
        if (userSession.isLoggedIn) {
            loadData()
        } else {
            RouteManager.getIntent(context, ApplinkConst.LOGIN).apply {
                startActivityForResult(this, REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun loadData() {
        cartPassData?.let {
            if (isATCFailed) {
                requestAddToCart(it)
            } else if (it.isFromPDP || it.needGetCart) {
                requestGetCart(it)
            } else {
                requestAddToCart(it)
            }
        }
    }

    private fun requestGetCart(passData: DigitalCheckoutPassData) {
        val categoryId = passData.categoryId ?: ""
        viewModel.getCart(
            categoryId,
            getString(R.string.digital_cart_login_message),
            passData.isSpecialProduct
        )
    }

    private fun requestAddToCart(passData: DigitalCheckoutPassData) {
        hideContent()
        resetAtcError()
        binding?.loaderCheckout?.visible()
        passData.idemPotencyKey = generateATokenRechargeCheckout(requireContext())
        addToCartViewModel.addToCart(
            passData,
            getDigitalIdentifierParam()
        )
    }

    private fun getCartAfterCheckout() {
        cartPassData?.let {
            val categoryId = cartPassData?.categoryId ?: ""
            val isSpecialProduct = cartPassData?.isSpecialProduct ?: false
            viewModel.getCart(
                categoryId,
                getString(R.string.digital_cart_login_message),
                isSpecialProduct
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addToCartViewModel.addToCartResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> viewModel.getCart(
                    it.data,
                    isSpecialProduct = cartPassData?.isSpecialProduct
                        ?: false
                )
                is Fail -> {
                    updateAtcError()
                    closeViewWithMessageAlert(it.throwable)
                }
            }
        }

        addToCartViewModel.errorAtc.observe(viewLifecycleOwner) {
            updateAtcError()
            showErrorPage(it)
        }

        viewModel.cartDigitalInfoData.observe(viewLifecycleOwner) {
            renderCartDigitalInfoData(it)
            renderCartBasedOnParamState()
        }

        viewModel.errorThrowable.observe(viewLifecycleOwner) {
            closeViewWithMessageAlert(it.throwable)
        }

        viewModel.cancelVoucherData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.defaultEmptyPromoMessage.isNotEmpty()) {
                        binding?.checkoutBottomViewWidget?.promoButtonTitle = it.data.defaultEmptyPromoMessage
                    }
                }
                is Fail -> onFailedCancelVoucher(it.throwable)
            }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            it?.let { totalPrice ->
                binding?.checkoutBottomViewWidget?.totalPayment =
                    getStringIdrFormat((totalPrice - getPromoData().amount))
            }
        }

        viewModel.showContentCheckout.observe(viewLifecycleOwner) { showContent ->
            if (showContent) showContent() else hideContent()
        }

        viewModel.showLoading.observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) {
                binding?.loaderCheckout?.visible()
            } else {
                binding?.loaderCheckout?.gone()
            }
        }

        viewModel.isNeedOtp.observe(viewLifecycleOwner) {
            interruptRequestTokenVerification(it)
        }

        viewModel.paymentPassData.observe(viewLifecycleOwner) {
            redirectToTopPayActivity(it)
        }

        viewModel.payment.observe(viewLifecycleOwner) { payment ->
            binding?.checkoutSummaryWidget?.setSummaries(payment)
        }

        observePromoData()
    }

    private fun observePromoData() {
        viewModel.promoData.observe(viewLifecycleOwner) {
            viewModel.applyPromoData(it)

            if (getPromoData().state == TickerCheckoutView.State.INACTIVE) {
                binding?.checkoutBottomViewWidget?.disableVoucherView()
                return@observe
            }

            binding?.checkoutBottomViewWidget?.let { checkoutWidget ->

                checkoutWidget.promoButtonDescription = getPromoData().description
                if (getPromoData().description.isEmpty()) {
                    renderDefaultEmptyPromoView()
                } else {
                    checkoutWidget.promoButtonTitle = getPromoData().title
                }
                binding?.checkoutBottomViewWidget?.promoButtonState =
                    getPromoData().state.mapToStatePromoCheckout()

                when (getPromoData().state) {
                    TickerCheckoutView.State.ACTIVE -> {
                        cartDetailInfoAdapter.isExpanded = true
                        binding?.checkoutBottomViewWidget?.promoButtonChevronIcon =
                            CommonRes.drawable.ic_system_action_close_grayscale_24
                    }
                    TickerCheckoutView.State.FAILED -> cartDetailInfoAdapter.isExpanded = true
                    else -> {
                    }
                }
            }
        }
    }

    private fun renderDefaultEmptyPromoView() {
        binding?.checkoutBottomViewWidget?.let {
            it.promoButtonTitle = getString(R.string.digital_checkout_promo_title)
            it.promoButtonChevronIcon = CommonRes.drawable.ic_system_action_arrow_right_grayscale_24
        }
    }

    private fun showContent() {
        binding?.let {
            it.contentCheckout.visible()
            it.checkoutBottomViewWidget.visible()
            it.viewEmptyState.gone()
        }
    }

    private fun renderCartDigitalInfoData(cartInfo: CartDigitalInfoData) {
        digitalSubscriptionParams.isSubscribed = cartInfo.isSubscribed
        sendGetCartAndCheckoutAnalytics()

        if (cartInfo.attributes.isOpenAmount) {
            renderInputPriceView(
                cartInfo.attributes.pricePlain.toLong(),
                cartInfo.attributes.userInputPrice
            )
        }

        val ifSubscriptionCheckboxDisabled = cartInfo.attributes.fintechProduct.firstOrNull {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }?.checkBoxDisabled
        if (ifSubscriptionCheckboxDisabled == true) {
            binding?.checkoutBottomViewWidget?.hideCrossSellConsent()
        }

        renderMyBillsLayout(cartInfo)

        binding?.let {
            it.iconCheckout.loadImage(cartInfo.attributes.icon)
            it.productTitle.text = cartInfo.attributes.categoryName
            cartDetailInfoAdapter.setInfoItems(cartInfo.mainInfo)
            cartDetailInfoAdapter.setAdditionalInfoItems(cartInfo.additionalInfos)
            it.containerSeeDetailToggle.showWithCondition(cartInfo.additionalInfos.isNotEmpty())

            if (!digitalSubscriptionParams.isSubscribed) {
                renderPostPaidPopup(cartInfo.attributes.postPaidPopupAttribute)
            }
            if (cartInfo.collectionPointId.isNotEmpty()) {
                productCollectionPointMetadata = CollectionPointMetadata(
                    cartInfo.collectionPointId,
                    cartInfo.collectionPointVersion
                )
                renderProductConsentWidget(
                    productCollectionPointMetadata,
                    cartInfo.collectionDataElements
                )
                it.checkoutBottomViewWidget.showProductConsent()
                it.checkoutBottomViewWidget.isCheckoutButtonEnabled = false
            }
        }
    }

    private fun renderCartBasedOnParamState() {
        viewModel.requestCheckoutParam.let { param ->
            // render input user
            if (param.userInputPriceValue != null) {
                binding?.inputPriceHolderView?.setPriceInput(param.userInputPriceValue)
            }

            // render fintechProduct & subscription
            if (param.isSubscriptionChecked) myBillsAdapter.setActiveSubscriptions()
            if (param.crossSellProducts.isNotEmpty()) {
                val fintechProducts = hashMapOf<String, FintechProduct>()
                param.crossSellProducts.forEach {
                    if (!it.value.isSubscription) {
                        fintechProducts[it.key] = it.value.product
                    }
                }
                myBillsAdapter.setActiveFintechProducts(fintechProducts)
            }
        }
    }

    private fun getDigitalIdentifierParam(): RequestBodyIdentifier =
        DeviceUtil.getDigitalIdentifierParam(requireActivity())

    private fun initViews() {
        // init recyclerview
        binding?.let {
            cartDetailInfoAdapter = DigitalCartDetailInfoAdapter(object : DigitalCartDetailInfoAdapter.ActionListener {
                override fun expandAdditionalList() {
                    it.tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_close_label)
                    it.ivSeeDetail.loadImage(CommonRes.drawable.ic_system_action_arrow_up_normal_24)
                }

                override fun collapseAdditionalList() {
                    it.tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_see_detail_label)
                    it.ivSeeDetail.loadImage(CommonRes.drawable.ic_system_action_arrow_down_normal_24)
                }
            })
            it.rvDetails.layoutManager = LinearLayoutManager(context)
            it.rvDetails.isNestedScrollingEnabled = false
            it.rvDetails.adapter = cartDetailInfoAdapter
            it.containerSeeDetailToggle.setOnClickListener {
                cartDetailInfoAdapter.toggleIsExpanded()
            }

            showPromoTicker()

            it.checkoutBottomViewWidget.setCheckoutButtonListener {
                if (it.checkoutBottomViewWidget.isProductConsentWidgetVisible()) {
                    val consentPayload = it.checkoutBottomViewWidget.getProductConsentPayload()
                    viewModel.updateProductConsentPayload(consentPayload)
                }
                if ((
                    viewModel.requestCheckoutParam.isSubscriptionChecked &&
                        it.checkoutBottomViewWidget.isProductConsentWidgetVisible()
                    ) ||
                    it.checkoutBottomViewWidget.isCrossSellConsentWidgetVisible()
                ) {
                    val consentPayload = it.checkoutBottomViewWidget.getCrossSellConsentPayload()
                    viewModel.updateSubscriptionMetadata(consentPayload)
                }
                viewModel.proceedToCheckout(getDigitalIdentifierParam())
            }

            it.checkoutBottomViewWidget.setOnClickConsentListener { url ->
                RouteManager.route(context, url)
            }
        }
    }

    private fun showError(error: Throwable) {
        hideContent()
        val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(
            requireContext(),
            error,
            ErrorHandler.Builder().build()
        )
        binding?.viewEmptyState?.let { viewEmptyState ->
            viewEmptyState.setActionClickListener {
                viewEmptyState.gone()
                loadData()
            }

            if (errMsg == ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL || errMsg == ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION ||
                errMsg == ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            ) {
                viewEmptyState.setType(GlobalError.NO_CONNECTION)
            } else if (errMsg == ErrorNetMessage.MESSAGE_ERROR_SERVER || errMsg == ErrorNetMessage.MESSAGE_ERROR_DEFAULT) {
                viewEmptyState.setType(GlobalError.SERVER_ERROR)
            } else {
                viewEmptyState.errorTitle.text = getString(R.string.digital_checkout_empty_state_title)
                viewEmptyState.errorIllustration.loadImage(getString(R.string.digital_cart_default_error_img_url))
                viewEmptyState.errorDescription.text = getString(
                    R.string.digital_cart_error_message,
                    errMsg,
                    errCode
                )
            }

            viewEmptyState.errorAction.text = getString(R.string.digital_checkout_empty_state_btn)
            viewEmptyState.visible()
        }
    }

    private fun showErrorPage(error: ErrorAtc) {
        binding?.viewEmptyState?.let {
            binding?.loaderCheckout?.gone()
            hideContent()

            it.errorIllustration.loadImageFitCenter(error.atcErrorPage.imageUrl)
            it.errorTitle.text = error.atcErrorPage.title.ifEmpty { error.title }
            it.errorDescription.text = error.atcErrorPage.subTitle
            it.errorSecondaryAction.gone()

            it.visible()

            val categoryId = cartPassData?.categoryId ?: ""
            val operatorId = cartPassData?.operatorId ?: ""

            if (error.atcErrorPage.buttons.isNullOrEmpty()) {
                it.errorAction.text = getString(R.string.digital_checkout_empty_state_btn)
                it.setActionClickListener { _ ->
                    it.gone()
                    loadData()
                }
                return@let
            }

            val button = error.atcErrorPage.buttons.first()

            it.errorAction.text = button.label

            digitalAnalytics.eventViewErrorPage(categoryId, operatorId, button.actionType)

            it.setActionClickListener {
                digitalAnalytics.eventClickErrorButton(categoryId, operatorId, button.actionType)
                if (button.actionType == AtcErrorButton.TYPE_PHONE_VERIFICATION) {
                    RouteManager.getIntent(context, button.appLinkUrl).apply {
                        startActivityForResult(this, REQUEST_VERIFY_PHONE_NUMBER)
                    }
                } else {
                    RouteManager.route(context, button.appLinkUrl)
                }
            }
        }
    }

    private fun showPromoTicker() {
        renderDefaultEmptyPromoView()

        binding?.checkoutBottomViewWidget?.let { checkoutBottomView ->
            checkoutBottomView.setDigitalPromoButtonListener { onClickUsePromo() }

            checkoutBottomView.setButtonChevronIconListener {
                if (checkoutBottomView.promoButtonDescription.isNotEmpty()) {
                    checkoutBottomView.promoButtonState = ButtonPromoCheckoutView.State.LOADING
                    onResetPromoDiscount()
                } else {
                    onClickUsePromo()
                }
            }
            checkoutBottomView.promoButtonVisibility = View.VISIBLE
        }
    }

    private fun onFailedCancelVoucher(throwable: Throwable) {
        binding?.checkoutBottomViewWidget?.promoButtonState = ButtonPromoCheckoutView.State.ACTIVE
        showToastMessage(ErrorHandler.getErrorMessage(activity, throwable))
    }

    private fun showToastMessage(message: String) {
        build(
            requireView(),
            message,
            Snackbar.LENGTH_LONG,
            TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.close),
            View.OnClickListener { /** do nothing **/ }
        ).show()
    }

    private fun closeViewWithMessageAlert(error: Throwable) {
        binding?.loaderCheckout?.gone()
        if (cartPassData?.isFromPDP == true) {
            val intent = Intent()
            intent.putExtra(DigitalExtraParam.EXTRA_MESSAGE, error)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            showError(error)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == REQUEST_CODE_PROMO_LIST || requestCode == REQUEST_CODE_PROMO_DETAIL) && resultCode == Activity.RESULT_OK) {
            if (data?.hasExtra(EXTRA_PROMO_DATA) == true) {
                viewModel.setPromoData(data.getParcelableExtra(EXTRA_PROMO_DATA) ?: PromoData())
            }
        } else if (requestCode == REQUEST_CODE_OTP) {
            if (resultCode == Activity.RESULT_OK) {
                cartPassData?.let {
                    viewModel.processPatchOtpCart(
                        getDigitalIdentifierParam(),
                        it,
                        getString(R.string.digital_cart_login_message),
                        cartPassData?.isSpecialProduct ?: false
                    )
                }
            } else {
                activity?.finish()
            }
        } else if (requestCode == PaymentConstant.REQUEST_CODE) {
            val categoryId = cartPassData?.categoryId ?: ""
            when (resultCode) {
                PaymentConstant.PAYMENT_SUCCESS -> {
                    if (categoryId.isNotEmpty()) {
                        PersistentCacheManager.instance.delete(DigitalCache.NEW_DIGITAL_CATEGORY_AND_FAV + "/" + categoryId)
                    }
                }
                PaymentConstant.PAYMENT_FAILED -> {
                    showToastMessage(getString(R.string.digital_cart_alert_payment_canceled_or_failed))
                    resetCrossSellData()
                    getCartAfterCheckout()
                }
                PaymentConstant.PAYMENT_CANCELLED -> {
                    showToastMessage(getString(R.string.digital_cart_alert_payment_canceled))
                    resetCrossSellData()
                    getCartAfterCheckout()
                }
                else -> getCartAfterCheckout()
            }
        } else if (requestCode == REQUEST_VERIFY_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
            loadData()
        } else if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                loadData()
            } else {
                activity?.finish()
            }
        }
    }

    private fun renderPostPaidPopup(postPaidPopupAttribute: AttributesDigitalData.PostPaidPopupAttribute) {
        if (postPaidPopupAttribute.title.isNotEmpty() || postPaidPopupAttribute.content.isNotEmpty()) {
            val dialog = DialogUnify(
                requireActivity(),
                DialogUnify.SINGLE_ACTION,
                DialogUnify.WITH_ILLUSTRATION
            )

            dialog.setTitle(postPaidPopupAttribute.title)
            dialog.setDescription(MethodChecker.fromHtml(postPaidPopupAttribute.content))
            dialog.setImageUrl(postPaidPopupAttribute.imageUrl)
            dialog.setPrimaryCTAText(postPaidPopupAttribute.confirmButtonTitle)
            dialog.setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun renderMyBillsLayout(cartInfo: CartDigitalInfoData) {
        myBillsAdapter = DigitalMyBillsAdapter(this)

        binding?.rvMyBills?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.isNestedScrollingEnabled = false
            it.adapter = myBillsAdapter
        }

        val (subscriptions, fintechProducts) = cartInfo.attributes.fintechProduct.partition {
            it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
        }
        myBillsAdapter.setItems(subscriptions, fintechProducts)
    }

    override fun onSubscriptionChecked(fintechProduct: FintechProduct, isChecked: Boolean) {
        digitalAnalytics.eventClickSubscription(
            isChecked,
            getCategoryName(),
            getOperatorName(),
            userSession.userId
        )
        binding?.run {
            handleCrossSellConsent(fintechProduct, isChecked)
            viewModel.onSubscriptionChecked(fintechProduct, isChecked)
        }
    }

    private fun handleCrossSellConsent(fintechProduct: FintechProduct, isSubscriptionChecked: Boolean) {
        binding?.run {
            if (isSubscriptionChecked) {
                val collectionPointMetadata = getCollectionPointData(fintechProduct)
                if (collectionPointMetadata.collectionPointId.isNotEmpty()) {
                    if (!hasProductConsent()) {
                        checkoutBottomViewWidget.isCheckoutButtonEnabled = false
                        checkoutBottomViewWidget.showCrossSellConsent()
                    }
                    renderCrossSellConsentWidget(collectionPointMetadata, !hasProductConsent())
                }
            } else {
                renderCrossSellConsentJob?.cancel()
                if (!hasProductConsent()) {
                    checkoutBottomViewWidget.isCheckoutButtonEnabled = true
                }
                checkoutBottomViewWidget.hideCrossSellConsent()
            }
        }
    }

    private fun getCollectionPointData(fintechProduct: FintechProduct): CollectionPointMetadata {
        try {
            var map: Map<String, Any> = hashMapOf()
            map = gson.fromJson(fintechProduct.crossSellMetadata, map.javaClass)

            val metadataKey = map[KEY_METADATA]
            if (metadataKey != null && metadataKey.toString().length > Int.ZERO) {
                return gson.fromJson(metadataKey.toString(), CollectionPointMetadata::class.java)
            }
        } catch (e: Exception) {
            DigitalCheckoutUtil.logExceptionToCrashlytics(e)
        }

        return CollectionPointMetadata()
    }

    private fun renderCrossSellConsentWidget(
        collectionPointData: CollectionPointMetadata,
        isEnableCheckoutButtonInteraction: Boolean
    ) {
        binding?.run {
            if (collectionPointData.collectionPointId.isNotEmpty()) {
                renderCrossSellConsentJob?.cancel()
                renderCrossSellConsentJob = lifecycleScope.launch {
                    val consentParam = ConsentCollectionParam(
                        collectionId = collectionPointData.collectionPointId,
                        version = collectionPointData.collectionPointVersion,
                        identifier = userSession.userId
                    )
                    checkoutBottomViewWidget.setCrossSellConsentWidget(
                        viewLifecycleOwner,
                        this@DigitalCartFragment,
                        consentParam,
                        isEnableCheckoutButtonInteraction
                    )
                }
            }
        }
    }

    private fun renderProductConsentWidget(
        collectionPointData: CollectionPointMetadata,
        collectionDataElements: List<RechargeGetCart.CollectionDataElements>
    ) {
        binding?.run {
            if (collectionPointData.collectionPointId.isNotEmpty()) {
                renderProductConsentJob?.cancel()
                renderProductConsentJob = lifecycleScope.launch {
                    val consentParam = ConsentCollectionParam(
                        collectionId = collectionPointData.collectionPointId,
                        version = collectionPointData.collectionPointVersion,
                        dataElements = collectionDataElements.map {
                            DataElements(it.key, it.value)
                        }.toMutableList(),
                        identifier = userSession.userId
                    )

                    checkoutBottomViewWidget.setProductConsentWidget(
                        viewLifecycleOwner,
                        this@DigitalCartFragment,
                        consentParam,
                        collectionDataElements.isNotEmpty()
                    )
                }
            }
        }
    }

    override fun onSubscriptionImpression(fintechProduct: FintechProduct) {
        digitalAnalytics.eventImpressionSubscription(
            userSession.userId,
            fintechProduct.checkBoxDisabled,
            getCategoryName(),
            getOperatorName()
        )
    }

    override fun onSubscriptionMoreInfoClicked(fintechProduct: FintechProduct) {
        digitalAnalytics.eventSubscriptionMoreInfoClicked(
            userSession.userId,
            getCategoryName(),
            getOperatorName()
        )
        binding?.checkoutBottomViewWidget?.let {
            if (isGotoPlus()) {
                renderPlusSubscriptionMoreInfoBottomSheet()
            } else {
                renderSubscriptionMoreInfoBottomSheet()
            }
        }
    }

    override fun onTebusMurahImpression(fintechProduct: FintechProduct, position: Int) {
        digitalAnalytics.eventTebusMurahImpression(
            fintechProduct,
            getCategoryName(),
            position,
            userSession.userId
        )
    }

    override fun onCrossellImpression(fintechProduct: FintechProduct, position: Int) {
        digitalAnalytics.eventImpressionCrossSell(
            fintechProduct,
            getCategoryName(),
            position,
            userSession.userId
        )
    }

    override fun onTebusMurahChecked(
        fintechProduct: FintechProduct,
        position: Int,
        isChecked: Boolean
    ) {
        if (isChecked) {
            digitalAnalytics.eventTebusMurahChecked(
                fintechProduct,
                getCategoryName(),
                position,
                userSession.userId
            )
        } else {
            digitalAnalytics.eventTebusMurahUnchecked(
                fintechProduct,
                getCategoryName(),
                userSession.userId
            )
        }
        viewModel.onFintechProductChecked(fintechProduct, isChecked, getPriceInput())
    }

    override fun onFintechProductChecked(
        fintechProduct: FintechProduct,
        isChecked: Boolean,
        position: Int
    ) {
        if (isChecked) {
            digitalAnalytics.eventClickCrossSell(
                fintechProduct,
                getCategoryName(),
                position,
                userSession.userId
            )
        } else {
            digitalAnalytics.eventUnclickCrossSell(fintechProduct, userSession.userId)
        }
        viewModel.onFintechProductChecked(fintechProduct, isChecked, getPriceInput())
    }

    override fun onFintechMoreInfoChecked(info: FintechProduct.FintechProductInfo) {
        renderFintechProductMoreInfo(info)
    }

    private fun renderFintechProductMoreInfo(fintechProductInfo: FintechProduct.FintechProductInfo) {
        if (fintechProductInfo.urlLink.isNotEmpty()) {
            RouteManager.route(
                context,
                fintechProductInfo.urlLink
            )
        } else if (fintechProductInfo.tooltipText.isNotEmpty()) {
            val moreInfoView = View.inflate(
                context,
                R.layout.layout_digital_fintech_product_info_bottom_sheet,
                null
            )
            val moreInfoText: Typography = moreInfoView.findViewById(R.id.egold_tooltip)
            moreInfoText.setPadding(
                Int.ZERO,
                Int.ZERO,
                Int.ZERO,
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
            )
            moreInfoText.text = MethodChecker.fromHtml(fintechProductInfo.tooltipText)

            val moreInfoBottomSheet = BottomSheetUnify()
            moreInfoBottomSheet.setTitle(
                MethodChecker.fromHtml(fintechProductInfo.title).toString()
            )
            moreInfoBottomSheet.isFullpage = false
            moreInfoBottomSheet.setChild(moreInfoView)
            moreInfoBottomSheet.clearAction()
            moreInfoBottomSheet.setCloseClickListener {
                moreInfoBottomSheet.dismiss()
            }
            moreInfoBottomSheet.show(childFragmentManager, MORE_INFO_BOTTOM_SHEET_TAG)
        }
    }

    private fun renderInputPriceView(
        total: Long,
        userInputPriceDigital: AttributesDigitalData.UserInputPriceDigital?
    ) {
        userInputPriceDigital?.let {
            binding?.inputPriceContainer?.visible()

            if (!userInputPriceDigital.minPayment.isNullOrEmpty()) {
                binding?.inputPriceHolderView?.actionListener = this
            }

            val minPayment = userInputPriceDigital.minPayment ?: ""
            val maxPayment = userInputPriceDigital.maxPayment ?: ""
            binding?.inputPriceHolderView?.setMinMaxPayment(
                userInputPriceDigital.minPaymentPlain.toLong(),
                userInputPriceDigital.maxPaymentPlain.toLong(),
                minPayment,
                maxPayment
            )
            binding?.inputPriceHolderView?.setPriceInput(total)
        }
    }

    override fun onInputPriceByUserFilled(paymentAmount: Long?) {
        viewModel.setTotalPriceBasedOnUserInput(paymentAmount?.toDouble() ?: ZERO_DOUBLE)
        viewModel.setSubtotalPaymentSummaryOnUserInput(paymentAmount?.toDouble() ?: ZERO_DOUBLE)
    }

    override fun enableCheckoutButton() {
        binding?.checkoutBottomViewWidget?.isCheckoutButtonEnabled = true
    }

    override fun disableCheckoutButton() {
        binding?.checkoutBottomViewWidget?.isCheckoutButtonEnabled = false
    }

    private fun interruptRequestTokenVerification(phoneNumber: String?) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.COTP)
        val bundle = Bundle()
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_CHECKOUT_DIGITAL)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_OTP)
    }

    private fun redirectToTopPayActivity(paymentPassData: PaymentPassData) {
        val categoryId = cartPassData?.categoryId ?: ""
        digitalAnalytics.eventProceedToPayment(
            getCartDigitalInfoData(),
            getPromoData().promoCode,
            userSession.userId,
            categoryId
        )
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
    }

    private fun sendGetCartAndCheckoutAnalytics() {
        digitalAnalytics.sendCartScreen()
        val categoryId = cartPassData?.categoryId?.toIntSafely() ?: Int.ZERO
        rechargeAnalytics.trackAddToCartRechargePushEventRecommendation(categoryId)
    }

    private fun onClickUsePromo() {
        val attributes = getCartDigitalInfoData().attributes
        attributes.let { attr ->
            digitalAnalytics.eventClickUseVoucher(getCategoryName())
            digitalAnalytics.eventClickPromoButton(
                attr.categoryName,
                attributes.operatorName,
                userSession.userId
            )
        }
        navigateToPromoListPage()
    }

    private fun onResetPromoDiscount() {
        digitalAnalytics.eventClickCancelApplyCoupon(getCategoryName(), getPromoData().promoCode)
        viewModel.cancelVoucherCart(
            getPromoData().promoCode,
            getString(R.string.digital_checkout_error_remove_coupon_message)
        )
    }

    private fun navigateToPromoListPage() {
        val couponActive = viewModel.cartDigitalInfoData.value?.attributes?.isCouponActive == Int.ONE
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
        intent.putExtra(EXTRA_COUPON_ACTIVE, couponActive)
        intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
        startActivityForResult(intent, REQUEST_CODE_PROMO_LIST)
    }

    private fun hideContent() {
        binding?.let {
            it.contentCheckout.hide()
            it.checkoutBottomViewWidget.hide()
        }
    }

    private fun renderPlusSubscriptionMoreInfoBottomSheet() {
        val bottomSheet = DigitalPlusMoreInfoBottomSheet()
        bottomSheet.show(childFragmentManager)
    }

    private fun renderSubscriptionMoreInfoBottomSheet() {
        context?.let {
            val linearLayout = LinearLayout(it)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                Int.ZERO,
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_24)
            )

            val descriptionArray =
                context?.resources?.getStringArray(R.array.subscription_more_info_bottomsheet_description)
                    ?: emptyArray()
            descriptionArray.forEachIndexed { index, text ->
                val simpleWidget = DigitalCheckoutSimpleWidget(it)
                val leftContent = "${index + Int.ONE}."
                simpleWidget.setContent(leftContent, text)
                linearLayout.addView(simpleWidget)
            }

            val bottomSheetUnify = BottomSheetUnify()
            bottomSheetUnify.setTitle(getString(R.string.subscription_more_info_bottomsheet_title))
            bottomSheetUnify.setChild(linearLayout)
            bottomSheetUnify.setCloseClickListener {
                digitalAnalytics.eventSubscriptionMoreInfoCloseClicked(
                    userSession.userId,
                    getCategoryName(),
                    getOperatorName()
                )
                bottomSheetUnify.dismissAllowingStateLoss()
            }
            bottomSheetUnify.show(childFragmentManager, SUBSCRIPTION_BOTTOM_SHEET_TAG)

            digitalAnalytics.eventSubscriptionViewMoreInfoBottomSheet(
                userSession.userId,
                getCategoryName(),
                getOperatorName()
            )
        }
    }

    private fun resetCrossSellData() {
        viewModel.requestCheckoutParam.isSubscriptionChecked = false
        viewModel.requestCheckoutParam.crossSellProducts = hashMapOf()
    }

    private fun getPromoDigitalModel(): PromoDigitalModel =
        viewModel.getPromoDigitalModel(cartPassData, getPriceInput())

    private fun getCartDigitalInfoData(): CartDigitalInfoData = viewModel.cartDigitalInfoData.value
        ?: CartDigitalInfoData()

    private fun getCategoryName(): String = getCartDigitalInfoData().attributes.categoryName
    private fun getOperatorName(): String = getCartDigitalInfoData().attributes.operatorName
    private fun getPromoData(): PromoData = viewModel.promoData.value ?: PromoData()
    private fun getPriceInput(): Double? {
        return if (binding?.inputPriceHolderView?.getPriceInput() == null) {
            return null
        } else {
            binding?.inputPriceHolderView?.getPriceInput()?.toDouble()
        }
    }

    private fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return context?.resources?.getDimensionPixelSize(id) ?: Int.ZERO
    }

    private fun resetAtcError() {
        isATCFailed = false
    }

    private fun updateAtcError() {
        isATCFailed = true
    }

    private fun isGotoPlus(): Boolean {
        return getCategoryName().lowercase() == GOTO_PLUS_CATEGORY_NAME
    }

    private fun hasProductConsent(): Boolean {
        return productCollectionPointMetadata.collectionPointId.isNotEmpty()
    }

    companion object {

        const val ARG_PASS_DATA = "ARG_PASS_DATA"
        const val ARG_SUBSCRIPTION_PARAMS = "ARG_SUBSCRIPTION_PARAMS"

        private const val EXTRA_STATE_PROMO_DATA = "EXTRA_STATE_PROMO_DATA"
        private const val EXTRA_IS_ATC_ERROR = "EXTRA_IS_ATC_ERROR"
        private const val EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER =
            "EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER"

        private const val KEY_METADATA = "metadata"

        private const val REQUEST_VERIFY_PHONE_NUMBER = 1012
        private const val REQUEST_CODE_LOGIN = 1013
        private const val REQUEST_CODE_OTP = 1001
        const val OTP_TYPE_CHECKOUT_DIGITAL = 16
        private const val GOTO_PLUS_CATEGORY_ID = "129"
        private const val GOTO_PLUS_OPERATOR_ID = "9475"
        private const val GOTO_PLUS_CATEGORY_NAME = "plus"

        private const val DEFAULT_ANDROID_DEVICE_ID = 5
        private const val ZERO_DOUBLE = 0.0

        private const val SUBSCRIPTION_BOTTOM_SHEET_TAG = "SUBSCRIPTION_BOTTOM_SHEET_TAG"
        private const val MORE_INFO_BOTTOM_SHEET_TAG = "E-gold more info bottom sheet"

        fun newInstance(
            passData: DigitalCheckoutPassData?,
            subParams: DigitalSubscriptionParams?
        ): DigitalCartFragment {
            val fragment = DigitalCartFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_PASS_DATA, passData)
            bundle.putParcelable(ARG_SUBSCRIPTION_PARAMS, subParams)
            fragment.arguments = bundle
            return fragment
        }
    }
}
