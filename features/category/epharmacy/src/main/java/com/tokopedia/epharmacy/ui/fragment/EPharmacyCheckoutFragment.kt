package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.databinding.EpharmacyCheckoutChatDokterFragmentBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.params.EPharmacyCheckoutParams
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse
import com.tokopedia.epharmacy.network.response.EPharmacyCartGeneralCheckoutResponse
import com.tokopedia.epharmacy.utils.CategoryKeys
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.EPHARMACY_CHECKOUT_PAGE
import com.tokopedia.epharmacy.utils.DEFAULT_ZERO_VALUE
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.epharmacy.utils.TrackerId.Companion.CHECKOUT_PAGE_EVENT
import com.tokopedia.epharmacy.utils.TrackerId.Companion.CLICK_PILIH_PEMBAYARAN_EVENT
import com.tokopedia.epharmacy.viewmodel.EPharmacyCheckoutViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.track.builder.Tracker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.epharmacy.R as epharmacyR
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData as EPATCData
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.ShoppingSummary as EPCheckoutSummary

class EPharmacyCheckoutFragment : BaseDaggerFragment() {

    private var ePharmacyLoader: ConstraintLayout? = null
    private var ePharmacyData: Group? = null
    private var ePharmacyGlobalError: GlobalError? = null

    private var tConsultationId = DEFAULT_ZERO_VALUE
    private var enablerId = DEFAULT_ZERO_VALUE
    private var groupId = String.EMPTY
    private var enablerName = String.EMPTY

    private var ePharmacyCheckoutParams = EPharmacyCheckoutParams(ePharmacyCheckoutCartGroup = null)

    private var binding by autoClearedNullable<EpharmacyCheckoutChatDokterFragmentBinding>()

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val ePharmacyCheckoutViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it)[EPharmacyCheckoutViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyCheckoutChatDokterFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        setUpObservers()
        initViews(view)
        getData()
    }

    private fun initArguments() {
        groupId = arguments?.getString(EPHARMACY_GROUP_ID, String.EMPTY).orEmpty()
        enablerId = arguments?.getLong(EPHARMACY_ENABLER_ID).orZero()
        tConsultationId = arguments?.getLong(EPHARMACY_TOKO_CONSULTATION_ID).orZero()
    }

    private fun setUpObservers() {
        observerAtcDetail()
        observerEPharmacyCheckoutGeneral()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyLoader = findViewById(R.id.epharmacy_loader)
            ePharmacyData = findViewById(R.id.epharmacy_data)
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
        }
    }

    private fun getData() {
        addShimmer()
        ePharmacyCheckoutViewModel?.getEPharmacyAtcData(EPharmacyUtils.createAtcParams(groupId, enablerId, tConsultationId, ePharmacyCheckoutParams))
    }

    private fun observerAtcDetail() {
        ePharmacyCheckoutViewModel?.ePharmacyAtcData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessData(it)
                }
                is Fail -> {
                    onFailData(it)
                }
            }
        }
    }

    private fun observerEPharmacyCheckoutGeneral() {
        ePharmacyCheckoutViewModel?.ePharmacyCheckoutData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessCartCheckout(it.data)
                }
                is Fail -> {
                    onFailCartCheckout()
                }
            }
        }
    }

    private fun onSuccessData(data: Success<EPharmacyAtcInstantResponse>) {
        removeShimmer()
        updateUi(data.data)
        enablerName = data.data.cartGeneralAddToCartInstant?.cartGeneralAddToCartInstantData?.businessDataList?.businessData?.firstOrNull()?.cartGroups?.firstOrNull()?.carts?.firstOrNull()?.customResponse?.enablerName.orEmpty()
        sendViewCheckoutPageEvent("$enablerName - $groupId - $tConsultationId")
    }

    private fun updateUi(data: EPharmacyAtcInstantResponse) {
        ePharmacyData?.show()
        if (data.cartGeneralAddToCartInstant?.cartGeneralAddToCartInstantData?.success == 1) {
            setData(data.cartGeneralAddToCartInstant)
        } else {
            setApiError(data.cartGeneralAddToCartInstant?.cartGeneralAddToCartInstantData?.message)
        }
    }

    private fun setData(cartGeneralAddToCartInstant: EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant) {
        ePharmacyCheckoutParams.ePharmacyCheckoutCartGroup = cartGeneralAddToCartInstant.cartGeneralAddToCartInstantData?.businessDataList?.businessData?.firstOrNull()?.cartGroups?.firstOrNull()
        cartGeneralAddToCartInstant.cartGeneralAddToCartInstantData?.businessDataList?.businessData?.firstOrNull()?.let { info ->
            setTitle(info.customResponse?.title)
            setCartInfo(info.cartGroups?.firstOrNull()?.carts?.firstOrNull())
            setSummaryInfo(cartGeneralAddToCartInstant.cartGeneralAddToCartInstantData.businessDataList.shoppingSummary)
            setTotalInfo(cartGeneralAddToCartInstant.cartGeneralAddToCartInstantData.businessDataList.shoppingSummary)
        } ?: setGlobalErrors(GlobalError.SERVER_ERROR)
    }

    private fun setTitle(title: String?) {
        if (!title.isNullOrBlank()) {
            binding?.lblChatDoctor?.text = title
        }
    }

    private fun setCartInfo(cart: EPATCData.BusinessDataList.BusinessData.CartGroup.Cart?) {
        binding?.epharmacyCheckoutDetailView?.apply {
            detailProductHeader.hide()
            lblValueItemOfService.text = cart?.customResponse?.serviceType
            serviceProviderValue.text = cart?.customResponse?.enablerName
            durationValue.text = cart?.customResponse?.durationMinutes
        }
    }

    private fun setSummaryInfo(summary: EPCheckoutSummary?) {
        binding?.apply {
            lblSubtotalBill.text = summary?.businessBreakDown?.firstOrNull()?.product?.title
            lblSubtotalBillValue.text = summary?.businessBreakDown?.firstOrNull()?.totalBillFmt
            epharmacyCheckoutDetailView.feeValue.text = summary?.businessBreakDown?.firstOrNull()?.totalBillFmt
        }
    }

    private fun setTotalInfo(shoppingSummary: EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.ShoppingSummary?) {
        binding?.qcTotalAmount?.apply {
            setLabelTitle(context.getString(epharmacyR.string.epharmacy_total_tagihan))
            setAmount(shoppingSummary?.businessBreakDown?.firstOrNull()?.product?.totalPriceFmt.orEmpty())
            setCtaText(context.getString(epharmacyR.string.epharmacy_pilih_pembayaran))
            amountCtaView.setOnClickListener {
                onSelectPayment()
            }
        }
    }

    private fun setApiError(message: String?) {
        ePharmacyData?.hide()
        setGlobalErrors(GlobalError.PAGE_FULL, message)
    }

    private fun onFailData(it: Fail) {
        removeShimmer()
        ePharmacyData?.hide()
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setGlobalErrors(errorType: Int, message: String? = String.EMPTY) {
        ePharmacyGlobalError?.apply {
            show()
            setType(errorType)
            if (!message.isNullOrBlank()) {
                errorDescription.text = message
            }
            setActionClickListener {
                gone()
                getData()
            }
        }
    }

    private fun showToast(type: Int = Toaster.TYPE_NORMAL, message: String) {
        if (message.isNotBlank()) {
            view?.let { safeView ->
                Toaster.build(safeView, message, LENGTH_LONG, type).show()
            }
        }
    }

    private fun addShimmer() {
        ePharmacyData?.hide()
        ePharmacyLoader?.show()
        ePharmacyGlobalError?.hide()
    }

    private fun removeShimmer() {
        ePharmacyLoader?.hide()
    }

    private fun onSelectPayment() {
        sendClickPilihPembayaranEvent("$enablerName - $groupId - $tConsultationId")
        ePharmacyCheckoutViewModel?.getEPharmacyCheckoutData(EPharmacyUtils.createCheckoutGeneralParams(ePharmacyCheckoutParams))
    }

    private fun onSuccessCartCheckout(cartGeneralResponse: EPharmacyCartGeneralCheckoutResponse) {
        when (cartGeneralResponse.checkout?.checkoutData?.success) {
            EPharmacyCartGeneralCheckoutResponse.ERROR -> {
                showToast(TYPE_ERROR, cartGeneralResponse.checkout.checkoutData.message.orEmpty())
            }
            EPharmacyCartGeneralCheckoutResponse.SUCCESS -> successCheckout(cartGeneralResponse.checkout.checkoutData.cartGeneralResponse)
        }
    }

    private fun successCheckout(checkoutData: EPharmacyCartGeneralCheckoutResponse.CheckoutResponse.CheckoutData.CartGeneralResponse?) {
        PaymentPassData().apply {
            redirectUrl = checkoutData?.redirectUrl
            transactionId = checkoutData?.parameter?.transactionId
            paymentId = checkoutData?.parameter?.transactionId
            callbackSuccessUrl = checkoutData?.callbackUrl
            callbackFailedUrl = checkoutData?.callbackFailUrl
            queryString = checkoutData?.queryString
        }.also {
            val intent =
                RouteManager.getIntent(
                    activity,
                    ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                )
            intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, it)
            startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
        }
    }

    private fun onFailCartCheckout() {
        showToast(TYPE_ERROR, getString(epharmacyR.string.epharmacy_internet_error))
    }

    override fun getScreenName() = EPHARMACY_CHECKOUT_PAGE

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyCheckoutFragment {
            return EPharmacyCheckoutFragment().apply {
                arguments = bundle
            }
        }
    }

    private fun sendViewCheckoutPageEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view checkout page")
            .setEventCategory(CategoryKeys.EPHARMACY_CHAT_DOKTER_CHECKOUT_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, CHECKOUT_PAGE_EVENT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendClickPilihPembayaranEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_GROCERIES)
            .setEventAction("click pilih pembayaran")
            .setEventCategory(CategoryKeys.EPHARMACY_CHAT_DOKTER_CHECKOUT_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, CLICK_PILIH_PEMBAYARAN_EVENT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
