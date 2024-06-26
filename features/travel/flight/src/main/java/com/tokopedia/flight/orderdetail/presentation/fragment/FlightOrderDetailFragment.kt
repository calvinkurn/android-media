package com.tokopedia.flight.orderdetail.presentation.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.presentation.adapter.TravelCrossSellAdapter
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.data.FlightCancellationResponseEntity
import com.tokopedia.flight.cancellation.presentation.activity.FlightCancellationPassengerActivity
import com.tokopedia.flight.cancellation.presentation.fragment.FlightCancellationPassengerFragment
import com.tokopedia.flight.cancellation_navigation.presentation.FlightCancellationActivity
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationListActivity
import com.tokopedia.flight.databinding.FragmentFlightOrderDetailBinding
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.activity.FlightOrderDetailBrowserActivity
import com.tokopedia.flight.orderdetail.presentation.activity.FlightOrderDetailWebCheckInActivity
import com.tokopedia.flight.orderdetail.presentation.bottomsheet.FlightOrderDetailPaymentDetailBottomSheet
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailButtonsView
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailHeaderStatusView
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailJourneyView
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailButtonModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailErrorModel
import com.tokopedia.flight.orderdetail.presentation.model.mapper.FlightOrderDetailStatusMapper
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailViewModel
import com.tokopedia.flight.resend_eticket.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailFragment :
    BaseDaggerFragment(),
    FlightOrderDetailJourneyView.Listener,
    FlightOrderDetailHeaderStatusView.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightOrderDetailViewModel: FlightOrderDetailViewModel
    private lateinit var remoteConfig: RemoteConfig

    private var isCancellation: Boolean = false
    private var isRequestCancel: Boolean = false
    private var isOpenTrackSent: Boolean = false
    private var isOpenInvoice: Boolean = false

    private var isTravelInsurance: Boolean = false
    private var isZeroCancellation: Boolean = false

    private var binding by autoClearedNullable<FragmentFlightOrderDetailBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            remoteConfig = FirebaseRemoteConfigImpl(it)
        }

        isCancellation = arguments?.getBoolean(EXTRA_IS_CANCELLATION) ?: false
        isRequestCancel = arguments?.getBoolean(EXTRA_REQUEST_CANCEL) ?: false
        isOpenInvoice = arguments?.getBoolean(EXTRA_IS_OPEN_INVOICE) ?: false

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        flightOrderDetailViewModel = viewModelProvider.get(FlightOrderDetailViewModel::class.java)
        flightOrderDetailViewModel.orderId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
        flightOrderDetailViewModel.fetchOrderDetailData()
        flightOrderDetailViewModel.fetchCrossSellData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlightOrderDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()

        flightOrderDetailViewModel.orderDetailData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (!isOpenTrackSent) {
                            flightOrderDetailViewModel.trackOpenOrderDetail(it.data.statusString)
                            isOpenTrackSent = true
                        }
                        renderView(it.data)
                        checkIfShouldGoToCancellation(it.data)
                        checkIfShouldGoToInvoice()
                    }
                    is Fail -> {
                        var title = ""
                        var message = ""
                        try {
                            val gson = Gson()
                            val itemType =
                                object : TypeToken<List<FlightOrderDetailErrorModel>>() {}.type
                            val errorData = gson.fromJson<List<FlightOrderDetailErrorModel>>(
                                it.throwable.message,
                                itemType
                            )
                            title = errorData[0].title
                            message = errorData[0].message
                        } catch (error: Throwable) {
                            message = ErrorHandler.getErrorMessage(requireContext(), error)
                        }
                        renderErrorView(title, message)
                    }
                }
            }
        )

        flightOrderDetailViewModel.crossSell.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        renderCrossSell(it.data)
                    }
                    is Fail -> {
                    }
                }
            }
        )

        flightOrderDetailViewModel.eticketData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        navigateToWebview(E_TICKET_TITLE, it.data)
                    }
                    is Fail -> {
                        showSnackbarError(
                            String.format(
                                getString(R.string.flight_order_detail_failed_to_fetch_data),
                                E_TICKET_TITLE.toLowerCase()
                            )
                        )
                    }
                }
            }
        )

        flightOrderDetailViewModel.invoiceData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        navigateToWebview(INVOICE_TITLE, it.data)
                    }
                    is Fail -> {
                        showSnackbarError(
                            String.format(
                                getString(R.string.flight_order_detail_failed_to_fetch_data),
                                INVOICE_TITLE.toLowerCase()
                            )
                        )
                    }
                }
            }
        )

        flightOrderDetailViewModel.cancellationData.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNotEmpty()) {
                    navigateToCancellationPage(it)
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_SEND_E_TICKET -> {
                if (resultCode == Activity.RESULT_OK) {
                    showSnackbar(getString(R.string.flight_resend_eticket_success))
                }
            }
            REQUEST_CODE_CANCELLATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    activity?.let {
                        val intent = Intent()
                        intent.putExtra(EXTRA_IS_AFTER_CANCELLATION, true)
                        it.setResult(Activity.RESULT_OK, intent)
                        it.finish()
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    data?.let {
                        if (it.hasExtra(FlightCancellationPassengerFragment.EXTRA_IS_CANCEL_ERROR) &&
                            it.getBooleanExtra(
                                    FlightCancellationPassengerFragment.EXTRA_IS_CANCEL_ERROR,
                                    false
                                )
                        ) {
                            showSnackbarError(
                                String.format(
                                    getString(R.string.flight_order_detail_failed_to_fetch_data),
                                    CANCELLATION_TITLE.toLowerCase()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onPnrCopyClicked(pnr: String, isReturn: Boolean) {
        copyToClipboard(
            if (isReturn) CLIP_LABEL_RETURN_BOOKING_CODE else CLIP_LABEL_DEPARTURE_BOOKING_CODE,
            pnr
        )
    }

    override fun onSendETicketClicked() {
        flightOrderDetailViewModel.trackSendETicketClicked()
        val bottomSheet = FlightOrderResendEmailBottomSheet.getInstance(
            flightOrderDetailViewModel.getUserEmail(),
            flightOrderDetailViewModel.orderId
        )
        bottomSheet.setTargetFragment(this, REQUEST_CODE_SEND_E_TICKET)
        bottomSheet.setShowListener {
            bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheet.show(requireFragmentManager(), FlightOrderResendEmailBottomSheet.TAG)
    }

    override fun onViewETicketClicked() {
        flightOrderDetailViewModel.fetchETicketData()
    }

    override fun onCopyInvoiceIdClicked(invoiceId: String) {
        copyToClipboard(CLIP_LABEL_INVOICE_ID, invoiceId)
    }

    override fun onDetailPaymentClicked() {
        openDetailPaymentBottomSheet()
    }

    override fun onInvoiceIdClicked() {
        flightOrderDetailViewModel.fetchInvoiceData()
    }

    private fun showLoading() {
        binding?.containerContentOrderDetail?.visibility = View.GONE
        binding?.containerLoaderOrderDetail?.visibility = View.VISIBLE
        binding?.containerFlightOrderError?.visibility = View.GONE
    }

    private fun hideLoading() {
        binding?.containerContentOrderDetail?.visibility = View.VISIBLE
        binding?.containerLoaderOrderDetail?.visibility = View.GONE
        binding?.containerFlightOrderError?.visibility = View.GONE
    }

    private fun showError() {
        binding?.containerContentOrderDetail?.visibility = View.GONE
        binding?.containerLoaderOrderDetail?.visibility = View.GONE
        binding?.containerFlightOrderError?.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding?.containerContentOrderDetail?.visibility = View.GONE
        binding?.containerLoaderOrderDetail?.visibility = View.VISIBLE
        binding?.containerFlightOrderError?.visibility = View.GONE
    }

    private fun renderView(data: FlightOrderDetailDataModel) {
        /* Render Order Status */
        binding?.flightOrderDetailHeaderStatus?.listener = this
        binding?.flightOrderDetailHeaderStatus?.setData(
            data.status,
            data.statusString,
            flightOrderDetailViewModel.orderId,
            data.createTime,
            data.payment.gatewayName,
            data.payment.totalAmountStr,
            data.payment.additionalInfo
        )
        binding?.flightOrderDetailHeaderStatus?.buildView()

        /* Render Journey Ticket View */
        binding?.flightOrderDetailJourney?.listener = this
        binding?.flightOrderDetailJourney?.setData(
            data.hasETicket,
            data.journeys
        )
        binding?.flightOrderDetailJourney?.buildView()

        /* Render Passenger View */
        binding?.flightOrderDetailPassenger?.setData(data.passengers)
        binding?.flightOrderDetailPassenger?.buildView()

        /* Setup Cancellation Detail View */
        if (data.cancellations.isNotEmpty()) {
            binding?.containerFlightOrderDetailCancellationDetail?.visibility = View.VISIBLE
            binding?.containerFlightOrderDetailCancellationDetail?.setOnClickListener {
                navigateToCancellationDetailPage()
            }
        } else {
            binding?.containerFlightOrderDetailCancellationDetail?.visibility = View.GONE
        }

        /* Render Insurance View */
        if (data.insurances.isNotEmpty()) {
            binding?.flightOrderDetailInsurance?.visibility = View.VISIBLE
            binding?.flightOrderDetailInsurance?.listener =
                object : FlightOrderDetailButtonsView.Listener {
                    override fun onTopButtonClicked() {}

                    override fun onBottomButtonClicked() {}
                }

            data.insurances.forEach {
                when (it.id) {
                    CODE_TRAVEL_INSURANCE -> isTravelInsurance = true
                    CODE_ZERO_CANCELLATION_INSURANCE -> isZeroCancellation = true
                }
            }

            binding?.flightOrderDetailInsurance?.setData(
                getString(R.string.flight_order_detail_insurance_title_label),
                FlightOrderDetailButtonModel(
                    MethodChecker.getDrawable(
                        requireContext(),
                        R.drawable.ic_flight_order_detail_insurance
                    ),
                    getString(R.string.flight_order_detail_insurance_trip_label),
                    getString(R.string.flight_order_detail_insurance_trip_description),
                    isTravelInsurance,
                    false
                ),
                FlightOrderDetailButtonModel(
                    MethodChecker.getDrawable(
                        requireContext(),
                        R.drawable.ic_flight_order_detail_insurance
                    ),
                    getString(R.string.flight_order_detail_insurance_cancel_label),
                    getString(R.string.flight_order_detail_insurance_trip_description),
                    isZeroCancellation,
                    false
                )
            )
            binding?.flightOrderDetailInsurance?.buildView()
        } else {
            binding?.flightOrderDetailInsurance?.visibility = View.GONE
        }

        /* Render Web Check In View */
        val isWebCheckInButtonVisible: Pair<Boolean, String> =
            flightOrderDetailViewModel.isWebCheckInAvailable(data)
        binding?.flightOrderDetailCheckIn?.listener =
            object : FlightOrderDetailButtonsView.Listener {
                override fun onTopButtonClicked() {
                    flightOrderDetailViewModel.trackClickWebCheckIn()
                    context?.let {
                        startActivity(
                            FlightOrderDetailWebCheckInActivity
                                .getIntent(it, flightOrderDetailViewModel.orderId)
                        )
                    }
                }

                override fun onBottomButtonClicked() {
                    flightOrderDetailViewModel.trackClickCancel()
                    flightOrderDetailViewModel.onNavigateToCancellationClicked(data.journeys)
                }
            }
        binding?.flightOrderDetailCheckIn?.setData(
            getString(R.string.flight_order_detail_check_in_title_label),
            FlightOrderDetailButtonModel(
                MethodChecker.getDrawable(
                    requireContext(),
                    R.drawable.ic_flight_order_detail_web_check_in
                ),
                getString(R.string.flight_order_detail_check_in_label),
                isWebCheckInButtonVisible.second,
                true,
                isWebCheckInButtonVisible.first,
                isWebCheckInButtonVisible.first
            ),
            FlightOrderDetailButtonModel(
                MethodChecker.getDrawable(
                    requireContext(),
                    R.drawable.ic_flight_order_detail_cancellation
                ),
                getString(R.string.flight_label_cancel_ticket),
                getString(R.string.flight_order_detail_cancel_description),
                FlightOrderDetailStatusMapper.getStatusOrder(data.status) == FlightOrderDetailStatusMapper.SUCCESS,
                true
            )
        )
        binding?.flightOrderDetailCheckIn?.buildView()

        /* Render Contact Us */
        binding?.tgFlightOrderContactUs?.text =
            MethodChecker.fromHtml(getString(R.string.flight_order_detail_contact_us))
        binding?.tgFlightOrderContactUs?.setOnClickListener {
            RouteManager.route(requireContext(), data.contactUsURL)
        }

        hideLoading()
    }

    private fun renderCrossSell(crossSellData: TravelCrossSelling) {
        if (crossSellData.items.isNotEmpty()) {
            binding?.flightOrderDetailCrossSell?.visibility = View.VISIBLE
            binding?.flightOrderDetailCrossSell?.buildView(crossSellData)
            binding?.flightOrderDetailCrossSell?.setListener(object :
                    TravelCrossSellAdapter.OnItemClickListener {
                    override fun onItemClickListener(item: TravelCrossSelling.Item, position: Int) {
                        RouteManager.route(context, item.uri)
                    }
                })
        } else {
            binding?.flightOrderDetailCrossSell?.visibility = View.GONE
        }
    }

    private fun copyToClipboard(label: String, textToCopy: String) {
        context?.let {
            val clipboardManager: ClipboardManager =
                it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(label, textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            showSnackbar(getString(R.string.flight_order_detail_success_copy_message, label))
        }
    }

    private fun openDetailPaymentBottomSheet() {
        val bottomSheet = FlightOrderDetailPaymentDetailBottomSheet.getInstance(
            flightOrderDetailViewModel.buildPaymentDetailData(),
            flightOrderDetailViewModel.buildAmenitiesPaymentDetailData(),
            flightOrderDetailViewModel.buildInsurancePaymentDetailData(),
            flightOrderDetailViewModel.getTotalAmount()
        )
        bottomSheet.setShowListener {
            bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheet.show(requireFragmentManager(), FlightOrderDetailPaymentDetailBottomSheet.TAG)
    }

    private fun showSnackbar(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
    }

    private fun showSnackbarError(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun navigateToCancellationDetailPage() {
        context?.let {
            startActivity(
                FlightOrderCancellationListActivity.createIntent(
                    it,
                    flightOrderDetailViewModel.orderId
                )
            )
        }
    }

    private fun navigateToCancellationPage(cancellationItems: List<FlightCancellationResponseEntity>) {
        val intent: Intent = if (remoteConfig.getBoolean(
                RemoteConfigKey.ANDROID_CUSTOMER_FLIGHT_CANCELLATION_NAVIGATION,
                true
            )
        ) {
            FlightCancellationActivity.createIntent(
                requireContext(),
                flightOrderDetailViewModel.orderId,
                cancellationItems
            )
        } else {
            FlightCancellationPassengerActivity.createIntent(
                requireContext(),
                flightOrderDetailViewModel.orderId,
                cancellationItems
            )
        }
        startActivityForResult(intent, REQUEST_CODE_CANCELLATION)
    }

    private fun checkIfShouldGoToCancellation(data: FlightOrderDetailDataModel) {
        if (isCancellation) {
            navigateToCancellationDetailPage()
        } else if (isRequestCancel) {
            flightOrderDetailViewModel.onNavigateToCancellationClicked(data.journeys)
        }
    }

    private fun checkIfShouldGoToInvoice() {
        if (isOpenInvoice) {
            onInvoiceIdClicked()
        }
    }

    private fun navigateToWebview(title: String, htmlContent: String) {
        context?.let {
            startActivity(
                FlightOrderDetailBrowserActivity.getIntent(
                    it,
                    title,
                    flightOrderDetailViewModel.orderId,
                    htmlContent,
                    flightOrderDetailViewModel.getOrderDetailStatus()
                )
            )
        }
    }

    private fun renderErrorView(title: String, message: String) {
        if (title.isNotEmpty()) {
            binding?.tgFlightOrderErrorTitle?.visibility = View.VISIBLE
            binding?.tgFlightOrderErrorTitle?.text = title
        } else {
            binding?.tgFlightOrderErrorTitle?.visibility = View.GONE
        }
        if (message.isNotEmpty()) {
            binding?.tgFlightOrderErrorMessage?.visibility = View.VISIBLE
            binding?.tgFlightOrderErrorMessage?.text = message
        } else {
            binding?.tgFlightOrderErrorMessage?.visibility = View.GONE
        }
        binding?.btnFlightOrderError?.setOnClickListener {
            hideError()
            flightOrderDetailViewModel.fetchOrderDetailData()
        }
        showError()
    }

    companion object {
        private const val E_TICKET_TITLE = "E-Ticket"
        private const val INVOICE_TITLE = "Invoice"
        private const val CANCELLATION_TITLE = "Pembatalan"

        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_IS_CANCELLATION = "EXTRA_IS_CANCELLATION"
        private const val EXTRA_REQUEST_CANCEL = "EXTRA_REQUEST_CANCEL"
        private const val EXTRA_IS_AFTER_CANCELLATION = "EXTRA_IS_AFTER_CANCELLATION"
        private const val EXTRA_IS_OPEN_INVOICE = "EXTRA_IS_OPEN_INVOICE"

        private const val CLIP_LABEL_INVOICE_ID = "Invoice id"
        private const val CLIP_LABEL_DEPARTURE_BOOKING_CODE = "Kode booking keberangkatan"
        private const val CLIP_LABEL_RETURN_BOOKING_CODE = "Kode booking kepulangan"

        private const val REQUEST_CODE_SEND_E_TICKET = 1111
        private const val REQUEST_CODE_CANCELLATION = 1112

        private const val CODE_TRAVEL_INSURANCE = "1"
        private const val CODE_ZERO_CANCELLATION_INSURANCE = "2"

        fun createInstance(
            invoiceId: String,
            isCancellation: Boolean,
            isRequestCancellation: Boolean,
            isOpenInvoice: Boolean
        ): FlightOrderDetailFragment =
            FlightOrderDetailFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_INVOICE_ID, invoiceId)
                    putBoolean(EXTRA_IS_CANCELLATION, isCancellation)
                    putBoolean(EXTRA_REQUEST_CANCEL, isRequestCancellation)
                    putBoolean(EXTRA_IS_OPEN_INVOICE, isOpenInvoice)
                }
            }
    }
}
