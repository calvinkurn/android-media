package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.databinding.EpharmacyReminderScreenBottomSheetBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.response.EPharmacyVerifyConsultationResponse
import com.tokopedia.epharmacy.utils.CategoryKeys
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.EPHARMACY_ERROR_PAGE
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.epharmacy.utils.IS_SINGLE_CONSUL_FLOW
import com.tokopedia.epharmacy.utils.TrackerId
import com.tokopedia.epharmacy.utils.WEB_LINK_PREFIX
import com.tokopedia.epharmacy.viewmodel.EPharmacyChatLoadingViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.track.builder.Tracker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacyChatLoadingFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyGlobalError: GlobalError? = null
    private var illustrationImage: DeferredImageView? = null
    private var tConsultationId = 0L
    private var singleConsulFlow = false

    private val mainHandler = Handler(Looper.getMainLooper())
    private var verifyRunnable: Runnable? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: UserSessionInterface

    private val ePharmacyChatLoadingViewModel: EPharmacyChatLoadingViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider[EPharmacyChatLoadingViewModel::class.java]
    }

    private var binding by autoClearedNullable<EpharmacyReminderScreenBottomSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyReminderScreenBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        setUpObservers()
        initViews(view)
        getData()
    }

    private fun extractArguments() {
        tConsultationId = arguments?.getLong(EPHARMACY_TOKO_CONSULTATION_ID).orZero()
        singleConsulFlow = arguments?.getBoolean(IS_SINGLE_CONSUL_FLOW).orFalse()
    }

    private fun setUpObservers() {
        observerVerifyConsultationOrder()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
            illustrationImage = findViewById(R.id.ep_illustration_image)
        }
        verifyRunnable = Runnable {
            ePharmacyChatLoadingViewModel.getVerifyConsultationOrder(tConsultationId, singleConsulFlow)
        }
    }

    private fun getData() {
        setDataLoading(
            getString(epharmacyR.string.epharmacy_chat_loading_title),
            getString(epharmacyR.string.epharmacy_chat_loading_description)
        )
        if (verifyRunnable != null) { mainHandler.postDelayed(verifyRunnable!!, DELAY_MILLIS_VERIFY) }
    }

    private fun observerVerifyConsultationOrder() {
        ePharmacyChatLoadingViewModel.ePharmacyVerifyConsultationData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessData(it.data)
                }
                is Fail -> {
                    onFailData(it)
                }
            }
        }
    }

    private fun onSuccessData(response: EPharmacyVerifyConsultationResponse) {
        val orderData = response.verifyConsultationOrder?.verifyConsultationOrderData

        if (orderData?.isOrderCreated == true) {
            orderData.pwaLink?.takeIf { it.isNotBlank() }?.let { pwaLink ->
                activity?.finish()
                routeAction(pwaLink)
            } ?: handleFail()
        } else {
            handleFail()
        }
    }

    private fun handleFail() {
        sendViewErrorPageFailedOrderCreationEvent(tConsultationId.toString())
        setErrorData(
            getString(epharmacyR.string.epharmacy_chat_loading_error_title),
            getString(epharmacyR.string.epharmacy_chat_loading_error_description),
            getString(epharmacyR.string.epharmacy_chat_loading_error_cta_text)
        )
    }

    private fun setErrorData(title: String, description: String, ctaText: String) {
        ePharmacyGlobalError?.setType(GlobalError.SERVER_ERROR)
        ePharmacyGlobalError?.errorAction?.show()
        ePharmacyGlobalError?.errorIllustration?.show()
        illustrationImage?.hide()
        ePharmacyGlobalError?.errorTitle?.text = title
        ePharmacyGlobalError?.errorAction?.text = ctaText
        ePharmacyGlobalError?.errorDescription?.text = description
        ePharmacyGlobalError?.setActionClickListener {
            getData()
        }
    }

    private fun routeAction(appLink: String) {
        RouteManager.route(context, "$WEB_LINK_PREFIX$appLink")
    }

    private fun onFailData(it: Fail) {
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setDataLoading(title: String, description: String) {
        ePharmacyGlobalError?.show()
        illustrationImage?.show()
        ePharmacyGlobalError?.errorIllustration?.hide()
        ePharmacyGlobalError?.errorDescription?.text = description
        ePharmacyGlobalError?.errorTitle?.text = title
        ePharmacyGlobalError?.errorAction?.hide()
    }

    private fun setGlobalErrors(errorType: Int) {
        illustrationImage?.hide()
        ePharmacyGlobalError?.errorIllustration?.show()
        ePharmacyGlobalError?.setType(errorType)
        ePharmacyGlobalError?.errorAction?.show()
        ePharmacyGlobalError?.setActionClickListener {
            getData()
        }
    }

    override fun getScreenName(): String = CategoryKeys.EPHARMACY_LOADING_PAGE

    override fun initInjector() {
        getComponent(EPharmacyComponent::class.java).inject(this)
    }

    companion object {
        const val DELAY_MILLIS_VERIFY = 3000L

        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyChatLoadingFragment {
            val fragment = EPharmacyChatLoadingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onPause() {
        super.onPause()
        if (verifyRunnable != null) { mainHandler.removeCallbacks(verifyRunnable!!) }
    }

    private fun sendViewErrorPageFailedOrderCreationEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view error page - failed order creation")
            .setEventCategory(EPHARMACY_ERROR_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.FAILED_ORDER_CREATION)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
