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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.presentation.adapter.TravelCrossSellAdapter
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationListActivity
import com.tokopedia.flight.cancellationV2.presentation.activity.FlightCancellationPassengerActivity
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.bottomsheet.FlightOrderDetailPaymentDetailBottomSheet
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailButtonsView
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailHeaderStatusView
import com.tokopedia.flight.orderdetail.presentation.customview.FlightOrderDetailJourneyView
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailButtonModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailViewModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import com.tokopedia.flight.resend_email.presentation.bottomsheet.FlightOrderResendEmailBottomSheet
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_order_detail.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailFragment : BaseDaggerFragment(),
        FlightOrderDetailJourneyView.Listener,
        FlightOrderDetailHeaderStatusView.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightOrderDetailViewModel: FlightOrderDetailViewModel
    private lateinit var remoteConfig: RemoteConfig

    private var isCancellation: Boolean = false
    private var isRequestCancel: Boolean = false

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

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        flightOrderDetailViewModel = viewModelProvider.get(FlightOrderDetailViewModel::class.java)
        flightOrderDetailViewModel.orderId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
        flightOrderDetailViewModel.fetchOrderDetailData()
        if (remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_TRAVEL_ENABLE_CROSS_SELL))
            flightOrderDetailViewModel.fetchCrossSellData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_order_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()

        flightOrderDetailViewModel.orderDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderView(it.data)
                }
                is Fail -> {
                }
            }
        })

        flightOrderDetailViewModel.crossSell.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderCrossSell(it.data)
                }
                is Fail -> {
                }
            }
        })

        flightOrderDetailViewModel.eticketData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {

                }
                is Fail -> {

                }
            }
        })

        flightOrderDetailViewModel.cancellationData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                navigateToCancellationPage(it)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_SEND_E_TICKET -> {
                if (resultCode == Activity.RESULT_OK)
                    showSnackbarSuccess(getString(com.tokopedia.flight.orderlist.R.string.resend_eticket_success))
            }
            REQUEST_CODE_CANCELLATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    activity?.let {
                        val intent = Intent()
                        intent.putExtra(EXTRA_IS_AFTER_CANCELLATION, true)
                        it.setResult(Activity.RESULT_OK, intent)
                        it.finish()
                    }
                }
            }
        }
    }

    override fun onPnrCopyClicked(pnr: String, isReturn: Boolean) {
        copyToClipboard(if (isReturn) CLIP_LABEL_RETURN_BOOKING_CODE else CLIP_LABEL_DEPARTURE_BOOKING_CODE, pnr)
    }

    override fun onSendETicketClicked() {
        val bottomSheet = FlightOrderResendEmailBottomSheet.getInstance(
                flightOrderDetailViewModel.getUserEmail(),
                flightOrderDetailViewModel.orderId
        )
        bottomSheet.setTargetFragment(this, REQUEST_CODE_SEND_E_TICKET)
        bottomSheet.setShowListener { bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
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

    private fun showLoading() {
        containerContentOrderDetail.visibility = View.GONE
        containerLoaderOrderDetail.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        containerContentOrderDetail.visibility = View.VISIBLE
        containerLoaderOrderDetail.visibility = View.GONE
    }

    private fun renderView(data: FlightOrderDetailDataModel) {
        /* Render Order Status */
        flightOrderDetailHeaderStatus.listener = this
        flightOrderDetailHeaderStatus.setData(
                data.status,
                data.statusString,
                flightOrderDetailViewModel.orderId,
                data.createTime,
                data.payment.gatewayName,
                data.payment.totalAmountStr
        )
        flightOrderDetailHeaderStatus.buildView()

        /* Render Journey Ticket View */
        flightOrderDetailJourney.listener = this
        flightOrderDetailJourney.setData(
                data.hasETicket,
                data.journeys
        )
        flightOrderDetailJourney.buildView()

        /* Render Passenger View */
        flightOrderDetailPassenger.setData(data.passengers)
        flightOrderDetailPassenger.buildView()

        /* Setup Cancellation Detail View */
        if (data.cancellations.isNotEmpty()) {
            containerFlightOrderDetailCancellationDetail.visibility = View.VISIBLE
            containerFlightOrderDetailCancellationDetail.setOnClickListener {
                navigateToCancellationDetailPage()
            }
        } else {
            containerFlightOrderDetailCancellationDetail.visibility = View.GONE
        }

        /* Render Insurance View */
        flightOrderDetailInsurance.listener = object : FlightOrderDetailButtonsView.Listener {
            override fun onTopButtonClicked() {}

            override fun onBottomButtonClicked() {}

        }
        flightOrderDetailInsurance.setData(
                getString(R.string.flight_order_detail_insurance_title_label),
                FlightOrderDetailButtonModel(
                        MethodChecker.getDrawable(requireContext(), R.drawable.ic_flight_order_detail_insurance),
                        getString(R.string.flight_order_detail_insurance_trip_label),
                        getString(R.string.flight_order_detail_insurance_trip_description),
                        true,
                        false
                ),
                FlightOrderDetailButtonModel(
                        MethodChecker.getDrawable(requireContext(), R.drawable.ic_flight_order_detail_insurance),
                        getString(R.string.flight_order_detail_insurance_cancel_label),
                        getString(R.string.flight_order_detail_insurance_trip_description),
                        true,
                        false
                )
        )
        flightOrderDetailInsurance.buildView()


        /* Render Web Check In View */
        val today = FlightDateUtil.removeTime(FlightDateUtil.getCurrentDate())
        val isCancellationButtonVisible: Boolean =
                when {
                    data.journeys.size > 1 -> {
                        val returnDate: Date = FlightDateUtil.removeTime(FlightDateUtil.stringToDate(
                                FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, data.journeys[1].departureTime))
                        !today.after(returnDate)
                    }
                    data.journeys.isNotEmpty() -> {
                        val departureDate = FlightDateUtil.removeTime(FlightDateUtil.stringToDate(
                                FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, data.journeys[0].departureTime))
                        !today.after(departureDate)
                    }
                    else -> {
                        false
                    }
                }
        flightOrderDetailCheckIn.listener = object : FlightOrderDetailButtonsView.Listener {
            override fun onTopButtonClicked() {
//                TODO("Not yet implemented")
            }

            override fun onBottomButtonClicked() {
                flightOrderDetailViewModel.onNavigateToCancellationClicked(data.journeys)
            }

        }
        flightOrderDetailCheckIn.setData(
                getString(R.string.flight_order_detail_check_in_title_label),
                FlightOrderDetailButtonModel(
                        MethodChecker.getDrawable(requireContext(), R.drawable.ic_flight_order_detail_web_check_in),
                        getString(R.string.flight_order_detail_check_in_label),
                        getString(R.string.flight_order_detail_check_in_description),
                        true,
                        true
                ),
                FlightOrderDetailButtonModel(
                        MethodChecker.getDrawable(requireContext(), R.drawable.ic_flight_order_detail_cancellation),
                        getString(R.string.flight_label_cancel_ticket),
                        getString(R.string.flight_order_detail_cancel_description),
                        isCancellationButtonVisible,
                        true
                )
        )
        flightOrderDetailCheckIn.buildView()

        /* Render Contact Us */
        tgFlightOrderContactUs.text = MethodChecker.fromHtml(getString(R.string.flight_order_detail_contact_us))
        tgFlightOrderContactUs.setOnClickListener {
            RouteManager.route(requireContext(), data.contactUsURL)
        }

        hideLoading()
    }

    private fun renderCrossSell(crossSellData: TravelCrossSelling) {
        if (crossSellData.items.isNotEmpty()) {
            flightOrderDetailCrossSell.visibility = View.VISIBLE
            flightOrderDetailCrossSell.buildView(crossSellData)
            flightOrderDetailCrossSell.setListener(object : TravelCrossSellAdapter.OnItemClickListener {
                override fun onItemClickListener(item: TravelCrossSelling.Item, position: Int) {
                    RouteManager.route(context, item.uri)
                }

            })
        } else {
            flightOrderDetailCrossSell.visibility = View.GONE
        }
    }

    private fun copyToClipboard(label: String, textToCopy: String) {
        context?.let {
            val clipboardManager: ClipboardManager = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(label, textToCopy)
            clipboardManager.setPrimaryClip(clipData)
        }
    }

    private fun openDetailPaymentBottomSheet() {
        val bottomSheet = FlightOrderDetailPaymentDetailBottomSheet.getInstance(
                flightOrderDetailViewModel.buildPaymentDetailData(),
                flightOrderDetailViewModel.buildAmenitiesPaymentDetailData(),
                flightOrderDetailViewModel.getTotalAmount()
        )
        bottomSheet.setShowListener { bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        bottomSheet.show(requireFragmentManager(), FlightOrderDetailPaymentDetailBottomSheet.TAG)
    }

    private fun showSnackbarSuccess(message: String) {
        Toaster.make(requireView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
    }

    private fun navigateToCancellationDetailPage() {
        startActivity(FlightCancellationListActivity.createIntent(context, flightOrderDetailViewModel.orderId))
    }

    private fun navigateToCancellationPage(cancellationItems: List<FlightCancellationJourney>) {
        val intent = FlightCancellationPassengerActivity.createIntent(
                requireContext(),
                flightOrderDetailViewModel.orderId,
                cancellationItems
        )
        startActivityForResult(intent, REQUEST_CODE_CANCELLATION)
    }

    companion object {
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_IS_CANCELLATION = "EXTRA_IS_CANCELLATION"
        private const val EXTRA_REQUEST_CANCEL = "EXTRA_REQUEST_CANCEL"
        private const val EXTRA_IS_AFTER_CANCELLATION = "EXTRA_IS_AFTER_CANCELLATION"

        private const val CLIP_LABEL_INVOICE_ID = "Flight Invoice Id"
        private const val CLIP_LABEL_DEPARTURE_BOOKING_CODE = "Flight Departure Booking Code"
        private const val CLIP_LABEL_RETURN_BOOKING_CODE = "Flight Return Booking Code"

        private const val REQUEST_CODE_SEND_E_TICKET = 1111
        private const val REQUEST_CODE_CANCELLATION = 1112

        fun createInstance(invoiceId: String,
                           isCancellation: Boolean,
                           isRequestCancellation: Boolean): FlightOrderDetailFragment =
                FlightOrderDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                        putBoolean(EXTRA_IS_CANCELLATION, isCancellation)
                        putBoolean(EXTRA_REQUEST_CANCEL, isRequestCancellation)
                    }
                }
    }
}