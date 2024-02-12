package com.tokopedia.epharmacy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyAdapter
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactoryImpl
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAttachmentDetailDiffUtil
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.databinding.EpharmacyOrderDetailFragmentBinding
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.epharmacy.network.response.OrderButtonData
import com.tokopedia.epharmacy.network.response.OrderTrackingData
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.EPHARMACY_ORDER_DETAIL_PAGE
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_VERTICAL_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_WAITING_INVOICE
import com.tokopedia.epharmacy.utils.EPharmacyAttachmentUiUpdater
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.epharmacy.utils.TrackerId.Companion.CLICK_LIHAT_INVOICE
import com.tokopedia.epharmacy.utils.TrackerId.Companion.CLICK_PUSAT_BANTUAN
import com.tokopedia.epharmacy.utils.TrackerId.Companion.ORDER_DETAIL_MAIN_CTA
import com.tokopedia.epharmacy.utils.TrackerId.Companion.VIEW_CHAT_DOKTER_ORDER_DETAIL_PAGE
import com.tokopedia.epharmacy.utils.TrackerId.Companion.VIEW_PRESC_IMAGE_WEBVIEW
import com.tokopedia.epharmacy.viewmodel.EPharmacyOrderDetailViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.track.builder.Tracker
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class EPharmacyOrderDetailFragment : BaseDaggerFragment(), EPharmacyListener {

    private var ePharmacyRecyclerView: RecyclerView? = null
    private var ePharmacyActionButton: ConstraintLayout? = null
    private var ePharmacyPrimaryButton: UnifyButton? = null
    private var ePharmacySecondaryButton: CardUnify? = null
    private var ePharmacyGlobalError: GlobalError? = null

    private var tConsultationId = 0L
    private var waitingInvoice = false
    private var verticalId = String.EMPTY

    private var orderTrackingData: OrderTrackingData? = null

    private var binding by autoClearedNullable<EpharmacyOrderDetailFragmentBinding>()

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    private val ePharmacyOrderDetailViewModel by lazy {
        viewModelFactory?.let {
            ViewModelProvider(this, it)[EPharmacyOrderDetailViewModel::class.java]
        }
    }

    private var ePharmacyAttachmentUiUpdater: EPharmacyAttachmentUiUpdater = EPharmacyAttachmentUiUpdater(
        linkedMapOf()
    )

    private val ePharmacyAdapterFactory by lazy(LazyThreadSafetyMode.NONE) { EPharmacyAdapterFactoryImpl(this) }

    private val ePharmacyAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val asyncDifferConfig: AsyncDifferConfig<BaseEPharmacyDataModel> = AsyncDifferConfig.Builder(
            EPharmacyAttachmentDetailDiffUtil()
        )
            .build()
        EPharmacyAdapter(asyncDifferConfig, ePharmacyAdapterFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpharmacyOrderDetailFragmentBinding.inflate(
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
        tConsultationId = arguments?.getLong(EPHARMACY_TOKO_CONSULTATION_ID).orZero()
        waitingInvoice = arguments?.getBoolean(EPHARMACY_WAITING_INVOICE).orFalse()
        verticalId = arguments?.getString(EPHARMACY_VERTICAL_ID, String.EMPTY).orEmpty()
    }

    private fun setUpObservers() {
        observeOrderDetail()
        observeButtonData()
    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            ePharmacyPrimaryButton = findViewById(R.id.primaryButton)
            ePharmacyActionButton = findViewById(R.id.action_buttons)
            ePharmacySecondaryButton = findViewById(R.id.secondaryButton)
            ePharmacyGlobalError = findViewById(R.id.epharmacy_global_error)
        }
        setupRecyclerView()
    }

    private fun getData() {
        addShimmer()
        ePharmacyOrderDetailViewModel?.getEPharmacyOrderDetail(
            tConsultationId,
            verticalId,
            waitingInvoice
        )
    }

    private fun addShimmer() {
        ePharmacyRecyclerView?.show()
        ePharmacyActionButton?.hide()
        ePharmacyAttachmentUiUpdater.addShimmer()
        updateUi()
    }

    private fun removeShimmer() {
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        ePharmacyRecyclerView?.hide()
        updateUi()
    }

    private fun setupRecyclerView() {
        ePharmacyRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ePharmacyAdapter
        }
    }

    private fun observeOrderDetail() {
        ePharmacyOrderDetailViewModel?.ePharmacyOrderDetailData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessOrderData(it)
                }
                is Fail -> {
                    onFailOrderData(it)
                }
            }
        }
    }

    private fun observeButtonData() {
        ePharmacyOrderDetailViewModel?.ePharmacyButtonData?.observe(viewLifecycleOwner) {
            renderButtons(it)
            orderTrackingData = it.orderTrackingData
            sendViewChatDokterOrderDetailPageEvent(orderTrackingData.toString())
        }
    }

    private fun renderButtons(buttonData: OrderButtonData?) {
        buttonData?.cta?.firstOrNull()?.let { primaryButtonData ->
            ePharmacyActionButton?.show()
            ePharmacyPrimaryButton?.apply {
                text = primaryButtonData.label
                buttonVariant = OrderButtonData.mapButtonVariant(primaryButtonData.variantColor)
                buttonType = OrderButtonData.mapButtonType(primaryButtonData.type)
                setOnClickListener {
                    onPrimaryButtonClick(primaryButtonData.appUrl, primaryButtonData.label)
                }
                show()
            }
        } ?: ePharmacyPrimaryButton?.hide()

        buttonData?.triDots?.let { secondaryButtonData ->
            if (secondaryButtonData.isEmpty()) {
                ePharmacySecondaryButton?.hide()
            } else {
                ePharmacySecondaryButton?.show()
                ePharmacySecondaryButton?.setOnClickListener {
                    onSecondaryButtonClick(secondaryButtonData)
                }
            }
        } ?: ePharmacySecondaryButton?.hide()
    }

    private fun onPrimaryButtonClick(appUrl: String?, label: String?) {
        redirectionAppLink(appUrl)
        sendClickMainCtaEvent("$label - $orderTrackingData")
    }

    private fun onSecondaryButtonClick(secondaryButtonData: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?>) {
        showSecondaryActionButtonBottomSheet(
            secondaryButtonData,
            object : EPharmacySecondaryActionButtonBottomSheet.ActionButtonClickListener {
                override fun onActionButtonClicked(isFromPrimaryButton: Boolean, button: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?) {
                    redirectionAppLink(button?.appUrl)
                    if (!isFromPrimaryButton) {
                        sendClickSecondaryCtaEvent(orderTrackingData.toString(), "click ${button?.label} on lainnya", "")
                    }
                }
            }
        )
    }

    private fun redirectionAppLink(appLink: String?) {
        RouteManager.route(context, appLink)
    }

    private fun showSecondaryActionButtonBottomSheet(secondaryActionButtons: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?>, actionButtonClickListener: EPharmacySecondaryActionButtonBottomSheet.ActionButtonClickListener?) {
        context?.let {
            val bottomSheet = EPharmacySecondaryActionButtonBottomSheet(it, actionButtonClickListener)
            bottomSheet.setSecondaryActionButtons(secondaryActionButtons)
            bottomSheet.show(childFragmentManager)
        }
    }

    private fun updateUi() {
        val updatedComponents = ePharmacyAttachmentUiUpdater.mapOfData.values.toList()
        ePharmacyAdapter.submitList(updatedComponents)
    }

    private fun onSuccessOrderData(it: Success<EPharmacyDataModel>) {
        ePharmacyAttachmentUiUpdater.mapOfData.clear()
        it.data.listOfComponents.forEach { component ->
            ePharmacyAttachmentUiUpdater.updateModel(component)
        }
        updateUi()
    }

    private fun onFailOrderData(it: Fail) {
        ePharmacyActionButton?.hide()
        removeShimmer()
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

    override fun onHelpButtonClicked(appUrl: String?) {
        super.onHelpButtonClicked(appUrl)
        RouteManager.route(context, appUrl)
        sendClickPusatBantuanEvent(orderTrackingData.toString())
    }

    override fun onLihatInvoiceClicked(appUrl: String?) {
        super.onLihatInvoiceClicked(appUrl)
        RouteManager.route(context, appUrl)
        sendClickLihatInvoiceEvent(orderTrackingData.toString())
    }

    override fun getScreenName() = EPHARMACY_ORDER_DETAIL_PAGE

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): EPharmacyOrderDetailFragment {
            val fragment = EPharmacyOrderDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun sendClickMainCtaEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_GROCERIES)
            .setEventAction("click main CTA")
            .setEventCategory(EPHARMACY_ORDER_DETAIL_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, ORDER_DETAIL_MAIN_CTA)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendViewPrescriptionImageWebviewEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view prescription image webview")
            .setEventCategory("epharmacy prescription image webview")
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, VIEW_PRESC_IMAGE_WEBVIEW)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendViewChatDokterOrderDetailPageEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_GROCERIES_IRIS)
            .setEventAction("view chat dokter order detail page")
            .setEventCategory(EPHARMACY_ORDER_DETAIL_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, VIEW_CHAT_DOKTER_ORDER_DETAIL_PAGE)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendClickLihatInvoiceEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_GROCERIES)
            .setEventAction("click lihat invoice")
            .setEventCategory(EPHARMACY_ORDER_DETAIL_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, CLICK_LIHAT_INVOICE)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendClickPusatBantuanEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_GROCERIES)
            .setEventAction("click pusat bantuan")
            .setEventCategory(EPHARMACY_ORDER_DETAIL_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, CLICK_PUSAT_BANTUAN)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    private fun sendClickSecondaryCtaEvent(eventLabel: String, eventAction: String, tracker: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_GROCERIES)
            .setEventAction(eventAction)
            .setEventCategory(EPHARMACY_ORDER_DETAIL_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, tracker)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
