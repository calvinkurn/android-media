package com.tokopedia.digital_checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
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
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCartDetailInfoAdapter
import com.tokopedia.digital_checkout.presentation.adapter.DigitalMyBillsAdapter
import com.tokopedia.digital_checkout.presentation.adapter.vh.MyBillsActionListener
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.presentation.widget.DigitalCartInputPriceWidget
import com.tokopedia.digital_checkout.presentation.widget.DigitalCheckoutSimpleWidget
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.DigitalCurrencyUtil.getStringIdrFormat
import com.tokopedia.digital_checkout.utils.PromoDataUtil.mapToStatePromoCheckout
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
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
import kotlinx.android.synthetic.main.fragment_digital_checkout_page.*
import javax.inject.Inject

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartFragment : BaseDaggerFragment(), MyBillsActionListener,
    DigitalCartInputPriceWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var digitalAnalytics: DigitalAnalytics

    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DigitalCartViewModel::class.java) }
    private val addToCartViewModel by lazy { viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java) }

    private var cartPassData: DigitalCheckoutPassData? = null
    private var digitalSubscriptionParams: DigitalSubscriptionParams = DigitalSubscriptionParams()
    lateinit var cartDetailInfoAdapter: DigitalCartDetailInfoAdapter
    lateinit var myBillsAdapter: DigitalMyBillsAdapter

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
        return inflater.inflate(R.layout.fragment_digital_checkout_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if user activate don't keep activities
        if (savedInstanceState != null) {
            viewModel.setPromoData(
                savedInstanceState.getParcelable(EXTRA_STATE_PROMO_DATA)
                    ?: PromoData()
            )
            viewModel.requestCheckoutParam =
                savedInstanceState.getParcelable(EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER)
                    ?: DigitalCheckoutDataParameter()
            cartPassData?.needGetCart = true
        } else {
            viewModel.requestCheckoutParam = DigitalCheckoutDataParameter()
        }

        viewModel.requestCheckoutParam.deviceId =
            cartPassData?.deviceId ?: DEFAULT_ANDROID_DEVICE_ID

        initViews()
        loadData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //save userInputView value for don't keep activities
        viewModel.requestCheckoutParam.userInputPriceValue = inputPriceHolderView.getPriceInput()

        outState.putParcelable(EXTRA_STATE_PROMO_DATA, viewModel.promoData.value)
        outState.putParcelable(
            EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER,
            viewModel.requestCheckoutParam
        )
        super.onSaveInstanceState(outState)
    }

    private fun loadData() {
        cartPassData?.let {
            if (it.isFromPDP || it.needGetCart) {
                viewModel.getCart(
                    cartPassData?.categoryId
                        ?: "", getString(R.string.digital_cart_login_message),
                    cartPassData?.isSpecialProduct ?: false
                )
            } else {
                hideContent()
                loaderCheckout.visibility = View.VISIBLE
                addToCartViewModel.addToCart(
                    it,
                    getDigitalIdentifierParam(),
                    digitalSubscriptionParams
                )
            }
        }
    }

    private fun getCartAfterCheckout() {
        cartPassData?.let {
            viewModel.getCart(
                cartPassData?.categoryId
                    ?: "", getString(R.string.digital_cart_login_message),
                cartPassData?.isSpecialProduct ?: false
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addToCartViewModel.addToCartResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> viewModel.getCart(
                    it.data, isSpecialProduct = cartPassData?.isSpecialProduct
                        ?: false
                )
                is Fail -> closeViewWithMessageAlert(it.throwable)
            }
        })

        viewModel.cartDigitalInfoData.observe(viewLifecycleOwner, Observer {
            renderCartDigitalInfoData(it)
            renderCartBasedOnParamState()
        })

        viewModel.errorThrowable.observe(viewLifecycleOwner, Observer {
            closeViewWithMessageAlert(it.throwable)
        })

        viewModel.cancelVoucherData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.defaultEmptyPromoMessage.isNotEmpty()) {
                        checkoutBottomViewWidget.promoButtonTitle = it.data.defaultEmptyPromoMessage
                    }
                }
                is Fail -> onFailedCancelVoucher(it.throwable)
            }
        })

        viewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            it?.let { totalPrice ->
                checkoutBottomViewWidget.totalPayment =
                    getStringIdrFormat((totalPrice - getPromoData().amount))
            }
        })

        viewModel.showContentCheckout.observe(viewLifecycleOwner, Observer { showContent ->
            if (showContent) showContent() else hideContent()
        })

        viewModel.showLoading.observe(viewLifecycleOwner, Observer { showLoader ->
            if (showLoader) loaderCheckout.visibility = View.VISIBLE
            else loaderCheckout.visibility = View.GONE
        })

        viewModel.isNeedOtp.observe(viewLifecycleOwner, Observer {
            interruptRequestTokenVerification(it)
        })

        viewModel.paymentPassData.observe(viewLifecycleOwner, Observer {
            redirectToTopPayActivity(it)
        })

        viewModel.payment.observe(viewLifecycleOwner, Observer { payment ->
            checkoutSummaryWidget.setSummaries(payment)
        })

        observePromoData()
    }

    private fun observePromoData() {
        viewModel.promoData.observe(viewLifecycleOwner, Observer {
            viewModel.applyPromoData(it)

            if (getPromoData().state == TickerCheckoutView.State.INACTIVE) {
                checkoutBottomViewWidget.disableVoucherView()
                return@Observer
            }

            checkoutBottomViewWidget.promoButtonDescription = getPromoData().description
            if (getPromoData().description.isEmpty()) {
                renderDefaultEmptyPromoView()
            } else {
                checkoutBottomViewWidget.promoButtonTitle = getPromoData().title
            }
            checkoutBottomViewWidget.promoButtonState =
                getPromoData().state.mapToStatePromoCheckout()

            when (getPromoData().state) {
                TickerCheckoutView.State.ACTIVE -> {
                    cartDetailInfoAdapter.isExpanded = true
                    checkoutBottomViewWidget.promoButtonChevronIcon =
                        com.tokopedia.resources.common.R.drawable.ic_system_action_close_grayscale_24
                }
                TickerCheckoutView.State.FAILED -> cartDetailInfoAdapter.isExpanded = true
                else -> {
                }
            }
        })
    }

    private fun renderDefaultEmptyPromoView() {
        checkoutBottomViewWidget.promoButtonTitle = getString(R.string.digital_checkout_promo_title)
        checkoutBottomViewWidget.promoButtonChevronIcon =
            com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_right_grayscale_24
    }

    private fun showContent() {
        contentCheckout.visibility = View.VISIBLE
        checkoutBottomViewWidget.visibility = View.VISIBLE
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

        renderMyBillsLayout(cartInfo)

        cartInfo.attributes.icon.let { iconCheckout.loadImage(it) }
        productTitle.text = cartInfo.attributes.categoryName
        cartDetailInfoAdapter.setInfoItems(cartInfo.mainInfo)
        cartDetailInfoAdapter.setAdditionalInfoItems(cartInfo.additionalInfos)
        containerSeeDetailToggle.visibility =
            if (cartInfo.additionalInfos.isEmpty()) View.GONE else View.VISIBLE

        if (!digitalSubscriptionParams.isSubscribed) {
            renderPostPaidPopup(cartInfo.attributes.postPaidPopupAttribute)
        }
    }

    private fun disableVoucherView() {
        checkoutBottomViewWidget.disableVoucherView()
    }

    private fun renderCartBasedOnParamState() {
        viewModel.requestCheckoutParam.let { param ->
            //render input user
            if (param.userInputPriceValue != null) {
                inputPriceHolderView?.setPriceInput(param.userInputPriceValue)
            }

            //render fintechProduct & subscription
            if (param.isSubscriptionChecked) myBillsAdapter.setActiveSubscriptions()
            if (param.fintechProducts.isNotEmpty()) myBillsAdapter.setActiveFintechProducts(param.fintechProducts)
        }
    }

    private fun getDigitalIdentifierParam(): RequestBodyIdentifier =
        DeviceUtil.getDigitalIdentifierParam(requireActivity())


    private fun initViews() {
        // init recyclerview
        cartDetailInfoAdapter =
            DigitalCartDetailInfoAdapter(object : DigitalCartDetailInfoAdapter.ActionListener {
                override fun expandAdditionalList() {
                    tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_close_label)
                    ivSeeDetail.loadImageDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_normal_24)
                }

                override fun collapseAdditionalList() {
                    tvSeeDetailToggle.text =
                        getString(R.string.digital_cart_detail_see_detail_label)
                    ivSeeDetail.loadImageDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_normal_24)
                }
            })
        rvDetails.layoutManager = LinearLayoutManager(context)
        rvDetails.isNestedScrollingEnabled = false
        rvDetails.adapter = cartDetailInfoAdapter
        containerSeeDetailToggle.setOnClickListener {
            cartDetailInfoAdapter.toggleIsExpanded()
        }

        showPromoTicker()

        checkoutBottomViewWidget.setCheckoutButtonListener {
            viewModel.proceedToCheckout(getDigitalIdentifierParam())
        }
    }

    private fun showError(error: Throwable) {
        val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(
            requireContext(), error, ErrorHandler.Builder().build()
        )
        if (viewEmptyState != null) {
            viewEmptyState.setPrimaryCTAClickListener {
                viewEmptyState.visibility = View.GONE
                loadData()
            }

            if (errMsg == ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL || errMsg == ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION
                || errMsg == ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            ) {
                viewEmptyState.setTitle(getString(com.tokopedia.globalerror.R.string.noConnectionTitle))
                viewEmptyState.setImageDrawable(resources.getDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection))
                viewEmptyState.setDescription(
                    "${getString(com.tokopedia.globalerror.R.string.noConnectionDesc)} Kode Error: ($errCode)"
                )
            } else if (errMsg == ErrorNetMessage.MESSAGE_ERROR_SERVER || errMsg == ErrorNetMessage.MESSAGE_ERROR_DEFAULT) {
                viewEmptyState.setTitle(getString(com.tokopedia.globalerror.R.string.error500Title))
                viewEmptyState.setImageDrawable(resources.getDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500))
                viewEmptyState.setDescription(
                    "${getString(com.tokopedia.globalerror.R.string.error500Desc)} Kode Error: ($errCode)"
                )
            } else {
                viewEmptyState.setTitle(getString(R.string.digital_checkout_empty_state_title))
                viewEmptyState.setImageUrl(getString(R.string.digital_cart_default_error_img_url))
                viewEmptyState.setDescription("${errMsg}. Kode Error: ($errCode)")
            }

            viewEmptyState.setPrimaryCTAText(getString(R.string.digital_checkout_empty_state_btn))
            viewEmptyState.visibility = View.VISIBLE
        }
    }

    private fun showPromoTicker() {
        renderDefaultEmptyPromoView()

        checkoutBottomViewWidget.setDigitalPromoButtonListener { onClickUsePromo() }

        checkoutBottomViewWidget.setButtonChevronIconListener {
            if (checkoutBottomViewWidget.promoButtonDescription.isNotEmpty()) {
                checkoutBottomViewWidget.promoButtonState = ButtonPromoCheckoutView.State.LOADING
                onResetPromoDiscount()
            } else {
                onClickUsePromo()
            }
        }
        checkoutBottomViewWidget.promoButtonVisibility = View.VISIBLE
    }

    private fun onFailedCancelVoucher(throwable: Throwable) {
        checkoutBottomViewWidget.promoButtonState = ButtonPromoCheckoutView.State.ACTIVE
        showToastMessage(ErrorHandler.getErrorMessage(activity, throwable))
    }

    private fun showToastMessage(message: String) {
        build(requireView(),
            message,
            Snackbar.LENGTH_LONG,
            TYPE_ERROR,
            getString(com.tokopedia.abstraction.R.string.close),
            View.OnClickListener { /** do nothing **/ }).show()
    }

    private fun closeViewWithMessageAlert(error: Throwable) {
        loaderCheckout.visibility = View.GONE

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
                        getDigitalIdentifierParam(), it,
                        getString(R.string.digital_cart_login_message),
                        cartPassData?.isSpecialProduct ?: false
                    )
                }
            } else activity?.finish()

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
                    getCartAfterCheckout()
                }
                PaymentConstant.PAYMENT_CANCELLED -> {
                    showToastMessage(getString(R.string.digital_cart_alert_payment_canceled))
                    getCartAfterCheckout()
                }
                else -> getCartAfterCheckout()
            }
        }
    }

    private fun renderPostPaidPopup(postPaidPopupAttribute: AttributesDigitalData.PostPaidPopupAttribute) {
        if (postPaidPopupAttribute.title.isNotEmpty() || postPaidPopupAttribute.content.isNotEmpty()) {
            val dialog =
                DialogUnify(
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

        rvMyBills.layoutManager = LinearLayoutManager(context)
        rvMyBills.isNestedScrollingEnabled = false
        rvMyBills.adapter = myBillsAdapter

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
        viewModel.onSubscriptionChecked(isChecked)
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
        renderSubscriptionMoreInfoBottomSheet()
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
        if (isChecked) digitalAnalytics.eventTebusMurahChecked(
            fintechProduct,
            getCategoryName(),
            position,
            userSession.userId
        )
        else digitalAnalytics.eventTebusMurahUnchecked(
            fintechProduct,
            getCategoryName(),
            userSession.userId
        )
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
        if (fintechProductInfo.urlLink.isNotEmpty()) RouteManager.route(
            context,
            fintechProductInfo.urlLink
        )
        else if (fintechProductInfo.tooltipText.isNotEmpty()) {
            val moreInfoView = View.inflate(
                context,
                R.layout.layout_digital_fintech_product_info_bottom_sheet,
                null
            )
            val moreInfoText: Typography = moreInfoView.findViewById(R.id.egold_tooltip)
            moreInfoText.setPadding(
                0, 0, 0,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
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
            fragmentManager?.run { moreInfoBottomSheet.show(this, "E-gold more info bottom sheet") }
        }
    }

    private fun renderInputPriceView(
        total: Long,
        userInputPriceDigital: AttributesDigitalData.UserInputPriceDigital?
    ) {
        userInputPriceDigital?.let {
            inputPriceContainer.visibility = View.VISIBLE

            if (!userInputPriceDigital.minPayment.isNullOrEmpty())
                inputPriceHolderView.actionListener = this

            inputPriceHolderView.setMinMaxPayment(
                userInputPriceDigital.minPaymentPlain.toLong(),
                userInputPriceDigital.maxPaymentPlain.toLong(),
                userInputPriceDigital.minPayment ?: "",
                userInputPriceDigital.maxPayment ?: ""
            )
            inputPriceHolderView.setPriceInput(total)
        }
    }

    override fun onInputPriceByUserFilled(paymentAmount: Long?) {
        viewModel.setTotalPriceBasedOnUserInput(paymentAmount?.toDouble() ?: 0.0)
        viewModel.setSubtotalPaymentSummaryOnUserInput(paymentAmount?.toDouble() ?: 0.0)
    }

    override fun enableCheckoutButton() {
        checkoutBottomViewWidget.isCheckoutButtonEnabled = true
    }

    override fun disableCheckoutButton() {
        checkoutBottomViewWidget.isCheckoutButtonEnabled = false
    }

    private fun interruptRequestTokenVerification(phoneNumber: String?) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.COTP)
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
        digitalAnalytics.eventProceedToPayment(
            getCartDigitalInfoData(), getPromoData().promoCode, userSession.userId,
            cartPassData?.categoryId ?: ""
        )
        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
    }

    private fun sendGetCartAndCheckoutAnalytics() {
        digitalAnalytics.sendCartScreen()
        rechargeAnalytics.trackAddToCartRechargePushEventRecommendation(
            cartPassData?.categoryId?.toIntOrNull()
                ?: 0
        )
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
        val couponActive = viewModel.cartDigitalInfoData.value?.attributes?.isCouponActive == 1
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
        intent.putExtra(EXTRA_COUPON_ACTIVE, couponActive)
        intent.putExtra(EXTRA_PROMO_DIGITAL_MODEL, getPromoDigitalModel())
        startActivityForResult(intent, REQUEST_CODE_PROMO_LIST)
    }

    private fun hideContent() {
        contentCheckout.hide()
        checkoutBottomViewWidget.hide()
    }

    private fun renderSubscriptionMoreInfoBottomSheet() {
        context?.let {
            val linearLayout = LinearLayout(it)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                0,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_24),
            )

            val descriptionArray =
                resources.getStringArray(com.tokopedia.digital_checkout.R.array.subscription_more_info_bottomsheet_description)
            descriptionArray.forEachIndexed { index, text ->
                val simpleWidget = DigitalCheckoutSimpleWidget(it)
                simpleWidget.setContent((index + 1).toString(), text)

                linearLayout.addView(simpleWidget)
            }


            val bottomSheetUnify = BottomSheetUnify()
            bottomSheetUnify.setTitle(getString(com.tokopedia.digital_checkout.R.string.subscription_more_info_bottomsheet_title))
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

    private fun getPromoDigitalModel(): PromoDigitalModel =
        viewModel.getPromoDigitalModel(cartPassData, getPriceInput())

    private fun getCartDigitalInfoData(): CartDigitalInfoData = viewModel.cartDigitalInfoData.value
        ?: CartDigitalInfoData()

    private fun getCategoryName(): String = getCartDigitalInfoData().attributes.categoryName
    private fun getOperatorName(): String = getCartDigitalInfoData().attributes.operatorName
    private fun getPromoData(): PromoData = viewModel.promoData.value ?: PromoData()
    private fun getPriceInput(): Double? {
        return if (inputPriceHolderView.getPriceInput() == null) return null
        else inputPriceHolderView.getPriceInput()?.toDouble()
    }

    companion object {
        const val ARG_PASS_DATA = "ARG_PASS_DATA"
        const val ARG_SUBSCRIPTION_PARAMS = "ARG_SUBSCRIPTION_PARAMS"

        private const val EXTRA_STATE_PROMO_DATA = "EXTRA_STATE_PROMO_DATA"
        private const val EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER =
            "EXTRA_STATE_CHECKOUT_DATA_PARAMETER_BUILDER"

        private const val REQUEST_CODE_OTP = 1001
        const val OTP_TYPE_CHECKOUT_DIGITAL = 16

        private const val DEFAULT_ANDROID_DEVICE_ID = 5

        private const val SUBSCRIPTION_BOTTOM_SHEET_TAG = "SUBSCRIPTION_BOTTOM_SHEET_TAG"
        private const val LEADING_MARGIN_SPAN = 16

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