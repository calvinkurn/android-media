package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.databinding.EpharmacyCheckoutChatDokterFragmentBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.params.CartGeneralAddToCartInstantParams
import com.tokopedia.epharmacy.network.response.EPharmacyCheckoutResponse
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.EPHARMACY_CHECKOUT_PAGE
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.viewmodel.EPharmacyCheckoutViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.LENGTH_LONG
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EPharmacyCheckoutFragment : BaseDaggerFragment() {

    private var ePharmacyLoader: ConstraintLayout? = null
    private var ePharmacyData: Group? = null
    private var ePharmacyGlobalError: GlobalError? = null

    private var tokoConsultationId = ""
    private var enablerId = ""
    private var groupId = ""

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
        return inflater.inflate(R.layout.epharmacy_checkout_chat_dokter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        setUpObservers()
        initViews(view)
        getData()
    }

    private fun initArguments() {
        groupId = arguments?.getString(EPHARMACY_GROUP_ID, "") ?: ""
        enablerId = arguments?.getString(EPHARMACY_ENABLER_ID, "") ?: ""
        tokoConsultationId = arguments?.getString(EPHARMACY_TOKO_CONSULTATION_ID, "") ?: ""
    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
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
        ePharmacyCheckoutViewModel?.getEPharmacyCheckoutData(createParams())
    }

    private fun createParams(): CartGeneralAddToCartInstantParams {
        return CartGeneralAddToCartInstantParams(
            CartGeneralAddToCartInstantParams.CartGeneralAddToCartInstantRequestBusinessData(
                "8811b325f-d0cf-4ba9-8d0a-3bce7e8c96c0",
                arrayListOf(
                    CartGeneralAddToCartInstantParams.CartGeneralAddToCartInstantRequestBusinessData.CartGeneralAddToCartInstantRequestProductData(
                        "ECONSUL",
                        1,
                        CartGeneralAddToCartInstantParams.CartGeneralAddToCartInstantRequestBusinessData.CartGeneralAddToCartInstantRequestProductData.CartGeneralCustomStruct(
                            groupId,
                            enablerId,
                            tokoConsultationId
                        ),
                        "paidconsultation",
                        ""
                    )
                )
            ),
            "android"
        )
    }

    private fun observerEPharmacyDetail() {
        ePharmacyCheckoutViewModel?.ePharmacyCheckoutData?.observe(viewLifecycleOwner) {
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

    private fun showToasterError(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_internet_error) ?: "")
            else -> showToast(TYPE_ERROR, context?.resources?.getString(R.string.epharmacy_reminder_fail) ?: "")
        }
    }

    private fun onSuccessData(data: Success<EPharmacyCheckoutResponse>) {
        removeShimmer()
        updateUi(data.data)
    }

    private fun updateUi(data: EPharmacyCheckoutResponse) {
        if (data.cartGeneralAddToCartInstant?.cartGeneralAddToCartInstantData?.success == 1) {
            setData(data.cartGeneralAddToCartInstant)
        } else {
            setApiError(data.cartGeneralAddToCartInstant?.cartGeneralAddToCartInstantData?.message)
        }
    }

    private fun setData(cartGeneralAddToCartInstant: EPharmacyCheckoutResponse.CartGeneralAddToCartInstant) {
        cartGeneralAddToCartInstant.cartGeneralAddToCartInstantData?.businessDataList?.businessData?.firstOrNull()?.let { info ->
            setTitle(info.customResponse?.title)
            setCartInfo(info.cartGroups?.firstOrNull()?.carts?.firstOrNull())
            setSummaryInfo(info.shoppingSummary?.product)
        }
    }

    private fun setTitle(title: String?) {
    }

    private fun setCartInfo(firstOrNull: EPharmacyCheckoutResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.CartGroup.Cart?) {
    }

    private fun setSummaryInfo(product: EPharmacyCheckoutResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.ShoppingSummary.Product?) {
    }

    private fun setApiError(message: String?) {
        ePharmacyData?.hide()
        setGlobalErrors(GlobalError.PAGE_FULL, message)
    }

    private fun onFailData(it: Fail) {
        removeShimmer()
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setGlobalErrors(errorType: Int, message: String? = "") {
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
        ePharmacyData?.show()
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
