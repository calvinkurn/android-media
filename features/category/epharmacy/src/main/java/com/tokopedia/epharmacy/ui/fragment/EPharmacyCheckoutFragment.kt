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
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.databinding.EpharmacyCheckoutChatDokterFragmentBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.params.EPharmacyCheckoutParams
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse
import com.tokopedia.epharmacy.network.response.EPharmacyCartGeneralCheckoutResponse
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.EPHARMACY_CHECKOUT_PAGE
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.viewmodel.EPharmacyCheckoutViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showToast
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.CartGroup.Cart as EPCart
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.ShoppingSummary as EPCheckoutSummary

class EPharmacyCheckoutFragment : BaseDaggerFragment() {

    private var ePharmacyLoader: ConstraintLayout? = null
    private var ePharmacyData: Group? = null
    private var ePharmacyGlobalError: GlobalError? = null

    private var tConsultationId = String.EMPTY
    private var enablerId = String.EMPTY
    private var groupId = String.EMPTY

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
        enablerId = arguments?.getString(EPHARMACY_ENABLER_ID, String.EMPTY).orEmpty()
        tConsultationId = arguments?.getString(EPHARMACY_TOKO_CONSULTATION_ID, String.EMPTY).orEmpty()
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
                    onSuccessCartCheckout(it)
                }
                is Fail -> {
                    onFailCartCheckout()
                }
            }
        }
    }

    private fun showToasterError(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_internet_error).orEmpty())
            else -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail).orEmpty())
        }
    }

    private fun onSuccessData(data: Success<EPharmacyAtcInstantResponse>) {
        removeShimmer()
        updateUi(data.data)
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
            setSummaryInfo(info.shoppingSummary)
            setTotalInfo(info.shoppingSummary)
        }
    }

    private fun setTitle(title: String?) {
        if (!title.isNullOrBlank()) {
            binding?.titleChatDokter?.text = title
        }
    }

    private fun setCartInfo(cart: EPCart?) {
        binding?.epharmacyCheckoutDetailView?.apply {
            serviceTypeValue.text = cart?.customResponse?.serviceType
            serviceProviderValue.text = cart?.customResponse?.enablerName
            durationValue.text = cart?.customResponse?.durationMinutes
            feeValue.text = cart?.priceFmt
        }
    }

    private fun setSummaryInfo(summary: EPCheckoutSummary?) {
        binding?.apply {
            subtotal.text = summary?.product?.title
            subtotalValue.text = summary?.totalBillFmt
        }
    }

    private fun setTotalInfo(shoppingSummary: EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.ShoppingSummary?) {
        binding?.qcTotalAmount?.apply {
            setLabelTitle("Total Tagihan")
            setAmount(shoppingSummary?.product?.totalPriceFmt.orEmpty())
            setCtaText("Pilih Pembayaran")
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
        ePharmacyCheckoutViewModel?.getEPharmacyCheckoutData(EPharmacyUtils.createCheckoutGeneralParams(ePharmacyCheckoutParams))
    }

    private fun onSuccessCartCheckout(result: Success<EPharmacyCartGeneralCheckoutResponse>) {
        when (result.data.checkout?.checkoutData?.success) {
            0 -> showToast(TYPE_ERROR, result.data.checkout?.checkoutData?.message.orEmpty())
            1 -> successCheckout(result.data.checkout?.checkoutData?.cartGeneralResponse?.redirectUrl)
        }
    }

    private fun successCheckout(redirectUrl: String?) {
        RouteManager.route(context, redirectUrl)
    }

    private fun onFailCartCheckout() {
        showToast(TYPE_ERROR, getString(com.tokopedia.epharmacy.R.string.epharmacy_internet_error))
    }

    override fun getScreenName() = EPHARMACY_CHECKOUT_PAGE

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyCheckoutFragment {
            val fragment = EPharmacyCheckoutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
