package com.tokopedia.digital_checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.presentation.adapter.DigitalCartDetailInfoAdapter
import com.tokopedia.digital_checkout.presentation.viewmodel.DigitalCartViewModel
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.digital_checkout.utils.PromoDataUtil.mapToStatePromoCheckout
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_DETAIL
import com.tokopedia.promocheckout.common.data.REQUEST_CODE_PROMO_LIST
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView.ActionListener
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.build
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_checkout_page.*
import javax.inject.Inject

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(DigitalCartViewModel::class.java) }

    private var cartPassData: DigitalCheckoutPassData? = null
    private var digitalSubscriptionParams: DigitalSubscriptionParams = DigitalSubscriptionParams()

    lateinit var cartDetailInfoAdapter: DigitalCartDetailInfoAdapter

    private var isCouponActive = false
    private var promoData = PromoData()

    override fun getScreenName(): String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartPassData = arguments?.getParcelable(ARG_PASS_DATA)
        val subParams: DigitalSubscriptionParams? = arguments?.getParcelable(ARG_SUBSCRIPTION_PARAMS)
        if (subParams != null) {
            digitalSubscriptionParams = subParams
        }
    }

    override fun initInjector() {
        getComponent(DigitalCheckoutComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_checkout_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        initViews()
    }

    private fun loadData() {
        cartPassData?.let {
            if (it.needGetCart) {
                viewModel.getCart(it, getString(R.string.digital_cart_login_message))
            } else {
                viewModel.addToCart(it, getDigitalIdentifierParam(), digitalSubscriptionParams,
                        getString(R.string.digital_cart_login_message))
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.cartDigitalInfoData.observe(viewLifecycleOwner, Observer {
            renderCartDigitalInfoData(it)
        })

        viewModel.cartAdditionalInfoList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                tvSeeDetailToggle.visibility = View.GONE
            } else {
                tvSeeDetailToggle.visibility = View.VISIBLE
                cartDetailInfoAdapter.setAdditionalInfoItems(it)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            closeViewWithMessageAlert(it)
        })

        viewModel.isSuccessCancelVoucherCart.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    resetPromoTickerView()
                }
                is Fail -> {
                    onFailedCancelVoucher(it.throwable)
                }
            }
        })

        viewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            tvTotalPayment.text = it
        })
    }

    private fun renderCartDigitalInfoData(cartInfo: CartDigitalInfoData) {
        productTitle.text = cartInfo.attributes?.categoryName
        cartDetailInfoAdapter.setInfoItems(cartInfo.mainInfo ?: listOf())

        if (cartInfo.attributes?.isEnableVoucher == true) {
            digitalPromoTickerView.visibility = View.VISIBLE
        } else digitalPromoTickerView.visibility = View.GONE

        cartInfo.attributes?.postPaidPopupAttribute?.let { postPaidPopupAttribute ->
            if (digitalSubscriptionParams.isSubscribed) renderPostPaidPopup(postPaidPopupAttribute)
        }
    }

    private fun getDigitalIdentifierParam(): RequestBodyIdentifier = DeviceUtil.getDigitalIdentifierParam(requireActivity())

    private fun initViews() {
        cartDetailInfoAdapter = DigitalCartDetailInfoAdapter(object : DigitalCartDetailInfoAdapter.ActionListener {
            override fun expandAdditionalList() {
                tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_close_label)
            }

            override fun collapseAdditionalList() {
                tvSeeDetailToggle.text = getString(R.string.digital_cart_detail_see_detail_label)
            }
        })

        rvDetails.layoutManager = LinearLayoutManager(context)
        rvDetails.isNestedScrollingEnabled = false
        rvDetails.adapter = cartDetailInfoAdapter

        tvSeeDetailToggle.setOnClickListener {
            cartDetailInfoAdapter.toggleIsExpanded()
        }

        showPromoTicker()
    }

    private fun showError(message: String) {
        if (viewGlobalError != null) {
            viewGlobalError.setActionClickListener {
                viewGlobalError.visibility = View.GONE
                loadData()
            }
            var errorType = SERVER_ERROR
            if (message == ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL) {
                errorType = NO_CONNECTION
            }
            viewGlobalError.setType(errorType)
            viewGlobalError.visibility = View.VISIBLE
        }
    }

    private fun showPromoTicker() {
        digitalPromoTickerView.state = TickerPromoStackingCheckoutView.State.EMPTY
        digitalPromoTickerView.title = ""
        digitalPromoTickerView.desc = ""
        digitalPromoTickerView.actionListener = object : ActionListener {
            override fun onClickUsePromo() {
//                digitalAnalytics.eventclickUseVoucher(getCategoryName())
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
                intent.putExtra("EXTRA_COUPON_ACTIVE", isCouponActive)
                intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel())
                startActivityForResult(intent, REQUEST_CODE_PROMO_LIST)
            }

            override fun onResetPromoDiscount() {
//                digitalAnalytics.eventclickCancelApplyCoupon(getCategoryName(), promoData.promoCode)
                viewModel.cancelVoucherCart()
            }

            override fun onClickDetailPromo() {
                val intent: Intent
                val promoCode: String = promoData.promoCode
                if (!promoCode.isEmpty()) {
                    val requestCode: Int
                    if (promoData.typePromo == PromoData.TYPE_VOUCHER) {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_DIGITAL)
                        intent.putExtra("EXTRA_PROMO_CODE", promoCode)
                        intent.putExtra("EXTRA_COUPON_ACTIVE", isCouponActive)
                        requestCode = REQUEST_CODE_PROMO_LIST
                    } else {
                        intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_DIGITAL)
                        intent.putExtra("EXTRA_IS_USE", true)
                        intent.putExtra("EXTRA_KUPON_CODE", promoCode)
                        requestCode = REQUEST_CODE_PROMO_DETAIL
                    }
                    intent.putExtra("EXTRA_PROMO_DIGITAL_MODEL", getPromoDigitalModel())
                    startActivityForResult(intent, requestCode)
                } else {
                    showToastMessage(getString(com.tokopedia.promocheckout.common.R.string.promo_none_applied))
                }
            }

            override fun onDisablePromoDiscount() {
//                digitalAnalytics.eventclickCancelApplyCoupon(getCategoryName(), promoData.promoCode)
                viewModel.cancelVoucherCart()
            }
        }
        digitalPromoTickerView.visibility = View.VISIBLE
    }

    private fun getPromoDigitalModel(): PromoDigitalModel? {
        var price: Long = getCartDigitalInfoData().attributes?.pricePlain ?: 0
//        if (inputPriceContainer.getVisibility() == View.VISIBLE) {
//            price = inputPriceHolderView.getPriceInput()
//        }
        return PromoDigitalModel(cartPassData?.categoryId?.toIntOrNull() ?: 0,
                getCategoryName(),
                getOperatorName(),
                getProductId(),
                cartPassData?.clientNumber ?: "",
                price
        )
    }

    private fun onFailedCancelVoucher(throwable: Throwable) {
        var message: String = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
        if (!throwable.message.isNullOrEmpty()) {
            message = ErrorHandler.getErrorMessage(activity, throwable)
        }
        showToastMessage(message)
    }

    fun showToastMessage(message: String) {
        build(requireView(), message, Snackbar.LENGTH_LONG, TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.close), View.OnClickListener { v: View? -> }).show()
    }

    private fun closeViewWithMessageAlert(message: String?) {
        if (cartPassData?.isFromPDP == true) {
            val intent = Intent()
            intent.putExtra(DigitalExtraParam.EXTRA_MESSAGE, message)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            showError(message ?: "")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == REQUEST_CODE_PROMO_LIST || requestCode == REQUEST_CODE_PROMO_DETAIL) && resultCode == Activity.RESULT_OK) {
            data?.let { data ->
                if (data.hasExtra(EXTRA_PROMO_DATA)) {
                    promoData = data.getParcelableExtra(EXTRA_PROMO_DATA)
                    viewModel.resetVoucherCart()
                    when (promoData.state) {
                        TickerCheckoutView.State.FAILED -> {
                            promoData.promoCode = ""
                            renderPromoTickerView()
                            cartDetailInfoAdapter.isExpanded = true
                        }
                        TickerCheckoutView.State.ACTIVE -> {
                            renderPromoTickerView()
                            viewModel.onReceivedPromoCode(promoData)
                            cartDetailInfoAdapter.isExpanded = true
                        }
                        TickerCheckoutView.State.EMPTY -> {
                            promoData.promoCode = ""
                            renderPromoTickerView()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
        //need to check others behaviour
    }

    private fun renderPromoTickerView() {
        digitalPromoTickerView.desc = promoData.description
        digitalPromoTickerView.title = promoData.title
        digitalPromoTickerView.state = promoData.state.mapToStatePromoCheckout()
    }

    private fun resetPromoTickerView() {
        promoData = PromoData()
        renderPromoTickerView()
    }

    private fun renderPostPaidPopup(postPaidPopupAttribute: AttributesDigitalData.PostPaidPopupAttribute) {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.setTitle(postPaidPopupAttribute.title)
        dialog.setDescription(postPaidPopupAttribute.content ?: "")
        dialog.setPrimaryCTAText(postPaidPopupAttribute.confirmButtonTitle ?: "")
        dialog.setImageUrl(postPaidPopupAttribute.imageUrl ?: "")
        dialog.show()
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
    }

    private fun getCartDigitalInfoData(): CartDigitalInfoData = viewModel.cartDigitalInfoData.value
            ?: CartDigitalInfoData()

    private fun getCategoryName(): String = getCartDigitalInfoData().attributes?.categoryName ?: ""
    private fun getOperatorName(): String = getCartDigitalInfoData().attributes?.operatorName ?: ""
    private fun getProductId(): Int = cartPassData?.productId?.toIntOrNull() ?: 0

    companion object {
        private const val ARG_PASS_DATA = "ARG_PASS_DATA"
        private const val ARG_SUBSCRIPTION_PARAMS = "ARG_SUBSCRIPTION_PARAMS"

        fun newInstance(passData: DigitalCheckoutPassData?,
                        subParams: DigitalSubscriptionParams?): DigitalCartFragment {
            val fragment = DigitalCartFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_PASS_DATA, passData)
            bundle.putParcelable(ARG_SUBSCRIPTION_PARAMS, subParams)
            fragment.arguments = bundle
            return fragment
        }
    }
}